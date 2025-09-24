/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriTermLoanExtendedCodeUI.java
 *
 * Created on April 27, 2009, 4:16 PM
 */
package com.see.truetransact.ui.termloan.agritermloan;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;

/**
 *
 * @author  Administrator
 */
public class AgriTermLoanExtendedCodeUI implements UIMandatoryField {
    HashMap mandatoryMap;
    /** Creates a new instance of AgriTermLoanExtendedCodeUI */
    public AgriTermLoanExtendedCodeUI() {
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        //        rdoYes_Executed_DOC.setEnabled(val);
        //        rdoNo_Executed_DOC.setEnabled(val);
        //        rdoYes_Mandatory_DOC.setEnabled(val);
        //        rdoNo_Mandatory_DOC.setEnabled(val);
        mandatoryMap.put("txtAcct_Name", new Boolean(true));
        mandatoryMap.put("txtRepayScheduleMode", new Boolean(true));
        mandatoryMap.put("tdtExecuteDate_DOC", new Boolean(true));
        mandatoryMap.put("tdtExpiryDate_DOC", new Boolean(true));
        mandatoryMap.put("tdtDOB_GD", new Boolean(true));
        mandatoryMap.put("tdtDisbursement_Dt", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboIntGetFrom", new Boolean(true));
        mandatoryMap.put("txtPoANo", new Boolean(true));
        mandatoryMap.put("cboRepayFreq_Repayment", new Boolean(true));
        mandatoryMap.put("cboAddrType_PoA", new Boolean(true));
        mandatoryMap.put("txtCustID", new Boolean(true));
        mandatoryMap.put("cboAccStatus", new Boolean(true));
        mandatoryMap.put("cboConstitution", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("txtReferences", new Boolean(true));
        mandatoryMap.put("txtCompanyRegisNo", new Boolean(true));
        mandatoryMap.put("tdtDateEstablishment", new Boolean(true));
        mandatoryMap.put("tdtDealingWithBankSince", new Boolean(true));
        mandatoryMap.put("txtRiskRating", new Boolean(true));
        mandatoryMap.put("cboNatureBusiness", new Boolean(true));
        mandatoryMap.put("txtRemarks__CompDetail", new Boolean(true));
        mandatoryMap.put("txtNetWorth", new Boolean(true));
        mandatoryMap.put("tdtAsOn", new Boolean(true));
        mandatoryMap.put("tdtCreditFacilityAvailSince", new Boolean(true));
        mandatoryMap.put("txtChiefExecutiveName", new Boolean(true));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("txtStreet_CompDetail", new Boolean(true));
        mandatoryMap.put("txtArea_CompDetail", new Boolean(true));
        mandatoryMap.put("cboCity_CompDetail", new Boolean(true));
        mandatoryMap.put("cboState_CompDetail", new Boolean(true));
        mandatoryMap.put("cboCountry_CompDetail", new Boolean(true));
        mandatoryMap.put("txtPin_CompDetail", new Boolean(true));
        mandatoryMap.put("txtPhone_CompDetail", new Boolean(true));
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("txtLimits", new Boolean(true));
        mandatoryMap.put("txtSanctionNo", new Boolean(true));
        mandatoryMap.put("txtSanctionSlNo", new Boolean(true));
        mandatoryMap.put("tdtSanctionDate", new Boolean(true));
        mandatoryMap.put("cboSanctioningAuthority", new Boolean(true));
        mandatoryMap.put("txtSanctionRemarks", new Boolean(true));
        mandatoryMap.put("cboModeSanction", new Boolean(true));
        mandatoryMap.put("txtNoInstallments", new Boolean(true));
        mandatoryMap.put("cboRepayFreq", new Boolean(true));
        mandatoryMap.put("cboTypeOfFacility", new Boolean(true));
        mandatoryMap.put("txtLimit_SD", new Boolean(true));
        mandatoryMap.put("tdtFDate", new Boolean(true));
        mandatoryMap.put("tdtTDate", new Boolean(true));
        mandatoryMap.put("cboSanctionSlNo", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("rdoSecurityDetails_Unsec", new Boolean(true));
        mandatoryMap.put("chkStockInspect", new Boolean(true));
        mandatoryMap.put("chkInsurance", new Boolean(true));
        mandatoryMap.put("chkGurantor", new Boolean(true));
        mandatoryMap.put("rdoAccType_New", new Boolean(true));
        mandatoryMap.put("rdoAccLimit_Main", new Boolean(true));
        mandatoryMap.put("rdoNatureInterest_PLR", new Boolean(true));
        mandatoryMap.put("cboInterestType", new Boolean(true));
        mandatoryMap.put("rdoRiskWeight_Yes", new Boolean(true));
        mandatoryMap.put("tdtDemandPromNoteDate", new Boolean(true));
        mandatoryMap.put("tdtDemandPromNoteExpDate", new Boolean(true));
        mandatoryMap.put("tdtAODDate", new Boolean(true));
        mandatoryMap.put("rdoMultiDisburseAllow_Yes", new Boolean(true));
        mandatoryMap.put("rdoSubsidy_Yes", new Boolean(true));
        mandatoryMap.put("txtPurposeDesc", new Boolean(true));
        mandatoryMap.put("txtGroupDesc", new Boolean(true));
        mandatoryMap.put("rdoInterest_Simple", new Boolean(true));
        mandatoryMap.put("rdoRecarable_Yes", new Boolean(true));
        mandatoryMap.put("txtContactPerson", new Boolean(true));
        mandatoryMap.put("txtContactPhone", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtSecurityNo", new Boolean(true));
        mandatoryMap.put("txtCustID_Security", new Boolean(true));
        mandatoryMap.put("rdoSecurityType_Primary", new Boolean(true));
        mandatoryMap.put("txtSecurityValue", new Boolean(true));
        mandatoryMap.put("tdtAson", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("cboNatureCharge", new Boolean(true));
        mandatoryMap.put("tdtDateCharge", new Boolean(true));
        mandatoryMap.put("chkSelCommodityItem", new Boolean(true));
        mandatoryMap.put("cboForMillIndus", new Boolean(true));
        mandatoryMap.put("tdtDateInspection", new Boolean(true));
        mandatoryMap.put("cboStockStateFreq", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("txtMargin", new Boolean(true));
        mandatoryMap.put("txtScheduleNo", new Boolean(true));
        mandatoryMap.put("txtLaonAmt", new Boolean(true));
        mandatoryMap.put("cboRepayFreq_Repayment", new Boolean(true));
        mandatoryMap.put("cboRepayType", new Boolean(true));
        mandatoryMap.put("chkMoraGiven", new Boolean(true));
        mandatoryMap.put("chkMoratorium_Given", new Boolean(true));
        mandatoryMap.put("txtNoMonthsMora", new Boolean(true));
        mandatoryMap.put("txtFacility_Moratorium_Period", new Boolean(true));
        mandatoryMap.put("tdtFacility_Repay_Date", new Boolean(true));
        mandatoryMap.put("tdtFirstInstall", new Boolean(true));
        mandatoryMap.put("tdtLastInstall", new Boolean(true));
        mandatoryMap.put("txtTotalBaseAmt", new Boolean(true));
        mandatoryMap.put("txtAmtPenulInstall", new Boolean(true));
        mandatoryMap.put("txtAmtLastInstall", new Boolean(true));
        mandatoryMap.put("txtTotalInstallAmt", new Boolean(true));
        mandatoryMap.put("rdoDoAddSIs_Yes", new Boolean(true));
        mandatoryMap.put("rdoPostDatedCheque_Yes", new Boolean(true));
        mandatoryMap.put("rdoStatus_Repayment", new Boolean(true));
        mandatoryMap.put("txtNoInstall", new Boolean(true));
        mandatoryMap.put("txtCustomerID_GD", new Boolean(true));
        mandatoryMap.put("txtGuaranAccNo", new Boolean(true));
        mandatoryMap.put("txtGuaranName", new Boolean(true));
        mandatoryMap.put("txtAge", new Boolean(true));
        mandatoryMap.put("txtStreet_GD", new Boolean(true));
        mandatoryMap.put("txtArea_GD", new Boolean(true));
        mandatoryMap.put("cboCity_GD", new Boolean(true));
        mandatoryMap.put("txtPin_GD", new Boolean(true));
        mandatoryMap.put("cboState_GD", new Boolean(true));
        mandatoryMap.put("cboCountry_GD", new Boolean(true));
        mandatoryMap.put("txtPhone_GD", new Boolean(true));
        mandatoryMap.put("cboConstitution_GD", new Boolean(true));
        mandatoryMap.put("txtGuarantorNetWorth", new Boolean(true));
        mandatoryMap.put("tdtAsOn_GD", new Boolean(true));
        mandatoryMap.put("tdtAccountOpenDate",new Boolean(true));
        mandatoryMap.put("txtInsureCompany", new Boolean(true));
        mandatoryMap.put("txtPolicyNumber", new Boolean(true));
        mandatoryMap.put("txtPolicyAmt", new Boolean(true));
        mandatoryMap.put("tdtPolicyDate", new Boolean(true));
        mandatoryMap.put("txtPremiumAmt", new Boolean(true));
        mandatoryMap.put("tdtExpityDate", new Boolean(true));
        mandatoryMap.put("cboNatureRisk", new Boolean(true));
        mandatoryMap.put("tdtFrom", new Boolean(true));
        mandatoryMap.put("tdtTo", new Boolean(true));
        mandatoryMap.put("txtFromAmt", new Boolean(true));
        mandatoryMap.put("txtToAmt", new Boolean(true));
        mandatoryMap.put("txtInter", new Boolean(true));
        mandatoryMap.put("txtPenalInter", new Boolean(true));
        mandatoryMap.put("txtAgainstClearingInter", new Boolean(true));
        mandatoryMap.put("txtPenalStatement", new Boolean(true));
        mandatoryMap.put("txtInterExpLimit", new Boolean(true));
        mandatoryMap.put("cboCommodityCode", new Boolean(true));
        mandatoryMap.put("cboGuaranteeCoverCode", new Boolean(true));
        mandatoryMap.put("cboSectorCode1", new Boolean(true));
        mandatoryMap.put("cboHealthCode", new Boolean(true));
        mandatoryMap.put("cboTypeFacility", new Boolean(true));
        mandatoryMap.put("cboDistrictCode", new Boolean(true));
        mandatoryMap.put("cboPurposeCode", new Boolean(true));
        mandatoryMap.put("cboSectorCode2", new Boolean(true));
        mandatoryMap.put("cboIndusCode", new Boolean(true));
        mandatoryMap.put("cboRepaymentCode", new Boolean(true));
        mandatoryMap.put("cbo20Code", new Boolean(true));
        mandatoryMap.put("cboRefinancingInsti", new Boolean(true));
        mandatoryMap.put("cboGovtSchemeCode", new Boolean(true));
        mandatoryMap.put("cboAssetCode", new Boolean(true));
        mandatoryMap.put("tdtNPADate", new Boolean(true));
        mandatoryMap.put("chkDirectFinance", new Boolean(true));
        mandatoryMap.put("chkECGC", new Boolean(true));
        mandatoryMap.put("chkPrioritySector", new Boolean(true));
        mandatoryMap.put("chkDocumentcomplete", new Boolean(true));
        mandatoryMap.put("chkQIS", new Boolean(true));
        mandatoryMap.put("cboSecurityNo_Insurance", new Boolean(true));
        mandatoryMap.put("txtRemark_Insurance", new Boolean(true));
        mandatoryMap.put("txtRemarks_DocumentDetails", new Boolean(true));
        mandatoryMap.put("tdtSubmitDate_DocumentDetails", new Boolean(true));
        mandatoryMap.put("txtPeriodDifference_Years", new Boolean(true));
        mandatoryMap.put("txtPeriodDifference_Months", new Boolean(true));
        mandatoryMap.put("txtPeriodDifference_Days", new Boolean(true));
        mandatoryMap.put("txtEligibleLoan", new Boolean(true));
        mandatoryMap.put("chkChequeBookAD", new Boolean(true));
        mandatoryMap.put("chkCustGrpLimitValidationAD", new Boolean(true));
        mandatoryMap.put("chkMobileBankingAD", new Boolean(true));
        mandatoryMap.put("chkNROStatusAD", new Boolean(true));
        mandatoryMap.put("chkATMAD", new Boolean(true));
        mandatoryMap.put("txtATMNoAD", new Boolean(true));
        mandatoryMap.put("tdtATMFromDateAD", new Boolean(true));
        mandatoryMap.put("tdtATMToDateAD", new Boolean(true));
        mandatoryMap.put("chkDebitAD", new Boolean(true));
        mandatoryMap.put("txtDebitNoAD", new Boolean(true));
        mandatoryMap.put("tdtDebitFromDateAD", new Boolean(true));
        mandatoryMap.put("tdtDebitToDateAD", new Boolean(true));
        mandatoryMap.put("chkCreditAD", new Boolean(true));
        mandatoryMap.put("txtCreditNoAD", new Boolean(true));
        mandatoryMap.put("tdtCreditFromDateAD", new Boolean(true));
        mandatoryMap.put("tdtCreditToDateAD", new Boolean(true));
        mandatoryMap.put("cboSettlementModeAI", new Boolean(true));
        mandatoryMap.put("cboOpModeAI", new Boolean(true));
        mandatoryMap.put("txtAccOpeningChrgAD", new Boolean(true));
        mandatoryMap.put("txtMisServiceChrgAD", new Boolean(true));
        mandatoryMap.put("chkStopPmtChrgAD", new Boolean(true));
        mandatoryMap.put("txtChequeBookChrgAD", new Boolean(true));
        mandatoryMap.put("chkChequeRetChrgAD", new Boolean(true));
        mandatoryMap.put("txtFolioChrgAD", new Boolean(true));
        mandatoryMap.put("chkInopChrgAD", new Boolean(true));
        mandatoryMap.put("txtAccCloseChrgAD", new Boolean(true));
        mandatoryMap.put("chkStmtChrgAD", new Boolean(true));
        mandatoryMap.put("cboStmtFreqAD", new Boolean(true));
        mandatoryMap.put("chkNonMainMinBalChrgAD", new Boolean(true));
        mandatoryMap.put("txtExcessWithChrgAD", new Boolean(true));
        mandatoryMap.put("chkABBChrgAD", new Boolean(true));
        mandatoryMap.put("chkNPAChrgAD", new Boolean(true));
        mandatoryMap.put("txtABBChrgAD", new Boolean(true));
        mandatoryMap.put("tdtNPAChrgAD", new Boolean(true));
        mandatoryMap.put("txtMinActBalanceAD", new Boolean(true));
        mandatoryMap.put("tdtDebit", new Boolean(true));
        mandatoryMap.put("tdtCredit", new Boolean(true));
        mandatoryMap.put("chkPayIntOnCrBalIN", new Boolean(true));
        mandatoryMap.put("chkPayIntOnDrBalIN", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public static void internationalize(java.awt.Container comp, java.util.ResourceBundle resourceBundle) {
        java.awt.Component[] children = comp.getComponents();
        javax.swing.JTextField txtDefault = new javax.swing.JTextField();
        javax.swing.JPanel panelDefault = new javax.swing.JPanel();
        
        
        for (int i = 0; i < children.length; i++) {
            if ((children[i] != null)) {
                if (children[i] instanceof JButton) {
                    JButton b1 = (JButton)children[i];
                    b1.setText(resourceBundle.getString(b1.getName()));
                } else if (children[i] instanceof JCheckBox){
                    JCheckBox b1 = (JCheckBox)children[i];
                    b1.setText(resourceBundle.getString(b1.getName()));
                } else if (children[i] instanceof JRadioButton){
                    JRadioButton b1 = (JRadioButton)children[i];
                    b1.setText(resourceBundle.getString(b1.getName()));
                } else if (children[i] instanceof JLabel){
                    JLabel b1 = (JLabel)children[i];
                    b1.setText(resourceBundle.getString(b1.getName()));
                }
            }
        }
        return;
    }
        
    
    
}
