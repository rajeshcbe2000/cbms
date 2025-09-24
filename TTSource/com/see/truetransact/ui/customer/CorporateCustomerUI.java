/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CorporateCustomerUI.java
 *
 * Created on March 22, 2004, 4:31 PM
 */

package com.see.truetransact.ui.customer;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;

import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import java.net.URL;
import java.util.ResourceBundle;
import com.see.truetransact.transferobject.customer.AuthPersonsTO;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.List;

/**
 *
 * @author  amathan
 * modified by Ashok Viajayakumar
 * @modified JK
 */
public class CorporateCustomerUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    ResourceBundle objCommRB = ResourceBundle.getBundle("com.see.truetransact.ui.deposit.CommonRB", ProxyParameters.LANGUAGE);
    
    private CustomerOB observable;
    private CustomerUISupport objCustomerUISupport;
    private HashMap mandatoryMap;
    private HashMap tempMap;
    private HashMap photoSignMap;
    private HashMap totAuthPerMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private boolean phoneExist;
    private int phoneRow;
    private boolean proofExist;
    private int proofRow;
    private final int EDIT_DELETE = 1;
    private final int AUTH_CUST_ID = 2;
    private final int JOINT_CUST_ID = 4;
    private final int SEARCH = 6;
    //int saveContact=0;
    private int viewType;
    final int AUTHORIZE=3;
    final int DELETE = 1;
    boolean flag = false;
    boolean isFilled = false;
    boolean tabCorpFocused = false;   // Added by Rajesh
    boolean photoCreated = false;     // Added by Rajesh
    boolean signCreated = false;      // Added by Rajesh
    private String mandatoryMessage;
    private final String CLASSNAME = this.getClass().getName();
    private String actionType = "";
    
//    ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.customer.CustomerRB", ProxyParameters.LANGUAGE);
    ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.customer.CorporateCustomerRB", ProxyParameters.LANGUAGE);
    
    private String search = "";
    private String CUSTOMERID;//Variable used to get the CustomerId when the popup comes on clicking on Search button in the UI
    
    //--- Defines the Screen Name for using it in IntroDetials
    final String SCREEN = "CUS";
    /**
     * Declare a new instance of the  IntroducerUI...
     */
    IntroducerUI introducerUI = new IntroducerUI(SCREEN);
    private Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    
    /** Creates new form CorporateCustomerUI */
    public CorporateCustomerUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
    }
    
    private void initStartup(){
        setFieldNames();
        internationalize();
        setObservable();
        observable.resetForm();
        objCustomerUISupport = new CustomerUISupport(observable);
        /**
         * To add the  Introduce Tab in the IndividualCustUI...
         */
        tabCorpCust.add(introducerUI, "Introducer", 1);
        initComponentData();
        initTableData();
        ClientUtil.enableDisable(this, false);
        setMaximumLength();
        setButtonEnableDisable();
        setMandatoryHashMap();
        setMandatoryMarks();
        setHelpMessage();
        colWidthChange();
        btnContactAdd.setEnabled(false);
        btnContactDelete.setEnabled(false);
        btnContactToMain.setEnabled(false);
        btnSearch.setEnabled(false);
        tabCorpCust.resetVisits();
        tabContactAndIdentityInfo.resetVisits();
        photoSignMap = new HashMap();
        totAuthPerMap = new HashMap();
        txtCustomerID.setEditable(true);        
        txtCustomerID.setEnabled(true);
    }
    
    private void initTableData(){
        tblContactList.setModel(observable.getTblContactList());
        tblPhoneList.setModel(observable.getTblPhoneList());
        tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
    }
    
    /** To initialize comboboxes */
    private void initComponentData() {
        //        cboTitle.setModel(observable.getCbmTitle());
        cboCustomerType.setModel(observable.getCbmCustomerType());
        cboRelationManager.setModel(observable.getCbmRelationManager());
        cboPrefCommunication.setModel(observable.getCbmPrefCommunication());
        cboCountry.setModel(observable.getCbmCountry());
        cboAddressType.setModel(observable.getCbmAddressType());
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboPhoneType.setModel(observable.getCbmPhoneType());
        cboBusNature.setModel(observable.getCbmBusNature());
        cboIntroType.setModel(observable.getCbmIntroType());
        cboMembershipClass.setModel(observable.getCbmMembershipClass());
        cboAddrProof.setModel(observable.getCbmAddrProof());
        cboIdenProof.setModel(observable.getCbmIdenProof());
         cbcomboAmsam.setModel(observable.getCbmcomboAmsam());
        cbcomboDesam.setModel(observable.getCbmcomboDesam());
    }
    
    private void setObservable(){
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '2' indicates that the customer type is CORPORATE
        observable = new CustomerOB(2);
        observable.addObserver(this);
    }
     private void setMandatoryMarks() {
        objCustomerUISupport.putMandatoryMarks(CLASSNAME,panKYC);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME,panCompanyInfo);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME,panCustomerSide);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME,panContactInfo);
    }
    private void setMaximumLength(){
        //        txtFirstName.setMaxLength(128);
        //        txtMiddleName.setMaxLength(128);
        //        txtLastName.setMaxLength(128);
        txtCustomerID.setAllowAll(true);
        txtNetWorth.setValidation(new CurrencyValidation(14,2));
        txtRemarks.setMaxLength(256);
        txtCompany.setMaxLength(128);
        txtEmailID.setMaxLength(64);
        txtCustUserid.setMaxLength(64);
        txtWebSite.setMaxLength(128);
        txtRemarks.setMaxLength(256);
        txtRiskRate.setMaxLength(8);
        
        txtStreet.setMaxLength(256);
        txtArea.setMaxLength(128);
        txtPincode.setMaxLength(16);
        txtAddrRemarks.setMaxLength(256);
        txtAreaCode.setValidation(new NumericValidation());
        txtAreaCode.setMaxLength(8);
        txtAreaCode.setAllowNumber(true);
        txtAreaCode.setAllowAll(false);
        txtPhoneNumber.setValidation(new NumericValidation());
        txtPhoneNumber.setMaxLength(16);
        txtPhoneNumber.setAllowNumber(true);
        txtPhoneNumber.setAllowAll(false);
        txtRegNumber.setMaxLength(32);
        txtCEO.setMaxLength(32);
        txtPanNumber.setMaxLength(32);
        
        /* Financial Details*/
        txtAuthCapital.setValidation(new CurrencyValidation(14,2));
        txtIssuedCapital.setValidation(new CurrencyValidation(14,2));
        txtSubscribedCapital.setValidation(new CurrencyValidation(14,2));
        txtTotalResource.setValidation(new CurrencyValidation(14,2));
        txtTotalIncome.setValidation(new CurrencyValidation(14,2));
        txtTotalNonTaxExp.setValidation(new CurrencyValidation(14,2));
        txtTaxliability.setValidation(new CurrencyValidation(14,2));
        txtprofitBefTax.setValidation(new CurrencyValidation(14,2));
        txtLastYrPL.setValidation(new CurrencyValidation(14,2));
        txtDividendPercentage.setValidation(new NumericValidation(3,0));
        txtAuthCustId.setAllowAll(true);
        //        txtCustPwd.setValidation(new DefaultValidation());
        //        txtTransPwd.setValidation(new DefaultValidation());
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        lblAddrVerified.setName("lblAddrVerified");
        btnAuthCustId.setName("btnAuthCustId");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnContactAdd.setName("btnContactAdd");
        btnContactDelete.setName("btnContactDelete");
        btnContactNew.setName("btnContactNew");
        btnContactNoAdd.setName("btnContactNoAdd");
        btnContactToMain.setName("btnContactToMain");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPhoneDelete.setName("btnPhoneDelete");
        btnPhoneNew.setName("btnPhoneNew");
        btnPhotoLoad.setName("btnPhotoLoad");
        btnPhotoRemove.setName("btnPhotoRemove");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSignLoad.setName("btnSignLoad");
        btnSignRemove.setName("btnSignRemove");
        cSeparator1.setName("cSeparator1");
        cboAddressType.setName("cboAddressType");
        cboCity.setName("cboCity");
        cboCountry.setName("cboCountry");
        cboCustomerType.setName("cboCustomerType");
        cboPhoneType.setName("cboPhoneType");
        cboPrefCommunication.setName("cboPrefCommunication");
        cboRelationManager.setName("cboRelationManager");
        cboState.setName("cboState");
        //        cboTitle.setName("cboTitle");
        lblAddressType.setName("lblAddressType");
        lblArea.setName("lblArea");
        lblAreaCode.setName("lblAreaCode");
        lblAuthCapital.setName("lblAuthCapital");
        lblAuthCustId.setName("lblAuthCustId");
        lblCity.setName("lblCity");
        lblCompany.setName("lblCompany");
        lblCountry.setName("lblCountry");
        lblCustPwd.setName("lblCustPwd");
        lblCustUserid.setName("lblCustUserid");
        lblCustomerID.setName("lblCustomerID");
        lblCreatedDt.setName("lblCreatedDt");
        lblCreatedDt1.setName("lblCreatedDt1");
        lblCustomerType.setName("lblCustomerType");
        lblDividendPercentage.setName("lblDividendPercentage");
        lblDividendPercentage1.setName("lblDividendPercentage1");
        lblEmailID.setName("lblEmailID");
        //        lblExistingCust.setName("lblExistingCust");
        //        lblFirstName.setName("lblFirstName");
        //        lblGender.setName("lblGender");
        lblIssuedCapital.setName("lblIssuedCapital");
        //        lblLastName.setName("lblLastName");
        lblLastYrPL.setName("lblLastYrPL");
        //        lblMiddleName.setName("lblMiddleName");
        lblMsg.setName("lblMsg");
        lblNetWorth.setName("lblNetWorth");
        lblTransPwd.setName("lblTransPwd");
        lblPhoneNumber.setName("lblPhoneNumber");
        lblPhoneType.setName("lblPhoneType");
        lblPhoto.setName("lblPhoto");
        lblPincode.setName("lblPincode");
        lblPrefCommunication.setName("lblPrefCommunication");
        lblRelationManager.setName("lblRelationManager");
        lblRemarks.setName("lblRemarks");
        lblSign.setName("lblSign");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblStreet.setName("lblStreet");
        lblSubscribedCapital.setName("lblSubscribedCapital");
        //        lblTitle.setName("lblTitle");
        lblTotalResource.setName("lblTotalResource");
        lblWebSite.setName("lblWebSite");
        mbrCorporateCustomer.setName("mbrCorporateCustomer");
        panAdditionalInfo.setName("panAdditionalInfo");
        panAddress.setName("panAddress");
        panAddressDetails.setName("panAddressDetails");
        panAddressType.setName("panAddressType");
        panAuthCustId.setName("panAuthCustId");
        //        panAuthPersonDetails.setName("panAuthPersonDetails");
        //        panAuthPersonInfo.setName("panAuthPersonInfo");
        panCapital.setName("panCapital");
        panCity.setName("panCity");
        panCompanyAuthPersonInfo.setName("panCompanyAuthPersonInfo");
        panCompanyInfo.setName("panCompanyInfo");
        panContactAndIdentityInfo.setName("panContactAndIdentityInfo");
        panContactControl.setName("panContactControl");
        panContactInfo.setName("panContactInfo");
        panContactNo.setName("panContactNo");
        panContacts.setName("panContacts");
        panContactsList.setName("panContactsList");
        panCorporateCustomer.setName("panCorporateCustomer");
        panCountry.setName("panCountry");
        panCountryDetails.setName("panCountryDetails");
        //        panExistingCust.setName("panExistingCust");
        panFinancialDetails.setName("panFinancialDetails");
        //        panGender.setName("panGender");
        panOthers.setName("panOthers");
        panPhoneAreaNumber.setName("panPhoneAreaNumber");
        panPhoneList.setName("panPhoneList");
        panPhoneSave.setName("panPhoneSave");
        panPhoneType.setName("panPhoneType");
        panPhoto.setName("panPhoto");
        panPhotoButtons.setName("panPhotoButtons");
        panPhotoSign.setName("panPhotoSign");
        panSign.setName("panSign");
        panSignButtons.setName("panSignButtons");
        panStatus.setName("panStatus");
        panTeleCommunication.setName("panTeleCommunication");
        panTelecomDetails.setName("panTelecomDetails");
        //        rdoExistingCust_No.setName("rdoExistingCust_No");
        //        rdoExistingCust_Yes.setName("rdoExistingCust_Yes");
        //        rdoGender_Female.setName("rdoGender_Female");
        //        rdoGender_Male.setName("rdoGender_Male");
        srpContactList.setName("srpContactList");
        srpPhoneList.setName("srpPhoneList");
        srpPhotoLoad.setName("srpPhotoLoad");
        srpSignLoad.setName("srpSignLoad");
        tabContactAndIdentityInfo.setName("tabContactAndIdentityInfo");
        tblContactList.setName("tblContactList");
        tblPhoneList.setName("tblPhoneList");
        txtArea.setName("txtArea");
        txtAreaCode.setName("txtAreaCode");
        txtAuthCapital.setName("txtAuthCapital");
        txtAuthCustId.setName("txtAuthCustId");
        txtCompany.setName("txtCompany");
        txtCustPwd.setName("txtCustPwd");
        txtCustUserid.setName("txtCustUserid");
        txtCustomerID.setName("txtCustomerID");
        txtDividendPercentage.setName("txtDividendPercentage");
        txtEmailID.setName("txtEmailID");
        //        txtFirstName.setName("txtFirstName");
        txtIssuedCapital.setName("txtIssuedCapital");
        //        txtLastName.setName("txtLastName");
        txtLastYrPL.setName("txtLastYrPL");
        //        txtMiddleName.setName("txtMiddleName");
        txtNetWorth.setName("txtNetWorth");
        txtTransPwd.setName("txtTransPwd");
        txtPhoneNumber.setName("txtPhoneNumber");
        txtPincode.setName("txtPincode");
        txtRemarks.setName("txtRemarks");
        txtStreet.setName("txtStreet");
        txtSubscribedCapital.setName("txtSubscribedCapital");
        txtTotalResource.setName("txtTotalResource");
        txtWebSite.setName("txtWebSite");
        lblCrAvldSince.setName("lblCrAvldSince");
        tdtCrAvldSince.setName("tdtCrAvldSince");
        lblRiskRate.setName("lblRiskRate");
        txtRiskRate.setName("txtRiskRate");
        lblRegNumber.setName("lblRegNumber");
        txtRegNumber.setName("txtRegNumber");
        lblDtEstablished.setName("lblDtEstablished");
        tdtDtEstablished.setName("tdtDtEstablished");
        lblCEO.setName("lblCEO");
        txtCEO.setName("txtCEO");
        cboIntroType.setName("cboIntroType");
        txtAddrRemarks.setName("txtAddrRemarks");
        
        lblNetWorthAsOn.setName("lblNetWorthAsOn");
        tdtNetWorthAsOn.setName("tdtNetWorthAsOn");
        txtPanNumber.setName("txtPanNumber");
        lblPanNumber.setName("lblPanNumber");
        btnDeleteDetails.setName("btnDeleteDetails");
        chkAddrVerified.setName("chkAddrVerified");
        lblMembershipClass.setName("lblMembershipClass");
        cboMembershipClass.setName("cboMembershipClass");
        lblAddrProof.setName("lblAddrProof");
        cboAddrProof.setName("cboAddrProof");
        lblIdenProof.setName("lblIdenProof");
        cboIdenProof.setName("cboIdenProof");
        
        tdtFinacialYrEnd.setName("tdtFinacialYrEnd");
        txtTotalIncome.setName("txtTotalIncome");
        txtTotalNonTaxExp.setName("txtTotalNonTaxExp");
        txtprofitBefTax.setName("txtprofitBefTax");
        txtTaxliability.setName("txtTaxliability");
        lblFinancialyrEnd.setName("lblFinancialyrEnd");
        lblTotalIncome.setName("lblTotalIncome");
        lblTotalNonTaxExp.setName("lblTotalNonTaxExp");
        lblProfitBeforeTax.setName("lblProfitBeforeTax");
        lblLiablityTax.setName("lblLiablityTax");
        panSuspendCustomer.setName("panSuspendCustomer");
        lblSuspendCust.setName("lblSuspendCust");
        lblSuspendDate.setName("lblSuspendDate");
        chkSuspendCust.setName("chkSuspendCust");
        tdtSuspendCustFrom.setName("tdtSuspendCustFrom"); 
        lblRevokeCust.setName("lblRevokeCust");
        tdtRevokedCustDate.setName("tdtRevokedCustDate");
        lblSuspendRemarks.setName("lblSuspendRemarks");
        chkRevokeCust.setName("chkRevokeCust");
        txtSuspRevRemarks.setName("txtSuspRevRemarks");
        lblRevokeDate.setName("lblRevokeDate");
        lblBankruptsy.setName("lblBankruptsy");
        txtBankruptsy.setName("txtBankruptsy");
        txtMemNum.setText("txtMemNum");
        lblMemNum.setText("lblMemNum");
        rdoITDec_Pan.setName("rdoITDec_Pan");
        rdoITDec_F60.setName("rdoITDec_F60");
        rdoITDec_F61.setName("rdoITDec_F61");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblAddrVerified.setText(resourceBundle.getString("lblAddrVerified"));
        lblSign.setText(resourceBundle.getString("lblSign"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblAddressType.setText(resourceBundle.getString("lblAddressType"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        ((javax.swing.border.TitledBorder)panSign.getBorder()).setTitle(resourceBundle.getString("panSign"));
        btnContactNew.setText(resourceBundle.getString("btnContactNew"));
        ((javax.swing.border.TitledBorder)panCompanyInfo.getBorder()).setTitle(resourceBundle.getString("panCompanyInfo"));
        lblPincode.setText(resourceBundle.getString("lblPincode"));
        //        rdoGender_Male.setText(resourceBundle.getString("rdoGender_Male"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblCustomerID.setText(resourceBundle.getString("lblCustomerID"));
        lblCreatedDt.setText(resourceBundle.getString("lblCreatedDt"));
        lblCreatedDt1.setText(resourceBundle.getString("lblCreatedDt1"));
        btnPhotoRemove.setText(resourceBundle.getString("btnPhotoRemove"));
        //        lblMiddleName.setText(resourceBundle.getString("lblMiddleName"));
        lblAreaCode.setText(resourceBundle.getString("lblAreaCode"));
        btnSignRemove.setText(resourceBundle.getString("btnSignRemove"));
        lblWebSite.setText(resourceBundle.getString("lblWebSite"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        lblTransPwd.setText(resourceBundle.getString("lblTransPwd"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        ((javax.swing.border.TitledBorder)panContactInfo.getBorder()).setTitle(resourceBundle.getString("panContactInfo"));
        btnSignLoad.setText(resourceBundle.getString("btnSignLoad"));
        btnContactDelete.setText(resourceBundle.getString("btnContactDelete"));
        ((javax.swing.border.TitledBorder)panCorporateCustomer.getBorder()).setTitle(resourceBundle.getString("panCorporateCustomer"));
        //        lblGender.setText(resourceBundle.getString("lblGender"));
        lblNetWorth.setText(resourceBundle.getString("lblNetWorth"));
        lblRelationManager.setText(resourceBundle.getString("lblRelationManager"));
        //        lblLastName.setText(resourceBundle.getString("lblLastName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnPhotoLoad.setText(resourceBundle.getString("btnPhotoLoad"));
        ((javax.swing.border.TitledBorder)panTelecomDetails.getBorder()).setTitle(resourceBundle.getString("panTelecomDetails"));
        btnContactAdd.setText(resourceBundle.getString("btnContactAdd"));
        lblPhoneNumber.setText(resourceBundle.getString("lblPhoneNumber"));
        ((javax.swing.border.TitledBorder)panContacts.getBorder()).setTitle(resourceBundle.getString("panContacts"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblCustUserid.setText(resourceBundle.getString("lblCustUserid"));
        lblPrefCommunication.setText(resourceBundle.getString("lblPrefCommunication"));
        //        rdoGender_Female.setText(resourceBundle.getString("rdoGender_Female"));
        lblEmailID.setText(resourceBundle.getString("lblEmailID"));
        //        ((javax.swing.border.TitledBorder)panAuthPersonInfo.getBorder()).setTitle(resourceBundle.getString("panAuthPersonInfo"));
        lblAuthCustId.setText(resourceBundle.getString("lblAuthCustId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        //        lblTitle.setText(resourceBundle.getString("lblTitle"));
        btnPhoneDelete.setText(resourceBundle.getString("btnPhoneDelete"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblCustomerType.setText(resourceBundle.getString("lblCustomerType"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblPhoto.setText(resourceBundle.getString("lblPhoto"));
        lblPhoneType.setText(resourceBundle.getString("lblPhoneType"));
        btnContactNoAdd.setText(resourceBundle.getString("btnContactNoAdd"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblCompany.setText(resourceBundle.getString("lblCompany"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnContactToMain.setText(resourceBundle.getString("btnContactToMain"));
        lblCustPwd.setText(resourceBundle.getString("lblCustPwd"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPhoneNew.setText(resourceBundle.getString("btnPhoneNew"));
        lblState.setText(resourceBundle.getString("lblState"));
        ((javax.swing.border.TitledBorder)panPhoto.getBorder()).setTitle(resourceBundle.getString("panPhoto"));
        btnAuthCustId.setText(resourceBundle.getString("btnAuthCustId"));
        //        lblFirstName.setText(resourceBundle.getString("lblFirstName"));
        lblCrAvldSince.setText(resourceBundle.getString("lblCrAvldSince"));
        lblRiskRate.setText(resourceBundle.getString("lblRiskRate"));
        lblBusNature.setText(resourceBundle.getString("lblBusNature"));
        lblRegNumber.setText(resourceBundle.getString("lblRegNumber"));
        lblDtEstablished.setText(resourceBundle.getString("lblDtEstablished"));
        lblCEO.setText(resourceBundle.getString("lblCEO"));
        
        lblNetWorthAsOn.setText(resourceBundle.getString("lblNetWorthAsOn"));
        lblPanNumber.setText(resourceBundle.getString("lblPanNumber"));
        lblMembershipClass.setText(resourceBundle.getString("lblMembershipClass"));
        lblAddrProof.setText(resourceBundle.getString("lblAddrProof"));
        lblIdenProof.setText(resourceBundle.getString("lblIdenProof"));
        lblFinancialyrEnd.setText(resourceBundle.getString("lblFinancialyrEnd"));
        lblTotalIncome.setText(resourceBundle.getString("lblTotalIncome"));
        lblTotalNonTaxExp.setText(resourceBundle.getString("lblTotalNonTaxExp"));
        lblProfitBeforeTax.setText(resourceBundle.getString("lblProfitBeforeTax"));
        lblLiablityTax.setText(resourceBundle.getString("lblLiablityTax"));
        lblBankruptsy.setText(resourceBundle.getString("lblBankruptsy"));
        lblMemNum.setText(resourceBundle.getString("lblMemNum"));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("txtCompany", new Boolean(true));
        mandatoryMap.put("cboCustomerType", new Boolean(true));
        mandatoryMap.put("cboRelationManager", new Boolean(true));
        mandatoryMap.put("txtNetWorth", new Boolean(true));
        mandatoryMap.put("txtEmailID", new Boolean(true));
        mandatoryMap.put("cboPrefCommunication", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("rdoGender_Male", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("txtMiddleName", new Boolean(true));
        mandatoryMap.put("txtLastName", new Boolean(true));
        mandatoryMap.put("cboTitle", new Boolean(true));
        mandatoryMap.put("txtAuthCustId", new Boolean(true));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPhoneNumber", new Boolean(true));
        mandatoryMap.put("txtAreaCode", new Boolean(true));
        mandatoryMap.put("cboPhoneType", new Boolean(true));
        mandatoryMap.put("txtOtherApplNo", new Boolean(true));
        mandatoryMap.put("txtCustUserid", new Boolean(true));
        mandatoryMap.put("txtCustPwd", new Boolean(true));
        mandatoryMap.put("txtWebSite", new Boolean(true));
        mandatoryMap.put("cboBusNature", new Boolean(true));
        mandatoryMap.put("txtRegNumber", new Boolean(true));
        mandatoryMap.put("tdtDtEstablished", new Boolean(true));
        mandatoryMap.put("txtCEO", new Boolean(true));
        mandatoryMap.put("cboIntroType", new Boolean(true));
        mandatoryMap.put("txtAddrRemrks", new Boolean(false));   
        mandatoryMap.put("tdtNetWorthAsOn", new Boolean(false));
        mandatoryMap.put("txtPanNumber", new Boolean(false));
        mandatoryMap.put("chkAddrVerified", new Boolean(true));
        mandatoryMap.put("cboMembershipClass", new Boolean(true));
        mandatoryMap.put("cboAddrProof" ,new Boolean(false));
        mandatoryMap.put("cboIdenProof", new Boolean(false));
        mandatoryMap.put("txtBankruptsy", new Boolean(false)); 
        mandatoryMap.put("rdoITDec_Pan", new Boolean(true));
        
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        CorporateCustomerMRB objMandatoryRB = new CorporateCustomerMRB();
        txtCustomerID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerID"));
        txtCompany.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCompany"));
        cboCustomerType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustomerType"));
        cboRelationManager.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRelationManager"));
        txtNetWorth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetWorth"));
        txtEmailID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmailID"));
        cboPrefCommunication.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPrefCommunication"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        //        rdoGender_Male.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoGender_Male"));
        //        txtFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFirstName"));
        //        txtMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiddleName"));
        //        txtLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastName"));
        //        cboTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTitle"));
        txtAuthCustId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAuthCustId"));
        txtStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet"));
        txtArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea"));
        cboAddressType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAddressType"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
        txtPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPincode"));
        cboCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry"));
        txtPhoneNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhoneNumber"));
        txtAreaCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAreaCode"));
        cboPhoneType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPhoneType"));
        txtTransPwd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransPwd"));
        txtCustUserid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustUserid"));
        txtCustPwd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustPwd"));
        txtWebSite.setHelpMessage(lblMsg, objMandatoryRB.getString("txtWebSite"));
        cboBusNature.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBusNature"));
        txtRegNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRegNumber"));
        tdtDtEstablished.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDtEstablished"));
        txtCEO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCEO"));
        tdtNetWorthAsOn.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNetWorthAsOn"));
        txtPanNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPanNumber"));
        cboMembershipClass.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMembershipClass"));
        cboAddrProof.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAddrProof"));
        cboIdenProof.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIdenProof"));
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        updateAddress();
        updateCustomerData();
    }
    
    private void updateCustomerData(){
        //        removeButtons();
        try
        {   
        cbcomboAmsam.setSelectedItem(observable.getCbcomboAmsam());
        cbcomboDesam.setSelectedItem(observable.getCbcomboDesam());
        txtCustomerID.setText(observable.getTxtCustomerID());
        lblCreatedDt1.setText(observable.getLblCreatedDt1());
        lblDealingPeriod.setText(observable.getLblDealingPeriod());
        txtCompany.setText(observable.getTxtCompany());
        cboCustomerType.setSelectedItem(observable.getCboCustomerType());
        cboRelationManager.setSelectedItem(observable.getCboRelationManager());
        txtNetWorth.setText(observable.getTxtNetWorth());
        tdtNetWorthAsOn.setDateValue(observable.getTdtNetWorthAsOn());
//        cboIntroType.setSelectedItem(observable.getCboIntroType());
        
        txtEmailID.setText(observable.getTxtEmailID());
        cboPrefCommunication.setSelectedItem(observable.getCboPrefCommunication());
        txtRemarks.setText(observable.getTxtRemarks());
        //        rdoGender_Male.setSelected(observable.getRdoGender_Male());
        //        rdoGender_Female.setSelected(observable.getRdoGender_Female());
        //        txtFirstName.setText(observable.getTxtFirstName());
        //        txtMiddleName.setText(observable.getTxtMiddleName());
        //        txtLastName.setText(observable.getTxtLastName());
        //        cboTitle.setSelectedItem(observable.getCboTitle());
        txtAuthCustId.setText(observable.getTxtAuthCustId());
        txtTransPwd.setText(observable.getTxtTransPwd());
        txtCustUserid.setText(observable.getTxtCustUserid());
        txtCustPwd.setText(observable.getTxtCustPwd());
        txtWebSite.setText(observable.getTxtWebSite());
        tblContactList.setModel(observable.getTblContactList());
        lblStatus.setText(observable.getLblStatus());
        txtAuthCustId.setText(observable.getTxtAuthCustId());
        tblCustomerHistory.setModel(observable.getTbmCustomerHistory());

        /* Finance Details*/
        txtAuthCapital.setText(observable.getTxtAuthCapital());
        txtIssuedCapital.setText(observable.getTxtIssuedCapital());
        txtSubscribedCapital.setText(observable.getTxtSubscribedCapital());
        txtTotalResource.setText(observable.getTxtTotalResource());
        txtLastYrPL.setText(observable.getTxtLastYrPL());
        txtTotalIncome.setText(observable.getTxtTotalIncome());
        txtTotalNonTaxExp.setText(observable.getTxtTotalNonTaxExp());
        txtprofitBefTax.setText(observable.getTxtprofitBefTax());
        txtTaxliability.setText(observable.getTxtTaxliability());
        tdtFinacialYrEnd.setDateValue(observable.getTdtFinacialYrEnd());
        txtDividendPercentage.setText(observable.getTxtDividendPercentage());
        tdtCrAvldSince.setDateValue(observable.getTdtCrAvldSince());
        txtRiskRate.setText(observable.getTxtRiskRate());
        cboBusNature.setSelectedItem(observable.getCboBusNature());
        txtRegNumber.setText(observable.getTxtRegNumber());
        tdtDtEstablished.setDateValue(observable.getTdtDtEstablished());
        txtCEO.setText(observable.getTxtCEO());
        chkAddrVerified.setSelected(observable.getChkAddrVerified());
        chkPhVerified.setSelected(observable.getChkPhVerified());
        chkFinanceStmtVerified.setSelected(observable.getChkFinanceStmtVerified());
        //        addButtons();
        tblJointAcctHolder.setModel(observable.getTblJointAccnt());
        txtPanNumber.setText(observable.getTxtPanNumber());
        cboMembershipClass.setSelectedItem(observable.getCboMembershipClass());
        cboAddrProof.setSelectedItem(observable.getCboAddrProof());
        cboIdenProof.setSelectedItem(observable.getCboIdenProof());
        chkSuspendCust.setSelected(observable.isChksuspendedBy());
        chkRevokeCust.setSelected(observable.isChkrevokedBy());
        tdtSuspendCustFrom.setDateValue(observable.getSuspendedDate());
        tdtRevokedCustDate.setDateValue(observable.getRevokedDate());
        txtSuspRevRemarks.setText(observable.getSusRevRemarks());
        txtBankruptsy.setText(observable.getBankruptcy());
        txtMemNum.setText(observable.getMemberShipNo());
        rdoITDec_Pan.setSelected(observable.isRdoITDec_pan());
        rdoITDec_F60.setSelected(observable.isRdoITDec_F60());
        rdoITDec_F61.setSelected(observable.isRdoITDec_F61());
        tblProofList.setModel(observable.getTblProofList());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("error"+e);
        }
    }
    private void updateAddress(){
        updateAddressExceptAddType();
        tempMap = observable.getPhoneList();
//        cboAddressType.setSelectedItem(observable.getCboAddressType());
        observable.setPhoneList(tempMap);
    }
    
    private void updateAddressExceptAddType(){
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        txtPincode.setText(observable.getTxtPincode());
        txtStreet.setText(observable.getTxtStreet());
        cboCountry.setSelectedItem(observable.getCboCountry());
        txtArea.setText(observable.getTxtArea());
        txtAddrRemarks.setText(observable.getTxtAddrRemarks());
        updatePhone();
    }
    
    public void updatePhone(){
        tblPhoneList.setModel(observable.getTblPhoneList());
        txtPhoneNumber.setText(observable.getTxtPhoneNumber());
        txtAreaCode.setText(observable.getTxtAreaCode());
        cboPhoneType.setSelectedItem(observable.getCboPhoneType());
    }
    
    //    private void removeButtons(){
    //        rdoGender.remove(rdoGender_Male);
    //        rdoGender.remove(rdoGender_Female);
    //        rdgExistingCust.remove(rdoExistingCust_Yes);
    //        rdgExistingCust.remove(rdoExistingCust_No);
    //    }
    
    //    private void addButtons(){
    //        rdoGender.add(rdoGender_Male);
    //        rdoGender.add(rdoGender_Female);
    //        rdgExistingCust.add(rdoExistingCust_Yes);
    //        rdgExistingCust.add(rdoExistingCust_No);
    //    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        if (cbcomboAmsam.getSelectedItem() != null) {
            observable.setCbcomboAmsam((String) ((ComboBoxModel) cbcomboAmsam.getModel()).getKeyForSelected());
        }
        if (cbcomboDesam.getSelectedItem() != null) {
            observable.setCbcomboDesam((String) ((ComboBoxModel) cbcomboDesam.getModel()).getKeyForSelected());
        }
        observable.setCboIntroType((String)cboIntroType.getSelectedItem());
        observable.setTxtCustomerID(txtCustomerID.getText());
        observable.setTxtCompany(txtCompany.getText());
        observable.setCboCustomerType((String) cboCustomerType.getSelectedItem());
        observable.setCboRelationManager((String) cboRelationManager.getSelectedItem());
        observable.setTxtNetWorth(txtNetWorth.getText());
        observable.setTdtNetWorthAsOn(tdtNetWorthAsOn.getDateValue());
        observable.setTxtEmailID(txtEmailID.getText());
        observable.setCboPrefCommunication((String) cboPrefCommunication.getSelectedItem());
        observable.setTxtRemarks(txtRemarks.getText());
        //        observable.setRdoGender_Male(rdoGender_Male.isSelected());
        //        observable.setRdoGender_Female(rdoGender_Female.isSelected());
        //        observable.setTxtFirstName(txtFirstName.getText());
        //        observable.setTxtMiddleName(txtMiddleName.getText());
        //        observable.setTxtLastName(txtLastName.getText());
        //        observable.setCboTitle((String) cboTitle.getSelectedItem());
        observable.setTxtAuthCustId(txtAuthCustId.getText());
        observable.setTxtStreet(txtStreet.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setCboAddressType(CommonUtil.convertObjToStr(cboAddressType.getSelectedItem()));
        observable.setCboCity(CommonUtil.convertObjToStr(cboCity.getSelectedItem()));
        observable.setCboState(CommonUtil.convertObjToStr(cboState.getSelectedItem()));
        observable.setTxtPincode(txtPincode.getText());
        observable.setCboCountry(CommonUtil.convertObjToStr(cboCountry.getSelectedItem()));
        observable.setTxtAddrRemarks(txtAddrRemarks.getText());
        observable.setTxtPhoneNumber(txtPhoneNumber.getText());
        observable.setTxtAreaCode(txtAreaCode.getText());
        if (cboPhoneType.getSelectedItem() != null) {
            observable.setCboPhoneType(CommonUtil.convertObjToStr(((ComboBoxModel)cboPhoneType.getModel()).getKeyForSelected()));
        }
        observable.setTblContactList((com.see.truetransact.clientutil.EnhancedTableModel)tblContactList.getModel());
        observable.setTblPhoneList((com.see.truetransact.clientutil.EnhancedTableModel)tblPhoneList.getModel());
        observable.setTxtTransPwd(txtTransPwd.getText());
        observable.setTxtCustUserid(txtCustUserid.getText());
        observable.setTxtCustPwd(txtCustPwd.getText());
        observable.setTxtWebSite(txtWebSite.getText());
        
        /*Financial Details*/
        observable.setTxtAuthCapital(txtAuthCapital.getText());
        observable.setTxtIssuedCapital(txtIssuedCapital.getText());
        observable.setTxtSubscribedCapital(txtSubscribedCapital.getText());
        observable.setTxtTotalResource(txtTotalResource.getText());
        observable.setTxtLastYrPL(txtLastYrPL.getText());
        observable.setTxtDividendPercentage(txtDividendPercentage.getText());
        observable.setTxtTotalIncome(txtTotalIncome.getText());
        observable.setTxtTotalNonTaxExp(txtTotalNonTaxExp.getText());
        observable.setTxtTaxliability(txtTaxliability.getText());
        observable.setTxtprofitBefTax(txtprofitBefTax.getText());
        observable.setTdtFinacialYrEnd(tdtFinacialYrEnd.getDateValue());
        observable.setCboBusNature(CommonUtil.convertObjToStr(((ComboBoxModel)cboBusNature.getModel()).getKeyForSelected()));
        observable.setTdtDtEstablished(tdtDtEstablished.getDateValue());
        observable.setTxtCEO(txtCEO.getText());
        observable.setTxtRegNumber(txtRegNumber.getText());
        observable.setTdtCrAvldSince(tdtCrAvldSince.getDateValue());
        observable.setTxtRiskRate(txtRiskRate.getText());
        observable.setChkAddrVerified(chkAddrVerified.isSelected());
        observable.setChkPhVerified(chkPhVerified.isSelected());
        observable.setChkFinanceStmtVerified(chkFinanceStmtVerified.isSelected());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtPanNumber(txtPanNumber.getText());
        
        observable.setLblValCustomerName(lblValCustomerName.getText());
        lblValCustomerName.setToolTipText(lblValCustomerName.getText());
        observable.setLblValDateOfBirth(lblValDateOfBirth.getText());
        observable.setLblValStreet(lblValStreet.getText());
        observable.setLblValArea(lblValArea.getText());
        observable.setLblValCity(lblValCity.getText());
        observable.setLblValState(lblValState.getText());
        observable.setLblValCountry(lblValCountry.getText());
        observable.setLblValCorpCustDesig(lblValCorpCustDesig.getText());
        observable.setLblValPin(lblValPin.getText());
        /** Setting up the Selected Branch **/
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setCboMembershipClass(CommonUtil.convertObjToStr(cboMembershipClass.getSelectedItem()));
        observable.setCboAddrProof(CommonUtil.convertObjToStr(cboAddrProof.getSelectedItem()));
        observable.setCboIdenProof(CommonUtil.convertObjToStr(cboIdenProof.getSelectedItem()));
        observable.setChkrevokedBy(chkRevokeCust.isSelected());
        observable.setChksuspendedBy(chkSuspendCust.isSelected());   
        observable.setSuspendedDate(tdtSuspendCustFrom.getDateValue());
        observable.setRevokedDate(tdtRevokedCustDate.getDateValue());
        observable.setSusRevRemarks(txtSuspRevRemarks.getText());
        observable.setBankruptcy(txtBankruptsy.getText());
        observable.setMemberShipNo(txtMemNum.getText());
        observable.setRdoITDec_pan(rdoITDec_Pan.isSelected());
        observable.setRdoITDec_F60(rdoITDec_F60.isSelected());
        observable.setRdoITDec_F61(rdoITDec_F61.isSelected());
        observable.setTxtUniqueId(txtUniqueId.getText());
    }
    
    /** Check out whether the Corporate Details entered  is in blocked list, if so return true **/
    private boolean isBlocked(){
        
        boolean blocked = false;
        HashMap where = new HashMap();
        if(!txtCompany.getText().equals("")){
            where.put("COMPANY", txtCompany.getText());
        }
        if(!txtStreet.getText().equals("")){
            where.put("BUSINESS_ADDR", txtStreet.getText());
        }
        
        ArrayList resultList = (ArrayList) ClientUtil.executeQuery("getBlockedCorporateDetails", where);
        if (resultList != null) {
            if (resultList.size() > 0) {
                callView("getBlockedCorporateDetails", where);
                blocked = true; }}
         return blocked;
    }
    
    /** Method to do the validation of Subscribed Capital and paidup captial to be greater
     *than the Authorized Capital **/
    private void captitalValidation(CTextField txtAuthorizedCapital, CTextField txtCapital){
        double  authorizedCapital = Double.parseDouble(txtAuthorizedCapital.getText());
        double  capital = Double.parseDouble(txtCapital.getText());
        if(capital>authorizedCapital){
            ClientUtil.displayAlert("Issued Capital Should Be Less Than Authorized Capital");
            txtCapital.setText("");
        }
        
    }
    
    private void captitalSubScribeValidation(CTextField txtIssuedCapital, CTextField txtCapital){
        double  IssuedCapital = Double.parseDouble(txtIssuedCapital.getText());
        double  capital = Double.parseDouble(txtCapital.getText());
        if(capital>IssuedCapital){
            ClientUtil.displayAlert("SubScribe Capital Should Be Less Than Issued Capital");
            txtCapital.setText("");
        }
        
    }
    
    /** The method used to enable or disable the button after Edit button is Clicked**/
    private void enableDisable(){
        ClientUtil.enableDisable(this,true);
        ClientUtil.enableDisable(this.panPhoneAreaNumber, false);
        objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
        objCustomerUISupport.setPhoneButtonEnableDisableDefault( btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        objCustomerUISupport.setContactAddEnableDisable(true, btnContactAdd);
        objCustomerUISupport.setPhotoSignLoadEnableDisable(true, btnPhotoLoad, btnSignLoad);
        setAuthCustIdEnableDisable(true);
        btnSearch.setEnabled(false);
        btnContactDelete.setEnabled(false);
        btnContactAdd.setEnabled(false);
        cboMembershipClass.setEnabled(true);
    }
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        CorporateCustomerUI ui = new CorporateCustomerUI();
        frame.getContentPane().add(ui);
        frame.setVisible(true);
        ui.setVisible(true);
        ui.show();
        frame.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoGender = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMaritalStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgExistingCust = new com.see.truetransact.uicomponent.CButtonGroup();
        panCorporateCustomer = new com.see.truetransact.uicomponent.CPanel();
        panContactsList = new com.see.truetransact.uicomponent.CPanel();
        panContacts = new com.see.truetransact.uicomponent.CPanel();
        srpContactList = new com.see.truetransact.uicomponent.CScrollPane();
        tblContactList = new com.see.truetransact.uicomponent.CTable();
        panContactControl = new com.see.truetransact.uicomponent.CPanel();
        btnContactToMain = new com.see.truetransact.uicomponent.CButton();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        panContactAndIdentityInfo = new com.see.truetransact.uicomponent.CPanel();
        tabContactAndIdentityInfo = new com.see.truetransact.uicomponent.CTabbedPane();
        panContactInfo = new com.see.truetransact.uicomponent.CPanel();
        panAddress = new com.see.truetransact.uicomponent.CPanel();
        panAddressDetails = new com.see.truetransact.uicomponent.CPanel();
        panAddressType = new com.see.truetransact.uicomponent.CPanel();
        lblAddressType = new com.see.truetransact.uicomponent.CLabel();
        txtStreet = new com.see.truetransact.uicomponent.CTextField();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        txtArea = new com.see.truetransact.uicomponent.CTextField();
        cboAddressType = new com.see.truetransact.uicomponent.CComboBox();
        panCountryDetails = new com.see.truetransact.uicomponent.CPanel();
        panCountry = new com.see.truetransact.uicomponent.CPanel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        panCity = new com.see.truetransact.uicomponent.CPanel();
        lblPincode = new com.see.truetransact.uicomponent.CLabel();
        txtPincode = new com.see.truetransact.uicomponent.CTextField();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        cboCountry = new com.see.truetransact.uicomponent.CComboBox();
        panRemarks = new com.see.truetransact.uicomponent.CPanel();
        lblAddrRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtAddrRemarks = new com.see.truetransact.uicomponent.CTextField();
        panContactNo = new com.see.truetransact.uicomponent.CPanel();
        panTeleCommunication = new com.see.truetransact.uicomponent.CPanel();
        panTelecomDetails = new com.see.truetransact.uicomponent.CPanel();
        panPhoneType = new com.see.truetransact.uicomponent.CPanel();
        panPhoneSave = new com.see.truetransact.uicomponent.CPanel();
        btnContactNoAdd = new com.see.truetransact.uicomponent.CButton();
        btnPhoneNew = new com.see.truetransact.uicomponent.CButton();
        btnPhoneDelete = new com.see.truetransact.uicomponent.CButton();
        panPhoneAreaNumber = new com.see.truetransact.uicomponent.CPanel();
        lblPhoneType = new com.see.truetransact.uicomponent.CLabel();
        lblPhoneNumber = new com.see.truetransact.uicomponent.CLabel();
        txtPhoneNumber = new com.see.truetransact.uicomponent.CTextField();
        lblAreaCode = new com.see.truetransact.uicomponent.CLabel();
        txtAreaCode = new com.see.truetransact.uicomponent.CTextField();
        cboPhoneType = new com.see.truetransact.uicomponent.CComboBox();
        panPhoneList = new com.see.truetransact.uicomponent.CPanel();
        srpPhoneList = new com.see.truetransact.uicomponent.CScrollPane();
        tblPhoneList = new com.see.truetransact.uicomponent.CTable();
        btnContactAdd = new com.see.truetransact.uicomponent.CButton();
        btnContactNew = new com.see.truetransact.uicomponent.CButton();
        btnContactDelete = new com.see.truetransact.uicomponent.CButton();
        panPhotoSign = new com.see.truetransact.uicomponent.CPanel();
        panPhoto = new com.see.truetransact.uicomponent.CPanel();
        srpPhotoLoad = new com.see.truetransact.uicomponent.CScrollPane();
        lblPhoto = new com.see.truetransact.uicomponent.CLabel();
        panPhotoButtons = new com.see.truetransact.uicomponent.CPanel();
        btnPhotoLoad = new com.see.truetransact.uicomponent.CButton();
        btnPhotoRemove = new com.see.truetransact.uicomponent.CButton();
        btnPhotoSave = new com.see.truetransact.uicomponent.CButton();
        panSign = new com.see.truetransact.uicomponent.CPanel();
        srpSignLoad = new com.see.truetransact.uicomponent.CScrollPane();
        lblSign = new com.see.truetransact.uicomponent.CLabel();
        panSignButtons = new com.see.truetransact.uicomponent.CPanel();
        btnSignLoad = new com.see.truetransact.uicomponent.CButton();
        btnSignRemove = new com.see.truetransact.uicomponent.CButton();
        btnSignSave = new com.see.truetransact.uicomponent.CButton();
        panAdditionalInfo = new com.see.truetransact.uicomponent.CPanel();
        lblTransPwd = new com.see.truetransact.uicomponent.CLabel();
        lblCustUserid = new com.see.truetransact.uicomponent.CLabel();
        txtCustUserid = new com.see.truetransact.uicomponent.CTextField();
        lblCustPwd = new com.see.truetransact.uicomponent.CLabel();
        lblWebSite = new com.see.truetransact.uicomponent.CLabel();
        txtWebSite = new com.see.truetransact.uicomponent.CTextField();
        txtCustPwd = new com.see.truetransact.uicomponent.CPasswordField();
        txtTransPwd = new com.see.truetransact.uicomponent.CPasswordField();
        txtBankruptsy = new com.see.truetransact.uicomponent.CTextField();
        lblBankruptsy = new com.see.truetransact.uicomponent.CLabel();
        lblAmsam = new com.see.truetransact.uicomponent.CLabel();
        cbcomboAmsam = new javax.swing.JComboBox();
        lblDesam = new com.see.truetransact.uicomponent.CLabel();
        cbcomboDesam = new javax.swing.JComboBox();
        panFinancialDetails = new com.see.truetransact.uicomponent.CPanel();
        panCapital = new com.see.truetransact.uicomponent.CPanel();
        lblAuthCapital = new com.see.truetransact.uicomponent.CLabel();
        txtAuthCapital = new com.see.truetransact.uicomponent.CTextField();
        lblIssuedCapital = new com.see.truetransact.uicomponent.CLabel();
        txtIssuedCapital = new com.see.truetransact.uicomponent.CTextField();
        lblSubscribedCapital = new com.see.truetransact.uicomponent.CLabel();
        txtSubscribedCapital = new com.see.truetransact.uicomponent.CTextField();
        tdtFinacialYrEnd = new com.see.truetransact.uicomponent.CDateField();
        lblFinancialyrEnd = new com.see.truetransact.uicomponent.CLabel();
        panOthers = new com.see.truetransact.uicomponent.CPanel();
        lblTotalResource = new com.see.truetransact.uicomponent.CLabel();
        txtTotalResource = new com.see.truetransact.uicomponent.CTextField();
        lblLastYrPL = new com.see.truetransact.uicomponent.CLabel();
        txtLastYrPL = new com.see.truetransact.uicomponent.CTextField();
        lblDividendPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtDividendPercentage = new com.see.truetransact.uicomponent.CTextField();
        lblDividendPercentage1 = new com.see.truetransact.uicomponent.CLabel();
        txtTotalIncome = new com.see.truetransact.uicomponent.CTextField();
        lblTotalIncome = new com.see.truetransact.uicomponent.CLabel();
        txtTotalNonTaxExp = new com.see.truetransact.uicomponent.CTextField();
        lblTotalNonTaxExp = new com.see.truetransact.uicomponent.CLabel();
        txtprofitBefTax = new com.see.truetransact.uicomponent.CTextField();
        lblProfitBeforeTax = new com.see.truetransact.uicomponent.CLabel();
        lblLiablityTax = new com.see.truetransact.uicomponent.CLabel();
        txtTaxliability = new com.see.truetransact.uicomponent.CTextField();
        cSeparator1 = new com.see.truetransact.uicomponent.CSeparator();
        panCorporateInfo = new com.see.truetransact.uicomponent.CPanel();
        lblCrAvldSince = new com.see.truetransact.uicomponent.CLabel();
        lblRiskRate = new com.see.truetransact.uicomponent.CLabel();
        lblBusNature = new com.see.truetransact.uicomponent.CLabel();
        lblRegNumber = new com.see.truetransact.uicomponent.CLabel();
        lblDtEstablished = new com.see.truetransact.uicomponent.CLabel();
        lblCEO = new com.see.truetransact.uicomponent.CLabel();
        txtRegNumber = new com.see.truetransact.uicomponent.CTextField();
        txtCEO = new com.see.truetransact.uicomponent.CTextField();
        cboBusNature = new com.see.truetransact.uicomponent.CComboBox();
        tdtDtEstablished = new com.see.truetransact.uicomponent.CDateField();
        cSeparator2 = new com.see.truetransact.uicomponent.CSeparator();
        tdtCrAvldSince = new com.see.truetransact.uicomponent.CDateField();
        txtRiskRate = new com.see.truetransact.uicomponent.CTextField();
        panSuspendCustomer = new com.see.truetransact.uicomponent.CPanel();
        lblSuspendCust = new com.see.truetransact.uicomponent.CLabel();
        chkSuspendCust = new com.see.truetransact.uicomponent.CCheckBox();
        tdtSuspendCustFrom = new com.see.truetransact.uicomponent.CDateField();
        lblSuspendDate = new com.see.truetransact.uicomponent.CLabel();
        chkRevokeCust = new com.see.truetransact.uicomponent.CCheckBox();
        lblRevokeCust = new com.see.truetransact.uicomponent.CLabel();
        txtSuspRevRemarks = new com.see.truetransact.uicomponent.CTextField();
        tdtRevokedCustDate = new com.see.truetransact.uicomponent.CDateField();
        lblRevokeDate = new com.see.truetransact.uicomponent.CLabel();
        lblSuspendRemarks = new com.see.truetransact.uicomponent.CLabel();
        tabCorpCust = new com.see.truetransact.uicomponent.CTabbedPane();
        panCompanyAuthPersonInfo = new com.see.truetransact.uicomponent.CPanel();
        panCompanyInfo = new com.see.truetransact.uicomponent.CPanel();
        lblCompany = new com.see.truetransact.uicomponent.CLabel();
        txtCompany = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerType = new com.see.truetransact.uicomponent.CLabel();
        cboCustomerType = new com.see.truetransact.uicomponent.CComboBox();
        lblRelationManager = new com.see.truetransact.uicomponent.CLabel();
        cboRelationManager = new com.see.truetransact.uicomponent.CComboBox();
        lblNetWorth = new com.see.truetransact.uicomponent.CLabel();
        txtNetWorth = new com.see.truetransact.uicomponent.CTextField();
        lblEmailID = new com.see.truetransact.uicomponent.CLabel();
        lblPrefCommunication = new com.see.truetransact.uicomponent.CLabel();
        cboPrefCommunication = new com.see.truetransact.uicomponent.CComboBox();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblIntroType = new com.see.truetransact.uicomponent.CLabel();
        cboIntroType = new com.see.truetransact.uicomponent.CComboBox();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        txtEmailID = new com.see.truetransact.uicomponent.CTextField();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        lblNetWorthAsOn = new com.see.truetransact.uicomponent.CLabel();
        tdtNetWorthAsOn = new com.see.truetransact.uicomponent.CDateField();
        lblCreatedDt = new com.see.truetransact.uicomponent.CLabel();
        lblCreatedDt1 = new com.see.truetransact.uicomponent.CLabel();
        cboMembershipClass = new com.see.truetransact.uicomponent.CComboBox();
        lblMembershipClass = new com.see.truetransact.uicomponent.CLabel();
        txtMemNum = new com.see.truetransact.uicomponent.CTextField();
        lblMemNum = new com.see.truetransact.uicomponent.CLabel();
        panITDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoITDec_Pan = new com.see.truetransact.uicomponent.CRadioButton();
        rdoITDec_F60 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoITDec_F61 = new com.see.truetransact.uicomponent.CRadioButton();
        txtPanNumber = new com.see.truetransact.uicomponent.CTextField();
        lblPanNumber = new com.see.truetransact.uicomponent.CLabel();
        panKYC = new com.see.truetransact.uicomponent.CPanel();
        proofPanel = new com.see.truetransact.uicomponent.CPanel();
        lblAddrVerified = new com.see.truetransact.uicomponent.CLabel();
        chkAddrVerified = new com.see.truetransact.uicomponent.CCheckBox();
        lblPhVerified = new com.see.truetransact.uicomponent.CLabel();
        chkPhVerified = new com.see.truetransact.uicomponent.CCheckBox();
        lblFinanceStmtVerified = new com.see.truetransact.uicomponent.CLabel();
        cboAddrProof = new com.see.truetransact.uicomponent.CComboBox();
        chkFinanceStmtVerified = new com.see.truetransact.uicomponent.CCheckBox();
        lblAddrProof = new com.see.truetransact.uicomponent.CLabel();
        panProofDetails = new com.see.truetransact.uicomponent.CPanel();
        lblIdenProof = new com.see.truetransact.uicomponent.CLabel();
        cboIdenProof = new com.see.truetransact.uicomponent.CComboBox();
        lblUniqueNo = new com.see.truetransact.uicomponent.CLabel();
        txtUniqueId = new com.see.truetransact.uicomponent.CTextField();
        panProof = new com.see.truetransact.uicomponent.CPanel();
        srpProofList = new com.see.truetransact.uicomponent.CScrollPane();
        tblProofList = new com.see.truetransact.uicomponent.CTable();
        panProofControl = new com.see.truetransact.uicomponent.CPanel();
        btnProofNew = new com.see.truetransact.uicomponent.CButton();
        btnProofAdd = new com.see.truetransact.uicomponent.CButton();
        btnProofDelete = new com.see.truetransact.uicomponent.CButton();
        panCorpCustDet = new com.see.truetransact.uicomponent.CPanel();
        panCustomerSide = new com.see.truetransact.uicomponent.CPanel();
        panCustomerName = new com.see.truetransact.uicomponent.CPanel();
        lblValCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblDateOfBirth = new com.see.truetransact.uicomponent.CLabel();
        lblValDateOfBirth = new com.see.truetransact.uicomponent.CLabel();
        lblValStreet = new com.see.truetransact.uicomponent.CLabel();
        lblStreet1 = new com.see.truetransact.uicomponent.CLabel();
        lblArea1 = new com.see.truetransact.uicomponent.CLabel();
        lblValArea = new com.see.truetransact.uicomponent.CLabel();
        lblValCity = new com.see.truetransact.uicomponent.CLabel();
        lblCity1 = new com.see.truetransact.uicomponent.CLabel();
        lblCountry1 = new com.see.truetransact.uicomponent.CLabel();
        lblValCountry = new com.see.truetransact.uicomponent.CLabel();
        lblValPin = new com.see.truetransact.uicomponent.CLabel();
        lblPin = new com.see.truetransact.uicomponent.CLabel();
        lblState1 = new com.see.truetransact.uicomponent.CLabel();
        lblValState = new com.see.truetransact.uicomponent.CLabel();
        lblCorpCustDesig = new com.see.truetransact.uicomponent.CLabel();
        lblValCorpCustDesig = new com.see.truetransact.uicomponent.CLabel();
        panJointAcctHolder = new com.see.truetransact.uicomponent.CPanel();
        srpJointAcctHolder = new com.see.truetransact.uicomponent.CScrollPane();
        tblJointAcctHolder = new com.see.truetransact.uicomponent.CTable();
        panJointAcctButton = new com.see.truetransact.uicomponent.CPanel();
        btnJointAcctNew = new com.see.truetransact.uicomponent.CButton();
        btnJointAcctDel = new com.see.truetransact.uicomponent.CButton();
        btnJointAcctToMain = new com.see.truetransact.uicomponent.CButton();
        panCustId = new com.see.truetransact.uicomponent.CPanel();
        lblAuthCustId = new com.see.truetransact.uicomponent.CLabel();
        panAuthCustId = new com.see.truetransact.uicomponent.CPanel();
        txtAuthCustId = new com.see.truetransact.uicomponent.CTextField();
        btnAuthCustId = new com.see.truetransact.uicomponent.CButton();
        panCustomerHistory = new com.see.truetransact.uicomponent.CPanel();
        srpCustomerHistory = new com.see.truetransact.uicomponent.CScrollPane();
        tblCustomerHistory = new com.see.truetransact.uicomponent.CTable();
        lblDealingWith = new com.see.truetransact.uicomponent.CLabel();
        lblDealingPeriod = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDeleteDetails = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCorporateCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Corporate Customer");
        setPreferredSize(new java.awt.Dimension(870, 640));

        panCorporateCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panCorporateCustomer.setMinimumSize(new java.awt.Dimension(844, 490));
        panCorporateCustomer.setPreferredSize(new java.awt.Dimension(844, 490));
        panCorporateCustomer.setLayout(new java.awt.GridBagLayout());

        panContactsList.setMinimumSize(new java.awt.Dimension(244, 160));
        panContactsList.setPreferredSize(new java.awt.Dimension(244, 160));
        panContactsList.setLayout(new java.awt.GridBagLayout());

        panContacts.setBorder(javax.swing.BorderFactory.createTitledBorder("Contacts"));
        panContacts.setMinimumSize(new java.awt.Dimension(230, 165));
        panContacts.setName("panContacts"); // NOI18N
        panContacts.setPreferredSize(new java.awt.Dimension(230, 165));
        panContacts.setLayout(new java.awt.GridBagLayout());

        srpContactList.setMinimumSize(new java.awt.Dimension(220, 250));
        srpContactList.setPreferredSize(new java.awt.Dimension(220, 250));

        tblContactList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblContactListMousePressed(evt);
            }
        });
        srpContactList.setViewportView(tblContactList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContacts.add(srpContactList, gridBagConstraints);

        panContactControl.setMinimumSize(new java.awt.Dimension(220, 33));
        panContactControl.setPreferredSize(new java.awt.Dimension(220, 33));
        panContactControl.setLayout(new java.awt.GridBagLayout());

        btnContactToMain.setText("Set as Primary");
        btnContactToMain.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnContactToMain.setEnabled(false);
        btnContactToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactToMainActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panContactControl.add(btnContactToMain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContacts.add(panContactControl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panContactsList.add(panContacts, gridBagConstraints);

        lblCustomerID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactsList.add(lblCustomerID, gridBagConstraints);

        txtCustomerID.setEditable(false);
        txtCustomerID.setMaxLength(10);
        txtCustomerID.setMinimumSize(new java.awt.Dimension(150, 21));
        txtCustomerID.setPreferredSize(new java.awt.Dimension(150, 21));
        txtCustomerID.setValidation(new DefaultValidation());
        txtCustomerID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactsList.add(txtCustomerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        panCorporateCustomer.add(panContactsList, gridBagConstraints);

        panContactAndIdentityInfo.setLayout(new java.awt.GridBagLayout());

        tabContactAndIdentityInfo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabContactAndIdentityInfoStateChanged(evt);
            }
        });

        panContactInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact Info"));
        panContactInfo.setLayout(new java.awt.GridBagLayout());

        panAddress.setLayout(new java.awt.GridBagLayout());

        panAddressDetails.setLayout(new java.awt.GridBagLayout());

        panAddressType.setLayout(new java.awt.GridBagLayout());

        lblAddressType.setText("Address Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(lblAddressType, gridBagConstraints);

        txtStreet.setMaxLength(256);
        txtStreet.setMinimumSize(new java.awt.Dimension(200, 21));
        txtStreet.setPreferredSize(new java.awt.Dimension(200, 21));
        txtStreet.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(txtStreet, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(lblArea, gridBagConstraints);

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(lblStreet, gridBagConstraints);

        txtArea.setMaxLength(128);
        txtArea.setMinimumSize(new java.awt.Dimension(200, 21));
        txtArea.setPreferredSize(new java.awt.Dimension(200, 21));
        txtArea.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(txtArea, gridBagConstraints);

        cboAddressType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAddressType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAddressTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(cboAddressType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAddressDetails.add(panAddressType, gridBagConstraints);

        panCountryDetails.setLayout(new java.awt.GridBagLayout());

        panCountry.setLayout(new java.awt.GridBagLayout());

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCountry.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCity.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 4);
        panCountry.add(cboCity, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCountry.add(lblState, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 3);
        panCountry.add(cboState, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCountryDetails.add(panCountry, gridBagConstraints);

        panCity.setLayout(new java.awt.GridBagLayout());

        lblPincode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCity.add(lblPincode, gridBagConstraints);

        txtPincode.setMaxLength(16);
        txtPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPincode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCity.add(txtPincode, gridBagConstraints);

        lblCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCity.add(lblCountry, gridBagConstraints);

        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCity.add(cboCountry, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panCountryDetails.add(panCity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 44, 0, 0);
        panAddressDetails.add(panCountryDetails, gridBagConstraints);

        panRemarks.setLayout(new java.awt.GridBagLayout());

        lblAddrRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemarks.add(lblAddrRemarks, gridBagConstraints);

        txtAddrRemarks.setMinimumSize(new java.awt.Dimension(200, 21));
        txtAddrRemarks.setPreferredSize(new java.awt.Dimension(200, 21));
        txtAddrRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemarks.add(txtAddrRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
        panAddressDetails.add(panRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panAddress.add(panAddressDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 17, 0, 0);
        panContactInfo.add(panAddress, gridBagConstraints);

        panContactNo.setLayout(new java.awt.GridBagLayout());

        panTeleCommunication.setLayout(new java.awt.GridBagLayout());

        panTelecomDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTelecomDetails.setLayout(new java.awt.GridBagLayout());

        panPhoneType.setLayout(new java.awt.GridBagLayout());

        panPhoneSave.setLayout(new java.awt.GridBagLayout());

        btnContactNoAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnContactNoAdd.setEnabled(false);
        btnContactNoAdd.setMaximumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setMinimumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setPreferredSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactNoAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoneSave.add(btnContactNoAdd, gridBagConstraints);

        btnPhoneNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnPhoneNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.setEnabled(false);
        btnPhoneNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhoneNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoneSave.add(btnPhoneNew, gridBagConstraints);

        btnPhoneDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnPhoneDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.setEnabled(false);
        btnPhoneDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhoneDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoneSave.add(btnPhoneDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panPhoneType.add(panPhoneSave, gridBagConstraints);

        panPhoneAreaNumber.setLayout(new java.awt.GridBagLayout());

        lblPhoneType.setText("Phone Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblPhoneType, gridBagConstraints);

        lblPhoneNumber.setText("Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblPhoneNumber, gridBagConstraints);

        txtPhoneNumber.setMaxLength(16);
        txtPhoneNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(txtPhoneNumber, gridBagConstraints);

        lblAreaCode.setText("Area Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblAreaCode, gridBagConstraints);

        txtAreaCode.setMaxLength(16);
        txtAreaCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(txtAreaCode, gridBagConstraints);

        cboPhoneType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(cboPhoneType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panPhoneType.add(panPhoneAreaNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 1);
        panTelecomDetails.add(panPhoneType, gridBagConstraints);

        panPhoneList.setLayout(new java.awt.GridBagLayout());

        srpPhoneList.setMinimumSize(new java.awt.Dimension(200, 75));
        srpPhoneList.setPreferredSize(new java.awt.Dimension(200, 75));

        tblPhoneList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPhoneListMousePressed(evt);
            }
        });
        srpPhoneList.setViewportView(tblPhoneList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPhoneList.add(srpPhoneList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 2);
        panTelecomDetails.add(panPhoneList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTeleCommunication.add(panTelecomDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panContactNo.add(panTeleCommunication, gridBagConstraints);

        btnContactAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnContactAdd.setText("Save Contact");
        btnContactAdd.setEnabled(false);
        btnContactAdd.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnContactAdd.setMaximumSize(new java.awt.Dimension(110, 25));
        btnContactAdd.setMinimumSize(new java.awt.Dimension(130, 25));
        btnContactAdd.setPreferredSize(new java.awt.Dimension(130, 25));
        btnContactAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panContactNo.add(btnContactAdd, gridBagConstraints);

        btnContactNew.setText("New");
        btnContactNew.setEnabled(false);
        btnContactNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panContactNo.add(btnContactNew, gridBagConstraints);

        btnContactDelete.setText("Delete");
        btnContactDelete.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnContactDelete.setEnabled(false);
        btnContactDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panContactNo.add(btnContactDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panContactInfo.add(panContactNo, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Contact Info", panContactInfo);

        panPhotoSign.setLayout(new java.awt.GridBagLayout());

        panPhoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Photograph"));
        panPhoto.setLayout(new java.awt.GridBagLayout());

        srpPhotoLoad.setMinimumSize(new java.awt.Dimension(0, 0));
        srpPhotoLoad.setPreferredSize(new java.awt.Dimension(120, 150));

        lblPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPhotoMouseClicked(evt);
            }
        });
        srpPhotoLoad.setViewportView(lblPhoto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhoto.add(srpPhotoLoad, gridBagConstraints);

        panPhotoButtons.setLayout(new java.awt.GridBagLayout());

        btnPhotoLoad.setText("Load");
        btnPhotoLoad.setPreferredSize(new java.awt.Dimension(73, 25));
        btnPhotoLoad.setEnabled(false);
        btnPhotoLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoLoadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhotoButtons.add(btnPhotoLoad, gridBagConstraints);

        btnPhotoRemove.setText("Remove");
        btnPhotoRemove.setEnabled(false);
        btnPhotoRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhotoButtons.add(btnPhotoRemove, gridBagConstraints);

        btnPhotoSave.setText("Save");
        btnPhotoSave.setMaximumSize(new java.awt.Dimension(73, 25));
        btnPhotoSave.setMinimumSize(new java.awt.Dimension(73, 25));
        btnPhotoSave.setPreferredSize(new java.awt.Dimension(73, 25));
        btnPhotoSave.setEnabled(false);
        btnPhotoSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhotoButtons.add(btnPhotoSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPhoto.add(panPhotoButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panPhotoSign.add(panPhoto, gridBagConstraints);

        panSign.setBorder(javax.swing.BorderFactory.createTitledBorder("Signature"));
        panSign.setLayout(new java.awt.GridBagLayout());

        srpSignLoad.setMinimumSize(new java.awt.Dimension(0, 0));
        srpSignLoad.setPreferredSize(new java.awt.Dimension(120, 150));

        lblSign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSignMouseClicked(evt);
            }
        });
        srpSignLoad.setViewportView(lblSign);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSign.add(srpSignLoad, gridBagConstraints);

        panSignButtons.setLayout(new java.awt.GridBagLayout());

        btnSignLoad.setText("Load");
        btnSignLoad.setPreferredSize(new java.awt.Dimension(73, 25));
        btnSignLoad.setEnabled(false);
        btnSignLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignLoadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSignButtons.add(btnSignLoad, gridBagConstraints);

        btnSignRemove.setText("Remove");
        btnSignRemove.setEnabled(false);
        btnSignRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSignButtons.add(btnSignRemove, gridBagConstraints);

        btnSignSave.setText("Save");
        btnSignSave.setMaximumSize(new java.awt.Dimension(73, 25));
        btnSignSave.setMinimumSize(new java.awt.Dimension(73, 25));
        btnSignSave.setPreferredSize(new java.awt.Dimension(73, 25));
        btnSignSave.setEnabled(false);
        btnSignSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSignButtons.add(btnSignSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panSign.add(panSignButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPhotoSign.add(panSign, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Auth Person Photo & Signature", panPhotoSign);

        panAdditionalInfo.setLayout(new java.awt.GridBagLayout());

        lblTransPwd.setText("Transaction Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblTransPwd, gridBagConstraints);

        lblCustUserid.setText("User Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblCustUserid, gridBagConstraints);

        txtCustUserid.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(txtCustUserid, gridBagConstraints);

        lblCustPwd.setText("Login Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblCustPwd, gridBagConstraints);

        lblWebSite.setText("Company Web Site");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblWebSite, gridBagConstraints);

        txtWebSite.setPreferredSize(new java.awt.Dimension(150, 21));
        txtWebSite.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(txtWebSite, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(txtCustPwd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(txtTransPwd, gridBagConstraints);

        txtBankruptsy.setMinimumSize(new java.awt.Dimension(200, 21));
        txtBankruptsy.setPreferredSize(new java.awt.Dimension(200, 21));
        txtBankruptsy.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(txtBankruptsy, gridBagConstraints);

        lblBankruptsy.setText("Bankruptcy Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblBankruptsy, gridBagConstraints);

        lblAmsam.setText("Amsam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblAmsam, gridBagConstraints);

        cbcomboAmsam.setMinimumSize(new java.awt.Dimension(80, 18));
        cbcomboAmsam.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(cbcomboAmsam, gridBagConstraints);

        lblDesam.setText("Desam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblDesam, gridBagConstraints);

        cbcomboDesam.setMinimumSize(new java.awt.Dimension(80, 18));
        cbcomboDesam.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(cbcomboDesam, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Additional Info", panAdditionalInfo);

        panFinancialDetails.setLayout(new java.awt.GridBagLayout());

        panCapital.setMinimumSize(new java.awt.Dimension(235, 101));
        panCapital.setPreferredSize(new java.awt.Dimension(235, 92));
        panCapital.setLayout(new java.awt.GridBagLayout());

        lblAuthCapital.setText("Authorized Capital");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(lblAuthCapital, gridBagConstraints);

        txtAuthCapital.setMinimumSize(new java.awt.Dimension(101, 24));
        txtAuthCapital.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(txtAuthCapital, gridBagConstraints);

        lblIssuedCapital.setText("Issued Capital");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(lblIssuedCapital, gridBagConstraints);

        txtIssuedCapital.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIssuedCapital.setValidation(new CurrencyValidation(14,2));
        txtIssuedCapital.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIssuedCapitalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(txtIssuedCapital, gridBagConstraints);

        lblSubscribedCapital.setText("Subscribed Capital");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(lblSubscribedCapital, gridBagConstraints);

        txtSubscribedCapital.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSubscribedCapital.setValidation(new CurrencyValidation(14,2));
        txtSubscribedCapital.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubscribedCapitalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(txtSubscribedCapital, gridBagConstraints);

        tdtFinacialYrEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFinacialYrEndFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(tdtFinacialYrEnd, gridBagConstraints);

        lblFinancialyrEnd.setText("Financial Year End");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCapital.add(lblFinancialyrEnd, gridBagConstraints);

        panFinancialDetails.add(panCapital, new java.awt.GridBagConstraints());

        panOthers.setMinimumSize(new java.awt.Dimension(600, 160));
        panOthers.setPreferredSize(new java.awt.Dimension(600, 160));
        panOthers.setLayout(new java.awt.GridBagLayout());

        lblTotalResource.setText("Total Resource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblTotalResource, gridBagConstraints);

        txtTotalResource.setNextFocusableComponent(txtTotalIncome);
        txtTotalResource.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(txtTotalResource, gridBagConstraints);

        lblLastYrPL.setText("Last Yeat Profit/Loss");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblLastYrPL, gridBagConstraints);

        txtLastYrPL.setNextFocusableComponent(txtDividendPercentage);
        txtLastYrPL.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(txtLastYrPL, gridBagConstraints);

        lblDividendPercentage.setText("Dividend Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblDividendPercentage, gridBagConstraints);

        txtDividendPercentage.setValidation(new PercentageValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(txtDividendPercentage, gridBagConstraints);

        lblDividendPercentage1.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblDividendPercentage1, gridBagConstraints);

        txtTotalIncome.setNextFocusableComponent(txtTotalNonTaxExp);
        txtTotalIncome.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(txtTotalIncome, gridBagConstraints);

        lblTotalIncome.setText("Total Income");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblTotalIncome, gridBagConstraints);

        txtTotalNonTaxExp.setNextFocusableComponent(txtprofitBefTax);
        txtTotalNonTaxExp.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(txtTotalNonTaxExp, gridBagConstraints);

        lblTotalNonTaxExp.setText("Total Non Tax Expenditure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblTotalNonTaxExp, gridBagConstraints);

        txtprofitBefTax.setNextFocusableComponent(txtTaxliability);
        txtprofitBefTax.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(txtprofitBefTax, gridBagConstraints);

        lblProfitBeforeTax.setText("Profit Before Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblProfitBeforeTax, gridBagConstraints);

        lblLiablityTax.setText("Tax Liability");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(lblLiablityTax, gridBagConstraints);

        txtTaxliability.setNextFocusableComponent(txtLastYrPL);
        txtTaxliability.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOthers.add(txtTaxliability, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panFinancialDetails.add(panOthers, gridBagConstraints);

        cSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panFinancialDetails.add(cSeparator1, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Financial Details", panFinancialDetails);

        panCorporateInfo.setLayout(new java.awt.GridBagLayout());

        lblCrAvldSince.setText("Credit Facilities Availed Since");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(lblCrAvldSince, gridBagConstraints);

        lblRiskRate.setText("Risk Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(lblRiskRate, gridBagConstraints);

        lblBusNature.setText("Nature Of Business");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(lblBusNature, gridBagConstraints);

        lblRegNumber.setText("Company Reg. No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(lblRegNumber, gridBagConstraints);

        lblDtEstablished.setText("Date Of Establishment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(lblDtEstablished, gridBagConstraints);

        lblCEO.setText("Chief Executive Officer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(lblCEO, gridBagConstraints);

        txtRegNumber.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(txtRegNumber, gridBagConstraints);

        txtCEO.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(txtCEO, gridBagConstraints);

        cboBusNature.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBusNature.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(cboBusNature, gridBagConstraints);

        tdtDtEstablished.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDtEstablishedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(tdtDtEstablished, gridBagConstraints);

        cSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panCorporateInfo.add(cSeparator2, gridBagConstraints);

        tdtCrAvldSince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCrAvldSinceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(tdtCrAvldSince, gridBagConstraints);

        txtRiskRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRiskRate.setValidation(new PercentageValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCorporateInfo.add(txtRiskRate, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Other Info", panCorporateInfo);

        panSuspendCustomer.setLayout(new java.awt.GridBagLayout());

        lblSuspendCust.setText("Suspend Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSuspendCustomer.add(lblSuspendCust, gridBagConstraints);

        chkSuspendCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSuspendCustActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSuspendCustomer.add(chkSuspendCust, gridBagConstraints);

        tdtSuspendCustFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSuspendCustFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSuspendCustomer.add(tdtSuspendCustFrom, gridBagConstraints);

        lblSuspendDate.setText("Suspended Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSuspendCustomer.add(lblSuspendDate, gridBagConstraints);

        chkRevokeCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRevokeCustActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSuspendCustomer.add(chkRevokeCust, gridBagConstraints);

        lblRevokeCust.setText("Revoke Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSuspendCustomer.add(lblRevokeCust, gridBagConstraints);

        txtSuspRevRemarks.setMaxLength(256);
        txtSuspRevRemarks.setMinimumSize(new java.awt.Dimension(200, 21));
        txtSuspRevRemarks.setName("txtRemarks"); // NOI18N
        txtSuspRevRemarks.setPreferredSize(new java.awt.Dimension(200, 21));
        txtSuspRevRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSuspendCustomer.add(txtSuspRevRemarks, gridBagConstraints);

        tdtRevokedCustDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRevokedCustDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSuspendCustomer.add(tdtRevokedCustDate, gridBagConstraints);

        lblRevokeDate.setText("Revoked Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSuspendCustomer.add(lblRevokeDate, gridBagConstraints);

        lblSuspendRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSuspendCustomer.add(lblSuspendRemarks, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Suspend Customer", panSuspendCustomer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panContactAndIdentityInfo.add(tabContactAndIdentityInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panCorporateCustomer.add(panContactAndIdentityInfo, gridBagConstraints);

        tabCorpCust.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabCorpCustFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabCorpCustFocusLost(evt);
            }
        });
        tabCorpCust.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabCorpCustStateChanged(evt);
            }
        });
        tabCorpCust.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabCorpCustMouseClicked(evt);
            }
        });

        panCompanyAuthPersonInfo.setPreferredSize(new java.awt.Dimension(590, 265));
        panCompanyAuthPersonInfo.setLayout(new java.awt.GridBagLayout());

        panCompanyInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Company Info"));
        panCompanyInfo.setMinimumSize(new java.awt.Dimension(286, 120));
        panCompanyInfo.setPreferredSize(new java.awt.Dimension(286, 245));
        panCompanyInfo.setLayout(new java.awt.GridBagLayout());

        lblCompany.setText("Company");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblCompany, gridBagConstraints);

        txtCompany.setMaxLength(128);
        txtCompany.setMinimumSize(new java.awt.Dimension(250, 21));
        txtCompany.setPreferredSize(new java.awt.Dimension(150, 21));
        txtCompany.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(txtCompany, gridBagConstraints);

        lblCustomerType.setText("Customer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblCustomerType, gridBagConstraints);

        cboCustomerType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCustomerType.setPopupWidth(200);
        cboCustomerType.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(cboCustomerType, gridBagConstraints);

        lblRelationManager.setText("Relation Manager");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblRelationManager, gridBagConstraints);

        cboRelationManager.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRelationManager.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(cboRelationManager, gridBagConstraints);

        lblNetWorth.setText("NetWorth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblNetWorth, gridBagConstraints);

        txtNetWorth.setMaxLength(40);
        txtNetWorth.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNetWorth.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(txtNetWorth, gridBagConstraints);

        lblEmailID.setText("Email ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblEmailID, gridBagConstraints);

        lblPrefCommunication.setText("Pref. Communication");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblPrefCommunication, gridBagConstraints);

        cboPrefCommunication.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(cboPrefCommunication, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMaxLength(256);
        txtRemarks.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(150, 21));
        txtRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(txtRemarks, gridBagConstraints);

        lblIntroType.setText("Introducer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblIntroType, gridBagConstraints);

        cboIntroType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIntroType.setPopupWidth(200);
        cboIntroType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntroTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(cboIntroType, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        txtEmailID.setMaxLength(64);
        txtEmailID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmailID.setName("txtEmailID"); // NOI18N
        txtEmailID.setValidation(new EmailValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSearch.add(txtEmailID, gridBagConstraints);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif"))); // NOI18N
        btnSearch.setText("Search");
        btnSearch.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSearch.setMaximumSize(new java.awt.Dimension(65, 21));
        btnSearch.setMinimumSize(new java.awt.Dimension(65, 21));
        btnSearch.setPreferredSize(new java.awt.Dimension(65, 21));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panSearch.add(btnSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(panSearch, gridBagConstraints);

        lblNetWorthAsOn.setText("As On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblNetWorthAsOn, gridBagConstraints);

        tdtNetWorthAsOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtNetWorthAsOnFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(tdtNetWorthAsOn, gridBagConstraints);

        lblCreatedDt.setText("Created Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCompanyInfo.add(lblCreatedDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblCreatedDt1, gridBagConstraints);

        cboMembershipClass.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMembershipClass.setName("cboCustomerType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(cboMembershipClass, gridBagConstraints);

        lblMembershipClass.setText("Membership Class");
        lblMembershipClass.setName("lblCustomerType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCompanyInfo.add(lblMembershipClass, gridBagConstraints);

        txtMemNum.setMaxLength(48);
        txtMemNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemNum.setName("txtNationality"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(txtMemNum, gridBagConstraints);

        lblMemNum.setText("Membership No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblMemNum, gridBagConstraints);

        panITDetails.setMinimumSize(new java.awt.Dimension(140, 23));
        panITDetails.setPreferredSize(new java.awt.Dimension(150, 23));
        panITDetails.setLayout(new java.awt.GridBagLayout());

        rdoITDec_Pan.setText("Pan");
        rdoITDec_Pan.setMaximumSize(new java.awt.Dimension(60, 27));
        rdoITDec_Pan.setMinimumSize(new java.awt.Dimension(60, 27));
        rdoITDec_Pan.setPreferredSize(new java.awt.Dimension(60, 27));
        rdoITDec_Pan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoITDec_PanActionPerformed(evt);
            }
        });
        panITDetails.add(rdoITDec_Pan, new java.awt.GridBagConstraints());

        rdoITDec_F60.setText("Form-60");
        rdoITDec_F60.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoITDec_F60.setMaximumSize(new java.awt.Dimension(61, 27));
        rdoITDec_F60.setMinimumSize(new java.awt.Dimension(76, 27));
        rdoITDec_F60.setPreferredSize(new java.awt.Dimension(76, 27));
        rdoITDec_F60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoITDec_F60ActionPerformed(evt);
            }
        });
        panITDetails.add(rdoITDec_F60, new java.awt.GridBagConstraints());

        rdoITDec_F61.setText("Form-61");
        rdoITDec_F61.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoITDec_F61.setMaximumSize(new java.awt.Dimension(61, 27));
        rdoITDec_F61.setMinimumSize(new java.awt.Dimension(76, 27));
        rdoITDec_F61.setPreferredSize(new java.awt.Dimension(76, 27));
        rdoITDec_F61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoITDec_F61ActionPerformed(evt);
            }
        });
        panITDetails.add(rdoITDec_F61, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 4, 4);
        panCompanyInfo.add(panITDetails, gridBagConstraints);

        txtPanNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPanNumber.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(txtPanNumber, gridBagConstraints);

        lblPanNumber.setText("PAN Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCompanyInfo.add(lblPanNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCompanyAuthPersonInfo.add(panCompanyInfo, gridBagConstraints);

        tabCorpCust.addTab("Company Details", panCompanyAuthPersonInfo);

        panKYC.setLayout(new java.awt.GridBagLayout());

        proofPanel.setMinimumSize(new java.awt.Dimension(350, 300));
        proofPanel.setPreferredSize(new java.awt.Dimension(350, 300));
        proofPanel.setLayout(new java.awt.GridBagLayout());

        lblAddrVerified.setText("Address Verified");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        proofPanel.add(lblAddrVerified, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        proofPanel.add(chkAddrVerified, gridBagConstraints);

        lblPhVerified.setText("Phone Number Verified");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        proofPanel.add(lblPhVerified, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        proofPanel.add(chkPhVerified, gridBagConstraints);

        lblFinanceStmtVerified.setText("Obtained Financial Statement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        proofPanel.add(lblFinanceStmtVerified, gridBagConstraints);

        cboAddrProof.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        proofPanel.add(cboAddrProof, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        proofPanel.add(chkFinanceStmtVerified, gridBagConstraints);

        lblAddrProof.setText("Address Proof");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        proofPanel.add(lblAddrProof, gridBagConstraints);

        panProofDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        panProofDetails.setMinimumSize(new java.awt.Dimension(200, 55));
        panProofDetails.setPreferredSize(new java.awt.Dimension(200, 55));
        panProofDetails.setLayout(new java.awt.GridBagLayout());

        lblIdenProof.setText("Identity Proof");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 21, 0, 0);
        panProofDetails.add(lblIdenProof, gridBagConstraints);

        cboIdenProof.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIdenProof.setName("cboCareOf"); // NOI18N
        cboIdenProof.setPopupWidth(150);
        cboIdenProof.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIdenProofActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 10);
        panProofDetails.add(cboIdenProof, gridBagConstraints);

        lblUniqueNo.setText("Unique No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 33, 0, 0);
        panProofDetails.add(lblUniqueNo, gridBagConstraints);

        txtUniqueId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 10);
        panProofDetails.add(txtUniqueId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 0, 8);
        proofPanel.add(panProofDetails, gridBagConstraints);

        panKYC.add(proofPanel, new java.awt.GridBagConstraints());

        panProof.setBorder(javax.swing.BorderFactory.createTitledBorder("Proof"));
        panProof.setMinimumSize(new java.awt.Dimension(300, 324));
        panProof.setName("panProof"); // NOI18N
        panProof.setLayout(new java.awt.GridBagLayout());

        srpProofList.setMinimumSize(new java.awt.Dimension(210, 250));
        srpProofList.setName("srpProofList"); // NOI18N

        tblProofList.setName("tblProofList"); // NOI18N
        tblProofList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblProofListMousePressed(evt);
            }
        });
        srpProofList.setViewportView(tblProofList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panProof.add(srpProofList, gridBagConstraints);

        panProofControl.setMinimumSize(new java.awt.Dimension(210, 33));
        panProofControl.setName("panContactControl"); // NOI18N
        panProofControl.setPreferredSize(new java.awt.Dimension(210, 33));
        panProofControl.setLayout(new java.awt.GridBagLayout());

        btnProofNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnProofNew.setEnabled(false);
        btnProofNew.setMaximumSize(new java.awt.Dimension(25, 25));
        btnProofNew.setMinimumSize(new java.awt.Dimension(25, 25));
        btnProofNew.setName("btnProofNew"); // NOI18N
        btnProofNew.setPreferredSize(new java.awt.Dimension(25, 25));
        btnProofNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProofNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panProofControl.add(btnProofNew, gridBagConstraints);

        btnProofAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnProofAdd.setEnabled(false);
        btnProofAdd.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnProofAdd.setMaximumSize(new java.awt.Dimension(27, 27));
        btnProofAdd.setMinimumSize(new java.awt.Dimension(27, 27));
        btnProofAdd.setName("btnProofAdd"); // NOI18N
        btnProofAdd.setPreferredSize(new java.awt.Dimension(27, 27));
        btnProofAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProofAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProofControl.add(btnProofAdd, gridBagConstraints);

        btnProofDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnProofDelete.setEnabled(false);
        btnProofDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnProofDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnProofDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnProofDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProofDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProofControl.add(btnProofDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProof.add(panProofControl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 3, 2);
        panKYC.add(panProof, gridBagConstraints);

        tabCorpCust.addTab("KYC", panKYC);

        panCorpCustDet.setLayout(new java.awt.GridBagLayout());

        panCustomerSide.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panCustomerSide.setLayout(new java.awt.GridBagLayout());

        panCustomerName.setLayout(new java.awt.GridBagLayout());

        lblValCustomerName.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValCustomerName.setMinimumSize(new java.awt.Dimension(185, 10));
        lblValCustomerName.setPreferredSize(new java.awt.Dimension(185, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 37);
        panCustomerName.add(lblValCustomerName, gridBagConstraints);

        lblCustomerName.setText("Customer Name");
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblCustomerName, gridBagConstraints);

        lblDateOfBirth.setText("Date of Birth");
        lblDateOfBirth.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblDateOfBirth, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValDateOfBirth, gridBagConstraints);

        lblValStreet.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValStreet.setMinimumSize(new java.awt.Dimension(200, 10));
        lblValStreet.setPreferredSize(new java.awt.Dimension(200, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValStreet, gridBagConstraints);

        lblStreet1.setText("Street");
        lblStreet1.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblStreet1, gridBagConstraints);

        lblArea1.setText("Area");
        lblArea1.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblArea1, gridBagConstraints);

        lblValArea.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValArea.setMinimumSize(new java.awt.Dimension(70, 10));
        lblValArea.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValArea, gridBagConstraints);

        lblValCity.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValCity.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValCity.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValCity, gridBagConstraints);

        lblCity1.setText("City");
        lblCity1.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblCity1, gridBagConstraints);

        lblCountry1.setText("Country");
        lblCountry1.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblCountry1, gridBagConstraints);

        lblValCountry.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValCountry.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValCountry.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValCountry, gridBagConstraints);

        lblValPin.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValPin.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValPin.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValPin, gridBagConstraints);

        lblPin.setText("Pin");
        lblPin.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblPin, gridBagConstraints);

        lblState1.setText("State");
        lblState1.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblState1, gridBagConstraints);

        lblValState.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValState.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValState.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValState, gridBagConstraints);

        lblCorpCustDesig.setText("Designation");
        lblCorpCustDesig.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblCorpCustDesig, gridBagConstraints);

        lblValCorpCustDesig.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValCorpCustDesig.setMinimumSize(new java.awt.Dimension(70, 10));
        lblValCorpCustDesig.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValCorpCustDesig, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 41, 104);
        panCustomerSide.add(panCustomerName, gridBagConstraints);

        panJointAcctHolder.setLayout(new java.awt.GridBagLayout());

        srpJointAcctHolder.setMinimumSize(new java.awt.Dimension(250, 100));
        srpJointAcctHolder.setPreferredSize(new java.awt.Dimension(250, 100));

        tblJointAcctHolder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Cust. Id", "Type", "Main / Joint"
            }
        ));
        tblJointAcctHolder.setMinimumSize(new java.awt.Dimension(300, 100));
        tblJointAcctHolder.setPreferredSize(null);
        tblJointAcctHolder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblJointAcctHolderMousePressed(evt);
            }
        });
        srpJointAcctHolder.setViewportView(tblJointAcctHolder);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 11);
        panJointAcctHolder.add(srpJointAcctHolder, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panCustomerSide.add(panJointAcctHolder, gridBagConstraints);

        panJointAcctButton.setLayout(new java.awt.GridBagLayout());

        btnJointAcctNew.setText("New");
        btnJointAcctNew.setMinimumSize(new java.awt.Dimension(59, 24));
        btnJointAcctNew.setPreferredSize(new java.awt.Dimension(59, 24));
        btnJointAcctNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJointAcctNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panJointAcctButton.add(btnJointAcctNew, gridBagConstraints);

        btnJointAcctDel.setText("Delete");
        btnJointAcctDel.setMaximumSize(new java.awt.Dimension(71, 24));
        btnJointAcctDel.setMinimumSize(new java.awt.Dimension(71, 24));
        btnJointAcctDel.setPreferredSize(new java.awt.Dimension(71, 24));
        btnJointAcctDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJointAcctDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panJointAcctButton.add(btnJointAcctDel, gridBagConstraints);

        btnJointAcctToMain.setText("To Main");
        btnJointAcctToMain.setMinimumSize(new java.awt.Dimension(81, 24));
        btnJointAcctToMain.setPreferredSize(new java.awt.Dimension(81, 24));
        btnJointAcctToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJointAcctToMainActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panJointAcctButton.add(btnJointAcctToMain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCustomerSide.add(panJointAcctButton, gridBagConstraints);

        panCustId.setLayout(new java.awt.GridBagLayout());

        lblAuthCustId.setText("Main Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustId.add(lblAuthCustId, gridBagConstraints);

        panAuthCustId.setLayout(new java.awt.GridBagLayout());

        txtAuthCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAuthCustId.setValidation(new DefaultValidation());
        txtAuthCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuthCustIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAuthCustId.add(txtAuthCustId, gridBagConstraints);

        btnAuthCustId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAuthCustId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAuthCustId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAuthCustId.setEnabled(false);
        btnAuthCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthCustIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAuthCustId.add(btnAuthCustId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panCustId.add(panAuthCustId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCustomerSide.add(panCustId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCorpCustDet.add(panCustomerSide, gridBagConstraints);

        tabCorpCust.addTab("Authorized Persons", panCorpCustDet);

        panCustomerHistory.setLayout(new java.awt.GridBagLayout());

        srpCustomerHistory.setMinimumSize(new java.awt.Dimension(600, 200));
        srpCustomerHistory.setPreferredSize(new java.awt.Dimension(600, 404));

        tblCustomerHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Customer Id", "Relationship", "Product Id", "Product Type", "Account No.", "From", "To", "Deposit Amount", "Maturity Amount", "Available Balance"
            }
        ));
        srpCustomerHistory.setViewportView(tblCustomerHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerHistory.add(srpCustomerHistory, gridBagConstraints);

        lblDealingWith.setText("Dealing with the bank since :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerHistory.add(lblDealingWith, gridBagConstraints);

        lblDealingPeriod.setMaximumSize(new java.awt.Dimension(200, 21));
        lblDealingPeriod.setMinimumSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerHistory.add(lblDealingPeriod, gridBagConstraints);

        tabCorpCust.addTab("Cust.360", panCustomerHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCorporateCustomer.add(tabCorpCust, gridBagConstraints);

        getContentPane().add(panCorporateCustomer, java.awt.BorderLayout.CENTER);

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace30);

        btnDeleteDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeleteDetails.setToolTipText("Enquiry Of Closed  Corporates");
        btnDeleteDetails.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDeleteDetails.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDeleteDetails.setEnabled(false);
        btnDeleteDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDetailsActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDeleteDetails);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrCorporateCustomer.add(mnuProcess);

        setJMenuBar(mbrCorporateCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblSignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSignMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==2) {
            objCustomerUISupport.zoomImage(lblSign);
        }
    }//GEN-LAST:event_lblSignMouseClicked

    private void lblPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPhotoMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==2) {
            objCustomerUISupport.zoomImage(lblPhoto);
        }
    }//GEN-LAST:event_lblPhotoMouseClicked

    private void tdtRevokedCustDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRevokedCustDateFocusLost
        // TODO add your handling code here:
         Date cur_date=(Date) currDt.clone();
        ClientUtil.validateToDate(tdtRevokedCustDate, DateUtil.getStringDate(DateUtil.addDays(cur_date, -1)));
        boolean chk= chkRevokeCust.isSelected();
        if(!chk){
            ClientUtil.displayAlert("Select The Revoke Option First!!!!!!!!");
            tdtRevokedCustDate.setDateValue("");
        }
    }//GEN-LAST:event_tdtRevokedCustDateFocusLost

    private void tdtSuspendCustFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSuspendCustFromFocusLost
        // TODO add your handling code here:
          // TODO add your handling code here:
        Date cur_date=(Date) currDt.clone();
        ClientUtil.validateToDate(tdtSuspendCustFrom, DateUtil.getStringDate(DateUtil.addDays(cur_date, -1)));
        boolean chk= chkSuspendCust.isSelected();
        if(!chk){
            ClientUtil.displayAlert("Select The Suspend Option First!!!!!!!!");
            tdtSuspendCustFrom.setDateValue("");
        }
    }//GEN-LAST:event_tdtSuspendCustFromFocusLost

    private void chkRevokeCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRevokeCustActionPerformed
        // TODO add your handling code here:
         // TODO add your handling code here:
        boolean ck= chkRevokeCust.isSelected();
        if(ck){
        tdtRevokedCustDate.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
        chkSuspendCust.setSelected(false);
        tdtSuspendCustFrom.setDateValue("");
        }
        else
            tdtRevokedCustDate.setDateValue("");
    }//GEN-LAST:event_chkRevokeCustActionPerformed

    private void chkSuspendCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSuspendCustActionPerformed
        // TODO add your handling code here:
        boolean ck=chkSuspendCust.isSelected();
        if(ck){
            HashMap relationMap = new HashMap();
            relationMap.put("CUST_ID",txtCustomerID.getText());
            List lst = ClientUtil.executeQuery("getSelectCustomerHistory", relationMap);
            if(lst!=null && lst.size()>0){
                ClientUtil.displayAlert("Cannot Suspend Customer!! Refer Role Info Tab");
                tdtSuspendCustFrom.setDateValue("");
                chkSuspendCust.setSelected(false);
            }
            else{
        tdtSuspendCustFrom.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
        chkRevokeCust.setSelected(false);
        tdtRevokedCustDate.setDateValue("");
            }
        }
        else
            tdtSuspendCustFrom.setDateValue("");    
    }//GEN-LAST:event_chkSuspendCustActionPerformed

    private void tdtFinacialYrEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFinacialYrEndFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtFinacialYrEnd);
    }//GEN-LAST:event_tdtFinacialYrEndFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
         popUp(150);
          btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnDeleteDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDetailsActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
         popUp(100);
        btnCheck();
        observable.setStatus();
    }//GEN-LAST:event_btnDeleteDetailsActionPerformed

    private void txtAuthCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuthCustIdActionPerformed
        // TODO add your handling code here:
        if (txtAuthCustId.getText().length()>0) {
            viewType = AUTH_CUST_ID;
            HashMap fillHash = new HashMap();
            fillHash.put("CUSTOMER ID", txtAuthCustId.getText());
            fillData(fillHash);
        }
    }//GEN-LAST:event_txtAuthCustIdActionPerformed

    private void btnSignSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignSaveActionPerformed
        // TODO add your handling code here:
        int option=0;
        if (tblJointAcctHolder.getSelectedRow()>=0){
           option = ClientUtil.confirmationAlert("This sign assigned to "+tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(),0)+".  Are u sure?");
           if (option==0) {
           if (totAuthPerMap==null) totAuthPerMap = new HashMap();
               photoSignMap.put("signCreated", Boolean.valueOf(signCreated));
               totAuthPerMap.put(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(),1), photoSignMap);
               btnSignSave.setEnabled(false);
           }
        }
    }//GEN-LAST:event_btnSignSaveActionPerformed

    private void btnPhotoSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoSaveActionPerformed
        // TODO add your handling code here:
        int option=0;
        if (tblJointAcctHolder.getSelectedRow()>=0){
           option = ClientUtil.confirmationAlert("This photo assigned to "+tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(),0)+".  Are u sure?");
           if (option==0) {
               if (totAuthPerMap==null) totAuthPerMap = new HashMap();
               photoSignMap.put("photoCreated", Boolean.valueOf(photoCreated));
               totAuthPerMap.put(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(),1), photoSignMap);
               btnPhotoSave.setEnabled(false);
           }
        }        
    }//GEN-LAST:event_btnPhotoSaveActionPerformed

    private void tabCorpCustMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCorpCustMouseClicked
        // TODO add your handling code here:
        tabCorpFocused=true;
    }//GEN-LAST:event_tabCorpCustMouseClicked

    private void tabCorpCustFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabCorpCustFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tabCorpCustFocusLost

    private void tabCorpCustFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabCorpCustFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tabCorpCustFocusGained

    private void tabContactAndIdentityInfoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabContactAndIdentityInfoStateChanged
        // TODO add your handling code here:
        if (tabCorpFocused) {
            if (tabCorpCust.getTitleAt(tabCorpCust.getSelectedIndex()).equals("Authorized Persons")) {
               tabContactAndIdentityInfo.setSelectedIndex(1); 
            }
        }
    }//GEN-LAST:event_tabContactAndIdentityInfoStateChanged

    private void tabCorpCustStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabCorpCustStateChanged
        // TODO add your handling code here:
        if (tabCorpFocused) {
            System.out.println("#### Tab Title : "+tabCorpCust.getTitleAt(tabCorpCust.getSelectedIndex())+"  tab index : "+tabCorpCust.getSelectedIndex());
            if (tabCorpCust.getTitleAt(tabCorpCust.getSelectedIndex()).equals("Authorized Persons")) {
                tabContactAndIdentityInfo.setSelectedIndex(1); 
            }
        }
    }//GEN-LAST:event_tabCorpCustStateChanged

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void tdtDtEstablishedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDtEstablishedFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtDtEstablished);
        ClientUtil.validateFromDate(tdtDtEstablished, tdtCrAvldSince.getDateValue());
    }//GEN-LAST:event_tdtDtEstablishedFocusLost
    
    private void tdtCrAvldSinceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCrAvldSinceFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtCrAvldSince);
    }//GEN-LAST:event_tdtCrAvldSinceFocusLost
    
    private void txtSubscribedCapitalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubscribedCapitalFocusLost
        // TODO add your handling code here:
        if(!txtAuthCapital.getText().equals("") && !txtSubscribedCapital.getText().equals("")&& !txtIssuedCapital.getText().equals("")){
            captitalSubScribeValidation(txtIssuedCapital, txtSubscribedCapital);
        } else
            txtSubscribedCapital.setText("");
            
    }//GEN-LAST:event_txtSubscribedCapitalFocusLost
    
    private void txtIssuedCapitalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIssuedCapitalFocusLost
        // TODO add your handling code here:
        if(!txtAuthCapital.getText().equals("") && !txtIssuedCapital.getText().equals("")){
            captitalValidation(txtAuthCapital, txtIssuedCapital);
        }  else
            txtIssuedCapital.setText("");
    }//GEN-LAST:event_txtIssuedCapitalFocusLost
        
    private void tblJointAcctHolderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblJointAcctHolderMousePressed
//        if(observable.getActionType()!=(ClientConstants.ACTIONTYPE_DELETE)) { //--- If it is not in Delete mode, select the row
            tblJointAcctHolderRowSelected(tblJointAcctHolder.getSelectedRow());
//        }
    }//GEN-LAST:event_tblJointAcctHolderMousePressed
    /** Populates the Joint Account Holder data in the Screen
     *  for the row passed as argument.
     *  @param  int rowSelected is passed as argument
     */
    private void tblJointAcctHolderRowSelected(int rowSelected){
        if(tblJointAcctHolder.getSelectedRow()!=0){
            setBtnJointAccnt(true);
            //        } else if(cboConstitution.getSelectedItem().equals("Joint Account")){
            //            setBtnJointAccnt(false);
            //            btnJointAcctNew.setEnabled(true);
        } else {
            setBtnJointAccnt(false);
            btnJointAcctNew.setEnabled(true);
        }
        if ((viewType == AUTHORIZE) || (observable.getActionType() ==(ClientConstants.ACTIONTYPE_DELETE))){
             setBtnJointAccnt(false);
        }
        HashMap cust = new HashMap();
        cust.put("CUST_ID",tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1));
        observable.populateScreen(cust,true);
        updateCustomerDetails();
        populatePhotoSign();
        cust = null;
    }

    private void populatePhotoSign() {
        if (totAuthPerMap!=null) {
            photoSignMap = (HashMap)totAuthPerMap.get(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(),1));
            if (photoSignMap!=null) {
                photoSignBtnEnableDisable();
                if (photoSignMap.get("PHOTO_FILE_BYTE")!=null)
                    lblPhoto.setIcon(new javax.swing.ImageIcon((byte[])photoSignMap.get("PHOTO_FILE_BYTE")));
                else 
                    lblPhoto.setIcon(null);
                if (photoSignMap.get("SIGNATURE_FILE_BYTE")!=null)
                    lblSign.setIcon(new javax.swing.ImageIcon((byte[])photoSignMap.get("SIGNATURE_FILE_BYTE")));
                else 
                    lblSign.setIcon(null);
//                String phFile = (String)photoSignMap.get("PHOTO_FILE");
//                String signFile = (String)photoSignMap.get("SIGNATURE_FILE");;
//                if (phFile.length()>0 || !phFile.equals("")) 
//                    observable.setPhotoFile(phFile);
//                else 
//                    btnPhotoRemove.setEnabled(false);
//                if (signFile.length()>0 || !signFile.equals(""))
//                    observable.setSignFile(signFile);
//                else 
//                    btnSignRemove.setEnabled(false);
//                String ph = photoSignMap.get("photoCreated")==null ? "" : photoSignMap.get("photoCreated").toString();
//                String si = photoSignMap.get("signCreated")==null ? "" : photoSignMap.get("signCreated").toString();
//                if (ph == "true") 
//                    objCustomerUISupport.setPhotoAction(1);
//                else
//                    objCustomerUISupport.setPhotoAction(0);
//                if (si == "true") 
//                    objCustomerUISupport.setSignAction(1);
//                else
//                    objCustomerUISupport.setSignAction(0);
//                objCustomerUISupport.fillPhotoSign(lblPhoto, lblSign, null, null);
            }
            else 
            {
                lblPhoto.setIcon(null);
                lblSign.setIcon(null);
            }
        }
    }
    
    private void photoSignBtnEnableDisable() {
        int actionType=observable.getActionType();
        if (actionType==ClientConstants.ACTIONTYPE_EDIT||
        actionType==ClientConstants.ACTIONTYPE_NEW) {
            btnPhotoLoad.setEnabled(true);
            btnPhotoRemove.setEnabled(true);
            btnSignLoad.setEnabled(true);
            btnSignRemove.setEnabled(true);
        }
        else {
            btnPhotoLoad.setEnabled(false);
            btnPhotoRemove.setEnabled(false);
            btnSignLoad.setEnabled(false);
            btnSignRemove.setEnabled(false);
        }
        btnPhotoSave.setEnabled(false);
        btnSignSave.setEnabled(false);        
    }
    private void btnJointAcctToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJointAcctToMainActionPerformed
        observable.setIstrue(true);
        updateOBFields();
        setBtnJointAccnt(false);
        btnJointAcctNew.setEnabled(true);
        observable.moveToMain(CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1)), tblJointAcctHolder.getSelectedRow());
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnJointAcctToMainActionPerformed
    
    private void btnJointAcctDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJointAcctDelActionPerformed
        observable.setIstrue(true);
        updateOBFields();
        //
        //        if (poaUI.checkCustIDExistInJointAcctAndPoA(CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1)))){
        
        setBtnJointAccnt(false);
        btnJointAcctNew.setEnabled(true);
        observable.delJointAccntHolder(CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1)), tblJointAcctHolder.getSelectedRow());
        observable.resetCustDetails();
        //            poaUI.getPowerOfAttorneyOB().ttNotifyObservers();
        observable.ttNotifyObservers();
        //        }
    }//GEN-LAST:event_btnJointAcctDelActionPerformed
    private void  setBtnJointAccnt(boolean val){
        btnJointAcctDel.setEnabled(val);
        btnJointAcctNew.setEnabled(val);
        btnJointAcctToMain.setEnabled(val);
    }
    private void btnJointAcctNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJointAcctNewActionPerformed
        if(tblJointAcctHolder.getRowCount()!=0){ //--- If the Main Accnt Holder is selected,
            observable.setIstrue(true);
//            callViewJnt(JOINT_CUST_ID);
            viewType = JOINT_CUST_ID;
            new CheckCustomerIdUI(this);//--- allow the user to add Jnt Acct Holder
        } else {  //--- else if the Main Acct Holder is not selected, prompt the user to select
            CommonMethods.showDialogOk(objCommRB.getString("selectMainAccntHolder")); //--- the Main Acct. holder
            btnAuthCustId.requestFocus(true);
        }
    }//GEN-LAST:event_btnJointAcctNewActionPerformed
    /** To display customer list popup for Edit & Delete options */
    private void callViewJnt(int currField){
        viewType = currField;
        //--- If Customer Id is selected OR JointAccnt New is clciked, show the popup Screen of Customer Table
        if ((currField == AUTH_CUST_ID)||(currField ==JOINT_CUST_ID)){
            HashMap viewMap = new HashMap();
            StringBuffer presentCust = new StringBuffer();
            int jntAccntTablRow = tblJointAcctHolder.getRowCount();
            if(tblJointAcctHolder.getRowCount()!=0) {
                for(int i =0, sizeJointAcctAll = tblJointAcctHolder.getRowCount();i<sizeJointAcctAll;i++){
                    if(i==0 || i==sizeJointAcctAll){
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(i, 1)) + "'");
                    } else{
                        presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(i, 1)) + "'");
                    }
                }
            }
            if(txtAuthCustId.getText().length()>0){
                if(presentCust.length()>0){
                    presentCust.append("," + "'" + txtAuthCustId.getText() + "'");
                } else {
                    presentCust.append("'" + txtAuthCustId.getText() + "'");
                }
            }
            
            viewMap.put("BRANCH_CODE", getSelectedBranchID());
            viewMap.put("MAPNAME", "getSelectCustInfoList");
            
            HashMap whereMap = new HashMap();
            whereMap.put("CUSTOMER_ID", presentCust);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap).show();
        }
    }    /** To display customer list popup for Edit & Delete options */
    private void callView(String mapName, HashMap whereMap){
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, mapName);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        boolean available = new com.see.truetransact.ui.common.viewsearch.ViewSearchUI(this, viewMap).show("Search");
        if (available && isFilled) {
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            HashMap hash = new HashMap();
            hash.put(CommonConstants.MAP_WHERE, CUSTOMERID);
            hash.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            observable.getData(hash, introducerUI.getIntroducerOB());
            introducerUI.update(null, null);
            objCustomerUISupport.fillPhotoSign(lblPhoto, lblSign, btnPhotoRemove, btnSignRemove);
            setButtonEnableDisable();
            observable.fillTbmCustomerHistory(txtCustomerID.getText());
            observable.fillTbmCustomerDepositHistory(txtCustomerID.getText());
            tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
            enableDisable();
            observable.setStatus();
            btnContactDelete.setEnabled(false);
            btnContactToMain.setEnabled(false);
            btnPhoneNew.setEnabled(false);
            btnContactAdd.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
        }
    }    
    private void cboAddressTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddressTypeActionPerformed
        // Add your handling code here:
        final String addrType = (String)((ComboBoxModel)cboAddressType.getModel()).getKeyForSelected();
        if (cboAddressType.getSelectedIndex() != 0 && chkContactExistance(addrType)){
            observable.addressTypeChanged(addrType);
            observable.setNewAddress(false);
            //To enable contact buttons for NEW & EDIT
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
                objCustomerUISupport.setContactAddEnableDisable(true,btnContactAdd);
                phoneDetailsBtnDefault();
                ClientUtil.enableDisable(panAddressDetails,true);
                ClientUtil.enableDisable(panPhoneAreaNumber,false);
            }else  if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()== ClientConstants.ACTIONTYPE_AUTHORIZE){
                objCustomerUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
                objCustomerUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactToMain);
                objCustomerUISupport.setContactAddEnableDisable(false,btnContactAdd);
            }
        }else {
            observable.setNewAddress(true);
            objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
            objCustomerUISupport.setContactAddEnableDisable(true,btnContactAdd);
            observable.setCboAddressType((String) cboAddressType.getSelectedItem());
            updateOBFields();
            observable.resetAddressExceptAddTypeDetails();
            observable.resetPhoneListTable();
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_cboAddressTypeActionPerformed
    
    private boolean chkContactExistance(String addrType){
        final ArrayList tbldata = new ArrayList();
        final int tblContactListSize = tblContactList.getRowCount();
        for (int i = 0;i<tblContactListSize;i++){
            tbldata.add(tblContactList.getValueAt(i, 0));
        }
        return tbldata.contains(addrType);
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnSearch.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        if (viewType == AUTHORIZE && isFilled){
            observable.setResult(observable.getActionType());   
            //Changed By Suresh
//            String strWarnMsg = tabCorpCust.isAllTabsVisited();
//            strWarnMsg = strWarnMsg + tabContactAndIdentityInfo.isAllTabsVisited();
//            if (strWarnMsg.length() > 0){
//                objCustomerUISupport.displayAlert(strWarnMsg);
//                return;
//            }
//            strWarnMsg = null;
            tabCorpCust.resetVisits();
            tabContactAndIdentityInfo.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
               if(authorizeStatus.equalsIgnoreCase("REJECTED") && flag == true)
            {
               singleAuthorizeMap.put("DELETESTATUS", "MODIFIED");
               singleAuthorizeMap.put(CommonConstants.STATUS, "AUTHORIZED");
               singleAuthorizeMap.put("DELETEREMARKS", "");
               singleAuthorizeMap.put("STATUSCHECK", "");
              
            }
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("CUSTOMER ID", this.txtCustomerID.getText());
            ClientUtil.execute("authorizeCorpCust", singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtCustomerID.getText());
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Corporate Customer");
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Customer Master");
            }
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);  //This line added by Rajesh
        } else{
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSelectCorpCustAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeCorpCust");
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
        }
    }
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    private void btnAuthCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthCustIdActionPerformed
        // Add your handling code here:
        //        popUp(AUTH_CUST_ID);
//        callViewJnt(AUTH_CUST_ID);
        viewType = AUTH_CUST_ID;
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnAuthCustIdActionPerformed
        
    private void tblPhoneListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhoneListMousePressed
        // Add your handling code here:
        phoneExist = true;
        updateOBFields();
        phoneRow = tblPhoneList.getSelectedRow();
        observable.populatePhone(phoneRow);
        updatePhone();
        //To enable PhoneDetails fields for NEW & EDIT options
        if(  observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE  && viewType != AUTHORIZE && viewType != 100){
            ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
            objCustomerUISupport.setPhoneButtonEnableDisable(true, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        }
    }//GEN-LAST:event_tblPhoneListMousePressed
    
    private void btnSignRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignRemoveActionPerformed
        // Add your handling code here:
        lblSign.setIcon(null);
        btnSignRemove.setEnabled(false);
        observable.setSignFile(null);
        observable.setSignByteArray(null);
        photoSignMap.put("SIGNATURE_FILE","") ;
        photoSignMap.put("SIGNATURE_FILE_BYTE","");
        totAuthPerMap.put(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(),1), photoSignMap);
        btnSignSave.setEnabled(false);
    }//GEN-LAST:event_btnSignRemoveActionPerformed
    
    private void btnSignLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignLoadActionPerformed
        // Add your handling code here:
        if (tblJointAcctHolder.getSelectedRow()>=0){
            objCustomerUISupport.loadActivities(lblSign,btnSignRemove);
            if (objCustomerUISupport.getFileSelected()==1)
//            if (observable.getSignFile()!=""||observable.getSignFile()!=null) {
            if (observable.getSignByteArray()!=null) {
                if(photoSignMap==null) photoSignMap=new HashMap();
                photoSignMap.put("SIGNATURE_FILE",observable.getSignFile()) ;
                photoSignMap.put("SIGNATURE_FILE_BYTE",observable.getSignByteArray());
                btnSignSave.setEnabled(true);
                signCreated = true;
            }
        }
        else 
        {
            ClientUtil.displayAlert("Please select a Authorized customer in the table");
        }
    }//GEN-LAST:event_btnSignLoadActionPerformed
    
    private void btnPhotoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoRemoveActionPerformed
        // Add your handling code here:
        lblPhoto.setIcon(null);
        btnPhotoRemove.setEnabled(false);
        observable.setPhotoFile(null);
        observable.setPhotoByteArray(new byte[0]);
        photoSignMap.put("PHOTO_FILE","") ;
        photoSignMap.put("PHOTO_FILE_BYTE","");
        totAuthPerMap.put(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(),1), photoSignMap);
        btnPhotoSave.setEnabled(false);
    }//GEN-LAST:event_btnPhotoRemoveActionPerformed
    
    private void btnPhotoLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoLoadActionPerformed
        // Add your handling code here:
        if (tblJointAcctHolder.getSelectedRow()>=0){
            objCustomerUISupport.loadActivities(lblPhoto,btnPhotoRemove);
            if (objCustomerUISupport.getFileSelected()==1)
//            if (observable.getPhotoFile()!=""||observable.getPhotoFile()!=null) {
            if (observable.getPhotoByteArray()!=null) {
                if(photoSignMap==null) photoSignMap=new HashMap();
                photoSignMap.put("PHOTO_FILE",observable.getPhotoFile()) ;
                photoSignMap.put("PHOTO_FILE_BYTE",observable.getPhotoByteArray());
                btnPhotoSave.setEnabled(true);
                photoCreated = true;
            }
        }
        else 
        {
            ClientUtil.displayAlert("Please select a Authorized customer in the table");
        }
    }//GEN-LAST:event_btnPhotoLoadActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        if(observable.getAuthorizeStatus()!=null)
        super.removeEditLock(observable.getTxtCustomerID());
        photoSignMap = new HashMap();
        totAuthPerMap = new HashMap();
         cbcomboAmsam.setSelectedIndex(0);
        cbcomboDesam.setSelectedIndex(0);
        setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        setButtonEnableDisable();
        observable.setStatus();
        objCustomerUISupport.setContactButtonEnableDisableDefault(false, btnContactNew, btnContactDelete, btnContactToMain);
        objCustomerUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
        setAuthCustIdEnableDisable(false);
        objCustomerUISupport.setLblPhotoSignDefault(lblPhoto, lblSign);
        objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
        observable.resetForm();
        //        existingCustDefault();
        observable.resetCustDetails();
        setLables();
        observable.resetJntAccntHoldTbl();
        ClientUtil.clearAll(this);
        introducerUI.resetIntroducerData();
        btnSearch.setEnabled(false);
        setBtnJointAccnt(false);
        btnContactNew.setEnabled(false);
        btnContactAdd.setEnabled(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnDeleteDetails.setEnabled(true);
        ClientUtil.enableDisable(this,false);
        txtCustomerID.setEditable(true);
        txtCustomerID.setEnabled(true);
        btnEdit.setEnabled(true);
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
    }//GEN-LAST:event_btnCancelActionPerformed
    public void setLables(){
        lblValArea.setText(observable.getLblValArea());
        lblValCity.setText(observable.getLblValCity());
        lblValCountry.setText(observable.getLblValCountry());
        lblValCorpCustDesig.setText(observable.getLblValCorpCustDesig());
        lblValCustomerName.setText(observable.getLblValCustomerName());
        lblValDateOfBirth.setText(observable.getLblValDateOfBirth());
        lblValPin.setText(observable.getLblValPin());
        lblValState.setText(observable.getLblValState());
        lblValStreet.setText(observable.getLblValStreet());
    }
    //    /**
    //     * To remove existing customer: Yes/No Radio button selection
    //     **/
    //    private void existingCustDefault(){
    //        removeButtons();
    //        rdoExistingCust_Yes.setSelected(false);
    //        rdoExistingCust_No.setSelected(false);
    //        addButtons();
    //    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        try{
            updateOBFields();
              if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE)    {
               String DelRemarks = COptionPane.showInputDialog(this,"Delete Remarks");
               observable.setDeletedRemarks(DelRemarks);
          }
            
            //To check mandtoryness of the Customer Personal panel and diplay appropriate
            //error message, else proceed
            mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME,panCompanyAuthPersonInfo);
            mandatoryMessage += objCustomerUISupport.checkMandatory(CLASSNAME,panKYC);
            
            
            //--- For mandatory check for Intro details
            final String INTRO = CommonUtil.convertObjToStr(observable.getCbmIntroType().getKeyForSelected());
             if (observable.getCbmIntroType().getKeyForSelected()!= "INTRO_NOT_APPLICABLE");
            mandatoryMessage = mandatoryMessage + (introducerUI.mandatoryCheck(INTRO));
            
             if(chkSuspendCust.isSelected()||chkRevokeCust.isSelected())
                 observable.setIsCustSuspended(true);
            //--- If there is no main Customer alert him.
            if(txtAuthCustId.getText().trim().length()==0){
//                ResourceBundle corpResourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.customer.CorporateCustomerRB", ProxyParameters.LANGUAGE);
//                mandatoryMessage = mandatoryMessage + corpResourceBundle.getString("CheckForMainCust");
                mandatoryMessage = mandatoryMessage + resourceBundle.getString("CheckForMainCust");
            }
            if (tblContactList.getRowCount() == 0) {
                mandatoryMessage = mandatoryMessage + resourceBundle.getString("CheckForConInfo");
            }
            if(rdoITDec_Pan.isSelected() && txtPanNumber.getText().length()<=0){
                 ClientUtil.displayAlert("Enter Pan Number");
                      return;
             }
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
               objCustomerUISupport.displayAlert(mandatoryMessage);
            }else{
                String alertMsg = "BlockedMsg";
                int optionSelected = 2;
                if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
                     
                    /** Checks up whether the Name entered comes under blocked list, if so an alert
                     *is given with yes no option,if  user wants to save that customer with
                     * he can click on yes and addup that customer, if user
                     *does not want to do so, he can click on No, so that he can change the information
                     *of the customer  */
              
                    if(isBlocked()){
                        optionSelected = observable.showAlertWindow(alertMsg);
                    }
                    if(optionSelected == 1){
                        return;
                    }
                }
                saveAction();
//                btnContactNew.setEnabled(false);
//                btnContactAdd.setEnabled(false);
                btnReject.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnException.setEnabled(true);
            }
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void saveAction(){
        observable.setPhotoByteArray(null);
        observable.setPhotoFile(null);
        observable.setSignByteArray(null);
        observable.setSignFile(null);
        storeAuthToMap();
        //If communication address type has been set, then proceed
        if( objCustomerUISupport.chkMinAddrTypeCommAddr(tblContactList) ){
            introducerUI.updateOBFields();
            HashMap resultMap = observable.doAction(introducerUI.getIntroducerOB());
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
//                 HashMap map = new HashMap();
//                map.put("custId",observable.getTxtAuthCustId());
//                map.put("depositNo",observable.getProxyReturnMap().get("CUST_ID"));
//               map.put("status","DELETED");
//               ClientUtil.execute("insertCustJointAccntTO", map);
               HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("CUSTOMER ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if(observable.getResult()==ClientConstants.ACTIONTYPE_NEW)
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("CUST_ID")) {
                       lockMap.put("CUSTOMER ID",observable.getProxyReturnMap().get("CUST_ID")); 										
                    }
                }
               if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                    lockMap.put("CUSTOMER ID",observable.getTxtCustomerID());
                }
                setEditLockMap(lockMap);
                setEditLock();
                setModified(false);
                ClientUtil.enableDisable(this,false);
                setButtonEnableDisable();
                objCustomerUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
                objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
                objCustomerUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactToMain);
                setAuthCustIdEnableDisable(false);
                //            observable.setResultStatus();
                objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
                objCustomerUISupport.setLblPhotoSignDefault(lblPhoto, lblSign);
                //            observable.resetCustDetails();
                
                observable.resetJntAccntHoldTbl();
                observable.resetForm();
                observable.resetCustDetails();
                setLables();
                observable.setResultStatus();
                ClientUtil.clearAll(this);
                introducerUI.resetIntroducerData();
                introducerUI.enableIntroducerData(false);
                setBtnJointAccnt(false);
                btnSearch.setEnabled(false);
                if(observable.getResult()==ClientConstants.ACTIONTYPE_NEW){
//                    txtCustomerID.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    ClientUtil.showMessageWindow ("Customer ID : " + CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                }
                btnContactNew.setEnabled(false);
                btnContactAdd.setEnabled(false);
            }
            //            existingCustDefault();
        }
    }
    
    private void storeAuthToMap() {
        HashMap eachRowMap;
        HashMap totalMap = new HashMap();
//        AuthPersonsTO objAuthPersonsTO;
        tempMap = new HashMap();
        if (totAuthPerMap != null) 
            if (totAuthPerMap.size()>0)
                for (int i=0; i<tblJointAcctHolder.getRowCount(); i++) {
                    tempMap = (HashMap) totAuthPerMap.get(tblJointAcctHolder.getValueAt(i, 1));
        //            objAuthPersonsTO = new AuthPersonsTO();
                    eachRowMap = new HashMap();
                    if (tempMap!=null) {
        //            objAuthPersonsTO.setAuthCustID((String)tblJointAcctHolder.getValueAt(i, 1));
        //            objAuthPersonsTO.setPhotoFile((String)tempMap.get("PHOTO_FILE"));
        //            objAuthPersonsTO.setSignatureFile((String)tempMap.get("SIGNATURE_FILE"));
        //            objAuthPersonsTO.setCustID(txtCustomerID.getText());
                    eachRowMap.put("AUTH_CUST_ID", tblJointAcctHolder.getValueAt(i, 1));
                    eachRowMap.put("CUST_ID", txtCustomerID.getText());
        //            eachRowMap.put("objAuthPersonsTO", objAuthPersonsTO);
                    eachRowMap.put("PHOTO_FILE_BYTE", tempMap.get("PHOTO_FILE_BYTE"));
                    eachRowMap.put("SIGNATURE_FILE_BYTE", tempMap.get("SIGNATURE_FILE_BYTE"));
                    eachRowMap.put("photoCreated", tempMap.get("photoCreated"));
                    eachRowMap.put("signCreated", tempMap.get("signCreated"));
                    }
                    totalMap.put(String.valueOf(i+1), eachRowMap);
        //            objAuthPersonsTO = null;
                    tempMap=null;
                    eachRowMap=null;
                }
        observable.setCorpAuthCustMap(totalMap);
        totalMap = null;
    }
    
    private void btnContactAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactAddActionPerformed
        // Add your handling code here:
        try{
            updateOBFields();
            mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME,panAddressDetails);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objCustomerUISupport.displayAlert(mandatoryMessage);
            }else{
                final String alertMsg = "phoneDetailsMsg";
                int action = observable.showAlertWindow(alertMsg);
                if(action==0){
                    btnPhoneNew.setEnabled(true);
                }else if(action==1){
                    observable.addAddressMap();
                    ClientUtil.clearAll(panContactInfo);
                    ClientUtil.enableDisable(panContactInfo,false);
                    objCustomerUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew,btnContactNoAdd,btnPhoneDelete);
                    objCustomerUISupport.setContactAddEnableDisable(false,btnContactAdd);
                }
            }
            //saveContact=1;
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnContactAddActionPerformed
    
    private void btnPhoneDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneDeleteActionPerformed
        // Add your handling code here:
        observable.deletePhoneDetails(tblPhoneList.getSelectedRow());
        updatePhone();
        phoneDetailsBtnDefault();
    }//GEN-LAST:event_btnPhoneDeleteActionPerformed
    
    /**
     * To disable all the phone related entry fields and enable only Phone New button
     */
    private void phoneDetailsBtnDefault(){
        ClientUtil.enableDisable(this.panPhoneAreaNumber, false);
        objCustomerUISupport.setPhoneButtonEnableDisableDefault( btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
    }
    
    private void btnContactNoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactNoAddActionPerformed
        // Add your handling code here:
        mandatoryMessage =  objCustomerUISupport.checkMandatory(CLASSNAME,panPhoneAreaNumber);
        //To check whether all the mandatory fields of Phone details have been entered.
        //If not entered properly display alert message, else proceed
        if( mandatoryMessage.length() > 0 ){
            objCustomerUISupport.displayAlert(mandatoryMessage);
        }else if( cboPhoneType.getSelectedItem().equals("Mobile") && txtPhoneNumber.getText().length() != 10) {
            ClientUtil.showAlertWindow("Mobile Number should be 10 digits only!!!");
            return;
        }else{
            updateOBFields();
            observable.addPhoneList(phoneExist,phoneRow);
            updatePhone();
            observable.addAddressMap();
            objCustomerUISupport.setPhoneButtonEnableDisableDefault(btnPhoneNew,btnContactNoAdd,btnPhoneDelete);
            objCustomerUISupport.setContactAddEnableDisable(false,btnContactAdd);
            ClientUtil.enableDisable(panPhoneAreaNumber,false);
            ClientUtil.enableDisable(panAddressDetails,false);
        }
    }//GEN-LAST:event_btnContactNoAddActionPerformed
    
    private void btnPhoneNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneNewActionPerformed
        // Add your handling code here:
        phoneExist = false;
        phoneRow = tblPhoneList.getModel().getRowCount();
        updateOBFields();
        observable.resetPhoneDetails();
        updatePhone();
        objCustomerUISupport.setPhoneButtonEnableDisableNew(btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
    }//GEN-LAST:event_btnPhoneNewActionPerformed
        
    private void btnContactDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactDeleteActionPerformed
        // Add your handling code here:
        try{
            if (((String)tblContactList.getModel().getValueAt(tblContactList.getSelectedRow(),0)).equals(observable.getCommAddrType())){
                objCustomerUISupport.displayAlert(resourceBundle.getString("mainAddrType"));
            }else{
                String alertMsg = "deleteWarningMsg";
                int optionSelected = observable.showAlertWindow(alertMsg);
                if(optionSelected==0){
                    observable.deleteAddress(tblContactList.getSelectedRow());
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btnContactDeleteActionPerformed
    
    private void btnContactNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactNewActionPerformed
        // Add your handling code here:
        updateOBFields();
        observable.setNewAddress(true);
        ClientUtil.enableDisable(panAddressDetails,true);
        btnContactAdd.setEnabled(true);
        observable.resetNewAddress();
        objCustomerUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        objCustomerUISupport.setContactButtonEnableDisable(btnContactAdd,btnContactDelete,btnContactToMain);
    }//GEN-LAST:event_btnContactNewActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(EDIT_DELETE);
        introducerUI.setActionType(DELETE);
        objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if(txtCustomerID.getText().length()>0){
            String[] obj ={"Yes ","No"};
            int option =COptionPane.showOptionDialog(null,("Do you want to Enable all those fields?"), ("Customer"),
            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj,obj[0]);
            if(option == 0){
                ClientUtil.enableDisable(this,true);
                btnSave.setEnabled(true);
                txtCustomerID.setEnabled(false);
                btnEdit.setEnabled(false);
                cboCustomerType.setEnabled(false);
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            }else{
                ClientUtil.enableDisable(this,false);
                cboCustomerType.setEnabled(true);
            }
        }else{
            phoneExist = false;
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            popUp(EDIT_DELETE);
            btnContactDelete.setEnabled(false);
            btnContactToMain.setEnabled(false);
            btnPhoneNew.setEnabled(false);
             btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
             boolean ck=chkSuspendCust.isSelected();
            if(ck){
                chkRevokeCust.setEnabled(true);
                tdtRevokedCustDate.setEnabled(true);
                chkSuspendCust.setEnabled(false);
                tdtSuspendCustFrom.setEnabled(false);
            }
            else{
                chkRevokeCust.setEnabled(false);
                tdtRevokedCustDate.setEnabled(false);  
                chkSuspendCust.setEnabled(true);
                tdtSuspendCustFrom.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
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
        photoSignMap = new HashMap();
        totAuthPerMap = new HashMap();
        setModified(true);
        phoneExist = false;
        ClientUtil.enableDisable(this,true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        // To remove the Intro Type -   Intro N/A - Legacy for new customers.
        if (observable.getCbmIntroType().getDataForKey("INTRO_NOT_APPLICABLE")!=null){
            observable.getCbmIntroType().removeKeyAndElement("INTRO_NOT_APPLICABLE");
            cboIntroType.setModel(observable.getCbmIntroType());
        }
        
        setButtonEnableDisable();
        objCustomerUISupport.setPhoneButtonEnableDisableDefault( btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        ClientUtil.enableDisable(this.panPhoneAreaNumber, false);
        ClientUtil.enableDisable(panAddressDetails,false);
        //        ClientUtil.enableDisable(panAuthPersonDetails,false);
        objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
        objCustomerUISupport.setContactAddEnableDisable(true, btnContactAdd);
        objCustomerUISupport.setPhotoSignLoadEnableDisable(true, btnPhotoLoad, btnSignLoad);
        btnPhoneNew.setEnabled(false);
        btnContactAdd.setEnabled(false);
        btnContactDelete.setEnabled(false);
        btnContactToMain.setEnabled(false);
        observable.setStatus();
        btnSearch.setEnabled(true);
        btnAuthCustId.setEnabled(true);
        introducerUI.resetIntroducerData();
        setBtnJointAccnt(false);
        btnJointAcctNew.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnDeleteDetails.setEnabled(true);
        cboMembershipClass.setSelectedItem(observable.getCbmMembershipClass().getDataForKey("NONE"));
        cboMembershipClass.setEnabled(true);
        chkRevokeCust.setEnabled(false);
        tdtRevokedCustDate.setEnabled(false);  
        chkSuspendCust.setEnabled(false);
        tdtSuspendCustFrom.setEnabled(false);
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void rdoITDec_F61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoITDec_F61ActionPerformed
        // TODO add your handling code here:
        rdoITDec_F60.setSelected(false);
        rdoITDec_Pan.setSelected(false);
        txtPanNumber.setText("");
        txtPanNumber.setVisible(false);
        lblPanNumber.setVisible(false);
}//GEN-LAST:event_rdoITDec_F61ActionPerformed

    private void rdoITDec_F60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoITDec_F60ActionPerformed
        // TODO add your handling code here:
        rdoITDec_F61.setSelected(false);
        rdoITDec_Pan.setSelected(false);
        txtPanNumber.setVisible(false);
        lblPanNumber.setVisible(false);
        txtPanNumber.setText("");
}//GEN-LAST:event_rdoITDec_F60ActionPerformed

    private void cboIntroTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntroTypeActionPerformed
        // TODO add your handling code here:
        //        updateOBFields();
        if( observable.getCboIntroType() != null ){
            if(!observable.getCboIntroType().equals("Intro N/A - Legacy")) {
                if (observable.getCbmIntroType().getDataForKey("INTRO_NOT_APPLICABLE")!=null){
                    observable.getCbmIntroType().removeKeyAndElement("INTRO_NOT_APPLICABLE");
                    cboIntroType.setModel(observable.getCbmIntroType());
                }
            }
            observable.setCboIntroType((String) ((ComboBoxModel) cboIntroType.getModel()).getKeyForSelected());
            introducerUI.setIntroducerType(CommonUtil.convertObjToStr(cboIntroType.getSelectedItem()));
            final String INTRO = CommonUtil.convertObjToStr(observable.getCboIntroType());
            
            /* If Some Value is Selected...*/
            if(INTRO.equalsIgnoreCase("")){
                introducerUI.setIntroPanel(false);
                introducerUI.setPanInVisible();
            }else{
                introducerUI.setPanInVisible();
                introducerUI.setIntroPanel(true);
                introducerUI.setPanVisible(INTRO);
                introducerUI.enableDisableBtn(true);
            }
        }
}//GEN-LAST:event_cboIntroTypeActionPerformed

    private void tdtNetWorthAsOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtNetWorthAsOnFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtNetWorthAsOn);
}//GEN-LAST:event_tdtNetWorthAsOnFocusLost

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        //        search = "YES";
        viewType = SEARCH;
        phoneExist = false;
        //observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        //        viewType = EDIT_DELETE;
        HashMap map = new HashMap();
        map.put("COMP_NAME", txtCompany.getText());
        map.put("CUST_TYPE_ID", cboCustomerType.getSelectedItem());
        map.put("EMAIL_ID", txtEmailID.getText());
        callView("getSearchCorpCustomerValues",map);
}//GEN-LAST:event_btnSearchActionPerformed

    private void rdoITDec_PanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoITDec_PanActionPerformed
        // TODO add your handling code here:
        rdoITDec_F61.setSelected(false);
        rdoITDec_F60.setSelected(false);
        txtPanNumber.setVisible(true);
        lblPanNumber.setVisible(true);
}//GEN-LAST:event_rdoITDec_PanActionPerformed

    private void txtCustomerIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIDFocusLost
        // TODO add your handling code here:
        if(txtCustomerID.getText().length()>0){
            HashMap fillMap = new HashMap();
            String CUSTID = txtCustomerID.getText();
            fillMap.put("CUSTOMER ID",txtCustomerID.getText());
            fillMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
            java.util.List lst = ClientUtil.executeQuery("getCorporateCustomerList", fillMap);
            if(lst!=null && lst.size()>0){
                if(txtCustomerID.getText().equals(observable.getTxtCustomerID())){
                    btnCancel.setEnabled(true);
                    btnEdit.setEnabled(true);
                    btnSave.setEnabled(true);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                    return;
                }else{
                    btnCancelActionPerformed(null);
                    txtCustomerID.setText(CUSTID);
                    viewType = 150;
                    fillData(fillMap);
                    btnCancel.setEnabled(true);
                    btnEdit.setEnabled(true);
                    btnSave.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                }
            }else{
                ClientUtil.showAlertWindow("Invalid Customer Id...");
                btnCancelActionPerformed(null);
                btnCancel.setEnabled(true);
                btnEdit.setEnabled(true);
                btnSave.setEnabled(false);
                btnNew.setEnabled(false);
                btnDelete.setEnabled(false);
                txtCustomerID.setText("");
            }
        }
}//GEN-LAST:event_txtCustomerIDFocusLost

    private void btnContactToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactToMainActionPerformed
        // Add your handling code here:
        observable.setCommunicationAddress(tblContactList.getSelectedRow());
        objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
}//GEN-LAST:event_btnContactToMainActionPerformed

    private void tblContactListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContactListMousePressed
        // Add your handling code here:
        updateOBFields();
        btnContactAdd.setEnabled(true);
        observable.populateAddress(tblContactList.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            objCustomerUISupport.setContactButtonEnableDisableDefault(true,btnContactNew, btnContactDelete, btnContactToMain);
            phoneDetailsBtnDefault();
            ClientUtil.enableDisable(panAddressDetails,true);
            ClientUtil.enableDisable(panPhoneAreaNumber,false);
            btnContactDelete.setEnabled(true);
            btnContactToMain.setEnabled(true);
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||(viewType == 100)){
            introducerUI.enableIntroducerData(false);
            objCustomerUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
            objCustomerUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactToMain);
            objCustomerUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
        
           // Add your handling code here:
}//GEN-LAST:event_tblContactListMousePressed
   private void updateProof()
     {
     txtUniqueId.setText(observable.getTxtUniqueId());
     }
    private void tblProofListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProofListMousePressed
        // Add your handling code here:
        proofExist = true;
        updateOBFields();
        btnProofAdd.setEnabled(true);
        proofRow = tblProofList.getSelectedRow();
        observable.populateProof(proofRow);
        updateProof();
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objCustomerUISupport.setProofButtonEnableDisableDefault(true,btnProofNew,btnProofDelete, btnProofAdd);
            phoneDetailsBtnDefault();
            ClientUtil.enableDisable(panProofDetails, true);
            btnProofDelete.setEnabled(true);
            btnProofAdd.setEnabled(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")) {
            objCustomerUISupport.setProofButtonEnableDisableDefault(false, btnProofNew, btnProofDelete, btnProofAdd);
            objCustomerUISupport.setProofAddEnableDisable(false, btnProofAdd);
        }
}//GEN-LAST:event_tblProofListMousePressed

    private void btnProofNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProofNewActionPerformed
        // Add your handling code here:
        observable.setNewProof(true);
        proofExist=false;
        proofRow = tblProofList.getModel().getRowCount();
        updateOBFields();
        ClientUtil.enableDisable(panProofDetails, true);
        btnProofAdd.setEnabled(true);
        observable.resetNewProofDetails();
    }//GEN-LAST:event_btnProofNewActionPerformed

    private void btnProofAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProofAddActionPerformed
        // Add your handling code here:
        try {
            updateOBFields();
            observable.addProofMap(proofExist,proofRow);
            ClientUtil.clearAll(panProofDetails);
            ClientUtil.enableDisable(panProofDetails, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
}//GEN-LAST:event_btnProofAddActionPerformed

    private void btnProofDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProofDeleteActionPerformed
        // Add your handling code here:
        observable.deleteProofDetails(tblProofList.getSelectedRow());
        updateProof();
        objCustomerUISupport.setProofButtonEnableDisable(btnProofNew,btnProofAdd, btnProofDelete);
        ClientUtil.clearAll(panProofDetails);
        ClientUtil.enableDisable(panProofDetails, false);
}//GEN-LAST:event_btnProofDeleteActionPerformed
 private boolean chkProofExistance(String ProofType) {
        final ArrayList tbldata = new ArrayList();
        final int tblproofListSize = tblProofList.getRowCount();
        for (int i = 0; i < tblproofListSize; i++) {
            tbldata.add(tblProofList.getValueAt(i, 0));
        }
        return tbldata.contains(ProofType);
    }
    private void cboIdenProofActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIdenProofActionPerformed
        
        final String proofType = (String) ((ComboBoxModel) cboIdenProof.getModel()).getKeyForSelected();
        if (cboIdenProof.getSelectedIndex() != 0 && chkProofExistance(proofType)) {
            observable.setNewProof(false);
            observable.proofTypeChanged(proofType);
            //To enable contact buttons for NEW & EDIT
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                objCustomerUISupport.setProofButtonEnableDisable(btnProofNew, btnProofAdd, btnProofDelete);
                objCustomerUISupport.setProofAddEnableDisable(true, btnProofAdd);
                ClientUtil.enableDisable(panProofDetails, true);
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                objCustomerUISupport.setProofButtonEnableDisableDefault(false, btnProofNew, btnProofAdd, btnProofDelete);
                objCustomerUISupport.setProofAddEnableDisable(false, btnProofAdd);
            }
        } else {
            observable.setNewProof(true);
            objCustomerUISupport.setProofButtonEnableDisable(btnProofNew, btnProofAdd, btnProofDelete);
            objCustomerUISupport.setProofAddEnableDisable(true, btnProofAdd);
            updateOBFields();
            // observable.resetProofListTable();
            observable.ttNotifyObservers();
        }
        // TODO add your handling code here:
}//GEN-LAST:event_cboIdenProofActionPerformed
    
    /** To display customer list popup for Edit & Delete options */
    private void popUp(int field){
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap where = new HashMap();
        where.put("BRANCH_CODE", getSelectedBranchID());
        if (field == EDIT_DELETE){
            ArrayList lst = new ArrayList();
            lst.add("CUSTOMER ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            viewMap.put(CommonConstants.MAP_NAME, "getCorporateCustomerList");
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }else if (field == AUTH_CUST_ID){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectIndividualCustInfo");
        }
        else if (field==100){
             HashMap whereMap = new HashMap();
                viewMap.put(CommonConstants.MAP_NAME, "DeletedCustomerDetailsCorporate");
                viewMap.put(CommonConstants.MAP_WHERE, where);
        }
         else if (field==150){
            viewMap.put(CommonConstants.MAP_NAME, "getCorporateCustomerList");
            viewMap.put(CommonConstants.MAP_WHERE, where);   
         }
        new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap).show();
    }
    
    /** To get data based on customer id received from the popup and populate into the
     * screen
     */
    public void fillData(Object obj) {
        final HashMap hash = (HashMap) obj;
         String st = CommonUtil.convertObjToStr(hash.get("STATUS"));
        if(st.equalsIgnoreCase("DELETED"))
        {
            flag = true;
        }
        HashMap map = new HashMap();
        CUSTOMERID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
//        hash.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
//        hash.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        map.put("CUST_ID",hash.get("CUSTOMER ID"));
        map.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
        map.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        setModified(true);
//         if (viewType == 100)
//         {
//           map.put("DELETECHECK","");
//         }
          if (viewType == AUTHORIZE)
         {
           map.put("AUTHORIZECHECK","");
         }
       if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            map.put("CUST_ID",hash.get("CUST_ID"));
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancel.setFocusable(false);
            btnAuthorize.setEnabled(true);           
        }
       if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            map.put("CUST_ID",hash.get("CUST_ID"));
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancel.setFocusable(false);
            btnAuthorize.setEnabled(true);           
        }
        if (viewType == EDIT_DELETE || viewType == AUTHORIZE || viewType == 100|| viewType == 150){
            isFilled = true;
            observable.getData(map,introducerUI.getIntroducerOB());
            introducerUI.update(null,null);
            totAuthPerMap = observable.getCorpAuthCustMap(); //Added by Rajesh
            //            if (txtAuthCustId.getText().length() > 0){
            //                rdoExistingCust_Yes.setSelected(true);
            //            }else{
            //                rdoExistingCust_No.setSelected(true);
            //            }
//            objCustomerUISupport.fillPhotoSign(lblPhoto, lblSign, btnPhotoRemove, btnSignRemove);
            setButtonEnableDisable();
            observable.fillTbmCustomerHistory(txtCustomerID.getText());
            observable.fillTbmCustomerDepositHistory(txtCustomerID.getText());
            tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
            //For EDIT option enable disable fields and controls appropriately
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                enableDisable();
                //                rdoExistingCust_YesActionPerformed(null);
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                introducerUI.enableIntroducerData(false);
            }
            observable.setStatus();
            if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
        }else if (viewType == AUTH_CUST_ID){
            if(hash.containsKey("CUST_ID")){
                hash.put("CUSTOMER ID",hash.get("CUST_ID")); 
            }
            CustInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
        }else if (viewType == JOINT_CUST_ID) {
//            JointAcctDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            JointAcctDisplay(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
        }else if (viewType == SEARCH){
            System.out.println("In Search");
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            isFilled = true;
            observable.getData(hash,introducerUI.getIntroducerOB());
            introducerUI.update(null,null);
            //            objCustomerUISupport.fillPhotoSign(lblPhoto, lblSign, btnPhotoRemove, btnSignRemove);
            setButtonEnableDisable();
            observable.fillTbmCustomerHistory(txtCustomerID.getText());
            tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
            enableDisable();
            observable.setStatus();
            btnContactDelete.setEnabled(false);
            btnContactToMain.setEnabled(false);
            btnPhoneNew.setEnabled(false);
            btnContactAdd.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            
        }
        //        if(search.equals("YES")){
        //            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        //            setButtonEnableDisable();
        //            search = "";
        //        }
        //--- If Edit,set the New Btn enabled.
        if(observable.getActionType() == (ClientConstants.ACTIONTYPE_EDIT)){
            setBtnJointAccnt(false);
            btnJointAcctNew.setEnabled(true);
        }
        
        //-- If Authorize disabling the Panel
        if(viewType == AUTHORIZE){
            ClientUtil.enableDisable(panCorporateCustomer, false);
            setAuthCustIdEnableDisable(false);
            introducerUI.enableDisableBtn(false);
            setBtnJointAccnt(false);
        }
        if(observable.getActionType() == (ClientConstants.ACTIONTYPE_VIEW_MODE)){
            ClientUtil.enableDisable(this, false);
        }
    }
    
    private void JointAcctDisplay(String custId){
        HashMap hash = new HashMap();
        hash.put("CUST_ID",custId);
        observable.populateJointAccntTable(hash);
        tblJointAcctHolder.setModel(observable.getTblJointAccnt());
        btnJointAcctDel.setEnabled(false);
        btnJointAcctToMain.setEnabled(false);
        hash = null;
    }
    
    private void CustInfoDisplay(String custId){
        HashMap hash = new HashMap();
        hash.put("CUST_ID",custId);
        observable.populateScreen(hash,false);
        updateCustomerDetails();
        txtAuthCustId.setText(observable.getTxtAuthCustId());
        tblJointAcctHolder.setModel(observable.getTblJointAccnt());
        hash = null;
    }
    
    private void updateCustomerDetails(){
        lblValCustomerName.setText(observable.getLblValCustomerName());
        lblValCustomerName.setToolTipText(lblValCustomerName.getText());
        lblValDateOfBirth.setText(observable.getLblValDateOfBirth());
        lblValDateOfBirth.setToolTipText(lblValDateOfBirth.getText());
        lblValStreet.setText(observable.getLblValStreet());
        lblValStreet.setToolTipText(lblValStreet.getText());
        lblValArea.setText(observable.getLblValArea());
        lblValArea.setToolTipText(lblValArea.getText());
        lblValCity.setText(observable.getLblValCity());
        lblValCity.setToolTipText(lblValCity.getText());
        lblValState.setText(observable.getLblValState());
        lblValState.setToolTipText(lblValState.getText());
        lblValCountry.setText(observable.getLblValCountry());
        lblValCountry.setToolTipText(lblValCountry.getText());
        lblValCorpCustDesig.setText(observable.getLblValCorpCustDesig());
        lblValCorpCustDesig.setToolTipText(lblValCorpCustDesig.getText());
        lblValPin.setText(observable.getLblValPin());
        lblValPin.setToolTipText(lblValPin.getText());
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnDeleteDetails.setEnabled(!btnDeleteDetails.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    /**
     * To enable or disable the Authorized Customer ID text box based on whether
     * the Customer is an existing customer or not
     **/
    private void setAuthCustIdEnableDisable(boolean authCustId){
        this.btnAuthCustId.setEnabled(authCustId);
    }
    
    private void colWidthChange(){
        tblPhoneList.getColumn(resourceBundle.getString("tblPhoneColumn1")).setPreferredWidth(40);
    }
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthCustId;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnContactAdd;
    private com.see.truetransact.uicomponent.CButton btnContactDelete;
    private com.see.truetransact.uicomponent.CButton btnContactNew;
    private com.see.truetransact.uicomponent.CButton btnContactNoAdd;
    private com.see.truetransact.uicomponent.CButton btnContactToMain;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteDetails;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnJointAcctDel;
    private com.see.truetransact.uicomponent.CButton btnJointAcctNew;
    private com.see.truetransact.uicomponent.CButton btnJointAcctToMain;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPhoneDelete;
    private com.see.truetransact.uicomponent.CButton btnPhoneNew;
    private com.see.truetransact.uicomponent.CButton btnPhotoLoad;
    private com.see.truetransact.uicomponent.CButton btnPhotoRemove;
    private com.see.truetransact.uicomponent.CButton btnPhotoSave;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProofAdd;
    private com.see.truetransact.uicomponent.CButton btnProofDelete;
    private com.see.truetransact.uicomponent.CButton btnProofNew;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnSignLoad;
    private com.see.truetransact.uicomponent.CButton btnSignRemove;
    private com.see.truetransact.uicomponent.CButton btnSignSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CSeparator cSeparator2;
    private javax.swing.JComboBox cbcomboAmsam;
    private javax.swing.JComboBox cbcomboDesam;
    private com.see.truetransact.uicomponent.CComboBox cboAddrProof;
    private com.see.truetransact.uicomponent.CComboBox cboAddressType;
    private com.see.truetransact.uicomponent.CComboBox cboBusNature;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboCustomerType;
    private com.see.truetransact.uicomponent.CComboBox cboIdenProof;
    private com.see.truetransact.uicomponent.CComboBox cboIntroType;
    private com.see.truetransact.uicomponent.CComboBox cboMembershipClass;
    private com.see.truetransact.uicomponent.CComboBox cboPhoneType;
    private com.see.truetransact.uicomponent.CComboBox cboPrefCommunication;
    private com.see.truetransact.uicomponent.CComboBox cboRelationManager;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CCheckBox chkAddrVerified;
    private com.see.truetransact.uicomponent.CCheckBox chkFinanceStmtVerified;
    private com.see.truetransact.uicomponent.CCheckBox chkPhVerified;
    private com.see.truetransact.uicomponent.CCheckBox chkRevokeCust;
    private com.see.truetransact.uicomponent.CCheckBox chkSuspendCust;
    private com.see.truetransact.uicomponent.CLabel lblAddrProof;
    private com.see.truetransact.uicomponent.CLabel lblAddrRemarks;
    private com.see.truetransact.uicomponent.CLabel lblAddrVerified;
    private com.see.truetransact.uicomponent.CLabel lblAddressType;
    private com.see.truetransact.uicomponent.CLabel lblAmsam;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblArea1;
    private com.see.truetransact.uicomponent.CLabel lblAreaCode;
    private com.see.truetransact.uicomponent.CLabel lblAuthCapital;
    private com.see.truetransact.uicomponent.CLabel lblAuthCustId;
    private com.see.truetransact.uicomponent.CLabel lblBankruptsy;
    private com.see.truetransact.uicomponent.CLabel lblBusNature;
    private com.see.truetransact.uicomponent.CLabel lblCEO;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCity1;
    private com.see.truetransact.uicomponent.CLabel lblCompany;
    private com.see.truetransact.uicomponent.CLabel lblCorpCustDesig;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCountry1;
    private com.see.truetransact.uicomponent.CLabel lblCrAvldSince;
    private com.see.truetransact.uicomponent.CLabel lblCreatedDt;
    private com.see.truetransact.uicomponent.CLabel lblCreatedDt1;
    private com.see.truetransact.uicomponent.CLabel lblCustPwd;
    private com.see.truetransact.uicomponent.CLabel lblCustUserid;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerType;
    private com.see.truetransact.uicomponent.CLabel lblDateOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblDealingPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDealingWith;
    private com.see.truetransact.uicomponent.CLabel lblDesam;
    private com.see.truetransact.uicomponent.CLabel lblDividendPercentage;
    private com.see.truetransact.uicomponent.CLabel lblDividendPercentage1;
    private com.see.truetransact.uicomponent.CLabel lblDtEstablished;
    private com.see.truetransact.uicomponent.CLabel lblEmailID;
    private com.see.truetransact.uicomponent.CLabel lblFinanceStmtVerified;
    private com.see.truetransact.uicomponent.CLabel lblFinancialyrEnd;
    private com.see.truetransact.uicomponent.CLabel lblIdenProof;
    private com.see.truetransact.uicomponent.CLabel lblIntroType;
    private com.see.truetransact.uicomponent.CLabel lblIssuedCapital;
    private com.see.truetransact.uicomponent.CLabel lblLastYrPL;
    private com.see.truetransact.uicomponent.CLabel lblLiablityTax;
    private com.see.truetransact.uicomponent.CLabel lblMemNum;
    private com.see.truetransact.uicomponent.CLabel lblMembershipClass;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetWorth;
    private com.see.truetransact.uicomponent.CLabel lblNetWorthAsOn;
    private com.see.truetransact.uicomponent.CLabel lblPanNumber;
    private com.see.truetransact.uicomponent.CLabel lblPhVerified;
    private com.see.truetransact.uicomponent.CLabel lblPhoneNumber;
    private com.see.truetransact.uicomponent.CLabel lblPhoneType;
    private com.see.truetransact.uicomponent.CLabel lblPhoto;
    private com.see.truetransact.uicomponent.CLabel lblPin;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblPrefCommunication;
    private com.see.truetransact.uicomponent.CLabel lblProfitBeforeTax;
    private com.see.truetransact.uicomponent.CLabel lblRegNumber;
    private com.see.truetransact.uicomponent.CLabel lblRelationManager;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRevokeCust;
    private com.see.truetransact.uicomponent.CLabel lblRevokeDate;
    private com.see.truetransact.uicomponent.CLabel lblRiskRate;
    private com.see.truetransact.uicomponent.CLabel lblSign;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblState1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblStreet1;
    private com.see.truetransact.uicomponent.CLabel lblSubscribedCapital;
    private com.see.truetransact.uicomponent.CLabel lblSuspendCust;
    private com.see.truetransact.uicomponent.CLabel lblSuspendDate;
    private com.see.truetransact.uicomponent.CLabel lblSuspendRemarks;
    private com.see.truetransact.uicomponent.CLabel lblTotalIncome;
    private com.see.truetransact.uicomponent.CLabel lblTotalNonTaxExp;
    private com.see.truetransact.uicomponent.CLabel lblTotalResource;
    private com.see.truetransact.uicomponent.CLabel lblTransPwd;
    private com.see.truetransact.uicomponent.CLabel lblUniqueNo;
    private com.see.truetransact.uicomponent.CLabel lblValArea;
    private com.see.truetransact.uicomponent.CLabel lblValCity;
    private com.see.truetransact.uicomponent.CLabel lblValCorpCustDesig;
    private com.see.truetransact.uicomponent.CLabel lblValCountry;
    private com.see.truetransact.uicomponent.CLabel lblValCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblValDateOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblValPin;
    private com.see.truetransact.uicomponent.CLabel lblValState;
    private com.see.truetransact.uicomponent.CLabel lblValStreet;
    private com.see.truetransact.uicomponent.CLabel lblWebSite;
    private com.see.truetransact.uicomponent.CMenuBar mbrCorporateCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAdditionalInfo;
    private com.see.truetransact.uicomponent.CPanel panAddress;
    private com.see.truetransact.uicomponent.CPanel panAddressDetails;
    private com.see.truetransact.uicomponent.CPanel panAddressType;
    private com.see.truetransact.uicomponent.CPanel panAuthCustId;
    private com.see.truetransact.uicomponent.CPanel panCapital;
    private com.see.truetransact.uicomponent.CPanel panCity;
    private com.see.truetransact.uicomponent.CPanel panCompanyAuthPersonInfo;
    private com.see.truetransact.uicomponent.CPanel panCompanyInfo;
    private com.see.truetransact.uicomponent.CPanel panContactAndIdentityInfo;
    private com.see.truetransact.uicomponent.CPanel panContactControl;
    private com.see.truetransact.uicomponent.CPanel panContactInfo;
    private com.see.truetransact.uicomponent.CPanel panContactNo;
    private com.see.truetransact.uicomponent.CPanel panContacts;
    private com.see.truetransact.uicomponent.CPanel panContactsList;
    private com.see.truetransact.uicomponent.CPanel panCorpCustDet;
    private com.see.truetransact.uicomponent.CPanel panCorporateCustomer;
    private com.see.truetransact.uicomponent.CPanel panCorporateInfo;
    private com.see.truetransact.uicomponent.CPanel panCountry;
    private com.see.truetransact.uicomponent.CPanel panCountryDetails;
    private com.see.truetransact.uicomponent.CPanel panCustId;
    private com.see.truetransact.uicomponent.CPanel panCustomerHistory;
    private com.see.truetransact.uicomponent.CPanel panCustomerName;
    private com.see.truetransact.uicomponent.CPanel panCustomerSide;
    private com.see.truetransact.uicomponent.CPanel panFinancialDetails;
    private com.see.truetransact.uicomponent.CPanel panITDetails;
    private com.see.truetransact.uicomponent.CPanel panJointAcctButton;
    private com.see.truetransact.uicomponent.CPanel panJointAcctHolder;
    private com.see.truetransact.uicomponent.CPanel panKYC;
    private com.see.truetransact.uicomponent.CPanel panOthers;
    private com.see.truetransact.uicomponent.CPanel panPhoneAreaNumber;
    private com.see.truetransact.uicomponent.CPanel panPhoneList;
    private com.see.truetransact.uicomponent.CPanel panPhoneSave;
    private com.see.truetransact.uicomponent.CPanel panPhoneType;
    private com.see.truetransact.uicomponent.CPanel panPhoto;
    private com.see.truetransact.uicomponent.CPanel panPhotoButtons;
    private com.see.truetransact.uicomponent.CPanel panPhotoSign;
    private com.see.truetransact.uicomponent.CPanel panProof;
    private com.see.truetransact.uicomponent.CPanel panProofControl;
    private com.see.truetransact.uicomponent.CPanel panProofDetails;
    private com.see.truetransact.uicomponent.CPanel panRemarks;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSign;
    private com.see.truetransact.uicomponent.CPanel panSignButtons;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuspendCustomer;
    private com.see.truetransact.uicomponent.CPanel panTeleCommunication;
    private com.see.truetransact.uicomponent.CPanel panTelecomDetails;
    private com.see.truetransact.uicomponent.CPanel proofPanel;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExistingCust;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGender;
    private com.see.truetransact.uicomponent.CRadioButton rdoITDec_F60;
    private com.see.truetransact.uicomponent.CRadioButton rdoITDec_F61;
    private com.see.truetransact.uicomponent.CRadioButton rdoITDec_Pan;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMaritalStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpContactList;
    private com.see.truetransact.uicomponent.CScrollPane srpCustomerHistory;
    private com.see.truetransact.uicomponent.CScrollPane srpJointAcctHolder;
    private com.see.truetransact.uicomponent.CScrollPane srpPhoneList;
    private com.see.truetransact.uicomponent.CScrollPane srpPhotoLoad;
    private com.see.truetransact.uicomponent.CScrollPane srpProofList;
    private com.see.truetransact.uicomponent.CScrollPane srpSignLoad;
    private com.see.truetransact.uicomponent.CTabbedPane tabContactAndIdentityInfo;
    private com.see.truetransact.uicomponent.CTabbedPane tabCorpCust;
    private com.see.truetransact.uicomponent.CTable tblContactList;
    private com.see.truetransact.uicomponent.CTable tblCustomerHistory;
    private com.see.truetransact.uicomponent.CTable tblJointAcctHolder;
    private com.see.truetransact.uicomponent.CTable tblPhoneList;
    private com.see.truetransact.uicomponent.CTable tblProofList;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtCrAvldSince;
    private com.see.truetransact.uicomponent.CDateField tdtDtEstablished;
    private com.see.truetransact.uicomponent.CDateField tdtFinacialYrEnd;
    private com.see.truetransact.uicomponent.CDateField tdtNetWorthAsOn;
    private com.see.truetransact.uicomponent.CDateField tdtRevokedCustDate;
    private com.see.truetransact.uicomponent.CDateField tdtSuspendCustFrom;
    private com.see.truetransact.uicomponent.CTextField txtAddrRemarks;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtAreaCode;
    private com.see.truetransact.uicomponent.CTextField txtAuthCapital;
    private com.see.truetransact.uicomponent.CTextField txtAuthCustId;
    private com.see.truetransact.uicomponent.CTextField txtBankruptsy;
    private com.see.truetransact.uicomponent.CTextField txtCEO;
    private com.see.truetransact.uicomponent.CTextField txtCompany;
    private com.see.truetransact.uicomponent.CPasswordField txtCustPwd;
    private com.see.truetransact.uicomponent.CTextField txtCustUserid;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtDividendPercentage;
    private com.see.truetransact.uicomponent.CTextField txtEmailID;
    private com.see.truetransact.uicomponent.CTextField txtIssuedCapital;
    private com.see.truetransact.uicomponent.CTextField txtLastYrPL;
    private com.see.truetransact.uicomponent.CTextField txtMemNum;
    private com.see.truetransact.uicomponent.CTextField txtNetWorth;
    private com.see.truetransact.uicomponent.CTextField txtPanNumber;
    private com.see.truetransact.uicomponent.CTextField txtPhoneNumber;
    private com.see.truetransact.uicomponent.CTextField txtPincode;
    private com.see.truetransact.uicomponent.CTextField txtRegNumber;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRiskRate;
    private com.see.truetransact.uicomponent.CTextField txtStreet;
    private com.see.truetransact.uicomponent.CTextField txtSubscribedCapital;
    private com.see.truetransact.uicomponent.CTextField txtSuspRevRemarks;
    private com.see.truetransact.uicomponent.CTextField txtTaxliability;
    private com.see.truetransact.uicomponent.CTextField txtTotalIncome;
    private com.see.truetransact.uicomponent.CTextField txtTotalNonTaxExp;
    private com.see.truetransact.uicomponent.CTextField txtTotalResource;
    private com.see.truetransact.uicomponent.CPasswordField txtTransPwd;
    private com.see.truetransact.uicomponent.CTextField txtUniqueId;
    private com.see.truetransact.uicomponent.CTextField txtWebSite;
    private com.see.truetransact.uicomponent.CTextField txtprofitBefTax;
    // End of variables declaration//GEN-END:variables
    
}
