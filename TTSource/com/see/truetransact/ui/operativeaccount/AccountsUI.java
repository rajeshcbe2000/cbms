/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AccountsUI.java
 *
 * Created on August 6, 2003, 10:50 AM
 */
package com.see.truetransact.ui.operativeaccount;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JComponent;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.ui.common.authorizestatus.*;
import com.see.truetransact.ui.common.powerofattorney.*;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorizedsignatory.*;
import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.AcctSearchUI;
import com.see.truetransact.ui.termloan.customerDetailsScreen.CustomerDetailsScreenUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.customer.IndividualCustUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author annamalai_t1
 * @author Pranav
 * @author Rahul
 * @author 152721
 * @author K.R.Jayakrishnan May 31, 2004
 */
public class AccountsUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    //    private AccountsRB resourceBundle = new AccountsRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.AccountsRB", ProxyParameters.LANGUAGE);
    AccountsMRB objMandatoryRB = new AccountsMRB();
    private AccountsOB observable;
    private final static Logger log = Logger.getLogger(AccountsUI.class);
    private String viewType = "";
    private int yes = 0;
    private int no = 1;
    private String poaId;
    private ArrayList selectedNomineeData;
    private ArrayList selectedPoAData;
    private HashMap mandatoryMap;
    private HashMap acountParamMap = new HashMap();
    private HashMap acountCardsMap;
    private HashMap acountChargesMap;
    private HashMap acountCreditMap;
    private HashMap transIdMap;
    private HashMap transTypeMap;
    private HashMap editTransMap;
    private boolean dontShowJointDialog = false;
    final int DELETE = 1, AUTHORIZE = 2;
    String minNominee = "";
    int updateTab = -1;
    final String SCREEN = "OA";
    private boolean transNew = true;
    private boolean finalChecking = false;
    private Date currDt = null;
    private int rejectFlag = 0;
    private boolean updateMode = false;
    IndividualCustUI individualCustUI;
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    String sbNode = "";
    /**
     * Declare a new instance of the NomineeUI and IntroducerUI...
     */
    NomineeUI nomineeUi = new NomineeUI(SCREEN, false);
    PowerOfAttorneyUI poaUI = new PowerOfAttorneyUI(SCREEN);
    AuthorizedSignatoryUI authSignUI = new AuthorizedSignatoryUI(SCREEN);
    private AcctSearchUI acctsearch = null;
    private boolean memberUpdateMode = false; // nithya
    int memberUpdateTab = -1;
    private LinkedHashMap memberTypeMap;
    private LinkedHashMap deletedMemberTypeMap;
    boolean sbODRenewal = false;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    //private String ACCOUNTNO;

    /**
     * Creates new form AccountsUI
     */
    public AccountsUI() {
        // first generate the controls
        initComponents();
        initStartup();
    }

    private void initStartup() {
        // then set the names for the controls using setName()
        setFieldNames();
        /* call the intenationalize() method to load the RB values
         * and initialize the Observable for this class
         */
        internationalize();
        setMandatoryHashMap();
        setObservable();
//        cboAgentId.setModel(observable.getCbmagentID());
        /* Fill up all the combo boxes and set up the initial values for
         * the radio buttons
         */
        initComponentData();
        
        panBorrowerSalaryDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Borrower Salary Details")); // nithya
        panSuretyDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Surety Details")); // nithya
        panServiceDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Service Details")); // nithya
        //panShowSurety.setBorder(javax.swing.BorderFactory.createTitledBorder("Surety Members")); // nithya
        panBorrowerServiceDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Borrower Service Details")); // nithya
        // Disable all the screen
        ClientUtil.enableDisable(this, false);
        // Some other fields should also be disabled at init time
        enableDisableComponents(false);
        /*
         * To Clear all the fields and Labels...
         */
        observable.resetCustDetails();
        observable.resetJntAccntHoldTbl();
        //observable.resetMemberTypeTbl(); //Added by nithya
        observable.resetOBFields();
        observable.resetLabels();
        setupMenuToolBarPanel();
        setHelpMessage();
        setMaxLenths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panActInfo);
        //Hide currency..comboBox
        this.lblBaseCurrAI.setVisible(false);
        this.cboBaseCurrAI.setVisible(false);
        //--- Disables the Joint account holder Buttons
        setBtnJointAccnt(false);
        accountOpeningAmountEnable(false);
        /**
         * To add the Nominee Tab and Introduce Tab in the AccountUI...
         */
        tabAccounts.add(nomineeUi, "Nominee", 2);
        nomineeUi.disableNewButton(false);
        tabAccounts.add(poaUI, "Power Of Attorney", 1);
        poaUI.setPoAToolBtnsEnableDisable(false);
        tabAccounts.add(authSignUI, "Authorized Signatory", 3);
        authSignUI.setAuthEnableDisable(false);
        authSignUI.setAllAuthInstEnableDisable(false);
        setEnableDateFields(false);
        //__ To reset the tabs visited...
        tabAccounts.resetVisits();
        txtClosedDt.setVisible(false);
        lblClosedDt.setVisible(false);
        panExistingCustomer.setVisible(false);
        lblExistingCustomer.setVisible(false);
        txtExistingAcctNo.setVisible(false);
        lblExistingAcctNo.setVisible(false);
        txtEditOperativeNo.setEnabled(true);
        currDt = ClientUtil.getCurrentDate();
        panCustDet.setVisible(false);
        lblSettlementModeAI.setVisible(false);
        cboSettlementModeAI.setVisible(false);
        txtNextAccNo.setText("");
        tabAccounts.remove(panATMCardDetails);      //Added By Suresh R
        setBtnCard(false);
        lblATMCardNoVal.setText("");
        panSecurityDetails.setVisible(false);// nithya 
        tabAccounts.remove(panSecurityDetails); // nithya
        tblMemberType.setModel(observable.getTblMemberTypeDetails()); // nithya
        chkEnhance.setEnabled(false);      
        //Added By Revathi.L
        btnAgentID.setEnabled(false);
        btnDealerID.setEnabled(false);
        rdoKYCNormsNo.setActionCommand(null);
        txtLinkingActNum.setEditable(false);
        rdoKYCNormsNoActionPerformed(null);
        btnLinkingActNum.setEnabled(true);
    }
    
    private void setBtnCard(boolean val){
        btnATMNew.setEnabled(val);
        btnATMSave.setEnabled(val);
        btnATMDelete.setEnabled(val);
    }

    private void setBtnJointAccnt(boolean val) {
        btnCustDetNew.setEnabled(val);
        btnCustDetDelete.setEnabled(val);
        btnToMain.setEnabled(val);
    }
    
    private void setBtnATM(boolean val) {
        btnCustDetNew.setEnabled(val);
        btnCustDetDelete.setEnabled(val);
        btnToMain.setEnabled(val);
    }

    /* this method will be called to setup the screen for "Transfer In" account
     */
    private void setupComponentsForTransferIn() {
        cboActStatusAI.setSelectedItem(
                ((ComboBoxModel) cboActStatusAI.getModel()).getDataForKey(
                "TRANSFER_IN"));
        cboActStatusAI.setEnabled(false);
        dtdOpeningDateAI.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        dtdOpeningDateAI.setEnabled(false);
        dtdActOpenDateAI.setEnabled(false);
        updateOBFields();
    }

    /* this method will be called to setup the screen for "Transfer In" account
     */
    private void setupComponentsForNew() {
        cboActStatusAI.setSelectedItem(
                ((ComboBoxModel) cboActStatusAI.getModel()).getDataForKey("NEW"));
        cboActStatusAI.setEnabled(false);
        dtdOpeningDateAI.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        dtdOpeningDateAI.setEnabled(false);
    }

    /* now setup the menu, as it should be based on the current operation,
     * which at startup is "NOOP"
     */
    private void setupMenuToolBarPanel() {

        /* disable the previous account number text/label, we don;t require
         * them anymore
         */
        lblPrevActNumAI.setVisible(false);
        txtPrevActNumAI.setVisible(false);

        if (observable.getOperation() == ClientConstants.ACTIONTYPE_CANCEL) {

            // Toolbar -->
            // Disabled
            btnCancel.setEnabled(false);
            btnSave.setEnabled(false);

            // Enabled
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(true);
            btnClose.setEnabled(true);
            btnDelete.setEnabled(true);
            btnView.setEnabled(true);

            // Menu -->
            // Disabled
            mitCancel.setEnabled(false);
            mitResume.setEnabled(false);
            mitSave.setEnabled(false);

            // Enabled
            mitAddNew.setEnabled(true);
            mitAddTransferIn.setEnabled(true);
            mitAuthorize.setEnabled(true);
            mitEditNew.setEnabled(true);
            mitEditTransferIn.setEnabled(true);
            mitClose.setEnabled(true);
            mitDelete.setEnabled(true);

            // Introducer's panel
            // This will only happen  when the user selects the TransferIn option
            panTransferBranchInfo.setVisible(false);

        } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW
                || observable.getOperation() == ClientConstants.ACTIONTYPE_NEWTI
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EDITTI
                || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) {
            // Toolbar -->
            // Disabled
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);

            // Enabled
            btnCancel.setEnabled(true);
            btnClose.setEnabled(true);
            btnSave.setEnabled(true);

            // Menu -->
            // Disabled
            mitAddNew.setEnabled(false);
            mitAddTransferIn.setEnabled(false);
            mitAuthorize.setEnabled(false);
            mitEditNew.setEnabled(false);
            mitEditTransferIn.setEnabled(false);
            mitDelete.setEnabled(false);

            // Enabled
            mitCancel.setEnabled(true);
            mitClose.setEnabled(true);
            mitResume.setEnabled(true);
            mitSave.setEnabled(true);

            // Introducer's panel
            // This will only happen  when the user selects the TransferIn option
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEWTI
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
                panTransferBranchInfo.setVisible(true);
            }

            // Account Details, Card information panel
            /*txtATMNoAD.setEnabled(false);
             dtdATMToDateAD.setEnabled(false);
             txtCreditNoAD.setEnabled(false);
             dtdCreditToDateAD.setEnabled(false);
             txtDebitNoAD.setEnabled(false);
             dtdDebitToDateAD.setEnabled(false);*/


            /*
             * Enabling and Disabling is already handled in the Chech Box Events...
             */
            /*txtATMNoAD.setEnabled(true);
             dtdATMFromDateAD.setEnabled(true);
             dtdATMToDateAD.setEnabled(true);
             txtCreditNoAD.setEnabled(true);
             dtdCreditFromDateAD.setEnabled(true);
             dtdCreditToDateAD.setEnabled(true);
             txtDebitNoAD.setEnabled(true);
             dtdDebitFromDateAD.setEnabled(true);
             dtdDebitToDateAD.setEnabled(true);*/


            /*
             * Enabling and Disabling is already handled in the Chech Box Events...
             */
            // Account Details, Flexi option panel
            /*txtMinBal1FlexiAD.setEnabled(false);
             txtMinBal2FlexiAD.setEnabled(false);
             txtReqFlexiPeriodAD.setEnabled(false);
             cboDMYAD.setEnabled(false);*/

            // Account Details, Different charges panel
            /*
             * Enabling and Disabling is already handled in the Chech Box Events...
             */
            /*txtMinActBalanceAD.setEnabled(false);
             txtABBChrgAD.setEnabled(false);
             dtdNPAChrgAD.setEnabled(false);*/

            // Nominee, Guardian panel
            //            ClientUtil.enableDisable(panGuardianDetails, false);
            //            addressPanelGuardianNO.setEnabled(false);

        }
    }

    /* Fill up all the combo boxes and set up the initial values for
     * the radio buttons
     */
    private void initComponentData() {
        // fill all the combo boxes, with the data from lookup table, and others
        cboProductIdAI.setModel(observable.getCbmProductIdAI());
        cboLinkingProductId.setModel(observable.getCbmLinkingProductId());
        cboActStatusAI.setModel(observable.getCbmActStatusAI());
        cboConstitutionAI.setModel(observable.getCbmConstitutionAI());
        cboOpModeAI.setModel(observable.getCbmOpModeAI());
               cboCommAddr.setModel(observable.getCbmCommAddr());
        cboGroupCodeAI.setModel(observable.getCbmGroupCodeAI());
        cboSettlementModeAI.setModel(observable.getCbmSettlementModeAI());
        cboCategory.setModel(observable.getCbmCategory());
        //        cboBaseCurrAI.setModel(observable.getCbmBaseCurrAI());
        //        cboDMYAD.setModel(observable.getCbmDMYAD());
        cboStmtFreqAD.setModel(observable.getCbmStmtFreqAD());
        cboPreviousActNo.setModel(observable.getCbmPrevAcctNo());
        cboRoleHierarchy.setModel(observable.getCbmRoleHierarchy());
        tblATMCardDetails.setModel(observable.getTblATMCardDetails());
		//Added by Sreekrishnan
//        cboIntroducer.setModel(observable.getCbmIntroducer());
    }

    // Some other fields should also be disabled at init time
    private void enableDisableComponents(boolean truefalse) {
        /* The tables/buttons in the nominee and PoA panels should also
         * be disabled to start with
         */
        //        tblPOAListPA.setEnabled(truefalse);
        // disable the button for the customerID, in the Account Details tab
        if ((observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE)
                && (observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE)
                && (observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT)
                && (observable.getOperation() != ClientConstants.ACTIONTYPE_EXCEPTION)) {

            btnAccNum.setEnabled(truefalse);
        }
        cboActStatusAI.setEnabled(false);
    }

    // Added by nithya
    private void setSuretyComponents(){
        txtBorrowerSalary.setEnabled(true);
        txtBorrowerSalary.setAllowAll(true);
        txtBorrowerSalary.setAllowNumber(true);
        //txtBorrowerSalary.setValidation(new CurrencyValidation(14, 2));
        txtBorrowerNetworth.setEnabled(false);
        txtBorrowerNetworth.setAllowAll(true);
        txtBorrowerNetworth.setAllowNumber(true);
        //txtBorrowerNetworth.setValidation(new CurrencyValidation(14, 2));
        txtMemberNo.setEnabled(false);
        txtMemberNo.setAllowAll(true);
        txtMemberNo.setAllowNumber(true);
        txtMemberName.setEnabled(false);
        txtMemberType.setEnabled(false);
        txtMemberSalary.setEnabled(true);
        txtMemberSalary.setAllowAll(true);
        txtMemberSalary.setAllowNumber(true);
        txtMemberSalary.setValidation(new CurrencyValidation(14, 2));
        txtMemberNetworth.setEnabled(true);
        txtMemberNetworth.setAllowAll(true);
        txtMemberNetworth.setAllowNumber(true); 
        //txtMemberNetworth.setValidation(new CurrencyValidation(14, 2));
        txtContactNo.setEnabled(true);
        txtContactNo.setAllowAll(true);
        txtContactNo.setAllowNumber(true);   
        chkRenew.setEnabled(false);
        txtAppliedAmt.setEnabled(true);
        txtAppliedAmt.setAllowAll(true);
        txtAppliedAmt.setAllowNumber(true); 
        //btnGetODLimit.setEnabled(false);//Commented By Kannan AR Ref. Mr.Prasanth
    }
    
    private void setObservable() {
        try {
            observable = new AccountsOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMaxLenths() {
        txtODLimitAI.setMaxLength(16);

        txtODLimitAI.setValidation(new CurrencyValidation(8, 2));

        txtATMNoAD.setValidation(new NumericValidation());

        txtDebitNoAD.setMaxLength(16);
        txtDebitNoAD.setValidation(new NumericValidation());

        txtCreditNoAD.setMaxLength(16);
        txtCreditNoAD.setValidation(new NumericValidation());

        txtABBChrgAD.setValidation(new CurrencyValidation(14, 2));
        txtAcctOpeningAmount.setValidation(new CurrencyValidation(14, 2));
        txtMinBal1FlexiAD.setMaxLength(16);
        txtMinBal1FlexiAD.setValidation(new CurrencyValidation(8, 2));

        txtMinBal2FlexiAD.setMaxLength(16);
        txtMinBal2FlexiAD.setValidation(new CurrencyValidation(8, 2));

        txtReqFlexiPeriodAD.setMaxLength(16); //10
        txtReqFlexiPeriodAD.setValidation(new NumericValidation());

        txtAccOpeningChrgAD.setValidation(new CurrencyValidation(14, 2));

        txtMinActBalanceAD.setValidation(new CurrencyValidation(14, 2));

        txtChequeBookChrgAD.setValidation(new CurrencyValidation(14, 2));

        txtMisServiceChrgAD.setValidation(new CurrencyValidation(14, 2));

        txtFolioChrgAD.setValidation(new CurrencyValidation(14, 2));

        txtAccCloseChrgAD.setValidation(new CurrencyValidation(14, 2));

        txtExcessWithChrgAD.setValidation(new CurrencyValidation(14, 2));

        //txtActName.setMaxLength(32);
        txtRemarks.setMaxLength(64);
        txtEditOperativeNo.setAllowAll(true);
        txtAccountNo.setMaxLength(20);
        txtAccountNo.setAllowAll(true);
        txtExistingAcctNo.setAllowAll(true);
        txtMobileNo.setMaxLength(10);
        txtMobileNo.setValidation(new NumericValidation());
//        txtCardActNumber.setValidation(new NumericValidation()); //Added By Suresh
        txtCardActNumber.setMaxLength(36);
        txtCardActNumber.setAllowAll(true);
        txtAtmCardLimit.setMaxLength(16); //10
        txtAtmCardLimit.setValidation(new CurrencyValidation(12, 2));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgExistingCustomer = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgKYCNormsFollowed = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccounts = new com.see.truetransact.uicomponent.CPanel();
        tabAccounts = new com.see.truetransact.uicomponent.CTabbedPane();
        panActInfo = new com.see.truetransact.uicomponent.CPanel();
        panTransferBranchInfo = new com.see.truetransact.uicomponent.CPanel();
        panBranch = new com.see.truetransact.uicomponent.CPanel();
        lblBranchCodeAI = new com.see.truetransact.uicomponent.CLabel();
        txtBranchCodeAI = new com.see.truetransact.uicomponent.CTextField();
        lblBranchNameAI = new com.see.truetransact.uicomponent.CLabel();
        lblBranchNameValueAI = new com.see.truetransact.uicomponent.CLabel();
        lblPrevActNoAI = new com.see.truetransact.uicomponent.CLabel();
        btnBranchCode = new com.see.truetransact.uicomponent.CButton();
        cboPreviousActNo = new com.see.truetransact.uicomponent.CComboBox();
        panTransfered = new com.see.truetransact.uicomponent.CPanel();
        lblActOpenDateAI = new com.see.truetransact.uicomponent.CLabel();
        dtdActOpenDateAI = new com.see.truetransact.uicomponent.CDateField();
        txtAmoutTransAI = new com.see.truetransact.uicomponent.CTextField();
        lblAmoutTransAI = new com.see.truetransact.uicomponent.CLabel();
        lblRemarksAI = new com.see.truetransact.uicomponent.CLabel();
        txtRemarksAI = new com.see.truetransact.uicomponent.CTextField();
        panInsideAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        lblProductIdAI = new com.see.truetransact.uicomponent.CLabel();
        cboProductIdAI = new com.see.truetransact.uicomponent.CComboBox();
        lblActHeadAI = new com.see.truetransact.uicomponent.CLabel();
        lblActHeadValueAI = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdAI = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerIdAI = new com.see.truetransact.uicomponent.CTextField();
        btnAccNum = new com.see.truetransact.uicomponent.CButton();
        lblOpeningDateAI = new com.see.truetransact.uicomponent.CLabel();
        dtdOpeningDateAI = new com.see.truetransact.uicomponent.CDateField();
        lblActStatusAI = new com.see.truetransact.uicomponent.CLabel();
        cboActStatusAI = new com.see.truetransact.uicomponent.CComboBox();
        lblConstitutionAI = new com.see.truetransact.uicomponent.CLabel();
        cboConstitutionAI = new com.see.truetransact.uicomponent.CComboBox();
        lblOpModeAI = new com.see.truetransact.uicomponent.CLabel();
        cboOpModeAI = new com.see.truetransact.uicomponent.CComboBox();
        lblCommAddr = new com.see.truetransact.uicomponent.CLabel();
        cboCommAddr = new com.see.truetransact.uicomponent.CComboBox();
        lblODLimitAI = new com.see.truetransact.uicomponent.CLabel();
        txtODLimitAI = new com.see.truetransact.uicomponent.CTextField();
        lblGroupCodeAI = new com.see.truetransact.uicomponent.CLabel();
        cboGroupCodeAI = new com.see.truetransact.uicomponent.CComboBox();
        lblSettlementModeAI = new com.see.truetransact.uicomponent.CLabel();
        cboSettlementModeAI = new com.see.truetransact.uicomponent.CComboBox();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblPrevActNumAI = new com.see.truetransact.uicomponent.CLabel();
        txtPrevActNumAI = new com.see.truetransact.uicomponent.CTextField();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblActName = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtClosedDt = new com.see.truetransact.uicomponent.CTextField();
        lblClosedDt = new com.see.truetransact.uicomponent.CLabel();
        txtActName = new com.see.truetransact.uicomponent.CTextField();
        panExistingCustomer = new com.see.truetransact.uicomponent.CPanel();
        rdoExistingCustomer_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExistingCustomer_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtExistingAcctNo = new com.see.truetransact.uicomponent.CTextField();
        lblExistingAcctNo = new com.see.truetransact.uicomponent.CLabel();
        lblExistingCustomer = new com.see.truetransact.uicomponent.CLabel();
        txtAcctOpeningAmount = new com.see.truetransact.uicomponent.CTextField();
        lblAcctOpeningAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        cboConstitutionAI1 = new com.see.truetransact.uicomponent.CComboBox();
        btnCustomerIdAI = new com.see.truetransact.uicomponent.CButton();
        lblBaseCurrAI = new com.see.truetransact.uicomponent.CLabel();
        cboBaseCurrAI = new com.see.truetransact.uicomponent.CComboBox();
        chkPrimaryAccount = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtUPIMobileNo = new com.see.truetransact.uicomponent.CTextField();
        lblUPIMobileno = new com.see.truetransact.uicomponent.CLabel();
        panCustomerSide1 = new com.see.truetransact.uicomponent.CPanel();
        jPanel2 = new com.see.truetransact.uicomponent.CPanel();
        jLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtNextAccNo = new com.see.truetransact.uicomponent.CTextField();
        panCustomerInfo = new com.see.truetransact.uicomponent.CPanel();
        panCustOperation = new com.see.truetransact.uicomponent.CPanel();
        btnCustDetDelete = new com.see.truetransact.uicomponent.CButton();
        btnCustDetNew = new com.see.truetransact.uicomponent.CButton();
        btnToMain = new com.see.truetransact.uicomponent.CButton();
        panCustDet = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblCustValue = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblCityValue = new com.see.truetransact.uicomponent.CLabel();
        lblDOB = new com.see.truetransact.uicomponent.CLabel();
        lblDOBValue = new com.see.truetransact.uicomponent.CLabel();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        lblCountryValue = new com.see.truetransact.uicomponent.CLabel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblStreetValue = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblStateValue = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblAreaValue = new com.see.truetransact.uicomponent.CLabel();
        lblPin = new com.see.truetransact.uicomponent.CLabel();
        lblPinValue = new com.see.truetransact.uicomponent.CLabel();
        lblMinOrMaj = new com.see.truetransact.uicomponent.CLabel();
        panInsideCustTable = new com.see.truetransact.uicomponent.CPanel();
        srpAct_Joint = new com.see.truetransact.uicomponent.CScrollPane();
        tblAct_Joint = new com.see.truetransact.uicomponent.CTable();
        jPanel3 = new com.see.truetransact.uicomponent.CPanel();
        lblAgentId = new com.see.truetransact.uicomponent.CLabel();
        lblDealer = new com.see.truetransact.uicomponent.CLabel();
        panAgentID = new com.see.truetransact.uicomponent.CPanel();
        txtDealerID = new com.see.truetransact.uicomponent.CTextField();
        btnDealerID = new com.see.truetransact.uicomponent.CButton();
        panAgentID1 = new com.see.truetransact.uicomponent.CPanel();
        txtAgentID = new com.see.truetransact.uicomponent.CTextField();
        btnAgentID = new com.see.truetransact.uicomponent.CButton();
        lblDealerIDVal = new com.see.truetransact.uicomponent.CLabel();
        lblAgentIDVal = new com.see.truetransact.uicomponent.CLabel();
        jPanel4 = new com.see.truetransact.uicomponent.CPanel();
        lblLinkingProductId = new com.see.truetransact.uicomponent.CLabel();
        lblLinkingActNum = new com.see.truetransact.uicomponent.CLabel();
        panLinkingActNum = new com.see.truetransact.uicomponent.CPanel();
        txtLinkingActNum = new com.see.truetransact.uicomponent.CTextField();
        btnLinkingActNum = new com.see.truetransact.uicomponent.CButton();
        cboLinkingProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblLinkingActNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblCardActNumber = new com.see.truetransact.uicomponent.CLabel();
        txtCardActNumber = new com.see.truetransact.uicomponent.CTextField();
        lblGender = new com.see.truetransact.uicomponent.CLabel();
        panGender = new com.see.truetransact.uicomponent.CPanel();
        rdoKYCNormsYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoKYCNormsNo = new com.see.truetransact.uicomponent.CRadioButton();
        txtAtmCardLimit = new com.see.truetransact.uicomponent.CTextField();
        lblAtmCardLimit = new com.see.truetransact.uicomponent.CLabel();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        panIsRequired = new com.see.truetransact.uicomponent.CPanel();
        chkChequeBookAD = new com.see.truetransact.uicomponent.CCheckBox();
        chkCustGrpLimitValidationAD = new com.see.truetransact.uicomponent.CCheckBox();
        chkNROStatusAD = new com.see.truetransact.uicomponent.CCheckBox();
        panCardInfo = new com.see.truetransact.uicomponent.CPanel();
        chkATMAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblATMNoAD = new com.see.truetransact.uicomponent.CLabel();
        txtATMNoAD = new com.see.truetransact.uicomponent.CTextField();
        lblATMFromDateAD = new com.see.truetransact.uicomponent.CLabel();
        dtdATMFromDateAD = new com.see.truetransact.uicomponent.CDateField();
        lblATMToDateAD = new com.see.truetransact.uicomponent.CLabel();
        dtdATMToDateAD = new com.see.truetransact.uicomponent.CDateField();
        chkDebitAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblDebitNoAD = new com.see.truetransact.uicomponent.CLabel();
        txtDebitNoAD = new com.see.truetransact.uicomponent.CTextField();
        lblDebitFromDateAD = new com.see.truetransact.uicomponent.CLabel();
        dtdDebitFromDateAD = new com.see.truetransact.uicomponent.CDateField();
        lblDebitToDateAD = new com.see.truetransact.uicomponent.CLabel();
        dtdDebitToDateAD = new com.see.truetransact.uicomponent.CDateField();
        chkCreditAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblCreditNoAD = new com.see.truetransact.uicomponent.CLabel();
        txtCreditNoAD = new com.see.truetransact.uicomponent.CTextField();
        lblCreditFromDateAD = new com.see.truetransact.uicomponent.CLabel();
        dtdCreditFromDateAD = new com.see.truetransact.uicomponent.CDateField();
        lblCreditToDateAD = new com.see.truetransact.uicomponent.CLabel();
        dtdCreditToDateAD = new com.see.truetransact.uicomponent.CDateField();
        panFlexiOpt = new com.see.truetransact.uicomponent.CPanel();
        chkFlexiAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblMinBal1FlexiAD = new com.see.truetransact.uicomponent.CLabel();
        txtMinBal1FlexiAD = new com.see.truetransact.uicomponent.CTextField();
        lblMinBal2FlexiAD = new com.see.truetransact.uicomponent.CLabel();
        txtMinBal2FlexiAD = new com.see.truetransact.uicomponent.CTextField();
        lblReqFlexiPeriodAD = new com.see.truetransact.uicomponent.CLabel();
        txtReqFlexiPeriodAD = new com.see.truetransact.uicomponent.CTextField();
        lblReqFlexiPeriodAD1 = new com.see.truetransact.uicomponent.CLabel();
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
        dtdNPAChrgAD = new com.see.truetransact.uicomponent.CDateField();
        lblMinActBalanceAD = new com.see.truetransact.uicomponent.CLabel();
        txtMinActBalanceAD = new com.see.truetransact.uicomponent.CTextField();
        lblStopPayment = new com.see.truetransact.uicomponent.CLabel();
        lblChequeReturn = new com.see.truetransact.uicomponent.CLabel();
        lblCollectInoperative = new com.see.truetransact.uicomponent.CLabel();
        lblStatement = new com.see.truetransact.uicomponent.CLabel();
        lblNonMaintenance = new com.see.truetransact.uicomponent.CLabel();
        lblABB = new com.see.truetransact.uicomponent.CLabel();
        lblNPA = new com.see.truetransact.uicomponent.CLabel();
        lblHideBalanceTrans = new com.see.truetransact.uicomponent.CLabel();
        chkHideBalanceTrans = new com.see.truetransact.uicomponent.CCheckBox();
        lblRoleHierarchy = new com.see.truetransact.uicomponent.CLabel();
        cboRoleHierarchy = new com.see.truetransact.uicomponent.CComboBox();
        lblPassBook = new com.see.truetransact.uicomponent.CLabel();
        chkPassBook = new com.see.truetransact.uicomponent.CCheckBox();
        panLastIntApp = new com.see.truetransact.uicomponent.CPanel();
        lblDebit = new com.see.truetransact.uicomponent.CLabel();
        dtdDebit = new com.see.truetransact.uicomponent.CDateField();
        lblCredit = new com.see.truetransact.uicomponent.CLabel();
        dtdCredit = new com.see.truetransact.uicomponent.CDateField();
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
        panMobileBanking = new com.see.truetransact.uicomponent.CPanel();
        chkMobileBankingAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblMobileNo = new com.see.truetransact.uicomponent.CLabel();
        txtMobileNo = new com.see.truetransact.uicomponent.CTextField();
        tdtMobileSubscribedFrom = new com.see.truetransact.uicomponent.CDateField();
        lblMobileSubscribedFrom = new com.see.truetransact.uicomponent.CLabel();
        panATMCardDetails = new com.see.truetransact.uicomponent.CPanel();
        panATMAccountCardDetails = new com.see.truetransact.uicomponent.CPanel();
        lblATMCardNo = new com.see.truetransact.uicomponent.CLabel();
        panATMCARDBtn = new com.see.truetransact.uicomponent.CPanel();
        btnATMNew = new com.see.truetransact.uicomponent.CButton();
        btnATMSave = new com.see.truetransact.uicomponent.CButton();
        btnATMDelete = new com.see.truetransact.uicomponent.CButton();
        lblATMCardNoVal = new com.see.truetransact.uicomponent.CLabel();
        panAction = new com.see.truetransact.uicomponent.CPanel();
        rdoActionStop = new com.see.truetransact.uicomponent.CRadioButton();
        rdoActionRevoke = new com.see.truetransact.uicomponent.CRadioButton();
        lblExistingCustomer1 = new com.see.truetransact.uicomponent.CLabel();
        lblActionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtActionDt = new com.see.truetransact.uicomponent.CDateField();
        srpCardRemarks = new com.see.truetransact.uicomponent.CScrollPane();
        txtCardRemarks = new com.see.truetransact.uicomponent.CTextArea();
        lblREmarks = new com.see.truetransact.uicomponent.CLabel();
        panATMCardTableDetails = new com.see.truetransact.uicomponent.CPanel();
        srpATMCardTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblATMCardDetails = new com.see.truetransact.uicomponent.CTable();
        panSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        panBorrowerSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBorrowerSalary = new com.see.truetransact.uicomponent.CLabel();
        txtBorrowerSalary = new com.see.truetransact.uicomponent.CTextField();
        lblBorrowerNetworth = new com.see.truetransact.uicomponent.CLabel();
        txtBorrowerNetworth = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtAppliedAmt = new com.see.truetransact.uicomponent.CTextField();
        lblEligAmt = new com.see.truetransact.uicomponent.CLabel();
        lblBorrEligAmt = new com.see.truetransact.uicomponent.CLabel();
        panSuretyDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberType = new com.see.truetransact.uicomponent.CLabel();
        lblContactNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemberSalary = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNetworth = new com.see.truetransact.uicomponent.CLabel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        txtMemberName = new com.see.truetransact.uicomponent.CTextField();
        txtMemberType = new com.see.truetransact.uicomponent.CTextField();
        txtContactNo = new com.see.truetransact.uicomponent.CTextField();
        txtMemberSalary = new com.see.truetransact.uicomponent.CTextField();
        txtMemberNetworth = new com.see.truetransact.uicomponent.CTextField();
        btnSecurityNew = new com.see.truetransact.uicomponent.CButton();
        btnSecuritySave = new com.see.truetransact.uicomponent.CButton();
        btnSecurityDelete = new com.see.truetransact.uicomponent.CButton();
        btnMemberSearch = new com.see.truetransact.uicomponent.CButton();
        btnGetODLimit = new com.see.truetransact.uicomponent.CButton();
        panShowSurety = new com.see.truetransact.uicomponent.CPanel();
        srpShowSurety = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberType = new com.see.truetransact.uicomponent.CTable();
        panServiceDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDob = new com.see.truetransact.uicomponent.CLabel();
        lblRetireDt = new com.see.truetransact.uicomponent.CLabel();
        lblService = new com.see.truetransact.uicomponent.CLabel();
        panBorrowerServiceDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBorrowerDob = new com.see.truetransact.uicomponent.CLabel();
        lblBorrowerRetireDt = new com.see.truetransact.uicomponent.CLabel();
        lblBorrowerService = new com.see.truetransact.uicomponent.CLabel();
        chkRenew = new com.see.truetransact.uicomponent.CCheckBox();
        chkEnhance = new com.see.truetransact.uicomponent.CCheckBox();
        chkODClose = new com.see.truetransact.uicomponent.CCheckBox();
        tbrAccounts = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblTBSep4 = new javax.swing.JLabel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblTBSep1 = new javax.swing.JLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblTBSep2 = new javax.swing.JLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblTBSep3 = new com.see.truetransact.uicomponent.CLabel();
        btnReport = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnDeletedDetails = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        lblPanNumber1 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        txtEditOperativeNo = new com.see.truetransact.uicomponent.CTextField();
        lblSpace9 = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTransfer = new javax.swing.JMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mnuAdd = new javax.swing.JMenu();
        mitAddNew = new javax.swing.JMenuItem();
        sptAdd = new javax.swing.JSeparator();
        mitAddTransferIn = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mitEditNew = new javax.swing.JMenuItem();
        sptEdit = new javax.swing.JSeparator();
        mitEditTransferIn = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess1 = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        sptProcess2 = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();
        mnuAction = new javax.swing.JMenu();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptAction = new javax.swing.JSeparator();
        mitResume = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(900, 680));
        setPreferredSize(new java.awt.Dimension(900, 680));

        panAccounts.setLayout(new java.awt.GridBagLayout());

        tabAccounts.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabAccountsFocusLost(evt);
            }
        });

        panActInfo.setMinimumSize(new java.awt.Dimension(828, 660));
        panActInfo.setPreferredSize(new java.awt.Dimension(718, 660));
        panActInfo.setLayout(new java.awt.GridBagLayout());

        panTransferBranchInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Transfering Branch Info."));
        panTransferBranchInfo.setMinimumSize(new java.awt.Dimension(667, 100));
        panTransferBranchInfo.setPreferredSize(new java.awt.Dimension(667, 100));
        panTransferBranchInfo.setLayout(new java.awt.GridBagLayout());

        panBranch.setLayout(new java.awt.GridBagLayout());

        lblBranchCodeAI.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBranch.add(lblBranchCodeAI, gridBagConstraints);

        txtBranchCodeAI.setMaxLength(8);
        txtBranchCodeAI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCodeAI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBranchCodeAIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBranch.add(txtBranchCodeAI, gridBagConstraints);

        lblBranchNameAI.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBranch.add(lblBranchNameAI, gridBagConstraints);

        lblBranchNameValueAI.setText("M G ROAD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBranch.add(lblBranchNameValueAI, gridBagConstraints);

        lblPrevActNoAI.setText("Previous Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBranch.add(lblPrevActNoAI, gridBagConstraints);

        btnBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchCode.setToolTipText("Branch Code");
        btnBranchCode.setPreferredSize(new java.awt.Dimension(25, 25));
        btnBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchCodeActionPerformed(evt);
            }
        });
        panBranch.add(btnBranchCode, new java.awt.GridBagConstraints());

        cboPreviousActNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPreviousActNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranch.add(cboPreviousActNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransferBranchInfo.add(panBranch, gridBagConstraints);

        panTransfered.setLayout(new java.awt.GridBagLayout());

        lblActOpenDateAI.setText("Account Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransfered.add(lblActOpenDateAI, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransfered.add(dtdActOpenDateAI, gridBagConstraints);

        txtAmoutTransAI.setEditable(false);
        txtAmoutTransAI.setMaxLength(16);
        txtAmoutTransAI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmoutTransAI.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransfered.add(txtAmoutTransAI, gridBagConstraints);

        lblAmoutTransAI.setText("Amount Transfered");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransfered.add(lblAmoutTransAI, gridBagConstraints);

        lblRemarksAI.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransfered.add(lblRemarksAI, gridBagConstraints);

        txtRemarksAI.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRemarksAI.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTransfered.add(txtRemarksAI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransferBranchInfo.add(panTransfered, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(45, 253, 0, 254);
        panActInfo.add(panTransferBranchInfo, gridBagConstraints);

        panInsideAccountInfo.setMinimumSize(new java.awt.Dimension(760, 545));
        panInsideAccountInfo.setPreferredSize(new java.awt.Dimension(760, 545));
        panInsideAccountInfo.setLayout(new java.awt.GridBagLayout());

        panAccountInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Info."));
        panAccountInfo.setMinimumSize(new java.awt.Dimension(520, 600));
        panAccountInfo.setName("panAccountInfo"); // NOI18N
        panAccountInfo.setPreferredSize(new java.awt.Dimension(520, 600));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        lblProductIdAI.setText("Account Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 73, 0, 0);
        panAccountInfo.add(lblProductIdAI, gridBagConstraints);

        cboProductIdAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductIdAI.setPopupWidth(225);
        cboProductIdAI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProductIdAIItemStateChanged(evt);
            }
        });
        cboProductIdAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdAIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 4, 0, 0);
        panAccountInfo.add(cboProductIdAI, gridBagConstraints);

        lblActHeadAI.setText("Account Head");
        lblActHeadAI.setMaximumSize(new java.awt.Dimension(69, 21));
        lblActHeadAI.setMinimumSize(new java.awt.Dimension(75, 21));
        lblActHeadAI.setPreferredSize(new java.awt.Dimension(69, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 73, 0, 0);
        panAccountInfo.add(lblActHeadAI, gridBagConstraints);

        lblActHeadValueAI.setForeground(new java.awt.Color(0, 51, 204));
        lblActHeadValueAI.setText("[]");
        lblActHeadValueAI.setMinimumSize(new java.awt.Dimension(230, 15));
        lblActHeadValueAI.setPreferredSize(new java.awt.Dimension(230, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panAccountInfo.add(lblActHeadValueAI, gridBagConstraints);

        lblCustomerIdAI.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 98, 0, 0);
        panAccountInfo.add(lblCustomerIdAI, gridBagConstraints);

        txtCustomerIdAI.setEditable(false);
        txtCustomerIdAI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerIdAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerIdAIActionPerformed(evt);
            }
        });
        txtCustomerIdAI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdAIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(txtCustomerIdAI, gridBagConstraints);

        btnAccNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNum.setToolTipText("Customer Data");
        btnAccNum.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccNum.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        panAccountInfo.add(btnAccNum, gridBagConstraints);

        lblOpeningDateAI.setText("Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 91, 0, 0);
        panAccountInfo.add(lblOpeningDateAI, gridBagConstraints);

        dtdOpeningDateAI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdOpeningDateAIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(dtdOpeningDateAI, gridBagConstraints);

        lblActStatusAI.setText("Account Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 80, 0, 0);
        panAccountInfo.add(lblActStatusAI, gridBagConstraints);

        cboActStatusAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboActStatusAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActStatusAIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(cboActStatusAI, gridBagConstraints);

        lblConstitutionAI.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 101, 0, 0);
        panAccountInfo.add(lblConstitutionAI, gridBagConstraints);

        cboConstitutionAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboConstitutionAI.setNextFocusableComponent(rdoExistingCustomer_Yes);
        cboConstitutionAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConstitutionAIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panAccountInfo.add(cboConstitutionAI, gridBagConstraints);

        lblOpModeAI.setText("Mode of Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 64, 0, 0);
        panAccountInfo.add(lblOpModeAI, gridBagConstraints);

        cboOpModeAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOpModeAI.setPopupWidth(290);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(cboOpModeAI, gridBagConstraints);

        lblCommAddr.setText("Communication Addr Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 17, 0, 0);
        panAccountInfo.add(lblCommAddr, gridBagConstraints);

        cboCommAddr.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCommAddr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCommAddrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(cboCommAddr, gridBagConstraints);

        lblODLimitAI.setText("Over Draft Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 81, 0, 0);
        panAccountInfo.add(lblODLimitAI, gridBagConstraints);

        txtODLimitAI.setMaxLength(16);
        txtODLimitAI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtODLimitAI.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(txtODLimitAI, gridBagConstraints);

        lblGroupCodeAI.setText("Group Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 101, 0, 0);
        panAccountInfo.add(lblGroupCodeAI, gridBagConstraints);

        cboGroupCodeAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGroupCodeAI.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(cboGroupCodeAI, gridBagConstraints);

        lblSettlementModeAI.setText("Settlement Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 71, 0, 0);
        panAccountInfo.add(lblSettlementModeAI, gridBagConstraints);

        cboSettlementModeAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSettlementModeAI.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(cboSettlementModeAI, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 118, 0, 0);
        panAccountInfo.add(lblCategory, gridBagConstraints);

        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(cboCategory, gridBagConstraints);

        lblPrevActNumAI.setText("Previous Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 45, 0, 0);
        panAccountInfo.add(lblPrevActNumAI, gridBagConstraints);

        txtPrevActNumAI.setMaxLength(10);
        txtPrevActNumAI.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(txtPrevActNumAI, gridBagConstraints);

        lblAccountNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAccountNumber.setText("Account No");
        lblAccountNumber.setMinimumSize(new java.awt.Dimension(57, 21));
        lblAccountNumber.setPreferredSize(new java.awt.Dimension(57, 21));
        lblAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 91, 0, 0);
        panAccountInfo.add(lblAccountNumber, gridBagConstraints);

        lblCustName.setForeground(new java.awt.Color(0, 51, 255));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustName.setMaximumSize(new java.awt.Dimension(300, 13));
        lblCustName.setMinimumSize(new java.awt.Dimension(300, 13));
        lblCustName.setPreferredSize(new java.awt.Dimension(300, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 51, 0, 0);
        panAccountInfo.add(lblCustName, gridBagConstraints);

        lblActName.setText("Account Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 84, 0, 0);
        panAccountInfo.add(lblActName, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 42;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 118, 0, 0);
        panAccountInfo.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 42;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(txtRemarks, gridBagConstraints);

        txtClosedDt.setMaxLength(10);
        txtClosedDt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 44;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(txtClosedDt, gridBagConstraints);

        lblClosedDt.setText("Closed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 44;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 99, 0, 0);
        panAccountInfo.add(lblClosedDt, gridBagConstraints);

        txtActName.setEnabled(false);
        txtActName.setMaxLength(500);
        txtActName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtActName.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panAccountInfo.add(txtActName, gridBagConstraints);

        panExistingCustomer.setMinimumSize(new java.awt.Dimension(100, 16));
        panExistingCustomer.setPreferredSize(new java.awt.Dimension(100, 18));
        panExistingCustomer.setLayout(new java.awt.GridBagLayout());

        rdgExistingCustomer.add(rdoExistingCustomer_Yes);
        rdoExistingCustomer_Yes.setText("Yes");
        rdoExistingCustomer_Yes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoExistingCustomer_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoExistingCustomer_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoExistingCustomer_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExistingCustomer_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panExistingCustomer.add(rdoExistingCustomer_Yes, gridBagConstraints);

        rdgExistingCustomer.add(rdoExistingCustomer_No);
        rdoExistingCustomer_No.setText("No");
        rdoExistingCustomer_No.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoExistingCustomer_No.setMinimumSize(new java.awt.Dimension(45, 18));
        rdoExistingCustomer_No.setPreferredSize(new java.awt.Dimension(45, 18));
        rdoExistingCustomer_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExistingCustomer_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panExistingCustomer.add(rdoExistingCustomer_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panAccountInfo.add(panExistingCustomer, gridBagConstraints);

        txtExistingAcctNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExistingAcctNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExistingAcctNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(txtExistingAcctNo, gridBagConstraints);

        lblExistingAcctNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExistingAcctNo.setText("AccountNo/Member No");
        lblExistingAcctNo.setMaximumSize(new java.awt.Dimension(172, 18));
        lblExistingAcctNo.setMinimumSize(new java.awt.Dimension(172, 16));
        lblExistingAcctNo.setPreferredSize(new java.awt.Dimension(172, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = -8;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        panAccountInfo.add(lblExistingAcctNo, gridBagConstraints);

        lblExistingCustomer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExistingCustomer.setText("Existing Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 63, 0, 0);
        panAccountInfo.add(lblExistingCustomer, gridBagConstraints);

        txtAcctOpeningAmount.setMaxLength(10);
        txtAcctOpeningAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 40;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(txtAcctOpeningAmount, gridBagConstraints);

        lblAcctOpeningAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 40;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 125, 0, 0);
        panAccountInfo.add(lblAcctOpeningAmount, gridBagConstraints);

        txtAccountNo.setAllowNumber(true);
        txtAccountNo.setMaxLength(10);
        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNoActionPerformed(evt);
            }
        });
        txtAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        panAccountInfo.add(txtAccountNo, gridBagConstraints);

        cboConstitutionAI1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboConstitutionAI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConstitutionAI1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panAccountInfo.add(cboConstitutionAI1, gridBagConstraints);

        btnCustomerIdAI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerIdAI.setToolTipText("Customer Data");
        btnCustomerIdAI.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdAI.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdAI.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerIdAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdAIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);
        panAccountInfo.add(btnCustomerIdAI, gridBagConstraints);

        lblBaseCurrAI.setText("Base Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 84, 0, 0);
        panAccountInfo.add(lblBaseCurrAI, gridBagConstraints);

        cboBaseCurrAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBaseCurrAI.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountInfo.add(cboBaseCurrAI, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 46;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAccountInfo.add(chkPrimaryAccount, gridBagConstraints);

        cLabel1.setText("Primary Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 46;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 77, 0, 0);
        panAccountInfo.add(cLabel1, gridBagConstraints);

        txtUPIMobileNo.setAllowNumber(true);
        txtUPIMobileNo.setMinimumSize(new java.awt.Dimension(175, 21));
        txtUPIMobileNo.setPreferredSize(new java.awt.Dimension(175, 21));
        txtUPIMobileNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUPIMobileNoKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 48;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 10, 0);
        panAccountInfo.add(txtUPIMobileNo, gridBagConstraints);

        lblUPIMobileno.setText("UPI/ATM  Mobile No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 48;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 40, 0, 0);
        panAccountInfo.add(lblUPIMobileno, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -131;
        gridBagConstraints.ipady = -14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panInsideAccountInfo.add(panAccountInfo, gridBagConstraints);

        panCustomerSide1.setMinimumSize(new java.awt.Dimension(365, 600));
        panCustomerSide1.setPreferredSize(new java.awt.Dimension(365, 600));
        panCustomerSide1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(290, 25));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setForeground(new java.awt.Color(51, 102, 255));
        jLabel1.setText("Next Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jLabel1, gridBagConstraints);

        txtNextAccNo.setEditable(false);
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(txtNextAccNo, gridBagConstraints);

        panCustomerSide1.add(jPanel2, new java.awt.GridBagConstraints());

        panCustomerInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Info."));
        panCustomerInfo.setMinimumSize(new java.awt.Dimension(400, 285));
        panCustomerInfo.setName("panCustomerInfo"); // NOI18N
        panCustomerInfo.setPreferredSize(new java.awt.Dimension(400, 285));
        panCustomerInfo.setLayout(new java.awt.GridBagLayout());

        panCustOperation.setLayout(new java.awt.GridBagLayout());

        btnCustDetDelete.setText("Delete");
        btnCustDetDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustDetDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustOperation.add(btnCustDetDelete, gridBagConstraints);

        btnCustDetNew.setText("New");
        btnCustDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustOperation.add(btnCustDetNew, gridBagConstraints);

        btnToMain.setText("To Main");
        btnToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToMainActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustOperation.add(btnToMain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        panCustomerInfo.add(panCustOperation, gridBagConstraints);

        panCustDet.setMinimumSize(new java.awt.Dimension(315, 5));
        panCustDet.setPreferredSize(new java.awt.Dimension(315, 5));
        panCustDet.setLayout(new java.awt.GridBagLayout());

        lblCustomerName.setText("CustomerName");
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblCustomerName.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblCustomerName, gridBagConstraints);

        lblCustValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblCustValue.setMinimumSize(new java.awt.Dimension(240, 10));
        lblCustValue.setPreferredSize(new java.awt.Dimension(240, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblCustValue, gridBagConstraints);

        lblCity.setText("City");
        lblCity.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblCity, gridBagConstraints);

        lblCityValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblCityValue.setMinimumSize(new java.awt.Dimension(50, 10));
        lblCityValue.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblCityValue, gridBagConstraints);

        lblDOB.setText("Date Of Birth");
        lblDOB.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblDOB, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblDOBValue, gridBagConstraints);

        lblCountry.setText("Country");
        lblCountry.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblCountry, gridBagConstraints);

        lblCountryValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblCountryValue.setMinimumSize(new java.awt.Dimension(50, 10));
        lblCountryValue.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblCountryValue, gridBagConstraints);

        lblStreet.setText("Street");
        lblStreet.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblStreet, gridBagConstraints);

        lblStreetValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblStreetValue.setMinimumSize(new java.awt.Dimension(70, 10));
        lblStreetValue.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panCustDet.add(lblStreetValue, gridBagConstraints);

        lblState.setText("State");
        lblState.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblState, gridBagConstraints);

        lblStateValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblStateValue.setMinimumSize(new java.awt.Dimension(50, 10));
        lblStateValue.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblStateValue, gridBagConstraints);

        lblArea.setText("Area");
        lblArea.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblArea, gridBagConstraints);

        lblAreaValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblAreaValue.setMinimumSize(new java.awt.Dimension(70, 10));
        lblAreaValue.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblAreaValue, gridBagConstraints);

        lblPin.setText("Pin");
        lblPin.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblPin, gridBagConstraints);

        lblPinValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblPinValue.setMaximumSize(new java.awt.Dimension(50, 10));
        lblPinValue.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblPinValue, gridBagConstraints);

        lblMinOrMaj.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblMinOrMaj.setMinimumSize(new java.awt.Dimension(50, 10));
        lblMinOrMaj.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustDet.add(lblMinOrMaj, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCustomerInfo.add(panCustDet, gridBagConstraints);

        panInsideCustTable.setMinimumSize(new java.awt.Dimension(310, 215));
        panInsideCustTable.setPreferredSize(new java.awt.Dimension(310, 215));
        panInsideCustTable.setLayout(new java.awt.GridBagLayout());

        srpAct_Joint.setPreferredSize(new java.awt.Dimension(300, 200));

        tblAct_Joint.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Cust. Id", "Type", "Main / Joint", "Status"
            }
        ));
        tblAct_Joint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAct_JointMousePressed(evt);
            }
        });
        srpAct_Joint.setViewportView(tblAct_Joint);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideCustTable.add(srpAct_Joint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panCustomerInfo.add(panInsideCustTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        panCustomerSide1.add(panCustomerInfo, gridBagConstraints);

        jPanel3.setMinimumSize(new java.awt.Dimension(350, 55));
        jPanel3.setPreferredSize(new java.awt.Dimension(350, 55));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        lblAgentId.setText("Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        jPanel3.add(lblAgentId, gridBagConstraints);

        lblDealer.setText("Dealer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        jPanel3.add(lblDealer, gridBagConstraints);

        panAgentID.setMinimumSize(new java.awt.Dimension(120, 29));
        panAgentID.setPreferredSize(new java.awt.Dimension(120, 29));
        panAgentID.setLayout(new java.awt.GridBagLayout());

        txtDealerID.setEditable(false);
        txtDealerID.setEnabled(false);
        txtDealerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDealerID.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAgentID.add(txtDealerID, gridBagConstraints);

        btnDealerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDealerID.setToolTipText("Agent ID");
        btnDealerID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDealerID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDealerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDealerIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAgentID.add(btnDealerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        jPanel3.add(panAgentID, gridBagConstraints);

        panAgentID1.setMinimumSize(new java.awt.Dimension(120, 29));
        panAgentID1.setPreferredSize(new java.awt.Dimension(120, 29));
        panAgentID1.setLayout(new java.awt.GridBagLayout());

        txtAgentID.setEditable(false);
        txtAgentID.setEnabled(false);
        txtAgentID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAgentID.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAgentID1.add(txtAgentID, gridBagConstraints);

        btnAgentID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAgentID.setToolTipText("Agent ID");
        btnAgentID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAgentID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAgentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAgentID1.add(btnAgentID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        jPanel3.add(panAgentID1, gridBagConstraints);

        lblDealerIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblDealerIDVal.setMaximumSize(new java.awt.Dimension(150, 20));
        lblDealerIDVal.setMinimumSize(new java.awt.Dimension(150, 20));
        lblDealerIDVal.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel3.add(lblDealerIDVal, gridBagConstraints);

        lblAgentIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblAgentIDVal.setMaximumSize(new java.awt.Dimension(150, 20));
        lblAgentIDVal.setMinimumSize(new java.awt.Dimension(150, 20));
        lblAgentIDVal.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel3.add(lblAgentIDVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        panCustomerSide1.add(jPanel3, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(360, 170));
        jPanel4.setPreferredSize(new java.awt.Dimension(350, 150));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        lblLinkingProductId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLinkingProductId.setText("Linking Product Id");
        lblLinkingProductId.setMaximumSize(new java.awt.Dimension(100, 18));
        lblLinkingProductId.setMinimumSize(new java.awt.Dimension(110, 18));
        lblLinkingProductId.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(lblLinkingProductId, gridBagConstraints);

        lblLinkingActNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLinkingActNum.setText("Linking Ac Number");
        lblLinkingActNum.setMinimumSize(new java.awt.Dimension(150, 18));
        lblLinkingActNum.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(lblLinkingActNum, gridBagConstraints);

        panLinkingActNum.setMinimumSize(new java.awt.Dimension(120, 29));
        panLinkingActNum.setPreferredSize(new java.awt.Dimension(120, 29));
        panLinkingActNum.setLayout(new java.awt.GridBagLayout());

        txtLinkingActNum.setEditable(false);
        txtLinkingActNum.setEnabled(false);
        txtLinkingActNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLinkingActNum.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panLinkingActNum.add(txtLinkingActNum, gridBagConstraints);

        btnLinkingActNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLinkingActNum.setToolTipText("Agent ID");
        btnLinkingActNum.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLinkingActNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLinkingActNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinkingActNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLinkingActNum.add(btnLinkingActNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(panLinkingActNum, gridBagConstraints);

        cboLinkingProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLinkingProductId.setPopupWidth(225);
        cboLinkingProductId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLinkingProductIdItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(cboLinkingProductId, gridBagConstraints);

        lblLinkingActNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblLinkingActNameValue.setMaximumSize(new java.awt.Dimension(150, 20));
        lblLinkingActNameValue.setMinimumSize(new java.awt.Dimension(350, 20));
        lblLinkingActNameValue.setPreferredSize(new java.awt.Dimension(350, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(lblLinkingActNameValue, gridBagConstraints);

        lblCardActNumber.setText("Card A/c Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(lblCardActNumber, gridBagConstraints);

        txtCardActNumber.setMaxLength(10);
        txtCardActNumber.setMinimumSize(new java.awt.Dimension(175, 21));
        txtCardActNumber.setPreferredSize(new java.awt.Dimension(175, 21));
        txtCardActNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCardActNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        jPanel4.add(txtCardActNumber, gridBagConstraints);

        lblGender.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGender.setText("Whether KYC Norms followed");
        lblGender.setMaximumSize(new java.awt.Dimension(220, 18));
        lblGender.setMinimumSize(new java.awt.Dimension(190, 18));
        lblGender.setName("lblGender"); // NOI18N
        lblGender.setPreferredSize(new java.awt.Dimension(210, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(lblGender, gridBagConstraints);

        panGender.setMinimumSize(new java.awt.Dimension(200, 18));
        panGender.setName("panGender"); // NOI18N
        panGender.setPreferredSize(new java.awt.Dimension(200, 18));
        panGender.setLayout(new java.awt.GridBagLayout());

        rdgKYCNormsFollowed.add(rdoKYCNormsYes);
        rdoKYCNormsYes.setText("Yes");
        rdoKYCNormsYes.setMaximumSize(new java.awt.Dimension(49, 18));
        rdoKYCNormsYes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoKYCNormsYes.setName("rdoGender_Male"); // NOI18N
        rdoKYCNormsYes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoKYCNormsYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoKYCNormsYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panGender.add(rdoKYCNormsYes, gridBagConstraints);

        rdgKYCNormsFollowed.add(rdoKYCNormsNo);
        rdoKYCNormsNo.setText("No");
        rdoKYCNormsNo.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoKYCNormsNo.setName("rdoGender_Female"); // NOI18N
        rdoKYCNormsNo.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoKYCNormsNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoKYCNormsNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGender.add(rdoKYCNormsNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        jPanel4.add(panGender, gridBagConstraints);

        txtAtmCardLimit.setMaxLength(10);
        txtAtmCardLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAtmCardLimit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAtmCardLimitFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        jPanel4.add(txtAtmCardLimit, gridBagConstraints);

        lblAtmCardLimit.setText("ATM Card Limit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        jPanel4.add(lblAtmCardLimit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panCustomerSide1.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -46;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panInsideAccountInfo.add(panCustomerSide1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        panActInfo.add(panInsideAccountInfo, gridBagConstraints);

        tabAccounts.addTab("Account Details", panActInfo);

        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        panIsRequired.setBorder(javax.swing.BorderFactory.createTitledBorder("Is Required ?"));
        panIsRequired.setLayout(new java.awt.GridBagLayout());

        chkChequeBookAD.setText("Cheque Book");
        chkChequeBookAD.setMinimumSize(new java.awt.Dimension(110, 21));
        chkChequeBookAD.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panIsRequired.add(chkChequeBookAD, gridBagConstraints);

        chkCustGrpLimitValidationAD.setText("Cust. Group Limit Validation");
        chkCustGrpLimitValidationAD.setMinimumSize(new java.awt.Dimension(191, 21));
        chkCustGrpLimitValidationAD.setPreferredSize(new java.awt.Dimension(191, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panIsRequired.add(chkCustGrpLimitValidationAD, gridBagConstraints);

        chkNROStatusAD.setText("NRO Status");
        chkNROStatusAD.setMinimumSize(new java.awt.Dimension(95, 21));
        chkNROStatusAD.setPreferredSize(new java.awt.Dimension(95, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panIsRequired.add(chkNROStatusAD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        panAccountDetails.add(panIsRequired, gridBagConstraints);

        panCardInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Card Info."));
        panCardInfo.setMinimumSize(new java.awt.Dimension(574, 100));
        panCardInfo.setPreferredSize(new java.awt.Dimension(574, 100));
        panCardInfo.setLayout(new java.awt.GridBagLayout());

        chkATMAD.setText("ATM");
        chkATMAD.setMaximumSize(new java.awt.Dimension(59, 27));
        chkATMAD.setMinimumSize(new java.awt.Dimension(59, 27));
        chkATMAD.setPreferredSize(new java.awt.Dimension(59, 27));
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
        txtATMNoAD.setMinimumSize(new java.awt.Dimension(160, 21));
        txtATMNoAD.setPreferredSize(new java.awt.Dimension(160, 21));
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

        dtdATMFromDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdATMFromDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panCardInfo.add(dtdATMFromDateAD, gridBagConstraints);

        lblATMToDateAD.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panCardInfo.add(lblATMToDateAD, gridBagConstraints);

        dtdATMToDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdATMToDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panCardInfo.add(dtdATMToDateAD, gridBagConstraints);

        chkDebitAD.setText("Debit");
        chkDebitAD.setMinimumSize(new java.awt.Dimension(59, 21));
        chkDebitAD.setPreferredSize(new java.awt.Dimension(59, 21));
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
        txtDebitNoAD.setMinimumSize(new java.awt.Dimension(160, 21));
        txtDebitNoAD.setPreferredSize(new java.awt.Dimension(160, 21));
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

        dtdDebitFromDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdDebitFromDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panCardInfo.add(dtdDebitFromDateAD, gridBagConstraints);

        lblDebitToDateAD.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCardInfo.add(lblDebitToDateAD, gridBagConstraints);

        dtdDebitToDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdDebitToDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panCardInfo.add(dtdDebitToDateAD, gridBagConstraints);

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
        txtCreditNoAD.setMinimumSize(new java.awt.Dimension(160, 21));
        txtCreditNoAD.setPreferredSize(new java.awt.Dimension(160, 21));
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

        dtdCreditFromDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdCreditFromDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 4, 4);
        panCardInfo.add(dtdCreditFromDateAD, gridBagConstraints);

        lblCreditToDateAD.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panCardInfo.add(lblCreditToDateAD, gridBagConstraints);

        dtdCreditToDateAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdCreditToDateADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 4, 4);
        panCardInfo.add(dtdCreditToDateAD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccountDetails.add(panCardInfo, gridBagConstraints);

        panFlexiOpt.setBorder(javax.swing.BorderFactory.createTitledBorder("Flexi Option"));
        panFlexiOpt.setMinimumSize(new java.awt.Dimension(758, 40));
        panFlexiOpt.setPreferredSize(new java.awt.Dimension(758, 40));
        panFlexiOpt.setLayout(new java.awt.GridBagLayout());

        chkFlexiAD.setText("Flexi");
        chkFlexiAD.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chkFlexiAD.setMinimumSize(new java.awt.Dimension(56, 21));
        chkFlexiAD.setPreferredSize(new java.awt.Dimension(56, 21));
        chkFlexiAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFlexiADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(chkFlexiAD, gridBagConstraints);

        lblMinBal1FlexiAD.setText("Minimum Balance1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(lblMinBal1FlexiAD, gridBagConstraints);

        txtMinBal1FlexiAD.setMaxLength(32);
        txtMinBal1FlexiAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinBal1FlexiAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(txtMinBal1FlexiAD, gridBagConstraints);

        lblMinBal2FlexiAD.setText("Minimum Balance2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(lblMinBal2FlexiAD, gridBagConstraints);

        txtMinBal2FlexiAD.setMaxLength(32);
        txtMinBal2FlexiAD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinBal2FlexiAD.setValidation(new com.see.truetransact.uivalidation.CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(txtMinBal2FlexiAD, gridBagConstraints);

        lblReqFlexiPeriodAD.setText("Required Flexi Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(lblReqFlexiPeriodAD, gridBagConstraints);

        txtReqFlexiPeriodAD.setMaxLength(32);
        txtReqFlexiPeriodAD.setMinimumSize(new java.awt.Dimension(50, 21));
        txtReqFlexiPeriodAD.setPreferredSize(new java.awt.Dimension(50, 21));
        txtReqFlexiPeriodAD.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtReqFlexiPeriodAD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReqFlexiPeriodADFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(txtReqFlexiPeriodAD, gridBagConstraints);

        lblReqFlexiPeriodAD1.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panFlexiOpt.add(lblReqFlexiPeriodAD1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        panAccountDetails.add(panFlexiOpt, gridBagConstraints);

        panDiffCharges.setBorder(javax.swing.BorderFactory.createTitledBorder("Different Charges"));
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
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panDiffCharges.add(txtMisServiceChrgAD, gridBagConstraints);

        chkStopPmtChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkStopPmtChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panDiffCharges.add(txtChequeBookChrgAD, gridBagConstraints);

        chkChequeRetChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkChequeRetChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panDiffCharges.add(txtAccCloseChrgAD, gridBagConstraints);

        chkStmtChrgAD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkStmtChrgAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkStmtChrgAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStmtChrgADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panDiffCharges.add(dtdNPAChrgAD, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
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

        lblHideBalanceTrans.setText("Hide Balance in Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblHideBalanceTrans, gridBagConstraints);

        chkHideBalanceTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkHideBalanceTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panDiffCharges.add(chkHideBalanceTrans, gridBagConstraints);

        lblRoleHierarchy.setText("Above which Role Hierarchy Balance is to Shown?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblRoleHierarchy, gridBagConstraints);

        cboRoleHierarchy.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoleHierarchy.setPopupWidth(110);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panDiffCharges.add(cboRoleHierarchy, gridBagConstraints);

        lblPassBook.setText("PassBook");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiffCharges.add(lblPassBook, gridBagConstraints);

        chkPassBook.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkPassBook.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkPassBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPassBookActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panDiffCharges.add(chkPassBook, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
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
        panLastIntApp.add(dtdDebit, gridBagConstraints);

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
        panLastIntApp.add(dtdCredit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        panAccountDetails.add(panLastIntApp, gridBagConstraints);

        panRatesIN.setBorder(javax.swing.BorderFactory.createTitledBorder("Rate Details"));
        panRatesIN.setMaximumSize(new java.awt.Dimension(130, 100));
        panRatesIN.setMinimumSize(new java.awt.Dimension(130, 100));
        panRatesIN.setPreferredSize(new java.awt.Dimension(130, 100));
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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panAccountDetails.add(panRatesIN, gridBagConstraints);

        panInterestPayableIN.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest"));
        panInterestPayableIN.setMinimumSize(new java.awt.Dimension(460, 45));
        panInterestPayableIN.setPreferredSize(new java.awt.Dimension(460, 45));
        panInterestPayableIN.setLayout(new java.awt.GridBagLayout());

        chkPayIntOnCrBalIN.setText("Pay Interest on Credit Balance");
        chkPayIntOnCrBalIN.setMaximumSize(new java.awt.Dimension(209, 20));
        chkPayIntOnCrBalIN.setMinimumSize(new java.awt.Dimension(209, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 10);
        panInterestPayableIN.add(chkPayIntOnCrBalIN, gridBagConstraints);

        chkPayIntOnDrBalIN.setText("Receive Interest on Debit Balance");
        chkPayIntOnDrBalIN.setMaximumSize(new java.awt.Dimension(229, 20));
        chkPayIntOnDrBalIN.setMinimumSize(new java.awt.Dimension(229, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 4);
        panInterestPayableIN.add(chkPayIntOnDrBalIN, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccountDetails.add(panInterestPayableIN, gridBagConstraints);

        panMobileBanking.setBorder(javax.swing.BorderFactory.createTitledBorder("Mobile Banking"));
        panMobileBanking.setMinimumSize(new java.awt.Dimension(460, 45));
        panMobileBanking.setPreferredSize(new java.awt.Dimension(460, 45));
        panMobileBanking.setLayout(new java.awt.GridBagLayout());

        chkMobileBankingAD.setText("Required");
        chkMobileBankingAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkMobileBankingAD.setMinimumSize(new java.awt.Dimension(80, 21));
        chkMobileBankingAD.setPreferredSize(new java.awt.Dimension(80, 21));
        chkMobileBankingAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMobileBankingADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panMobileBanking.add(chkMobileBankingAD, gridBagConstraints);

        lblMobileNo.setText("Mobile No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panMobileBanking.add(lblMobileNo, gridBagConstraints);

        txtMobileNo.setAllowAll(true);
        txtMobileNo.setMaxLength(16);
        txtMobileNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMobileNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMobileNoFocusLost(evt);
            }
        });
        txtMobileNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMobileNoKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 13);
        panMobileBanking.add(txtMobileNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panMobileBanking.add(tdtMobileSubscribedFrom, gridBagConstraints);

        lblMobileSubscribedFrom.setText("Subscribed From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panMobileBanking.add(lblMobileSubscribedFrom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccountDetails.add(panMobileBanking, gridBagConstraints);

        tabAccounts.addTab("Other Details", panAccountDetails);

        panATMCardDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panATMCardDetails.setMinimumSize(new java.awt.Dimension(570, 450));
        panATMCardDetails.setPreferredSize(new java.awt.Dimension(570, 450));
        panATMCardDetails.setLayout(new java.awt.GridBagLayout());

        panATMAccountCardDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panATMAccountCardDetails.setMinimumSize(new java.awt.Dimension(375, 440));
        panATMAccountCardDetails.setPreferredSize(new java.awt.Dimension(375, 440));
        panATMAccountCardDetails.setLayout(new java.awt.GridBagLayout());

        lblATMCardNo.setText("Card Act No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMAccountCardDetails.add(lblATMCardNo, gridBagConstraints);

        panATMCARDBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panATMCARDBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panATMCARDBtn.setLayout(new java.awt.GridBagLayout());

        btnATMNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnATMNew.setToolTipText("New");
        btnATMNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnATMNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnATMNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnATMNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnATMNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMCARDBtn.add(btnATMNew, gridBagConstraints);

        btnATMSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnATMSave.setToolTipText("Save");
        btnATMSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnATMSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnATMSave.setName("btnContactNoAdd"); // NOI18N
        btnATMSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnATMSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnATMSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMCARDBtn.add(btnATMSave, gridBagConstraints);

        btnATMDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnATMDelete.setToolTipText("Delete");
        btnATMDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnATMDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnATMDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnATMDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnATMDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMCARDBtn.add(btnATMDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 0, 15);
        panATMAccountCardDetails.add(panATMCARDBtn, gridBagConstraints);

        lblATMCardNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblATMCardNoVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblATMCardNoVal.setMaximumSize(new java.awt.Dimension(270, 18));
        lblATMCardNoVal.setMinimumSize(new java.awt.Dimension(270, 18));
        lblATMCardNoVal.setPreferredSize(new java.awt.Dimension(270, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMAccountCardDetails.add(lblATMCardNoVal, gridBagConstraints);

        panAction.setMinimumSize(new java.awt.Dimension(120, 18));
        panAction.setPreferredSize(new java.awt.Dimension(120, 18));
        panAction.setLayout(new java.awt.GridBagLayout());

        rdgExistingCustomer.add(rdoActionStop);
        rdoActionStop.setText("Stop");
        rdoActionStop.setMaximumSize(new java.awt.Dimension(60, 18));
        rdoActionStop.setMinimumSize(new java.awt.Dimension(60, 18));
        rdoActionStop.setPreferredSize(new java.awt.Dimension(60, 18));
        rdoActionStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoActionStopActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAction.add(rdoActionStop, gridBagConstraints);

        rdgExistingCustomer.add(rdoActionRevoke);
        rdoActionRevoke.setText("Revoke");
        rdoActionRevoke.setMaximumSize(new java.awt.Dimension(70, 18));
        rdoActionRevoke.setMinimumSize(new java.awt.Dimension(70, 18));
        rdoActionRevoke.setPreferredSize(new java.awt.Dimension(70, 18));
        rdoActionRevoke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoActionRevokeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAction.add(rdoActionRevoke, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 0);
        panATMAccountCardDetails.add(panAction, gridBagConstraints);

        lblExistingCustomer1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExistingCustomer1.setText("Action");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMAccountCardDetails.add(lblExistingCustomer1, gridBagConstraints);

        lblActionDt.setText("Action Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMAccountCardDetails.add(lblActionDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panATMAccountCardDetails.add(tdtActionDt, gridBagConstraints);

        srpCardRemarks.setMinimumSize(new java.awt.Dimension(200, 55));
        srpCardRemarks.setPreferredSize(new java.awt.Dimension(200, 55));

        txtCardRemarks.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtCardRemarks.setLineWrap(true);
        txtCardRemarks.setMinimumSize(new java.awt.Dimension(20, 100));
        txtCardRemarks.setPreferredSize(new java.awt.Dimension(20, 100));
        srpCardRemarks.setViewportView(txtCardRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panATMAccountCardDetails.add(srpCardRemarks, gridBagConstraints);

        lblREmarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panATMAccountCardDetails.add(lblREmarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        panATMCardDetails.add(panATMAccountCardDetails, gridBagConstraints);

        panATMCardTableDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panATMCardTableDetails.setMinimumSize(new java.awt.Dimension(425, 440));
        panATMCardTableDetails.setPreferredSize(new java.awt.Dimension(425, 440));
        panATMCardTableDetails.setLayout(new java.awt.GridBagLayout());

        srpATMCardTable.setMinimumSize(new java.awt.Dimension(400, 420));
        srpATMCardTable.setPreferredSize(new java.awt.Dimension(400, 420));

        tblATMCardDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL_NO", "ACTION", "ACTION_DT", "AUTH_STATUS"
            }
        ));
        tblATMCardDetails.setMinimumSize(new java.awt.Dimension(400, 1000));
        tblATMCardDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 400));
        tblATMCardDetails.setPreferredSize(new java.awt.Dimension(400, 1000));
        tblATMCardDetails.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblATMCardDetails.setSelectionForeground(new java.awt.Color(153, 0, 0));
        tblATMCardDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblATMCardDetailsMousePressed(evt);
            }
        });
        srpATMCardTable.setViewportView(tblATMCardDetails);

        panATMCardTableDetails.add(srpATMCardTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panATMCardDetails.add(panATMCardTableDetails, gridBagConstraints);

        tabAccounts.addTab("ATM A/c Card Details", panATMCardDetails);

        panBorrowerSalaryDetails.setLayout(new java.awt.GridBagLayout());

        lblBorrowerSalary.setText("Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(41, 65, 0, 0);
        panBorrowerSalaryDetails.add(lblBorrowerSalary, gridBagConstraints);

        txtBorrowerSalary.setAllowAll(true);
        txtBorrowerSalary.setAllowNumber(true);
        txtBorrowerSalary.setFocusCycleRoot(true);
        txtBorrowerSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBorrowerSalary.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBorrowerSalaryFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(39, 8, 0, 150);
        panBorrowerSalaryDetails.add(txtBorrowerSalary, gridBagConstraints);

        lblBorrowerNetworth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 57, 0, 0);
        panBorrowerSalaryDetails.add(lblBorrowerNetworth, gridBagConstraints);

        txtBorrowerNetworth.setAllowAll(true);
        txtBorrowerNetworth.setAllowNumber(true);
        txtBorrowerNetworth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 5, 0, 0);
        panBorrowerSalaryDetails.add(txtBorrowerNetworth, gridBagConstraints);

        cLabel2.setText("Applied Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 19, 0, 0);
        panBorrowerSalaryDetails.add(cLabel2, gridBagConstraints);

        txtAppliedAmt.setAllowAll(true);
        txtAppliedAmt.setAllowNumber(true);
        txtAppliedAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 4, 25, 0);
        panBorrowerSalaryDetails.add(txtAppliedAmt, gridBagConstraints);

        lblEligAmt.setText("Elig Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 39, 0, 0);
        panBorrowerSalaryDetails.add(lblEligAmt, gridBagConstraints);

        lblBorrEligAmt.setText("Elig Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 7, 0, 0);
        panBorrowerSalaryDetails.add(lblBorrEligAmt, gridBagConstraints);

        panSuretyDetails.setLayout(new java.awt.GridBagLayout());

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 46, 0, 0);
        panSuretyDetails.add(lblMemberNo, gridBagConstraints);

        lblMemberName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 79, 0, 0);
        panSuretyDetails.add(lblMemberName, gridBagConstraints);

        lblMemberType.setText("Type of Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 0, 0);
        panSuretyDetails.add(lblMemberType, gridBagConstraints);

        lblContactNo.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 48, 0, 0);
        panSuretyDetails.add(lblContactNo, gridBagConstraints);

        lblMemberSalary.setText("Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 76, 0, 0);
        panSuretyDetails.add(lblMemberSalary, gridBagConstraints);

        lblMemberNetworth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 62, 0, 0);
        panSuretyDetails.add(lblMemberNetworth, gridBagConstraints);

        txtMemberNo.setAllowAll(true);
        txtMemberNo.setAllowNumber(true);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 32, 0, 0);
        panSuretyDetails.add(txtMemberNo, gridBagConstraints);

        txtMemberName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 32, 0, 0);
        panSuretyDetails.add(txtMemberName, gridBagConstraints);

        txtMemberType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 32, 0, 0);
        panSuretyDetails.add(txtMemberType, gridBagConstraints);

        txtContactNo.setAllowAll(true);
        txtContactNo.setAllowNumber(true);
        txtContactNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 32, 0, 0);
        panSuretyDetails.add(txtContactNo, gridBagConstraints);

        txtMemberSalary.setAllowAll(true);
        txtMemberSalary.setAllowNumber(true);
        txtMemberSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 32, 0, 0);
        panSuretyDetails.add(txtMemberSalary, gridBagConstraints);

        txtMemberNetworth.setAllowAll(true);
        txtMemberNetworth.setAllowNumber(true);
        txtMemberNetworth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 32, 0, 0);
        panSuretyDetails.add(txtMemberNetworth, gridBagConstraints);

        btnSecurityNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSecurityNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = -16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 32, 0, 0);
        panSuretyDetails.add(btnSecurityNew, gridBagConstraints);

        btnSecuritySave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSecuritySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecuritySaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = -18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 0);
        panSuretyDetails.add(btnSecuritySave, gridBagConstraints);

        btnSecurityDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSecurityDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 0);
        panSuretyDetails.add(btnSecurityDelete, gridBagConstraints);

        btnMemberSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -18;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 10, 0, 59);
        panSuretyDetails.add(btnMemberSearch, gridBagConstraints);

        btnGetODLimit.setText("Get OD limit");
        btnGetODLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetODLimitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 36, 28, 0);
        panSuretyDetails.add(btnGetODLimit, gridBagConstraints);

        tblMemberType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member No", "Name", "MemberType", "Contact", "Salary", "Networth", "Title 7"
            }
        ));
        tblMemberType.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblMemberType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMemberTypeMousePressed(evt);
            }
        });
        srpShowSurety.setViewportView(tblMemberType);

        panServiceDetails.setLayout(new java.awt.GridBagLayout());

        lblDob.setText("Dob");
        lblDob.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 29, 0, 0);
        panServiceDetails.add(lblDob, gridBagConstraints);

        lblRetireDt.setText("Retirement Dt");
        lblRetireDt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 278;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 29, 0, 0);
        panServiceDetails.add(lblRetireDt, gridBagConstraints);

        lblService.setText("Service");
        lblService.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 331;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 29, 26, 0);
        panServiceDetails.add(lblService, gridBagConstraints);

        javax.swing.GroupLayout panShowSuretyLayout = new javax.swing.GroupLayout(panShowSurety);
        panShowSurety.setLayout(panShowSuretyLayout);
        panShowSuretyLayout.setHorizontalGroup(
            panShowSuretyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panShowSuretyLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(panShowSuretyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panServiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(srpShowSurety, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panShowSuretyLayout.setVerticalGroup(
            panShowSuretyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panShowSuretyLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(srpShowSurety, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panServiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panBorrowerServiceDetails.setLayout(new java.awt.GridBagLayout());

        lblBorrowerDob.setText("Dob");
        lblBorrowerDob.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 37, 0, 11);
        panBorrowerServiceDetails.add(lblBorrowerDob, gridBagConstraints);

        lblBorrowerRetireDt.setText("Retirement Dt");
        lblBorrowerRetireDt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 305;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 37, 0, 10);
        panBorrowerServiceDetails.add(lblBorrowerRetireDt, gridBagConstraints);

        lblBorrowerService.setText("Service");
        lblBorrowerService.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 335;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 37, 25, 11);
        panBorrowerServiceDetails.add(lblBorrowerService, gridBagConstraints);

        chkRenew.setText("Renew(Extend only)");
        chkRenew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRenewActionPerformed(evt);
            }
        });

        chkEnhance.setText("Enhancement");
        chkEnhance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEnhanceActionPerformed(evt);
            }
        });

        chkODClose.setText("Close OD");
        chkODClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkODCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panSecurityDetailsLayout = new javax.swing.GroupLayout(panSecurityDetails);
        panSecurityDetails.setLayout(panSecurityDetailsLayout);
        panSecurityDetailsLayout.setHorizontalGroup(
            panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panBorrowerSalaryDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panSuretyDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panShowSurety, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panBorrowerServiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                                .addComponent(chkRenew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkEnhance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chkODClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(308, Short.MAX_VALUE))
        );
        panSecurityDetailsLayout.setVerticalGroup(
            panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                        .addComponent(panBorrowerSalaryDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panSuretyDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                        .addGroup(panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkRenew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkEnhance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkODClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(panBorrowerServiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panShowSurety, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(230, Short.MAX_VALUE))
        );

        panBorrowerSalaryDetails.getAccessibleContext().setAccessibleName("Borrower Salary Details");

        tabAccounts.addTab("Security Details", panSecurityDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccounts.add(tabAccounts, gridBagConstraints);

        getContentPane().add(panAccounts, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnView);

        lblTBSep4.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep4.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep4.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrAccounts.add(lblTBSep4);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnAdd.setToolTipText("New");
        btnAdd.setEnabled(false);
        btnAdd.setName("btnAdd"); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnAdd);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAccounts.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setName("btnEdit"); // NOI18N
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAccounts.add(lblSpace12);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setName("btnDelete"); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnDelete);

        lblTBSep1.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep1.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep1.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrAccounts.add(lblTBSep1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnSave);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAccounts.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.setNextFocusableComponent(btnAdd);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnCancel);

        lblTBSep2.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep2.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep2.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrAccounts.add(lblTBSep2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnAuthorize);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAccounts.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAccounts.add(lblSpace15);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnReject);

        lblTBSep3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrAccounts.add(lblTBSep3);

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnReport.setToolTipText("Print");
        btnReport.setFocusable(false);
        btnReport.setName("btnReport"); // NOI18N
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnReport);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAccounts.add(lblSpace16);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.setName("btnClose"); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnClose);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAccounts.add(lblSpace17);

        btnDeletedDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeletedDetails.setToolTipText("Closed Accounts Details");
        btnDeletedDetails.setFocusable(false);
        btnDeletedDetails.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDeletedDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletedDetailsActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnDeletedDetails);

        lblSpace8.setText("     ");
        tbrAccounts.add(lblSpace8);

        lblSpace6.setText("     ");
        lblSpace6.setMaximumSize(new java.awt.Dimension(110, 18));
        lblSpace6.setMinimumSize(new java.awt.Dimension(110, 18));
        lblSpace6.setPreferredSize(new java.awt.Dimension(110, 18));
        tbrAccounts.add(lblSpace6);

        lblPanNumber1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanNumber1.setText("Account No : ");
        lblPanNumber1.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPanNumber1.setMinimumSize(new java.awt.Dimension(100, 16));
        lblPanNumber1.setPreferredSize(new java.awt.Dimension(100, 16));
        tbrAccounts.add(lblPanNumber1);

        lblSpace7.setText("     ");
        tbrAccounts.add(lblSpace7);

        txtEditOperativeNo.setFocusable(false);
        txtEditOperativeNo.setMaximumSize(new java.awt.Dimension(150, 30));
        txtEditOperativeNo.setMinimumSize(new java.awt.Dimension(150, 30));
        txtEditOperativeNo.setPreferredSize(new java.awt.Dimension(150, 30));
        txtEditOperativeNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEditOperativeNoFocusLost(evt);
            }
        });
        tbrAccounts.add(txtEditOperativeNo);

        lblSpace9.setText("     ");
        tbrAccounts.add(lblSpace9);

        getContentPane().add(tbrAccounts, java.awt.BorderLayout.NORTH);

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

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mnuAdd.setText("Add");

        mitAddNew.setText("New");
        mitAddNew.setName("mitAdd"); // NOI18N
        mitAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAddNewActionPerformed(evt);
            }
        });
        mnuAdd.add(mitAddNew);
        mnuAdd.add(sptAdd);

        mitAddTransferIn.setText("Transfer In");
        mitAddTransferIn.setName("mitEdit"); // NOI18N
        mitAddTransferIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAddTransferInActionPerformed(evt);
            }
        });
        mnuAdd.add(mitAddTransferIn);

        mnuProcess.add(mnuAdd);

        mnuEdit.setText("Edit");

        mitEditNew.setText("New");
        mitEditNew.setName("mitAdd"); // NOI18N
        mitEditNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditNewActionPerformed(evt);
            }
        });
        mnuEdit.add(mitEditNew);
        mnuEdit.add(sptEdit);

        mitEditTransferIn.setText("Transfer In");
        mitEditTransferIn.setName("mitEdit"); // NOI18N
        mitEditTransferIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditTransferInActionPerformed(evt);
            }
        });
        mnuEdit.add(mitEditTransferIn);

        mnuProcess.add(mnuEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess1);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setName("mitClose"); // NOI18N
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);
        mnuProcess.add(sptProcess2);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTransfer.add(mnuProcess);

        mnuAction.setText("Action");
        mnuAction.setName("mnuAction"); // NOI18N

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuAction.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuAction.add(mitCancel);
        mnuAction.add(sptAction);

        mitResume.setText("Resume");
        mitResume.setName("mitResume"); // NOI18N
        mitResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitResumeActionPerformed(evt);
            }
        });
        mnuAction.add(mitResume);

        mbrTransfer.add(mnuAction);

        setJMenuBar(mbrTransfer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkMobileBankingADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMobileBankingADActionPerformed
        // TODO add your handling code here:
        if (chkMobileBankingAD.isSelected()) {
            if(txtCustomerIdAI.getText().length()>0){
                long mobileNo = observable.getMobileNo(CommonUtil.convertObjToStr(txtCustomerIdAI.getText()));
                if(mobileNo != 0){
                    txtMobileNo.setText(CommonUtil.convertObjToStr(mobileNo));
                    tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
                }
            }
            EnableDisbleMobileBanking(true);
        } else {
            EnableDisbleMobileBanking(false);
            txtMobileNo.setText("");
            tdtMobileSubscribedFrom.setDateValue("");
        }
    }//GEN-LAST:event_chkMobileBankingADActionPerformed
    private void EnableDisbleMobileBanking(boolean flag) {
        txtMobileNo.setEnabled(flag);
        tdtMobileSubscribedFrom.setEnabled(flag);
    }
    private void txtCustomerIdAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIdAIActionPerformed
        // TODO add your handling code here:
        showCustomerDetails();

    }//GEN-LAST:event_txtCustomerIdAIActionPerformed
    private void showCustomerDetails() {
        String customerId = CommonUtil.convertObjToStr(txtCustomerIdAI.getText());
        if (customerId.length() > 0) {
            callView("New Customer");
        }
    }
    private void rdoExistingCustomer_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistingCustomer_NoActionPerformed
        // TODO add your handling code here:
        txtExistingAcctNo.setVisible(false);
        lblExistingAcctNo.setVisible(false);
        if (rdoExistingCustomer_No.isSelected() == true) {
            txtAccountNo.setText("");
            txtCustomerIdAI.setText("");
            lblCustName.setText("");
            nomineeUi.setMainCustomerId(txtCustomerIdAI.getText());
            lblAccountNumber.setVisible(false);
            txtAccountNo.setVisible(false);
            txtCustomerIdAI.setEnabled(true);
            individualCustUI = new IndividualCustUI();
            com.see.truetransact.ui.TrueTransactMain.showScreen(individualCustUI);
            individualCustUI.loanCreationCustId(this);
        }
    }//GEN-LAST:event_rdoExistingCustomer_NoActionPerformed

    private void rdoExistingCustomer_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistingCustomer_YesActionPerformed
        // TODO add your handling code here:
        txtExistingAcctNo.setVisible(true);
        lblExistingAcctNo.setVisible(true);
    }//GEN-LAST:event_rdoExistingCustomer_YesActionPerformed

    private void txtAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNoFocusLost
        // TODO add your handling code here:
        if (!txtAccountNo.getText().toString().equals("") && (txtAccountNo.getText().length() > 0)) {
            if ((txtAccountNo.getText().length() == 13)) {

//           }
//           else
//           {
//               ClientUtil.showAlertWindow("Invalid Account No");
//           }
                // }

//        if(txtAccountNo.getText().length()>0){
                if (!txtAccountNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
                    HashMap operativeMap = new HashMap();
                    operativeMap.put("ACT_NUM", txtAccountNo.getText());
                    operativeMap.put("BRANCH_CODE", getSelectedBranchID());
                    List lst = ClientUtil.executeQuery("getAccountList", operativeMap);
                    if (lst != null && lst.size() > 0) {
                        operativeMap = (HashMap) lst.get(0);
                        operativeMap.put("ACCOUNTNO", txtAccountNo.getText());
                        operativeMap.put("BRANCH_CODE", getSelectedBranchID());
                        operativeMap.put("AUTHORIZATIONSTATUS", operativeMap.get("authorizationStatus"));
                        viewType = "EditN";
                        fillData(operativeMap);
                        btnView.setEnabled(false);
                        btnReport.setEnabled(false);
                        btnDeletedDetails.setEnabled(false);
                        panExistingCustomer.setVisible(false);
                        lblExistingCustomer.setVisible(false);
                        txtExistingAcctNo.setVisible(false);
                        lblExistingAcctNo.setVisible(false);
                        btnSave.setEnabled(true);
                        btnReject.setEnabled(false);
                        btnAuthorize.setEnabled(false);
                        btnException.setEnabled(false);
                        txtExistingAcctNo.setVisible(false);
                        lblExistingAcctNo.setVisible(false);
                        txtAccountNo.setEnabled(false);
                        txtAcctOpeningAmount.setEnabled(false);
                        btnATMNew.setEnabled(true);
                        if (tblATMCardDetails.getRowCount() > 0) {
                            String lastRowStatus = CommonUtil.convertObjToStr(tblATMCardDetails.getValueAt(tblATMCardDetails.getRowCount() - 1, 3));
                            if (lastRowStatus.length() > 0) {
                                btnATMNew.setEnabled(true);
                            } else {
                                btnATMNew.setEnabled(false);
                            }
                        }
                        ClientUtil.enableDisable(panATMAccountCardDetails, false);
                        if (txtCardActNumber.getText().length() > 0) {
                            txtCardActNumber.setEnabled(false);
                        } else {
                            txtCardActNumber.setEnabled(true);
                        }
                        // Added by nithya -- start
                        HashMap whereMap = new HashMap();
                        whereMap.put("CUST_ID", txtCustomerIdAI.getText()); 
                        List shareList = ClientUtil.executeQuery("getBorrowerShareDetails", whereMap);
                        if (shareList != null && shareList.size() > 0) {
                            HashMap resultMap = (HashMap) shareList.get(0);  
                            //System.out.println("getBorrowerShareDetails :: " + resultMap);
                            if(resultMap.containsKey("MEMBERSHIP_NO")){
                             observable.setBorrowerMemberNo(CommonUtil.convertObjToStr(resultMap.get("MEMBERSHIP_NO")));
                                HashMap serviceMap = new HashMap();
                                serviceMap.put("CUST_ID", txtCustomerIdAI.getText());
                                List serviceList = ClientUtil.executeQuery("getFutureServicePeriod", serviceMap);
                                if (serviceList != null && serviceList.size() > 0) {
                                    HashMap hMap = (HashMap) serviceList.get(0);
                                    if (hMap != null && hMap.size() > 0 && hMap.containsKey("SERVICE")) {
                                        lblBorrowerService.setText("Service : " + CommonUtil.convertObjToStr(hMap.get("SERVICE")));
                                    }
                                }
                            }if(resultMap.containsKey("DOB")){
                              lblBorrowerDob.setText("Date of Birth : " + CommonUtil.convertObjToStr(resultMap.get("DOB")));                              
                            }if(resultMap.containsKey("RETIREMENT_DT")){
                              lblBorrowerRetireDt.setText("Retirement Date : " + CommonUtil.convertObjToStr(resultMap.get("RETIREMENT_DT")));   
                            } 
                        }
                        //  Added by nithya -- end
                    } else {
                        ClientUtil.showAlertWindow("Invalid Account No");
                        btnCancelActionPerformed(null);
                        txtAccountNo.setText("");
                        return;
                    }
                }
                editTransMap = getTransDetailsEdit(txtAccountNo.getText());
                transIdMap = (HashMap) editTransMap.get("TRANSID");
                transTypeMap = (HashMap) editTransMap.get("TRANSTYPE");
                //System.out.println("transIdMap1111assd>>>" + transIdMap + "transTypeMap>>" + transTypeMap);
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                //System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    //System.out.println("currDt>>>>" + currDt + "ProxyParameters.BRANCH_ID>>>>" + ProxyParameters.BRANCH_ID);
                    paramMap.put("TransDt", currDt);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    //System.out.println("transIdMap>>>" + transIdMap);
                    Object keys[] = transIdMap.keySet().toArray();
                    //System.out.println("keys>>>" + keys);
                    for (int i = 0; i < keys.length; i++) {
                        //System.out.println("keys[i]>>" + keys[i]);
                        paramMap.put("TransId", keys[i]);
                        ttIntgration.setParam(paramMap);
                        //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                        if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                            ttIntgration.integrationForPrint("ReceiptPayment");
                        } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
                            ttIntgration.integrationForPrint("CashPayment", false);
                        } else {
                            ttIntgration.integrationForPrint("CashReceipt", false);
                        }
                    }
                }
                transIdMap = null;
                transTypeMap = null;
            } else {
                ClientUtil.showAlertWindow("Invalid Account No");
                btnCancelActionPerformed(null);
                txtAccountNo.setText("");
                return;
            }


        }

    }//GEN-LAST:event_txtAccountNoFocusLost

    private void tabAccountsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabAccountsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tabAccountsFocusLost

    private void txtExistingAcctNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExistingAcctNoFocusLost
        // TODO add your handling code here:
        callView("EXISTING_CUSTOMER");
    }//GEN-LAST:event_txtExistingAcctNoFocusLost

    private void txtEditOperativeNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEditOperativeNoFocusLost
        // TODO add your handling code here:
        if (txtEditOperativeNo.getText().length() > 0) {
            HashMap operativeMap = new HashMap();
            operativeMap.put("ACT_NUM", txtEditOperativeNo.getText());
            operativeMap.put("BRANCH_CODE", getSelectedBranchID());
            List lst = ClientUtil.executeQuery("getAccountList", operativeMap);
            if (lst != null && lst.size() > 0) {
                operativeMap = (HashMap) lst.get(0);
                operativeMap.put("ACCOUNTNO", txtEditOperativeNo.getText());
                operativeMap.put("BRANCH_CODE", getSelectedBranchID());
                operativeMap.put("AUTHORIZATIONSTATUS", operativeMap.get("authorizationStatus"));
                viewType = "EditN";
                fillData(operativeMap);
                btnView.setEnabled(false);
                btnReport.setEnabled(false);
                btnDeletedDetails.setEnabled(false);
                panExistingCustomer.setVisible(false);
                lblExistingCustomer.setVisible(false);
                txtExistingAcctNo.setVisible(false);
                lblExistingAcctNo.setVisible(false);
                btnSave.setEnabled(true);
                btnReject.setEnabled(false);
                btnAuthorize.setEnabled(false);
                btnException.setEnabled(false);
                lblExistingAcctNo.setVisible(false);
                txtExistingAcctNo.setVisible(false);
                txtAccountNo.setEnabled(false);
                txtAcctOpeningAmount.setEnabled(false);
            } else {
                ClientUtil.showAlertWindow("Invalid Account No");
                btnCancelActionPerformed(null);
                txtEditOperativeNo.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtEditOperativeNoFocusLost

    private void chkPassBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPassBookActionPerformed
        // TODO add your handling code here:
        chkStmtChrgAD.setSelected(false);
    }//GEN-LAST:event_chkPassBookActionPerformed

    private void chkStmtChrgADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStmtChrgADActionPerformed
        // TODO add your handling code here:
        chkPassBook.setSelected(false);
    }//GEN-LAST:event_chkStmtChrgADActionPerformed

    private void txtReqFlexiPeriodADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReqFlexiPeriodADFocusLost
        // TODO add your handling code here:
        HashMap productMap = new HashMap();
        String PROD_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
        productMap.put("PROD_ID", PROD_ID);
        List lst = ClientUtil.executeQuery("getProductFlext.Account", productMap);
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
            txtMinBal1FlexiAD.setEnabled(!isSelected);
            txtMinBal2FlexiAD.setEnabled(!isSelected);
            txtMinBal1FlexiAD.setText(CommonUtil.convertObjToStr(productMap.get("MIN_BAL1")));
            txtMinBal2FlexiAD.setText(CommonUtil.convertObjToStr(productMap.get("MIN_BAL2")));
            productMap.put("PROD_ID", productMap.get("FLEXI_PROD_ID"));
            lst = ClientUtil.executeQuery("getSchemeIntroDate", productMap);
            if (lst != null && lst.size() > 0) {
                productMap = (HashMap) lst.get(0);
                double minPeriod = CommonUtil.convertObjToDouble(productMap.get("MIN_DEPOSIT_PERIOD")).doubleValue();
                double maxPeriod = CommonUtil.convertObjToDouble(productMap.get("MAX_DEPOSIT_PERIOD")).doubleValue();
                double txtPeriod = CommonUtil.convertObjToDouble(txtReqFlexiPeriodAD.getText()).doubleValue();
                if (!((txtPeriod <= Integer.parseInt(CommonUtil.convertObjToStr(productMap.get("MAX_DEPOSIT_PERIOD")))) && txtPeriod >= Integer.parseInt(CommonUtil.convertObjToStr(productMap.get("MIN_DEPOSIT_PERIOD"))))) {
                    StringBuffer strBMsg = new StringBuffer();
                    strBMsg.append(" The Deposit period should be between ");
                    strBMsg.append(CommonUtil.convertObjToInt(productMap.get("MIN_DEPOSIT_PERIOD")));
                    strBMsg.append(" to ");
                    strBMsg.append(CommonUtil.convertObjToInt(productMap.get("MAX_DEPOSIT_PERIOD")));
                    String stringBMsg = strBMsg.toString();
                    ClientUtil.displayAlert(stringBMsg + " " + "days");
                    txtReqFlexiPeriodAD.setText("");
                }
            }
        }
    }//GEN-LAST:event_txtReqFlexiPeriodADFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquirystatus");
        btnCheck();
        ClientUtil.enableDisable(panAccountInfo, false);
        ClientUtil.enableDisable(panAccountDetails, false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnDeletedDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletedDetailsActionPerformed
        txtClosedDt.setVisible(true);
        lblClosedDt.setVisible(true);
        observable.setOperation(ClientConstants.ACTIONTYPE_VIEW_MODE);
        callView("Deletedstatus");
        btnCheck();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeletedDetailsActionPerformed

    private void dtdOpeningDateAIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdOpeningDateAIFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_dtdOpeningDateAIFocusLost

    private void chkHideBalanceTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHideBalanceTransActionPerformed
        // TODO add your handling code here:
        boolean isSelected = chkHideBalanceTrans.isSelected();
        if (!isSelected) {
            cboRoleHierarchy.setSelectedIndex(0);
        } else {
            cboRoleHierarchy.setEnabled(true);
        }
    }//GEN-LAST:event_chkHideBalanceTransActionPerformed

    private void cboDMYADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDMYADActionPerformed
    }//GEN-LAST:event_cboDMYADActionPerformed
    //__ Data required for Auth Signatory...

    private void addCustIDNAuthSignatory() {
        int borrowerTabRowCount = tblAct_Joint.getRowCount();
        for (int i = borrowerTabRowCount - 1, j = 0; i >= 0; --i, ++j) {
            authSignUI.addAcctLevelCustomer(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(j, 1)));
        }
    }

    private void removedJointAcctCustIDNAuthSignatory() {
        int borrowerTabRowCount = tblAct_Joint.getRowCount();
        for (int i = borrowerTabRowCount - 1, j = 1; i >= 1; --i, ++j) {
            authSignUI.removeAcctLevelCustomer(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(j, 1)));
        }
    }
    private void cboCommAddrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCommAddrActionPerformed
        // TODO add your handling code here:
        final String ADDRTYPE = CommonUtil.convertObjToStr(((ComboBoxModel) (cboCommAddr.getModel())).getKeyForSelected());
        if (!ADDRTYPE.equalsIgnoreCase("")) {
            HashMap dataMap = new HashMap();
            dataMap.put("CUST_ID", txtCustomerIdAI.getText());
            dataMap.put("ADDR_TYPE", ADDRTYPE);
            final HashMap resultMap = observable.getCustAddrType(dataMap);
            lblCustValue.setText(CommonUtil.convertObjToStr(resultMap.get("Name")));
            lblDOBValue.setText(DateUtil.getStringDate((java.util.Date) resultMap.get("DOB")));
            lblStreetValue.setText(CommonUtil.convertObjToStr(resultMap.get("STREET")));
            lblAreaValue.setText(CommonUtil.convertObjToStr(resultMap.get("AREA")));
            lblCityValue.setText(CommonUtil.convertObjToStr(resultMap.get("CITY1")));
            lblStateValue.setText(CommonUtil.convertObjToStr(resultMap.get("STATE1")));
            lblCountryValue.setText(CommonUtil.convertObjToStr(resultMap.get("COUNTRY1")));
            lblPinValue.setText(CommonUtil.convertObjToStr(resultMap.get("PIN_CODE")));
            dataMap = null;
        }
    }//GEN-LAST:event_cboCommAddrActionPerformed

    private void cboProductIdAIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductIdAIItemStateChanged
        // TODO add your handling code here:
        final String ProductID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProductIdAI.getModel())).getKeyForSelected());
        lblActHeadValueAI.setText(observable.getAccountHeadForProductId(ProductID));
        lblActHeadValueAI.setToolTipText(lblActHeadValueAI.getText());

        if (ProductID.length() > 0) {
            acountParamMap = observable.getAccountParamData(ProductID);
            acountCardsMap = observable.getAccountCardsData(ProductID);
            acountChargesMap = observable.getAccountChargesData(ProductID);
            acountCreditMap = observable.getAccountCreditData(ProductID);

            minNominee = CommonUtil.convertObjToStr(acountParamMap.get("NO_OF_NOMINEE"));
            //            nomineeUi.setMinNominee(CommonUtil.convertObjToStr(acountParamMap.get("NO_OF_NOMINEE")));
            nomineeUi.setMaxNominee(CommonUtil.convertObjToInt(acountParamMap.get("NO_OF_NOMINEE")));
            //Added By Suresh R     25-Nov-2015     Refferred By Abi
            if (CommonUtil.convertObjToStr(acountCardsMap.get("ATM_CARD_ISSUED")).equalsIgnoreCase("Y")) {    //Added By Suresh Refferred by Abi 25-Nov-2015
                lblCardActNumber.setVisible(true);
                txtCardActNumber.setVisible(true);
                tabAccounts.addTab("ATM A/c Card Details", panATMCardDetails);
            } else {
                lblCardActNumber.setVisible(false);
                txtCardActNumber.setVisible(false);
                txtCardActNumber.setText("");
                lblATMCardNoVal.setText("");
                tabAccounts.remove(panATMCardDetails);
                observable.resetCardTbl();
            }
            if ((observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)
                    || (observable.getOperation() == ClientConstants.ACTIONTYPE_NEWTI)) {
                /*
                 * Fetching the Data from acountParamMap...
                 */
                if (CommonUtil.convertObjToStr(acountParamMap.get("CHK_ALLOWED")).equalsIgnoreCase("N")) {
                    chkChequeBookAD.setSelected(false);
                } else {
                    chkChequeBookAD.setSelected(true);
                }
                cboStmtFreqAD.setSelectedItem(((ComboBoxModel) cboStmtFreqAD.getModel()).getDataForKey(
                        (CommonUtil.convertObjToStr(acountParamMap.get("STAT_FREQUENCY")))));
                //                cboStmtFreqAD.setEnabled(false);
                if ((CommonUtil.convertObjToStr(acountParamMap.get("TAX_INT_APPLICABLE")).equalsIgnoreCase("N"))) {
                    chkNROStatusAD.setSelected(false);
                    chkNROStatusAD.setEnabled(false);
                }
                //                else{

                //                    chkNROStatusAD.setSelected(true);
                //                    chkNROStatusAD.setEnabled(true);
                //                }

                /*
                 * Fetching the Data from acountCardsMap...
                 */
                if (CommonUtil.convertObjToStr(acountCardsMap.get("ATM_CARD_ISSUED")).equalsIgnoreCase("N")) {
                    chkATMAD.setSelected(false);
                    chkATMAD.setEnabled(false);
                    //__________________________
                    txtATMNoAD.setEnabled(false);
                    dtdATMToDateAD.setEnabled(false);
                    dtdATMFromDateAD.setEnabled(false);

                }
                //                else{
                //                    chkATMAD.setSelected(true);
                //                }
                chkATMADActionPerformed(null);

                if (CommonUtil.convertObjToStr(acountCardsMap.get("CR_CARD_ISSUED")).equalsIgnoreCase("N")) {
                    chkCreditAD.setSelected(false);
                    chkCreditAD.setEnabled(false);

                    //____________________________
                    txtCreditNoAD.setEnabled(false);
                    dtdCreditFromDateAD.setEnabled(false);
                    dtdCreditToDateAD.setEnabled(false);
                }
                //                else{
                //                    chkCreditAD.setSelected(true);
                //                }

                chkCreditADActionPerformed(null);

                if (CommonUtil.convertObjToStr(acountCardsMap.get("DR_CARD_ISSUED")).equalsIgnoreCase("N")) {
                    chkDebitAD.setSelected(false);
                    chkDebitAD.setEnabled(false);
                    //_____________________________
                    txtDebitNoAD.setEnabled(false);
                    dtdDebitToDateAD.setEnabled(false);
                    dtdDebitFromDateAD.setEnabled(false);
                }
                //                else{
                //                    chkDebitAD.setSelected(true);
                //                }
                chkDebitADActionPerformed(null);
                if (CommonUtil.convertObjToStr(acountCardsMap.get("MOBILE_BANKING")).equalsIgnoreCase("N")) {
                    chkMobileBankingAD.setSelected(false);
                    chkMobileBankingAD.setEnabled(false);
                } else {
                    chkMobileBankingAD.setSelected(true);
                    chkMobileBankingAD.setEnabled(true);
                }
                if (CommonUtil.convertObjToStr(acountCardsMap.get("ANY_BRANCH_BANKING")).equalsIgnoreCase("N")) {
                    chkABBChrgAD.setSelected(false);
                } else {
                    chkABBChrgAD.setSelected(true);
                    txtABBChrgAD.setText(CommonUtil.convertObjToStr(acountCardsMap.get("MIN_BAL_ABB")));
                }
                chkABBChrgADActionPerformed(null);
                if (CommonUtil.convertObjToStr(acountCardsMap.get("LINKED_FLEXI_ACCT")).equalsIgnoreCase("N")) {
                    chkFlexiAD.setSelected(false);
                    ClientUtil.enableDisable(panFlexiOpt, false);

                    //___________________________
                    //                    txtMinBal1FlexiAD.setEnabled(false);
                    //                    txtMinBal2FlexiAD.setEnabled(false);
                    //                    txtReqFlexiPeriodAD.setEnabled(false);
                }
                //                else{
                //                    //ClientUtil.enableDisable(panFlexiOpt,true);
                //                    chkFlexiAD.setSelected(true);
                //                    txtMinBal1FlexiAD.setText(CommonUtil.convertObjToStr(acountCardsMap.get("MIN_BAL1_FLEXIDEPOSIT")));
                //                    txtMinBal2FlexiAD.setText(CommonUtil.convertObjToStr(acountCardsMap.get("MIN_BAL2_FLEXIDEPOSIT")));
                //                }
                chkFlexiADActionPerformed(null);
                /*
                 * Fetching the Data from acountChargesMap...
                 */
                if (CommonUtil.convertObjToDouble(acountChargesMap.get("INOPERATIVE_AC_CHARGES")).doubleValue() > 0) {
                    chkInopChrgAD.setSelected(true);
                    chkInopChrgAD.setEnabled(true);
                } else {
                    chkInopChrgAD.setSelected(false);
                    chkInopChrgAD.setEnabled(false);
                }
                if (CommonUtil.convertObjToStr(acountCardsMap.get("FOLIO_CHG_APPLICABLE")).equalsIgnoreCase("Y")) {
                    txtFolioChrgAD.setText(CommonUtil.convertObjToStr(acountChargesMap.get("RATE_PER_FOLIO")));
                } else {
                    txtFolioChrgAD.setText("");
                    txtFolioChrgAD.setEditable(false);
                    txtFolioChrgAD.setEnabled(false);
                }
                if (CommonUtil.convertObjToStr(acountChargesMap.get("NONMAIN_MIN_BAL_CHG")).equalsIgnoreCase("N")) {
                    chkNonMainMinBalChrgAD.setSelected(false);
                    chkNonMainMinBalChrgAD.setEnabled(false);
                    txtMinActBalanceAD.setText("");
                    txtMinActBalanceAD.setEditable(false);
                    txtMinActBalanceAD.setEnabled(false);
                } else {
                    chkNonMainMinBalChrgAD.setSelected(true);
                    chkNonMainMinBalChrgAD.setEnabled(true);
                    txtMinActBalanceAD.setText(CommonUtil.convertObjToStr(acountChargesMap.get("AMT_NONMAIN_MINBAL")));
                }
                chkNonMainMinBalChrgADActionPerformed(null);
                if (CommonUtil.convertObjToStr(acountChargesMap.get("STOP_PAYMENT_CHG")).equalsIgnoreCase("N")) {
                    chkStopPmtChrgAD.setSelected(false);
                    chkStopPmtChrgAD.setEnabled(false);
                } else {
                    chkStopPmtChrgAD.setSelected(true);
                    chkStopPmtChrgAD.setEnabled(true);
                }
                if (CommonUtil.convertObjToStr(acountChargesMap.get("STAT_CHARGE")).equalsIgnoreCase("N")) {
                    chkStmtChrgAD.setSelected(false);
                    chkStmtChrgAD.setEnabled(false);
                } else {
                    chkStmtChrgAD.setSelected(true);
                    chkStmtChrgAD.setEnabled(true);
                }
                if (CommonUtil.convertObjToStr(acountChargesMap.get("PASS_BOOK")).equalsIgnoreCase("N")) {
                    chkPassBook.setSelected(false);
                    chkPassBook.setEnabled(false);
                } else {
                    chkPassBook.setSelected(true);
                    chkPassBook.setEnabled(true);
                }
                if (CommonUtil.convertObjToStr(acountChargesMap.get("CHK_ISSUE_CHG")).equalsIgnoreCase("N")) {
                    txtChequeBookChrgAD.setText("");
                    txtChequeBookChrgAD.setEditable(false);
                    txtChequeBookChrgAD.setEnabled(false);
                } else {
                    txtChequeBookChrgAD.setText(CommonUtil.convertObjToStr(acountChargesMap.get("CHK_ISSUE_CHGPERLEAF")));
                }
                if (Double.parseDouble(CommonUtil.convertObjToStr(acountChargesMap.get("CHK_RETURN_CHG_INWARD"))) > 0) {
                    chkChequeRetChrgAD.setSelected(true);
                    chkChequeRetChrgAD.setEnabled(true);
                } else {
                    chkChequeRetChrgAD.setSelected(false);
                    chkChequeRetChrgAD.setEnabled(false);
                }
                txtExcessWithChrgAD.setText(CommonUtil.convertObjToStr(acountChargesMap.get("CHG_EXCESSFREEWD_PERTRANS")));
                txtMisServiceChrgAD.setText(CommonUtil.convertObjToStr(acountChargesMap.get("MISC_SERVICE_CHG")));
                txtAccOpeningChrgAD.setText(CommonUtil.convertObjToStr(acountChargesMap.get("ACCT_OPENING_CHG")));
                txtAccCloseChrgAD.setText(CommonUtil.convertObjToStr(acountChargesMap.get("ACCT_CLOSING_CHG")));
                /*
                 * Fetching the Data from acountCreditMap...
                 */
                if ((CommonUtil.convertObjToStr(acountCreditMap.get("CREDIT_INT_GIVEN")).equalsIgnoreCase("N"))) {
                    chkPayIntOnCrBalIN.setSelected(false);
                    chkPayIntOnCrBalIN.setEnabled(false);
                    chkPayIntOnDrBalIN.setSelected(false);
                    chkPayIntOnDrBalIN.setEnabled(false);

                } else {
                    chkPayIntOnCrBalIN.setSelected(true);
                    chkPayIntOnCrBalIN.setEnabled(true);
                    chkPayIntOnDrBalIN.setSelected(true);
                    chkPayIntOnDrBalIN.setEnabled(true);
                }
                if (acountParamMap.containsKey("STAFF_ACCT_OPENED")) {
                    String staffYN = CommonUtil.convertObjToStr(acountParamMap.get("STAFF_ACCT_OPENED"));
                    if (staffYN.equalsIgnoreCase("Y")) {
                        observable.setStaffOnly(staffYN);
                    } else {
                        observable.setStaffOnly("");
                    }
                }
            }
            this.txtODLimitAI.setEnabled(true);
            ClientUtil.enableDisable(this.panLastIntApp, true);
            this.chkNPAChrgAD.setEnabled(true);
            this.dtdNPAChrgAD.setEnabled(true);
            if (((String) acountParamMap.get("LMT")).equalsIgnoreCase("N")
                    && ((String) acountParamMap.get("TOD")).equalsIgnoreCase("N")) {
                this.txtODLimitAI.setEnabled(false);
                ClientUtil.enableDisable(this.panLastIntApp, false);
                this.chkNPAChrgAD.setEnabled(false);
                this.dtdNPAChrgAD.setEnabled(false);
            } 
            
            
            // Added by nithya on 04-05-2016 for 4384
            
            if(((String) acountParamMap.get("TOD")).equalsIgnoreCase("Y")){ 
                txtODLimitAI.setEditable(false);
                panSecurityDetails.setVisible(true);
                tabAccounts.add(panSecurityDetails);
                observable.setIsSbOD("Y"); // Added by nithya on 19.08.2016
                tabAccounts.setTitleAt(tabAccounts.getTabCount()-1, "Security Details");                  
            }else{
              panSecurityDetails.setVisible(false);  
              tabAccounts.remove(panSecurityDetails);  
              observable.setIsSbOD("N"); // Added by nithya on 19.08.2016
              txtODLimitAI.setEditable(true);
              txtODLimitAI.setEnabled(true);
            }
            
            // End
            
            ClientUtil.enableDisable(panFlexiOpt, true);
            if (CommonUtil.convertObjToStr(acountCardsMap.get("LINKED_FLEXI_ACCT")).equalsIgnoreCase("N")) {
                chkFlexiAD.setSelected(false);
                ClientUtil.enableDisable(panFlexiOpt, false);
            }
            //            chkBoxDisableEnable();
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                //                if(!CommonUtil.convertObjToStr(acountCardsMap.get("ATM_CARD_ISSUED")).equalsIgnoreCase("N")){
                //                    chkATMAD.setEnabled(true);
                //                }else{
                //                    txtATMNoAD.setEnabled(false);
                //                    dtdATMToDateAD.setEnabled(false);
                //                    dtdATMFromDateAD.setEnabled(false);
                //                }
                //
                //
                //
                //                if(!CommonUtil.convertObjToStr(acountCardsMap.get("CR_CARD_ISSUED")).equalsIgnoreCase("N")){
                //                    chkCreditAD.setEnabled(true);
                //                }else{
                //                    txtCreditNoAD.setEnabled(false);
                //                    dtdCreditFromDateAD.setEnabled(false);
                //                    dtdCreditToDateAD.setEnabled(false);
                //                }
                //
                //
                //                if(!CommonUtil.convertObjToStr(acountCardsMap.get("DR_CARD_ISSUED")).equalsIgnoreCase("N")){
                //                    chkDebitAD.setEnabled(true);
                //                }else{
                //                    txtDebitNoAD.setEnabled(false);
                //                    dtdDebitToDateAD.setEnabled(false);
                //                    dtdDebitFromDateAD.setEnabled(false);
                //                }
                if (CommonUtil.convertObjToStr(acountCardsMap.get("ATM_CARD_ISSUED")).equalsIgnoreCase("N")) {
                    chkATMAD.setSelected(false);
                    chkATMAD.setEnabled(false);
                    //__________________________
                    txtATMNoAD.setEnabled(false);
                    dtdATMToDateAD.setEnabled(false);
                    dtdATMFromDateAD.setEnabled(false);
                }
                if (CommonUtil.convertObjToStr(acountCardsMap.get("CR_CARD_ISSUED")).equalsIgnoreCase("N")) {
                    chkCreditAD.setSelected(false);
                    chkCreditAD.setEnabled(false);
                    //____________________________
                    txtCreditNoAD.setEnabled(false);
                    dtdCreditFromDateAD.setEnabled(false);
                    dtdCreditToDateAD.setEnabled(false);
                }
                if (CommonUtil.convertObjToStr(acountCardsMap.get("DR_CARD_ISSUED")).equalsIgnoreCase("N")) {
                    chkDebitAD.setSelected(false);
                    chkDebitAD.setEnabled(false);

                    //_____________________________
                    txtDebitNoAD.setEnabled(false);
                    dtdDebitToDateAD.setEnabled(false);
                    dtdDebitFromDateAD.setEnabled(false);
                }
                if (CommonUtil.convertObjToStr(acountCardsMap.get("MOBILE_BANKING")).equalsIgnoreCase("N")) {
                    chkMobileBankingAD.setEnabled(false);
                    txtMobileNo.setEnabled(false);
                    tdtMobileSubscribedFrom.setEnabled(false);
                } else {
                    chkMobileBankingAD.setEnabled(true);
                    txtMobileNo.setEnabled(true);
                    tdtMobileSubscribedFrom.setEnabled(true);
                }
                if (!(CommonUtil.convertObjToStr(acountParamMap.get("TAX_INT_APPLICABLE")).equalsIgnoreCase("N"))) {
                    chkNROStatusAD.setEnabled(true);
                } else {
                    chkNROStatusAD.setEnabled(false);
                    chkNROStatusAD.setSelected(false);
                }

                if (!(CommonUtil.convertObjToStr(acountCreditMap.get("CREDIT_INT_GIVEN")).equalsIgnoreCase("N"))) {
                    chkPayIntOnCrBalIN.setEnabled(true);
                    chkPayIntOnDrBalIN.setEnabled(true);
                } else {
                    chkPayIntOnCrBalIN.setEnabled(false);
                    chkPayIntOnDrBalIN.setEnabled(false);
                    chkPayIntOnCrBalIN.setSelected(false);
                    chkPayIntOnDrBalIN.setSelected(false);
                }

                if (CommonUtil.convertObjToDouble(acountChargesMap.get("INOPERATIVE_AC_CHARGES")).doubleValue() > 0) {
                    chkInopChrgAD.setEnabled(true);
                } else {
                    chkInopChrgAD.setSelected(false);
                    chkInopChrgAD.setEnabled(false);
                }

                if (CommonUtil.convertObjToDouble(acountChargesMap.get("CHK_RETURN_CHG_INWARD")).doubleValue() > 0) {
                    chkChequeRetChrgAD.setEnabled(true);
                } else {
                    chkChequeRetChrgAD.setSelected(false);
                    chkChequeRetChrgAD.setEnabled(false);
                }

                if (((String) acountChargesMap.get("NONMAIN_MIN_BAL_CHG")).equalsIgnoreCase("N")) {
                    chkNonMainMinBalChrgAD.setEnabled(false);
                    txtMinActBalanceAD.setEditable(false);
                    txtMinActBalanceAD.setEnabled(false);
                } else {
                    chkNonMainMinBalChrgAD.setEnabled(true);
                    txtMinActBalanceAD.setEditable(true);
                    txtMinActBalanceAD.setEnabled(true);
                }

                if (CommonUtil.convertObjToStr(acountCardsMap.get("FOLIO_CHG_APPLICABLE")).equalsIgnoreCase("Y")) {
                    txtFolioChrgAD.setEnabled(true);
                    txtFolioChrgAD.setEditable(true);
                } else {
                    txtFolioChrgAD.setEditable(false);
                    txtFolioChrgAD.setEnabled(false);
                }

                if (CommonUtil.convertObjToStr(acountChargesMap.get("STOP_PAYMENT_CHG")).equalsIgnoreCase("N")) {
                    chkStopPmtChrgAD.setSelected(false);
                } else {
                    chkStopPmtChrgAD.setSelected(true);
                }

                if (CommonUtil.convertObjToStr(acountChargesMap.get("STAT_CHARGE")).equalsIgnoreCase("N")) {
                    chkStmtChrgAD.setEnabled(false);
                    chkStmtChrgAD.setSelected(false);
                } else {
                    chkStmtChrgAD.setEnabled(true);
                }

                if (CommonUtil.convertObjToStr(acountChargesMap.get("PASS_BOOK")).equalsIgnoreCase("N")) {
                    chkPassBook.setEnabled(false);
                    chkPassBook.setSelected(false);
                } else {
                    chkPassBook.setEnabled(true);
                }

                if (CommonUtil.convertObjToStr(acountChargesMap.get("CHK_ISSUE_CHG")).equalsIgnoreCase("N")) {
                    txtChequeBookChrgAD.setText("");
                    txtChequeBookChrgAD.setEditable(false);
                    txtChequeBookChrgAD.setEnabled(false);
                } else {
                    txtChequeBookChrgAD.setEditable(true);
                    txtChequeBookChrgAD.setEnabled(true);
                }
            }
            List chargeList = null;
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            whereMap.put("PRODUCT_ID", (String) ((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            chargeList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap));
            if (chargeList != null && chargeList.size() > 0) {
                String accountClosingCharge = CommonUtil.convertObjToStr((chargeList.get(0)));
                txtNextAccNo.setText(String.valueOf(accountClosingCharge));
            }
            chargeList = null;


        }
        ClientUtil.enableDisable(this.panLastIntApp, true);
        if (CommonUtil.convertObjToStr(this.cboActStatusAI.getSelectedItem()).equalsIgnoreCase("NEW")) {
            ClientUtil.enableDisable(this.panLastIntApp, false);
            //this.panLastIntApp.setEnabled(false);
        }

        /* we have to update the product interest values also */
        if (!lblActHeadValueAI.getText().equals("")) {
            updateProductInterestRates();
        }
    }//GEN-LAST:event_cboProductIdAIItemStateChanged

    private void tblAct_JointMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAct_JointMousePressed
        String accType = CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 3));
        if (accType.equals("Main")) {
            btnCustDetDelete.setVisible(false);
            btnToMain.setVisible(false);
            btnCustDetNew.setEnabled(true);
        } else if (accType.equals("Joint")) {
            btnCustDetDelete.setVisible(true);
            btnToMain.setVisible(true);
            btnCustDetNew.setEnabled(true);
        }
        //        if(!(viewType.equals("Delete") || !viewType.equals("AUTHORIZE")|| cboConstitutionAI.getSelectedItem().equals("Individual"))){ //--- If it is not in Delete mode, select the row
        if (!(cboConstitutionAI.getSelectedItem().equals("Individual"))) { //--- If it is not in Delete mode, select the row
            tblAct_JointRowSelected(tblAct_Joint.getSelectedRow());
        }
        if (viewType.equals("AUTHORIZE") || viewType.equals("EXCEPTION") || viewType.equals("REJECT") || viewType.equals("Enquirystatus")) {
            setBtnJointAccnt(false);
        }
        if (tblAct_Joint.getSelectedRowCount() > 0 && evt.getClickCount() == 2) {
            new CustomerDetailsScreenUI(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1))).show();
        }
        
    }//GEN-LAST:event_tblAct_JointMousePressed
    private void tblAct_JointRowSelected(int rowSelected) {
        if (tblAct_Joint.getSelectedRow() > (-1)) {
            if (!(viewType.equals("Delete") || viewType.equals("AUTHORIZE") || viewType.equals("Enquirystatus"))) {
                setBtnJointAccnt(true);
            } 
            else {
                setBtnJointAccnt(false);
                btnCustDetNew.setEnabled(false);
            }
        }
        HashMap cust = new HashMap();
        cust.put("CUST_ID", tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1));
        observable.populateScreen(cust, true, poaUI.getPowerOfAttorneyOB());
        updateCustomerDetails();
        cust = null;
    }
    private void btnToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToMainActionPerformed
        updateOBFields();
        setBtnJointAccnt(false);
        btnCustDetNew.setEnabled(true);
        observable.moveToMain(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1)), tblAct_Joint.getSelectedRow());
        observable.notifyObservers();
        // Add your handling code here:
    }//GEN-LAST:event_btnToMainActionPerformed

    private void btnCustDetDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustDetDeleteActionPerformed
        updateOBFields();
        viewType = "tblDelete";
        if (poaUI.checkCustIDExistInJointAcctAndPoA(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1)))) {

            //__ Changes in AuthSignatory...
            String strCustIDToDel = CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1));
            //            observableBorrow.deleteJointAccntHolder(strCustIDToDel, tblBorrowerTabCTable.getSelectedRow());

            //__ Changes in AuthSignatory...            
            observable.delJointAccntHolder(strCustIDToDel, tblAct_Joint.getSelectedRow(), poaUI.getPowerOfAttorneyOB());
            authSignUI.removeAcctLevelCustomer(strCustIDToDel);

            setBtnJointAccnt(false);
            btnCustDetNew.setEnabled(true);
            observable.resetCustDetails();
            observable.notifyObservers();
        }
    }//GEN-LAST:event_btnCustDetDeleteActionPerformed

    private void btnCustDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustDetNewActionPerformed
        if (tblAct_Joint.getRowCount() != 0) { //--- If the Main Accnt Holder is selected,
//            callView("JointAccount");               //--- allow the user to add Jnt Acct Holder
            viewType = "JointAccount";
            new CheckCustomerIdUI(this);

        } else {  //--- else if the Main Acct Holder is not selected, prompt the user to select
            observable.checkMainAcctHolder("selectMainAccntHolder"); //--- the Main Acct. holder
            btnAccNum.requestFocus(true);
        }
        // Add your handling code here:
    }//GEN-LAST:event_btnCustDetNewActionPerformed

    private void checkJointAccntHolderForData() {
        if ((tblAct_Joint.getRowCount() > 1) && (dontShowJointDialog == false)) {
            int reset = observable.showDialog("dialogForJointAccntHolder");
            if (reset == yes) { //--- If Yes, disable Joint Account Holder Tab
                //__ Changes in AuthSignatory...
                removedJointAcctCustIDNAuthSignatory();

                observable.resetJntAccntHoldTbl();
                CustInfoDisplay(txtCustomerIdAI.getText());
                setBtnJointAccnt(false);
                updateCustomerDetails();
                tblAct_Joint.setModel(observable.getTblJointAccnt());
                //                    observable.notifyObservers();
            } else if (reset == no) { //--- If No, don't isable Joint Account Holder Tab.
                observable.setCboConstitutionAI("Joint");
                cboConstitutionAI.setSelectedItem(observable.getCboConstitutionAI());
            }
        } else {
            setBtnJointAccnt(false);
        }
    }
    private void cboConstitutionAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboConstitutionAIActionPerformed
        //--- If some data is seleted, check whether it is Joint Account
        observable.setCboConstitutionAI((String) cboConstitutionAI.getSelectedItem());
        if ((cboConstitutionAI.getSelectedItem() != null) && (!cboConstitutionAI.getSelectedItem().equals(""))) {
            //--- If Selected data is "Joint Account", enable the New Button
            if (cboConstitutionAI.getSelectedItem().equals("Joint")) {
                btnCustDetNew.setEnabled(true);
            } else {
                checkJointAccntHolderForData();
            }
            if (!cboConstitutionAI.getSelectedItem().equals("Corporate")) {
                cboGroupCodeAI.setEnabled(false);
                cboGroupCodeAI.setSelectedItem("None");
                tabAccounts.add(nomineeUi, "Nominee", 4);
            } else {
                cboGroupCodeAI.setEnabled(true);
                cboGroupCodeAI.setSelectedItem("");
            }
            //--- Else if no data is seleted, disable the Buttons
        } else if (cboConstitutionAI.getSelectedItem()!= null && cboConstitutionAI.getSelectedItem().equals("")) {
            checkJointAccntHolderForData();
        }
        if (cboConstitutionAI.getSelectedItem()!= null && cboConstitutionAI.getSelectedItem().equals("Corporate")) {
            tabAccounts.remove(nomineeUi);
        }
        // Add your handling code here:
    }//GEN-LAST:event_cboConstitutionAIActionPerformed

    private void cboActStatusAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActStatusAIActionPerformed
        // Add your handling code here: ClientUtil.enableDisable(this.panLastIntApp,true);
        ClientUtil.enableDisable(this.panLastIntApp, true);
        if ((cboActStatusAI.getSelectedItem() != null) && (CommonUtil.convertObjToStr(cboActStatusAI.getSelectedItem()) != "")) {
            if (((String) this.cboActStatusAI.getSelectedItem()).equalsIgnoreCase("NEW")) {
                ClientUtil.enableDisable(this.panLastIntApp, false);
                //this.panLastIntApp.setEnabled(false);
            }
        }
    }//GEN-LAST:event_cboActStatusAIActionPerformed

    private void cboPreviousActNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPreviousActNoActionPerformed
        // Add your handling code here:
        //Changed BY Suresh
        if (!cboPreviousActNo.getSelectedItem().equals("") && cboPreviousActNo.getSelectedIndex() > 0) {
            observable.getPreviousAccountDetails();
        }
    }//GEN-LAST:event_cboPreviousActNoActionPerformed

    private void btnBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchCodeActionPerformed
        // Add your handling code here:
        callView("TransferBranch");
    }//GEN-LAST:event_btnBranchCodeActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);

        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EXCEPTION]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EXCEPTION]);
        nomineeUi.setActionType(AUTHORIZE);
        nomineeUi.disableNewButton(false);

        setBtnJointAccnt(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);

        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_REJECT]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_REJECT]);
        nomineeUi.setActionType(AUTHORIZE);
        nomineeUi.disableNewButton(false);

        setBtnJointAccnt(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    public void authorizeStatus(String auth) {
        authorizeActionPerformed(auth);
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);

        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_AUTHORIZE]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_AUTHORIZE]);
        nomineeUi.setActionType(AUTHORIZE);
        nomineeUi.disableNewButton(false);

        setBtnJointAccnt(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void getTransDetails(String actNum) {
        String displayStr = "";
        HashMap transTypeMap = new HashMap();
        HashMap transMap = new HashMap();
        HashMap transCashMap = new HashMap();
        transCashMap.put("BATCH_ID", actNum);
        transCashMap.put("TRANS_DT", currDt);
        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap transIdMap = new HashMap();
        List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
        if (list != null && list.size() > 0) {
            displayStr += "Transfer Transaction Details...\n";
            for (int i = 0; i < list.size(); i++) {
                transMap = (HashMap) list.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Batch Id : " + transMap.get("BATCH_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No : " + transMap.get("ACT_NUM")
                            + "   Deposit Amount : " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                            + "   Interest Amount : " + transMap.get("AMOUNT") + "\n";
                }
                transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                //System.out.println("#### :" + transMap);
//                                 oldBatchId = newBatchId;
            }
        }

        list = ClientUtil.executeQuery("getCashDetails", transCashMap);
        if (list != null && list.size() > 0) {
            displayStr += "Cash Transaction Details...\n";
            for (int i = 0; i < list.size(); i++) {
                transMap = (HashMap) list.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                            + "   Deposit Amount :  " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID")
                            + "   Interest Amount :  " + transMap.get("AMOUNT") + "\n";
                }
                transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
            }
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
    }

    private HashMap getTransDetailsEdit(String actNum) {
        String displayStr = "";
        HashMap transTypeMap = new HashMap();
        HashMap transMap = new HashMap();
        HashMap transCashMap = new HashMap();
        editTransMap = new HashMap();
        transCashMap.put("BATCH_ID", actNum);
        transCashMap.put("TRANS_DT", currDt);
        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap transIdMap = new HashMap();
        List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
        if (list != null && list.size() > 0) {
            displayStr += "Transfer Transaction Details...\n";
            for (int i = 0; i < list.size(); i++) {
                transMap = (HashMap) list.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Batch Id : " + transMap.get("BATCH_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No : " + transMap.get("ACT_NUM")
                            + "   Deposit Amount : " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                            + "   Interest Amount : " + transMap.get("AMOUNT") + "\n";
                }
                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                //System.out.println("#### :" + transMap);
//                                 oldBatchId = newBatchId;
            }
        }

        list = ClientUtil.executeQuery("getCashDetails", transCashMap);
        if (list != null && list.size() > 0) {
            displayStr += "Cash Transaction Details...\n";
            for (int i = 0; i < list.size(); i++) {
                transMap = (HashMap) list.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                            + "   Deposit Amount :  " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID")
                            + "   Interest Amount :  " + transMap.get("AMOUNT") + "\n";
                }
                transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
            }
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
        //System.out.println("transIdMap in trans details>>>>" + transIdMap + "transTypeMap>>" + transTypeMap);
        // return transIdMap;
        editTransMap.put("TRANSTYPE", transTypeMap);
        editTransMap.put("TRANSID", transIdMap);
        return editTransMap;
    }

    private void authorizeActionPerformed(String authStatus) {
        btnCustDetNew.setEnabled(false);
        if (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL])) {
            viewType = "";
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        if (!(viewType.equals("AUTHORIZE"))) {
            viewType = "AUTHORIZE";
            HashMap whereMap = new HashMap();
            HashMap mapParam = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectAccountMasterCashierAuthorizeTOList");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectAccountMasterAuthorizeTOList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAccountMaster");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            enableDisableAuthorizeBtns();
            btnSave.setEnabled(false);
            //__ If there's no data to be Authorized, call Cancel action...
            if (!isModified()) {
                mitCancelActionPerformed(null);
            }
        } else if (viewType.equals("AUTHORIZE")) {
            //Changed BY Suresh
//            String warningMessage = tabAccounts.isAllTabsVisited();
//            if(warningMessage.length() > 0){
//                displayAlert(warningMessage);
//            }else{
            //__ To reset the value of the visited tabs...
            tabAccounts.resetVisits();

            try {
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authStatus);
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("ACCOUNTNO", txtAccountNo.getText());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                singleAuthorizeMap.put("PROD_TYPE", "OA");
                singleAuthorizeMap.put("PROD_ID", observable.getCbmProductIdAI().getKeyForSelected());
                // Added by nithya for SBOD RBI changes
                HashMap todCheckmap = new HashMap();
                todCheckmap.put("PROD_ID", observable.getCbmProductIdAI().getKeyForSelected());
                List todList = ClientUtil.executeQuery("isTODSetForProduct", todCheckmap);
                if (todList != null && todList.size() > 0) {
                    HashMap todMap = (HashMap) todList.get(0);
                    if (todMap.containsKey("TEMP_OD_ALLOWED")) {
                        if (CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                            observable.setIsSbOD("Y");
                        }
                    }
                }   
                if(observable.getIsSbOD().equalsIgnoreCase("Y")){
                    String newOD = "";
                    HashMap odWhrMap = new HashMap();
                    odWhrMap.put("ACT_NUM",txtAccountNo.getText());
                    List sbODList = ClientUtil.executeQuery("checkIfTodAdded", odWhrMap);
                    if(sbODList != null && sbODList.size() > 0){
                        newOD = "Y";
                    }else{
                        newOD = "N";
                    }
                    //System.out.println("sbODList size"+ sbODList.size());
                    if(newOD.equalsIgnoreCase("Y")){
                       singleAuthorizeMap.put("SB_OD_PROD","SB_OD_PROD");
                    } 
                    if(chkODClose.isSelected()){
                        singleAuthorizeMap.put("SB_OD_PROD_CLOSE","SB_OD_PROD_CLOSE");
                    }
                }
                observable.setSingleAuthorizeMap(singleAuthorizeMap);

                observable.doAuthorize();
//                boolean b = ClientUtil.executeWithResult("authorizeAccountMaster", singleAuthorizeMap);
//                if(authStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)){
//                    ClientUtil.execute("removeJointActDetails",singleAuthorizeMap);
//                }
                HashMap resultStatus = new HashMap();
                resultStatus = observable.getProxyReturnMap();
                //System.out.println("@#$@#$@#$resultStatus:" + resultStatus);
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(txtAccountNo.getText());
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("SB/Current Account Opening");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    if(sbNode.equals("SB_NO_TRANSACTION")){
                        authorizeListUI.displayDetails("SB/Current AccountOpening");
                    }else{
                    authorizeListUI.displayDetails("SB/Current Account Opening");
                }
                }
                if (fromCashierAuthorizeUI) {
                    CashierauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    CashierauthorizeListUI.setFocusToTable();
                }
                if (fromManagerAuthorizeUI) {
                    ManagerauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    ManagerauthorizeListUI.setFocusToTable();
                }
                
                btnCancelActionPerformed(null);
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(txtAccountNo.getText());
//                if (b)  lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getOperation()]);

                if (resultStatus.containsKey("AUTHDATA")) {
                    String result = CommonUtil.convertObjToStr(resultStatus.get("AUTHDATA"));
                    //System.out.println("#@%#$%#$5result:" + result);
                    lblStatus.setText(result);
                }
                viewType = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void enableDisableAuthorizeBtns() {
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);

    }
    private void dtdCreditToDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdCreditToDateADFocusLost
        // Add your handling code here:

        ClientUtil.validateToDate(dtdCreditToDateAD, dtdCreditFromDateAD.getDateValue());
    }//GEN-LAST:event_dtdCreditToDateADFocusLost

    private void dtdCreditFromDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdCreditFromDateADFocusLost
        // Add your handling code here:

        ClientUtil.validateFromDate(dtdCreditFromDateAD, dtdCreditToDateAD.getDateValue());
    }//GEN-LAST:event_dtdCreditFromDateADFocusLost

    private void dtdDebitToDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdDebitToDateADFocusLost
        // Add your handling code here:

        ClientUtil.validateToDate(dtdDebitToDateAD, dtdDebitFromDateAD.getDateValue());
    }//GEN-LAST:event_dtdDebitToDateADFocusLost

    private void dtdDebitFromDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdDebitFromDateADFocusLost
        // Add your handling code here:

        ClientUtil.validateFromDate(dtdDebitFromDateAD, dtdDebitToDateAD.getDateValue());
    }//GEN-LAST:event_dtdDebitFromDateADFocusLost

    private void dtdATMToDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdATMToDateADFocusLost
        // Add your handling code here:
        ClientUtil.validateToDate(dtdATMToDateAD, dtdATMFromDateAD.getDateValue());
    }//GEN-LAST:event_dtdATMToDateADFocusLost

    private void dtdATMFromDateADFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdATMFromDateADFocusLost
        // Add your handling code here:
        ClientUtil.validateFromDate(dtdATMFromDateAD, dtdATMToDateAD.getDateValue());
    }//GEN-LAST:event_dtdATMFromDateADFocusLost

    private void txtBranchCodeAIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchCodeAIFocusLost
        // Add your handling code here:
        /* based on the branch code, get the branch name and display it.
         * this is an exceptional scenarion, we should not call update, because
         * calling envolve reading all the existing values and then updating
         * the observable data.
         * instead we can just set the branch code from this method itself.
         */
        lblBranchNameValueAI.setText(observable.getBranchNameForCode(txtBranchCodeAI.getText()));
    }//GEN-LAST:event_txtBranchCodeAIFocusLost

    private void chkNPAChrgADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNPAChrgADActionPerformed
        // Add your handling code here:
        /* we have to show the NPA charge date only if the corresponding
         * check box has been selected
         */
        dtdNPAChrgAD.setEnabled(chkNPAChrgAD.isSelected());
        dtdNPAChrgAD.setDateValue("");
    }//GEN-LAST:event_chkNPAChrgADActionPerformed

    private void chkABBChrgADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkABBChrgADActionPerformed
        // Add your handling code here:
        /* we have to show the ABBA charge text box only if the corresponding
         
         * check box has been selected
         */
        txtABBChrgAD.setEnabled(chkABBChrgAD.isSelected());
        if (!chkABBChrgAD.isSelected()) {
            txtABBChrgAD.setText("");
        }
    }//GEN-LAST:event_chkABBChrgADActionPerformed

    private void chkNonMainMinBalChrgADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNonMainMinBalChrgADActionPerformed
        // Add your handling code here:
        /* we have to show the Non maintenance of minimum balance charge text box
         * only if the corresponding check box has been selected
         */
        txtMinActBalanceAD.setEnabled(chkNonMainMinBalChrgAD.isSelected());
        if (!chkNonMainMinBalChrgAD.isSelected()) {
            txtMinActBalanceAD.setText("");
        }
    }//GEN-LAST:event_chkNonMainMinBalChrgADActionPerformed

    private void chkFlexiADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFlexiADActionPerformed
        // Add your handling code here:
        /* we have to show the Flexi minimum balance 1, minimum balance 2,
         * the required flexi period and the day/month/year value only if the
         * corresponding check box has been selected
         */
        boolean isSelected = chkFlexiAD.isSelected();
        txtMinBal1FlexiAD.setEnabled(isSelected);
        txtMinBal2FlexiAD.setEnabled(isSelected);
        txtReqFlexiPeriodAD.setEnabled(isSelected);
        //        cboDMYAD.setEnabled(isSelected);
        if (!isSelected) {
            txtMinBal1FlexiAD.setText("");
            txtMinBal2FlexiAD.setText("");
            txtReqFlexiPeriodAD.setText("");
            //            cboDMYAD.setSelectedIndex(0);
        } else {
            HashMap productMap = new HashMap();
            String PROD_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            productMap.put("PROD_ID", PROD_ID);
            List lst = ClientUtil.executeQuery("getProductFlext.Account", productMap);
            if (lst != null && lst.size() > 0) {
                productMap = (HashMap) lst.get(0);
                txtMinBal1FlexiAD.setEnabled(!isSelected);
                txtMinBal2FlexiAD.setEnabled(!isSelected);
                txtReqFlexiPeriodAD.setEnabled(isSelected);
                txtMinBal1FlexiAD.setText(CommonUtil.convertObjToStr(productMap.get("MIN_BAL1")));
                txtMinBal2FlexiAD.setText(CommonUtil.convertObjToStr(productMap.get("MIN_BAL2")));
            }
        }
    }//GEN-LAST:event_chkFlexiADActionPerformed

    private void chkCreditADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditADActionPerformed
        // Add your handling code here:
        /* we have to enable the Credit card No., text field and the validity date
         * only when the user selected the credit card option
         */
        boolean isSelected = chkCreditAD.isSelected();

        //        txtCreditNoAD.setEnabled(isSelected);
        //        dtdCreditFromDateAD.setEnabled(isSelected);
        //        dtdCreditToDateAD.setEnabled(isSelected);
        if (!isSelected) {
            txtCreditNoAD.setText("");
            dtdCreditFromDateAD.setDateValue("");
            dtdCreditToDateAD.setDateValue("");
        } else {
            txtCreditNoAD.setEnabled(isSelected);
            dtdCreditFromDateAD.setEnabled(isSelected);
            dtdCreditToDateAD.setEnabled(isSelected);
        }
    }//GEN-LAST:event_chkCreditADActionPerformed

    private void chkDebitADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDebitADActionPerformed
        // Add your handling code here:
        /* we have to enable the debit card No., text field and the validity date
         * only when the user selected the debit card option
         */
        boolean isSelected = chkDebitAD.isSelected();
        if (isSelected) {
            txtDebitNoAD.setEnabled(isSelected);
            dtdDebitToDateAD.setEnabled(isSelected);
            dtdDebitFromDateAD.setEnabled(isSelected);
        } else {
            //            txtDebitNoAD.setEnabled(isSelected);
            //            dtdDebitToDateAD.setEnabled(isSelected);
            //            dtdDebitFromDateAD.setEnabled(isSelected);
            txtDebitNoAD.setText("");
            dtdDebitToDateAD.setDateValue("");
            dtdDebitFromDateAD.setDateValue("");
        }
    }//GEN-LAST:event_chkDebitADActionPerformed

    private void chkATMADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkATMADActionPerformed
        // Add your handling code here:
        /* we have to enable the ATM No., text field and the validity date
         * only when the user selected the ATM card option
         */
        boolean isSelected = chkATMAD.isSelected();
        //        txtATMNoAD.setEnabled(isSelected);
        //        dtdATMToDateAD.setEnabled(isSelected);
        //        dtdATMFromDateAD.setEnabled(isSelected);
        if (!isSelected) {
            txtATMNoAD.setText("");
            dtdATMToDateAD.setDateValue("");
            dtdATMFromDateAD.setDateValue("");
        } else {
            txtATMNoAD.setEnabled(isSelected);
            dtdATMToDateAD.setEnabled(isSelected);
            dtdATMFromDateAD.setEnabled(isSelected);
        }
    }//GEN-LAST:event_chkATMADActionPerformed

    private void btnAccNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNumActionPerformed

        String prodId = CommonUtil.convertObjToStr(cboProductIdAI.getSelectedItem());
        viewType ="Account Number For Edit";
        //system.out.println("prodIdprodId test"+prodId);
        if (prodId.length() > 0) {
            //system.out.println("testfinal");
            //  resetUIForm
           // viewType="Account Number For Edit";
            popUp();
        }


    }//GEN-LAST:event_btnAccNumActionPerformed

    private void chkBoxDisableEnable() {
        if (!this.chkATMAD.isSelected()) {
            this.chkATMAD.setEnabled(false);
            this.txtATMNoAD.setEnabled(false);
            this.dtdATMFromDateAD.setEnabled(false);
            this.dtdATMToDateAD.setEnabled(false);
        } else {
            this.chkATMAD.setEnabled(true);
            this.txtATMNoAD.setEnabled(true);
            this.dtdATMFromDateAD.setEnabled(true);
            this.dtdATMToDateAD.setEnabled(true);
        }
        if (!this.chkCreditAD.isSelected()) {
            this.txtCreditNoAD.setEnabled(false);
            this.chkCreditAD.setEnabled(false);
            this.dtdCreditFromDateAD.setEnabled(false);
            this.dtdCreditToDateAD.setEnabled(false);
        } else {
            this.txtCreditNoAD.setEnabled(true);
            this.chkCreditAD.setEnabled(true);
            this.dtdCreditFromDateAD.setEnabled(true);
            this.dtdCreditToDateAD.setEnabled(true);
        }
        if (!this.chkDebitAD.isSelected()) {
            this.chkDebitAD.setEnabled(false);
            this.txtDebitNoAD.setEnabled(false);
            this.dtdDebitFromDateAD.setEnabled(false);
            this.dtdDebitToDateAD.setEnabled(false);
        } else {
            this.chkDebitAD.setEnabled(true);
            this.txtDebitNoAD.setEnabled(true);
            this.dtdDebitFromDateAD.setEnabled(true);
            this.dtdDebitToDateAD.setEnabled(true);
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnReportActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        btnAuthorize.setBorderPainted(false);
        btnAuthorize.setFocusPainted(false);
        btnAuthorize.setFocusable(false);
        btnAdd.setFocusable(true);
        mitCancelActionPerformed(evt);
        btnReject.setEnabled(true);
  
        btnException.setEnabled(true);
        btnDeletedDetails.setEnabled(true);
        txtClosedDt.setVisible(false);
        lblClosedDt.setVisible(false);
        txtEditOperativeNo.setText("");
        btnReport.setEnabled(true);
        accountOpeningAmountEnable(false);
        txtEditOperativeNo.setEnabled(true);
        btnAuthorize.setEnabled(true);
        ClientUtil.clearAll(this);
        setBtnCard(false);
        lblATMCardNoVal.setText("");
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        clearSecurityTab(); // Added by nithya
        //Added By Revathi.L
        btnAgentID.setEnabled(false);
        btnDealerID.setEnabled(false);
        txtLinkingActNum.setText("");
        txtAtmCardLimit.setText("");
        lblLinkingActNameValue.setText("");
        btnLinkingActNum.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private String getUPIMobileAccountNo(){
        String actNum = "";
        HashMap whereMap = new HashMap();
        whereMap.put("MOBILE_NO",txtUPIMobileNo.getText());
        List lst = ClientUtil.executeQuery("getUPIMobileNoExists", whereMap);
        if(lst != null && lst.size() > 0){
            whereMap = (HashMap)lst.get(0);
            actNum = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
        }
        return actNum;
    }
    
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT||viewType.equals("Edit For AccountNumber")) {
            
            //enhancement validation
            
            if(observable.getIsSbOD().equalsIgnoreCase("Y") && chkEnhance.isSelected()){
                if(!observable.isEnhanceValidate()){
                    ClientUtil.displayAlert("SB OD validation is not done. Please press Get OD Limit button for validation");
                    return;
                }
            }
            
            HashMap hashmap = new HashMap();
            if (tblAct_Joint != null && tblAct_Joint.getRowCount() > 0) {
                int row = tblAct_Joint.getRowCount();
                for (int i = 0; i < row; i++) {
                    String custid = CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(i, 1));
                    hashmap.put("CUST_ID", custid);
                    hashmap.put("MEMBER_NO", custid);
                    List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        ClientUtil.displayAlert("Customer is death marked please select another customerId");
                        return;
                    }
                }

            }
            if (poaUI.getTblRowCount() > 0) {
                for (int i = 0; i < poaUI.getTblRowCount(); i++) {
                    PowerOfAttorneyOB observablePoA = poaUI.getPowerOfAttorneyOB();
                    observablePoA.populatePoATable(i);
                    hashmap.put("CUST_ID", observablePoA.getTxtCustID_PoA());
                    hashmap.put("MEMBER_NO", observablePoA.getTxtCustID_PoA());
                    List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        ClientUtil.displayAlert("Customer is death marked please select another customerId");
                        return;
                    }

                }
            }
            if (authSignUI.getAuthorizedSignatoryRowCount() > 0) {
                for (int i = 0; i < authSignUI.getAuthorizedSignatoryRowCount(); i++) {
                    AuthorizedSignatoryOB observableAuthSign = authSignUI.getAuthorizedSignatoryOB();
                    observableAuthSign.populateAuthorizeTab(i);
                    hashmap.put("CUST_ID", observableAuthSign.getTxtCustomerID());
                    hashmap.put("MEMBER_NO", observableAuthSign.getTxtCustomerID());
                    List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        ClientUtil.displayAlert("Customer is death marked please select another customerId");
                        return;
                    }
                }
            }
            if (nomineeUi.getTblRowCount() > 0) {
                for (int i = 0; i < nomineeUi.getTblRowCount(); i++) {
                    NomineeOB observable = nomineeUi.getNomineeOB();
                    observable.populateNomineeTab(i);
                    hashmap.put("CUST_ID", observable.getLblCustNo());
                    hashmap.put("MEMBER_NO", observable.getLblCustNo());
                    List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        ClientUtil.displayAlert("Customer is death marked please select another customerId");
                        return;
                    }
                }
            }
        }
        
        
        if ((txtUPIMobileNo.getText().length() != 0) && (txtUPIMobileNo.getText().length() > 10 || txtUPIMobileNo.getText().length() < 10)) {
            ClientUtil.showAlertWindow("UPI/ATM Mobile not valid...");
            return;
        }

        if (txtUPIMobileNo.getText().length() == 10) {
            String actNum = getUPIMobileAccountNo();
            if (actNum.length() > 0) {
                ClientUtil.showAlertWindow("UPI/ATM Mobile No already linked with Account No : " + actNum);
                return;
            }
        }

        
        if(rdoKYCNormsYes.isSelected()){
            if(txtCardActNumber.getText().length() == 0){
                ClientUtil.showAlertWindow("Atm Linking Card Number should not be empty...");
                return;
            }
            String linkingProdId = CommonUtil.convertObjToStr(cboLinkingProductId.getSelectedItem());
            if(linkingProdId.length() == 0){
                ClientUtil.showAlertWindow("Atm Linking Product Id should not be empty...");
                return;
            }
            if(txtLinkingActNum.getText().length() == 0){
                ClientUtil.showAlertWindow("Atm Linking Act Num should not be empty...");
                return;
            }
            if(txtAtmCardLimit.getText().length() == 0){
                ClientUtil.showAlertWindow("Atm Card Limit Amount should not be empty...");
                return;
           }
            
//           if(txtUPIMobileNo.getText().length() == 0){
//                ClientUtil.showAlertWindow("UPI/ATM Mobile No Should not be empty...");
//                return;
//           } 
//           
//           if(txtUPIMobileNo.getText().length() > 10 || txtUPIMobileNo.getText().length() < 10){
//                ClientUtil.showAlertWindow("UPI/ATM Mobile not valid...");
//                return;
//           } 
//           
//           if(txtUPIMobileNo.getText().length() == 10){
//               String actNum = getUPIMobileAccountNo();
//               if(actNum.length() > 0){
//                  ClientUtil.showAlertWindow("UPI/ATM Mobile No already linked with Account No : " + actNum);
//                  return; 
//               }
//           }
           
        }
        mitSaveActionPerformed(evt);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        txtNextAccNo.setText("");
        txtExistingAcctNo.setText("");
        txtNextAccNo.setEnabled(false);
        clearSecurityTab(); // Added by nithya
        //observable.resetMemberTypeDetails(); // commented to follow design pattern
        //observable.resetBorrowerDetails(); // Commented to follow design pattern
    }//GEN-LAST:event_btnSaveActionPerformed

    private void clearSecurityTab(){
       txtBorrowerNetworth.setText("");
       txtBorrowerSalary.setText("");
       txtMemberName.setText("");
       txtMemberNetworth.setText("");
       txtMemberNo.setText("");
       txtMemberType.setText("");
       txtMemberSalary.setText("");
       txtContactNo.setText("");
       lblBorrowerDob.setText("");
       lblBorrowerRetireDt.setText("");
       lblBorrowerService.setText("");
       lblDob.setText("");
       lblRetireDt.setText("");
       lblService.setText("");
       chkRenew.setSelected(false);
       txtAppliedAmt.setText("");
       lblBorrEligAmt.setText("Elig Amount");   
       txtAgentID.setText("");
       txtDealerID.setText("");
       lblAgentIDVal.setText("");
       lblDealerIDVal.setText("");
       chkODClose.setSelected(false);// Added by nithya on 14-06-2019 
       // observable.resetMemberTypeTbl();   // commented to follow design pattern  
    }
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        mitEditNewActionPerformed(evt);
        tabAccounts.setSelectedComponent(panActInfo);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnAccNum.setEnabled(true);
        accountOpeningAmountEnable(false);
        lblAcctOpeningAmount.setEnabled(true);
        cboProductIdAI.setEnabled(true);
        lblProductIdAI.setEnabled(true);

        //Added BY Suresh
        txtAccountNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
        observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
        btnAccNum.setVisible(true);
        lblAccountNumber.setVisible(true);
        txtAccountNo.setVisible(true);
        chkEnhance.setEnabled(true);
        // Added by nithya on 11-08-2017 for 7453
        btnAgentID.setEnabled(true);
        btnDealerID.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        mitDeleteActionPerformed(evt);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        accountOpeningAmountEnable(false);
        txtNextAccNo.setText("");
        txtAcctOpeningAmount.setVisible(true);
        lblAcctOpeningAmount.setVisible(true);
        txtAcctOpeningAmount.setEnabled(false);
        lblAcctOpeningAmount.setEnabled(true);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
        if (!CommonUtil.convertObjToStr(TrueTransactMain.selBranch).equals("") && CommonUtil.convertObjToStr(TrueTransactMain.selBranch).length()>0 && 
        !TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
            ClientUtil.showMessageWindow("Interbranch Account creation not allowed for this screen...");
            TrueTransactMain.populateBranches();
            TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
            observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
            return;
        }else{
        mitAddNewActionPerformed(evt);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnDeletedDetails.setEnabled(true);
        panExistingCustomer.setVisible(true);
        lblExistingCustomer.setVisible(true);
        txtExistingAcctNo.setVisible(true);
        lblExistingAcctNo.setVisible(true);
        rdoExistingCustomer_Yes.setSelected(true);
        accountOpeningAmountEnable(true);
        txtEditOperativeNo.setEnabled(false);
        txtAccountNo.setEnabled(false);
        txtNextAccNo.setEnabled(false);
        lblAcctOpeningAmount.setEnabled(true);
        btnAccNum.setVisible(false);
        btnATMNew.setEnabled(true);
        lblATMCardNoVal.setText("");
        ClientUtil.enableDisable(panATMAccountCardDetails, false);
        setSuretyComponents();
        //Added By Revathi.L
        btnAgentID.setEnabled(true);
        btnDealerID.setEnabled(true);
        }
    }//GEN-LAST:event_btnAddActionPerformed
    public void accountOpeningAmountEnable(boolean acOpnFlag) {
        txtAcctOpeningAmount.setEnabled(acOpnFlag);
        lblAcctOpeningAmount.setEnabled(acOpnFlag);
        txtAcctOpeningAmount.setVisible(acOpnFlag);
        lblAcctOpeningAmount.setVisible(acOpnFlag);
        txtAcctOpeningAmount.setText("");
        transNew = acOpnFlag;
    }
    private void mitResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitResumeActionPerformed
        // Add your handling code here:
        // Just take the user to the firt editable field of the first tab
    }//GEN-LAST:event_mitResumeActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        //__ To reset the value of the visited tabs...
        if (observable.getAuthorizeStatus() != null) {
            super.removeEditLock(txtAccountNo.getText());
        }
        tabAccounts.resetVisits();

        //         if(observable.getAuthorizeStatus()!=null){
        //            super.removeEditLock(lblAccountNo.getText());
        //        }
        dontShowJointDialog = true;
        // Add your handling code here:
        // clear everything
        observable.resetCustDetails();
        observable.resetJntAccntHoldTbl();
        observable.resetCardTbl();
        observable.resetMemberTypeTbl();
        observable.resetOBFields();
        observable.resetLabels();
        // Disable all the screen
        ClientUtil.enableDisable(this, false);

        //To Disable the Nominee Panel after Cancel...
        //        enableDisableNominee_SaveDelete();
        //        btnNewNO.setEnabled(false);
        //To Disable the Nominee Panel after Cancel...
        ////        enableDisablePA_SaveDelete();
        //        btnNewPA.setEnabled(false);

        /*
         * To reset the POA, Introducer and Nominee Table
         */
        (poaUI.getPowerOfAttorneyOB()).resetAllFieldsInPoA();
        poaUI.setPoAToolBtnsEnableDisable(false);
        poaUI.setAllPoAEnableDisable(false);

        //__ To get back the Nominee Screen in case Corporate Cust was selected...
        tabAccounts.add(nomineeUi, "Nominee", 4);

        nomineeUi.resetTable();
        nomineeUi.resetNomineeData();
        nomineeUi.resetNomineeTab();
        nomineeUi.disableNewButton(false);

        authSignUI.resetAllFieldsInAuthTab();
        authSignUI.setAuthEnableDisable(false);
        authSignUI.setAllAuthInstEnableDisable(false);
        
        // Some other fields should also be disabled at init time
        enableDisableComponents(false);
        /*
         * To make The No of Nominees as null...
         */
        //        txtMinNominees.setText("");
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());

        // set the operation as "NOOP" and reload the menu and toolbar
        //        if (observable.getOperation()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
        //            observable.getOperation()!=ClientConstants.ACTIONTYPE_REJECT)
        observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
        setupMenuToolBarPanel();
        lblCustName.setText("");
        dontShowJointDialog = false;

        viewType = "";

        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_mitCancelActionPerformed
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private String checkMandatory(JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    private String periodLengthValidation(CTextField txtField, CComboBox comboField) {
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)) {
            message = objMandatoryRB.getString(txtField.getName());
        }
        return message;
    }

    private boolean insertTransactionPart() {
        HashMap singleAuthorizeMap = new HashMap();
        java.util.ArrayList arrList = new java.util.ArrayList();
        HashMap authDataMap = new HashMap();
        arrList.add(authDataMap);
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT /*&& observable.getAvailableBalance() >0 */) {
            //        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getAvailableBalance() >0){
            String[] debitType = {"Cash", "Transfer"};
            String[] obj4 = {"Yes", "No"};
            //            int option3 = COptionPane.showOptionDialog(null,("Do you want to make Transaction?"), ("Transaction"),
            //            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
//            int option3 = 0;
            double depoAmount = CommonUtil.convertObjToDouble(txtAcctOpeningAmount.getText()).doubleValue();
            if (depoAmount > 0) {
                String transType =(String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                authDataMap.put("TRANS_TYPE", transType.toUpperCase());
                String prodId = ((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected().toString();
                authDataMap.put("PROD_ID", prodId);
//                authDataMap.put("PROD_ID",CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));

                authDataMap.put("OPERATIVE_AMOUNT", String.valueOf(depoAmount));
                authDataMap.put("USER_ID", ProxyParameters.USER_ID);
//                if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")){
//                    boolean flag = true;
//                    do {
//                        String tokenNo = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_CASH_TRANS"));
//                        if (tokenNo != null && tokenNo.length()>0) {
//                            flag = tokenValidation(tokenNo);
//                        }
//                        if(flag == false){
//                            ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
//                        }else{
//                            authDataMap.put("TOKEN_NO",tokenNo);
//                        }
                //                        } else {
                //                            ClientUtil.showMessageWindow("Transaction Not Created");
                //                            flag = true;
                //                            authDataMap.remove("TRANSACTION_PART");
                //                        }
//                    } while (!flag);
//                }else 
                if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                    boolean flag = true;
                    do {
                        String sbAcNo = firstEnteredActNo();
                       
                        if (sbAcNo != null && sbAcNo.length() > 0) {
                            flag = checkingActNo(sbAcNo);
                            if (flag == false && finalChecking == false) {
                                ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                            } else {
                                authDataMap.put("CR_ACT_NUM", sbAcNo);
                            }
                            finalChecking = false;
                        } else {
                            ClientUtil.showMessageWindow("Transaction Not Created");
                            flag = true;
                            authDataMap.remove("TRANSACTION_PART");
                            return false;
                        }
                    } while (!flag);
                }
                observable.setTransactionMap(authDataMap);
            } else {
                int opt = ClientUtil.confirmationAlert("Amount not entered. Do you want to continue without Transaction?");
                if (opt == 1) {
                    return false;
                }
            }

        }
        return true;
    }

    private boolean checkingActNo(String sbAcNo) {
        boolean flag = false;
        HashMap existingMap = new HashMap();
        existingMap.put("ACT_NUM", sbAcNo.toUpperCase());
        List mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
        //System.out.println("#### mapDataList :" + mapDataList);
        if (mapDataList != null && mapDataList.size() > 0) {
            existingMap = (HashMap) mapDataList.get(0);
            String[] obj5 = {"Proceed", "ReEnter"};
            int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name coreect or not " + "\nOperative AcctNo is : " + existingMap.get("ACCOUNT NUMBER") + "\nCustomer Name :" + existingMap.get("CUSTOMER NAME")), ("Transaction Part"),
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
            if (option4 == 0) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    private String firstEnteredActNo() {
       // String sbAcNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TRANSFER_TRANS"));
        	acctsearch=new AcctSearchUI();
         	acctsearch.show();
         	String sbAcNo=acctsearch.getAccountNo();
       //  return sbAcNo;
         if(sbAcNo.equals("")){
          ClientUtil.displayAlert("Account number should be given!!!!!!");
           return(null);
        }
        else{
        return sbAcNo;
       
    }
    }

    private boolean tokenValidation(String tokenNo) {
        boolean tokenflag = false;
        HashMap tokenWhereMap = new HashMap();// Separating Serias No and Token No
        char[] chrs = tokenNo.toCharArray();
        StringBuffer seriesNo = new StringBuffer();
        int i = 0;
        for (int j = chrs.length; i < j; i++) {
            if (Character.isDigit(chrs[i])) {
                break;
            } else {
                seriesNo.append(chrs[i]);
            }
        }
        tokenWhereMap.put("SERIES_NO", seriesNo.toString());
        tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
        tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
        tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        tokenWhereMap.put("CURRENT_DT", currDt.clone());
        List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
        if (((Integer) lst.get(0)).intValue() == 0) {
            tokenflag = false;
        } else {
            tokenflag = true;
        }
        return tokenflag;
    }
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        dontShowJointDialog = true;
        try {
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panActInfo);
            StringBuffer strBAlert = new StringBuffer();
            if (mandatoryMessage.length() > 0) {
                //                displayAlert(mandatoryMessage);
                strBAlert.append(mandatoryMessage + "\n");
            }
            if (!txtATMNoAD.getText().equalsIgnoreCase("")) {
                if (dtdATMToDateAD.getDateValue().equalsIgnoreCase("")) {
                    strBAlert.append(resourceBundle.getString("ATMDATEWARNING") + "\n");
                }
            }
            if (!txtDebitNoAD.getText().equalsIgnoreCase("")) {
                if (dtdDebitToDateAD.getDateValue().equalsIgnoreCase("")) {
                    strBAlert.append(resourceBundle.getString("DEBITDATEWARNING") + "\n");
                }
            }
            if (!txtCreditNoAD.getText().equalsIgnoreCase("")) {
                if (dtdCreditToDateAD.getDateValue().equalsIgnoreCase("")) {
                    strBAlert.append(resourceBundle.getString("CREDITDATEWARNING") + "\n");
                }
            }
            if (cboConstitutionAI.getSelectedItem().equals("Joint")) {
                if (tblAct_Joint.getRowCount() <= 1) {
                    strBAlert.append(resourceBundle.getString("JOINTWARNING") + "\n");
                }
            }
            boolean result = authSignUI.getAuthorizedSignatoryOB().CheckForLimit();
            if (result == true) {
                strBAlert.append(resourceBundle.getString("LIMITWARNING") + "\n");
            }

            String PROD_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.PRODUCT_ID, PROD_ID);
            //	    String behaviour = ((OperativeAcctProductTO)ClientUtil.executeQuery("getOpAcctProductTOByProdId", whereMap).get(0)).getBehavior();

            //            if(CommonUtil.convertObjToStr(((ComboBoxModel) cboOpModeAI.getModel()).getKeyForSelected()).equals("SEE_OP_MODE_REMARKS")){
            //            if (behaviour.equals("CA")) {
            String constitution = CommonUtil.convertObjToStr(((ComboBoxModel) cboConstitutionAI.getModel()).getKeyForSelected());
            if (constitution.equals("CORPORATE")) {
                if (!(authSignUI.getAuthorizedSignatoryRowCount() >= 1)) {
                    strBAlert.append(resourceBundle.getString("AUTH_SIGN_WARNING") + "\n");
                }
            }
            whereMap = null;

            if (chkHideBalanceTrans.isSelected()) {
                String str = CommonUtil.convertObjToStr(cboRoleHierarchy.getSelectedItem());
                if (str == null || str.equalsIgnoreCase("")) {
                    strBAlert.append(resourceBundle.getString("ROLEWARNING") + "\n");
                }
            } else {
                cboRoleHierarchy.setSelectedIndex(0);
            }
            if (chkFlexiAD.isSelected() == true && txtReqFlexiPeriodAD.getText().length() == 0) {
                strBAlert.append("FlexiPeriod Should not be Empty!!!" + "\n");
            }

            String str = "";
            //            str = periodLengthValidation(txtReqFlexiPeriodAD, cboDMYAD);
            //            if(str.length() > 0){
            //                strBAlert.append(str+"\n");
            //                str = "";
            //            }

            if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE) {
                //__ To Check if the Total Share of the Nominee(s) is 100% or not...

                String alert = nomineeUi.validateData();
                if (!alert.equalsIgnoreCase("")) {
                    strBAlert.append(alert);
                }
            }
            
            if (chkMobileBankingAD.isSelected() == true && txtMobileNo.getText().length() == 0) {
                strBAlert.append("Mobile no should not be empty!!!" + "\n");
            }
            //            final String NOMINEE = CommonUtil.convertObjToStr(acountParamMap.get("NO_OF_NOMINEE"));
            //__ To Display the Alerts

            if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0) {
                displayAlert(strBAlert.toString());
            } else {
                updateOBFields();
                poaUI.updateOBFields();
                boolean isContinue = true;
                if (transNew) {
                    isContinue = insertTransactionPart();
                }
                if (isContinue) {
                    observable.doAction(nomineeUi.getNomineeOB(), poaUI.getPowerOfAttorneyOB(), authSignUI.getAuthorizedSignatoryOB(), authSignUI.getAuthorizedSignatoryInstructionOB());
                } else {
                    return;
                }

                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("ACCOUNTNO");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap() != null) {
                    //System.out.println("MAP@@@" + observable.getProxyReturnMap());
                    if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                        lockMap.put("ACCOUNTNO", observable.getProxyReturnMap().get("TRANS_ID"));
                    }
                }
                
                if(observable.getIsSbOD().equalsIgnoreCase("Y")){
                    if(chkRenew.isSelected()){
                        ClientUtil.showMessageWindow("OD Renewed");
                    }
                    if(chkODClose.isSelected()){
                        ClientUtil.showMessageWindow("OD Closed");
                    }else{
                        HashMap odToDtMap = new HashMap();
                        //Mr.Prasanth suggested new mode also need to show sanction amt and expiry date. code changes done by Kannan AR 
                        if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                            odToDtMap.put("ACT_NUM", CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("TRANS_ID")));
                        } else {
                            odToDtMap.put("ACT_NUM", txtAccountNo.getText());
                        }
                        String sbODExpdate = "";
                        List odToDtList = ClientUtil.executeQuery("getSBODExpiryDateForAcct", odToDtMap);
                        if (odToDtList != null && odToDtList.size() > 0) {
                            HashMap odExpDtMap = (HashMap) odToDtList.get(0);
                            Date expdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(odExpDtMap.get("TO_DT")));
                            sbODExpdate = DateUtil.getStringDate(expdt);
                        }
                        if (!CommonUtil.convertObjToStr(txtODLimitAI.getText()).equals("") && txtODLimitAI.getText() != null) {
                            Double todLimit = CommonUtil.convertObjToDouble(txtODLimitAI.getText());
                            if (todLimit > 0) {
                                ClientUtil.showMessageWindow("OD Sanctioned for amount : " + todLimit + "\nExpiry Date : " + sbODExpdate);
                            }
                        }
                    }                                 
                }
                
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("ACCOUNT_OPENING_TRANSACTION")) {
                    String actNum = (String) observable.getProxyReturnMap().get("ACCOUNT_OPENING_TRANSACTION");
                    String displayStr = "";
                    HashMap transTypeMap = new HashMap();
                    HashMap transMap = new HashMap();
                    HashMap transCashMap = new HashMap();
                    transCashMap.put("BATCH_ID", actNum);
                    transCashMap.put("TRANS_DT", currDt);
                    transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    HashMap transIdMap = new HashMap();
                    List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                    if (list != null && list.size() > 0) {
                        displayStr += "Transfer Transaction Details...\n";
                        for (int i = 0; i < list.size(); i++) {
                            transMap = (HashMap) list.get(i);
                            displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                    + "   Batch Id : " + transMap.get("BATCH_ID")
                                    + "   Trans Type : " + transMap.get("TRANS_TYPE");
                            actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                            if (actNum != null && !actNum.equals("")) {
                                displayStr += "   Account No : " + transMap.get("ACT_NUM")
                                        + "   Deposit Amount : " + transMap.get("AMOUNT") + "\n";
                            } else {
                                displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                        + "   Interest Amount : " + transMap.get("AMOUNT") + "\n";
                            }
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                            //System.out.println("#### :" + transMap);
//                                 oldBatchId = newBatchId;
                        }
                    }
////                         actNum = lblValRenewDep.getText();
//                         transMap = new HashMap();
//                         transMap.put("DEPOSIT_NO",actNum);
//                         transMap.put("CURR_DT", currDt.clone());
                    list = ClientUtil.executeQuery("getCashDetails", transCashMap);
                    if (list != null && list.size() > 0) {
                        displayStr += "Cash Transaction Details...\n";
                        for (int i = 0; i < list.size(); i++) {
                            transMap = (HashMap) list.get(i);
                            displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                    + "   Trans Type : " + transMap.get("TRANS_TYPE");
                            actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                            if (actNum != null && !actNum.equals("")) {
                                displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                                        + "   Deposit Amount :  " + transMap.get("AMOUNT") + "\n";
                            } else {
                                displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID")
                                        + "   Interest Amount :  " + transMap.get("AMOUNT") + "\n";
                            }
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                            transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                        }
                    }
                    if (!displayStr.equals("")) {
                        ClientUtil.showMessageWindow("" + displayStr);
                    }
                    int yesNo = 1;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    //System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("TransDt", currDt);
                        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        Object keys[] = transIdMap.keySet().toArray();
                        for (int i = 0; i < keys.length; i++) {
                            paramMap.put("TransId", keys[i]);
                            ttIntgration.setParam(paramMap);
                            //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                ttIntgration.integrationForPrint("ReceiptPayment");
                            } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
                                ttIntgration.integrationForPrint("CashPayment", false);
                            } else {
                                ttIntgration.integrationForPrint("CashReceipt", false);
                            }
                        }
                    }
//                    if(transNew){
//                        displayTransDetail(actNum);
//                    }
//                    
//                    if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
//                        String displayStr = "";
//                        String oldBatchId = "";
//                        String newBatchId = "";
//                        String depositNumber = actNum;
//                        HashMap transMap = new HashMap();
//                        transMap.put("LOAN_NO",actNum);
//                        transMap.put("CURR_DT",currDt );
//                        HashMap transIdMap = new HashMap();
//                        HashMap transTypeMap = new HashMap();
//                        List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
//                        if(lst !=null && lst.size()>0){
//                            displayStr += "Transfer Transaction Details...\n";
//                            for(int i = 0;i<lst.size();i++){
//                                transMap = (HashMap)lst.get(i);
//                                displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
//                                "   Batch Id : "+transMap.get("BATCH_ID")+
//                                "   Trans Type : "+transMap.get("TRANS_TYPE");
//                                depositNumber = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                                if(depositNumber != null && !depositNumber.equals("")){
//                                    displayStr +="   Account No : "+transMap.get("ACT_NUM")+
//                                    "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
//                                }else{
//                                    displayStr += "   Account Head : "+transMap.get("AC_HD_ID")+
//                                    "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
//                                }
//                                System.out.println("#### :" +transMap);
//                                oldBatchId = newBatchId;
//                            }
//                        }
//                        depositNumber = actNum;
//                        transMap = new HashMap();
//                        transMap.put("LOAN_NO",depositNumber);
//                        transMap.put("CURR_DT", currDt);
//                        lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
//                        if(lst !=null && lst.size()>0){
//                            displayStr += "Cash Transaction Details...\n";
//                            for(int i = 0;i<lst.size();i++){
//                                transMap = (HashMap)lst.get(i);
//                                displayStr +="Trans Id : "+transMap.get("TRANS_ID")+
//                                "   Trans Type : "+transMap.get("TRANS_TYPE");
//                                depositNumber = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                                if(depositNumber != null && !depositNumber.equals("")){
//                                    displayStr +="   Account No :  "+transMap.get("ACT_NUM")+
//                                    "   Deposit Amount :  "+transMap.get("AMOUNT")+"\n";
//                                }else{
//                                    displayStr +="   Account Head :  "+transMap.get("AC_HD_ID")+
//                                    "   Interest Amount :  "+transMap.get("AMOUNT")+"\n";
//                                }
//                            }
//                            transIdMap.put(transMap.get("TRANS_ID"),"CASH");
//                            transTypeMap.put(transMap.get("TRANS_ID"),transMap.get("TRANS_TYPE"));
//                        }
//                        
//                        if(!displayStr.equals("")){
//                            ClientUtil.showMessageWindow(""+displayStr);
//                        }
//                    }

                }
                if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("ACCOUNTNO", txtAccountNo.getText());
                }
                setEditLockMap(lockMap);
                setEditLock();
                super.removeEditLock(txtAccountNo.getText());
                //__ Clear Screen...
                observable.resetCustDetails();
                observable.resetJntAccntHoldTbl();
                observable.resetOBFields();
                observable.resetLabels();
                observable.resetCardTbl();
                /*
                 * To reset the POA, Introducer and Nominee Table
                 */
                (poaUI.getPowerOfAttorneyOB()).resetAllFieldsInPoA();
                poaUI.setPoAToolBtnsEnableDisable(false);
                poaUI.setAllPoAEnableDisable(false);

                //__ To get back the Nominee Screen in case Corporate Cust was selected...
                tabAccounts.add(nomineeUi, "Nominee", 4);

                nomineeUi.resetTable();
                nomineeUi.resetNomineeData();
                nomineeUi.getNomineeOB().ttNotifyObservers();
                nomineeUi.disableNewButton(false);

                authSignUI.resetAllFieldsInAuthTab();
                authSignUI.setAuthEnableDisable(false);
                authSignUI.setAllAuthInstEnableDisable(false);

                // Disable all the screen
                ClientUtil.enableDisable(this, false);               
                setEnableDateFields(false);
                // Some other fields should also be disabled at init time
                enableDisableComponents(false);
                
                /*
                 * set the operation to "NOOP" and reload the menu
                 * and the toolbar
                 */
                txtAcctOpeningAmount.setText("");
                observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
                observable.setTransactionMap(null);
                setupMenuToolBarPanel();
                observable.setResultStatus();
                lblCustName.setText("");
                lblStatus.setText(observable.getLblStatus());
            }
            dontShowJointDialog = false;
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_mitSaveActionPerformed
                                                                                            private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
                                                                                                // Add your handling code here:
                                                                                                cifClosingAlert();
                                                                                                (poaUI.getPowerOfAttorneyOB()).resetAllFieldsInPoA();
                                                                                                nomineeUi.resetNomineeData();
                                                                                                nomineeUi.resetTable();
                                                                                                authSignUI.resetAllFieldsInAuthTab();
                                                                                                //                                                                            this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed
    private void displayTransDetail(String actNum) {
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            String displayStr = "";
            String oldBatchId = "";
            String newBatchId = "";
            String depositNumber = actNum;
            HashMap transMap = new HashMap();
            transMap.put("LOAN_NO", actNum);
            transMap.put("CURR_DT", currDt);
            HashMap transIdMap = new HashMap();
            HashMap transTypeMap = new HashMap();
            List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
            if (lst != null && lst.size() > 0) {
                displayStr += "Transfer Transaction Details...\n";
                for (int i = 0; i < lst.size(); i++) {
                    transMap = (HashMap) lst.get(i);
                    displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    depositNumber = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (depositNumber != null && !depositNumber.equals("")) {
                        displayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Deposit Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Interest Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    //System.out.println("#### :" + transMap);
                    oldBatchId = newBatchId;
                }
            }
            depositNumber = actNum;
            transMap = new HashMap();
            transMap.put("LOAN_NO", depositNumber);
            transMap.put("CURR_DT", currDt);
            lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
            if (lst != null && lst.size() > 0) {
                displayStr += "Cash Transaction Details...\n";
                for (int i = 0; i < lst.size(); i++) {
                    transMap = (HashMap) lst.get(i);
                    displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    depositNumber = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (depositNumber != null && !depositNumber.equals("")) {
                        displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                                + "   Deposit Amount :  " + transMap.get("AMOUNT") + "\n";
                    } else {
                        displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID")
                                + "   Interest Amount :  " + transMap.get("AMOUNT") + "\n";
                    }
                }
                transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
            }
            if (!displayStr.equals("")) {
                ClientUtil.showMessageWindow("" + displayStr);
            }
        }
    }

    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // Add your handling code here:
        // open the popup window, authorize in the same window and come back
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_DELETE]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_DELETE]);
        nomineeUi.setActionType(DELETE);
        callView("Delete");
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditTransferInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditTransferInActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_EDITTI);
        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EDIT]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EDIT]);
        callView("EditT");
    }//GEN-LAST:event_mitEditTransferInActionPerformed

    private void mitEditNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditNewActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EDIT]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EDIT]);
//        callView("EditN");
        txtAccountNo.setEnabled(true);
    }//GEN-LAST:event_mitEditNewActionPerformed

    private void mitAddTransferInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAddTransferInActionPerformed
        // Add your handling code here:
        ClientUtil.enableDisable(this, true);
        // Some other fields should also be enables at init time
        enableDisableComponents(true);

        observable.setOperation(ClientConstants.ACTIONTYPE_NEWTI);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());

        /* now setup the menu, as it should be based on the current operation,
         * which at startup is "NOOP"
         */
        setupMenuToolBarPanel();
        /* some more components are to be setup, because its the "Transfer In"
         */
        setupComponentsForTransferIn();

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_mitAddTransferInActionPerformed

    private void mitAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAddNewActionPerformed
        // Add your handling code here:
        ClientUtil.enableDisable(this, true);
        // Some other fields should also be enables at init time
        enableDisableComponents(true);

        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();

        lblStatus.setText(observable.getLblStatus());

        /* now setup the menu, as it should be based on the current operation,
         * which at startup is "NOOP"
         */
        setupMenuToolBarPanel();
        /* some more components are to be setup, because its the "Transfer In"
         */
        setupComponentsForNew();
        /**
         * To reset the POA, Introducer and Nominee Tab at the Time of New...
         */
        (poaUI.getPowerOfAttorneyOB()).resetAllFieldsInPoA();
        poaUI.setAllPoAEnableDisable(false);
        poaUI.setPoANewOnlyEnable();


        nomineeUi.resetNomineeTab();

        authSignUI.resetAllFieldsInAuthTab();
        authSignUI.setLblStatus(observable.getLblStatus());
        authSignUI.setAuthEnableDisable(false);
        authSignUI.setAuthOnlyNewBtnEnable();
        authSignUI.setAuthInstOnlyNewBtnEnable();
        authSignUI.setAllAuthInstEnableDisable(false);  //Added by Rajesh.

        setEnableDateFields(false);

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_mitAddNewActionPerformed

    private void cboConstitutionAI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboConstitutionAI1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboConstitutionAI1ActionPerformed

    private void btnCustomerIdAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdAIActionPerformed
        // TODO add your handling code here:
        viewType="Customer";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustomerIdAIActionPerformed

    private void txtAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNoActionPerformed
if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
    System.out.println("Accountnumber Action performewd");
}
    }//GEN-LAST:event_txtAccountNoActionPerformed

    private void lblAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblAccountNumberFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_lblAccountNumberFocusLost

    private void txtCustomerIdAIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdAIFocusLost
        // TODO add your handling code here:
           showCustomerDetails();
    }//GEN-LAST:event_txtCustomerIdAIFocusLost

    private void btnATMNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnATMNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        setBtnCard(false);
        btnATMSave.setEnabled(true);
        observable.setNewData(true);
        resetCatdTableDetails();
        ClientUtil.enableDisable(panATMAccountCardDetails, true);
        if(tblATMCardDetails.getRowCount()<=0){
            rdoActionRevoke.setEnabled(false);
            rdoActionStop.setSelected(true);
        }else{
            String lastAction = CommonUtil.convertObjToStr(tblATMCardDetails.getValueAt(tblATMCardDetails.getRowCount() - 1, 1));
            if(lastAction.equals("STOP")){
                rdoActionStop.setEnabled(false);
                rdoActionRevoke.setSelected(true);
            }else if(lastAction.equals("REVOKE")){
                rdoActionRevoke.setEnabled(false);
                rdoActionStop.setSelected(true);
            }
        }
    }//GEN-LAST:event_btnATMNewActionPerformed

    private void btnATMSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnATMSaveActionPerformed
        // TODO add your handling code here:
        try {
            if (lblATMCardNoVal.getText().length() > 0) {
                if (rdoActionStop.isSelected() || rdoActionRevoke.isSelected()) {
                    if (tdtActionDt.getDateValue().length() > 0) {
                        updateCardOBFields();
                        observable.addDataToCardDetailsTable(updateTab, updateMode);
                        tblATMCardDetails.setModel(observable.getTblATMCardDetails());
                        observable.resetCardTableDetails();
                        resetCatdTableDetails();
                        ClientUtil.enableDisable(panATMAccountCardDetails, false);
                        setBtnCard(false);
                    } else {
                        ClientUtil.showMessageWindow("Action Date Should not be Empty !!!");
                        return;
                    }
                } else {
                    ClientUtil.showMessageWindow("Please Select Stop/Revoke Action !!!");
                    return;
                }
            } else {
                ClientUtil.showMessageWindow("Card Number Should not be Empty !!!");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnATMSaveActionPerformed
    public void updateCardOBFields() {
        observable.setLblATMCardNoVal(lblATMCardNoVal.getText());
        if(rdoActionStop.isSelected()){
            observable.setActionType("STOP");
        }else if(rdoActionRevoke.isSelected()){
            observable.setActionType("REVOKE");
        }
        observable.setActionDt(tdtActionDt.getDateValue());
        observable.setTxtCardRemarks(txtCardRemarks.getText());
    }
    private void resetCatdTableDetails() {
        rdoActionStop.setSelected(false);
        rdoActionRevoke.setSelected(false);
        tdtActionDt.setDateValue("");
        txtCardRemarks.setText("");
    }
    private void btnATMDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnATMDeleteActionPerformed
        // TODO add your handling code here:
        if (tblATMCardDetails.getRowCount() > 0) {
            int st = CommonUtil.convertObjToInt(tblATMCardDetails.getValueAt(tblATMCardDetails.getSelectedRow(), 0));
            observable.deleteCardTableData(st, tblATMCardDetails.getSelectedRow());
            observable.resetCardTableDetails();
            resetCatdTableDetails();
            tblATMCardDetails.setModel(observable.getTblATMCardDetails());
            setBtnCard(false);
            btnATMNew.setEnabled(true);
            ClientUtil.enableDisable(panATMAccountCardDetails, false);
        }
    }//GEN-LAST:event_btnATMDeleteActionPerformed

    private void tblATMCardDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblATMCardDetailsMousePressed
        // TODO add your handling code here:
        if (tblATMCardDetails.getRowCount() > 0) {
            updateMode = true;
            updateTab = tblATMCardDetails.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblATMCardDetails.getValueAt(tblATMCardDetails.getSelectedRow(), 0));
            ClientUtil.enableDisable(panATMAccountCardDetails, true);
            observable.populateCardTableDetails(st);
            cardTableUpdate();
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_VIEW || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                setBtnCard(false);
                ClientUtil.enableDisable(panATMAccountCardDetails, false);
            } else {
                setBtnCard(true);
                btnATMNew.setEnabled(false);
            }
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                String authStatus = CommonUtil.convertObjToStr(tblATMCardDetails.getValueAt(tblATMCardDetails.getSelectedRow(), 3));
                if (authStatus.length() > 0) {
                    ClientUtil.enableDisable(panATMAccountCardDetails, false);
                    setBtnCard(false);
                    if (tblATMCardDetails.getSelectedRow() == tblATMCardDetails.getRowCount() - 1) {
                        btnATMNew.setEnabled(true);
                    } else {
                        btnATMNew.setEnabled(false);
                    }
                }
                if (tblATMCardDetails.getSelectedRow() == tblATMCardDetails.getRowCount() - 1) {
                    String lastRowStatus = CommonUtil.convertObjToStr(tblATMCardDetails.getValueAt(tblATMCardDetails.getRowCount() - 1, 3));
                    if (lastRowStatus.length() > 0) {
                        ClientUtil.enableDisable(panATMAccountCardDetails, false);
                        setBtnCard(false);
                        btnATMNew.setEnabled(true);
                    } else {
                        ClientUtil.enableDisable(panATMAccountCardDetails, true);
                        rdoActionStop.setEnabled(false);
                        rdoActionRevoke.setEnabled(false);
                        setBtnCard(true);
                        btnATMNew.setEnabled(false);
                    }
                }
            }
        }
    }//GEN-LAST:event_tblATMCardDetailsMousePressed
    public void cardTableUpdate() {
        if(CommonUtil.convertObjToStr(observable.getActionType()).equals("STOP")){
            rdoActionStop.setSelected(true);
            rdoActionRevoke.setSelected(false);
        }else if(CommonUtil.convertObjToStr(observable.getActionType()).equals("REVOKE")){
            rdoActionRevoke.setSelected(true);
            rdoActionStop.setSelected(false);
        }
        tdtActionDt.setDateValue(observable.getActionDt());
        txtCardRemarks.setText(observable.getTxtCardRemarks());
    }
    private void rdoActionStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoActionStopActionPerformed
        // TODO add your handling code here:
        rdoActionStop.setSelected(true);
        rdoActionRevoke.setSelected(false);
    }//GEN-LAST:event_rdoActionStopActionPerformed

    private void rdoActionRevokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoActionRevokeActionPerformed
        // TODO add your handling code here:
        rdoActionStop.setSelected(false);
        rdoActionRevoke.setSelected(true);
    }//GEN-LAST:event_rdoActionRevokeActionPerformed

    private void txtCardActNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCardActNumberFocusLost
        // TODO add your handling code here:
        if(txtCardActNumber.getText().length()>0){
            lblATMCardNoVal.setText(txtCardActNumber.getText());
        }else{
            lblATMCardNoVal.setText("");
        }
    }//GEN-LAST:event_txtCardActNumberFocusLost

    private void btnMemberSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberSearchActionPerformed
        // TODO add your handling code here:
        viewType = "SUB_MEMBER_NO";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnMemberSearchActionPerformed

    private void txtBorrowerSalaryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBorrowerSalaryFocusLost
       
        // nithya
       
       
        
        
        // TODO add your handling code here:
//        long borrowerSalary = CommonUtil.convertObjToLong(txtBorrowerSalary.getText());
//        System.out.println("borrowerSalary :: " + borrowerSalary);
//        long borrowerNetworth = borrowerSalary * 4;
//        //txtBorrowerNetworth.getfo
//        txtBorrowerNetworth.setText(CommonUtil.convertObjToStr(borrowerNetworth));        
//        //txtBorrowerSalary.getText()
    }//GEN-LAST:event_txtBorrowerSalaryFocusLost

    private void btnGetODLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetODLimitActionPerformed
        // TODO add your handling code here:
        HashMap borrowerEligMap = new HashMap() ;
        String PROD_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());        
        //System.out.println("product Id :" + PROD_ID );
        borrowerEligMap.put("MEMBER_NO",observable.getBorrowerMemberNo()); // Hardcoded - To be changed by nithya
        borrowerEligMap.put("PROD_ID",PROD_ID);
        //borrowerEligMap.put("PROD_ID","309"); //USED FOR TESTING
        borrowerEligMap.put("SALARY",txtBorrowerSalary.getText());
        borrowerEligMap.put("APPLY_TYPE","NEW");
        borrowerEligMap.put("APPLIED_AMT",txtAppliedAmt.getText());
        borrowerEligMap.put("COSTOFVEHICLE",0);
        List lst = ClientUtil.executeQuery("getSBODBorrowerEligAmt",borrowerEligMap);        
        if(lst!=null && lst.size()>0){
         HashMap eligMap = (HashMap)lst.get(0);
         if(eligMap.containsKey("ELIGIBLEAMT")){
          double eligAmount = CommonUtil.convertObjToDouble(eligMap.get("ELIGIBLEAMT"));
          double appliedAmnt = CommonUtil.convertObjToDouble(txtAppliedAmt.getText());          
          lblBorrEligAmt.setText(CommonUtil.convertObjToStr(eligMap.get("ELIGIBLEAMT")));
             // rounding off
             HashMap roundParamMap = new HashMap();
             int roundFactorSBOD = 0;
             roundParamMap.put("PROD_ID", PROD_ID);
             List sbodRoundFactorLst = ClientUtil.executeQuery("getRoudOffParameterForSBOD", roundParamMap);
             if (sbodRoundFactorLst != null && sbodRoundFactorLst.size() > 0) {
                 HashMap roundValMap = (HashMap) sbodRoundFactorLst.get(0);
                 roundFactorSBOD = CommonUtil.convertObjToInt(roundValMap.get("ROUNDOFF"));
             } else {
                 roundFactorSBOD = 1000;
             }
          double totalSuretyEligAmt = 0;
          for(int i=0; i<tblMemberType.getRowCount(); i++){
             totalSuretyEligAmt  = totalSuretyEligAmt + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(i, 6));
          }
          if ((totalSuretyEligAmt % roundFactorSBOD) >= 500) {
              totalSuretyEligAmt = (totalSuretyEligAmt - (totalSuretyEligAmt % roundFactorSBOD)) + roundFactorSBOD;
          } else {
              totalSuretyEligAmt = totalSuretyEligAmt - (totalSuretyEligAmt % roundFactorSBOD);
          }              
          double newTOD = 0;
          double lastTOD = 0;
          if ((eligAmount <= appliedAmnt) && (eligAmount <= totalSuretyEligAmt)) {
             if (eligAmount <= appliedAmnt) {                 
                 newTOD = eligAmount;
             } else {                 
                 newTOD = appliedAmnt;
             }      
             double newTODWithoutRounding = newTOD;
              if((newTOD % roundFactorSBOD) >= 500){
                  newTOD = (newTOD - (newTOD % roundFactorSBOD)) + roundFactorSBOD;
              }else{
                  newTOD = newTOD - (newTOD % roundFactorSBOD);
              }
              
              if(observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT){
                 HashMap odWhrMap = new HashMap();
                 odWhrMap.put("ACT_NUM",txtAccountNo.getText());
                 List oldODLimitDetailsLst = ClientUtil.executeQuery("getLatestSBODLimitForAccNum",odWhrMap); 
                 if(oldODLimitDetailsLst != null && oldODLimitDetailsLst.size() > 0){
                    HashMap oldODLimitDetailsMap = (HashMap)oldODLimitDetailsLst.get(0);
                    lastTOD = CommonUtil.convertObjToDouble(oldODLimitDetailsMap.get("TOD_LIMIT"));
                    double clearBal = CommonUtil.convertObjToDouble(oldODLimitDetailsMap.get("CLEAR_BALANCE"));
//                    if(newTOD < lastTOD){
//                      if(clearBal < 0){
//                         ClientUtil.showMessageWindow("New OD Limit is less than the current OD");   
//                      }else{
//                         txtODLimitAI.setText(CommonUtil.convertObjToStr(newTOD));
//                      }                        
//                    }else{
//                        txtODLimitAI.setText(CommonUtil.convertObjToStr(newTOD));
//                    }
                    if(clearBal < 0){
                        double absClearBal =  (-1) * clearBal;
                        if(newTODWithoutRounding <  absClearBal){
                            ClientUtil.showMessageWindow("Already OD availed Rs." + lastTOD);  
                        }else{
                            txtODLimitAI.setText(CommonUtil.convertObjToStr(newTOD));
                        }
                    }else{
                        txtODLimitAI.setText(CommonUtil.convertObjToStr(newTOD));
                    }
                 }else{
                     txtODLimitAI.setText(CommonUtil.convertObjToStr(newTOD));
                 }
              }else{
                txtODLimitAI.setText(CommonUtil.convertObjToStr(newTOD)); 
              }              
          }else{
              ClientUtil.showMessageWindow("The number of suretys are not sufficient to avail OD"); 
          }                 
         }if(eligMap.containsKey("NETWORTH")) {             
          txtBorrowerNetworth.setText(CommonUtil.convertObjToStr(eligMap.get("NETWORTH")));
         }
       }
        observable.setEnhanceValidate(true);
    }//GEN-LAST:event_btnGetODLimitActionPerformed

    private void btnSecuritySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecuritySaveActionPerformed
        // TODO add your handling code here:
        // Added by nithya

        try {            
            observable.setEnhanceValidate(false);
            if (txtMemberNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Member number should not be empty");
            } else if (observable.getBorrowerMemberNo().equalsIgnoreCase(CommonUtil.convertObjToStr(txtMemberNo.getText()))){
                ClientUtil.showAlertWindow("Lonee Cannot stand as surety"); 
                txtMemberNo.setText("");
            } else {
                  updateMemberTypeFields();
                  updateBorrowerSalaryFilds();                
                  // Check for surety eligibility                  
                  HashMap suretyMap = new HashMap();                  
                  suretyMap.put("SURETY_MEMBERNO",observable.getSuretyMemberNo());
                  suretyMap.put("BORROW_MEMNO",observable.getBorrowerMemberNo());
                  suretyMap.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected()));
                  //suretyMap.put("PROD_ID","309"); // USED FOR TESTING
                  suretyMap.put("SALARY",observable.getSuretyMemberSalary());
                  if (chkRenew.isSelected()) {
                    suretyMap.put("APPLY_TYPE","RENEW");
                  } else {
                    suretyMap.put("APPLY_TYPE","NEW");
                  }
                  //System.out.println("observable.getBorrowerAppliedAmnt() :: " + observable.getBorrowerAppliedAmnt());
                  suretyMap.put("APPLIED_AMT",0);
//                  observable.addMemberTypeTable(memberUpdateTab, memberUpdateMode);// Added for testing purpose
//                  tblMemberType.setModel(observable.getTblMemberTypeDetails());// Added for testing purpose
                  List lst = ClientUtil.executeQuery("getSuretyEligibilityForSBOD",suretyMap);
                  //System.out.println("getSuretyEligibilityForSBOD" + lst);
                  if(lst != null && lst.size() > 0 ){                       
                      HashMap suretyEligMap = (HashMap)lst.get(0);      
                      if(suretyEligMap.containsKey("ELIGIBLEAMT")){
                          double eligAmt = CommonUtil.convertObjToDouble(suretyEligMap.get("ELIGIBLEAMT"));
                          if(eligAmt > 0){
                            //txtODLimitAI.setText(CommonUtil.convertObjToStr(suretyEligMap.get("ELIGIBLEAMT")));
                            observable.setSuretyEligAmnt(CommonUtil.convertObjToStr(suretyEligMap.get("ELIGIBLEAMT")));
                            if(suretyEligMap.containsKey("NETWORTH")){
                             txtMemberNetworth.setText(CommonUtil.convertObjToStr(suretyEligMap.get("NETWORTH")));
                             observable.setSuretyMemberNetworth(CommonUtil.convertObjToStr(suretyEligMap.get("NETWORTH")));
                            }
                            observable.addMemberTypeTable(memberUpdateTab, memberUpdateMode);
                            tblMemberType.setModel(observable.getTblMemberTypeDetails());                            
                          }else{
                            ClientUtil.showMessageWindow("The member is not eligible"); 
                            ClientUtil.showMessageWindow("SHARERECOVERABLE : " + CommonUtil.convertObjToStr(suretyEligMap.get("SHARERECOVERABLE"))+"\n SURETYSTANDBALOS : " + CommonUtil.convertObjToStr(suretyEligMap.get("SURETYSTANDBALOS"))); 
                            //txtODLimitAI.setText("");
                            
                          }
//                          SHARERECOVERABLE
//                          NETWORTH
//                          MAXLOANAMTPROD
//                          SURETYSTANDBALOS
                      }
                                           
                  }else{
                  ClientUtil.showMessageWindow("The member is not eligible"); 
                  //ClientUtil.showMessageWindow("SHARERECOVERABLE : " + CommonUtil.convertObjToStr(suretyEligMap.get("SHARERECOVERABLE"))+"\n SURETYSTANDBALOS : " + CommonUtil.convertObjToStr(suretyEligMap.get("SURETYSTANDBALOS"))); 
                  txtODLimitAI.setText("");
                  observable.resetMemberTypeDetails();
                  resetMemberTypeDetails();               
                  btnSecurityMember(false);
                  btnSecurityNew.setEnabled(true);
                  btnMemberSearch.setEnabled(false);
                  btnSecuritySave.setEnabled(false);
                 }
                  observable.resetMemberTypeDetails();
                  resetMemberTypeDetails();               
                  btnSecurityMember(false);
                  btnSecurityNew.setEnabled(true);
                  btnMemberSearch.setEnabled(false);
                  btnSecuritySave.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSecuritySaveActionPerformed

    public void populateMemberTypeFields() {
        txtMemberNo.setText(observable.getSuretyMemberNo());
        txtMemberName.setText(observable.getSuretyMemberName());
        txtMemberType.setText(observable.getSuretyMemberType());
        txtContactNo.setText(observable.getSuretyMemberContact());
        txtMemberNetworth.setText(observable.getSuretyMemberNetworth());
        txtMemberSalary.setText(observable.getSuretyMemberSalary());        
    }
    
    private void btnSecurityNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityNewActionPerformed
        // TODO add your handling code here:
        observable.setEnhanceValidate(false);
        memberUpdateMode = false;
        observable.setMemberTypeData(true);
        btnSecurityMember(false);
        btnSecuritySave.setEnabled(true);
        ClientUtil.enableDisable(panSuretyDetails, true);
        btnMemberSearch.setEnabled(true);
        txtMemberName.setEnabled(false);
        txtMemberType.setEnabled(false); 
        txtMemberNo.setEnabled(false);
    }//GEN-LAST:event_btnSecurityNewActionPerformed

    private void tblMemberTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberTypeMousePressed
        // TODO add your handling code here:
        updateMemberTypeFields();
        memberUpdateMode = true;
        observable.setEnhanceValidate(false);
        memberUpdateTab = tblMemberType.getSelectedRow();
        observable.setMemberTypeData(false);
        String st = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.populateMemberTypeDetails(st);
        populateMemberTypeFields();
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getOperation() == ClientConstants.ACTIONTYPE_VIEW || observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            btnSecurityMember(false);           
        }else{
            btnSecurityMember(true);
            txtMemberSalary.setEnabled(true);
            txtMemberNetworth.setEnabled(true);
        }
        
        boolean rowExists = false;
        System.out.println("observable.getOldSuretys() ::" + observable.getOldSuretys());
        if (observable.getOldSuretys() != null && observable.getOldSuretys().size() > 0) {
            for (int i = 0; i < observable.getOldSuretys().size(); i++) {
                if (observable.getOldSuretys().get(i).equals(txtMemberNo.getText())) {
                    rowExists = true;
                    break;
                }
            }
        }
        System.out.println("rowExists ::" + rowExists);
        if (!rowExists) {
            observable.setMemberTypeData(true);
        }

    }//GEN-LAST:event_tblMemberTypeMousePressed

    private void btnSecurityDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityDeleteActionPerformed
        // TODO add your handling code here:
        observable.setEnhanceValidate(false);
        String s = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.deleteMemberTableData(s, tblMemberType.getSelectedRow());
        observable.resetMemberTypeDetails();
        resetMemberTypeDetails();
        //ClientUtil.enableDisable(panMemberDetails, false);
        btnSecurityMember(false);
        btnSecurityNew.setEnabled(true);
    }//GEN-LAST:event_btnSecurityDeleteActionPerformed

    private void chkRenewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRenewActionPerformed
        // TODO add your handling code here:
        if(chkRenew.isSelected()){
           observable.setChkRenew("RENEW"); 
           chkEnhance.setEnabled(false);
        }else{
           observable.setChkRenew("NEW"); 
           chkEnhance.setEnabled(true);
        }
    }//GEN-LAST:event_chkRenewActionPerformed

    private void txtMobileNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobileNoFocusLost
        // TODO add your handling code here:
        tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
    }//GEN-LAST:event_txtMobileNoFocusLost

    private void chkEnhanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEnhanceActionPerformed
        // TODO add your handling code here:
        if(chkEnhance.isSelected()){
            observable.setIsODEnhanced("Y");   
            btnGetODLimit.setEnabled(true);
        }else{
            observable.setIsODEnhanced("N");
            btnGetODLimit.setEnabled(false);
        }
    }//GEN-LAST:event_chkEnhanceActionPerformed

    private void chkODCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkODCloseActionPerformed
        // TODO add your handling code here:
        if (chkODClose.isSelected()) {
            chkRenew.setEnabled(false);
            chkEnhance.setEnabled(false);
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                double lastTOD = 0;
                HashMap odWhrMap = new HashMap();
                odWhrMap.put("ACT_NUM", txtAccountNo.getText());
                List oldODLimitDetailsLst = ClientUtil.executeQuery("getLatestSBODLimitForAccNum", odWhrMap);
                if (oldODLimitDetailsLst != null && oldODLimitDetailsLst.size() > 0) {
                    HashMap oldODLimitDetailsMap = (HashMap)oldODLimitDetailsLst.get(0);
                    lastTOD = CommonUtil.convertObjToDouble(oldODLimitDetailsMap.get("TOD_LIMIT"));
                    double clearBal = CommonUtil.convertObjToDouble(oldODLimitDetailsMap.get("CLEAR_BALANCE"));
                    if (clearBal < 0) {
                        ClientUtil.showMessageWindow("Cannot close OD");
                        chkODClose.setSelected(false);
                        observable.setChkODClose("N");
                    }else{
                        int yes_no = ClientUtil.confirmationAlert("Are you sure you want to close OD?");
                        if(yes_no == 0){
                            observable.setChkODClose("Y");
                        }else{
                            observable.setChkODClose("N");
                            chkODClose.setSelected(false);
                        }                        
                    }
                }
            }
        }else{
            observable.setChkODClose("N");
        }
    }//GEN-LAST:event_chkODCloseActionPerformed

    private void btnDealerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDealerIDActionPerformed
        // TODO add your handling code here:
        callView("DEALER_ID");
    }//GEN-LAST:event_btnDealerIDActionPerformed

    private void btnAgentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentIDActionPerformed
        // TODO add your handling code here:
        callView("AGENT_ID");
    }//GEN-LAST:event_btnAgentIDActionPerformed

    private void rdoKYCNormsYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoKYCNormsYesActionPerformed
        // TODO add your handling code here:
        rdoKYCNormsYes.setSelected(true);
        rdoKYCNormsNo.setSelected(false);
        lblCardActNumber.setVisible(true);
        txtCardActNumber.setVisible(true);
        lblLinkingProductId.setVisible(true);
        cboLinkingProductId.setVisible(true);
        lblLinkingActNum.setVisible(true);
        panLinkingActNum.setVisible(true);
        lblLinkingActNameValue.setVisible(true);
        txtAtmCardLimit.setVisible(true);
        lblAtmCardLimit.setVisible(true);
        btnLinkingActNum.setEnabled(true);
        //lblUPIMobileno.setVisible(true);
        //txtUPIMobileNo.setVisible(true);
    }//GEN-LAST:event_rdoKYCNormsYesActionPerformed

    private void rdoKYCNormsNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoKYCNormsNoActionPerformed
        // TODO add your handling code here:
        rdoKYCNormsYes.setSelected(false);
        rdoKYCNormsNo.setSelected(true);
        lblCardActNumber.setVisible(false);
        txtCardActNumber.setVisible(false);
        lblLinkingProductId.setVisible(false);
        cboLinkingProductId.setVisible(false);
        lblLinkingActNum.setVisible(false);
        panLinkingActNum.setVisible(false);
        lblLinkingActNameValue.setVisible(false);
        txtAtmCardLimit.setVisible(false);
        lblAtmCardLimit.setVisible(false);
        //lblUPIMobileno.setVisible(false);
        //txtUPIMobileNo.setVisible(false);
    }//GEN-LAST:event_rdoKYCNormsNoActionPerformed

    private void btnLinkingActNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinkingActNumActionPerformed
        // TODO add your handling code here:
        viewType="LinkingActNum";
        callView("LinkingActNum");
    }//GEN-LAST:event_btnLinkingActNumActionPerformed

    private void cboLinkingProductIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLinkingProductIdItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLinkingProductIdItemStateChanged

    private void txtAtmCardLimitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAtmCardLimitFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAtmCardLimitFocusLost
    
    private void txtMobileNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobileNoKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != java.awt.event.KeyEvent.VK_SPACE)) {
            evt.consume();  // ignore event
        }
    }//GEN-LAST:event_txtMobileNoKeyTyped

    private void cboProductIdAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdAIActionPerformed
        // TODO add your handling code here:
        if (cboProductIdAI.getSelectedIndex() > 0) {
            HashMap checkMap = new HashMap();
            checkMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            checkMap.put("PROD_ID", (String) ((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            List actList = (List) (ClientUtil.executeQuery("getAccountMaintenanceCount", checkMap));
            if (actList != null && actList.size() > 0) {
                checkMap = (HashMap) actList.get(0);
                int cnt = CommonUtil.convertObjToInt(checkMap.get("CNT"));
                if (cnt == 0) {
                    ClientUtil.displayAlert("Branch Account Number Settings Not Done. Please Check !!!");
                    btnCancelActionPerformed(null);
                }
            }
        }
    }//GEN-LAST:event_cboProductIdAIActionPerformed

    private void txtUPIMobileNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUPIMobileNoKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != java.awt.event.KeyEvent.VK_SPACE)) {
            evt.consume();  // ignore event
        }
    }//GEN-LAST:event_txtUPIMobileNoKeyTyped
    
    // Added by nithya
    private void resetMemberTypeDetails() {
        txtMemberName.setText("");
        txtMemberNo.setText("");
        txtMemberType.setText("");
        txtContactNo.setText("");
        txtMemberSalary.setText("");
        txtMemberNetworth.setText("");
    }
    
    private void btnSecurityMember(boolean flag) {
        btnSecurityNew.setEnabled(flag);
        btnSecuritySave.setEnabled(flag);
        btnSecurityDelete.setEnabled(flag);
    }
    
    // Added by nithya
    public void updateMemberTypeFields() {
       observable.setSuretyMemberName(txtMemberName.getText());
       observable.setSuretyMemberNo(txtMemberNo.getText());
       observable.setSuretyMemberType(txtMemberType.getText());
       observable.setSuretyMemberContact(txtContactNo.getText());
       observable.setSuretyMemberSalary(txtMemberSalary.getText());
       observable.setSuretyMemberNetworth(txtMemberNetworth.getText());
   }
   
    private void updateBorrowerSalaryFilds(){
        observable.setBorrowerSalary(txtBorrowerSalary.getText());
        observable.setBorrowerNetworth(txtBorrowerNetworth.getText());
//        if(chkRenew.isSelected()){
//           observable.setChkRenew("RENEW"); 
//        }else{
//           observable.setChkRenew("NEW");  
//        }
        observable.setBorrowerAppliedAmnt(txtAppliedAmt.getText());
        if(observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT){
            //observable.setBorrowerMemberNo(lblBorrowerMemberNo.getText());
        }else{
            observable.setBorrowerMemberNo(txtExistingAcctNo.getText());
        }       
    }
    
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);


        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
        btnAccNum.setEnabled(false);
    }

    private void setEditMode(boolean opened, String accountNo) {
        HashMap editMap = new HashMap();
        if (opened) {
            editMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        } else {
            editMap.put(CommonConstants.USER_ID, null);
        }
        editMap.put("ACT_NUM", accountNo);
        ClientUtil.execute("setEditMode", editMap);
        editMap = null;
    }

    /* This method is used to popup the window which will have some display
     * information
     */
    private void callView(String currField) {
        HashMap viewMap = new HashMap();
        HashMap whereMap;
        viewType = currField;

        //--- If Customer Id is selected OR JointAccnt New is clciked, show the popup Screen of Customer Table
        if ((currField == "Customer") || (currField == "JointAccount")) {
            StringBuffer presentCust = new StringBuffer();
            int jntAccntTablRow = tblAct_Joint.getRowCount();
            if (tblAct_Joint.getRowCount() != 0) {
                for (int i = 0, sizeJointAcctAll = tblAct_Joint.getRowCount(); i < sizeJointAcctAll; i++) {
                    if (i == 0 || i == sizeJointAcctAll) {
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(i, 1)) + "'");
                    } else {
                        presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(i, 1)) + "'");
                    }
                }
            }
            //            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccInfoTOList");
            viewMap.put(CommonConstants.MAP_NAME, "OperativeAcct.getCustData");
            String PROD_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            whereMap = new HashMap();
            whereMap.put(CommonConstants.PRODUCT_ID, PROD_ID);
            String behaviour = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", whereMap).get(0)).getBehavior();
            whereMap.clear();
            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            //            if(!behaviour.equalsIgnoreCase("SB")){
            //                whereMap.put("CUST_TYPE", CommonConstants.CUSTOMER_CORPORATE);
            //            }else{
            whereMap.put("CUST_TYPE", behaviour);
            //            }
            whereMap.put("CONSTITUTION", ((ComboBoxModel) cboConstitutionAI.getModel()).getKeyForSelected());
            //__ TO prevent the duplication of the Customer in case of Joint account...
            whereMap.put("CUSTOMER_ID", presentCust);
            if (CommonUtil.convertObjToStr(observable.getStaffOnly()).equalsIgnoreCase("Y")) {
                whereMap.put("STAFF_ID", "");
            }
            whereMap.put("PRODUCT_ID", PROD_ID);
            String status = ((OperativeAcctProductTO) ClientUtil.executeQuery("getOpAcctProductTOByProdId", whereMap).get(0)).getSRemarks();
            if (status.equals("NRO") || status.equals("NRE")) {
                whereMap.put("RESIDENTIAL_STATUS", "NONRESIDENT");
            } else {
                whereMap.put("RESIDENTIAL_STATUS", "");
            }

            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            //System.out.println("#$#$#$ AccountsUI viewMap : " + viewMap);
            //__ To make the Clear Button Enabled...
            new ViewAll(this, viewMap, true).show();

        } else if (currField.equals("EditN") || currField.equals("Delete")) {
            whereMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("ACCOUNTNO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());

            viewMap.put(CommonConstants.MAP_NAME, "getAccountList");
            if (currField.equals("Delete")) {
                whereMap.put("DELETED", "DELETED");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("EditT")) {
            viewMap.put(CommonConstants.MAP_NAME, "getTransferAccountList");
            new ViewAll(this, viewMap).show();

        } else if (currField.equals("TransferBranch")) {
            viewMap.put(CommonConstants.MAP_NAME, "getBranchList");
            HashMap whereList = new HashMap();
            whereList.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereList);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("Deletedstatus")) {
            HashMap where = new HashMap();
            where.put("BRANCH_ID", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_NAME, "DeletedAccountDetails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("Enquirystatus")) {
            HashMap where = new HashMap();
            where.put("BRANCH_ID", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_NAME, "getAccountList");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("EXISTING_CUSTOMER") && txtExistingAcctNo.getText().length() > 0) {
            HashMap existingMap = new HashMap();
            existingMap.put("ACT_NUM", txtExistingAcctNo.getText());
            List mapDataList = ClientUtil.executeQuery("getSelectExistingCustId", existingMap);
            //System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                existingMap = (HashMap) mapDataList.get(0);
                existingMap.put("ACT_NUM", txtExistingAcctNo.getText());
                fillData(existingMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Account No");
                txtExistingAcctNo.setText("");
                return;
            }
        } else if (currField.equals("New Customer")) {
            HashMap newCustomerDetailMap = new HashMap();
            newCustomerDetailMap.put("CUSTOMER_ID", txtCustomerIdAI.getText());
            newCustomerDetailMap.put("BRANCH_CODE", getSelectedBranchID());
            List mapDataList = ClientUtil.executeQuery("getSelectCustDetails", newCustomerDetailMap);
            //System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                newCustomerDetailMap = (HashMap) mapDataList.get(0);
                // existingMap.put("ACT_NUM",txtExistingAcctNo.getText());
                viewType = "Customer";
                fillData(newCustomerDetailMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Account No");
                txtExistingAcctNo.setText("");
                return;
            }
        } else if (currField.equals("AGENT_ID")) {//Added By Revathi.L
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            where.put("TYPE", "A");
            where.put("AGENT", "AGENT");
            viewMap.put(CommonConstants.MAP_NAME, "getDealerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            new ViewAll(this, viewMap).show();
        }else if (currField.equals("DEALER_ID")) {
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            where.put("TYPE", "D");
            where.put("DEALER", "DEALER");
            viewMap.put(CommonConstants.MAP_NAME, "getDealerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            new ViewAll(this, viewMap).show();
        }else if (currField.equals("LinkingActNum")) {
            HashMap where = new HashMap();
            final String ProductID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboLinkingProductId.getModel())).getKeyForSelected());
            where.put("PROD_ID", ProductID);
            viewMap.put(CommonConstants.MAP_NAME, "getAccountLinktoNormalAct");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            new ViewAll(this, viewMap).show();
        }

        //        new ViewAll(this, viewMap).show();
    }

    /* this method will be called by the ViewAll class, to which we are
     * passing the UI class reference
     * this will also set the account number and operation value for OB
     */
    /**
     * @param obj
     */
    public void fillData(Object obj) {
        //System.out.println("@@@@OBJ" + obj);
        HashMap hash = (HashMap) obj;
       //if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
        //
       // System.out.println("viewType -----" + viewType);           
        //System.out.println("hash1===="+hash);       
        if (hash.containsKey("CHECK")) {      // To view OA Account Screen When Double Clicking Row Table Data From Individual Customer Screen
            viewType = "Enquirystatus";
            observable.setOperation(ClientConstants.ACTIONTYPE_VIEW);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = "AUTHORIZE";
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnReject.setEnabled(false);
            rejectFlag = 1;
            if(hash.containsKey("SB_NO_TRANSACTION") && hash.get("SB_NO_TRANSACTION") != null && hash.get("SB_NO_TRANSACTION").equals("SB_NO_TRANSACTION")){
                sbNode = "SB_NO_TRANSACTION";
            }
        }
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = "AUTHORIZE";
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = "AUTHORIZE";
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = "AUTHORIZE";
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")) {
            //System.out.println("HASH DATE ====================" + hash);
            fromAuthorizeUI = false;
            fromCashierAuthorizeUI = false;
            fromManagerAuthorizeUI = false;
            viewType = "AUTHORIZE";
            observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
            //  btnSaveDisable();
        }
        authSignUI.setViewType(viewType);
        poaUI.setViewType(viewType);
        nomineeUi.setViewType(viewType);
        
        if (viewType.equals("AGENT_ID")) {
            txtAgentID.setText(CommonUtil.convertObjToStr(hash.get("AGENT_ID")));
            lblAgentIDVal.setText(CommonUtil.convertObjToStr(hash.get("AGENT_NAME")));
        }
        if (viewType.equals("DEALER_ID")) {
            txtDealerID.setText(CommonUtil.convertObjToStr(hash.get("DEALER_ID")));
            lblDealerIDVal.setText(CommonUtil.convertObjToStr(hash.get("DEALER_NAME")));
        }

        if (viewType.equals("Customer")) {
            //__ To reset the data for the Previous selected Customer..
            observable.resetCustDetails();
//            final String CUSTID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
            final String CUSTID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
            txtCustomerIdAI.setText(CUSTID);
            txtCustomerIdAI.setText(CUSTID);
            //__ To get the ComboBox value for the Communication Addr type...
            observable.getCustAddrData(CUSTID);
            cboCommAddr.setModel(observable.getCbmCommAddr());
            txtExistingAcctNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            //__ To display the customer Info...
            CustInfoDisplay(CUSTID);
            authSignUI.getAuthorizedSignatoryOB().authorizedSignatoryList(CUSTID);
            //__ To set the Communication addr Type...
            cboCommAddr.setSelectedItem(((ComboBoxModel) cboCommAddr.getModel()).getDataForKey(observable.getAddrType()));

            //__ To set the Name of the Customer...
            final String CUSTNAME = CommonUtil.convertObjToStr(hash.get("NAME"));
            txtActName.setText(CUSTNAME);
            lblCustName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
             txtActName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));

            nomineeUi.setMainCustomerId(txtCustomerIdAI.getText());

            //__ To set the Category if its Staff...
            final String CATEGORY = CommonUtil.convertObjToStr(hash.get("CUSTOMER TYPE ID"));
            if (CATEGORY.equalsIgnoreCase("STAFF")) {
                cboCategory.setSelectedItem(((ComboBoxModel) cboCategory.getModel()).getDataForKey(CATEGORY));

            } else {
                cboCategory.setSelectedIndex(3);
            }

            // Added code by nithya
           
            if(hash.containsKey("MEMBER_NO")){
              HashMap whereMap = new HashMap();
              whereMap.put("CUST_ID", hash.get("CUST_ID"));
              List serviceList = ClientUtil.executeQuery("getFutureServicePeriod", whereMap);
              if (serviceList != null && serviceList.size() > 0) {
                 HashMap hMap = (HashMap) serviceList.get(0);
                 if (hMap != null && hMap.size() > 0 && hMap.containsKey("SERVICE")) {
                       lblBorrowerService.setText("Service : " + CommonUtil.convertObjToStr(hMap.get("SERVICE"))); 
                 }
              } 
              lblBorrowerDob.setText("Date of Birth : " + CommonUtil.convertObjToStr(hash.get("DOB")));
              lblBorrowerRetireDt.setText("Retirement Date : " + CommonUtil.convertObjToStr(hash.get("RETIREMENT_DT")));
              observable.setBorrowerMemberNo(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            }  
            // End
            //__ if the selected Customer is Corporate, Do not enter the Nominee...
            //             final String CUSTTYPE = CommonUtil.convertObjToStr(hash.get("CUSTOMERTYPE"));
            //             if(CUSTTYPE.equalsIgnoreCase("CORPORATE")){
            //                 //                nomineeUi.setBtnEnableDisable(false);
            //                 tabAccounts.remove(4);
            //
            //             }else{
            //                 tabAccounts.add(nomineeUi,"Nominee", 4);
            //                 nomineeUi.enableDisableNominee_SaveDelete();
            //             }
        } else if (viewType.equals("JointAccount")) {
//            JointAcctDisplay((String) hash.get("CUSTOMER ID"));
            JointAcctDisplay((String) hash.get("CUST_ID"));
        } else if (viewType.equals("TransferBranch")) {
            this.txtBranchCodeAI.setText((String) hash.get("BRANCHCODE"));
            this.lblBranchNameValueAI.setText((String) hash.get("BRANCHNAME"));
            updatePreviousBranchDetails();
            observable.populatePreviousAccounts();

        } else if (viewType.equals("EditN")
                || viewType.equals("EditT")
                || viewType.equals("Delete") || viewType.equals("AUTHORIZE") || viewType.equals("Deletedstatus") || viewType.equals("Enquirystatus")) {
            final String CUSTTYPE = CommonUtil.convertObjToStr(hash.get("CUSTOMERTYPE"));
            // set the accountnumber and the operation for the observer
         String ACCOUNTNO ="";
            if(hash.get("ACCOUNTNO")!=null)
             ACCOUNTNO= hash.get("ACCOUNTNO").toString();
            else{
             ACCOUNTNO=txtAccountNo.getText();
             hash.put("ACCOUNTNO",ACCOUNTNO);
            }
            //System.out.println("AccountNo====="+ACCOUNTNO);
            
            observable.setAccountNumber(ACCOUNTNO);
            observable.setAccountNo(ACCOUNTNO);
            updateProductInterestRates();

            if (viewType.equals("EditN")) {
                observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
            } else if (viewType.equals("EditT")) {
                observable.setOperation(ClientConstants.ACTIONTYPE_EDITTI);
            } else if (viewType.equals("Delete")) {
                observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
            } else if (viewType.equals("AUTHORIZE")) {
                observable.setOperation(observable.getOperation());
            }

            /* we have to load all the data from database to the screen
             * add the WHERE condition in the hashmap and pass it onto the OB
             */
            hash.put(CommonConstants.MAP_WHERE, hash.get("ACCOUNTNO"));
            //System.out.println("hash======"+hash);
            // Added by nithya for SBOD RBI changes
            String prodNp = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));                         
            HashMap todCheckmap = new HashMap();
            todCheckmap.put("PROD_ID", prodNp.substring(4,7));
            List todList = ClientUtil.executeQuery("isTODSetForProduct", todCheckmap);
            if (todList != null && todList.size() > 0) {
                HashMap todMap = (HashMap) todList.get(0);
                if (todMap.containsKey("TEMP_OD_ALLOWED")) {
                    if (CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                        observable.setIsSbOD("Y");
                    }
                }
            } 
            if(observable.getIsSbOD().equalsIgnoreCase("Y")){
                hash.put("SB_OD_SET","SB_OD_SET");
            }
            observable.populateData(hash, nomineeUi.getNomineeOB(), poaUI.getPowerOfAttorneyOB(), authSignUI.getAuthorizedSignatoryOB(), authSignUI.getAuthorizedSignatoryInstructionOB());

            cboCommAddr.setModel(observable.getCbmCommAddr());
            update(null, null);
            //nithya start
            tblMemberType.setModel(observable.getTblMemberTypeDetails());
            nomineeUi.setMainCustomerId(txtCustomerIdAI.getText());
            tblATMCardDetails.setModel(observable.getTblATMCardDetails());           
            
            //__ To set the Name of the Customer...
            lblCustName.setText(observable.getCustName(observable.getTxtCustomerIdAI()));
            txtActName.setText(observable.getCustName(observable.getTxtCustomerIdAI()));

            if ((observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) || (viewType.equals("AUTHORIZE"))) {
                ClientUtil.enableDisable(panAccounts, false);         // Disables the panel...
                setEnableDateFields(false);
                this.btnAccNum.setEnabled(false);
                txtAcctOpeningAmount.setVisible(true);
                lblAcctOpeningAmount.setVisible(true);
                lblAcctOpeningAmount.setEnabled(true);
            } else {
                ClientUtil.enableDisable(this, true);
                setEnableDateFields(false);
                if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
                    ClientUtil.enableDisable(panTransferBranchInfo, false);
                }

                dtdOpeningDateAI.setEnabled(false);
                this.cboProductIdAI.setEnabled(false);

                //__ Testing...
                //                cboProductIdAIItemStateChanged(null);
            }
            //__ Changes in AuthSignatory...

            addCustIDNAuthSignatory();

            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
                this.btnBranchCode.setEnabled(false);
            }

            enableDisableComponents(true);
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDITTI
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                this.btnAccNum.setEnabled(true);

            }
            /*
             * To set the Status in the UI...
             */

            observable.setStatus();// To set the Value of lblStatus...
            lblStatus.setText(observable.getLblStatus());

            /*
             * To Enable or Disable the Menu items and Buttons...
             */
            setupMenuToolBarPanel();

            /* call the poroductId selection combo box action handler to update
             * the account head and the interest rates
             */
            //            cboProductIdAIItemStateChanged(null);

            /* update the value for customer also, this value comes afer the
             * customerId has been selected.
             * this address is selected based on the customer selection, so this
             * code is not in the update() method, so we can not set this value
             * through the OB class
             */

            updateCustomerAddressForAI();
            txtBranchCodeAIFocusLost(null);

            poaUI.setPoAToolBtnsEnableDisable(false);
            authSignUI.setAuthEnableDisable(false);
            authSignUI.setAllAuthInstEnableDisable(false);
            nomineeUi.setAuthInstEnableDisable(false);
            /**
             * TO get the Max of the deleted Nominee(s) for the particular
             * Account-Holder...
             */
            nomineeUi.callMaxDel(ACCOUNTNO);
            nomineeUi.resetNomineeTab();
            nomineeUi.setMinNominee(CommonUtil.convertObjToStr(minNominee));



            if (viewType.equals("Delete") || viewType.equals("AUTHORIZE")) {
                nomineeUi.disableNewButton(false);
                this.btnAccNum.setEnabled(false);
            } else {
                poaUI.setPoANewOnlyEnable();
                authSignUI.setAuthOnlyNewBtnEnable();
                authSignUI.setAuthInstOnlyNewBtnEnable();
            }


            //System.out.println("CUSTTYPE: " + CUSTTYPE);
            //__ if the selected Customer is Corporate, Do not enter the Nominee...
            if (CUSTTYPE.equalsIgnoreCase("CORPORATE")) {
                //                nomineeUi.setBtnEnableDisable(false);
                tabAccounts.remove(4);
            } else {
                tabAccounts.add(nomineeUi, "Nominee", 4);
                ClientUtil.enableDisable(nomineeUi, false);   //This line added by Rajesh.
            }
            if (viewType.equals("AUTHORIZE")) {
                btnAuthorize.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                if (txtAccountNo.getText().length() > 0) {
                    getTransDetails(txtAccountNo.getText());
                    btnCancel.setFocusable(false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                }
            }
            
            // Nithya
            HashMap renewalCheckMap = new HashMap();
            renewalCheckMap.put("ACT_NUM",hash.get("ACCOUNTNO"));
            renewalCheckMap.put("TO_DT",currDt);
            //List lst = ClientUtil.executeQuery("checkODForRenewal",renewalCheckMap);
            List lst = ClientUtil.executeQuery("getSBODRenewalDate",renewalCheckMap);
            if(lst != null && lst.size() > 0){
                ClientUtil.showMessageWindow("Period over. You need to renew the account"); 
                chkRenew.setEnabled(true);
                btnGetODLimit.setEnabled(false);
                sbODRenewal = true;
            }else{
                chkRenew.setEnabled(false);
                if(acountParamMap != null && acountParamMap.containsKey("TOD") && ((String) acountParamMap.get("TOD")).equalsIgnoreCase("Y") && tblMemberType.getRowCount() > 0){
                     btnGetODLimit.setEnabled(false);
                }
                sbODRenewal = false;
            }
            // End
//            txtAccountNo.setText(CommonUtil.convertObjToStr(ACCOUNTNO));
//            txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        } else if (viewType.equals("EXISTING_CUSTOMER") && txtExistingAcctNo.getText().length() > 0) {
            txtCustomerIdAI.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            CustInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            lblCustName.setText(observable.getLblCustValue());
        txtActName.setText(observable.getLblCustValue());
        }else if(viewType == "SUB_MEMBER_NO"){ // nithya
            //System.out.println("SUB_MEMBER_NO hash :: " + hash);
            HashMap shareInfoMap = new HashMap();
            shareInfoMap.put("SHARE ACCOUNT NO", hash.get("MEMBER_NO"));
            txtMemberNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            txtMemberName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            lblDob.setText("Date of Birth : " + CommonUtil.convertObjToStr(hash.get("DOB")));
            lblRetireDt.setText("Retirement Date : " + CommonUtil.convertObjToStr(hash.get("RETIREMENT_DT")));
            List lst = ClientUtil.executeQuery("getShareAccInfoTO", shareInfoMap);
                if (lst != null && lst.size() > 0) {
                    HashMap resultMap = (HashMap) lst.get(0);
                    txtMemberType.setText(CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE")));                    
                }
            HashMap whereMap = new HashMap();
            whereMap.put("CUST_ID", hash.get("CUST_ID")); 
            //whereMap.put("CUST_ID", "C000110984"); 
            List serviceList = ClientUtil.executeQuery("getFutureServicePeriod", whereMap);
                if (serviceList != null && serviceList.size() > 0) {
                    HashMap hMap = (HashMap) serviceList.get(0);
                    if (hMap != null && hMap.size() > 0 && hMap.containsKey("SERVICE")) {
                       lblService.setText("Service : " + CommonUtil.convertObjToStr(hMap.get("SERVICE"))); 
                    }
                }   
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            ClientUtil.enableDisable(this, false);
        }
        if (viewType.equals("EditN")) {
            if (CommonUtil.convertObjToStr(hash.get("AUTHORIZATIONSTATUS")).equals("AUTHORIZED") || CommonUtil.convertObjToStr(hash.get("AUTHORIZATIONSTATUS")).equals("REJECTED")) {
                txtCustomerIdAI.setEnabled(false);
                btnAccNum.setEnabled(false);
                txtAcctOpeningAmount.setVisible(false);
                lblAcctOpeningAmount.setVisible(false);
            } else {
                txtAcctOpeningAmount.setVisible(true);
                lblAcctOpeningAmount.setVisible(true);
                lblAcctOpeningAmount.setEnabled(true);
            }
        }
        
        if (viewType.equals("LinkingActNum")) {
            txtLinkingActNum.setEditable(false);
            txtLinkingActNum.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            observable.setTxtLinkingActNum(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            lblLinkingActNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
//            txtAtmCardLimit.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME")));
        }
        //Added By Suresh
        if (viewType.equals("EditN") || viewType.equals("EditT")) {
            chkMobileBankingAD.setEnabled(true);
            chkMobileBankingADActionPerformed(null);
        }

        txtMinBal1FlexiAD.setEnabled(false);
        txtMinBal2FlexiAD.setEnabled(false);
        setModified(true);
        rdoKYCNormsYes.setSelected(false);
        rdoKYCNormsNo.setSelected(false);
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            btnReject.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
            btnDeletedDetails.setEnabled(false);
            btnAuthorize.setEnabled(true);
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            btnReject.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
            btnDeletedDetails.setEnabled(false);
            btnAuthorize.setEnabled(true);
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            btnReject.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
            btnDeletedDetails.setEnabled(false);
            btnAuthorize.setEnabled(true);
        }
//         HashMap addressVerifyMap = new HashMap();
//         addressVerifyMap.put("CUSTOMER ID",txtCustomerIdAI.getText());
//         List lst = ClientUtil.executeQuery("addressVerifiedStatus", addressVerifyMap);
//         if(lst!=null && lst.size()>0){
//             addressVerifyMap = (HashMap)lst.get(0);
//             if(!addressVerifyMap.get("ADDR_VERIFIED").equals("") && addressVerifyMap.get("ADDR_VERIFIED").equals("Y")){
//                 rdoAddressVerified_Yes.setSelected(true);
//             }else if(!addressVerifyMap.get("ADDR_VERIFIED").equals("") && addressVerifyMap.get("ADDR_VERIFIED").equals("N")){
//                 rdoAddressVerified_No.setSelected(true);
//             }
//             rdoAddressVerified_Yes.setEnabled(false);
//             rdoAddressVerified_No.setEnabled(false);
//         }
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
         if(viewType.equals("Account Number For Edit")){
          // nithya to be removed
          //System.out.println("hash hash :: " + hash);
          // End
          String actNum=  CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER"));
          //System.out.println("actNum===="+actNum);
          txtAccountNo.setText(actNum);
          txtAccountNoFocusLost(null);
        }
        txtBorrowerNetworth.setEnabled(false); 
        txtMemberNo.setEnabled(false); 
        txtMemberNetworth.setEnabled(false); 
    }

    private void CustInfoDisplay(String custId) {
        //System.out.println("CustInfoDisplay()");
        HashMap hash = new HashMap();
        hash.put("CUST_ID", custId);

        //__ Changes in AuthSignatory...
        authSignUI.removeAcctLevelCustomer(observable.getTxtCustomerIdAI());

        observable.populateScreen(hash, false, poaUI.getPowerOfAttorneyOB());
        updateCustomerDetails();

        //__ Changes in AuthSignatory...
        authSignUI.addAcctLevelCustomer(observable.getTxtCustomerIdAI());

        //        txtCustomerIdAI.setText(observable.getTxtCustomerIdAI());
        tblAct_Joint.setModel(observable.getTblJointAccnt());
        hash = null;
    }

    private void JointAcctDisplay(String custId) {
        HashMap hash = new HashMap();
        hash.put("CUST_ID", custId);

        //__ Changes in AuthSignatory...
        authSignUI.addAcctLevelCustomer(custId);

        observable.populateJointAccntTable(hash, poaUI.getPowerOfAttorneyOB());
        tblAct_Joint.setModel(observable.getTblJointAccnt());
        btnCustDetDelete.setEnabled(false);
        btnToMain.setEnabled(false);
        hash = null;
    }

    private void updateCustomerDetails() {
        lblCustValue.setText(observable.getLblCustValue());
        lblCustValue.setToolTipText(lblCustValue.getText());
        lblCityValue.setText(observable.getLblCityValue());
        lblDOBValue.setText(observable.getLblDOBValue());

        lblCountryValue.setText(observable.getLblCountryValue());
        lblStreetValue.setText(observable.getLblStreetValue());
        lblStreetValue.setToolTipText(lblStreetValue.getText());
        lblStateValue.setText(observable.getLblStateValue());
        lblAreaValue.setText(observable.getLblAreaValue());
        lblPinValue.setText(observable.getLblPinValue());
        lblMinOrMaj.setText(observable.getLblMajOMinVal());
    }

    private void updatePreviousBranchDetails() {
        observable.setTxtBranchCodeAI(this.txtBranchCodeAI.getText());
    }
    /* use this method to get the customer informatio and update the name and
     * address for the customer in the Account Details screen
     */

    public void updateCustomerAddressForAI() {
        //HashMap hash = (HashMap)observable.getDetailsForCustomerId(txtCustomerIdAI.getText());        );
        //lblCustomerNameValueAI.setText((String)hash.get("NAME"));
        //addressPanelCustAI.setAddress((Address)hash.get("ADDRESS"));
    }

    /* use this method to get the product interest details */
    public void updateProductInterestRates() {
        HashMap hash = (HashMap) observable.getProductInterestRates(
                (String) ((ComboBoxModel) (cboProductIdAI.getModel())).getKeyForSelected());

        // this is just for display, so we can afford to hard code (%) sign
        lblCrInterestRateValueIN.setText(hash.get("APPL_CR_INT_RATE") + " %");
        observable.setLblCrInterestRateValueIN(CommonUtil.convertObjToStr(hash.get("APPL_CR_INT_RATE") + " %"));
        lblDrInterestRateValueIN.setText(hash.get("APPL_DEBIT_INT_RATE") + " %");
        observable.setLblDrInterestRateValueIN(CommonUtil.convertObjToStr(hash.get("APPL_DEBIT_INT_RATE") + " %"));
        lblPenalInterestValueIN.setText(hash.get("PENAL_INT_DEBIT_BALACCT") + " %");
        observable.setLblPenalInterestValueIN(CommonUtil.convertObjToStr(hash.get("PENAL_INT_DEBIT_BALACCT") + " %"));
        lblAgClearingValueIN.setText(hash.get("AG_CLEARING") + " %");
        observable.setLblAgClearingValueIN(CommonUtil.convertObjToStr(hash.get("AG_CLEARING") + " %"));
    }

    public void update(java.util.Observable observed, Object arg) {          
        txtBranchCodeAI.setText(observable.getTxtBranchCodeAI());
        txtAmoutTransAI.setText(observable.getTxtAmoutTransAI());
        txtRemarksAI.setText(observable.getTxtRemarksAI());
        txtClosedDt.setText(observable.getClosedDt());
//        cboAgentId.setSelectedItem(((ComboBoxModel) cboAgentId.getModel()).getDataForKey(observable.getCboagentId()));
        if (observable.getCboProductIdAI().equals("")) {
            cboProductIdAI.setSelectedIndex(0);
        } else {
            cboProductIdAI.setSelectedItem(((ComboBoxModel) cboProductIdAI.getModel()).getDataForKey(observable.getCboProductIdAI()));
        }

        txtCustomerIdAI.setText(observable.getTxtCustomerIdAI());
        txtPrevActNumAI.setText(observable.getTxtPrevActNumAI());

        if (observable.getCboActStatusAI().equals("")) {
            cboActStatusAI.setSelectedIndex(0);
        } else {
            cboActStatusAI.setSelectedItem(
                    ((ComboBoxModel) cboActStatusAI.getModel()).getDataForKey(
                    observable.getCboActStatusAI()));
        }

        if (observable.getCboConstitutionAI().equals("")) {
            cboConstitutionAI.setSelectedIndex(0);
        } else {
            cboConstitutionAI.setSelectedItem(
                    ((ComboBoxModel) cboConstitutionAI.getModel()).getDataForKey(
                    observable.getCboConstitutionAI()));
        }

        if (observable.getCboOpModeAI().equals("")) {
            cboOpModeAI.setSelectedIndex(0);
        } else {
            cboOpModeAI.setSelectedItem(
                    ((ComboBoxModel) cboOpModeAI.getModel()).getDataForKey(
                    observable.getCboOpModeAI()));
        }

        txtODLimitAI.setText(observable.getTxtODLimitAI());

        if (observable.getCboGroupCodeAI().equals("")) {
            cboGroupCodeAI.setSelectedIndex(0);
        } else {
            cboGroupCodeAI.setSelectedItem(
                    ((ComboBoxModel) cboGroupCodeAI.getModel()).getDataForKey(
                    observable.getCboGroupCodeAI()));
        }

        if (observable.getCboSettlementModeAI().equals("")) {
            cboSettlementModeAI.setSelectedIndex(0);
        } else {
            cboSettlementModeAI.setSelectedItem(
                    ((ComboBoxModel) cboSettlementModeAI.getModel()).getDataForKey(
                    observable.getCboSettlementModeAI()));
        }

        if (observable.getCboCategory().equals("")) {
            cboCategory.setSelectedIndex(0);
        } else {
            cboCategory.setSelectedItem(
                    ((ComboBoxModel) cboCategory.getModel()).getDataForKey(
                    observable.getCboCategory()));
        }
        txtActName.setText(observable.getTxtActName());
        txtRemarks.setText(observable.getTxtRemarks());
        txtCardActNumber.setText(observable.getTxtCardActNumber());
        txtLinkingActNum.setText(observable.getTxtLinkingActNum());
        txtAtmCardLimit.setText(observable.getTxtAtmCardLimit());
        if (observable.getCboLinkingProductId().equals("")) {
            cboLinkingProductId.setSelectedIndex(0);
        } else {
            cboLinkingProductId.setSelectedItem(((ComboBoxModel) cboLinkingProductId.getModel()).getDataForKey(observable.getCboLinkingProductId()));
        }
        lblATMCardNoVal.setText(observable.getTxtCardActNumber());
        if(txtCardActNumber.getText().length()>0){
            txtCardActNumber.setEnabled(false);
        }else{
            txtCardActNumber.setEnabled(true);
        }
        txtAcctOpeningAmount.setText(observable.getTxtOpeningAmount());

        //        if (observable.getCboBaseCurrAI().equals("")) {
        //            cboBaseCurrAI.setSelectedIndex(0);
        //        } else {
        //            cboBaseCurrAI.setSelectedItem(
        //            ((ComboBoxModel) cboBaseCurrAI.getModel()).getDataForKey(
        //            observable.getCboBaseCurrAI()));
        //        }
        //
        chkPayIntOnCrBalIN.setSelected(observable.getChkPayIntOnCrBalIN());
        chkPayIntOnDrBalIN.setSelected(observable.getChkPayIntOnDrBalIN());
        chkChequeBookAD.setSelected(observable.getChkChequeBookAD());
        chkCustGrpLimitValidationAD.setSelected(observable.getChkCustGrpLimitValidationAD());
        chkMobileBankingAD.setSelected(observable.getChkMobileBankingAD());
        txtMobileNo.setText(observable.getTxtMobileNo());
        tdtMobileSubscribedFrom.setDateValue(observable.getTdtMobileSubscribedFrom());
        chkNROStatusAD.setSelected(observable.getChkNROStatusAD());
        chkATMAD.setSelected(observable.getChkATMAD());
        chkATMADActionPerformed(null);
        txtATMNoAD.setText(observable.getTxtATMNoAD());
        chkDebitAD.setSelected(observable.getChkDebitAD());
        chkDebitADActionPerformed(null);
        txtDebitNoAD.setText(observable.getTxtDebitNoAD());
        chkCreditAD.setSelected(observable.getChkCreditAD());
        chkCreditADActionPerformed(null);
        txtCreditNoAD.setText(observable.getTxtCreditNoAD());

        chkFlexiAD.setSelected(observable.getChkFlexiAD());
        txtMinBal1FlexiAD.setText(observable.getTxtMinBal1FlexiAD());
        txtMinBal2FlexiAD.setText(observable.getTxtMinBal2FlexiAD());
        txtReqFlexiPeriodAD.setText(observable.getTxtReqFlexiPeriodAD());
        //         if (observable.getCboDMYAD().equals("")) {
        //             cboDMYAD.setSelectedIndex(0);
        //         } else {
        //             cboDMYAD.setSelectedItem(
        //             ((ComboBoxModel) cboDMYAD.getModel()).getDataForKey(
        //             observable.getCboDMYAD()));
        //         }
        chkFlexiADActionPerformed(null);

        //         if (observable.getCboDMYAD().equals("")) {
        //             cboDMYAD.setSelectedIndex(0);
        //         } else {
        //             cboDMYAD.setSelectedItem(
        //             ((ComboBoxModel) cboDMYAD.getModel()).getDataForKey(
        //             observable.getCboDMYAD()));
        //         }

        if (observable.getCboStmtFreqAD().equals("")) {
            cboStmtFreqAD.setSelectedIndex(0);
        } else {
            cboStmtFreqAD.setSelectedItem(
                    ((ComboBoxModel) cboStmtFreqAD.getModel()).getDataForKey(
                    observable.getCboStmtFreqAD()));
        }

        chkStopPmtChrgAD.setSelected(observable.getChkStopPmtChrgAD());
        chkChequeRetChrgAD.setSelected(observable.getChkChequeRetChrgAD());
        chkInopChrgAD.setSelected(observable.getChkInopChrgAD());
        chkStmtChrgAD.setSelected(observable.getChkStmtChrgAD());
        chkPassBook.setSelected(observable.isChkPassBook());
        txtAccOpeningChrgAD.setText(observable.getTxtAccOpeningChrgAD());
        txtAccCloseChrgAD.setText(observable.getTxtAccCloseChrgAD());
        txtMisServiceChrgAD.setText(observable.getTxtMisServiceChrgAD());
        txtChequeBookChrgAD.setText(observable.getTxtChequeBookChrgAD());
        txtFolioChrgAD.setText(observable.getTxtFolioChrgAD());

        txtExcessWithChrgAD.setText(observable.getTxtExcessWithChrgAD());

        chkNonMainMinBalChrgAD.setSelected(observable.getChkNonMainMinBalChrgAD());
        chkNonMainMinBalChrgADActionPerformed(null);
        txtMinActBalanceAD.setText(observable.getTxtMinActBalanceAD());
        chkABBChrgAD.setSelected(observable.getChkABBChrgAD());
        chkABBChrgADActionPerformed(null);
        txtABBChrgAD.setText(observable.getTxtABBChrgAD());
        chkNPAChrgAD.setSelected(observable.getChkNPAChrgAD());
        chkNPAChrgADActionPerformed(null);

        // transfering branch information
        lblBranchNameValueAI.setText(observable.getLblBranchNameValueAI());
        // interest rates
        lblRateCodeValueIN.setText(observable.getLblRateCodeValueIN());
        lblCrInterestRateValueIN.setText(observable.getLblCrInterestRateValueIN());
        lblDrInterestRateValueIN.setText(observable.getLblDrInterestRateValueIN());
        lblPenalInterestValueIN.setText(observable.getLblPenalInterestValueIN());
        lblAgClearingValueIN.setText(observable.getLblAgClearingValueIN());

        // set the date control
        dtdATMFromDateAD.setDateValue(observable.getDtdATMFromDateAD());
        dtdATMToDateAD.setDateValue(observable.getDtdATMToDateAD());
        dtdActOpenDateAI.setDateValue(observable.getDtdActOpenDateAI());
        dtdCredit.setDateValue(observable.getDtdCredit());
        dtdCreditFromDateAD.setDateValue(observable.getDtdCreditFromDateAD());
        dtdCreditToDateAD.setDateValue(observable.getDtdCreditToDateAD());
        dtdDebit.setDateValue(observable.getDtdDebit());
        dtdDebitFromDateAD.setDateValue(observable.getDtdDebitFromDateAD());
        dtdDebitToDateAD.setDateValue(observable.getDtdDebitToDateAD());
        dtdNPAChrgAD.setDateValue(observable.getDtdNPAChrgAD());
        dtdOpeningDateAI.setDateValue(observable.getDtdOpeningDateAI());

        if (observable.getOthersAddress() != null) {
            addressPanelINP5.setAddress(observable.getOthersAddress());
        }
        this.cboPreviousActNo.setModel(observable.getCbmPrevAcctNo());
        tblAct_Joint.setModel(observable.getTblJointAccnt());
        //To set the  Account No...
        txtAccountNo.setText(observable.getAccountNo());

        if (observable.getCboCommAddr() != null && observable.getCboCommAddr().equals("")) {
            //            cboCommAddr.setSelectedIndex(0);
            cboCommAddr.setSelectedItem("");
        } else {
            cboCommAddr.setSelectedItem(
                    ((ComboBoxModel) cboCommAddr.getModel()).getDataForKey(
                    observable.getCboCommAddr()));
        }

        chkHideBalanceTrans.setSelected(observable.getChkHideBalanceTrans());
        chkHideBalanceTransActionPerformed(null);

        if (observable.getCboRoleHierarchy() != null && observable.getCboRoleHierarchy().equals("")) {
            //            cboCommAddr.setSelectedIndex(0);
            cboRoleHierarchy.setSelectedItem("");
        } else {
            cboRoleHierarchy.setSelectedItem(
                    ((ComboBoxModel) cboRoleHierarchy.getModel()).getDataForKey(
                    observable.getCboRoleHierarchy()));
        }


        //        chkBoxDisableEnable();

        //TO set the Customer details
        updateCustomerDetails();
		//Added by sreekrishnan
//        cboIntroducer.setSelectedItem(observable.getCbmIntroducer().getDataForKey(observable.getCboIntroducer()));
        // Added by nithya
        txtBorrowerSalary.setText(observable.getBorrowerSalary());
        txtBorrowerNetworth.setText(observable.getBorrowerNetworth());
        txtAppliedAmt.setText(observable.getBorrowerAppliedAmnt());
//        if(observable.getChkRenew() != null){
//          if(observable.getChkRenew().equalsIgnoreCase("RENEW")){
//            chkRenew.setSelected(true); 
//          }else{
//            chkRenew.setSelected(false); 
//          }
//        } 
        //lblBorrowerMemberNo.setText(observable.getBorrowerMemberNo());
        
        // Added by nithya for RBI changes SBOD
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            if (observable.getIsRenewd().equalsIgnoreCase("Y")) {
                chkRenew.setSelected(true);
            } else {
                chkRenew.setSelected(false);
            }
            lblEligAmt.setEnabled(false);
            lblBorrEligAmt.setEnabled(false);
        }
        if(observable.getIsODEnhanced().equalsIgnoreCase("Y")){
            chkEnhance.setSelected(true);
        }else{
            chkEnhance.setSelected(false);
        }
        tblMemberType.setModel(observable.getTblMemberTypeDetails());
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            if (observable.getChkODClose().equalsIgnoreCase("Y")) {
                chkODClose.setSelected(true);
            } else {
                chkODClose.setSelected(false);
            }
        }
       //Added By Revathi.L
        txtAgentID.setText(CommonUtil.convertObjToStr(observable.getTxtAgentID()));
        displayAgentName();
        txtDealerID.setText(CommonUtil.convertObjToStr(observable.getTxtDealerID()));
        displayDealerName();
        
        rdoKYCNormsYes.setSelected(observable.getRdoKYCNormsYes());
        rdoKYCNormsNo.setSelected(observable.getRdoKYCNormsNo());
        if(rdoKYCNormsYes.isSelected()){
            rdoKYCNormsYesActionPerformed(null);
        }
        
        if(observable.getChkPrimaryAccount().equals("Y")){
            chkPrimaryAccount.setSelected(true);
        }else{
            chkPrimaryAccount.setSelected(false);
        }
        txtUPIMobileNo.setText(observable.getTxtUPIMobileNo());
    }
    
    private void displayAgentName() {
        if (txtAgentID.getText().length() > 0) {
            HashMap custMap = new HashMap();
            custMap.put("CUST_ID", txtAgentID.getText());
            List namelst = ClientUtil.executeQuery("getCustNameForDeposit", custMap);
            if (namelst.size() > 0) {
                custMap = (HashMap) namelst.get(0);
                lblAgentIDVal.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
            }
        }
    }
    private void displayDealerName() {
        if (txtDealerID.getText().length() > 0) {
            HashMap custMap = new HashMap();
            custMap.put("CUST_ID", txtDealerID.getText());
            List namelst = ClientUtil.executeQuery("getCustNameForDeposit", custMap);
            if (namelst.size() > 0) {
                custMap = (HashMap) namelst.get(0);
                lblDealerIDVal.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
            }
        }
    }

    private void setEnableDateFields(boolean value) {
        dtdDebit.setEnabled(value);
        dtdCredit.setEnabled(value);
    }

    private void setFieldNames() {
        /*  addressPanelCustAI.setCboCityName("cboCustAddrCity");
         addressPanelCustAI.setCboCountryName("cboCustAddrCountry");
         addressPanelCustAI.setCboStateName("cboCustAddrState");
         addressPanelCustAI.setTxtAreaName("txtCustAddrArea");
         addressPanelCustAI.setTxtStreetName("txtCustAddrStreet");
         addressPanelCustAI.setTxtPincodeName("txtCustAddrPincode"); */

        //        addressPanelGuardianNO.setCboCityName("cboGuardianAddrCity");
        //        addressPanelGuardianNO.setCboCountryName("cboGuardianAddrCountry");
        //        addressPanelGuardianNO.setCboStateName("cboGuardianAddrState");
        //        addressPanelGuardianNO.setTxtAreaName("txtGuardianAddrArea");
        //        addressPanelGuardianNO.setTxtStreetName("txtGuardianAddrStreet");
        //        addressPanelGuardianNO.setTxtPincodeName("txtGuardianAddrPincode");

        //        addressPanelINP5.setCboCityName("cboINP5AddrCity");
        //        addressPanelINP5.setCboCountryName("cboINP5AddrCountry");
        //        addressPanelINP5.setCboStateName("cboINP5AddrState");
        //        addressPanelINP5.setTxtAreaName("txtINP5AddrArea");
        //        addressPanelINP5.setTxtStreetName("txtINP5ddrStreet");
        //        addressPanelINP5.setTxtPincodeName("txtINP5AddrPincode");

        //        addressPanelNomineeNO.setCboCityName("cboNomineeAddrCity");
        //        addressPanelNomineeNO.setCboCountryName("cboNomineeAddrCountry");
        //        addressPanelNomineeNO.setCboStateName("cboNomineeAddrState");
        //        addressPanelNomineeNO.setTxtAreaName("txtNomineeAddrArea");
        //        addressPanelNomineeNO.setTxtStreetName("txtNomineeAddrStreet");
        //        addressPanelNomineeNO.setTxtPincodeName("txtNomineeAddrPincode");

        //        addressPanelPOAPA.setCboCityName("cboPOAAddrCity");
        //        addressPanelPOAPA.setCboCountryName("cboPOAAddrCountry");
        //        addressPanelPOAPA.setCboStateName("cboPOAAddrState");
        //        addressPanelPOAPA.setTxtAreaName("txtPOAAddrArea");
        //        addressPanelPOAPA.setTxtStreetName("txtPOAAddrStreet");
        //        addressPanelPOAPA.setTxtPincodeName("txtPOAAddrPincode");

        //        btnAccountNoITP1.setName("btnAccountNoITP1");
        btnAdd.setName("btnAdd");
        //        btnAddNO.setName("btnAddNO");
        btnAuthorize.setName("btnAuthorize");
        btnBranchCode.setName("btnBranchCode");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnAccNum.setName("btnCustomerIdAI");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        //        btnListAddPA.setName("btnListAddPA");
        //        btnNewNO.setName("btnNewNO");
        //        btnNewPA.setName("btnNewPA");
        btnReject.setName("btnReject");
        //        btnRemoveNO.setName("btnRemoveNO");
        //        btnRemovePA.setName("btnRemovePA");
        btnReport.setName("btnReport");
        btnSave.setName("btnSave");
        cboActStatusAI.setName("cboActStatusAI");
        cboCategory.setName("cboCategory");
//        cboAgentId.setName("cboBaseCurrAI");
        cboConstitutionAI.setName("cboConstitutionAI");
        //         cboDMYAD.setName("cboDMYAD");
        //        cboDocTypeITP3.setName("cboDocTypeITP3");
        cboGroupCodeAI.setName("cboGroupCodeAI");
        //        cboIdentityTypeITP4.setName("cboIdentityTypeITP4");
        cboCommAddr.setName("cboCommAddr");
        //        cboNomineeRelationNO.setName("cboNomineeRelationNO");
        cboOpModeAI.setName("cboOpModeAI");
        cboPreviousActNo.setName("cboPreviousActNo");
        cboProductIdAI.setName("cboProductIdAI");
        //        cboRelationNO.setName("cboRelationNO");
        cboSettlementModeAI.setName("cboSettlementModeAI");
        cboStmtFreqAD.setName("cboStmtFreqAD");
        chkABBChrgAD.setName("chkABBChrgAD");
        chkATMAD.setName("chkATMAD");
        chkChequeBookAD.setName("chkChequeBookAD");
        chkChequeRetChrgAD.setName("chkChequeRetChrgAD");
        chkCreditAD.setName("chkCreditAD");
        chkCustGrpLimitValidationAD.setName("chkCustGrpLimitValidationAD");
        chkDebitAD.setName("chkDebitAD");
        chkFlexiAD.setName("chkFlexiAD");
        chkInopChrgAD.setName("chkInopChrgAD");
        panMobileBanking.setName("panMobileBanking");
        chkMobileBankingAD.setName("chkMobileBankingAD");
        lblMobileNo.setName("lblMobileNo");
        txtMobileNo.setName("txtMobileNo");
        lblMobileSubscribedFrom.setName("lblMobileSubscribedFrom");
        tdtMobileSubscribedFrom.setName("tdtMobileSubscribedFrom");
        chkNPAChrgAD.setName("chkNPAChrgAD");
        chkNROStatusAD.setName("chkNROStatusAD");
        chkNonMainMinBalChrgAD.setName("chkNonMainMinBalChrgAD");
        chkPayIntOnCrBalIN.setName("chkPayIntOnCrBalIN");
        chkPayIntOnDrBalIN.setName("chkPayIntOnDrBalIN");
        chkStmtChrgAD.setName("chkStmtChrgAD");
        chkPassBook.setName("chkPassBook");
        chkStopPmtChrgAD.setName("chkStopPmtChrgAD");
        dtdATMFromDateAD.setName("dtdATMFromDateAD");
        dtdATMToDateAD.setName("dtdATMToDateAD");
        dtdActOpenDateAI.setName("dtdActOpenDateAI");
        dtdCredit.setName("dtdCredit");
        dtdCreditFromDateAD.setName("dtdCreditFromDateAD");
        dtdCreditToDateAD.setName("dtdCreditToDateAD");
        dtdDebit.setName("dtdDebit");
        dtdDebitFromDateAD.setName("dtdDebitFromDateAD");
        dtdDebitToDateAD.setName("dtdDebitToDateAD");
        //        dtdExpiryDateITP3.setName("dtdExpiryDateITP3");
        //        dtdIssuedDateITP3.setName("dtdIssuedDateITP3");
        //        dtdMinorDOBNO.setName("dtdMinorDOBNO");
        dtdNPAChrgAD.setName("dtdNPAChrgAD");
        dtdOpeningDateAI.setName("dtdOpeningDateAI");
        //        dtdPOAFromDatePA.setName("dtdPOAFromDatePA");
        //        dtdPOAToDatePA.setName("dtdPOAToDatePA");
        lblABB.setName("lblABB");
        lblABBChrgAD.setName("lblABBChrgAD");
        lblATMFromDateAD.setName("lblATMFromDateAD");
        lblATMNoAD.setName("lblATMNoAD");
        lblATMToDateAD.setName("lblATMToDateAD");
        lblAccCloseChrgAD.setName("lblAccCloseChrgAD");
        lblAccOpeningChrgAD.setName("lblAccOpeningChrgAD");
        txtAccountNo.setName("txtAccountNo");
        //        lblAccountNoITP1.setName("lblAccountNoITP1");
        //        lblAccountNoITP2.setName("lblAccountNoITP2");
        lblAccountNumber.setName("lblAccountNumber");
        lblActHeadAI.setName("lblActHeadAI");
        //        lblActHeadITP1.setName("lblActHeadITP1");
        lblActHeadValueAI.setName("lblActHeadValueAI");
        //        lblActHeadValueITP1.setName("lblActHeadValueITP1");
        lblActOpenDateAI.setName("lblActOpenDateAI");
        lblActStatusAI.setName("lblActStatusAI");
        lblCategory.setName("lblCategory");
        lblAgClearingIN.setName("lblAgClearingIN");
        lblAgClearingValueIN.setName("lblAgClearingValueIN");
        lblAmoutTransAI.setName("lblAmoutTransAI");
        //        lblBankITP2.setName("lblBankITP2");
        lblBaseCurrAI.setName("lblBaseCurrAI");
        lblBranchCodeAI.setName("lblBranchCodeAI");
        //        lblBranchCodeITP1.setName("lblBranchCodeITP1");
        //        lblBranchCodeValueITP1.setName("lblBranchCodeValueITP1");
        //        lblBranchITP1.setName("lblBranchITP1");
        //        lblBranchITP2.setName("lblBranchITP2");
        lblBranchNameAI.setName("lblBranchNameAI");
        lblBranchNameValueAI.setName("lblBranchNameValueAI");
        //        lblBranchValueITP1.setName("lblBranchValueITP1");
        lblChequeBookChrgAD.setName("lblChequeBookChrgAD");
        lblChequeReturn.setName("lblChequeReturn");
        lblCollectInoperative.setName("lblCollectInoperative");
        lblConstitutionAI.setName("lblConstitutionAI");
        lblCrInterestRateIN.setName("lblCrInterestRateIN");
        lblCrInterestRateValueIN.setName("lblCrInterestRateValueIN");
        lblCredit.setName("lblCredit");
        lblCreditFromDateAD.setName("lblCreditFromDateAD");
        lblCreditNoAD.setName("lblCreditNoAD");
        lblCreditToDateAD.setName("lblCreditToDateAD");
        lblCustomerIdAI.setName("lblCustomerIdAI");
        //        lblCustomerIdITP1.setName("lblCustomerIdITP1");
        //        lblCustomerIdValueITP1.setName("lblCustomerIdValueITP1");
        btnCustDetNew.setName("New");
        btnCustDetDelete.setName("Delete");
        btnToMain.setName("ToMain");
        lblCustomerName.setName("lblCustomerName");
        lblDOB.setName("lblDOB");
        lblStreet.setName("lblStreet");
        lblArea.setName("lblArea");
        lblCity.setName("lblCity");
        lblCountry.setName("lblCountry");
        lblState.setName("lblState");
        lblPin.setName("lblPin");
        lblDebit.setName("lblDebit");
        lblDebitFromDateAD.setName("lblDebitFromDateAD");
        lblDebitNoAD.setName("lblDebitNoAD");
        lblDebitToDateAD.setName("lblDebitToDateAD");
        //        lblDesignationOTP5.setName("lblDesignationOTP5");
        //        lblDocNoITP3.setName("lblDocNoITP3");
        //        lblDocTypeITP3.setName("lblDocTypeITP3");
        lblDrInterestRateIN.setName("lblDrInterestRateIN");
        lblDrInterestRateValueIN.setName("lblDrInterestRateValueIN");
        lblExcessWithChrgAD.setName("lblExcessWithChrgAD");
        //        lblExpiryDateITP3.setName("lblExpiryDateITP3");
        lblFolioChrgAD.setName("lblFolioChrgAD");
        lblGroupCodeAI.setName("lblGroupCodeAI");
        //        lblGuardianNameNO.setName("lblGuardianNameNO");
        //        lblGuardianPhoneNO.setName("lblGuardianPhoneNO");
        //        lblIdITP4.setName("lblIdITP4");
        //        lblIdentityTypeITP4.setName("lblIdentityTypeITP4");
        //        lblIntroNameOTP5.setName("lblIntroNameOTP5");
        //        lblIntroTypeAI.setName("lblIntroTypeAI");
        //        lblIntroducerTypeIT.setName("lblIntroducerTypeIT");
        //        lblIntroducerTypeValueIT.setName("lblIntroducerTypeValueIT");
        //        lblIssuedByITP3.setName("lblIssuedByITP3");
        //        lblIssuedByITP4.setName("lblIssuedByITP4");
        //        lblIssuedDateITP3.setName("lblIssuedDateITP3");
        lblMinActBalanceAD.setName("lblMinActBalanceAD");
        lblMinBal1FlexiAD.setName("lblMinBal1FlexiAD");
        lblMinBal2FlexiAD.setName("lblMinBal2FlexiAD");
        lblCustName.setName("lblCustName");
        //        lblMinNominees.setName("lblMinNominees");
        //        lblMinorDOBNO.setName("lblMinorDOBNO");
        lblMisServiceChrgAD.setName("lblMisServiceChrgAD");
        lblMsg.setName("lblMsg");
        lblNPA.setName("lblNPA");
        lblNPAChrgAD.setName("lblNPAChrgAD");
        //        lblNameITP1.setName("lblNameITP1");
        //        lblNameITP2.setName("lblNameITP2");
        //        lblNameValueITP1.setName("lblNameValueITP1");
        //        lblNomineeNameNO.setName("lblNomineeNameNO");
        //        lblNomineePhoneNO.setName("lblNomineePhoneNO");
        //        lblNomineeRelationNO.setName("lblNomineeRelationNO");
        //        lblNomineeShareNO.setName("lblNomineeShareNO");
        //        lblNomineeStatusNO.setName("lblNomineeStatusNO");
        lblNonMaintenance.setName("lblNonMaintenance");
        lblODLimitAI.setName("lblODLimitAI");
        lblOpModeAI.setName("lblOpModeAI");
        lblOpeningDateAI.setName("lblOpeningDateAI");
        //        lblPOAFromDatePA.setName("lblPOAFromDatePA");
        //        lblPOANamePA.setName("lblPOANamePA");
        //        lblPOAPhonePA.setName("lblPOAPhonePA");
        //        lblPOAToDatePA.setName("lblPOAToDatePA");
        lblPenalInterestRateIN.setName("lblPenalInterestRateIN");
        lblPenalInterestValueIN.setName("lblPenalInterestValueIN");
        //        lblPhoneOTP5.setName("lblPhoneOTP5");
        lblPrevActNoAI.setName("lblPrevActNoAI");
        lblPrevActNumAI.setName("lblPrevActNumAI");
        lblProductIdAI.setName("lblProductIdAI");
        lblRateCodeIN.setName("lblRateCodeIN");
        lblRateCodeValueIN.setName("lblRateCodeValueIN");
        //        lblRelationNO.setName("lblRelationNO");
        lblRemarksAI.setName("lblRemarksAI");
        //        lblRemarksPA.setName("lblRemarksPA");
        lblReqFlexiPeriodAD.setName("lblReqFlexiPeriodAD");
        lblSettlementModeAI.setName("lblSettlementModeAI");
        lblSpace1.setName("lblSpace1");
        lblStatement.setName("lblStatement");
        lblPassBook.setName("lblPassBook");
        lblStatus.setName("lblStatus");
        lblStmtFreqAD.setName("lblStmtFreqAD");
        lblStopPayment.setName("lblStopPayment");
        lblTBSep3.setName("lblTBSep3");
        //        lblTotalShareNO.setName("lblTotalShareNO");
        panAccountDetails.setName("panAccountDetails");
        panAccountInfo.setName("panAccountInfo");
        panAccounts.setName("panAccounts");
        panActInfo.setName("panActInfo");
        //        panAddEditRemovePA.setName("panAddEditRemovePA");
        //        panBank.setName("panBank");
        panBranch.setName("panBranch");
        panCardInfo.setName("panCardInfo");
        panCustomerInfo.setName("panCustomerInfo");
        panCustOperation.setName("panCustOperation");
        srpAct_Joint.setName("srpAct_Joint");
        tblAct_Joint.setName("tblAct_Joint");
        panCustDet.setName("panCustDet");
        panDiffCharges.setName("panDiffCharges");
        //        panDocDetails.setName("panDocDetails");
        panFlexiOpt.setName("panFlexiOpt");
        //        panGuardianDetails.setName("panGuardianDetails");
        //        panIdentity.setName("panIdentity");
        panInterestPayableIN.setName("panInterestPayableIN");
        //        panIntroducer.setName("panIntroducer");
        //        panIntroducerDetails.setName("panIntroducerDetails");
        //        panIntroducerType.setName("panIntroducerType");
        panIsRequired.setName("panIsRequired");
        panLastIntApp.setName("panLastIntApp");
        //        panNominee.setName("panNominee");
        //        panNomineeDetails.setName("panNomineeDetails");
        //        panNomineeInfo.setName("panNomineeInfo");
        //        panNomineeList.setName("panNomineeList");
        //        panNomineeStatus.setName("panNomineeStatus");
        //        panOthers.setName("panOthers");
        //        panPOA.setName("panPOA");
        //        panPOADetails.setName("panPOADetails");
        //        panPOAList.setName("panPOAList");
        panRatesIN.setName("panRatesIN");
        //        panSelfCustomer.setName("panSelfCustomer");
        //        panShareAddEditRemoveNO.setName("panShareAddEditRemoveNO");
        panStatus.setName("panStatus");
        panTransferBranchInfo.setName("panTransferBranchInfo");
        panTransfered.setName("panTransfered");

        //        phoneGPanelNO.setName("phoneGPanelNO");
        //        phoneNPanelNO.setName("phoneNPanelNO");
        //        phoneNPanelOTP5.setName("phoneNPanelOTP5");
        //        phoneNPanelPA.setName("phoneNPanelPA");
        //        rdoStatus_MajorNO.setName("rdoStatus_MajorNO");
        //        rdoStatus_MinorNO.setName("rdoStatus_MinorNO");
        //        scrpPOAListPA.setName("scrpPOAListPA");
//        sepSepAI.setName("sepSepAI");
        //        sepSepNOP1.setName("sepSepNOP1");
        //        sepSepNOP2.setName("sepSepNOP2");
        //        sepSepNOP4.setName("sepSepNOP4");
        //        sepSepOTP5.setName("sepSepOTP5");
        //        sepSepPA.setName("sepSepPA");
        //        srpListNomineeNO.setName("srpListNomineeNO");
        tabAccounts.setName("tabAccounts");
        //        tblListNominee.setName("tblListNominee");
        //        tblPOAListPA.setName("tblPOAListPA");
        txtABBChrgAD.setName("txtABBChrgAD");
        //        txtACodeOTP5.setName("txtACodeOTP5");
        txtATMNoAD.setName("txtATMNoAD");
        txtAccCloseChrgAD.setName("txtAccCloseChrgAD");
        txtAccOpeningChrgAD.setName("txtAccOpeningChrgAD");
        //        txtAccountNoITP1.setName("txtAccountNoITP1");
        //        txtAccountNoITP2.setName("txtAccountNoITP2");
        txtAmoutTransAI.setName("txtAmoutTransAI");
        //        txtBankITP2.setName("txtBankITP2");
        txtBranchCodeAI.setName("txtBranchCodeAI");
        //        txtBranchITP2.setName("txtBranchITP2");
        txtChequeBookChrgAD.setName("txtChequeBookChrgAD");
        txtCreditNoAD.setName("txtCreditNoAD");
        txtCustomerIdAI.setName("txtCustomerIdAI");
        txtDebitNoAD.setName("txtDebitNoAD");
        //        txtDesignationOTP5.setName("txtDesignationOTP5");
        //        txtDocNoITP3.setName("txtDocNoITP3");
        txtExcessWithChrgAD.setName("txtExcessWithChrgAD");
        txtFolioChrgAD.setName("txtFolioChrgAD");
        //        txtGuardianACodeNO.setName("txtGuardianACodeNO");
        //        txtGuardianNameNO.setName("txtGuardianNameNO");
        //        txtGuardianPhoneNO.setName("txtGuardianPhoneNO");
        //        txtIdITP4.setName("txtIdITP4");
        //        txtIntroNameOTP5.setName("txtIntroNameOTP5");
        //        txtIssuedAuthITP4.setName("txtIssuedAuthITP4");
        //        txtIssuedByITP3.setName("txtIssuedByITP3");
        txtMinActBalanceAD.setName("txtMinActBalanceAD");
        txtMinBal1FlexiAD.setName("txtMinBal1FlexiAD");
        txtMinBal2FlexiAD.setName("txtMinBal2FlexiAD");
        //        txtMinNominees.setName("txtMinNominees");
        txtMisServiceChrgAD.setName("txtMisServiceChrgAD");
        //        txtNameITP2.setName("txtNameITP2");
        //        txtNomineeACodeNO.setName("txtNomineeACodeNO");
        //        txtNomineeNameNO.setName("txtNomineeNameNO");
        //        txtNomineePhoneNO.setName("txtNomineePhoneNO");
        //        txtNomineeShareNO.setName("txtNomineeShareNO");
        txtODLimitAI.setName("txtODLimitAI");
        //        txtPOAACodePA.setName("txtPOAACodePA");
        //        txtPOANamePA.setName("txtPOANamePA");
        //        txtPOAPhonePA.setName("txtPOAPhonePA");
        //        txtPhoneOTP5.setName("txtPhoneOTP5");
        txtPrevActNumAI.setName("txtPrevActNumAI");
        txtRemarksAI.setName("txtRemarksAI");
        //        txtRemarksPA.setName("txtRemarksPA");
        txtReqFlexiPeriodAD.setName("txtReqFlexiPeriodAD");
        //        txtTotalShareNO.setName("txtTotalShareNO");

        lblActName.setName("lblActName");
        txtActName.setName("txtActName");
        lblRemarks.setName("lblRemarks");
        txtRemarks.setName("txtRemarks");

        lblHideBalanceTrans.setName("lblHideBalanceTrans");
        chkHideBalanceTrans.setName("chkHideBalanceTrans");
        lblRoleHierarchy.setName("lblRoleHierarchy");
        cboRoleHierarchy.setName("cboRoleHierarchy");
        txtNextAccNo.setName("txtNextAccNo");

    }

    private void popUp() {
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        //System.out.println("dsfssdfsdf..."+observable.getActionType());
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            //system.out.println("test new");
            testMap = accountViewMap();
            // clearPreviousAccountDetails();

        }
        new com.see.truetransact.ui.common.viewall.ViewAll(this, testMap).show();

    }

    private void internationalize() {
        java.util.Locale currentLocale = null;
        currentLocale = new java.util.Locale(TrueTransactMain.language, TrueTransactMain.country);
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.AccountsRB", currentLocale);

        lblPenalInterestValueIN.setText(resourceBundle.getString("lblPenalInterestValueIN"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        //        ((javax.swing.border.TitledBorder)panIdentity.getBorder()).setTitle(resourceBundle.getString("panIdentity"));
        //lblState.setText(resourceBundle.getString("lblState"));
        btnCustDetNew.setText(resourceBundle.getString("btnCustDetNew"));
        btnCustDetDelete.setText(resourceBundle.getString("btnCustDetDelete"));
        btnToMain.setText(resourceBundle.getString("btnToMain"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblDOB.setText(resourceBundle.getString("lblDOB"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblPin.setText(resourceBundle.getString("lblPin"));
        //        ((javax.swing.border.TitledBorder)panBank.getBorder()).setTitle(resourceBundle.getString("panBank"));
        //        lblIntroducerTypeValueIT.setText(resourceBundle.getString("lblIntroducerTypeValueIT"));
        chkNonMainMinBalChrgAD.setText(resourceBundle.getString("chkNonMainMinBalChrgAD"));
        lblAgClearingIN.setText(resourceBundle.getString("lblAgClearingIN"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblCrInterestRateIN.setText(resourceBundle.getString("lblCrInterestRateIN"));
        lblFolioChrgAD.setText(resourceBundle.getString("lblFolioChrgAD"));
        //        lblDesignationOTP5.setText(resourceBundle.getString("lblDesignationOTP5"));
        lblMinActBalanceAD.setText(resourceBundle.getString("lblMinActBalanceAD"));
        //        ((javax.swing.border.TitledBorder)panDocDetails.getBorder()).setTitle(resourceBundle.getString("panDocDetails"));
        ((javax.swing.border.TitledBorder) panLastIntApp.getBorder()).setTitle(resourceBundle.getString("panLastIntApp"));
        //        btnNewNO.setText(resourceBundle.getString("btnNewNO"));
        //lblArea.setText(resourceBundle.getString("lblArea"));
        //        lblPhoneOTP5.setText(resourceBundle.getString("lblPhoneOTP5"));
        lblBaseCurrAI.setText(resourceBundle.getString("lblBaseCurrAI"));
        //        lblBankITP2.setText(resourceBundle.getString("lblBankITP2"));
        lblPenalInterestRateIN.setText(resourceBundle.getString("lblPenalInterestRateIN"));
        //        lblNameValueITP1.setText(resourceBundle.getString("lblNameValueITP1"));
        //lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblStatement.setText(resourceBundle.getString("lblStatement"));
        lblPassBook.setText(resourceBundle.getString("lblPassBook"));
        lblTBSep3.setText(resourceBundle.getString("lblTBSep3"));
        lblCommAddr.setText(resourceBundle.getString("lblCommAddr"));
        //        lblNomineeRelationNO.setText(resourceBundle.getString("lblNomineeRelationNO"));
        lblCustomerIdAI.setText(resourceBundle.getString("lblCustomerIdAI"));
        chkChequeRetChrgAD.setText(resourceBundle.getString("chkChequeRetChrgAD"));
        lblDebit.setText(resourceBundle.getString("lblDebit"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMinBal2FlexiAD.setText(resourceBundle.getString("lblMinBal2FlexiAD"));
        //        lblTotalShareNO.setText(resourceBundle.getString("lblTotalShareNO"));
        lblATMToDateAD.setText(resourceBundle.getString("lblATMToDateAD"));
        lblCredit.setText(resourceBundle.getString("lblCredit"));
        //        lblIntroducerTypeIT.setText(resourceBundle.getString("lblIntroducerTypeIT"));
        lblNPA.setText(resourceBundle.getString("lblNPA"));
        lblATMNoAD.setText(resourceBundle.getString("lblATMNoAD"));
        //        lblBranchITP2.setText(resourceBundle.getString("lblBranchITP2"));
        lblRemarksAI.setText(resourceBundle.getString("lblRemarksAI"));
        //lblCity.setText(resourceBundle.getString("lblCity"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        ((javax.swing.border.TitledBorder) panFlexiOpt.getBorder()).setTitle(resourceBundle.getString("panFlexiOpt"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        //        lblIdentityTypeITP4.setText(resourceBundle.getString("lblIdentityTypeITP4"));
        //        lblBranchCodeValueITP1.setText(resourceBundle.getString("lblBranchCodeValueITP1"));
        lblOpeningDateAI.setText(resourceBundle.getString("lblOpeningDateAI"));
        txtAccountNo.setText(resourceBundle.getString("txtAccountNo"));
        chkABBChrgAD.setText(resourceBundle.getString("chkABBChrgAD"));
        chkATMAD.setText(resourceBundle.getString("chkATMAD"));
        lblActStatusAI.setText(resourceBundle.getString("lblActStatusAI"));
        chkNPAChrgAD.setText(resourceBundle.getString("chkNPAChrgAD"));
        //        lblPOAFromDatePA.setText(resourceBundle.getString("lblPOAFromDatePA"));
        //        ((javax.swing.border.TitledBorder)panNomineeList.getBorder()).setTitle(resourceBundle.getString("panNomineeList"));
        btnReport.setText(resourceBundle.getString("btnReport"));
        //        ((javax.swing.border.TitledBorder)panNomineeDetails.getBorder()).setTitle(resourceBundle.getString("panNomineeDetails"));
        chkFlexiAD.setText(resourceBundle.getString("chkFlexiAD"));
        //        lblNameITP1.setText(resourceBundle.getString("lblNameITP1"));
        btnBranchCode.setText(resourceBundle.getString("btnBranchCode"));
        chkStmtChrgAD.setText(resourceBundle.getString("chkStmtChrgAD"));
        chkPassBook.setText(resourceBundle.getString("chkPassBook"));
        //        rdoStatus_MajorNO.setText(resourceBundle.getString("rdoStatus_MajorNO"));
        lblABBChrgAD.setText(resourceBundle.getString("lblABBChrgAD"));
        //        lblNomineeNameNO.setText(resourceBundle.getString("lblNomineeNameNO"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblNPAChrgAD.setText(resourceBundle.getString("lblNPAChrgAD"));
        //        rdoStatus_MinorNO.setText(resourceBundle.getString("rdoStatus_MinorNO"));
        //        lblBranchValueITP1.setText(resourceBundle.getString("lblBranchValueITP1"));
        lblAmoutTransAI.setText(resourceBundle.getString("lblAmoutTransAI"));
        chkCreditAD.setText(resourceBundle.getString("chkCreditAD"));
        lblDebitToDateAD.setText(resourceBundle.getString("lblDebitToDateAD"));
        lblGroupCodeAI.setText(resourceBundle.getString("lblGroupCodeAI"));
        ((javax.swing.border.TitledBorder) panAccountInfo.getBorder()).setTitle(resourceBundle.getString("panAccountInfo"));
        //        lblIssuedDateITP3.setText(resourceBundle.getString("lblIssuedDateITP3"));
        lblChequeBookChrgAD.setText(resourceBundle.getString("lblChequeBookChrgAD"));
        //        ((javax.swing.border.TitledBorder)panPOADetails.getBorder()).setTitle(resourceBundle.getString("panPOADetails"));
        lblCreditToDateAD.setText(resourceBundle.getString("lblCreditToDateAD"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblDebitNoAD.setText(resourceBundle.getString("lblDebitNoAD"));
        lblODLimitAI.setText(resourceBundle.getString("lblODLimitAI"));
        chkChequeBookAD.setText(resourceBundle.getString("chkChequeBookAD"));
        lblRateCodeValueIN.setText(resourceBundle.getString("lblRateCodeValueIN"));
        //        lblRelationNO.setText(resourceBundle.getString("lblRelationNO"));
        lblDebitFromDateAD.setText(resourceBundle.getString("lblDebitFromDateAD"));
        lblExcessWithChrgAD.setText(resourceBundle.getString("lblExcessWithChrgAD"));
        chkCustGrpLimitValidationAD.setText(resourceBundle.getString("chkCustGrpLimitValidationAD"));
        //        lblIntroNameOTP5.setText(resourceBundle.getString("lblIntroNameOTP5"));
        lblBranchNameAI.setText(resourceBundle.getString("lblBranchNameAI"));
        //        lblPOANamePA.setText(resourceBundle.getString("lblPOANamePA"));
        //        lblIssuedByITP3.setText(resourceBundle.getString("lblIssuedByITP3"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //        lblAccountNoITP2.setText(resourceBundle.getString("lblAccountNoITP2"));
        lblAgClearingValueIN.setText(resourceBundle.getString("lblAgClearingValueIN"));
        ((javax.swing.border.TitledBorder) panInterestPayableIN.getBorder()).setTitle(resourceBundle.getString("panInterestPayableIN"));
        lblCollectInoperative.setText(resourceBundle.getString("lblCollectInoperative"));
        btnAccNum.setText(resourceBundle.getString("btnCustomerIdAI"));
        lblProductIdAI.setText(resourceBundle.getString("lblProductIdAI"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        chkPayIntOnCrBalIN.setText(resourceBundle.getString("chkPayIntOnCrBalIN"));
        //        lblNomineePhoneNO.setText(resourceBundle.getString("lblNomineePhoneNO"));
        lblMinBal1FlexiAD.setText(resourceBundle.getString("lblMinBal1FlexiAD"));
        //        ((javax.swing.border.TitledBorder)panIntroducerDetails.getBorder()).setTitle(resourceBundle.getString("panIntroducerDetails"));
        //        btnRemovePA.setText(resourceBundle.getString("btnRemovePA"));
        //WARNING_NOMINEE.setText(resourceBundle.getString("WARNING_NOMINEE"));
        lblBranchCodeAI.setText(resourceBundle.getString("lblBranchCodeAI"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        //lblPincode.setText(resourceBundle.getString("lblPincode"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSettlementModeAI.setText(resourceBundle.getString("lblSettlementModeAI"));
        ((javax.swing.border.TitledBorder) panCustomerInfo.getBorder()).setTitle(resourceBundle.getString("panCustomerInfo"));
        ((javax.swing.border.TitledBorder) panIsRequired.getBorder()).setTitle(resourceBundle.getString("panIsRequired"));
        lblPrevActNumAI.setText(resourceBundle.getString("lblPrevActNumAI"));
        //        ((javax.swing.border.TitledBorder)panOthers.getBorder()).setTitle(resourceBundle.getString("panOthers"));
        lblActOpenDateAI.setText(resourceBundle.getString("lblActOpenDateAI"));

        lblNonMaintenance.setText(resourceBundle.getString("lblNonMaintenance"));
        chkNROStatusAD.setText(resourceBundle.getString("chkNROStatusAD"));
        lblStmtFreqAD.setText(resourceBundle.getString("lblStmtFreqAD"));
        //        lblMinorDOBNO.setText(resourceBundle.getString("lblMinorDOBNO"));
        //        btnListAddPA.setText(resourceBundle.getString("btnListAddPA"));
        lblDrInterestRateIN.setText(resourceBundle.getString("lblDrInterestRateIN"));
        //        lblGuardianNameNO.setText(resourceBundle.getString("lblGuardianNameNO"));
        //        lblDocNoITP3.setText(resourceBundle.getString("lblDocNoITP3"));
        //        lblBranchCodeITP1.setText(resourceBundle.getString("lblBranchCodeITP1"));
        lblMisServiceChrgAD.setText(resourceBundle.getString("lblMisServiceChrgAD"));
        lblABB.setText(resourceBundle.getString("lblABB"));
        lblAccOpeningChrgAD.setText(resourceBundle.getString("lblAccOpeningChrgAD"));
        lblCreditFromDateAD.setText(resourceBundle.getString("lblCreditFromDateAD"));
        //        lblDocTypeITP3.setText(resourceBundle.getString("lblDocTypeITP3"));
        //        lblActHeadITP1.setText(resourceBundle.getString("lblActHeadITP1"));
        lblDrInterestRateValueIN.setText(resourceBundle.getString("lblDrInterestRateValueIN"));
        //        lblGuardianPhoneNO.setText(resourceBundle.getString("lblGuardianPhoneNO"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblCrInterestRateValueIN.setText(resourceBundle.getString("lblCrInterestRateValueIN"));
        //        lblPOAToDatePA.setText(resourceBundle.getString("lblPOAToDatePA"));
        //        lblBranchITP1.setText(resourceBundle.getString("lblBranchITP1"));
        ((javax.swing.border.TitledBorder) panMobileBanking.getBorder()).setTitle(resourceBundle.getString("panMobileBanking"));
        chkMobileBankingAD.setText(resourceBundle.getString("chkMobileBankingAD"));
        lblMobileNo.setText(resourceBundle.getString("lblMobileNo"));
        lblMobileSubscribedFrom.setText(resourceBundle.getString("lblMobileSubscribedFrom"));
        //        btnRemoveNO.setText(resourceBundle.getString("btnRemoveNO"));
        //        lblIdITP4.setText(resourceBundle.getString("lblIdITP4"));
        lblATMFromDateAD.setText(resourceBundle.getString("lblATMFromDateAD"));
        lblActHeadValueAI.setText(resourceBundle.getString("lblActHeadValueAI"));
        //        lblCustomerIdITP1.setText(resourceBundle.getString("lblCustomerIdITP1"));
        lblConstitutionAI.setText(resourceBundle.getString("lblConstitutionAI"));
        ((javax.swing.border.TitledBorder) panTransferBranchInfo.getBorder()).setTitle(resourceBundle.getString("panTransferBranchInfo"));
        chkPayIntOnDrBalIN.setText(resourceBundle.getString("chkPayIntOnDrBalIN"));
        lblBranchNameValueAI.setText(resourceBundle.getString("lblBranchNameValueAI"));
        //SHARE_NOMINEE.setText(resourceBundle.getString("SHARE_NOMINEE"));
        //        lblNameITP2.setText(resourceBundle.getString("lblNameITP2"));
        lblReqFlexiPeriodAD.setText(resourceBundle.getString("lblReqFlexiPeriodAD"));
        lblAccCloseChrgAD.setText(resourceBundle.getString("lblAccCloseChrgAD"));
        chkInopChrgAD.setText(resourceBundle.getString("chkInopChrgAD"));
        lblRateCodeIN.setText(resourceBundle.getString("lblRateCodeIN"));
        //        btnAddNO.setText(resourceBundle.getString("btnAddNO"));
        //        lblExpiryDateITP3.setText(resourceBundle.getString("lblExpiryDateITP3"));
        //        lblNomineeShareNO.setText(resourceBundle.getString("lblNomineeShareNO"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
        lblActHeadAI.setText(resourceBundle.getString("lblActHeadAI"));
        //        ((javax.swing.border.TitledBorder)panPOAList.getBorder()).setTitle(resourceBundle.getString("panPOAList"));
        lblOpModeAI.setText(resourceBundle.getString("lblOpModeAI"));
        //        lblActHeadValueITP1.setText(resourceBundle.getString("lblActHeadValueITP1"));
        //        btnAccountNoITP1.setText(resourceBundle.getString("btnAccountNoITP1"));
        //        lblMinNominees.setText(resourceBundle.getString("lblMinNominees"));
        ((javax.swing.border.TitledBorder) panDiffCharges.getBorder()).setTitle(resourceBundle.getString("panDiffCharges"));
        ((javax.swing.border.TitledBorder) panCardInfo.getBorder()).setTitle(resourceBundle.getString("panCardInfo"));
        ((javax.swing.border.TitledBorder) panRatesIN.getBorder()).setTitle(resourceBundle.getString("panRatesIN"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        //        btnNewPA.setText(resourceBundle.getString("btnNewPA"));
        //        ((javax.swing.border.TitledBorder)panGuardianDetails.getBorder()).setTitle(resourceBundle.getString("panGuardianDetails"));
        lblCreditNoAD.setText(resourceBundle.getString("lblCreditNoAD"));
        //            lblPOAPhonePA.setText(resourceBundle.getString("lblPOAPhonePA"));
        lblPrevActNoAI.setText(resourceBundle.getString("lblPrevActNoAI"));
        //        lblCustomerIdValueITP1.setText(resourceBundle.getString("lblCustomerIdValueITP1"));
        lblStopPayment.setText(resourceBundle.getString("lblStopPayment"));
        //        lblNomineeStatusNO.setText(resourceBundle.getString("lblNomineeStatusNO"));
        //        lblRemarksPA.setText(resourceBundle.getString("lblRemarksPA"));
        chkStopPmtChrgAD.setText(resourceBundle.getString("chkStopPmtChrgAD"));
        chkDebitAD.setText(resourceBundle.getString("chkDebitAD"));
        //        lblIssuedByITP4.setText(resourceBundle.getString("lblIssuedByITP4"));
        //        ((javax.swing.border.TitledBorder)panSelfCustomer.getBorder()).setTitle(resourceBundle.getString("panSelfCustomer"));
        lblChequeReturn.setText(resourceBundle.getString("lblChequeReturn"));
        lblCustName.setText(resourceBundle.getString("lblCustName"));
        //lblCountry.setText(resourceBundle.getString("lblCountry"));
        lblActName.setText(resourceBundle.getString("lblActName"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblHideBalanceTrans.setText(resourceBundle.getString("lblHideBalanceTrans"));
        chkHideBalanceTrans.setText(resourceBundle.getString("chkHideBalanceTrans"));
        lblRoleHierarchy.setText(resourceBundle.getString("lblRoleHierarchy"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBranchCodeAI", new Boolean(true));
        mandatoryMap.put("cboPreviousActNo", new Boolean(true));
        mandatoryMap.put("dtdActOpenDateAI", new Boolean(true));
        mandatoryMap.put("txtAmoutTransAI", new Boolean(true));
        mandatoryMap.put("txtRemarksAI", new Boolean(true));
        mandatoryMap.put("cboProductIdAI", new Boolean(true));
        mandatoryMap.put("txtCustomerIdAI", new Boolean(true));
        mandatoryMap.put("dtdOpeningDateAI", new Boolean(true));
        mandatoryMap.put("cboActStatusAI", new Boolean(true));
        mandatoryMap.put("cboConstitutionAI", new Boolean(true));
        mandatoryMap.put("cboOpModeAI", new Boolean(true));
        mandatoryMap.put("cboCommAddr", new Boolean(true));
        mandatoryMap.put("txtODLimitAI", new Boolean(true));
        mandatoryMap.put("cboGroupCodeAI", new Boolean(true));
        mandatoryMap.put("cboSettlementModeAI", new Boolean(false));
        mandatoryMap.put("cboCategory", new Boolean(true));
        //        mandatoryMap.put("cboBaseCurrAI", new Boolean(true));
        mandatoryMap.put("txtPrevActNumAI", new Boolean(true));
        mandatoryMap.put("txtCustAddrStreet", new Boolean(true));
        mandatoryMap.put("cboCustAddrCountry", new Boolean(true));
        mandatoryMap.put("cboCustAddrState", new Boolean(true));
        mandatoryMap.put("cboCustAddrCity", new Boolean(true));
        mandatoryMap.put("txtCustAddrPincode", new Boolean(true));
        mandatoryMap.put("txtCustAddrArea", new Boolean(true));
        mandatoryMap.put("txtAccountNoITP1", new Boolean(true));
        mandatoryMap.put("txtBankITP2", new Boolean(true));
        mandatoryMap.put("txtBranchITP2", new Boolean(true));
        mandatoryMap.put("txtAccountNoITP2", new Boolean(true));
        mandatoryMap.put("txtNameITP2", new Boolean(true));
        mandatoryMap.put("cboDocTypeITP3", new Boolean(true));
        mandatoryMap.put("txtDocNoITP3", new Boolean(true));
        mandatoryMap.put("txtIssuedByITP3", new Boolean(true));
        mandatoryMap.put("dtdIssuedDateITP3", new Boolean(true));
        mandatoryMap.put("dtdExpiryDateITP3", new Boolean(true));
        mandatoryMap.put("cboIdentityTypeITP4", new Boolean(true));
        mandatoryMap.put("txtIssuedAuthITP4", new Boolean(true));
        mandatoryMap.put("txtIdITP4", new Boolean(true));
        mandatoryMap.put("txtINP5ddrStreet", new Boolean(true));
        mandatoryMap.put("cboINP5AddrCountry", new Boolean(true));
        mandatoryMap.put("cboINP5AddrState", new Boolean(true));
        mandatoryMap.put("cboINP5AddrCity", new Boolean(true));
        mandatoryMap.put("txtINP5AddrPincode", new Boolean(true));
        mandatoryMap.put("txtINP5AddrArea", new Boolean(true));
        mandatoryMap.put("txtIntroNameOTP5", new Boolean(true));
        mandatoryMap.put("txtDesignationOTP5", new Boolean(true));
        mandatoryMap.put("txtACodeOTP5", new Boolean(true));
        mandatoryMap.put("txtPhoneOTP5", new Boolean(true));
        mandatoryMap.put("chkChequeBookAD", new Boolean(true));
        mandatoryMap.put("chkCustGrpLimitValidationAD", new Boolean(true));
        mandatoryMap.put("chkMobileBankingAD", new Boolean(true));
        mandatoryMap.put("chkNROStatusAD", new Boolean(true));
        mandatoryMap.put("chkATMAD", new Boolean(true));
        mandatoryMap.put("txtATMNoAD", new Boolean(true));
        mandatoryMap.put("dtdATMFromDateAD", new Boolean(true));
        mandatoryMap.put("dtdATMToDateAD", new Boolean(true));
        mandatoryMap.put("chkDebitAD", new Boolean(true));
        mandatoryMap.put("txtDebitNoAD", new Boolean(true));
        mandatoryMap.put("dtdDebitFromDateAD", new Boolean(true));
        mandatoryMap.put("dtdDebitToDateAD", new Boolean(true));
        mandatoryMap.put("chkCreditAD", new Boolean(true));
        mandatoryMap.put("txtCreditNoAD", new Boolean(true));
        mandatoryMap.put("dtdCreditFromDateAD", new Boolean(true));
        mandatoryMap.put("dtdCreditToDateAD", new Boolean(true));
        mandatoryMap.put("chkFlexiAD", new Boolean(true));
        mandatoryMap.put("txtMinBal1FlexiAD", new Boolean(true));
        mandatoryMap.put("txtMinBal2FlexiAD", new Boolean(true));
        mandatoryMap.put("txtReqFlexiPeriodAD", new Boolean(true));
        mandatoryMap.put("cboDMYAD", new Boolean(true));
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
        mandatoryMap.put("dtdNPAChrgAD", new Boolean(true));
        mandatoryMap.put("txtMinActBalanceAD", new Boolean(true));
        mandatoryMap.put("dtdDebit", new Boolean(true));
        mandatoryMap.put("dtdCredit", new Boolean(true));
        mandatoryMap.put("chkPayIntOnCrBalIN", new Boolean(true));
        mandatoryMap.put("chkPayIntOnDrBalIN", new Boolean(true));
        mandatoryMap.put("txtNomineeAddrStreet", new Boolean(true));
        mandatoryMap.put("cboNomineeAddrCountry", new Boolean(true));
        mandatoryMap.put("cboNomineeAddrState", new Boolean(true));
        mandatoryMap.put("cboNomineeAddrCity", new Boolean(true));
        mandatoryMap.put("txtNomineeAddrPincode", new Boolean(true));
        mandatoryMap.put("txtNomineeAddrArea", new Boolean(true));
        mandatoryMap.put("txtNomineeNameNO", new Boolean(true));
        mandatoryMap.put("cboNomineeRelationNO", new Boolean(true));
        mandatoryMap.put("txtNomineeACodeNO", new Boolean(true));
        mandatoryMap.put("txtNomineePhoneNO", new Boolean(true));
        mandatoryMap.put("rdoStatus_MinorNO", new Boolean(true));
        mandatoryMap.put("txtNomineeShareNO", new Boolean(true));
        mandatoryMap.put("txtMinNominees", new Boolean(true));
        mandatoryMap.put("txtGuardianAddrStreet", new Boolean(true));
        mandatoryMap.put("cboGuardianAddrCountry", new Boolean(true));
        mandatoryMap.put("cboGuardianAddrState", new Boolean(true));
        mandatoryMap.put("cboGuardianAddrCity", new Boolean(true));
        mandatoryMap.put("txtGuardianAddrPincode", new Boolean(true));
        mandatoryMap.put("txtGuardianAddrArea", new Boolean(true));
        mandatoryMap.put("dtdMinorDOBNO", new Boolean(true));
        mandatoryMap.put("cboRelationNO", new Boolean(true));
        mandatoryMap.put("txtGuardianNameNO", new Boolean(true));
        mandatoryMap.put("txtGuardianACodeNO", new Boolean(true));
        mandatoryMap.put("txtGuardianPhoneNO", new Boolean(true));
        mandatoryMap.put("txtTotalShareNO", new Boolean(true));
        mandatoryMap.put("txtPOAAddrStreet", new Boolean(true));
        mandatoryMap.put("cboPOAAddrCountry", new Boolean(true));
        mandatoryMap.put("cboPOAAddrState", new Boolean(true));
        mandatoryMap.put("cboPOAAddrCity", new Boolean(true));
        mandatoryMap.put("txtPOAAddrPincode", new Boolean(true));
        mandatoryMap.put("txtPOAAddrArea", new Boolean(true));
        mandatoryMap.put("txtPOANamePA", new Boolean(true));
        mandatoryMap.put("txtPOAACodePA", new Boolean(true));
        mandatoryMap.put("txtPOAPhonePA", new Boolean(true));
        mandatoryMap.put("dtdPOAFromDatePA", new Boolean(true));
        mandatoryMap.put("dtdPOAToDatePA", new Boolean(true));
        mandatoryMap.put("txtRemarksPA", new Boolean(true));
        mandatoryMap.put("txtActName", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));

        mandatoryMap.put("chkHideBalanceTrans", new Boolean(true));
        mandatoryMap.put("cboRoleHierarchy", new Boolean(true));
    }

    private HashMap accountViewMap() {
        final HashMap testMap = new HashMap();
        testMap.put("MAPNAME", "getSelectAccountList");

        final HashMap whereMap = new HashMap();
        // log.info("productID : " + observable.getCboProductIdAI());
        String prodId = ((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected().toString();
      
        whereMap.put("PRODID", prodId);
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }
  
    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Use this method to pick all the values from the nominee panel, remainign fields
     * may be coming from some other querying method
     */
    //    private void updateNomineeOBFields() {
    //
    //        // For nominee
    //        observable.setTxtNomineeNameNO(txtNomineeNameNO.getText());
    //        observable.setCboNomineeRelationNO((String) ((ComboBoxModel) cboNomineeRelationNO.getModel()).getKeyForSelected());
    //        observable.setTxtNomineePhoneNO(txtNomineePhoneNO.getText());
    //        observable.setTxtNomineeACodeNO(txtNomineeACodeNO.getText());
    //        observable.setRdoMajorNO(rdoStatus_MajorNO.isSelected());
    //        observable.setRdoMinorNO(rdoStatus_MinorNO.isSelected());
    //        observable.setTxtNomineeShareNO(txtNomineeShareNO.getText());
    //
    //        // setup the total share
    //        observable.setTxtTotalShareNO(txtTotalShareNO.getText());
    //
    //
    //        //        observable.setRdoMinorNO(false);
    //        //        observable.setRdoMajorNO(false);
    //        observable.setNomineeAddress(addressPanelNomineeNO.getAddress());
    //
    //        if (rdoStatus_MinorNO.isSelected()) {
    //            observable.setRdoMinorNO(true);
    //            observable.setRdoMajorNO(false);
    //            observable.setDtdMinorDOBNO(dtdMinorDOBNO.getDateValue());
    //            observable.setCboRelationNO((String) ((ComboBoxModel) cboRelationNO.getModel()).getKeyForSelected());
    //            observable.setTxtGuardianNameNO(txtGuardianNameNO.getText());
    //            observable.setTxtGuardianPhoneNO(txtGuardianPhoneNO.getText());
    //            observable.setTxtGuardianACodeNO(txtGuardianACodeNO.getText());
    //            observable.setGuardianAddress(addressPanelGuardianNO.getAddress());
    //        }
    //    }
    /* Use this method to pick all the values from the PoA panel, remainign fields
     * may be coming from some other querying method
     */
    //    private void updatePoAOBFields() {
    //
    //        // For PoA
    //        observable.setTxtPOANamePA(txtPOANamePA.getText());
    //        observable.setTxtPOAACodePA(txtPOAACodePA.getText());
    //        observable.setTxtPOAPhonePA(txtPOAPhonePA.getText());
    //        observable.setDtdPOAFromDatePA(dtdPOAFromDatePA.getDateValue());
    //        observable.setDtdPOAToDatePA(dtdPOAToDatePA.getDateValue());
    //        observable.setTxtRemarksPA(txtRemarksPA.getText());
    //        observable.setPoaAddress(addressPanelPOAPA.getAddress());
    //    }
    /* Use this method to pick all the values from the screen, remainign fields
     * may be coming from some other querying method
     */
    private void updateOBFields() {

        // For Account Info
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setCboProductIdAI((String) ((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
        observable.setTxtCustomerIdAI(txtCustomerIdAI.getText());
        observable.setCboActStatusAI((String) ((ComboBoxModel) cboActStatusAI.getModel()).getKeyForSelected());
        // set the values based on the type of introduction
        if (observable.getCboActStatusAI().equals("TRANSFER_IN")) {
            observable.setCbmPrevAcctNo((ComboBoxModel) this.cboPreviousActNo.getModel());
        }
        observable.setDtdOpeningDateAI(dtdOpeningDateAI.getDateValue());
        observable.setCboConstitutionAI((String) ((ComboBoxModel) cboConstitutionAI.getModel()).getKeyForSelected());
        observable.setCboOpModeAI((String) ((ComboBoxModel) cboOpModeAI.getModel()).getKeyForSelected());
        observable.setCboCommAddr((String) ((ComboBoxModel) cboCommAddr.getModel()).getKeyForSelected());
        observable.setTxtODLimitAI(txtODLimitAI.getText());
        observable.setCboGroupCodeAI((String) ((ComboBoxModel) cboGroupCodeAI.getModel()).getKeyForSelected());
        observable.setCboSettlementModeAI((String) ((ComboBoxModel) cboSettlementModeAI.getModel()).getKeyForSelected());
        observable.setCboCategory((String) ((ComboBoxModel) cboCategory.getModel()).getKeyForSelected());
        observable.setTxtActName(txtActName.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtCardActNumber(txtCardActNumber.getText());
        observable.setTxtLinkingActNum(txtLinkingActNum.getText());
        observable.setTxtAtmCardLimit(txtAtmCardLimit.getText());
        observable.setTxtOpeningAmount(txtAcctOpeningAmount.getText());
        //        observable.setCboBaseCurrAI((String) ((ComboBoxModel) cboBaseCurrAI.getModel()).getKeyForSelected());
        // set the values based on the type of introduction
        //        if (observable.getCboIntroTypeAI().equals("SELF_CUSTOMER")) {
        //            observable.setTxtAccountNoITP1(txtAccountNoITP1.getText());
        //        } else if (observable.getCboIntroTypeAI().equals("IDENTITY")) {
        //            observable.setCboIdentityTypeITP4((String)
        //            (
        //            (ComboBoxModel)(cboIdentityTypeITP4.getModel())
        //            ).getKeyForSelected());
        //            observable.setTxtIssuedAuthITP4(txtIssuedAuthITP4.getText());
        //            observable.setTxtIdITP4(txtIdITP4.getText());
        //        } else if (observable.getCboIntroTypeAI().equals("OTHERS")) {
        //            observable.setTxtIntroNameOTP5(txtIntroNameOTP5.getText());
        //            observable.setTxtDesignationOTP5(txtDesignationOTP5.getText());
        //            observable.setTxtACodeOTP5(txtACodeOTP5.getText());
        //            observable.setTxtPhoneOTP5(txtPhoneOTP5.getText());
        //            observable.setOthersAddress(addressPanelINP5.getAddress());
        //        } else if (observable.getCboIntroTypeAI().equals("DOC_DETAILS")) {
        //
        //            observable.setCboDocTypeITP3((String)
        //            (
        //            (ComboBoxModel)(cboDocTypeITP3.getModel())
        //            ).getKeyForSelected());
        //            observable.setTxtDocNoITP3(txtDocNoITP3.getText());
        //            observable.setTxtIssuedByITP3(txtIssuedByITP3.getText());
        //            observable.setDtdExpiryDateITP3(dtdExpiryDateITP3.getDateValue());
        //            observable.setDtdIssuedDateITP3(dtdIssuedDateITP3.getDateValue());
        //        } else if (observable.getCboIntroTypeAI().equals("OTHER_BANK")) {
        //
        //            observable.setTxtBankITP2(txtBankITP2.getText());
        //            observable.setTxtBranchITP2(txtBranchITP2.getText());
        //            observable.setTxtAccountNoITP2(txtAccountNoITP2.getText());
        //            observable.setTxtNameITP2(txtNameITP2.getText());
        //        }

        // For Interest
        observable.setChkPayIntOnCrBalIN(chkPayIntOnCrBalIN.isSelected());
        observable.setChkPayIntOnDrBalIN(chkPayIntOnDrBalIN.isSelected());

        // For Other Details
        observable.setChkChequeBookAD(chkChequeBookAD.isSelected());
        observable.setChkNROStatusAD(chkNROStatusAD.isSelected());
        observable.setChkMobileBankingAD(chkMobileBankingAD.isSelected());
        observable.setTxtMobileNo(txtMobileNo.getText());
        observable.setTdtMobileSubscribedFrom(tdtMobileSubscribedFrom.getDateValue());
        observable.setChkCustGrpLimitValidationAD(chkCustGrpLimitValidationAD.isSelected());
        // ATM Card...
        observable.setChkATMAD(chkATMAD.isSelected());
        observable.setTxtATMNoAD(txtATMNoAD.getText());
        observable.setDtdATMFromDateAD(dtdATMFromDateAD.getDateValue());
        observable.setDtdATMToDateAD(dtdATMToDateAD.getDateValue());
        // Credit Card...
        observable.setChkCreditAD(chkCreditAD.isSelected());
        observable.setTxtCreditNoAD(txtCreditNoAD.getText());
        observable.setDtdCreditFromDateAD(dtdCreditFromDateAD.getDateValue());
        observable.setDtdCreditToDateAD(dtdCreditToDateAD.getDateValue());
        // Debit Card...
        observable.setChkDebitAD(chkDebitAD.isSelected());
        observable.setTxtDebitNoAD(txtDebitNoAD.getText());
        observable.setDtdDebitFromDateAD(dtdDebitFromDateAD.getDateValue());
        observable.setDtdDebitToDateAD(dtdDebitToDateAD.getDateValue());

        observable.setChkFlexiAD(chkFlexiAD.isSelected());
        observable.setTxtMinBal1FlexiAD(txtMinBal1FlexiAD.getText());
        observable.setTxtMinBal2FlexiAD(txtMinBal2FlexiAD.getText());
        observable.setTxtReqFlexiPeriodAD(txtReqFlexiPeriodAD.getText());
        //         observable.setCboDMYAD((String) ((ComboBoxModel) cboDMYAD.getModel()).getKeyForSelected());

        observable.setCboStmtFreqAD((String) ((ComboBoxModel) cboStmtFreqAD.getModel()).getKeyForSelected());
        observable.setChkStopPmtChrgAD(chkStopPmtChrgAD.isSelected());
        observable.setChkChequeRetChrgAD(chkChequeRetChrgAD.isSelected());
        observable.setChkInopChrgAD(chkInopChrgAD.isSelected());
        observable.setChkStmtChrgAD(chkStmtChrgAD.isSelected());
        observable.setChkPassBook(chkPassBook.isSelected());

        observable.setTxtAccOpeningChrgAD(txtAccOpeningChrgAD.getText());
        observable.setTxtAccCloseChrgAD(txtAccCloseChrgAD.getText());
        observable.setTxtMisServiceChrgAD(txtMisServiceChrgAD.getText());
        observable.setTxtChequeBookChrgAD(txtChequeBookChrgAD.getText());
        observable.setTxtFolioChrgAD(txtFolioChrgAD.getText());
        observable.setTxtExcessWithChrgAD(txtExcessWithChrgAD.getText());

        observable.setChkNonMainMinBalChrgAD(chkNonMainMinBalChrgAD.isSelected());
        observable.setTxtMinActBalanceAD(txtMinActBalanceAD.getText());
        observable.setChkABBChrgAD(chkABBChrgAD.isSelected());
        observable.setTxtABBChrgAD(txtABBChrgAD.getText());
        observable.setChkNPAChrgAD(chkNPAChrgAD.isSelected());
        observable.setDtdNPAChrgAD(dtdNPAChrgAD.getDateValue());

        observable.setDtdDebit(dtdDebit.getDateValue());
        observable.setDtdCredit(dtdCredit.getDateValue());

        observable.setChkHideBalanceTrans(chkHideBalanceTrans.isSelected());
        observable.setCboRoleHierarchy((String) ((ComboBoxModel) cboRoleHierarchy.getModel()).getKeyForSelected());
//        observable.setCboagentId((String) ((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected());
//        System.out.println("((String) ((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected())"+((String) ((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected()));
//		if (cboIntroducer.getSelectedItem() != null && !cboIntroducer.getSelectedItem().equals(""))
//            observable.setCboIntroducer(CommonUtil.convertObjToStr(observable.getCbmIntroducer().getKeyForSelected()));
         //Added By Revathi.L
        observable.setTxtAgentID(CommonUtil.convertObjToStr(txtAgentID.getText()));
        observable.setTxtDealerID(CommonUtil.convertObjToStr(txtDealerID.getText()));       
        updateBorrowerSalaryFilds(); // Added by nithya     
        if(chkPrimaryAccount.isSelected()){
            observable.setChkPrimaryAccount("Y");
        }else{
            observable.setChkPrimaryAccount("N");
        }
        observable.setRdoKYCNormsYes(rdoKYCNormsYes.isSelected());
        observable.setRdoKYCNormsNo(rdoKYCNormsNo.isSelected());
        observable.setTxtUPIMobileNo(txtUPIMobileNo.getText());
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        //        AccountsMRB objMandatoryRB = new AccountsMRB();
        txtBranchCodeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCodeAI"));
        cboPreviousActNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPreviousActNo"));
        dtdActOpenDateAI.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdActOpenDateAI"));
        txtAmoutTransAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmoutTransAI"));
        txtRemarksAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarksAI"));
        cboProductIdAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductIdAI"));
        txtCustomerIdAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerIdAI"));
        dtdOpeningDateAI.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdOpeningDateAI"));
        cboActStatusAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboActStatusAI"));
        cboConstitutionAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConstitutionAI"));
        cboOpModeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOpModeAI"));
        cboCommAddr.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommAddr"));
        txtODLimitAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtODLimitAI"));
        cboGroupCodeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGroupCodeAI"));
        cboSettlementModeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSettlementModeAI"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
//        cboAgentId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBaseCurrAI"));
        txtPrevActNumAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrevActNumAI"));

        txtActName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtActName"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));

        //txtCustAddrStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustAddrStreet"));
        //cboCustAddrCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustAddrCountry"));
        //cboCustAddrState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustAddrState"));
        //cboCustAddrCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustAddrCity"));
        //txtCustAddrPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustAddrPincode"));
        //txtCustAddrArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustAddrArea"));
        //        txtAccountNoITP1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNoITP1"));
        //        txtBankITP2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankITP2"));
        //        txtBranchITP2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchITP2"));
        //        txtAccountNoITP2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNoITP2"));
        //        txtNameITP2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNameITP2"));
        //        cboDocTypeITP3.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDocTypeITP3"));
        //        txtDocNoITP3.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDocNoITP3"));
        //        txtIssuedByITP3.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIssuedByITP3"));
        //        dtdIssuedDateITP3.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdIssuedDateITP3"));
        //        dtdExpiryDateITP3.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdExpiryDateITP3"));
        //        cboIdentityTypeITP4.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIdentityTypeITP4"));
        //        txtIssuedAuthITP4.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIssuedAuthITP4"));
        //        txtIdITP4.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIdITP4"));
        //txtINP5ddrStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtINP5ddrStreet"));
        //cboINP5AddrCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboINP5AddrCountry"));
        //cboINP5AddrState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboINP5AddrState"));
        //cboINP5AddrCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboINP5AddrCity"));
        //txtINP5AddrPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtINP5AddrPincode"));
        //txtINP5AddrArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtINP5AddrArea"));
        //        txtIntroNameOTP5.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntroNameOTP5"));
        //        txtDesignationOTP5.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesignationOTP5"));
        //        txtACodeOTP5.setHelpMessage(lblMsg, objMandatoryRB.getString("txtACodeOTP5"));
        //        txtPhoneOTP5.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhoneOTP5"));
        chkChequeBookAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkChequeBookAD"));
        chkCustGrpLimitValidationAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCustGrpLimitValidationAD"));
        chkMobileBankingAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkMobileBankingAD"));
        chkNROStatusAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNROStatusAD"));
        chkATMAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkATMAD"));
        txtATMNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtATMNoAD"));
        dtdATMFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdATMFromDateAD"));
        dtdATMToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdATMToDateAD"));
        chkDebitAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitAD"));
        txtDebitNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitNoAD"));
        dtdDebitFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdDebitFromDateAD"));
        dtdDebitToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdDebitToDateAD"));
        chkCreditAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditAD"));
        txtCreditNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditNoAD"));
        dtdCreditFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdCreditFromDateAD"));
        dtdCreditToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdCreditToDateAD"));
        chkFlexiAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkFlexiAD"));
        txtMinBal1FlexiAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBal1FlexiAD"));
        txtMinBal2FlexiAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBal2FlexiAD"));
        txtReqFlexiPeriodAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReqFlexiPeriodAD"));
        //         cboDMYAD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDMYAD"));
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
        dtdNPAChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdNPAChrgAD"));
        txtMinActBalanceAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinActBalanceAD"));
        dtdDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdDebit"));
        dtdCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdCredit"));
        chkPayIntOnCrBalIN.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPayIntOnCrBalIN"));
        chkPayIntOnDrBalIN.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPayIntOnDrBalIN"));

        chkHideBalanceTrans.setHelpMessage(lblMsg, objMandatoryRB.getString("chkHideBalanceTrans"));
        cboRoleHierarchy.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoleHierarchy"));
        //txtNomineeAddrStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeAddrStreet"));
        //cboNomineeAddrCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNomineeAddrCountry"));
        //cboNomineeAddrState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNomineeAddrState"));
        //cboNomineeAddrCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNomineeAddrCity"));
        //txtNomineeAddrPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeAddrPincode"));
        //txtNomineeAddrArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeAddrArea"));
        //        txtNomineeNameNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeNameNO"));
        //        cboNomineeRelationNO.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNomineeRelationNO"));
        //        txtNomineeACodeNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeACodeNO"));
        //        txtNomineePhoneNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineePhoneNO"));
        //        rdoStatus_MinorNO.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoStatus_MinorNO"));
        //        txtNomineeShareNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeShareNO"));
        //        txtMinNominees.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinNominees"));
        //txtGuardianAddrStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianAddrStreet"));
        //cboGuardianAddrCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGuardianAddrCountry"));
        //cboGuardianAddrState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGuardianAddrState"));
        //cboGuardianAddrCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGuardianAddrCity"));
        //txtGuardianAddrPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianAddrPincode"));
        //txtGuardianAddrArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianAddrArea"));
        //        dtdMinorDOBNO.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdMinorDOBNO"));
        //        cboRelationNO.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRelationNO"));
        //        txtGuardianNameNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianNameNO"));
        //        txtGuardianACodeNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianACodeNO"));
        //        txtGuardianPhoneNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianPhoneNO"));
        //        txtTotalShareNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalShareNO"));
        //txtPOAAddrStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPOAAddrStreet"));
        //cboPOAAddrCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPOAAddrCountry"));
        //cboPOAAddrState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPOAAddrState"));
        //cboPOAAddrCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPOAAddrCity"));
        //txtPOAAddrPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPOAAddrPincode"));
        //txtPOAAddrArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPOAAddrArea"));
        //        txtPOANamePA.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPOANamePA"));
        //        txtPOAACodePA.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPOAACodePA"));
        //        txtPOAPhonePA.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPOAPhonePA"));
        //        dtdPOAFromDatePA.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdPOAFromDatePA"));
        //        dtdPOAToDatePA.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdPOAToDatePA"));
        //        txtRemarksPA.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarksPA"));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnATMDelete;
    private com.see.truetransact.uicomponent.CButton btnATMNew;
    private com.see.truetransact.uicomponent.CButton btnATMSave;
    private com.see.truetransact.uicomponent.CButton btnAccNum;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAgentID;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBranchCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustDetDelete;
    private com.see.truetransact.uicomponent.CButton btnCustDetNew;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdAI;
    private com.see.truetransact.uicomponent.CButton btnDealerID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeletedDetails;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGetODLimit;
    private com.see.truetransact.uicomponent.CButton btnLinkingActNum;
    private com.see.truetransact.uicomponent.CButton btnMemberSearch;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnReport;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSecurityDelete;
    private com.see.truetransact.uicomponent.CButton btnSecurityNew;
    private com.see.truetransact.uicomponent.CButton btnSecuritySave;
    private com.see.truetransact.uicomponent.CButton btnToMain;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CComboBox cboActStatusAI;
    private com.see.truetransact.uicomponent.CComboBox cboBaseCurrAI;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboCommAddr;
    private com.see.truetransact.uicomponent.CComboBox cboConstitutionAI;
    private com.see.truetransact.uicomponent.CComboBox cboConstitutionAI1;
    private com.see.truetransact.uicomponent.CComboBox cboGroupCodeAI;
    private com.see.truetransact.uicomponent.CComboBox cboLinkingProductId;
    private com.see.truetransact.uicomponent.CComboBox cboOpModeAI;
    private com.see.truetransact.uicomponent.CComboBox cboPreviousActNo;
    private com.see.truetransact.uicomponent.CComboBox cboProductIdAI;
    private com.see.truetransact.uicomponent.CComboBox cboRoleHierarchy;
    private com.see.truetransact.uicomponent.CComboBox cboSettlementModeAI;
    private com.see.truetransact.uicomponent.CComboBox cboStmtFreqAD;
    private com.see.truetransact.uicomponent.CCheckBox chkABBChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkATMAD;
    private com.see.truetransact.uicomponent.CCheckBox chkChequeBookAD;
    private com.see.truetransact.uicomponent.CCheckBox chkChequeRetChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditAD;
    private com.see.truetransact.uicomponent.CCheckBox chkCustGrpLimitValidationAD;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitAD;
    private com.see.truetransact.uicomponent.CCheckBox chkEnhance;
    private com.see.truetransact.uicomponent.CCheckBox chkFlexiAD;
    private com.see.truetransact.uicomponent.CCheckBox chkHideBalanceTrans;
    private com.see.truetransact.uicomponent.CCheckBox chkInopChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkMobileBankingAD;
    private com.see.truetransact.uicomponent.CCheckBox chkNPAChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkNROStatusAD;
    private com.see.truetransact.uicomponent.CCheckBox chkNonMainMinBalChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkODClose;
    private com.see.truetransact.uicomponent.CCheckBox chkPassBook;
    private com.see.truetransact.uicomponent.CCheckBox chkPayIntOnCrBalIN;
    private com.see.truetransact.uicomponent.CCheckBox chkPayIntOnDrBalIN;
    private com.see.truetransact.uicomponent.CCheckBox chkPrimaryAccount;
    private com.see.truetransact.uicomponent.CCheckBox chkRenew;
    private com.see.truetransact.uicomponent.CCheckBox chkStmtChrgAD;
    private com.see.truetransact.uicomponent.CCheckBox chkStopPmtChrgAD;
    private com.see.truetransact.uicomponent.CDateField dtdATMFromDateAD;
    private com.see.truetransact.uicomponent.CDateField dtdATMToDateAD;
    private com.see.truetransact.uicomponent.CDateField dtdActOpenDateAI;
    private com.see.truetransact.uicomponent.CDateField dtdCredit;
    private com.see.truetransact.uicomponent.CDateField dtdCreditFromDateAD;
    private com.see.truetransact.uicomponent.CDateField dtdCreditToDateAD;
    private com.see.truetransact.uicomponent.CDateField dtdDebit;
    private com.see.truetransact.uicomponent.CDateField dtdDebitFromDateAD;
    private com.see.truetransact.uicomponent.CDateField dtdDebitToDateAD;
    private com.see.truetransact.uicomponent.CDateField dtdNPAChrgAD;
    private com.see.truetransact.uicomponent.CDateField dtdOpeningDateAI;
    private com.see.truetransact.uicomponent.CLabel jLabel1;
    private com.see.truetransact.uicomponent.CPanel jPanel2;
    private com.see.truetransact.uicomponent.CPanel jPanel3;
    private com.see.truetransact.uicomponent.CPanel jPanel4;
    private com.see.truetransact.uicomponent.CLabel lblABB;
    private com.see.truetransact.uicomponent.CLabel lblABBChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblATMCardNo;
    private com.see.truetransact.uicomponent.CLabel lblATMCardNoVal;
    private com.see.truetransact.uicomponent.CLabel lblATMFromDateAD;
    private com.see.truetransact.uicomponent.CLabel lblATMNoAD;
    private com.see.truetransact.uicomponent.CLabel lblATMToDateAD;
    private com.see.truetransact.uicomponent.CLabel lblAccCloseChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblAccOpeningChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAcctOpeningAmount;
    private com.see.truetransact.uicomponent.CLabel lblActHeadAI;
    private com.see.truetransact.uicomponent.CLabel lblActHeadValueAI;
    private com.see.truetransact.uicomponent.CLabel lblActName;
    private com.see.truetransact.uicomponent.CLabel lblActOpenDateAI;
    private com.see.truetransact.uicomponent.CLabel lblActStatusAI;
    private com.see.truetransact.uicomponent.CLabel lblActionDt;
    private com.see.truetransact.uicomponent.CLabel lblAgClearingIN;
    private com.see.truetransact.uicomponent.CLabel lblAgClearingValueIN;
    private com.see.truetransact.uicomponent.CLabel lblAgentIDVal;
    private com.see.truetransact.uicomponent.CLabel lblAgentId;
    private com.see.truetransact.uicomponent.CLabel lblAmoutTransAI;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaValue;
    private com.see.truetransact.uicomponent.CLabel lblAtmCardLimit;
    private com.see.truetransact.uicomponent.CLabel lblBaseCurrAI;
    private com.see.truetransact.uicomponent.CLabel lblBorrEligAmt;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerDob;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerNetworth;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerRetireDt;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerSalary;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerService;
    private com.see.truetransact.uicomponent.CLabel lblBranchCodeAI;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameAI;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameValueAI;
    private com.see.truetransact.uicomponent.CLabel lblCardActNumber;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblChequeBookChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblChequeReturn;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCityValue;
    private com.see.truetransact.uicomponent.CLabel lblClosedDt;
    private com.see.truetransact.uicomponent.CLabel lblCollectInoperative;
    private com.see.truetransact.uicomponent.CLabel lblCommAddr;
    private com.see.truetransact.uicomponent.CLabel lblConstitutionAI;
    private com.see.truetransact.uicomponent.CLabel lblContactNo;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCountryValue;
    private com.see.truetransact.uicomponent.CLabel lblCrInterestRateIN;
    private com.see.truetransact.uicomponent.CLabel lblCrInterestRateValueIN;
    private com.see.truetransact.uicomponent.CLabel lblCredit;
    private com.see.truetransact.uicomponent.CLabel lblCreditFromDateAD;
    private com.see.truetransact.uicomponent.CLabel lblCreditNoAD;
    private com.see.truetransact.uicomponent.CLabel lblCreditToDateAD;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdAI;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDOB;
    private com.see.truetransact.uicomponent.CLabel lblDOBValue;
    private com.see.truetransact.uicomponent.CLabel lblDealer;
    private com.see.truetransact.uicomponent.CLabel lblDealerIDVal;
    private com.see.truetransact.uicomponent.CLabel lblDebit;
    private com.see.truetransact.uicomponent.CLabel lblDebitFromDateAD;
    private com.see.truetransact.uicomponent.CLabel lblDebitNoAD;
    private com.see.truetransact.uicomponent.CLabel lblDebitToDateAD;
    private com.see.truetransact.uicomponent.CLabel lblDob;
    private com.see.truetransact.uicomponent.CLabel lblDrInterestRateIN;
    private com.see.truetransact.uicomponent.CLabel lblDrInterestRateValueIN;
    private com.see.truetransact.uicomponent.CLabel lblEligAmt;
    private com.see.truetransact.uicomponent.CLabel lblExcessWithChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblExistingAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblExistingCustomer;
    private com.see.truetransact.uicomponent.CLabel lblExistingCustomer1;
    private com.see.truetransact.uicomponent.CLabel lblFolioChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblGender;
    private com.see.truetransact.uicomponent.CLabel lblGroupCodeAI;
    private com.see.truetransact.uicomponent.CLabel lblHideBalanceTrans;
    private com.see.truetransact.uicomponent.CLabel lblLinkingActNameValue;
    private com.see.truetransact.uicomponent.CLabel lblLinkingActNum;
    private com.see.truetransact.uicomponent.CLabel lblLinkingProductId;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNetworth;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMemberSalary;
    private com.see.truetransact.uicomponent.CLabel lblMemberType;
    private com.see.truetransact.uicomponent.CLabel lblMinActBalanceAD;
    private com.see.truetransact.uicomponent.CLabel lblMinBal1FlexiAD;
    private com.see.truetransact.uicomponent.CLabel lblMinBal2FlexiAD;
    private com.see.truetransact.uicomponent.CLabel lblMinOrMaj;
    private com.see.truetransact.uicomponent.CLabel lblMisServiceChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblMobileNo;
    private com.see.truetransact.uicomponent.CLabel lblMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNPA;
    private com.see.truetransact.uicomponent.CLabel lblNPAChrgAD;
    private com.see.truetransact.uicomponent.CLabel lblNonMaintenance;
    private com.see.truetransact.uicomponent.CLabel lblODLimitAI;
    private com.see.truetransact.uicomponent.CLabel lblOpModeAI;
    private com.see.truetransact.uicomponent.CLabel lblOpeningDateAI;
    private com.see.truetransact.uicomponent.CLabel lblPanNumber1;
    private com.see.truetransact.uicomponent.CLabel lblPassBook;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestRateIN;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestValueIN;
    private com.see.truetransact.uicomponent.CLabel lblPin;
    private com.see.truetransact.uicomponent.CLabel lblPinValue;
    private com.see.truetransact.uicomponent.CLabel lblPrevActNoAI;
    private com.see.truetransact.uicomponent.CLabel lblPrevActNumAI;
    private com.see.truetransact.uicomponent.CLabel lblProductIdAI;
    private com.see.truetransact.uicomponent.CLabel lblREmarks;
    private com.see.truetransact.uicomponent.CLabel lblRateCodeIN;
    private com.see.truetransact.uicomponent.CLabel lblRateCodeValueIN;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRemarksAI;
    private com.see.truetransact.uicomponent.CLabel lblReqFlexiPeriodAD;
    private com.see.truetransact.uicomponent.CLabel lblReqFlexiPeriodAD1;
    private com.see.truetransact.uicomponent.CLabel lblRetireDt;
    private com.see.truetransact.uicomponent.CLabel lblRoleHierarchy;
    private com.see.truetransact.uicomponent.CLabel lblService;
    private com.see.truetransact.uicomponent.CLabel lblSettlementModeAI;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblSpace9;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStateValue;
    private com.see.truetransact.uicomponent.CLabel lblStatement;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStmtFreqAD;
    private com.see.truetransact.uicomponent.CLabel lblStopPayment;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblStreetValue;
    private javax.swing.JLabel lblTBSep1;
    private javax.swing.JLabel lblTBSep2;
    private com.see.truetransact.uicomponent.CLabel lblTBSep3;
    private javax.swing.JLabel lblTBSep4;
    private com.see.truetransact.uicomponent.CLabel lblUPIMobileno;
    private javax.swing.JMenuBar mbrTransfer;
    private javax.swing.JMenuItem mitAddNew;
    private javax.swing.JMenuItem mitAddTransferIn;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEditNew;
    private javax.swing.JMenuItem mitEditTransferIn;
    private javax.swing.JMenuItem mitResume;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuAction;
    private javax.swing.JMenu mnuAdd;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panATMAccountCardDetails;
    private com.see.truetransact.uicomponent.CPanel panATMCARDBtn;
    private com.see.truetransact.uicomponent.CPanel panATMCardDetails;
    private com.see.truetransact.uicomponent.CPanel panATMCardTableDetails;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAccounts;
    private com.see.truetransact.uicomponent.CPanel panActInfo;
    private com.see.truetransact.uicomponent.CPanel panAction;
    private com.see.truetransact.uicomponent.CPanel panAgentID;
    private com.see.truetransact.uicomponent.CPanel panAgentID1;
    private com.see.truetransact.uicomponent.CPanel panBorrowerSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panBorrowerServiceDetails;
    private com.see.truetransact.uicomponent.CPanel panBranch;
    private com.see.truetransact.uicomponent.CPanel panCardInfo;
    private com.see.truetransact.uicomponent.CPanel panCustDet;
    private com.see.truetransact.uicomponent.CPanel panCustOperation;
    private com.see.truetransact.uicomponent.CPanel panCustomerInfo;
    private com.see.truetransact.uicomponent.CPanel panCustomerSide1;
    private com.see.truetransact.uicomponent.CPanel panDiffCharges;
    private com.see.truetransact.uicomponent.CPanel panExistingCustomer;
    private com.see.truetransact.uicomponent.CPanel panFlexiOpt;
    private com.see.truetransact.uicomponent.CPanel panGender;
    private com.see.truetransact.uicomponent.CPanel panInsideAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panInsideCustTable;
    private com.see.truetransact.uicomponent.CPanel panInterestPayableIN;
    private com.see.truetransact.uicomponent.CPanel panIsRequired;
    private com.see.truetransact.uicomponent.CPanel panLastIntApp;
    private com.see.truetransact.uicomponent.CPanel panLinkingActNum;
    private com.see.truetransact.uicomponent.CPanel panMobileBanking;
    private com.see.truetransact.uicomponent.CPanel panRatesIN;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails;
    private com.see.truetransact.uicomponent.CPanel panServiceDetails;
    private com.see.truetransact.uicomponent.CPanel panShowSurety;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuretyDetails;
    private com.see.truetransact.uicomponent.CPanel panTransferBranchInfo;
    private com.see.truetransact.uicomponent.CPanel panTransfered;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExistingCustomer;
    private com.see.truetransact.uicomponent.CButtonGroup rdgKYCNormsFollowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoActionRevoke;
    private com.see.truetransact.uicomponent.CRadioButton rdoActionStop;
    private com.see.truetransact.uicomponent.CRadioButton rdoExistingCustomer_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoExistingCustomer_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoKYCNormsNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoKYCNormsYes;
    private javax.swing.JSeparator sptAction;
    private javax.swing.JSeparator sptAdd;
    private javax.swing.JSeparator sptEdit;
    private javax.swing.JSeparator sptProcess1;
    private javax.swing.JSeparator sptProcess2;
    private com.see.truetransact.uicomponent.CScrollPane srpATMCardTable;
    private com.see.truetransact.uicomponent.CScrollPane srpAct_Joint;
    private com.see.truetransact.uicomponent.CScrollPane srpCardRemarks;
    private com.see.truetransact.uicomponent.CScrollPane srpShowSurety;
    private com.see.truetransact.uicomponent.CTabbedPane tabAccounts;
    private com.see.truetransact.uicomponent.CTable tblATMCardDetails;
    private com.see.truetransact.uicomponent.CTable tblAct_Joint;
    private com.see.truetransact.uicomponent.CTable tblMemberType;
    private com.see.truetransact.uicomponent.CToolBar tbrAccounts;
    private com.see.truetransact.uicomponent.CDateField tdtActionDt;
    private com.see.truetransact.uicomponent.CDateField tdtMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CTextField txtABBChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtATMNoAD;
    private com.see.truetransact.uicomponent.CTextField txtAccCloseChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtAccOpeningChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtAcctOpeningAmount;
    private com.see.truetransact.uicomponent.CTextField txtActName;
    private com.see.truetransact.uicomponent.CTextField txtAgentID;
    private com.see.truetransact.uicomponent.CTextField txtAmoutTransAI;
    private com.see.truetransact.uicomponent.CTextField txtAppliedAmt;
    private com.see.truetransact.uicomponent.CTextField txtAtmCardLimit;
    private com.see.truetransact.uicomponent.CTextField txtBorrowerNetworth;
    private com.see.truetransact.uicomponent.CTextField txtBorrowerSalary;
    private com.see.truetransact.uicomponent.CTextField txtBranchCodeAI;
    private com.see.truetransact.uicomponent.CTextField txtCardActNumber;
    private com.see.truetransact.uicomponent.CTextArea txtCardRemarks;
    private com.see.truetransact.uicomponent.CTextField txtChequeBookChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtClosedDt;
    private com.see.truetransact.uicomponent.CTextField txtContactNo;
    private com.see.truetransact.uicomponent.CTextField txtCreditNoAD;
    public static com.see.truetransact.uicomponent.CTextField txtCustomerIdAI;
    private com.see.truetransact.uicomponent.CTextField txtDealerID;
    private com.see.truetransact.uicomponent.CTextField txtDebitNoAD;
    private com.see.truetransact.uicomponent.CTextField txtEditOperativeNo;
    private com.see.truetransact.uicomponent.CTextField txtExcessWithChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtExistingAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtFolioChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtLinkingActNum;
    private com.see.truetransact.uicomponent.CTextField txtMemberName;
    private com.see.truetransact.uicomponent.CTextField txtMemberNetworth;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtMemberSalary;
    private com.see.truetransact.uicomponent.CTextField txtMemberType;
    private com.see.truetransact.uicomponent.CTextField txtMinActBalanceAD;
    private com.see.truetransact.uicomponent.CTextField txtMinBal1FlexiAD;
    private com.see.truetransact.uicomponent.CTextField txtMinBal2FlexiAD;
    private com.see.truetransact.uicomponent.CTextField txtMisServiceChrgAD;
    private com.see.truetransact.uicomponent.CTextField txtMobileNo;
    private com.see.truetransact.uicomponent.CTextField txtNextAccNo;
    private com.see.truetransact.uicomponent.CTextField txtODLimitAI;
    private com.see.truetransact.uicomponent.CTextField txtPrevActNumAI;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRemarksAI;
    private com.see.truetransact.uicomponent.CTextField txtReqFlexiPeriodAD;
    private com.see.truetransact.uicomponent.CTextField txtUPIMobileNo;
    // End of variables declaration//GEN-END:variables
    private com.see.truetransact.ui.common.CAddressPanel addressPanelCustAI;
    private com.see.truetransact.ui.common.CAddressPanel addressPanelINP5;
    private com.see.truetransact.ui.common.CAddressPanel addressPanelNomineeNO;
    private com.see.truetransact.ui.common.CAddressPanel addressPanelGuardianNO;
    private com.see.truetransact.ui.common.CAddressPanel addressPanelPOAPA;

    public static void main(String[] args) {
        AccountsUI ac = new AccountsUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(ac);
        j.setSize(600, 650);
        j.show();
        ac.show();
    }
}
