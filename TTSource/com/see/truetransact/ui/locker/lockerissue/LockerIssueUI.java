/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountsUI.java
 *
 * Created on August 6, 2003, 10:50 AM
 */
package com.see.truetransact.ui.locker.lockerissue;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.*;
import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.CMandatoryDialog;

import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.ui.common.powerofattorney.*;
import com.see.truetransact.ui.common.authorizedsignatory.*;
import com.see.truetransact.ui.product.operativeacct.*;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.HashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.List;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.ui.common.transaction.TransactionUI;

import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.uicomponent.COptionPane;

//import com.see.truetransact.ui.product.operativeacct;

/**
 *
 * @author annamalai_t1
 * @author Pranav
 * @author Rahul
 * @author 152721
 * @author K.R.Jayakrishnan May 31, 2004
 */
public class LockerIssueUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    //    private AccountsRB resourceBundle = new AccountsRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockerissue.LockerIssueRB", ProxyParameters.LANGUAGE);
    LockerIssueMRB objMandatoryRB = new LockerIssueMRB();
    private LockerIssueOB observable;
    private String viewType = "";
    private int yes = 0;
    private int no = 1;
    private HashMap mandatoryMap;
    private HashMap acountParamMap = new HashMap();
    private boolean dontShowJointDialog = false;
    final int DELETE = 1, AUTHORIZE = 2;
    String minNominee = "";
    final String SCREEN = "OA";
    private int selectedRow = -1;
    private int selectedRowChrg = -1;
    private int selectedData = 0;
    private int selectedDataChrg = 0;
    private int resultChrg;
    private String issueId = "";
    private double serviceTaxPercentage = 0;
    private HashMap depProdDetails;
    private TransactionUI transactionUI = new TransactionUI();
    private boolean resetPasswordDetails = false;
    String depSus = "";
    private String transType = "";
    private boolean resetPwdTrue= false;
    private boolean fromAuthorizeUI = false;
    ResetLockerPasswordApproveUI resetLockerPasswordApproveUI = null;
    AuthorizeListUI authorizeListUI = null;
    private String authorizeStatus = null;
    private int rejectFlag = 0;
    /**
     * Declare a new instance of the NomineeUI and IntroducerUI...
     */
    NomineeUI nomineeUi = new NomineeUI(SCREEN, false);
    PowerOfAttorneyUI poaUI = new PowerOfAttorneyUI(SCREEN);
    AuthorizedSignatoryUI authSignUI = new AuthorizedSignatoryUI(SCREEN);
    private java.util.Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private List lockerTransHeadList ;
    private ServiceTaxCalculation objServiceTax;// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
    public HashMap serviceTax_Map;

    /** Creates new form AccountsUI */
    public LockerIssueUI() {
        // first generate the controls
        //panOperations2.setVisible(false);
        initComponents();
        initStartup();
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        currDt = ClientUtil.getCurrentDate();
    }

    private void initStartup() {
        // then set the names for the controls using setName()
        setFieldNames();
        panOperations2.setVisible(false);
        /* call the intenationalize() method to load the RB values
         * and initialize the Observable for this class
         */
        internationalize();
        setMandatoryHashMap();
        setObservable();

        /* Fill up all the combo boxes and set up the initial values for
         * the radio buttons
         */
        initComponentData();
        transactionUI.setSourceScreen("LOCKER_ISSUE");
        transactionUI.setProdType();
        // Disable all the screen
        ClientUtil.enableDisable(this, false);
        // Some other fields should also be disabled at init time
        enableDisableComponents(false);
        /*
         * To Clear all the fields and Labels...
         */
        observable.resetCustDetails();
        observable.resetJntAccntHoldTbl();
        observable.resetOBFields();
        observable.resetLabels();
        setupMenuToolBarPanel();
        setHelpMessage();
        //cboProdType.setModel(observable.getCbmProdType());
        setMaxLenths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panActInfo);
        //Hide currency..comboBox
        this.lblBaseCurrAI.setVisible(false);
        this.cboBaseCurrAI.setVisible(false);
        //--- Disables the Joint account holder Buttons
        setBtnJointAccnt(false);

        //__ To reset the tabs visited...
//        lblActHeadAI.setVisible(false);
        // lblAccountNumber.setVisible(false);
//        lblActHeadValueAI.setVisible(false);
        tabAccounts.resetVisits();
        txtClosedDt.setVisible(false);
        lblClosedDt.setVisible(false);
        // lblODLimitAI.setVisible(false);
        // txtODLimitAI.setVisible(false);
        // lblGroupCodeAI.setVisible(false);
        // cboGroupCodeAI.setVisible(false);
        btnLockerNo.setEnabled(false);
        btnDepositNo.setEnabled(false);
        ClientUtil.enableDisable(panOperations, false, false, true);
        tabAccounts.remove(panBillsCharges);
        btnPasNew.setVisible(false);
        btnPasDelete.setVisible(false);
        //lblSettlementModeAI.setVisible(false);
        // cboSettlementModeAI.setVisible(false);
        txtServiceTax.setEnabled(false);
        lblfreezRemarks.setVisible(false);
        txtFreezeRemarks.setVisible(false);
        lblFreezeDt.setVisible(false);
        dtdFreezeDt.setVisible(false);
        tabAccounts.resetVisits();
        btnResetPwd.setEnabled(false);
    }

    private void setBtnJointAccnt(boolean val) {
        btnCustDetNew.setEnabled(val);
        btnCustDetDelete.setEnabled(val);
        btnToMain.setEnabled(val);
    }

    private void disbleComponents() {
        //        txtCustomerIdAI.setEnabled(false);
        txtLockerNo.setEnabled(false);
        txtPrevActNumAI.setEnabled(false);
        dtdOpeningDateAI.setEnabled(false);
        // dtdExpiryDate.setEnabled(false);
        txtRemarks.setEnabled(false);
        panChargesServiceTax.setEnabled(false);
    }

    /* this method will be called to setup the screen for "Transfer In" account
     */
    private void setupComponentsForNew() {
        dtdOpeningDateAI.setDateValue(DateUtil.getStringDate(currDt));
        dtdOpeningDateAI.setEnabled(false);
        //  dtdExpiryDate.setDateValue(DateUtil.getStringDate(DateUtil.addDays(currDt,365)));
        // dtdExpiryDate.setEnabled(false);
    }

    /* now setup the menu, as it should be based on the current operation,
     * which at startup is "NOOP"
     */
    private void setupMenuToolBarPanel() {

        /* disable the previous account number text/label, we don;t require
         * them anymore
         */
        lblPrevActNumAI.setVisible(true);
        txtPrevActNumAI.setVisible(true);

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
            mitAuthorize.setEnabled(true);
            mitEditNew.setEnabled(true);
            mitClose.setEnabled(true);
            mitDelete.setEnabled(true);

            // Introducer's panel
            // This will only happen  when the user selects the TransferIn option
            //            panTransferBranchInfo.setVisible(false);

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
            mitAuthorize.setEnabled(false);
            mitEditNew.setEnabled(false);
            mitDelete.setEnabled(false);

            // Enabled
            mitCancel.setEnabled(true);
            mitClose.setEnabled(true);
            mitResume.setEnabled(true);
            mitSave.setEnabled(true);

        }
    }

    /* Fill up all the combo boxes and set up the initial values for
     * the radio buttons
     */
    private void initComponentData() {

        // fill all the combo boxes, with the data from lookup table, and others
        tblInstruction2.setModel(observable.getTbmInstructions2());
        tblLockCharges.setModel(observable.getTbmLockCharges());
        // .setModel(observable.getTbmInstructions2());
        cboProductIdAI.setModel(observable.getCbmProductIdAI());
        cboConstitutionAI.setModel(observable.getCbmConstitutionAI());
        cboOpModeAI.setModel(observable.getCbmOpModeAI());
        cboProdType.setModel(observable.getCbmProdType());
        //cboProdId.setModel(observable.getCbmProdId());
        // cboProductId.setModel();
        //cboProductId.setModel();

        // transactionUI.setSourceScreen("LOCKER_ISSUE");
        //        cboCommAddr.setModel(observable.getCbmCommAddr());
        //cboGroupCodeAI.setModel(observable.getCbmGroupCodeAI());
        //cboSettlementModeAI.setModel(observable.getCbmSettlementModeAI());
        cboCategory.setModel(observable.getCbmCategory());
        cboChargeType.setModel(observable.getCbmChargeType());
        //        cboBaseCurrAI.setModel(observable.getCbmBaseCurrAI());
        //        cboDMYAD.setModel(observable.getCbmDMYAD());
        //        cboStmtFreqAD.setModel(observable.getCbmStmtFreqAD());
        //        cboPreviousActNo.setModel(observable.getCbmPrevAcctNo());
        //        cboRoleHierarchy.setModel(observable.getCbmRoleHierarchy());
    }

    private void setPasswordTab(HashMap hash) {
        HashMap mapData = new HashMap();
        //        mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected()));
        mapData.put("NAME", hash.get("NAME"));
        mapData.put("CUSTOMER_TYPE", hash.get("CUSTOMER TYPE"));
        if (hash.containsKey("CUSTOMER ID")) {
            mapData.put("CUSTOMER_ID", hash.get("CUSTOMER ID"));
        } else if (hash.containsKey("CUSTID")) {
            mapData.put("CUSTOMER_ID", hash.get("CUSTID"));
        }

        if (hash.containsKey("ADDCUST")) {
            mapData.put("ADDCUST", hash.get("ADDCUST"));
        }
        if (resetPasswordDetails) {
            observable.setExistingPwd();
            observable.resetPassTable();
        }
        observable.populateTblActData(mapData);
        tblInstruction2.setModel(observable.getTbmInstructions2());
        resetPasswordDetails = false;
        mapData = null;
    }

    private void setLockerCharges(String prodID) {
        //        HashMap mapData = new HashMap();
        ////        mapData.put("PRODUCT_ID", CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected()));
        //                    mapData.put("NAME", hash.get("NAME"));
        //                    mapData.put("CUSTOMER_TYPE", hash.get("CUSTOMER TYPE"));
        //                    mapData.put("CUSTOMER_ID", hash.get("CUSTOMER ID"));
        //                    observable.populateLockerCharges(prodID);  // Commented by Rajesh
        //                    tblLockCharges.setModel(observable.getTbmLockCharges());
        //                    mapData = null;
        HashMap stMap = new HashMap();
        stMap.put("PROD_ID", prodID);
        stMap.put("TODAY_DT", currDt);
        stMap.put("CHARGE_TYPE", "RENT_CHARGES");
        List stList = ClientUtil.executeQuery("getServiceTaxAndComissionForIssue", stMap);
        if (stList != null && stList.size() > 0) {
            stMap = null;
            stMap = (HashMap) stList.get(0);
            //txtCharges.setText
            //  double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();

            //  double com = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
            //double ser;
            //int com=(CommonUtil.convertObjToInt(stMap.get("COMMISION")));



            // double rcomm=rcom+ser;

            double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
            double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
            serviceTaxPercentage = serviceTax;
            serviceTax = comm * serviceTax / 100;
            serviceTax = (double) getNearest((long) (serviceTax * 100), 100) / 100;
            txtServiceTax.setText(String.valueOf(serviceTax));
            txtCharges.setText(String.valueOf(comm));
            transactionUI.setCallingAmount(String.valueOf(comm + serviceTax));
            lblTotAmtVal.setText(String.valueOf(comm + serviceTax));
            System.out.println("#####rentAmt+servTx" + (comm + serviceTax));
        } else {
            ClientUtil.showMessageWindow("Error In Populating Comission and Service Tax");
        }
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

            btnCustomerIdAI.setEnabled(truefalse);
        }
    }

    private void setObservable() {
        try {
            observable = new LockerIssueOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMaxLenths() {
        //txtODLimitAI.setMaxLength(16);

        //txtODLimitAI.setValidation(new CurrencyValidation(8,2));

        //        txtATMNoAD.setValidation(new NumericValidation());
        //
        //        txtDebitNoAD.setMaxLength(16);
        //        txtDebitNoAD.setValidation(new NumericValidation());
        //
        //        txtCreditNoAD.setMaxLength(16);
        //        txtCreditNoAD.setValidation(new NumericValidation());
        //
        //        txtABBChrgAD.setValidation(new CurrencyValidation(14,2));
        //
        //        txtMinBal1FlexiAD.setMaxLength(16);
        //        txtMinBal1FlexiAD.setValidation(new CurrencyValidation(8,2));
        //
        //        txtMinBal2FlexiAD.setMaxLength(16);
        //        txtMinBal2FlexiAD.setValidation(new CurrencyValidation(8,2));

        //        txtReqFlexiPeriodAD.setMaxLength(16); //10
        //        txtReqFlexiPeriodAD.setValidation(new NumericValidation());
        //
        //        txtAccOpeningChrgAD.setValidation(new CurrencyValidation(14,2));
        //
        //        txtMinActBalanceAD.setValidation(new CurrencyValidation(14,2));
        //
        //        txtChequeBookChrgAD.setValidation(new CurrencyValidation(14,2));
        //
        //        txtMisServiceChrgAD.setValidation(new CurrencyValidation(14,2));
        //
        //        txtFolioChrgAD.setValidation(new CurrencyValidation(14,2));
        //
        //        txtAccCloseChrgAD.setValidation(new CurrencyValidation(14,2));
        //
        //        txtExcessWithChrgAD.setValidation(new CurrencyValidation(14,2));
        txtCollectRentMM.setMaxLength(2);
        txtCollectRentMM.setValidation(new NumericValidation());
        txtActName.setMaxLength(32);
        txtRemarks.setMaxLength(64);
        txtCustomerIdCr.setAllowAll(true);

        // txtCustomerIdAI.setMaxLength(20);
        //txtCustomerIdAI.setValidation(new NumericValidation());
        txtCollectRentyyyy.setMaxLength(4);
        txtCollectRentyyyy.setValidation(new NumericValidation());
        txtFreezeRemarks.setMaxLength(100);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccounts = new com.see.truetransact.uicomponent.CPanel();
        tabAccounts = new com.see.truetransact.uicomponent.CTabbedPane();
        panActInfo = new com.see.truetransact.uicomponent.CPanel();
        panCustomerInfo = new com.see.truetransact.uicomponent.CPanel();
        sepSepAI = new com.see.truetransact.uicomponent.CSeparator();
        srpAct_Joint = new com.see.truetransact.uicomponent.CScrollPane();
        tblAct_Joint = new com.see.truetransact.uicomponent.CTable();
        panCustOperation = new com.see.truetransact.uicomponent.CPanel();
        btnCustDetDelete = new com.see.truetransact.uicomponent.CButton();
        btnCustDetNew = new com.see.truetransact.uicomponent.CButton();
        btnToMain = new com.see.truetransact.uicomponent.CButton();
        btnPhotoSign = new com.see.truetransact.uicomponent.CButton();
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
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        lblProductIdAI = new com.see.truetransact.uicomponent.CLabel();
        cboProductIdAI = new com.see.truetransact.uicomponent.CComboBox();
        lblConstitutionAI = new com.see.truetransact.uicomponent.CLabel();
        cboConstitutionAI = new com.see.truetransact.uicomponent.CComboBox();
        lblCustomerIdAI = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerIdAI = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerIdAI = new com.see.truetransact.uicomponent.CButton();
        lblLockerNo = new com.see.truetransact.uicomponent.CLabel();
        txtLockerNo = new com.see.truetransact.uicomponent.CTextField();
        btnLockerNo = new com.see.truetransact.uicomponent.CButton();
        lblLockerKeyNo = new com.see.truetransact.uicomponent.CLabel();
        lblPrevActNumAI = new com.see.truetransact.uicomponent.CLabel();
        txtPrevActNumAI = new com.see.truetransact.uicomponent.CTextField();
        btnDepositNo = new com.see.truetransact.uicomponent.CButton();
        lblActName = new com.see.truetransact.uicomponent.CLabel();
        txtActName = new com.see.truetransact.uicomponent.CTextField();
        lblCommAddr = new com.see.truetransact.uicomponent.CLabel();
        cboCommAddr = new com.see.truetransact.uicomponent.CComboBox();
        lblOpeningDateAI = new com.see.truetransact.uicomponent.CLabel();
        dtdOpeningDateAI = new com.see.truetransact.uicomponent.CDateField();
        lblCollectRent = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate = new com.see.truetransact.uicomponent.CLabel();
        dtdExpiryDate = new com.see.truetransact.uicomponent.CDateField();
        lblOpModeAI = new com.see.truetransact.uicomponent.CLabel();
        cboOpModeAI = new com.see.truetransact.uicomponent.CComboBox();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblBaseCurrAI = new com.see.truetransact.uicomponent.CLabel();
        cboBaseCurrAI = new com.see.truetransact.uicomponent.CComboBox();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblClosedDt = new com.see.truetransact.uicomponent.CLabel();
        txtClosedDt = new com.see.truetransact.uicomponent.CTextField();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblLockerKeyNoVal = new com.see.truetransact.uicomponent.CLabel();
        lblStreetValue1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblPwdRequired = new com.see.truetransact.uicomponent.CLabel();
        panOperations1 = new com.see.truetransact.uicomponent.CPanel();
        txtCollectRentMM = new com.see.truetransact.uicomponent.CTextField();
        txtCollectRentyyyy = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        panOperations2 = new com.see.truetransact.uicomponent.CPanel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountNo1 = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerIdCr = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerIdFileOpenCr = new com.see.truetransact.uicomponent.CButton();
        lblCustomerNameCrValue = new com.see.truetransact.uicomponent.CLabel();
        panOperations3 = new com.see.truetransact.uicomponent.CPanel();
        rbtnSiYes = new com.see.truetransact.uicomponent.CRadioButton();
        rbtnSiNo = new com.see.truetransact.uicomponent.CRadioButton();
        panOperations4 = new com.see.truetransact.uicomponent.CPanel();
        rbtnPwdYes = new com.see.truetransact.uicomponent.CRadioButton();
        rbtnPwdNo = new com.see.truetransact.uicomponent.CRadioButton();
        rbtnPwdYes1 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFreeze = new com.see.truetransact.uicomponent.CRadioButton();
        rdoUnFreeze = new com.see.truetransact.uicomponent.CRadioButton();
        lblFreezeDt = new com.see.truetransact.uicomponent.CLabel();
        dtdFreezeDt = new com.see.truetransact.uicomponent.CDateField();
        lblfreezRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtFreezeRemarks = new com.see.truetransact.uicomponent.CTextField();
        cboSuspDep = new com.see.truetransact.uicomponent.CComboBox();
        chkNoTransaction = new com.see.truetransact.uicomponent.CCheckBox();
        panActInfo1 = new com.see.truetransact.uicomponent.CPanel();
        panInstEntry = new com.see.truetransact.uicomponent.CPanel();
        panOperations = new com.see.truetransact.uicomponent.CPanel();
        btnPasNew = new com.see.truetransact.uicomponent.CButton();
        btnPastSave = new com.see.truetransact.uicomponent.CButton();
        btnPasDelete = new com.see.truetransact.uicomponent.CButton();
        panStdInstructions = new com.see.truetransact.uicomponent.CPanel();
        lblPassword = new com.see.truetransact.uicomponent.CLabel();
        lblConPassword = new com.see.truetransact.uicomponent.CLabel();
        lblPasCustId = new com.see.truetransact.uicomponent.CLabel();
        txtPasCustId = new com.see.truetransact.uicomponent.CTextField();
        txtPassword = new com.see.truetransact.uicomponent.CPasswordField();
        txtConPassword = new com.see.truetransact.uicomponent.CPasswordField();
        lblCurPassword = new com.see.truetransact.uicomponent.CLabel();
        txtCurPassword = new com.see.truetransact.uicomponent.CPasswordField();
        srpInstructions = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstruction2 = new com.see.truetransact.uicomponent.CTable();
        jSeparator1 = new javax.swing.JSeparator();
        panChargesServiceTax = new com.see.truetransact.uicomponent.CPanel();
        lblCharges = new com.see.truetransact.uicomponent.CLabel();
        txtCharges = new com.see.truetransact.uicomponent.CTextField();
        lblSerTax = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        chkEditPwd = new com.see.truetransact.uicomponent.CCheckBox();
        btnResetPwd = new com.see.truetransact.uicomponent.CButton();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panBillsCharges = new com.see.truetransact.uicomponent.CPanel();
        panDiscountRate1 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills4 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills5 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills6 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills7 = new com.see.truetransact.uicomponent.CPanel();
        panTransitPeriod1 = new com.see.truetransact.uicomponent.CPanel();
        panCharges1 = new com.see.truetransact.uicomponent.CPanel();
        sprRemitProdCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblLockCharges = new com.see.truetransact.uicomponent.CTable();
        panRemitProdCharges = new com.see.truetransact.uicomponent.CPanel();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        lblChargeType = new com.see.truetransact.uicomponent.CLabel();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        cboChargeType = new com.see.truetransact.uicomponent.CComboBox();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        lblToDt = new com.see.truetransact.uicomponent.CLabel();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTax2 = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax2 = new com.see.truetransact.uicomponent.CLabel();
        panRemitProdChargesButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        panAliasBrchRemittNumber = new com.see.truetransact.uicomponent.CPanel();
        tbrAccounts = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblTBSep4 = new javax.swing.JLabel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblTBSep1 = new javax.swing.JLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblTBSep2 = new javax.swing.JLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblTBSep3 = new com.see.truetransact.uicomponent.CLabel();
        btnReport = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnDeletedDetails = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTransfer = new javax.swing.JMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mnuAdd = new javax.swing.JMenu();
        mitAddNew = new javax.swing.JMenuItem();
        sptAdd = new javax.swing.JSeparator();
        mnuEdit = new javax.swing.JMenu();
        mitEditNew = new javax.swing.JMenuItem();
        sptEdit = new javax.swing.JSeparator();
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
        setMinimumSize(new java.awt.Dimension(930, 650));
        setPreferredSize(new java.awt.Dimension(930, 650));

        panAccounts.setLayout(new java.awt.GridBagLayout());

        tabAccounts.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabAccountsFocusLost(evt);
            }
        });

        panActInfo.setLayout(new java.awt.GridBagLayout());

        panCustomerInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Info."));
        panCustomerInfo.setMinimumSize(new java.awt.Dimension(301, 226));
        panCustomerInfo.setName("panCustomerInfo"); // NOI18N
        panCustomerInfo.setPreferredSize(new java.awt.Dimension(301, 324));
        panCustomerInfo.setLayout(new java.awt.GridBagLayout());

        sepSepAI.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerInfo.add(sepSepAI, gridBagConstraints);

        srpAct_Joint.setMinimumSize(new java.awt.Dimension(295, 200));
        srpAct_Joint.setPreferredSize(new java.awt.Dimension(295, 200));

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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerInfo.add(srpAct_Joint, gridBagConstraints);

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

        btnPhotoSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PHOTO_SIGN.gif"))); // NOI18N
        btnPhotoSign.setMaximumSize(new java.awt.Dimension(70, 25));
        btnPhotoSign.setMinimumSize(new java.awt.Dimension(70, 25));
        btnPhotoSign.setPreferredSize(new java.awt.Dimension(70, 25));
        btnPhotoSign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoSignActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panCustOperation.add(btnPhotoSign, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panCustomerInfo.add(panCustOperation, gridBagConstraints);

        panCustDet.setMinimumSize(new java.awt.Dimension(329, 145));
        panCustDet.setPreferredSize(new java.awt.Dimension(329, 145));
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
        gridBagConstraints.gridy = 3;
        panCustomerInfo.add(panCustDet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        panActInfo.add(panCustomerInfo, gridBagConstraints);

        panAccountInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Locker Account Info."));
        panAccountInfo.setMinimumSize(new java.awt.Dimension(596, 624));
        panAccountInfo.setName("panAccountInfo"); // NOI18N
        panAccountInfo.setPreferredSize(new java.awt.Dimension(590, 601));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        lblProductIdAI.setText("Account Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panAccountInfo.add(cboProductIdAI, gridBagConstraints);

        lblConstitutionAI.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblConstitutionAI, gridBagConstraints);

        cboConstitutionAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboConstitutionAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConstitutionAIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panAccountInfo.add(cboConstitutionAI, gridBagConstraints);

        lblCustomerIdAI.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblCustomerIdAI, gridBagConstraints);

        txtCustomerIdAI.setEditable(false);
        txtCustomerIdAI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerIdAI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdAIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(txtCustomerIdAI, gridBagConstraints);

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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountInfo.add(btnCustomerIdAI, gridBagConstraints);

        lblLockerNo.setText("Locker No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblLockerNo, gridBagConstraints);

        txtLockerNo.setEditable(false);
        txtLockerNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(txtLockerNo, gridBagConstraints);

        btnLockerNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerNo.setToolTipText("Customer Data");
        btnLockerNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountInfo.add(btnLockerNo, gridBagConstraints);

        lblLockerKeyNo.setText("Locker Key No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountInfo.add(lblLockerKeyNo, gridBagConstraints);

        lblPrevActNumAI.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblPrevActNumAI, gridBagConstraints);

        txtPrevActNumAI.setAllowAll(true);
        txtPrevActNumAI.setMaxLength(20);
        txtPrevActNumAI.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(txtPrevActNumAI, gridBagConstraints);

        btnDepositNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositNo.setToolTipText("Customer Data");
        btnDepositNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountInfo.add(btnDepositNo, gridBagConstraints);

        lblActName.setText("Account Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblActName, gridBagConstraints);

        txtActName.setEnabled(false);
        txtActName.setMaxLength(10);
        txtActName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtActName.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(txtActName, gridBagConstraints);

        lblCommAddr.setText("Communication Addr Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblCommAddr, gridBagConstraints);

        cboCommAddr.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCommAddr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCommAddrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(cboCommAddr, gridBagConstraints);

        lblOpeningDateAI.setText("Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblOpeningDateAI, gridBagConstraints);

        dtdOpeningDateAI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdOpeningDateAIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(dtdOpeningDateAI, gridBagConstraints);

        lblCollectRent.setText("Collect Rent Upto");
        lblCollectRent.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblCollectRentMouseMoved(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblCollectRent, gridBagConstraints);

        lblExpiryDate.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblExpiryDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(dtdExpiryDate, gridBagConstraints);

        lblOpModeAI.setText("Mode of Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblOpModeAI, gridBagConstraints);

        cboOpModeAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOpModeAI.setPopupWidth(290);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(cboOpModeAI, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblCategory, gridBagConstraints);

        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(cboCategory, gridBagConstraints);

        lblBaseCurrAI.setText("Base Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblBaseCurrAI, gridBagConstraints);

        cboBaseCurrAI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBaseCurrAI.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(cboBaseCurrAI, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(txtRemarks, gridBagConstraints);

        lblClosedDt.setText("Closed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblClosedDt, gridBagConstraints);

        txtClosedDt.setMaxLength(10);
        txtClosedDt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(txtClosedDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panAccountInfo.add(lblAccountNo, gridBagConstraints);

        lblCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustName.setMinimumSize(new java.awt.Dimension(70, 13));
        lblCustName.setPreferredSize(new java.awt.Dimension(140, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(lblCustName, gridBagConstraints);

        lblLockerKeyNoVal.setMinimumSize(new java.awt.Dimension(50, 13));
        lblLockerKeyNoVal.setPreferredSize(new java.awt.Dimension(50, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(lblLockerKeyNoVal, gridBagConstraints);

        lblStreetValue1.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblStreetValue1.setMinimumSize(new java.awt.Dimension(70, 10));
        lblStreetValue1.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        panAccountInfo.add(lblStreetValue1, gridBagConstraints);

        cLabel1.setText("S.I");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(cLabel1, gridBagConstraints);

        lblPwdRequired.setText("Password Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountInfo.add(lblPwdRequired, gridBagConstraints);

        panOperations1.setLayout(new java.awt.GridBagLayout());

        txtCollectRentMM.setPreferredSize(new java.awt.Dimension(30, 21));
        txtCollectRentMM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCollectRentMMActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        panOperations1.add(txtCollectRentMM, gridBagConstraints);

        txtCollectRentyyyy.setPreferredSize(new java.awt.Dimension(60, 21));
        txtCollectRentyyyy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCollectRentyyyyActionPerformed(evt);
            }
        });
        txtCollectRentyyyy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCollectRentyyyyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        panOperations1.add(txtCollectRentyyyy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(panOperations1, gridBagConstraints);

        cLabel2.setText("MM/YYYY");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        panAccountInfo.add(cLabel2, gridBagConstraints);

        panOperations2.setMinimumSize(new java.awt.Dimension(241, 94));
        panOperations2.setPreferredSize(new java.awt.Dimension(231, 87));
        panOperations2.setLayout(new java.awt.GridBagLayout());

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panOperations2.add(lblProductType, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(125);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        cboProdType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProdTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOperations2.add(cboProdType, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panOperations2.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(200);
        cboProdId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProdIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOperations2.add(cboProdId, gridBagConstraints);

        lblAccountNo1.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panOperations2.add(lblAccountNo1, gridBagConstraints);

        txtCustomerIdCr.setEditable(false);
        txtCustomerIdCr.setMinimumSize(new java.awt.Dimension(97, 21));
        txtCustomerIdCr.setPreferredSize(new java.awt.Dimension(97, 21));
        txtCustomerIdCr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdCrFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOperations2.add(txtCustomerIdCr, gridBagConstraints);

        btnCustomerIdFileOpenCr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerIdFileOpenCr.setPreferredSize(new java.awt.Dimension(18, 18));
        btnCustomerIdFileOpenCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdFileOpenCrActionPerformed(evt);
            }
        });
        btnCustomerIdFileOpenCr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnCustomerIdFileOpenCrFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 22;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOperations2.add(btnCustomerIdFileOpenCr, gridBagConstraints);

        lblCustomerNameCrValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameCrValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameCrValue.setMinimumSize(new java.awt.Dimension(95, 18));
        lblCustomerNameCrValue.setPreferredSize(new java.awt.Dimension(95, 18));
        lblCustomerNameCrValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblCustomerNameCrValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panOperations2.add(lblCustomerNameCrValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountInfo.add(panOperations2, gridBagConstraints);

        panOperations3.setLayout(new java.awt.GridBagLayout());

        rbtnSiYes.setText("Yes");
        rbtnSiYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSiYesActionPerformed(evt);
            }
        });
        rbtnSiYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rbtnSiYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panOperations3.add(rbtnSiYes, gridBagConstraints);

        rbtnSiNo.setText("No");
        rbtnSiNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSiNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOperations3.add(rbtnSiNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(panOperations3, gridBagConstraints);

        panOperations4.setLayout(new java.awt.GridBagLayout());

        rbtnPwdYes.setText("Yes");
        rbtnPwdYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPwdYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        panOperations4.add(rbtnPwdYes, gridBagConstraints);

        rbtnPwdNo.setText("No");
        rbtnPwdNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPwdNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOperations4.add(rbtnPwdNo, gridBagConstraints);

        rbtnPwdYes1.setText("Yes");
        rbtnPwdYes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPwdYes1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        panOperations4.add(rbtnPwdYes1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(panOperations4, gridBagConstraints);

        rdoFreeze.setText("Freeze");
        rdoFreeze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFreezeActionPerformed(evt);
            }
        });
        rdoFreeze.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoFreezeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(rdoFreeze, gridBagConstraints);

        rdoUnFreeze.setText("UnFreeze");
        rdoUnFreeze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoUnFreezeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountInfo.add(rdoUnFreeze, gridBagConstraints);

        lblFreezeDt.setText("Freeze Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblFreezeDt, gridBagConstraints);

        dtdFreezeDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdFreezeDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panAccountInfo.add(dtdFreezeDt, gridBagConstraints);

        lblfreezRemarks.setText("Freeze Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccountInfo.add(lblfreezRemarks, gridBagConstraints);

        txtFreezeRemarks.setMaxLength(10);
        txtFreezeRemarks.setMinimumSize(new java.awt.Dimension(200, 21));
        txtFreezeRemarks.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panAccountInfo.add(txtFreezeRemarks, gridBagConstraints);

        cboSuspDep.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Deposit", "Suspense", "Operative" }));
        cboSuspDep.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSuspDep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSuspDepActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        panAccountInfo.add(cboSuspDep, gridBagConstraints);

        chkNoTransaction.setText("No Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        panAccountInfo.add(chkNoTransaction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        panActInfo.add(panAccountInfo, gridBagConstraints);

        tabAccounts.addTab("Locker Account Details", panActInfo);

        panActInfo1.setLayout(new java.awt.GridBagLayout());

        panInstEntry.setMinimumSize(new java.awt.Dimension(600, 230));
        panInstEntry.setPreferredSize(new java.awt.Dimension(600, 230));
        panInstEntry.setLayout(new java.awt.GridBagLayout());

        panOperations.setLayout(new java.awt.GridBagLayout());

        btnPasNew.setText("New");
        btnPasNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnPasNew, gridBagConstraints);

        btnPastSave.setText("Save");
        btnPastSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPastSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnPastSave, gridBagConstraints);

        btnPasDelete.setText("Delete");
        btnPasDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnPasDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panInstEntry.add(panOperations, gridBagConstraints);

        panStdInstructions.setLayout(new java.awt.GridBagLayout());

        lblPassword.setText("New Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(lblPassword, gridBagConstraints);

        lblConPassword.setText("Confirm Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(lblConPassword, gridBagConstraints);

        lblPasCustId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(lblPasCustId, gridBagConstraints);

        txtPasCustId.setMaxLength(10);
        txtPasCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions.add(txtPasCustId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions.add(txtPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions.add(txtConPassword, gridBagConstraints);

        lblCurPassword.setText("Current Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(lblCurPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions.add(txtCurPassword, gridBagConstraints);

        panInstEntry.add(panStdInstructions, new java.awt.GridBagConstraints());

        srpInstructions.setMinimumSize(new java.awt.Dimension(250, 250));
        srpInstructions.setPreferredSize(new java.awt.Dimension(300, 100));

        tblInstruction2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "S.N", "Instructions", "gggg"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInstruction2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInstruction2MousePressed(evt);
            }
        });
        srpInstructions.setViewportView(tblInstruction2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        panInstEntry.add(srpInstructions, gridBagConstraints);

        jSeparator1.setPreferredSize(new java.awt.Dimension(400, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panInstEntry.add(jSeparator1, gridBagConstraints);

        panChargesServiceTax.setMaximumSize(new java.awt.Dimension(550, 50));
        panChargesServiceTax.setMinimumSize(new java.awt.Dimension(550, 50));
        panChargesServiceTax.setPreferredSize(new java.awt.Dimension(550, 50));
        panChargesServiceTax.setLayout(new java.awt.GridBagLayout());

        lblCharges.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panChargesServiceTax.add(lblCharges, gridBagConstraints);

        txtCharges.setAllowNumber(true);
        txtCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panChargesServiceTax.add(txtCharges, gridBagConstraints);

        lblSerTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 4);
        panChargesServiceTax.add(lblSerTax, gridBagConstraints);

        txtServiceTax.setAllowNumber(true);
        txtServiceTax.setMinimumSize(new java.awt.Dimension(100, 21));
        txtServiceTax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServiceTaxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panChargesServiceTax.add(txtServiceTax, gridBagConstraints);

        lblTotalAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 4);
        panChargesServiceTax.add(lblTotalAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panChargesServiceTax.add(lblTotAmtVal, gridBagConstraints);

        lblServiceTaxVal.setText("cLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panChargesServiceTax.add(lblServiceTaxVal, gridBagConstraints);

        cLabel5.setText("GST");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panChargesServiceTax.add(cLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panInstEntry.add(panChargesServiceTax, gridBagConstraints);

        chkEditPwd.setText("Change Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstEntry.add(chkEditPwd, gridBagConstraints);

        btnResetPwd.setText("Reset Password");
        btnResetPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPwdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInstEntry.add(btnResetPwd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(24, 0, 0, 0);
        panActInfo1.add(panInstEntry, gridBagConstraints);

        panTransaction.setMinimumSize(new java.awt.Dimension(700, 400));
        panTransaction.setPreferredSize(new java.awt.Dimension(700, 400));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panActInfo1.add(panTransaction, gridBagConstraints);

        tabAccounts.addTab("Password / Transaction Details", panActInfo1);

        panBillsCharges.setMinimumSize(new java.awt.Dimension(500, 300));
        panBillsCharges.setPreferredSize(new java.awt.Dimension(500, 300));
        panBillsCharges.setLayout(new java.awt.GridBagLayout());

        panDiscountRate1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panDiscountRate1, gridBagConstraints);

        panOverdueRateBills4.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills4, gridBagConstraints);

        panOverdueRateBills5.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills5, gridBagConstraints);

        panOverdueRateBills6.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills6, gridBagConstraints);

        panOverdueRateBills7.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills7, gridBagConstraints);

        panTransitPeriod1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBillsCharges.add(panTransitPeriod1, gridBagConstraints);

        panCharges1.setMinimumSize(new java.awt.Dimension(640, 450));
        panCharges1.setPreferredSize(new java.awt.Dimension(640, 300));
        panCharges1.setLayout(new java.awt.GridBagLayout());

        sprRemitProdCharges.setMinimumSize(new java.awt.Dimension(650, 300));
        sprRemitProdCharges.setPreferredSize(new java.awt.Dimension(700, 300));

        tblLockCharges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblLockCharges.setMinimumSize(new java.awt.Dimension(450, 300));
        tblLockCharges.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 300));
        tblLockCharges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblLockChargesMousePressed(evt);
            }
        });
        sprRemitProdCharges.setViewportView(tblLockCharges);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges1.add(sprRemitProdCharges, gridBagConstraints);

        panRemitProdCharges.setMinimumSize(new java.awt.Dimension(650, 300));
        panRemitProdCharges.setPreferredSize(new java.awt.Dimension(650, 350));
        panRemitProdCharges.setLayout(new java.awt.GridBagLayout());

        panCharges.setMinimumSize(new java.awt.Dimension(250, 250));
        panCharges.setPreferredSize(new java.awt.Dimension(250, 250));
        panCharges.setLayout(new java.awt.GridBagLayout());

        lblChargeType.setText("Charge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblChargeType, gridBagConstraints);

        lblAmt.setText("Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblAmt, gridBagConstraints);

        cboChargeType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboChargeType.setPopupWidth(180);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboChargeType, gridBagConstraints);

        txtAmt.setAllowNumber(true);
        txtAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtFromDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtToDt, gridBagConstraints);

        lblToDt.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblToDt, gridBagConstraints);

        lblFromDt.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblFromDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtServiceTax2, gridBagConstraints);

        lblServiceTax2.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblServiceTax2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdCharges.add(panCharges, gridBagConstraints);

        panRemitProdChargesButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdCharges.add(panRemitProdChargesButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCharges1.add(panRemitProdCharges, gridBagConstraints);

        panAliasBrchRemittNumber.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panCharges1.add(panAliasBrchRemittNumber, gridBagConstraints);

        panBillsCharges.add(panCharges1, new java.awt.GridBagConstraints());

        tabAccounts.addTab("Locker Charges", panBillsCharges);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccounts.add(tabAccounts, gridBagConstraints);

        getContentPane().add(panAccounts, java.awt.BorderLayout.CENTER);

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
        tbrAccounts.add(btnView);

        lblTBSep4.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep4.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep4.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrAccounts.add(lblTBSep4);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnAdd.setToolTipText("New");
        btnAdd.setName("btnAdd"); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnAdd);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setName("btnEdit"); // NOI18N
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnEdit);

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

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setName("btnCancel"); // NOI18N
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

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnException);

        lblTBSep3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrAccounts.add(lblTBSep3);

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnReport.setToolTipText("Print");
        btnReport.setName("btnReport"); // NOI18N
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnReport);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setName("btnClose"); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnClose);

        btnDeletedDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeletedDetails.setToolTipText("Closed Accounts Details");
        btnDeletedDetails.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDeletedDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletedDetailsActionPerformed(evt);
            }
        });
        tbrAccounts.add(btnDeletedDetails);

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

    private void btnCustomerIdFileOpenCrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnCustomerIdFileOpenCrFocusLost

        getDetails();// TODO add your handling code here:

    }//GEN-LAST:event_btnCustomerIdFileOpenCrFocusLost

    private void rbtnSiYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rbtnSiYesFocusLost

        getDetails();
    }//GEN-LAST:event_rbtnSiYesFocusLost

    public void getDetails() {
        if (rbtnSiYes.isSelected() == true) {
            //transactionUI.setCallingTransProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
            //  transactionUI.setCallingTransAcctNo(txtCustomerIdCr.getText());
            //transactionUI.setCallingTransType("transfer");
            // transactionUI.setCallingTransProdType("transfer");
//            transactionUI.setCallingProdID(CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
//
//            transactionUI.setCallingTransAcctNo(txtCustomerIdCr.getText());
//            transactionUI.setCallingAccNo(lblCustomerNameCrValue.getText());

            // Added for KD-3993 - By nithya
            if (((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString().equals("OA") && cboProdId.getSelectedIndex() > 0) {
                System.out.println("prod type :: " + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
                transactionUI.setCallingTransProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
                transactionUI.setCallingTransAcctNo(txtCustomerIdCr.getText());
                transactionUI.setCallingAccNo(lblCustomerNameCrValue.getText());
                transactionUI.setCallingProdID(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
                System.out.println("prod id key :: " + ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
            }


            // transactionUI.setCalling
        }
    }
    private void lblCustomerNameCrValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblCustomerNameCrValueFocusLost
        // TODO add your handling code here:
        getDetails();
    }//GEN-LAST:event_lblCustomerNameCrValueFocusLost

    private void txtCustomerIdCrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdCrFocusLost
        // TODO add your handling code here:
        getDetails();


        if (txtCustomerIdCr.getText().length() > 0) {
            txtAccNoActionPerformed();
        }
    }//GEN-LAST:event_txtCustomerIdCrFocusLost

    private void txtAccNoActionPerformed() {
        HashMap hash = new HashMap();
        String ACCOUNTNO = (String) txtCustomerIdCr.getText();
        //        observable.setProdType("");
        if (/*(!(observable.getProdType().length()>0)) && */ACCOUNTNO.length() > 0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProdId.setModel(observable.getCbmProdId());
                txtCustomerIdCr.setText(observable.getTxtCustomerIdCr());
                ACCOUNTNO = (String) txtCustomerIdCr.getText();
                cboProdIdActionPerformed(null);
//                txtCustomerIdCr.setText(ACCOUNTNO);
                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                observable.setAccountName(ACCOUNTNO, prodType);
                lblCustomerNameCrValue.setText(observable.getLblCustomerNameCrValue());
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtCustomerIdCr.setText("");
                return;
            }
        }
    }

    private void cboProdTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProdTypeFocusLost

        getDetails();
        // TODO add your handling code here:

    }//GEN-LAST:event_cboProdTypeFocusLost

    private void cboProdIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProdIdFocusLost
        // TODO add your handling code here:

        getDetails();
    }//GEN-LAST:event_cboProdIdFocusLost

    private void cboProductIdAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdAIActionPerformed
        // if(cboProductIdAI.)
    }//GEN-LAST:event_cboProductIdAIActionPerformed
    private void cboTransTypeActionPerformed(java.awt.event.ActionEvent evt) {
        // if(cboTransType.getSelectedItem())
        // cboTransType.setSelectedItem();
    }

    private void tabAccountsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabAccountsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tabAccountsFocusLost

    private void txtCollectRentyyyyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCollectRentyyyyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCollectRentyyyyActionPerformed

    private void btnCustomerIdFileOpenCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdFileOpenCrActionPerformed
        // TODO add your handling code here:
        callView("CREDIT_ACC_NO");// TODO add your handling code here:
        poaUI.ttNotifyObservers();
    }//GEN-LAST:event_btnCustomerIdFileOpenCrActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:

        if (cboProdType.getSelectedIndex() > 0) {        // TODO add your handling code here:
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();

            observable.setCbmProdId(prodType);
            //            if(prodType.equals("TD")){
            //                ClientUtil.displayAlert("Not allowing for crediting this Account No...");
            //                cboProdType.setSelectedItem("");
            //                cboProdId.setSelectedItem("");
            //            }
            if (prodType.equals("GL")) {
                cboProdId.setEnabled(false);
                txtCustomerIdCr.setText("");
                lblAccountNo.setText("Account Head Id");
                lblCustomerNameCrValue.setText("");
                btnCustomerIdFileOpenCr.setEnabled(true);
            } else if (prodType.equals("RM")) {
                lblAccountNo.setText("Favouring Name");
                btnCustomerIdFileOpenCr.setEnabled(false);
                txtCustomerIdCr.setText("");
                lblCustomerNameCrValue.setText("");
                //                cboProdId.setEnabled(false);
                txtCustomerIdCr.setEnabled(true);
            } else {
                cboProdId.setEnabled(true);
                lblAccountNo.setText("Account No");
                txtCustomerIdCr.setText("");
                lblCustomerNameCrValue.setText("");
                btnCustomerIdFileOpenCr.setEnabled(true);
                //                Commented by nikhil
                //                txtCustomerIdCr.setEnabled(false);
            }
            if (!prodType.equals("GL")) {
                cboProdId.setModel(observable.getCbmProdId());
            }
            //            }else
            //                cboProdId.setSelectedItem("PAY ORDR");

            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_RENEW)
                    || (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE)) {
                //cboInterestPaymentMode.setEnabled(false);
                cboProdType.setEnabled(false);
                cboProdId.setEnabled(false);
                txtCustomerIdCr.setEnabled(false);
                btnCustomerIdFileOpenCr.setEnabled(false);
                // cboInterestPaymentFrequency.setEnabled(false);
            }
        }
    }

    private void btnCustomerIdFileOpen1ActionPerformed(java.awt.event.ActionEvent evt) {
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
        btnSave.setEnabled(false);
        callView("CLOSED_DEPOSIT");
        System.out.println("getSelectedBranch" + getSelectedBranchID());
        poaUI.ttNotifyObservers();
        btnSave.setEnabled(false);// TODO add your handling code here:
        btnCancel.setEnabled(true);
        // btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        // btnPrint.setEnabled(false);
        //rdoDeathClaim_Yes.setEnabled(false);
        // rdoDeathClaim_No.setEnabled(false);

    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void rbtnPwdNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPwdNoActionPerformed
        // TODO add your handling code here:
        if (rbtnPwdNo.isSelected() == true) 
		// rbtnSiNo.setSelected(true);
        {
            rbtnPwdYes.setSelected(false);
            chkEditPwd.setEnabled(false);
            btnResetPwd.setEnabled(false);

        }


    }//GEN-LAST:event_rbtnPwdNoActionPerformed

    private void rbtnPwdYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPwdYesActionPerformed

        // TODO add your handling code here:
        if (rbtnPwdYes.isSelected() == true) 
		//rbtnPwdYes.setSelected(true);
        {
            rbtnPwdNo.setSelected(false);
            chkEditPwd.setEnabled(true);
            btnResetPwd.setEnabled(true);
        }



        // rbtnPwdNo.setSelected(false);
        // for(int i=0;i<  tblJointAccnt.rowCount();i++)
        //{
        // tblJointAccnt

    }//GEN-LAST:event_rbtnPwdYesActionPerformed

    private void txtCollectRentyyyyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCollectRentyyyyFocusLost
        // TODO add your handling code here:
        String date = dtdOpeningDateAI.getDateValue();
        //        String prodID=CommonUtil.convertObjToStr(cboProductIdAI.getSelectedItem());
        final String prodID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProductIdAI.getModel())).getKeyForSelected());
        // double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
        java.util.Date odate = DateUtil.getDateMMDDYYYY(date);

        int day = odate.getDate();
        int month = odate.getMonth() + 1;

        int year = odate.getYear() + 1900;

        int mm = CommonUtil.convertObjToInt(txtCollectRentMM.getText());
        int yyyy = CommonUtil.convertObjToInt(txtCollectRentyyyy.getText());

        HashMap stMap = new HashMap();
        stMap.put("PROD_ID", prodID);
        stMap.put("TODAY_DT", currDt);
        stMap.put("CHARGE_TYPE", "RENT_CHARGES");
        List stList = ClientUtil.executeQuery("getServiceTaxAndComissionForIssue", stMap);
        if (stList != null && stList.size() > 0) {
            stMap = null;
            stMap = (HashMap) stList.get(0);

            double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
            double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();

            if (yyyy <= year || (yyyy <= year + 1 && mm < month)) {
                displayAlert("it should be greaterthan one year");
                return;
            } // int MM=Integer.parseInt(mm);
            //int YYYY=Integer.parseInt(yyyy);
            // String opdate=DateUtil.getStringDate(DateUtil.addDays(odate,month));
            else {
                java.util.Date expdate = DateUtil.getDate(day, mm, yyyy);

                dtdExpiryDate.setDateValue(DateUtil.getStringDate(expdate));
                // int service =CommonUtil.convertObjToInt(txtServiceTax.getText());
                // double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
                // int charge=CommonUtil.convertObjToInt(txtCharges.getText());


                String ex = dtdExpiryDate.getDateValue();
                java.util.Date edate = DateUtil.getDateMMDDYYYY(ex);

                int eda = edate.getDate();
                int em = edate.getMonth() + 1;
                int ey = edate.getYear() + 1900;



                int diffm = em - month;
                int diffy = ey - year;
                int ry = diffy * 12;
                int ryy = ry + diffm;
                double rcom = comm / 12.0;
                double rcomm = rcom * ryy;
                rcomm = (double) getNearest((long) (rcomm * 100), 100) / 100;
                double ser = (rcomm / 100.0);
                double service = ser * serviceTax;
                service = (double) getNearest((long) (service * 100), 100) / 100;
                double charg = rcomm + service;
                txtServiceTax.setText(String.valueOf(service));
                txtCharges.setText(String.valueOf(rcomm));
                charg = (double) getNearest((long) (charg * 100), 100) / 100;

                lblTotAmtVal.setText(String.valueOf(charg));
                transactionUI.setCallingAmount(String.valueOf(charg));
                transactionUI.setCallingApplicantName(lblCustName.getText());

                // double rcom=(com/12)*ryy;
                //ser=(com/serviceTax)+serviceTax;




    }//GEN-LAST:event_txtCollectRentyyyyFocusLost
        }
    }

    private void rbtnSiNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSiNoActionPerformed
        // TODO add your handling code here:
        if (rbtnSiNo.isSelected() == true) {
            panOperations2.setVisible(false);

            rbtnSiYes.setSelected(false);
            // transactionUI.setCallingAccNo("");
            //  transactionUI.setCallingTransAcctNo("");
            // transactionUI.setCallingTransProdType("");
            // transactionUI.setCallingTransType("");
            // transactionUI.setCallingProdID("");
    }//GEN-LAST:event_rbtnSiNoActionPerformed
    }
    private void rbtnSiYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSiYesActionPerformed
        if (rbtnSiYes.isSelected() == true) {
            panOperations2.setVisible(true);
            rbtnSiYes.setSelected(true);
            cboProdType.setEnabled(true);
            cboProdId.setEnabled(true);
            txtCustomerIdCr.setEnabled(true);
            lblCustomerNameCrValue.setEnabled(true);
            rbtnSiNo.setSelected(false);
            //  cboProdType.setSelectedItem(observable.getCboProdType());
            // cboProdId.setSelectedItem(observable.getCboProdId());
            //txtCustomerIdCr.setText(observable.getCustomerIdCr());
            //  com.see.truetransact.ui.deposit.TermDepositOB ob=new com.see.truetransact.ui.deposit.TermDepositOB();

            // try{
            //cboProdType.setSelectedItem(ob.getCboProdType());
            //com.see.truetransact.ui.deposit.TermDepositOB ob= com.see.truetransact.ui.deposit.TermDepositOB.getInstance();
            //String prodtype= ob.getCboProdType();
            //cboProdType.setSelectedItem(prodtype);
            //cboProdId.setSelectedItem(ob.getCboProdId());
            // txtCustomerIdCr.setText(ob.getCustomerIdCr());
            //  lblCustomerNameCrValue.setText(ob.getCustomerNameCrValue());
            //  //dtdOpeningDateAI.getDateValue();





            // }catch(Exception e)
            // {

            //  }

            // TODO add your handling code here:
    }//GEN-LAST:event_rbtnSiYesActionPerformed

    }

    private void txtCollectRentMMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCollectRentMMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCollectRentMMActionPerformed
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    private void lblCollectRentMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCollectRentMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCollectRentMouseMoved

    private void txtChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargesFocusLost
        // TODO add your handling code here:
        calculateServiceTax();// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
        double gstVal = CommonUtil.convertObjToDouble(lblServiceTaxVal.getText()).doubleValue();
        String charges = txtCharges.getText();
        double chargeVal = CommonUtil.convertObjToDouble(charges).doubleValue();
        if (charges.length() > 0 && chargeVal > 0) {
            double serviceTax = chargeVal * serviceTaxPercentage / 100;
            serviceTax = (double) getNearest((long) (serviceTax * 100), 100) / 100;
            txtServiceTax.setText(String.valueOf(serviceTax));
            transactionUI.setCallingAmount(String.valueOf(chargeVal + serviceTax+gstVal));
            lblTotAmtVal.setText(String.valueOf(chargeVal + serviceTax + gstVal));
        } else {
            txtServiceTax.setText("0");
            transactionUI.setCallingAmount(String.valueOf(chargeVal + gstVal));
            lblTotAmtVal.setText(String.valueOf(chargeVal + gstVal));
        }

    }//GEN-LAST:event_txtChargesFocusLost

    private void txtCustomerIdAIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdAIFocusLost
        // TODO add your handling code here:
        if (txtCustomerIdAI.getText().length() > 0) {
            txtCustIDActionPerform();
        }
    }//GEN-LAST:event_txtCustomerIdAIFocusLost

    private void txtCustIDActionPerform() {
        String cust_id = CommonUtil.convertObjToStr(txtCustomerIdAI.getText());
        if (!cust_id.equals(observable.getTxtCustomerIdAI())) {
            txtPrevActNumAI.setText("");
        }
        List lst = null;
        HashMap executeMap = new HashMap();
        if (CommonUtil.convertObjToStr(cboConstitutionAI.getSelectedItem()).length() > 0 && cust_id.length() > 0) {
            //            if(tblAct_Joint.getRowCount()>0){
            //                for(int i=0;i<tblAct_Joint.getRowCount();i++){
            //                    custMap.put(tblAct_Joint.getValueAt(i,1),"");
            //                }
            //            }
            //            if(custMap.containsKey(cust_id))
            //            if (!resetPasswordDetails) {
            //                return;
            //            }
            executeMap.put("BRANCH_CODE", getSelectedBranchID());
            executeMap.put("CUST_ID", cust_id);
            viewType = "CUSTOMER ID";
            lst = ClientUtil.executeQuery("Locker.getCustData", executeMap);
            if (lst != null && lst.size() > 0) {
                executeMap = (HashMap) lst.get(0);
                viewType = "CUSTOMER ID";
                fillData(executeMap);
                lst = null;
                executeMap = null;
            } else {
                viewType = "";
                ClientUtil.displayAlert("Invalid Customer Number");
                txtCustomerIdAI.setText("");
            }
        } else if (CommonUtil.convertObjToStr(cboConstitutionAI.getSelectedItem()).length() == 0) {
            displayAlert("Select Constitution...");
        }
    }

    private void btnDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNoActionPerformed
        // TODO add your handling code here:

        if (txtCustomerIdAI.getText().length() > 0) {
            callView("DepositNo");
        } else {
            ClientUtil.displayAlert("Please select Customer ID");
        }


    }//GEN-LAST:event_btnDepositNoActionPerformed

    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // TODO add your handling code here:
        updateOBChrgsTab();
        observable.deleteSelectedRowChrgs(selectedRowChrg);
        clearChrgsTab();
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        // TODO add your handling code here:
        updateOBChrgsTab();
        addRowChrgs();
        clearChrgsTab();
    }//GEN-LAST:event_btnTabSaveActionPerformed

    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // TODO add your handling code here:
        clearChrgsTab();
    }//GEN-LAST:event_btnTabNewActionPerformed

    private void tblLockChargesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLockChargesMousePressed
        // TODO add your handling code here:
        selectedRowChrg = -1;
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE) {
            selectedDataChrg = 1;
            selectedRowChrg = tblLockCharges.getSelectedRow();
            //            ClientUtil.clearAll(panStdInstructions);
            updateOBChrgsTab();
            observable.populateSelectedRowLockCharges(tblLockCharges.getSelectedRow());
            updateChrgsTab();
            //            setPanOperationsEnable(true);
            //            ClientUtil.enableDisable(panStdInstructions,true);
            //            cboStdInstruction.setEnabled(false);
        }
    }//GEN-LAST:event_tblLockChargesMousePressed

    private void btnPasNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPasNewActionPerformed

    private void btnPasDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasDeleteActionPerformed
        // TODO add your handling code here:
        updateOBPassTab();
        observable.deleteSelectedRow(selectedRow);
        clearPassTab();
        //        btnInstDelete.setEnabled(false);
        //        btnInstSave.setEnabled(false);
    }//GEN-LAST:event_btnPasDeleteActionPerformed

    private void btnPasDeleteActionPerformed(String custID) {
        updateOBPassTab();
        observable.deleteSelectedCustIDPwd(custID);
        clearPassTab();
    }

    private void btnPastSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPastSaveActionPerformed
        // TODO add your handling code here:
        System.out.println("in save action pwd" + observable.getTxtPassword());


        //        String mandatoryMessage = new String();
        //        char[] pwd1=txtPassword.getPassword();
        //        char[] pwd2=txtConPassword.getPassword();
        //        if(pwd1.length==pwd2.length){
        //            int i;
        //            for(i=0;i<pwd1.length;i++){
        //                if(pwd1[i]!=pwd2[i])
        //                    break;
        //            }
        //            if(i==pwd1.length)
        //                return;
        //        }

        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                if (!txtCurPassword.getText().equals(observable.getTxtPassword()) && !resetPwdTrue)
                {                  
                    ClientUtil.showAlertWindow("Current password is wrong");
                    return;
                 }
                else if (txtPassword.getText().equals("")) {
                    ClientUtil.showAlertWindow("New Password is mandatory");
                    return;

                } else if (txtConPassword.getText().equals("")) {
                    ClientUtil.showAlertWindow("Confirm Password is mandatory");                   
                    return;
                } else if (!txtPassword.getText().equals(txtConPassword.getText())) {
                    ClientUtil.showAlertWindow("New password and confirm password should be same"); 
                    this.txtConPassword.setText("");
                    this.txtConPassword.requestFocus();
                    return;
                }else if(!chkEditPwd.isSelected() && !resetPwdTrue){
	                ClientUtil.showAlertWindow("Please check change password"); 
	                return;
                }    
                updateOBPassTab();
                addRow();
                clearPassTab();
                resetPwdTrue = false; 
                return;
           }
                   
        

        if (!txtPassword.getText().equals(txtConPassword.getText())) {
            javax.swing.JOptionPane.showMessageDialog(this, "Both passwords should be same");
            ClientUtil.showAlertWindow("Both password should be same");
            this.txtConPassword.setText("");
            this.txtConPassword.requestFocus();
            return;
        }
        //        if(cboStdInstruction.getSelectedItem().equals("")){
        //            mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("cboStdInstruction");
        //        }
        //        if(observable.getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        //            if(txtStdInstruction.getText().equals("")){
        //                mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("txtStdInstruction");
        //            }
        //        }
        /*
         * mandatoryMessage length will be greater than 0 if the mandatory
         * conditions are not satisfied and so the alert should be displayed
         */
        //        if (mandatoryMessage.length() > 0){
        //            displayAlert(mandatoryMessage);
        //
        //        }else{
        updateOBPassTab();
        addRow();
        clearPassTab();

        //            txtTotalAmt.setText(observable.getTxtTotalAmt());
        //        }
    }//GEN-LAST:event_btnPastSaveActionPerformed
    private void addRow() {
        //        if(selectedData==1){
        observable.setTableValueAt(selectedRow);
        //            observable.resetInstructions();
        //        } else{
        //            /** when clicked on the new button related to tblInstruction **/
        //            result=-1;
        //            result = observable.addTblInstructionData();
        //            if(result==0){
        //                ClientUtil.enableDisable(panStdInstructions ,true, false, true);
        ////                btnInstSave.setEnabled(true);
        ////                btnInstDelete.setEnabled(false);
        //            }
        //            if(result==1){
        ////                observable.resetInstructions();
        //                ClientUtil.enableDisable(panStdInstructions,false, false, true);
        ////                btnInstSave.setEnabled(false);
        ////                btnInstDelete.setEnabled(false);
        //            }
        //            if (result == 2){
        //                /** The action taken for the Cancel option **/
        //                ClientUtil.enableDisable(panStdInstructions,true, false, true);
        ////                    btnInstSave.setEnabled(true);
        ////                    btnInstDelete.setEnabled(false);
        //            }
        //            if(result==-1){
        ////                observable.resetInstructions();
        //                ClientUtil.enableDisable(panStdInstructions,false, false, true);
        ////                    btnInstNew.setEnabled(true);
        //            }
        //
        //        }
        ClientUtil.enableDisable(panStdInstructions, false, false, true);
        ClientUtil.enableDisable(panOperations, false, false, true);
    }

    private void addRowChrgs() {
        if (selectedDataChrg == 1) {
            observable.setTableValueLockCharges(selectedRowChrg);
            //            observable.resetInstructions();
        } else {
            /** when clicked on the new button related to tblInstruction **/
            resultChrg = -1;
            resultChrg = observable.addTblLockChargesData();
        }
    }
    private void tblInstruction2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstruction2MousePressed
        // TODO add your handling code here:
        selectedRow = -1;
        //        if(observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE){
        selectedData = 1;
        selectedRow = tblInstruction2.getSelectedRow();
        //            ClientUtil.clearAll(panStdInstructions);
        System.out.println("selected row here" + selectedRow);
        updateOBPassTab();
        observable.populateSelectedRowAct(tblInstruction2.getSelectedRow());
        updatePassTab();
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE
                && observable.getOperation() != ClientConstants.ACTIONTYPE_REJECT
                && observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE) {
            ClientUtil.enableDisable(panStdInstructions, true, false, true);
            ClientUtil.enableDisable(panOperations, true, false, true);
        } else {
            ClientUtil.enableDisable(panInstEntry, false, false, true);
        }
        //            setPanOperationsEnable(true);
        //            ClientUtil.enableDisable(panStdInstructions,true);
        //            cboStdInstruction.setEnabled(false);
        //        }
        //        else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
        //            setPanOperationsEnable(false);
        //        }
        //        if(viewType.equals(AUTHORIZE)){
        //            setPanOperationsEnable(false);
        //            ClientUtil.enableDisable(panStdInstructions,false);
        //            btnBranchCode.setEnabled(false);
        //        }
    }//GEN-LAST:event_tblInstruction2MousePressed

    private void btnLockerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerNoActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboProductIdAI.getSelectedItem()).length() > 0) {
            callView("LockerNo");
        } else {
            ClientUtil.displayAlert("Please select a Locker Product...");
        }
    }//GEN-LAST:event_btnLockerNoActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquirystatus");
        btnCheck();
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

    private void cboDMYADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDMYADActionPerformed
        // TODO add your handling code here:
        //txtReqFlexiPeriodAD
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
        } else {
            lblCustValue.setText("");
            lblDOBValue.setText("");
            lblStreetValue.setText("");
            lblAreaValue.setText("");
            lblCityValue.setText("");
            lblStateValue.setText("");
            lblCountryValue.setText("");
            lblPinValue.setText("");
        }
    }//GEN-LAST:event_cboCommAddrActionPerformed

    private void cboProductIdAIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductIdAIItemStateChanged
        // TODO add your handling code here:
        final String ProductID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProductIdAI.getModel())).getKeyForSelected());

        if (ProductID.length() > 0) {
            //            acountParamMap = observable.getAccountParamData(ProductID);
            //            acountCardsMap = observable.getAccountCardsData(ProductID);
            //            acountChargesMap = observable.getAccountChargesData(ProductID);
            //            acountCreditMap = observable.getAccountCreditData(ProductID);

            minNominee = CommonUtil.convertObjToStr(acountParamMap.get("NO_OF_NOMINEE"));
            //            nomineeUi.setMinNominee(CommonUtil.convertObjToStr(acountParamMap.get("NO_OF_NOMINEE")));
            nomineeUi.setMaxNominee(1);


            if ((observable.getOperation() == ClientConstants.ACTIONTYPE_NEW)
                    || (observable.getOperation() == ClientConstants.ACTIONTYPE_NEWTI)) {
                /*
                 * Fetching the Data from acountParamMap...
                 */

                if (acountParamMap.containsKey("STAFF_ACCT_OPENED")) {
                    String staffYN = CommonUtil.convertObjToStr(acountParamMap.get("STAFF_ACCT_OPENED"));
                    if (staffYN.equalsIgnoreCase("Y")) {
                        observable.setStaffOnly(staffYN);
                    } else {
                        observable.setStaffOnly("");
                    }
                }
            }
            // this.txtODLimitAI.setEnabled(true);
            //            if(observable.getOperation() != ClientConstants.ACTIONTYPE_EDIT) {
            //                if(tblLockCharges.getRowCount() <= 0) {
            //                    setLockerCharges(ProductID);
            //                    double rentAmt = observable.getTotCharges();
            //                    double servTx = observable.getServiceTax();
            //                    servTx = (double)getNearest((long)(servTx *100),100)/100;
            //                    transactionUI.setCallingAmount(String.valueOf(rentAmt+servTx));
            //                    System.out.println("#####rentAmt+servTx"+(rentAmt+servTx));
            //                }
            //            }
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
                txtLockerNo.setText("");
                setLockerCharges(ProductID);
            }
        }

        /* we have to update the product interest values also */
//        if (!lblActHeadValueAI.getText().equals("")) {
//            updateProductInterestRates();
//        }
    }//GEN-LAST:event_cboProductIdAIItemStateChanged

    private void tblAct_JointMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAct_JointMousePressed
        //        if(!(viewType.equals("Delete") || !viewType.equals("AUTHORIZE")|| cboConstitutionAI.getSelectedItem().equals("Individual"))){ //--- If it is not in Delete mode, select the row
        if (!(cboConstitutionAI.getSelectedItem().equals("Individual"))) { //--- If it is not in Delete mode, select the row
            tblAct_JointRowSelected(tblAct_Joint.getSelectedRow());
        }
        // Add your handling code here:
    }//GEN-LAST:event_tblAct_JointMousePressed
    private void tblAct_JointRowSelected(int rowSelected) {
        //        if(tblAct_Joint.getSelectedRow()> (-1) ){
        if (!(viewType.equals("Delete") || viewType.equals("AUTHORIZE")) && tblAct_Joint.getSelectedRow() != 0) {
            setBtnJointAccnt(true);
        } else {
            setBtnJointAccnt(false);
            btnCustDetNew.setEnabled(true);
        }
        //        }

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
        observable.moveLockerPwdToMain(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1)), tblAct_Joint.getSelectedRow());
        observable.moveToMain(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1)), tblAct_Joint.getSelectedRow());
        observable.notifyObservers();
        // Add your handling code here:
    }//GEN-LAST:event_btnToMainActionPerformed

    private void btnCustDetDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustDetDeleteActionPerformed
        updateOBFields();
        if (poaUI.checkCustIDExistInJointAcctAndPoA(CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1)))) {
            selectedRow = tblAct_Joint.getSelectedRow();

            //__ Changes in AuthSignatory...
            String strCustIDToDel = CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1));
            //            observableBorrow.deleteJointAccntHolder(strCustIDToDel, tblBorrowerTabCTable.getSelectedRow());

            //__ Changes in AuthSignatory...
            observable.delJointAccntHolder(strCustIDToDel, tblAct_Joint.getSelectedRow(), poaUI.getPowerOfAttorneyOB());
            authSignUI.removeAcctLevelCustomer(strCustIDToDel);

            setBtnJointAccnt(false);
            btnCustDetNew.setEnabled(true);
            observable.resetCustDetails();

            btnPasDeleteActionPerformed(strCustIDToDel);

            observable.notifyObservers();
        }
    }//GEN-LAST:event_btnCustDetDeleteActionPerformed

    private void btnCustDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustDetNewActionPerformed
        if (tblAct_Joint.getRowCount() != 0) { //--- If the Main Accnt Holder is selected,
            callView("JointAccount");               //--- allow the user to add Jnt Acct Holder

        } else {  //--- else if the Main Acct Holder is not selected, prompt the user to select
            observable.checkMainAcctHolder("selectMainAccntHolder"); //--- the Main Acct. holder
            btnCustomerIdAI.requestFocus(true);
        }


        //cboProdType.setEnabled(false);

        // cboProdType.setVisible(false);
        // panOperations2.setVisible(false);
        // rbtnSiYes.setEnabled(false);
        //        txtPasCustId.setEnabled(false);
        //        txtPassword.setEnabled(false);
        //        txtConPassword.setEnabled(false);
        //        btnPasNew.setEnabled(false);
        //        btnPastSave.setEnabled(false);
        //        btnPasDelete.setEnabled(false);
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
        //        cboConstitutionAI.setModel(observable.getCbmConstitutionAI());
        String constitution = CommonUtil.convertObjToStr(cboConstitutionAI.getSelectedItem());
        //        if(!constitution.equals("")){
        //            if (((constitution.equals("Individual") || constitution.equals("Joint")) && observable.getCboConstitutionAI().equals("Corporate")) ||
        //                (constitution.equals("Corporate") && (constitution.equals("Individual") || constitution.equals("Joint")))) {
        //                txtCustomerIdAI.setText("");
        //                checkJointAccntHolderForData();
        //            }
        //        }
        if (!constitution.equals("") && !observable.getCboConstitutionAI().equals("")) {
            if (!constitution.equals(observable.getCboConstitutionAI())) {
                resetPasswordDetails = true;
            }
        }
        observable.setCboConstitutionAI(constitution);
        if ((constitution != null) && (!constitution.equals(""))) {
            //--- If Selected data is "Joint Account", enable the New Button
            if (constitution.equals("Joint")) {
                btnCustDetNew.setEnabled(true);
            } else {
                checkJointAccntHolderForData();
            }
            if (!constitution.equals("Corporate")) {
                // cboGroupCodeAI.setEnabled(false);
                //cboGroupCodeAI.setSelectedItem("None");
                tabAccounts.add(nomineeUi, "Nominee", 2);
                tabAccounts.remove(poaUI);
                tabAccounts.remove(authSignUI);
            } else {
                // cboGroupCodeAI.setEnabled(true);
                //cboGroupCodeAI.setSelectedItem("");
                tabAccounts.remove(nomineeUi);
                tabAccounts.add(poaUI, "Power Of Attorney", 2);
                poaUI.setPoANewOnlyEnable();

                tabAccounts.add(authSignUI, "Authorized Signatory", 3);
                authSignUI.setAuthEnableDisable(false);
                authSignUI.setAllAuthInstEnableDisable(false);

            }
            //            if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            //                txtCustomerIdAI.setText("");
            //            }
            tabAccounts.resetVisits();
            txtCustIDActionPerform();
            //--- Else if no data is seleted, disable the Buttons
        } else if (constitution.equals("")) {
            checkJointAccntHolderForData();
        }
        //        if(constitution.equals("Corporate")){
        //            tabAccounts.remove(nomineeUi);
        //        }
        // Add your handling code here:
    }//GEN-LAST:event_cboConstitutionAIActionPerformed
    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);

        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EXCEPTION]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EXCEPTION]);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        nomineeUi.disableNewButton(false);
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);

        setBtnJointAccnt(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);

        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_REJECT]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_REJECT]);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        nomineeUi.disableNewButton(false);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);

        setBtnJointAccnt(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);

        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_AUTHORIZE]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_AUTHORIZE]);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        nomineeUi.disableNewButton(false);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        setBtnJointAccnt(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authorizeActionPerformed(String authStatus) {
        btnCustDetNew.setEnabled(false);
        if (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL])) {
            viewType = "";
        }
        //        //__ To Save the data in the Internal Frame...
        //        setModified(true);
        //
        if (!(viewType.equals("AUTHORIZE"))) {

            viewType = "AUTHORIZE";
            HashMap whereMap = new HashMap();
            HashMap mapParam = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            //            whereMap.put(CommonConstants.AUTHORIZESTATUS, authStatus);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, ClientConstants.ACTION_STATUS[observable.getOperation()]);
            whereMap.put("TRANS_DT",currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
              mapParam.put(CommonConstants.MAP_NAME, "getSelectLockerMasterCashierAuthorizeTOList");  
            }else{
            mapParam.put(CommonConstants.MAP_NAME, "getSelectLockerMasterAuthorizeTOList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLockerMaster");

            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);

            //__ If there's no data to be Authorized, call Cancel action...
            if (!isModified()) {
                btnCancelActionPerformed(null);
                return;
            }
            enableDisableAuthorizeBtns();
            //            panInstEntry.setEnabled(false);
            //            btnPasNew.setEnabled(false);
            //            btnPasDelete.setEnabled(false);
            //            btnPastSave.setEnabled(false);
        } else if (viewType.equals("AUTHORIZE")) {
            String warningMessage = tabAccounts.isAllTabsVisited();
            if (warningMessage.length() > 0) {
                displayAlert(warningMessage);

            } else {
                //__ To reset the value of the visited tabs...
                //                if(isPasTabEntered == false){
                //                    ClientUtil.showMessageWindow("Enter the Password Details");
                //////                    panInstEntry.setEnabled(true);
                //////                    panStdInstructions.setEnabled(true);
                //                    ClientUtil.enableDisable(panInstEntry, true);
                ////                    btnPasNew.setEnabled(true);
                ////                    btnPasDelete.setEnabled(true);
                ////                    btnPastSave.setEnabled(true);
                //
                //                }else{
                tabAccounts.resetVisits();

                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("ACCOUNTNO", lblAccountNo.getText());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);

                //                    boolean b = ClientUtil.executeWithResult("authorizeLockerMaster", singleAuthorizeMap);
                ////                    HashMap paramMap = new HashMap();
                ////                    paramMap.put("LOCKER_STATUS", "NOT_AVAILABLE");
                ////                    paramMap.put("LOC_NO", txtLockerNo.getText());
                ////                    ClientUtil.executeQuery("updateLockerStatus", paramMap);
                ArrayList arrList = new ArrayList();
                HashMap authDataMap = new HashMap();
                authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                authDataMap.put("LOCKER_NUM", txtLockerNo.getText());
                authDataMap.put("ISSUE_ID", getIssueId());
                //                    authDataMap.put("CUST_ID", lblCustomerIdVal.getText());
                //                    authDataMap.put("PROD_ID", CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdID).getModel())).getKeyForSelected()));
                arrList.add(authDataMap);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authStatus);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                singleAuthorizeMap.put("EXP_DT", observable.getDtdExpiryDate());

                authorize(singleAuthorizeMap);
                //                    authorize(singleAuthorizeMap);
                //                    paramMap = null;
                //                    super.setOpenForEditBy(observable.getStatusBy());
                //                    super.removeEditLock(lblAccountNo.getText());
                //                    btnCancelActionPerformed(null);
                //                    super.setOpenForEditBy(observable.getStatusBy());
                //                    super.removeEditLock(lblAccountNo.getText());
                //                    if (b)  lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getOperation()]);
                //                    viewType = "";
                //                }
            }
        }
    }

    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);

        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction("Authorize");
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            //        super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtRemarks.getText());
            //        observable.resetForm();
             if(fromNewAuthorizeUI){
                this.dispose();
                newauthorizeListUI.removeSelectedRow();
                newauthorizeListUI.setFocusToTable();
                fromNewAuthorizeUI = false;
                newauthorizeListUI.displayDetails("Locker Issue");
            }  
            if(fromAuthorizeUI){
                this.dispose();
                authorizeListUI.removeSelectedRow();
                authorizeListUI.setFocusToTable();
                fromAuthorizeUI = false;
                authorizeListUI.displayDetails("Locker Issue");
            }
            btnCancelActionPerformed(null);
            //        observable.setResultStatus();
        }
        //      lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);
    }

    private void enableDisableAuthorizeBtns() {
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);

    }
    private void btnCustomerIdAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdAIActionPerformed
        // Add your handling code here:
        /*
         * this will show all the Customer's details and the user can select one
         * customer by double=clicking on the particular row in the display
         * table. this will update the Customer Id value automatically
         */

        // if((cboProductId.getSelectedItem()!=null)&&(cboConstitutionAI.getSelectedItem()!=null)
        //&& (!cboProductId.getSelectedItem().equals("") && !cboConstitutionAI.getSelectedItem().equals(""))){
        //            callView("CUSTOMER ID");

        if ((cboProductIdAI.getSelectedItem() != null) && (cboConstitutionAI.getSelectedItem() != null)
                && (!cboProductIdAI.getSelectedItem().equals("") && !cboConstitutionAI.getSelectedItem().equals(""))) {
            viewType = "CUSTOMER ID";
            new CheckCustomerIdUI(this);
            System.out.println("getSelectedBranch" + getSelectedBranchID());
            poaUI.ttNotifyObservers();
        }
        if (cboConstitutionAI.getSelectedItem().equals("")) {
            ClientUtil.showAlertWindow("Choose Constitution...");
            return;
        }
        // callView("CUST_ID");
    }//GEN-LAST:event_btnCustomerIdAIActionPerformed

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
        mitCancelActionPerformed(evt);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnDeletedDetails.setEnabled(true);
        txtClosedDt.setVisible(false);
        lblClosedDt.setVisible(false);
        observable.resetPassTable();
        txtLockerNo.setText("");
        lblLockerKeyNoVal.setText("");
        clearPassTab();
        clearChrgsTab();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setMainEnableDisable(false);
        txtCharges.setText("");
        txtServiceTax.setText("");
        lblTotAmtVal.setText("");
        tabAccounts.setSelectedIndex(0);
        btnDepositNo.setEnabled(false);
        btnLockerNo.setEnabled(false);
        ClientUtil.enableDisable(panOperations, false, false, true);
        ClientUtil.clearAll(this);
        lblCustomerNameCrValue.setText("");
        lblfreezRemarks.setVisible(false);
        txtFreezeRemarks.setVisible(false);
        lblFreezeDt.setVisible(false);
        dtdFreezeDt.setVisible(false);
        cboSuspDep.setSelectedIndex(-1);
         if (fromNewAuthorizeUI) {
            this.dispose();
            newauthorizeListUI.setFocusToTable();
            fromNewAuthorizeUI = false;
        } 
        if (fromAuthorizeUI) {
            this.dispose();
            authorizeListUI.setFocusToTable();
            fromAuthorizeUI = false;
        } 
        lblServiceTaxVal.setText("");
        //        tblInstruction2.setModel(observable.getTbmInstructions2());
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        HashMap hashmap = new HashMap();
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
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
        mitSaveActionPerformed(evt);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        mitEditNewActionPerformed(evt);

        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtCollectRentMM.setEnabled(false);
        txtCollectRentyyyy.setEnabled(false);
        btnDepositNo.setEnabled(true);
        lblCurPassword.setVisible(true);
        txtCurPassword.setVisible(true);
        chkEditPwd.setVisible(true);
        btnResetPwd.setVisible(true);

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        mitDeleteActionPerformed(evt);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
        panOperations2.setVisible(false);
        mitAddNewActionPerformed(evt);
        btnLockerNo.setEnabled(true);
        btnDepositNo.setEnabled(true);
        ClientUtil.enableDisable(panOperations, true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnDeletedDetails.setEnabled(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        tabAccounts.setSelectedIndex(0);
        cboProdType.setEnabled(false);
        cboProdId.setEnabled(false);
        //cboProdType.setEnabled(false);
        txtCustomerIdCr.setEnabled(false);
        lblCustomerNameCrValue.setEnabled(false);
        lblCurPassword.setVisible(false);
        txtCurPassword.setVisible(false);
        chkEditPwd.setVisible(false);
        btnResetPwd.setVisible(false);
        
        // cboProdType.setVisible(false);
        // panOperations2.setVisible(false);
        // rbtnSiYes.setEnabled(false);
        //        txtPasCustId.setEnabled(false);
        //        txtPassword.setEnabled(false);
        //        txtConPassword.setEnabled(false);
        //        btnPasNew.setEnabled(false);
        //        btnPastSave.setEnabled(false);
        //        btnPasDelete.setEnabled(false);
    }//GEN-LAST:event_btnAddActionPerformed

    private void mitResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitResumeActionPerformed
        // Add your handling code here:
        // Just take the user to the firt editable field of the first tab
    }//GEN-LAST:event_mitResumeActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        //__ To reset the value of the visited tabs...
        dontShowJointDialog = true;

        super.removeEditLock(txtRemarks.getText());

        // Add your handling code here:
        // clear everything
        observable.resetCustDetails();
        observable.resetJntAccntHoldTbl();
        observable.resetChrgsTable();
        observable.resetOBFields();
        observable.resetLabels();
        // Disable all the screen
        ClientUtil.enableDisable(this, false, false, true);

        /*
         * To reset the POA, Introducer and Nominee Table
         */
        (poaUI.getPowerOfAttorneyOB()).resetAllFieldsInPoA();
        poaUI.setPoAToolBtnsEnableDisable(false);
        poaUI.setAllPoAEnableDisable(false);

        //__ To get back the Nominee Screen in case Corporate Cust was selected...
        //        tabAccounts.add(nomineeUi,"Nominee", 3);  // This line commented and added the following 3 lines by Rajesh
        tabAccounts.remove(nomineeUi);
        tabAccounts.remove(authSignUI);
        tabAccounts.remove(poaUI);

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

        observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
        setupMenuToolBarPanel();
        lblCustName.setText("");
        dontShowJointDialog = false;

        viewType = "";
        tabAccounts.resetVisits();
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
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        dontShowJointDialog = true;
        try {
            if (rdoFreeze.isSelected() == true || rdoUnFreeze.isSelected() == true) {
                if (txtFreezeRemarks.getText().length() <= 0) {
                    ClientUtil.displayAlert("Enter " + lblfreezRemarks.getText());
                    return;
                }
                if (dtdFreezeDt.getDateValue().length() <= 0) {
                    ClientUtil.displayAlert("Enter " + lblFreezeDt.getText());
                    return;
                }
            }
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panActInfo);
            StringBuffer strBAlert = new StringBuffer();
            if (mandatoryMessage.length() > 0) {
                //                displayAlert(mandatoryMessage);
                strBAlert.append(mandatoryMessage + "\n");
            }
            if (txtLockerNo.getText().equalsIgnoreCase("")) {
                strBAlert.append(resourceBundle.getString("LOCNOWARNING") + "\n");
            }
            // if((rbtnSiYes.isSelected()==false && rbtnSiNo.isSelected()==false)|| (rbtnPwdYes.isSelected()==false && rbtnPwdNo.isSelected()==false)){
            //  displayAlert("please select SI and PasswordRequiered");
            // }
            if (txtCustomerIdAI.getText().equalsIgnoreCase("")) {
                strBAlert.append(resourceBundle.getString("CUSTIDWARNING") + "\n");
            }

            // if(txtPrevActNumAI.getText().equalsIgnoreCase("")){
            //  strBAlert.append(resourceBundle.getString("DEPOSITNOWARNING")+"\n");
            // }
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

            if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE) {
                //__ To Check if the Total Share of the Nominee(s) is 100% or not...

                String alert = nomineeUi.validateData();
                if (!alert.equalsIgnoreCase("")) {
                    strBAlert.append(alert);
                }
            }
            //            final String NOMINEE = CommonUtil.convertObjToStr(acountParamMap.get("NO_OF_NOMINEE"));

            // To check the transaction amounts tallied...
            java.util.LinkedHashMap transMap = transactionUI.getOutputTO();
            if (observable.getAuthorizeDt() == null) {
                if (transMap != null && transMap.size() > 0) {
                    Object[] objKeys = transMap.keySet().toArray();
                    TransactionTO objTransactionTO = null;
                    double transAmt = 0;
                    for (int i = 0; i < objKeys.length; i++) {
                        objTransactionTO = (TransactionTO) transMap.get(objKeys[i]);
                        transAmt = transAmt + objTransactionTO.getTransAmt().doubleValue();
                    }
                    System.out.println("#$#$ TotalAmount : " + lblTotAmtVal.getText() + " / TransAmt : " + transAmt);
                    if (CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue() != transAmt) {
                        strBAlert.append(resourceBundle.getString("TRANSAMTWARNING") + "\n");
                    }
                } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && !chkNoTransaction.isSelected()) {
                    //if (DateUtil.dateDiff(currDt, DateUtil.getDateMMDDYYYY(observable.getDtdActOpenDateAI()))==0){
                    strBAlert.append(resourceBundle.getString("NOTRANSWARNING") + "\n");
                }
            }

            //__ To Display the Alerts
            if (observable.getOperation() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0) {
                displayAlert(strBAlert.toString());
            } else {
                if (rbtnPwdYes.isSelected() == false && rbtnPwdNo.isSelected() == false) {
                    ClientUtil.showAlertWindow("Please select password required Yes or No");
                    return;
                }
                if (rbtnSiYes.isSelected() == false && rbtnSiNo.isSelected() == false) {
                    ClientUtil.showAlertWindow("Please select SI required Yes or No");
                    return;
                }
                if (rbtnSiYes.isSelected() == true) {
                    String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()).toString();
                    if (prodType.equals("")) {
                        ClientUtil.showAlertWindow("SI Product type should not be empty");
                        return;
                    }
                    if (!prodType.equals("GL")) {
                        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()).toString();
                        if (prodId.equals("")) {
                            ClientUtil.showAlertWindow("SI Product id should not be empty");
                            return;
                        }
                        if (CommonUtil.convertObjToStr(txtCustomerIdCr.getText()).equals("")) {
                            ClientUtil.showAlertWindow("SI Account Number should not be empty");
                            return;
                        }
                    } else if (prodType.equals("GL") && CommonUtil.convertObjToStr(txtCustomerIdCr.getText()).equals("")) {
                        ClientUtil.showAlertWindow("SI Account head id should not be empty");
                        return;
                    }
                }
                if(CommonUtil.convertObjToStr(txtPrevActNumAI.getText()).length() == 0){
                    ClientUtil.showAlertWindow("Please select deposit no as security");
                    return;
                }
                if ((observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) && rbtnPwdYes.isSelected() == true && !chkEditPwd.isSelected()) {
                   observable.populateSelectedRowAct(0);
                  observable.setTableValueAt(0);
                }

                java.util.Map errorMap = observable.checkPwdTable();
                if (errorMap != null && errorMap.size() > 0) {
                    System.out.println("error list here map");
                    Object[] errKey = errorMap.keySet().toArray();
                    StringBuffer errorMsg = new StringBuffer();
                    if (rbtnPwdYes.isSelected() == true) {
                        errorMsg.append("<html>Password not entered for the following Customers<br><br>");
                        for (int i = 0; i < errKey.length; i++) {
                            //   errorMsg.append(errKey[i]).append(", ").append(errorMap.get(errKey[i])).append("<br>");
                        }
                        errorMsg.append("</html>");
                        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                            System.out.println("inside edit save");
                            if (chkEditPwd.isSelected()) {
                                errorMsg.append("<html>Password not entered for the following Customers<br><br>");
                                  errorMsg.append("</html>");
                            } 
                        } else {
                            errorMsg.append("<html>Password not entered for the following Customers<br><br>");
                            for (int i = 0; i < errKey.length; i++) {
                                //   errorMsg.append(errKey[i]).append(", ").append(errorMap.get(errKey[i])).append("<br>");
                            }
                              errorMsg.append("</html>");
                        }
                        if (null != errorMsg) {
                            ClientUtil.showAlertWindow(errorMsg.toString());
                            return;
                        }

                    }
                }
                updateOBFields();
                poaUI.updateOBFields();
                int transactionSize = 0;
                if (observable.getAuthorizeDt() == null) {
                    System.out.println("getAuthorizeDt()" + observable.getAuthorizeDt());
                    if (transactionUI.getOutputTO() == null) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    } else {
                        transactionSize = transMap.size();
                        observable.setAllowedTransactionDetailsTO(transMap);
                    }
                }

                //observable.setAllowedTransactionDetailsTO(transMap);
                if ((observable.getSiYes() == false && observable.getSiNo() == false) || (observable.getPwdYes() == false && observable.getPwdNo() == false)) {
                    ClientUtil.showMessageWindow("please select SI and PasswordRequired");
                    return;
                }
                observable.doAction(nomineeUi.getNomineeOB(), poaUI.getPowerOfAttorneyOB(), authSignUI.getAuthorizedSignatoryOB(), authSignUI.getAuthorizedSignatoryInstructionOB());
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    HashMap lockMap = new HashMap();
                    ArrayList lst = new ArrayList();
                    //  if (observable.getAuthorizeDt()==null)
                    //    lst.add("ACCOUNTNO");
                    //  else
                    lst.add("ISSUEID");
                    // lst.add("collectrentupto");

                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap() != null) {
                        System.out.println("MAP@@@" + observable.getProxyReturnMap());
                        if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                            lockMap.put("ISSUEID", observable.getProxyReturnMap().get("TRANS_ID"));
                        }
                    }
                    if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                        // if (observable.getAuthorizeDt()==null)
                        //  lockMap.put("ACCOUNTNO",lblAccountNo.getText());
                        //  else
                        lockMap.put("ISSUEID", txtRemarks.getText());
                    }
                    //                setEditLockMap(lockMap);
                    //                setEditLock();
                    //                super.removeEditLock(lblAccountNo.getText());
                    //__ Clear Screen...
                    System.out.println("getoperation asdasd"+observable.getOperation());
                    System.out.println("get action type"+observable.getActionType());
                    if (observable.getProxyReturnMap() != null && (observable.getOperation()==ClientConstants.ACTIONTYPE_NEW)) {
                        HashMap proxyResultMap = observable.getProxyReturnMap();
                        System.out.println("proxy result here asd" + proxyResultMap);
                        if (proxyResultMap.containsKey("CASH_TRANS_LIST") || proxyResultMap.containsKey("TRANSFER_TRANS_LIST")) {
                            System.out.println("locker return map here" + proxyResultMap);
                            displayTransDetails(proxyResultMap);
                            observable.setProxyReturnMap(null);
                        }
                    }
                    observable.resetCustDetails();
                    observable.resetJntAccntHoldTbl();
                    observable.resetChrgsTable();
                    observable.resetPassTable();
                    observable.resetOBFields();
                    observable.resetLabels();
                    /*
                     * To reset the POA, Introducer and Nominee Table
                     */
                    (poaUI.getPowerOfAttorneyOB()).resetAllFieldsInPoA();
                    poaUI.setPoAToolBtnsEnableDisable(false);
                    poaUI.setAllPoAEnableDisable(false);

                    //__ To get back the Nominee Screen in case Corporate Cust was selected...
                    tabAccounts.remove(nomineeUi);
                    tabAccounts.remove(authSignUI);
                    tabAccounts.remove(poaUI);

                    nomineeUi.resetTable();
                    nomineeUi.resetNomineeData();
                    nomineeUi.getNomineeOB().ttNotifyObservers();
                    nomineeUi.disableNewButton(false);

                    authSignUI.resetAllFieldsInAuthTab();
                    authSignUI.setAuthEnableDisable(false);
                    authSignUI.setAllAuthInstEnableDisable(false);

                    // Disable all the screen
                    ClientUtil.enableDisable(this, false, false, true);
                    // Some other fields should also be disabled at init time
                    enableDisableComponents(false);

                    /*
                     * set the operation to "NOOP" and reload the menu
                     * and the toolbar
                     */
                    observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
                    setupMenuToolBarPanel();
                    observable.setResultStatus();
                    lblCustName.setText("");
                    lblStatus.setText(observable.getLblStatus());

                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.cancelAction(false);
                    transactionUI.resetObjects();
                    transactionUI.setMainEnableDisable(false);
                    txtCharges.setText("");
                    txtServiceTax.setText("");
                    lblTotAmtVal.setText("");
                    tabAccounts.setSelectedIndex(0);
                    btnDepositNo.setEnabled(false);
                    btnLockerNo.setEnabled(false);
                    ClientUtil.enableDisable(panOperations, false, false, true);
                    ClientUtil.clearAll(this);
                    dontShowJointDialog = false;
                    //__ Make the Screen Closable..
                    setModified(false);
                    btnReject.setEnabled(true);
                    btnAuthorize.setEnabled(true);
                    btnException.setEnabled(true);
                    lblServiceTaxVal.setText("");
                    //displayTransDetail(observable.getProxyReturnMap());
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }


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

    public void displayTransDetails(HashMap returnMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = returnMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            System.out.println("jhj>>>>>>>adad1211222@@@@" + (returnMap.get(keys[i]) instanceof String));
            if (returnMap.get(keys[i]) instanceof String) {

                continue;
            }

            tempList = (List) returnMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {

                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {

                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {

                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {

                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {

                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {

                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            //paramMap.put("TransId", transId);
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                paramMap.put("TransId", keys1[i]);
                ttIntgration.setParam(paramMap);
                //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
//            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
//                ttIntgration.integrationForPrint("ReceiptPayment");
//            
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
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditNewActionPerformed
        // Add your handling code here:

        observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
        poaUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EDIT]);
        authSignUI.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_EDIT]);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("EditN");
    }//GEN-LAST:event_mitEditNewActionPerformed

    private void mitAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAddNewActionPerformed
        // Add your handling code here:
        ClientUtil.enableDisable(this, true);
        disbleComponents();
        // Some other fields should also be enables at init time
        enableDisableComponents(true);

        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();

        lblStatus.setText(observable.getLblStatus());

        /* now setup the menu, as it should be based on the current operation,
         * which at startup is "NOOP"
         */
        setupMenuToolBarPanel();
        /*
         * some more components are to be setup, because its the "Transfer In"
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

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_mitAddNewActionPerformed

    private void rbtnPwdYes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPwdYes1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnPwdYes1ActionPerformed

    private void rdoFreezeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFreezeActionPerformed
        // TODO add your handling code here:
        rdoUnFreeze.setSelected(false);
        rdoFreeze.setSelected(true);
        lblfreezRemarks.setText("Freeze Remarks");
        lblFreezeDt.setText("Freeze Date");
        lblfreezRemarks.setVisible(true);
        txtFreezeRemarks.setVisible(true);
        lblFreezeDt.setVisible(true);
        dtdFreezeDt.setVisible(true);
        dtdFreezeDt.setDateValue(CommonUtil.convertObjToStr(currDt));
        if (observable.isRdoFreeze() == true) {
            txtFreezeRemarks.setText(observable.getTxtFreezeRemarks());
            dtdFreezeDt.setDateValue(observable.getDtdFreezeDt());
        } else {
            txtFreezeRemarks.setText("");
            dtdFreezeDt.setDateValue(CommonUtil.convertObjToStr(currDt));
        }
    }//GEN-LAST:event_rdoFreezeActionPerformed

    private void rdoFreezeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoFreezeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoFreezeFocusLost

    private void rdoUnFreezeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoUnFreezeActionPerformed
        // TODO add your handling code here:
        rdoUnFreeze.setSelected(true);
        rdoFreeze.setSelected(false);
        lblfreezRemarks.setText("UnFreeze Remarks");
        lblFreezeDt.setText("UnFreeze Date");
        lblfreezRemarks.setVisible(true);
        txtFreezeRemarks.setVisible(true);
        lblFreezeDt.setVisible(true);
        dtdFreezeDt.setVisible(true);
        if (observable.isRdoUnFreeze() == true) {
            txtFreezeRemarks.setText(observable.getTxtFreezeRemarks());
            dtdFreezeDt.setDateValue(observable.getDtdFreezeDt());
        } else {
            txtFreezeRemarks.setText("");
            dtdFreezeDt.setDateValue(CommonUtil.convertObjToStr(currDt));
        }
    }//GEN-LAST:event_rdoUnFreezeActionPerformed

    private void dtdFreezeDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdFreezeDtFocusLost
        // TODO add your handling code here:
        java.util.Date dt = DateUtil.getDateMMDDYYYY(dtdFreezeDt.getDateValue());
        if (dt != null && DateUtil.dateDiff(currDt, dt) > 0) {
        if (DateUtil.dateDiff(currDt, dt) > 0) {
            ClientUtil.displayAlert("Future Date not allowed");
            dtdFreezeDt.setDateValue(CommonUtil.convertObjToStr(currDt));
        }
        }
    }//GEN-LAST:event_dtdFreezeDtFocusLost

    private void cboSuspDepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSuspDepActionPerformed
        // TODO add your handling code here:

        if(cboSuspDep.getSelectedItem() != null && !cboSuspDep.getSelectedItem().equals("")){
            depSus = cboSuspDep.getSelectedItem().toString();
        }

        //depSus=cboSuspDep.getSelectedItem().toString();

    }//GEN-LAST:event_cboSuspDepActionPerformed

private void btnResetPwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetPwdActionPerformed
// TODO add your handling code here:
            resetLockerPasswordApproveUI=new ResetLockerPasswordApproveUI(this);
            if(resetLockerPasswordApproveUI.isLoginTrue()){
                System.out.println("inside login true"); 
                ClientUtil.showMessageWindow("Continue with password reseting");
                 txtCurPassword.setVisible(false);
                 lblCurPassword.setVisible(false);
                 resetPwdTrue=true;
            }
  
}//GEN-LAST:event_btnResetPwdActionPerformed

    private void txtServiceTaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServiceTaxFocusLost
        // TODO add your handling code here:
        transactionUI.resetObjects();
        transactionUI.cancelAction(true);
        calculateServiceTax();// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
        double gstVal = CommonUtil.convertObjToDouble(lblServiceTaxVal.getText()).doubleValue();
        double com = CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue();
        double srTax = CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue();        
        double tot = com + srTax + gstVal;
        lblTotAmtVal.setText(String.valueOf(tot));
        transactionUI.setCallingAmount(String.valueOf(tot));
        //transactionUI.setCallingProdID(CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
        //transactionUI.setCallingProdID(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
        getDetails();
    }//GEN-LAST:event_txtServiceTaxFocusLost

    private void btnPhotoSignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoSignActionPerformed
        // TODO add your handling code here:
        if (tblAct_Joint.getRowCount() > 0) {
            String custId = CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(tblAct_Joint.getSelectedRow(), 1));
            if (custId != null && !custId.equalsIgnoreCase("")) {
                new com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI(custId, "NewActOpening").show();
            }
        }      
        
    }//GEN-LAST:event_btnPhotoSignActionPerformed

    private void btnCheck() {

        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);


        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCustomerIdAI.setEnabled(false);
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


    /*
     * This method is used to popup the window which will have some display


    /* This method is used to popup the window which will have some display

     * information
     */
    private void callView(String currField) {



        viewType = currField;
        authSignUI.setViewType(viewType);
        poaUI.setViewType(viewType);

        //--- If Customer Id is selected OR JointAccnt New is clciked, show the popup Screen of Customer Table
        if ((currField == "CUST_ID") || (currField == "JointAccount")) {
            HashMap viewMap = new HashMap();
            StringBuffer presentCust = new StringBuffer();

            if (tblAct_Joint.getRowCount() != 0) {
                //                int jntAccntTablRow = tblJointAcctHolder.getRowCount();
                for (int i = 0, sizeJointAcctAll = tblAct_Joint.getRowCount(); i < sizeJointAcctAll; i++) {
                    if (i == 0 || i == sizeJointAcctAll) {
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(i, 1)) + "'");
                    } else {
                        presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(i, 1)) + "'");
                    }
                }
            }

            HashMap whereMap = new HashMap();
            //HashMap viewMap=new HashMap();
            if (currField != "JointAccount") {
                if (depProdDetails.get("TYPES_OF_DEPOSIT") != null && !depProdDetails.get("TYPES_OF_DEPOSIT").equals("NORMAL")) {
                    if (depProdDetails.get("TYPES_OF_DEPOSIT").equals("NRO") || depProdDetails.get("TYPES_OF_DEPOSIT").equals("NRE")) {
                        whereMap.put("RESIDENTIALSTATUS", "NONRESIDENT");
                    }
                }
                if (depProdDetails.get("STAFF_ACCOUNT") != null && depProdDetails.get("STAFF_ACCOUNT").equals("Y")) {
                    whereMap.put("STAFF_ID", depProdDetails.get("STAFF_ACCOUNT"));
                }
            }




            viewMap.put("MAPNAME", "getSelectAccInfoTOList");
            whereMap.put("CUSTOMER_ID", presentCust);
            if (cboConstitutionAI.getSelectedItem().equals("Joint Account") || cboConstitutionAI.getSelectedItem().equals("Individual") || cboConstitutionAI.getSelectedItem().equals("Induvidual")) {
                whereMap.put("CUST_TYPE", "INDIVIDUAL");
            } else {
                whereMap.put("OTHER_INDIVIDUAL", "INDIVIDUAL");
            }
            whereMap.put("LOCKER_JOINT_CUST_ADD","LOCKER_JOINT_CUST_ADD");// Added by nithya on 15-05-2018 for 7857 [ while locker issue, the customer search not require branch code checking
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();

            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccInfoTOList");

            viewMap.put(CommonConstants.MAP_NAME, "Locker.getCustData");

            String PROD_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            //  HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.PRODUCT_ID, PROD_ID);
            //  String behaviour = ((OperativeAcctProductTO)ClientUtil.executeQuery("getOpAcctProductTOByProdId", whereMap).get(0)).getBehavior();
            whereMap.clear();
            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            // if(!behaviour.equalsIgnoreCase("SB")){
            //whereMap.put("CUST_TYPE", CommonConstants.CUSTOMER_CORPORATE);
            //  }else{
            // whereMap.put("CUST_TYPE", behaviour);
            //}
            whereMap.put("CONSTITUTION", ((ComboBoxModel) cboConstitutionAI.getModel()).getKeyForSelected());
            // TO prevent the duplication of the Customer in case of Joint account...
            whereMap.put("CUSTOMER_ID", presentCust);
            if (CommonUtil.convertObjToStr(observable.getStaffOnly()).equalsIgnoreCase("Y")) {
                whereMap.put("STAFF_ID", "");
            }
            whereMap.put("PRODUCT_ID", PROD_ID);
            //String status = ((OperativeAcctProductTO)ClientUtil.executeQuery("getOpAcctProductTOByProdId", whereMap).get(0)).getSRemarks();
            //if(status.equals("NRO")||status.equals("NRE"))
            // whereMap.put("RESIDENTIAL_STATUS", "NONRESIDENT");
            //else
            // whereMap.put("RESIDENTIAL_STATUS","");

            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            System.out.println("#$#$#$ AccountsUI viewMap : " + viewMap);
            //To make the Clear Button Enabled...
            // new ViewAll(this, viewMap).show();
        } else if (currField.equals("EditN") || currField.equals("Delete")) {
            HashMap whereMap = new HashMap();
            HashMap viewMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("ISSUEID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());

            viewMap.put(CommonConstants.MAP_NAME, "getLockerListEdit");
            if (currField.equals("Delete")) {
                whereMap.put("DELETED", "DELETED");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("Deletedstatus")) {
            HashMap where = new HashMap();
            HashMap viewMap = new HashMap();
            where.put("BRANCH_ID", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_NAME, "DeletedLockerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("Enquirystatus")) {
            HashMap where = new HashMap();
            HashMap viewMap = new HashMap();
            where.put("BRANCH_ID", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_NAME, "getLockerListEdit");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("LockerNo")) {
            HashMap where = new HashMap();
            HashMap viewMap = new HashMap();
            where.put("BRANCH_ID", getSelectedBranchID());
            String PRODUCT_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            where.put("PRODUCT_ID", PRODUCT_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getLockerList");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("DepositNo") || currField.equals("SuspenseNo")) {

            HashMap where = new HashMap();
            where.put("BRANCH_ID", getSelectedBranchID());
            String CUST_ID = txtCustomerIdAI.getText();
            //depSus=cboSuspDep.getSelectedItem().toString();
            where.put("CUST_ID", CUST_ID);
            where.put("TODAY_DT", currDt);

            HashMap viewMap = new HashMap();
//            if (depSus.equals("Deposit")) {
//                viewMap.put(CommonConstants.MAP_NAME, "getLockerDepList");
//            } else if (depSus.equals("Suspense")) {
//                viewMap.put(CommonConstants.MAP_NAME, "getLockerSusList");
//            }
            depSus=cboSuspDep.getSelectedItem().toString();
            if(depSus.equals("Deposit"))
            	viewMap.put(CommonConstants.MAP_NAME, "getLockerDepList");
            else if(depSus.equals("Suspense"))
            	viewMap.put(CommonConstants.MAP_NAME, "getLockerSusList");  
            else if(depSus.equals("Operative")) //Added to select operative a/c as deposit, by shihad on 05/06/2014  
            	viewMap.put(CommonConstants.MAP_NAME, "getLockerOperActList");     
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            new ViewAll(this, viewMap).show();
        }





        if (currField == "CREDIT_ACC_NO") {
            HashMap viewMap = new HashMap();
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
                //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            }
            HashMap whereMap = new HashMap();
            if (cboProdId.getModel() != null && cboProdId.getModel().getSize() > 0) {
                whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            }
            if (whereMap.get("SELECTED_BRANCH") == null) {
                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            } else {
                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            }
            whereMap.put("FILTERED_LIST", "");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
        //--- If Branch Code is seleted, show the popup screen of Branch Code details
        if (currField == "BRANCH CODE") {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("CURRENT_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBranchList");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
        //--- If Account Number is Selected, show the popup screen of Account Number
        if (currField == "ACCOUNT NUMBER") {
            HashMap viewMap = new HashMap();
            // if(!txtTransferingBranchCode.getText().equals("") && txtTransferingBranchCode.getText().length()>0){
            // HashMap viewTransMap = new HashMap();
            // viewTransMap.put("CURRENT_BRANCH",txtTransferingBranchCode.getText());
            // viewMap.put(CommonConstants.MAP_WHERE,viewTransMap);
            // viewMap.put(CommonConstants.MAP_NAME, "Deposite.ViewAllAccountNumber");
            //new ViewAll(this, viewMap).show();
            //}
        }
        //        //--- If Account No. is selected, show the popup screen of Account No. details
        //        if (currField.equals("ACCOUNT NO")) {
        //            HashMap viewMap = new HashMap();
        //            viewMap.put("MAPNAME", "getAccNoDet");
        //            new ViewAll(this, viewMap).show();

        //--- If Renewal is selected, show the popup screen of the Matured Deposit Accounts
        if (currField.equals("RENEW")) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[12]);
            final HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            System.out.println("callView");
            whereMap.put("CURR_DATE", currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAllRenewalAccInfo");
            new ViewAll(this, viewMap).show();
            whereMap = null;
            // rdoOpeningMode_Normal.setSelected(false);
            //rdoOpeningMode_Renewal.setSelected(true);

        }
        if (currField.equals("EXTENSION_OF_DEPOSIT")) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[21]);
            final HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            System.out.println("callView");
            whereMap.put("CURR_DATE", currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAllExtensionAccInfo");
            new ViewAll(this, viewMap).show();
            whereMap = null;
            // rdoOpeningMode_Normal.setEnabled(false);
            // rdoOpeningMode_Renewal.setEnabled(false);
            //rdoOpeningMode_Extension.setEnabled(true);
            // rdoOpeningMode_Extension.setSelected(true);
        }
        if (currField.equals("CLOSED_DEPOSIT")) {
            HashMap whereMap = new HashMap();
            HashMap viewMap = new HashMap();
            whereMap.put("SELECTED_BRANCH_ID", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getClosedDepositDetails");
            whereMap = null;
            new ViewAll(this, viewMap).show();
        }
        if (currField.equals("ENQUIRY_STATUS")) {
            HashMap whereMap = new HashMap();
            HashMap viewMap = new HashMap();
            whereMap.put("BRANCH_ID", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap.put(CommonConstants.MAP_NAME, "getAccountList");
            whereMap = null;
            new ViewAll(this, viewMap).show();
        }

        //--- If Button Agent Id is clicked, show the popup screen of the AgentIds.
        // if (currField.equals(VIEW_TYPE_AGENT_ID)) {
        //  final HashMap viewMap = new HashMap();
        // viewMap.put(CommonConstants.MAP_NAME, "getAgentId");
        //  new ViewAll(this,viewMap).show();

        if (currField == "RENEWAL_CREDIT_ACC_NO") {
            HashMap viewMap = new HashMap();
            //  String prodType = ((ComboBoxModel)cboRenewalProdType.getModel()).getKeyForSelected().toString();
            // if(!prodType.equals("GL")){
            // viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
            //  + ((ComboBoxModel)cboRenewalProdType.getModel()).getKeyForSelected().toString());
            // }else{
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            //}
            HashMap whereMap = new HashMap();
            //  whereMap.put("PROD_ID", ((ComboBoxModel) cboRenewalProdId.getModel()).getKeyForSelected());
            // if(whereMap.get("SELECTED_BRANCH")==null)
            // whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
            // else
            whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
        //if(currField == "RENEWAL_INT_TRANS_ACC_NO"){
        // HashMap viewMap = new HashMap();
        // String prodType = ((ComboBoxModel)cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString();
        //  if(!prodType.equals("GL")){
        //  viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
        //+ ((ComboBoxModel)cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString());
        // }else{
        // viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
        // }
        // HashMap whereMap = new HashMap();
        // whereMap.put("PROD_ID", ((ComboBoxModel) cboRenewalInterestTransProdId.getModel()).getKeyForSelected());
        //   if(whereMap.get("SELECTED_BRANCH")==null)
        ///   whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        // else
        //  whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
        // viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        //  new ViewAll(this, viewMap).show();
        // }
        //if(currField == "RENEWAL_DEP_TRANS_ACC_NO"){
        //   HashMap viewMap = new HashMap();
        // String prodType = ((ComboBoxModel)cboRenewalDepTransProdType.getModel()).getKeyForSelected().toString();
        //  if(!prodType.equals("GL")){
        // viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
        // + ((ComboBoxModel)cboRenewalDepTransProdType.getModel()).getKeyForSelected().toString());
        //}else{
        //viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
        // }
        //HashMap whereMap = new HashMap();
        // whereMap.put("PROD_ID", ((ComboBoxModel) cboRenewalDepTransProdId.getModel()).getKeyForSelected());
        //  if(whereMap.get("SELECTED_BRANCH")==null)
        //      whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        // else
        // whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
        //viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        // new ViewAll(this, viewMap).show();
        // }
        // if(currField == "EXTENSION_PAYMODE"){
        //   HashMap viewMap = new HashMap();
        //  String prodType = ((ComboBoxModel)cboExtensionProdType.getModel()).getKeyForSelected().toString();
        //  if(!prodType.equals("GL")){
        // viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
        //   + ((ComboBoxModel)cboExtensionProdType.getModel()).getKeyForSelected().toString());
        // }else{
        // viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
        // }
        //HashMap whereMap = new HashMap();
        //  whereMap.put("PROD_ID", ((ComboBoxModel) cboExtensionProdId.getModel()).getKeyForSelected());
        // if(whereMap.get("SELECTED_BRANCH")==null)
        // whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //else
        // whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
        //viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        //  new ViewAll(this, viewMap).show();
        //}
        if (currField == "EXTENSION_TRANS") {
            HashMap viewMap = new HashMap();
            // String prodType = ((ComboBoxModel)cboExtensionTransProdType.getModel()).getKeyForSelected().toString();
            //  if(!prodType.equals("GL")){
            //   viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
            ///  + ((ComboBoxModel)cboExtensionTransProdType.getModel()).getKeyForSelected().toString());
            //}else{
            //viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            // }
            //HashMap whereMap = new HashMap();
            //whereMap.put("PROD_ID", ((ComboBoxModel) cboExtensionTransProdId.getModel()).getKeyForSelected());
            //  if(whereMap.get("SELECTED_BRANCH")==null)
            // whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
            // else
            //  whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            // viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            // new ViewAll(this, viewMap).show();
            //}
            //  if(currField.equals("EXISTING_CUSTOMER") && txtExistingAcctNo.getText().length()>0){
            //  HashMap existingMap = new HashMap();
            //   existingMap.put("ACT_NUM",txtExistingAcctNo.getText());
            //   List mapDataList = ClientUtil.executeQuery("getSelectExistingCustId", existingMap);
            //System.out.println("#### mapDataList :"+mapDataList);
            // if (mapDataList!=null && mapDataList.size()>0) {
            //  existingMap = (HashMap)mapDataList.get(0);
            //   existingMap.put("ACT_NUM",txtExistingAcctNo.getText());
            //   fillData(existingMap);
            //}else{
            ClientUtil.showAlertWindow("Invalid Account No");
            // txtExistingAcctNo.setText("");
            return;
            //}
        }
    }

    /* this method will be called by the ViewAll class, to which we are
     * passing the UI class reference
     * this will also set the account number and operation value for OB
     */
    /**
     * @param obj
     */
    public void fillData(Object obj) {
        System.out.println("@@@@OBJ" + obj);
        HashMap hash = (HashMap) obj;
        authSignUI.setViewType(viewType);
        poaUI.setViewType(viewType);
        nomineeUi.setViewType(viewType);
        final String CUSTID;
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            setAuthorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            hash.remove("PARENT");
            viewType = "AUTHORIZE";
            btnReject.setEnabled(false);
            rejectFlag = 1;
            btnAuthorize.setEnabled(true);
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            System.out.println("hash.get(PARENT) tD" + hash.get("PARENT"));
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            setAuthorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            hash.remove("PARENT");
            viewType = "AUTHORIZE";
            btnReject.setEnabled(false);
            rejectFlag = 1;
            btnAuthorize.setEnabled(true);
        }
        if (viewType.equals("CUSTOMER ID")) {
            //__ To reset the data for the Previous selected Customer..
            System.out.println("inside cust");
            observable.resetCustDetails();
            if (hash.containsKey("CUSTOMER ID")) {
                CUSTID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
            } else {
                CUSTID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
            }
            txtCustomerIdAI.setText(CUSTID);
            hash.put("CUSTID", CUSTID);
            //  txtCustomerIdAI.setText(CUSTID);
            //__ To get the ComboBox value for the Communication Addr type...
            observable.getCustAddrData(CUSTID);
            cboCommAddr.setModel(observable.getCbmCommAddr());


            //__ To display the customer Info...
            CustInfoDisplay(CUSTID);
            authSignUI.getAuthorizedSignatoryOB().authorizedSignatoryList(CUSTID);
            //__ To set the Communication addr Type...
            cboCommAddr.setSelectedItem(((ComboBoxModel) cboCommAddr.getModel()).getDataForKey(observable.getAddrType()));


            //__ To set the Name of the Customer...
            String CUSTNAME = "";
            if (hash.containsKey("NAME")) {
                CUSTNAME = CommonUtil.convertObjToStr(hash.get("NAME"));
            } else if (hash.containsKey("Name")) {
                CUSTNAME = CommonUtil.convertObjToStr(hash.get("Name"));
                hash.put("NAME", CUSTNAME);
            }
            txtActName.setText(CUSTNAME);
            lblCustName.setText(CUSTNAME);

            nomineeUi.setMainCustomerId(txtCustomerIdAI.getText());



            //__ To set the Category if its Staff...
            final String CATEGORY = CommonUtil.convertObjToStr(hash.get("CUSTOMER TYPE ID"));
            if (CATEGORY.equalsIgnoreCase("STAFF")) {
                cboCategory.setSelectedItem(((ComboBoxModel) cboCategory.getModel()).getDataForKey(CATEGORY));

            } else {
                cboCategory.setSelectedIndex(0);
            }
            if (hash.containsKey("Customer Type")) {
                //                cboConstitutionAI.setSelectedItem(hash.get("Customer Type"));
                hash.put("CUSTOMER TYPE", hash.get("Customer Type"));
            }
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
            hash.put("ADDCUST", "MAIN");
            setPasswordTab(hash);
            btnDepositNo.setEnabled(true);
            //             ClientUtil.enableDisable(panInstEntry, false);
        } else if (viewType.equals("JointAccount")) {
            System.out.println("inside joint");
            JointAcctDisplay((String) hash.get("CUSTOMER ID"));
            hash.put("ADDCUST", "JOINT");
            setPasswordTab(hash);

        } else if (viewType.equals("TransferBranch")) {
            System.out.println("inside transfr");
            //             this.txtBranchCodeAI.setText((String)hash.get("BRANCHCODE"));
            //             this.lblBranchNameValueAI.setText((String)hash.get("BRANCHNAME"));
            updatePreviousBranchDetails();
            observable.populatePreviousAccounts();

        } else if (viewType.equals("EditN")
                || viewType.equals("Delete") || viewType.equals("AUTHORIZE") || viewType.equals("Deletedstatus") || viewType.equals("Enquirystatus")) {
            System.out.println("inside edit");
            final String CUSTTYPE = CommonUtil.convertObjToStr(hash.get("CUSTOMERTYPE"));
            // set the accountnumber and the operation for the observer
            final String ACCOUNTNO = hash.get("LOCNO").toString();
            //  final String CollectRentUpto=CommonUtil.convertObjToStr(hash.get("COLLECT_RENT_UPTO"));
            observable.setAccountNumber(ACCOUNTNO);
            observable.setAccountNo(ACCOUNTNO);
            //observable.setCollectRentUpto(CollectRentUpto);
            //             updateProductInterestRates();

            if (viewType.equals("EditN")) {
                observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
                //                 setLockerCharges(hash.get("PRODID").toString());
            } else if (viewType.equals("Delete")) {
                observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
            } else if (viewType.equals("AUTHORIZE")) {
                observable.setOperation(observable.getOperation());                
            }

            /* we have to load all the data from database to the screen
             * add the WHERE condition in the hashmap and pass it onto the OB
             */
            //            hash.put(CommonConstants.MAP_WHERE, hash.get("LOCNO"));
            hash.put(CommonConstants.MAP_WHERE, hash.get("ISSUEID"));
            hash.put("CUST_ID", hash.get("CUSTID"));
            hash.put("PROD_ID", hash.get("PRODID"));
            hash.put("LOCKER_NUM", hash.get("LOCNO"));
            hash.put("ISSUE_ID", hash.get("ISSUEID"));
            setIssueId(CommonUtil.convertObjToStr(hash.get("ISSUEID")));
            observable.populateData(hash, nomineeUi.getNomineeOB(), poaUI.getPowerOfAttorneyOB(), authSignUI.getAuthorizedSignatoryOB(), authSignUI.getAuthorizedSignatoryInstructionOB());
            //            tblInstruction2LockCharges.setModel(observable.getTbmLockCharges());
            cboCommAddr.setModel(observable.getCbmCommAddr());
            update(null, null);

            // updateOBPassTab();
            //rbtnSiYesFocusLost(null);
            rbtnSiNoActionPerformed(null);
            rbtnSiYesActionPerformed(null);
            nomineeUi.setMainCustomerId(txtCustomerIdAI.getText());

            //__ To set the Name of the Customer...
            lblCustName.setText(observable.getCustName(observable.getTxtCustomerIdAI()));

            if ((observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE) || (viewType.equals("AUTHORIZE"))) {
                ClientUtil.enableDisable(panAccounts, false);         // Disables the panel...
                this.btnCustomerIdAI.setEnabled(false);
                //                ClientUtil.enableDisable(panOperations, false, false, true);
                txtCustomerIdAI.setEnabled(false);
            } else {
                ClientUtil.enableDisable(this, true);
                disbleComponents();
                txtCustomerIdAI.setEnabled(true);

                dtdOpeningDateAI.setEnabled(false);
                //dtdExpiryDate.setEnabled(false);
                this.cboProductIdAI.setEnabled(false);

                //__ Testing...
                //                cboProductIdAIItemStateChanged(null);
            }
            //__ Changes in AuthSignatory...

            addCustIDNAuthSignatory();

            enableDisableComponents(true);
            if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDITTI
                    || observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                this.btnCustomerIdAI.setEnabled(true);
                if (observable.getAuthorizeDt() != null) {
                    txtCustomerIdAI.setEnabled(false);
                    btnCustomerIdAI.setEnabled(false);
                }

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
            //             txtBranchCodeAIFocusLost(null);

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
            nomineeUi.setMaxNominee(1);


            if (viewType.equals("Delete") || viewType.equals("AUTHORIZE")) {
                nomineeUi.disableNewButton(false);
                this.btnCustomerIdAI.setEnabled(false);
            } else {
                poaUI.setPoANewOnlyEnable();
                authSignUI.setAuthOnlyNewBtnEnable();
                authSignUI.setAuthInstOnlyNewBtnEnable();
            }


            System.out.println("CUSTTYPE: " + CUSTTYPE);
            //__ if the selected Customer is Corporate, Do not enter the Nominee...
            //             if(CUSTTYPE.equalsIgnoreCase("CORPORATE")){
            //                 //                nomineeUi.setBtnEnableDisable(false);
            //                 tabAccounts.remove(nomineeUi);
            //             }else{
            //                 tabAccounts.add(nomineeUi,"Nominee", 3);
            //                 ClientUtil.enableDisable(nomineeUi,false);   //This line added by Rajesh.
            //             }
            if (!CUSTTYPE.equals("Corporate")) {
                tabAccounts.add(nomineeUi, "Nominee", 2);
                tabAccounts.remove(poaUI);
                tabAccounts.remove(authSignUI);
                ClientUtil.enableDisable(nomineeUi, false);   //This line added by Rajesh.
            } else {
                tabAccounts.remove(nomineeUi);
                tabAccounts.add(poaUI, "Power Of Attorney", 2);
                poaUI.setPoAToolBtnsEnableDisable(false);

                tabAccounts.add(authSignUI, "Authorized Signatory", 3);
                authSignUI.setAuthEnableDisable(false);
                authSignUI.setAllAuthInstEnableDisable(false);

            }
            if (viewType.equals("AUTHORIZE")) {
                btnAuthorize.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getOperation() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            double comm = CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue();
            double serviceTax = CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue();
            lblTotAmtVal.setText(String.valueOf(comm + serviceTax));
             if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
                if (rbtnPwdNo.isSelected()) {
                    System.out.println("inside fll radio");
                    chkEditPwd.setEnabled(false);
                    btnResetPwd.setEnabled(false);
                }
                else{
                btnResetPwd.setEnabled(true);
                }

            }
        }  else if (viewType.equals("LockerNo")) {
            txtLockerNo.setText(CommonUtil.convertObjToStr(hash.get("FROM_LOC_NO")));
            lblLockerKeyNoVal.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_KEY_NO")));
            System.out.println("pro id" + CommonUtil.convertObjToStr(((ComboBoxModel) (cboProductIdAI.getModel())).getKeyForSelected()));
            HashMap headMap = new HashMap();
            headMap.put("value", CommonUtil.convertObjToStr(((ComboBoxModel) (cboProductIdAI.getModel())).getKeyForSelected()));
            // Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
            lockerTransHeadList = ClientUtil.executeQuery("getLockerAccountHeads", headMap); 
            calculateServiceTax();
        } else if (viewType.equals("DepositNo")) {
            txtPrevActNumAI.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO")));
            //             lblLockerKeyNoVal.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_KEY_NO")));
        } else if (viewType.equals("CREDIT_ACC_NO")) {
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (prodType != null && !prodType.equals("GL")) {
                if (prodType.equals("TD")) {
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
                }
                txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                lblCustomerNameCrValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
            } else {
                txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                lblCustomerNameCrValue.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
            }
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_VIEW_MODE && !viewType.equals("AUTHORIZE")) {
            ClientUtil.enableDisable(this, false, false, true);
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            txtCharges.setEnabled(true);
            txtServiceTax.setEnabled(true);
        } else {
            txtCharges.setEnabled(false);
            txtServiceTax.setEnabled(false);
            ClientUtil.enableDisable(panStdInstructions, false, false, true);
            ClientUtil.enableDisable(panOperations, false, false, true);
            ClientUtil.enableDisable(transactionUI, false, false, true);
        }
        //         txtMinBal1FlexiAD.setEnabled(false);
        //         txtMinBal2FlexiAD.setEnabled(false);
        setModified(true);
        tabAccounts.resetVisits();
    }

    private void CustInfoDisplay(String custId) {
        System.out.println("CustInfoDisplay()");
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
        //         observable.setTxtBranchCodeAI(this.txtBranchCodeAI.getText());
    }
    /*
     * use this method to get the customer informatio and update the name and
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
        //         lblCrInterestRateValueIN.setText(hash.get("APPL_CR_INT_RATE") + " %");
        //         observable.setLblCrInterestRateValueIN(CommonUtil.convertObjToStr(hash.get("APPL_CR_INT_RATE")+ " %"));
        //         lblDrInterestRateValueIN.setText(hash.get("APPL_DEBIT_INT_RATE") + " %");
        //         observable.setLblDrInterestRateValueIN(CommonUtil.convertObjToStr(hash.get("APPL_DEBIT_INT_RATE")+ " %"));
        //         lblPenalInterestValueIN.setText(hash.get("PENAL_INT_DEBIT_BALACCT") + " %");
        //         observable.setLblPenalInterestValueIN(CommonUtil.convertObjToStr(hash.get("PENAL_INT_DEBIT_BALACCT")+ " %"));
        //         lblAgClearingValueIN.setText(hash.get("AG_CLEARING") + " %");
        //         observable.setLblAgClearingValueIN(CommonUtil.convertObjToStr(hash.get("AG_CLEARING")+ " %"));
    }

    public void update(java.util.Observable observed, Object arg) {
        //         txtBranchCodeAI.setText(observable.getTxtBranchCodeAI());
        //         txtAmoutTransAI.setText(observable.getTxtAmoutTransAI());
        //         txtRemarksAI.setText(observable.getTxtRemarksAI());
        txtLockerNo.setText(observable.getTxtLockerNo());
        lblLockerKeyNoVal.setText(observable.getLblLockerKeyNoVal());
        txtClosedDt.setText(observable.getClosedDt());
        if (observable.getCboProductIdAI().equals("")) {
            cboProductIdAI.setSelectedIndex(0);
        } else {
            cboProductIdAI.setSelectedItem(((ComboBoxModel) cboProductIdAI.getModel()).getDataForKey(observable.getCboProductIdAI()));
        }

        txtCustomerIdAI.setText(observable.getTxtCustomerIdAI());
        txtPrevActNumAI.setText(observable.getTxtPrevActNumAI());
        txtCollectRentMM.setText((observable.getCollectRentMM()));
        txtCollectRentyyyy.setText(observable.getCollectRentYYYY());
        rbtnSiYes.setSelected(observable.getSiYes());
        rbtnSiNo.setSelected(observable.getSiNo());
        rbtnPwdYes.setSelected(observable.getPwdYes());
        rbtnPwdNo.setSelected(observable.getPwdNo());
        cboProdType.setSelectedItem(((ComboBoxModel) cboProdType.getModel()).getDataForKey(observable.getCboProdType()));
        transactionUI.setCallingTransProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getDataForKey(observable.getCboProdType())));
        //transactionUI.setCallingProdID(CommonUtil.convertObjToStr( ((ComboBoxModel) cboProdId.getModel()).getDataForKey(observable.getCboProductId())));
        if (!CommonUtil.convertObjToStr(observable.getCboProdType()).equals("")) {
            if (CommonUtil.convertObjToStr(observable.getCboProdType()).equals("GL")) {
                cboProdId.setSelectedItem("");
            } else {
                cboProdId.setSelectedItem(((ComboBoxModel) cboProdId.getModel()).getDataForKey(observable.getCboProductId()));
            }
        }
        //    if(observable.getSiYes()==true){
        //  cboProdType.setSelectedItem(observable.getCboProdType());
        //cboProdId.setSelectedItem(observable.getCboProdId());
        txtCustomerIdCr.setText(observable.getTxtCustomerIdCr());
        lblCustomerNameCrValue.setText(observable.getLblCustomerNameCrValue());
        // }
        // else if(observable.getSiNo()==true)
        //  {
        //   cboProdType.setSelectedItem("");
        // cboProdId.setSelectedItem("");
        //   txtCustomerIdCr.setText("");
        //  lblCustomerNameCrValue.setText("");
        // }
        rbtnPwdYes.setSelected(observable.getPwdYes());
        rbtnPwdNo.setSelected(observable.getPwdNo());
        dtdExpiryDate.setDateValue(observable.getDtdExpiryDate());


        //         if (observable.getCboConstitutionAI().equals("")) {
        //             cboConstitutionAI.setSelectedIndex(0);
        //         } else {
        //             cboConstitutionAI.setSelectedItem(
        //             ((ComboBoxModel) cboConstitutionAI.getModel()).getDataForKey(
        //             observable.getCboConstitutionAI()));
        //         }

        if (observable.getCboOpModeAI().equals("")) {
            cboOpModeAI.setSelectedIndex(0);
        } else {
            cboOpModeAI.setSelectedItem(
                    ((ComboBoxModel) cboOpModeAI.getModel()).getDataForKey(
                    observable.getCboOpModeAI()));
        }

        // txtODLimitAI.setText(observable.getTxtODLimitAI());

        //  if (observable.getCboGroupCodeAI().equals("")) {
        //    cboGroupCodeAI.setSelectedIndex(0);
        // } else {
        //   cboGroupCodeAI.setSelectedItem(
        //((ComboBoxModel) cboGroupCodeAI.getModel()).getDataForKey(
        // observable.getCboGroupCodeAI()));
        //}

        //   if (observable.getCboSettlementModeAI().equals("")) {
        //     cboSettlementModeAI.setSelectedIndex(0);
        // } else {
        //    cboSettlementModeAI.setSelectedItem(
        //   ((ComboBoxModel) cboSettlementModeAI.getModel()).getDataForKey(
        //  observable.getCboSettlementModeAI()));
        //  }

        if (observable.getCboCategory().equals("")) {
            cboCategory.setSelectedIndex(0);
        } else {
            cboCategory.setSelectedItem(
                    ((ComboBoxModel) cboCategory.getModel()).getDataForKey(
                    observable.getCboCategory()));
        }
        txtActName.setText(observable.getTxtActName());
        txtRemarks.setText(observable.getTxtRemarks());

        txtCharges.setText(observable.getTxtCharges());
        txtServiceTax.setText(observable.getTxtServiceTax());

        //        if (observable.getCboBaseCurrAI().equals("")) {
        //            cboBaseCurrAI.setSelectedIndex(0);
        //        } else {
        //            cboBaseCurrAI.setSelectedItem(
        //            ((ComboBoxModel) cboBaseCurrAI.getModel()).getDataForKey(
        //            observable.getCboBaseCurrAI()));
        //        }
        //
        //         chkPayIntOnCrBalIN.setSelected(observable.getChkPayIntOnCrBalIN());
        //         chkPayIntOnDrBalIN.setSelected(observable.getChkPayIntOnDrBalIN());
        //         chkChequeBookAD.setSelected(observable.getChkChequeBookAD());
        //         chkCustGrpLimitValidationAD.setSelected(observable.getChkCustGrpLimitValidationAD());
        //         chkMobileBankingAD.setSelected(observable.getChkMobileBankingAD());
        //         chkNROStatusAD.setSelected(observable.getChkNROStatusAD());
        //         chkATMAD.setSelected(observable.getChkATMAD());
        //         chkATMADActionPerformed(null);
        //         txtATMNoAD.setText(observable.getTxtATMNoAD());
        //         chkDebitAD.setSelected(observable.getChkDebitAD());
        //         chkDebitADActionPerformed(null);
        //         txtDebitNoAD.setText(observable.getTxtDebitNoAD());
        //         chkCreditAD.setSelected(observable.getChkCreditAD());
        //         chkCreditADActionPerformed(null);
        //         txtCreditNoAD.setText(observable.getTxtCreditNoAD());
        //
        //         chkFlexiAD.setSelected(observable.getChkFlexiAD());
        //         txtMinBal1FlexiAD.setText(observable.getTxtMinBal1FlexiAD());
        //         txtMinBal2FlexiAD.setText(observable.getTxtMinBal2FlexiAD());
        //         txtReqFlexiPeriodAD.setText(observable.getTxtReqFlexiPeriodAD());
        //         if (observable.getCboDMYAD().equals("")) {
        //             cboDMYAD.setSelectedIndex(0);
        //         } else {
        //             cboDMYAD.setSelectedItem(
        //             ((ComboBoxModel) cboDMYAD.getModel()).getDataForKey(
        //             observable.getCboDMYAD()));
        //         }
        //         chkFlexiADActionPerformed(null);

        //         if (observable.getCboDMYAD().equals("")) {
        //             cboDMYAD.setSelectedIndex(0);
        //         } else {
        //             cboDMYAD.setSelectedItem(
        //             ((ComboBoxModel) cboDMYAD.getModel()).getDataForKey(
        //             observable.getCboDMYAD()));
        //         }

        //         if (observable.getCboStmtFreqAD().equals("")) {
        //             cboStmtFreqAD.setSelectedIndex(0);
        //         } else {
        //             cboStmtFreqAD.setSelectedItem(
        //             ((ComboBoxModel) cboStmtFreqAD.getModel()).getDataForKey(
        //             observable.getCboStmtFreqAD()));
        //         }
        //
        //         chkStopPmtChrgAD.setSelected(observable.getChkStopPmtChrgAD());
        //         chkChequeRetChrgAD.setSelected(observable.getChkChequeRetChrgAD());
        //         chkInopChrgAD.setSelected(observable.getChkInopChrgAD());
        //         chkStmtChrgAD.setSelected(observable.getChkStmtChrgAD());
        //         chkPassBook.setSelected(observable.isChkPassBook());
        //         txtAccOpeningChrgAD.setText(observable.getTxtAccOpeningChrgAD());
        //         txtAccCloseChrgAD.setText(observable.getTxtAccCloseChrgAD());
        //         txtMisServiceChrgAD.setText(observable.getTxtMisServiceChrgAD());
        //         txtChequeBookChrgAD.setText(observable.getTxtChequeBookChrgAD());
        //         txtFolioChrgAD.setText(observable.getTxtFolioChrgAD());
        //
        //         txtExcessWithChrgAD.setText(observable.getTxtExcessWithChrgAD());
        //
        //         chkNonMainMinBalChrgAD.setSelected(observable.getChkNonMainMinBalChrgAD());
        //         chkNonMainMinBalChrgADActionPerformed(null);
        //         txtMinActBalanceAD.setText(observable.getTxtMinActBalanceAD());
        //         chkABBChrgAD.setSelected(observable.getChkABBChrgAD());
        //         chkABBChrgADActionPerformed(null);
        //         txtABBChrgAD.setText(observable.getTxtABBChrgAD());
        //         chkNPAChrgAD.setSelected(observable.getChkNPAChrgAD());
        //         chkNPAChrgADActionPerformed(null);
        //
        //         // transfering branch information
        //         lblBranchNameValueAI.setText(observable.getLblBranchNameValueAI());
        //         // interest rates
        //         lblRateCodeValueIN.setText(observable.getLblRateCodeValueIN());
        //         lblCrInterestRateValueIN.setText(observable.getLblCrInterestRateValueIN());
        //         lblDrInterestRateValueIN.setText(observable.getLblDrInterestRateValueIN());
        //         lblPenalInterestValueIN.setText(observable.getLblPenalInterestValueIN());
        //         lblAgClearingValueIN.setText(observable.getLblAgClearingValueIN());
        //
        //         // set the date control
        //         dtdATMFromDateAD.setDateValue(observable.getDtdATMFromDateAD());
        //         dtdATMToDateAD.setDateValue(observable.getDtdATMToDateAD());
        //         dtdActOpenDateAI.setDateValue(observable.getDtdActOpenDateAI());
        //         dtdCredit.setDateValue(observable.getDtdCredit());
        //         dtdCreditFromDateAD.setDateValue(observable.getDtdCreditFromDateAD());
        //         dtdCreditToDateAD.setDateValue(observable.getDtdCreditToDateAD());
        //         dtdDebit.setDateValue(observable.getDtdDebit());
        //         dtdDebitFromDateAD.setDateValue(observable.getDtdDebitFromDateAD());
        //         dtdDebitToDateAD.setDateValue(observable.getDtdDebitToDateAD());
        //         dtdNPAChrgAD.setDateValue(observable.getDtdNPAChrgAD());
        dtdOpeningDateAI.setDateValue(observable.getDtdOpeningDateAI());
        // dtdExpiryDate.setDateValue(observable.getDtdExpiryDate());
        tblAct_Joint.setModel(observable.getTblJointAccnt());
        //To set the  Account No...
        lblAccountNo.setText(observable.getAccountNo());
        rdoFreeze.setSelected(observable.isRdoFreeze());
        rdoUnFreeze.setSelected(observable.isRdoUnFreeze());
        txtFreezeRemarks.setText(observable.getTxtFreezeRemarks());
        dtdFreezeDt.setDateValue(observable.getDtdFreezeDt());
        if (rdoFreeze.isSelected() == true || rdoUnFreeze.isSelected() == true) {
            if (rdoFreeze.isSelected() == true) {
                lblfreezRemarks.setText("Freeze Remarks");
                lblFreezeDt.setText("Freeze Date");
            } else if (rdoUnFreeze.isSelected() == true) {
                lblfreezRemarks.setText("UnFreeze Remarks");
                lblFreezeDt.setText("UnFreeze Date");
            }
            lblfreezRemarks.setVisible(true);
            txtFreezeRemarks.setVisible(true);
            lblFreezeDt.setVisible(true);
            dtdFreezeDt.setVisible(true);
        }

        if (observable.getCboCommAddr() != null && observable.getCboCommAddr().equals("")) {
            //            cboCommAddr.setSelectedIndex(0);
            cboCommAddr.setSelectedItem("");
        } else {
            //             System.out.println("#@#@#@ cboCommAddr.getModel() : "+cboCommAddr.getModel());
            cboCommAddr.setSelectedItem(
                    ((ComboBoxModel) cboCommAddr.getModel()).getDataForKey(
                    observable.getCboCommAddr()));
        }

        //TO set the Customer details
        updateCustomerDetails();
        tblInstruction2.setModel(observable.getTbmInstructions2());
        System.out.println("passwor table values" + observable.getTbmInstructions2());
        tblLockCharges.setModel(observable.getTbmLockCharges());
    }

    private void setFieldNames() {
        txtPasCustId.setName("txtPasCustId");
        txtPassword.setName("txtPassword");
        txtConPassword.setName("txtConPassword");

        cboChargeType.setName("cboChargeType");
        tdtFromDt.setName("tdtFromDt");
        tdtToDt.setName("tdtToDt");
        txtAmt.setName("txtAmt");
        txtServiceTax.setName("txtServiceTax");
        txtLockerNo.setName("txtLockerNo");
        lblLockerNo.setName("lblLockerNo");

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
        tblInstruction2.setName("tblInstruction2");
        tblLockCharges.setName("tblLockCharges");
        btnAdd.setName("btnAdd");
        //        btnAddNO.setName("btnAddNO");
        btnAuthorize.setName("btnAuthorize");
        //         btnBranchCode.setName("btnBranchCode");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustomerIdAI.setName("btnCustomerIdAI");
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
        cboCategory.setName("cboCategory");
        cboBaseCurrAI.setName("cboBaseCurrAI");
        cboConstitutionAI.setName("cboConstitutionAI");
        //         cboDMYAD.setName("cboDMYAD");
        //        cboDocTypeITP3.setName("cboDocTypeITP3");
        //  cboGroupCodeAI.setName("cboGroupCodeAI");
        //        cboIdentityTypeITP4.setName("cboIdentityTypeITP4");
        cboCommAddr.setName("cboCommAddr");
        //        cboNomineeRelationNO.setName("cboNomineeRelationNO");
        cboOpModeAI.setName("cboOpModeAI");
        //         cboPreviousActNo.setName("cboPreviousActNo");
        cboProductIdAI.setName("cboProductIdAI");
        //        cboRelationNO.setName("cboRelationNO");
        //  cboSettlementModeAI.setName("cboSettlementModeAI");
        //         cboStmtFreqAD.setName("cboStmtFreqAD");
        //         chkABBChrgAD.setName("chkABBChrgAD");
        //         chkATMAD.setName("chkATMAD");
        //         chkChequeBookAD.setName("chkChequeBookAD");
        //         chkChequeRetChrgAD.setName("chkChequeRetChrgAD");
        //         chkCreditAD.setName("chkCreditAD");
        //         chkCustGrpLimitValidationAD.setName("chkCustGrpLimitValidationAD");
        //         chkDebitAD.setName("chkDebitAD");
        //         chkFlexiAD.setName("chkFlexiAD");
        //         chkInopChrgAD.setName("chkInopChrgAD");
        //         chkMobileBankingAD.setName("chkMobileBankingAD");
        //         chkNPAChrgAD.setName("chkNPAChrgAD");
        //         chkNROStatusAD.setName("chkNROStatusAD");
        //         chkNonMainMinBalChrgAD.setName("chkNonMainMinBalChrgAD");
        //         chkPayIntOnCrBalIN.setName("chkPayIntOnCrBalIN");
        //         chkPayIntOnDrBalIN.setName("chkPayIntOnDrBalIN");
        //         chkStmtChrgAD.setName("chkStmtChrgAD");
        //         chkPassBook.setName("chkPassBook");
        //         chkStopPmtChrgAD.setName("chkStopPmtChrgAD");
        //         dtdATMFromDateAD.setName("dtdATMFromDateAD");
        //         dtdATMToDateAD.setName("dtdATMToDateAD");
        //         dtdActOpenDateAI.setName("dtdActOpenDateAI");
        //         dtdCredit.setName("dtdCredit");
        //         dtdCreditFromDateAD.setName("dtdCreditFromDateAD");
        //         dtdCreditToDateAD.setName("dtdCreditToDateAD");
        //         dtdDebit.setName("dtdDebit");
        //         dtdDebitFromDateAD.setName("dtdDebitFromDateAD");
        //         dtdDebitToDateAD.setName("dtdDebitToDateAD");
        //        dtdExpiryDateITP3.setName("dtdExpiryDateITP3");
        //        dtdIssuedDateITP3.setName("dtdIssuedDateITP3");
        //        dtdMinorDOBNO.setName("dtdMinorDOBNO");
        //         dtdNPAChrgAD.setName("dtdNPAChrgAD");
        dtdOpeningDateAI.setName("dtdOpeningDateAI");
        //        dtdPOAFromDatePA.setName("dtdPOAFromDatePA");
        //        dtdPOAToDatePA.setName("dtdPOAToDatePA");
        //         lblABB.setName("lblABB");
        //         lblABBChrgAD.setName("lblABBChrgAD");
        //         lblATMFromDateAD.setName("lblATMFromDateAD");
        //         lblATMNoAD.setName("lblATMNoAD");
        //         lblATMToDateAD.setName("lblATMToDateAD");
        //         lblAccCloseChrgAD.setName("lblAccCloseChrgAD");
        //         lblAccOpeningChrgAD.setName("lblAccOpeningChrgAD");
        lblAccountNo.setName("lblAccountNo");
        //        lblAccountNoITP1.setName("lblAccountNoITP1");
        //        lblAccountNoITP2.setName("lblAccountNoITP2");
        // lblAccountNumber.setName("lblAccountNumber");
//        lblActHeadAI.setName("lblActHeadAI");
        //        lblActHeadITP1.setName("lblActHeadITP1");
//        lblActHeadValueAI.setName("lblActHeadValueAI");
        //        lblActHeadValueITP1.setName("lblActHeadValueITP1");
        //         lblActOpenDateAI.setName("lblActOpenDateAI");
        lblCategory.setName("lblCategory");
        //         lblAgClearingIN.setName("lblAgClearingIN");
        //         lblAgClearingValueIN.setName("lblAgClearingValueIN");
        //         lblAmoutTransAI.setName("lblAmoutTransAI");
        //        lblBankITP2.setName("lblBankITP2");
        lblBaseCurrAI.setName("lblBaseCurrAI");
        //         lblBranchCodeAI.setName("lblBranchCodeAI");
        //        lblBranchCodeITP1.setName("lblBranchCodeITP1");
        //        lblBranchCodeValueITP1.setName("lblBranchCodeValueITP1");
        //        lblBranchITP1.setName("lblBranchITP1");
        //        lblBranchITP2.setName("lblBranchITP2");
        //         lblBranchNameAI.setName("lblBranchNameAI");
        //         lblBranchNameValueAI.setName("lblBranchNameValueAI");
        //        lblBranchValueITP1.setName("lblBranchValueITP1");
        //         lblChequeBookChrgAD.setName("lblChequeBookChrgAD");
        //         lblChequeReturn.setName("lblChequeReturn");
        //         lblCollectInoperative.setName("lblCollectInoperative");
        lblConstitutionAI.setName("lblConstitutionAI");
        //         lblCrInterestRateIN.setName("lblCrInterestRateIN");
        //         lblCrInterestRateValueIN.setName("lblCrInterestRateValueIN");
        //         lblCredit.setName("lblCredit");
        //         lblCreditFromDateAD.setName("lblCreditFromDateAD");
        //         lblCreditNoAD.setName("lblCreditNoAD");
        //         lblCreditToDateAD.setName("lblCreditToDateAD");
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
        //         lblDebit.setName("lblDebit");
        //         lblDebitFromDateAD.setName("lblDebitFromDateAD");
        //         lblDebitNoAD.setName("lblDebitNoAD");
        //         lblDebitToDateAD.setName("lblDebitToDateAD");
        //        lblDesignationOTP5.setName("lblDesignationOTP5");
        //        lblDocNoITP3.setName("lblDocNoITP3");
        //        lblDocTypeITP3.setName("lblDocTypeITP3");
        //         lblDrInterestRateIN.setName("lblDrInterestRateIN");
        //         lblDrInterestRateValueIN.setName("lblDrInterestRateValueIN");
        //         lblExcessWithChrgAD.setName("lblExcessWithChrgAD");
        //        lblExpiryDateITP3.setName("lblExpiryDateITP3");
        //         lblFolioChrgAD.setName("lblFolioChrgAD");
        // lblGroupCodeAI.setName("lblGroupCodeAI");
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
        //         lblMinActBalanceAD.setName("lblMinActBalanceAD");
        //         lblMinBal1FlexiAD.setName("lblMinBal1FlexiAD");
        //         lblMinBal2FlexiAD.setName("lblMinBal2FlexiAD");
        lblCustName.setName("lblCustName");
        //        lblMinNominees.setName("lblMinNominees");
        //        lblMinorDOBNO.setName("lblMinorDOBNO");
        //         lblMisServiceChrgAD.setName("lblMisServiceChrgAD");
        lblMsg.setName("lblMsg");
        //         lblNPA.setName("lblNPA");
        //         lblNPAChrgAD.setName("lblNPAChrgAD");
        //        lblNameITP1.setName("lblNameITP1");
        //        lblNameITP2.setName("lblNameITP2");
        //        lblNameValueITP1.setName("lblNameValueITP1");
        //        lblNomineeNameNO.setName("lblNomineeNameNO");
        //        lblNomineePhoneNO.setName("lblNomineePhoneNO");
        //        lblNomineeRelationNO.setName("lblNomineeRelationNO");
        //        lblNomineeShareNO.setName("lblNomineeShareNO");
        //        lblNomineeStatusNO.setName("lblNomineeStatusNO");
        //         lblNonMaintenance.setName("lblNonMaintenance");
        // lblODLimitAI.setName("lblODLimitAI");
        lblOpModeAI.setName("lblOpModeAI");
        lblOpeningDateAI.setName("lblOpeningDateAI");
        //        lblPOAFromDatePA.setName("lblPOAFromDatePA");
        //        lblPOANamePA.setName("lblPOANamePA");
        //        lblPOAPhonePA.setName("lblPOAPhonePA");
        //        lblPOAToDatePA.setName("lblPOAToDatePA");
        //         lblPenalInterestRateIN.setName("lblPenalInterestRateIN");
        //         lblPenalInterestValueIN.setName("lblPenalInterestValueIN");
        //        lblPhoneOTP5.setName("lblPhoneOTP5");
        //         lblPrevActNoAI.setName("lblPrevActNoAI");
        lblPrevActNumAI.setName("lblPrevActNumAI");
        lblProductIdAI.setName("lblProductIdAI");
        //         lblRateCodeIN.setName("lblRateCodeIN");
        //         lblRateCodeValueIN.setName("lblRateCodeValueIN");
        //        lblRelationNO.setName("lblRelationNO");
        //         lblRemarksAI.setName("lblRemarksAI");
        //        lblRemarksPA.setName("lblRemarksPA");
        //         lblReqFlexiPeriodAD.setName("lblReqFlexiPeriodAD");
        //   lblSettlementModeAI.setName("lblSettlementModeAI");
        lblSpace1.setName("lblSpace1");
        //         lblStatement.setName("lblStatement");
        //         lblPassBook.setName("lblPassBook");
        lblStatus.setName("lblStatus");
        //         lblStmtFreqAD.setName("lblStmtFreqAD");
        //         lblStopPayment.setName("lblStopPayment");
        lblTBSep3.setName("lblTBSep3");
        //        lblTotalShareNO.setName("lblTotalShareNO");
        //         panAccountDetails.setName("panAccountDetails");
        panAccountInfo.setName("panAccountInfo");
        panAccounts.setName("panAccounts");
        panActInfo.setName("panActInfo");
        //        panAddEditRemovePA.setName("panAddEditRemovePA");
        //        panBank.setName("panBank");
        //         panBranch.setName("panBranch");
        //         panCardInfo.setName("panCardInfo");
        panCustomerInfo.setName("panCustomerInfo");
        panCustOperation.setName("panCustOperation");
        srpAct_Joint.setName("srpAct_Joint");
        tblAct_Joint.setName("tblAct_Joint");
        panCustDet.setName("panCustDet");
        //         panDiffCharges.setName("panDiffCharges");
        //        panDocDetails.setName("panDocDetails");
        //         panFlexiOpt.setName("panFlexiOpt");
        //        panGuardianDetails.setName("panGuardianDetails");
        //        panIdentity.setName("panIdentity");
        //         panInterestPayableIN.setName("panInterestPayableIN");
        //        panIntroducer.setName("panIntroducer");
        //        panIntroducerDetails.setName("panIntroducerDetails");
        //        panIntroducerType.setName("panIntroducerType");
        //         panIsRequired.setName("panIsRequired");
        //         panLastIntApp.setName("panLastIntApp");
        //        panNominee.setName("panNominee");
        //        panNomineeDetails.setName("panNomineeDetails");
        //        panNomineeInfo.setName("panNomineeInfo");
        //        panNomineeList.setName("panNomineeList");
        //        panNomineeStatus.setName("panNomineeStatus");
        //        panOthers.setName("panOthers");
        //        panPOA.setName("panPOA");
        //        panPOADetails.setName("panPOADetails");
        //        panPOAList.setName("panPOAList");
        //         panRatesIN.setName("panRatesIN");
        //        panSelfCustomer.setName("panSelfCustomer");
        //        panShareAddEditRemoveNO.setName("panShareAddEditRemoveNO");
        panStatus.setName("panStatus");
        //         panTransferBranchInfo.setName("panTransferBranchInfo");
        //         panTransfered.setName("panTransfered");

        //        phoneGPanelNO.setName("phoneGPanelNO");
        //        phoneNPanelNO.setName("phoneNPanelNO");
        //        phoneNPanelOTP5.setName("phoneNPanelOTP5");
        //        phoneNPanelPA.setName("phoneNPanelPA");
        //        rdoStatus_MajorNO.setName("rdoStatus_MajorNO");
        //        rdoStatus_MinorNO.setName("rdoStatus_MinorNO");
        //        scrpPOAListPA.setName("scrpPOAListPA");
        sepSepAI.setName("sepSepAI");
        //        sepSepNOP1.setName("sepSepNOP1");
        //        sepSepNOP2.setName("sepSepNOP2");
        //        sepSepNOP4.setName("sepSepNOP4");
        //        sepSepOTP5.setName("sepSepOTP5");
        //        sepSepPA.setName("sepSepPA");
        //        srpListNomineeNO.setName("srpListNomineeNO");
        tabAccounts.setName("tabAccounts");
        //        tblListNominee.setName("tblListNominee");
        //        tblPOAListPA.setName("tblPOAListPA");
        //         txtABBChrgAD.setName("txtABBChrgAD");
        //        txtACodeOTP5.setName("txtACodeOTP5");
        //         txtATMNoAD.setName("txtATMNoAD");
        //         txtAccCloseChrgAD.setName("txtAccCloseChrgAD");
        //         txtAccOpeningChrgAD.setName("txtAccOpeningChrgAD");
        //        txtAccountNoITP1.setName("txtAccountNoITP1");
        //        txtAccountNoITP2.setName("txtAccountNoITP2");
        //         txtAmoutTransAI.setName("txtAmoutTransAI");
        //        txtBankITP2.setName("txtBankITP2");
        //         txtBranchCodeAI.setName("txtBranchCodeAI");
        //        txtBranchITP2.setName("txtBranchITP2");
        //         txtChequeBookChrgAD.setName("txtChequeBookChrgAD");
        //         txtCreditNoAD.setName("txtCreditNoAD");
        txtCustomerIdAI.setName("txtCustomerIdAI");
        //         txtDebitNoAD.setName("txtDebitNoAD");
        //        txtDesignationOTP5.setName("txtDesignationOTP5");
        //        txtDocNoITP3.setName("txtDocNoITP3");
        //         txtExcessWithChrgAD.setName("txtExcessWithChrgAD");
        //         txtFolioChrgAD.setName("txtFolioChrgAD");
        //        txtGuardianACodeNO.setName("txtGuardianACodeNO");
        //        txtGuardianNameNO.setName("txtGuardianNameNO");
        //        txtGuardianPhoneNO.setName("txtGuardianPhoneNO");
        //        txtIdITP4.setName("txtIdITP4");
        //        txtIntroNameOTP5.setName("txtIntroNameOTP5");
        //        txtIssuedAuthITP4.setName("txtIssuedAuthITP4");
        //        txtIssuedByITP3.setName("txtIssuedByITP3");
        //         txtMinActBalanceAD.setName("txtMinActBalanceAD");
        //         txtMinBal1FlexiAD.setName("txtMinBal1FlexiAD");
        //         txtMinBal2FlexiAD.setName("txtMinBal2FlexiAD");
        //         //        txtMinNominees.setName("txtMinNominees");
        //         txtMisServiceChrgAD.setName("txtMisServiceChrgAD");
        //        txtNameITP2.setName("txtNameITP2");
        //        txtNomineeACodeNO.setName("txtNomineeACodeNO");
        //        txtNomineeNameNO.setName("txtNomineeNameNO");
        //        txtNomineePhoneNO.setName("txtNomineePhoneNO");
        //        txtNomineeShareNO.setName("txtNomineeShareNO");
        // txtODLimitAI.setName("txtODLimitAI");
        //        txtPOAACodePA.setName("txtPOAACodePA");
        //        txtPOANamePA.setName("txtPOANamePA");
        //        txtPOAPhonePA.setName("txtPOAPhonePA");
        //        txtPhoneOTP5.setName("txtPhoneOTP5");
        txtPrevActNumAI.setName("txtPrevActNumAI");
        //         txtRemarksAI.setName("txtRemarksAI");
        //        txtRemarksPA.setName("txtRemarksPA");
        //         txtReqFlexiPeriodAD.setName("txtReqFlexiPeriodAD");
        //        txtTotalShareNO.setName("txtTotalShareNO");

        lblActName.setName("lblActName");
        txtActName.setName("txtActName");
        lblRemarks.setName("lblRemarks");
        txtRemarks.setName("txtRemarks");

        //         lblHideBalanceTrans.setName("lblHideBalanceTrans");
        //         chkHideBalanceTrans.setName("chkHideBalanceTrans");
        //         lblRoleHierarchy.setName("lblRoleHierarchy");
        //         cboRoleHierarchy.setName("cboRoleHierarchy");


    }

    private void internationalize() {
        //         lblPenalInterestValueIN.setText(resourceBundle.getString("lblPenalInterestValueIN"));
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
        //         chkNonMainMinBalChrgAD.setText(resourceBundle.getString("chkNonMainMinBalChrgAD"));
        //         lblAgClearingIN.setText(resourceBundle.getString("lblAgClearingIN"));
        //         btnCancel.setText(resourceBundle.getString("btnCancel"));
        //         lblCrInterestRateIN.setText(resourceBundle.getString("lblCrInterestRateIN"));
        //         lblFolioChrgAD.setText(resourceBundle.getString("lblFolioChrgAD"));
        //        lblDesignationOTP5.setText(resourceBundle.getString("lblDesignationOTP5"));
        //         lblMinActBalanceAD.setText(resourceBundle.getString("lblMinActBalanceAD"));
        //         //        ((javax.swing.border.TitledBorder)panDocDetails.getBorder()).setTitle(resourceBundle.getString("panDocDetails"));
        //         ((javax.swing.border.TitledBorder)panLastIntApp.getBorder()).setTitle(resourceBundle.getString("panLastIntApp"));
        //        btnNewNO.setText(resourceBundle.getString("btnNewNO"));
        //lblArea.setText(resourceBundle.getString("lblArea"));
        //        lblPhoneOTP5.setText(resourceBundle.getString("lblPhoneOTP5"));
        lblBaseCurrAI.setText(resourceBundle.getString("lblBaseCurrAI"));
        //        lblBankITP2.setText(resourceBundle.getString("lblBankITP2"));
        //         lblPenalInterestRateIN.setText(resourceBundle.getString("lblPenalInterestRateIN"));
        //        lblNameValueITP1.setText(resourceBundle.getString("lblNameValueITP1"));
        //lblStreet.setText(resourceBundle.getString("lblStreet"));
        //         lblStatement.setText(resourceBundle.getString("lblStatement"));
        //         lblPassBook.setText(resourceBundle.getString("lblPassBook"));
        lblTBSep3.setText(resourceBundle.getString("lblTBSep3"));
        lblCommAddr.setText(resourceBundle.getString("lblCommAddr"));
        //        lblNomineeRelationNO.setText(resourceBundle.getString("lblNomineeRelationNO"));
        lblCustomerIdAI.setText(resourceBundle.getString("lblCustomerIdAI"));
        //         chkChequeRetChrgAD.setText(resourceBundle.getString("chkChequeRetChrgAD"));
        //         lblDebit.setText(resourceBundle.getString("lblDebit"));
        btnException.setText(resourceBundle.getString("btnException"));
        //         lblMinBal2FlexiAD.setText(resourceBundle.getString("lblMinBal2FlexiAD"));
        //        lblTotalShareNO.setText(resourceBundle.getString("lblTotalShareNO"));
        //         lblATMToDateAD.setText(resourceBundle.getString("lblATMToDateAD"));
        //         lblCredit.setText(resourceBundle.getString("lblCredit"));
        //        lblIntroducerTypeIT.setText(resourceBundle.getString("lblIntroducerTypeIT"));
        //         lblNPA.setText(resourceBundle.getString("lblNPA"));
        //         lblATMNoAD.setText(resourceBundle.getString("lblATMNoAD"));
        //        lblBranchITP2.setText(resourceBundle.getString("lblBranchITP2"));
        //         lblRemarksAI.setText(resourceBundle.getString("lblRemarksAI"));
        //lblCity.setText(resourceBundle.getString("lblCity"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        //         ((javax.swing.border.TitledBorder)panFlexiOpt.getBorder()).setTitle(resourceBundle.getString("panFlexiOpt"));
        // lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        //        lblIdentityTypeITP4.setText(resourceBundle.getString("lblIdentityTypeITP4"));
        //        lblBranchCodeValueITP1.setText(resourceBundle.getString("lblBranchCodeValueITP1"));
        lblOpeningDateAI.setText(resourceBundle.getString("lblOpeningDateAI"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        //         chkABBChrgAD.setText(resourceBundle.getString("chkABBChrgAD"));
        //         chkATMAD.setText(resourceBundle.getString("chkATMAD"));
        //         chkNPAChrgAD.setText(resourceBundle.getString("chkNPAChrgAD"));
        //        lblPOAFromDatePA.setText(resourceBundle.getString("lblPOAFromDatePA"));
        //        ((javax.swing.border.TitledBorder)panNomineeList.getBorder()).setTitle(resourceBundle.getString("panNomineeList"));
        btnReport.setText(resourceBundle.getString("btnReport"));
        //        ((javax.swing.border.TitledBorder)panNomineeDetails.getBorder()).setTitle(resourceBundle.getString("panNomineeDetails"));
        //         chkFlexiAD.setText(resourceBundle.getString("chkFlexiAD"));
        //        lblNameITP1.setText(resourceBundle.getString("lblNameITP1"));
        //         btnBranchCode.setText(resourceBundle.getString("btnBranchCode"));
        //         chkStmtChrgAD.setText(resourceBundle.getString("chkStmtChrgAD"));
        //         chkPassBook.setText(resourceBundle.getString("chkPassBook"));
        //         //        rdoStatus_MajorNO.setText(resourceBundle.getString("rdoStatus_MajorNO"));
        //         lblABBChrgAD.setText(resourceBundle.getString("lblABBChrgAD"));
        //        lblNomineeNameNO.setText(resourceBundle.getString("lblNomineeNameNO"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //         lblNPAChrgAD.setText(resourceBundle.getString("lblNPAChrgAD"));
        //        rdoStatus_MinorNO.setText(resourceBundle.getString("rdoStatus_MinorNO"));
        //        lblBranchValueITP1.setText(resourceBundle.getString("lblBranchValueITP1"));
        //         lblAmoutTransAI.setText(resourceBundle.getString("lblAmoutTransAI"));
        //         chkCreditAD.setText(resourceBundle.getString("chkCreditAD"));
        //         lblDebitToDateAD.setText(resourceBundle.getString("lblDebitToDateAD"));
        //   lblGroupCodeAI.setText(resourceBundle.getString("lblGroupCodeAI"));
        ((javax.swing.border.TitledBorder) panAccountInfo.getBorder()).setTitle(resourceBundle.getString("panAccountInfo"));
        //        lblIssuedDateITP3.setText(resourceBundle.getString("lblIssuedDateITP3"));
        //         lblChequeBookChrgAD.setText(resourceBundle.getString("lblChequeBookChrgAD"));
        //         //        ((javax.swing.border.TitledBorder)panPOADetails.getBorder()).setTitle(resourceBundle.getString("panPOADetails"));
        //         lblCreditToDateAD.setText(resourceBundle.getString("lblCreditToDateAD"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        //         lblDebitNoAD.setText(resourceBundle.getString("lblDebitNoAD"));
        //lblODLimitAI.setText(resourceBundle.getString("lblODLimitAI"));
        //         chkChequeBookAD.setText(resourceBundle.getString("chkChequeBookAD"));
        //         lblRateCodeValueIN.setText(resourceBundle.getString("lblRateCodeValueIN"));
        //         //        lblRelationNO.setText(resourceBundle.getString("lblRelationNO"));
        //         lblDebitFromDateAD.setText(resourceBundle.getString("lblDebitFromDateAD"));
        //         lblExcessWithChrgAD.setText(resourceBundle.getString("lblExcessWithChrgAD"));
        //         chkCustGrpLimitValidationAD.setText(resourceBundle.getString("chkCustGrpLimitValidationAD"));
        //        lblIntroNameOTP5.setText(resourceBundle.getString("lblIntroNameOTP5"));
        //         lblBranchNameAI.setText(resourceBundle.getString("lblBranchNameAI"));
        //        lblPOANamePA.setText(resourceBundle.getString("lblPOANamePA"));
        //        lblIssuedByITP3.setText(resourceBundle.getString("lblIssuedByITP3"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //        lblAccountNoITP2.setText(resourceBundle.getString("lblAccountNoITP2"));
        //         lblAgClearingValueIN.setText(resourceBundle.getString("lblAgClearingValueIN"));
        //         ((javax.swing.border.TitledBorder)panInterestPayableIN.getBorder()).setTitle(resourceBundle.getString("panInterestPayableIN"));
        //         lblCollectInoperative.setText(resourceBundle.getString("lblCollectInoperative"));
        btnCustomerIdAI.setText(resourceBundle.getString("btnCustomerIdAI"));
        lblProductIdAI.setText(resourceBundle.getString("lblProductIdAI"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        //         chkPayIntOnCrBalIN.setText(resourceBundle.getString("chkPayIntOnCrBalIN"));
        //         //        lblNomineePhoneNO.setText(resourceBundle.getString("lblNomineePhoneNO"));
        //         lblMinBal1FlexiAD.setText(resourceBundle.getString("lblMinBal1FlexiAD"));
        //        ((javax.swing.border.TitledBorder)panIntroducerDetails.getBorder()).setTitle(resourceBundle.getString("panIntroducerDetails"));
        //        btnRemovePA.setText(resourceBundle.getString("btnRemovePA"));
        //WARNING_NOMINEE.setText(resourceBundle.getString("WARNING_NOMINEE"));
        //         lblBranchCodeAI.setText(resourceBundle.getString("lblBranchCodeAI"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        //lblPincode.setText(resourceBundle.getString("lblPincode"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        // lblSettlementModeAI.setText(resourceBundle.getString("lblSettlementModeAI"));
        ((javax.swing.border.TitledBorder) panCustomerInfo.getBorder()).setTitle(resourceBundle.getString("panCustomerInfo"));
        //         ((javax.swing.border.TitledBorder)panIsRequired.getBorder()).setTitle(resourceBundle.getString("panIsRequired"));
        lblPrevActNumAI.setText(resourceBundle.getString("lblPrevActNumAI"));
        //        ((javax.swing.border.TitledBorder)panOthers.getBorder()).setTitle(resourceBundle.getString("panOthers"));
        //         lblActOpenDateAI.setText(resourceBundle.getString("lblActOpenDateAI"));

        //         lblNonMaintenance.setText(resourceBundle.getString("lblNonMaintenance"));
        //         chkNROStatusAD.setText(resourceBundle.getString("chkNROStatusAD"));
        //         lblStmtFreqAD.setText(resourceBundle.getString("lblStmtFreqAD"));
        //        lblMinorDOBNO.setText(resourceBundle.getString("lblMinorDOBNO"));
        //        btnListAddPA.setText(resourceBundle.getString("btnListAddPA"));
        //         lblDrInterestRateIN.setText(resourceBundle.getString("lblDrInterestRateIN"));
        //         //        lblGuardianNameNO.setText(resourceBundle.getString("lblGuardianNameNO"));
        //         //        lblDocNoITP3.setText(resourceBundle.getString("lblDocNoITP3"));
        //         //        lblBranchCodeITP1.setText(resourceBundle.getString("lblBranchCodeITP1"));
        //         lblMisServiceChrgAD.setText(resourceBundle.getString("lblMisServiceChrgAD"));
        //         lblABB.setText(resourceBundle.getString("lblABB"));
        //         lblAccOpeningChrgAD.setText(resourceBundle.getString("lblAccOpeningChrgAD"));
        //         lblCreditFromDateAD.setText(resourceBundle.getString("lblCreditFromDateAD"));
        //         //        lblDocTypeITP3.setText(resourceBundle.getString("lblDocTypeITP3"));
        //         //        lblActHeadITP1.setText(resourceBundle.getString("lblActHeadITP1"));
        //         lblDrInterestRateValueIN.setText(resourceBundle.getString("lblDrInterestRateValueIN"));
        //        lblGuardianPhoneNO.setText(resourceBundle.getString("lblGuardianPhoneNO"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        //         lblCrInterestRateValueIN.setText(resourceBundle.getString("lblCrInterestRateValueIN"));
        //         //        lblPOAToDatePA.setText(resourceBundle.getString("lblPOAToDatePA"));
        //         //        lblBranchITP1.setText(resourceBundle.getString("lblBranchITP1"));
        //         chkMobileBankingAD.setText(resourceBundle.getString("chkMobileBankingAD"));
        //         //        btnRemoveNO.setText(resourceBundle.getString("btnRemoveNO"));
        //         //        lblIdITP4.setText(resourceBundle.getString("lblIdITP4"));
        //         lblATMFromDateAD.setText(resourceBundle.getString("lblATMFromDateAD"));
//        lblActHeadValueAI.setText(resourceBundle.getString("lblActHeadValueAI"));
        //        lblCustomerIdITP1.setText(resourceBundle.getString("lblCustomerIdITP1"));
        lblConstitutionAI.setText(resourceBundle.getString("lblConstitutionAI"));
        //         ((javax.swing.border.TitledBorder)panTransferBranchInfo.getBorder()).setTitle(resourceBundle.getString("panTransferBranchInfo"));
        //         chkPayIntOnDrBalIN.setText(resourceBundle.getString("chkPayIntOnDrBalIN"));
        //         lblBranchNameValueAI.setText(resourceBundle.getString("lblBranchNameValueAI"));
        //SHARE_NOMINEE.setText(resourceBundle.getString("SHARE_NOMINEE"));
        //        lblNameITP2.setText(resourceBundle.getString("lblNameITP2"));
        //         lblReqFlexiPeriodAD.setText(resourceBundle.getString("lblReqFlexiPeriodAD"));
        //         lblAccCloseChrgAD.setText(resourceBundle.getString("lblAccCloseChrgAD"));
        //         chkInopChrgAD.setText(resourceBundle.getString("chkInopChrgAD"));
        //         lblRateCodeIN.setText(resourceBundle.getString("lblRateCodeIN"));
        //        btnAddNO.setText(resourceBundle.getString("btnAddNO"));
        //        lblExpiryDateITP3.setText(resourceBundle.getString("lblExpiryDateITP3"));
        //        lblNomineeShareNO.setText(resourceBundle.getString("lblNomineeShareNO"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
//        lblActHeadAI.setText(resourceBundle.getString("lblActHeadAI"));
        //        ((javax.swing.border.TitledBorder)panPOAList.getBorder()).setTitle(resourceBundle.getString("panPOAList"));
        lblOpModeAI.setText(resourceBundle.getString("lblOpModeAI"));
        //        lblActHeadValueITP1.setText(resourceBundle.getString("lblActHeadValueITP1"));
        //        btnAccountNoITP1.setText(resourceBundle.getString("btnAccountNoITP1"));
        //        lblMinNominees.setText(resourceBundle.getString("lblMinNominees"));
        //         ((javax.swing.border.TitledBorder)panDiffCharges.getBorder()).setTitle(resourceBundle.getString("panDiffCharges"));
        //         ((javax.swing.border.TitledBorder)panCardInfo.getBorder()).setTitle(resourceBundle.getString("panCardInfo"));
        //         ((javax.swing.border.TitledBorder)panRatesIN.getBorder()).setTitle(resourceBundle.getString("panRatesIN"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        //        btnNewPA.setText(resourceBundle.getString("btnNewPA"));
        //        ((javax.swing.border.TitledBorder)panGuardianDetails.getBorder()).setTitle(resourceBundle.getString("panGuardianDetails"));
        //         lblCreditNoAD.setText(resourceBundle.getString("lblCreditNoAD"));
        //            lblPOAPhonePA.setText(resourceBundle.getString("lblPOAPhonePA"));
        //         lblPrevActNoAI.setText(resourceBundle.getString("lblPrevActNoAI"));
        //        lblCustomerIdValueITP1.setText(resourceBundle.getString("lblCustomerIdValueITP1"));
        //         lblStopPayment.setText(resourceBundle.getString("lblStopPayment"));
        //        lblNomineeStatusNO.setText(resourceBundle.getString("lblNomineeStatusNO"));
        //        lblRemarksPA.setText(resourceBundle.getString("lblRemarksPA"));
        //         chkStopPmtChrgAD.setText(resourceBundle.getString("chkStopPmtChrgAD"));
        //         chkDebitAD.setText(resourceBundle.getString("chkDebitAD"));
        //        lblIssuedByITP4.setText(resourceBundle.getString("lblIssuedByITP4"));
        //        ((javax.swing.border.TitledBorder)panSelfCustomer.getBorder()).setTitle(resourceBundle.getString("panSelfCustomer"));
        //         lblChequeReturn.setText(resourceBundle.getString("lblChequeReturn"));
        lblCustName.setText(resourceBundle.getString("lblCustName"));
        //lblCountry.setText(resourceBundle.getString("lblCountry"));
        lblActName.setText(resourceBundle.getString("lblActName"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));


        //         lblHideBalanceTrans.setText(resourceBundle.getString("lblHideBalanceTrans"));
        //         chkHideBalanceTrans.setText(resourceBundle.getString("chkHideBalanceTrans"));
        //         lblRoleHierarchy.setText(resourceBundle.getString("lblRoleHierarchy"));
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
        mandatoryMap.put("cboConstitutionAI", new Boolean(true));
        mandatoryMap.put("cboOpModeAI", new Boolean(true));
        mandatoryMap.put("dtdExpiryDate", new Boolean(true));
        mandatoryMap.put("cboCommAddr", new Boolean(true));
        //  mandatoryMap.put("txtODLimitAI", new Boolean(true));
        // mandatoryMap.put("cboGroupCodeAI", new Boolean(true));
        //mandatoryMap.put("cboSettlementModeAI", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        //        mandatoryMap.put("cboBaseCurrAI", new Boolean(true));
        mandatoryMap.put("txtPrevActNumAI", new Boolean(false));
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
        observable.setScreen(this.getScreen()); // 23-08-2016
        // For Account Info
        observable.setTxtPrevActNumAI(txtPrevActNumAI.getText());
        observable.setTxtLockerNo(txtLockerNo.getText());
        observable.setLblLockerKeyNoVal(lblLockerKeyNoVal.getText());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setCboProductIdAI((String) ((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
        observable.setTxtCustomerIdAI(txtCustomerIdAI.getText());
        observable.setCollectRentMM(txtCollectRentMM.getText());
        observable.setCollectRentYYYY(txtCollectRentyyyy.getText());
        if (rbtnSiYes.isSelected() == true) {
            observable.setSiYes(rbtnSiYes.isSelected());
            String gl = "General Ledger";

            //observable.setCboProdId()

            observable.setCboProdType((String) ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            // if()
            if (cboProdType.getSelectedItem().equals(gl)) {
                observable.setCboProductId("");

                // observable.setCboProductId((String) ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                observable.setTxtCustomerIdCr(txtCustomerIdCr.getText());
                observable.setLblCustomerNameCrValue(lblCustomerNameCrValue.getText());
            } else {
                observable.setCboProductId((String) ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                observable.setTxtCustomerIdCr(txtCustomerIdCr.getText());
                observable.setLblCustomerNameCrValue(lblCustomerNameCrValue.getText());
            }
        } else {
            observable.setSiNo(rbtnSiNo.isSelected());

            observable.setCboProdType("");
            observable.setCboProductId("");
            observable.setTxtCustomerIdCr("");
            observable.setLblCustomerNameCrValue("");
        }

        observable.setPwdYes(rbtnPwdYes.isSelected());
        observable.setPwdNo(rbtnPwdNo.isSelected());

        //observable.setCboProdId((String) ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());





        // observable.set
        //      else if(rbtnSiNo.isSelected()==true)
        //   {
        //    observable.setCboProductId("");
        // observable.setn("");
        // observable.setTxtCustomerIdCr("");
        // observable.setLblCustomerNameCrValue("");

        // }
        // observable.setPwdYes(rbtnPwdYes.getText());
        //   observable.setPwdNo(rbtnPwdNo.getText());
        observable.setDtdOpeningDateAI(dtdOpeningDateAI.getDateValue());
        observable.setCboConstitutionAI((String) ((ComboBoxModel) cboConstitutionAI.getModel()).getKeyForSelected());
        observable.setCboOpModeAI((String) ((ComboBoxModel) cboOpModeAI.getModel()).getKeyForSelected());
        observable.setCboCommAddr((String) ((ComboBoxModel) cboCommAddr.getModel()).getKeyForSelected());
        //observable.setTxtODLimitAI(txtODLimitAI.getText());
        //  observable.setCboGroupCodeAI((String) ((ComboBoxModel) cboGroupCodeAI.getModel()).getKeyForSelected());
        // observable.setCboSettlementModeAI((String) ((ComboBoxModel) cboSettlementModeAI.getModel()).getKeyForSelected());
        observable.setCboCategory((String) ((ComboBoxModel) cboCategory.getModel()).getKeyForSelected());

        observable.setTxtActName(txtActName.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtCharges(txtCharges.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setDtdExpiryDate(dtdExpiryDate.getDateValue());
        observable.setRdoFreeze(rdoFreeze.isSelected());
        observable.setRdoUnFreeze(rdoUnFreeze.isSelected());
        observable.setTxtFreezeRemarks(txtFreezeRemarks.getText());
        observable.setDtdFreezeDt(dtdFreezeDt.getDateValue());
        
        if(chkNoTransaction.isSelected()){
            observable.setChkNoTransaction("Y");
        }else{
            observable.setChkNoTransaction("N");
        }
        observable.setServiceTax_Map(serviceTax_Map);// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening

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
        //         observable.setChkPayIntOnCrBalIN(chkPayIntOnCrBalIN.isSelected());
        //         observable.setChkPayIntOnDrBalIN(chkPayIntOnDrBalIN.isSelected());
        //
        //         // For Other Details
        //         observable.setChkChequeBookAD(chkChequeBookAD.isSelected());
        //         observable.setChkNROStatusAD(chkNROStatusAD.isSelected());
        //         observable.setChkMobileBankingAD(chkMobileBankingAD.isSelected());
        //         observable.setChkCustGrpLimitValidationAD(chkCustGrpLimitValidationAD.isSelected());
        //         // ATM Card...
        //         observable.setChkATMAD(chkATMAD.isSelected());
        //         observable.setTxtATMNoAD(txtATMNoAD.getText());
        //         observable.setDtdATMFromDateAD(dtdATMFromDateAD.getDateValue());
        //         observable.setDtdATMToDateAD(dtdATMToDateAD.getDateValue());
        //         // Credit Card...
        //         observable.setChkCreditAD(chkCreditAD.isSelected());
        //         observable.setTxtCreditNoAD(txtCreditNoAD.getText());
        //         observable.setDtdCreditFromDateAD(dtdCreditFromDateAD.getDateValue());
        //         observable.setDtdCreditToDateAD(dtdCreditToDateAD.getDateValue());
        //         // Debit Card...
        //         observable.setChkDebitAD(chkDebitAD.isSelected());
        //         observable.setTxtDebitNoAD(txtDebitNoAD.getText());
        //         observable.setDtdDebitFromDateAD(dtdDebitFromDateAD.getDateValue());
        //         observable.setDtdDebitToDateAD(dtdDebitToDateAD.getDateValue());
        //
        //         observable.setChkFlexiAD(chkFlexiAD.isSelected());
        //         observable.setTxtMinBal1FlexiAD(txtMinBal1FlexiAD.getText());
        //         observable.setTxtMinBal2FlexiAD(txtMinBal2FlexiAD.getText());
        //         observable.setTxtReqFlexiPeriodAD(txtReqFlexiPeriodAD.getText());
        //         observable.setCboDMYAD((String) ((ComboBoxModel) cboDMYAD.getModel()).getKeyForSelected());

        //         observable.setCboStmtFreqAD((String) ((ComboBoxModel) cboStmtFreqAD.getModel()).getKeyForSelected());
        //         observable.setChkStopPmtChrgAD(chkStopPmtChrgAD.isSelected());
        //         observable.setChkChequeRetChrgAD(chkChequeRetChrgAD.isSelected());
        //         observable.setChkInopChrgAD(chkInopChrgAD.isSelected());
        //         observable.setChkStmtChrgAD(chkStmtChrgAD.isSelected());
        //         observable.setChkPassBook(chkPassBook.isSelected());
        //
        //         observable.setTxtAccOpeningChrgAD(txtAccOpeningChrgAD.getText());
        //         observable.setTxtAccCloseChrgAD(txtAccCloseChrgAD.getText());
        //         observable.setTxtMisServiceChrgAD(txtMisServiceChrgAD.getText());
        //         observable.setTxtChequeBookChrgAD(txtChequeBookChrgAD.getText());
        //         observable.setTxtFolioChrgAD(txtFolioChrgAD.getText());
        //         observable.setTxtExcessWithChrgAD(txtExcessWithChrgAD.getText());
        //
        //         observable.setChkNonMainMinBalChrgAD(chkNonMainMinBalChrgAD.isSelected());
        //         observable.setTxtMinActBalanceAD(txtMinActBalanceAD.getText());
        //         observable.setChkABBChrgAD(chkABBChrgAD.isSelected());
        //         observable.setTxtABBChrgAD(txtABBChrgAD.getText());
        //         observable.setChkNPAChrgAD(chkNPAChrgAD.isSelected());
        //         observable.setDtdNPAChrgAD(dtdNPAChrgAD.getDateValue());
        //
        //         observable.setDtdDebit(dtdDebit.getDateValue());
        //         observable.setDtdCredit(dtdCredit.getDateValue());
        //
        //         observable.setChkHideBalanceTrans(chkHideBalanceTrans.isSelected());
        //         observable.setCboRoleHierarchy((String) ((ComboBoxModel) cboRoleHierarchy.getModel()).getKeyForSelected());

    }

    private void updateOBPassTab() {
        observable.setTxtPasCustId(txtPasCustId.getText());
        observable.setTxtPassword(txtPassword.getText());
        observable.setTxtConPassword(txtConPassword.getText());
        //int n= tblInstruction2.getSelectedRow();

        // for (int i=0;i<n;i++)
        //   {
        // if(txtPasCustId.getText()==tblInstruction2.getValueAt(i,0))
        //  {
        // tblInstruction2.setValueAt(txtPassword.getText(), i, 1);
        //tblInstruction2.setValueAt(txtConPassword.getText(), i, 2);
        //          }
        // }
    }


    private void updatePassTab() {
        System.out.println("here pwd" + observable.getTxtPassword());
        if (observable.getOperation() != ClientConstants.ACTIONTYPE_EDIT) {
            txtPasCustId.setText(observable.getTxtPasCustId());
            txtPassword.setText(observable.getTxtPassword());
            txtConPassword.setText(observable.getTxtConPassword());
        }
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT) {
            txtPasCustId.setText(observable.getTxtPasCustId());
        }
    }

    private void clearPassTab() {
        txtPasCustId.setText("");
        txtPassword.setText("");
        txtConPassword.setText("");
    }

    private void updateOBChrgsTab() {
        observable.setCboChargeType((String) ((ComboBoxModel) cboChargeType.getModel()).getKeyForSelected());
        observable.setTdtFromDt(tdtFromDt.getDateValue());
        observable.setTdtToDt(tdtToDt.getDateValue());
        observable.setTxtAmt(txtAmt.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
    }

    private void updateChrgsTab() {
        cboChargeType.setSelectedItem(((ComboBoxModel) cboChargeType.getModel()).getDataForKey(observable.getCboChargeType()));
        txtAmt.setText(observable.getTxtAmt());
        //         txtServiceTax.setText(observable.getTxtServiceTax());
        tdtFromDt.setDateValue(observable.getTdtFromDt());
        tdtToDt.setDateValue(observable.getTdtToDt());

    }

    private void clearChrgsTab() {
        cboChargeType.setSelectedItem("");
        txtAmt.setText("");
        //         txtServiceTax.setText("");
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
    }

    /* Auto Generated Method - setHelpMessage()
    This method shows tooltip help for all the input fields
    available in the UI. It needs the Mandatory Resource Bundle
    object. Help display Label name should be lblMsg. */

    public void setHelpMessage() {
        //        AccountsMRB objMandatoryRB = new AccountsMRB();
        //         txtBranchCodeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCodeAI"));
        //         cboPreviousActNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPreviousActNo"));
        //         dtdActOpenDateAI.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdActOpenDateAI"));
        //         txtAmoutTransAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmoutTransAI"));
        //         txtRemarksAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarksAI"));
        cboProductIdAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductIdAI"));
        txtCustomerIdAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerIdAI"));
        dtdOpeningDateAI.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdOpeningDateAI"));
        cboConstitutionAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConstitutionAI"));
        dtdExpiryDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdExpiryDate"));
        cboOpModeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOpModeAI"));
        cboCommAddr.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommAddr"));
        //  txtODLimitAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtODLimitAI"));
        //   cboGroupCodeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGroupCodeAI"));
        //cboSettlementModeAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSettlementModeAI"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
        cboBaseCurrAI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBaseCurrAI"));
        // txtPrevActNumAI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrevActNumAI"));

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
        //         chkChequeBookAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkChequeBookAD"));
        //         chkCustGrpLimitValidationAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCustGrpLimitValidationAD"));
        //         chkMobileBankingAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkMobileBankingAD"));
        //         chkNROStatusAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNROStatusAD"));
        //         chkATMAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkATMAD"));
        //         txtATMNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtATMNoAD"));
        //         dtdATMFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdATMFromDateAD"));
        //         dtdATMToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdATMToDateAD"));
        //         chkDebitAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitAD"));
        //         txtDebitNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitNoAD"));
        //         dtdDebitFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdDebitFromDateAD"));
        //         dtdDebitToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdDebitToDateAD"));
        //         chkCreditAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditAD"));
        //         txtCreditNoAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditNoAD"));
        //         dtdCreditFromDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdCreditFromDateAD"));
        //         dtdCreditToDateAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdCreditToDateAD"));
        //         chkFlexiAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkFlexiAD"));
        //         txtMinBal1FlexiAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBal1FlexiAD"));
        //         txtMinBal2FlexiAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBal2FlexiAD"));
        //         txtReqFlexiPeriodAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReqFlexiPeriodAD"));
        ////         cboDMYAD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDMYAD"));
        //         txtAccOpeningChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccOpeningChrgAD"));
        //         txtMisServiceChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMisServiceChrgAD"));
        //         chkStopPmtChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkStopPmtChrgAD"));
        //         txtChequeBookChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequeBookChrgAD"));
        //         chkChequeRetChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkChequeRetChrgAD"));
        //         txtFolioChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFolioChrgAD"));
        //         chkInopChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkInopChrgAD"));
        //         txtAccCloseChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccCloseChrgAD"));
        //         chkStmtChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkStmtChrgAD"));
        //         cboStmtFreqAD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStmtFreqAD"));
        //         chkNonMainMinBalChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNonMainMinBalChrgAD"));
        //         txtExcessWithChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExcessWithChrgAD"));
        //         chkABBChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkABBChrgAD"));
        //         chkNPAChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("chkNPAChrgAD"));
        //         txtABBChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtABBChrgAD"));
        //         dtdNPAChrgAD.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdNPAChrgAD"));
        //         txtMinActBalanceAD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinActBalanceAD"));
        //         dtdDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdDebit"));
        //         dtdCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdCredit"));
        //         chkPayIntOnCrBalIN.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPayIntOnCrBalIN"));
        //         chkPayIntOnDrBalIN.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPayIntOnDrBalIN"));
        //
        //         chkHideBalanceTrans.setHelpMessage(lblMsg, objMandatoryRB.getString("chkHideBalanceTrans"));
        //         cboRoleHierarchy.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoleHierarchy"));
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
    private com.see.truetransact.uicomponent.CTable tblJointAcctHolder;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustDetDelete;
    private com.see.truetransact.uicomponent.CButton btnCustDetNew;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdAI;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdFileOpenCr;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeletedDetails;
    private com.see.truetransact.uicomponent.CButton btnDepositNo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLockerNo;
    private com.see.truetransact.uicomponent.CButton btnPasDelete;
    private com.see.truetransact.uicomponent.CButton btnPasNew;
    private com.see.truetransact.uicomponent.CButton btnPastSave;
    private com.see.truetransact.uicomponent.CButton btnPhotoSign;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnReport;
    private com.see.truetransact.uicomponent.CButton btnResetPwd;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnToMain;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CComboBox cboBaseCurrAI;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboCommAddr;
    private com.see.truetransact.uicomponent.CComboBox cboConstitutionAI;
    private com.see.truetransact.uicomponent.CComboBox cboOpModeAI;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductIdAI;
    private com.see.truetransact.uicomponent.CComboBox cboSuspDep;
    private com.see.truetransact.uicomponent.CCheckBox chkEditPwd;
    private com.see.truetransact.uicomponent.CCheckBox chkNoTransaction;
    private com.see.truetransact.uicomponent.CDateField dtdExpiryDate;
    private com.see.truetransact.uicomponent.CDateField dtdFreezeDt;
    private com.see.truetransact.uicomponent.CDateField dtdOpeningDateAI;
    private javax.swing.JSeparator jSeparator1;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo1;
    private com.see.truetransact.uicomponent.CLabel lblActName;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaValue;
    private com.see.truetransact.uicomponent.CLabel lblBaseCurrAI;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblChargeType;
    private com.see.truetransact.uicomponent.CLabel lblCharges;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCityValue;
    private com.see.truetransact.uicomponent.CLabel lblClosedDt;
    private com.see.truetransact.uicomponent.CLabel lblCollectRent;
    private com.see.truetransact.uicomponent.CLabel lblCommAddr;
    private com.see.truetransact.uicomponent.CLabel lblConPassword;
    private com.see.truetransact.uicomponent.CLabel lblConstitutionAI;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCountryValue;
    private com.see.truetransact.uicomponent.CLabel lblCurPassword;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdAI;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameCrValue;
    private com.see.truetransact.uicomponent.CLabel lblDOB;
    private com.see.truetransact.uicomponent.CLabel lblDOBValue;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate;
    private com.see.truetransact.uicomponent.CLabel lblFreezeDt;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblLockerKeyNo;
    private com.see.truetransact.uicomponent.CLabel lblLockerKeyNoVal;
    private com.see.truetransact.uicomponent.CLabel lblLockerNo;
    private com.see.truetransact.uicomponent.CLabel lblMinOrMaj;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOpModeAI;
    private com.see.truetransact.uicomponent.CLabel lblOpeningDateAI;
    private com.see.truetransact.uicomponent.CLabel lblPasCustId;
    private com.see.truetransact.uicomponent.CLabel lblPassword;
    private com.see.truetransact.uicomponent.CLabel lblPin;
    private com.see.truetransact.uicomponent.CLabel lblPinValue;
    private com.see.truetransact.uicomponent.CLabel lblPrevActNumAI;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProductIdAI;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblPwdRequired;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSerTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax2;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStateValue;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblStreetValue;
    private com.see.truetransact.uicomponent.CLabel lblStreetValue1;
    private javax.swing.JLabel lblTBSep1;
    private javax.swing.JLabel lblTBSep2;
    private com.see.truetransact.uicomponent.CLabel lblTBSep3;
    private javax.swing.JLabel lblTBSep4;
    private com.see.truetransact.uicomponent.CLabel lblToDt;
    private com.see.truetransact.uicomponent.CLabel lblTotAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblfreezRemarks;
    private javax.swing.JMenuBar mbrTransfer;
    private javax.swing.JMenuItem mitAddNew;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEditNew;
    private javax.swing.JMenuItem mitResume;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuAction;
    private javax.swing.JMenu mnuAdd;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAccounts;
    private com.see.truetransact.uicomponent.CPanel panActInfo;
    private com.see.truetransact.uicomponent.CPanel panActInfo1;
    private com.see.truetransact.uicomponent.CPanel panAliasBrchRemittNumber;
    private com.see.truetransact.uicomponent.CPanel panBillsCharges;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panCharges1;
    private com.see.truetransact.uicomponent.CPanel panChargesServiceTax;
    private com.see.truetransact.uicomponent.CPanel panCustDet;
    private com.see.truetransact.uicomponent.CPanel panCustOperation;
    private com.see.truetransact.uicomponent.CPanel panCustomerInfo;
    private com.see.truetransact.uicomponent.CPanel panDiscountRate1;
    private com.see.truetransact.uicomponent.CPanel panInstEntry;
    private com.see.truetransact.uicomponent.CPanel panOperations;
    private com.see.truetransact.uicomponent.CPanel panOperations1;
    private com.see.truetransact.uicomponent.CPanel panOperations2;
    private com.see.truetransact.uicomponent.CPanel panOperations3;
    private com.see.truetransact.uicomponent.CPanel panOperations4;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills4;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills5;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills6;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills7;
    private com.see.truetransact.uicomponent.CPanel panRemitProdCharges;
    private com.see.truetransact.uicomponent.CPanel panRemitProdChargesButtons;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransitPeriod1;
    private com.see.truetransact.uicomponent.CRadioButton rbtnPwdNo;
    private com.see.truetransact.uicomponent.CRadioButton rbtnPwdYes;
    private com.see.truetransact.uicomponent.CRadioButton rbtnPwdYes1;
    private com.see.truetransact.uicomponent.CRadioButton rbtnSiNo;
    private com.see.truetransact.uicomponent.CRadioButton rbtnSiYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdgStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoFreeze;
    private com.see.truetransact.uicomponent.CRadioButton rdoUnFreeze;
    private com.see.truetransact.uicomponent.CSeparator sepSepAI;
    private com.see.truetransact.uicomponent.CScrollPane sprRemitProdCharges;
    private javax.swing.JSeparator sptAction;
    private javax.swing.JSeparator sptAdd;
    private javax.swing.JSeparator sptEdit;
    private javax.swing.JSeparator sptProcess1;
    private javax.swing.JSeparator sptProcess2;
    private com.see.truetransact.uicomponent.CScrollPane srpAct_Joint;
    private com.see.truetransact.uicomponent.CScrollPane srpInstructions;
    private com.see.truetransact.uicomponent.CTabbedPane tabAccounts;
    private com.see.truetransact.uicomponent.CTable tblAct_Joint;
    private com.see.truetransact.uicomponent.CTable tblInstruction2;
    private com.see.truetransact.uicomponent.CTable tblLockCharges;
    private com.see.truetransact.uicomponent.CToolBar tbrAccounts;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtActName;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtCharges;
    private com.see.truetransact.uicomponent.CTextField txtClosedDt;
    private com.see.truetransact.uicomponent.CTextField txtCollectRentMM;
    private com.see.truetransact.uicomponent.CTextField txtCollectRentyyyy;
    private com.see.truetransact.uicomponent.CPasswordField txtConPassword;
    private com.see.truetransact.uicomponent.CPasswordField txtCurPassword;
    private com.see.truetransact.uicomponent.CTextField txtCustomerIdAI;
    private com.see.truetransact.uicomponent.CTextField txtCustomerIdCr;
    private com.see.truetransact.uicomponent.CTextField txtFreezeRemarks;
    private com.see.truetransact.uicomponent.CTextField txtLockerNo;
    private com.see.truetransact.uicomponent.CTextField txtPasCustId;
    private com.see.truetransact.uicomponent.CPasswordField txtPassword;
    private com.see.truetransact.uicomponent.CTextField txtPrevActNumAI;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax2;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        LockerIssueUI ac = new LockerIssueUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(ac);
        j.setSize(600, 650);
        j.show();
        ac.show();
    }

    /**
     * Getter for property issueId.
     *
     * @return Value of property issueId.
     */
    public java.lang.String getIssueId() {
        return issueId;
    }

    /**
     * Setter for property issueId.
     *
     * @param issueId New value of property issueId.
     */
    public void setIssueId(java.lang.String issueId) {
        this.issueId = issueId;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
    
    private void setCaseExpensesAmount(List taxSettingsList) {    // Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening   
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    lblServiceTaxVal.setText(amt);
                    observable.setLblServiceTaxval(lblServiceTaxVal.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                } else {
                     lblServiceTaxVal.setText("0.00");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private HashMap getGSTAmountMap(HashMap checkForTaxMap){
        HashMap taxMap = new HashMap();
        if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
            if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
               taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));    
            }
        }
        return taxMap;
    }
    
    private void calculateServiceTax(){// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
        // Added by nithya for GST
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        if (observable.getOperation()==ClientConstants.ACTIONTYPE_NEW) {
            System.out.println("lockerTransHeadList :: " + lockerTransHeadList);
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                //LOC_RENT_AC_HD,LOC_SUSP_AC_HD,LOC_MISC_AC_HD,LOC_BRK_AC_HD_YN,SERV_TAX_AC_HD,PENAL_INTEREST_AC_HEAD
                if (lockerTransHeadList != null && lockerTransHeadList.size() > 0) {
                    HashMap lockerTransHeadMap = (HashMap) lockerTransHeadList.get(0);
                    //-- GST for Penal --
                    if (txtCharges.getText().length() > 0) {
                        String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("LOC_RENT_AC_HD"));
                        HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                        taxMap = getGSTAmountMap(checkForTaxMap);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtCharges.getText());
                        if (taxMap != null && taxMap.size() > 0) {
                            taxSettingsList.add(taxMap);
                        }
                    }
                    //-- GST for Penal --
                    if (txtServiceTax.getText().length() > 0) {
                        String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("SERV_TAX_AC_HD"));
                        HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                        taxMap = getGSTAmountMap(checkForTaxMap);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtServiceTax.getText());
                        if (taxMap != null && taxMap.size() > 0) {
                            taxSettingsList.add(taxMap);
                        }
                    }
                    //-- GST for Penal --
                    
                    System.out.println("taxSettingsList :: " + taxSettingsList);
                    setCaseExpensesAmount(taxSettingsList);
                    if(CommonUtil.convertObjToDouble(lblServiceTaxVal.getText()) > 0){
                        double gstVal = CommonUtil.convertObjToDouble(lblServiceTaxVal.getText()).doubleValue();
                        double com = CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue();
                        double srTax = CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue();                        
                        double tot = com + srTax + gstVal;
                        lblTotAmtVal.setText(String.valueOf(tot));
                        transactionUI.setCallingAmount(String.valueOf(tot));
                    }  
                }
            }
        }
        // End
    }
    
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";  
        String fromTransferID = "";
        String toTransferID = "";
        String fromCashID = "";
        String toCashID = "";
        HashMap transTypeMap = new HashMap();
        HashMap transMap = new HashMap();
        HashMap transCashMap = new HashMap();
        String actNum = "";
        String oldBatchId = "";
        String newBatchId = "";
        transCashMap.put("BATCH_ID", proxyResultMap.get(CommonConstants.TRANS_ID));
        transCashMap.put("TRANS_DT", currDt);
        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);//AUTHORIZE_STATUS
        transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
        HashMap transIdMap = new HashMap();
        List lst = ClientUtil.executeQuery("getDepositAccountTransferDetails", transCashMap);
        if (lst != null && lst.size() > 0) {
            displayStr += "Transfer Transaction Details...\n";
            for (int i = 0; i < lst.size(); i++) {
                transMap = (HashMap) lst.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Batch Id : " + transMap.get("BATCH_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No : " + transMap.get("ACT_NUM")
                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
                }
                System.out.println("rish......"+transMap.get("SINGLE_TRANS_ID"));
                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");

                System.out.println("#### :" + transMap);
                oldBatchId = newBatchId;
                if (i == 0) {
                    fromTransferID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                }
                if (i == lst.size() - 1) {
                    toTransferID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                }
            }
        }
        actNum = CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID));
        lst = ClientUtil.executeQuery("getCashDetails", transCashMap);
        if (lst != null && lst.size() > 0) {
            //system.out.println("eeeeeeeeeeeeeeeeeeeeeeeee");
            displayStr += "Cash Transaction Details...\n";
            for (int i = 0; i < lst.size(); i++) {
                transMap = (HashMap) lst.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                            + "   Amount :  " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Account Head :  " + transMap.get("AC_HD_ID")
                            + "   Amount :  " + transMap.get("AMOUNT") + "\n";
                }
                transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                if (i == 0) {
                    fromCashID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                }
                if (i == lst.size() - 1) {
                    toCashID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
                }
            }
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
//        int yesNo = 0;
//        String[] options = {"Yes", "No"};
//        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
//                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                null, options, options[0]);
//        System.out.println("#$#$$ yesNo : " + yesNo);
//        if (yesNo == 0) {
//            TTIntegration ttIntgration = null;
//            HashMap printParamMap = new HashMap();
//            printParamMap.put("TransDt", currDt);
//            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
//            Object keys1[] = transIdMap.keySet().toArray();
//            for (int i = 0; i < keys1.length; i++) {
//                printParamMap.put("TransId", keys1[i]);
//                ttIntgration.setParam(printParamMap);
//                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
//                    ttIntgration.integrationForPrint("ReceiptPayment");
//                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
//                    ttIntgration.integrationForPrint("CashPayment", false);
//                } else {
//                    ttIntgration.integrationForPrint("CashReceipt", false);
//                }
//            }
//        }
    }
}
