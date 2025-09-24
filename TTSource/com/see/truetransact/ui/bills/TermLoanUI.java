/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanUI.java
 *
 * Created on November 28, 2003, 3:55 PM
 */

package com.see.truetransact.ui.bills;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryUI;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyUI;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.clientutil.EnhancedTableModel;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.util.List;

/*
 *
 * @author  shanmugavel
 * Created on November 28, 2003, 3:55 PM
 *
 */

public class TermLoanUI extends CInternalFrame  implements java.util.Observer,UIMandatoryField {
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.bills.TermLoanRB", ProxyParameters.LANGUAGE);
    //    TermLoanRB resourceBundle = new TermLoanRB();
    TermLoanOB observable;
    TermLoanBorrowerOB observableBorrow;
    TermLoanCompanyOB observableComp;
    TermLoanSecurityOB observableSecurity;
    TermLoanRepaymentOB observableRepay;
    TermLoanGuarantorOB observableGuarantor;
    TermLoanDocumentDetailsOB observableDocument;
    TermLoanInterestOB observableInt;
    TermLoanClassificationOB observableClassi;
    TermLoanOtherDetailsOB observableOtherDetails;
    AuthorizedSignatoryUI authSignUI = null;
    PowerOfAttorneyUI poaUI = null;
    private Date date;
    private HashMap mandatoryMap;
    
    private boolean updateModeAuthorize = false;
    private boolean updateModePoA = false;
    private boolean updateSanctionFacility = false;
    private boolean updateSanctionMain = false;
    private boolean updateSecurity = false;
    private boolean updateRepayment = false;
    private boolean repayNewMode    = false;
    private boolean allowMultiRepay = true;
    private boolean updateGuarantor = false;
    private boolean updateDocument = false;
    private boolean updateInterest  = false;
    private boolean isFilled = false;
    private boolean sanMousePress=false;
    private boolean sanDetailMousePressedForLTD = false;
    private boolean facilityFlag=true;
    private  HashMap accNumMap=new HashMap();
    
    
    int result;
    int modeAuthorize = -1;
    int modePoA       = -1;
    int rowSanctionFacility     = -1;
    int rowSanctionMain         = -1;
    int rowFacilityTabSanction  = -1;
    int rowFacilityTabFacility  = -1;
    int rowSecurity  = -1;
    int rowRepayment = -1;
    int dumRowRepay  = -1;
    int rowGuarantor = -1;
    int rowDocument = -1;
    int rowInterest  = -1;
    boolean sanction=false;
    boolean sandetail=false;
    boolean santab=false;
    boolean sanfacTab=false;
    int rowmaintab=-1;
    int rowfactab=-1;
    int rowsan=-1;
    int rowsanDetail=-1;
    private final static Logger log = Logger.getLogger(TermLoanUI.class);
    private final        String ACT = "ACT";
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String CORPORATE = "CORPORATE";
    private final        String FLOATING_RATE = "FLOATING_RATE";
    private final        String FIXED_RATE = "FIXED_RATE";
    private final        String INDIVIDUAL = "INDIVIDUAL";
    private final        String IS_COOPERATIVE = "IS_COOPERATIVE";
    private final        String JOINT_ACCOUNT = "JOINT_ACCOUNT";
    private final        String LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final        String REJECT = "REJECT";
    private final        String EXCEPTION = "EXCEPTION";
    private final        String PROD = "PROD";
    
    private String viewType = "";
    private String loanType = "OTHERS";
    private boolean facilitySaved = false;
    private boolean btnNewPressed = false;
    private Date currDt = null;
    
    /** Creates new form TermLoanUI */
    public TermLoanUI() {
        currDt = ClientUtil.getCurrentDate();
        termLoanUI();
    }
    
    public TermLoanUI(String loanType) {
        currDt = ClientUtil.getCurrentDate();
        this.loanType = loanType;
        termLoanUI();
    }
    
    private void termLoanUI(){
        initComponents();
        setFieldNames();
        internationalize();
        setMaxLength();
        setObservable();
        if (loanType.equals("LTD")) {
            tabLimitAmount.remove(panGuarantorInsuranceDetails);
            //            tabLimitAmount.remove(panAccountDetails);
            tabLimitAmount.remove(panShareMaintenance);
        }
        authSignUI = new AuthorizedSignatoryUI("TL");
        poaUI = new PowerOfAttorneyUI("TL");
        observableBorrow.setAuthSignAndPoAOB(authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
        observable.setExtendedOB(authSignUI.getAuthorizedSignatoryOB(), authSignUI.getAuthorizedSignatoryInstructionOB(), poaUI.getPowerOfAttorneyOB());
        ClientUtil.enableDisable(this, false);
        allEnableDisable();
        initComponentData();
        if (loanType.equals("LTD")) {
            if (observable.getCbmIntGetFrom().containsElement("Product"))
                observable.getCbmIntGetFrom().removeKeyAndElement("PROD");
        } else if (!observable.getCbmIntGetFrom().containsElement("Product"))
            observable.getCbmIntGetFrom().addKeyAndElement("PROD", "Product");
        if(loanType.equals("OTHERS") && observable.getCbmTypeOfFacility().containsElement("Loans Against Deposits")) {
            //            observable.getCbmTypeOfFacility().removeElement("Loans Against Deposits");
            observable.getCbmTypeOfFacility().removeKeyAndElement(LOANS_AGAINST_DEPOSITS);
        } else if(loanType.equals("LTD") && !observable.getCbmTypeOfFacility().containsElement("Loans Against Deposits")) {
            observable.getCbmTypeOfFacility().addKeyAndElement(LOANS_AGAINST_DEPOSITS, "Loans Against Deposits");
        }
        if(loanType.equals("OTHERS") && observable.getCbmRepayFreq().containsElement("Lump Sum")){
            observable.getCbmRepayFreq().removeKeyAndElement("1");
        }else if (!observable.getCbmRepayFreq().containsElement("1"))
            observable.getCbmRepayFreq().addKeyAndElement("1", "Lump Sum");
        
        if(loanType.equals("OTHERS") && observableRepay.getCbmRepayType().containsElement("Lump Sum")){
            observableRepay.getCbmRepayType().removeKeyAndElement("LUMP_SUM");
        }else if (!observableRepay.getCbmRepayType().containsElement("LUMP_SUM"))
            observableRepay.getCbmRepayType().addKeyAndElement("LUMP_SUM", "Lump Sum");
        
        setMandatoryHashMap();
        setHelpMessage();
        observable.resetForm();
        observable.resetStatus();
        authSignUI.setLblStatus(observable.getLblStatus());
        poaUI.setLblStatus(observable.getLblStatus());
        authSignUI.setViewType(viewType);
        poaUI.setViewType(viewType);
        tabLimitAmount.add(authSignUI, "Authorized Signatory", 1);
        tabLimitAmount.add(poaUI, "Power of Attorney", 2);
        tabLimitAmount.remove(panAccountDetails);
        tabLimitAmount.resetVisits();
        if (!loanType.equals("LTD")) {
            new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTableFields_SD);
        }
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSanctionDetails_Sanction);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSanctionDetails_Mode);
        btnDelete.setVisible(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(!btnView.isEnabled());
        //        btnClosedAcc.setVisible(false);
    }
    
    private void allEnableDisable(){
        setCompanyDetailsEnableDisable(false);
        setBorrowerDetailsEnableDisable(false);
        txtPeriodDifference_Days.setEditable(false);
        txtPeriodDifference_Months.setEditable(false);
        txtPeriodDifference_Years.setEditable(false);
        setButtonEnableDisable();
        txtGuarantorNo.setEditable(false);
        setAllBorrowerBtnsEnableDisable(false);
        setbtnCustEnableDisable(false);
        setAllTablesEnableDisable(true);
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons Authorized Signatory
        setAllSanctionFacilityEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllRepaymentDetailsEnableDisable(false);
        btnEMI_Calculate.setEnabled(false);
        setAllRepaymentBtnsEnableDisable(false);
        setAllGuarantorDetailsEnableDisable(false);
        setAllGuarantorBtnsEnableDisable(false);
        setAllDocumentDetailsEnableDisable(false);
        setDocumentToolBtnEnableDisable(false);
        setAllInterestDetailsEnableDisable(false);
        setAllInterestBtnsEnableDisable(false);
        setAllClassificationDetailsEnableDisable(false);
        //        lblAccType.setVisible(false);
        //        rdoAccType_New.setVisible(false);
        //        rdoAccType_Transfered.setVisible(false);
        disableFields();
    }
    
    private void disableFields(){
        txtPurposeDesc.setVisible(false);
        lblPurposeDesc.setVisible(false);
        lblSettlementModeAI.setVisible(false);
        cboSettlementModeAI.setVisible(false);
        chkNPAChrgAD.setVisible(false);
        lblNPAChrgAD.setVisible(false);
        lblNPA.setVisible(false);
        tdtNPAChrgAD.setVisible(false);
        txtCustID.setEditable(true);
        //        txtCustomerID.setEnabled(true);
        txtSecurityNo.setEditable(false);
        btnFacilityDelete.setVisible(false);
        disableLastIntApplDate();
    }
    
    private void setObservable(){
        observable = TermLoanOB.getInstance();
        observable.addObserver(this);
        observable.setLoanType(loanType);
        observableBorrow = TermLoanBorrowerOB.getInstance();
        observableBorrow.addObserver(this);
        observableComp = TermLoanCompanyOB.getInstance();
        observableComp.addObserver(this);
        observableSecurity = TermLoanSecurityOB.getInstance();
        observableSecurity.addObserver(this);
        observableRepay = TermLoanRepaymentOB.getInstance();
        observableRepay.addObserver(this);
        observableInt = TermLoanInterestOB.getInstance();
        observableInt.addObserver(this);
        //        if (loanType.equals("OTHERS")) {
        observableGuarantor = TermLoanGuarantorOB.getInstance();
        observableGuarantor.addObserver(this);
        observableOtherDetails = TermLoanOtherDetailsOB.getInstance();
        observableOtherDetails.addObserver(this);
        //        }
        observableDocument = TermLoanDocumentDetailsOB.getInstance();
        observableDocument.addObserver(this);
        observableClassi = TermLoanClassificationOB.getInstance();
        observableClassi.addObserver(this);
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
    
    public void setHelpMessage() {
        final TermLoanMRB objMandatoryRB = new TermLoanMRB();
        txtRepayScheduleMode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRepayScheduleMode"));
        tdtDisbursement_Dt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDisbursement_Dt"));
        txtEligibleLoan.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEligibleLoan"));
        txtMargin.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMargin"));
        txtAcct_Name.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcct_Name"));
        cboIntGetFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntGetFrom"));
        cboRepayFreq_Repayment.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRepayFreq_Repayment"));
        txtCustID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustID"));
        cboAccStatus.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAccStatus"));
        cboConstitution.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConstitution"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
        txtReferences.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReferences"));
        txtCompanyRegisNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCompanyRegisNo"));
        tdtDateEstablishment.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateEstablishment"));
        tdtDealingWithBankSince.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDealingWithBankSince"));
        txtRiskRating.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRiskRating"));
        cboNatureBusiness.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNatureBusiness"));
        txtRemarks__CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks__CompDetail"));
        txtNetWorth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetWorth"));
        tdtAsOn.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAsOn"));
        tdtCreditFacilityAvailSince.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCreditFacilityAvailSince"));
        txtChiefExecutiveName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChiefExecutiveName"));
        cboAddressType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAddressType"));
        txtStreet_CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet_CompDetail"));
        txtArea_CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea_CompDetail"));
        cboCity_CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity_CompDetail"));
        cboState_CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState_CompDetail"));
        cboCountry_CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry_CompDetail"));
        txtPin_CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPin_CompDetail"));
        txtPhone_CompDetail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhone_CompDetail"));
        txtSanctionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSanctionNo"));
        tdtSanctionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSanctionDate"));
        cboSanctioningAuthority.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSanctioningAuthority"));
        txtSanctionRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSanctionRemarks"));
        cboModeSanction.setHelpMessage(lblMsg, objMandatoryRB.getString("cboModeSanction"));
        txtNoInstallments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoInstallments"));
        cboRepayFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRepayFreq"));
        cboTypeOfFacility.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTypeOfFacility"));
        txtLimit_SD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLimit_SD"));
        tdtFDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFDate"));
        tdtTDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTDate"));
        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        rdoSecurityDetails_Unsec.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSecurityDetails_Unsec"));
        chkStockInspect.setHelpMessage(lblMsg, objMandatoryRB.getString("chkStockInspect"));
        chkInsurance.setHelpMessage(lblMsg, objMandatoryRB.getString("chkInsurance"));
        chkGurantor.setHelpMessage(lblMsg, objMandatoryRB.getString("chkGurantor"));
        //        rdoAccType_New.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAccType_New"));
        rdoAccLimit_Main.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAccLimit_Main"));
        rdoNatureInterest_PLR.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNatureInterest_PLR"));
        cboInterestType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInterestType"));
        rdoRiskWeight_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoRiskWeight_Yes"));
        tdtDemandPromNoteDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDemandPromNoteDate"));
        tdtDemandPromNoteExpDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDemandPromNoteExpDate"));
        tdtAODDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAODDate"));
        rdoMultiDisburseAllow_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMultiDisburseAllow_Yes"));
        rdoSubsidy_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSubsidy_Yes"));
        txtPurposeDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurposeDesc"));
        txtGroupDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGroupDesc"));
        rdoInterest_Simple.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoInterest_Simple"));
        rdoActive_Repayment.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoActive_Repayment"));
        rdoInActive_Repayment.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoInActive_Repayment"));
        txtContactPerson.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContactPerson"));
        txtContactPhone.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContactPhone"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtSecurityNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSecurityNo"));
        txtCustID_Security.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustID_Security"));
        txtSecurityValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSecurityValue"));
        tdtFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDate"));
        tdtToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtToDate"));
        txtScheduleNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtScheduleNo"));
        txtLaonAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLaonAmt"));
        cboRepayType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRepayType"));
        txtNoMonthsMora.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoMonthsMora"));
        tdtFirstInstall.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFirstInstall"));
        tdtLastInstall.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastInstall"));
        txtTotalBaseAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalBaseAmt"));
        txtAmtPenulInstall.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmtPenulInstall"));
        txtAmtLastInstall.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmtLastInstall"));
        txtTotalInstallAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalInstallAmt"));
        rdoDoAddSIs_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDoAddSIs_Yes"));
        rdoPostDatedCheque_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPostDatedCheque_Yes"));
        txtNoInstall.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoInstall"));
        txtCustomerID_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerID_GD"));
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        txtGuaranAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuaranAccNo"));
        txtGuaranName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuaranName"));
        tdtDOB_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDOB_GD"));
        txtStreet_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet_GD"));
        txtArea_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea_GD"));
        cboCity_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity_GD"));
        txtPin_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPin_GD"));
        cboState_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState_GD"));
        cboCountry_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry_GD"));
        txtPhone_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhone_GD"));
        cboConstitution_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConstitution_GD"));
        txtGuarantorNetWorth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuarantorNetWorth"));
        tdtAsOn_GD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAsOn_GD"));
        tdtFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFrom"));
        tdtTo.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTo"));
        txtFromAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromAmt"));
        txtToAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToAmt"));
        txtInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInter"));
        txtFacility_Moratorium_Period.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFacility_Moratorium_Period"));
        tdtFacility_Repay_Date.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFacility_Repay_Date"));
        chkMoratorium_Given.setHelpMessage(lblMsg, objMandatoryRB.getString("chkMoratorium_Given"));
        txtPenalInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalInter"));
        txtAgainstClearingInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAgainstClearingInter"));
        txtPenalStatement.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalStatement"));
        txtInterExpLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterExpLimit"));
        cboCommodityCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommodityCode"));
        cboGuaranteeCoverCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGuaranteeCoverCode"));
        cboSectorCode1.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSectorCode1"));
        cboHealthCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHealthCode"));
        cboTypeFacility.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTypeFacility"));
        cboDistrictCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDistrictCode"));
        cboPurposeCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPurposeCode"));
        cboIndusCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIndusCode"));
        cboWeakerSectionCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWeakerSectionCode"));
        cbo20Code.setHelpMessage(lblMsg, objMandatoryRB.getString("cbo20Code"));
        cboRefinancingInsti.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRefinancingInsti"));
        cboGovtSchemeCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGovtSchemeCode"));
        cboAssetCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAssetCode"));
        tdtNPADate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNPADate"));
        chkDirectFinance.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDirectFinance"));
        chkECGC.setHelpMessage(lblMsg, objMandatoryRB.getString("chkECGC"));
        chkPrioritySector.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPrioritySector"));
        chkDocumentcomplete.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDocumentcomplete"));
        chkQIS.setHelpMessage(lblMsg, objMandatoryRB.getString("chkQIS"));
        tdtSubmitDate_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSubmitDate_DocumentDetails"));
        txtRemarks_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks_DocumentDetails"));
        rdoYes_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoYes_DocumentDetails"));
        rdoNo_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNo_DocumentDetails"));
        chkChequeBookAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkChequeBookAD"));
        chkCustGrpLimitValidationAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCustGrpLimitValidationAD"));
        chkMobileBankingAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkMobileBankingAD"));
        chkNROStatusAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNROStatusAD"));
        chkATMAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkATMAD"));
        txtATMNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtATMNoAD"));
        tdtATMFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtATMFromDateAD"));
        tdtATMToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtATMToDateAD"));
        chkDebitAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitAD"));
        txtDebitNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitNoAD"));
        tdtDebitFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDebitFromDateAD"));
        tdtDebitToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDebitToDateAD"));
        chkCreditAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditAD"));
        txtCreditNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditNoAD"));
        tdtCreditFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCreditFromDateAD"));
        tdtCreditToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCreditToDateAD"));
        cboSettlementModeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSettlementModeAI"));
        cboOpModeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOpModeAI"));
        txtAccOpeningChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccOpeningChrgAD"));
        txtMisServiceChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMisServiceChrgAD"));
        chkStopPmtChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkStopPmtChrgAD"));
        txtChequeBookChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequeBookChrgAD"));
        chkChequeRetChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkChequeRetChrgAD"));
        txtFolioChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFolioChrgAD"));
        chkInopChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkInopChrgAD"));
        txtAccCloseChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccCloseChrgAD"));
        chkStmtChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkStmtChrgAD"));
        cboStmtFreqAD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStmtFreqAD"));
        chkNonMainMinBalChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNonMainMinBalChrgAD"));
        txtExcessWithChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExcessWithChrgAD"));
        chkABBChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkABBChrgAD"));
        chkNPAChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNPAChrgAD"));
        txtABBChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtABBChrgAD"));
        tdtNPAChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNPAChrgAD"));
        txtMinActBalanceAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinActBalanceAD"));
        tdtDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDebit"));
        tdtCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCredit"));
        chkPayIntOnCrBalIN.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPayIntOnCrBalIN"));
        chkPayIntOnDrBalIN.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPayIntOnDrBalIN"));
        rdoYes_Executed_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoYes_Executed_DOC"));
        rdoNo_Executed_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNo_Executed_DOC"));
        rdoYes_Mandatory_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoYes_Mandatory_DOC"));
        rdoNo_Mandatory_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNo_Mandatory_DOC"));
        tdtExecuteDate_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExecuteDate_DOC"));
        tdtExpiryDate_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExpiryDate_DOC"));
        tdtAccountOpenDate.setHelpMessage(lblMsg,objMandatoryRB.getString("tdtAccountOpenDate"));
        cboRecommendedByType.setHelpMessage(lblMsg,objMandatoryRB.getString("cboRecommendedByType"));
    }
    
    
    private void setFieldNames() {
        lblRepayScheduleMode.setName("lblRepayScheduleMode");
        txtRepayScheduleMode.setName("txtRepayScheduleMode");
        lblAcct_Name.setName("lblAcct_Name");
        txtAcct_Name.setName("txtAcct_Name");
        lblExpiryDate_DOC.setName("lblExpiryDate_DOC");
        tdtExpiryDate_DOC.setName("tdtExpiryDate_DOC");
        lblExecuteDate_DOC.setName("lblExecuteDate_DOC");
        tdtExecuteDate_DOC.setName("tdtExecuteDate_DOC");
        lblExecuted_DOC.setName("lblExecuted_DOC");
        rdoNo_Executed_DOC.setName("rdoNo_Executed_DOC");
        rdoYes_Executed_DOC.setName("rdoYes_Executed_DOC");
        lblMandatory_DOC.setName("lblMandatory_DOC");
        rdoNo_Mandatory_DOC.setName("rdoNo_Mandatory_DOC");
        rdoYes_Mandatory_DOC.setName("rdoYes_Mandatory_DOC");
        lblProdID_Disp_ODetails.setName("lblProdID_Disp_ODetails");
        lblProdID_ODetails.setName("lblProdID_ODetails");
        lblAcctHead_Disp_ODetails.setName("lblAcctHead_Disp_ODetails");
        lblAcctHead_ODetails.setName("lblAcctHead_ODetails");
        lblAcctNo_Disp_ODetails.setName("lblAcctNo_Disp_ODetails");
        lblAcctNo_ODetails.setName("lblAcctNo_ODetails");
        panAcctInfo_ODetails.setName("panAcctInfo_ODetails");
        lblDisbursement_Dt.setName("lblDisbursement_Dt");
        tdtDisbursement_Dt.setName("tdtDisbursement_Dt");
        lblEligibleLoan.setName("lblEligibleLoan");
        txtEligibleLoan.setName("txtEligibleLoan");
        PanAcc_CD.setName("PanAcc_CD");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustID.setName("btnCustID");
        btnCustID_Security.setName("btnCustID_Security");
        btnSecurityNo_Security.setName("btnSecurityNo_Security");
        btnCustomerID_GD.setName("btnCustomerID_GD");
        btnAccNo.setName("btnAccNo");
        btnNew_Borrower.setName("btnNew_Borrower");
        btnEMI_Calculate.setName("btnEMI_Calculate");
        btnDeleteBorrower.setName("btnDeleteBorrower");
        btnToMain_Borrower.setName("btnToMain_Borrower");
        btnDelete.setName("btnDelete");
        btnDelete1.setName("btnDelete1");
        btnDelete2_SD.setName("btnDelete2_SD");
        btnFacilityDelete.setName("btnFacilityDelete");
        btnEdit.setName("btnEdit");
        panPeriodDifference.setName("panPeriodDifference");
        lblPeriodDifference_Years.setName("lblPeriodDifference_Years");
        txtPeriodDifference_Years.setName("txtPeriodDifference_Years");
        lblPeriodDifference_Months.setName("lblPeriodDifference_Months");
        txtPeriodDifference_Months.setName("txtPeriodDifference_Months");
        lblPeriodDifference_Days.setName("lblPeriodDifference_Days");
        txtPeriodDifference_Days.setName("txtPeriodDifference_Days");
        lblIntGetFrom.setName("lblIntGetFrom");
        cboIntGetFrom.setName("cboIntGetFrom");
        btnGuarantorDelete.setName("btnGuarantorDelete");
        btnGuarantorNew.setName("btnGuarantorNew");
        btnGuarantorSave.setName("btnGuarantorSave");
        btnInterestMaintenanceDelete.setName("btnInterestMaintenanceDelete");
        btnInterestMaintenanceNew.setName("btnInterestMaintenanceNew");
        btnInterestMaintenanceSave.setName("btnInterestMaintenanceSave");
        btnNew.setName("btnNew");
        btnNew1.setName("btnNew1");
        btnNew2_SD.setName("btnNew2_SD");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnRepayment_Delete.setName("btnRepayment_Delete");
        btnRepayment_New.setName("btnRepayment_New");
        btnRepayment_Save.setName("btnRepayment_Save");
        btnException.setName("btnException");
        btnSave_DocumentDetails.setName("btnSave_DocumentDetails");
        lblMargin.setName("lblMargin");
        txtMargin.setName("txtMargin");
        lblProdID_Disp_DocumentDetails.setName("lblProdID_Disp_DocumentDetails");
        lblProdID_DocumentDetails.setName("lblProdID_DocumentDetails");
        lblAcctHead_Disp_DocumentDetails.setName("lblAcctHead_Disp_DocumentDetails");
        lblAcctHead_DocumentDetails.setName("lblAcctHead_DocumentDetails");
        lblAcctNo_Disp_DocumentDetails.setName("lblAcctNo_Disp_DocumentDetails");
        lblAcctNo_DocumentDetails.setName("lblAcctNo_DocumentDetails");
        lblDocDesc_Disp_DocumentDetails.setName("lblDocDesc_Disp_DocumentDetails");
        lblDocDesc_DocumentDetails.setName("lblDocDesc_DocumentDetails");
        lblDocNo_Disp_DocumentDetails.setName("lblDocNo_Disp_DocumentDetails");
        lblDocNo_DocumentDetails.setName("lblDocNo_DocumentDetails");
        lblDocType_Disp_DocumentDetails.setName("lblDocType_Disp_DocumentDetails");
        lblDocType_DocumentDetails.setName("lblDocType_DocumentDetails");
        lblSubmitDate_DocumentDetails.setName("lblSubmitDate_DocumentDetails");
        lblSubmitted_DocumentDetails.setName("lblSubmitted_DocumentDetails");
        lblRemarks_DocumentDetails.setName("lblRemarks_DocumentDetails");
        txtRemarks_DocumentDetails.setName("txtRemarks_DocumentDetails");
        tdtSubmitDate_DocumentDetails.setName("tdtSubmitDate_DocumentDetails");
        rdoYes_DocumentDetails.setName("rdoYes_DocumentDetails");
        rdoNo_DocumentDetails.setName("rdoNo_DocumentDetails");
        srpTable_DocumentDetails.setName("srpTable_DocumentDetails");
        panTable_DocumentDetails.setName("panTable_DocumentDetails");
        tblTable_DocumentDetails.setName("tblTable_DocumentDetails");
        lblStatus_Repayment.setName("lblStatus_Repayment");
        rdoActive_Repayment.setName("rdoActive_Repayment");
        rdoInActive_Repayment.setName("rdoInActive_Repayment");
        panStatus_Repayment.setName("panStatus_Repayment");
        panAcctDetails_DocumentDetails.setName("panAcctDetails_DocumentDetails");
        panSubmitted_DocumentDetails.setName("panSubmitted_DocumentDetails");
        btnSave1.setName("btnSave1");
        btnSave2_SD.setName("btnSave2_SD");
        btnFacilitySave.setName("btnFacilitySave");
        btnSecurityDelete.setName("btnSecurityDelete");
        btnSecurityNew.setName("btnSecurityNew");
        btnSecuritySave.setName("btnSecuritySave");
        cbo20Code.setName("cbo20Code");
        cboAccStatus.setName("cboAccStatus");
        cboAddressType.setName("cboAddressType");
        cboAssetCode.setName("cboAssetCode");
        cboCategory.setName("cboCategory");
        cboCity_CompDetail.setName("cboCity_CompDetail");
        cboCity_GD.setName("cboCity_GD");
        cboProdId.setName("cboProdId");
        cboProdType.setName("cboProdType");
        cboCommodityCode.setName("cboCommodityCode");
        cboConstitution.setName("cboConstitution");
        cboConstitution_GD.setName("cboConstitution_GD");
        cboCountry_CompDetail.setName("cboCountry_CompDetail");
        cboCountry_GD.setName("cboCountry_GD");
        cboDistrictCode.setName("cboDistrictCode");
        cboGovtSchemeCode.setName("cboGovtSchemeCode");
        cboGuaranteeCoverCode.setName("cboGuaranteeCoverCode");
        cboHealthCode.setName("cboHealthCode");
        cboIndusCode.setName("cboIndusCode");
        cboRepayFreq_Repayment.setName("cboRepayFreq_Repayment");
        cboInterestType.setName("cboInterestType");
        cboModeSanction.setName("cboModeSanction");
        cboNatureBusiness.setName("cboNatureBusiness");
        lblProID_CD_Disp.setName("lblProID_CD_Disp");
        lblProdID_GD_Disp.setName("lblProdID_GD_Disp");
        lblProdID_RS_Disp.setName("lblProdID_RS_Disp");
        lblProdId_Disp.setName("lblProdId_Disp");
        cboProductId.setName("cboProductId");
        cboPurposeCode.setName("cboPurposeCode");
        cboRefinancingInsti.setName("cboRefinancingInsti");
        cboRepayFreq.setName("cboRepayFreq");
        cboRepayType.setName("cboRepayType");
        cboRecommendedByType.setName("cboRecommendedByType");
        cboWeakerSectionCode.setName("cboWeakerSectionCode");
        cboSanctioningAuthority.setName("cboSanctioningAuthority");
        cboSectorCode1.setName("cboSectorCode1");
        txtCustID_Security.setName("txtCustID_Security");
        cboState_CompDetail.setName("cboState_CompDetail");
        cboState_GD.setName("cboState_GD");
        cboTypeFacility.setName("cboTypeFacility");
        cboTypeOfFacility.setName("cboTypeOfFacility");
        lblProdID_IM_Disp.setName("lblProdID_IM_Disp");
        chkDirectFinance.setName("chkDirectFinance");
        chkDocumentcomplete.setName("chkDocumentcomplete");
        chkECGC.setName("chkECGC");
        chkGurantor.setName("chkGurantor");
        chkInsurance.setName("chkInsurance");
        chkPrioritySector.setName("chkPrioritySector");
        chkQIS.setName("chkQIS");
        chkStockInspect.setName("chkStockInspect");
        lbl20Code.setName("lbl20Code");
        lblAODDate.setName("lblAODDate");
        lblAccHead.setName("lblAccHead");
        lblAccHeadSec.setName("lblAccHeadSec");
        lblAccHeadSec_2.setName("lblAccHeadSec_2");
        lblAccHead_2.setName("lblAccHead_2");
        lblAccHead_CD.setName("lblAccHead_CD");
        lblAccHead_CD_2.setName("lblAccHead_CD_2");
        lblAccHead_GD.setName("lblAccHead_GD");
        lblAccHead_GD_2.setName("lblAccHead_GD_2");
        lblAccHead_IM.setName("lblAccHead_IM");
        lblAccHead_IM_2.setName("lblAccHead_IM_2");
        lblAccHead_RS.setName("lblAccHead_RS");
        lblAccHead_RS_2.setName("lblAccHead_RS_2");
        lblAccountHead_FD.setName("lblAccountHead_FD");
        lblAccountHead_FD_Disp.setName("lblAccountHead_FD_Disp");
        lblAccLimit.setName("lblAccLimit");
        lblAccNoSec.setName("lblAccNoSec");
        lblAccNoSec_2.setName("lblAccNoSec_2");
        lblAccNo_CD.setName("lblAccNo_CD");
        lblAccNo_CD_2.setName("lblAccNo_CD_2");
        lblAccNo_GD.setName("lblAccNo_GD");
        lblAccNo_GD_2.setName("lblAccNo_GD_2");
        lblAccNo_IM.setName("lblAccNo_IM");
        lblAccNo_IM_2.setName("lblAccNo_IM_2");
        lblAccNo_RS.setName("lblAccNo_RS");
        lblAccNo_RS_2.setName("lblAccNo_RS_2");
        lblAccStatus.setName("lblAccStatus");
        lblAcctNo_Sanction_Disp.setName("lblAcctNo_Sanction_Disp");
        lblAcctNo_Sanction.setName("lblAcctNo_Sanction");
        lblAcctNo_FD_Disp.setName("lblAcctNo_FD_Disp");
        lblAcctNo_FD.setName("lblAcctNo_FD");
        //        lblAccType.setName("lblAccType");
        lblAddressType.setName("lblAddressType");
        lblAgainstClearingInter.setName("lblAgainstClearingInter");
        lblDOB_GD.setName("lblDOB_GD");
        lblAmtLastInstall.setName("lblAmtLastInstall");
        lblAmtPenulInstall.setName("lblAmtPenulInstall");
        lblArea_CompDetail.setName("lblArea_CompDetail");
        lblArea_GD.setName("lblArea_GD");
        lblAsOn.setName("lblAsOn");
        lblAsOn_GD.setName("lblAsOn_GD");
        lblAssetCode.setName("lblAssetCode");
        lblBlank1.setName("lblBlank1");
        lblBorrowerNo.setName("lblBorrowerNo");
        lblBorrowerNo_2.setName("lblBorrowerNo_2");
        lblCategory.setName("lblCategory");
        lblChiefExecutiveName.setName("lblChiefExecutiveName");
        lblCity_BorrowerProfile.setName("lblCity_BorrowerProfile");
        lblCity_BorrowerProfile_2.setName("lblCity_BorrowerProfile_2");
        lblCity_CompDetail.setName("lblCity_CompDetail");
        lblCity_GD.setName("lblCity_GD");
        lblCommodityCode.setName("lblCommodityCode");
        lblCompanyRegisNo.setName("lblCompanyRegisNo");
        lblConstitution.setName("lblConstitution");
        lblConstitution_GD.setName("lblConstitution_GD");
        lblContactPerson.setName("lblContactPerson");
        lblContactPhone.setName("lblContactPhone");
        lblCountry_CompDetail.setName("lblCountry_CompDetail");
        lblCountry_GD.setName("lblCountry_GD");
        lblCreditFacilityAvailSince.setName("lblCreditFacilityAvailSince");
        lblCustID.setName("lblCustID");
        lblCustName_Security_Display.setName("lblCustName_Security_Display");
        lblCustName_Security.setName("lblCustName_Security");
        lblCustName.setName("lblCustName");
        lblCustName_2.setName("lblCustName_2");
        lblCustomerID_GD.setName("lblCustomerID_GD");
        lblDateEstablishment.setName("lblDateEstablishment");
        lblDealingWithBankSince.setName("lblDealingWithBankSince");
        lblDemandPromNoteDate.setName("lblDemandPromNoteDate");
        lblDemandPromNoteExpDate.setName("lblDemandPromNoteExpDate");
        lblDirectFinance.setName("lblDirectFinance");
        lblDistrictCode.setName("lblDistrictCode");
        lblDoAddSIs.setName("lblDoAddSIs");
        lblDocumentcomplete.setName("lblDocumentcomplete");
        lblECGC.setName("lblECGC");
        lblExpiryDate.setName("lblExpiryDate");
        lblExpiryDate_2.setName("lblExpiryDate_2");
        lblFDate.setName("lblFDate");
        lblFax_BorrowerProfile.setName("lblFax_BorrowerProfile");
        lblFax_BorrowerProfile_2.setName("lblFax_BorrowerProfile_2");
        lblFirstInstall.setName("lblFirstInstall");
        lblFrom.setName("lblFrom");
        lblFromAmt.setName("lblFromAmt");
        lblFromDate.setName("lblFromDate");
        lblGovtSchemeCode.setName("lblGovtSchemeCode");
        lblGroupDesc.setName("lblGroupDesc");
        lblProdId.setName("lblProdId");
        lblProdType.setName("lblProdType");
        lblGuaranAccNo.setName("lblGuaranAccNo");
        lblGuaranName.setName("lblGuaranName");
        lblGuarantorNo.setName("lblGuarantorNo");
        lblGuaranteeCoverCode.setName("lblGuaranteeCoverCode");
        lblGuarantorNetWorth.setName("lblGuarantorNetWorth");
        lblHealthCode.setName("lblHealthCode");
        lblIndusCode.setName("lblIndusCode");
        lblRepayFreq_Repayment.setName("lblRepayFreq_Repayment");
        lblInter.setName("lblInter");
        lblInterExpLimit.setName("lblInterExpLimit");
        lblInterest.setName("lblInterest");
        lblInterestType.setName("lblInterestType");
        lblLaonAmt.setName("lblLaonAmt");
        lblLastInstall.setName("lblLastInstall");
        lblPenalStatement.setName("lblPenalStatement");
        lblLimitAmt.setName("lblLimitAmt");
        lblLimitAmt_2.setName("lblLimitAmt_2");
        lblLimit_SD.setName("lblLimit_SD");
        lblModeSanction.setName("lblModeSanction");
        lblMsg.setName("lblMsg");
        lblMultiDisburseAllow.setName("lblMultiDisburseAllow");
        lblNPADate.setName("lblNPADate");
        lblNatureBusiness.setName("lblNatureBusiness");
        lblNatureInterest.setName("lblNatureInterest");
        lblNetWorth.setName("lblNetWorth");
        lblNoInstall.setName("lblNoInstall");
        lblNoInstallments.setName("lblNoInstallments");
        lblNoMonthsMora.setName("lblNoMonthsMora");
        lblOpenDate.setName("lblOpenDate");
        lblOpenDate2.setName("lblOpenDate2");
        lblPLR_Limit.setName("lblPLR_Limit");
        lblPLR_Limit_2.setName("lblPLR_Limit_2");
        lblPenalInter.setName("lblPenalInter");
        lblPhone_BorrowerProfile.setName("lblPhone_BorrowerProfile");
        lblPhone_BorrowerProfile_2.setName("lblPhone_BorrowerProfile_2");
        lblPhone_CompDetail.setName("lblPhone_CompDetail");
        lblPhone_GD.setName("lblPhone_GD");
        lblPin_BorrowerProfile.setName("lblPin_BorrowerProfile");
        lblPin_BorrowerProfile_2.setName("lblPin_BorrowerProfile_2");
        lblPin_CompDetail.setName("lblPin_CompDetail");
        lblPin_GD.setName("lblPin_GD");
        lblPostDatedCheque.setName("lblPostDatedCheque");
        lblPrioritySector.setName("lblPrioritySector");
        lblProID_CD.setName("lblProID_CD");
        lblProdID_GD.setName("lblProdID_GD");
        lblProdID_IM.setName("lblProdID_IM");
        lblProdID_RS.setName("lblProdID_RS");
        lblProdId1.setName("lblProdId1");
        lblProductId.setName("lblProductId");
        lblProductID_FD.setName("lblProductID_FD");
        lblProductID_FD_Disp.setName("lblProductID_FD_Disp");
        lblPurposeCode.setName("lblPurposeCode");
        lblPurposeDesc.setName("lblPurposeDesc");
        lblQIS.setName("lblQIS");
        lblReferences.setName("lblReferences");
        lblRefinancingInsti.setName("lblRefinancingInsti");
        lblRemark_FD.setName("lblRemark_FD");
        lblRemarks.setName("lblRemarks");
        lblRemarks__CompDetail.setName("lblRemarks__CompDetail");
        lblRepayFreq.setName("lblRepayFreq");
        lblRepayType.setName("lblRepayType");
        lblWeakerSectionCode.setName("lblWeakerSectionCode");
        lblRiskRating.setName("lblRiskRating");
        lblRiskWeight.setName("lblRiskWeight");
        lblSancDate.setName("lblSancDate");
        lblSancDate_2.setName("lblSancDate_2");
        lblSanctionDate.setName("lblSanctionDate");
        lblSanctionDate1.setName("lblSanctionDate1");
        lblSanctionDate2.setName("lblSanctionDate2");
        lblSanctionNo.setName("lblSanctionNo");
        lblSanctionSlNo.setName("lblSanctionSlNo");
        lblSanctionNo1.setName("lblSanctionNo1");
        lblSanctionNo2.setName("lblSanctionNo2");
        lblSanctioningAuthority.setName("lblSanctioningAuthority");
        lblScheduleNo.setName("lblScheduleNo");
        lblSectorCode1.setName("lblSectorCode1");
        lblCustID_Security.setName("lblCustID_Security");
        lblSecurityNo.setName("lblSecurityNo");
        lblSecurityValue.setName("lblSecurityValue");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblState_BorrowerProfile.setName("lblState_BorrowerProfile");
        lblState_BorrowerProfile_2.setName("lblState_BorrowerProfile_2");
        lblState_CompDetail.setName("lblState_CompDetail");
        lblState_GD.setName("lblState_GD");
        lblStatus.setName("lblStatus");
        lblStreet_CompDetail.setName("lblStreet_CompDetail");
        lblStreet_GD.setName("lblStreet_GD");
        lblSubsidy.setName("lblSubsidy");
        lblTDate.setName("lblTDate");
        lblTo.setName("lblTo");
        lblToAmt.setName("lblToAmt");
        lblToDate.setName("lblToDate");
        lblTotalBaseAmt.setName("lblTotalBaseAmt");
        lblTotalInstallAmt.setName("lblTotalInstallAmt");
        lblTypeFacility.setName("lblTypeFacility");
        lblTypeOfFacility.setName("lblTypeOfFacility");
        lblAccOpenDt.setName("lblAccOpenDt");
        
        lblRecommandByType.setName("lblRecommandByType");
        mbrTermLoan.setName("mbrTermLoan");
        panBorrowerTabCTable.setName("panBorrowerTabCTable");
        panBorrowerTabTools.setName("panBorrowerTabTools");
        panAccLimit.setName("panAccLimit");
        panTermLoan.setName("panTermLoan");
        //        panAccType.setName("panAccType");
        panAcc_IM.setName("panAcc_IM");
        panAcc_RS.setName("panAcc_RS");
        
        panBorrowCompanyDetails.setName("panBorrowCompanyDetails");
        panBorrowProfile.setName("panBorrowProfile");
        panSecurityDetails.setName("panSecurityDetails");
        panBorrowProfile_CustID.setName("panBorrowProfile_CustID");
        panBorrowProfile_CustName.setName("panBorrowProfile_CustName");
        panButton.setName("panButton");
        panButtons.setName("panButtons");
        panClassDetails.setName("panClassDetails");
        panClassDetails_Acc.setName("panClassDetails_Acc");
        panClassDetails_Details.setName("panClassDetails_Details");
        panShareMaintenance.setName("panShareMaintenance");
        panShareMaintenance_Table.setName("panShareMaintenance_Table");
        panCode.setName("panCode");
        panCode2.setName("panCode2");
        panCompanyDetails.setName("panCompanyDetails");
        panCompanyDetailsTrash.setName("panCompanyDetailsTrash");
        panCompanyDetails_Addr.setName("panCompanyDetails_Addr");
        panCompanyDetails_Company.setName("panCompanyDetails_Company");
        panDate.setName("panDate");
        panDemandPromssoryDate.setName("panDemandPromssoryDate");
        panDoAddSIs.setName("panDoAddSIs");
        panDocumentDetails.setName("panDocumentDetails");
        panFDAccount.setName("panFDAccount");
        panFDDate.setName("panFDDate");
        panFacilityProdID.setName("panFacilityProdID");
        panFacilityDetails.setName("panFacilityDetails");
        panFacilityDetails_Data.setName("panFacilityDetails_Data");
        panFacilityTools.setName("panFacilityTools");
        panGuaranAddr.setName("panGuaranAddr");
        panGuarantor.setName("panGuarantor");
        panGuarantorDetail_Detail.setName("panGuarantorDetail_Detail");
        panGuarantorDetails.setName("panGuarantorDetails");
        panGuarantorDetailsTable.setName("panGuarantorDetailsTable");
        panGuarantorInsuranceDetails.setName("panGuarantorInsuranceDetails");
        panInstall_RS.setName("panInstall_RS");
        panRepayment.setName("panRepayment");
        panInterMaintenance.setName("panInterMaintenance");
        panInterMaintenance_Acc.setName("panInterMaintenance_Acc");
        panInterMaintenance_Details.setName("panInterMaintenance_Details");
        panInterMaintenance_Table.setName("panInterMaintenance_Table");
        panInterest.setName("panInterest");
        panLimit.setName("panLimit");
        panMultiDisburseAllow.setName("panMultiDisburseAllow");
        panNatureInterest.setName("panNatureInterest");
        panPostDatedCheque.setName("panPostDatedCheque");
        panProd_CD.setName("panProd_CD");
        panProd_GD.setName("panProd_GD");
        panProd_IM.setName("panProd_IM");
        panProd_RS.setName("panProd_RS");
        panRepaymentCTable.setName("panRepaymentCTable");
        panRepaymentSchedule.setName("panRepaymentSchedule");
        panRepaymentSchedule_Details.setName("panRepaymentSchedule_Details");
        panRiskWeight.setName("panRiskWeight");
        panSanctionDetails.setName("panSanctionDetails");
        panSanctionDetails_Mode.setName("panSanctionDetails_Mode");
        panSanctionDetails_Sanction.setName("panSanctionDetails_Sanction");
        panSanctionDetails_Table.setName("panSanctionDetails_Table");
        panSanctionDetails_Upper.setName("panSanctionDetails_Upper");
        panSchedule_RS.setName("panSchedule_RS");
        panSecDetails.setName("panSecDetails");
        panSecurity.setName("panSecurity");
        panSecurityDetails_Acc.setName("panSecurityDetails_Acc");
        panSecurityDetails_FD.setName("panSecurityDetails_FD");
        panSecurityDetails_security.setName("panSecurityDetails_security");
        panSecurityNature.setName("panSecurityNature");
        panSecurityTable.setName("panSecurityTable");
        panSecurityTools.setName("panSecurityTools");
        panStatus.setName("panStatus");
        panSubsidy.setName("panSubsidy");
        panTableFields.setName("panTableFields");
        panTableFields_SD.setName("panTableFields_SD");
        panTable_IM.setName("panTable_IM");
        panTable_SD.setName("panTable_SD");
        panTable2_SD.setName("panTable2_SD");
        panToolBtns.setName("panToolBtns");
        panFacilityChkBoxes.setName("panFacilityChkBoxes");
        panTotalSecurity_Value.setName("panTotalSecurity_Value");
        rdoAccLimit_Main.setName("rdoAccLimit_Main");
        rdoAccLimit_Submit.setName("rdoAccLimit_Submit");
        //        rdoAccType_New.setName("rdoAccType_New");
        //        rdoAccType_Transfered.setName("rdoAccType_Transfered");
        rdoDoAddSIs_No.setName("rdoDoAddSIs_No");
        rdoDoAddSIs_Yes.setName("rdoDoAddSIs_Yes");
        rdoInterest_Compound.setName("rdoInterest_Compound");
        rdoInterest_Simple.setName("rdoInterest_Simple");
        rdoMultiDisburseAllow_No.setName("rdoMultiDisburseAllow_No");
        rdoMultiDisburseAllow_Yes.setName("rdoMultiDisburseAllow_Yes");
        rdoNatureInterest_NonPLR.setName("rdoNatureInterest_NonPLR");
        rdoNatureInterest_PLR.setName("rdoNatureInterest_PLR");
        rdoPostDatedCheque_No.setName("rdoPostDatedCheque_No");
        rdoPostDatedCheque_Yes.setName("rdoPostDatedCheque_Yes");
        rdoRiskWeight_No.setName("rdoRiskWeight_No");
        rdoRiskWeight_Yes.setName("rdoRiskWeight_Yes");
        rdoSecurityDetails_Fully.setName("rdoSecurityDetails_Fully");
        rdoSecurityDetails_Partly.setName("rdoSecurityDetails_Partly");
        rdoSecurityDetails_Unsec.setName("rdoSecurityDetails_Unsec");
        rdoSubsidy_No.setName("rdoSubsidy_No");
        rdoSubsidy_Yes.setName("rdoSubsidy_Yes");
        sptBorroewrProfile.setName("sptBorroewrProfile");
        sptClassDetails.setName("sptClassDetails");
        sptClassification_vertical.setName("sptClassification_vertical");
        sptCompanyDetails.setName("sptCompanyDetails");
        sptFacilityDetails_Vert.setName("sptFacilityDetails_Vert");
        sptGuarantorDetail_Hori1.setName("sptGuarantorDetail_Hori1");
        sptGuarantorDetail_Vert.setName("sptGuarantorDetail_Vert");
        sptInterMaintenance_Hori.setName("sptInterMaintenance_Hori");
        sptInterMaintenance_Hori2.setName("sptInterMaintenance_Hori2");
        sptRepatmentSchedule_Hori.setName("sptRepatmentSchedule_Hori");
        sptRepatmentSchedule_Vert.setName("sptRepatmentSchedule_Vert");
        sptSecurityDetails_Hori.setName("sptSecurityDetails_Hori");
        srpGuarantorTable.setName("srpGuarantorTable");
        srpInterMaintenance.setName("srpInterMaintenance");
        srpBorrowerTabCTable.setName("srpBorrowerTabCTable");
        srpRepaymentCTable.setName("srpRepaymentCTable");
        srpSecurityTable.setName("srpSecurityTable");
        srpTable_SD.setName("srpTable_SD");
        srpTable2_SD.setName("srpTable2_SD");
        tabLimitAmount.setName("tabLimitAmount");
        tblBorrowerTabCTable.setName("tblBorrowerTabCTable");
        tblGuarantorTable.setName("tblGuarantorTable");
        tblInterMaintenance.setName("tblInterMaintenance");
        tblRepaymentCTable.setName("tblRepaymentCTable");
        tblSanctionDetails.setName("tblSanctionDetails");
        tblSanctionDetails2.setName("tblSanctionDetails2");
        tblShareMaintenance.setName("tblShareMaintenance");
        tdtAODDate.setName("tdtAODDate");
        tdtAsOn.setName("tdtAsOn");
        tdtAsOn_GD.setName("tdtAsOn_GD");
        tdtCreditFacilityAvailSince.setName("tdtCreditFacilityAvailSince");
        tdtDateEstablishment.setName("tdtDateEstablishment");
        tdtDealingWithBankSince.setName("tdtDealingWithBankSince");
        tdtDemandPromNoteDate.setName("tdtDemandPromNoteDate");
        tdtDemandPromNoteExpDate.setName("tdtDemandPromNoteExpDate");
        tdtFDate.setName("tdtFDate");
        tdtFirstInstall.setName("tdtFirstInstall");
        tdtFrom.setName("tdtFrom");
        tdtFromDate.setName("tdtFromDate");
        tdtLastInstall.setName("tdtLastInstall");
        tdtNPADate.setName("tdtNPADate");
        tdtSanctionDate.setName("tdtSanctionDate");
        tdtTDate.setName("tdtTDate");
        tdtTo.setName("tdtTo");
        tdtToDate.setName("tdtToDate");
        txtAgainstClearingInter.setName("txtAgainstClearingInter");
        tdtDOB_GD.setName("tdtDOB_GD");
        txtAmtLastInstall.setName("txtAmtLastInstall");
        txtAmtPenulInstall.setName("txtAmtPenulInstall");
        txtArea_CompDetail.setName("txtArea_CompDetail");
        txtArea_GD.setName("txtArea_GD");
        txtChiefExecutiveName.setName("txtChiefExecutiveName");
        txtCompanyRegisNo.setName("txtCompanyRegisNo");
        txtContactPerson.setName("txtContactPerson");
        txtContactPhone.setName("txtContactPhone");
        txtCustID.setName("txtCustID");
        txtCustomerID_GD.setName("txtCustomerID_GD");
        txtFromAmt.setName("txtFromAmt");
        txtGroupDesc.setName("txtGroupDesc");
        txtGuaranAccNo.setName("txtGuaranAccNo");
        txtGuarantorNo.setName("txtGuarantorNo");
        txtGuaranName.setName("txtGuaranName");
        txtGuarantorNetWorth.setName("txtGuarantorNetWorth");
        txtInter.setName("txtInter");
        txtInterExpLimit.setName("txtInterExpLimit");
        txtLaonAmt.setName("txtLaonAmt");
        txtPenalStatement.setName("txtPenalStatement");
        txtLimit_SD.setName("txtLimit_SD");
        txtNetWorth.setName("txtNetWorth");
        txtNoInstall.setName("txtNoInstall");
        txtNoInstallments.setName("txtNoInstallments");
        txtNoMonthsMora.setName("txtNoMonthsMora");
        txtPenalInter.setName("txtPenalInter");
        txtPhone_CompDetail.setName("txtPhone_CompDetail");
        txtPhone_GD.setName("txtPhone_GD");
        txtPin_CompDetail.setName("txtPin_CompDetail");
        txtPin_GD.setName("txtPin_GD");
        txtPurposeDesc.setName("txtPurposeDesc");
        txtReferences.setName("txtReferences");
        txtRemarks.setName("txtRemarks");
        txtRemarks__CompDetail.setName("txtRemarks__CompDetail");
        txtRiskRating.setName("txtRiskRating");
        txtSanctionNo.setName("txtSanctionNo");
        txtSanctionSlNo.setName("txtSanctionSlNo");
        txtSanctionRemarks.setName("txtSanctionRemarks");
        txtScheduleNo.setName("txtScheduleNo");
        txtSecurityNo.setName("txtSecurityNo");
        txtSecurityValue.setName("txtSecurityValue");
        txtStreet_CompDetail.setName("txtStreet_CompDetail");
        txtStreet_GD.setName("txtStreet_GD");
        txtToAmt.setName("txtToAmt");
        txtTotalBaseAmt.setName("txtTotalBaseAmt");
        txtTotalInstallAmt.setName("txtTotalInstallAmt");
        tdtFacility_Repay_Date.setName("tdtFacility_Repay_Date");
        chkMoratorium_Given.setName("chkMoratorium_Given");
        txtFacility_Moratorium_Period.setName("txtFacility_Moratorium_Period");
        lblFacility_Repay_Date.setName("lblFacility_Repay_Date");
        lblMoratorium_Given.setName("lblMoratorium_Given");
        lblFacility_Moratorium_Period.setName("lblFacility_Moratorium_Period");
        cboOpModeAI.setName("cboOpModeAI");
        cboSettlementModeAI.setName("cboSettlementModeAI");
        cboStmtFreqAD.setName("cboStmtFreqAD");
        chkABBChrgAD.setName("chkABBChrgAD");
        chkATMAD.setName("chkATMAD");
        chkChequeBookAD.setName("chkChequeBookAD");
        chkChequeRetChrgAD.setName("chkChequeRetChrgAD");
        chkCreditAD.setName("chkCreditAD");
        chkCustGrpLimitValidationAD.setName("chkCustGrpLimitValidationAD");
        chkDebitAD.setName("chkDebitAD");
        chkInopChrgAD.setName("chkInopChrgAD");
        chkMobileBankingAD.setName("chkMobileBankingAD");
        chkNPAChrgAD.setName("chkNPAChrgAD");
        chkNROStatusAD.setName("chkNROStatusAD");
        chkNonMainMinBalChrgAD.setName("chkNonMainMinBalChrgAD");
        chkPayIntOnCrBalIN.setName("chkPayIntOnCrBalIN");
        chkPayIntOnDrBalIN.setName("chkPayIntOnDrBalIN");
        chkStmtChrgAD.setName("chkStmtChrgAD");
        chkStopPmtChrgAD.setName("chkStopPmtChrgAD");
        lblABB.setName("lblABB");
        lblABBChrgAD.setName("lblABBChrgAD");
        lblATMFromDateAD.setName("lblATMFromDateAD");
        lblATMNoAD.setName("lblATMNoAD");
        lblATMToDateAD.setName("lblATMToDateAD");
        lblAccCloseChrgAD.setName("lblAccCloseChrgAD");
        lblAccOpeningChrgAD.setName("lblAccOpeningChrgAD");
        lblAgClearingIN.setName("lblAgClearingIN");
        lblAgClearingValueIN.setName("lblAgClearingValueIN");
        lblChequeBookChrgAD.setName("lblChequeBookChrgAD");
        lblChequeReturn.setName("lblChequeReturn");
        lblCollectInoperative.setName("lblCollectInoperative");
        lblCrInterestRateIN.setName("lblCrInterestRateIN");
        lblCrInterestRateValueIN.setName("lblCrInterestRateValueIN");
        lblCredit.setName("lblCredit");
        lblCreditFromDateAD.setName("lblCreditFromDateAD");
        lblCreditNoAD.setName("lblCreditNoAD");
        lblCreditToDateAD.setName("lblCreditToDateAD");
        lblDebit.setName("lblDebit");
        lblDebitFromDateAD.setName("lblDebitFromDateAD");
        lblDebitNoAD.setName("lblDebitNoAD");
        lblDebitToDateAD.setName("lblDebitToDateAD");
        lblDrInterestRateIN.setName("lblDrInterestRateIN");
        lblDrInterestRateValueIN.setName("lblDrInterestRateValueIN");
        lblExcessWithChrgAD.setName("lblExcessWithChrgAD");
        lblFolioChrgAD.setName("lblFolioChrgAD");
        lblMinActBalanceAD.setName("lblMinActBalanceAD");
        lblMisServiceChrgAD.setName("lblMisServiceChrgAD");
        lblNPA.setName("lblNPA");
        lblNPAChrgAD.setName("lblNPAChrgAD");
        lblNonMaintenance.setName("lblNonMaintenance");
        lblOpModeAI.setName("lblOpModeAI");
        lblPenalInterestRateIN.setName("lblPenalInterestRateIN");
        lblPenalInterestValueIN.setName("lblPenalInterestValueIN");
        lblRateCodeIN.setName("lblRateCodeIN");
        lblRateCodeValueIN.setName("lblRateCodeValueIN");
        lblSettlementModeAI.setName("lblSettlementModeAI");
        lblStatement.setName("lblStatement");
        lblStmtFreqAD.setName("lblStmtFreqAD");
        lblStopPayment.setName("lblStopPayment");
        panAccountDetails.setName("panAccountDetails");
        panCardInfo.setName("panCardInfo");
        panDiffCharges.setName("panDiffCharges");
        panFlexiOpt.setName("panFlexiOpt");
        panInterestPayableIN.setName("panInterestPayableIN");
        panIsRequired.setName("panIsRequired");
        panLastIntApp.setName("panLastIntApp");
        panRatesIN.setName("panRatesIN");
        tdtATMFromDateAD.setName("tdtATMFromDateAD");
        tdtATMToDateAD.setName("tdtATMToDateAD");
        tdtCredit.setName("tdtCredit");
        tdtCreditFromDateAD.setName("tdtCreditFromDateAD");
        tdtCreditToDateAD.setName("tdtCreditToDateAD");
        tdtDebit.setName("tdtDebit");
        tdtDebitFromDateAD.setName("tdtDebitFromDateAD");
        tdtAccountOpenDate.setName("tdtAccountOpenDate");
        tdtDebitToDateAD.setName("tdtDebitToDateAD");
        tdtNPAChrgAD.setName("tdtNPAChrgAD");
        txtABBChrgAD.setName("txtABBChrgAD");
        txtATMNoAD.setName("txtATMNoAD");
        txtAccCloseChrgAD.setName("txtAccCloseChrgAD");
        txtAccOpeningChrgAD.setName("txtAccOpeningChrgAD");
        txtChequeBookChrgAD.setName("txtChequeBookChrgAD");
        txtCreditNoAD.setName("txtCreditNoAD");
        txtDebitNoAD.setName("txtDebitNoAD");
        txtExcessWithChrgAD.setName("txtExcessWithChrgAD");
        txtFolioChrgAD.setName("txtFolioChrgAD");
        txtMinActBalanceAD.setName("txtMinActBalanceAD");
        txtMisServiceChrgAD.setName("txtMisServiceChrgAD");
        
    }
    
    
    private void internationalize() {
        lblRepayScheduleMode.setText(resourceBundle.getString("lblRepayScheduleMode"));
        lblAcct_Name.setText(resourceBundle.getString("lblAcct_Name"));
        lblExecuteDate_DOC.setText(resourceBundle.getString("lblExecuteDate_DOC"));
        lblExecuted_DOC.setText(resourceBundle.getString("lblExecuted_DOC"));
        lblExpiryDate_DOC.setText(resourceBundle.getString("lblExpiryDate_DOC"));
        lblMandatory_DOC.setText(resourceBundle.getString("lblMandatory_DOC"));
        rdoYes_Executed_DOC.setText(resourceBundle.getString("rdoYes_Executed_DOC"));
        rdoNo_Executed_DOC.setText(resourceBundle.getString("rdoNo_Executed_DOC"));
        rdoYes_Mandatory_DOC.setText(resourceBundle.getString("rdoYes_Mandatory_DOC"));
        rdoNo_Mandatory_DOC.setText(resourceBundle.getString("rdoNo_Mandatory_DOC"));
        lblProdID_Disp_ODetails.setText(resourceBundle.getString("lblProdID_Disp_ODetails"));
        lblProdID_ODetails.setText(resourceBundle.getString("lblProdID_ODetails"));
        lblAcctHead_Disp_ODetails.setText(resourceBundle.getString("lblAcctHead_Disp_ODetails"));
        lblAcctHead_ODetails.setText(resourceBundle.getString("lblAcctHead_ODetails"));
        lblAcctNo_Disp_ODetails.setText(resourceBundle.getString("lblAcctNo_Disp_ODetails"));
        lblAcctNo_ODetails.setText(resourceBundle.getString("lblAcctNo_ODetails"));
        lblEligibleLoan.setText(resourceBundle.getString("lblEligibleLoan"));
        lblMargin.setText(resourceBundle.getString("lblMargin"));
        lblDisbursement_Dt.setText(resourceBundle.getString("lblDisbursement_Dt"));
        lblPeriodDifference.setText(resourceBundle.getString("lblPeriodDifference"));
        lblPeriodDifference_Days.setText(resourceBundle.getString("lblPeriodDifference_Days"));
        lblPeriodDifference_Months.setText(resourceBundle.getString("lblPeriodDifference_Months"));
        lblPeriodDifference_Years.setText(resourceBundle.getString("lblPeriodDifference_Years"));
        lblGuarantorNo.setText(resourceBundle.getString("lblGuarantorNo"));
        lblQIS.setText(resourceBundle.getString("lblQIS"));
        lblFDate.setText(resourceBundle.getString("lblFDate"));
        lblIntGetFrom.setText(resourceBundle.getString("lblIntGetFrom"));
        btnSecurityDelete.setText(resourceBundle.getString("btnSecurityDelete"));
        rdoMultiDisburseAllow_No.setText(resourceBundle.getString("rdoMultiDisburseAllow_No"));
        lblAccStatus.setText(resourceBundle.getString("lblAccStatus"));
        lblAccLimit.setText(resourceBundle.getString("lblAccLimit"));
        lblCustName.setText(resourceBundle.getString("lblCustName"));
        chkPrioritySector.setText(resourceBundle.getString("chkPrioritySector"));
        lblPhone_BorrowerProfile.setText(resourceBundle.getString("lblPhone_BorrowerProfile"));
        lblArea_GD.setText(resourceBundle.getString("lblArea_GD"));
        lblScheduleNo.setText(resourceBundle.getString("lblScheduleNo"));
        lblRefinancingInsti.setText(resourceBundle.getString("lblRefinancingInsti"));
        lblInterExpLimit.setText(resourceBundle.getString("lblInterExpLimit"));
        lblConstitution_GD.setText(resourceBundle.getString("lblConstitution_GD"));
        lblAccHead_IM.setText(resourceBundle.getString("lblAccHead_IM"));
        btnInterestMaintenanceNew.setText(resourceBundle.getString("btnInterestMaintenanceNew"));
        lblCity_BorrowerProfile_2.setText(resourceBundle.getString("lblCity_BorrowerProfile_2"));
        chkDocumentcomplete.setText(resourceBundle.getString("chkDocumentcomplete"));
        btnInterestMaintenanceSave.setText(resourceBundle.getString("btnInterestMaintenanceSave"));
        lblContactPhone.setText(resourceBundle.getString("lblContactPhone"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        lblAccHead_GD.setText(resourceBundle.getString("lblAccHead_GD"));
        rdoPostDatedCheque_No.setText(resourceBundle.getString("rdoPostDatedCheque_No"));
        lblRiskRating.setText(resourceBundle.getString("lblRiskRating"));
        lblAgainstClearingInter.setText(resourceBundle.getString("lblAgainstClearingInter"));
        lblPhone_BorrowerProfile_2.setText(resourceBundle.getString("lblPhone_BorrowerProfile_2"));
        lblCountry_GD.setText(resourceBundle.getString("lblCountry_GD"));
        lblCustID.setText(resourceBundle.getString("lblCustID"));
        lblAccNo_IM.setText(resourceBundle.getString("lblAccNo_IM"));
        btnNew1.setText(resourceBundle.getString("btnNew1"));
        lblDateEstablishment.setText(resourceBundle.getString("lblDateEstablishment"));
        lblRepayFreq_Repayment.setText(resourceBundle.getString("lblRepayFreq_Repayment"));
        lblPenalInter.setText(resourceBundle.getString("lblPenalInter"));
        lblLastInstall.setText(resourceBundle.getString("lblLastInstall"));
        lblTotalBaseAmt.setText(resourceBundle.getString("lblTotalBaseAmt"));
        lblAccHead_RS_2.setText(resourceBundle.getString("lblAccHead_RS_2"));
        lblAccountHead_FD_Disp.setText(resourceBundle.getString("lblAccountHead_FD_Disp"));
        lblLimitAmt.setText(resourceBundle.getString("lblLimitAmt"));
        lblAmtLastInstall.setText(resourceBundle.getString("lblAmtLastInstall"));
        lblFax_BorrowerProfile.setText(resourceBundle.getString("lblFax_BorrowerProfile"));
        lblAccHead_2.setText(resourceBundle.getString("lblAccHead_2"));
        lblAcctNo_Sanction_Disp.setText(resourceBundle.getString("lblAcctNo_Sanction_Disp"));
        lblAcctNo_Sanction.setText(resourceBundle.getString("lblAcctNo_Sanction"));
        lblAcctNo_FD_Disp.setText(resourceBundle.getString("lblAcctNo_FD_Disp"));
        lblAcctNo_FD.setText(resourceBundle.getString("lblAcctNo_FD"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblDemandPromNoteExpDate.setText(resourceBundle.getString("lblDemandPromNoteExpDate"));
        chkECGC.setText(resourceBundle.getString("chkECGC"));
        lblSanctionDate.setText(resourceBundle.getString("lblSanctionDate"));
        lblPenalStatement.setText(resourceBundle.getString("lblPenalStatement"));
        rdoInterest_Simple.setText(resourceBundle.getString("rdoInterest_Simple"));
        btnSave1.setText(resourceBundle.getString("btnSave1"));
        lblCustName_Security.setText(resourceBundle.getString("lblCustName_Security"));
        lblCustName_Security_Display.setText(resourceBundle.getString("lblCustName_Security_Display"));
        lblDemandPromNoteDate.setText(resourceBundle.getString("lblDemandPromNoteDate"));
        rdoAccLimit_Submit.setText(resourceBundle.getString("rdoAccLimit_Submit"));
        lblChiefExecutiveName.setText(resourceBundle.getString("lblChiefExecutiveName"));
        lblCommodityCode.setText(resourceBundle.getString("lblCommodityCode"));
        lblPin_GD.setText(resourceBundle.getString("lblPin_GD"));
        lblPhone_GD.setText(resourceBundle.getString("lblPhone_GD"));
        btnInterestMaintenanceDelete.setText(resourceBundle.getString("btnInterestMaintenanceDelete"));
        lblECGC.setText(resourceBundle.getString("lblECGC"));
        lblToAmt.setText(resourceBundle.getString("lblToAmt"));
        ((javax.swing.border.TitledBorder)panCompanyDetailsTrash.getBorder()).setTitle(resourceBundle.getString("panCompanyDetailsTrash"));
        lblExpiryDate.setText(resourceBundle.getString("lblExpiryDate"));
        lblDistrictCode.setText(resourceBundle.getString("lblDistrictCode"));
        lblOpenDate2.setText(resourceBundle.getString("lblOpenDate2"));
        //        lblAccType.setText(resourceBundle.getString("lblAccType"));
        lblRepayFreq.setText(resourceBundle.getString("lblRepayFreq"));
        lblFromAmt.setText(resourceBundle.getString("lblFromAmt"));
        lblExpiryDate_2.setText(resourceBundle.getString("lblExpiryDate_2"));
        chkDirectFinance.setText(resourceBundle.getString("chkDirectFinance"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblAccNoSec.setText(resourceBundle.getString("lblAccNoSec"));
        lblSanctionNo1.setText(resourceBundle.getString("lblSanctionNo1"));
        lblSanctionSlNo.setText(resourceBundle.getString("lblSanctionSlNo"));
        lblHealthCode.setText(resourceBundle.getString("lblHealthCode"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblPostDatedCheque.setText(resourceBundle.getString("lblPostDatedCheque"));
        chkGurantor.setText(resourceBundle.getString("chkGurantor"));
        lblTypeOfFacility.setText(resourceBundle.getString("lblTypeOfFacility"));
        ((javax.swing.border.TitledBorder)panSecurityDetails_FD.getBorder()).setTitle(resourceBundle.getString("panSecurityDetails_FD"));
        btnSecurityNew.setText(resourceBundle.getString("btnSecurityNew"));
        lblPin_BorrowerProfile_2.setText(resourceBundle.getString("lblPin_BorrowerProfile_2"));
        lblOpenDate.setText(resourceBundle.getString("lblOpenDate"));
        lblSanctionNo2.setText(resourceBundle.getString("lblSanctionNo2"));
        lblTo.setText(resourceBundle.getString("lblTo"));
        lblTotalInstallAmt.setText(resourceBundle.getString("lblTotalInstallAmt"));
        lblProdID_RS.setText(resourceBundle.getString("lblProdID_RS"));
        lblProductID_FD.setText(resourceBundle.getString("lblProductID_FD"));
        lblProductID_FD_Disp.setText(resourceBundle.getString("lblProductID_FD_Disp"));
        lblIndusCode.setText(resourceBundle.getString("lblIndusCode"));
        lblGuarantorNetWorth.setText(resourceBundle.getString("lblGuarantorNetWorth"));
        lblAODDate.setText(resourceBundle.getString("lblAODDate"));
        chkInsurance.setText(resourceBundle.getString("chkInsurance"));
        lblCreditFacilityAvailSince.setText(resourceBundle.getString("lblCreditFacilityAvailSince"));
        ((javax.swing.border.TitledBorder)panBorrowProfile.getBorder()).setTitle(resourceBundle.getString("panBorrowProfile"));
        //        ((javax.swing.border.TitledBorder)panSecurityDetails.getBorder()).setTitle(resourceBundle.getString("panSecurityDetails"));
        lblLaonAmt.setText(resourceBundle.getString("lblLaonAmt"));
        lblGuaranAccNo.setText(resourceBundle.getString("lblGuaranAccNo"));
        lblCity_GD.setText(resourceBundle.getString("lblCity_GD"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnGuarantorNew.setText(resourceBundle.getString("btnGuarantorNew"));
        lblAccHead_IM_2.setText(resourceBundle.getString("lblAccHead_IM_2"));
        //        rdoAccType_New.setText(resourceBundle.getString("rdoAccType_New"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSanctionNo.setText(resourceBundle.getString("lblSanctionNo"));
        lblAssetCode.setText(resourceBundle.getString("lblAssetCode"));
        lblInterest.setText(resourceBundle.getString("lblInterest"));
        lblSecurityNo.setText(resourceBundle.getString("lblSecurityNo"));
        lblNatureInterest.setText(resourceBundle.getString("lblNatureInterest"));
        rdoSubsidy_No.setText(resourceBundle.getString("rdoSubsidy_No"));
        lblState_GD.setText(resourceBundle.getString("lblState_GD"));
        lblProdId1.setText(resourceBundle.getString("lblProdId1"));
        lblPurposeCode.setText(resourceBundle.getString("lblPurposeCode"));
        lblAccHead_CD_2.setText(resourceBundle.getString("lblAccHead_CD_2"));
        lblCountry_CompDetail.setText(resourceBundle.getString("lblCountry_CompDetail"));
        btnDelete1.setText(resourceBundle.getString("btnDelete1"));
        btnDelete2_SD.setText(resourceBundle.getString("btnDelete2_SD"));
        btnFacilityDelete.setText(resourceBundle.getString("btnFacilityDelete"));
        lblStreet_GD.setText(resourceBundle.getString("lblStreet_GD"));
        lblProID_CD.setText(resourceBundle.getString("lblProID_CD"));
        btnCustID.setText(resourceBundle.getString("btnCustID"));
        btnCustID_Security.setText(resourceBundle.getString("btnCustID_Security"));
        btnSecurityNo_Security.setText(resourceBundle.getString("btnSecurityNo_Security"));
        lblRiskWeight.setText(resourceBundle.getString("lblRiskWeight"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        //        rdoAccType_Transfered.setText(resourceBundle.getString("rdoAccType_Transfered"));
        lblAddressType.setText(resourceBundle.getString("lblAddressType"));
        lblAccNo_RS_2.setText(resourceBundle.getString("lblAccNo_RS_2"));
        rdoNatureInterest_PLR.setText(resourceBundle.getString("rdoNatureInterest_PLR"));
        lblNoMonthsMora.setText(resourceBundle.getString("lblNoMonthsMora"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblLimit_SD.setText(resourceBundle.getString("lblLimit_SD"));
        lblSancDate.setText(resourceBundle.getString("lblSancDate"));
        lblSubsidy.setText(resourceBundle.getString("lblSubsidy"));
        lblPin_BorrowerProfile.setText(resourceBundle.getString("lblPin_BorrowerProfile"));
        lblBorrowerNo.setText(resourceBundle.getString("lblBorrowerNo"));
        lblSanctionDate1.setText(resourceBundle.getString("lblSanctionDate1"));
        lblModeSanction.setText(resourceBundle.getString("lblModeSanction"));
        lblAmtPenulInstall.setText(resourceBundle.getString("lblAmtPenulInstall"));
        rdoSecurityDetails_Unsec.setText(resourceBundle.getString("rdoSecurityDetails_Unsec"));
        lblDocumentcomplete.setText(resourceBundle.getString("lblDocumentcomplete"));
        lblAccNo_GD_2.setText(resourceBundle.getString("lblAccNo_GD_2"));
        lblWeakerSectionCode.setText(resourceBundle.getString("lblWeakerSectionCode"));
        lblTDate.setText(resourceBundle.getString("lblTDate"));
        lblCity_CompDetail.setText(resourceBundle.getString("lblCity_CompDetail"));
        lblPLR_Limit.setText(resourceBundle.getString("lblPLR_Limit"));
        lblSecurityValue.setText(resourceBundle.getString("lblSecurityValue"));
        btnGuarantorSave.setText(resourceBundle.getString("btnGuarantorSave"));
        lblSanctioningAuthority.setText(resourceBundle.getString("lblSanctioningAuthority"));
        lblAccNo_CD_2.setText(resourceBundle.getString("lblAccNo_CD_2"));
        lblFrom.setText(resourceBundle.getString("lblFrom"));
        lblDoAddSIs.setText(resourceBundle.getString("lblDoAddSIs"));
        btnCustomerID_GD.setText(resourceBundle.getString("btnCustomerID_GD"));
        btnAccNo.setText(resourceBundle.getString("btnAccNo"));
        lblAsOn_GD.setText(resourceBundle.getString("lblAsOn_GD"));
        lblAccNo_IM_2.setText(resourceBundle.getString("lblAccNo_IM_2"));
        lblNetWorth.setText(resourceBundle.getString("lblNetWorth"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAccHead.setText(resourceBundle.getString("lblAccHead"));
        lblNoInstallments.setText(resourceBundle.getString("lblNoInstallments"));
        rdoSubsidy_Yes.setText(resourceBundle.getString("rdoSubsidy_Yes"));
        lblBlank1.setText(resourceBundle.getString("lblBlank1"));
        lblAccNo_CD.setText(resourceBundle.getString("lblAccNo_CD"));
        lblLimitAmt_2.setText(resourceBundle.getString("lblLimitAmt_2"));
        lblSancDate_2.setText(resourceBundle.getString("lblSancDate_2"));
        lblPrioritySector.setText(resourceBundle.getString("lblPrioritySector"));
        lblState_CompDetail.setText(resourceBundle.getString("lblState_CompDetail"));
        lblInter.setText(resourceBundle.getString("lblInter"));
        lblBorrowerNo_2.setText(resourceBundle.getString("lblBorrowerNo_2"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        rdoRiskWeight_No.setText(resourceBundle.getString("rdoRiskWeight_No"));
        lblFirstInstall.setText(resourceBundle.getString("lblFirstInstall"));
        lblPhone_CompDetail.setText(resourceBundle.getString("lblPhone_CompDetail"));
        lblRepayType.setText(resourceBundle.getString("lblRepayType"));
        lblSanctionDate2.setText(resourceBundle.getString("lblSanctionDate2"));
        lblGroupDesc.setText(resourceBundle.getString("lblGroupDesc"));
        lblAccHead_CD.setText(resourceBundle.getString("lblAccHead_CD"));
        lblSectorCode1.setText(resourceBundle.getString("lblSectorCode1"));
        rdoDoAddSIs_No.setText(resourceBundle.getString("rdoDoAddSIs_No"));
        lblGuaranName.setText(resourceBundle.getString("lblGuaranName"));
        rdoPostDatedCheque_Yes.setText(resourceBundle.getString("rdoPostDatedCheque_Yes"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblCustomerID_GD.setText(resourceBundle.getString("lblCustomerID_GD"));
        lblProdID_IM.setText(resourceBundle.getString("lblProdID_IM"));
        lblDirectFinance.setText(resourceBundle.getString("lblDirectFinance"));
        lblAccHeadSec.setText(resourceBundle.getString("lblAccHeadSec"));
        lblAccNo_RS.setText(resourceBundle.getString("lblAccNo_RS"));
        btnSecuritySave.setText(resourceBundle.getString("btnSecuritySave"));
        lblCompanyRegisNo.setText(resourceBundle.getString("lblCompanyRegisNo"));
        lblDealingWithBankSince.setText(resourceBundle.getString("lblDealingWithBankSince"));
        lblTypeFacility.setText(resourceBundle.getString("lblTypeFacility"));
        chkQIS.setText(resourceBundle.getString("chkQIS"));
        lblInterestType.setText(resourceBundle.getString("lblInterestType"));
        rdoAccLimit_Main.setText(resourceBundle.getString("rdoAccLimit_Main"));
        lblConstitution.setText(resourceBundle.getString("lblConstitution"));
        lblRemark_FD.setText(resourceBundle.getString("lblRemark_FD"));
        lblAccHead_RS.setText(resourceBundle.getString("lblAccHead_RS"));
        lblAccountHead_FD.setText(resourceBundle.getString("lblAccountHead_FD"));
        rdoSecurityDetails_Fully.setText(resourceBundle.getString("rdoSecurityDetails_Fully"));
        rdoSecurityDetails_Partly.setText(resourceBundle.getString("rdoSecurityDetails_Partly"));
        lblArea_CompDetail.setText(resourceBundle.getString("lblArea_CompDetail"));
        btnGuarantorDelete.setText(resourceBundle.getString("btnGuarantorDelete"));
        lbl20Code.setText(resourceBundle.getString("lbl20Code"));
        lblContactPerson.setText(resourceBundle.getString("lblContactPerson"));
        lblState_BorrowerProfile.setText(resourceBundle.getString("lblState_BorrowerProfile"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        rdoDoAddSIs_Yes.setText(resourceBundle.getString("rdoDoAddSIs_Yes"));
        lblReferences.setText(resourceBundle.getString("lblReferences"));
        rdoRiskWeight_Yes.setText(resourceBundle.getString("rdoRiskWeight_Yes"));
        rdoNatureInterest_NonPLR.setText(resourceBundle.getString("rdoNatureInterest_NonPLR"));
        lblAccNoSec_2.setText(resourceBundle.getString("lblAccNoSec_2"));
        lblCity_BorrowerProfile.setText(resourceBundle.getString("lblCity_BorrowerProfile"));
        lblFax_BorrowerProfile_2.setText(resourceBundle.getString("lblFax_BorrowerProfile_2"));
        chkStockInspect.setText(resourceBundle.getString("chkStockInspect"));
        lblGuaranteeCoverCode.setText(resourceBundle.getString("lblGuaranteeCoverCode"));
        lblPin_CompDetail.setText(resourceBundle.getString("lblPin_CompDetail"));
        lblNoInstall.setText(resourceBundle.getString("lblNoInstall"));
        lblMultiDisburseAllow.setText(resourceBundle.getString("lblMultiDisburseAllow"));
        rdoMultiDisburseAllow_Yes.setText(resourceBundle.getString("rdoMultiDisburseAllow_Yes"));
        lblRemarks__CompDetail.setText(resourceBundle.getString("lblRemarks__CompDetail"));
        lblNatureBusiness.setText(resourceBundle.getString("lblNatureBusiness"));
        lblStreet_CompDetail.setText(resourceBundle.getString("lblStreet_CompDetail"));
        lblCustName_2.setText(resourceBundle.getString("lblCustName_2"));
        lblProdID_GD.setText(resourceBundle.getString("lblProdID_GD"));
        
        
        lblAccNo_GD.setText(resourceBundle.getString("lblAccNo_GD"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        ((javax.swing.border.TitledBorder)panDemandPromssoryDate.getBorder()).setTitle(resourceBundle.getString("panDemandPromssoryDate"));
        lblDOB_GD.setText(resourceBundle.getString("lblDOB_GD"));
        lblState_BorrowerProfile_2.setText(resourceBundle.getString("lblState_BorrowerProfile_2"));
        lblNPADate.setText(resourceBundle.getString("lblNPADate"));
        lblAsOn.setText(resourceBundle.getString("lblAsOn"));
        lblAccHeadSec_2.setText(resourceBundle.getString("lblAccHeadSec_2"));
        lblPLR_Limit_2.setText(resourceBundle.getString("lblPLR_Limit_2"));
        lblCustID_Security.setText(resourceBundle.getString("lblCustID_Security"));
        rdoInterest_Compound.setText(resourceBundle.getString("rdoInterest_Compound"));
        
        lblAccHead_GD_2.setText(resourceBundle.getString("lblAccHead_GD_2"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblPurposeDesc.setText(resourceBundle.getString("lblPurposeDesc"));
        lblGovtSchemeCode.setText(resourceBundle.getString("lblGovtSchemeCode"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnNew_Borrower.setText(resourceBundle.getString("btnNew_Borrower"));
        btnToMain_Borrower.setText(resourceBundle.getString("btnToMain_Borrower"));
        btnDeleteBorrower.setText(resourceBundle.getString("btnDelete_Borrower"));
        btnRepayment_Delete.setText(resourceBundle.getString("btnRepayment_Delete"));
        btnRepayment_New.setText(resourceBundle.getString("btnRepayment_New"));
        btnRepayment_Save.setText(resourceBundle.getString("btnRepayment_Save"));
        lblFacility_Repay_Date.setText(resourceBundle.getString("lblFacility_Repay_Date"));
        lblMoratorium_Given.setText(resourceBundle.getString("lblMoratorium_Given"));
        lblFacility_Moratorium_Period.setText(resourceBundle.getString("lblFacility_Moratorium_Period"));
        
        lblPenalInterestValueIN.setText(resourceBundle.getString("lblPenalInterestValueIN"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        
        chkNonMainMinBalChrgAD.setText(resourceBundle.getString("chkNonMainMinBalChrgAD"));
        lblAgClearingIN.setText(resourceBundle.getString("lblAgClearingIN"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblCrInterestRateIN.setText(resourceBundle.getString("lblCrInterestRateIN"));
        lblFolioChrgAD.setText(resourceBundle.getString("lblFolioChrgAD"));
        lblMinActBalanceAD.setText(resourceBundle.getString("lblMinActBalanceAD"));
        ((javax.swing.border.TitledBorder)panLastIntApp.getBorder()).setTitle(resourceBundle.getString("panLastIntApp"));
        lblPenalInterestRateIN.setText(resourceBundle.getString("lblPenalInterestRateIN"));
        lblStatement.setText(resourceBundle.getString("lblStatement"));
        chkChequeRetChrgAD.setText(resourceBundle.getString("chkChequeRetChrgAD"));
        lblDebit.setText(resourceBundle.getString("lblDebit"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblATMToDateAD.setText(resourceBundle.getString("lblATMToDateAD"));
        lblCredit.setText(resourceBundle.getString("lblCredit"));
        lblNPA.setText(resourceBundle.getString("lblNPA"));
        lblATMNoAD.setText(resourceBundle.getString("lblATMNoAD"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        ((javax.swing.border.TitledBorder)panFlexiOpt.getBorder()).setTitle(resourceBundle.getString("panFlexiOpt"));
        chkABBChrgAD.setText(resourceBundle.getString("chkABBChrgAD"));
        chkATMAD.setText(resourceBundle.getString("chkATMAD"));
        chkNPAChrgAD.setText(resourceBundle.getString("chkNPAChrgAD"));
        chkStmtChrgAD.setText(resourceBundle.getString("chkStmtChrgAD"));
        lblABBChrgAD.setText(resourceBundle.getString("lblABBChrgAD"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblNPAChrgAD.setText(resourceBundle.getString("lblNPAChrgAD"));
        chkCreditAD.setText(resourceBundle.getString("chkCreditAD"));
        lblDebitToDateAD.setText(resourceBundle.getString("lblDebitToDateAD"));
        lblChequeBookChrgAD.setText(resourceBundle.getString("lblChequeBookChrgAD"));
        lblCreditToDateAD.setText(resourceBundle.getString("lblCreditToDateAD"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblDebitNoAD.setText(resourceBundle.getString("lblDebitNoAD"));
        chkChequeBookAD.setText(resourceBundle.getString("chkChequeBookAD"));
        lblRateCodeValueIN.setText(resourceBundle.getString("lblRateCodeValueIN"));
        lblDebitFromDateAD.setText(resourceBundle.getString("lblDebitFromDateAD"));
        lblExcessWithChrgAD.setText(resourceBundle.getString("lblExcessWithChrgAD"));
        chkCustGrpLimitValidationAD.setText(resourceBundle.getString("chkCustGrpLimitValidationAD"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblAgClearingValueIN.setText(resourceBundle.getString("lblAgClearingValueIN"));
        ((javax.swing.border.TitledBorder)panInterestPayableIN.getBorder()).setTitle(resourceBundle.getString("panInterestPayableIN"));
        lblCollectInoperative.setText(resourceBundle.getString("lblCollectInoperative"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        chkPayIntOnCrBalIN.setText(resourceBundle.getString("chkPayIntOnCrBalIN"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSettlementModeAI.setText(resourceBundle.getString("lblSettlementModeAI"));
        ((javax.swing.border.TitledBorder)panIsRequired.getBorder()).setTitle(resourceBundle.getString("panIsRequired"));
        lblNonMaintenance.setText(resourceBundle.getString("lblNonMaintenance"));
        chkNROStatusAD.setText(resourceBundle.getString("chkNROStatusAD"));
        lblStmtFreqAD.setText(resourceBundle.getString("lblStmtFreqAD"));
        lblDrInterestRateIN.setText(resourceBundle.getString("lblDrInterestRateIN"));
        lblMisServiceChrgAD.setText(resourceBundle.getString("lblMisServiceChrgAD"));
        lblABB.setText(resourceBundle.getString("lblABB"));
        lblAccOpeningChrgAD.setText(resourceBundle.getString("lblAccOpeningChrgAD"));
        lblCreditFromDateAD.setText(resourceBundle.getString("lblCreditFromDateAD"));
        lblDrInterestRateValueIN.setText(resourceBundle.getString("lblDrInterestRateValueIN"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblCrInterestRateValueIN.setText(resourceBundle.getString("lblCrInterestRateValueIN"));
        chkMobileBankingAD.setText(resourceBundle.getString("chkMobileBankingAD"));
        lblATMFromDateAD.setText(resourceBundle.getString("lblATMFromDateAD"));
        chkPayIntOnDrBalIN.setText(resourceBundle.getString("chkPayIntOnDrBalIN"));
        lblAccCloseChrgAD.setText(resourceBundle.getString("lblAccCloseChrgAD"));
        chkInopChrgAD.setText(resourceBundle.getString("chkInopChrgAD"));
        lblRateCodeIN.setText(resourceBundle.getString("lblRateCodeIN"));
        lblOpModeAI.setText(resourceBundle.getString("lblOpModeAI"));
        ((javax.swing.border.TitledBorder)panDiffCharges.getBorder()).setTitle(resourceBundle.getString("panDiffCharges"));
        ((javax.swing.border.TitledBorder)panCardInfo.getBorder()).setTitle(resourceBundle.getString("panCardInfo"));
        ((javax.swing.border.TitledBorder)panRatesIN.getBorder()).setTitle(resourceBundle.getString("panRatesIN"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        lblCreditNoAD.setText(resourceBundle.getString("lblCreditNoAD"));
        lblStopPayment.setText(resourceBundle.getString("lblStopPayment"));
        chkStopPmtChrgAD.setText(resourceBundle.getString("chkStopPmtChrgAD"));
        chkDebitAD.setText(resourceBundle.getString("chkDebitAD"));
        lblChequeReturn.setText(resourceBundle.getString("lblChequeReturn"));
        lblCustName.setText(resourceBundle.getString("lblCustName"));
        lblAccOpenDt.setText(resourceBundle.getString("lblAccOpenDt"));
        
        lblRecommandByType.setText(resourceBundle.getString("lblRecommandByType"));
    }
    
    private void removeRadioButtons() {
        removeFacilityRadioBtns();
        removeRepaymentRadioBtns();
        removeDocumentRadioBtns();
    }
    
    private void removeFacilityRadioBtns(){
        removeFacilitySecurityRadioBtns();
        //        rdoAccType.remove(rdoAccType_New);
        //        rdoAccType.remove(rdoAccType_Transfered);
        rdoAccLimit.remove(rdoAccLimit_Main);
        rdoAccLimit.remove(rdoAccLimit_Submit);
        rdoRiskWeight.remove(rdoRiskWeight_No);
        rdoRiskWeight.remove(rdoRiskWeight_Yes);
        rdoMultiDisburseAllow.remove(rdoMultiDisburseAllow_No);
        rdoMultiDisburseAllow.remove(rdoMultiDisburseAllow_Yes);
        removeFacilityInterestNature();
        removeFacilitySubsidy();
        rdoInterest.remove(rdoInterest_Compound);
        rdoInterest.remove(rdoInterest_Simple);
    }
    private void removeFacilitySecurityRadioBtns(){
        rdoSecurityDetails.remove(rdoSecurityDetails_Fully);
        rdoSecurityDetails.remove(rdoSecurityDetails_Partly);
        rdoSecurityDetails.remove(rdoSecurityDetails_Unsec);
    }
    private void removeFacilitySubsidy(){
        rdoSubsidy.remove(rdoSubsidy_No);
        rdoSubsidy.remove(rdoSubsidy_Yes);
    }
    private void removeFacilityInterestNature(){
        rdoNatureInterest.remove(rdoNatureInterest_NonPLR);
        rdoNatureInterest.remove(rdoNatureInterest_PLR);
    }
    private void removeRepaymentRadioBtns(){
        rdoDoAddSIs.remove(rdoDoAddSIs_No);
        rdoDoAddSIs.remove(rdoDoAddSIs_Yes);
        
        rdoPostDatedCheque.remove(rdoPostDatedCheque_Yes);
        rdoPostDatedCheque.remove(rdoPostDatedCheque_No);
        
        rdoStatus_Repayment.remove(rdoActive_Repayment);
        rdoStatus_Repayment.remove(rdoInActive_Repayment);
    }
    
    private void removeDocumentRadioBtns(){
        removeDocSubmittRadioBtns();
        removeDocExecuteRadioBtns();
        removeDocMandatoryRadioBtns();
    }
    private void removeDocSubmittRadioBtns(){
        rdoIsSubmitted_DocumentDetails.remove(rdoYes_DocumentDetails);
        rdoIsSubmitted_DocumentDetails.remove(rdoNo_DocumentDetails);
    }
    private void removeDocExecuteRadioBtns(){
        rdoExecuted_DOC.remove(rdoYes_Executed_DOC);
        rdoExecuted_DOC.remove(rdoNo_Executed_DOC);
    }
    private void removeDocMandatoryRadioBtns(){
        rdoMandatory_DOC.remove(rdoYes_Mandatory_DOC);
        rdoMandatory_DOC.remove(rdoNo_Mandatory_DOC);
    }
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        lblStatus.setText(observable.getLblStatus());
        lblBorrowerNo_2.setText(observableBorrow.getLblBorrowerNo_2());
        lblOpenDate2.setText(observableBorrow.getLblOpenDate());
        lblCustName_2.setText(observableBorrow.getLblCustName());
        lblProdID_Disp_ODetails.setText(observableOtherDetails.getLblProdID_Disp_ODetails());
        lblAcctHead_Disp_ODetails.setText(observableOtherDetails.getLblAcctHead_Disp_ODetails());
        lblAcctNo_Disp_ODetails.setText(observableOtherDetails.getLblAcctNo_Disp_ODetails());
        lblAccHead_RS_2.setText(observableRepay.getLblAccHead_RS_2());
        lblAccountHead_FD_Disp.setText(observable.getLblAccountHead_FD_Disp());
        lblAccNo_RS_2.setText(observableRepay.getLblAccNo_RS_2());
        lblAccHead_GD_2.setText(observableGuarantor.getLblAccHead_GD_2());
        lblAccNo_GD_2.setText(observableGuarantor.getLblAccNo_GD_2());
        lblAccHead_IM_2.setText(observableInt.getLblAccHead_IM_2());
        lblAccNo_IM_2.setText(observableInt.getLblAccNo_IM_2());
        lblAccHead_CD_2.setText(observableClassi.getLblAccHead_CD_2());
        lblAccNo_CD_2.setText(observableClassi.getLblAccNo_CD_2());
        lblAcctNo_Sanction_Disp.setText(observable.getStrACNumber());
        lblAcctNo_FD_Disp.setText(observable.getStrACNumber());
        lblCity_BorrowerProfile_2.setText(observableBorrow.getLblCity());
        lblState_BorrowerProfile_2.setText(observableBorrow.getLblState());
        lblPin_BorrowerProfile_2.setText(observableBorrow.getLblPin());
        lblPhone_BorrowerProfile_2.setText(observableBorrow.getLblPhone());
        lblFax_BorrowerProfile_2.setText(observableBorrow.getLblFax());
        lblAccHead_2.setText(observable.getLblAccHead_2());
        lblLimitAmt_2.setText(observableInt.getLblLimitAmt_2());
        lblPLR_Limit_2.setText(observableInt.getLblPLR_Limit_2());
        lblSancDate_2.setText(observableInt.getLblSancDate_2());
        lblExpiryDate_2.setText(observableInt.getLblExpiryDate_2());
        lblSanctionNo2.setText(observableClassi.getLblSanctionNo2());
        lblSanctionDate2.setText(observableClassi.getLblSanctionDate2());
        txtCustID.setText(observableBorrow.getTxtCustID());
        cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
        cboCategory.setSelectedItem(observableBorrow.getCboCategory());
        txtReferences.setText(observableBorrow.getTxtReferences());
        txtCompanyRegisNo.setText(observableComp.getTxtCompanyRegisNo());
        tdtDateEstablishment.setDateValue(observableComp.getTdtDateEstablishment());
        tdtDealingWithBankSince.setDateValue(observableComp.getTdtDealingWithBankSince());
        txtRiskRating.setText(observableComp.getTxtRiskRating());
        cboNatureBusiness.setSelectedItem(observableComp.getCboNatureBusiness());
        txtRemarks__CompDetail.setText(observableComp.getTxtRemarks__CompDetail());
        txtNetWorth.setText(observableComp.getTxtNetWorth());
        tdtAsOn.setDateValue(observableComp.getTdtAsOn());
        tdtCreditFacilityAvailSince.setDateValue(observableComp.getTdtCreditFacilityAvailSince());
        txtChiefExecutiveName.setText(observableComp.getTxtChiefExecutiveName());
        cboAddressType.setSelectedItem(observableComp.getCboAddressType());
        txtStreet_CompDetail.setText(observableComp.getTxtStreet_CompDetail());
        txtArea_CompDetail.setText(observableComp.getTxtArea_CompDetail());
        cboCity_CompDetail.setSelectedItem(observableComp.getCboCity_CompDetail());
        cboState_CompDetail.setSelectedItem(observableComp.getCboState_CompDetail());
        cboCountry_CompDetail.setSelectedItem(observableComp.getCboCountry_CompDetail());
        txtPin_CompDetail.setText(observableComp.getTxtPin_CompDetail());
        txtPhone_CompDetail.setText(observableComp.getTxtPhone_CompDetail());
        cboAccStatus.setSelectedItem(observable.getCboAccStatus());
        txtSanctionNo.setText(observable.getTxtSanctionNo());
        txtSanctionSlNo.setText(observable.getTxtSanctionSlNo());
        tdtSanctionDate.setDateValue(observable.getTdtSanctionDate());
        cboSanctioningAuthority.setSelectedItem(observable.getCboSanctioningAuthority());
        txtSanctionRemarks.setText(observable.getTxtSanctionRemarks());
        cboModeSanction.setSelectedItem(observable.getCboModeSanction());
        txtNoInstallments.setText(observable.getTxtNoInstallments());
        txtPeriodDifference_Days.setText(observable.getTxtPeriodDifference_Days());
        txtPeriodDifference_Months.setText(observable.getTxtPeriodDifference_Months());
        txtPeriodDifference_Years.setText(observable.getTxtPeriodDifference_Years());
        //        cboRepayFreq.setSelectedItem(observable.getCboRepayFreq());
        tdtFacility_Repay_Date.setDateValue(observable.getTdtFacility_Repay_Date());
        txtFacility_Moratorium_Period.setText(observable.getTxtFacility_Moratorium_Period());
        chkMoratorium_Given.setSelected(observable.getChkMoratorium_Given());
        txtLimit_SD.setText(observable.getTxtLimit_SD());
        tdtFDate.setDateValue(observable.getTdtFDate());
        tdtTDate.setDateValue(observable.getTdtTDate());
        chkStockInspect.setSelected(observable.getChkStockInspect());
        chkInsurance.setSelected(observable.getChkInsurance());
        chkGurantor.setSelected(observable.getChkGurantor());
        rdoSecurityDetails_Unsec.setSelected(observable.getRdoSecurityDetails_Unsec());
        rdoSecurityDetails_Partly.setSelected(observable.getRdoSecurityDetails_Partly());
        rdoSecurityDetails_Fully.setSelected(observable.getRdoSecurityDetails_Fully());
        //        rdoAccType_New.setSelected(observable.getRdoAccType_New());
        //        rdoAccType_Transfered.setSelected(observable.getRdoAccType_Transfered());
        rdoAccLimit_Main.setSelected(observable.getRdoAccLimit_Main());
        rdoAccLimit_Submit.setSelected(observable.getRdoAccLimit_Submit());
        rdoNatureInterest_PLR.setSelected(observable.getRdoNatureInterest_PLR());
        rdoNatureInterest_NonPLR.setSelected(observable.getRdoNatureInterest_NonPLR());
        cboInterestType.setSelectedItem(observable.getCboInterestType());
        rdoRiskWeight_Yes.setSelected(observable.getRdoRiskWeight_Yes());
        rdoRiskWeight_No.setSelected(observable.getRdoRiskWeight_No());
        tdtDemandPromNoteDate.setDateValue(observable.getTdtDemandPromNoteDate());
        tdtDemandPromNoteExpDate.setDateValue(observable.getTdtDemandPromNoteExpDate());
        tdtAODDate.setDateValue(observable.getTdtAODDate());
        rdoMultiDisburseAllow_Yes.setSelected(observable.getRdoMultiDisburseAllow_Yes());
        rdoMultiDisburseAllow_No.setSelected(observable.getRdoMultiDisburseAllow_No());
        rdoSubsidy_Yes.setSelected(observable.getRdoSubsidy_Yes());
        rdoSubsidy_No.setSelected(observable.getRdoSubsidy_No());
        txtPurposeDesc.setText(observable.getTxtPurposeDesc());
        txtGroupDesc.setText(observable.getTxtGroupDesc());
        rdoInterest_Simple.setSelected(observable.getRdoInterest_Simple());
        rdoInterest_Compound.setSelected(observable.getRdoInterest_Compound());
        txtContactPerson.setText(observable.getTxtContactPerson());
        txtContactPhone.setText(observable.getTxtContactPhone());
        txtRemarks.setText(observable.getTxtRemarks());
        txtAcct_Name.setText(observable.getTxtAcct_Name());
        //        cboIntGetFrom.setSelectedItem(observable.getCboIntGetFrom());
        lblProdId_Disp.setText(observableSecurity.getLblProdId_Disp());
        lblAccHeadSec_2.setText(observableSecurity.getLblAccHeadSec_2());
        lblAccHeadSec_2.setText(observableSecurity.getLblAccHeadSec_2());
        lblAccNoSec_2.setText(observableSecurity.getLblAccNoSec_2());
        txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
        txtCustID_Security.setText(observableSecurity.getTxtCustID_Security());
        txtSecurityValue.setText(observableSecurity.getTxtSecurityValue());
        tdtFromDate.setDateValue(observableSecurity.getTdtFromDate());
        tdtToDate.setDateValue(observableSecurity.getTdtToDate());
        txtMargin.setText(observableSecurity.getTxtMargin());
        txtEligibleLoan.setText(observableSecurity.getTxtEligibleLoan());
        lblCustName_Security_Display.setText(observableSecurity.getLblCustName_Security_Display());
        lblProdID_RS_Disp.setText(observableRepay.getLblProdID_RS_Disp());
        lblProductID_FD_Disp.setText(observable.getLblProductID_FD_Disp());
        rdoActive_Repayment.setSelected(observableRepay.getRdoActive_Repayment());
        rdoInActive_Repayment.setSelected(observableRepay.getRdoInActive_Repayment());
        txtScheduleNo.setText(observableRepay.getTxtScheduleNo());
        txtLaonAmt.setText(observableRepay.getTxtLaonAmt());
        txtNoMonthsMora.setText(observableRepay.getTxtNoMonthsMora());
        tdtFirstInstall.setDateValue(observableRepay.getTdtFirstInstall());
        tdtLastInstall.setDateValue(observableRepay.getTdtLastInstall());
        txtTotalBaseAmt.setText(observableRepay.getTxtTotalBaseAmt());
        txtAmtPenulInstall.setText(observableRepay.getTxtAmtPenulInstall());
        txtAmtLastInstall.setText(observableRepay.getTxtAmtLastInstall());
        txtTotalInstallAmt.setText(observableRepay.getTxtTotalInstallAmt());
        rdoDoAddSIs_Yes.setSelected(observableRepay.getRdoDoAddSIs_Yes());
        rdoDoAddSIs_No.setSelected(observableRepay.getRdoDoAddSIs_No());
        rdoPostDatedCheque_Yes.setSelected(observableRepay.getRdoPostDatedCheque_Yes());
        rdoPostDatedCheque_No.setSelected(observableRepay.getRdoPostDatedCheque_No());
        txtNoInstall.setText(observableRepay.getTxtNoInstall());
        tdtDisbursement_Dt.setDateValue(observableRepay.getTdtDisbursement_Dt());
        txtRepayScheduleMode.setText(observableRepay.getTxtRepayScheduleMode());
        lblProdID_GD_Disp.setText(observableGuarantor.getLblProdID_GD_Disp());
        tdtDOB_GD.setDateValue(observableGuarantor.getTdtDOB_GD());
        txtCustomerID_GD.setText(observableGuarantor.getTxtCustomerID_GD());
        txtGuarantorNo.setText(observableGuarantor.getTxtGuarantorNo());
        txtGuaranAccNo.setText(observableGuarantor.getTxtGuaranAccNo());
        txtGuaranName.setText(observableGuarantor.getTxtGuaranName());
        txtStreet_GD.setText(observableGuarantor.getTxtStreet_GD());
        txtArea_GD.setText(observableGuarantor.getTxtArea_GD());
        cboCity_GD.setSelectedItem(observableGuarantor.getCboCity_GD());
        txtPin_GD.setText(observableGuarantor.getTxtPin_GD());
        cboState_GD.setSelectedItem(observableGuarantor.getCboState_GD());
        cboCountry_GD.setSelectedItem(observableGuarantor.getCboCountry_GD());
        txtPhone_GD.setText(observableGuarantor.getTxtPhone_GD());
        cboConstitution_GD.setSelectedItem(observableGuarantor.getCboConstitution_GD());
        txtGuarantorNetWorth.setText(observableGuarantor.getTxtGuarantorNetWorth());
        tdtAsOn_GD.setDateValue(observableGuarantor.getTdtAsOn_GD());
        cboProdType.setSelectedItem(observableGuarantor.getCboProdType());
        cboProdId.setSelectedItem(observableGuarantor.getCboProdId());
        lblProdID_Disp_DocumentDetails.setText(observableDocument.getLblProdID_Disp_DocumentDetails());
        lblAcctHead_Disp_DocumentDetails.setText(observableDocument.getLblAcctHead_Disp_DocumentDetails());
        lblAcctNo_Disp_DocumentDetails.setText(observableDocument.getLblAcctNo_Disp_DocumentDetails());
        lblDocDesc_Disp_DocumentDetails.setText(observableDocument.getLblDocDesc_Disp_DocumentDetails());
        lblDocNo_Disp_DocumentDetails.setText(observableDocument.getLblDocNo_Disp_DocumentDetails());
        lblDocType_Disp_DocumentDetails.setText(observableDocument.getLblDocType_Disp_DocumentDetails());
        txtRemarks_DocumentDetails.setText(observableDocument.getTxtRemarks_DocumentDetails());
        tdtSubmitDate_DocumentDetails.setDateValue(observableDocument.getTdtSubmitDate_DocumentDetails());
        rdoYes_DocumentDetails.setSelected(observableDocument.getRdoYes_DocumentDetails());
        rdoNo_DocumentDetails.setSelected(observableDocument.getRdoNo_DocumentDetails());
        rdoYes_Executed_DOC.setSelected(observableDocument.getRdoYes_Executed_DOC());
        rdoNo_Executed_DOC.setSelected(observableDocument.getRdoNo_Executed_DOC());
        rdoYes_Mandatory_DOC.setSelected(observableDocument.getRdoYes_Mandatory_DOC());
        rdoNo_Mandatory_DOC.setSelected(observableDocument.getRdoNo_Mandatory_DOC());
        tdtExecuteDate_DOC.setDateValue(observableDocument.getTdtExecuteDate_DOC());
        tdtExpiryDate_DOC.setDateValue(observableDocument.getTdtExpiryDate_DOC());
        lblProdID_IM_Disp.setText(observableInt.getLblProdID_IM_Disp());
        tdtFrom.setDateValue(observableInt.getTdtFrom());
        tdtTo.setDateValue(observableInt.getTdtTo());
        txtFromAmt.setText(observableInt.getTxtFromAmt());
        txtToAmt.setText(observableInt.getTxtToAmt());
        txtInter.setText(observableInt.getTxtInter());
        txtPenalInter.setText(observableInt.getTxtPenalInter());
        txtAgainstClearingInter.setText(observableInt.getTxtAgainstClearingInter());
        txtPenalStatement.setText(observableInt.getTxtPenalStatement());
        txtInterExpLimit.setText(observableInt.getTxtInterExpLimit());
        lblProID_CD_Disp.setText(observableClassi.getLblProdID_CD_Disp());
        cboCommodityCode.setSelectedItem(observableClassi.getCboCommodityCode());
        cboGuaranteeCoverCode.setSelectedItem(observableClassi.getCboGuaranteeCoverCode());
        cboSectorCode1.setSelectedItem(observableClassi.getCboSectorCode1());
        cboHealthCode.setSelectedItem(observableClassi.getCboHealthCode());
        cboTypeFacility.setSelectedItem(observableClassi.getCboTypeFacility());
        cboDistrictCode.setSelectedItem(observableClassi.getCboDistrictCode());
        cboPurposeCode.setSelectedItem(observableClassi.getCboPurposeCode());
        cboIndusCode.setSelectedItem(observableClassi.getCboIndusCode());
        cboWeakerSectionCode.setSelectedItem(observableClassi.getCboWeakerSectionCode());
        cbo20Code.setSelectedItem(observableClassi.getCbo20Code());
        cboRefinancingInsti.setSelectedItem(observableClassi.getCboRefinancingInsti());
        cboGovtSchemeCode.setSelectedItem(observableClassi.getCboGovtSchemeCode());
        cboAssetCode.setSelectedItem(observableClassi.getCboAssetCode());
        tdtNPADate.setDateValue(observableClassi.getTdtNPADate());
        chkDirectFinance.setSelected(observableClassi.getChkDirectFinance());
        chkECGC.setSelected(observableClassi.getChkECGC());
        chkPrioritySector.setSelected(observableClassi.getChkPrioritySector());
        chkDocumentcomplete.setSelected(observableClassi.getChkDocumentcomplete());
        chkQIS.setSelected(observableClassi.getChkQIS());
        chkChequeBookAD.setSelected(observableOtherDetails.getChkChequeBookAD());
        chkCustGrpLimitValidationAD.setSelected(observableOtherDetails.getChkCustGrpLimitValidationAD());
        chkMobileBankingAD.setSelected(observableOtherDetails.getChkMobileBankingAD());
        chkNROStatusAD.setSelected(observableOtherDetails.getChkNROStatusAD());
        chkATMAD.setSelected(observableOtherDetails.getChkATMAD());
        txtATMNoAD.setText(observableOtherDetails.getTxtATMNoAD());
        tdtATMFromDateAD.setDateValue(observableOtherDetails.getTdtATMFromDateAD());
        tdtATMToDateAD.setDateValue(observableOtherDetails.getTdtATMToDateAD());
        chkDebitAD.setSelected(observableOtherDetails.getChkDebitAD());
        txtDebitNoAD.setText(observableOtherDetails.getTxtDebitNoAD());
        tdtDebitFromDateAD.setDateValue(observableOtherDetails.getTdtDebitFromDateAD());
        tdtDebitToDateAD.setDateValue(observableOtherDetails.getTdtDebitToDateAD());
        chkCreditAD.setSelected(observableOtherDetails.getChkCreditAD());
        txtCreditNoAD.setText(observableOtherDetails.getTxtCreditNoAD());
        tdtCreditFromDateAD.setDateValue(observableOtherDetails.getTdtCreditFromDateAD());
        tdtCreditToDateAD.setDateValue(observableOtherDetails.getTdtCreditToDateAD());
        cboSettlementModeAI.setSelectedItem(observableOtherDetails.getCboSettlementModeAI());
        cboOpModeAI.setSelectedItem(observableOtherDetails.getCboOpModeAI());
        txtAccOpeningChrgAD.setText(observableOtherDetails.getTxtAccOpeningChrgAD());
        txtMisServiceChrgAD.setText(observableOtherDetails.getTxtMisServiceChrgAD());
        chkStopPmtChrgAD.setSelected(observableOtherDetails.getChkStopPmtChrgAD());
        txtChequeBookChrgAD.setText(observableOtherDetails.getTxtChequeBookChrgAD());
        chkChequeRetChrgAD.setSelected(observableOtherDetails.getChkChequeRetChrgAD());
        txtFolioChrgAD.setText(observableOtherDetails.getTxtFolioChrgAD());
        chkInopChrgAD.setSelected(observableOtherDetails.getChkInopChrgAD());
        txtAccCloseChrgAD.setText(observableOtherDetails.getTxtAccCloseChrgAD());
        chkStmtChrgAD.setSelected(observableOtherDetails.getChkStmtChrgAD());
        cboStmtFreqAD.setSelectedItem(observableOtherDetails.getCboStmtFreqAD());
        chkNonMainMinBalChrgAD.setSelected(observableOtherDetails.getChkNonMainMinBalChrgAD());
        txtExcessWithChrgAD.setText(observableOtherDetails.getTxtExcessWithChrgAD());
        chkABBChrgAD.setSelected(observableOtherDetails.getChkABBChrgAD());
        chkNPAChrgAD.setSelected(observableOtherDetails.getChkNPAChrgAD());
        txtABBChrgAD.setText(observableOtherDetails.getTxtABBChrgAD());
        tdtNPAChrgAD.setDateValue(observableOtherDetails.getTdtNPAChrgAD());
        txtMinActBalanceAD.setText(observableOtherDetails.getTxtMinActBalanceAD());
        tdtDebit.setDateValue(observableOtherDetails.getTdtDebit());
        tdtCredit.setDateValue(observableOtherDetails.getTdtCredit());
        chkPayIntOnCrBalIN.setSelected(observableOtherDetails.getChkPayIntOnCrBalIN());
        chkPayIntOnDrBalIN.setSelected(observableOtherDetails.getChkPayIntOnDrBalIN());
        lblRateCodeValueIN.setText(observableOtherDetails.getLblRateCodeValueIN());
        lblCrInterestRateValueIN.setText(observableOtherDetails.getLblCrInterestRateValueIN());
        lblDrInterestRateValueIN.setText(observableOtherDetails.getLblDrInterestRateValueIN());
        lblPenalInterestValueIN.setText(observableOtherDetails.getLblPenalInterestValueIN());
        lblAgClearingValueIN.setText(observableOtherDetails.getLblAgClearingValueIN());
        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
        tblSanctionDetails.setModel(observable.getTblSanctionFacility());
        tblSanctionDetails2.setModel(observable.getTblSanctionMain());
        tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
        tblRepaymentCTable.setModel(observableRepay.getTblRepaymentTab());
        tblGuarantorTable.setModel(observableGuarantor.getTblGuarantorTab());
        tblInterMaintenance.setModel(observableInt.getTblInterestTab());
        tblShareMaintenance.setModel(observable.getTblShare());
        tblTable_DocumentDetails.setModel(observableDocument.getTblDocumentTab());
        cboRecommendedByType.setSelectedItem(observable.getCboRecommendedByType());
        tdtAccountOpenDate.setDateValue(observable.getAccountOpenDate());
        //        cboRepayFreq_Repayment.setSelectedItem(observableRepay.getCboRepayFreq_Repayment());
        //        cboRepayType.setSelectedItem(observableRepay.getCboRepayType());
        addRadioButtons();
    }
    
    private void addRadioButtons(){
        addFacilityRadioBtns();
        addRepaymentRadioBtns();
        addDocumentRadioBtns();
    }
    
    private void addFacilityRadioBtns(){
        addFacilitySecurityRadioBtns();
        
        rdoAccType = new CButtonGroup();
        //        rdoAccType.add(rdoAccType_New);
        //        rdoAccType.add(rdoAccType_Transfered);
        
        rdoAccLimit = new CButtonGroup();
        rdoAccLimit.add(rdoAccLimit_Main);
        rdoAccLimit.add(rdoAccLimit_Submit);
        
        rdoRiskWeight = new CButtonGroup();
        rdoRiskWeight.add(rdoRiskWeight_No);
        rdoRiskWeight.add(rdoRiskWeight_Yes);
        
        rdoMultiDisburseAllow = new CButtonGroup();
        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_No);
        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_Yes);

        
        addFacilityInterestNatureBtns();
        
        addFacilitySubsidyRadioBtns();
        
        rdoInterest = new CButtonGroup();
        rdoInterest.add(rdoInterest_Compound);
        rdoInterest.add(rdoInterest_Simple);
        
    }
    
    private void addFacilitySecurityRadioBtns(){
        rdoSecurityDetails = new CButtonGroup();
        rdoSecurityDetails.add(rdoSecurityDetails_Fully);
        rdoSecurityDetails.add(rdoSecurityDetails_Partly);
        rdoSecurityDetails.add(rdoSecurityDetails_Unsec);
        
    }
    
    private void addFacilitySubsidyRadioBtns(){
        rdoSubsidy = new CButtonGroup();
        rdoSubsidy.add(rdoSubsidy_No);
        rdoSubsidy.add(rdoSubsidy_Yes);
    }
    private void addFacilityInterestNatureBtns(){
        rdoNatureInterest = new CButtonGroup();
        rdoNatureInterest.add(rdoNatureInterest_NonPLR);
        rdoNatureInterest.add(rdoNatureInterest_PLR);
    }
    private void addRepaymentRadioBtns(){
        rdoDoAddSIs = new  CButtonGroup();
        rdoDoAddSIs.add(rdoDoAddSIs_No);
        rdoDoAddSIs.add(rdoDoAddSIs_Yes);
        
        rdoPostDatedCheque = new CButtonGroup();
        rdoPostDatedCheque.add(rdoPostDatedCheque_Yes);
        rdoPostDatedCheque.add(rdoPostDatedCheque_No);
        
        rdoStatus_Repayment = new CButtonGroup();
        rdoStatus_Repayment.add(rdoActive_Repayment);
        rdoStatus_Repayment.add(rdoInActive_Repayment);
    }
    
    private void addDocumentRadioBtns(){
        addDocSubmittRadioBtns();
        addDocExecuteRadioBtns();
        addDocMandatoryRadioBtns();
    }
    private void addDocSubmittRadioBtns(){
        rdoIsSubmitted_DocumentDetails = new CButtonGroup();
        rdoIsSubmitted_DocumentDetails.add(rdoYes_DocumentDetails);
        rdoIsSubmitted_DocumentDetails.add(rdoNo_DocumentDetails);
    }
    private void addDocExecuteRadioBtns(){
        rdoExecuted_DOC = new CButtonGroup();
        rdoExecuted_DOC.add(rdoYes_Executed_DOC);
        rdoExecuted_DOC.add(rdoNo_Executed_DOC);
    }
    private void addDocMandatoryRadioBtns(){
        rdoMandatory_DOC = new CButtonGroup();
        rdoMandatory_DOC.add(rdoYes_Mandatory_DOC);
        rdoMandatory_DOC.add(rdoNo_Mandatory_DOC);
    }
    
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setSelectedBranchID(getSelectedBranchID());
        observableBorrow.setSelectedBranchID(getSelectedBranchID());
        observableClassi.setSelectedBranchID(getSelectedBranchID());
        observableComp.setSelectedBranchID(getSelectedBranchID());
        observableDocument.setSelectedBranchID(getSelectedBranchID());
        observableGuarantor.setSelectedBranchID(getSelectedBranchID());
        observableInt.setSelectedBranchID(getSelectedBranchID());
        observableOtherDetails.setSelectedBranchID(getSelectedBranchID());
        observableRepay.setSelectedBranchID(getSelectedBranchID());
        observableSecurity.setSelectedBranchID(getSelectedBranchID());
        observableBorrow.setTxtCustID(txtCustID.getText());
        observableBorrow.setCboConstitution(CommonUtil.convertObjToStr(cboConstitution.getSelectedItem()));
        observableBorrow.setCboCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
        observableBorrow.setTxtReferences(txtReferences.getText());
        observableComp.setTxtCompanyRegisNo(txtCompanyRegisNo.getText());
        observableComp.setTdtDateEstablishment(tdtDateEstablishment.getDateValue());
        observableComp.setTdtDealingWithBankSince(tdtDealingWithBankSince.getDateValue());
        observableComp.setTxtRiskRating(txtRiskRating.getText());
        observableComp.setCboNatureBusiness(CommonUtil.convertObjToStr(cboNatureBusiness.getSelectedItem()));
        observableComp.setTxtRemarks__CompDetail(txtRemarks__CompDetail.getText());
        observableComp.setTxtNetWorth(txtNetWorth.getText());
        observableComp.setTdtAsOn(tdtAsOn.getDateValue());
        observableComp.setTdtCreditFacilityAvailSince(tdtCreditFacilityAvailSince.getDateValue());
        observableComp.setTxtChiefExecutiveName(txtChiefExecutiveName.getText());
        observableComp.setCboAddressType(CommonUtil.convertObjToStr(cboAddressType.getSelectedItem()));
        observableComp.setTxtStreet_CompDetail(txtStreet_CompDetail.getText());
        observableComp.setTxtArea_CompDetail(txtArea_CompDetail.getText());
        observableComp.setCboCity_CompDetail(CommonUtil.convertObjToStr(cboCity_CompDetail.getSelectedItem()));
        observableComp.setCboState_CompDetail(CommonUtil.convertObjToStr(cboState_CompDetail.getSelectedItem()));
        observableComp.setCboCountry_CompDetail(CommonUtil.convertObjToStr(cboCountry_CompDetail.getSelectedItem()));
        observableComp.setTxtPin_CompDetail(txtPin_CompDetail.getText());
        observableComp.setTxtPhone_CompDetail(txtPhone_CompDetail.getText());
        authSignUI.updateOBFields();
        poaUI.updateOBFields();
        observable.setCboAccStatus(CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()));
        observable.setTxtSanctionNo(txtSanctionNo.getText());
        observable.setTxtSanctionSlNo(txtSanctionSlNo.getText());
        observable.setTdtSanctionDate(tdtSanctionDate.getDateValue());
        observable.setCboSanctioningAuthority(CommonUtil.convertObjToStr(cboSanctioningAuthority.getSelectedItem()));
        observable.setTxtSanctionRemarks(txtSanctionRemarks.getText());
        observable.setCboModeSanction(CommonUtil.convertObjToStr(cboModeSanction.getSelectedItem()));
        observable.setTxtNoInstallments(txtNoInstallments.getText());
        observable.setCboRepayFreq(CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()));
        observable.setCboTypeOfFacility(CommonUtil.convertObjToStr(cboTypeOfFacility.getSelectedItem()));
        observable.setTxtLimit_SD(txtLimit_SD.getText());
        observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue());
        observable.setTxtFacility_Moratorium_Period(txtFacility_Moratorium_Period.getText());
        observable.setChkMoratorium_Given(chkMoratorium_Given.isSelected());
        observable.setTdtFDate(tdtFDate.getDateValue());
        observable.setTdtTDate(tdtTDate.getDateValue());
        observable.setRdoSecurityDetails_Unsec(rdoSecurityDetails_Unsec.isSelected());
        observable.setRdoSecurityDetails_Partly(rdoSecurityDetails_Partly.isSelected());
        observable.setRdoSecurityDetails_Fully(rdoSecurityDetails_Fully.isSelected());
        observable.setChkStockInspect(chkStockInspect.isSelected());
        observable.setChkInsurance(chkInsurance.isSelected());
        observable.setChkGurantor(chkGurantor.isSelected());
        observable.setAccountOpenDate(tdtAccountOpenDate.getDateValue());
        observable.setCboRecommendedByType(CommonUtil.convertObjToStr(cboRecommendedByType.getSelectedItem()));
        //        observable.setRdoAccType_New(rdoAccType_New.isSelected());
        //        observable.setRdoAccType_Transfered(rdoAccType_Transfered.isSelected());
        observable.setRdoAccLimit_Main(rdoAccLimit_Main.isSelected());
        observable.setRdoAccLimit_Submit(rdoAccLimit_Submit.isSelected());
        observable.setRdoNatureInterest_PLR(rdoNatureInterest_PLR.isSelected());
        observable.setRdoNatureInterest_NonPLR(rdoNatureInterest_NonPLR.isSelected());
        observable.setCboInterestType(CommonUtil.convertObjToStr(cboInterestType.getSelectedItem()));
        observable.setRdoRiskWeight_Yes(rdoRiskWeight_Yes.isSelected());
        observable.setRdoRiskWeight_No(rdoRiskWeight_No.isSelected());
        observable.setTdtDemandPromNoteDate(tdtDemandPromNoteDate.getDateValue());
        observable.setTdtDemandPromNoteExpDate(tdtDemandPromNoteExpDate.getDateValue());
        observable.setTdtAODDate(tdtAODDate.getDateValue());
        observable.setRdoMultiDisburseAllow_Yes(rdoMultiDisburseAllow_Yes.isSelected());
        observable.setRdoMultiDisburseAllow_No(rdoMultiDisburseAllow_No.isSelected());
        observable.setRdoSubsidy_Yes(rdoSubsidy_Yes.isSelected());
        observable.setRdoSubsidy_No(rdoSubsidy_No.isSelected());
        observable.setTxtPurposeDesc(txtPurposeDesc.getText());
        observable.setTxtGroupDesc(txtGroupDesc.getText());
        observable.setRdoInterest_Simple(rdoInterest_Simple.isSelected());
        observable.setRdoInterest_Compound(rdoInterest_Compound.isSelected());
        observable.setTxtContactPerson(txtContactPerson.getText());
        observable.setTxtContactPhone(txtContactPhone.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setCboIntGetFrom(CommonUtil.convertObjToStr(cboIntGetFrom.getSelectedItem()));
        observable.setTxtAcct_Name(txtAcct_Name.getText());
        observableSecurity.setLblProdId_Disp(lblProdId_Disp.getText());
        observableSecurity.setTxtSecurityNo(txtSecurityNo.getText());
        observableSecurity.setTxtCustID_Security(txtCustID_Security.getText());
        observableSecurity.setTxtSecurityValue(txtSecurityValue.getText());
        observableSecurity.setTdtFromDate(tdtFromDate.getDateValue());
        observableSecurity.setTdtToDate(tdtToDate.getDateValue());
        observableSecurity.setTxtMargin(txtMargin.getText());
        observableSecurity.setTxtEligibleLoan(txtEligibleLoan.getText());
        observableSecurity.setLblCustName_Security_Display(lblCustName_Security_Display.getText());
        observableSecurity.setTblSecurityTab((com.see.truetransact.clientutil.EnhancedTableModel)tblSecurityTable.getModel());
        observableRepay.setLblProdID_RS_Disp(lblProdID_RS_Disp.getText());
        observable.setLblProductID_FD_Disp(lblProductID_FD_Disp.getText());
        observableRepay.setTxtScheduleNo(txtScheduleNo.getText());
        observableRepay.setTxtLaonAmt(txtLaonAmt.getText());
        observableRepay.setCboRepayFreq_Repayment(CommonUtil.convertObjToStr(cboRepayFreq_Repayment.getSelectedItem()));
        observableRepay.setCboRepayType(CommonUtil.convertObjToStr(cboRepayType.getSelectedItem()));
        observableRepay.setTdtFirstInstall(tdtFirstInstall.getDateValue());
        observableRepay.setTdtLastInstall(tdtLastInstall.getDateValue());
        observableRepay.setTxtTotalBaseAmt(txtTotalBaseAmt.getText());
        observableRepay.setTxtAmtPenulInstall(txtAmtPenulInstall.getText());
        observableRepay.setTxtAmtLastInstall(txtAmtLastInstall.getText());
        observableRepay.setTxtTotalInstallAmt(txtTotalInstallAmt.getText());
        observableRepay.setRdoDoAddSIs_Yes(rdoDoAddSIs_Yes.isSelected());
        observableRepay.setRdoDoAddSIs_No(rdoDoAddSIs_No.isSelected());
        observableRepay.setRdoPostDatedCheque_Yes(rdoPostDatedCheque_Yes.isSelected());
        observableRepay.setRdoPostDatedCheque_No(rdoPostDatedCheque_No.isSelected());
        observableRepay.setRdoActive_Repayment(rdoActive_Repayment.isSelected());
        observableRepay.setRdoInActive_Repayment(rdoInActive_Repayment.isSelected());
        observableRepay.setTxtNoInstall(txtNoInstall.getText());
        observableRepay.setTxtRepayScheduleMode(txtRepayScheduleMode.getText());
        observableRepay.setTdtDisbursement_Dt(tdtDisbursement_Dt.getDateValue());
        observableRepay.setTblRepaymentTab((com.see.truetransact.clientutil.EnhancedTableModel)tblRepaymentCTable.getModel());
        observableGuarantor.setTxtGuarantorNo(txtGuarantorNo.getText());
        observableGuarantor.setLblProdID_GD_Disp(lblProdID_GD_Disp.getText());
        observableGuarantor.setTdtDOB_GD(tdtDOB_GD.getDateValue());
        observableGuarantor.setTxtCustomerID_GD(txtCustomerID_GD.getText());
        observableGuarantor.setTxtGuaranAccNo(txtGuaranAccNo.getText());
        observableGuarantor.setTxtGuaranName(txtGuaranName.getText());
        observableGuarantor.setTxtStreet_GD(txtStreet_GD.getText());
        observableGuarantor.setTxtArea_GD(txtArea_GD.getText());
        observableGuarantor.setCboCity_GD(CommonUtil.convertObjToStr(cboCity_GD.getSelectedItem()));
        observableGuarantor.setTxtPin_GD(txtPin_GD.getText());
        observableGuarantor.setCboState_GD(CommonUtil.convertObjToStr(cboState_GD.getSelectedItem()));
        observableGuarantor.setCboCountry_GD(CommonUtil.convertObjToStr(cboCountry_GD.getSelectedItem()));
        observableGuarantor.setTxtPhone_GD(txtPhone_GD.getText());
        observableGuarantor.setCboConstitution_GD(CommonUtil.convertObjToStr(cboConstitution_GD.getSelectedItem()));
        observableGuarantor.setTxtGuarantorNetWorth(txtGuarantorNetWorth.getText());
        observableGuarantor.setTdtAsOn_GD(tdtAsOn_GD.getDateValue());
        observableGuarantor.setCboProdId(CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
        observableGuarantor.setCboProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        observableDocument.setLblProdID_Disp_DocumentDetails(lblProdID_Disp_DocumentDetails.getText());
        observableDocument.setLblAcctHead_Disp_DocumentDetails(lblAcctHead_Disp_DocumentDetails.getText());
        observableDocument.setLblAcctNo_Disp_DocumentDetails(lblAcctNo_Disp_DocumentDetails.getText());
        observableDocument.setLblDocDesc_Disp_DocumentDetails(lblDocDesc_Disp_DocumentDetails.getText());
        observableDocument.setLblDocNo_Disp_DocumentDetails(lblDocNo_Disp_DocumentDetails.getText());
        observableDocument.setLblDocType_Disp_DocumentDetails(lblDocType_Disp_DocumentDetails.getText());
        observableDocument.setTxtRemarks_DocumentDetails(txtRemarks_DocumentDetails.getText());
        observableDocument.setTdtSubmitDate_DocumentDetails(tdtSubmitDate_DocumentDetails.getDateValue());
        observableDocument.setRdoYes_DocumentDetails(rdoYes_DocumentDetails.isSelected());
        observableDocument.setRdoNo_DocumentDetails(rdoNo_DocumentDetails.isSelected());
        observableDocument.setRdoYes_Executed_DOC(rdoYes_Executed_DOC.isSelected());
        observableDocument.setRdoNo_Executed_DOC(rdoNo_Executed_DOC.isSelected());
        observableDocument.setRdoYes_Mandatory_DOC(rdoYes_Mandatory_DOC.isSelected());
        observableDocument.setRdoNo_Mandatory_DOC(rdoNo_Mandatory_DOC.isSelected());
        observableDocument.setTdtExecuteDate_DOC(tdtExecuteDate_DOC.getDateValue());
        observableDocument.setTdtExpiryDate_DOC(tdtExpiryDate_DOC.getDateValue());
        observableInt.setLblProdID_IM_Disp(lblProdID_IM_Disp.getText());
        observableInt.setTdtFrom(tdtFrom.getDateValue());
        observableInt.setTdtTo(tdtTo.getDateValue());
        observableInt.setTxtFromAmt(txtFromAmt.getText());
        observableInt.setTxtToAmt(txtToAmt.getText());
        observableInt.setTxtInter(txtInter.getText());
        observableInt.setTxtPenalInter(txtPenalInter.getText());
        observableInt.setTxtAgainstClearingInter(txtAgainstClearingInter.getText());
        observableInt.setTxtPenalStatement(txtPenalStatement.getText());
        observableInt.setTxtInterExpLimit(txtInterExpLimit.getText());
        observableClassi.setLblProdID_CD_Disp(CommonUtil.convertObjToStr(lblProID_CD_Disp.getText()));
        observableClassi.setCboCommodityCode(CommonUtil.convertObjToStr(cboCommodityCode.getSelectedItem()));
        observableClassi.setCboGuaranteeCoverCode(CommonUtil.convertObjToStr(cboGuaranteeCoverCode.getSelectedItem()));
        observableClassi.setCboSectorCode1(CommonUtil.convertObjToStr(cboSectorCode1.getSelectedItem()));
        observableClassi.setCboHealthCode(CommonUtil.convertObjToStr(cboHealthCode.getSelectedItem()));
        observableClassi.setCboTypeFacility(CommonUtil.convertObjToStr(cboTypeFacility.getSelectedItem()));
        observableClassi.setCboDistrictCode(CommonUtil.convertObjToStr(cboDistrictCode.getSelectedItem()));
        observableClassi.setCboPurposeCode(CommonUtil.convertObjToStr(cboPurposeCode.getSelectedItem()));
        observableClassi.setCboIndusCode(CommonUtil.convertObjToStr(cboIndusCode.getSelectedItem()));
        observableClassi.setCboWeakerSectionCode(CommonUtil.convertObjToStr(cboWeakerSectionCode.getSelectedItem()));
        observableClassi.setCbo20Code(CommonUtil.convertObjToStr(cbo20Code.getSelectedItem()));
        observableClassi.setCboRefinancingInsti(CommonUtil.convertObjToStr(cboRefinancingInsti.getSelectedItem()));
        observableClassi.setCboGovtSchemeCode(CommonUtil.convertObjToStr(cboGovtSchemeCode.getSelectedItem()));
        observableClassi.setCboAssetCode(CommonUtil.convertObjToStr(cboAssetCode.getSelectedItem()));
        observableClassi.setTdtNPADate(tdtNPADate.getDateValue());
        observableClassi.setChkDirectFinance(chkDirectFinance.isSelected());
        observableClassi.setChkECGC(chkECGC.isSelected());
        observableClassi.setChkPrioritySector(chkPrioritySector.isSelected());
        observableClassi.setChkDocumentcomplete(chkDocumentcomplete.isSelected());
        observableClassi.setChkQIS(chkQIS.isSelected());
        observableOtherDetails.setChkChequeBookAD(chkChequeBookAD.isSelected());
        observableOtherDetails.setChkCustGrpLimitValidationAD(chkCustGrpLimitValidationAD.isSelected());
        observableOtherDetails.setChkMobileBankingAD(chkMobileBankingAD.isSelected());
        observableOtherDetails.setChkNROStatusAD(chkNROStatusAD.isSelected());
        observableOtherDetails.setChkATMAD(chkATMAD.isSelected());
        observableOtherDetails.setTxtATMNoAD(txtATMNoAD.getText());
        observableOtherDetails.setTdtATMFromDateAD(tdtATMFromDateAD.getDateValue());
        observableOtherDetails.setTdtATMToDateAD(tdtATMToDateAD.getDateValue());
        observableOtherDetails.setChkDebitAD(chkDebitAD.isSelected());
        observableOtherDetails.setTxtDebitNoAD(txtDebitNoAD.getText());
        observableOtherDetails.setTdtDebitFromDateAD(tdtDebitFromDateAD.getDateValue());
        observableOtherDetails.setTdtDebitToDateAD(tdtDebitToDateAD.getDateValue());
        observableOtherDetails.setChkCreditAD(chkCreditAD.isSelected());
        observableOtherDetails.setTxtCreditNoAD(txtCreditNoAD.getText());
        observableOtherDetails.setTdtCreditFromDateAD(tdtCreditFromDateAD.getDateValue());
        observableOtherDetails.setTdtCreditToDateAD(tdtCreditToDateAD.getDateValue());
        observableOtherDetails.setCboSettlementModeAI(CommonUtil.convertObjToStr(cboSettlementModeAI.getSelectedItem()));
        observableOtherDetails.setCboOpModeAI(CommonUtil.convertObjToStr(cboOpModeAI.getSelectedItem()));
        observableOtherDetails.setTxtAccOpeningChrgAD(txtAccOpeningChrgAD.getText());
        observableOtherDetails.setTxtMisServiceChrgAD(txtMisServiceChrgAD.getText());
        observableOtherDetails.setChkStopPmtChrgAD(chkStopPmtChrgAD.isSelected());
        observableOtherDetails.setTxtChequeBookChrgAD(txtChequeBookChrgAD.getText());
        observableOtherDetails.setChkChequeRetChrgAD(chkChequeRetChrgAD.isSelected());
        observableOtherDetails.setTxtFolioChrgAD(txtFolioChrgAD.getText());
        observableOtherDetails.setChkInopChrgAD(chkInopChrgAD.isSelected());
        observableOtherDetails.setTxtAccCloseChrgAD(txtAccCloseChrgAD.getText());
        observableOtherDetails.setChkStmtChrgAD(chkStmtChrgAD.isSelected());
        observableOtherDetails.setCboStmtFreqAD(CommonUtil.convertObjToStr(cboStmtFreqAD.getSelectedItem()));
        observableOtherDetails.setChkNonMainMinBalChrgAD(chkNonMainMinBalChrgAD.isSelected());
        observableOtherDetails.setTxtExcessWithChrgAD(txtExcessWithChrgAD.getText());
        observableOtherDetails.setChkABBChrgAD(chkABBChrgAD.isSelected());
        observableOtherDetails.setChkNPAChrgAD(chkNPAChrgAD.isSelected());
        observableOtherDetails.setTxtABBChrgAD(txtABBChrgAD.getText());
        observableOtherDetails.setTdtNPAChrgAD(tdtNPAChrgAD.getDateValue());
        observableOtherDetails.setTxtMinActBalanceAD(txtMinActBalanceAD.getText());
        observableOtherDetails.setTdtDebit(tdtDebit.getDateValue());
        observableOtherDetails.setTdtCredit(tdtCredit.getDateValue());
        observableOtherDetails.setChkPayIntOnCrBalIN(chkPayIntOnCrBalIN.isSelected());
        observableOtherDetails.setChkPayIntOnDrBalIN(chkPayIntOnDrBalIN.isSelected());
        observableOtherDetails.setLblRateCodeValueIN(lblRateCodeValueIN.getText());
        observableOtherDetails.setLblCrInterestRateValueIN(lblCrInterestRateValueIN.getText());
        observableOtherDetails.setLblDrInterestRateValueIN(lblDrInterestRateValueIN.getText());
        observableOtherDetails.setLblPenalInterestValueIN(lblPenalInterestValueIN.getText());
        observableOtherDetails.setLblAgClearingValueIN(lblAgClearingValueIN.getText());
        observable.setLblStatus(lblStatus.getText());
        observableBorrow.setLblOpenDate(lblOpenDate2.getText());
        observableBorrow.setLblCustName(lblCustName_2.getText());
        observableBorrow.setLblCity(lblCity_BorrowerProfile_2.getText());
        observableBorrow.setLblState(lblState_BorrowerProfile_2.getText());
        observableBorrow.setLblPin(lblPin_BorrowerProfile_2.getText());
        observableBorrow.setLblPhone(lblPhone_BorrowerProfile_2.getText());
        observableBorrow.setLblFax(lblFax_BorrowerProfile_2.getText());
        observableBorrow.setLblBorrowerNo_2(lblBorrowerNo_2.getText());
        observable.setBorrowerNo(lblBorrowerNo_2.getText());
        observableSecurity.setLblAccHeadSec_2(lblAccHeadSec_2.getText());
        observableSecurity.setLblAccHeadSec_2(lblAccHeadSec_2.getText());
        observableSecurity.setLblAccNoSec_2(lblAccNoSec_2.getText());
        observableOtherDetails.setLblAcctHead_Disp_ODetails(lblAcctHead_Disp_ODetails.getText());
        observableOtherDetails.setLblAcctNo_Disp_ODetails(lblAcctNo_Disp_ODetails.getText());
        observableRepay.setLblAccHead_RS_2(lblAccHead_RS_2.getText());
        observable.setLblAccountHead_FD_Disp(lblAccountHead_FD_Disp.getText());
        observableRepay.setLblAccNo_RS_2(lblAccNo_RS_2.getText());
        observableGuarantor.setLblAccHead_GD_2(lblAccHead_GD_2.getText());
        observableGuarantor.setLblAccNo_GD_2(lblAccNo_GD_2.getText());
        observableInt.setLblAccHead_IM_2(lblAccHead_IM_2.getText());
        observableInt.setLblAccNo_IM_2(lblAccNo_IM_2.getText());
        observableClassi.setLblAccHead_CD_2(lblAccHead_CD_2.getText());
        observableClassi.setLblAccNo_CD_2(lblAccNo_CD_2.getText());
        observableInt.setLblLimitAmt_2(lblLimitAmt_2.getText());
        observableInt.setLblPLR_Limit_2(lblPLR_Limit_2.getText());
        observableInt.setLblSancDate_2(lblSancDate_2.getText());
        observableInt.setLblExpiryDate_2(lblExpiryDate_2.getText());
        observableClassi.setLblSanctionNo2(lblSanctionNo2.getText());
        observableClassi.setLblSanctionDate2(lblSanctionDate2.getText());
        observable.setCboProductId(CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
    }
    
    private void initComponentData() {
        cboCategory.setModel(observableBorrow.getCbmCategory());
        cboConstitution.setModel(observableBorrow.getCbmConstitution());
        cboAddressType.setModel(observableComp.getCbmAddressType());
        cboNatureBusiness.setModel(observableComp.getCbmNatureBusiness());
        cboCity_CompDetail.setModel(observableComp.getCbmCity_CompDetail());
        cboState_CompDetail.setModel(observableComp.getCbmState_CompDetail());
        cboCountry_CompDetail.setModel(observableComp.getCbmCountry_CompDetail());
        cboAccStatus.setModel(observable.getCbmAccStatus());
        cboSanctioningAuthority.setModel(observable.getCbmSanctioningAuthority());
        cboModeSanction.setModel(observable.getCbmModeSanction());
        cboRepayFreq.setModel(observable.getCbmRepayFreq());
        cboIntGetFrom.setModel(observable.getCbmIntGetFrom());
        cboTypeOfFacility.setModel(observable.getCbmTypeOfFacility());
        cboCity_GD.setModel(observableGuarantor.getCbmCity_GD());
        cboState_GD.setModel(observableGuarantor.getCbmState_GD());
        cboCountry_GD.setModel(observableGuarantor.getCbmCountry_GD());
        cboConstitution_GD.setModel(observableGuarantor.getCbmConstitution_GD());
        cboProdId.setModel(observableGuarantor.getCbmProdId());
        cboProdType.setModel(observableGuarantor.getCbmProdType());
        cboInterestType.setModel(observable.getCbmInterestType());
        cboProductId.setModel(observable.getCbmProductId());
        cboRepayFreq_Repayment.setModel(observableRepay.getCbmRepayFreq_Repayment());
        cboRepayType.setModel(observableRepay.getCbmRepayType());
        cboCommodityCode.setModel(observableClassi.getCbmCommodityCode());
        cboSectorCode1.setModel(observableClassi.getCbmSectorCode1());
        cboPurposeCode.setModel(observableClassi.getCbmPurposeCode());
        cboIndusCode.setModel(observableClassi.getCbmIndusCode());
        cbo20Code.setModel(observableClassi.getCbm20Code());
        cboGovtSchemeCode.setModel(observableClassi.getCbmGovtSchemeCode());
        cboGuaranteeCoverCode.setModel(observableClassi.getCbmGuaranteeCoverCode());
        cboHealthCode.setModel(observableClassi.getCbmHealthCode());
        cboDistrictCode.setModel(observableClassi.getCbmDistrictCode());
        cboWeakerSectionCode.setModel(observableClassi.getCbmWeakerSectionCode());
        cboRefinancingInsti.setModel(observableClassi.getCbmRefinancingInsti());
        cboAssetCode.setModel(observableClassi.getCbmAssetCode());
        cboTypeFacility.setModel(observableClassi.getCbmTypeFacility());
        cboOpModeAI.setModel(observableOtherDetails.getCbmOpModeAI());
        cboSettlementModeAI.setModel(observableOtherDetails.getCbmSettlementModeAI());
        cboStmtFreqAD.setModel(observableOtherDetails.getCbmStmtFreqAD());
        cboRecommendedByType.setModel(observable.getCbmRecommendedByType());
    }
    
    private void setMaxLength(){
        txtSanctionSlNo.setValidation(new NumericValidation());
        txtGuarantorNo.setValidation(new NumericValidation());
        txtScheduleNo.setValidation(new NumericValidation());
        txtAcct_Name.setMaxLength(32);
        txtPeriodDifference_Days.setValidation(new NumericValidation(3, 0));
        txtPeriodDifference_Months.setValidation(new NumericValidation(3, 0));
        txtPeriodDifference_Years.setValidation(new NumericValidation(3, 0));
        txtCustID.setMaxLength(16);
        txtCustID.setValidation(new DefaultValidation());
        txtReferences.setMaxLength(128);
        txtReferences.setValidation(new DefaultValidation());
        txtCompanyRegisNo.setMaxLength(16);
        txtRiskRating.setMaxLength(3);
        txtRiskRating.setValidation(new NumericValidation());
        txtNetWorth.setMaxLength(16);
        txtNetWorth.setValidation(new CurrencyValidation(14,2));
        txtChiefExecutiveName.setMaxLength(32);
        txtStreet_CompDetail.setMaxLength(256);
        txtStreet_CompDetail.setValidation(new DefaultValidation());
        txtArea_CompDetail.setMaxLength(128);
        txtArea_CompDetail.setValidation(new DefaultValidation());
        txtPin_CompDetail.setMaxLength(16);
        txtPin_CompDetail.setValidation(new PincodeValidation_IN());
        txtPhone_CompDetail.setMaxLength(32);
        txtPhone_CompDetail.setAllowNumber(true);
        txtRiskRating.setMaxLength(3);
        txtRemarks__CompDetail.setMaxLength(128);
        txtRemarks__CompDetail.setValidation(new DefaultValidation());
        txtNoInstallments.setMaxLength(3);
        txtNoInstallments.setValidation(new NumericValidation());
        txtSanctionNo.setMaxLength(16);
        txtSanctionNo.setAllowNumber(true);
        txtSanctionRemarks.setMaxLength(128);
        txtSanctionRemarks.setValidation(new DefaultValidation());
        txtLimit_SD.setMaxLength(16);
        txtLimit_SD.setValidation(new CurrencyValidation(14,2));
        txtPurposeDesc.setMaxLength(128);
        txtPurposeDesc.setValidation(new DefaultValidation());
        txtGroupDesc.setMaxLength(128);
        txtGroupDesc.setValidation(new DefaultValidation());
        txtContactPerson.setMaxLength(32);
        txtContactPerson.setValidation(new DefaultValidation());
        txtContactPhone.setMaxLength(32);
        txtContactPhone.setAllowNumber(true);
        txtSecurityNo.setMaxLength(16);
        txtSecurityValue.setMaxLength(16);
        txtSecurityValue.setValidation(new CurrencyValidation(14,2));
        txtLaonAmt.setMaxLength(16);
        txtLaonAmt.setValidation(new CurrencyValidation(14,2));
        txtNoInstall.setMaxLength(3);
        txtNoInstall.setValidation(new NumericValidation());
        txtNoMonthsMora.setMaxLength(3);
        txtNoMonthsMora.setValidation(new NumericValidation());
        txtFacility_Moratorium_Period.setMaxLength(3);
        txtFacility_Moratorium_Period.setValidation(new NumericValidation());
        txtSecurityValue.setMaxLength(16);
        txtSecurityValue.setValidation(new CurrencyValidation(14,2));
        txtEligibleLoan.setMaxLength(16);
        txtEligibleLoan.setValidation(new CurrencyValidation(14,2));
        txtMargin.setMaxLength(5);
        txtMargin.setValidation(new PercentageValidation());
        txtAmtPenulInstall.setMaxLength(16);
        txtAmtPenulInstall.setValidation(new CurrencyValidation(14,2));
        txtAmtLastInstall.setMaxLength(16);
        txtAmtLastInstall.setValidation(new CurrencyValidation(14,2));
        txtTotalInstallAmt.setMaxLength(16);
        txtTotalInstallAmt.setValidation(new CurrencyValidation(14,2));
        txtTotalBaseAmt.setMaxLength(16);
        txtTotalBaseAmt.setValidation(new CurrencyValidation(14, 2));
        txtCustomerID_GD.setMaxLength(16);
        txtCustomerID_GD.setValidation(new DefaultValidation());
        txtGuaranAccNo.setMaxLength(16);
        txtGuaranName.setMaxLength(32);
        txtStreet_GD.setMaxLength(256);
        txtStreet_GD.setValidation(new DefaultValidation());
        txtArea_GD.setMaxLength(128);
        txtPin_GD.setMaxLength(16);
        txtPin_GD.setValidation(new PincodeValidation_IN());
        txtPhone_GD.setMaxLength(32);
        txtPhone_GD.setAllowNumber(true);
        txtGuarantorNetWorth.setMaxLength(16);
        txtGuarantorNetWorth.setValidation(new CurrencyValidation(14,2));
        txtRemarks_DocumentDetails.setMaxLength(128);
        txtFromAmt.setMaxLength(16);
        txtFromAmt.setValidation(new CurrencyValidation(14,2));
        txtToAmt.setMaxLength(16);
        txtToAmt.setValidation(new CurrencyValidation(14,2));
        txtInter.setMaxLength(5);
        txtInter.setValidation(new PercentageValidation());
        txtPenalInter.setMaxLength(5);
        txtPenalInter.setValidation(new PercentageValidation());
        txtAgainstClearingInter.setMaxLength(5);
        txtAgainstClearingInter.setValidation(new PercentageValidation());
        txtPenalStatement.setMaxLength(5);
        txtPenalStatement.setValidation(new PercentageValidation());
        txtInterExpLimit.setMaxLength(5);
        txtInterExpLimit.setValidation(new PercentageValidation());
        txtATMNoAD.setMaxLength(16);
        txtATMNoAD.setAllowNumber(true);
        txtDebitNoAD.setMaxLength(16);
        txtDebitNoAD.setAllowNumber(true);
        txtCreditNoAD.setMaxLength(16);
        txtCreditNoAD.setAllowNumber(true);
        txtAccOpeningChrgAD.setValidation(new CurrencyValidation(14,2));
        txtAccCloseChrgAD.setValidation(new CurrencyValidation(14,2));
        txtMisServiceChrgAD.setValidation(new CurrencyValidation(14,2));
        txtChequeBookChrgAD.setValidation(new CurrencyValidation(14,2));
        txtFolioChrgAD.setValidation(new CurrencyValidation(14,2));
        txtExcessWithChrgAD.setValidation(new CurrencyValidation(14,2));
        txtMinActBalanceAD.setValidation(new CurrencyValidation(14,2));
        txtABBChrgAD.setValidation(new CurrencyValidation(14,2));
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled( btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        setAuthBtnEnableDisable();
    }
    
    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable(){
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccLimit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNatureInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRiskWeight = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMultiDisburseAllow = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidy = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDoAddSIs = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPostDatedCheque = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatus_Repayment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExecuted_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMandatory_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTermLoan = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTermLoan = new com.see.truetransact.uicomponent.CPanel();
        tabLimitAmount = new com.see.truetransact.uicomponent.CTabbedPane();
        panBorrowCompanyDetails = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile_CustID = new com.see.truetransact.uicomponent.CPanel();
        lblOpenDate = new com.see.truetransact.uicomponent.CLabel();
        lblOpenDate2 = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblCustName_2 = new com.see.truetransact.uicomponent.CLabel();
        lblCity_BorrowerProfile = new com.see.truetransact.uicomponent.CLabel();
        lblCity_BorrowerProfile_2 = new com.see.truetransact.uicomponent.CLabel();
        lblState_BorrowerProfile = new com.see.truetransact.uicomponent.CLabel();
        lblState_BorrowerProfile_2 = new com.see.truetransact.uicomponent.CLabel();
        lblPin_BorrowerProfile = new com.see.truetransact.uicomponent.CLabel();
        lblPin_BorrowerProfile_2 = new com.see.truetransact.uicomponent.CLabel();
        lblPhone_BorrowerProfile = new com.see.truetransact.uicomponent.CLabel();
        lblPhone_BorrowerProfile_2 = new com.see.truetransact.uicomponent.CLabel();
        lblFax_BorrowerProfile = new com.see.truetransact.uicomponent.CLabel();
        lblFax_BorrowerProfile_2 = new com.see.truetransact.uicomponent.CLabel();
        panBorrowerTabCTable = new com.see.truetransact.uicomponent.CPanel();
        srpBorrowerTabCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblBorrowerTabCTable = new com.see.truetransact.uicomponent.CTable();
        panBorrowerTabTools = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Borrower = new com.see.truetransact.uicomponent.CButton();
        btnToMain_Borrower = new com.see.truetransact.uicomponent.CButton();
        btnDeleteBorrower = new com.see.truetransact.uicomponent.CButton();
        panBorrowProfile_CustName = new com.see.truetransact.uicomponent.CPanel();
        cboConstitution = new com.see.truetransact.uicomponent.CComboBox();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        lblConstitution = new com.see.truetransact.uicomponent.CLabel();
        lblReferences = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        txtReferences = new com.see.truetransact.uicomponent.CTextField();
        tdtDealingWithBankSince = new com.see.truetransact.uicomponent.CDateField();
        lblDealingWithBankSince = new com.see.truetransact.uicomponent.CLabel();
        lblRiskRating = new com.see.truetransact.uicomponent.CLabel();
        txtRiskRating = new com.see.truetransact.uicomponent.CTextField();
        lblNatureBusiness = new com.see.truetransact.uicomponent.CLabel();
        cboNatureBusiness = new com.see.truetransact.uicomponent.CComboBox();
        lblRemarks__CompDetail = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks__CompDetail = new com.see.truetransact.uicomponent.CTextField();
        lblNetWorth = new com.see.truetransact.uicomponent.CLabel();
        txtNetWorth = new com.see.truetransact.uicomponent.CTextField();
        lblAsOn = new com.see.truetransact.uicomponent.CLabel();
        tdtAsOn = new com.see.truetransact.uicomponent.CDateField();
        lblCreditFacilityAvailSince = new com.see.truetransact.uicomponent.CLabel();
        tdtCreditFacilityAvailSince = new com.see.truetransact.uicomponent.CDateField();
        lblBorrowerNo = new com.see.truetransact.uicomponent.CLabel();
        lblBorrowerNo_2 = new com.see.truetransact.uicomponent.CLabel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        sptBorroewrProfile = new com.see.truetransact.uicomponent.CSeparator();
        panCompanyDetails = new com.see.truetransact.uicomponent.CPanel();
        panCompanyDetailsTrash = new com.see.truetransact.uicomponent.CPanel();
        panCompanyDetails_Company = new com.see.truetransact.uicomponent.CPanel();
        lblCompanyRegisNo = new com.see.truetransact.uicomponent.CLabel();
        txtCompanyRegisNo = new com.see.truetransact.uicomponent.CTextField();
        lblDateEstablishment = new com.see.truetransact.uicomponent.CLabel();
        tdtDateEstablishment = new com.see.truetransact.uicomponent.CDateField();
        lblChiefExecutiveName = new com.see.truetransact.uicomponent.CLabel();
        txtChiefExecutiveName = new com.see.truetransact.uicomponent.CTextField();
        lblAddressType = new com.see.truetransact.uicomponent.CLabel();
        cboAddressType = new com.see.truetransact.uicomponent.CComboBox();
        lblStreet_CompDetail = new com.see.truetransact.uicomponent.CLabel();
        txtStreet_CompDetail = new com.see.truetransact.uicomponent.CTextField();
        lblArea_CompDetail = new com.see.truetransact.uicomponent.CLabel();
        txtArea_CompDetail = new com.see.truetransact.uicomponent.CTextField();
        srpComp_Tab_Addr = new com.see.truetransact.uicomponent.CSeparator();
        lblCity_CompDetail = new com.see.truetransact.uicomponent.CLabel();
        cboCity_CompDetail = new com.see.truetransact.uicomponent.CComboBox();
        lblState_CompDetail = new com.see.truetransact.uicomponent.CLabel();
        cboState_CompDetail = new com.see.truetransact.uicomponent.CComboBox();
        sptCompanyDetails = new com.see.truetransact.uicomponent.CSeparator();
        panCompanyDetails_Addr = new com.see.truetransact.uicomponent.CPanel();
        lblCountry_CompDetail = new com.see.truetransact.uicomponent.CLabel();
        cboCountry_CompDetail = new com.see.truetransact.uicomponent.CComboBox();
        lblPin_CompDetail = new com.see.truetransact.uicomponent.CLabel();
        txtPin_CompDetail = new com.see.truetransact.uicomponent.CTextField();
        lblPhone_CompDetail = new com.see.truetransact.uicomponent.CLabel();
        txtPhone_CompDetail = new com.see.truetransact.uicomponent.CTextField();
        panSanctionDetails = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails_Upper = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails_Sanction = new com.see.truetransact.uicomponent.CPanel();
        lblSanctionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSanctionDate = new com.see.truetransact.uicomponent.CDateField();
        txtSanctionSlNo = new com.see.truetransact.uicomponent.CTextField();
        lblSanctionSlNo = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionNo = new com.see.truetransact.uicomponent.CLabel();
        txtSanctionNo = new com.see.truetransact.uicomponent.CTextField();
        panSanctionDetails_Mode = new com.see.truetransact.uicomponent.CPanel();
        lblModeSanction = new com.see.truetransact.uicomponent.CLabel();
        cboModeSanction = new com.see.truetransact.uicomponent.CComboBox();
        txtSanctionRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblSanctioningAuthority = new com.see.truetransact.uicomponent.CLabel();
        cboSanctioningAuthority = new com.see.truetransact.uicomponent.CComboBox();
        panButton2_SD = new com.see.truetransact.uicomponent.CPanel();
        btnNew2_SD = new com.see.truetransact.uicomponent.CButton();
        btnSave2_SD = new com.see.truetransact.uicomponent.CButton();
        btnDelete2_SD = new com.see.truetransact.uicomponent.CButton();
        panSanctionDetails_Table = new com.see.truetransact.uicomponent.CPanel();
        panTableFields_SD = new com.see.truetransact.uicomponent.CPanel();
        lblTypeOfFacility = new com.see.truetransact.uicomponent.CLabel();
        cboTypeOfFacility = new com.see.truetransact.uicomponent.CComboBox();
        lblLimit_SD = new com.see.truetransact.uicomponent.CLabel();
        txtLimit_SD = new com.see.truetransact.uicomponent.CTextField();
        lblFDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFDate = new com.see.truetransact.uicomponent.CDateField();
        lblTDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTDate = new com.see.truetransact.uicomponent.CDateField();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnNew1 = new com.see.truetransact.uicomponent.CButton();
        btnSave1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete1 = new com.see.truetransact.uicomponent.CButton();
        lblNoInstallments = new com.see.truetransact.uicomponent.CLabel();
        txtNoInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblRepayFreq = new com.see.truetransact.uicomponent.CLabel();
        cboRepayFreq = new com.see.truetransact.uicomponent.CComboBox();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead_2 = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_Sanction = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_Sanction_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblFacility_Repay_Date = new com.see.truetransact.uicomponent.CLabel();
        tdtFacility_Repay_Date = new com.see.truetransact.uicomponent.CDateField();
        chkMoratorium_Given = new com.see.truetransact.uicomponent.CCheckBox();
        lblMoratorium_Given = new com.see.truetransact.uicomponent.CLabel();
        lblFacility_Moratorium_Period = new com.see.truetransact.uicomponent.CLabel();
        txtFacility_Moratorium_Period = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodDifference = new com.see.truetransact.uicomponent.CLabel();
        panPeriodDifference = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodDifference_Years = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodDifference_Years = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodDifference_Months = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodDifference_Months = new com.see.truetransact.uicomponent.CLabel();
        txtPeriodDifference_Days = new com.see.truetransact.uicomponent.CTextField();
        lblPeriodDifference_Days = new com.see.truetransact.uicomponent.CLabel();
        panTable_SD = new com.see.truetransact.uicomponent.CPanel();
        srpTable_SD = new com.see.truetransact.uicomponent.CScrollPane();
        tblSanctionDetails = new com.see.truetransact.uicomponent.CTable();
        panTable2_SD = new com.see.truetransact.uicomponent.CPanel();
        srpTable2_SD = new com.see.truetransact.uicomponent.CScrollPane();
        tblSanctionDetails2 = new com.see.truetransact.uicomponent.CTable();
        panFacilityDetails = new com.see.truetransact.uicomponent.CPanel();
        panFacilityDetails_Data = new com.see.truetransact.uicomponent.CPanel();
        panFDAccount = new com.see.truetransact.uicomponent.CPanel();
        panSecurityDetails_FD = new com.see.truetransact.uicomponent.CPanel();
        rdoSecurityDetails_Unsec = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSecurityDetails_Partly = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSecurityDetails_Fully = new com.see.truetransact.uicomponent.CRadioButton();
        lblAccLimit = new com.see.truetransact.uicomponent.CLabel();
        panAccLimit = new com.see.truetransact.uicomponent.CPanel();
        rdoAccLimit_Main = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAccLimit_Submit = new com.see.truetransact.uicomponent.CRadioButton();
        lblRiskWeight = new com.see.truetransact.uicomponent.CLabel();
        panRiskWeight = new com.see.truetransact.uicomponent.CPanel();
        rdoRiskWeight_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRiskWeight_No = new com.see.truetransact.uicomponent.CRadioButton();
        panDemandPromssoryDate = new com.see.truetransact.uicomponent.CPanel();
        lblDemandPromNoteDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDemandPromNoteDate = new com.see.truetransact.uicomponent.CDateField();
        lblDemandPromNoteExpDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDemandPromNoteExpDate = new com.see.truetransact.uicomponent.CDateField();
        lblBlank1 = new com.see.truetransact.uicomponent.CLabel();
        panFacilityProdID = new javax.swing.JPanel();
        lblProductID_FD = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead_FD = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead_FD_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblProductID_FD_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_FD = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_FD_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblAccStatus = new com.see.truetransact.uicomponent.CLabel();
        cboAccStatus = new com.see.truetransact.uicomponent.CComboBox();
        lblAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        tdtAccountOpenDate = new com.see.truetransact.uicomponent.CDateField();
        panFacilityChkBoxes = new com.see.truetransact.uicomponent.CPanel();
        chkInsurance = new com.see.truetransact.uicomponent.CCheckBox();
        chkGurantor = new com.see.truetransact.uicomponent.CCheckBox();
        chkStockInspect = new com.see.truetransact.uicomponent.CCheckBox();
        sptFacilityDetails_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panFDDate = new com.see.truetransact.uicomponent.CPanel();
        lblAODDate = new com.see.truetransact.uicomponent.CLabel();
        tdtAODDate = new com.see.truetransact.uicomponent.CDateField();
        lblMultiDisburseAllow = new com.see.truetransact.uicomponent.CLabel();
        panMultiDisburseAllow = new com.see.truetransact.uicomponent.CPanel();
        rdoMultiDisburseAllow_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMultiDisburseAllow_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSubsidy = new com.see.truetransact.uicomponent.CLabel();
        panSubsidy = new com.see.truetransact.uicomponent.CPanel();
        rdoSubsidy_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSubsidy_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPurposeDesc = new com.see.truetransact.uicomponent.CLabel();
        txtPurposeDesc = new com.see.truetransact.uicomponent.CTextField();
        lblGroupDesc = new com.see.truetransact.uicomponent.CLabel();
        txtGroupDesc = new com.see.truetransact.uicomponent.CTextField();
        lblInterest = new com.see.truetransact.uicomponent.CLabel();
        panInterest = new com.see.truetransact.uicomponent.CPanel();
        rdoInterest_Simple = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterest_Compound = new com.see.truetransact.uicomponent.CRadioButton();
        lblContactPerson = new com.see.truetransact.uicomponent.CLabel();
        txtContactPerson = new com.see.truetransact.uicomponent.CTextField();
        lblContactPhone = new com.see.truetransact.uicomponent.CLabel();
        txtContactPhone = new com.see.truetransact.uicomponent.CTextField();
        lblRemark_FD = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panFacilityTools = new com.see.truetransact.uicomponent.CPanel();
        btnFacilitySave = new com.see.truetransact.uicomponent.CButton();
        btnFacilityDelete = new com.see.truetransact.uicomponent.CButton();
        txtAcct_Name = new com.see.truetransact.uicomponent.CTextField();
        lblAcct_Name = new com.see.truetransact.uicomponent.CLabel();
        cboRecommendedByType = new com.see.truetransact.uicomponent.CComboBox();
        lblRecommandByType = new com.see.truetransact.uicomponent.CLabel();
        lblInterestType = new com.see.truetransact.uicomponent.CLabel();
        cboInterestType = new com.see.truetransact.uicomponent.CComboBox();
        panNatureInterest = new com.see.truetransact.uicomponent.CPanel();
        rdoNatureInterest_PLR = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNatureInterest_NonPLR = new com.see.truetransact.uicomponent.CRadioButton();
        lblNatureInterest = new com.see.truetransact.uicomponent.CLabel();
        panSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        panSecurityDetails_Acc = new com.see.truetransact.uicomponent.CPanel();
        panSecurity = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblProdId_Disp = new com.see.truetransact.uicomponent.CLabel();
        panSecurityNature = new com.see.truetransact.uicomponent.CPanel();
        lblAccHeadSec = new com.see.truetransact.uicomponent.CLabel();
        lblAccHeadSec_2 = new com.see.truetransact.uicomponent.CLabel();
        lblAccNoSec = new com.see.truetransact.uicomponent.CLabel();
        lblAccNoSec_2 = new com.see.truetransact.uicomponent.CLabel();
        sptSecurityDetails_Hori = new com.see.truetransact.uicomponent.CSeparator();
        panSecurityDetails_security = new com.see.truetransact.uicomponent.CPanel();
        panSecDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSecurityNo = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityNo = new com.see.truetransact.uicomponent.CTextField();
        lblCustName_Security = new com.see.truetransact.uicomponent.CLabel();
        lblCustID_Security = new com.see.truetransact.uicomponent.CLabel();
        txtCustID_Security = new com.see.truetransact.uicomponent.CTextField();
        btnCustID_Security = new com.see.truetransact.uicomponent.CButton();
        btnSecurityNo_Security = new com.see.truetransact.uicomponent.CButton();
        lblCustName_Security_Display = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        panSecurityTools = new com.see.truetransact.uicomponent.CPanel();
        btnSecurityNew = new com.see.truetransact.uicomponent.CButton();
        btnSecuritySave = new com.see.truetransact.uicomponent.CButton();
        btnSecurityDelete = new com.see.truetransact.uicomponent.CButton();
        panTotalSecurity_Value = new com.see.truetransact.uicomponent.CPanel();
        lblSecurityValue = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityValue = new com.see.truetransact.uicomponent.CTextField();
        lblMargin = new com.see.truetransact.uicomponent.CLabel();
        txtMargin = new com.see.truetransact.uicomponent.CTextField();
        lblEligibleLoan = new com.see.truetransact.uicomponent.CLabel();
        txtEligibleLoan = new com.see.truetransact.uicomponent.CTextField();
        panSecurityTableMain = new com.see.truetransact.uicomponent.CPanel();
        panSecurityTable = new com.see.truetransact.uicomponent.CPanel();
        srpSecurityTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSecurityTable = new com.see.truetransact.uicomponent.CTable();
        panGuarantorInsuranceDetails = new com.see.truetransact.uicomponent.CPanel();
        panGuarantorDetails = new com.see.truetransact.uicomponent.CPanel();
        panProd_GD = new com.see.truetransact.uicomponent.CPanel();
        lblProdID_GD = new com.see.truetransact.uicomponent.CLabel();
        lblProdID_GD_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead_GD = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead_GD_2 = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_GD = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_GD_2 = new com.see.truetransact.uicomponent.CLabel();
        panGuarantorDetail_Detail = new com.see.truetransact.uicomponent.CPanel();
        panGuarantor = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerID_GD = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerID_GD = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerID_GD = new com.see.truetransact.uicomponent.CButton();
        lblGuaranAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtGuaranAccNo = new com.see.truetransact.uicomponent.CTextField();
        lblGuaranName = new com.see.truetransact.uicomponent.CLabel();
        txtGuaranName = new com.see.truetransact.uicomponent.CTextField();
        lblDOB_GD = new com.see.truetransact.uicomponent.CLabel();
        lblStreet_GD = new com.see.truetransact.uicomponent.CLabel();
        txtStreet_GD = new com.see.truetransact.uicomponent.CTextField();
        lblGuarantorNo = new com.see.truetransact.uicomponent.CLabel();
        txtGuarantorNo = new com.see.truetransact.uicomponent.CTextField();
        tdtDOB_GD = new com.see.truetransact.uicomponent.CDateField();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId1 = new com.see.truetransact.uicomponent.CLabel();
        lblArea_GD = new com.see.truetransact.uicomponent.CLabel();
        txtArea_GD = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        sptGuarantorDetail_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panGuaranAddr = new com.see.truetransact.uicomponent.CPanel();
        lblPin_GD = new com.see.truetransact.uicomponent.CLabel();
        txtPin_GD = new com.see.truetransact.uicomponent.CTextField();
        lblState_GD = new com.see.truetransact.uicomponent.CLabel();
        lblCountry_GD = new com.see.truetransact.uicomponent.CLabel();
        cboCountry_GD = new com.see.truetransact.uicomponent.CComboBox();
        lblPhone_GD = new com.see.truetransact.uicomponent.CLabel();
        txtPhone_GD = new com.see.truetransact.uicomponent.CTextField();
        lblConstitution_GD = new com.see.truetransact.uicomponent.CLabel();
        cboConstitution_GD = new com.see.truetransact.uicomponent.CComboBox();
        lblGuarantorNetWorth = new com.see.truetransact.uicomponent.CLabel();
        txtGuarantorNetWorth = new com.see.truetransact.uicomponent.CTextField();
        lblAsOn_GD = new com.see.truetransact.uicomponent.CLabel();
        tdtAsOn_GD = new com.see.truetransact.uicomponent.CDateField();
        cboState_GD = new com.see.truetransact.uicomponent.CComboBox();
        cboCity_GD = new com.see.truetransact.uicomponent.CComboBox();
        lblCity_GD = new com.see.truetransact.uicomponent.CLabel();
        panToolBtns = new com.see.truetransact.uicomponent.CPanel();
        btnGuarantorNew = new com.see.truetransact.uicomponent.CButton();
        btnGuarantorSave = new com.see.truetransact.uicomponent.CButton();
        btnGuarantorDelete = new com.see.truetransact.uicomponent.CButton();
        panGuarantorDetailsTable = new com.see.truetransact.uicomponent.CPanel();
        srpGuarantorTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblGuarantorTable = new com.see.truetransact.uicomponent.CTable();
        sptGuarantorDetail_Hori1 = new com.see.truetransact.uicomponent.CSeparator();
        panInterMaintenance = new com.see.truetransact.uicomponent.CPanel();
        panInterMaintenance_Acc = new com.see.truetransact.uicomponent.CPanel();
        panProd_IM = new com.see.truetransact.uicomponent.CPanel();
        lblProdID_IM = new com.see.truetransact.uicomponent.CLabel();
        lblProdID_IM_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblIntGetFrom = new com.see.truetransact.uicomponent.CLabel();
        cboIntGetFrom = new com.see.truetransact.uicomponent.CComboBox();
        panAcc_IM = new com.see.truetransact.uicomponent.CPanel();
        lblAccHead_IM = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead_IM_2 = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_IM = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_IM_2 = new com.see.truetransact.uicomponent.CLabel();
        sptInterMaintenance_Hori = new com.see.truetransact.uicomponent.CSeparator();
        panInterMaintenance_Details = new com.see.truetransact.uicomponent.CPanel();
        panLimit = new com.see.truetransact.uicomponent.CPanel();
        lblLimitAmt = new com.see.truetransact.uicomponent.CLabel();
        lblLimitAmt_2 = new com.see.truetransact.uicomponent.CLabel();
        lblPLR_Limit = new com.see.truetransact.uicomponent.CLabel();
        lblPLR_Limit_2 = new com.see.truetransact.uicomponent.CLabel();
        panDate = new com.see.truetransact.uicomponent.CPanel();
        lblSancDate = new com.see.truetransact.uicomponent.CLabel();
        lblSancDate_2 = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate_2 = new com.see.truetransact.uicomponent.CLabel();
        sptInterMaintenance_Hori2 = new com.see.truetransact.uicomponent.CSeparator();
        panInterMaintenance_Table = new com.see.truetransact.uicomponent.CPanel();
        panTableFields = new com.see.truetransact.uicomponent.CPanel();
        lblFrom = new com.see.truetransact.uicomponent.CLabel();
        tdtFrom = new com.see.truetransact.uicomponent.CDateField();
        lblTo = new com.see.truetransact.uicomponent.CLabel();
        tdtTo = new com.see.truetransact.uicomponent.CDateField();
        lblFromAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmt = new com.see.truetransact.uicomponent.CTextField();
        lblToAmt = new com.see.truetransact.uicomponent.CLabel();
        txtToAmt = new com.see.truetransact.uicomponent.CTextField();
        lblInter = new com.see.truetransact.uicomponent.CLabel();
        txtInter = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInter = new com.see.truetransact.uicomponent.CLabel();
        txtPenalInter = new com.see.truetransact.uicomponent.CTextField();
        lblAgainstClearingInter = new com.see.truetransact.uicomponent.CLabel();
        txtAgainstClearingInter = new com.see.truetransact.uicomponent.CTextField();
        lblPenalStatement = new com.see.truetransact.uicomponent.CLabel();
        txtPenalStatement = new com.see.truetransact.uicomponent.CTextField();
        lblInterExpLimit = new com.see.truetransact.uicomponent.CLabel();
        txtInterExpLimit = new com.see.truetransact.uicomponent.CTextField();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnInterestMaintenanceNew = new com.see.truetransact.uicomponent.CButton();
        btnInterestMaintenanceSave = new com.see.truetransact.uicomponent.CButton();
        btnInterestMaintenanceDelete = new com.see.truetransact.uicomponent.CButton();
        panTable_IM = new com.see.truetransact.uicomponent.CPanel();
        srpInterMaintenance = new com.see.truetransact.uicomponent.CScrollPane();
        tblInterMaintenance = new com.see.truetransact.uicomponent.CTable();
        panRepaymentSchedule = new com.see.truetransact.uicomponent.CPanel();
        panRepayment = new com.see.truetransact.uicomponent.CPanel();
        sptRepatmentSchedule_Hori = new com.see.truetransact.uicomponent.CSeparator();
        panRepaymentSchedule_Details = new com.see.truetransact.uicomponent.CPanel();
        panSchedule_RS = new com.see.truetransact.uicomponent.CPanel();
        lblScheduleNo = new com.see.truetransact.uicomponent.CLabel();
        txtScheduleNo = new com.see.truetransact.uicomponent.CTextField();
        lblLaonAmt = new com.see.truetransact.uicomponent.CLabel();
        txtLaonAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRepayType = new com.see.truetransact.uicomponent.CLabel();
        cboRepayType = new com.see.truetransact.uicomponent.CComboBox();
        lblRepayFreq_Repayment = new com.see.truetransact.uicomponent.CLabel();
        cboRepayFreq_Repayment = new com.see.truetransact.uicomponent.CComboBox();
        lblNoMonthsMora = new com.see.truetransact.uicomponent.CLabel();
        txtNoMonthsMora = new com.see.truetransact.uicomponent.CTextField();
        txtNoInstall = new com.see.truetransact.uicomponent.CTextField();
        lblNoInstall = new com.see.truetransact.uicomponent.CLabel();
        tdtFirstInstall = new com.see.truetransact.uicomponent.CDateField();
        lblFirstInstall = new com.see.truetransact.uicomponent.CLabel();
        lblLastInstall = new com.see.truetransact.uicomponent.CLabel();
        tdtLastInstall = new com.see.truetransact.uicomponent.CDateField();
        lblDisbursement_Dt = new com.see.truetransact.uicomponent.CLabel();
        tdtDisbursement_Dt = new com.see.truetransact.uicomponent.CDateField();
        sptRepatmentSchedule_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panInstall_RS = new com.see.truetransact.uicomponent.CPanel();
        lblTotalBaseAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalBaseAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAmtPenulInstall = new com.see.truetransact.uicomponent.CLabel();
        txtAmtPenulInstall = new com.see.truetransact.uicomponent.CTextField();
        lblAmtLastInstall = new com.see.truetransact.uicomponent.CLabel();
        txtAmtLastInstall = new com.see.truetransact.uicomponent.CTextField();
        lblTotalInstallAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalInstallAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDoAddSIs = new com.see.truetransact.uicomponent.CLabel();
        panDoAddSIs = new com.see.truetransact.uicomponent.CPanel();
        rdoDoAddSIs_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDoAddSIs_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPostDatedCheque = new com.see.truetransact.uicomponent.CLabel();
        panPostDatedCheque = new com.see.truetransact.uicomponent.CPanel();
        rdoPostDatedCheque_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPostDatedCheque_No = new com.see.truetransact.uicomponent.CRadioButton();
        panStatus_Repayment = new com.see.truetransact.uicomponent.CPanel();
        rdoActive_Repayment = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInActive_Repayment = new com.see.truetransact.uicomponent.CRadioButton();
        lblStatus_Repayment = new com.see.truetransact.uicomponent.CLabel();
        lblRepayScheduleMode = new com.see.truetransact.uicomponent.CLabel();
        txtRepayScheduleMode = new com.see.truetransact.uicomponent.CTextField();
        panProd_RS = new com.see.truetransact.uicomponent.CPanel();
        lblProdID_RS = new com.see.truetransact.uicomponent.CLabel();
        lblProdID_RS_Disp = new com.see.truetransact.uicomponent.CLabel();
        panAcc_RS = new com.see.truetransact.uicomponent.CPanel();
        lblAccHead_RS = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead_RS_2 = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_RS = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_RS_2 = new com.see.truetransact.uicomponent.CLabel();
        panRepaymentToolBtns = new com.see.truetransact.uicomponent.CPanel();
        btnRepayment_New = new com.see.truetransact.uicomponent.CButton();
        btnRepayment_Save = new com.see.truetransact.uicomponent.CButton();
        btnRepayment_Delete = new com.see.truetransact.uicomponent.CButton();
        panRepaymentCTable = new com.see.truetransact.uicomponent.CPanel();
        srpRepaymentCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblRepaymentCTable = new com.see.truetransact.uicomponent.CTable();
        btnEMI_Calculate = new com.see.truetransact.uicomponent.CButton();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        panIsRequired = new com.see.truetransact.uicomponent.CPanel();
        chkChequeBookAD = new com.see.truetransact.uicomponent.CCheckBox();
        chkCustGrpLimitValidationAD = new com.see.truetransact.uicomponent.CCheckBox();
        chkMobileBankingAD = new com.see.truetransact.uicomponent.CCheckBox();
        chkNROStatusAD = new com.see.truetransact.uicomponent.CCheckBox();
        panCardInfo = new com.see.truetransact.uicomponent.CPanel();
        chkATMAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblATMNoAD = new com.see.truetransact.uicomponent.CLabel();
        txtATMNoAD = new com.see.truetransact.uicomponent.CTextField();
        lblATMFromDateAD = new com.see.truetransact.uicomponent.CLabel();
        tdtATMFromDateAD = new com.see.truetransact.uicomponent.CDateField();
        lblATMToDateAD = new com.see.truetransact.uicomponent.CLabel();
        tdtATMToDateAD = new com.see.truetransact.uicomponent.CDateField();
        chkDebitAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblDebitNoAD = new com.see.truetransact.uicomponent.CLabel();
        txtDebitNoAD = new com.see.truetransact.uicomponent.CTextField();
        lblDebitFromDateAD = new com.see.truetransact.uicomponent.CLabel();
        tdtDebitFromDateAD = new com.see.truetransact.uicomponent.CDateField();
        lblDebitToDateAD = new com.see.truetransact.uicomponent.CLabel();
        tdtDebitToDateAD = new com.see.truetransact.uicomponent.CDateField();
        chkCreditAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblCreditNoAD = new com.see.truetransact.uicomponent.CLabel();
        txtCreditNoAD = new com.see.truetransact.uicomponent.CTextField();
        lblCreditFromDateAD = new com.see.truetransact.uicomponent.CLabel();
        tdtCreditFromDateAD = new com.see.truetransact.uicomponent.CDateField();
        lblCreditToDateAD = new com.see.truetransact.uicomponent.CLabel();
        tdtCreditToDateAD = new com.see.truetransact.uicomponent.CDateField();
        panFlexiOpt = new com.see.truetransact.uicomponent.CPanel();
        cboSettlementModeAI = new com.see.truetransact.uicomponent.CComboBox();
        lblSettlementModeAI = new com.see.truetransact.uicomponent.CLabel();
        lblOpModeAI = new com.see.truetransact.uicomponent.CLabel();
        cboOpModeAI = new com.see.truetransact.uicomponent.CComboBox();
        panDiffCharges = new com.see.truetransact.uicomponent.CPanel();
        lblAccOpeningChrgAD = new com.see.truetransact.uicomponent.CLabel();
        txtAccOpeningChrgAD = new com.see.truetransact.uicomponent.CTextField();
        lblMisServiceChrgAD = new com.see.truetransact.uicomponent.CLabel();
        txtMisServiceChrgAD = new com.see.truetransact.uicomponent.CTextField();
        chkStopPmtChrgAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblChequeBookChrgAD = new com.see.truetransact.uicomponent.CLabel();
        txtChequeBookChrgAD = new com.see.truetransact.uicomponent.CTextField();
        chkChequeRetChrgAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblFolioChrgAD = new com.see.truetransact.uicomponent.CLabel();
        txtFolioChrgAD = new com.see.truetransact.uicomponent.CTextField();
        chkInopChrgAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblAccCloseChrgAD = new com.see.truetransact.uicomponent.CLabel();
        txtAccCloseChrgAD = new com.see.truetransact.uicomponent.CTextField();
        chkStmtChrgAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblStmtFreqAD = new com.see.truetransact.uicomponent.CLabel();
        cboStmtFreqAD = new com.see.truetransact.uicomponent.CComboBox();
        chkNonMainMinBalChrgAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblExcessWithChrgAD = new com.see.truetransact.uicomponent.CLabel();
        txtExcessWithChrgAD = new com.see.truetransact.uicomponent.CTextField();
        chkABBChrgAD = new com.see.truetransact.uicomponent.CCheckBox();
        chkNPAChrgAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblABBChrgAD = new com.see.truetransact.uicomponent.CLabel();
        txtABBChrgAD = new com.see.truetransact.uicomponent.CTextField();
        lblNPAChrgAD = new com.see.truetransact.uicomponent.CLabel();
        tdtNPAChrgAD = new com.see.truetransact.uicomponent.CDateField();
        lblMinActBalanceAD = new com.see.truetransact.uicomponent.CLabel();
        txtMinActBalanceAD = new com.see.truetransact.uicomponent.CTextField();
        lblStopPayment = new com.see.truetransact.uicomponent.CLabel();
        lblChequeReturn = new com.see.truetransact.uicomponent.CLabel();
        lblCollectInoperative = new com.see.truetransact.uicomponent.CLabel();
        lblStatement = new com.see.truetransact.uicomponent.CLabel();
        lblNonMaintenance = new com.see.truetransact.uicomponent.CLabel();
        lblABB = new com.see.truetransact.uicomponent.CLabel();
        lblNPA = new com.see.truetransact.uicomponent.CLabel();
        panLastIntApp = new com.see.truetransact.uicomponent.CPanel();
        lblDebit = new com.see.truetransact.uicomponent.CLabel();
        tdtDebit = new com.see.truetransact.uicomponent.CDateField();
        lblCredit = new com.see.truetransact.uicomponent.CLabel();
        tdtCredit = new com.see.truetransact.uicomponent.CDateField();
        panRatesIN = new com.see.truetransact.uicomponent.CPanel();
        lblRateCodeIN = new com.see.truetransact.uicomponent.CLabel();
        lblRateCodeValueIN = new com.see.truetransact.uicomponent.CLabel();
        lblCrInterestRateIN = new com.see.truetransact.uicomponent.CLabel();
        lblCrInterestRateValueIN = new com.see.truetransact.uicomponent.CLabel();
        lblDrInterestRateIN = new com.see.truetransact.uicomponent.CLabel();
        lblDrInterestRateValueIN = new com.see.truetransact.uicomponent.CLabel();
        lblPenalInterestRateIN = new com.see.truetransact.uicomponent.CLabel();
        lblPenalInterestValueIN = new com.see.truetransact.uicomponent.CLabel();
        lblAgClearingIN = new com.see.truetransact.uicomponent.CLabel();
        lblAgClearingValueIN = new com.see.truetransact.uicomponent.CLabel();
        panInterestPayableIN = new com.see.truetransact.uicomponent.CPanel();
        chkPayIntOnCrBalIN = new com.see.truetransact.uicomponent.CCheckBox();
        chkPayIntOnDrBalIN = new com.see.truetransact.uicomponent.CCheckBox();
        panAcctInfo_ODetails = new com.see.truetransact.uicomponent.CPanel();
        lblProdID_ODetails = new com.see.truetransact.uicomponent.CLabel();
        lblProdID_Disp_ODetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctHead_ODetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctHead_Disp_ODetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_ODetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_Disp_ODetails = new com.see.truetransact.uicomponent.CLabel();
        panDocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        panTable_DocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_DocumentDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTable_DocumentDetails = new com.see.truetransact.uicomponent.CTable();
        panAcctDetails_DocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProdID_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblProdID_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctHead_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctHead_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        panTabDetails_DocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDocType_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocType_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocNo_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocNo_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocDesc_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocDesc_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        panSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoYes_DocumentDetails = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo_DocumentDetails = new com.see.truetransact.uicomponent.CRadioButton();
        lblSubmitDate_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        tdtSubmitDate_DocumentDetails = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks_DocumentDetails = new com.see.truetransact.uicomponent.CTextField();
        btnSave_DocumentDetails = new com.see.truetransact.uicomponent.CButton();
        lblMandatory_DOC = new com.see.truetransact.uicomponent.CLabel();
        panMandatory_DOC = new com.see.truetransact.uicomponent.CPanel();
        rdoYes_Mandatory_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo_Mandatory_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        lblExecuted_DOC = new com.see.truetransact.uicomponent.CLabel();
        panExecuted_DOC = new com.see.truetransact.uicomponent.CPanel();
        rdoYes_Executed_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo_Executed_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        lblExecuteDate_DOC = new com.see.truetransact.uicomponent.CLabel();
        tdtExecuteDate_DOC = new com.see.truetransact.uicomponent.CDateField();
        lblExpiryDate_DOC = new com.see.truetransact.uicomponent.CLabel();
        tdtExpiryDate_DOC = new com.see.truetransact.uicomponent.CDateField();
        panClassDetails = new com.see.truetransact.uicomponent.CPanel();
        panClassDetails_Acc = new com.see.truetransact.uicomponent.CPanel();
        panProd_CD = new com.see.truetransact.uicomponent.CPanel();
        lblProID_CD = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionNo1 = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionNo2 = new com.see.truetransact.uicomponent.CLabel();
        lblProID_CD_Disp = new com.see.truetransact.uicomponent.CLabel();
        PanAcc_CD = new com.see.truetransact.uicomponent.CPanel();
        lblAccHead_CD = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead_CD_2 = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_CD = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo_CD_2 = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionDate2 = new com.see.truetransact.uicomponent.CLabel();
        sptClassDetails = new com.see.truetransact.uicomponent.CSeparator();
        panClassDetails_Details = new com.see.truetransact.uicomponent.CPanel();
        panCode = new com.see.truetransact.uicomponent.CPanel();
        lblCommodityCode = new com.see.truetransact.uicomponent.CLabel();
        cboCommodityCode = new com.see.truetransact.uicomponent.CComboBox();
        lblGuaranteeCoverCode = new com.see.truetransact.uicomponent.CLabel();
        cboGuaranteeCoverCode = new com.see.truetransact.uicomponent.CComboBox();
        lblSectorCode1 = new com.see.truetransact.uicomponent.CLabel();
        cboSectorCode1 = new com.see.truetransact.uicomponent.CComboBox();
        lblHealthCode = new com.see.truetransact.uicomponent.CLabel();
        cboHealthCode = new com.see.truetransact.uicomponent.CComboBox();
        lblTypeFacility = new com.see.truetransact.uicomponent.CLabel();
        cboTypeFacility = new com.see.truetransact.uicomponent.CComboBox();
        lblDistrictCode = new com.see.truetransact.uicomponent.CLabel();
        cboDistrictCode = new com.see.truetransact.uicomponent.CComboBox();
        lblPurposeCode = new com.see.truetransact.uicomponent.CLabel();
        cboPurposeCode = new com.see.truetransact.uicomponent.CComboBox();
        lblIndusCode = new com.see.truetransact.uicomponent.CLabel();
        cboIndusCode = new com.see.truetransact.uicomponent.CComboBox();
        lblWeakerSectionCode = new com.see.truetransact.uicomponent.CLabel();
        cboWeakerSectionCode = new com.see.truetransact.uicomponent.CComboBox();
        sptClassification_vertical = new com.see.truetransact.uicomponent.CSeparator();
        panCode2 = new com.see.truetransact.uicomponent.CPanel();
        lbl20Code = new com.see.truetransact.uicomponent.CLabel();
        lblRefinancingInsti = new com.see.truetransact.uicomponent.CLabel();
        cboRefinancingInsti = new com.see.truetransact.uicomponent.CComboBox();
        lblGovtSchemeCode = new com.see.truetransact.uicomponent.CLabel();
        cboGovtSchemeCode = new com.see.truetransact.uicomponent.CComboBox();
        lblAssetCode = new com.see.truetransact.uicomponent.CLabel();
        cboAssetCode = new com.see.truetransact.uicomponent.CComboBox();
        lblNPADate = new com.see.truetransact.uicomponent.CLabel();
        tdtNPADate = new com.see.truetransact.uicomponent.CDateField();
        lblDirectFinance = new com.see.truetransact.uicomponent.CLabel();
        chkDirectFinance = new com.see.truetransact.uicomponent.CCheckBox();
        lblECGC = new com.see.truetransact.uicomponent.CLabel();
        chkECGC = new com.see.truetransact.uicomponent.CCheckBox();
        lblPrioritySector = new com.see.truetransact.uicomponent.CLabel();
        chkPrioritySector = new com.see.truetransact.uicomponent.CCheckBox();
        lblDocumentcomplete = new com.see.truetransact.uicomponent.CLabel();
        chkDocumentcomplete = new com.see.truetransact.uicomponent.CCheckBox();
        lblQIS = new com.see.truetransact.uicomponent.CLabel();
        chkQIS = new com.see.truetransact.uicomponent.CCheckBox();
        cbo20Code = new com.see.truetransact.uicomponent.CComboBox();
        panShareMaintenance = new com.see.truetransact.uicomponent.CPanel();
        panShareMaintenance_Table = new com.see.truetransact.uicomponent.CPanel();
        panTable_Share = new com.see.truetransact.uicomponent.CPanel();
        srpShareMaintenance = new com.see.truetransact.uicomponent.CScrollPane();
        tblShareMaintenance = new com.see.truetransact.uicomponent.CTable();
        mbrTermLoan = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Term Loan Account");
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 650));

        tbrTermLoan.setMinimumSize(new java.awt.Dimension(345, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnView);

        lblSpace4.setText("     ");
        tbrTermLoan.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnDelete);

        lblSpace2.setText("     ");
        tbrTermLoan.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTermLoan.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.setEnabled(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.setEnabled(false);
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTermLoan.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnClose);

        getContentPane().add(tbrTermLoan, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panTermLoan.setMinimumSize(new java.awt.Dimension(850, 650));
        panTermLoan.setPreferredSize(new java.awt.Dimension(850, 650));
        panTermLoan.setLayout(new java.awt.GridBagLayout());

        tabLimitAmount.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabLimitAmount.setMinimumSize(new java.awt.Dimension(850, 650));
        tabLimitAmount.setPreferredSize(new java.awt.Dimension(850, 650));

        panBorrowCompanyDetails.setMinimumSize(new java.awt.Dimension(659, 545));
        panBorrowCompanyDetails.setPreferredSize(new java.awt.Dimension(659, 545));
        panBorrowCompanyDetails.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile.setBorder(javax.swing.BorderFactory.createTitledBorder("Borrower's Profile"));
        panBorrowProfile.setMinimumSize(new java.awt.Dimension(747, 325));
        panBorrowProfile.setPreferredSize(new java.awt.Dimension(747, 325));
        panBorrowProfile.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile_CustID.setMinimumSize(new java.awt.Dimension(405, 290));
        panBorrowProfile_CustID.setPreferredSize(new java.awt.Dimension(405, 290));
        panBorrowProfile_CustID.setLayout(new java.awt.GridBagLayout());

        lblOpenDate.setText("Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblOpenDate, gridBagConstraints);

        lblOpenDate2.setText("x");
        lblOpenDate2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblOpenDate2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblOpenDate2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblOpenDate2, gridBagConstraints);

        lblCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 44, 2, 4);
        panBorrowProfile_CustID.add(lblCustName, gridBagConstraints);

        lblCustName_2.setText("Prithvi Ram");
        lblCustName_2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCustName_2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCustName_2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblCustName_2, gridBagConstraints);

        lblCity_BorrowerProfile.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblCity_BorrowerProfile, gridBagConstraints);

        lblCity_BorrowerProfile_2.setText("Bangalore");
        lblCity_BorrowerProfile_2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCity_BorrowerProfile_2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCity_BorrowerProfile_2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblCity_BorrowerProfile_2, gridBagConstraints);

        lblState_BorrowerProfile.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblState_BorrowerProfile, gridBagConstraints);

        lblState_BorrowerProfile_2.setText("Karnataka");
        lblState_BorrowerProfile_2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblState_BorrowerProfile_2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblState_BorrowerProfile_2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblState_BorrowerProfile_2, gridBagConstraints);

        lblPin_BorrowerProfile.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblPin_BorrowerProfile, gridBagConstraints);

        lblPin_BorrowerProfile_2.setText("560 025");
        lblPin_BorrowerProfile_2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblPin_BorrowerProfile_2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblPin_BorrowerProfile_2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblPin_BorrowerProfile_2, gridBagConstraints);

        lblPhone_BorrowerProfile.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblPhone_BorrowerProfile, gridBagConstraints);

        lblPhone_BorrowerProfile_2.setText("2434433");
        lblPhone_BorrowerProfile_2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblPhone_BorrowerProfile_2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblPhone_BorrowerProfile_2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblPhone_BorrowerProfile_2, gridBagConstraints);

        lblFax_BorrowerProfile.setText("Fax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblFax_BorrowerProfile, gridBagConstraints);

        lblFax_BorrowerProfile_2.setText("1");
        lblFax_BorrowerProfile_2.setMaximumSize(new java.awt.Dimension(100, 21));
        lblFax_BorrowerProfile_2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblFax_BorrowerProfile_2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBorrowProfile_CustID.add(lblFax_BorrowerProfile_2, gridBagConstraints);

        panBorrowerTabCTable.setMinimumSize(new java.awt.Dimension(375, 120));
        panBorrowerTabCTable.setPreferredSize(new java.awt.Dimension(375, 120));
        panBorrowerTabCTable.setLayout(new java.awt.GridBagLayout());

        srpBorrowerTabCTable.setMinimumSize(new java.awt.Dimension(375, 120));
        srpBorrowerTabCTable.setPreferredSize(new java.awt.Dimension(375, 120));

        tblBorrowerTabCTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblBorrowerTabCTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBorrowerTabCTableMousePressed(evt);
            }
        });
        srpBorrowerTabCTable.setViewportView(tblBorrowerTabCTable);

        panBorrowerTabCTable.add(srpBorrowerTabCTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBorrowProfile_CustID.add(panBorrowerTabCTable, gridBagConstraints);

        panBorrowerTabTools.setMinimumSize(new java.awt.Dimension(228, 33));
        panBorrowerTabTools.setPreferredSize(new java.awt.Dimension(228, 33));
        panBorrowerTabTools.setLayout(new java.awt.GridBagLayout());

        btnNew_Borrower.setText("New");
        btnNew_Borrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_BorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBorrowerTabTools.add(btnNew_Borrower, gridBagConstraints);

        btnToMain_Borrower.setText("To Main");
        btnToMain_Borrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToMain_BorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBorrowerTabTools.add(btnToMain_Borrower, gridBagConstraints);

        btnDeleteBorrower.setText("Delete");
        btnDeleteBorrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBorrowerTabTools.add(btnDeleteBorrower, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBorrowProfile_CustID.add(panBorrowerTabTools, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panBorrowProfile.add(panBorrowProfile_CustID, gridBagConstraints);

        panBorrowProfile_CustName.setMinimumSize(new java.awt.Dimension(303, 290));
        panBorrowProfile_CustName.setPreferredSize(new java.awt.Dimension(303, 290));
        panBorrowProfile_CustName.setLayout(new java.awt.GridBagLayout());

        cboConstitution.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboConstitution.setMinimumSize(new java.awt.Dimension(100, 21));
        cboConstitution.setPopupWidth(235);
        cboConstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConstitutionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(cboConstitution, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblCategory, gridBagConstraints);

        lblConstitution.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblConstitution, gridBagConstraints);

        lblReferences.setText("References");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblReferences, gridBagConstraints);

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(cboCategory, gridBagConstraints);

        txtReferences.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(txtReferences, gridBagConstraints);

        tdtDealingWithBankSince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDealingWithBankSinceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(tdtDealingWithBankSince, gridBagConstraints);

        lblDealingWithBankSince.setText("Dealing With the Bank Since");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblDealingWithBankSince, gridBagConstraints);

        lblRiskRating.setText("Risk Rating");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblRiskRating, gridBagConstraints);

        txtRiskRating.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(txtRiskRating, gridBagConstraints);

        lblNatureBusiness.setText("Nature of Business");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblNatureBusiness, gridBagConstraints);

        cboNatureBusiness.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboNatureBusiness.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(cboNatureBusiness, gridBagConstraints);

        lblRemarks__CompDetail.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblRemarks__CompDetail, gridBagConstraints);

        txtRemarks__CompDetail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(txtRemarks__CompDetail, gridBagConstraints);

        lblNetWorth.setText("Net Worth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblNetWorth, gridBagConstraints);

        txtNetWorth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(txtNetWorth, gridBagConstraints);

        lblAsOn.setText("As On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblAsOn, gridBagConstraints);

        tdtAsOn.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAsOn.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtAsOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtAsOnFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(tdtAsOn, gridBagConstraints);

        lblCreditFacilityAvailSince.setText("Credit Facilities Availed Since");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblCreditFacilityAvailSince, gridBagConstraints);

        tdtCreditFacilityAvailSince.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtCreditFacilityAvailSince.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtCreditFacilityAvailSince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCreditFacilityAvailSinceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(tdtCreditFacilityAvailSince, gridBagConstraints);

        lblBorrowerNo.setText("Borrower No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panBorrowProfile_CustName.add(lblBorrowerNo, gridBagConstraints);

        lblBorrowerNo_2.setMaximumSize(new java.awt.Dimension(100, 16));
        lblBorrowerNo_2.setMinimumSize(new java.awt.Dimension(100, 16));
        lblBorrowerNo_2.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBorrowProfile_CustName.add(lblBorrowerNo_2, gridBagConstraints);

        txtCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustIDActionPerformed(evt);
            }
        });
        txtCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panBorrowProfile_CustName.add(txtCustID, gridBagConstraints);

        lblCustID.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 4);
        panBorrowProfile_CustName.add(lblCustID, gridBagConstraints);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID.setToolTipText("Select Customer");
        btnCustID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustID.setMaximumSize(new java.awt.Dimension(35, 25));
        btnCustID.setMinimumSize(new java.awt.Dimension(35, 25));
        btnCustID.setPreferredSize(new java.awt.Dimension(35, 21));
        btnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBorrowProfile_CustName.add(btnCustID, gridBagConstraints);

        sptBorroewrProfile.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panBorrowProfile_CustName.add(sptBorroewrProfile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panBorrowProfile.add(panBorrowProfile_CustName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panBorrowCompanyDetails.add(panBorrowProfile, gridBagConstraints);

        panCompanyDetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panCompanyDetails.setLayout(new java.awt.GridBagLayout());

        panCompanyDetailsTrash.setBorder(javax.swing.BorderFactory.createTitledBorder("Company Details"));
        panCompanyDetailsTrash.setMinimumSize(new java.awt.Dimension(747, 125));
        panCompanyDetailsTrash.setPreferredSize(new java.awt.Dimension(747, 125));
        panCompanyDetailsTrash.setLayout(new java.awt.GridBagLayout());

        panCompanyDetails_Company.setMinimumSize(new java.awt.Dimension(500, 100));
        panCompanyDetails_Company.setPreferredSize(new java.awt.Dimension(500, 100));
        panCompanyDetails_Company.setLayout(new java.awt.GridBagLayout());

        lblCompanyRegisNo.setText("Company Registration No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panCompanyDetails_Company.add(lblCompanyRegisNo, gridBagConstraints);

        txtCompanyRegisNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(txtCompanyRegisNo, gridBagConstraints);

        lblDateEstablishment.setText("Date of Establishment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panCompanyDetails_Company.add(lblDateEstablishment, gridBagConstraints);

        tdtDateEstablishment.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDateEstablishment.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtDateEstablishment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateEstablishmentFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(tdtDateEstablishment, gridBagConstraints);

        lblChiefExecutiveName.setText("Chief Executive's Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panCompanyDetails_Company.add(lblChiefExecutiveName, gridBagConstraints);

        txtChiefExecutiveName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(txtChiefExecutiveName, gridBagConstraints);

        lblAddressType.setText("Address Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panCompanyDetails_Company.add(lblAddressType, gridBagConstraints);

        cboAddressType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboAddressType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(cboAddressType, gridBagConstraints);

        lblStreet_CompDetail.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 24, 1, 4);
        panCompanyDetails_Company.add(lblStreet_CompDetail, gridBagConstraints);

        txtStreet_CompDetail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(txtStreet_CompDetail, gridBagConstraints);

        lblArea_CompDetail.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 24, 1, 4);
        panCompanyDetails_Company.add(lblArea_CompDetail, gridBagConstraints);

        txtArea_CompDetail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(txtArea_CompDetail, gridBagConstraints);

        srpComp_Tab_Addr.setOrientation(javax.swing.SwingConstants.VERTICAL);
        srpComp_Tab_Addr.setPreferredSize(new java.awt.Dimension(3, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 24, 1, 4);
        panCompanyDetails_Company.add(srpComp_Tab_Addr, gridBagConstraints);

        lblCity_CompDetail.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 24, 1, 4);
        panCompanyDetails_Company.add(lblCity_CompDetail, gridBagConstraints);

        cboCity_CompDetail.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCity_CompDetail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(cboCity_CompDetail, gridBagConstraints);

        lblState_CompDetail.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 24, 1, 4);
        panCompanyDetails_Company.add(lblState_CompDetail, gridBagConstraints);

        cboState_CompDetail.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyDetails_Company.add(cboState_CompDetail, gridBagConstraints);

        sptCompanyDetails.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptCompanyDetails.setPreferredSize(new java.awt.Dimension(3, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 24, 1, 4);
        panCompanyDetails_Company.add(sptCompanyDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCompanyDetailsTrash.add(panCompanyDetails_Company, gridBagConstraints);

        panCompanyDetails_Addr.setMinimumSize(new java.awt.Dimension(175, 80));
        panCompanyDetails_Addr.setPreferredSize(new java.awt.Dimension(175, 80));
        panCompanyDetails_Addr.setLayout(new java.awt.GridBagLayout());

        lblCountry_CompDetail.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCompanyDetails_Addr.add(lblCountry_CompDetail, gridBagConstraints);

        cboCountry_CompDetail.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCountry_CompDetail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCompanyDetails_Addr.add(cboCountry_CompDetail, gridBagConstraints);

        lblPin_CompDetail.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCompanyDetails_Addr.add(lblPin_CompDetail, gridBagConstraints);

        txtPin_CompDetail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCompanyDetails_Addr.add(txtPin_CompDetail, gridBagConstraints);

        lblPhone_CompDetail.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCompanyDetails_Addr.add(lblPhone_CompDetail, gridBagConstraints);

        txtPhone_CompDetail.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCompanyDetails_Addr.add(txtPhone_CompDetail, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCompanyDetailsTrash.add(panCompanyDetails_Addr, gridBagConstraints);

        panCompanyDetails.add(panCompanyDetailsTrash, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panBorrowCompanyDetails.add(panCompanyDetails, gridBagConstraints);

        tabLimitAmount.addTab("Borrower & Company Details", panBorrowCompanyDetails);

        panSanctionDetails.setMinimumSize(new java.awt.Dimension(820, 550));
        panSanctionDetails.setPreferredSize(new java.awt.Dimension(820, 550));
        panSanctionDetails.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Upper.setMinimumSize(new java.awt.Dimension(475, 140));
        panSanctionDetails_Upper.setPreferredSize(new java.awt.Dimension(475, 140));
        panSanctionDetails_Upper.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Sanction.setMinimumSize(new java.awt.Dimension(179, 87));
        panSanctionDetails_Sanction.setPreferredSize(new java.awt.Dimension(179, 87));
        panSanctionDetails_Sanction.setLayout(new java.awt.GridBagLayout());

        lblSanctionDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblSanctionDate, gridBagConstraints);

        tdtSanctionDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSanctionDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(tdtSanctionDate, gridBagConstraints);

        txtSanctionSlNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(txtSanctionSlNo, gridBagConstraints);

        lblSanctionSlNo.setText("Sl No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblSanctionSlNo, gridBagConstraints);

        lblSanctionNo.setText("Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblSanctionNo, gridBagConstraints);

        txtSanctionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(txtSanctionNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Upper.add(panSanctionDetails_Sanction, gridBagConstraints);

        panSanctionDetails_Mode.setMinimumSize(new java.awt.Dimension(268, 87));
        panSanctionDetails_Mode.setPreferredSize(new java.awt.Dimension(268, 87));
        panSanctionDetails_Mode.setLayout(new java.awt.GridBagLayout());

        lblModeSanction.setText("Mode of Sanction Communication");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Mode.add(lblModeSanction, gridBagConstraints);

        cboModeSanction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboModeSanction.setMinimumSize(new java.awt.Dimension(100, 21));
        cboModeSanction.setPopupWidth(115);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Mode.add(cboModeSanction, gridBagConstraints);

        txtSanctionRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Mode.add(txtSanctionRemarks, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Mode.add(lblRemarks, gridBagConstraints);

        lblSanctioningAuthority.setText("Sanctioning Authority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Mode.add(lblSanctioningAuthority, gridBagConstraints);

        cboSanctioningAuthority.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSanctioningAuthority.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSanctioningAuthority.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Mode.add(cboSanctioningAuthority, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Upper.add(panSanctionDetails_Mode, gridBagConstraints);

        panButton2_SD.setMinimumSize(new java.awt.Dimension(215, 33));
        panButton2_SD.setPreferredSize(new java.awt.Dimension(215, 33));
        panButton2_SD.setLayout(new java.awt.GridBagLayout());

        btnNew2_SD.setText("New");
        btnNew2_SD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew2_SDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD.add(btnNew2_SD, gridBagConstraints);

        btnSave2_SD.setText("Save");
        btnSave2_SD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave2_SDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD.add(btnSave2_SD, gridBagConstraints);

        btnDelete2_SD.setText("Delete");
        btnDelete2_SD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete2_SDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD.add(btnDelete2_SD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Upper.add(panButton2_SD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails.add(panSanctionDetails_Upper, gridBagConstraints);

        panSanctionDetails_Table.setBorder(javax.swing.BorderFactory.createTitledBorder("Facility Details"));
        panSanctionDetails_Table.setMinimumSize(new java.awt.Dimension(775, 310));
        panSanctionDetails_Table.setPreferredSize(new java.awt.Dimension(775, 310));
        panSanctionDetails_Table.setLayout(new java.awt.GridBagLayout());

        panTableFields_SD.setMinimumSize(new java.awt.Dimension(475, 300));
        panTableFields_SD.setPreferredSize(new java.awt.Dimension(475, 300));
        panTableFields_SD.setLayout(new java.awt.GridBagLayout());

        lblTypeOfFacility.setText("Type of Facility");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblTypeOfFacility, gridBagConstraints);

        cboTypeOfFacility.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboTypeOfFacility.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTypeOfFacility.setPopupWidth(150);
        cboTypeOfFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTypeOfFacilityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(cboTypeOfFacility, gridBagConstraints);

        lblLimit_SD.setText("Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblLimit_SD, gridBagConstraints);

        txtLimit_SD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLimit_SD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLimit_SDActionPerformed(evt);
            }
        });
        txtLimit_SD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLimit_SDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(txtLimit_SD, gridBagConstraints);

        lblFDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblFDate, gridBagConstraints);

        tdtFDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFDateFocusLost(evt);
            }
        });
        tdtFDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tdtFDateMouseExited(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(tdtFDate, gridBagConstraints);

        lblTDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblTDate, gridBagConstraints);

        tdtTDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtTDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtTDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(tdtTDate, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnNew1.setText("New");
        btnNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnNew1, gridBagConstraints);

        btnSave1.setText("Save");
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnSave1, gridBagConstraints);

        btnDelete1.setText("Delete");
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnDelete1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields_SD.add(panButton, gridBagConstraints);

        lblNoInstallments.setText("No. of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblNoInstallments, gridBagConstraints);

        txtNoInstallments.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoInstallments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoInstallmentsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(txtNoInstallments, gridBagConstraints);

        lblRepayFreq.setText("Repay Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblRepayFreq, gridBagConstraints);

        cboRepayFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepayFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepayFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepayFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(cboRepayFreq, gridBagConstraints);

        cboProductId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(250);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(cboProductId, gridBagConstraints);

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblProductId, gridBagConstraints);

        lblAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblAccHead, gridBagConstraints);

        lblAccHead_2.setMaximumSize(new java.awt.Dimension(100, 15));
        lblAccHead_2.setMinimumSize(new java.awt.Dimension(100, 15));
        lblAccHead_2.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblAccHead_2, gridBagConstraints);

        lblAcctNo_Sanction.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblAcctNo_Sanction, gridBagConstraints);

        lblAcctNo_Sanction_Disp.setText("LA0000000034324");
        lblAcctNo_Sanction_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblAcctNo_Sanction_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblAcctNo_Sanction_Disp, gridBagConstraints);

        lblFacility_Repay_Date.setText("Repayment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblFacility_Repay_Date, gridBagConstraints);

        tdtFacility_Repay_Date.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFacility_Repay_Date.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(tdtFacility_Repay_Date, gridBagConstraints);

        chkMoratorium_Given.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkMoratorium_GivenStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panTableFields_SD.add(chkMoratorium_Given, gridBagConstraints);

        lblMoratorium_Given.setText("Moratorium to be Given");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblMoratorium_Given, gridBagConstraints);

        lblFacility_Moratorium_Period.setText("Moratorium Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblFacility_Moratorium_Period, gridBagConstraints);

        txtFacility_Moratorium_Period.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFacility_Moratorium_Period.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFacility_Moratorium_PeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(txtFacility_Moratorium_Period, gridBagConstraints);

        lblPeriodDifference.setText("Period Difference");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_SD.add(lblPeriodDifference, gridBagConstraints);

        panPeriodDifference.setMinimumSize(new java.awt.Dimension(135, 20));
        panPeriodDifference.setPreferredSize(new java.awt.Dimension(135, 20));
        panPeriodDifference.setLayout(new java.awt.GridBagLayout());

        txtPeriodDifference_Years.setMinimumSize(new java.awt.Dimension(20, 20));
        txtPeriodDifference_Years.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panPeriodDifference.add(txtPeriodDifference_Years, gridBagConstraints);

        lblPeriodDifference_Years.setText("YY");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriodDifference.add(lblPeriodDifference_Years, gridBagConstraints);

        txtPeriodDifference_Months.setMinimumSize(new java.awt.Dimension(20, 20));
        txtPeriodDifference_Months.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriodDifference.add(txtPeriodDifference_Months, gridBagConstraints);

        lblPeriodDifference_Months.setText("MM");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriodDifference.add(lblPeriodDifference_Months, gridBagConstraints);

        txtPeriodDifference_Days.setMinimumSize(new java.awt.Dimension(20, 20));
        txtPeriodDifference_Days.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriodDifference.add(txtPeriodDifference_Days, gridBagConstraints);

        lblPeriodDifference_Days.setText("DD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriodDifference.add(lblPeriodDifference_Days, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 0);
        panTableFields_SD.add(panPeriodDifference, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Table.add(panTableFields_SD, gridBagConstraints);

        panTable_SD.setMinimumSize(new java.awt.Dimension(330, 300));
        panTable_SD.setPreferredSize(new java.awt.Dimension(330, 300));
        panTable_SD.setLayout(new java.awt.GridBagLayout());

        srpTable_SD.setMinimumSize(new java.awt.Dimension(330, 300));
        srpTable_SD.setPreferredSize(new java.awt.Dimension(330, 300));

        tblSanctionDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSanctionDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSanctionDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanctionDetailsMousePressed(evt);
            }
        });
        srpTable_SD.setViewportView(tblSanctionDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panTable_SD.add(srpTable_SD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Table.add(panTable_SD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails.add(panSanctionDetails_Table, gridBagConstraints);

        panTable2_SD.setMinimumSize(new java.awt.Dimension(330, 140));
        panTable2_SD.setPreferredSize(new java.awt.Dimension(330, 140));
        panTable2_SD.setLayout(new java.awt.GridBagLayout());

        srpTable2_SD.setMinimumSize(new java.awt.Dimension(330, 140));
        srpTable2_SD.setPreferredSize(new java.awt.Dimension(330, 140));

        tblSanctionDetails2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSanctionDetails2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanctionDetails2MousePressed(evt);
            }
        });
        srpTable2_SD.setViewportView(tblSanctionDetails2);

        panTable2_SD.add(srpTable2_SD, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails.add(panTable2_SD, gridBagConstraints);

        tabLimitAmount.addTab("Sanction Details", panSanctionDetails);

        panFacilityDetails.setMinimumSize(new java.awt.Dimension(795, 550));
        panFacilityDetails.setPreferredSize(new java.awt.Dimension(795, 550));
        panFacilityDetails.setLayout(new java.awt.GridBagLayout());

        panFacilityDetails_Data.setMinimumSize(new java.awt.Dimension(800, 475));
        panFacilityDetails_Data.setPreferredSize(new java.awt.Dimension(800, 475));
        panFacilityDetails_Data.setLayout(new java.awt.GridBagLayout());

        panFDAccount.setMinimumSize(new java.awt.Dimension(360, 475));
        panFDAccount.setPreferredSize(new java.awt.Dimension(360, 475));
        panFDAccount.setLayout(new java.awt.GridBagLayout());

        panSecurityDetails_FD.setBorder(javax.swing.BorderFactory.createTitledBorder("Security Details"));
        panSecurityDetails_FD.setMinimumSize(new java.awt.Dimension(140, 100));
        panSecurityDetails_FD.setPreferredSize(new java.awt.Dimension(140, 100));
        panSecurityDetails_FD.setLayout(new java.awt.GridBagLayout());

        rdoSecurityDetails.add(rdoSecurityDetails_Unsec);
        rdoSecurityDetails_Unsec.setText("Unsecured");
        rdoSecurityDetails_Unsec.setMinimumSize(new java.awt.Dimension(89, 16));
        rdoSecurityDetails_Unsec.setPreferredSize(new java.awt.Dimension(89, 16));
        rdoSecurityDetails_Unsec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSecurityDetails_UnsecActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityDetails_FD.add(rdoSecurityDetails_Unsec, gridBagConstraints);

        rdoSecurityDetails.add(rdoSecurityDetails_Partly);
        rdoSecurityDetails_Partly.setText("Partly Secured");
        rdoSecurityDetails_Partly.setMinimumSize(new java.awt.Dimension(123, 16));
        rdoSecurityDetails_Partly.setPreferredSize(new java.awt.Dimension(123, 16));
        rdoSecurityDetails_Partly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSecurityDetails_PartlyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityDetails_FD.add(rdoSecurityDetails_Partly, gridBagConstraints);

        rdoSecurityDetails.add(rdoSecurityDetails_Fully);
        rdoSecurityDetails_Fully.setText("Fully Secured");
        rdoSecurityDetails_Fully.setMinimumSize(new java.awt.Dimension(105, 16));
        rdoSecurityDetails_Fully.setPreferredSize(new java.awt.Dimension(105, 16));
        rdoSecurityDetails_Fully.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSecurityDetails_FullyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityDetails_FD.add(rdoSecurityDetails_Fully, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDAccount.add(panSecurityDetails_FD, gridBagConstraints);

        lblAccLimit.setText("Account Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panFDAccount.add(lblAccLimit, gridBagConstraints);

        panAccLimit.setMinimumSize(new java.awt.Dimension(175, 21));
        panAccLimit.setPreferredSize(new java.awt.Dimension(175, 21));
        panAccLimit.setLayout(new java.awt.GridBagLayout());

        rdoAccLimit.add(rdoAccLimit_Main);
        rdoAccLimit_Main.setText("Main");
        rdoAccLimit_Main.setMaximumSize(new java.awt.Dimension(74, 27));
        rdoAccLimit_Main.setMinimumSize(new java.awt.Dimension(74, 27));
        rdoAccLimit_Main.setPreferredSize(new java.awt.Dimension(74, 27));
        rdoAccLimit_Main.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        rdoAccLimit_Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAccLimit_MainActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccLimit.add(rdoAccLimit_Main, gridBagConstraints);

        rdoAccLimit.add(rdoAccLimit_Submit);
        rdoAccLimit_Submit.setText("Sub-Limit");
        rdoAccLimit_Submit.setMaximumSize(new java.awt.Dimension(95, 27));
        rdoAccLimit_Submit.setMinimumSize(new java.awt.Dimension(95, 27));
        rdoAccLimit_Submit.setPreferredSize(new java.awt.Dimension(95, 27));
        rdoAccLimit_Submit.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccLimit.add(rdoAccLimit_Submit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDAccount.add(panAccLimit, gridBagConstraints);

        lblRiskWeight.setText("Risk Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panFDAccount.add(lblRiskWeight, gridBagConstraints);

        panRiskWeight.setMinimumSize(new java.awt.Dimension(175, 21));
        panRiskWeight.setPreferredSize(new java.awt.Dimension(175, 21));
        panRiskWeight.setLayout(new java.awt.GridBagLayout());

        rdoRiskWeight.add(rdoRiskWeight_Yes);
        rdoRiskWeight_Yes.setText("Yes");
        rdoRiskWeight_Yes.setMaximumSize(new java.awt.Dimension(74, 27));
        rdoRiskWeight_Yes.setMinimumSize(new java.awt.Dimension(74, 27));
        rdoRiskWeight_Yes.setPreferredSize(new java.awt.Dimension(60, 27));
        panRiskWeight.add(rdoRiskWeight_Yes, new java.awt.GridBagConstraints());

        rdoRiskWeight.add(rdoRiskWeight_No);
        rdoRiskWeight_No.setText("No");
        rdoRiskWeight_No.setMaximumSize(new java.awt.Dimension(95, 27));
        rdoRiskWeight_No.setMinimumSize(new java.awt.Dimension(95, 27));
        rdoRiskWeight_No.setPreferredSize(new java.awt.Dimension(95, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRiskWeight.add(rdoRiskWeight_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDAccount.add(panRiskWeight, gridBagConstraints);

        panDemandPromssoryDate.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Demand Promissory Note", 0, 0, new java.awt.Font("Dialog", 0, 11))); // NOI18N
        panDemandPromssoryDate.setMinimumSize(new java.awt.Dimension(260, 85));
        panDemandPromssoryDate.setPreferredSize(new java.awt.Dimension(260, 85));
        panDemandPromssoryDate.setLayout(new java.awt.GridBagLayout());

        lblDemandPromNoteDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 54, 4, 4);
        panDemandPromssoryDate.add(lblDemandPromNoteDate, gridBagConstraints);

        tdtDemandPromNoteDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDemandPromNoteDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtDemandPromNoteDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDemandPromNoteDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDemandPromssoryDate.add(tdtDemandPromNoteDate, gridBagConstraints);

        lblDemandPromNoteExpDate.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 54, 4, 4);
        panDemandPromssoryDate.add(lblDemandPromNoteExpDate, gridBagConstraints);

        tdtDemandPromNoteExpDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDemandPromNoteExpDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtDemandPromNoteExpDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDemandPromNoteExpDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDemandPromssoryDate.add(tdtDemandPromNoteExpDate, gridBagConstraints);

        lblBlank1.setMinimumSize(new java.awt.Dimension(12, 15));
        lblBlank1.setPreferredSize(new java.awt.Dimension(12, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDemandPromssoryDate.add(lblBlank1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panFDAccount.add(panDemandPromssoryDate, gridBagConstraints);

        panFacilityProdID.setMinimumSize(new java.awt.Dimension(430, 200));
        panFacilityProdID.setPreferredSize(new java.awt.Dimension(430, 200));
        panFacilityProdID.setLayout(new java.awt.GridBagLayout());

        lblProductID_FD.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panFacilityProdID.add(lblProductID_FD, gridBagConstraints);

        lblAccountHead_FD.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(lblAccountHead_FD, gridBagConstraints);

        lblAccountHead_FD_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblAccountHead_FD_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        lblAccountHead_FD_Disp.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(lblAccountHead_FD_Disp, gridBagConstraints);

        lblProductID_FD_Disp.setMaximumSize(new java.awt.Dimension(100, 100));
        lblProductID_FD_Disp.setMinimumSize(new java.awt.Dimension(121, 15));
        lblProductID_FD_Disp.setPreferredSize(new java.awt.Dimension(121, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(lblProductID_FD_Disp, gridBagConstraints);

        lblAcctNo_FD.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(lblAcctNo_FD, gridBagConstraints);

        lblAcctNo_FD_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblAcctNo_FD_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(lblAcctNo_FD_Disp, gridBagConstraints);

        lblAccStatus.setText("Account Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(lblAccStatus, gridBagConstraints);

        cboAccStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboAccStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(cboAccStatus, gridBagConstraints);

        lblAccOpenDt.setText("Account Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(lblAccOpenDt, gridBagConstraints);

        tdtAccountOpenDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAccountOpenDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityProdID.add(tdtAccountOpenDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 44, 4, 20);
        panFDAccount.add(panFacilityProdID, gridBagConstraints);

        panFacilityChkBoxes.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panFacilityChkBoxes.setMinimumSize(new java.awt.Dimension(154, 90));
        panFacilityChkBoxes.setPreferredSize(new java.awt.Dimension(154, 90));
        panFacilityChkBoxes.setLayout(new java.awt.GridBagLayout());

        chkInsurance.setText("Insurance");
        chkInsurance.setMaximumSize(new java.awt.Dimension(63, 21));
        chkInsurance.setMinimumSize(new java.awt.Dimension(83, 16));
        chkInsurance.setPreferredSize(new java.awt.Dimension(83, 16));
        chkInsurance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkInsuranceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityChkBoxes.add(chkInsurance, gridBagConstraints);

        chkGurantor.setText("Guarantor");
        chkGurantor.setMinimumSize(new java.awt.Dimension(69, 16));
        chkGurantor.setPreferredSize(new java.awt.Dimension(119, 16));
        chkGurantor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGurantorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityChkBoxes.add(chkGurantor, gridBagConstraints);

        chkStockInspect.setText("Stock / Inspection");
        chkStockInspect.setMinimumSize(new java.awt.Dimension(134, 16));
        chkStockInspect.setPreferredSize(new java.awt.Dimension(134, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityChkBoxes.add(chkStockInspect, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 6, 4);
        panFDAccount.add(panFacilityChkBoxes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityDetails_Data.add(panFDAccount, gridBagConstraints);

        sptFacilityDetails_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptFacilityDetails_Vert.setMinimumSize(new java.awt.Dimension(5, 0));
        sptFacilityDetails_Vert.setPreferredSize(new java.awt.Dimension(5, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityDetails_Data.add(sptFacilityDetails_Vert, gridBagConstraints);

        panFDDate.setMaximumSize(new java.awt.Dimension(350, 500));
        panFDDate.setMinimumSize(new java.awt.Dimension(350, 500));
        panFDDate.setPreferredSize(new java.awt.Dimension(350, 500));
        panFDDate.setLayout(new java.awt.GridBagLayout());

        lblAODDate.setText("AOD Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblAODDate, gridBagConstraints);

        tdtAODDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAODDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtAODDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtAODDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(tdtAODDate, gridBagConstraints);

        lblMultiDisburseAllow.setText("Multi Disbursement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblMultiDisburseAllow, gridBagConstraints);

        panMultiDisburseAllow.setLayout(new java.awt.GridBagLayout());

        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_Yes);
        rdoMultiDisburseAllow_Yes.setText("Yes");
        rdoMultiDisburseAllow_Yes.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoMultiDisburseAllow_Yes.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panMultiDisburseAllow.add(rdoMultiDisburseAllow_Yes, gridBagConstraints);

        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_No);
        rdoMultiDisburseAllow_No.setText("No");
        rdoMultiDisburseAllow_No.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoMultiDisburseAllow_No.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panMultiDisburseAllow.add(rdoMultiDisburseAllow_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(panMultiDisburseAllow, gridBagConstraints);

        lblSubsidy.setText("Subsidy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblSubsidy, gridBagConstraints);

        panSubsidy.setLayout(new java.awt.GridBagLayout());

        rdoSubsidy.add(rdoSubsidy_Yes);
        rdoSubsidy_Yes.setText("Yes");
        rdoSubsidy_Yes.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoSubsidy_Yes.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panSubsidy.add(rdoSubsidy_Yes, gridBagConstraints);

        rdoSubsidy.add(rdoSubsidy_No);
        rdoSubsidy_No.setText("No");
        rdoSubsidy_No.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoSubsidy_No.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panSubsidy.add(rdoSubsidy_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(panSubsidy, gridBagConstraints);

        lblPurposeDesc.setText("Purpose Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblPurposeDesc, gridBagConstraints);

        txtPurposeDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(txtPurposeDesc, gridBagConstraints);

        lblGroupDesc.setText("Group Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblGroupDesc, gridBagConstraints);

        txtGroupDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(txtGroupDesc, gridBagConstraints);

        lblInterest.setText("Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblInterest, gridBagConstraints);

        panInterest.setMaximumSize(new java.awt.Dimension(180, 21));
        panInterest.setMinimumSize(new java.awt.Dimension(180, 21));
        panInterest.setPreferredSize(new java.awt.Dimension(180, 21));
        panInterest.setLayout(new java.awt.GridBagLayout());

        rdoInterest.add(rdoInterest_Simple);
        rdoInterest_Simple.setText("Simple");
        rdoInterest_Simple.setMaximumSize(new java.awt.Dimension(74, 27));
        rdoInterest_Simple.setMinimumSize(new java.awt.Dimension(74, 27));
        rdoInterest_Simple.setPreferredSize(new java.awt.Dimension(74, 27));
        rdoInterest_Simple.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rdoInterest_SimpleMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInterest.add(rdoInterest_Simple, gridBagConstraints);

        rdoInterest.add(rdoInterest_Compound);
        rdoInterest_Compound.setText("Compound");
        rdoInterest_Compound.setMaximumSize(new java.awt.Dimension(95, 27));
        rdoInterest_Compound.setMinimumSize(new java.awt.Dimension(95, 27));
        rdoInterest_Compound.setPreferredSize(new java.awt.Dimension(95, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInterest.add(rdoInterest_Compound, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(panInterest, gridBagConstraints);

        lblContactPerson.setText("Contact Person");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblContactPerson, gridBagConstraints);

        txtContactPerson.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(txtContactPerson, gridBagConstraints);

        lblContactPhone.setText("Contact Phone No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblContactPhone, gridBagConstraints);

        txtContactPhone.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(txtContactPhone, gridBagConstraints);

        lblRemark_FD.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(lblRemark_FD, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(txtRemarks, gridBagConstraints);

        panFacilityTools.setMinimumSize(new java.awt.Dimension(150, 33));
        panFacilityTools.setPreferredSize(new java.awt.Dimension(150, 33));
        panFacilityTools.setLayout(new java.awt.GridBagLayout());

        btnFacilitySave.setText("Save");
        btnFacilitySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacilitySaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityTools.add(btnFacilitySave, gridBagConstraints);

        btnFacilityDelete.setText("Delete");
        btnFacilityDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacilityDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityTools.add(btnFacilityDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(20, 4, 4, 4);
        panFDDate.add(panFacilityTools, gridBagConstraints);

        txtAcct_Name.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAcct_Name.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panFDDate.add(txtAcct_Name, gridBagConstraints);

        lblAcct_Name.setText("Account Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panFDDate.add(lblAcct_Name, gridBagConstraints);

        cboRecommendedByType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRecommendedByType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(cboRecommendedByType, gridBagConstraints);

        lblRecommandByType.setText("Recommended By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panFDDate.add(lblRecommandByType, gridBagConstraints);

        lblInterestType.setText("Interest Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panFDDate.add(lblInterestType, gridBagConstraints);

        cboInterestType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboInterestType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInterestType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInterestTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(cboInterestType, gridBagConstraints);

        panNatureInterest.setMaximumSize(new java.awt.Dimension(175, 21));
        panNatureInterest.setMinimumSize(new java.awt.Dimension(175, 21));
        panNatureInterest.setPreferredSize(new java.awt.Dimension(175, 21));
        panNatureInterest.setLayout(new java.awt.GridBagLayout());

        rdoNatureInterest.add(rdoNatureInterest_PLR);
        rdoNatureInterest_PLR.setText("PLR");
        rdoNatureInterest_PLR.setMaximumSize(new java.awt.Dimension(74, 27));
        rdoNatureInterest_PLR.setMinimumSize(new java.awt.Dimension(74, 25));
        rdoNatureInterest_PLR.setPreferredSize(new java.awt.Dimension(74, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNatureInterest.add(rdoNatureInterest_PLR, gridBagConstraints);

        rdoNatureInterest.add(rdoNatureInterest_NonPLR);
        rdoNatureInterest_NonPLR.setText("Non-PLR");
        rdoNatureInterest_NonPLR.setMaximumSize(new java.awt.Dimension(95, 27));
        rdoNatureInterest_NonPLR.setMinimumSize(new java.awt.Dimension(95, 27));
        rdoNatureInterest_NonPLR.setPreferredSize(new java.awt.Dimension(95, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panNatureInterest.add(rdoNatureInterest_NonPLR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFDDate.add(panNatureInterest, gridBagConstraints);

        lblNatureInterest.setText("Nature of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panFDDate.add(lblNatureInterest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityDetails_Data.add(panFDDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFacilityDetails.add(panFacilityDetails_Data, gridBagConstraints);

        tabLimitAmount.addTab("Facility Details", panFacilityDetails);

        panSecurityDetails.setMinimumSize(new java.awt.Dimension(814, 320));
        panSecurityDetails.setPreferredSize(new java.awt.Dimension(814, 320));
        panSecurityDetails.setLayout(new java.awt.GridBagLayout());

        panSecurityDetails_Acc.setMinimumSize(new java.awt.Dimension(425, 50));
        panSecurityDetails_Acc.setPreferredSize(new java.awt.Dimension(425, 50));
        panSecurityDetails_Acc.setLayout(new java.awt.GridBagLayout());

        panSecurity.setMaximumSize(new java.awt.Dimension(165, 23));
        panSecurity.setMinimumSize(new java.awt.Dimension(165, 23));
        panSecurity.setPreferredSize(new java.awt.Dimension(165, 23));
        panSecurity.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity.add(lblProdId, gridBagConstraints);

        lblProdId_Disp.setText("P0001");
        lblProdId_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblProdId_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProdId_Disp.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity.add(lblProdId_Disp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 37, 4, 4);
        panSecurityDetails_Acc.add(panSecurity, gridBagConstraints);

        panSecurityNature.setMinimumSize(new java.awt.Dimension(300, 45));
        panSecurityNature.setPreferredSize(new java.awt.Dimension(300, 45));
        panSecurityNature.setLayout(new java.awt.GridBagLayout());

        lblAccHeadSec.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityNature.add(lblAccHeadSec, gridBagConstraints);

        lblAccHeadSec_2.setText("TT");
        lblAccHeadSec_2.setMaximumSize(new java.awt.Dimension(100, 15));
        lblAccHeadSec_2.setMinimumSize(new java.awt.Dimension(100, 15));
        lblAccHeadSec_2.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityNature.add(lblAccHeadSec_2, gridBagConstraints);

        lblAccNoSec.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityNature.add(lblAccNoSec, gridBagConstraints);

        lblAccNoSec_2.setText("4325");
        lblAccNoSec_2.setMaximumSize(new java.awt.Dimension(125, 15));
        lblAccNoSec_2.setMinimumSize(new java.awt.Dimension(125, 15));
        lblAccNoSec_2.setPreferredSize(new java.awt.Dimension(125, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityNature.add(lblAccNoSec_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 31, 4, 4);
        panSecurityDetails_Acc.add(panSecurityNature, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityDetails.add(panSecurityDetails_Acc, gridBagConstraints);

        sptSecurityDetails_Hori.setMinimumSize(new java.awt.Dimension(415, 3));
        sptSecurityDetails_Hori.setPreferredSize(new java.awt.Dimension(415, 3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityDetails.add(sptSecurityDetails_Hori, gridBagConstraints);

        panSecurityDetails_security.setMinimumSize(new java.awt.Dimension(425, 180));
        panSecurityDetails_security.setPreferredSize(new java.awt.Dimension(425, 180));
        panSecurityDetails_security.setLayout(new java.awt.GridBagLayout());

        panSecDetails.setMinimumSize(new java.awt.Dimension(222, 140));
        panSecDetails.setPreferredSize(new java.awt.Dimension(222, 140));
        panSecDetails.setLayout(new java.awt.GridBagLayout());

        lblSecurityNo.setText("Security No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(lblSecurityNo, gridBagConstraints);

        txtSecurityNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(txtSecurityNo, gridBagConstraints);

        lblCustName_Security.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(lblCustName_Security, gridBagConstraints);

        lblCustID_Security.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(lblCustID_Security, gridBagConstraints);

        txtCustID_Security.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(txtCustID_Security, gridBagConstraints);

        btnCustID_Security.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID_Security.setMaximumSize(new java.awt.Dimension(35, 25));
        btnCustID_Security.setMinimumSize(new java.awt.Dimension(35, 25));
        btnCustID_Security.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustID_Security.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustID_SecurityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panSecDetails.add(btnCustID_Security, gridBagConstraints);

        btnSecurityNo_Security.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSecurityNo_Security.setMaximumSize(new java.awt.Dimension(35, 25));
        btnSecurityNo_Security.setMinimumSize(new java.awt.Dimension(35, 25));
        btnSecurityNo_Security.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSecurityNo_Security.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityNo_SecurityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panSecDetails.add(btnSecurityNo_Security, gridBagConstraints);

        lblCustName_Security_Display.setText("ABCDEFG");
        lblCustName_Security_Display.setMaximumSize(new java.awt.Dimension(100, 15));
        lblCustName_Security_Display.setMinimumSize(new java.awt.Dimension(100, 15));
        lblCustName_Security_Display.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(lblCustName_Security_Display, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(lblFromDate, gridBagConstraints);

        tdtFromDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(lblToDate, gridBagConstraints);

        tdtToDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtToDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecDetails.add(tdtToDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityDetails_security.add(panSecDetails, gridBagConstraints);

        panSecurityTools.setLayout(new java.awt.GridBagLayout());

        btnSecurityNew.setText("New");
        btnSecurityNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTools.add(btnSecurityNew, gridBagConstraints);

        btnSecuritySave.setText("Save");
        btnSecuritySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecuritySaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTools.add(btnSecuritySave, gridBagConstraints);

        btnSecurityDelete.setText("Delete");
        btnSecurityDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTools.add(btnSecurityDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 4);
        panSecurityDetails_security.add(panSecurityTools, gridBagConstraints);

        panTotalSecurity_Value.setMinimumSize(new java.awt.Dimension(187, 90));
        panTotalSecurity_Value.setPreferredSize(new java.awt.Dimension(187, 90));
        panTotalSecurity_Value.setLayout(new java.awt.GridBagLayout());

        lblSecurityValue.setText("Security Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(lblSecurityValue, gridBagConstraints);

        txtSecurityValue.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSecurityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(txtSecurityValue, gridBagConstraints);

        lblMargin.setText("Margin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(lblMargin, gridBagConstraints);

        txtMargin.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMargin.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMargin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMarginFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(txtMargin, gridBagConstraints);

        lblEligibleLoan.setText("Eligible Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(lblEligibleLoan, gridBagConstraints);

        txtEligibleLoan.setMaximumSize(new java.awt.Dimension(100, 21));
        txtEligibleLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(txtEligibleLoan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityDetails_security.add(panTotalSecurity_Value, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityDetails.add(panSecurityDetails_security, gridBagConstraints);

        panSecurityTableMain.setMinimumSize(new java.awt.Dimension(390, 220));
        panSecurityTableMain.setPreferredSize(new java.awt.Dimension(390, 220));
        panSecurityTableMain.setLayout(new java.awt.GridBagLayout());

        panSecurityTable.setMinimumSize(new java.awt.Dimension(390, 215));
        panSecurityTable.setPreferredSize(new java.awt.Dimension(390, 215));
        panSecurityTable.setLayout(new java.awt.GridBagLayout());

        srpSecurityTable.setMinimumSize(new java.awt.Dimension(390, 212));
        srpSecurityTable.setPreferredSize(new java.awt.Dimension(390, 212));

        tblSecurityTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Security No.", "Value", "Category", "As on"
            }
        ));
        tblSecurityTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSecurityTableMousePressed(evt);
            }
        });
        srpSecurityTable.setViewportView(tblSecurityTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panSecurityTable.add(srpSecurityTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panSecurityTableMain.add(panSecurityTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSecurityDetails.add(panSecurityTableMain, gridBagConstraints);

        tabLimitAmount.addTab("Security Details", panSecurityDetails);

        panGuarantorInsuranceDetails.setMinimumSize(new java.awt.Dimension(830, 540));
        panGuarantorInsuranceDetails.setPreferredSize(new java.awt.Dimension(830, 540));
        panGuarantorInsuranceDetails.setLayout(new java.awt.GridBagLayout());

        panGuarantorDetails.setMinimumSize(new java.awt.Dimension(830, 350));
        panGuarantorDetails.setPreferredSize(new java.awt.Dimension(830, 350));
        panGuarantorDetails.setLayout(new java.awt.GridBagLayout());

        panProd_GD.setMaximumSize(new java.awt.Dimension(720, 25));
        panProd_GD.setMinimumSize(new java.awt.Dimension(720, 25));
        panProd_GD.setPreferredSize(new java.awt.Dimension(720, 25));
        panProd_GD.setLayout(new java.awt.GridBagLayout());

        lblProdID_GD.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 0, 4);
        panProd_GD.add(lblProdID_GD, gridBagConstraints);

        lblProdID_GD_Disp.setText("P1000");
        lblProdID_GD_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblProdID_GD_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProdID_GD_Disp.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panProd_GD.add(lblProdID_GD_Disp, gridBagConstraints);

        lblAccHead_GD.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 0, 4);
        panProd_GD.add(lblAccHead_GD, gridBagConstraints);

        lblAccHead_GD_2.setMaximumSize(new java.awt.Dimension(110, 15));
        lblAccHead_GD_2.setMinimumSize(new java.awt.Dimension(110, 15));
        lblAccHead_GD_2.setPreferredSize(new java.awt.Dimension(110, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panProd_GD.add(lblAccHead_GD_2, gridBagConstraints);

        lblAccNo_GD.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 0, 4);
        panProd_GD.add(lblAccNo_GD, gridBagConstraints);

        lblAccNo_GD_2.setText("4321");
        lblAccNo_GD_2.setMaximumSize(new java.awt.Dimension(110, 15));
        lblAccNo_GD_2.setMinimumSize(new java.awt.Dimension(110, 15));
        lblAccNo_GD_2.setPreferredSize(new java.awt.Dimension(110, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panProd_GD.add(lblAccNo_GD_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panGuarantorDetails.add(panProd_GD, gridBagConstraints);

        panGuarantorDetail_Detail.setMinimumSize(new java.awt.Dimension(513, 240));
        panGuarantorDetail_Detail.setPreferredSize(new java.awt.Dimension(521, 240));
        panGuarantorDetail_Detail.setLayout(new java.awt.GridBagLayout());

        panGuarantor.setMinimumSize(new java.awt.Dimension(270, 235));
        panGuarantor.setPreferredSize(new java.awt.Dimension(270, 235));
        panGuarantor.setLayout(new java.awt.GridBagLayout());

        lblCustomerID_GD.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblCustomerID_GD, gridBagConstraints);

        txtCustomerID_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(txtCustomerID_GD, gridBagConstraints);

        btnCustomerID_GD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerID_GD.setToolTipText("Save");
        btnCustomerID_GD.setMaximumSize(new java.awt.Dimension(35, 25));
        btnCustomerID_GD.setMinimumSize(new java.awt.Dimension(35, 25));
        btnCustomerID_GD.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerID_GD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerID_GDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGuarantor.add(btnCustomerID_GD, gridBagConstraints);

        lblGuaranAccNo.setText("Guarantor Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblGuaranAccNo, gridBagConstraints);

        txtGuaranAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(txtGuaranAccNo, gridBagConstraints);

        lblGuaranName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblGuaranName, gridBagConstraints);

        txtGuaranName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(txtGuaranName, gridBagConstraints);

        lblDOB_GD.setText("Date of Birth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblDOB_GD, gridBagConstraints);

        lblStreet_GD.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblStreet_GD, gridBagConstraints);

        txtStreet_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(txtStreet_GD, gridBagConstraints);

        lblGuarantorNo.setText("Guarantor Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblGuarantorNo, gridBagConstraints);

        txtGuarantorNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtGuarantorNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(txtGuarantorNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(tdtDOB_GD, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblProdType, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(cboProdType, gridBagConstraints);

        cboProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(200);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(cboProdId, gridBagConstraints);

        lblProdId1.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblProdId1, gridBagConstraints);

        lblArea_GD.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(lblArea_GD, gridBagConstraints);

        txtArea_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantor.add(txtArea_GD, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setMaximumSize(new java.awt.Dimension(35, 25));
        btnAccNo.setMinimumSize(new java.awt.Dimension(35, 25));
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panGuarantor.add(btnAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantorDetail_Detail.add(panGuarantor, gridBagConstraints);

        sptGuarantorDetail_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptGuarantorDetail_Vert.setMinimumSize(new java.awt.Dimension(3, 235));
        sptGuarantorDetail_Vert.setPreferredSize(new java.awt.Dimension(3, 235));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantorDetail_Detail.add(sptGuarantorDetail_Vert, gridBagConstraints);

        panGuaranAddr.setMinimumSize(new java.awt.Dimension(224, 207));
        panGuaranAddr.setPreferredSize(new java.awt.Dimension(224, 207));
        panGuaranAddr.setLayout(new java.awt.GridBagLayout());

        lblPin_GD.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblPin_GD, gridBagConstraints);

        txtPin_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(txtPin_GD, gridBagConstraints);

        lblState_GD.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblState_GD, gridBagConstraints);

        lblCountry_GD.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblCountry_GD, gridBagConstraints);

        cboCountry_GD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCountry_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(cboCountry_GD, gridBagConstraints);

        lblPhone_GD.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblPhone_GD, gridBagConstraints);

        txtPhone_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(txtPhone_GD, gridBagConstraints);

        lblConstitution_GD.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblConstitution_GD, gridBagConstraints);

        cboConstitution_GD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Selected---" }));
        cboConstitution_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(cboConstitution_GD, gridBagConstraints);

        lblGuarantorNetWorth.setText("Guarantor's Net worth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblGuarantorNetWorth, gridBagConstraints);

        txtGuarantorNetWorth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(txtGuarantorNetWorth, gridBagConstraints);

        lblAsOn_GD.setText("As On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblAsOn_GD, gridBagConstraints);

        tdtAsOn_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAsOn_GD.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtAsOn_GD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtAsOn_GDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(tdtAsOn_GD, gridBagConstraints);

        cboState_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        cboState_GD.setPopupWidth(100);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(cboState_GD, gridBagConstraints);

        cboCity_GD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCity_GD.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCity_GD.setPopupWidth(100);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(cboCity_GD, gridBagConstraints);

        lblCity_GD.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuaranAddr.add(lblCity_GD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantorDetail_Detail.add(panGuaranAddr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panGuarantorDetails.add(panGuarantorDetail_Detail, gridBagConstraints);

        panToolBtns.setLayout(new java.awt.GridBagLayout());

        btnGuarantorNew.setText("New");
        btnGuarantorNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuarantorNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToolBtns.add(btnGuarantorNew, gridBagConstraints);

        btnGuarantorSave.setText("Save");
        btnGuarantorSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuarantorSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToolBtns.add(btnGuarantorSave, gridBagConstraints);

        btnGuarantorDelete.setText("Delete");
        btnGuarantorDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuarantorDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToolBtns.add(btnGuarantorDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 39, 0, 4);
        panGuarantorDetails.add(panToolBtns, gridBagConstraints);

        panGuarantorDetailsTable.setMinimumSize(new java.awt.Dimension(275, 250));
        panGuarantorDetailsTable.setPreferredSize(new java.awt.Dimension(275, 250));
        panGuarantorDetailsTable.setLayout(new java.awt.GridBagLayout());

        srpGuarantorTable.setMinimumSize(new java.awt.Dimension(275, 250));
        srpGuarantorTable.setPreferredSize(new java.awt.Dimension(275, 250));

        tblGuarantorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No.", "Cust ID", "Name", "A/C Head", "A/C No."
            }
        ));
        tblGuarantorTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblGuarantorTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblGuarantorTableMousePressed(evt);
            }
        });
        srpGuarantorTable.setViewportView(tblGuarantorTable);

        panGuarantorDetailsTable.add(srpGuarantorTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuarantorDetails.add(panGuarantorDetailsTable, gridBagConstraints);

        sptGuarantorDetail_Hori1.setMinimumSize(new java.awt.Dimension(800, 3));
        sptGuarantorDetail_Hori1.setPreferredSize(new java.awt.Dimension(800, 3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuarantorDetails.add(sptGuarantorDetail_Hori1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panGuarantorInsuranceDetails.add(panGuarantorDetails, gridBagConstraints);

        tabLimitAmount.addTab("Guarantor Details", panGuarantorInsuranceDetails);

        panInterMaintenance.setLayout(new java.awt.GridBagLayout());

        panInterMaintenance_Acc.setMinimumSize(new java.awt.Dimension(650, 60));
        panInterMaintenance_Acc.setPreferredSize(new java.awt.Dimension(650, 60));
        panInterMaintenance_Acc.setLayout(new java.awt.GridBagLayout());

        panProd_IM.setMaximumSize(new java.awt.Dimension(195, 52));
        panProd_IM.setMinimumSize(new java.awt.Dimension(195, 52));
        panProd_IM.setPreferredSize(new java.awt.Dimension(195, 52));
        panProd_IM.setLayout(new java.awt.GridBagLayout());

        lblProdID_IM.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panProd_IM.add(lblProdID_IM, gridBagConstraints);

        lblProdID_IM_Disp.setText("            ");
        lblProdID_IM_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblProdID_IM_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProdID_IM_Disp.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_IM.add(lblProdID_IM_Disp, gridBagConstraints);

        lblIntGetFrom.setText("Interest Get From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panProd_IM.add(lblIntGetFrom, gridBagConstraints);

        cboIntGetFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboIntGetFrom.setMaximumSize(new java.awt.Dimension(100, 21));
        cboIntGetFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIntGetFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntGetFromActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_IM.add(cboIntGetFrom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 30, 4);
        panInterMaintenance_Acc.add(panProd_IM, gridBagConstraints);

        panAcc_IM.setMinimumSize(new java.awt.Dimension(303, 52));
        panAcc_IM.setPreferredSize(new java.awt.Dimension(303, 52));
        panAcc_IM.setLayout(new java.awt.GridBagLayout());

        lblAccHead_IM.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 6);
        panAcc_IM.add(lblAccHead_IM, gridBagConstraints);

        lblAccHead_IM_2.setText("SBI");
        lblAccHead_IM_2.setMaximumSize(new java.awt.Dimension(146, 15));
        lblAccHead_IM_2.setMinimumSize(new java.awt.Dimension(146, 15));
        lblAccHead_IM_2.setPreferredSize(new java.awt.Dimension(146, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcc_IM.add(lblAccHead_IM_2, gridBagConstraints);

        lblAccNo_IM.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 6);
        panAcc_IM.add(lblAccNo_IM, gridBagConstraints);

        lblAccNo_IM_2.setText("4321");
        lblAccNo_IM_2.setMaximumSize(new java.awt.Dimension(146, 15));
        lblAccNo_IM_2.setMinimumSize(new java.awt.Dimension(146, 15));
        lblAccNo_IM_2.setPreferredSize(new java.awt.Dimension(146, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcc_IM.add(lblAccNo_IM_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 25, 30, 4);
        panInterMaintenance_Acc.add(panAcc_IM, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 53, 4, 4);
        panInterMaintenance.add(panInterMaintenance_Acc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterMaintenance.add(sptInterMaintenance_Hori, gridBagConstraints);

        panInterMaintenance_Details.setMinimumSize(new java.awt.Dimension(650, 77));
        panInterMaintenance_Details.setPreferredSize(new java.awt.Dimension(650, 77));
        panInterMaintenance_Details.setLayout(new java.awt.GridBagLayout());

        panLimit.setMinimumSize(new java.awt.Dimension(150, 46));
        panLimit.setPreferredSize(new java.awt.Dimension(150, 46));
        panLimit.setLayout(new java.awt.GridBagLayout());

        lblLimitAmt.setText("Limit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimit.add(lblLimitAmt, gridBagConstraints);

        lblLimitAmt_2.setText("100000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimit.add(lblLimitAmt_2, gridBagConstraints);

        lblPLR_Limit.setText("PLR");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimit.add(lblPLR_Limit, gridBagConstraints);

        lblPLR_Limit_2.setText("14");
        lblPLR_Limit_2.setMaximumSize(new java.awt.Dimension(36, 15));
        lblPLR_Limit_2.setMinimumSize(new java.awt.Dimension(36, 15));
        lblPLR_Limit_2.setPreferredSize(new java.awt.Dimension(36, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimit.add(lblPLR_Limit_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panInterMaintenance_Details.add(panLimit, gridBagConstraints);

        panDate.setMinimumSize(new java.awt.Dimension(280, 46));
        panDate.setPreferredSize(new java.awt.Dimension(280, 46));
        panDate.setLayout(new java.awt.GridBagLayout());

        lblSancDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(lblSancDate, gridBagConstraints);

        lblSancDate_2.setText("24/07/2002");
        lblSancDate_2.setMaximumSize(new java.awt.Dimension(146, 15));
        lblSancDate_2.setMinimumSize(new java.awt.Dimension(146, 15));
        lblSancDate_2.setPreferredSize(new java.awt.Dimension(146, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(lblSancDate_2, gridBagConstraints);

        lblExpiryDate.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(lblExpiryDate, gridBagConstraints);

        lblExpiryDate_2.setText("23/07/2003");
        lblExpiryDate_2.setMaximumSize(new java.awt.Dimension(146, 15));
        lblExpiryDate_2.setMinimumSize(new java.awt.Dimension(146, 15));
        lblExpiryDate_2.setPreferredSize(new java.awt.Dimension(146, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(lblExpiryDate_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterMaintenance_Details.add(panDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 53, 4, 4);
        panInterMaintenance.add(panInterMaintenance_Details, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterMaintenance.add(sptInterMaintenance_Hori2, gridBagConstraints);

        panInterMaintenance_Table.setLayout(new java.awt.GridBagLayout());

        panTableFields.setLayout(new java.awt.GridBagLayout());

        lblFrom.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblFrom, gridBagConstraints);

        tdtFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFrom.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(tdtFrom, gridBagConstraints);

        lblTo.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblTo, gridBagConstraints);

        tdtTo.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtTo.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(tdtTo, gridBagConstraints);

        lblFromAmt.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblFromAmt, gridBagConstraints);

        txtFromAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(txtFromAmt, gridBagConstraints);

        lblToAmt.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblToAmt, gridBagConstraints);

        txtToAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(txtToAmt, gridBagConstraints);

        lblInter.setText("Interest %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblInter, gridBagConstraints);

        txtInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(txtInter, gridBagConstraints);

        lblPenalInter.setText("Penal Interest %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblPenalInter, gridBagConstraints);

        txtPenalInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(txtPenalInter, gridBagConstraints);

        lblAgainstClearingInter.setText("Against Clearing Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblAgainstClearingInter, gridBagConstraints);

        txtAgainstClearingInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(txtAgainstClearingInter, gridBagConstraints);

        lblPenalStatement.setText("Limit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblPenalStatement, gridBagConstraints);

        txtPenalStatement.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(txtPenalStatement, gridBagConstraints);

        lblInterExpLimit.setText("Interest For Expiry of Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(lblInterExpLimit, gridBagConstraints);

        txtInterExpLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(txtInterExpLimit, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnInterestMaintenanceNew.setText("New");
        btnInterestMaintenanceNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestMaintenanceNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnInterestMaintenanceNew, gridBagConstraints);

        btnInterestMaintenanceSave.setText("Save");
        btnInterestMaintenanceSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestMaintenanceSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnInterestMaintenanceSave, gridBagConstraints);

        btnInterestMaintenanceDelete.setText("Delete");
        btnInterestMaintenanceDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestMaintenanceDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnInterestMaintenanceDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields.add(panButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterMaintenance_Table.add(panTableFields, gridBagConstraints);

        panTable_IM.setLayout(new java.awt.GridBagLayout());

        srpInterMaintenance.setMinimumSize(new java.awt.Dimension(470, 293));
        srpInterMaintenance.setPreferredSize(new java.awt.Dimension(470, 293));

        tblInterMaintenance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No.", "From Date", "To Date", "From Amount", "To Amount", "Interest"
            }
        ));
        tblInterMaintenance.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 100));
        tblInterMaintenance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInterMaintenanceMousePressed(evt);
            }
        });
        srpInterMaintenance.setViewportView(tblInterMaintenance);

        panTable_IM.add(srpInterMaintenance, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterMaintenance_Table.add(panTable_IM, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterMaintenance.add(panInterMaintenance_Table, gridBagConstraints);

        tabLimitAmount.addTab("Interest Maintenance", panInterMaintenance);

        panRepaymentSchedule.setMinimumSize(new java.awt.Dimension(830, 556));
        panRepaymentSchedule.setPreferredSize(new java.awt.Dimension(830, 556));
        panRepaymentSchedule.setLayout(new java.awt.GridBagLayout());

        panRepayment.setMinimumSize(new java.awt.Dimension(580, 350));
        panRepayment.setPreferredSize(new java.awt.Dimension(580, 350));
        panRepayment.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepayment.add(sptRepatmentSchedule_Hori, gridBagConstraints);

        panRepaymentSchedule_Details.setMinimumSize(new java.awt.Dimension(566, 275));
        panRepaymentSchedule_Details.setPreferredSize(new java.awt.Dimension(568, 275));
        panRepaymentSchedule_Details.setLayout(new java.awt.GridBagLayout());

        panSchedule_RS.setMinimumSize(new java.awt.Dimension(255, 265));
        panSchedule_RS.setPreferredSize(new java.awt.Dimension(255, 265));
        panSchedule_RS.setLayout(new java.awt.GridBagLayout());

        lblScheduleNo.setText("Schedule No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblScheduleNo, gridBagConstraints);

        txtScheduleNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(txtScheduleNo, gridBagConstraints);

        lblLaonAmt.setText("Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblLaonAmt, gridBagConstraints);

        txtLaonAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLaonAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLaonAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(txtLaonAmt, gridBagConstraints);

        lblRepayType.setText("Repayment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblRepayType, gridBagConstraints);

        cboRepayType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepayType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepayType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepayTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(cboRepayType, gridBagConstraints);

        lblRepayFreq_Repayment.setText("Repayment Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblRepayFreq_Repayment, gridBagConstraints);

        cboRepayFreq_Repayment.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepayFreq_Repayment.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepayFreq_Repayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepayFreq_RepaymentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(cboRepayFreq_Repayment, gridBagConstraints);

        lblNoMonthsMora.setText("No. of Months for Moratorium");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblNoMonthsMora, gridBagConstraints);

        txtNoMonthsMora.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(txtNoMonthsMora, gridBagConstraints);

        txtNoInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoInstall.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoInstallFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(txtNoInstall, gridBagConstraints);

        lblNoInstall.setText("No. of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblNoInstall, gridBagConstraints);

        tdtFirstInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFirstInstall.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFirstInstall.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFirstInstallFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(tdtFirstInstall, gridBagConstraints);

        lblFirstInstall.setText("First Installment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblFirstInstall, gridBagConstraints);

        lblLastInstall.setText("Last Installment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblLastInstall, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(tdtLastInstall, gridBagConstraints);

        lblDisbursement_Dt.setText("Disbursement Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(lblDisbursement_Dt, gridBagConstraints);

        tdtDisbursement_Dt.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtDisbursement_Dt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDisbursement_Dt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule_RS.add(tdtDisbursement_Dt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentSchedule_Details.add(panSchedule_RS, gridBagConstraints);

        sptRepatmentSchedule_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptRepatmentSchedule_Vert.setMinimumSize(new java.awt.Dimension(2, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panRepaymentSchedule_Details.add(sptRepatmentSchedule_Vert, gridBagConstraints);

        panInstall_RS.setMinimumSize(new java.awt.Dimension(298, 242));
        panInstall_RS.setPreferredSize(new java.awt.Dimension(298, 242));
        panInstall_RS.setLayout(new java.awt.GridBagLayout());

        lblTotalBaseAmt.setText("Total Base Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblTotalBaseAmt, gridBagConstraints);

        txtTotalBaseAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(txtTotalBaseAmt, gridBagConstraints);

        lblAmtPenulInstall.setText("Amount for Penultimate Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblAmtPenulInstall, gridBagConstraints);

        txtAmtPenulInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(txtAmtPenulInstall, gridBagConstraints);

        lblAmtLastInstall.setText("Amount for Last Installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblAmtLastInstall, gridBagConstraints);

        txtAmtLastInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(txtAmtLastInstall, gridBagConstraints);

        lblTotalInstallAmt.setText("Total Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblTotalInstallAmt, gridBagConstraints);

        txtTotalInstallAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(txtTotalInstallAmt, gridBagConstraints);

        lblDoAddSIs.setText("Do You Want to Add SIs?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblDoAddSIs, gridBagConstraints);

        panDoAddSIs.setLayout(new java.awt.GridBagLayout());

        rdoDoAddSIs.add(rdoDoAddSIs_Yes);
        rdoDoAddSIs_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panDoAddSIs.add(rdoDoAddSIs_Yes, gridBagConstraints);

        rdoDoAddSIs.add(rdoDoAddSIs_No);
        rdoDoAddSIs_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panDoAddSIs.add(rdoDoAddSIs_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInstall_RS.add(panDoAddSIs, gridBagConstraints);

        lblPostDatedCheque.setText("Post Dated Cheques Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblPostDatedCheque, gridBagConstraints);

        panPostDatedCheque.setLayout(new java.awt.GridBagLayout());

        rdoPostDatedCheque.add(rdoPostDatedCheque_Yes);
        rdoPostDatedCheque_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panPostDatedCheque.add(rdoPostDatedCheque_Yes, gridBagConstraints);

        rdoPostDatedCheque.add(rdoPostDatedCheque_No);
        rdoPostDatedCheque_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panPostDatedCheque.add(rdoPostDatedCheque_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInstall_RS.add(panPostDatedCheque, gridBagConstraints);

        panStatus_Repayment.setLayout(new java.awt.GridBagLayout());

        rdoActive_Repayment.setText("Active");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panStatus_Repayment.add(rdoActive_Repayment, gridBagConstraints);

        rdoInActive_Repayment.setText("Inactive");
        rdoInActive_Repayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInActive_RepaymentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panStatus_Repayment.add(rdoInActive_Repayment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInstall_RS.add(panStatus_Repayment, gridBagConstraints);

        lblStatus_Repayment.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblStatus_Repayment, gridBagConstraints);

        lblRepayScheduleMode.setText("Schedule Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(lblRepayScheduleMode, gridBagConstraints);

        txtRepayScheduleMode.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRepayScheduleMode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstall_RS.add(txtRepayScheduleMode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentSchedule_Details.add(panInstall_RS, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepayment.add(panRepaymentSchedule_Details, gridBagConstraints);

        panProd_RS.setMinimumSize(new java.awt.Dimension(150, 23));
        panProd_RS.setPreferredSize(new java.awt.Dimension(150, 23));
        panProd_RS.setLayout(new java.awt.GridBagLayout());

        lblProdID_RS.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_RS.add(lblProdID_RS, gridBagConstraints);

        lblProdID_RS_Disp.setText("P1000");
        lblProdID_RS_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblProdID_RS_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProdID_RS_Disp.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_RS.add(lblProdID_RS_Disp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 100, 4, 4);
        panRepayment.add(panProd_RS, gridBagConstraints);

        panAcc_RS.setMinimumSize(new java.awt.Dimension(190, 42));
        panAcc_RS.setPreferredSize(new java.awt.Dimension(190, 42));
        panAcc_RS.setLayout(new java.awt.GridBagLayout());

        lblAccHead_RS.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcc_RS.add(lblAccHead_RS, gridBagConstraints);

        lblAccHead_RS_2.setMaximumSize(new java.awt.Dimension(100, 16));
        lblAccHead_RS_2.setMinimumSize(new java.awt.Dimension(100, 16));
        lblAccHead_RS_2.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcc_RS.add(lblAccHead_RS_2, gridBagConstraints);

        lblAccNo_RS.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcc_RS.add(lblAccNo_RS, gridBagConstraints);

        lblAccNo_RS_2.setText("4321");
        lblAccNo_RS_2.setMaximumSize(new java.awt.Dimension(100, 16));
        lblAccNo_RS_2.setMinimumSize(new java.awt.Dimension(100, 16));
        lblAccNo_RS_2.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcc_RS.add(lblAccNo_RS_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 25, 4, 4);
        panRepayment.add(panAcc_RS, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentSchedule.add(panRepayment, gridBagConstraints);

        panRepaymentToolBtns.setMinimumSize(new java.awt.Dimension(215, 33));
        panRepaymentToolBtns.setPreferredSize(new java.awt.Dimension(215, 33));
        panRepaymentToolBtns.setLayout(new java.awt.GridBagLayout());

        btnRepayment_New.setText("New");
        btnRepayment_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepayment_NewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentToolBtns.add(btnRepayment_New, gridBagConstraints);

        btnRepayment_Save.setText("Save");
        btnRepayment_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepayment_SaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentToolBtns.add(btnRepayment_Save, gridBagConstraints);

        btnRepayment_Delete.setText("Delete");
        btnRepayment_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepayment_DeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentToolBtns.add(btnRepayment_Delete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentSchedule.add(panRepaymentToolBtns, gridBagConstraints);

        panRepaymentCTable.setMinimumSize(new java.awt.Dimension(230, 395));
        panRepaymentCTable.setPreferredSize(new java.awt.Dimension(230, 395));
        panRepaymentCTable.setLayout(new java.awt.GridBagLayout());

        srpRepaymentCTable.setMinimumSize(new java.awt.Dimension(230, 360));
        srpRepaymentCTable.setPreferredSize(new java.awt.Dimension(230, 360));

        tblRepaymentCTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sanction No.", "Loan Amount"
            }
        ));
        tblRepaymentCTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRepaymentCTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblRepaymentCTableMousePressed(evt);
            }
        });
        srpRepaymentCTable.setViewportView(tblRepaymentCTable);

        panRepaymentCTable.add(srpRepaymentCTable, new java.awt.GridBagConstraints());

        btnEMI_Calculate.setText("EMI Calculator");
        btnEMI_Calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEMI_CalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentCTable.add(btnEMI_Calculate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRepaymentSchedule.add(panRepaymentCTable, gridBagConstraints);

        tabLimitAmount.addTab("Repayment Schedule", panRepaymentSchedule);

        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        panIsRequired.setBorder(javax.swing.BorderFactory.createTitledBorder("Is Required ?"));
        panIsRequired.setLayout(new java.awt.GridBagLayout());

        chkChequeBookAD.setText("Cheque Book");
        chkChequeBookAD.setMinimumSize(new java.awt.Dimension(100, 21));
        chkChequeBookAD.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panIsRequired.add(chkChequeBookAD, gridBagConstraints);

        chkCustGrpLimitValidationAD.setText("Cust. Group Limit Validation");
        chkCustGrpLimitValidationAD.setMinimumSize(new java.awt.Dimension(181, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panIsRequired.add(chkCustGrpLimitValidationAD, gridBagConstraints);

        chkMobileBankingAD.setText("Mobile Banking");
        chkMobileBankingAD.setMinimumSize(new java.awt.Dimension(111, 21));
        chkMobileBankingAD.setPreferredSize(new java.awt.Dimension(111, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panIsRequired.add(chkMobileBankingAD, gridBagConstraints);

        chkNROStatusAD.setText("NRO Status");
        chkNROStatusAD.setMinimumSize(new java.awt.Dimension(90, 21));
        chkNROStatusAD.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panIsRequired.add(chkNROStatusAD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        panAccountDetails.add(panIsRequired, gridBagConstraints);

        panCardInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Card Info."));
        panCardInfo.setMaximumSize(new java.awt.Dimension(497, 100));
        panCardInfo.setMinimumSize(new java.awt.Dimension(497, 100));
        panCardInfo.setPreferredSize(new java.awt.Dimension(497, 100));
        panCardInfo.setLayout(new java.awt.GridBagLayout());

        chkATMAD.setText("ATM");
        chkATMAD.setMinimumSize(new java.awt.Dimension(50, 21));
        chkATMAD.setPreferredSize(new java.awt.Dimension(50, 21));
        chkATMAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkATMADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panCardInfo.add(chkATMAD, gridBagConstraints);

        lblATMNoAD.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panCardInfo.add(lblATMNoAD, gridBagConstraints);

        txtATMNoAD.setMaxLength(16);
        txtATMNoAD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panCardInfo.add(txtATMNoAD, gridBagConstraints);

        lblATMFromDateAD.setText("Valid From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panCardInfo.add(lblATMFromDateAD, gridBagConstraints);

        tdtATMFromDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtATMFromDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panCardInfo.add(tdtATMFromDateAD, gridBagConstraints);

        lblATMToDateAD.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panCardInfo.add(lblATMToDateAD, gridBagConstraints);

        tdtATMToDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtATMToDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panCardInfo.add(tdtATMToDateAD, gridBagConstraints);

        chkDebitAD.setText("Debit");
        chkDebitAD.setMinimumSize(new java.awt.Dimension(54, 21));
        chkDebitAD.setPreferredSize(new java.awt.Dimension(54, 21));
        chkDebitAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDebitADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCardInfo.add(chkDebitAD, gridBagConstraints);

        lblDebitNoAD.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCardInfo.add(lblDebitNoAD, gridBagConstraints);

        txtDebitNoAD.setMaxLength(16);
        txtDebitNoAD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCardInfo.add(txtDebitNoAD, gridBagConstraints);

        lblDebitFromDateAD.setText("Valid From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCardInfo.add(lblDebitFromDateAD, gridBagConstraints);

        tdtDebitFromDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDebitFromDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panCardInfo.add(tdtDebitFromDateAD, gridBagConstraints);

        lblDebitToDateAD.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCardInfo.add(lblDebitToDateAD, gridBagConstraints);

        tdtDebitToDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDebitToDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panCardInfo.add(tdtDebitToDateAD, gridBagConstraints);

        chkCreditAD.setText("Credit");
        chkCreditAD.setMinimumSize(new java.awt.Dimension(59, 21));
        chkCreditAD.setPreferredSize(new java.awt.Dimension(59, 21));
        chkCreditAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCreditADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panCardInfo.add(chkCreditAD, gridBagConstraints);

        lblCreditNoAD.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panCardInfo.add(lblCreditNoAD, gridBagConstraints);

        txtCreditNoAD.setMaxLength(16);
        txtCreditNoAD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panCardInfo.add(txtCreditNoAD, gridBagConstraints);

        lblCreditFromDateAD.setText("Valid From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panCardInfo.add(lblCreditFromDateAD, gridBagConstraints);

        tdtCreditFromDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCreditFromDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 4, 4);
        panCardInfo.add(tdtCreditFromDateAD, gridBagConstraints);

        lblCreditToDateAD.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panCardInfo.add(lblCreditToDateAD, gridBagConstraints);

        tdtCreditToDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCreditToDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 4, 4);
        panCardInfo.add(tdtCreditToDateAD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        panAccountDetails.add(panCardInfo, gridBagConstraints);

        panFlexiOpt.setBorder(javax.swing.BorderFactory.createTitledBorder("Mode of Operation"));
        panFlexiOpt.setMaximumSize(new java.awt.Dimension(633, 40));
        panFlexiOpt.setMinimumSize(new java.awt.Dimension(633, 40));
        panFlexiOpt.setPreferredSize(new java.awt.Dimension(633, 40));
        panFlexiOpt.setLayout(new java.awt.GridBagLayout());

        cboSettlementModeAI.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFlexiOpt.add(cboSettlementModeAI, gridBagConstraints);

        lblSettlementModeAI.setText("Settlement Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 225, 4, 4);
        panFlexiOpt.add(lblSettlementModeAI, gridBagConstraints);

        lblOpModeAI.setText("Mode of Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFlexiOpt.add(lblOpModeAI, gridBagConstraints);

        cboOpModeAI.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFlexiOpt.add(cboOpModeAI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        panAccountDetails.add(panFlexiOpt, gridBagConstraints);

        panDiffCharges.setBorder(javax.swing.BorderFactory.createTitledBorder("Different Charges"));
        panDiffCharges.setMaximumSize(new java.awt.Dimension(483, 220));
        panDiffCharges.setMinimumSize(new java.awt.Dimension(483, 220));
        panDiffCharges.setPreferredSize(new java.awt.Dimension(483, 220));
        panDiffCharges.setLayout(new java.awt.GridBagLayout());

        lblAccOpeningChrgAD.setText("Account Opening");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panDiffCharges.add(lblAccOpeningChrgAD, gridBagConstraints);

        txtAccOpeningChrgAD.setMaxLength(32);
        txtAccOpeningChrgAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccOpeningChrgAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panDiffCharges.add(txtAccOpeningChrgAD, gridBagConstraints);

        lblMisServiceChrgAD.setText("Misc. Service");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblMisServiceChrgAD, gridBagConstraints);

        txtMisServiceChrgAD.setMaxLength(32);
        txtMisServiceChrgAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMisServiceChrgAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(txtMisServiceChrgAD, gridBagConstraints);

        chkStopPmtChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkStopPmtChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(chkStopPmtChrgAD, gridBagConstraints);

        lblChequeBookChrgAD.setText("Cheque Book");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblChequeBookChrgAD, gridBagConstraints);

        txtChequeBookChrgAD.setMaxLength(32);
        txtChequeBookChrgAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChequeBookChrgAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(txtChequeBookChrgAD, gridBagConstraints);

        chkChequeRetChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkChequeRetChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(chkChequeRetChrgAD, gridBagConstraints);

        lblFolioChrgAD.setText("Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblFolioChrgAD, gridBagConstraints);

        txtFolioChrgAD.setMaxLength(32);
        txtFolioChrgAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFolioChrgAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(txtFolioChrgAD, gridBagConstraints);

        chkInopChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkInopChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(chkInopChrgAD, gridBagConstraints);

        lblAccCloseChrgAD.setText("Account Closing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblAccCloseChrgAD, gridBagConstraints);

        txtAccCloseChrgAD.setMaxLength(32);
        txtAccCloseChrgAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccCloseChrgAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(txtAccCloseChrgAD, gridBagConstraints);

        chkStmtChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkStmtChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(chkStmtChrgAD, gridBagConstraints);

        lblStmtFreqAD.setText("Statement Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panDiffCharges.add(lblStmtFreqAD, gridBagConstraints);

        cboStmtFreqAD.setMinimumSize(new java.awt.Dimension(75, 21));
        cboStmtFreqAD.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panDiffCharges.add(cboStmtFreqAD, gridBagConstraints);

        chkNonMainMinBalChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkNonMainMinBalChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkNonMainMinBalChrgAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNonMainMinBalChrgADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(chkNonMainMinBalChrgAD, gridBagConstraints);

        lblExcessWithChrgAD.setText("Excess Withdrawal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblExcessWithChrgAD, gridBagConstraints);

        txtExcessWithChrgAD.setMaxLength(32);
        txtExcessWithChrgAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExcessWithChrgAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(txtExcessWithChrgAD, gridBagConstraints);

        chkABBChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkABBChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkABBChrgAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkABBChrgADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(chkABBChrgAD, gridBagConstraints);

        chkNPAChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkNPAChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkNPAChrgAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNPAChrgADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(chkNPAChrgAD, gridBagConstraints);

        lblABBChrgAD.setText("ABB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblABBChrgAD, gridBagConstraints);

        txtABBChrgAD.setMaxLength(32);
        txtABBChrgAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtABBChrgAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(txtABBChrgAD, gridBagConstraints);

        lblNPAChrgAD.setText("NPA Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblNPAChrgAD, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(tdtNPAChrgAD, gridBagConstraints);

        lblMinActBalanceAD.setText("Minimum Account Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblMinActBalanceAD, gridBagConstraints);

        txtMinActBalanceAD.setMaxLength(32);
        txtMinActBalanceAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinActBalanceAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(txtMinActBalanceAD, gridBagConstraints);

        lblStopPayment.setText("Stop Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblStopPayment, gridBagConstraints);

        lblChequeReturn.setText("Cheque Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblChequeReturn, gridBagConstraints);

        lblCollectInoperative.setText("Collect Inoperative");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblCollectInoperative, gridBagConstraints);

        lblStatement.setText("Statement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblStatement, gridBagConstraints);

        lblNonMaintenance.setText("Non-maintenance of Min Bal.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblNonMaintenance, gridBagConstraints);

        lblABB.setText("ABB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblABB, gridBagConstraints);

        lblNPA.setText("NPA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblNPA, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        panAccountDetails.add(panDiffCharges, gridBagConstraints);

        panLastIntApp.setBorder(javax.swing.BorderFactory.createTitledBorder("Last Interest Application Date"));
        panLastIntApp.setPreferredSize(new java.awt.Dimension(430, 49));
        panLastIntApp.setLayout(new java.awt.GridBagLayout());

        lblDebit.setText("Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panLastIntApp.add(lblDebit, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panLastIntApp.add(tdtDebit, gridBagConstraints);

        lblCredit.setText("Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panLastIntApp.add(lblCredit, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panLastIntApp.add(tdtCredit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        panAccountDetails.add(panLastIntApp, gridBagConstraints);

        panRatesIN.setBorder(javax.swing.BorderFactory.createTitledBorder("Rate Details"));
        panRatesIN.setLayout(new java.awt.GridBagLayout());

        lblRateCodeIN.setText("Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblRateCodeIN, gridBagConstraints);

        lblRateCodeValueIN.setText("100");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblRateCodeValueIN, gridBagConstraints);

        lblCrInterestRateIN.setText("Credit Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblCrInterestRateIN, gridBagConstraints);

        lblCrInterestRateValueIN.setText("10%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblCrInterestRateValueIN, gridBagConstraints);

        lblDrInterestRateIN.setText("Debit Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblDrInterestRateIN, gridBagConstraints);

        lblDrInterestRateValueIN.setText("10%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblDrInterestRateValueIN, gridBagConstraints);

        lblPenalInterestRateIN.setText("Penal Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblPenalInterestRateIN, gridBagConstraints);

        lblPenalInterestValueIN.setText("10%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblPenalInterestValueIN, gridBagConstraints);

        lblAgClearingIN.setText("Ag Clearing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblAgClearingIN, gridBagConstraints);

        lblAgClearingValueIN.setText("10%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRatesIN.add(lblAgClearingValueIN, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panAccountDetails.add(panRatesIN, gridBagConstraints);

        panInterestPayableIN.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest"));
        panInterestPayableIN.setMaximumSize(new java.awt.Dimension(394, 36));
        panInterestPayableIN.setMinimumSize(new java.awt.Dimension(394, 36));
        panInterestPayableIN.setPreferredSize(new java.awt.Dimension(394, 36));
        panInterestPayableIN.setLayout(new java.awt.GridBagLayout());

        chkPayIntOnCrBalIN.setText("Pay Interest on Credit Balanace");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panInterestPayableIN.add(chkPayIntOnCrBalIN, gridBagConstraints);

        chkPayIntOnDrBalIN.setText("Receive Interest on Debit Balanace");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panInterestPayableIN.add(chkPayIntOnDrBalIN, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panAccountDetails.add(panInterestPayableIN, gridBagConstraints);

        panAcctInfo_ODetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAcctInfo_ODetails.setLayout(new java.awt.GridBagLayout());

        lblProdID_ODetails.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo_ODetails.add(lblProdID_ODetails, gridBagConstraints);

        lblProdID_Disp_ODetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblProdID_Disp_ODetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProdID_Disp_ODetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo_ODetails.add(lblProdID_Disp_ODetails, gridBagConstraints);

        lblAcctHead_ODetails.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 65, 4, 4);
        panAcctInfo_ODetails.add(lblAcctHead_ODetails, gridBagConstraints);

        lblAcctHead_Disp_ODetails.setMaximumSize(new java.awt.Dimension(110, 15));
        lblAcctHead_Disp_ODetails.setMinimumSize(new java.awt.Dimension(110, 15));
        lblAcctHead_Disp_ODetails.setPreferredSize(new java.awt.Dimension(110, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo_ODetails.add(lblAcctHead_Disp_ODetails, gridBagConstraints);

        lblAcctNo_ODetails.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 56, 4, 4);
        panAcctInfo_ODetails.add(lblAcctNo_ODetails, gridBagConstraints);

        lblAcctNo_Disp_ODetails.setMaximumSize(new java.awt.Dimension(110, 15));
        lblAcctNo_Disp_ODetails.setMinimumSize(new java.awt.Dimension(110, 15));
        lblAcctNo_Disp_ODetails.setPreferredSize(new java.awt.Dimension(110, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctInfo_ODetails.add(lblAcctNo_Disp_ODetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountDetails.add(panAcctInfo_ODetails, gridBagConstraints);

        tabLimitAmount.addTab("Other Details", panAccountDetails);

        panDocumentDetails.setMinimumSize(new java.awt.Dimension(795, 550));
        panDocumentDetails.setPreferredSize(new java.awt.Dimension(795, 550));
        panDocumentDetails.setLayout(new java.awt.GridBagLayout());

        panTable_DocumentDetails.setMaximumSize(new java.awt.Dimension(425, 320));
        panTable_DocumentDetails.setMinimumSize(new java.awt.Dimension(425, 320));
        panTable_DocumentDetails.setPreferredSize(new java.awt.Dimension(425, 320));
        panTable_DocumentDetails.setLayout(new java.awt.GridBagLayout());

        srpTable_DocumentDetails.setMaximumSize(new java.awt.Dimension(425, 320));
        srpTable_DocumentDetails.setMinimumSize(new java.awt.Dimension(425, 320));
        srpTable_DocumentDetails.setPreferredSize(new java.awt.Dimension(425, 320));

        tblTable_DocumentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTable_DocumentDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblTable_DocumentDetailsMousePressed(evt);
            }
        });
        srpTable_DocumentDetails.setViewportView(tblTable_DocumentDetails);

        panTable_DocumentDetails.add(srpTable_DocumentDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(panTable_DocumentDetails, gridBagConstraints);

        panAcctDetails_DocumentDetails.setMinimumSize(new java.awt.Dimension(685, 23));
        panAcctDetails_DocumentDetails.setPreferredSize(new java.awt.Dimension(685, 23));
        panAcctDetails_DocumentDetails.setLayout(new java.awt.GridBagLayout());

        lblProdID_DocumentDetails.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDetails_DocumentDetails.add(lblProdID_DocumentDetails, gridBagConstraints);

        lblProdID_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblProdID_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProdID_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDetails_DocumentDetails.add(lblProdID_Disp_DocumentDetails, gridBagConstraints);

        lblAcctHead_DocumentDetails.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 65, 4, 4);
        panAcctDetails_DocumentDetails.add(lblAcctHead_DocumentDetails, gridBagConstraints);

        lblAcctHead_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(110, 15));
        lblAcctHead_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(110, 15));
        lblAcctHead_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(110, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDetails_DocumentDetails.add(lblAcctHead_Disp_DocumentDetails, gridBagConstraints);

        lblAcctNo_DocumentDetails.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 56, 4, 4);
        panAcctDetails_DocumentDetails.add(lblAcctNo_DocumentDetails, gridBagConstraints);

        lblAcctNo_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(110, 15));
        lblAcctNo_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(110, 15));
        lblAcctNo_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(110, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDetails_DocumentDetails.add(lblAcctNo_Disp_DocumentDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(panAcctDetails_DocumentDetails, gridBagConstraints);

        panTabDetails_DocumentDetails.setMaximumSize(new java.awt.Dimension(260, 350));
        panTabDetails_DocumentDetails.setMinimumSize(new java.awt.Dimension(260, 350));
        panTabDetails_DocumentDetails.setPreferredSize(new java.awt.Dimension(260, 350));
        panTabDetails_DocumentDetails.setLayout(new java.awt.GridBagLayout());

        lblDocType_DocumentDetails.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocType_DocumentDetails, gridBagConstraints);

        lblDocType_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocType_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocType_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocType_Disp_DocumentDetails, gridBagConstraints);

        lblDocNo_DocumentDetails.setText("Document Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocNo_DocumentDetails, gridBagConstraints);

        lblDocNo_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocNo_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocNo_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocNo_Disp_DocumentDetails, gridBagConstraints);

        lblDocDesc_DocumentDetails.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocDesc_DocumentDetails, gridBagConstraints);

        lblDocDesc_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocDesc_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocDesc_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocDesc_Disp_DocumentDetails, gridBagConstraints);

        lblSubmitted_DocumentDetails.setText("Is Submitted?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblSubmitted_DocumentDetails, gridBagConstraints);

        panSubmitted_DocumentDetails.setLayout(new java.awt.GridBagLayout());

        rdoIsSubmitted_DocumentDetails.add(rdoYes_DocumentDetails);
        rdoYes_DocumentDetails.setText("Yes");
        rdoYes_DocumentDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoYes_DocumentDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panSubmitted_DocumentDetails.add(rdoYes_DocumentDetails, gridBagConstraints);

        rdoIsSubmitted_DocumentDetails.add(rdoNo_DocumentDetails);
        rdoNo_DocumentDetails.setText("No");
        rdoNo_DocumentDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNo_DocumentDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panSubmitted_DocumentDetails.add(rdoNo_DocumentDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(panSubmitted_DocumentDetails, gridBagConstraints);

        lblSubmitDate_DocumentDetails.setText("Submitted Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblSubmitDate_DocumentDetails, gridBagConstraints);

        tdtSubmitDate_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtSubmitDate_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSubmitDate_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtSubmitDate_DocumentDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSubmitDate_DocumentDetailsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(tdtSubmitDate_DocumentDetails, gridBagConstraints);

        lblRemarks_DocumentDetails.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblRemarks_DocumentDetails, gridBagConstraints);

        txtRemarks_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRemarks_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(txtRemarks_DocumentDetails, gridBagConstraints);

        btnSave_DocumentDetails.setText("Save");
        btnSave_DocumentDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_DocumentDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(btnSave_DocumentDetails, gridBagConstraints);

        lblMandatory_DOC.setText(" Is Mandatory?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblMandatory_DOC, gridBagConstraints);

        panMandatory_DOC.setLayout(new java.awt.GridBagLayout());

        rdoMandatory_DOC.add(rdoYes_Mandatory_DOC);
        rdoYes_Mandatory_DOC.setText("Yes");
        panMandatory_DOC.add(rdoYes_Mandatory_DOC, new java.awt.GridBagConstraints());

        rdoMandatory_DOC.add(rdoNo_Mandatory_DOC);
        rdoNo_Mandatory_DOC.setText("No");
        panMandatory_DOC.add(rdoNo_Mandatory_DOC, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(panMandatory_DOC, gridBagConstraints);

        lblExecuted_DOC.setText(" Is Executed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblExecuted_DOC, gridBagConstraints);

        panExecuted_DOC.setLayout(new java.awt.GridBagLayout());

        rdoExecuted_DOC.add(rdoYes_Executed_DOC);
        rdoYes_Executed_DOC.setText("Yes");
        rdoYes_Executed_DOC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoYes_Executed_DOCActionPerformed(evt);
            }
        });
        panExecuted_DOC.add(rdoYes_Executed_DOC, new java.awt.GridBagConstraints());

        rdoExecuted_DOC.add(rdoNo_Executed_DOC);
        rdoNo_Executed_DOC.setText("No");
        rdoNo_Executed_DOC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNo_Executed_DOCActionPerformed(evt);
            }
        });
        panExecuted_DOC.add(rdoNo_Executed_DOC, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(panExecuted_DOC, gridBagConstraints);

        lblExecuteDate_DOC.setText("Executed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblExecuteDate_DOC, gridBagConstraints);

        tdtExecuteDate_DOC.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtExecuteDate_DOC.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtExecuteDate_DOC.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(tdtExecuteDate_DOC, gridBagConstraints);

        lblExpiryDate_DOC.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblExpiryDate_DOC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(tdtExpiryDate_DOC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(panTabDetails_DocumentDetails, gridBagConstraints);

        tabLimitAmount.addTab("Document Details", panDocumentDetails);

        panClassDetails.setLayout(new java.awt.GridBagLayout());

        panClassDetails_Acc.setLayout(new java.awt.GridBagLayout());

        panProd_CD.setLayout(new java.awt.GridBagLayout());

        lblProID_CD.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblProID_CD, gridBagConstraints);

        lblSanctionNo1.setText("Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblSanctionNo1, gridBagConstraints);

        lblSanctionNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblSanctionNo2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblSanctionNo2, gridBagConstraints);

        lblProID_CD_Disp.setText("P1001");
        lblProID_CD_Disp.setMaximumSize(new java.awt.Dimension(100, 15));
        lblProID_CD_Disp.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProID_CD_Disp.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblProID_CD_Disp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panClassDetails_Acc.add(panProd_CD, gridBagConstraints);

        PanAcc_CD.setLayout(new java.awt.GridBagLayout());

        lblAccHead_CD.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblAccHead_CD, gridBagConstraints);

        lblAccHead_CD_2.setText("SB Saving Bank Account");
        lblAccHead_CD_2.setMinimumSize(new java.awt.Dimension(139, 21));
        lblAccHead_CD_2.setPreferredSize(new java.awt.Dimension(139, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblAccHead_CD_2, gridBagConstraints);

        lblAccNo_CD.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblAccNo_CD, gridBagConstraints);

        lblAccNo_CD_2.setText("4325");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblAccNo_CD_2, gridBagConstraints);

        lblSanctionDate1.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblSanctionDate1, gridBagConstraints);

        lblSanctionDate2.setMinimumSize(new java.awt.Dimension(100, 21));
        lblSanctionDate2.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblSanctionDate2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 48, 4, 4);
        panClassDetails_Acc.add(PanAcc_CD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails.add(panClassDetails_Acc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails.add(sptClassDetails, gridBagConstraints);

        panClassDetails_Details.setLayout(new java.awt.GridBagLayout());

        panCode.setLayout(new java.awt.GridBagLayout());

        lblCommodityCode.setText("Commodity Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblCommodityCode, gridBagConstraints);

        cboCommodityCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCommodityCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboCommodityCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboCommodityCode, gridBagConstraints);

        lblGuaranteeCoverCode.setText("Guarantee Cover Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblGuaranteeCoverCode, gridBagConstraints);

        cboGuaranteeCoverCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboGuaranteeCoverCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboGuaranteeCoverCode.setPopupWidth(120);
        cboGuaranteeCoverCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboGuaranteeCoverCode, gridBagConstraints);

        lblSectorCode1.setText("Sector Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblSectorCode1, gridBagConstraints);

        cboSectorCode1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSectorCode1.setMinimumSize(new java.awt.Dimension(180, 21));
        cboSectorCode1.setPopupWidth(130);
        cboSectorCode1.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboSectorCode1, gridBagConstraints);

        lblHealthCode.setText("Health Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblHealthCode, gridBagConstraints);

        cboHealthCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboHealthCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboHealthCode.setPopupWidth(200);
        cboHealthCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboHealthCode, gridBagConstraints);

        lblTypeFacility.setText("Type of Facility");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblTypeFacility, gridBagConstraints);

        cboTypeFacility.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboTypeFacility.setMinimumSize(new java.awt.Dimension(180, 21));
        cboTypeFacility.setPopupWidth(300);
        cboTypeFacility.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboTypeFacility, gridBagConstraints);

        lblDistrictCode.setText("District Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblDistrictCode, gridBagConstraints);

        cboDistrictCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDistrictCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboDistrictCode.setPopupWidth(120);
        cboDistrictCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboDistrictCode, gridBagConstraints);

        lblPurposeCode.setText("Purpose Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblPurposeCode, gridBagConstraints);

        cboPurposeCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPurposeCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboPurposeCode.setPopupWidth(230);
        cboPurposeCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboPurposeCode, gridBagConstraints);

        lblIndusCode.setText("Industry Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblIndusCode, gridBagConstraints);

        cboIndusCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboIndusCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboIndusCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboIndusCode, gridBagConstraints);

        lblWeakerSectionCode.setText(" Weaker Section Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblWeakerSectionCode, gridBagConstraints);

        cboWeakerSectionCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboWeakerSectionCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboWeakerSectionCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboWeakerSectionCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails_Details.add(panCode, gridBagConstraints);

        sptClassification_vertical.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails_Details.add(sptClassification_vertical, gridBagConstraints);

        panCode2.setLayout(new java.awt.GridBagLayout());

        lbl20Code.setText("20 Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lbl20Code, gridBagConstraints);

        lblRefinancingInsti.setText("Refinancing Institution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblRefinancingInsti, gridBagConstraints);

        cboRefinancingInsti.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRefinancingInsti.setMinimumSize(new java.awt.Dimension(180, 21));
        cboRefinancingInsti.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(cboRefinancingInsti, gridBagConstraints);

        lblGovtSchemeCode.setText("Govt. Scheme Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblGovtSchemeCode, gridBagConstraints);

        cboGovtSchemeCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboGovtSchemeCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboGovtSchemeCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(cboGovtSchemeCode, gridBagConstraints);

        lblAssetCode.setText("Asset Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblAssetCode, gridBagConstraints);

        cboAssetCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboAssetCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboAssetCode.setPopupWidth(115);
        cboAssetCode.setPreferredSize(new java.awt.Dimension(180, 21));
        cboAssetCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAssetCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(cboAssetCode, gridBagConstraints);

        lblNPADate.setText("NPA Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblNPADate, gridBagConstraints);

        tdtNPADate.setMinimumSize(new java.awt.Dimension(180, 21));
        tdtNPADate.setPreferredSize(new java.awt.Dimension(180, 21));
        tdtNPADate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtNPADateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(tdtNPADate, gridBagConstraints);

        lblDirectFinance.setText("Direct Finance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblDirectFinance, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(chkDirectFinance, gridBagConstraints);

        lblECGC.setText("ECGC");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblECGC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(chkECGC, gridBagConstraints);

        lblPrioritySector.setText("Priority Sector");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblPrioritySector, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(chkPrioritySector, gridBagConstraints);

        lblDocumentcomplete.setText("Document Complete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblDocumentcomplete, gridBagConstraints);

        chkDocumentcomplete.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkDocumentcompleteStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(chkDocumentcomplete, gridBagConstraints);

        lblQIS.setText("QIS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblQIS, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(chkQIS, gridBagConstraints);

        cbo20Code.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cbo20Code.setMinimumSize(new java.awt.Dimension(180, 21));
        cbo20Code.setPopupWidth(200);
        cbo20Code.setPreferredSize(new java.awt.Dimension(180, 21));
        panCode2.add(cbo20Code, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails_Details.add(panCode2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails.add(panClassDetails_Details, gridBagConstraints);

        tabLimitAmount.addTab("Classification Details", panClassDetails);

        panShareMaintenance.setLayout(new java.awt.GridBagLayout());

        panShareMaintenance_Table.setMinimumSize(new java.awt.Dimension(578, 301));
        panShareMaintenance_Table.setPreferredSize(new java.awt.Dimension(578, 301));
        panShareMaintenance_Table.setLayout(new java.awt.GridBagLayout());

        panTable_Share.setMinimumSize(new java.awt.Dimension(570, 293));
        panTable_Share.setPreferredSize(new java.awt.Dimension(570, 293));
        panTable_Share.setLayout(new java.awt.GridBagLayout());

        srpShareMaintenance.setMinimumSize(new java.awt.Dimension(470, 293));
        srpShareMaintenance.setPreferredSize(new java.awt.Dimension(470, 293));

        tblShareMaintenance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "cust_name", "cust_id", "Loan No", "Share Type", "Share_Ac_No", "No Of Share", "Title 7"
            }
        ));
        tblShareMaintenance.setMinimumSize(new java.awt.Dimension(90, 20));
        tblShareMaintenance.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 100));
        tblShareMaintenance.setPreferredSize(new java.awt.Dimension(450, 268));
        tblShareMaintenance.setOpaque(false);
        srpShareMaintenance.setViewportView(tblShareMaintenance);

        panTable_Share.add(srpShareMaintenance, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareMaintenance_Table.add(panTable_Share, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareMaintenance.add(panShareMaintenance_Table, gridBagConstraints);

        tabLimitAmount.addTab("Share  Maintenance", panShareMaintenance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTermLoan.add(tabLimitAmount, gridBagConstraints);

        getContentPane().add(panTermLoan, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setMinimumSize(new java.awt.Dimension(73, 19));

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.setEnabled(false);
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.setEnabled(false);
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setEnabled(false);
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.setEnabled(false);
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.setEnabled(false);
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTermLoan.add(mnuProcess);

        setJMenuBar(mbrTermLoan);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void tdtFDateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtFDateMouseExited
        // TODO add your handling code here:
        //        txtNoInstallmentsFocusLost();
    }//GEN-LAST:event_tdtFDateMouseExited
    
    private void tdtNPADateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtNPADateFocusLost
        // TODO add your handling code here:
        observableClassi.setLblAccNo_CD_2(lblAccNo_CD_2.getText());
        //        observableClassi.setTdtNPAChangeDt(tdtNPADate.getDateValue());
        //        observableClassi.updateAssetStatus();
        observableClassi.setTdtNPAChangeDt("");
    }//GEN-LAST:event_tdtNPADateFocusLost
    
    private void txtCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustIDFocusLost
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp("Enquirystatus");
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void cboInterestTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInterestTypeActionPerformed
        // TODO add your handling code here:
        String interstType=CommonUtil.convertObjToStr(((ComboBoxModel)cboInterestType.getModel()).getKeyForSelected());
        if(CommonUtil.convertObjToStr(((ComboBoxModel)cboInterestType.getModel()).getKeyForSelected()).equals("FLAT_RATE")  ){
            if(rdoInterest_Simple.isSelected()){}
            else if(rdoInterest_Compound.isSelected()) {
                ClientUtil.showMessageWindow("Compounding interest does not allow to flat_rate");
                ((ComboBoxModel)cboInterestType.getModel()).setKeyForSelected("");
            }
            else
                ClientUtil.showMessageWindow("Select interest type SIMPLE OR COMPOUND");
        }
        
    }//GEN-LAST:event_cboInterestTypeActionPerformed
    
    private void rdoAccLimit_MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAccLimit_MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoAccLimit_MainActionPerformed
    
    private void cboAssetCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAssetCodeActionPerformed
        // TODO add your handling code here:
        observableClassi.setLblAccNo_CD_2(lblAccNo_CD_2.getText());
//        observableClassi.updateAssetStatus();
        
    }//GEN-LAST:event_cboAssetCodeActionPerformed
    
    private void txtCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIDActionPerformed
        
        
        
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_txtCustIDActionPerformed
    
    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed
    
    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    private void cboInterestTypeItemStateChanged() {
        String strInterestTypeKey = CommonUtil.convertObjToStr(((ComboBoxModel) cboInterestType.getModel()).getKeyForSelected());
        if (rdoNatureInterest_NonPLR.isSelected() && strInterestTypeKey.equals(FLOATING_RATE)){
            cboInterestType.setSelectedItem("");
        }
        strInterestTypeKey = null;
    }
    private void chkDocumentcompleteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkDocumentcompleteStateChanged
        // TODO add your handling code here:
        chkDocumentcompleteStateChanged();
    }//GEN-LAST:event_chkDocumentcompleteStateChanged
    private void btnCheck(){
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    
    private void chkDocumentcompleteStateChanged() {
        // TODO add your handling code here:
        if (chkDocumentcomplete.isSelected()){
            if (!observableDocument.isDocumentCompleted()){
                chkDocumentcomplete.setSelected(false);
                observableClassi.setChkDocumentcomplete(false);
            }
        }
    }
    
    
    private void rdoNo_Executed_DOCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNo_Executed_DOCActionPerformed
        // TODO add your handling code here:
        rdoNo_Executed_DOCActionPerformed();
    }//GEN-LAST:event_rdoNo_Executed_DOCActionPerformed
    private void rdoNo_Executed_DOCActionPerformed() {
        // TODO add your handling code here:
        if (rdoNo_Executed_DOC.isSelected()){
            observableDocument.setTdtExecuteDate_DOC("");
            tdtExecuteDate_DOC.setDateValue("");
            tdtExecuteDate_DOC.setEnabled(false);
        }
    }
    private void rdoYes_Executed_DOCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoYes_Executed_DOCActionPerformed
        // TODO add your handling code here:
        rdoYes_Executed_DOCActionPerformed();
    }//GEN-LAST:event_rdoYes_Executed_DOCActionPerformed
    private void rdoYes_Executed_DOCActionPerformed() {
        // TODO add your handling code here:
        if (rdoYes_Executed_DOC.isSelected()){
            tdtExecuteDate_DOC.setEnabled(true);
        }
    }
    private void txtLaonAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLaonAmtFocusLost
        // TODO add your handling code here:
        txtLaonAmtFocusLost();
    }//GEN-LAST:event_txtLaonAmtFocusLost
    private boolean isLoanAmtExceedLimit(){
        //        if (loanType.equals("OTHERS"))
        if (rdoActive_Repayment.isSelected() && !updateRepayment && CommonUtil.convertObjToDouble(observableRepay.getStrLimitAmt()).doubleValue() < (observableRepay.getActiveLoanAmt() + CommonUtil.convertObjToDouble(txtLaonAmt.getText()).doubleValue())){
            observableRepay.repayTabWarning("loanExceedLimitWarning");
            txtLaonAmt.setText("");
            return false;
        }else{
            return true;
        }
        //        else
        //            return true;
    }
    
    private void txtLaonAmtFocusLost() {
        // If the Loan amount is less than or equal to limit amount then calculate total base amt
        if (CommonUtil.convertObjToDouble(txtLaonAmt.getText()).doubleValue() <= CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()){
            // To chk the Total Loan amount doesn't exceed limit amt
            if (!isLoanAmtExceedLimit()){
                observableRepay.resetRepaymentSchedule();
                //                observable.ttNotifyObservers();
                return;
            }
            observableRepay.setTxtLaonAmt(txtLaonAmt.getText());
            // The following block commented because in Co-operative banks no interest should be
            // added for the moratorium period  // by Rajesh
            //            if (chkMoratorium_Given.isSelected()){
            //                calculateTotalBaseAmount();
            //                txtTotalBaseAmt.setText(observableRepay.getTxtTotalBaseAmt());
            //            }else{
            observableRepay.setTxtTotalBaseAmt(txtLaonAmt.getText());
            txtTotalBaseAmt.setText(txtLaonAmt.getText());
            //            }
        }else{
            txtLaonAmt.setText("");
            observableRepay.setTxtTotalBaseAmt("");
            txtTotalBaseAmt.setText("");
            observableRepay.setTxtAmtPenulInstall("");
            txtAmtPenulInstall.setText("");
            observableRepay.setTxtAmtLastInstall("");
            txtAmtLastInstall.setText("");
            observableRepay.setTxtTotalInstallAmt("");
            txtTotalInstallAmt.setText("");
        }
    }
    private void chkNPAChrgADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNPAChrgADActionPerformed
        // TODO add your handling code here:
        chkNPAChrgADActionPerformed();
    }//GEN-LAST:event_chkNPAChrgADActionPerformed
    private void chkNPAChrgADActionPerformed() {
        /* we have to show the NPA charge date only if the corresponding
         * check box has been selected
         */
        tdtNPAChrgAD.setEnabled(chkNPAChrgAD.isSelected());
        tdtNPAChrgAD.setDateValue("");
        observableOtherDetails.setTdtNPAChrgAD("");
    }
    private void chkABBChrgADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkABBChrgADActionPerformed
        // TODO add your handling code here:
        chkABBChrgADActionPerformed();
    }//GEN-LAST:event_chkABBChrgADActionPerformed
    private void chkABBChrgADActionPerformed() {
        /* we have to show the ABBA charge text box only if the corresponding
         * check box has been selected
         */
        boolean isSelected = chkABBChrgAD.isSelected();
        txtABBChrgAD.setEditable(isSelected);
        txtABBChrgAD.setEnabled(isSelected);
        if(!isSelected){
            txtABBChrgAD.setText("");
            observableOtherDetails.setTxtABBChrgAD("");
        }
    }
    private void chkNonMainMinBalChrgADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNonMainMinBalChrgADActionPerformed
        // TODO add your handling code here:
        chkNonMainMinBalChrgADActionPerformed();
    }//GEN-LAST:event_chkNonMainMinBalChrgADActionPerformed
    private void chkNonMainMinBalChrgADActionPerformed(){
        /* we have to show the Non maintenance of minimum balance charge text box
         * only if the corresponding check box has been selected
         */
        boolean isSelected = chkNonMainMinBalChrgAD.isSelected();
        txtMinActBalanceAD.setEditable(isSelected);
        txtMinActBalanceAD.setEnabled(isSelected);
        if(!isSelected){
            txtMinActBalanceAD.setText("");
            observableOtherDetails.setTxtMinActBalanceAD("");
        }
    }
    private void tdtCreditToDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCreditToDateADFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtCreditToDateAD, tdtCreditFromDateAD.getDateValue());
    }//GEN-LAST:event_tdtCreditToDateADFocusLost
    
    private void tdtCreditFromDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCreditFromDateADFocusLost
        // TODO add your handling code here:
        ClientUtil.validateFromDate(tdtCreditFromDateAD, tdtCreditToDateAD.getDateValue());
    }//GEN-LAST:event_tdtCreditFromDateADFocusLost
    
    private void tdtDebitToDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDebitToDateADFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtDebitToDateAD, tdtDebitFromDateAD.getDateValue());
    }//GEN-LAST:event_tdtDebitToDateADFocusLost
    
    private void tdtDebitFromDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDebitFromDateADFocusLost
        // TODO add your handling code here:
        ClientUtil.validateFromDate(tdtDebitFromDateAD, tdtDebitToDateAD.getDateValue());
    }//GEN-LAST:event_tdtDebitFromDateADFocusLost
    
    private void tdtATMToDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtATMToDateADFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtATMToDateAD, tdtATMFromDateAD.getDateValue());
    }//GEN-LAST:event_tdtATMToDateADFocusLost
    
    private void tdtATMFromDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtATMFromDateADFocusLost
        // TODO add your handling code here:
        ClientUtil.validateFromDate(tdtATMFromDateAD, tdtATMToDateAD.getDateValue());
    }//GEN-LAST:event_tdtATMFromDateADFocusLost
    
    private void chkCreditADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditADActionPerformed
        // TODO add your handling code here:
        chkCreditADActionPerformed();
    }//GEN-LAST:event_chkCreditADActionPerformed
    private void chkCreditADActionPerformed() {
        /* we have to enable the Credit card No., text field and the validity date
         * only when the user selected the credit card option
         */
        boolean isSelected = chkCreditAD.isSelected();
        txtCreditNoAD.setEditable(isSelected);
        txtCreditNoAD.setEnabled(isSelected);
        tdtCreditFromDateAD.setEnabled(isSelected);
        tdtCreditToDateAD.setEnabled(isSelected);
        //        chkCreditAD.setEnabled(isSelected);
        if(!isSelected){
            observableOtherDetails.setTxtCreditNoAD("");
            observableOtherDetails.setTdtCreditToDateAD("");
            observableOtherDetails.setTdtCreditFromDateAD("");
            txtCreditNoAD.setText("");
            tdtCreditFromDateAD.setDateValue("");
            tdtCreditToDateAD.setDateValue("");
        }
    }
    private void chkDebitADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDebitADActionPerformed
        // TODO add your handling code here:
        chkDebitADActionPerformed();
    }//GEN-LAST:event_chkDebitADActionPerformed
    private void chkDebitADActionPerformed() {
        /* we have to enable the debit card No., text field and the validity date
         * only when the user selected the debit card option
         */
        boolean isSelected = chkDebitAD.isSelected();
        txtDebitNoAD.setEditable(isSelected);
        txtDebitNoAD.setEnabled(isSelected);
        tdtDebitToDateAD.setEnabled(isSelected);
        tdtDebitFromDateAD.setEnabled(isSelected);
        //        chkDebitAD.setEnabled(isSelected);
        if(!isSelected){
            observableOtherDetails.setTxtDebitNoAD("");
            observableOtherDetails.setTdtDebitToDateAD("");
            observableOtherDetails.setTdtDebitFromDateAD("");
            txtDebitNoAD.setText("");
            tdtDebitToDateAD.setDateValue("");
            tdtDebitFromDateAD.setDateValue("");
        }
    }
    private void chkATMADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkATMADActionPerformed
        // TODO add your handling code here:
        chkATMADActionPerformed();
    }//GEN-LAST:event_chkATMADActionPerformed
    private void chkATMADActionPerformed() {
        /* we have to enable the ATM No., text field and the validity date
         * only when the user selected the ATM card option
         */
        boolean isSelected = chkATMAD.isSelected();
        txtATMNoAD.setEditable(isSelected);
        txtATMNoAD.setEnabled(isSelected);
        tdtATMToDateAD.setEnabled(isSelected);
        tdtATMFromDateAD.setEnabled(isSelected);
        //        chkATMAD.setEnabled(isSelected);
        if(!isSelected){
            observableOtherDetails.setTxtATMNoAD("");
            observableOtherDetails.setTdtATMToDateAD("");
            observableOtherDetails.setTdtATMFromDateAD("");
            txtATMNoAD.setText("");
            tdtATMToDateAD.setDateValue("");
            tdtATMFromDateAD.setDateValue("");
        }
    }
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
        cboProdIdActionPerformed();
    }//GEN-LAST:event_cboProdIdActionPerformed
    private void cboProdIdActionPerformed() {
        String strOldProdId = observableGuarantor.getCboProdId();
        String prodId = CommonUtil.convertObjToStr(cboProdId.getSelectedItem());
        if (cboProdId.getSelectedIndex() > 0 && !strOldProdId.equals(prodId)){
            txtGuaranAccNo.setText("");
            observableGuarantor.setTxtGuaranAccNo(txtGuaranAccNo.getText());
        }
    }
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        btnAccNoActionPerformed();
    }//GEN-LAST:event_btnAccNoActionPerformed
    private void btnAccNoActionPerformed() {
        popUp("GUARANTOR_ACCT_NO");
    }
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        cboProdTypeActionPerformed();
    }//GEN-LAST:event_cboProdTypeActionPerformed
    private void cboProdTypeActionPerformed() {
        String oldProdTypeVal = observableGuarantor.getCboProdType();
        String prodTypeVal = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
        
        if (cboProdType.getSelectedIndex() > 0) {
            
            observableGuarantor.setCboProdType(prodTypeVal);
            if (prodType.equals("GL")) {
                observableGuarantor.removeAllProdID();
                cboProdId.setModel(observableGuarantor.getCbmProdId());
                txtGuaranAccNo.setText("");
                setProdEnable(false);
            }
            
            if (!prodType.equals("GL")) {
                if (!oldProdTypeVal.equals(prodTypeVal)){
                    txtGuaranAccNo.setText("");
                }
                setProdEnable(true);
                observableGuarantor.setCbmProdId(prodType);
                cboProdId.setModel(observableGuarantor.getCbmProdId());
            }
        }
    }
    private void setProdEnable(boolean isEnable) {
        cboProdId.setEnabled(isEnable);
        txtGuaranAccNo.setEditable(false);
        txtGuaranAccNo.setEnabled(isEnable);
        btnAccNo.setEnabled(isEnable);
    }
    private void txtMarginFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarginFocusLost
        // TODO add your handling code here:
        txtMarginFocusLost();
    }//GEN-LAST:event_txtMarginFocusLost
    private void txtMarginFocusLost(){
        if (txtMargin.getText().length() == 0){
            txtEligibleLoan.setText("");
        }else{
            txtEligibleLoan.setText(observableSecurity.populateEligibleLoanAgainstSecurity(txtSecurityValue.getText(), txtMargin.getText(), txtLimit_SD.getText()));
        }
    }
    private void cboRepayFreq_RepaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepayFreq_RepaymentActionPerformed
        // TODO add your handling code here:
        cboRepayFreq_RepaymentActionPerformed();
    }//GEN-LAST:event_cboRepayFreq_RepaymentActionPerformed
    private void cboRepayFreq_RepaymentActionPerformed() {
        observableRepay.setRepaymentFrequency(CommonUtil.convertObjToStr(cboRepayFreq_Repayment.getSelectedItem()));
        cboRepayType.setSelectedItem(observableRepay.getCboRepayType());
    }
    private void cboIntGetFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntGetFromActionPerformed
        // TODO add your handling code here:
        if (!(viewType.equals("Delete") || viewType.equals(AUTHORIZE))) {
            if (loanType.equals("LTD")) {
                if (sanDetailMousePressedForLTD && evt.getModifiers()==16
                && CommonUtil.convertObjToStr(cboIntGetFrom.getSelectedItem()).length()>0) {
                    populateInterestRateForLTD();
                    sanDetailMousePressedForLTD = false;
                }
                return;
            }
            if (evt.getModifiers()==16)
                cboIntGetFromActionPerformed();
        }
    }//GEN-LAST:event_cboIntGetFromActionPerformed
    private java.sql.Timestamp getTimestamp(java.util.Date date){
        return new java.sql.Timestamp(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), 0);
    }
    
    private java.math.BigDecimal getBigDecimal(double doubleValue){
        return new java.math.BigDecimal(doubleValue);
    }
    
    private void cboIntGetFromActionPerformed() {
        String strOldValue = observable.getCboIntGetFrom();
        String strSelectedKey = CommonUtil.convertObjToStr(((ComboBoxModel)cboIntGetFrom.getModel()).getKeyForSelected());
        String strSelectedVal = CommonUtil.convertObjToStr(cboIntGetFrom.getSelectedItem());
        System.out.println("getcbogetintgetform"+strOldValue+"selectedkey"+strSelectedKey+"strselectedval"+strSelectedVal);
        if (observable.getStrACNumber().length() <= 0 || strOldValue.equals(strSelectedVal)){
            //            if(loanType.equals("OTHERS"))
            if (strSelectedKey.equals(ACT))
                setInterestDetailsOnlyNewEnabled();
            //            if(!strOldValue.equals("Product"))
            //                return;
        }else if (strOldValue.length() > 0 && tblRepaymentCTable.getRowCount() > 0){
            observable.repaymentExistingWarning();
            cboIntGetFrom.setSelectedItem(observable.getCboIntGetFrom());
            return;
        }else if (strOldValue.length() > 0 && tblRepaymentCTable.getRowCount() == 0){
            observableRepay.setTxtTotalBaseAmt("");
            txtTotalBaseAmt.setText(observableRepay.getTxtTotalBaseAmt());
        }
        if (strOldValue.length() > 0 && strSelectedKey.equals(PROD)){
            if ((tblInterMaintenance.getRowCount() > 0) && observable.interestGetFromValChangeWarn() == 1){
                
                // if no is selected then reset the value
                cboIntGetFrom.setSelectedItem(strOldValue);
                return;
            }else{
                HashMap whereMap = new HashMap();
                whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
                whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
                whereMap.put("PROD_ID", observable.getLblProductID_FD_Disp());
                if (tdtFDate.getDateValue().length()==0) tdtFDate.setDateValue(observable.getTdtFDate());
                whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
                if (tdtTDate.getDateValue().length()==0) tdtTDate.setDateValue(observable.getTdtTDate());
                whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));
                deleteAllInterestDetails();
                observableInt.resetInterestDetails();
                updateInterestDetails();
                // Populate the values
                ArrayList interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductBillsInterestTO", whereMap);
                if (interestList!=null && interestList.size()>0)
                    observableInt.setTermLoanInterestTO(interestList, null);
                else {
                    displayAlert("Interest rates not created for this product...");
                    strSelectedVal = "";
                    //                    cboIntGetFrom.setSelectedItem("");
                }
            }
        }else if (strOldValue.length() > 0 && strSelectedKey.equals(ACT)){
            observableInt.destroyObjects();
            observableInt.createObject();
            observableInt.resetInterestDetails();
            updateInterestDetails();
            observableInt.resetInterestDetails();
            setInterestDetailsOnlyNewEnabled();
        }else if (strOldValue.length() == 0 && strSelectedKey.equals(PROD)){
            HashMap whereMap = new HashMap();
            whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
            whereMap.put("PROD_ID", observable.getLblProductID_FD_Disp());
            whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
            whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));
            deleteAllInterestDetails();
            observableInt.resetInterestDetails();
            updateInterestDetails();
            // Populate the values
            ArrayList interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductBillsInterestTO", whereMap);
            if (interestList!=null && interestList.size()>0)
                observableInt.setTermLoanInterestTO(interestList, null);
            else {
                displayAlert("Interest rates not created for this product...");
                strSelectedVal = "";
                //                cboIntGetFrom.setSelectedItem("");
            }
        }else if (strOldValue.length() > 0 && strSelectedKey.equals("")){
            deleteAllInterestDetails();
            observableInt.resetInterestDetails();
            updateInterestDetails();
            setAllInterestBtnsEnableDisable(false);
            setAllInterestDetailsEnableDisable(false);
        }
        
        if ((strSelectedKey.equals(PROD)) || (strSelectedKey.equals("")) || (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION)) || viewType.equals(REJECT)){
            // If the interest is from Product level or nothing selected
            // If the record is populated for Delete or Authorization
            setAllInterestDetailsEnableDisable(false);
            setAllInterestBtnsEnableDisable(false);
        }else if (strSelectedKey.equals(ACT)){
            setAllInterestDetailsEnableDisable(false);
            setInterestDetailsOnlyNewEnabled();
        }
        observable.setCboIntGetFrom(CommonUtil.convertObjToStr(strSelectedVal));
    }
    private void updateInterestDetails(){
        tdtFrom.setDateValue(observableInt.getTdtFrom());
        tdtTo.setDateValue(observableInt.getTdtTo());
        txtFromAmt.setText(observableInt.getTxtFromAmt());
        txtToAmt.setText(observableInt.getTxtToAmt());
        txtInter.setText(observableInt.getTxtInter());
        txtPenalInter.setText(observableInt.getTxtPenalInter());
        txtAgainstClearingInter.setText(observableInt.getTxtAgainstClearingInter());
        txtPenalStatement.setText(observableInt.getTxtPenalStatement());
        txtInterExpLimit.setText(observableInt.getTxtInterExpLimit());
    }
    private void rdoInActive_RepaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInActive_RepaymentActionPerformed
        // TODO add your handling code here:
        rdoInActive_RepaymentActionPerformed();
    }//GEN-LAST:event_rdoInActive_RepaymentActionPerformed
    private void rdoInActive_RepaymentActionPerformed() {
        if (rdoInActive_Repayment.isSelected()){
            setRdoRepaymentStatusDisable();
        }
    }
    private void setRdoRepaymentStatusDisable(){
        rdoActive_Repayment.setEnabled(false);
        rdoInActive_Repayment.setEnabled(false);
    }
    private void btnEMI_CalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEMI_CalculateActionPerformed
        // TODO add your handling code here:
        btnEMI_CalculateActionPerformed();
    }//GEN-LAST:event_btnEMI_CalculateActionPerformed
    private void btnEMI_CalculateActionPerformed() {
        if(tblInterMaintenance.getRowCount() > 0){
            updateOBFields();
            HashMap repayData = new HashMap();
            HashMap prodLevelValues = observable.getCompFreqRoundOffValues();
            repayData.put("ACT_NO", lblAccNo_RS_2.getText());
            //            repayData.put("FROM_DATE", tdtFirstInstall.getDateValue());
            if(date !=null)
                repayData.put("FROM_DATE", DateUtil.getStringDate(date));
            else
                repayData.put("FROM_DATE", tdtFacility_Repay_Date.getDateValue());
            //            if (txtNoInstallments.getText().equals("1"))
            //                repayData.put("FROM_DATE", tdtFromDate.getDateValue());
            repayData.put("TO_DATE", tdtLastInstall.getDateValue());
            repayData.put("NO_INSTALL", txtNoInstall.getText());
            repayData.put("ISDURATION_DDMMYY", "YES");
            //            if (rdoInterest_Compound.isSelected()){
            repayData.put("INTEREST_TYPE", "COMPOUND");
            //            }else if (rdoInterest_Simple.isSelected()){
            //                repayData.put("INTEREST_TYPE", "SIMPLE");
            //            }
            repayData.put("DURATION_YY", txtNoInstall.getText());
            //            date=null;
            repayData.put("COMPOUNDING_PERIOD", CommonUtil.convertObjToStr(prodLevelValues.get("DEBITINT_COMP_FREQ")));
            if (cboRepayType.getSelectedItem().equals("User Defined")){
                repayData.put("REPAYMENT_TYPE", observableRepay.getCbmRepayType().getKeyForSelected());
            }else{
                repayData.put("REPAYMENT_TYPE", observableRepay.getCbmRepayType().getKeyForSelected());
            }
            //            repayData.put("EMI_TYPE","UNIFORM_EMI");
            repayData.put("PRINCIPAL_AMOUNT", txtTotalBaseAmt.getText());
            repayData.put("ROUNDING_FACTOR", "1_RUPEE");
            repayData.put("ROUNDING_TYPE", CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
            repayData.put("REPAYMENT_FREQUENCY", observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected());
            java.util.ArrayList interestList= new ArrayList();
            if(loanType.equals("LTD"))
                interestList = observableInt.getInterestDetails(tdtFDate.getDateValue(), tdtTDate.getDateValue(), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
            else  if (txtNoInstallments.getText().equals("1"))
                interestList = observableInt.getInterestDetails(tdtFDate.getDateValue(), tdtTDate.getDateValue(), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
            else
                interestList = observableInt.getInterestDetails(tdtFacility_Repay_Date.getDateValue(), tdtTDate.getDateValue(), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
            if (interestList !=null && interestList.size() > 0){
                repayData.put("INTEREST", ((HashMap)interestList.get(0)).get("INTEREST"));
                repayData.put("VARIOUS_INTEREST_RATE", interestList);
                repayData.put(loanType, "");
                ArrayList deletedRepaySchNosList = observableRepay.getDeletedRepaymentScheduleNos();
                StringBuffer deletedScheduleNos = new StringBuffer();
                int delCount = deletedRepaySchNosList.size();
                if(delCount!=0) {
                    for(int i=0; i<delCount; i++){
                        if(i==0 || i==delCount){
                            deletedScheduleNos.append(CommonUtil.convertObjToDouble(deletedRepaySchNosList.get(i)));
                        } else{
                            deletedScheduleNos.append("," + CommonUtil.convertObjToDouble(deletedRepaySchNosList.get(i)));
                        }
                    }
                    repayData.put("DELETEDSCHEDULES", deletedScheduleNos);
                }
                deletedRepaySchNosList = null;
                int installmentCount = CommonUtil.convertObjToInt(ClientUtil.executeQuery("getCountOfInstallments",repayData).get(0));
//                if (installmentCount>0){
//                    new TermLoanInstallmentUI(this, repayData, true);
//                }else{
//                    new TermLoanInstallmentUI(this, repayData);
//                }
            }else{
                displayAlert(resourceBundle.getString("interestDetailsWarning"));
            }
            repayData = null;
        }else{
            displayAlert(resourceBundle.getString("interestDetailsWarning"));
        }
    }
    private void btnSecurityNo_SecurityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityNo_SecurityActionPerformed
        // TODO add your handling code here:
        callView("SECURITY NO");
    }//GEN-LAST:event_btnSecurityNo_SecurityActionPerformed
    
    private void btnCustID_SecurityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustID_SecurityActionPerformed
        // TODO add your handling code here:
        callView("SECURITY_CUSTOMER ID");
    }//GEN-LAST:event_btnCustID_SecurityActionPerformed
    
    private void rdoNo_DocumentDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNo_DocumentDetailsActionPerformed
        // TODO add your handling code here:
        rdoNo_DocumentDetailsActionPerformed();
    }//GEN-LAST:event_rdoNo_DocumentDetailsActionPerformed
    private void rdoNo_DocumentDetailsActionPerformed() {
        if (rdoNo_DocumentDetails.isSelected()){
            observableDocument.setTdtSubmitDate_DocumentDetails("");
            tdtSubmitDate_DocumentDetails.setDateValue("");
            tdtSubmitDate_DocumentDetails.setEnabled(false);
        }
    }
    private void rdoYes_DocumentDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoYes_DocumentDetailsActionPerformed
        // TODO add your handling code here:
        rdoYes_DocumentDetailsActionPerformed();
    }//GEN-LAST:event_rdoYes_DocumentDetailsActionPerformed
    private void rdoYes_DocumentDetailsActionPerformed() {
        if (rdoYes_DocumentDetails.isSelected()){
            tdtSubmitDate_DocumentDetails.setEnabled(true);
        }
    }
    private void tdtSubmitDate_DocumentDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSubmitDate_DocumentDetailsFocusLost
        // TODO add your handling code here:
        tdtSubmitDate_DocumentDetailsFocusLost();
    }//GEN-LAST:event_tdtSubmitDate_DocumentDetailsFocusLost
    private void tdtSubmitDate_DocumentDetailsFocusLost() {
        // To check whether the Submitted Date is not future date
        ClientUtil.validateLTDate(tdtSubmitDate_DocumentDetails);
    }
    private void btnSave_DocumentDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_DocumentDetailsActionPerformed
        // TODO add your handling code here:
        btnSave_DocumentDetailsActionPerformed();
    }//GEN-LAST:event_btnSave_DocumentDetailsActionPerformed
    private void btnSave_DocumentDetailsActionPerformed() {
        String strWarnMsg = isMandatoryEnteredNDocDetails();
        if (strWarnMsg.length() != 0){
            displayAlert(strWarnMsg);
        }else{
            updateOBFields();
            observableDocument.saveDocumentTab(rowDocument);
            observableDocument.resetDocumentDetails();
            observable.ttNotifyObservers();
            chkDocumentcompleteStateChanged();
            setAllDocumentDetailsEnableDisable(false);
            rowDocument = -1;
            updateDocument = false;
        }
    }
    private String isMandatoryEnteredNDocDetails(){
        StringBuffer stbWarnMsg = new StringBuffer("");
        TermLoanMRB termLoanMRB = new TermLoanMRB();
        if (rdoYes_Executed_DOC.isSelected() && tdtExecuteDate_DOC.getDateValue().length() <= 0){
            stbWarnMsg.append(termLoanMRB.getString("tdtExecuteDate_DOC"));
        }
        if (rdoYes_DocumentDetails.isSelected() && tdtSubmitDate_DocumentDetails.getDateValue().length() <= 0){
            stbWarnMsg.append(termLoanMRB.getString("tdtSubmitDate_DocumentDetails"));
        }
        termLoanMRB = null;
        return stbWarnMsg.toString();
    }
    private void tblTable_DocumentDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTable_DocumentDetailsMousePressed
        // TODO add your handling code here:
        tblTable_DocumentDetailsMousePressed();
    }//GEN-LAST:event_tblTable_DocumentDetailsMousePressed
    
    private void tblTable_DocumentDetailsMousePressed() {
        if (tblTable_DocumentDetails.getSelectedRow() >= 0){
            updateOBFields();
            removeDocumentRadioBtns();
            addDocumentRadioBtns();
            observableDocument.resetDocumentDetails();
            observableDocument.populateDocumentDetails((tblTable_DocumentDetails.getSelectedRow()));
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))){
                // If the record is populated for Delete or Authorization
                setAllDocumentDetailsEnableDisable(false);
                setDocumentToolBtnEnableDisable(false);
            }else{
                setAllDocumentDetailsEnableDisable(true);
                setDocumentToolBtnEnableDisable(true);
                if (observableDocument.getRdoNo_DocumentDetails()){
                    tdtSubmitDate_DocumentDetails.setEnabled(false);
                }
                updateDocument = true;
            }
            rowDocument = tblTable_DocumentDetails.getSelectedRow();
            observable.ttNotifyObservers();
        }
    }
    
    private void txtFacility_Moratorium_PeriodFocusLost() {
        tdtFDateFocusLost();
        tdtTDateFocusLost();
        observable.setTxtFacility_Moratorium_Period(txtFacility_Moratorium_Period.getText());
    }
    
    private void moratorium_Given_Calculation(){
        if (!tdtFDate.getDateValue().equals("") && !cboRepayFreq.getSelectedItem().equals("") && !(txtNoInstallments.getText().length() == 0)){
            if (chkMoratorium_Given.isSelected()){
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
                gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
                int incVal = CommonUtil.convertObjToInt(txtFacility_Moratorium_Period.getText());
                gCalendar.add(gCalendar.MONTH, incVal);
                tdtFacility_Repay_Date.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
                observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue());
                gCalendar = null;
            }else{
                if(!loanType.equals("LTD")){
                    tdtFacility_Repay_Date.setDateValue(tdtFDate.getDateValue());
                    observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue());
                }
            }
        }
    }
    private void txtFacility_Moratorium_PeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFacility_Moratorium_PeriodFocusLost
        // TODO add your handling code here:
        txtFacility_Moratorium_PeriodFocusLost();
    }//GEN-LAST:event_txtFacility_Moratorium_PeriodFocusLost
    private void chkMoratorium_GivenStateChanged() {
        if (chkMoratorium_Given.isSelected()){
            txtFacility_Moratorium_Period.setEditable(true);
            tdtTDateFocusLost();
        }else{
            txtFacility_Moratorium_Period.setEditable(false);
            txtFacility_Moratorium_Period.setText("");
            observable.setTxtFacility_Moratorium_Period("");
            tdtFDateFocusLost();
        }
    }
    private void chkMoratorium_GivenStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkMoratorium_GivenStateChanged
        // TODO add your handling code here:
        chkMoratorium_GivenStateChanged();
    }//GEN-LAST:event_chkMoratorium_GivenStateChanged
                                private void txtNoInstallFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoInstallFocusLost
                                    // TODO add your handling code here:
                                    txtNoInstallFocusLost();
    }//GEN-LAST:event_txtNoInstallFocusLost
                                private void txtNoInstallFocusLost() {
                                    calculateRepayToDate();
                                    observableRepay.setTxtNoInstall(txtNoInstall.getText());
                                }
                                private void calculateRepayToDate(){
                                    if (!tdtFirstInstall.getDateValue().equals("") && !cboRepayFreq_Repayment.getSelectedItem().equals("") && !txtNoInstall.getText().equals("")){
                                        moratorium_Given_Calculation();
                                        java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                                        gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFirstInstall.getDateValue()));
                                        gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtFirstInstall.getDateValue()));
                                        int dateVal = observableRepay.getRepayIncrementType();
                                        int incVal = observable.getInstallNo(txtNoInstall.getText(), dateVal);
                                        if (dateVal <= 7){
                                            gCalendar.add(gCalendar.DATE, incVal);
                                        }else if (dateVal >= 30){
                                            gCalendar.add(gCalendar.MONTH, incVal);
                                        }
                                        //            tdtLastInstall.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
                                        //            observableRepay.setTdtLastInstall(tdtLastInstall.getDateValue());
                                        //            gCalendar = null;
                                        //            tdtLastInstallFocusLost();
                                        //            if (tdtLastInstall.getDateValue().length() == 0){
                                        //                txtNoInstall.setText("");
                                        //                observableRepay.setTxtNoInstall(txtNoInstall.getText());
                                        //            }
                                    }else{
                                        //            tdtLastInstall.setDateValue("");
                                        //            observableRepay.setTdtLastInstall("");
                                    }
                                }
    private void tdtAODDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAODDateFocusLost
        // TODO add your handling code here:
        tdtAODDateFocusLost();
    }//GEN-LAST:event_tdtAODDateFocusLost
    private void tdtAODDateFocusLost() {
        // AOD date have to fall within Demand Promissory Note Issue Date and Expiry Date
        ClientUtil.validateFromDate(tdtAODDate, tdtDemandPromNoteExpDate.getDateValue());
        ClientUtil.validateToDate(tdtAODDate, DateUtil.getStringDate(DateUtil.addDays((Date) currDt.clone(),-1)));
    }
    
    private void txtNoInstallmentsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoInstallmentsFocusLost
        // TODO add your handling code here:
        txtNoInstallmentsFocusLost();
    }//GEN-LAST:event_txtNoInstallmentsFocusLost
    private void txtNoInstallmentsFocusLost() {
        if (cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")){
            moratorium_Given_Calculation();
        }else{
            calculateSanctionToDate();
        }
        populatePeriodDifference();
    }
    private void txtLimit_SDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLimit_SDFocusLost
        // TODO add your handling code here:
        txtLimit_SDFocusLost();
        //od balance check
        txtLimit_SDFocusLostOD();
    }//GEN-LAST:event_txtLimit_SDFocusLost
    private void txtLimit_SDFocusLost(){
        if (!txtLimit_SD.getText().equals("")){
            if (!observable.checkLimitValue(txtLimit_SD.getText())){
                String message = new String("The Limit value must fall within "+observable.getMinLimitValue().toString()+" and  "+observable.getMaxLimitValue().toString());
                displayAlert(message);
                observable.setTxtLimit_SD("");
                txtLimit_SD.setText(observable.getTxtLimit_SD());
                message = null;
            }
        }
    }
    private void txtLimit_SDFocusLostOD(){
        if (!txtLimit_SD.getText().equals("")&& observable.getStrACNumber()!=null && observable.getStrACNumber().length()>0){
            HashMap totbalancemap=new HashMap();
            System.out.println("txtLimit_SD.getText()###"+txtLimit_SD.getText());
            double limit=Double.parseDouble(txtLimit_SD.getText());
            totbalancemap.put("ACT_NUM",observable.getStrACNumber());
            List totbalance=ClientUtil.executeQuery("getActDataAD",totbalancemap);
            if(totbalance !=null && totbalance.size()>0){
                totbalancemap=(HashMap)totbalance.get(0);
                double totbal=CommonUtil.convertObjToDouble(totbalancemap.get("TOTAL_BALANCE")).doubleValue();
                observable.setBEHAVES_LIKE(CommonUtil.convertObjToStr(totbalancemap.get("BEHAVES_LIKE")));
                totbal*=(-1);
                if(limit<totbal){
                    ClientUtil.showMessageWindow("Total balance is"+CommonUtil.convertObjToStr(totbalancemap.get("TOTAL_BALANCE")));
                    txtLimit_SD.setText("");
                    return;
                }
                observable.setRenewAvailableBalance(String .valueOf(limit-totbal));
            }
        }
    }
    private void txtLimit_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLimit_SDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLimit_SDActionPerformed
    
    private void btnRepayment_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepayment_DeleteActionPerformed
        // TODO add your handling code here:
        btnRepayment_DeleteActionPerformed();
    }//GEN-LAST:event_btnRepayment_DeleteActionPerformed
    private void btnRepayment_DeleteActionPerformed() {
        // TODO add your handling code here
        updateOBFields();
        setRepaymentNewOnlyEnable();
        setAllRepaymentDetailsEnableDisable(false);
        setRepaymentBtnsEnableDisable(true);
        observableRepay.deleteRepaymentTabRecord(rowRepayment);
        observableRepay.resetRepaymentSchedule();
        rowRepayment = -1;
        dumRowRepay = -1;
        updateRepayment = false;
        observable.ttNotifyObservers();
    }
    private void btnRepayment_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepayment_SaveActionPerformed
        // TODO add your handling code here:
        btnRepayment_SaveActionPerformed();
    }//GEN-LAST:event_btnRepayment_SaveActionPerformed
    private void btnRepayment_SaveActionPerformed() {
        //            observable.doAction(3);
        // TODO add your handling code here:
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panSchedule_RS);
        String mandatoryMessage2 = "";
        //        if (loanType.equals("OTHERS"))
        mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panInstall_RS);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0){
            displayAlert(mandatoryMessage1+mandatoryMessage2+"\n\nPress EMI Calculator to get above details...");
        }else{
            if(accNumMap.containsKey(lblAccNo_RS_2.getText()))
                accNumMap.remove(lblAccNo_RS_2.getText());
            String strActiveScheduleWarn = observableRepay.validateActiveSchedules(rdoActive_Repayment.isSelected(), txtRepayScheduleMode.getText(), rowRepayment);
            if (strActiveScheduleWarn.length() <= 0){
                if (((tblRepaymentCTable.getRowCount() > 1) && allowMultiRepay && updateRepayment) || (allowMultiRepay && !updateRepayment) ||
                (allowMultiRepay && updateRepayment && dumRowRepay == 0) || (!allowMultiRepay && updateRepayment && tblRepaymentCTable.getRowCount() == 1)
                || ((tblRepaymentCTable.getRowCount() == 0) && !updateRepayment)){
                    // To allow more than one Repayment schedule the repayment type is not as Lump Sum
                    updateOBFields();
                    if (observableRepay.addRepaymentDetails(rowRepayment, updateRepayment) == 1){
                        // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
                    }else{
                        // To reset the Fields
                        setRepaymentNewOnlyEnable();
                        observableRepay.resetRepaymentSchedule();
                        setAllRepaymentDetailsEnableDisable(false);
                    }
                    observable.ttNotifyObservers();
                    rowRepayment = -1;
                    dumRowRepay = -1;
                    updateRepayment = false;
                }else{
                    // Give warning message if the the repayment type is Lump Sum with more than one Repayment Schedule
                    observableRepay.repayTabWarning("existanceRepayLumpSumWarning");
                }
            }else{
                observableRepay.repayTabWarning(strActiveScheduleWarn);
            }
            strActiveScheduleWarn = null;
        }
    }
    private void btnRepayment_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepayment_NewActionPerformed
        // TODO add your handling code here:
        // Don't allow the user to create the schedule, if there is no interest details
        //        if(loanType.equals("OTHERS"))
        if (tblInterMaintenance.getRowCount() > 0 ){
            boolean chekAmt=false;
            boolean acttodate=true;
            if(cboIntGetFrom.getSelectedItem().equals("Account")){
                ArrayList arryList=((EnhancedTableModel)tblInterMaintenance.getModel()).getDataArrayList();
                
                for(int i=0;i<arryList.size();i++){
                    ArrayList listRec= (ArrayList)arryList.get(i);
                    double toAmt =CommonUtil.convertObjToDouble(listRec.get(4)).doubleValue();
                    //                    double limitAmt=CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue();
                    double limitAmt=99999999;
                    if(limitAmt<=toAmt){
                        Date toDate=null;
                        String todate =CommonUtil.convertObjToStr(listRec.get(2));
                        if(todate !=null &&todate.length()>0){
                            toDate=DateUtil.getDateMMDDYYYY(todate);
                            Date sanToDate=DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue());
                            System.out.println("DateUtil.dateDiff(toDate,sanToDate)"+ DateUtil.dateDiff(toDate,sanToDate));
                            if(DateUtil.dateDiff(toDate,sanToDate)>0){
                                displayAlert(resourceBundle.getString("interestDetailsWarningDate"));
                                return;
                                //                                     chekAmt=false;
                                //                                    acttodate=false;
                            }
                        }
                        //                             else
                        //                                 acttodate=false;
                    }  if(limitAmt <= toAmt){
                        chekAmt=true;
                        break;
                    }
                }
                //                   String strRecordKey = CommonUtil.convertObjToStr(((ArrayList) (tblInterestTab.getDataArrayList().get(recordPosition))).get(4));
                if(chekAmt || loanType.equals("LTD"))
                    btnRepayment_NewActionPerformed();
                
                else //if(acttodate)
                    displayAlert(resourceBundle.getString("interestDetailsWarningAmt"));
            }else
                btnRepayment_NewActionPerformed();
            
        }else{
            displayAlert(resourceBundle.getString("interestDetailsWarning"));
        }
    }//GEN-LAST:event_btnRepayment_NewActionPerformed
    private void btnRepayment_NewActionPerformed() {
        // TODO add your handling code here:
        updateOBFields();
        repayNewMode    = true;
        observableRepay.resetRepaymentSchedule();
        //        if(tblRepaymentCTable.getRowCount()>0)
        //            return;
        //        callView("DISBURSEMENT_DETAILS");
        setRepayDefaultValueNewBtnPressed();
        setAllRepaymentDetailsEnableDisable(true);
        setRepaymentDeleteOnlyDisbale();
        rowRepayment = -1;
        dumRowRepay  = -1;
        updateRepayment = false;
        observable.ttNotifyObservers();
        //        txtLaonAmtFocusLost(null);
        //        if (chkMoratorium_Given.isSelected()){
        //            calculateTotalBaseAmount();
        //        }else{
        //            observableRepay.setTxtTotalBaseAmt(observableRepay.getTxtLaonAmt());
        //        }
        //        observable.ttNotifyObservers();
    }
    private void setRepayDefaultValueNewBtnPressed(){
        if (loanType.equals("LTD"))
            observableRepay.setTxtTotalBaseAmt(observable.getTxtLimit_SD());
        observableRepay.setTxtLaonAmt(observable.getTxtLimit_SD());
        txtLaonAmt.setText(observableRepay.getTxtLaonAmt());
        //         if (loanType.equals("OTHERS")) commented by abi for not need
        observableRepay.setTdtFirstInstall(observable.getTdtFacility_Repay_Date());
        observableRepay.setTdtLastInstall(observable.getTdtTDate());
        observableRepay.setTxtNoMonthsMora(observable.getTxtFacility_Moratorium_Period());
        observableRepay.setTxtNoInstall(observable.getTxtNoInstallments());
        observableRepay.setCboRepayFreq_Repayment(CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()));
        observableRepay.setTdtDisbursement_Dt(DateUtil.getStringDate((Date) currDt.clone()));
        observableRepay.setTxtRepayScheduleMode(CommonConstants.REPAY_SCHEDULE_MODE_REGULAR);
        cboRepayFreq_Repayment.setSelectedItem(cboRepayFreq.getSelectedItem());
        removeRepaymentRadioBtns();
        observableRepay.setRdoDoAddSIs_Yes(true);
        rdoDoAddSIs_Yes.setSelected(observableRepay.getRdoDoAddSIs_Yes());
        observableRepay.setRdoDoAddSIs_No(false);
        rdoDoAddSIs_No.setSelected(observableRepay.getRdoDoAddSIs_No());
        observableRepay.setRdoPostDatedCheque_Yes(true);
        rdoPostDatedCheque_Yes.setSelected(observableRepay.getRdoPostDatedCheque_Yes());
        observableRepay.setRdoPostDatedCheque_No(false);
        rdoPostDatedCheque_No.setSelected(observableRepay.getRdoPostDatedCheque_No());
        observableRepay.setRdoActive_Repayment(true);
        rdoActive_Repayment.setSelected(observableRepay.getRdoActive_Repayment());
        observableRepay.setRdoInActive_Repayment(false);
        rdoInActive_Repayment.setSelected(observableRepay.getRdoInActive_Repayment());
        addRepaymentRadioBtns();
        txtLaonAmtFocusLost();
    }
    private void calculateTotalBaseAmount(){
        HashMap repayData = new HashMap();
        HashMap prodLevelValues = observable.getCompFreqRoundOffValues();
        repayData.put("ACT_NO", lblAccNo_RS_2.getText());
        //        repayData.put("TO_DATE", tdtFirstInstall.getDateValue());
        //        if(!(repayData.get("TO_DATE")!=null))//commentedby abi for componet not having date value
        repayData.put("TO_DATE",observableRepay.getTdtFirstInstall());
        repayData.put("ISDURATION_DDMMYY", "NO");
        //        if (rdoInterest_Compound.isSelected()){
        repayData.put("INTEREST_TYPE", "COMPOUND");
        //        }else if (rdoInterest_Simple.isSelected()){
        //            repayData.put("INTEREST_TYPE", "SIMPLE");
        //        }
        repayData.put("COMPOUNDING_PERIOD", CommonUtil.convertObjToStr(prodLevelValues.get("DEBITINT_COMP_FREQ")));
        repayData.put("REPAYMENT_TYPE", "MORATORIUM");
        repayData.put("COMPOUNDING_TYPE", "REPAYMENT");
        repayData.put("PRINCIPAL_AMOUNT", txtLaonAmt.getText());
        repayData.put("ROUNDING_FACTOR", "1_RUPEE");
        repayData.put("ROUNDING_TYPE", CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
        repayData.put("REPAYMENT_FREQUENCY", observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected());
        java.util.ArrayList interestList = observableInt.getInterestDetails(tdtSanctionDate.getDateValue(), tdtFacility_Repay_Date.getDateValue(), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
        if (interestList.size() > 0){
            repayData.put("INTEREST", ((HashMap)interestList.get(0)).get("INTEREST"));
            repayData.put("VARIOUS_INTEREST_RATE", interestList);
            repayData.put("FROM_DATE",tdtFDate.getDateValue());
            observableRepay.calculateTotalBaseAmount(repayData);
        }else{
            displayAlert(resourceBundle.getString("interestDetailsWarning"));
        }
        repayData = null;
    }
    private void tblRepaymentCTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRepaymentCTableMousePressed
        // TODO add your handling code here:
        tblRepaymentCTableMousePressed();
    }//GEN-LAST:event_tblRepaymentCTableMousePressed
    private void tblRepaymentCTableMousePressed() {
        // TODO add your handling code here:
        
        if (tblRepaymentCTable.getSelectedRow() >= 0 || sanMousePress){
            // If the table is in editable mode
            repayNewMode    = false;
            if(sanMousePress)
                dumRowRepay     = 0;
            else
                dumRowRepay     = tblRepaymentCTable.getSelectedRow();
            updateOBFields();
            //            setAllRepaymentBtnsEnableDisable(true);
            //            setAllRepaymentDetailsEnableDisable(true);
            removeRepaymentRadioBtns();
            addRepaymentRadioBtns();
            observableRepay.resetRepaymentSchedule();
            //            observableRepay.populateRepaymentDetails(tblRepaymentCTable.getSelectedRow());
            observableRepay.populateRepaymentDetails(dumRowRepay);
            txtNoMonthsMora.setText(txtFacility_Moratorium_Period.getText());
            observableRepay.setTxtNoMonthsMora(txtNoMonthsMora.getText());
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) ||  (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW)|| (observable.getCboAccStatus().equals("Closed")))){
                // If the record is populated for Delete or Authorization
                setAllRepaymentBtnsEnableDisable(false);
                setAllRepaymentDetailsEnableDisable(false);
                //                btnEMI_Calculate.setEnabled(false); need for authorization
                if (loanType.equals("OTHERS"))
                    btnEMI_Calculate.setEnabled(true);
                updateRepayment = true;
            }else{
                setAllRepaymentBtnsEnableDisable(true);
                setAllRepaymentDetailsEnableDisable(true);
                cboRepayTypeActionPerformed();
                setRepaymentBtnsEnableDisable(false);
                //                if(observable.getLblStatus().equals("Inserted"))
                //                    updateRepayment = false;
                //                else
                updateRepayment = true;
            }
            //            rowRepayment = tblRepaymentCTable.getSelectedRow();
            rowRepayment = dumRowRepay;
            observable.ttNotifyObservers();
        }
    }
    private void tdtDateEstablishmentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateEstablishmentFocusLost
        // TODO add your handling code here:
        tdtDateEstablishmentFocusLost();
    }//GEN-LAST:event_tdtDateEstablishmentFocusLost
    private void tdtDateEstablishmentFocusLost() {
        // TODO add your handling code here:
        // To check the entered date is less than or equal to current date
        ClientUtil.validateLTDate(tdtDateEstablishment);
    }
    private void tdtCreditFacilityAvailSinceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCreditFacilityAvailSinceFocusLost
        // TODO add your handling code here:
        tdtCreditFacilityAvailSinceFocusLost();
    }//GEN-LAST:event_tdtCreditFacilityAvailSinceFocusLost
    private void tdtCreditFacilityAvailSinceFocusLost() {
        // TODO add your handling code here:
        // To check the entered date is less than or equal to current date
        ClientUtil.validateLTDate(tdtCreditFacilityAvailSince);
    }
    private void tdtDealingWithBankSinceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDealingWithBankSinceFocusLost
        // TODO add your handling code here:
        tdtDealingWithBankSinceFocusLost();
    }//GEN-LAST:event_tdtDealingWithBankSinceFocusLost
    private void tdtDealingWithBankSinceFocusLost(){
        // To check the entered date is less than or equal to current date
        ClientUtil.validateLTDate(tdtDealingWithBankSince);
    }
    private void cboTypeOfFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTypeOfFacilityActionPerformed
        // Add your handling code here:
        cboTypeOfFacilityActionPerformed();
    }//GEN-LAST:event_cboTypeOfFacilityActionPerformed
    private void cboTypeOfFacilityActionPerformed(){
        // Account Head should be resetted since its value will be changed
        // based on Type of Facility and Product ID
        observable.setLblAccHead_2("");
        lblAccHead_2.setText(observable.getLblAccHead_2());
        // update the Type of Facility in Observable
        String strFacilityType = getCboTypeOfFacilityKeyForSelected();
        System.out.println("@@@@@@@@@strFacilityType"+strFacilityType);
        if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
            //            if (!observable.isThisCustDepositAcctHolder(txtCustID.getText())){
            //                clearFieldsForDefaultFacilityType();  // Commented by Rajesh
            //                return;
            //            }
        }else if (strFacilityType.length() > 0){
            observable.setProductFacilityType(strFacilityType);
            if (!observable.isThisCustShareHolder(txtCustID.getText())){
                clearFieldsForDefaultFacilityType();
                return;
            }
        }
        observable.setCboTypeOfFacility(CommonUtil.convertObjToStr(cboTypeOfFacility.getSelectedItem()));
        if (strFacilityType.equals("CC") || strFacilityType.equals("OD") || (strFacilityType.equals("BILLS"))){
            changesInUIForAdvanceProducts();
            if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))){
                changesInUIExceptLoanAgainstDeposit();
            }
        }else{
            changesInUIForLoanProducts();
            if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
                changesInUIForLoanAgainstDeposit();
            }else if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))){
                changesInUIExceptLoanAgainstDeposit();
            }
        }
        // Set the Product ID model according to the Type of Facility
        if (observable.getCboTypeOfFacility().length() > 0){
            observable.setFacilityProductID();
        }else{
            observable.setProdIDAsBlank();
        }
        // Update Product ID in the Observer thru Product ID ComboBoxModel
        cboProductId.setModel(observable.getCbmProductId());
        //        if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
        //            if (cboProductId.getItemCount() > 1)
        //                cboProductId.setSelectedIndex(1);
        //        }
        strFacilityType = null;
        // Populate the Product ID which is in the existing record
        if(sandetail){
            observable.populateSanctionTabProdID(rowSanctionFacility);
        }
        else
            observable.populateSanctionTabProdID(tblSanctionDetails.getSelectedRow());
        // Update the selected Item
        cboProductId.setSelectedItem(observable.getCboProductId());
        //        lblAccHead_2.setText(observable.getLblAccHead_2());
        
    }
    
    private String getCboTypeOfFacilityKeyForSelected(){
        return CommonUtil.convertObjToStr(((ComboBoxModel) cboTypeOfFacility.getModel()).getKeyForSelected());
    }
    
    private void clearFieldsForDefaultFacilityType(){
        cboTypeOfFacility.setSelectedItem("");
        observable.setProdIDAsBlank();
        cboProductId.setModel(observable.getCbmProductId());
        observable.setCboProductId("");
        cboProductId.setSelectedItem(observable.getCboProductId());
        if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))){
            changesInUIExceptLoanAgainstDeposit();
        }
    }
    
    private void changesInUIForAdvanceProducts(){
        tabLimitAmount.add(panAccountDetails, "Other Details", 8);
        fieldsToHideBasedOnAccount(false);
        lblNoInstallments.setText(resourceBundle.getString("lblNoInstallments_PROD"));
        lblRepayFreq.setText("");
        if (!(viewType.equals(AUTHORIZE) || viewType.equals("Delete"))) {
            observable.setCbmRepayFreq(observable.getCbmRepayFreq_ADVANCE());
            cboRepayFreq.setModel(observable.getCbmRepayFreq());
        }
        if ((observable.getStrACNumber().length() <= 0) || (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
            ClientUtil.enableDisable(panAccountDetails, false);
        }else{
            ClientUtil.enableDisable(panAccountDetails, true);
            disableLastIntApplDate();
        }
        tabLimitAmount.remove(panRepaymentSchedule);
    }
    
    private void disableLastIntApplDate(){
        tdtDebit.setEnabled(false);
        tdtCredit.setEnabled(false);
    }
    
    private void changesInUIForLoanProducts(){
        fieldsToHideBasedOnAccount(true);
        tabLimitAmount.add(panRepaymentSchedule, "Repayment Schedule", 8);
        tabLimitAmount.remove(panAccountDetails);
        lblNoInstallments.setText(resourceBundle.getString("lblNoInstallments"));
        lblRepayFreq.setText(resourceBundle.getString("lblRepayFreq"));
        if (!(viewType.equals(AUTHORIZE) || viewType.equals("Delete"))) {
            observable.setCbmRepayFreq(observable.getCbmRepayFreq_LOAN());
            cboRepayFreq.setModel(observable.getCbmRepayFreq());
        }
    }
    
    private void changesInUIForLoanAgainstDeposit(){
        HashMap whereMap = new HashMap();
        HashMap keyMap = new HashMap();
        HashMap depositName=null;
        boolean haveData = false;
        
        if (observable.getStrACNumber().length() > 0){
            keyMap.put("ACCT_NO", observable.getStrACNumber());
            
            whereMap.put(CommonConstants.MAP_NAME, "getDepositLienDetails");
            whereMap.put(CommonConstants.MAP_WHERE, keyMap);
            java.util.List lst=ClientUtil.executeQuery("getDepositLienHolderName", keyMap);
            if(lst.size()>0){
                depositName=(HashMap)lst.get(0);
                haveData = ClientUtil.setTableModel(whereMap, tblSecurityTable, false);
                observableSecurity.setLblCustName_Security_Display(CommonUtil.convertObjToStr(depositName.get("NAME")));
                lblCustName_Security_Display.setText(CommonUtil.convertObjToStr(depositName.get("NAME")));
            }
            else
                ClientUtil.showMessageWindow("Deposit Lien details not found...");
        }
        
        if (!haveData){
            observableSecurity.setTblDepositSecurityTable();
            tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
        } else {
            observable.setDepositNo(CommonUtil.convertObjToStr(tblSecurityTable.getValueAt(0,0)));
            observableSecurity.setTblSecurityTab((com.see.truetransact.clientutil.EnhancedTableModel)tblSecurityTable.getModel());
        }
        
        whereMap = null;
        keyMap = null;
        setAllSecurityDetailsEnableDisable(false);
        setAllSecurityBtnsEnableDisable(false);
        tblSecurityTable.setEnabled(false);
    }
    
    private void changesInUIExceptLoanAgainstDeposit(){
        observableSecurity.setTblSecurityTable();
        tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
        tblSecurityTable.setEnabled(true);
    }
    
    private void fieldsToHideBasedOnAccount(boolean val){
        lblMoratorium_Given.setVisible(val);
        chkMoratorium_Given.setVisible(val);
        txtFacility_Moratorium_Period.setVisible(val);
        lblFacility_Moratorium_Period.setVisible(val);
        lblFacility_Repay_Date.setVisible(val);
        tdtFacility_Repay_Date.setVisible(val);
        lblMultiDisburseAllow.setVisible(val);
        rdoMultiDisburseAllow_No.setVisible(val);
        rdoMultiDisburseAllow_Yes.setVisible(val);
    }
    private void rdoInterest_SimpleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoInterest_SimpleMousePressed
        // Add your handling code here:
    }//GEN-LAST:event_rdoInterest_SimpleMousePressed
                                                            private void txtToAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAmtFocusLost
                                                                // Add your handling code here:
                                                                txtToAmtFocusLost();
    }//GEN-LAST:event_txtToAmtFocusLost
                                                            private void txtToAmtFocusLost(){
                                                                // To check whether the From amount is less than the To amount
                                                                updateFromToAmountOB();
                                                                if (!txtFromAmt.getText().equals("")){
                                                                    if (CommonUtil.convertObjToInt(txtFromAmt.getText()) > CommonUtil.convertObjToInt(txtToAmt.getText())){
                                                                        observableInt.setTxtToAmt("");
                                                                    }
                                                                }
                                                                updateFromToAmount();
                                                            }
    private void txtFromAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAmtFocusLost
        // Add your handling code here:
        txtFromAmtFocusLost();
    }//GEN-LAST:event_txtFromAmtFocusLost
    private void txtFromAmtFocusLost(){
        // To check whether the From amount is less than the To amount
        updateFromToAmountOB();
        if (!txtToAmt.getText().equals("")){
            if (CommonUtil.convertObjToInt(txtFromAmt.getText()) > CommonUtil.convertObjToInt(txtToAmt.getText())){
                observableInt.setTxtFromAmt("");
            }
        }
        updateFromToAmount();
    }
    
    private void updateFromToAmountOB(){
        observableInt.setTxtFromAmt(txtFromAmt.getText());
        observableInt.setTxtToAmt(txtToAmt.getText());
    }
    
    private void updateFromToAmount(){
        txtFromAmt.setText(observableInt.getTxtFromAmt());
        txtToAmt.setText(observableInt.getTxtToAmt());
    }
    private void tdtAsOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsOnFocusLost
        // Add your handling code here:
        tdtAsOnFocusLost();
    }//GEN-LAST:event_tdtAsOnFocusLost
    private void tdtAsOnFocusLost(){
        // To check the entered date is less than or equal to current date
        ClientUtil.validateLTDate(tdtAsOn);
    }
    
    private void tdtTDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTDateFocusLost
        // Add your handling code here:
        tdtTDateFocusLost();
    }//GEN-LAST:event_tdtTDateFocusLost
    private void tdtTDateFocusLost(){
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtTDate, tdtFDate.getDateValue());
        // To check whether this To date is greater than this details repayment date
        int installmentInt=0;
        if((!txtNoInstallments.getText().equals(""))){
            String instNo=txtNoInstallments.getText();
            installmentInt=Integer.parseInt(instNo);
        }
        if(loanType.equals("OTHERS")){
            if (cboRepayFreq.getSelectedItem().equals("Lump Sum"))
                tdtFacility_Repay_Date.setDateValue(tdtTDate.getDateValue());
            else if(installmentInt>1)
                ClientUtil.validateToDate(tdtTDate, tdtFacility_Repay_Date.getDateValue());
            tdtTDate.setEnabled(true);
        }else
            tdtTDate.setEnabled(false);
        populatePeriodDifference();
    }
    private void tdtFDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFDateFocusLost
        // Add your handling code here:
        tdtFDateFocusLost();
        //        txtNoInstallmentsFocusLost();
    }//GEN-LAST:event_tdtFDateFocusLost
    private void tdtFDateFocusLost(){
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtFDate, tdtTDate.getDateValue());
        // To check whether this From date is greater than the sanction date
        if (!tdtFDate.getDateValue().equals(tdtSanctionDate.getDateValue())){
            ClientUtil.validateToDate(tdtFDate, tdtSanctionDate.getDateValue());
        }
        if (cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")){
            moratorium_Given_Calculation();
        }else{
            calculateSanctionToDate();
        }
        populatePeriodDifference();
        txtNoMonthsMora.setText(txtFacility_Moratorium_Period.getText());
        observableRepay.setTxtNoMonthsMora(txtNoMonthsMora.getText());
    }
    private void calculateSanctionToDate(){
        if (! (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete")))
            if (!tdtFDate.getDateValue().equals("") && !cboRepayFreq.getSelectedItem().equals("") && !txtNoInstallments.getText().equals("")){
                moratorium_Given_Calculation();
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt
                gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
                gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
                gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
                gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
                int dateVal = observable.getIncrementType();
                int incVal = observable.getInstallNo(txtNoInstallments.getText(), dateVal);
                date=new java.util.Date();
                date=DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue());
                if (txtNoInstallments.getText().equals("1"))
                    date=DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue());
                System.out.println("Date##"+date);
                if (dateVal <= 7){
                    gCalendar.add(gCalendar.DATE, incVal);
                }else if (dateVal >= 30){
                    gCalendar.add(gCalendar.MONTH, incVal);
                    int firstInstall=dateVal/30;
                    gCalendarrepaydt.add(gCalendarrepaydt.MONTH,firstInstall);//for repaydate
                }
                tdtTDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
                
                observable.setTdtTDate(tdtTDate.getDateValue());
                tdtFirstInstall.setDateValue(tdtFacility_Repay_Date.getDateValue());//repay
                tdtFacility_Repay_Date.setDateValue(DateUtil.getStringDate(gCalendarrepaydt.getTime()));
                observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue()); //for repaydate
                gCalendar = null;
                gCalendarrepaydt=null;
            }else{
                tdtTDate.setDateValue("");
                observable.setTdtTDate("");
                tdtFacility_Repay_Date.setDateValue("");
                observable.setTdtFacility_Repay_Date("");
                observable.setTxtPeriodDifference_Days("");
                observable.setTxtPeriodDifference_Months("");
                observable.setTxtPeriodDifference_Years("");
                updatePeriodDifference();
            }
    }
    
    private void populatePeriodDifference(){
        if (!tdtFDate.getDateValue().equals("") && !tdtTDate.getDateValue().equals("") && !tdtFacility_Repay_Date.getDateValue().equals("") && !cboRepayFreq.getSelectedItem().equals("") && !txtNoInstallments.getText().equals("")){
            if (CommonUtil.convertObjToStr(((ComboBoxModel)cboRepayFreq.getModel()).getKeyForSelected()).equals("0")){
                observable.populatePeriodDifference(tdtFDate.getDateValue(), tdtTDate.getDateValue());
            }else{
                observable.populatePeriodDifference(txtNoInstallments.getText(), CommonUtil.convertObjToStr(((ComboBoxModel)cboRepayFreq.getModel()).getKeyForSelected()), txtFacility_Moratorium_Period.getText());
            }
            updatePeriodDifference();
        }else{
            observable.setTxtPeriodDifference_Days("");
            observable.setTxtPeriodDifference_Months("");
            observable.setTxtPeriodDifference_Years("");
            updatePeriodDifference();
        }
    }
    private void updatePeriodDifference(){
        txtPeriodDifference_Days.setText(observable.getTxtPeriodDifference_Days());
        txtPeriodDifference_Months.setText(observable.getTxtPeriodDifference_Months());
        txtPeriodDifference_Years.setText(observable.getTxtPeriodDifference_Years());
    }
    private void tdtDemandPromNoteExpDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDemandPromNoteExpDateFocusLost
        // Add your handling code here:
        tdtDemandPromNoteExpDateFocusLost();
    }//GEN-LAST:event_tdtDemandPromNoteExpDateFocusLost
    private void tdtDemandPromNoteExpDateFocusLost(){
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtDemandPromNoteExpDate, tdtDemandPromNoteDate.getDateValue());
    }
    private void tdtDemandPromNoteDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDemandPromNoteDateFocusLost
        // Add your handling code here:
        tdtDemandPromNoteDateFocusLost();
    }//GEN-LAST:event_tdtDemandPromNoteDateFocusLost
    private void tdtDemandPromNoteDateFocusLost(){
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtDemandPromNoteDate, tdtDemandPromNoteExpDate.getDateValue());
        if (observable.getStrACNumber().equals("")){
            calculateDPNExpDate();
        }
    }
    private void calculateDPNExpDate(){
        if (tdtDemandPromNoteDate.getDateValue().length() > 0){
            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtDemandPromNoteDate.getDateValue()));
            gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtDemandPromNoteDate.getDateValue()));
            gCalendar.add(gCalendar.YEAR, 3);
            tdtDemandPromNoteExpDate.setDateValue(DateUtil.getStringDate(DateUtil.addDays(gCalendar.getTime(), -1)));
            observable.setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(tdtDemandPromNoteExpDate.getDateValue()));
            gCalendar = null;
        }
    }
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // Add your handling code here:
        tdtToDateFocusLost();
    }//GEN-LAST:event_tdtToDateFocusLost
    private void tdtToDateFocusLost(){
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtToDate, tdtFromDate.getDateValue());
    }
    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // Add your handling code here:
        tdtFromDateFocusLost();
    }//GEN-LAST:event_tdtFromDateFocusLost
    private void tdtFromDateFocusLost(){
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtFromDate, tdtToDate.getDateValue());
    }
    private void tdtAsOn_GDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsOn_GDFocusLost
        // Add your handling code here:
        tdtAsOn_GDFocusLost();
    }//GEN-LAST:event_tdtAsOn_GDFocusLost
    private void tdtAsOn_GDFocusLost(){
        // To check the entered date is less than or equal to current date
        ClientUtil.validateLTDate(tdtAsOn_GD);
    }    private void tdtLastInstallFocusLost(){
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtLastInstall, tdtFirstInstall.getDateValue());
        // To check whether this To date is fall within Facility details To date
        if (!tdtLastInstall.getDateValue().equals(tdtTDate.getDateValue())){
            ClientUtil.validateFromDate(tdtLastInstall, tdtTDate.getDateValue());
        }
        // To check whether this To date is fall within Facility details From date
        ClientUtil.validateToDate(tdtLastInstall, tdtFDate.getDateValue());
        // To check whether this To date is fall within Facility details Repayment date
        ClientUtil.validateToDate(tdtLastInstall, tdtFacility_Repay_Date.getDateValue());
    }
    private void tdtFirstInstallFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFirstInstallFocusLost
        // Add your handling code here:
        tdtFirstInstallFocusLost();
    }//GEN-LAST:event_tdtFirstInstallFocusLost
    private void tdtFirstInstallFocusLost(){
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtFirstInstall, tdtLastInstall.getDateValue());
        // To check whether this From date is less than Facility details To date
        ClientUtil.validateFromDate(tdtFirstInstall, tdtTDate.getDateValue());
        // To check whether this From date is greater than Facility details From date
        ClientUtil.validateToDate(tdtFirstInstall, tdtFDate.getDateValue());
        // To check whether this From date is greater than Facility details Repayment date
        if (! tdtFirstInstall.getDateValue().equals(tdtFacility_Repay_Date.getDateValue())){
            ClientUtil.validateToDate(tdtFirstInstall, tdtFacility_Repay_Date.getDateValue());
        }
        calculateRepayToDate();
    }
    private void btnDeleteBorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBorrowerActionPerformed
        // Add your handling code here:
        if (poaUI.checkCustIDExistInJointAcctAndPoA(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1)))){
            btnDeleteBorrowerActionPerformed();
        }
    }//GEN-LAST:event_btnDeleteBorrowerActionPerformed
    private void btnDeleteBorrowerActionPerformed(){
        updateOBFields();
        setBorrowerNewOnlyEnable();
        String strCustIDToDel = CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1));
        observableBorrow.deleteJointAccntHolder(strCustIDToDel, tblBorrowerTabCTable.getSelectedRow());
        observableBorrow.resetBorrowerTabCustomer();
        authSignUI.removeAcctLevelCustomer(strCustIDToDel);
        strCustIDToDel = null;
        observable.ttNotifyObservers();
    }
    private void btnDelete_BorrowerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_BorrowerMousePressed
        // Add your handling code here:
    }//GEN-LAST:event_btnDelete_BorrowerMousePressed
    
    private void btnToMain_BorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToMain_BorrowerActionPerformed
        // Add your handling code here:
        btnToMain_BorrowerActionPerformed();
    }//GEN-LAST:event_btnToMain_BorrowerActionPerformed
    private void btnToMain_BorrowerActionPerformed(){
        updateOBFields();
        setBorrowerNewOnlyEnable();
        observableComp.resetCustomerDetails();
        observableBorrow.moveToMain(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1)), tblBorrowerTabCTable.getSelectedRow());
        observable.ttNotifyObservers();
    }
//GEN-FIRST:event_btnDelete_BorrowerActionPerformed
//GEN-LAST:event_btnDelete_BorrowerActionPerformed
                                                            private void btnNew_BorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_BorrowerActionPerformed
                                                                // Add your handling code here:
                                                                if(tblBorrowerTabCTable.getRowCount() != 0){
                                                                    // If the Main Accnt Holder is selected,
                                                                    callView("JOINT ACCOUNT");
                                                                    // Allow the user to add Jnt Acct Holder
                                                                }else{
                                                                    // Else if the Main Acct Holder is not selected, prompt the user to select
                                                                    // the Main Acct. holder
                                                                    observableBorrow.mainCustDoesntExistWarn();
                                                                    btnCustID.requestFocus(true);
                                                                }
    }//GEN-LAST:event_btnNew_BorrowerActionPerformed
                                                            
    private void tblBorrowerTabCTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBorrowerTabCTableMousePressed
        // Add your handling code here:
        
        observableBorrow.btnPressed = true;
        observableBorrow.resetBorrowerTabCustomer();
        if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
            // Don't do anything if the record is in Delete or Authroization mode
            setAllBorrowerBtnsEnableDisable(false);
            setCompanyDetailsEnableDisable(false);
            setBorrowerDetailsEnableDisable(false);
            HashMap cust = new HashMap();
            cust.put("CUST_ID",tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1));
            observableBorrow.populateBorrowerTabCustomerDetails(cust, true, loanType);
            updateBorrowerTabCustDetails();
            cust = null;
        }else{
            tblBorrowerTabCTableMousePressed(tblBorrowerTabCTable.getSelectedRow());
        }
    }//GEN-LAST:event_tblBorrowerTabCTableMousePressed
    private void tblBorrowerTabCTableMousePressed(int rowSelected){
        if(tblBorrowerTabCTable.getSelectedRow() != 0){
            if ((((ComboBoxModel)cboConstitution.getModel()).getKeyForSelected()).equals(JOINT_ACCOUNT)){
                setAllBorrowerBtnsEnableDisable(true);
            }else{
                setAllBorrowerBtnsEnableDisable(false);
            }
        }else{
            if ((((ComboBoxModel)cboConstitution.getModel()).getKeyForSelected()).equals(JOINT_ACCOUNT)){
                setBorrowerNewOnlyEnable();
            }else{
                setAllBorrowerBtnsEnableDisable(false);
            }
        }
        HashMap cust = new HashMap();
        cust.put("CUST_ID",tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1));
        observableBorrow.populateBorrowerTabCustomerDetails(cust, true, loanType);
        updateBorrowerTabCustDetails();
        cust = null;
    }
    private void cboConstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboConstitutionActionPerformed
        // Add your handling code here:
        observableBorrow.setCboConstitution(CommonUtil.convertObjToStr(cboConstitution.getSelectedItem()));
        if (observableBorrow.getCboConstitution().length() > 0){
            cboConstitutionActionPerformed();
            validateConstitutionCustID();
        }
    }//GEN-LAST:event_cboConstitutionActionPerformed
    private void cboConstitutionActionPerformed(){
        if (observableBorrow.getCbmConstitution().getKeyForSelected().equals(JOINT_ACCOUNT)){
            setBorrowerNewOnlyEnable();
        }else{
            // To delete all the Customer(Excluding Main) Records
            // when Constitution is not Joint Account
            checkJointAccntHolderForData();
        }
    }
    
    private String isJointAcctHavingAtleastOneCust(){
        StringBuffer stbWarnMsg = new StringBuffer("");
        if (observableBorrow.getCbmConstitution().getKeyForSelected().equals(JOINT_ACCOUNT) && tblBorrowerTabCTable.getRowCount() <= 1){
            stbWarnMsg.append("\n");
            stbWarnMsg.append(resourceBundle.getString("jointAcctDontHaveProperCustDetailsWarning"));
        }
        return stbWarnMsg.toString();
    }
    
    private void addCustIDNAuthSignatory(){
        int borrowerTabRowCount = tblBorrowerTabCTable.getRowCount();
        for (int i = borrowerTabRowCount - 1,j = 0;i >= 0;--i,++j){
            authSignUI.addAcctLevelCustomer(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(j, 1)));
        }
    }
    
    private void removedJointAcctCustIDNAuthSignatory(){
        int borrowerTabRowCount = tblBorrowerTabCTable.getRowCount();
        for (int i = borrowerTabRowCount - 1,j = 1;i >= 1;--i,++j){
            authSignUI.removeAcctLevelCustomer(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(j, 1)));
        }
    }
    
    private void checkJointAccntHolderForData(){
        if(tblBorrowerTabCTable.getRowCount()>1){
            int reset = observableBorrow.jointAcctWarn();
            if (reset == 0){
                removedJointAcctCustIDNAuthSignatory();
                observableBorrow.resetBorrowerTabCTable();
                custInfoDisplay(txtCustID.getText(), loanType);
                poaUI.resetPoACustID(txtCustID.getText());
                observableBorrow.resetBorrowerTabCustomer();
                updateBorrowerTabCustDetails();
                setAllBorrowerBtnsEnableDisable(false);
                poaUI.setCboPoACustModel();
            }else if (reset == 1){
                observableBorrow.setCboConstitution(CommonUtil.convertObjToStr(observableBorrow.getCbmConstitution().getDataForKey(JOINT_ACCOUNT)));
                cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
            }
        }else{
            setAllBorrowerBtnsEnableDisable(false);
        }
    }
    
    public void callView(String currField) {
        viewType = currField;
        authSignUI.setViewType(viewType);
        poaUI.setViewType(viewType);
        // If Customer Id is selected OR JointAccnt New is clciked, show the popup Screen of Customer Table
        if ((currField.equals("CUSTOMER ID")) || (currField.equals("JOINT ACCOUNT"))){
            HashMap viewMap = new HashMap();
            StringBuffer presentCust = new StringBuffer();
            int jntAccntTablRow = tblBorrowerTabCTable.getRowCount();
            if(tblBorrowerTabCTable.getRowCount()!=0) {
                for(int i =0, sizeJointAcctAll = tblBorrowerTabCTable.getRowCount();i<sizeJointAcctAll;i++){
                    if(i==0 || i==sizeJointAcctAll){
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(i, 1)) + "'");
                    } else{
                        presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(i, 1)) + "'");
                    }
                }
            }
            if (loanType.equals("LTD")) {
                viewMap.put("MAPNAME", "getSelectCustListForLTD");
            } else {
                viewMap.put("MAPNAME", "getSelectLoanAccInfoList");
            }
            HashMap whereMap = new HashMap();
            whereMap.put("CUSTOMER_ID", presentCust);
            //            String strSelectedProdType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
            //            if (!strSelectedProdType.equals(LOANS_AGAINST_DEPOSITS)){
            //                // If the Product is not Loan against Term Deposit
            //                // then the customer should be a share holder
            //                // else the customer should be Term Deposit Holder
            //                whereMap.put(IS_COOPERATIVE, CommonConstants.IS_COOPERATIVE);
            //            }
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put("CURR_DT",currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
            presentCust = null;
        }else if (currField.equals("SECURITY_CUSTOMER ID")){
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectCustSecurityTOList");
            HashMap whereMap = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }else if (currField.equals("SECURITY NO")){
            HashMap viewMap = new HashMap();
            String strCurrentCustID = txtCustID_Security.getText();
            StringBuffer presentSecNo = new StringBuffer("");
            int securityTablRow = tblSecurityTable.getRowCount();
            int thisCustIDCount = 0;
            int custIDCounter   = 0;
            if(securityTablRow!=0) {
                for (int i = 0, sizeSecurity = securityTablRow;i < sizeSecurity;i++){
                    if (tblSecurityTable.getValueAt(i, 1).equals(strCurrentCustID)){
                        thisCustIDCount++;
                    }
                }
                for(int i =0, sizeSecurity = securityTablRow;i < sizeSecurity;i++){
                    if (tblSecurityTable.getValueAt(i, 1).equals(strCurrentCustID)){
                        if(custIDCounter == 0 || thisCustIDCount == custIDCounter){
                            log.info(tblSecurityTable.getValueAt(i, 2));
                            presentSecNo.append("'" + CommonUtil.convertObjToStr(tblSecurityTable.getValueAt(i, 2)) + "'");
                        } else{
                            presentSecNo.append("," + "'" + CommonUtil.convertObjToStr(tblSecurityTable.getValueAt(i, 2)) + "'");
                        }
                        custIDCounter++;
                    }
                }
            }
            viewMap.put("MAPNAME", "getSelectCustSecurityNoTOList");
            HashMap whereMap = new HashMap();
            whereMap.put("CUST_ID", strCurrentCustID);
            whereMap.put("SECURITY_NO", presentSecNo);
            whereMap.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()));
            whereMap.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
            
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
            presentSecNo = null;
            strCurrentCustID = null;
        }else if (currField.equals("DISBURSEMENT_DETAILS")){
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getDisbursementDetails");
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", observable.getStrACNumber());
            HashMap statusMap = observableRepay.getActiveAndInActiveScheduleNo();
            if (statusMap.containsKey("INACTIVE_NO")){
                whereMap.put("REPAYMENT_SCHEDULE_NO", statusMap.get("INACTIVE_NO"));
            }
            if (statusMap.containsKey("ACTIVE_NO")){
                whereMap.put("DISBURSEMENT_ID", statusMap.get("ACTIVE_NO"));
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }else if(currField.equals("DEPOSIT_CUSTOMER")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectDepositCustListForLTD");
            HashMap whereMap = new HashMap();
//            Date currDt = currDt.clone();
            Date sancDt = DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue());
            currDt.setDate(sancDt.getDate());
            currDt.setMonth(sancDt.getMonth());
            currDt.setYear(sancDt.getYear());
            whereMap.put("CURR_DT",currDt);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
    }
    private void tdtToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToFocusLost
        // Add your handling code here:
        tdtToFocusLost();
    }//GEN-LAST:event_tdtToFocusLost
    private void tdtToFocusLost(){
        // The to date should not be later than the sanction to date for the facility
        //        if (!tdtTo.getDateValue().equals(tdtTDate.getDateValue())){
        //            // To check whether this To date is fall within Facility details To date
        //            ClientUtil.validateFromDate(tdtTo, tdtTDate.getDateValue());
        //        }
        //        // To check whether this To date is fall within Facility details From date
        //        ClientUtil.validateToDate(tdtTo, tdtFDate.getDateValue());
        //        // Check for the last interest payment date
        //        if (tblInterMaintenance.getRowCount() > 0){
        //            if (updateInterest && tblInterMaintenance.getSelectedRow() > 0){
        //                ClientUtil.validateToDate(tdtTo, observableInt.checkLastInterestDate(tblInterMaintenance.getSelectedRow()));
        //            }else if (!updateInterest){
        //                ClientUtil.validateToDate(tdtTo, observableInt.checkLastInterestDate(tblInterMaintenance.getRowCount()));
        //            }
        //        }
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtTo, tdtFrom.getDateValue());
        //         if(cboIntGetFrom.getSelectedItem().equals("Account")){
        //             tdtTo.setDateValue("");
        //         }
    }
    private void tdtFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromFocusLost
        // Add your handling code here:
        tdtFromFocusLost();
    }//GEN-LAST:event_tdtFromFocusLost
    private void tdtFromFocusLost(){
        // The from date should not be earlier than the sanction from date for the facility
        // To check whether this From date is fall within Facility details To date
        //        ClientUtil.validateFromDate(tdtFrom, tdtTDate.getDateValue());
        // To check whether this From date is fall within Facility details From date
        //        ClientUtil.validateToDate(tdtFrom, tdtFDate.getDateValue());
        // Check for the last interest payment date
        //        if ((!tdtFrom.getDateValue().equals(tdtFDate.getDateValue())) && (tblInterMaintenance.getSelectedRow() == 0 || tblInterMaintenance.getRowCount() == 0)){
        //            ClientUtil.validateFromDate(tdtFrom, tdtFDate.getDateValue());
        //        }
        //        if (tblInterMaintenance.getRowCount() > 0){
        //            if (updateInterest && tblInterMaintenance.getSelectedRow() > 0){
        //                ClientUtil.validateToDate(tdtFrom, observableInt.checkLastInterestDate(tblInterMaintenance.getSelectedRow()));
        //            }else if (!updateInterest){
        //                ClientUtil.validateToDate(tdtFrom, observableInt.checkLastInterestDate(tblInterMaintenance.getRowCount()));
        //            }
        //        }
        ClientUtil.validateFromDate(tdtFrom, tdtTo.getDateValue());
    }
    private void cboRepayTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepayTypeActionPerformed
        // Add your handling code here:
        if (evt.getModifiers()==16 && CommonUtil.convertObjToStr(cboRepayType.getSelectedItem()).length()>0) {
            cboRepayTypeActionPerformed();
        }
    }//GEN-LAST:event_cboRepayTypeActionPerformed
    private void cboRepayTypeActionPerformed(){
        System.out.println("cborepaytypeaction#####"+repayNewMode);
        if ((cboRepayType.getSelectedItem().equals("Lump Sum")) && (observableRepay.checkRepayLumpSumRecCount(tblRepaymentCTable.getRowCount(), repayNewMode))){
            observableRepay.setCboRepayType("");
            cboRepayType.setSelectedItem(observableRepay.getCboRepayType());
            observableRepay.setCboRepayFreq_Repayment("");
            cboRepayFreq_Repayment.setSelectedItem(observableRepay.getCboRepayFreq_Repayment());
            return;
        }else{
            observableRepay.setCboRepayType(CommonUtil.convertObjToStr(cboRepayType.getSelectedItem()));
        }
        observableRepay.setCboRepayFreq_Repayment(CommonUtil.convertObjToStr(cboRepayFreq_Repayment.getSelectedItem()));
        if (observableRepay.getCboRepayType().length() > 0){
            if (observableRepay.getCboRepayType().equals("Lump Sum")){
                // When the Repayment Type is Lump Sum the No of Installments
                // must be "1"
                txtNoInstall.setEditable(false);
                txtNoInstall.setText("1");
                // To set the First and Last installment date
                observableRepay.setFirstInstallDate();
                tdtFirstInstall.setEnabled(false);
                //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
                //                    cboRepayFreq_Repayment.setEnabled(true);
                //                }
                tdtFirstInstall.setDateValue(observableRepay.getTdtFirstInstall());
                tdtLastInstall.setDateValue(observableRepay.getTdtLastInstall());
                observableRepay.setCboRepayFreq_Repayment("Lump Sum");
                allowMultiRepay = false;
            }else if(observableRepay.getCboRepayType().equals("User Defined")){
                // When the Repayment type is User Defined the Repayment Frequency
                // must be User Defined
                observableRepay.setCboRepayFreq_Repayment("User Defined");
                cboRepayFreq_Repayment.setEnabled(false);
                //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
                //                    txtNoInstall.setEditable(true);
                //                    tdtFirstInstall.setEnabled(true);
                //                }
                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
                    allowMultiRepay = true;
                }
            }else if (observableRepay.getCboRepayType().equals("EMI") || observableRepay.getCboRepayType().equals("Uniform Principle EMI")){
                // When the Repayment type is EMI the Repayment Frequency
                // must be Monthly
                observableRepay.setCboRepayFreq_Repayment("Monthly");
                cboRepayFreq_Repayment.setEnabled(false);
                //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
                //                    txtNoInstall.setEditable(true);
                //                    tdtFirstInstall.setEnabled(true);
                //                }
                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
                    allowMultiRepay = true;
                }
            }else if(observableRepay.getCboRepayType().equals("EQI")){
                // When the Repayment type is EQI the Repayment Frequency
                // must be Quaterly
                observableRepay.setCboRepayFreq_Repayment("Quaterly");
                cboRepayFreq_Repayment.setEnabled(false);
                //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
                //                    txtNoInstall.setEditable(true);
                //                    tdtFirstInstall.setEnabled(true);
                //                }
                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
                    allowMultiRepay = true;
                }
            }else if(observableRepay.getCboRepayType().equals("EHI")){
                // When the Repayment type is EHI the Repayment Frequency
                // must be Half Yearly
                observableRepay.setCboRepayFreq_Repayment("Half Yearly");
                cboRepayFreq_Repayment.setEnabled(false);
                //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
                //                    txtNoInstall.setEditable(true);
                //                    tdtFirstInstall.setEnabled(true);
                //                }
                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
                    allowMultiRepay = true;
                }
            }else if(observableRepay.getCboRepayType().equals("EYI")){
                // When the Repayment type is EYI the Repayment Frequency
                // must be Yearly
                observableRepay.setCboRepayFreq_Repayment("Yearly");
                cboRepayFreq_Repayment.setEnabled(false);
                //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
                //                    txtNoInstall.setEditable(true);
                //                    tdtFirstInstall.setEnabled(true);
                //                }
                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
                    allowMultiRepay = true;
                }
            }else{
                //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
                //                    tdtFirstInstall.setEnabled(true);
                //                    txtNoInstall.setEditable(true);
                //                    cboRepayFreq_Repayment.setEnabled(false);
                //                }
                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
                    allowMultiRepay = true;
                }
            }
        }
        //        cboRepayFreq_Repayment.setSelectedItem(observableRepay.getCboRepayFreq_Repayment());
        calculateRepayToDate();
    }
    private void chkGurantorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGurantorActionPerformed
        // Add your handling code here:
        chkGuarantorActionPerformed();
    }//GEN-LAST:event_chkGurantorActionPerformed
    private void chkGuarantorActionPerformed(){
        updateOBFields();
        if (chkGurantor.isSelected()){
            if (!(observable.getStrACNumber().equals(""))){
                rowGuarantor = -1;
                updateGuarantor = false;
                setGuarantorDetailsNewOnlyEnabled();
            }else{
                rowGuarantor = -1;
                updateGuarantor = false;
                deleteAllGuarantorDetails();
                observableGuarantor.resetGuarantorDetails();
                setAllGuarantorBtnsEnableDisable(false);
                setAllGuarantorDetailsEnableDisable(false);
            }
        }else{
            rowGuarantor = -1;
            updateGuarantor = false;
            deleteAllGuarantorDetails();
            observableGuarantor.resetGuarantorDetails();
            setAllGuarantorBtnsEnableDisable(false);
            setAllGuarantorDetailsEnableDisable(false);
        }
        observable.ttNotifyObservers();
    }
    
    private void deleteAllGuarantorDetails(){
        // To delete all the Guarantor Records when Guarantor Check Box is not selected
        for (int i = tblGuarantorTable.getRowCount() - 1;i >= 0;--i){
            observableGuarantor.deleteGuarantorTabRecord(i);
        }
    }
    
    private void deleteAllInterestDetails(){
        for (int i = tblInterMaintenance.getRowCount() - 1;i >= 0;--i){
            observableInt.deleteInterestTabRecord(i);
        }
    }
    private void chkInsuranceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkInsuranceActionPerformed
        // Add your handling code here:
        chkinsuranceActionPerformed();
    }//GEN-LAST:event_chkInsuranceActionPerformed
    private void chkinsuranceActionPerformed(){
        //        updateOBFields();
        //        if (chkInsurance.isSelected()){
        //            if (!(observable.getStrACNumber().equals(""))){
        //                // If the Account Number exist then enable Insurance Tab New Button
        //            }else{
        //
        //            }
        //        }else{
        //
        //        }
        //        observable.ttNotifyObservers();
    }
    private void rdoSecurityDetails_FullyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSecurityDetails_FullyActionPerformed
        // Add your handling code here:
        rdoFullySecuredActionPerformed();
    }//GEN-LAST:event_rdoSecurityDetails_FullyActionPerformed
    private void rdoFullySecuredActionPerformed(){
        if (rdoSecurityDetails_Fully.isSelected()){
            chkStockInspect.setEnabled(true);
            chkInsurance.setEnabled(true);
            String strFacilityType = getCboTypeOfFacilityKeyForSelected();
            if ((observable.getStrACNumber().length() > 0) && (!(strFacilityType.equals(LOANS_AGAINST_DEPOSITS)))){
                setSecurityBtnsOnlyNewEnable();
            }
            strFacilityType = null;
        }
    }
    private void rdoSecurityDetails_PartlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSecurityDetails_PartlyActionPerformed
        // Add your handling code here:
        rdoPartlySecuredActionPerformed();
    }//GEN-LAST:event_rdoSecurityDetails_PartlyActionPerformed
    private void rdoPartlySecuredActionPerformed(){
        if (rdoSecurityDetails_Partly.isSelected()){
            chkStockInspect.setEnabled(true);
            chkInsurance.setEnabled(true);
            String strFacilityType = getCboTypeOfFacilityKeyForSelected();
            if ((observable.getStrACNumber().length() > 0) && (!(strFacilityType.equals(LOANS_AGAINST_DEPOSITS)))){
                // If the account number is there then Enable New Button in Security Tab
                setSecurityBtnsOnlyNewEnable();
            }
            strFacilityType = null;
        }
    }
    private void rdoSecurityDetails_UnsecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSecurityDetails_UnsecActionPerformed
        // Add your handling code here:
        rdoUnsecuredActionPerformed();
    }//GEN-LAST:event_rdoSecurityDetails_UnsecActionPerformed
    private void rdoUnsecuredActionPerformed(){
        // If the Unsecured Button is selected
        // Enable and Disable the corresponding fields and Tabs
        updateOBFields();
        if (rdoSecurityDetails_Unsec.isSelected()){
            chkStockInspect.setEnabled(false);
            observable.setChkStockInspect(false);
            chkStockInspect.setSelected(observable.getChkStockInspect());
            chkInsurance.setEnabled(false);
            observable.setChkInsurance(false);
            chkInsurance.setSelected(observable.getChkInsurance());
            setAllSecurityDetailsEnableDisable(false);
            setAllSecurityBtnsEnableDisable(false);
            observableSecurity.resetSecurityDetails();
            if (!(observable.getStrACNumber().equals(""))){
                // To delete all the records in Security table
                // if Unsecured RadioButton is selected(When the account number is an existing one)
                deleteAllSecurityDetails();
                rowSecurity = -1;
                updateSecurity = false;
            }
        }
        observable.ttNotifyObservers();
    }
    
    private void deleteAllSecurityDetails(){
        for (int i = tblSecurityTable.getRowCount() - 1;i >= 0;--i){
            observableSecurity.deleteSecurityTabRecord(i);
        }
    }
    
    private void btnInterestMaintenanceDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestMaintenanceDeleteActionPerformed
        // Add your handling code here:
        btninterestDeletedActionPerformed();
    }//GEN-LAST:event_btnInterestMaintenanceDeleteActionPerformed
    private void btninterestDeletedActionPerformed(){
        updateOBFields();
        observableInt.deleteInterestTabRecord(rowInterest);
        setAllInterestDetailsEnableDisable(false);
        setInterestDetailsOnlyNewEnabled();
        rowInterest = -1;
        updateInterest = false;
        observableInt.resetInterestDetails();
        observable.ttNotifyObservers();
    }
    private void btnInterestMaintenanceSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestMaintenanceSaveActionPerformed
        // Add your handling code here:
        StringBuffer mandatoryMessage = new StringBuffer("");
        mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panTableFields));
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if(CommonUtil.convertObjToDouble(txtPenalInter.getText()).doubleValue() > CommonUtil.convertObjToDouble(txtInter.getText()).doubleValue()){
            mandatoryMessage.append(resourceBundle.getString("PENAL_ROI_WARNING")+"\n");
        }
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage.toString());
        }else{
            btninterestSaveActionPerformed();
        }
    }//GEN-LAST:event_btnInterestMaintenanceSaveActionPerformed
    private void btninterestSaveActionPerformed(){
        updateOBFields();
        
        if (observableInt.addInterestDetails(rowInterest, updateInterest) == 1){
            // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
        }else{
            // To reset the Fields
            setAllInterestDetailsEnableDisable(false);
            setInterestDetailsOnlyNewEnabled();
            observableInt.resetInterestDetails();
            updateInterest = false;
        }
        observable.ttNotifyObservers();
    }
    private void btnInterestMaintenanceNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestMaintenanceNewActionPerformed
        // Add your handling code here:
        btninterestNewActionPerformed();
    }//GEN-LAST:event_btnInterestMaintenanceNewActionPerformed
    private void btninterestNewActionPerformed(){
        updateOBFields();
        observableInt.resetInterestDetails();
        rowInterest = -1;
        updateInterest = false;
        setAllInterestDetailsEnableDisable(true);
        observableInt.setTxtPenalInter(observableInt.getPenalInter());
        if (observableInt.getEnableInterExpLimit()){
            txtInterExpLimit.setEnabled(true);
        }else{
            txtInterExpLimit.setEnabled(false);
            observableInt.setTxtInterExpLimit("");
        }
        setInterestDefaultValues();
        observable.ttNotifyObservers();
        setInterestDetailsOnlyDeleteDisabled();
    }
    private void setInterestDefaultValues(){
        //        if (tblInterMaintenance.getRowCount() > 0){
        //            tdtFrom.setEnabled(false);
        //            observableInt.populateFromDate();
        //        }
    }
    private void tblInterMaintenanceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInterMaintenanceMousePressed
        // Add your handling code here:
        tblinterestDetailsMousePressed();
    }//GEN-LAST:event_tblInterMaintenanceMousePressed
    private void tblinterestDetailsMousePressed(){
        if (tblInterMaintenance.getSelectedRow() >= 0){
            updateOBFields();
            // If the table is in editable mode
            setAllInterestDetailsEnableDisable(true);
            setAllInterestBtnsEnableDisable(true);
            observableInt.populateInterestDetails(tblInterMaintenance.getSelectedRow());
            if ((observable.getCbmIntGetFrom().getKeyForSelected().equals(PROD)) ||
            (observable.getCbmIntGetFrom().getKeyForSelected().equals("")) ||
            (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
            (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))
            || loanType.equals("LTD") || (observable.getCboAccStatus().equals("Closed"))){
                // If the interest is from Product level or nothing selected
                // If the record is populated for Delete or Authorization
                setAllInterestDetailsEnableDisable(false);
                setAllInterestBtnsEnableDisable(false);
                observable.ttNotifyObservers();
            }else{
                setAllInterestDetailsEnableDisable(true);
                if (observableInt.getEnableInterExpLimit()){
                    txtInterExpLimit.setEnabled(true);
                }else{
                    txtInterExpLimit.setEnabled(false);
                    observableInt.setTxtInterExpLimit("");
                }
                //                if (tblInterMaintenance.getSelectedRow() > 0){
                //                    tdtFrom.setEnabled(false);
                //                }
                //                if (!((observable.getCbmIntGetFrom().getKeyForSelected().equals(PROD)) || (observable.getCbmIntGetFrom().getKeyForSelected().equals(""))) && tblInterMaintenance.getSelectedRow() == (tblInterMaintenance.getRowCount()-1)){
                //                    tdtTo.setEnabled(true);
                //                }else{
                //                    tdtTo.setEnabled(false);
                //                }
                updateInterest = true;
                observable.ttNotifyObservers();
                setAllInterestBtnsEnableDisable(true);
            }
            rowInterest = tblInterMaintenance.getSelectedRow();
        }
    }
    private void tblGuarantorTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGuarantorTableMousePressed
        // Add your handling code here:
        tblguarantorTableMousePressed();
    }//GEN-LAST:event_tblGuarantorTableMousePressed
    // Actions have to be taken when a record from Guarantor Details have been chosen
    private void tblguarantorTableMousePressed(){
        
        if (tblGuarantorTable.getSelectedRow() >= 0){
            // If the table is in editable mode
            btnCustomerID_GD.setEnabled(false);
            btnAccNo.setEnabled(false);
            setAllGuarantorDetailsEnableDisable(true);
            setAllGuarantorBtnsEnableDisable(true);
            observableGuarantor.populateGuarantorDetails(tblGuarantorTable.getSelectedRow());
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))){
                // If the record is populated for Delete or Authorization
                setAllGuarantorDetailsEnableDisable(false);
                setAllGuarantorBtnsEnableDisable(false);
            }else{
                setAllGuarantorDetailsEnableDisable(true);
                setAllGuarantorBtnsEnableDisable(true);
                updateGuarantor = true;
            }
            rowGuarantor = tblGuarantorTable.getSelectedRow();
        }
    }
    private void btnGuarantorDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuarantorDeleteActionPerformed
        // Add your handling code here:
        btnguarantorDeleteActionPerformed();
    }//GEN-LAST:event_btnGuarantorDeleteActionPerformed
    private void btnguarantorDeleteActionPerformed(){
        updateOBFields();
        observableGuarantor.deleteGuarantorTabRecord(rowGuarantor);
        setAllGuarantorDetailsEnableDisable(false);
        setGuarantorDetailsNewOnlyEnabled();
        rowGuarantor = -1;
        updateGuarantor = false;
        observableGuarantor.resetGuarantorDetails();
        observable.ttNotifyObservers();
    }
    private void btnGuarantorSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuarantorSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panGuarantor);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panGuaranAddr);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0){
            displayAlert(mandatoryMessage1+mandatoryMessage2);
        }else{
            guarantorSaveBtnPressed();
            updateGuarantor = false;
        }
    }//GEN-LAST:event_btnGuarantorSaveActionPerformed
    // Actions have to be taken when Guarantor Details Save button pressed
    private void guarantorSaveBtnPressed(){
        updateOBFields();
        if (observableGuarantor.addGuarantorDetails(rowGuarantor, updateGuarantor) == 1){
            // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
        }else{
            // To reset the Fields
            setAllGuarantorDetailsEnableDisable(false);
            setGuarantorDetailsNewOnlyEnabled();
            observableGuarantor.resetGuarantorDetails();
            btnCustomerID_GD.setEnabled(false);
            btnAccNo.setEnabled(false);
        }
        observable.ttNotifyObservers();
    }
    private void btnGuarantorNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuarantorNewActionPerformed
        // Add your handling code here:
        guarantorNewBtnPressed();
    }//GEN-LAST:event_btnGuarantorNewActionPerformed
    private void guarantorNewBtnPressed(){
        updateOBFields();
        observableGuarantor.resetGuarantorDetails();
        btnCustomerID_GD.setEnabled(true);
        btnAccNo.setEnabled(false);
        rowGuarantor = -1;
        updateGuarantor = false;
        setAllGuarantorDetailsEnableDisable(true);
        setGuarantorDetailsDeleteOnlyDisabled();
        observable.ttNotifyObservers();
    }
    private void btnCustomerID_GDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerID_GDActionPerformed
        // Add your handling code here:
        popUp("Guarant_Cust_Id");
    }//GEN-LAST:event_btnCustomerID_GDActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        authEnableDisable();
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        authEnableDisable();
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        authEnableDisable();
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authEnableDisable(){
        setAllBorrowerBtnsEnableDisable(false);
        setbtnCustEnableDisable(false);
        setAllTablesEnableDisable(true);
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons Authorized Signatory
        setAllSanctionFacilityEnableDisable(false);
        
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllRepaymentDetailsEnableDisable(false);
        btnEMI_Calculate.setEnabled(false);
        //        setAllInsuranceDetailsEnableDisable(false);
        //        setAllInsuranceBtnsEnableDisable(false);
        setAllRepaymentBtnsEnableDisable(false);
        setAllGuarantorDetailsEnableDisable(false);
        setAllGuarantorBtnsEnableDisable(false);
        setAllDocumentDetailsEnableDisable(false);
        setDocumentToolBtnEnableDisable(false);
        setAllInterestDetailsEnableDisable(false);
        setAllInterestBtnsEnableDisable(false);
        setAllClassificationDetailsEnableDisable(false);
        txtCustID.setEnabled(false);
        //        txtCustomerID.setEnabled(false);
        txtSecurityNo.setEditable(false);
    }
    
    // Actions have to be taken when Authorize button is pressed
    private void authorizeActionPerformed(String authorizeStatus){
        tblSanctionDetails.setEnabled(false);
        if ((viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) && isFilled){
            // If a record is populated for authorize
            HashMap singleAuthorizeMap = new HashMap();
            java.util.ArrayList arrList = new java.util.ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("ACCT_NUM", observable.getStrACNumber());
            
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            //            ClientUtil.enableDisable(this, false);
            observable.setAuthorizeMap(null);
        }else{
            // If no record is populated for authorize
            HashMap mapParam = new HashMap();
            
            HashMap authorizeMapCondition = new HashMap();
            authorizeMapCondition.put("STATUS_BY", TrueTransactMain.USER_ID);
            authorizeMapCondition.put("BRANCH_ID", getSelectedBranchID());
            authorizeMapCondition.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, authorizeMapCondition);
            if (loanType.equals("LTD"))
                mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOListForLTD");
            else
                mapParam.put(CommonConstants.MAP_NAME, "getSelectBillsAuthorizeTOList");
            if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)){
                viewType = AUTHORIZE;
            }else if (authorizeStatus.equals(CommonConstants.STATUS_EXCEPTION)){
                viewType = EXCEPTION;
            }else if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)){
                viewType = REJECT;
            }
            authSignUI.setViewType(viewType);
            poaUI.setViewType(viewType);
            
            isFilled = false;
            tabLimitAmount.resetVisits();
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            setAuthBtnEnableDisable();
            
            authorizeMapCondition = null;
            //__ If there's no data to be Authorized, call Cancel action...
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        }
        
    }
    
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
    }
    public void authorize(HashMap map) {
        String strWarnMsg = tabLimitAmount.isAllTabsVisited();
        if (strWarnMsg.length() > 0){
            displayAlert(strWarnMsg);
            return;
        }
        strWarnMsg = null;
        tabLimitAmount.resetVisits();
        observable.setAuthorizeMap(map);
        observable.doAction(4);
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(lblBorrowerNo_2.getText());
            isFilled = false;
            btnCancelActionPerformed(null);
        }
        observable.setResultStatus();
    }
    
    private void tblSecurityTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSecurityTableMousePressed
        // Add your handling code here:
        if (tblSecurityTable.isEnabled()){
            tblsecurityTableMousePressed();
        }
    }//GEN-LAST:event_tblSecurityTableMousePressed
    // Actions have to be taken when a record from Security Details have been chosen
    private void tblsecurityTableMousePressed(){
        updateOBFields();
        if (tblSecurityTable.getSelectedRow() >= 0){
            // If the table is in editable mode
            setAllSecurityDetailsEnableDisable(true);
            setAllSecurityBtnsEnableDisable(true);
            observableSecurity.populateSecurityDetails(tblSecurityTable.getSelectedRow());
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))){
                // If the record is populated for Delete or Authorization
                setAllSecurityDetailsEnableDisable(false);
                setAllSecurityBtnsEnableDisable(false);
            }else{
                setAllSecurityDetailsEnableDisable(true);
                setAllSecurityBtnsEnableDisable(true);
            }
            updateSecurity = true;
            rowSecurity = tblSecurityTable.getSelectedRow();
        }
        observable.ttNotifyObservers();
    }
    private void btnSecurityDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityDeleteActionPerformed
        // Add your handling code here:
        btnsecurityDeleteActionPerformed();
    }//GEN-LAST:event_btnSecurityDeleteActionPerformed
    // Actions have to be taken when Security Details Delete button pressed
    private void btnsecurityDeleteActionPerformed(){
        updateOBFields();
        observableSecurity.deleteSecurityTabRecord(rowSecurity);
        setAllSecurityDetailsEnableDisable(false);
        setSecurityBtnsOnlyNewEnable();
        updateSecurity = false;
        rowSecurity = -1;
        observableSecurity.resetSecurityDetails();
        observable.ttNotifyObservers();
    }
    private void btnSecuritySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecuritySaveActionPerformed
        // Add your handling code here:
        btnsecuritySaveActionPerformed();
    }//GEN-LAST:event_btnSecuritySaveActionPerformed
    // Actions have to be taken when Security Details Save button pressed
    private void btnsecuritySaveActionPerformed(){
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panSecDetails);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panTotalSecurity_Value);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0){
            displayAlert(mandatoryMessage1 + mandatoryMessage2);
        }else{
            updateOBFields();
            if (observableSecurity.addSecurityDetails(rowSecurity, updateSecurity) == 1){
                // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
            }else{
                // To reset the Fields
                setAllSecurityDetailsEnableDisable(false);
                setSecurityBtnsOnlyNewEnable();
                updateSecurity = false;
                rowSecurity = -1;
                observableSecurity.resetSecurityDetails();
            }
            observable.ttNotifyObservers();
        }
    }
    private void btnSecurityNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityNewActionPerformed
        // Add your handling code here:
        btnsecurityNewActionPerformed();
    }//GEN-LAST:event_btnSecurityNewActionPerformed
    // Actions have to be taken when Security Details New button is pressed
    private void btnsecurityNewActionPerformed(){
        updateOBFields();
        updateSecurity = false;
        observableSecurity.resetSecurityDetails();
        setAllSecurityDetailsEnableDisable(true);
        setSecurityBtnsOnlyNewSaveEnable();
        setSecurityDefaultValWhenNewBtnPressed();
        rowSecurity = -1;
        observable.ttNotifyObservers();
    }
    
    private void setSecurityDefaultValWhenNewBtnPressed(){
        observableSecurity.setTdtFromDate(tdtFDate.getDateValue());
        tdtFromDate.setDateValue(observableSecurity.getTdtFromDate());
        observableSecurity.setTdtToDate(tdtTDate.getDateValue());
        tdtToDate.setDateValue(observableSecurity.getTdtToDate());
    }
    
    // Actions have to be taken when a record of Facility Details is selected in Facility Table(Sanction Details)
    private void sanctionFacilityTabPressed(){
        int selRow=-1;
        if (loanType.equals("LTD"))
            selRow = 0;
        else if(sanfacTab)
            selRow=rowfactab;
        else{
            selRow = tblSanctionDetails.getSelectedRow();
            rowfactab=selRow;
        }
        sanfacTab=false;
        if (selRow >= 0){
            // If the the table is in editable mode
            updateSecurity = false;
            updateRepayment = false;
            updateGuarantor = false;
            updateInterest = false;
            updateDocument = false;
            if (loanType.equals("OTHERS") && !viewType.equals(CommonConstants.STATUS_AUTHORIZED)) {
                observableSecurity.resetAllSecurityDetails();
                observableSecurity.resetSecurityTableUtil();
                observableOtherDetails.resetOtherDetailsFields();
                observableRepay.resetAllRepayment();
                observableRepay.resetRepaymentCTable();
                observableGuarantor.resetGuarantorDetails();
                observableGuarantor.resetGuarantorCTable();
                observableDocument.resetAllDocumentDetails();
                observableDocument.resetDocCTable();
                observableInt.resetAllInterestDetails();
                observableClassi.resetClassificationDetails();
                observable.ttNotifyObservers();
            }
            observable.populateFacilityDetails(rowSanctionMain, selRow);
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || (observable.getCboAccStatus().equals("Closed")))){
                // If the record is populated for Delete and Authorize
                setAllFacilityDetailsEnableDisable(false);
                setFacilityBtnsEnableDisable(false);
                //                setAllInsuranceBtnsEnableDisable(false);
                setAllGuarantorBtnsEnableDisable(false);
                setDocumentToolBtnEnableDisable(false);
            }else{
                // If the record is populated for Edit mode
                setAllFacilityDetailsEnableDisable(true);
                setFacilityBtnsEnableDisable(true);
            }
            displayTabsByAccountNumber();
            rowFacilityTabFacility = selRow;
        }
        observable.setFacilityProdID(selRow);
        observable.ttNotifyObservers();
        String strFacilityType = getCboTypeOfFacilityKeyForSelected();
        if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
            changesInUIForLoanAgainstDeposit();
            if (observable.getStrACNumber().length() > 0){
                if (loanType.equals("LTD")) {
                    if (facilitySaved && btnNewPressed) {
                        populateInterestRateForLTD();
                        btnNewPressed = false;
                    }
                }
            }
            strFacilityType = null;
            if (loanType.equals("LTD")) {
                rdoInterest_Simple.setEnabled(false);
                rdoInterest_Compound.setEnabled(false);
                //            if (observable.isLienChanged())
                //                tabLimitAmount.setSelectedIndex(5);
                //            else
                //                tabLimitAmount.setSelectedIndex(4);
            }
        }
    }
    
    private void populateInterestRateForLTD() {
        if (tblInterMaintenance.getRowCount()<1) {
            HashMap whereMap = new HashMap();
            whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
            whereMap.put("PROD_ID", observable.getLblProductID_FD_Disp());
            whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
            whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));
            deleteAllInterestDetails();
            observableInt.resetInterestDetails();
            updateInterestDetails();
            // Populate the values
            ArrayList interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductBillsInterestTO", whereMap);
            observableInt.setIsNew(true);
            if (interestList!=null && interestList.size()>0) {
                observableInt.setTermLoanInterestTO(interestList, null);
                cboIntGetFrom.setSelectedItem("Account");
            }
            else {
                displayAlert("Interest rates not created for this product...");
                cboIntGetFrom.setSelectedItem("");
            }
            if (tblRepaymentCTable.getRowCount()<1) {
                btnRepayment_NewActionPerformed();
                btnEMI_CalculateActionPerformed();
                btnRepayment_SaveActionPerformed();
            }
            observableInt.setIsNew(false);
        } else if (tblRepaymentCTable.getRowCount()>0) {
            HashMap where = new HashMap();
            where.put("ACT_NO", lblAccNo_RS_2.getText());
            int installmentCount = CommonUtil.convertObjToInt(ClientUtil.executeQuery("getCountOfInstallments",where).get(0));
            if (installmentCount<=0) {
                sanMousePress=true;
                tblRepaymentCTableMousePressed();
                btnEMI_CalculateActionPerformed();
                btnRepayment_SaveActionPerformed();
            }
            where = null;
        }
    }
    
    // Security Details, Repayment Schedule will be populated on the basis of Account Number
    private void displayTabsByAccountNumber(){
        final HashMap hash = new HashMap();
        updateOBFields();
        if (observable.getStrACNumber().length() > 0){
            // Retrieve the values on the basis of Account Number
            hash.put("WHERE", observable.getStrACNumber());
            hash.put("KEY_VALUE", "ACCOUNT_NUMBER");
            
            observable.populateData(hash, authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
            
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)|| (observable.getCboAccStatus().equals("Closed")))){
                // If the record is populated for Delete and Authorize
                btnsDisableBasedOnAccountNumber();
            }else{
                String strFacilityType = getCboTypeOfFacilityKeyForSelected();
                setAllSecurityDetailsEnableDisable(false);
                if (!strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
                    setSecurityBtnsOnlyNewEnable();
                }else{
                    setAllSecurityBtnsEnableDisable(false);
                }
                strFacilityType = null;
                enableDisableGetIntFrom(true);
                setAllInterestDetailsEnableDisable(false);
                setAllRepaymentDetailsEnableDisable(false);
                setRepaymentNewOnlyEnable();
                setAllGuarantorDetailsEnableDisable(false);
                setGuarantorDetailsNewOnlyEnabled();
                setAllDocumentDetailsEnableDisable(false);
                setDocumentToolBtnEnableDisable(false);
                String intGetFrom = CommonUtil.convertObjToStr(observable.getCbmIntGetFrom().getKeyForSelected());
                if ((intGetFrom.equals(PROD)) || (intGetFrom.equals(""))){
                    setAllInterestBtnsEnableDisable(false);
                }else{
                    setInterestDetailsOnlyNewEnabled();
                }
                if(observable.getCboAccStatus().equals("Closed"))
                    setAllClassificationDetailsEnableDisable(false);
                else
                    setAllClassificationDetailsEnableDisable(true);
                ClientUtil.enableDisable(panAccountDetails, true);
                disableLastIntApplDate();
                if (rdoSecurityDetails_Fully.isSelected()){
                    rdoSecurityDetails_FullyActionPerformed(null);
                }else if (rdoSecurityDetails_Partly.isSelected()){
                    rdoSecurityDetails_PartlyActionPerformed(null);
                }else if (rdoSecurityDetails_Unsec.isSelected()){
                    rdoSecurityDetails_UnsecActionPerformed(null);
                }
                
                if (chkGurantor.isSelected()){
                    chkGurantorActionPerformed(null);
                }else{
                    setAllGuarantorBtnsEnableDisable(false);
                }
                
                if (chkInsurance.isSelected()){
                    chkInsuranceActionPerformed(null);
                }else{
                    //                    setAllInsuranceBtnsEnableDisable(false);
                    //                    setAllInsuranceDetailsEnableDisable(false);
                }
                setSanctionProductDetailsDisable();
            }
            observableInt.setValByProdID();
        }else{
            btnsDisableBasedOnAccountNumber();
            setDefaultValB4AcctCreation();
            observableClassi.populateClassiDetailsFromProd();
            observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
            observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
            updateProdClassiFields();
            ClientUtil.enableDisable(panAccountDetails, false);
        }
        // This will populate the customer details in account level tabs
        populateCustomerProdLeveFields();
        observable.ttNotifyObservers();
    }
    
    private void populateCustomerProdLeveFields(){
        // This will populate Group description field
        observable.populateCustomerProdLeveFields();
    }
    
    private void enableDisableGetIntFrom(boolean val){
        cboIntGetFrom.setEnabled(val);
    }
    private void setSanctionProductDetailsDisable(){
        cboTypeOfFacility.setEnabled(false);
        cboProductId.setEnabled(false);
    }
    
    private void setDefaultValB4AcctCreation(){
        observable.setDefaultValB4AcctCreation();
        tdtDemandPromNoteDate.setDateValue(observable.getTdtDemandPromNoteDate());
        calculateDPNExpDate();
        observableClassi.setDefaultValB4AcctCreation();
        observableOtherDetails.populateProdLevelValB4AcctCreation(CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
    }
    
    private void btnsDisableBasedOnAccountNumber(){
        enableDisableGetIntFrom(false);
        tdtAODDate.setEnabled(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllSecurityBtnsEnableDisable(false);
        setAllRepaymentDetailsEnableDisable(false);
        btnEMI_Calculate.setEnabled(false);
        //        setAllInsuranceDetailsEnableDisable(false);
        //        setAllInsuranceBtnsEnableDisable(false);
        setAllRepaymentBtnsEnableDisable(false);
        setAllGuarantorDetailsEnableDisable(false);
        setAllGuarantorBtnsEnableDisable(false);
        setAllDocumentDetailsEnableDisable(false);
        setDocumentToolBtnEnableDisable(false);
        setAllInterestDetailsEnableDisable(false);
        setAllInterestBtnsEnableDisable(false);
        setAllClassificationDetailsEnableDisable(false);
    }
    
    private void resetTabsDependsOnAccountNumber(){
        observableSecurity.resetAllSecurityDetails();
        observableSecurity.resetSecurityTableUtil();
        observableOtherDetails.resetOtherDetailsFields();
        observableRepay.resetAllRepayment();
        observableRepay.resetRepaymentCTable();
        observableGuarantor.resetAllGuarantorDetails();
        observableGuarantor.resetGuarantorCTable();
        observableDocument.resetAllDocumentDetails();
        observableDocument.resetDocCTable();
        observableInt.resetAllInterestDetails();
        observableInt.resetInterestCTable();
        observableClassi.resetClassificationDetails();
    }
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        cboProductIDActionPerformed();
    }//GEN-LAST:event_cboProductIdActionPerformed
    private void cboProductIDActionPerformed(){
        observable.setCboProductId(CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
        if (observable.getCboProductId().length() > 0){
            observable.setFacilityAcctHead();
            if (observable.getStrACNumber().equals("")){
                if (tblBorrowerTabCTable.getRowCount() > 0){
                    observable.setFacilityContactDetails(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0,1)));
                    txtContactPerson.setText(observable.getTxtContactPerson());
                    txtContactPhone.setText(observable.getTxtContactPhone());
                }
                observableClassi.populateClassiDetailsFromProd();
                updateProdClassiFields();
                observableOtherDetails.populateProdLevelValB4AcctCreation(CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
                updateOtherDetailsTab();
                //FOR CHECK SHARE HOLDER OR NOT
                checkShareHolder();
            }else{
                observableInt.setValByProdID();
            }
        }else{
            observable.setLblAccHead_2("");
            updateRdoSubsidyAndInterestNature();
        }
        lblAccHead_2.setText(observable.getLblAccHead_2());
        updateAccHead_ProdID();
    }
    private void checkShareHolder(){
        String selectKey=CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        HashMap shareMap=new HashMap();
        shareMap.put("PROD_ID",selectKey);
        String sharetype="NOMINAL";
        List shareList=ClientUtil.executeQuery("getSelectShareProductLoanAcct", shareMap);
        if(shareList !=null && shareList.size()>0) {
            //            if(CommonUtil.convertObjToStr(((ComboBoxModel)cboConstitution.getModel()).getKeyForSelected()).equals("JOINT_ACCOUNT") && tblBorrowerTabCTable.getRowCount()>0)
            
            shareMap.put("CUST_ID",txtCustID.getText());
            List share= ClientUtil.executeQuery("getShareAccInfoDataForLoan",shareMap);
            if(share !=null && share.size()>0){
                shareMap=(HashMap)share.get(0);
                String notElegibleLoan=CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_LOAN"));
                if(notElegibleLoan !=null && notElegibleLoan.equals("Y") && shareMap.get("NOT_ELIGIBLE_DT")!=null ){
                    Date eligibal_dt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_DT")));
                    if(DateUtil.dateDiff(eligibal_dt,(Date) currDt.clone())<0)
                        ClientUtil.showMessageWindow(" Eligible Date is Not Expiry");
                    return;
                    
                    
                }else if(shareMap.get("SHARE_TYPE")!=null && sharetype.equals(CommonUtil.convertObjToStr(shareMap.get("SHARE_TYPE")))){
                    ClientUtil.showMessageWindow("Share Type is NOMINAL");
                    return;
                }
                //            ClientUtil.showMessageWindow("This account Holder Not Having Share");
            }
        }
    }
    private void updateProdClassiFields(){
        removeFacilitySecurityRadioBtns();
        rdoSecurityDetails_Fully.setSelected(observable.getRdoSecurityDetails_Fully());
        rdoSecurityDetails_Partly.setSelected(observable.getRdoSecurityDetails_Partly());
        rdoSecurityDetails_Unsec.setSelected(observable.getRdoSecurityDetails_Unsec());
        addFacilitySecurityRadioBtns();
        
        cboCommodityCode.setSelectedItem(observableClassi.getCboCommodityCode());
        cboGuaranteeCoverCode.setSelectedItem(observableClassi.getCboGuaranteeCoverCode());
        cboSectorCode1.setSelectedItem(observableClassi.getCboSectorCode1());
        cboHealthCode.setSelectedItem(observableClassi.getCboHealthCode());
        cboTypeFacility.setSelectedItem(observableClassi.getCboTypeFacility());
        cboPurposeCode.setSelectedItem(observableClassi.getCboPurposeCode());
        cboIndusCode.setSelectedItem(observableClassi.getCboIndusCode());
        cboWeakerSectionCode.setSelectedItem(observableClassi.getCboWeakerSectionCode());
        cbo20Code.setSelectedItem(observableClassi.getCbo20Code());
        cboRefinancingInsti.setSelectedItem(observableClassi.getCboRefinancingInsti());
        cboGovtSchemeCode.setSelectedItem(observableClassi.getCboGovtSchemeCode());
        cboAssetCode.setSelectedItem(observableClassi.getCboAssetCode());
        chkDirectFinance.setSelected(observableClassi.getChkDirectFinance());
        chkECGC.setSelected(observableClassi.getChkECGC());
        chkPrioritySector.setSelected(observableClassi.getChkPrioritySector());
        chkQIS.setSelected(observableClassi.getChkQIS());
    }
    
    private void updateAccHead_ProdID(){
        lblProdID_Disp_DocumentDetails.setText(observableDocument.getLblProdID_Disp_DocumentDetails());
        lblProdID_Disp_ODetails.setText(observableOtherDetails.getLblProdID_Disp_ODetails());
        lblProductID_FD_Disp.setText(observable.getLblProductID_FD_Disp());
        lblAcctHead_Disp_DocumentDetails.setText(observableDocument.getLblAcctHead_Disp_DocumentDetails());
        lblAcctHead_Disp_ODetails.setText(observableOtherDetails.getLblAcctHead_Disp_ODetails());
        lblAccountHead_FD_Disp.setText(observable.getLblAccountHead_FD_Disp());
        lblAccHeadSec_2.setText(observableSecurity.getLblAccHeadSec_2());
        lblAccHead_RS_2.setText(observableRepay.getLblAccHead_RS_2());
        lblAccHead_GD_2.setText(observableGuarantor.getLblAccHead_GD_2());
        lblAccHead_IM_2.setText(observableInt.getLblAccHead_IM_2());
        lblAccHead_CD_2.setText(observableClassi.getLblAccHead_CD_2());
        lblProdId_Disp.setText(observableSecurity.getLblProdId_Disp());
        lblProdID_RS_Disp.setText(observableRepay.getLblProdID_RS_Disp());
        lblProdID_GD_Disp.setText(observableGuarantor.getLblProdID_GD_Disp());
        lblProdID_IM_Disp.setText(observableInt.getLblProdID_IM_Disp());
        lblProID_CD_Disp.setText(observableClassi.getLblProdID_CD_Disp());
    }
    
    private void updateOtherDetailsTab(){
        chkChequeBookAD.setSelected(observableOtherDetails.getChkChequeBookAD());
        chkCustGrpLimitValidationAD.setSelected(observableOtherDetails.getChkCustGrpLimitValidationAD());
        chkMobileBankingAD.setSelected(observableOtherDetails.getChkMobileBankingAD());
        chkNROStatusAD.setSelected(observableOtherDetails.getChkNROStatusAD());
        chkATMAD.setSelected(observableOtherDetails.getChkATMAD());
        txtATMNoAD.setText(observableOtherDetails.getTxtATMNoAD());
        tdtATMFromDateAD.setDateValue(observableOtherDetails.getTdtATMFromDateAD());
        tdtATMToDateAD.setDateValue(observableOtherDetails.getTdtATMToDateAD());
        chkDebitAD.setSelected(observableOtherDetails.getChkDebitAD());
        txtDebitNoAD.setText(observableOtherDetails.getTxtDebitNoAD());
        tdtDebitFromDateAD.setDateValue(observableOtherDetails.getTdtDebitFromDateAD());
        tdtDebitToDateAD.setDateValue(observableOtherDetails.getTdtDebitToDateAD());
        chkCreditAD.setSelected(observableOtherDetails.getChkCreditAD());
        txtCreditNoAD.setText(observableOtherDetails.getTxtCreditNoAD());
        tdtCreditFromDateAD.setDateValue(observableOtherDetails.getTdtCreditFromDateAD());
        tdtCreditToDateAD.setDateValue(observableOtherDetails.getTdtCreditToDateAD());
        cboSettlementModeAI.setSelectedItem(observableOtherDetails.getCboSettlementModeAI());
        cboOpModeAI.setSelectedItem(observableOtherDetails.getCboOpModeAI());
        txtAccOpeningChrgAD.setText(observableOtherDetails.getTxtAccOpeningChrgAD());
        txtMisServiceChrgAD.setText(observableOtherDetails.getTxtMisServiceChrgAD());
        chkStopPmtChrgAD.setSelected(observableOtherDetails.getChkStopPmtChrgAD());
        txtChequeBookChrgAD.setText(observableOtherDetails.getTxtChequeBookChrgAD());
        chkChequeRetChrgAD.setSelected(observableOtherDetails.getChkChequeRetChrgAD());
        txtFolioChrgAD.setText(observableOtherDetails.getTxtFolioChrgAD());
        chkInopChrgAD.setSelected(observableOtherDetails.getChkInopChrgAD());
        txtAccCloseChrgAD.setText(observableOtherDetails.getTxtAccCloseChrgAD());
        chkStmtChrgAD.setSelected(observableOtherDetails.getChkStmtChrgAD());
        cboStmtFreqAD.setSelectedItem(observableOtherDetails.getCboStmtFreqAD());
        chkNonMainMinBalChrgAD.setSelected(observableOtherDetails.getChkNonMainMinBalChrgAD());
        txtExcessWithChrgAD.setText(observableOtherDetails.getTxtExcessWithChrgAD());
        chkABBChrgAD.setSelected(observableOtherDetails.getChkABBChrgAD());
        chkNPAChrgAD.setSelected(observableOtherDetails.getChkNPAChrgAD());
        txtABBChrgAD.setText(observableOtherDetails.getTxtABBChrgAD());
        tdtNPAChrgAD.setDateValue(observableOtherDetails.getTdtNPAChrgAD());
        txtMinActBalanceAD.setText(observableOtherDetails.getTxtMinActBalanceAD());
        tdtDebit.setDateValue(observableOtherDetails.getTdtDebit());
        tdtCredit.setDateValue(observableOtherDetails.getTdtCredit());
        chkPayIntOnCrBalIN.setSelected(observableOtherDetails.getChkPayIntOnCrBalIN());
        chkPayIntOnDrBalIN.setSelected(observableOtherDetails.getChkPayIntOnDrBalIN());
        lblRateCodeValueIN.setText(observableOtherDetails.getLblRateCodeValueIN());
        lblCrInterestRateValueIN.setText(observableOtherDetails.getLblCrInterestRateValueIN());
        lblDrInterestRateValueIN.setText(observableOtherDetails.getLblDrInterestRateValueIN());
        lblPenalInterestValueIN.setText(observableOtherDetails.getLblPenalInterestValueIN());
        lblAgClearingValueIN.setText(observableOtherDetails.getLblAgClearingValueIN());
    }
    private void btnFacilityDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacilityDeleteActionPerformed
        // Add your handling code here:
        btnFacilityDeletePressed();
    }//GEN-LAST:event_btnFacilityDeleteActionPerformed
    private void btnFacilityDeletePressed(){
        if (allCTablesNotNull()){
            observable.deleteFacilityRecord(rowFacilityTabSanction, rowFacilityTabFacility);
            deleteAllSecurityDetails();
            deleteAllGuarantorDetails();
            //            deleteAllInsuranceDetails();
            deleteAllInterestDetails();
            observable.resetAllFacilityDetails();
            updateCboTypeOfFacility();
            observable.ttNotifyObservers();
            updateOBFields();
            observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_DELETE);
            observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_DELETE);
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            observable.doAction(3);
            authSignUI.setLblStatus(observable.getLblStatus());
            poaUI.setLblStatus(observable.getLblStatus());
            resetTabsDependsOnAccountNumber();
            observable.setResultStatus();
            rowFacilityTabSanction = tblSanctionDetails2.getSelectedRow();
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
            updateRdoSubsidyAndInterestNature();
        }
    }
    private void btnFacilitySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacilitySaveActionPerformed
        // Add your handling code here:
        btnFacilitySavePressed();
    }//GEN-LAST:event_btnFacilitySaveActionPerformed
    private boolean btnFacilitySavePressed(){
        //changeplace to 12###@
        facilitySaved = false;
        facilityFlag=false;
        sanction=true;
        sandetail=true;
        santab=true;
        sanfacTab=true;
        System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
        boolean isWarnMsgExist = false;
        if (rowSanctionMain == -1){
            displayAlert(resourceBundle.getString("existenceSancDetailsTableWarning"));
            isWarnMsgExist = true;
            return isWarnMsgExist;
        }
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDAccount);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        final String mandatoryMessage3 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDDate);
        /* mandatoryMessage3 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        
        String mandatoryMessage4 = "";
        String mandatoryMessage5 = "";
        String mandatoryMessage6 = isJointAcctHavingAtleastOneCust();
        if (productBasedValidation()){
            mandatoryMessage5 = new TermLoanMRB().getString("rdoMultiDisburseAllow_Yes");
        }
        
        String mandatoryMessage7 = "";
        String mandatoryMessage8 = "";
        if (observable.getStrACNumber().length() > 0){
            //            if (loanType.equals("LTD")) {
            //            if (facilitySaved || observable.getActionType()!=ClientConstants.ACTIONTYPE_CANCEL) {
            //                if (tblInterMaintenance.getRowCount()<1) {
            //                    cboIntGetFrom.setSelectedItem(((ComboBoxModel)cboIntGetFrom.getModel()).getDataForKey("PROD"));
            //                    observable.setCboIntGetFrom(CommonUtil.convertObjToStr(cboIntGetFrom.getSelectedItem()));
            //                    java.util.LinkedHashMap intFromProd = observableInt.getInterestAll();
            //                    cboIntGetFrom.setSelectedItem(((ComboBoxModel)cboIntGetFrom.getModel()).getDataForKey("ACT"));
            //                    btnInterestMaintenanceNewActionPerformed(null);
            //                    if(intFromProd.size()!=0) {
            //                        HashMap actIntDet = new HashMap();
            //                        actIntDet = (HashMap)intFromProd.get("1");
            //                        tdtFrom.setDateValue(CommonUtil.convertObjToStr(actIntDet.get("FROM_DATE")));
            //                        tdtTo.setDateValue(CommonUtil.convertObjToStr(actIntDet.get("TO_DATE")));
            //                        txtFromAmt.setText(CommonUtil.convertObjToStr(actIntDet.get("FROM_AMOUNT")));
            //                        txtToAmt.setText(CommonUtil.convertObjToStr(actIntDet.get("TO_AMOUNT")));
            //                        txtInter.setText(CommonUtil.convertObjToStr(actIntDet.get("INTEREST")));
            //                        txtPenalInter.setText(CommonUtil.convertObjToStr(actIntDet.get("PENALTY_INTEREST")));
            //                        txtAgainstClearingInter.setText(CommonUtil.convertObjToStr(actIntDet.get("AGAINST_CLEAR_INT")));
            //                        txtPenalStatement.setText(CommonUtil.convertObjToStr(actIntDet.get("PENAL_STATEMENT")));
            //                        txtInterExpLimit.setText(CommonUtil.convertObjToStr(actIntDet.get("INTER_EXP_LIMIT")));
            //                    }
            //                    btnInterestMaintenanceSaveActionPerformed(null);
            //                    if (tblRepaymentCTable.getRowCount()<1) {
            //                        btnRepayment_NewActionPerformed();
            //                        btnRepayment_SaveActionPerformed();
            //                    }
            //                }
            //            }
            //            }
            mandatoryMessage4 = new MandatoryCheck().checkMandatory(getClass().getName(), panProd_IM);
            mandatoryMessage7 = isInterestDetailsExistForThisAcct();
            mandatoryMessage8 = validateOtherDetailsMandatoryFields();
        }
        
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0 || mandatoryMessage4.length() > 0 || mandatoryMessage5.length() > 0 || mandatoryMessage6.length() > 0 || mandatoryMessage7.length() > 0 || mandatoryMessage8.length() > 0){
            displayAlert(mandatoryMessage1+mandatoryMessage2+mandatoryMessage3+mandatoryMessage4+mandatoryMessage5+mandatoryMessage6+mandatoryMessage7+mandatoryMessage8);
            isWarnMsgExist = true;
        }else{
            
            //change from line 12#### top check
            System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
            if (isTablesInEditMode(false) && allCTablesNotNull() && checkForSecurityValue() && repayTableNotNull()){
                updateOBFields();
                if(loanType.equals("LTD") )//|| loanType.equals("OTHERS"))
                    rowFacilityTabSanction=0; //BY ABI FOR ONE MORE LOAN SANCTION NOT UPDATE
                else
                    rowFacilityTabSanction=rowsan;
                observable.addFacilityDetails(rowFacilityTabSanction, rowFacilityTabFacility);
                updateOBFields();
                observable.doAction(2);
                //                if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED && loanType.equals("LTD"))
                //                    btnFacilitySave.setEnabled(true);
                //                else
                //                    btnFacilitySave.setEnabled(false);
                
                facilitySaved = true;
                authSignUI.setLblStatus(observable.getLblStatus());
                poaUI.setLblStatus(observable.getLblStatus());
                observable.resetSanctionFacility();
                resetTabsDependsOnAccountNumber();
                observable.resetAllFacilityDetails();
                updateCboTypeOfFacility();
                observable.setBorrowerNumber();
                observable.setResultStatus();
                setAllSanctionFacilityEnableDisable(false);
                setSanctionFacilityBtnsEnableDisable(false);
                btnNew1.setEnabled(true);
                setAllFacilityDetailsEnableDisable(false);
                setFacilityBtnsEnableDisable(false);
                btnsDisableBasedOnAccountNumber();
                rowFacilityTabSanction = tblSanctionDetails2.getSelectedRow();
                observable.ttNotifyObservers();
                updateRdoSubsidyAndInterestNature();
                observable.setStrACNumber(observable.getLoanACNo());
                lblAcctNo_Sanction_Disp.setText(observable.getLoanACNo());
                lblAcctNo_Sanction_Disp.setText(observable.getStrACNumber());
                //                if(loanType.equals("OTHERS") ){    //ltd and testing for same abi
                btnSave2_SDActionPerformed(null);
                tblSanctionDetails2MousePressed(null);
                tblSanctionDetailsMousePressed(null);
                System.out.println("####%%%% observable.getCbmProductId().getSelectedItem() "+observable.getCbmProductId().getSelectedItem());
                System.out.println("####%%%% cboProductId.getSelectedItem() "+cboProductId.getSelectedItem());
                //                    cboProductId.setSelectedItem(observable.getCbmProductId().getSelectedItem());
                //                    cboProductIdActionPerformed(null);
                //                }
//                if (observable.isLienChanged()) {
//                    tblSanctionDetails2MousePressed(null);
//                }
                
            }else{
                isWarnMsgExist = true;
            }
        }
        facilityFlag=true;
        return isWarnMsgExist;
    }
    
    private String validateOtherDetailsMandatoryFields(){
        StringBuffer stbWarnMsg = new StringBuffer("");
        String strSelectedProdType = getCboTypeOfFacilityKeyForSelected();
        
        if ((strSelectedProdType.equals("OD") || strSelectedProdType.equals("CC") || strSelectedProdType.equals("BILLS"))){
            TermLoanMRB objMandatoryRB = new TermLoanMRB();
            if (CommonUtil.convertObjToStr(cboOpModeAI.getSelectedItem()).length() <= 0){
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objMandatoryRB.getString("cboOpModeAI"));
            }
            if (chkATMAD.isSelected()){
                if (txtATMNoAD.getText().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("txtATMNoAD"));
                }
                if (tdtATMFromDateAD.getDateValue().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("tdtATMFromDateAD"));
                }
                if (tdtATMToDateAD.getDateValue().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("tdtATMToDateAD"));
                }
            }
            if (chkDebitAD.isSelected()){
                if (txtDebitNoAD.getText().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("txtDebitNoAD"));
                }
                if (tdtDebitFromDateAD.getDateValue().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("tdtDebitFromDateAD"));
                }
                if (tdtDebitToDateAD.getDateValue().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("tdtDebitToDateAD"));
                }
            }
            if (chkCreditAD.isSelected()){
                if (txtCreditNoAD.getText().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("txtCreditNoAD"));
                }
                if (tdtCreditFromDateAD.getDateValue().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("tdtCreditFromDateAD"));
                }
                if (tdtCreditToDateAD.getDateValue().length() <= 0){
                    stbWarnMsg.append("\n");
                    stbWarnMsg.append(objMandatoryRB.getString("tdtCreditToDateAD"));
                }
            }
            if (chkABBChrgAD.isSelected() && (txtABBChrgAD.getText().length() <= 0 || CommonUtil.convertObjToInt(txtABBChrgAD.getText()) == 0)){
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objMandatoryRB.getString("txtABBChrgAD"));
            }
            if (chkNonMainMinBalChrgAD.isSelected() && (txtMinActBalanceAD.getText().length() <= 0 || CommonUtil.convertObjToInt(txtMinActBalanceAD.getText()) == 0)){
                stbWarnMsg.append("\n");
                stbWarnMsg.append(objMandatoryRB.getString("txtMinActBalanceAD"));
            }
            objMandatoryRB = null;
        }
        
        return stbWarnMsg.toString();
    }
    
    private String isInterestDetailsExistForThisAcct(){
        StringBuffer stbWarnMsg = new StringBuffer("");
        String strSelectedProdType = getCboTypeOfFacilityKeyForSelected();
        if (!(strSelectedProdType.equals("OD") || strSelectedProdType.equals("CC") || strSelectedProdType.equals("CC")) && tblInterMaintenance.getRowCount() < 1){
            stbWarnMsg.append("\n");
            stbWarnMsg.append(resourceBundle.getString("interestDetailsWarning"));
        }
        return stbWarnMsg.toString();
    }
    
    private void updateRdoSubsidyAndInterestNature(){
        updateRdoSubsidy();
        updateRdoInterestNature();
    }
    private void updateRdoSubsidy(){
        observable.resetFacilityTabSubsidy();
        removeFacilitySubsidy();
        rdoSubsidy_Yes.setSelected(observable.getRdoSubsidy_Yes());
        rdoSubsidy_No.setSelected(observable.getRdoSubsidy_No());
        addFacilitySubsidyRadioBtns();
    }
    
    private void updateRdoInterestNature(){
        observable.resetFacilityTabInterestNature();
        removeFacilityInterestNature();
        rdoNatureInterest_PLR.setSelected(observable.getRdoNatureInterest_PLR());
        rdoNatureInterest_NonPLR.setSelected(observable.getRdoNatureInterest_NonPLR());
        addFacilityInterestNatureBtns();
    }
    
    private void sanctionMainTabPressed(){
        int selRow=-1;
        if (loanType.equals("LTD"))
            selRow = 0;
        else if(santab)
            selRow=rowmaintab;
        else{
            selRow = tblSanctionDetails2.getSelectedRow();
            rowmaintab=selRow;
        }
        santab=false;
        if (selRow >= 0){
            updateOBFields();
            observable.populateFacilityTabSanction(selRow, rowSanctionMain);
            rowFacilityTabSanction = selRow;
            if (!(viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
                observable.resetAllFacilityDetails();
                updateCboTypeOfFacility();
                observableSecurity.resetSecurityTableUtil();
                observableSecurity.resetAllSecurityDetails();
                observableOtherDetails.resetOtherDetailsFields();
                observableRepay.resetAllRepayment();
                observableRepay.resetRepaymentCTable();
                observableGuarantor.resetGuarantorCTable();
                observableGuarantor.resetAllGuarantorDetails();
                observableDocument.resetAllDocumentDetails();
                observableDocument.resetDocCTable();
                observableInt.resetInterestCTable();
                observableInt.resetAllInterestDetails();
                observableClassi.resetClassificationDetails();
            }
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
            observable.ttNotifyObservers();
        }
    }
    private void tblSanctionDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanctionDetailsMousePressed
        // Add your handling code here:
        if (!(viewType.equals(AUTHORIZE) || viewType.equals("Delete")))
            tblSanctionDetailsPopulate();
    }//GEN-LAST:event_tblSanctionDetailsMousePressed
    
    private void tblSanctionDetails2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanctionDetails2MousePressed
        // Add your handling code here:
        tblSanctionDetails2Populate();
    }//GEN-LAST:event_tblSanctionDetails2MousePressed
    private void updateCboTypeOfFacility(){
        cboTypeOfFacility.setSelectedItem(observable.getCboTypeOfFacility());
    }
    private void tblSanctionDetailsPopulate(){
        sanMousePress=false;
        // Actions have to be taken when a record of Facility Details is selected in Sanction Details Tab
        //        if ((viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Edit")))
        if (tblSanctionDetails.getSelectedRow() >= 0 || loanType.equals("LTD") || sandetail){
            allowMultiRepay = true;
            observableRepay.setAllowMultiRepay(true);
            updateOBFields();
            if(! loanType.equals("LTD") && (viewType.equals(AUTHORIZE)||viewType.equals("Delete")))
                observable.resetSanctionFacility();
            // If Facility Details is in Edit Mode
            //if ((tblSanctionDetails2.getSelectedRow() == tblFacilityDetails.getSelectedRow()) && (tblSanctionDetails.getSelectedRow() == tblFacilityDetails2.getSelectedRow())){
            if (viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) ||  (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW)){
                setSanctionFacilityBtnsEnableDisable(false);
                setAllSanctionFacilityEnableDisable(false);
//                observable.populateSanctionFacility(rowSanctionFacility);//vinay
//                updateCboTypeOfFacility();//vinay
                //                observable.sanctionFacilityEditWarning();
            }else{
                if (loanType.equals("LTD")) {
                    rowSanctionFacility = 0;
                    sanDetailMousePressedForLTD = true;
                }
                else if(sandetail)
                    rowSanctionFacility=rowsanDetail;
                else  {
                    rowSanctionFacility = tblSanctionDetails.getSelectedRow();
                    rowsanDetail=rowSanctionFacility;
                    sanMousePress=true;
                }
                
                if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
                    setSanctionFacilityBtnsEnableDisable(false);
                    setAllSanctionFacilityEnableDisable(false);
                }else{
                    updateSanctionFacility = true;
                    setSanctionFacilityBtnsEnableDisable(true);
                    setAllSanctionFacilityEnableDisable(true);
                }
                //                observable.populateSanctionFacility(tblSanctionDetails.getSelectedRow());
                observable.populateSanctionFacility(rowSanctionFacility);
                updateCboTypeOfFacility();
                cboRepayFreq.setSelectedItem(observable.getCboRepayFreq());
                sandetail=false;
            }
            observable.ttNotifyObservers();
            sanctionFacilityTabPressed();
            allowMultiRepay = observableRepay.getAllowMultiRepay();
            HashMap hash2=new HashMap();
            hash2.put("PROD_DESC",observable.getCboProductId());
            List behaveslike=ClientUtil.executeQuery("getLoanBehaves", hash2);
            if(behaveslike !=null && behaveslike.size()>0){
                hash2=(HashMap)behaveslike.get(0);
            }
            if(hash2.containsKey("BEHAVES_LIKE")&& (!CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("OD")) && (!CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("BILLS")) && lblAcctNo_Sanction_Disp.getText() !=null && lblAcctNo_Sanction_Disp.getText().length()>0){
                hash2.put("ACT_NUM", lblAcctNo_Sanction_Disp.getText());
                List lst=ClientUtil.executeQuery("checkTransaction", hash2);
                hash2=(HashMap)lst.get(0);
                int count=0;
                count=CommonUtil.convertObjToInt(hash2.get("CNT"));
                System.out.print("hash###2"+hash2);
                if(count !=0) {
                    txtLimit_SD.setEnabled(false);
                    
                    txtNoInstallments.setEnabled(false);
                    cboRepayFreq.setEnabled(false);
                    tdtFDate.setEnabled(false);
                    chkMoratorium_Given.setEnabled(false);
                }else {
                    txtLimit_SD.setEnabled(true);
                    txtNoInstallments.setEnabled(true);
                    cboRepayFreq.setEnabled(true);
                    tdtFDate.setEnabled(true);
                    chkMoratorium_Given.setEnabled(true);
                }
            }
            //renewal od
            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getTdtTDate()),DateUtil.getDateWithoutMinitues((Date) currDt.clone()))>=0)
                if(accNumMap.containsKey(lblAcctNo_Sanction_Disp.getText()) && sanMousePress){
                    if(tblRepaymentCTable.getRowCount()>0){
                        tblRepaymentCTableMousePressed(null);
                        btnRepayment_DeleteActionPerformed(null);
                        ClientUtil.displayAlert("Change the sanction Details so Create new Repayment schdule");
                        tabLimitAmount.setSelectedIndex(8);
                        
                    }
                }
            if (observable.getStrACNumber().length()>0) {
                btnFacilitySave.setVisible(false);
            } else {
                btnFacilitySave.setVisible(true);
            }
        }
        if(loanType.equals("LTD")) {
            cboTypeOfFacility.setEnabled(false);
        }
        tdtTDate.setEnabled(false);
    }
    
    private void tblSanctionDetails2Populate(){
        // Actions have to be taken when a record of Sanction Details is selected in Sanction Details Tab
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        int selRow=-1;
        if (loanType.equals("LTD")) {
            selRow = 0;
            //            ClientUtil.enableDisable(panTable_SD,false);
        }
        else if(sanction)
            selRow=rowsan;
        
        else{
            selRow = tblSanctionDetails2.getSelectedRow();
            rowsan=selRow;
        }
        sanction=false;
        if (selRow >= 0){
            updateOBFields();
            updateSanctionMain = true;
            observable.populateSanctionMain(selRow);
            rowSanctionMain = selRow;
            if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))){
                observable.resetSanctionFacility();
            }
            updateCboTypeOfFacility();
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))){
                setAllSanctionMainEnableDisable(false);
                setSanctionFacilityBtnsEnableDisable(false);
                setAllSanctionFacilityEnableDisable(false);
            }else{
                setSanctionMainBtnsEnableDisable(true);
                if (tblSanctionDetails.getRowCount() > 0){
                    btnDelete2_SD.setEnabled(false);
                }
                setAllSanctionMainEnableDisable(true);
                setSanctionFacilityBtnsEnableDisable(false);
                setAllSanctionFacilityEnableDisable(false);
                btnNew1.setEnabled(true);
                btnNew2_SD.setEnabled(true);
            }
            observable.ttNotifyObservers();
            sanctionMainTabPressed();
        }
        //        if (loanType.equals("LTD")) {
        //            tblSanctionDetailsMousePressed(null);
        //        }
    }
    
    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // Add your handling code here:
        btnDelete1Action();
    }//GEN-LAST:event_btnDelete1ActionPerformed
    private void btnDelete1Action(){
        // Facility Details CTable(Sanction Details Tab) Delete pressed
        observable.setLoanACNo(lblAcctNo_Sanction_Disp.getText());
        int mainSlno = rowSanctionMain;
        int slno = CommonUtil.convertObjToInt(((ArrayList)((EnhancedTableModel)tblSanctionDetails.getModel()).getDataArrayList().get(rowSanctionFacility)).get(0));
        int rows=observable.deleteSanctionFacility(rowSanctionFacility, updateSanctionMain, rowSanctionMain, rowFacilityTabSanction, rowFacilityTabFacility);
        if (rows != -1) {
            observable.deleteFacilityRecord(mainSlno, slno);
            observable.resetSanctionFacility();
            observable.resetAllFacilityDetails();
            updateCboTypeOfFacility();
            resetTabsDependsOnAccountNumber();
            setSanctionFacilityBtnsEnableDisable(false);
            setAllSanctionFacilityEnableDisable(false);
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
            btnNew1.setEnabled(true);
            observable.ttNotifyObservers();
            //            if (tblSanctionDetails.getRowCount() < 1){
            //                btnDelete2_SD.setEnabled(true);
            //            }
            
            
            rowSanctionFacility = -1;
            updateSanctionFacility = false;
        }
        //        else if(rows==0){
        //            btnFacilityDeletePressed();
        //        }
    }
    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        // Add your handling code here:
        String mandatoryMessage="";
        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTableFields_SD);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (chkMoratorium_Given.isSelected() && (txtFacility_Moratorium_Period.getText().length() == 0)){
            TermLoanRB termLoanRB = new TermLoanRB();
            mandatoryMessage = mandatoryMessage + termLoanRB.getString("moratorium_Given_Warning");
            termLoanRB = null;
        }
        //for renew   od
        txtLimit_SDFocusLostOD();
        //
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            //check repay detail delete or not
            if(!((observable.getTxtLimit_SD()).equals(txtLimit_SD.getText())) ||
            !((observable.getTxtNoInstallments()).equals(txtNoInstallments.getText())) ||
            !(((String)cboRepayFreq.getSelectedItem()).equals(observable.getCboRepayFreq() )) ||
            !((observable.getTdtFDate()).equals(tdtFDate.getDateValue()))){
                
                
                accNumMap.put(lblAcctNo_Sanction_Disp.getText(),lblAcctNo_Sanction_Disp.getText());
            }
            //end checking
            boolean periodFlag = false;
            boolean limitFlag = false;
            String message = new String();
            if (loanType.equals("OTHERS")) {
                if (!(cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(txtNoInstallments.getText(), txtFacility_Moratorium_Period.getText())){
                    observable.decoratePeriod();
                    message = message.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
                    periodFlag = false;
                }else if ((cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()))){
                    observable.decoratePeriod();
                    message = message.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
                    periodFlag = false;
                }else{
                    periodFlag = true;
                }
            }else{
                periodFlag = true;
            }
            if (loanType.equals("OTHERS")) {
                if (!observable.checkLimitValue(txtLimit_SD.getText())) {
                    observable.setTxtLimit_SD("");
                    txtLimit_SD.setText(observable.getTxtLimit_SD());
                    message = message.concat("\nThe Limit value must fall within "+observable.getMinLimitValue().toString()+" and  "+observable.getMaxLimitValue().toString());
                    limitFlag = false;
                }else{
                    limitFlag = true;
                }
            }else{
                limitFlag = true;
            }
            if (periodFlag && limitFlag){
                btnSave1Action();
            }else{
                displayAlert(message);
            }
            message = null;
        }
    }//GEN-LAST:event_btnSave1ActionPerformed
    private void btnSave1Action(){
        updateOBFields();
        if ((observable.addSanctionFacilityTab(rowSanctionFacility, updateSanctionFacility, updateSanctionMain, rowSanctionMain, rowFacilityTabSanction)) == 1){
            setAllSanctionFacilityEnableDisable(true);
        }else{
            // It will update the database tables based on the Account Number
            if (observable.getStrACNumber().length() > 0){
                if (!btnFacilitySavePressed()){
                    resetEnableDisableFieldsBasedOnSave1Action();
                }
            }else{
                resetEnableDisableFieldsBasedOnSave1Action();
            }
        }
        observable.ttNotifyObservers();
        //        if (loanType.equals("LTD")) btnSave2_SDActionPerformed(null); for testig by abi
    }
    private void resetEnableDisableFieldsBasedOnSave1Action(){
        observable.resetSanctionFacility();
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        btnNew1.setEnabled(true);
        btnSave2_SD.setEnabled(true);
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        observableSecurity.resetAllSecurityDetails();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        resetTabsDependsOnAccountNumber();
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
    }
    private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
        // Add your handling code here:
        btnNew1Action();
    }//GEN-LAST:event_btnNew1ActionPerformed
    private void btnNew1Action(){
        // Facility Details CTable(Sanction Details Tab) New pressed
        if (loanType.equals("LTD")) {
            
            if (tblSanctionDetails.getRowCount()>=1) {
                ClientUtil.showMessageWindow("More than one loan not allowed for a single borrower...");
                return;
            }
            
        }
        updateOBFields();
        observable.resetSanctionFacility();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        resetTabsDependsOnAccountNumber();
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        observable.ttNotifyObservers();
        //        tdtFDate.setDateValue(DateUtil.getStringDate(DateUtil.addDays(currDt.clone(), 30)));
        //        tdtFDate.setDateValue(DateUtil.getStringDate(currDt.clone()));tdtSanctionDate for mahila sandate assend as from date
        tdtFDate.setDateValue(tdtSanctionDate.getDateValue());
        observable.setTdtFDate(CommonUtil.convertObjToStr(tdtFDate.getDateValue()));
        setSanctionFacilityBtnsEnableDisable(true);
        setAllSanctionFacilityEnableDisable(true);
        btnDelete1.setEnabled(false);
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        if (loanType.equals("LTD")) {
            cboTypeOfFacility.setSelectedItem(
            ((ComboBoxModel) cboTypeOfFacility.getModel()).getDataForKey(LOANS_AGAINST_DEPOSITS));
            
            cboTypeOfFacilityActionPerformed();
            cboTypeOfFacility.setEnabled(false);
            callView("DEPOSIT_CUSTOMER");
        }
    }
    private void btnDelete2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete2_SDActionPerformed
        // Add your handling code here:
        btnDelete2_SDAction();
    }//GEN-LAST:event_btnDelete2_SDActionPerformed
    private void btnDelete2_SDAction(){
        // Sanction Details CTable(Sanction Details Tab) Delete pressed
        observable.deleteSanctionMain(rowSanctionMain);
        observable.resetSanctionMain();
        observable.resetSanctionFacilityTable();
        observable.resetSanctionFacility();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        resetTabsDependsOnAccountNumber();
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        setSanctionMainBtnsEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        btnNew2_SD.setEnabled(true);
        rowSanctionMain = -1;
        observable.ttNotifyObservers();
    }
    private void btnSave2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave2_SDActionPerformed
        // Add your handling code here:
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Mode);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if ((mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0) || (facilityFlag && updateSanctionFacility )) {
            if(updateSanctionFacility  && facilityFlag)
                displayAlert(resourceBundle.getString("santionFacilityEditWarning"));
            
            else
                displayAlert(mandatoryMessage1+mandatoryMessage2);
            
        }else{
            rowSanctionFacility = -1;
            updateSanctionFacility = false;
            if (observable.checkSanctionNoDublication(txtSanctionNo.getText())){
                btnSave2_SDAction();
            }
        }
        //        if (loanType.equals("LTD")) {
        //            tblSanctionDetails2MousePressed(null);
        //        }
    }//GEN-LAST:event_btnSave2_SDActionPerformed
    private void btnSave2_SDAction(){
        // Sanction Details CTable(Sanction Details Tab) Save pressed
        updateOBFields();
        if (tblSanctionDetails.getRowCount() > 0){
            if (updateSanctionMain == false){
                observable.setSanctionNumber();
            }
            observable.addSanctionMainTab(rowSanctionMain,  updateSanctionMain);
            setSanctionFacilityBtnsEnableDisable(false);
            setAllSanctionFacilityEnableDisable(false);
            setSanctionMainBtnsEnableDisable(false);
            setAllSanctionMainEnableDisable(false);
            observable.resetSanctionFacility();
            observable.resetSanctionMain();
            observable.resetSanctionFacilityTable();
            observable.resetAllFacilityDetails();
            updateCboTypeOfFacility();
            resetTabsDependsOnAccountNumber();
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
            btnNew2_SD.setEnabled(true);
        }else{
            observable.setSanctionTableWarningMessage();
        }
        observable.ttNotifyObservers();
    }
    private void btnNew2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew2_SDActionPerformed
        // Add your handling code here:
        btnNew2_SDAction();
    }//GEN-LAST:event_btnNew2_SDActionPerformed
    private void btnNew2_SDAction(){
        // Sanction Details CTable(Sanction Details Tab) New pressed
        updateOBFields();
        observable.resetSanctionFacility();
        updateCboTypeOfFacility();
        observable.createSanctionMainRowObjects();
        observable.resetSanctionMain();
        observable.resetAllFacilityDetails();
        observable.setStrRealSanctionNo("");
        observable.createTableUtilSanctionFacility();
        resetTabsDependsOnAccountNumber();
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        setSanctionMainBtnsEnableDisable(true);  //false changed as true by Rajesh
        setAllSanctionMainEnableDisable(true);
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        btnNew1.setEnabled(true);
        btnNew2_SD.setEnabled(true);
        btnDelete2_SD.setEnabled(false);         // This line added by Rajesh
        rowSanctionMain = -1;
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        updateSanctionMain = false;
        observable.setTdtSanctionDate(DateUtil.getStringDate((Date) currDt.clone()));
        observable.ttNotifyObservers();
        observable.destroyCreateSanctionFacilityObjects();
    }
    
    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // Add your handling code here:
        callView("CUSTOMER ID");
    }//GEN-LAST:event_btnCustIDActionPerformed
    private void validateConstitutionCustID(){
        String strConstitution = CommonUtil.convertObjToStr(cboConstitution.getSelectedItem());
        // If the constitution is blank then allow to select the customer
        if (strConstitution.length() != 0 && tblBorrowerTabCTable.getRowCount() > 0){
            observableBorrow.validateConstitutionCustID(strConstitution, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
            cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void removeObservable(){
        observable.deleteObserver(this);
        observableBorrow.deleteObserver(this);
        observableComp.deleteObserver(this);
        observableSecurity.deleteObserver(this);
        observableRepay.deleteObserver(this);
        observableGuarantor.deleteObserver(this);
        observableInt.deleteObserver(this);
        observableClassi.deleteObserver(this);
    }
    
    private void destroyOBObjects(){
        observableBorrow = null;
        observableClassi = null;
        observableComp = null;
        observableGuarantor = null;
        observableInt = null;
        observable = null;
        observableRepay = null;
        observableSecurity = null;
    }
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        btncancelActionPerformed();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetAllExtendedTab(){
        authSignUI.resetAllFieldsInAuthTab();
        poaUI.getPowerOfAttorneyOB().resetAllFieldsInPoA();
    }
    
    private void btncancelActionPerformed(){
        //        if( viewType.equals("Edit") ||  viewType.equals("Delete") ){
        //        if(observable.getAuthorizeStatus() !=null ) //commented for checking edit lock by using borrow no but it shoude be edit lock by loan no  dont delete
        super.removeEditLock(lblBorrowerNo_2.getText());
        //        }
        setFocusFirstTab();
        observable.resetForm();
        resetAllExtendedTab();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        observable.destroyObjects();
        observable.createObject();
        setMode(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);// Disables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
        authSignUI.setLblStatus(observable.getLblStatus());
        poaUI.setLblStatus(observable.getLblStatus());
        setButtonEnableDisable();
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for all the CTable
        setbtnCustEnableDisable(false);
        setAllTablesEnableDisable(true);
        btnsDisableBasedOnAccountNumber();
        setModified(false);
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        viewType = "";
        date=null;
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        btndeleteActionPerformed();
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btndeleteActionPerformed(){
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for all the CTable
        popUp("Delete");
        authSignUI.setAuthTabBtnEnableDisable(false);
        setAllTablesEnableDisable(true); // To disable the All tables...
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
    }
    private void setAllTablesEnableDisable(boolean val){
        tblSanctionDetails.setEnabled(val);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        btneditActionPerformed();
    }//GEN-LAST:event_btnEditActionPerformed
    private void btneditActionPerformed(){
        observableBorrow.btnPressed = true;
        observable.createObject();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        authSignUI.setAuthEnableDisable(false);
        authSignUI.setAuthOnlyNewBtnEnable();
        authSignUI.setAllAuthInstEnableDisable(false);
        authSignUI.setAuthInstOnlyNewBtnEnable();
        poaUI.setAllPoAEnableDisable(false);
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        setSanctionMainBtnsEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        setAllTablesEnableDisable(true);
        btnNew2_SD.setEnabled(true);
        updateModeAuthorize = false;
        updateModePoA = false;
        updateSanctionFacility = false;
        updateSecurity = false;
        updateRepayment = false;
        updateGuarantor = false;
        updateInterest = false;
        updateDocument = false;
        rowSanctionFacility = -1;
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        //         if (!( observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ))//&&
        //        if(loanType.equals("OTHERS"))
        //        if(tblInterMaintenance.getRowCount() > 0 && tblRepaymentCTable.getRowCount()>0){
        //            btnSave1ActionPerformed(null);
        //            btnSave2_SDActionPerformed(null);
        //        }
        if (viewType.equals("Delete"))
            saveAction();
        else if (observable.getFacilitySize()>0 && observable.checkAllfacilityDetails())
            btnsaveActionPerformed();
        else
            ClientUtil.showMessageWindow("Enter the Facility Details");
        
    }//GEN-LAST:event_btnSaveActionPerformed
    private void btnsaveActionPerformed(){
        //observable.setAuthorizeNo();
        //        if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT && loanType.equals("LTD")) {
        //            btnFacilitySavePressed();
        //            saveAction();
        //        } else
        if (isTablesInEditMode(true) && allCTablesNotNull() && checkFieldsWhenMainSavePressed() && checkForSecurityValue()){
            if(tblInterMaintenance.getRowCount() > 0 && tblRepaymentCTable.getRowCount()>0){
                btnSave1ActionPerformed(null);
                btnSave2_SDActionPerformed(null);
            }
            saveAction();
        }
    }
    
    private void saveAction() {
        if (!viewType.equals("Delete"))
            updateOBFields();
        if (rowFacilityTabFacility > -1 && rowFacilityTabSanction > -1 && observable.getStrACNumber().length() > 0
        && !viewType.equals("Delete")){
            observable.addFacilityDetails(rowFacilityTabSanction, rowFacilityTabFacility);
        }
        observable.doAction(1);
        //        super.removeEditLock(lblBorrowerNo_2.getText()); remove edit lock by abi for authorize only remove the lock
        observable.resetForm();
        resetAllExtendedTab();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for the CTable
        setbtnCustEnableDisable(false);  // To disable the Customer Buttons
        observable.setResultStatus();
        authSignUI.setLblStatus(observable.getLblStatus());
        poaUI.setLblStatus(observable.getLblStatus());
        observable.destroyObjects();
        observable.createObject();
        observable.ttNotifyObservers();
        rowSanctionFacility = -1;
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            setModified(false);
        }
    }
    private boolean checkForSecurityValue(){
        System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
        boolean canSave = false;
        if (rdoSecurityDetails_Fully.isSelected() && observable.getStrACNumber().length() > 0){
            String strFacilityType = getCboTypeOfFacilityKeyForSelected();
            boolean isLimitNotTallied = false;
            if (!strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
                isLimitNotTallied = observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText());
            }else{
                isLimitNotTallied = observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText(), tblSecurityTable.getModel());
            }
            if (isLimitNotTallied == true){
                displayAlert(resourceBundle.getString("securityValueWarning"));
                canSave = false;
            }else{
                canSave = true;
            }
        }else{
            canSave = true;
        }
        return canSave;
    }
    
    private boolean checkFieldsWhenMainSavePressed(){
        if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)))){
            final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
            /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
            String mandatoryMessage2 = "";
            String mandatoryMessage3 = "";
            String mandatoryMessage5 = "";
            String mandatoryMessage6 = "";
            String mandatoryMessage7 = isJointAcctHavingAtleastOneCust();
            String mandatoryMessage8 = "";
            if (observable.getStrACNumber().length() > 0){
                mandatoryMessage3 = new MandatoryCheck().checkMandatory(getClass().getName(), panProd_IM);
                mandatoryMessage5 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDAccount);
                mandatoryMessage6 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDDate);
                if (loanType.equals("OTHERS"))
                    mandatoryMessage8 = isInterestDetailsExistForThisAcct();
            }
            if (rdoSecurityDetails_Fully.isSelected() && observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText())){
                mandatoryMessage2 = resourceBundle.getString("securityValueWarning");
            }
            final String mandatoryMessage4 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
            /* mandatoryMessage4 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
            if (loanType.equals("OTHERS"))
                if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0 || mandatoryMessage4.length() > 0 || mandatoryMessage5.length() > 0 || mandatoryMessage6.length() > 0 || mandatoryMessage7.length() > 0 || mandatoryMessage8.length() > 0){
                    displayAlert(mandatoryMessage1 + mandatoryMessage2 + mandatoryMessage3 + mandatoryMessage4 + mandatoryMessage5 + mandatoryMessage6+mandatoryMessage7+mandatoryMessage8);
                    return false;
                }
        }
        return true;
    }
    
    private boolean isTablesInEditMode(boolean isMainSave){
        if (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])){
            return true;
        }
        StringBuffer stbWarnMsg = new StringBuffer("");
        if (authSignUI.isUpdateModeAuthorize()){
            stbWarnMsg.append(resourceBundle.getString("authSignatoryEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (authSignUI.isUpdateModeAuthorizeInst()){
            stbWarnMsg.append(resourceBundle.getString("authSignatoryInstEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (poaUI.isUpdateModePoA()){
            stbWarnMsg.append(resourceBundle.getString("poaEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateRepayment){
            stbWarnMsg.append(resourceBundle.getString("repaymentEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (isMainSave && updateSanctionFacility){
            stbWarnMsg.append(resourceBundle.getString("santionFacilityEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateDocument){
            stbWarnMsg.append(resourceBundle.getString("documentDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateInterest){
            stbWarnMsg.append(resourceBundle.getString("interestDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateGuarantor){
            stbWarnMsg.append(resourceBundle.getString("guarantorDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateSecurity){
            stbWarnMsg.append(resourceBundle.getString("securityDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (stbWarnMsg.length() > 0){
            displayAlert(stbWarnMsg.toString());
            return false;
        }else{
            return true;
        }
    }
    private boolean repayTableNotNull(){
        String behaves=getCboTypeOfFacilityKeyForSelected();System.out.print("repaytable###"+behaves);
        if(behaves !=null && (!behaves.equals("OD")) && (!behaves.equals("BILLS")))
            if (!observable.getStrACNumber().equals("") &&  tblRepaymentCTable.getRowCount() < 1 ){ //&&  tblRepaymentCTable.getRowCount() < 1
                String strWarning =  resourceBundle.getString("repayfreqtable");
                displayAlert(strWarning);
                return false;
            }
        return true;
    }
    private boolean allCTablesNotNull(){
        String strWarning = "";
        if (tblSanctionDetails2.getRowCount() < 1){
            strWarning = strWarning + resourceBundle.getString("existenceSancDetailsTableWarning");
        }
        if (!observable.getStrACNumber().equals("") && chkGurantor.isSelected() && tblGuarantorTable.getRowCount() < 1){
            strWarning = strWarning + resourceBundle.getString("existenceGuarantorDetailsTableWarning");
        }
        //        if (!observable.getStrACNumber().equals("")&&  tblRepaymentCTable.getRowCount() < 1 ) //&&  tblRepaymentCTable.getRowCount() < 1
        //            strWarning = strWarning + resourceBundle.getString("repayfreqtable");
        strWarning = authSignUI.isHavingProperNoOfRecords(strWarning);
        if (!strWarning.equals("")){
            displayAlert(strWarning);
            strWarning = null;
            return false;
        }
        strWarning = null;
        return true;
    }
    private boolean productBasedValidation(){
        boolean isNotSelected = false;
        String strFacilityType = getCboTypeOfFacilityKeyForSelected();
        if (!(strFacilityType.equals("CC") || strFacilityType.equals("OD") || strFacilityType.equals("BILLS"))){
            if (!(rdoMultiDisburseAllow_No.isSelected() || rdoMultiDisburseAllow_Yes.isSelected())){
                isNotSelected = true;
            }
        }
        return isNotSelected;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        btnnewActionPerformed();
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnnewActionPerformed(){
        btnNewPressed = true;
        setFocusFirstTab();
        resetAllExtendedTab();
        observable.destroyObjects();
        observable.createObject();
        observable.resetForm();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        ClientUtil.enableDisable(this, true);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        authSignUI.setLblStatus(observable.getLblStatus());
        poaUI.setLblStatus(observable.getLblStatus());
        authSignUI.setAuthEnableDisable(false);
        authSignUI.setAuthOnlyNewBtnEnable();
        authSignUI.setAllAuthInstEnableDisable(false);
        authSignUI.setAuthInstOnlyNewBtnEnable();
        poaUI.setAllPoAEnableDisable(false);
        poaUI.setPoANewOnlyEnable();
        setButtonEnableDisable();
        setAllTablesEnableDisable(true);
        setbtnCustEnableDisable(false);
        btnCustID.setEnabled(true);
        newPressedEnableDisable(true);
        setCompanyDetailsEnableDisable(false);
        setBorrowerDetailsEnableDisable(true);
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        setSanctionMainBtnsEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        btnNew2_SD.setEnabled(true);
        updateModeAuthorize = false;
        updateModePoA = false;
        updateSanctionFacility = false;
        updateSecurity = false;
        updateGuarantor = false;
        updateDocument = false;
        updateInterest = false;
        rowSanctionFacility = -1;
        setModified(true);
    }
    
    public void repaymentFillData(java.util.LinkedHashMap data, double totRepayVal){
        observableRepay.populateEMICalculatedFields(data, totRepayVal);
    }
    
    // To display the All the Cust Id's which r having status as
    // created or updated, in a table...
    private void popUp(String field) {
        
        
        final HashMap viewMap = new HashMap();
        viewType = field;
        authSignUI.setViewType(viewType);
        poaUI.setViewType(viewType);
        //if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
        if(field.equals("Edit") || field.equals("Delete") || field.equals("Enquirystatus") ) {
            
            //            super.removeEditLock(lblBorrowerNo_2.getText()); remove only accour authorization by abi
            
            ArrayList lst = new ArrayList();
            lst.add("BORROWER NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            HashMap editMapCondition = new HashMap();
            editMapCondition.put("BRANCH_ID", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, editMapCondition);
            if (loanType.equals("LTD"))
                viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForLTD");
            else
                viewMap.put(CommonConstants.MAP_NAME, "viewBills");
            if(field.equals("Delete"))
                if (loanType.equals("LTD"))
                    viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForDeleteLTD");
                else
                    viewMap.put(CommonConstants.MAP_NAME, "viewBillsForDelete");
            //mapped statement: viewTermLoan---> result map should be a Hashmap in OB...
        }else if(field.equals("Borr_Cust_Id")){
            viewMap.put(CommonConstants.MAP_NAME, "getCustomers");
        }else if(field.equals("Guarant_Cust_Id")){
            viewMap.put(CommonConstants.MAP_NAME, "getCustomers");
        }else if(field.equals("GUARANTOR_ACCT_NO")){
            String strSelectedProdType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
            String strSelectedProdID = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdId.getModel()).getKeyForSelected());
            if (strSelectedProdType.length() <= 0 || strSelectedProdID.length() <= 0){
                //If the Product is not selected then return.
                return;
            }
            updateOBFields();
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getGuarantorAccountList"+strSelectedProdType);
            HashMap whereListMap = new HashMap();
            whereListMap.put("CUST_ID", txtCustomerID_GD.getText());
            whereListMap.put("PROD_ID", strSelectedProdID);
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        }
        new ViewAll(this, viewMap).show();
    }
    
    private void setFocusFirstTab(){
        tabLimitAmount.setSelectedIndex(0);
    }
    private void setFocusAcctLevelTab(){
        tabLimitAmount.setSelectedIndex(4);
    }
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("calling filldata#####"+hash);
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus")) {
                isFilled = true;
                if (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete")){
                    // To populate the tabs on the basis of KEY_VALUE->ACCT_NUM
                    // To reset the Customer details in the Borrower Tab
                    setFocusAcctLevelTab();
                    observableBorrow.resetBorrowerTabCTable();
                    observableBorrow.resetBorrowerTabCustomer();
                    updateBorrowerTabCustDetails();
                    observable.setStrACNumber(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
                    hash.put(CommonConstants.MAP_WHERE, observable.getStrACNumber());
                    observable.populateData(hash, authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
                    //                    tblShareMaintenance.setModel(observable.getTblShare());
                    cboTypeOfFacility.setSelectedItem(observable.getCboTypeOfFacility());
                    if (viewType.equals(AUTHORIZE)|| viewType.equals(REJECT)){
                        if (viewType.equals(AUTHORIZE))
                            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                        if ( viewType.equals(REJECT))
                            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
                        if (loanType.equals("LTD")){
                            if (hash.containsKey(CommonConstants.MAP_WHERE)) {
                                hash.remove(CommonConstants.MAP_WHERE);
                            }
                            boolean checkvalue=observable.setDetailsForLTD(hash);
                            HashMap map=new HashMap();
                            map=(HashMap)observable.getDepositCustDetMap();
                            System.out.println("mapvalue###"+map);
                            if (map.containsKey("LIENNO")) {
                                HashMap lienMap = new HashMap();
                                lienMap.put("CUSTOMER_NAME",map.get("NAME"));
                                lienMap.put("PRODID",map.get("PROD_ID"));
                                lienMap.put("DEPOSIT_ACT_NUM",map.get("DEPOSIT_NO"));
                                lienMap.put("SUBNO",CommonUtil.convertObjToInt(map.get("DEPOSIT_SUB_NO")));
                                lienMap.put("CUST_ID",map.get("CUST_ID"));
                                lienMap.put("AMOUNT",map.get("AMOUNT"));
                                lienMap.put("BALANCE",map.get("BALANCE"));
                                lienMap.put("STATUS",CommonConstants.STATUS_CREATED);
                                lienMap.put("LIENNO",map.get("LIENNO"));
                                System.out.println("lienMap#####"+lienMap);
                                DepositLienUI depLienUI = new DepositLienUI();
                                depLienUI.setViewType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                                if (viewType.equals(REJECT))
                                    depLienUI.setViewType(ClientConstants.ACTIONTYPE_REJECT);
                                depLienUI.fillData(lienMap);
                                com.see.truetransact.ui.TrueTransactMain.showScreen(depLienUI);
                                lienMap = null;
                            }
                            map = null;
                        }
                    }else if (viewType.equals(EXCEPTION)){
                        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                    }else if (viewType.equals(REJECT)){
                        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
                    }
                    observable.setStatus();
                    authSignUI.setLblStatus(observable.getLblStatus());
                    poaUI.setLblStatus(observable.getLblStatus());
                }else{
                    setFocusFirstTab();
                    hash.put(CommonConstants.MAP_WHERE, hash.get("BORROWER NO"));
                    // To populate the tabs on the basis of KEY_VALUE->BORROWER NO
                    hash.put("KEY_VALUE", "BORROWER_NUMBER");
                    observable.populateData(hash, authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
                }
                // To add the Borrower level Customer ID's in Authorized Signatory's
                // acctLevelCustomerList
                addCustIDNAuthSignatory();
                observable.ttNotifyObservers();
                if (viewType.equals("Delete") ||  (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
                    ClientUtil.enableDisable(this, false);
                }else{
                    ClientUtil.enableDisable(this, true);
                    btnCustID.setEnabled(true);
                    authSignUI.setAuthOnlyNewBtnEnable();
                    authSignUI.setAuthInstOnlyNewBtnEnable();
                    observable.setStatus();
                    authSignUI.setLblStatus(observable.getLblStatus());
                    poaUI.setLblStatus(observable.getLblStatus());
                    poaUI.setPoANewOnlyEnable();
                    setCompanyDetailsEnableDisable(false);
                    setBorrowerDetailsEnableDisable(true);
                }
                observableBorrow.setCustOpenDate(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                observableBorrow.setCustAddr(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                observableBorrow.setCustPhone(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                observable.setStatus();
                authSignUI.setLblStatus(observable.getLblStatus());
                poaUI.setLblStatus(observable.getLblStatus());
                setButtonEnableDisable();
            }else if (viewType.equals("Borr_Cust_Id")) {
                final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
                observableBorrow.setTxtCustID(CustID);
                if (observableBorrow.setCustOpenDate(CustID)){
                    //                    setCompanyDetailsEnableDisable(true);
                }else{
                    //                    setCompanyDetailsEnableDisable(false);
                }
                observableBorrow.setCustAddr(CustID);
                observableBorrow.setCustPhone(CustID);
                observable.ttNotifyObservers();
                //                txtCustID.setEditable(false);
            }else if (viewType.equals("Guarant_Cust_Id")){
                observableGuarantor.resetGuarantorDetails();
                updateGuarantorTabCustDetails();
                observableGuarantor.setTxtCustomerID_GD(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
                observableGuarantor.setTxtGuarantorNetWorth(CommonUtil.convertObjToStr(hash.get("NETWORTH")));
                
                if (hash.get("DOB") != null && CommonUtil.convertObjToStr(hash.get("DOB")).length() > 0){
                    observableGuarantor.setTdtDOB_GD(DateUtil.getStringDate((java.util.Date) hash.get("DOB")));
                }else{
                    observableGuarantor.setTdtDOB_GD("");
                }
                observableGuarantor.setGuarantorCustOtherAccounts(CustID);
                observableGuarantor.setGuarantorCustAddr(CustID);
                observableGuarantor.setGuarantorCustName(CustID);
                observableGuarantor.setGuarantorCustPhone(CustID);
                cboProdType.setSelectedItem(observableGuarantor.getCboProdType());
                cboProdType.setEnabled(false);
                cboProdId.setSelectedItem(observableGuarantor.getCboProdId());
                cboProdId.setEnabled(false);
                txtGuaranAccNo.setText(observableGuarantor.getTxtGuaranAccNo());
                updateGuarantorTabCustDetails();
            }else if (viewType.equals("CUSTOMER ID")){
                observableComp.resetCustomerDetails();
                custInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")), loanType);
                validateConstitutionCustID();
                if (loanType.equals("LTD")){
                    
                    btnSecurityNew.setEnabled(true);
                }
            }else if (viewType.equals("JOINT ACCOUNT")){
                jointAcctDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            }else if(viewType.equals("SECURITY_CUSTOMER ID")){
                securityCustDetails(hash);
            }else if (viewType.equals("SECURITY NO")){
                securityIDDetails(hash);
            }else if (viewType.equals("DISBURSEMENT_DETAILS")){
                populateDisbursementDetails(hash);
            }else if (viewType.equals("GUARANTOR_ACCT_NO")){
                observableGuarantor.setTxtGuaranAccNo(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                txtGuaranAccNo.setText(observableGuarantor.getTxtGuaranAccNo());
                observableGuarantor.setCboConstitution_GD(CommonUtil.convertObjToStr(observableGuarantor.getCbmConstitution_GD().getDataForKey(CommonUtil.convertObjToStr(hash.get("CONSTITUTION")))));
                cboConstitution_GD.setSelectedItem(observableGuarantor.getCboConstitution_GD());
            }else if (viewType.equals("DEPOSIT_CUSTOMER")) {
                System.out.println("### DEPOSIT_CUSTOMER fillData hash : "+hash);
                double bal = CommonUtil.convertObjToDouble(hash.get("BALANCE")).doubleValue();
                observable.setDepositNo("");
                observable.setDepositNo(CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO")));
                if (bal>0) {
                    txtLimit_SD.setText(String.valueOf(bal*(85.0/100.0)));
                    //
                    boolean checkSameCustomer=observable.setDetailsForLTD(hash);
                    if(checkSameCustomer){
                        setAllSanctionFacilityEnableDisable(true);
                        cboTypeOfFacility.setEnabled(false);
                        tdtTDate.setDateValue(observable.getTdtTDate());
                        tdtFacility_Repay_Date.setDateValue(tdtFDate.getDateValue());//for need ltd
                        tdtFacility_Repay_Date.setDateValue(observable.getTdtTDate());
                        rdoInterest_Compound.setSelected(observable.getRdoInterest_Compound());
                        rdoInterest_Simple.setSelected(observable.getRdoInterest_Simple());
                        cboRepayFreq.setSelectedItem(
                        ((ComboBoxModel) cboRepayFreq.getModel()).getDataForKey("1"));
                        txtNoInstallments.setText("1");
                        txtNoInstallmentsFocusLost();
                        //                    observable.setCboProductId(CommonUtil.convertObjToStr(hash.get("PRODUCT ID")));
                        //                    cboProductId.setSelectedItem(
                        //                    ((ComboBoxModel) cboProductId.getModel()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PRODUCT ID"))));
                    }
                    else{
                        setAllSanctionFacilityEnableDisable(false);
                        txtLimit_SD.setText("");
                    }
                }else{
                    //                    ClientUtil.showAlertWindow("Deposit Amount is Zero");
                    ClientUtil.showMessageWindow("Deposit Amount is Zero");
                    return;
                }
            }
        }
        
        if (viewType.equals("Delete") ||  (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
            setAllBorrowerBtnsEnableDisable(false);
        }
        setModified(true);
    }
    
    private void updateGuarantorTabCustDetails(){
        txtCustomerID_GD.setText(observableGuarantor.getTxtCustomerID_GD());
        txtGuaranName.setText(observableGuarantor.getTxtGuaranName());
        tdtAsOn_GD.setDateValue(observableGuarantor.getTdtAsOn_GD());
        txtArea_GD.setText(observableGuarantor.getTxtArea_GD());
        txtStreet_GD.setText(observableGuarantor.getTxtStreet_GD());
        txtPin_GD.setText(observableGuarantor.getTxtPin_GD());
        cboCity_GD.setSelectedItem(observableGuarantor.getCboCity_GD());
        cboState_GD.setSelectedItem(observableGuarantor.getCboState_GD());
        cboCountry_GD.setSelectedItem(observableGuarantor.getCboCountry_GD());
        txtPhone_GD.setText(observableGuarantor.getTxtPhone_GD());
        txtGuarantorNetWorth.setText(observableGuarantor.getTxtGuarantorNetWorth());
        tdtDOB_GD.setDateValue(observableGuarantor.getTdtDOB_GD());
    }
    
    private void custInfoDisplay(String Cust_ID, String loanType){
        HashMap hash = new HashMap();
        hash.put("CUST_ID",Cust_ID);
        // Remove the old Main CUSTOMER ID
        authSignUI.removeAcctLevelCustomer(observableBorrow.getTxtCustID());
        if (observableBorrow.populateBorrowerTabCustomerDetails(hash,false,loanType)){
            //            setCompanyDetailsEnableDisable(true);
            authSignUI.addAcctLevelCustomer(observableBorrow.getTxtCustID());
            updateBorrowerTabCustDetails();
            txtCustID.setText(observableBorrow.getTxtCustID());
            tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
            System.out.println("inside custinfo@@@@@@@@");
            observableBorrow.populateBorrowerTabCustFields(hash, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
            updateBorrowerTabCustFields();
            hash = null;
        }else{
            txtCustID.setText(observableBorrow.getTxtCustID());
            //            setCompanyDetailsEnableDisable(false);
        }
        // Add the new Main CUSTOMER ID
       /* authSignUI.addAcctLevelCustomer(observableBorrow.getTxtCustID());
        updateBorrowerTabCustDetails();
        txtCustID.setText(observableBorrow.getTxtCustID());
        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
        System.out.println("inside custinfo@@@@@@@@");
        observableBorrow.populateBorrowerTabCustFields(hash, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
        updateBorrowerTabCustFields();
        hash = null;*/
    }
    
    private void updateBorrowerTabCustFields(){
        txtNetWorth.setText(observableComp.getTxtNetWorth());
        txtRiskRating.setText(observableComp.getTxtRiskRating());
        tdtCreditFacilityAvailSince.setDateValue(observableComp.getTdtCreditFacilityAvailSince());
        tdtDealingWithBankSince.setDateValue(observableComp.getTdtDealingWithBankSince());
        cboNatureBusiness.setSelectedItem(observableComp.getCboNatureBusiness());
        tdtAsOn.setDateValue(observableComp.getTdtAsOn());
        updateCompanyDetails();
    }
    
    private void updateCompanyDetails(){
        cboAddressType.setSelectedItem(observableComp.getCboAddressType());
        cboCity_CompDetail.setSelectedItem(observableComp.getCboCity_CompDetail());
        cboCountry_CompDetail.setSelectedItem(observableComp.getCboCountry_CompDetail());
        cboState_CompDetail.setSelectedItem(observableComp.getCboState_CompDetail());
        txtCompanyRegisNo.setText(observableComp.getTxtCompanyRegisNo());
        tdtDateEstablishment.setDateValue(observableComp.getTdtDateEstablishment());
        txtChiefExecutiveName.setText(observableComp.getTxtChiefExecutiveName());
        txtPin_CompDetail.setText(observableComp.getTxtPin_CompDetail());
        txtPhone_CompDetail.setText(observableComp.getTxtPhone_CompDetail());
        txtStreet_CompDetail.setText(observableComp.getTxtStreet_CompDetail());
        txtArea_CompDetail.setText(observableComp.getTxtArea_CompDetail());
    }
    
    private void updateBorrowerTabCustDetails(){
        lblCustName_2.setText(observableBorrow.getLblCustName());
        lblOpenDate2.setText(observableBorrow.getLblOpenDate());
        lblCity_BorrowerProfile_2.setText(observableBorrow.getLblCity());
        lblState_BorrowerProfile_2.setText(observableBorrow.getLblState());
        lblPin_BorrowerProfile_2.setText(observableBorrow.getLblPin());
        lblPhone_BorrowerProfile_2.setText(observableBorrow.getLblPhone());
        lblFax_BorrowerProfile_2.setText(observableBorrow.getLblFax());
    }
    
    private void jointAcctDisplay(String Cust_ID){
        HashMap hash = new HashMap();
        hash.put("CUST_ID", Cust_ID);
        observableBorrow.populateJointAccntTable(hash);
        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
        setBorrowerNewOnlyEnable();
        authSignUI.addAcctLevelCustomer(Cust_ID);
        hash = null;
    }
    
    private void securityCustDetails(HashMap map){
        String strPrevCustID = observableSecurity.getTxtCustID_Security();
        observableSecurity.populateCustDetails(map);
        txtCustID_Security.setText(observableSecurity.getTxtCustID_Security());
        lblCustName_Security_Display.setText(observableSecurity.getLblCustName_Security_Display());
        if (!(strPrevCustID.equals(observableSecurity.getTxtCustID_Security()))){
            // If the prev. cust ID doesn't match with the current one then
            // clear the security no.
            observableSecurity.setTxtSecurityNo("");
            txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
            observableSecurity.setTxtMargin("");
            txtMargin.setText(observableSecurity.getTxtMargin());
        }
        strPrevCustID = null;
    }
    
    private void securityIDDetails(HashMap hash){
        observableSecurity.populateSecurityID_Value(hash, txtLimit_SD.getText());
        txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
        txtSecurityValue.setText(observableSecurity.getTxtSecurityValue());
    }
    
    private void populateDisbursementDetails(HashMap hash){
        observableRepay.populateDisbursementDetails(hash);
        txtLaonAmt.setText(observableRepay.getTxtLaonAmt());
    }
    private void cboRepayFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepayFreqActionPerformed
        // Add your handling code here:
        cboRepayFreqActionPerformed();
    }//GEN-LAST:event_cboRepayFreqActionPerformed
    private void cboRepayFreqActionPerformed(){
        if (cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")){
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
                tdtTDate.setEnabled(false);
            }else{
                tdtTDate.setEnabled(true);
            }
            moratorium_Given_Calculation();
        }else{
            tdtTDate.setEnabled(false);
            calculateSanctionToDate();
        }
        populatePeriodDifference();
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    //To enable and disable the textfields and buttons when NEW button is pressed
    private void newPressedEnableDisable(boolean val){
        poaUI.setPoANewOnlyEnable();
        btnCustomerID_GD.setEnabled(!val);
        btnAccNo.setEnabled(!val);
    }
    
    //To enable or disable text fields and buttons of all Customer fields
    //in the tabbed panel
    private void setbtnCustEnableDisable(boolean val){
        btnCustID.setEnabled(val);
        btnCustomerID_GD.setEnabled(val);
        btnAccNo.setEnabled(val);
        authSignUI.setbtnCustEnableDisable(val);
        poaUI.setbtnCustEnableDisable(val);
    }
    
    private void setBorrowerDetailsEnableDisable(boolean val){
        tdtDealingWithBankSince.setEnabled(false);
        tdtCreditFacilityAvailSince.setEnabled(false);
        txtRiskRating.setEnabled(val);
        txtRiskRating.setEditable(false);
        txtNetWorth.setEnabled(val);
        //        txtNetWorth.setEditable(false);
        cboNatureBusiness.setEnabled(false);
        cboNatureBusiness.setEditable(false);
        tdtAsOn.setEnabled(val);
        //        tdtAsOn.setEnabled(false);
    }
    // To enable or diable the fields in Company details
    private void setCompanyDetailsEnableDisable(boolean val){
        txtCompanyRegisNo.setEnabled(val);
        txtCompanyRegisNo.setEditable(false);
        tdtDateEstablishment.setEnabled(false);
        txtChiefExecutiveName.setEnabled(val);
        txtChiefExecutiveName.setEditable(false);
        cboAddressType.setEnabled(false);
        cboAddressType.setEditable(false);
        txtStreet_CompDetail.setEnabled(val);
        txtStreet_CompDetail.setEditable(false);
        txtArea_CompDetail.setEnabled(val);
        txtArea_CompDetail.setEditable(false);
        cboCity_CompDetail.setEnabled(false);
        cboCity_CompDetail.setEditable(false);
        cboState_CompDetail.setEnabled(false);
        cboState_CompDetail.setEditable(false);
        cboCountry_CompDetail.setEnabled(false);
        cboCountry_CompDetail.setEditable(false);
        txtPin_CompDetail.setEnabled(val);
        txtPin_CompDetail.setEditable(false);
        txtPhone_CompDetail.setEnabled(val);
        txtPhone_CompDetail.setEditable(false);
    }
    
    // To enable and disable all the Tool buttons for the CTable
    private void setAllTableBtnsEnableDisable(boolean val){
        authSignUI.setAuthTabBtnEnableDisable(val);
        authSignUI.setAuthInstAllBtnsEnableDisable(val);
        poaUI.setPoAToolBtnsEnableDisable(val);
        setSanctionFacilityBtnsEnableDisable(val);
        setSanctionMainBtnsEnableDisable(val);
        setFacilityBtnsEnableDisable(val);
        setAllSecurityBtnsEnableDisable(val);
        setAllRepaymentBtnsEnableDisable(val);
        setAllGuarantorBtnsEnableDisable(val);
        setAllInterestBtnsEnableDisable(val);
        setAllBorrowerBtnsEnableDisable(val);
    }
    
    //To enable and disable the buttons in Santion Facility
    private void setSanctionFacilityBtnsEnableDisable(boolean val){
        btnNew1.setEnabled(val);
        btnSave1.setEnabled(val);
        btnDelete1.setEnabled(val);
    }
    
    private void setAllSanctionFacilityEnableDisable(boolean val){
        if (loanType.equals("LTD"))
            cboTypeOfFacility.setEnabled(false);
        else
            cboTypeOfFacility.setEnabled(val);
        cboProductId.setEnabled(val);
        txtLimit_SD.setEnabled(val);
        tdtFDate.setEnabled(val);
        tdtTDate.setEnabled(false);
        txtNoInstallments.setEnabled(val);
        cboRepayFreq.setEnabled(val);
        tdtFacility_Repay_Date.setEnabled(false);
        txtPeriodDifference_Days.setEnabled(val);
        txtPeriodDifference_Days.setEditable(false);
        txtPeriodDifference_Months.setEnabled(val);
        txtPeriodDifference_Months.setEditable(false);
        txtPeriodDifference_Years.setEnabled(val);
        txtPeriodDifference_Years.setEditable(false);
        chkMoratorium_Given.setEnabled(val);
        txtFacility_Moratorium_Period.setEnabled(val);
    }
    
    private void setSanctionMainBtnsEnableDisable(boolean val){
        btnNew2_SD.setEnabled(val);
        btnSave2_SD.setEnabled(val);
        btnDelete2_SD.setEnabled(val);
    }
    
    private void setAllSanctionMainEnableDisable(boolean val){
        txtSanctionSlNo.setEnabled(val);
        txtSanctionSlNo.setEditable(false);
        txtSanctionNo.setEnabled(val);
        tdtSanctionDate.setEnabled(val);
        cboSanctioningAuthority.setEnabled(val);
        txtSanctionRemarks.setEnabled(val);
        cboModeSanction.setEnabled(val);
    }
    
    private void setAllFacilityDetailsEnableDisable(boolean val){
        txtAcct_Name.setEnabled(val);
        if (loanType.equals("LTD")) {
            rdoSecurityDetails_Fully.setEnabled(false);
            rdoSecurityDetails_Partly.setEnabled(false);
            rdoSecurityDetails_Unsec.setEnabled(false);
            rdoMultiDisburseAllow_No.setEnabled(false);
            rdoMultiDisburseAllow_Yes.setEnabled(false);
            chkInsurance.setEnabled(false);
            chkGurantor.setEnabled(false);
            chkStockInspect.setEnabled(false);
            
        } else {
            rdoSecurityDetails_Fully.setEnabled(val);
            rdoSecurityDetails_Partly.setEnabled(val);
            rdoSecurityDetails_Unsec.setEnabled(val);
            rdoMultiDisburseAllow_No.setEnabled(val);
            rdoMultiDisburseAllow_Yes.setEnabled(val);
            chkStockInspect.setEnabled(val);
            chkInsurance.setEnabled(val);
            chkGurantor.setEnabled(val);
            rdoInterest_Simple.setEnabled(false);
            rdoInterest_Compound.setEnabled(false);
        }
        cboAccStatus.setEnabled(val);
        tdtAccountOpenDate.setEnabled(val);
        cboRecommendedByType.setEnabled(val);
        //        rdoAccType_New.setEnabled(val);
        //        rdoAccType_Transfered.setEnabled(val);
        rdoAccLimit_Main.setEnabled(val);
        rdoAccLimit_Submit.setEnabled(val);
        rdoRiskWeight_No.setEnabled(val);
        rdoRiskWeight_Yes.setEnabled(val);
        rdoNatureInterest_NonPLR.setEnabled(false);
        rdoNatureInterest_PLR.setEnabled(false);
        cboInterestType.setEnabled(val);
        tdtDemandPromNoteDate.setEnabled(val);
        tdtDemandPromNoteExpDate.setEnabled(val);
        tdtAODDate.setEnabled(val);
        rdoSubsidy_No.setEnabled(false);
        rdoSubsidy_Yes.setEnabled(false);
        txtPurposeDesc.setEnabled(val);
        txtGroupDesc.setEnabled(false);
        //        rdoInterest_Compound.setEnabled(val);
        //        rdoInterest_Simple.setEnabled(val);
        txtContactPerson.setEnabled(val);
        txtContactPhone.setEnabled(val);
        txtRemarks.setEnabled(val);
    }
    private void setFacilityBtnsEnableDisable(boolean val){
        btnFacilityDelete.setEnabled(val);
        btnFacilitySave.setEnabled(val);
    }
    
    private void setAllSecurityDetailsEnableDisable(boolean val){
        ClientUtil.enableDisable(panSecurityDetails_security, val);
        btnSecurityNo_Security.setEnabled(val);
        btnCustID_Security.setEnabled(val);
        txtSecurityNo.setEditable(false);
        txtSecurityNo.setEnabled(val);
        txtSecurityValue.setEditable(false);
        txtSecurityValue.setEnabled(val);
        txtCustID_Security.setEditable(false);
        txtCustID_Security.setEnabled(val);
        txtMargin.setEnabled(val);
        txtEligibleLoan.setEditable(false);
        txtEligibleLoan.setEnabled(val);
        tdtToDate.setEnabled(val);
        tdtFromDate.setEnabled(val);
    }
    private void setSecurityBtnsOnlyNewEnable(){
        btnSecurityDelete.setEnabled(false);
        btnSecurityNew.setEnabled(true);
        btnSecuritySave.setEnabled(false);
    }
    private void setSecurityBtnsOnlyNewSaveEnable(){
        btnSecurityDelete.setEnabled(false);
        btnSecurityNew.setEnabled(true);
        btnSecuritySave.setEnabled(true);
    }
    private void setAllSecurityBtnsEnableDisable(boolean val){
        btnSecurityDelete.setEnabled(val);
        btnSecurityNew.setEnabled(val);
        btnSecuritySave.setEnabled(val);
    }
    private void setAllRepaymentDetailsEnableDisable(boolean val){
        txtScheduleNo.setEditable(false);
        if(loanType.equals("OTHERS")){
            cboRepayType.setEnabled(val);
            btnEMI_Calculate.setEnabled(true);
        }else {
            cboRepayType.setEnabled(false);
            btnEMI_Calculate.setEnabled(false);
        }
        
        txtLaonAmt.setEnabled(false);
        txtLaonAmt.setEditable(false);
        txtRepayScheduleMode.setEditable(false);
        txtRepayScheduleMode.setEnabled(val);
        cboRepayFreq_Repayment.setEnabled(false);
        
        txtNoMonthsMora.setEditable(false);
        txtNoMonthsMora.setEnabled(val);
        tdtFirstInstall.setEnabled(false);
        tdtLastInstall.setEnabled(false);
        txtNoInstall.setEditable(false);
        txtNoInstall.setEnabled(val);
        tdtDisbursement_Dt.setEnabled(false);
        txtTotalBaseAmt.setEditable(false);
        txtTotalBaseAmt.setEnabled(val);
        txtAmtLastInstall.setEditable(false);
        txtAmtLastInstall.setEnabled(val);
        txtAmtPenulInstall.setEditable(false);
        txtAmtPenulInstall.setEnabled(val);
        txtTotalInstallAmt.setEditable(false);
        txtTotalInstallAmt.setEnabled(val);
        rdoDoAddSIs_No.setEnabled(val);
        rdoDoAddSIs_Yes.setEnabled(val);
        rdoPostDatedCheque_No.setEnabled(val);
        rdoPostDatedCheque_Yes.setEnabled(val);
        if (rdoInActive_Repayment.isSelected()){
            setRdoRepaymentStatusDisable();
        }else{
            rdoActive_Repayment.setEnabled(val);
            rdoInActive_Repayment.setEnabled(val);
        }
    }
    
    private void setAllRepaymentBtnsEnableDisable(boolean val){
        btnRepayment_Delete.setEnabled(val);
        btnRepayment_New.setEnabled(val);
        btnRepayment_Save.setEnabled(val);
    }
    
    private void setRepaymentBtnsEnableDisable(boolean val){
        btnRepayment_Delete.setEnabled(!val);
        btnRepayment_New.setEnabled(val);
        btnRepayment_Save.setEnabled(!val);
    }
    
    private void setRepaymentNewOnlyEnable(){
        btnRepayment_Delete.setEnabled(false);
        //        if (loanType.equals("LTD"))
        //            btnRepayment_New.setEnabled(false);
        //        else
        btnRepayment_New.setEnabled(true);
        btnRepayment_Save.setEnabled(false);
        btnEMI_Calculate.setEnabled(false);
    }
    
    private void setRepaymentDeleteOnlyDisbale(){
        btnRepayment_Delete.setEnabled(false);
        if (loanType.equals("LTD"))
            btnRepayment_New.setEnabled(false);
        else
            btnRepayment_New.setEnabled(true);
        btnRepayment_Save.setEnabled(true);
        btnEMI_Calculate.setEnabled(true);
    }
    
    private void setAllGuarantorDetailsEnableDisable(boolean val){
        txtCustomerID_GD.setEditable(false);
        txtCustomerID_GD.setEnabled(val);
        txtGuaranAccNo.setEditable(false);
        txtGuaranAccNo.setEnabled(val);
        txtGuaranName.setEditable(false);
        txtGuaranName.setEnabled(val);
        txtGuarantorNo.setEnabled(val);
        txtStreet_GD.setEditable(false);
        txtStreet_GD.setEnabled(val);
        txtArea_GD.setEditable(false);
        txtArea_GD.setEnabled(val);
        cboCity_GD.setEnabled(false);
        cboState_GD.setEnabled(false);
        txtPin_GD.setEditable(false);
        txtPin_GD.setEnabled(val);
        txtPhone_GD.setEditable(false);
        txtPhone_GD.setEnabled(val);
        cboConstitution_GD.setEnabled(false);
        txtGuarantorNetWorth.setEditable(false);
        txtGuarantorNetWorth.setEnabled(val);
        tdtAsOn_GD.setEnabled(false);
        cboCountry_GD.setEnabled(false);
        tdtDOB_GD.setEnabled(false);
        cboProdId.setEnabled(val);
        cboProdType.setEnabled(val);
    }
    
    private void setGuarantorDetailsNewOnlyEnabled(){
        btnGuarantorNew.setEnabled(true);
        btnGuarantorSave.setEnabled(false);
        btnGuarantorDelete.setEnabled(false);
    }
    
    private void setGuarantorDetailsDeleteOnlyDisabled(){
        btnGuarantorNew.setEnabled(true);
        btnGuarantorSave.setEnabled(true);
        btnGuarantorDelete.setEnabled(false);
    }
    
    private void setAllGuarantorBtnsEnableDisable(boolean val){
        btnGuarantorNew.setEnabled(val);
        btnGuarantorSave.setEnabled(val);
        btnGuarantorDelete.setEnabled(val);
    }
    
    private void setAllDocumentDetailsEnableDisable(boolean val){
        //        rdoYes_DocumentDetails.setEnabled(val);
        //        rdoNo_DocumentDetails.setEnabled(val);
        //        tdtSubmitDate_DocumentDetails.setEnabled(val);
        //        txtRemarks_DocumentDetails.setEnabled(val);
        //        rdoYes_Executed_DOC.setEnabled(val);
        //        rdoNo_Executed_DOC.setEnabled(val);
        //        rdoYes_Mandatory_DOC.setEnabled(val);
        //        rdoNo_Mandatory_DOC.setEnabled(val);
        //        tdtExecuteDate_DOC.setEnabled(val);
        //        tdtExpiryDate_DOC.setEnabled(val);
        ClientUtil.enableDisable(panTabDetails_DocumentDetails, val);
    }
    
    private void setDocumentToolBtnEnableDisable(boolean val){
        btnSave_DocumentDetails.setEnabled(val);
    }
    
    private void setAllInterestDetailsEnableDisable(boolean val){
        //        tdtFrom.setEnabled(val);
        //        tdtTo.setEnabled(val);
        //        txtFromAmt.setEnabled(val);
        //        txtToAmt.setEnabled(val);
        //        txtInter.setEnabled(val);
        //        txtPenalInter.setEnabled(val);
        //        txtAgainstClearingInter.setEnabled(val);
        //        txtPenalStatement.setEnabled(val);
        //        txtInterExpLimit.setEnabled(val);
        ClientUtil.enableDisable(panTableFields, val);
    }
    
    private void setInterestDetailsOnlyNewEnabled(){
        btnInterestMaintenanceNew.setEnabled(true);
        btnInterestMaintenanceSave.setEnabled(false);
        btnInterestMaintenanceDelete.setEnabled(false);
    }
    
    private void setInterestDetailsOnlyDeleteDisabled(){
        btnInterestMaintenanceNew.setEnabled(true);
        btnInterestMaintenanceSave.setEnabled(true);
        btnInterestMaintenanceDelete.setEnabled(false);
    }
    
    private void setAllInterestBtnsEnableDisable(boolean val){
        btnInterestMaintenanceNew.setEnabled(val);
        btnInterestMaintenanceSave.setEnabled(val);
        btnInterestMaintenanceDelete.setEnabled(val);
    }
    
    private void setAllClassificationDetailsEnableDisable(boolean val){
        cboCommodityCode.setEnabled(val);
        cboSectorCode1.setEnabled(val);
        cboPurposeCode.setEnabled(val);
        cboIndusCode.setEnabled(val);
        cbo20Code.setEnabled(val);
        cboGovtSchemeCode.setEnabled(val);
        cboGuaranteeCoverCode.setEnabled(val);
        cboHealthCode.setEnabled(val);
        cboDistrictCode.setEnabled(val);
        cboWeakerSectionCode.setEnabled(val);
        cboRefinancingInsti.setEnabled(val);
        cboAssetCode.setEnabled(val);
        tdtNPADate.setEnabled(val);//false
        cboTypeFacility.setEnabled(val);
        chkDirectFinance.setEnabled(val);
        chkECGC.setEnabled(val);
        chkPrioritySector.setEnabled(val);
        chkDocumentcomplete.setEnabled(val);
        chkQIS.setEnabled(val);
    }
    
    private void setAllBorrowerBtnsEnableDisable(boolean val){
        btnNew_Borrower.setEnabled(val);
        btnDeleteBorrower.setEnabled(val);
        btnToMain_Borrower.setEnabled(val);
    }
    
    private void setBorrowerToMainOnlyDisable(){
        btnNew_Borrower.setEnabled(true);
        btnDeleteBorrower.setEnabled(true);
        btnToMain_Borrower.setEnabled(false);
    }
    
    private void setBorrowerNewOnlyEnable(){
        btnNew_Borrower.setEnabled(true);
        btnDeleteBorrower.setEnabled(false);
        btnToMain_Borrower.setEnabled(false);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        TermLoanUI termLoanUI = new TermLoanUI();
        frm.getContentPane().add(termLoanUI);
        termLoanUI.show();
        frm.setSize(600, 500);
        frm.show();
    }
    
    /**
     * Getter for property facilitySaved.
     * @return Value of property facilitySaved.
     */
    public boolean isFacilitySaved() {
        return facilitySaved;
    }
    
    /**
     * Setter for property facilitySaved.
     * @param facilitySaved New value of property facilitySaved.
     */
    public void setFacilitySaved(boolean facilitySaved) {
        this.facilitySaved = facilitySaved;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel PanAcc_CD;
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnCustID_Security;
    private com.see.truetransact.uicomponent.CButton btnCustomerID_GD;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete1;
    private com.see.truetransact.uicomponent.CButton btnDelete2_SD;
    private com.see.truetransact.uicomponent.CButton btnDeleteBorrower;
    private com.see.truetransact.uicomponent.CButton btnEMI_Calculate;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFacilityDelete;
    private com.see.truetransact.uicomponent.CButton btnFacilitySave;
    private com.see.truetransact.uicomponent.CButton btnGuarantorDelete;
    private com.see.truetransact.uicomponent.CButton btnGuarantorNew;
    private com.see.truetransact.uicomponent.CButton btnGuarantorSave;
    private com.see.truetransact.uicomponent.CButton btnInterestMaintenanceDelete;
    private com.see.truetransact.uicomponent.CButton btnInterestMaintenanceNew;
    private com.see.truetransact.uicomponent.CButton btnInterestMaintenanceSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew1;
    private com.see.truetransact.uicomponent.CButton btnNew2_SD;
    private com.see.truetransact.uicomponent.CButton btnNew_Borrower;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRepayment_Delete;
    private com.see.truetransact.uicomponent.CButton btnRepayment_New;
    private com.see.truetransact.uicomponent.CButton btnRepayment_Save;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave1;
    private com.see.truetransact.uicomponent.CButton btnSave2_SD;
    private com.see.truetransact.uicomponent.CButton btnSave_DocumentDetails;
    private com.see.truetransact.uicomponent.CButton btnSecurityDelete;
    private com.see.truetransact.uicomponent.CButton btnSecurityNew;
    private com.see.truetransact.uicomponent.CButton btnSecurityNo_Security;
    private com.see.truetransact.uicomponent.CButton btnSecuritySave;
    private com.see.truetransact.uicomponent.CButton btnToMain_Borrower;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cbo20Code;
    private com.see.truetransact.uicomponent.CComboBox cboAccStatus;
    private com.see.truetransact.uicomponent.CComboBox cboAddressType;
    private com.see.truetransact.uicomponent.CComboBox cboAssetCode;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboCity_CompDetail;
    private com.see.truetransact.uicomponent.CComboBox cboCity_GD;
    private com.see.truetransact.uicomponent.CComboBox cboCommodityCode;
    private com.see.truetransact.uicomponent.CComboBox cboConstitution;
    private com.see.truetransact.uicomponent.CComboBox cboConstitution_GD;
    private com.see.truetransact.uicomponent.CComboBox cboCountry_CompDetail;
    private com.see.truetransact.uicomponent.CComboBox cboCountry_GD;
    private com.see.truetransact.uicomponent.CComboBox cboDistrictCode;
    private com.see.truetransact.uicomponent.CComboBox cboGovtSchemeCode;
    private com.see.truetransact.uicomponent.CComboBox cboGuaranteeCoverCode;
    private com.see.truetransact.uicomponent.CComboBox cboHealthCode;
    private com.see.truetransact.uicomponent.CComboBox cboIndusCode;
    private com.see.truetransact.uicomponent.CComboBox cboIntGetFrom;
    private com.see.truetransact.uicomponent.CComboBox cboInterestType;
    private com.see.truetransact.uicomponent.CComboBox cboModeSanction;
    private com.see.truetransact.uicomponent.CComboBox cboNatureBusiness;
    private com.see.truetransact.uicomponent.CComboBox cboOpModeAI;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboPurposeCode;
    private com.see.truetransact.uicomponent.CComboBox cboRecommendedByType;
    private com.see.truetransact.uicomponent.CComboBox cboRefinancingInsti;
    private com.see.truetransact.uicomponent.CComboBox cboRepayFreq;
    private com.see.truetransact.uicomponent.CComboBox cboRepayFreq_Repayment;
    private com.see.truetransact.uicomponent.CComboBox cboRepayType;
    private com.see.truetransact.uicomponent.CComboBox cboSanctioningAuthority;
    private com.see.truetransact.uicomponent.CComboBox cboSectorCode1;
    private com.see.truetransact.uicomponent.CComboBox cboSettlementModeAI;
    private com.see.truetransact.uicomponent.CComboBox cboState_CompDetail;
    private com.see.truetransact.uicomponent.CComboBox cboState_GD;
    private com.see.truetransact.uicomponent.CComboBox cboStmtFreqAD;
    private com.see.truetransact.uicomponent.CComboBox cboTypeFacility;
    private com.see.truetransact.uicomponent.CComboBox cboTypeOfFacility;
    private com.see.truetransact.uicomponent.CComboBox cboWeakerSectionCode;
    private com.see.truetransact.uicomponent.CCheckBox chkABBChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkATMAD;
    private com.see.truetransact.uicomponent.CCheckBox chkChequeBookAD;
    private com.see.truetransact.uicomponent.CCheckBox chkChequeRetChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditAD;
    private com.see.truetransact.uicomponent.CCheckBox chkCustGrpLimitValidationAD;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitAD;
    private com.see.truetransact.uicomponent.CCheckBox chkDirectFinance;
    private com.see.truetransact.uicomponent.CCheckBox chkDocumentcomplete;
    private com.see.truetransact.uicomponent.CCheckBox chkECGC;
    private com.see.truetransact.uicomponent.CCheckBox chkGurantor;
    private com.see.truetransact.uicomponent.CCheckBox chkInopChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkInsurance;
    private com.see.truetransact.uicomponent.CCheckBox chkMobileBankingAD;
    private com.see.truetransact.uicomponent.CCheckBox chkMoratorium_Given;
    private com.see.truetransact.uicomponent.CCheckBox chkNPAChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkNROStatusAD;
    private com.see.truetransact.uicomponent.CCheckBox chkNonMainMinBalChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkPayIntOnCrBalIN;
    private com.see.truetransact.uicomponent.CCheckBox chkPayIntOnDrBalIN;
    private com.see.truetransact.uicomponent.CCheckBox chkPrioritySector;
    private com.see.truetransact.uicomponent.CCheckBox chkQIS;
    private com.see.truetransact.uicomponent.CCheckBox chkStmtChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkStockInspect;
    private com.see.truetransact.uicomponent.CCheckBox chkStopPmtChrgAD;
    private com.see.truetransact.uicomponent.CLabel lbl20Code;
    private com.see.truetransact.uicomponent.CLabel lblABB;
    private com.see.truetransact.uicomponent.CLabel lblABBChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblAODDate;
    private com.see.truetransact.uicomponent.CLabel lblATMFromDateAD;
    private com.see.truetransact.uicomponent.CLabel lblATMNoAD;
    private com.see.truetransact.uicomponent.CLabel lblATMToDateAD;
    private com.see.truetransact.uicomponent.CLabel lblAccCloseChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadSec;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadSec_2;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_2;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_CD;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_CD_2;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_GD;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_GD_2;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_IM;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_IM_2;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_RS;
    private com.see.truetransact.uicomponent.CLabel lblAccHead_RS_2;
    private com.see.truetransact.uicomponent.CLabel lblAccLimit;
    private com.see.truetransact.uicomponent.CLabel lblAccNoSec;
    private com.see.truetransact.uicomponent.CLabel lblAccNoSec_2;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_CD;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_CD_2;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_GD;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_GD_2;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_IM;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_IM_2;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_RS;
    private com.see.truetransact.uicomponent.CLabel lblAccNo_RS_2;
    private com.see.truetransact.uicomponent.CLabel lblAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblAccOpeningChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblAccStatus;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead_FD;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead_FD_Disp;
    private com.see.truetransact.uicomponent.CLabel lblAcctHead_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctHead_Disp_ODetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctHead_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctHead_ODetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Disp_ODetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_FD;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_FD_Disp;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_ODetails;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Sanction;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Sanction_Disp;
    private com.see.truetransact.uicomponent.CLabel lblAcct_Name;
    private com.see.truetransact.uicomponent.CLabel lblAddressType;
    private com.see.truetransact.uicomponent.CLabel lblAgClearingIN;
    private com.see.truetransact.uicomponent.CLabel lblAgClearingValueIN;
    private com.see.truetransact.uicomponent.CLabel lblAgainstClearingInter;
    private com.see.truetransact.uicomponent.CLabel lblAmtLastInstall;
    private com.see.truetransact.uicomponent.CLabel lblAmtPenulInstall;
    private com.see.truetransact.uicomponent.CLabel lblArea_CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblArea_GD;
    private com.see.truetransact.uicomponent.CLabel lblAsOn;
    private com.see.truetransact.uicomponent.CLabel lblAsOn_GD;
    private com.see.truetransact.uicomponent.CLabel lblAssetCode;
    private com.see.truetransact.uicomponent.CLabel lblBlank1;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerNo;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerNo_2;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblChequeBookChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblChequeReturn;
    private com.see.truetransact.uicomponent.CLabel lblChiefExecutiveName;
    private com.see.truetransact.uicomponent.CLabel lblCity_BorrowerProfile;
    private com.see.truetransact.uicomponent.CLabel lblCity_BorrowerProfile_2;
    private com.see.truetransact.uicomponent.CLabel lblCity_CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblCity_GD;
    private com.see.truetransact.uicomponent.CLabel lblCollectInoperative;
    private com.see.truetransact.uicomponent.CLabel lblCommodityCode;
    private com.see.truetransact.uicomponent.CLabel lblCompanyRegisNo;
    private com.see.truetransact.uicomponent.CLabel lblConstitution;
    private com.see.truetransact.uicomponent.CLabel lblConstitution_GD;
    private com.see.truetransact.uicomponent.CLabel lblContactPerson;
    private com.see.truetransact.uicomponent.CLabel lblContactPhone;
    private com.see.truetransact.uicomponent.CLabel lblCountry_CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblCountry_GD;
    private com.see.truetransact.uicomponent.CLabel lblCrInterestRateIN;
    private com.see.truetransact.uicomponent.CLabel lblCrInterestRateValueIN;
    private com.see.truetransact.uicomponent.CLabel lblCredit;
    private com.see.truetransact.uicomponent.CLabel lblCreditFacilityAvailSince;
    private com.see.truetransact.uicomponent.CLabel lblCreditFromDateAD;
    private com.see.truetransact.uicomponent.CLabel lblCreditNoAD;
    private com.see.truetransact.uicomponent.CLabel lblCreditToDateAD;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustID_Security;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustName_2;
    private com.see.truetransact.uicomponent.CLabel lblCustName_Security;
    private com.see.truetransact.uicomponent.CLabel lblCustName_Security_Display;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID_GD;
    private com.see.truetransact.uicomponent.CLabel lblDOB_GD;
    private com.see.truetransact.uicomponent.CLabel lblDateEstablishment;
    private com.see.truetransact.uicomponent.CLabel lblDealingWithBankSince;
    private com.see.truetransact.uicomponent.CLabel lblDebit;
    private com.see.truetransact.uicomponent.CLabel lblDebitFromDateAD;
    private com.see.truetransact.uicomponent.CLabel lblDebitNoAD;
    private com.see.truetransact.uicomponent.CLabel lblDebitToDateAD;
    private com.see.truetransact.uicomponent.CLabel lblDemandPromNoteDate;
    private com.see.truetransact.uicomponent.CLabel lblDemandPromNoteExpDate;
    private com.see.truetransact.uicomponent.CLabel lblDirectFinance;
    private com.see.truetransact.uicomponent.CLabel lblDisbursement_Dt;
    private com.see.truetransact.uicomponent.CLabel lblDistrictCode;
    private com.see.truetransact.uicomponent.CLabel lblDoAddSIs;
    private com.see.truetransact.uicomponent.CLabel lblDocDesc_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocDesc_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocNo_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocNo_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocType_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocType_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocumentcomplete;
    private com.see.truetransact.uicomponent.CLabel lblDrInterestRateIN;
    private com.see.truetransact.uicomponent.CLabel lblDrInterestRateValueIN;
    private com.see.truetransact.uicomponent.CLabel lblECGC;
    private com.see.truetransact.uicomponent.CLabel lblEligibleLoan;
    private com.see.truetransact.uicomponent.CLabel lblExcessWithChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblExecuteDate_DOC;
    private com.see.truetransact.uicomponent.CLabel lblExecuted_DOC;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate_2;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate_DOC;
    private com.see.truetransact.uicomponent.CLabel lblFDate;
    private com.see.truetransact.uicomponent.CLabel lblFacility_Moratorium_Period;
    private com.see.truetransact.uicomponent.CLabel lblFacility_Repay_Date;
    private com.see.truetransact.uicomponent.CLabel lblFax_BorrowerProfile;
    private com.see.truetransact.uicomponent.CLabel lblFax_BorrowerProfile_2;
    private com.see.truetransact.uicomponent.CLabel lblFirstInstall;
    private com.see.truetransact.uicomponent.CLabel lblFolioChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblFrom;
    private com.see.truetransact.uicomponent.CLabel lblFromAmt;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGovtSchemeCode;
    private com.see.truetransact.uicomponent.CLabel lblGroupDesc;
    private com.see.truetransact.uicomponent.CLabel lblGuaranAccNo;
    private com.see.truetransact.uicomponent.CLabel lblGuaranName;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeCoverCode;
    private com.see.truetransact.uicomponent.CLabel lblGuarantorNetWorth;
    private com.see.truetransact.uicomponent.CLabel lblGuarantorNo;
    private com.see.truetransact.uicomponent.CLabel lblHealthCode;
    private com.see.truetransact.uicomponent.CLabel lblIndusCode;
    private com.see.truetransact.uicomponent.CLabel lblIntGetFrom;
    private com.see.truetransact.uicomponent.CLabel lblInter;
    private com.see.truetransact.uicomponent.CLabel lblInterExpLimit;
    private com.see.truetransact.uicomponent.CLabel lblInterest;
    private com.see.truetransact.uicomponent.CLabel lblInterestType;
    private com.see.truetransact.uicomponent.CLabel lblLaonAmt;
    private com.see.truetransact.uicomponent.CLabel lblLastInstall;
    private com.see.truetransact.uicomponent.CLabel lblLimitAmt;
    private com.see.truetransact.uicomponent.CLabel lblLimitAmt_2;
    private com.see.truetransact.uicomponent.CLabel lblLimit_SD;
    private com.see.truetransact.uicomponent.CLabel lblMandatory_DOC;
    private com.see.truetransact.uicomponent.CLabel lblMargin;
    private com.see.truetransact.uicomponent.CLabel lblMinActBalanceAD;
    private com.see.truetransact.uicomponent.CLabel lblMisServiceChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblModeSanction;
    private com.see.truetransact.uicomponent.CLabel lblMoratorium_Given;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CLabel lblNPA;
    private com.see.truetransact.uicomponent.CLabel lblNPAChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblNPADate;
    private com.see.truetransact.uicomponent.CLabel lblNatureBusiness;
    private com.see.truetransact.uicomponent.CLabel lblNatureInterest;
    private com.see.truetransact.uicomponent.CLabel lblNetWorth;
    private com.see.truetransact.uicomponent.CLabel lblNoInstall;
    private com.see.truetransact.uicomponent.CLabel lblNoInstallments;
    private com.see.truetransact.uicomponent.CLabel lblNoMonthsMora;
    private com.see.truetransact.uicomponent.CLabel lblNonMaintenance;
    private com.see.truetransact.uicomponent.CLabel lblOpModeAI;
    private com.see.truetransact.uicomponent.CLabel lblOpenDate;
    private com.see.truetransact.uicomponent.CLabel lblOpenDate2;
    private com.see.truetransact.uicomponent.CLabel lblPLR_Limit;
    private com.see.truetransact.uicomponent.CLabel lblPLR_Limit_2;
    private com.see.truetransact.uicomponent.CLabel lblPenalInter;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestRateIN;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestValueIN;
    private com.see.truetransact.uicomponent.CLabel lblPenalStatement;
    private com.see.truetransact.uicomponent.CLabel lblPeriodDifference;
    private com.see.truetransact.uicomponent.CLabel lblPeriodDifference_Days;
    private com.see.truetransact.uicomponent.CLabel lblPeriodDifference_Months;
    private com.see.truetransact.uicomponent.CLabel lblPeriodDifference_Years;
    private com.see.truetransact.uicomponent.CLabel lblPhone_BorrowerProfile;
    private com.see.truetransact.uicomponent.CLabel lblPhone_BorrowerProfile_2;
    private com.see.truetransact.uicomponent.CLabel lblPhone_CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblPhone_GD;
    private com.see.truetransact.uicomponent.CLabel lblPin_BorrowerProfile;
    private com.see.truetransact.uicomponent.CLabel lblPin_BorrowerProfile_2;
    private com.see.truetransact.uicomponent.CLabel lblPin_CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblPin_GD;
    private com.see.truetransact.uicomponent.CLabel lblPostDatedCheque;
    private com.see.truetransact.uicomponent.CLabel lblPrioritySector;
    private com.see.truetransact.uicomponent.CLabel lblProID_CD;
    private com.see.truetransact.uicomponent.CLabel lblProID_CD_Disp;
    private com.see.truetransact.uicomponent.CLabel lblProdID_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblProdID_Disp_ODetails;
    private com.see.truetransact.uicomponent.CLabel lblProdID_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblProdID_GD;
    private com.see.truetransact.uicomponent.CLabel lblProdID_GD_Disp;
    private com.see.truetransact.uicomponent.CLabel lblProdID_IM;
    private com.see.truetransact.uicomponent.CLabel lblProdID_IM_Disp;
    private com.see.truetransact.uicomponent.CLabel lblProdID_ODetails;
    private com.see.truetransact.uicomponent.CLabel lblProdID_RS;
    private com.see.truetransact.uicomponent.CLabel lblProdID_RS_Disp;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdId1;
    private com.see.truetransact.uicomponent.CLabel lblProdId_Disp;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductID_FD;
    private com.see.truetransact.uicomponent.CLabel lblProductID_FD_Disp;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblPurposeCode;
    private com.see.truetransact.uicomponent.CLabel lblPurposeDesc;
    private com.see.truetransact.uicomponent.CLabel lblQIS;
    private com.see.truetransact.uicomponent.CLabel lblRateCodeIN;
    private com.see.truetransact.uicomponent.CLabel lblRateCodeValueIN;
    private com.see.truetransact.uicomponent.CLabel lblRecommandByType;
    private com.see.truetransact.uicomponent.CLabel lblReferences;
    private com.see.truetransact.uicomponent.CLabel lblRefinancingInsti;
    private com.see.truetransact.uicomponent.CLabel lblRemark_FD;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRemarks_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblRemarks__CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblRepayFreq;
    private com.see.truetransact.uicomponent.CLabel lblRepayFreq_Repayment;
    private com.see.truetransact.uicomponent.CLabel lblRepayScheduleMode;
    private com.see.truetransact.uicomponent.CLabel lblRepayType;
    private com.see.truetransact.uicomponent.CLabel lblRiskRating;
    private com.see.truetransact.uicomponent.CLabel lblRiskWeight;
    private com.see.truetransact.uicomponent.CLabel lblSancDate;
    private com.see.truetransact.uicomponent.CLabel lblSancDate_2;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDate;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDate1;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDate2;
    private com.see.truetransact.uicomponent.CLabel lblSanctionNo;
    private com.see.truetransact.uicomponent.CLabel lblSanctionNo1;
    private com.see.truetransact.uicomponent.CLabel lblSanctionNo2;
    private com.see.truetransact.uicomponent.CLabel lblSanctionSlNo;
    private com.see.truetransact.uicomponent.CLabel lblSanctioningAuthority;
    private com.see.truetransact.uicomponent.CLabel lblScheduleNo;
    private com.see.truetransact.uicomponent.CLabel lblSectorCode1;
    private com.see.truetransact.uicomponent.CLabel lblSecurityNo;
    private com.see.truetransact.uicomponent.CLabel lblSecurityValue;
    private com.see.truetransact.uicomponent.CLabel lblSettlementModeAI;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblState_BorrowerProfile;
    private com.see.truetransact.uicomponent.CLabel lblState_BorrowerProfile_2;
    private com.see.truetransact.uicomponent.CLabel lblState_CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblState_GD;
    private com.see.truetransact.uicomponent.CLabel lblStatement;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStatus_Repayment;
    private com.see.truetransact.uicomponent.CLabel lblStmtFreqAD;
    private com.see.truetransact.uicomponent.CLabel lblStopPayment;
    private com.see.truetransact.uicomponent.CLabel lblStreet_CompDetail;
    private com.see.truetransact.uicomponent.CLabel lblStreet_GD;
    private com.see.truetransact.uicomponent.CLabel lblSubmitDate_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblSubsidy;
    private com.see.truetransact.uicomponent.CLabel lblTDate;
    private com.see.truetransact.uicomponent.CLabel lblTo;
    private com.see.truetransact.uicomponent.CLabel lblToAmt;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotalBaseAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalInstallAmt;
    private com.see.truetransact.uicomponent.CLabel lblTypeFacility;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfFacility;
    private com.see.truetransact.uicomponent.CLabel lblWeakerSectionCode;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CMenuBar mbrTermLoan;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccLimit;
    private com.see.truetransact.uicomponent.CPanel panAcc_IM;
    private com.see.truetransact.uicomponent.CPanel panAcc_RS;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAcctDetails_DocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panAcctInfo_ODetails;
    private com.see.truetransact.uicomponent.CPanel panBorrowCompanyDetails;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustID;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustName;
    private com.see.truetransact.uicomponent.CPanel panBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CPanel panBorrowerTabTools;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panButton2_SD;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCardInfo;
    private com.see.truetransact.uicomponent.CPanel panClassDetails;
    private com.see.truetransact.uicomponent.CPanel panClassDetails_Acc;
    private com.see.truetransact.uicomponent.CPanel panClassDetails_Details;
    private com.see.truetransact.uicomponent.CPanel panCode;
    private com.see.truetransact.uicomponent.CPanel panCode2;
    private com.see.truetransact.uicomponent.CPanel panCompanyDetails;
    private com.see.truetransact.uicomponent.CPanel panCompanyDetailsTrash;
    private com.see.truetransact.uicomponent.CPanel panCompanyDetails_Addr;
    private com.see.truetransact.uicomponent.CPanel panCompanyDetails_Company;
    private com.see.truetransact.uicomponent.CPanel panDate;
    private com.see.truetransact.uicomponent.CPanel panDemandPromssoryDate;
    private com.see.truetransact.uicomponent.CPanel panDiffCharges;
    private com.see.truetransact.uicomponent.CPanel panDoAddSIs;
    private com.see.truetransact.uicomponent.CPanel panDocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panExecuted_DOC;
    private com.see.truetransact.uicomponent.CPanel panFDAccount;
    private com.see.truetransact.uicomponent.CPanel panFDDate;
    private com.see.truetransact.uicomponent.CPanel panFacilityChkBoxes;
    private com.see.truetransact.uicomponent.CPanel panFacilityDetails;
    private com.see.truetransact.uicomponent.CPanel panFacilityDetails_Data;
    private javax.swing.JPanel panFacilityProdID;
    private com.see.truetransact.uicomponent.CPanel panFacilityTools;
    private com.see.truetransact.uicomponent.CPanel panFlexiOpt;
    private com.see.truetransact.uicomponent.CPanel panGuaranAddr;
    private com.see.truetransact.uicomponent.CPanel panGuarantor;
    private com.see.truetransact.uicomponent.CPanel panGuarantorDetail_Detail;
    private com.see.truetransact.uicomponent.CPanel panGuarantorDetails;
    private com.see.truetransact.uicomponent.CPanel panGuarantorDetailsTable;
    private com.see.truetransact.uicomponent.CPanel panGuarantorInsuranceDetails;
    private com.see.truetransact.uicomponent.CPanel panInstall_RS;
    private com.see.truetransact.uicomponent.CPanel panInterMaintenance;
    private com.see.truetransact.uicomponent.CPanel panInterMaintenance_Acc;
    private com.see.truetransact.uicomponent.CPanel panInterMaintenance_Details;
    private com.see.truetransact.uicomponent.CPanel panInterMaintenance_Table;
    private com.see.truetransact.uicomponent.CPanel panInterest;
    private com.see.truetransact.uicomponent.CPanel panInterestPayableIN;
    private com.see.truetransact.uicomponent.CPanel panIsRequired;
    private com.see.truetransact.uicomponent.CPanel panLastIntApp;
    private com.see.truetransact.uicomponent.CPanel panLimit;
    private com.see.truetransact.uicomponent.CPanel panMandatory_DOC;
    private com.see.truetransact.uicomponent.CPanel panMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CPanel panNatureInterest;
    private com.see.truetransact.uicomponent.CPanel panPeriodDifference;
    private com.see.truetransact.uicomponent.CPanel panPostDatedCheque;
    private com.see.truetransact.uicomponent.CPanel panProd_CD;
    private com.see.truetransact.uicomponent.CPanel panProd_GD;
    private com.see.truetransact.uicomponent.CPanel panProd_IM;
    private com.see.truetransact.uicomponent.CPanel panProd_RS;
    private com.see.truetransact.uicomponent.CPanel panRatesIN;
    private com.see.truetransact.uicomponent.CPanel panRepayment;
    private com.see.truetransact.uicomponent.CPanel panRepaymentCTable;
    private com.see.truetransact.uicomponent.CPanel panRepaymentSchedule;
    private com.see.truetransact.uicomponent.CPanel panRepaymentSchedule_Details;
    private com.see.truetransact.uicomponent.CPanel panRepaymentToolBtns;
    private com.see.truetransact.uicomponent.CPanel panRiskWeight;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Mode;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Sanction;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Table;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Upper;
    private com.see.truetransact.uicomponent.CPanel panSchedule_RS;
    private com.see.truetransact.uicomponent.CPanel panSecDetails;
    private com.see.truetransact.uicomponent.CPanel panSecurity;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_Acc;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_FD;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_security;
    private com.see.truetransact.uicomponent.CPanel panSecurityNature;
    private com.see.truetransact.uicomponent.CPanel panSecurityTable;
    private com.see.truetransact.uicomponent.CPanel panSecurityTableMain;
    private com.see.truetransact.uicomponent.CPanel panSecurityTools;
    private com.see.truetransact.uicomponent.CPanel panShareMaintenance;
    private com.see.truetransact.uicomponent.CPanel panShareMaintenance_Table;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStatus_Repayment;
    private com.see.truetransact.uicomponent.CPanel panSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panSubsidy;
    private com.see.truetransact.uicomponent.CPanel panTabDetails_DocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panTable2_SD;
    private com.see.truetransact.uicomponent.CPanel panTableFields;
    private com.see.truetransact.uicomponent.CPanel panTableFields_SD;
    private com.see.truetransact.uicomponent.CPanel panTable_DocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panTable_IM;
    private com.see.truetransact.uicomponent.CPanel panTable_SD;
    private com.see.truetransact.uicomponent.CPanel panTable_Share;
    private com.see.truetransact.uicomponent.CPanel panTermLoan;
    private com.see.truetransact.uicomponent.CPanel panToolBtns;
    private com.see.truetransact.uicomponent.CPanel panTotalSecurity_Value;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccLimit;
    private com.see.truetransact.uicomponent.CRadioButton rdoAccLimit_Main;
    private com.see.truetransact.uicomponent.CRadioButton rdoAccLimit_Submit;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccType;
    private com.see.truetransact.uicomponent.CRadioButton rdoActive_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDoAddSIs;
    private com.see.truetransact.uicomponent.CRadioButton rdoDoAddSIs_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDoAddSIs_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExecuted_DOC;
    private com.see.truetransact.uicomponent.CRadioButton rdoInActive_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterest_Compound;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterest_Simple;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMandatory_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CRadioButton rdoMultiDisburseAllow_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoMultiDisburseAllow_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNatureInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoNatureInterest_NonPLR;
    private com.see.truetransact.uicomponent.CRadioButton rdoNatureInterest_PLR;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo_DocumentDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo_Executed_DOC;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo_Mandatory_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDatedCheque;
    private com.see.truetransact.uicomponent.CRadioButton rdoPostDatedCheque_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPostDatedCheque_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRiskWeight;
    private com.see.truetransact.uicomponent.CRadioButton rdoRiskWeight_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoRiskWeight_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoSecurityDetails_Fully;
    private com.see.truetransact.uicomponent.CRadioButton rdoSecurityDetails_Partly;
    private com.see.truetransact.uicomponent.CRadioButton rdoSecurityDetails_Unsec;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
    private com.see.truetransact.uicomponent.CRadioButton rdoSubsidy_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSubsidy_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes_DocumentDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes_Executed_DOC;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes_Mandatory_DOC;
    private com.see.truetransact.uicomponent.CSeparator sptBorroewrProfile;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptClassDetails;
    private com.see.truetransact.uicomponent.CSeparator sptClassification_vertical;
    private com.see.truetransact.uicomponent.CSeparator sptCompanyDetails;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CSeparator sptFacilityDetails_Vert;
    private com.see.truetransact.uicomponent.CSeparator sptGuarantorDetail_Hori1;
    private com.see.truetransact.uicomponent.CSeparator sptGuarantorDetail_Vert;
    private com.see.truetransact.uicomponent.CSeparator sptInterMaintenance_Hori;
    private com.see.truetransact.uicomponent.CSeparator sptInterMaintenance_Hori2;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CSeparator sptRepatmentSchedule_Hori;
    private com.see.truetransact.uicomponent.CSeparator sptRepatmentSchedule_Vert;
    private com.see.truetransact.uicomponent.CSeparator sptSecurityDetails_Hori;
    private com.see.truetransact.uicomponent.CScrollPane srpBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CSeparator srpComp_Tab_Addr;
    private com.see.truetransact.uicomponent.CScrollPane srpGuarantorTable;
    private com.see.truetransact.uicomponent.CScrollPane srpInterMaintenance;
    private com.see.truetransact.uicomponent.CScrollPane srpRepaymentCTable;
    private com.see.truetransact.uicomponent.CScrollPane srpSecurityTable;
    private com.see.truetransact.uicomponent.CScrollPane srpShareMaintenance;
    private com.see.truetransact.uicomponent.CScrollPane srpTable2_SD;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_DocumentDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_SD;
    private com.see.truetransact.uicomponent.CTabbedPane tabLimitAmount;
    private com.see.truetransact.uicomponent.CTable tblBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CTable tblGuarantorTable;
    private com.see.truetransact.uicomponent.CTable tblInterMaintenance;
    private com.see.truetransact.uicomponent.CTable tblRepaymentCTable;
    private com.see.truetransact.uicomponent.CTable tblSanctionDetails;
    private com.see.truetransact.uicomponent.CTable tblSanctionDetails2;
    private com.see.truetransact.uicomponent.CTable tblSecurityTable;
    private com.see.truetransact.uicomponent.CTable tblShareMaintenance;
    private com.see.truetransact.uicomponent.CTable tblTable_DocumentDetails;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdtAODDate;
    private com.see.truetransact.uicomponent.CDateField tdtATMFromDateAD;
    private com.see.truetransact.uicomponent.CDateField tdtATMToDateAD;
    private com.see.truetransact.uicomponent.CDateField tdtAccountOpenDate;
    private com.see.truetransact.uicomponent.CDateField tdtAsOn;
    private com.see.truetransact.uicomponent.CDateField tdtAsOn_GD;
    private com.see.truetransact.uicomponent.CDateField tdtCredit;
    private com.see.truetransact.uicomponent.CDateField tdtCreditFacilityAvailSince;
    private com.see.truetransact.uicomponent.CDateField tdtCreditFromDateAD;
    private com.see.truetransact.uicomponent.CDateField tdtCreditToDateAD;
    private com.see.truetransact.uicomponent.CDateField tdtDOB_GD;
    private com.see.truetransact.uicomponent.CDateField tdtDateEstablishment;
    private com.see.truetransact.uicomponent.CDateField tdtDealingWithBankSince;
    private com.see.truetransact.uicomponent.CDateField tdtDebit;
    private com.see.truetransact.uicomponent.CDateField tdtDebitFromDateAD;
    private com.see.truetransact.uicomponent.CDateField tdtDebitToDateAD;
    private com.see.truetransact.uicomponent.CDateField tdtDemandPromNoteDate;
    private com.see.truetransact.uicomponent.CDateField tdtDemandPromNoteExpDate;
    private com.see.truetransact.uicomponent.CDateField tdtDisbursement_Dt;
    private com.see.truetransact.uicomponent.CDateField tdtExecuteDate_DOC;
    private com.see.truetransact.uicomponent.CDateField tdtExpiryDate_DOC;
    private com.see.truetransact.uicomponent.CDateField tdtFDate;
    private com.see.truetransact.uicomponent.CDateField tdtFacility_Repay_Date;
    private com.see.truetransact.uicomponent.CDateField tdtFirstInstall;
    private com.see.truetransact.uicomponent.CDateField tdtFrom;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastInstall;
    private com.see.truetransact.uicomponent.CDateField tdtNPAChrgAD;
    private com.see.truetransact.uicomponent.CDateField tdtNPADate;
    private com.see.truetransact.uicomponent.CDateField tdtSanctionDate;
    private com.see.truetransact.uicomponent.CDateField tdtSubmitDate_DocumentDetails;
    private com.see.truetransact.uicomponent.CDateField tdtTDate;
    private com.see.truetransact.uicomponent.CDateField tdtTo;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtABBChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtATMNoAD;
    private com.see.truetransact.uicomponent.CTextField txtAccCloseChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtAccOpeningChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtAcct_Name;
    private com.see.truetransact.uicomponent.CTextField txtAgainstClearingInter;
    private com.see.truetransact.uicomponent.CTextField txtAmtLastInstall;
    private com.see.truetransact.uicomponent.CTextField txtAmtPenulInstall;
    private com.see.truetransact.uicomponent.CTextField txtArea_CompDetail;
    private com.see.truetransact.uicomponent.CTextField txtArea_GD;
    private com.see.truetransact.uicomponent.CTextField txtChequeBookChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtChiefExecutiveName;
    private com.see.truetransact.uicomponent.CTextField txtCompanyRegisNo;
    private com.see.truetransact.uicomponent.CTextField txtContactPerson;
    private com.see.truetransact.uicomponent.CTextField txtContactPhone;
    private com.see.truetransact.uicomponent.CTextField txtCreditNoAD;
    private com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtCustID_Security;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID_GD;
    private com.see.truetransact.uicomponent.CTextField txtDebitNoAD;
    private com.see.truetransact.uicomponent.CTextField txtEligibleLoan;
    private com.see.truetransact.uicomponent.CTextField txtExcessWithChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtFacility_Moratorium_Period;
    private com.see.truetransact.uicomponent.CTextField txtFolioChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtFromAmt;
    private com.see.truetransact.uicomponent.CTextField txtGroupDesc;
    private com.see.truetransact.uicomponent.CTextField txtGuaranAccNo;
    private com.see.truetransact.uicomponent.CTextField txtGuaranName;
    private com.see.truetransact.uicomponent.CTextField txtGuarantorNetWorth;
    private com.see.truetransact.uicomponent.CTextField txtGuarantorNo;
    private com.see.truetransact.uicomponent.CTextField txtInter;
    private com.see.truetransact.uicomponent.CTextField txtInterExpLimit;
    private com.see.truetransact.uicomponent.CTextField txtLaonAmt;
    private com.see.truetransact.uicomponent.CTextField txtLimit_SD;
    private com.see.truetransact.uicomponent.CTextField txtMargin;
    private com.see.truetransact.uicomponent.CTextField txtMinActBalanceAD;
    private com.see.truetransact.uicomponent.CTextField txtMisServiceChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtNetWorth;
    private com.see.truetransact.uicomponent.CTextField txtNoInstall;
    private com.see.truetransact.uicomponent.CTextField txtNoInstallments;
    private com.see.truetransact.uicomponent.CTextField txtNoMonthsMora;
    private com.see.truetransact.uicomponent.CTextField txtPenalInter;
    private com.see.truetransact.uicomponent.CTextField txtPenalStatement;
    private com.see.truetransact.uicomponent.CTextField txtPeriodDifference_Days;
    private com.see.truetransact.uicomponent.CTextField txtPeriodDifference_Months;
    private com.see.truetransact.uicomponent.CTextField txtPeriodDifference_Years;
    private com.see.truetransact.uicomponent.CTextField txtPhone_CompDetail;
    private com.see.truetransact.uicomponent.CTextField txtPhone_GD;
    private com.see.truetransact.uicomponent.CTextField txtPin_CompDetail;
    private com.see.truetransact.uicomponent.CTextField txtPin_GD;
    private com.see.truetransact.uicomponent.CTextField txtPurposeDesc;
    private com.see.truetransact.uicomponent.CTextField txtReferences;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRemarks_DocumentDetails;
    private com.see.truetransact.uicomponent.CTextField txtRemarks__CompDetail;
    private com.see.truetransact.uicomponent.CTextField txtRepayScheduleMode;
    private com.see.truetransact.uicomponent.CTextField txtRiskRating;
    private com.see.truetransact.uicomponent.CTextField txtSanctionNo;
    private com.see.truetransact.uicomponent.CTextField txtSanctionRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSanctionSlNo;
    private com.see.truetransact.uicomponent.CTextField txtScheduleNo;
    private com.see.truetransact.uicomponent.CTextField txtSecurityNo;
    private com.see.truetransact.uicomponent.CTextField txtSecurityValue;
    private com.see.truetransact.uicomponent.CTextField txtStreet_CompDetail;
    private com.see.truetransact.uicomponent.CTextField txtStreet_GD;
    private com.see.truetransact.uicomponent.CTextField txtToAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalBaseAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalInstallAmt;
    // End of variables declaration//GEN-END:variables
    
    
}
