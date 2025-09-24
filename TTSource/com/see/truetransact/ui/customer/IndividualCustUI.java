/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IndividualCustomer.java
 *
 * Created on August 5, 2003, 1:02 PM
 */
package com.see.truetransact.ui.customer;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;

import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
//import java.net.URL;
//import com.see.truetransact.uivalidation.ToDateValidation;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import com.see.truetransact.ui.termloan.GoldLoanUI;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.ui.termloan.TermLoanUI;
import com.see.truetransact.ui.share.ShareUI;
import com.see.truetransact.ui.suspenseaccount.SuspenseAccountMasterUI;
import com.see.truetransact.ui.operativeaccount.AccountsUI;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.deposit.multipledeposit.MultipleTermDepositUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.common.EngToMalTransliterator;
import com.see.truetransact.ui.common.viewall.MalayalamDicSearchUI;
import com.see.truetransact.ui.common.viewall.MalayalamKeyboardUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.*;
/**
 *
 * @author karthik Modified by Annamalai Bala RS Ashok .V Swaroop.V
 */
public class IndividualCustUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private CustomerOB observable = null;
    private CustomerUISupport objCustomerUISupport;
    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.CustomerRB", ProxyParameters.LANGUAGE);
    IndividualCustMRB objMandatoryRB = new IndividualCustMRB();
    private HashMap mandatoryMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private boolean phoneExist;
    private boolean proofExist;
    private int proofRow;
    private int phoneRow;
    private String mandatoryMessage = "";
    final int AUTHORIZE = 3;
    final int DELETE = 1;
    private int viewType = -1;
    boolean isFilled = false;
    private final String CLASSNAME = this.getClass().getName();
    private String actionType = "";
    private String CUSTOMERID;//Variable used to get the CustomerId when the popup comes on clicking on Search button in the UI
    private String CUST_TYPE_COURT = "COURT";
   // boolean checkminor = false;
    boolean flag = false;
    int updateTab = -1;
    private boolean updateMode = false;
    boolean chk = false;
    boolean chkLand = false;
    int LandDetailsupdateTab = -1;
    private boolean LandDetupdateMode = false;
    public int minorAge = 0;
    public int retireAge = 0;
    private HashMap loanCustMap;
    GoldLoanUI goldLoanUI = null;
    AccountsUI accountsUI = null;
    TermDepositUI termdepositUI = null;
    MultipleTermDepositUI multipleTermDepositUI = null;
    TermLoanUI termloanUI = null;
    SuspenseAccountMasterUI suspenseaccountUI = null;
    ShareUI shareUI = null;
    boolean checkminor = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    //--- Defines the Screen Name for using it in IntroDetials
    final String SCREEN = "INDIVIDUAL"; // earlier screen = CUS
    private Date currDt = null;
    /**
     * Declare a new instance of the IntroducerUI...
     */
    IntroducerUI introducerUI = new IntroducerUI(SCREEN);
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private String ScreenName="";
    List cboPostOfficeItemsList;
    /**
     * Creates new form IndividualCustomer
     */
    public IndividualCustUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
    }

    private void initStartup() {
        setFieldNames();
        setObservable();
        objCustomerUISupport = new CustomerUISupport(observable);
        //To add the  Introduce Tab in the IndividualCustUI...
        tabIndCust.add(introducerUI, "Introducer", 1);
        internationalize();
        observable.resetForm();
        //To add the  Introduce Tab in the IndividualCustUI...
//        tabIndCust.add(introducerUI, "Introducer", 1);
        initComponentData();
        initTableData();
        ClientUtil.enableDisable(this, false);
        setMaximumLength();
        setButtonEnableDisable();
        setMandatoryHashMap();
        setMandatoryMarks();
        setHelpMessage();
//        System.out.println("!!!! resourceBundle.getLocale().getLanguage() :"+resourceBundle.getLocale().getLanguage());
//        System.out.println("!!!! resourceBundle.getLocale().getCountry() :"+resourceBundle.getLocale().getCountry());
        colWidthChange();
        btnGenerateAppPwd.setEnabled(true);
        btnSearch.setEnabled(false);
        btnStaffId.setEnabled(false);
        txtStaffId.setEditable(false);
        txtStaffId.setEnabled(false);
        cboCustomerType.setEnabled(false);
        tabIndCust.resetVisits();
        tabContactAndIdentityInfo.resetVisits();
        tabIndCust.remove(panIncomeParticulars);
        tabIndCust.remove(panLandDetails);
        txtCustomerID.setEnabled(true);
        txtCustomerID.setEditable(true);
        List lst = ClientUtil.executeQuery("getMinorRetireAge", null);
        HashMap ageMap = new HashMap();
        ageMap = (HashMap) lst.get(0);
        minorAge = CommonUtil.convertObjToInt(ageMap.get("MINOR_AGE"));
        retireAge = CommonUtil.convertObjToInt(ageMap.get("RETIREMENT_AGE"));
        lblPhoto.setToolTipText("Double click to view full Photo");
        lblSign.setToolTipText("Double click to view full Signature");
//        changed by nikhil
        txtPanNumber.setEnabled(false);
//        lblPanNumber.setVisible(false);
        //added by AkhilA

        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            lblJoiningDate.setVisible(true);
            tdtJoiningDate.setVisible(true);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String today = formatter.format(currDt.clone());
            tdtJoiningDate.setDateValue(today);
        } else {
            lblJoiningDate.setVisible(false);
            tdtJoiningDate.setVisible(false);
        }

    }

    private void initTableData() {
        tblContactList.setModel(observable.getTblContactList());
        tblPhoneList.setModel(observable.getTblPhoneList());
        tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
        tblIncParticulars.setModel(observable.getTblIncomeParticulars());
        tblCustomerLandDetails.setModel(observable.getTblCustomerLandDetails());
    }

    private void setObservable() {
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '1' indicates that the customer type is INDIVIDUAL
        observable = new CustomerOB(1);
        observable.addObserver(this);
    }

    private void setMandatoryMarks() {
        objCustomerUISupport.putMandatoryMarks(CLASSNAME, panPersonalInfo);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME, panGuardianDetails);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME, panContactInfo);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME, panKYC);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME, panPassPortDetails);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME, panIncomeParticulars);
        objCustomerUISupport.putMandatoryMarks(CLASSNAME, panLandDetails);
    }

    public void callInternationalize() {
        internationalize();
    }

    private boolean isBlocked() {
        boolean blocked = false;
        ArrayList resultList = null;
        HashMap where = new HashMap();
        if (!txtStreet.getText().equals("")) {
            where.put("BUSINESS_ADDR", txtStreet.getText());
        }
        if (!txtFirstName.getText().equals("") || !txtMiddleName.getText().equals("") || !txtLastName.getText().equals("")) {
            where.put("FNAME", txtFirstName.getText());
            where.put("MNAME", txtMiddleName.getText());
            where.put("LNAME", txtLastName.getText());
        }

        resultList = (ArrayList) ClientUtil.executeQuery("getBlockedCustomerDetails", where);

        if (resultList != null) {
            if (resultList.size() > 0) {
                callView("getBlockedCustomerDetails", where);
                blocked = true;
            }
        }
        return blocked;
    }

    private void setMaximumLength() {
        txtFirstName.setMaxLength(128);
        txtMiddleName.setMaxLength(128);
        txtLastName.setMaxLength(128);
        txtName.setMaxLength(64);
//        txtNetWorth.setValidation(new CurrencyValidation(14,2));
        txtRemarks.setMaxLength(256);
        txtCompany.setMaxLength(128);
        txtEmailID.setMaxLength(64);
        txtNationality.setMaxLength(48);
        txtLanguage.setMaxLength(48);
        txtSsn.setMaxLength(64);
        txtCustUserid.setMaxLength(64);
        txtBranchId.setMaxLength(8);
        txtStreet.setMaxLength(256);
        txtArea.setMaxLength(128);
        txtPincode.setMaxLength(16);
        txtDesignation.setMaxLength(64);
        txtRiskRate.setMaxLength(8);
        txtRiskRate.setAllowNumber(true);
        //txtAreaCode.setMaxLength(16);
        txtPhoneNumber.setMaxLength(16);
        txtSsn.setMaxLength(64);
        txtSsn.setAllowAll(true);
        txtStaffId.setMaxLength(16);
        txtArea.setMaxLength(128);
        txtAddrRemarks.setMaxLength(256);
        txtGuardianNameNO.setMaxLength(64);
        txtGuardianACodeNO.setValidation(new NumericValidation());
        txtGuardianACodeNO.setMaxLength(8);
        txtGuardianACodeNO.setAllowNumber(true);
        txtGuardianACodeNO.setAllowAll(false);
        txtGuardianPhoneNO.setValidation(new NumericValidation());
        txtGuardianPhoneNO.setMaxLength(16);
        txtGuardianPhoneNO.setAllowNumber(true);
        txtGuardianPhoneNO.setAllowAll(false);
        txtGStreet.setMaxLength(256);
        txtGArea.setMaxLength(128);
        txtGPinCode.setMaxLength(16);
        txtAreaCode.setValidation(new NumericValidation());
        txtAreaCode.setMaxLength(8);
        txtAreaCode.setAllowNumber(true);
        txtAreaCode.setAllowAll(false);
        txtPhoneNumber.setValidation(new NumericValidation());
        txtPhoneNumber.setMaxLength(16);
        txtPhoneNumber.setAllowNumber(true);
        txtPhoneNumber.setAllowAll(false);
        txtPassportFirstName.setMaxLength(128);
        txtPassportMiddleName.setMaxLength(128);
        txtPassportLastName.setMaxLength(128);
        txtAge.setValidation(new NumericValidation());
        txtIncIncome.setMaxLength(17);
        txtIncIncome.setValidation(new CurrencyValidation(14, 2));
        txtLoc.setValidation(new DefaultValidation());
        txtSrNo.setAllowAll(true);
        txtAreaInAcrs.setAllowAll(true);
        txtVillage.setValidation(new DefaultValidation());
        txtPo.setValidation(new DefaultValidation());
        txtHobli.setValidation(new DefaultValidation());
        txtGuardianAge.setValidation(new NumericValidation());
        txtCustomerID.setAllowAll(true);
          //commented by rishad 23/02/2015
       // txtUid.setAllowAll(true);
        txtWardNo.setAllowAll(true);
        txtWardNo.setMaxLength(32);
          //commented by rishad 23/02/2015
//        txtRationCardNo.setAllowAll(true);
//        txtRationCardNo.setMaxLength(32);
    }

    /* Auto Generated Method - setMandatoryHashMap()
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("txtMiddleName", new Boolean(true));
        mandatoryMap.put("txtLastName", new Boolean(false));
        mandatoryMap.put("rdoGender_Male", new Boolean(true));
        mandatoryMap.put("rdoMaritalStatus_Single", new Boolean(true));
        mandatoryMap.put("txtNationality", new Boolean(true));
        mandatoryMap.put("txtLanguage", new Boolean(false));
        mandatoryMap.put("cboTitle", new Boolean(true));
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("cboResidentialStatus", new Boolean(true));
//        mandatoryMap.put("tdtDateOfBirth", new Boolean(true));
        mandatoryMap.put("cboCareOf", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("cboVehicle", new Boolean(true));
        mandatoryMap.put("cboCustomerType", new Boolean(true));
        mandatoryMap.put("cboRelationManager", new Boolean(true));
        mandatoryMap.put("txtCompany", new Boolean(true));
        mandatoryMap.put("cboAnnualIncomeLevel", new Boolean(true));
        mandatoryMap.put("cboPrefCommunication", new Boolean(true));
        mandatoryMap.put("cboCustomerGroup", new Boolean(true));
        mandatoryMap.put("cboProfession", new Boolean(true));
        mandatoryMap.put("cboPrimaryOccupation", new Boolean(true));
        mandatoryMap.put("cboEducationalLevel", new Boolean(true));
        mandatoryMap.put("txtEmailID", new Boolean(true));
        mandatoryMap.put("txtNetWorth", new Boolean(true));
        mandatoryMap.put("cboMembershipClass", new Boolean(true));
        mandatoryMap.put("cboCustStatus", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPhoneNumber", new Boolean(true));
        mandatoryMap.put("txtAreaCode", new Boolean(true));
        mandatoryMap.put("cboPhoneType", new Boolean(true));
        mandatoryMap.put("txtSsn", new Boolean(true));
        mandatoryMap.put("txtTransPwd", new Boolean(true));
        mandatoryMap.put("txtCustUserid", new Boolean(true));
        mandatoryMap.put("txtCustPwd", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("cboIntroType", new Boolean(true));
        mandatoryMap.put("txtAddrRemarks", new Boolean(false));
        mandatoryMap.put("chkAddrVerified", new Boolean(true));
        mandatoryMap.put("chkPhVerified", new Boolean(true));
        mandatoryMap.put("chkFinanceStmtVerified", new Boolean(true));
        mandatoryMap.put("tdtCrAvldSince", new Boolean(true));
        mandatoryMap.put("txtRiskRate", new Boolean(true));
        mandatoryMap.put("txtBranchId", new Boolean(true));
        mandatoryMap.put("txtGuardianNameNO", new Boolean(true));
        mandatoryMap.put("cboRelationNO", new Boolean(true));
        mandatoryMap.put("txtGuardianACodeNO", new Boolean(true));
        mandatoryMap.put("txtGuardianPhoneNO", new Boolean(true));
        mandatoryMap.put("txtGStreet", new Boolean(true));
        mandatoryMap.put("txtGArea", new Boolean(true));
        mandatoryMap.put("cboGCountry", new Boolean(true));
        mandatoryMap.put("cboGCity", new Boolean(true));
        mandatoryMap.put("cboGState", new Boolean(true));
        mandatoryMap.put("txtGPinCode", new Boolean(true));
        mandatoryMap.put("chkSentThanksLetter", new Boolean(true));
        mandatoryMap.put("chkConfirmationfromIntroducer", new Boolean(true));
        mandatoryMap.put("txtStaffId", new Boolean(true));
        mandatoryMap.put("txtDesignation", new Boolean(false));
        mandatoryMap.put("cboCaste", new Boolean(false));
        mandatoryMap.put("cboSubCaste", new Boolean(false));
        mandatoryMap.put("cboReligion", new Boolean(false));
        mandatoryMap.put("txtPanNumber", new Boolean(false));
        mandatoryMap.put("cboAddrProof", new Boolean(false));
        mandatoryMap.put("cboIdenProof", new Boolean(false));
        mandatoryMap.put("txtPassportFirstName", new Boolean(true));
        mandatoryMap.put("txtPassportMiddleName", new Boolean(true));
        mandatoryMap.put("tdtPassportIssueDt", new Boolean(true));
        mandatoryMap.put("tdtPassportValidUpto", new Boolean(true));
        mandatoryMap.put("txtPassportLastName", new Boolean(true));
        mandatoryMap.put("cboPassportTitle", new Boolean(true));
        mandatoryMap.put("txtPassportNo", new Boolean(true));
        mandatoryMap.put("txtPassportIssueAuth", new Boolean(true));
        mandatoryMap.put("cboPassportIssuePlace", new Boolean(true));
        mandatoryMap.put("cboFarClass", new Boolean(false));
        mandatoryMap.put("txtKartha", new Boolean(false));
        mandatoryMap.put("txtAge", new Boolean(true));
        mandatoryMap.put("chkIncParticulars", new Boolean(false));
        mandatoryMap.put("txtIncIncome", new Boolean(true));
        mandatoryMap.put("txtIncName", new Boolean(true));
        mandatoryMap.put("cboIncPack", new Boolean(true));
        mandatoryMap.put("cboIncRelation", new Boolean(true));
        mandatoryMap.put("chkLandDetails", new Boolean(false));
        mandatoryMap.put("txtLoc", new Boolean(true));
        mandatoryMap.put("txtSrNo", new Boolean(true));
        mandatoryMap.put("txtAreaInAcrs", new Boolean(true));
        mandatoryMap.put("cboIrrigated", new Boolean(true));
        mandatoryMap.put("cboSrIrrigation", new Boolean(true));
        mandatoryMap.put("txtVillage", new Boolean(true));
        mandatoryMap.put("txtPo", new Boolean(true));
        mandatoryMap.put("txtHobli", new Boolean(true));
        mandatoryMap.put("cboTaluk", new Boolean(true));
        mandatoryMap.put("cboDistrict", new Boolean(true));
        mandatoryMap.put("cboLandDetState", new Boolean(true));
        mandatoryMap.put("txtLandDetPincode", new Boolean(true));
        mandatoryMap.put("txtGuardianAge", new Boolean(true));
        mandatoryMap.put("txtBankruptsy", new Boolean(false));
        mandatoryMap.put("rdoITDec_Pan", new Boolean(true));
        mandatoryMap.put("cboIncProfession", new Boolean(true));
        mandatoryMap.put("cbcomboDesam", new Boolean(true));        
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void saveAsSettings() {
        ClientUtil.enableDisable(panAdditionalInfo, true);
        ClientUtil.enableDisable(panAdditionalInformation, true);
        ClientUtil.enableDisable(panCompanyInfo, true);
        ClientUtil.enableDisable(panPhoto, true);
        ClientUtil.enableDisable(panSign, true);
        enableHelpBtn(false);
        txtCustomerID.setText("");
        txtFirstName.setEditable(true);
        txtLastName.setEditable(true);
        txtMiddleName.setEditable(true);
        cboTitle.setEditable(true);
        cboCareOf.setEditable(true);
        txtName.setEditable(true);
        txtSsn.setEditable(true);
        txtCustUserid.setEditable(true);
        txtTransPwd.setEditable(true);
        txtRiskRate.setEditable(true);
        btnPhotoLoad.setEnabled(true);
        btnPhotoRemove.setEnabled(true);
        btnSignLoad.setEnabled(true);
        btnSignRemove.setEnabled(true);
        btnProofPhotoLoad.setEnabled(true);
        btnProofPhotoRemove.setEnabled(true);
    }

    /**
     * Making Maritial Status field visible or invisible according to the
     * custoemertype wheher its major or minor *
     */
    private void makeMaritialStatusVisible(boolean flag) {
        panMaritalStatus.setVisible(flag);
        lblMaritalStatus.setVisible(flag);
    }

    /**
     * Enabling or Disabling Certain MIS Panel fields depening on whether the
     * customer type is major or minor *
     */
    private void enableDisableFields(boolean flag) {
        if (!flag) {
            cboProfession.setSelectedItem("");
            txtCompany.setText("");
            cboPrimaryOccupation.setSelectedItem("");
            txtDesignation.setText("");
            cboAnnualIncomeLevel.setSelectedItem("");
        }
        cboProfession.setEnabled(flag);
        txtCompany.setEditable(flag);
        cboPrimaryOccupation.setEnabled(flag);
        txtDesignation.setEditable(flag);
        cboAnnualIncomeLevel.setEnabled(flag);
    }

    /**
     * This method is used to clear, disable the panGaurdianDeatails and then
     * remove the same when the customer is not minor
     */
    private void resetGaurdianPanel() {
        ClientUtil.clearAll(panGuardianDetails);
        ClientUtil.enableDisable(panGuardianDetails, false);
        tabIndCust.remove(panGuardianDetails);
        makeMaritialStatusVisible(true);
        enableDisableFields(true);
    }

    /**
     * This method is used to check whether a customer is minor or major
     * according to the DateofBirth entered *
     */
    private boolean isMinor() {

        boolean isMinor = false;
        if (tdtDateOfBirth.getDateValue().length() != 0) {
            Date dob = DateUtil.getDateMMDDYYYY(tdtDateOfBirth.getDateValue());
            Date dobMinor = DateUtil.addDays(dob, (minorAge * 365));
            Date dobSenior = DateUtil.addDays(dob, (retireAge * 365));
            double diff = DateUtil.dateDiff(dobMinor, (Date) currDt.clone());
            if (diff < 0) {
                isMinor = true;
                observable.setIsMinor(true);
                isMinor = minorMajorCheck(isMinor);
            } else if (diff >= 0) {
                isMinor = false;
                observable.setIsMinor(false);
                MajorminorCheck();
                resetGaurdianPanel();
            }
            diff = DateUtil.dateDiff(dobSenior, (Date) currDt.clone());
            if (diff >= 0) {
                isMinor = false;
//                lblCustomerStatus.setText(resourceBundle.getString("seniorCitizen"));
                observable.setIsMinor(false);
                resetGaurdianPanel();
            }
//            int currentYear = curDate.getYear();
//            int dob = DateUtil.getDateMMDDYYYY(tdtDateOfBirth.getDateValue()).getYear();
//            final int ageInYears = currentYear - dob;
//            if(ageInYears==minorAge || ageInYears<minorAge){
//                isMinor = true;
//                observable.setIsMinor(true);
//            }else if(ageInYears > minorAge && ageInYears < retireAge){
//                isMinor = false;
//                observable.setIsMinor(false);
//                resetGaurdianPanel();
//            }
//            else if(ageInYears >=retireAge){
//                isMinor = false;
//                lblCustomerStatus.setText(resourceBundle.getString("seniorCitizen"));
//                observable.setIsMinor(false);
//                resetGaurdianPanel();
//            }
        } else if (txtAge.getText().length() > 0) {
            int a = CommonUtil.convertObjToInt(txtAge.getText());
            if (a < minorAge) {
                isMinor = true;
            } else {
                isMinor = false;
            }
        }

        return isMinor;
    }

    private boolean isMinorSave() {

        boolean isMinor = false;
        if (tdtDateOfBirth.getDateValue().length() != 0) {
            Date dob = DateUtil.getDateMMDDYYYY(tdtDateOfBirth.getDateValue());
            Date dobMinor = DateUtil.addDays(dob, (minorAge * 365));
            Date dobSenior = DateUtil.addDays(dob, (retireAge * 365));
            double diff = DateUtil.dateDiff(dobMinor, (Date) currDt.clone());
            if (diff < 0) {
                isMinor = true;

            } else {
                isMinor = false;

            }


        } else if (txtAge.getText().length() > 0) {
            int a = CommonUtil.convertObjToInt(txtAge.getText());
            if (a < minorAge) {
                observable.setIsMinor(true);
                isMinor = true;
            } else {
                observable.setIsMinor(false);
                isMinor = false;
            }
        } else {
            observable.setIsMinor(isMinor);
        }
        return isMinor;
    }

    private boolean minorMajorCheck(boolean flag) {
        if (!chkMinor.isSelected()) {
            ClientUtil.showMessageWindow("Minor Option Is Not Selected!!!!!!");
           
//            lblCustomerStatus.setText("");
           // tdtDateOfBirth.setDateValue("");
            txtAge.setText("");
            flag = false;
            return flag;
        }
        return true;
    }

    private void MajorminorCheck() {
        if (chkMinor.isSelected()) {
            ClientUtil.showMessageWindow("Minor Option Is Selected!!!!");
             checkminor = false;
                chkMinor.setSelected(false);
           // tdtDateOfBirth.setDateValue("");
            return;
        }
    }

    /**
     * This method i{s used to do certain enable or disable according to whether
     * the customer is minor or major *
     */
    private void enableDisablePanGaurdian() {
        if (isMinor()) {
//            lblCustomerStatus.setText(resourceBundle.getString("minor"));
            ClientUtil.enableDisable(panGuardianDetails, true);
            tabIndCust.addTab("Guardian Details", panGuardianDetails);
            makeMaritialStatusVisible(false);
            rdoMaritalStatus_Single.setSelected(true);
            enableDisableFields(false);
            observable.setNewGuardian(true);
        } else {
            resetGaurdianPanel();
            makeMaritialStatusVisible(true);
            ClientUtil.enableDisable(panGuardianDetails, false);
        }
    }

    private void enableHelpBtn(boolean flag) {
        btnStaffId.setEnabled(flag);
    }

    /**
     * Method used to do Enabling or disabling of buttons after EDIT bUTOON IS
     * CLICKED *
     */
    private void enableDisable() {
        ClientUtil.enableDisable(this, true);
        cboCustomerType.setEnabled(false);
        enableDisablePanGaurdian();
        ClientUtil.enableDisable(this.panPhoneAreaNumber, false);
        objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
        objCustomerUISupport.setProofButtonEnableDisable(btnProofNew, btnProofAdd, btnProofDelete);
        objCustomerUISupport.setPhoneButtonEnableDisableDefault(btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        objCustomerUISupport.setContactAddEnableDisable(true, btnContactAdd);
        objCustomerUISupport.setPhotoSignLoadEnableDisable(true, btnPhotoLoad, btnSignLoad);
        enableHelpBtn(true);
        btnSearch.setEnabled(false);
        btnContactDelete.setEnabled(false);
        btnContactAdd.setEnabled(false);
        cboMembershipClass.setEnabled(true);
    }

    private boolean isGuardianMinor() {

        boolean isMinor = false;
        if (tdtGuardianDOB.getDateValue().length() != 0) {
            Date dob = DateUtil.getDateMMDDYYYY(tdtGuardianDOB.getDateValue());
            Date dobMinor = DateUtil.addDays(dob, (minorAge * 365));
            Date dobSenior = DateUtil.addDays(dob, (retireAge * 365));
            double diff = DateUtil.dateDiff(dobMinor, (Date) currDt.clone());
            if (diff < 0) {
                isMinor = true;
            } else if (diff >= 0) {
                isMinor = false;
            }
            diff = DateUtil.dateDiff(dobSenior, (Date) (Date) currDt.clone());
            if (diff >= 0) {
                isMinor = false;
            }
        }
        return isMinor;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoGender = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMaritalStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoITDec = new com.see.truetransact.uicomponent.CButtonGroup();
        panCustomer = new com.see.truetransact.uicomponent.CPanel();
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
        panAddrRemarks = new com.see.truetransact.uicomponent.CPanel();
        panKara = new com.see.truetransact.uicomponent.CPanel();
        lblCustVillage = new com.see.truetransact.uicomponent.CLabel();
        cboCustVillage = new com.see.truetransact.uicomponent.CComboBox();
        lblCustTaluk = new com.see.truetransact.uicomponent.CLabel();
        cboCustTaluk = new com.see.truetransact.uicomponent.CComboBox();
        panPost = new com.see.truetransact.uicomponent.CPanel();
        cboPostOffice = new com.see.truetransact.uicomponent.CComboBox();
        lblPostOffice = new com.see.truetransact.uicomponent.CLabel();
        lblWardName = new com.see.truetransact.uicomponent.CLabel();
        cboWardName = new com.see.truetransact.uicomponent.CComboBox();
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
        panSaveBtn = new com.see.truetransact.uicomponent.CPanel();
        btnContactAdd = new com.see.truetransact.uicomponent.CButton();
        btnContactNew = new com.see.truetransact.uicomponent.CButton();
        btnContactDelete = new com.see.truetransact.uicomponent.CButton();
        lblAddrRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtAddrRemarks = new com.see.truetransact.uicomponent.CTextField();
        panPhotoSign = new com.see.truetransact.uicomponent.CPanel();
        panPhoto = new com.see.truetransact.uicomponent.CPanel();
        srpPhotoLoad = new com.see.truetransact.uicomponent.CScrollPane();
        lblPhoto = new com.see.truetransact.uicomponent.CLabel();
        panPhotoButtons = new com.see.truetransact.uicomponent.CPanel();
        btnPhotoLoad = new com.see.truetransact.uicomponent.CButton();
        btnPhotoRemove = new com.see.truetransact.uicomponent.CButton();
        panSign = new com.see.truetransact.uicomponent.CPanel();
        srpSignLoad = new com.see.truetransact.uicomponent.CScrollPane();
        lblSign = new com.see.truetransact.uicomponent.CLabel();
        panSignButtons = new com.see.truetransact.uicomponent.CPanel();
        btnSignLoad = new com.see.truetransact.uicomponent.CButton();
        btnSignRemove = new com.see.truetransact.uicomponent.CButton();
        panAdditionalInformation = new com.see.truetransact.uicomponent.CPanel();
        lblSsn = new com.see.truetransact.uicomponent.CLabel();
        txtSsn = new com.see.truetransact.uicomponent.CTextField();
        lblCustUserid = new com.see.truetransact.uicomponent.CLabel();
        txtCustUserid = new com.see.truetransact.uicomponent.CTextField();
        lblCustPwd = new com.see.truetransact.uicomponent.CLabel();
        txtCustPwd = new com.see.truetransact.uicomponent.CPasswordField();
        txtTransPwd = new com.see.truetransact.uicomponent.CPasswordField();
        txtKartha = new com.see.truetransact.uicomponent.CTextField();
        lblFarClass = new com.see.truetransact.uicomponent.CLabel();
        cboFarClass = new com.see.truetransact.uicomponent.CComboBox();
        lblKartha = new com.see.truetransact.uicomponent.CLabel();
        txtBankruptsy = new com.see.truetransact.uicomponent.CTextField();
        lblBankruptsy = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblTransPwd = new com.see.truetransact.uicomponent.CLabel();
        lblVehicle = new com.see.truetransact.uicomponent.CLabel();
        cboVehicle = new com.see.truetransact.uicomponent.CComboBox();
        btnGenerateAppPwd = new com.see.truetransact.uicomponent.CButton();
        panCompanyInfo = new com.see.truetransact.uicomponent.CPanel();
        lblCrAvldSince = new com.see.truetransact.uicomponent.CLabel();
        lblRiskRate = new com.see.truetransact.uicomponent.CLabel();
        tdtCrAvldSince = new com.see.truetransact.uicomponent.CDateField();
        txtRiskRate = new com.see.truetransact.uicomponent.CTextField();
        panPassPortDetails = new com.see.truetransact.uicomponent.CPanel();
        panPassportFirstName = new com.see.truetransact.uicomponent.CPanel();
        txtPassportFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboPassportTitle = new com.see.truetransact.uicomponent.CComboBox();
        txtPassportMiddleName = new com.see.truetransact.uicomponent.CTextField();
        txtPassportLastName = new com.see.truetransact.uicomponent.CTextField();
        lblFirstNamePass = new com.see.truetransact.uicomponent.CLabel();
        lblMiddleNamePass = new com.see.truetransact.uicomponent.CLabel();
        lblLastNamePass = new com.see.truetransact.uicomponent.CLabel();
        txtPassportNo = new com.see.truetransact.uicomponent.CTextField();
        lblPassPortNo = new com.see.truetransact.uicomponent.CLabel();
        tdtPassportValidUpto = new com.see.truetransact.uicomponent.CDateField();
        lblValidUpto = new com.see.truetransact.uicomponent.CLabel();
        tdtPassportIssueDt = new com.see.truetransact.uicomponent.CDateField();
        lblIssuedDt = new com.see.truetransact.uicomponent.CLabel();
        txtPassportIssueAuth = new com.see.truetransact.uicomponent.CTextField();
        cboPassportIssuePlace = new com.see.truetransact.uicomponent.CComboBox();
        lblPassPlaceIssued = new com.see.truetransact.uicomponent.CLabel();
        lblPassIssueAuth = new com.see.truetransact.uicomponent.CLabel();
        btnClearPassport = new com.see.truetransact.uicomponent.CButton();
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
        panAddressProofPhoto = new com.see.truetransact.uicomponent.CPanel();
        panProofPhoto = new com.see.truetransact.uicomponent.CPanel();
        srpProofPhotoLoad = new com.see.truetransact.uicomponent.CScrollPane();
        lblProofPhoto = new com.see.truetransact.uicomponent.CLabel();
        panProofUploanButtons = new com.see.truetransact.uicomponent.CPanel();
        btnProofPhotoLoad = new com.see.truetransact.uicomponent.CButton();
        btnProofPhotoRemove = new com.see.truetransact.uicomponent.CButton();
        tabIndCust = new com.see.truetransact.uicomponent.CTabbedPane();
        panPersonalInfo = new com.see.truetransact.uicomponent.CPanel();
        panAdditionalInfo = new com.see.truetransact.uicomponent.CPanel();
        panGender = new com.see.truetransact.uicomponent.CPanel();
        lblGender = new com.see.truetransact.uicomponent.CLabel();
        rdoGender_Male = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGender_Female = new com.see.truetransact.uicomponent.CRadioButton();
        lblDivision = new com.see.truetransact.uicomponent.CLabel();
        cboDivision = new com.see.truetransact.uicomponent.CComboBox();
        panDOB = new com.see.truetransact.uicomponent.CPanel();
        tdtDateOfBirth = new com.see.truetransact.uicomponent.CDateField();
        txtAge = new com.see.truetransact.uicomponent.CTextField();
        lblAge = new com.see.truetransact.uicomponent.CLabel();
        lblDateOfBirth = new com.see.truetransact.uicomponent.CLabel();
        panCareOf = new com.see.truetransact.uicomponent.CPanel();
        cboCareOf = new com.see.truetransact.uicomponent.CComboBox();
        lblCareOf = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        txtEmailID = new com.see.truetransact.uicomponent.CTextField();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        lblEmailID = new com.see.truetransact.uicomponent.CLabel();
        panFirstName = new com.see.truetransact.uicomponent.CPanel();
        txtFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboTitle = new com.see.truetransact.uicomponent.CComboBox();
        lblFirstName = new com.see.truetransact.uicomponent.CLabel();
        panSearch1 = new com.see.truetransact.uicomponent.CPanel();
        panSearch3 = new com.see.truetransact.uicomponent.CPanel();
        panCareOf1 = new com.see.truetransact.uicomponent.CPanel();
        cboAgentCustId = new com.see.truetransact.uicomponent.CComboBox();
        lblAgentCustId = new com.see.truetransact.uicomponent.CLabel();
        lblAgentCustIdValue = new com.see.truetransact.uicomponent.CLabel();
        panCareOf2 = new com.see.truetransact.uicomponent.CPanel();
        panMaritalStatus = new com.see.truetransact.uicomponent.CPanel();
        rdoMaritalStatus_Single = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMaritalStatus_Married = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMaritalStatus_Widow = new com.see.truetransact.uicomponent.CRadioButton();
        lblMaritalStatus = new com.see.truetransact.uicomponent.CLabel();
        panCareOf3 = new com.see.truetransact.uicomponent.CPanel();
        lblRetireDt = new com.see.truetransact.uicomponent.CLabel();
        tdtretireDt = new com.see.truetransact.uicomponent.CDateField();
        lblRetireAge = new com.see.truetransact.uicomponent.CLabel();
        txtRetireAge = new com.see.truetransact.uicomponent.CTextField();
        panCareOf4 = new com.see.truetransact.uicomponent.CPanel();
        chkMinor = new com.see.truetransact.uicomponent.CCheckBox();
        lblChkMinor = new com.see.truetransact.uicomponent.CLabel();
        lblJoiningDate = new com.see.truetransact.uicomponent.CLabel();
        tdtJoiningDate = new com.see.truetransact.uicomponent.CDateField();
        panFirstName1 = new com.see.truetransact.uicomponent.CPanel();
        lblMiddleName = new com.see.truetransact.uicomponent.CLabel();
        lblLastName = new com.see.truetransact.uicomponent.CLabel();
        txtMiddleName = new com.see.truetransact.uicomponent.CTextField();
        txtLastName = new com.see.truetransact.uicomponent.CTextField();
        panFirstName2 = new com.see.truetransact.uicomponent.CPanel();
        lblBranchId = new com.see.truetransact.uicomponent.CLabel();
        txtBranchId = new com.see.truetransact.uicomponent.CTextField();
        panCustomerInfo = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerType = new com.see.truetransact.uicomponent.CLabel();
        cboCustomerType = new com.see.truetransact.uicomponent.CComboBox();
        lblCustomerGroup = new com.see.truetransact.uicomponent.CLabel();
        lblRelationManager = new com.see.truetransact.uicomponent.CLabel();
        cboRelationManager = new com.see.truetransact.uicomponent.CComboBox();
        cboCustomerGroup = new com.see.truetransact.uicomponent.CComboBox();
        lblMemNum = new com.see.truetransact.uicomponent.CLabel();
        lblMembershipClass = new com.see.truetransact.uicomponent.CLabel();
        cboMembershipClass = new com.see.truetransact.uicomponent.CComboBox();
        lblCustStatus = new com.see.truetransact.uicomponent.CLabel();
        cboCustStatus = new com.see.truetransact.uicomponent.CComboBox();
        lblNationality = new com.see.truetransact.uicomponent.CLabel();
        txtNationality = new com.see.truetransact.uicomponent.CTextField();
        lblResidentialStatus = new com.see.truetransact.uicomponent.CLabel();
        cboResidentialStatus = new com.see.truetransact.uicomponent.CComboBox();
        lblLanguage = new com.see.truetransact.uicomponent.CLabel();
        txtLanguage = new com.see.truetransact.uicomponent.CTextField();
        lblIntroType = new com.see.truetransact.uicomponent.CLabel();
        cboIntroType = new com.see.truetransact.uicomponent.CComboBox();
        lblStaffId = new com.see.truetransact.uicomponent.CLabel();
        txtStaffId = new com.see.truetransact.uicomponent.CTextField();
        lblCreatedDt = new com.see.truetransact.uicomponent.CLabel();
        lblCreatedDt1 = new com.see.truetransact.uicomponent.CLabel();
        chkStaff = new com.see.truetransact.uicomponent.CCheckBox();
        txtMemNum = new com.see.truetransact.uicomponent.CTextField();
        btnStaffId = new com.see.truetransact.uicomponent.CButton();
        chkRegionalLang = new com.see.truetransact.uicomponent.CCheckBox();
        lblRegionalLang = new com.see.truetransact.uicomponent.CLabel();
        panContactsList = new com.see.truetransact.uicomponent.CPanel();
        panContacts = new com.see.truetransact.uicomponent.CPanel();
        srpContactList = new com.see.truetransact.uicomponent.CScrollPane();
        tblContactList = new com.see.truetransact.uicomponent.CTable();
        panContactControl = new com.see.truetransact.uicomponent.CPanel();
        btnContactToMain = new com.see.truetransact.uicomponent.CButton();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        panMISKYC = new com.see.truetransact.uicomponent.CPanel();
        panMIS = new com.see.truetransact.uicomponent.CPanel();
        lblCompany = new com.see.truetransact.uicomponent.CLabel();
        txtCompany = new com.see.truetransact.uicomponent.CTextField();
        lblPrimaryOccupation = new com.see.truetransact.uicomponent.CLabel();
        cboPrimaryOccupation = new com.see.truetransact.uicomponent.CComboBox();
        lblProfession = new com.see.truetransact.uicomponent.CLabel();
        cboProfession = new com.see.truetransact.uicomponent.CComboBox();
        lblAnnualIncomeLevel = new com.see.truetransact.uicomponent.CLabel();
        cboAnnualIncomeLevel = new com.see.truetransact.uicomponent.CComboBox();
        lblEducationalLevel = new com.see.truetransact.uicomponent.CLabel();
        cboEducationalLevel = new com.see.truetransact.uicomponent.CComboBox();
        lblPrefCommunication = new com.see.truetransact.uicomponent.CLabel();
        cboPrefCommunication = new com.see.truetransact.uicomponent.CComboBox();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        txtDesignation = new com.see.truetransact.uicomponent.CTextField();
        panITDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoITDec_Pan = new com.see.truetransact.uicomponent.CRadioButton();
        rdoITDec_F60 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoITDec_F61 = new com.see.truetransact.uicomponent.CRadioButton();
        txtPanNumber = new com.see.truetransact.uicomponent.CTextField();
        panITDetails1 = new com.see.truetransact.uicomponent.CPanel();
        cboReligion = new com.see.truetransact.uicomponent.CComboBox();
        lblReligion = new com.see.truetransact.uicomponent.CLabel();
        cboCaste = new com.see.truetransact.uicomponent.CComboBox();
        lblCaste = new com.see.truetransact.uicomponent.CLabel();
        lblSubCaste = new com.see.truetransact.uicomponent.CLabel();
        cboSubCaste = new com.see.truetransact.uicomponent.CComboBox();
        lblChkMinority = new com.see.truetransact.uicomponent.CLabel();
        chkMinority = new com.see.truetransact.uicomponent.CCheckBox();
        panITDetails2 = new com.see.truetransact.uicomponent.CPanel();
        lblWardNo = new com.see.truetransact.uicomponent.CLabel();
        txtWardNo = new com.see.truetransact.uicomponent.CTextField();
        lblDesam = new com.see.truetransact.uicomponent.CLabel();
        cbcomboDesam = new com.see.truetransact.uicomponent.CComboBox();
        lblAmsam = new com.see.truetransact.uicomponent.CLabel();
        cbcomboAmsam = new javax.swing.JComboBox();
        panKYC = new com.see.truetransact.uicomponent.CPanel();
        lblAddrVerified = new com.see.truetransact.uicomponent.CLabel();
        chkAddrVerified = new com.see.truetransact.uicomponent.CCheckBox();
        lblPhVerified = new com.see.truetransact.uicomponent.CLabel();
        chkPhVerified = new com.see.truetransact.uicomponent.CCheckBox();
        lblFinanceStmtVerified = new com.see.truetransact.uicomponent.CLabel();
        chkFinanceStmtVerified = new com.see.truetransact.uicomponent.CCheckBox();
        chkSentThanksLetter = new com.see.truetransact.uicomponent.CCheckBox();
        lblSentThanksLetter = new com.see.truetransact.uicomponent.CLabel();
        chkConfirmationfromIntroducer = new com.see.truetransact.uicomponent.CCheckBox();
        lblConfirmationfromIntroducer = new com.see.truetransact.uicomponent.CLabel();
        cboAddrProof = new com.see.truetransact.uicomponent.CComboBox();
        lblAddrProof = new com.see.truetransact.uicomponent.CLabel();
        lblIncomeParticulars = new com.see.truetransact.uicomponent.CLabel();
        chkIncParticulars = new com.see.truetransact.uicomponent.CCheckBox();
        chkLandDetails = new com.see.truetransact.uicomponent.CCheckBox();
        lblLandDetails = new com.see.truetransact.uicomponent.CLabel();
        panProofDetails = new com.see.truetransact.uicomponent.CPanel();
        lblIdenProof = new com.see.truetransact.uicomponent.CLabel();
        cboIdenProof = new com.see.truetransact.uicomponent.CComboBox();
        lblUniqueNo = new com.see.truetransact.uicomponent.CLabel();
        txtUniqueId = new com.see.truetransact.uicomponent.CTextField();
        panProofList = new com.see.truetransact.uicomponent.CPanel();
        panProof = new com.see.truetransact.uicomponent.CPanel();
        srpProofList = new com.see.truetransact.uicomponent.CScrollPane();
        tblProofList = new com.see.truetransact.uicomponent.CTable();
        panProofControl = new com.see.truetransact.uicomponent.CPanel();
        btnProofNew = new com.see.truetransact.uicomponent.CButton();
        btnProofAdd = new com.see.truetransact.uicomponent.CButton();
        btnProofDelete = new com.see.truetransact.uicomponent.CButton();
        panGuardianDetails = new com.see.truetransact.uicomponent.CPanel();
        panGuardData = new com.see.truetransact.uicomponent.CPanel();
        lblRelationNO = new com.see.truetransact.uicomponent.CLabel();
        cboRelationNO = new com.see.truetransact.uicomponent.CComboBox();
        lblGuardianNameNO = new com.see.truetransact.uicomponent.CLabel();
        txtGuardianNameNO = new com.see.truetransact.uicomponent.CTextField();
        lblGuardianPhoneNO = new com.see.truetransact.uicomponent.CLabel();
        phoneGPanelNO = new com.see.truetransact.uicomponent.CPanel();
        txtGuardianACodeNO = new com.see.truetransact.uicomponent.CTextField();
        txtGuardianPhoneNO = new com.see.truetransact.uicomponent.CTextField();
        tdtGuardianDOB = new com.see.truetransact.uicomponent.CDateField();
        lblGuardianDoB = new com.see.truetransact.uicomponent.CLabel();
        txtGuardianAge = new com.see.truetransact.uicomponent.CTextField();
        lblGuardianAge = new com.see.truetransact.uicomponent.CLabel();
        sptGuard = new com.see.truetransact.uicomponent.CSeparator();
        panGuardAddr = new com.see.truetransact.uicomponent.CPanel();
        lblGStreet = new com.see.truetransact.uicomponent.CLabel();
        txtGStreet = new com.see.truetransact.uicomponent.CTextField();
        lblGArea = new com.see.truetransact.uicomponent.CLabel();
        txtGArea = new com.see.truetransact.uicomponent.CTextField();
        lblGCountry = new com.see.truetransact.uicomponent.CLabel();
        cboGCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblGState = new com.see.truetransact.uicomponent.CLabel();
        cboGState = new com.see.truetransact.uicomponent.CComboBox();
        lblGCity = new com.see.truetransact.uicomponent.CLabel();
        cboGCity = new com.see.truetransact.uicomponent.CComboBox();
        lblGPinCode = new com.see.truetransact.uicomponent.CLabel();
        txtGPinCode = new com.see.truetransact.uicomponent.CTextField();
        panIncomeParticulars = new com.see.truetransact.uicomponent.CPanel();
        panAdditionalInfo1 = new com.see.truetransact.uicomponent.CPanel();
        lblIncName = new com.see.truetransact.uicomponent.CLabel();
        txtIncName = new com.see.truetransact.uicomponent.CTextField();
        lblIncRelation = new com.see.truetransact.uicomponent.CLabel();
        cboIncRelation = new com.see.truetransact.uicomponent.CComboBox();
        lblIncIncome = new com.see.truetransact.uicomponent.CLabel();
        panSearch2 = new com.see.truetransact.uicomponent.CPanel();
        txtIncIncome = new com.see.truetransact.uicomponent.CTextField();
        cboIncPack = new com.see.truetransact.uicomponent.CComboBox();
        panFreezeSave = new com.see.truetransact.uicomponent.CPanel();
        btnIncSave = new com.see.truetransact.uicomponent.CButton();
        btnIncNew = new com.see.truetransact.uicomponent.CButton();
        btnIncDelete = new com.see.truetransact.uicomponent.CButton();
        cboIncDetProfession = new com.see.truetransact.uicomponent.CComboBox();
        txtIncDetCompany = new com.see.truetransact.uicomponent.CTextField();
        lblIncDetProfession = new com.see.truetransact.uicomponent.CLabel();
        lblIncDetCompany = new com.see.truetransact.uicomponent.CLabel();
        panCustomerInfo1 = new com.see.truetransact.uicomponent.CPanel();
        srpIncParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        tblIncParticulars = new com.see.truetransact.uicomponent.CTable();
        panLandDetails = new com.see.truetransact.uicomponent.CPanel();
        panLandInfoDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLocation = new com.see.truetransact.uicomponent.CLabel();
        lblTaluk = new com.see.truetransact.uicomponent.CLabel();
        lblAreaInAcrs = new com.see.truetransact.uicomponent.CLabel();
        lblIrrigated = new com.see.truetransact.uicomponent.CLabel();
        lblHobli = new com.see.truetransact.uicomponent.CLabel();
        lblVillage = new com.see.truetransact.uicomponent.CLabel();
        lblPost = new com.see.truetransact.uicomponent.CLabel();
        lblSurNo = new com.see.truetransact.uicomponent.CLabel();
        lblSrcIrr = new com.see.truetransact.uicomponent.CLabel();
        lbLandlState = new com.see.truetransact.uicomponent.CLabel();
        lblDistrict = new com.see.truetransact.uicomponent.CLabel();
        lblLandPin = new com.see.truetransact.uicomponent.CLabel();
        txtLoc = new com.see.truetransact.uicomponent.CTextField();
        txtSrNo = new com.see.truetransact.uicomponent.CTextField();
        txtAreaInAcrs = new com.see.truetransact.uicomponent.CTextField();
        cboIrrigated = new com.see.truetransact.uicomponent.CComboBox();
        cboSrIrrigation = new com.see.truetransact.uicomponent.CComboBox();
        txtVillage = new com.see.truetransact.uicomponent.CTextField();
        txtPo = new com.see.truetransact.uicomponent.CTextField();
        txtHobli = new com.see.truetransact.uicomponent.CTextField();
        cboLandDetState = new com.see.truetransact.uicomponent.CComboBox();
        cboDistrict = new com.see.truetransact.uicomponent.CComboBox();
        cboTaluk = new com.see.truetransact.uicomponent.CComboBox();
        txtLandDetPincode = new com.see.truetransact.uicomponent.CTextField();
        panFreezeSave1 = new com.see.truetransact.uicomponent.CPanel();
        btnLandNew = new com.see.truetransact.uicomponent.CButton();
        btnLandSave = new com.see.truetransact.uicomponent.CButton();
        btnLandDelete = new com.see.truetransact.uicomponent.CButton();
        panLandInfoTable = new com.see.truetransact.uicomponent.CPanel();
        srpCustomerLandDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblCustomerLandDetails = new com.see.truetransact.uicomponent.CTable();
        panCustomerHistory = new com.see.truetransact.uicomponent.CPanel();
        srpCustomerHistory = new com.see.truetransact.uicomponent.CScrollPane();
        tblCustomerHistory = new com.see.truetransact.uicomponent.CTable();
        lblDealingWith = new com.see.truetransact.uicomponent.CLabel();
        chkClosAcdetail = new com.see.truetransact.uicomponent.CCheckBox();
        lblDealingPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblClosAcdetail = new javax.swing.JLabel();
        panRegLanguage = new com.see.truetransact.uicomponent.CPanel();
        lblRegName = new com.see.truetransact.uicomponent.CLabel();
        txtRegName = new com.see.truetransact.uicomponent.CTextField();
        txtRegMName = new com.see.truetransact.uicomponent.CTextField();
        lblRegHname = new com.see.truetransact.uicomponent.CLabel();
        txtRegHname = new com.see.truetransact.uicomponent.CTextField();
        lblRegPlace = new com.see.truetransact.uicomponent.CLabel();
        txtRegPlace = new com.see.truetransact.uicomponent.CTextField();
        lblRegVillage = new com.see.truetransact.uicomponent.CLabel();
        txtRegState = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaHname = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaPlace = new com.see.truetransact.uicomponent.CTextField();
        txtRegMavillage = new com.see.truetransact.uicomponent.CTextField();
        lblRegTaluk = new com.see.truetransact.uicomponent.CLabel();
        lblRegCity = new com.see.truetransact.uicomponent.CLabel();
        lblRegState = new com.see.truetransact.uicomponent.CLabel();
        txtRegMaCity = new com.see.truetransact.uicomponent.CTextField();
        txtRegCity = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaTaluk = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaState = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaCountry = new com.see.truetransact.uicomponent.CTextField();
        txtRegTaluk = new com.see.truetransact.uicomponent.CTextField();
        lblRegAmsam = new com.see.truetransact.uicomponent.CLabel();
        lblRegDesam = new com.see.truetransact.uicomponent.CLabel();
        lblRegCountry = new com.see.truetransact.uicomponent.CLabel();
        txtRegMaAmsam = new com.see.truetransact.uicomponent.CTextField();
        txtRegAmsam = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaDesam = new com.see.truetransact.uicomponent.CTextField();
        txtRegDesam = new com.see.truetransact.uicomponent.CTextField();
        txtRegCountry = new com.see.truetransact.uicomponent.CTextField();
        txtRegGardName = new com.see.truetransact.uicomponent.CTextField();
        lblRegGardName = new com.see.truetransact.uicomponent.CLabel();
        txtRegMaGardName = new com.see.truetransact.uicomponent.CTextField();
        txtRegvillage = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnDeletedDetails = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
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
        mitSaveAs = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Individual Customer");
        setMinimumSize(new java.awt.Dimension(900, 700));
        setPreferredSize(new java.awt.Dimension(900, 660));

        panCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panCustomer.setMinimumSize(new java.awt.Dimension(860, 580));
        panCustomer.setPreferredSize(new java.awt.Dimension(860, 580));
        panCustomer.setLayout(new java.awt.GridBagLayout());

        panContactAndIdentityInfo.setMinimumSize(new java.awt.Dimension(788, 200));
        panContactAndIdentityInfo.setName("panContactAndIdentityInfo"); // NOI18N
        panContactAndIdentityInfo.setPreferredSize(new java.awt.Dimension(788, 200));
        panContactAndIdentityInfo.setLayout(new java.awt.GridBagLayout());

        tabContactAndIdentityInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tabContactAndIdentityInfo.setMinimumSize(new java.awt.Dimension(925, 220));
        tabContactAndIdentityInfo.setName("SuspendCustomer"); // NOI18N
        tabContactAndIdentityInfo.setPreferredSize(new java.awt.Dimension(925, 220));

        panContactInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panContactInfo.setName("panContactInfo"); // NOI18N
        panContactInfo.setLayout(new java.awt.GridBagLayout());

        panAddress.setMinimumSize(new java.awt.Dimension(360, 165));
        panAddress.setName("panAddress"); // NOI18N
        panAddress.setPreferredSize(new java.awt.Dimension(360, 165));
        panAddress.setLayout(new java.awt.GridBagLayout());

        panAddressDetails.setMinimumSize(new java.awt.Dimension(349, 160));
        panAddressDetails.setName("panAddressDetails"); // NOI18N
        panAddressDetails.setPreferredSize(new java.awt.Dimension(349, 160));
        panAddressDetails.setLayout(new java.awt.GridBagLayout());

        panAddressType.setLayout(new java.awt.GridBagLayout());

        lblAddressType.setText("Address Type");
        lblAddressType.setName("lblAddressType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAddressType.add(lblAddressType, gridBagConstraints);

        txtStreet.setMaxLength(256);
        txtStreet.setMinimumSize(new java.awt.Dimension(200, 21));
        txtStreet.setName("txtStreet"); // NOI18N
        txtStreet.setPreferredSize(new java.awt.Dimension(200, 21));
        txtStreet.setValidation(new DefaultValidation());
        txtStreet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStreetKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddressType.add(txtStreet, gridBagConstraints);

        lblArea.setText("Area/Kara");
        lblArea.setName("lblArea"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAddressType.add(lblArea, gridBagConstraints);

        lblStreet.setText("Street");
        lblStreet.setName("lblStreet"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAddressType.add(lblStreet, gridBagConstraints);

        txtArea.setMaxLength(128);
        txtArea.setMinimumSize(new java.awt.Dimension(200, 21));
        txtArea.setName("txtArea"); // NOI18N
        txtArea.setPreferredSize(new java.awt.Dimension(200, 21));
        txtArea.setValidation(new DefaultValidation());
        txtArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddressType.add(txtArea, gridBagConstraints);

        cboAddressType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAddressType.setName("cboAddressType"); // NOI18N
        cboAddressType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAddressTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddressType.add(cboAddressType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAddressDetails.add(panAddressType, gridBagConstraints);

        panCountryDetails.setLayout(new java.awt.GridBagLayout());

        panCountry.setMinimumSize(new java.awt.Dimension(165, 40));
        panCountry.setPreferredSize(new java.awt.Dimension(165, 40));
        panCountry.setLayout(new java.awt.GridBagLayout());

        lblCity.setText("City");
        lblCity.setToolTipText("City");
        lblCity.setMaximumSize(new java.awt.Dimension(36, 18));
        lblCity.setMinimumSize(new java.awt.Dimension(36, 18));
        lblCity.setName("lblCity"); // NOI18N
        lblCity.setPreferredSize(new java.awt.Dimension(36, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 1, 8);
        panCountry.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCity.setName("cboCity"); // NOI18N
        cboCity.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panCountry.add(cboCity, gridBagConstraints);

        lblState.setText("State");
        lblState.setToolTipText("State");
        lblState.setMaximumSize(new java.awt.Dimension(58, 18));
        lblState.setMinimumSize(new java.awt.Dimension(58, 18));
        lblState.setName("lblState"); // NOI18N
        lblState.setPreferredSize(new java.awt.Dimension(58, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 5);
        panCountry.add(lblState, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        cboState.setName("cboState"); // NOI18N
        cboState.setNextFocusableComponent(cboPostOffice);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 4, 0);
        panCountry.add(cboState, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panCountryDetails.add(panCountry, gridBagConstraints);

        panCity.setLayout(new java.awt.GridBagLayout());

        lblPincode.setText("Pincode");
        lblPincode.setName("lblPincode"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCity.add(lblPincode, gridBagConstraints);

        txtPincode.setMaxLength(16);
        txtPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPincode.setName("txtPincode"); // NOI18N
        txtPincode.setNextFocusableComponent(cboPhoneType);
        txtPincode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panCity.add(txtPincode, gridBagConstraints);

        lblCountry.setText("Country");
        lblCountry.setName("lblCountry"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 1, 4);
        panCity.add(lblCountry, gridBagConstraints);

        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCountry.setName("cboCountry"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panCity.add(cboCountry, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panCountryDetails.add(panCity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 41, 0, 0);
        panAddressDetails.add(panCountryDetails, gridBagConstraints);

        panAddrRemarks.setMinimumSize(new java.awt.Dimension(0, 0));
        panAddrRemarks.setPreferredSize(new java.awt.Dimension(0, 0));
        panAddrRemarks.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 23, 0, 0);
        panAddressDetails.add(panAddrRemarks, gridBagConstraints);

        panKara.setMinimumSize(new java.awt.Dimension(310, 26));
        panKara.setPreferredSize(new java.awt.Dimension(310, 26));
        panKara.setLayout(new java.awt.GridBagLayout());

        lblCustVillage.setText("Village  ");
        lblCustVillage.setToolTipText("");
        lblCustVillage.setMaximumSize(new java.awt.Dimension(54, 18));
        lblCustVillage.setMinimumSize(new java.awt.Dimension(54, 18));
        lblCustVillage.setName(""); // NOI18N
        lblCustVillage.setPreferredSize(new java.awt.Dimension(54, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 4);
        panKara.add(lblCustVillage, gridBagConstraints);

        cboCustVillage.setAutoscrolls(true);
        cboCustVillage.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panKara.add(cboCustVillage, gridBagConstraints);

        lblCustTaluk.setText("Taluk");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKara.add(lblCustTaluk, gridBagConstraints);

        cboCustTaluk.setAutoscrolls(true);
        cboCustTaluk.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panKara.add(cboCustTaluk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 33, 0, 0);
        panAddressDetails.add(panKara, gridBagConstraints);

        panPost.setPreferredSize(new java.awt.Dimension(295, 26));
        panPost.setLayout(new java.awt.GridBagLayout());

        cboPostOffice.setMinimumSize(new java.awt.Dimension(90, 21));
        cboPostOffice.setNextFocusableComponent(txtPincode);
        cboPostOffice.setPopupWidth(200);
        cboPostOffice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboPostOfficeMouseClicked(evt);
            }
        });
        cboPostOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPostOfficeActionPerformed(evt);
            }
        });
        cboPostOffice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboPostOfficeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPostOfficeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPost.add(cboPostOffice, gridBagConstraints);

        lblPostOffice.setText("Post.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panPost.add(lblPostOffice, gridBagConstraints);

        lblWardName.setText("Ward");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 16);
        panPost.add(lblWardName, gridBagConstraints);

        cboWardName.setMinimumSize(new java.awt.Dimension(70, 22));
        cboWardName.setPopupWidth(200);
        panPost.add(cboWardName, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 37, 3, 8);
        panAddressDetails.add(panPost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAddress.add(panAddressDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panContactInfo.add(panAddress, gridBagConstraints);

        panContactNo.setName("panContactNo"); // NOI18N
        panContactNo.setLayout(new java.awt.GridBagLayout());

        panTeleCommunication.setName("panTeleCommunication"); // NOI18N
        panTeleCommunication.setLayout(new java.awt.GridBagLayout());

        panTelecomDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTelecomDetails.setName("panTelecomDetails"); // NOI18N
        panTelecomDetails.setLayout(new java.awt.GridBagLayout());

        panPhoneType.setName("panPhoneType"); // NOI18N
        panPhoneType.setLayout(new java.awt.GridBagLayout());

        panPhoneSave.setLayout(new java.awt.GridBagLayout());

        btnContactNoAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnContactNoAdd.setEnabled(false);
        btnContactNoAdd.setMaximumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setMinimumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setName("btnContactNoAdd"); // NOI18N
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
        btnPhoneNew.setEnabled(false);
        btnPhoneNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPhoneNew.setPreferredSize(new java.awt.Dimension(29, 27));
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
        btnPhoneDelete.setEnabled(false);
        btnPhoneDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPhoneDelete.setPreferredSize(new java.awt.Dimension(29, 27));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPhoneType.add(panPhoneSave, gridBagConstraints);

        panPhoneAreaNumber.setLayout(new java.awt.GridBagLayout());

        lblPhoneType.setText("Phone Type");
        lblPhoneType.setName("lblPhoneType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblPhoneType, gridBagConstraints);

        lblPhoneNumber.setText("Number");
        lblPhoneNumber.setName("lblPhoneNumber"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblPhoneNumber, gridBagConstraints);

        txtPhoneNumber.setMaxLength(16);
        txtPhoneNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPhoneNumber.setName("txtPhoneNumber"); // NOI18N
        txtPhoneNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneNumberKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(txtPhoneNumber, gridBagConstraints);

        lblAreaCode.setText("Area Code");
        lblAreaCode.setName("lblAreaCode"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblAreaCode, gridBagConstraints);

        txtAreaCode.setMaxLength(16);
        txtAreaCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAreaCode.setName("txtAreaCode"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(txtAreaCode, gridBagConstraints);

        cboPhoneType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPhoneType.setName("cboPhoneType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(cboPhoneType, gridBagConstraints);

        panPhoneType.add(panPhoneAreaNumber, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 1);
        panTelecomDetails.add(panPhoneType, gridBagConstraints);

        panPhoneList.setName("panPhoneList"); // NOI18N
        panPhoneList.setLayout(new java.awt.GridBagLayout());

        srpPhoneList.setMinimumSize(new java.awt.Dimension(200, 75));
        srpPhoneList.setName("srpPhoneList"); // NOI18N
        srpPhoneList.setPreferredSize(new java.awt.Dimension(200, 75));

        tblPhoneList.setName("tblPhoneList"); // NOI18N
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

        panSaveBtn.setLayout(new java.awt.GridBagLayout());

        btnContactAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnContactAdd.setText("Save Contact");
        btnContactAdd.setEnabled(false);
        btnContactAdd.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnContactAdd.setName("btnContactAdd"); // NOI18N
        btnContactAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panSaveBtn.add(btnContactAdd, gridBagConstraints);

        btnContactNew.setText("New");
        btnContactNew.setEnabled(false);
        btnContactNew.setName("btnContactNew"); // NOI18N
        btnContactNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSaveBtn.add(btnContactNew, gridBagConstraints);

        btnContactDelete.setText("Delete");
        btnContactDelete.setEnabled(false);
        btnContactDelete.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnContactDelete.setName("btnContactDelete"); // NOI18N
        btnContactDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSaveBtn.add(btnContactDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panContactNo.add(panSaveBtn, gridBagConstraints);

        lblAddrRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactNo.add(lblAddrRemarks, gridBagConstraints);

        txtAddrRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAddrRemarks.setPreferredSize(new java.awt.Dimension(200, 21));
        txtAddrRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactNo.add(txtAddrRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panContactInfo.add(panContactNo, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Contact Info", panContactInfo);

        panPhotoSign.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
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
        btnPhotoLoad.setEnabled(false);
        btnPhotoLoad.setPreferredSize(new java.awt.Dimension(73, 25));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
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
        btnSignLoad.setEnabled(false);
        btnSignLoad.setPreferredSize(new java.awt.Dimension(73, 25));
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

        panSign.add(panSignButtons, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPhotoSign.add(panSign, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Photo & Signature", panPhotoSign);

        panAdditionalInformation.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAdditionalInformation.setLayout(new java.awt.GridBagLayout());

        lblSsn.setText("Social Security No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblSsn, gridBagConstraints);

        txtSsn.setMinimumSize(new java.awt.Dimension(200, 21));
        txtSsn.setPreferredSize(new java.awt.Dimension(200, 21));
        txtSsn.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInformation.add(txtSsn, gridBagConstraints);

        lblCustUserid.setText("User Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblCustUserid, gridBagConstraints);

        txtCustUserid.setValidation(new DefaultValidation());
        txtCustUserid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustUseridActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(txtCustUserid, gridBagConstraints);

        lblCustPwd.setText("Login Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblCustPwd, gridBagConstraints);

        txtCustPwd.setVerifyInputWhenFocusTarget(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(txtCustPwd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(txtTransPwd, gridBagConstraints);

        txtKartha.setMinimumSize(new java.awt.Dimension(200, 21));
        txtKartha.setPreferredSize(new java.awt.Dimension(200, 21));
        txtKartha.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panAdditionalInformation.add(txtKartha, gridBagConstraints);

        lblFarClass.setText("Classification Of Farmer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblFarClass, gridBagConstraints);

        cboFarClass.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInformation.add(cboFarClass, gridBagConstraints);

        lblKartha.setText("Kartha Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblKartha, gridBagConstraints);

        txtBankruptsy.setMinimumSize(new java.awt.Dimension(200, 21));
        txtBankruptsy.setPreferredSize(new java.awt.Dimension(200, 21));
        txtBankruptsy.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(txtBankruptsy, gridBagConstraints);

        lblBankruptsy.setText("Bankruptcy Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblBankruptsy, gridBagConstraints);

        txtRemarks.setMaxLength(256);
        txtRemarks.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRemarks.setName("txtRemarks"); // NOI18N
        txtRemarks.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInformation.add(txtRemarks, gridBagConstraints);

        lblRemarks.setText("Remarks");
        lblRemarks.setName("lblRemarks"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblRemarks, gridBagConstraints);

        lblTransPwd.setText("Transaction Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblTransPwd, gridBagConstraints);

        lblVehicle.setText("Vehicle");
        lblVehicle.setName("lblVehicle"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(lblVehicle, gridBagConstraints);

        cboVehicle.setMinimumSize(new java.awt.Dimension(100, 21));
        cboVehicle.setName("cboVehicle"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInformation.add(cboVehicle, gridBagConstraints);

        btnGenerateAppPwd.setText("Generate App Pwd");
        btnGenerateAppPwd.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnGenerateAppPwd.setName("btnContactToMain"); // NOI18N
        btnGenerateAppPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateAppPwdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panAdditionalInformation.add(btnGenerateAppPwd, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Additional Info", panAdditionalInformation);

        panCompanyInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panCompanyInfo.setLayout(new java.awt.GridBagLayout());

        lblCrAvldSince.setText("Credit Facilities Availed Since");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCompanyInfo.add(lblCrAvldSince, gridBagConstraints);

        lblRiskRate.setText("Risk Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCompanyInfo.add(lblRiskRate, gridBagConstraints);

        tdtCrAvldSince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCrAvldSinceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCompanyInfo.add(tdtCrAvldSince, gridBagConstraints);

        txtRiskRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRiskRate.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCompanyInfo.add(txtRiskRate, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Credit Info", panCompanyInfo);

        panPassPortDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPassPortDetails.setLayout(new java.awt.GridBagLayout());

        panPassportFirstName.setLayout(new java.awt.GridBagLayout());

        txtPassportFirstName.setMaxLength(128);
        txtPassportFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtPassportFirstName.setName("txtFirstName"); // NOI18N
        txtPassportFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtPassportFirstName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassportFirstName.add(txtPassportFirstName, gridBagConstraints);

        cboPassportTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboPassportTitle.setName("cboPassportTitle"); // NOI18N
        cboPassportTitle.setPopupWidth(80);
        cboPassportTitle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassportFirstName.add(cboPassportTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassPortDetails.add(panPassportFirstName, gridBagConstraints);

        txtPassportMiddleName.setMaxLength(128);
        txtPassportMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPassportMiddleName.setName("txtMiddleName"); // NOI18N
        txtPassportMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtPassportMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(txtPassportMiddleName, gridBagConstraints);

        txtPassportLastName.setMaxLength(128);
        txtPassportLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPassportLastName.setName("txtLastName"); // NOI18N
        txtPassportLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtPassportLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(txtPassportLastName, gridBagConstraints);

        lblFirstNamePass.setText("First Name");
        lblFirstNamePass.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblFirstNamePass, gridBagConstraints);

        lblMiddleNamePass.setText("Middle Name");
        lblMiddleNamePass.setName("lblMiddleName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblMiddleNamePass, gridBagConstraints);

        lblLastNamePass.setText("Last Name");
        lblLastNamePass.setName("lblLastName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblLastNamePass, gridBagConstraints);

        txtPassportNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPassportNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(txtPassportNo, gridBagConstraints);

        lblPassPortNo.setText("PassPort Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassPortNo, gridBagConstraints);

        tdtPassportValidUpto.setName("tdtDateOfBirth"); // NOI18N
        tdtPassportValidUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPassportValidUptoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(tdtPassportValidUpto, gridBagConstraints);

        lblValidUpto.setText("Valid upto");
        lblValidUpto.setName("lblDateOfBirth"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblValidUpto, gridBagConstraints);

        tdtPassportIssueDt.setName("tdtDateOfBirth"); // NOI18N
        tdtPassportIssueDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPassportIssueDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(tdtPassportIssueDt, gridBagConstraints);

        lblIssuedDt.setText("Issued Date");
        lblIssuedDt.setName("lblDateOfBirth"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblIssuedDt, gridBagConstraints);

        txtPassportIssueAuth.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPassportIssueAuth.setPreferredSize(new java.awt.Dimension(200, 21));
        txtPassportIssueAuth.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPassPortDetails.add(txtPassportIssueAuth, gridBagConstraints);

        cboPassportIssuePlace.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPassportIssuePlace.setName("cboCustomerType"); // NOI18N
        cboPassportIssuePlace.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(cboPassportIssuePlace, gridBagConstraints);

        lblPassPlaceIssued.setText("Place Of Issue");
        lblPassPlaceIssued.setName("lblResidentialStatus"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassPlaceIssued, gridBagConstraints);

        lblPassIssueAuth.setText("Issueing Authority");
        lblPassIssueAuth.setName("lblResidentialStatus"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassIssueAuth, gridBagConstraints);

        btnClearPassport.setText("Clear Passport Details");
        btnClearPassport.setEnabled(false);
        btnClearPassport.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnClearPassport.setName("btnContactToMain"); // NOI18N
        btnClearPassport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPassportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, -30, 0, 0);
        panPassPortDetails.add(btnClearPassport, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("PassPort Details", panPassPortDetails);

        panSuspendCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSuspendCustomer.setLayout(new java.awt.GridBagLayout());

        lblSuspendCust.setText("Suspend CustomerID");
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

        lblRevokeCust.setText("Revoke Suspension");
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

        panAddressProofPhoto.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAddressProofPhoto.setLayout(new java.awt.GridBagLayout());

        panProofPhoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Photograph"));
        panProofPhoto.setLayout(new java.awt.GridBagLayout());

        srpProofPhotoLoad.setPreferredSize(new java.awt.Dimension(120, 150));

        lblProofPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblProofPhotoMouseClicked(evt);
            }
        });
        srpProofPhotoLoad.setViewportView(lblProofPhoto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 392;
        gridBagConstraints.ipady = 124;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 10, 16, 0);
        panProofPhoto.add(srpProofPhotoLoad, gridBagConstraints);

        panProofUploanButtons.setLayout(new java.awt.GridBagLayout());

        btnProofPhotoLoad.setText("Load");
        btnProofPhotoLoad.setEnabled(false);
        btnProofPhotoLoad.setPreferredSize(new java.awt.Dimension(73, 25));
        btnProofPhotoLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProofPhotoLoadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProofUploanButtons.add(btnProofPhotoLoad, gridBagConstraints);

        btnProofPhotoRemove.setText("Remove");
        btnProofPhotoRemove.setEnabled(false);
        btnProofPhotoRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProofPhotoRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProofUploanButtons.add(btnProofPhotoRemove, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(54, 18, 0, 16);
        panProofPhoto.add(panProofUploanButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 368);
        panAddressProofPhoto.add(panProofPhoto, gridBagConstraints);

        tabContactAndIdentityInfo.addTab("Upload Address Proof", panAddressProofPhoto);

        panContactAndIdentityInfo.add(tabContactAndIdentityInfo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panCustomer.add(panContactAndIdentityInfo, gridBagConstraints);

        tabIndCust.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabIndCust.setMinimumSize(new java.awt.Dimension(800, 335));
        tabIndCust.setPreferredSize(new java.awt.Dimension(800, 335));
        tabIndCust.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabIndCustMouseClicked(evt);
            }
        });

        panPersonalInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Personal Info"));
        panPersonalInfo.setMinimumSize(new java.awt.Dimension(804, 300));
        panPersonalInfo.setName("panPersonalInfo"); // NOI18N
        panPersonalInfo.setPreferredSize(new java.awt.Dimension(804, 300));
        panPersonalInfo.setLayout(new java.awt.GridBagLayout());

        panAdditionalInfo.setMinimumSize(new java.awt.Dimension(360, 325));
        panAdditionalInfo.setName("panAdditionalInfo"); // NOI18N
        panAdditionalInfo.setPreferredSize(new java.awt.Dimension(360, 325));
        panAdditionalInfo.setLayout(new java.awt.GridBagLayout());

        panGender.setMinimumSize(new java.awt.Dimension(400, 21));
        panGender.setName("panGender"); // NOI18N
        panGender.setPreferredSize(new java.awt.Dimension(400, 21));
        panGender.setLayout(new java.awt.GridBagLayout());

        lblGender.setText("Gender");
        lblGender.setName("lblGender"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panGender.add(lblGender, gridBagConstraints);

        rdoGender.add(rdoGender_Male);
        rdoGender_Male.setText("Male");
        rdoGender_Male.setName("rdoGender_Male"); // NOI18N
        rdoGender_Male.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rdoGender_MaleFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoGender_MaleFocusLost(evt);
            }
        });
        panGender.add(rdoGender_Male, new java.awt.GridBagConstraints());

        rdoGender.add(rdoGender_Female);
        rdoGender_Female.setText("Female");
        rdoGender_Female.setName("rdoGender_Female"); // NOI18N
        rdoGender_Female.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGender_FemaleActionPerformed(evt);
            }
        });
        rdoGender_Female.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rdoGender_FemaleFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGender.add(rdoGender_Female, gridBagConstraints);

        lblDivision.setText("Division");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panGender.add(lblDivision, gridBagConstraints);

        cboDivision.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDivision.setPopupWidth(550);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panGender.add(cboDivision, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panGender, gridBagConstraints);

        panDOB.setMinimumSize(new java.awt.Dimension(400, 25));
        panDOB.setPreferredSize(new java.awt.Dimension(400, 25));
        panDOB.setLayout(new java.awt.GridBagLayout());

        tdtDateOfBirth.setName("tdtDateOfBirth"); // NOI18N
        tdtDateOfBirth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tdtDateOfBirthMouseClicked(evt);
            }
        });
        tdtDateOfBirth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateOfBirthFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDOB.add(tdtDateOfBirth, gridBagConstraints);

        txtAge.setMaxLength(128);
        txtAge.setMinimumSize(new java.awt.Dimension(40, 21));
        txtAge.setName("txtFirstName"); // NOI18N
        txtAge.setPreferredSize(new java.awt.Dimension(40, 21));
        txtAge.setValidation(new DefaultValidation());
        txtAge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAgeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtAgeMouseEntered(evt);
            }
        });
        txtAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgeActionPerformed(evt);
            }
        });
        txtAge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAgeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAgeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDOB.add(txtAge, gridBagConstraints);

        lblAge.setText("Age");
        lblAge.setName("lblLastName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDOB.add(lblAge, gridBagConstraints);

        lblDateOfBirth.setText("Date of Birth");
        lblDateOfBirth.setName("lblDateOfBirth"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDOB.add(lblDateOfBirth, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panDOB, gridBagConstraints);

        panCareOf.setMinimumSize(new java.awt.Dimension(400, 23));
        panCareOf.setPreferredSize(new java.awt.Dimension(400, 23));
        panCareOf.setLayout(new java.awt.GridBagLayout());

        cboCareOf.setMinimumSize(new java.awt.Dimension(70, 21));
        cboCareOf.setName("cboCareOf"); // NOI18N
        cboCareOf.setPopupWidth(150);
        cboCareOf.setPreferredSize(new java.awt.Dimension(70, 21));
        cboCareOf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboCareOfFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 9, 1, 4);
        panCareOf.add(cboCareOf, gridBagConstraints);

        lblCareOf.setText("Care Of");
        lblCareOf.setName("lblCareOf"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCareOf.add(lblCareOf, gridBagConstraints);

        lblName.setText("Name");
        lblName.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCareOf.add(lblName, gridBagConstraints);

        txtName.setMaxLength(128);
        txtName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtName.setName("txtFirstName"); // NOI18N
        txtName.setValidation(new DefaultValidation());
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCareOf.add(txtName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panCareOf, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(400, 27));
        panSearch.setPreferredSize(new java.awt.Dimension(234, 27));
        panSearch.setLayout(new java.awt.GridBagLayout());

        txtEmailID.setMaxLength(64);
        txtEmailID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmailID.setName("txtEmailID"); // NOI18N
        txtEmailID.setValidation(new EmailValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSearch.add(txtEmailID, gridBagConstraints);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif"))); // NOI18N
        btnSearch.setText("Search");
        btnSearch.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnSearch, gridBagConstraints);

        lblEmailID.setText("Email ID");
        lblEmailID.setName("lblEmailID"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panSearch.add(lblEmailID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panSearch, gridBagConstraints);

        panFirstName.setMinimumSize(new java.awt.Dimension(375, 23));
        panFirstName.setPreferredSize(new java.awt.Dimension(375, 23));
        panFirstName.setLayout(new java.awt.GridBagLayout());

        txtFirstName.setMaxLength(128);
        txtFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtFirstName.setName("txtFirstName"); // NOI18N
        txtFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtFirstName.setValidation(new DefaultValidation());
        txtFirstName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFirstNameFocusLost(evt);
            }
        });
        txtFirstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFirstNameKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName.add(txtFirstName, gridBagConstraints);

        cboTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboTitle.setName("cboTitle"); // NOI18N
        cboTitle.setPopupWidth(80);
        cboTitle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName.add(cboTitle, gridBagConstraints);

        lblFirstName.setText("First Name");
        lblFirstName.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panFirstName.add(lblFirstName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 4);
        panAdditionalInfo.add(panFirstName, gridBagConstraints);

        panSearch1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAdditionalInfo.add(panSearch1, gridBagConstraints);

        panSearch3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAdditionalInfo.add(panSearch3, gridBagConstraints);

        panCareOf1.setMinimumSize(new java.awt.Dimension(400, 23));
        panCareOf1.setPreferredSize(new java.awt.Dimension(400, 23));
        panCareOf1.setLayout(new java.awt.GridBagLayout());

        cboAgentCustId.setMinimumSize(new java.awt.Dimension(70, 21));
        cboAgentCustId.setName("cboCareOf"); // NOI18N
        cboAgentCustId.setPopupWidth(150);
        cboAgentCustId.setPreferredSize(new java.awt.Dimension(70, 21));
        cboAgentCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentCustIdActionPerformed(evt);
            }
        });
        cboAgentCustId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboAgentCustIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf1.add(cboAgentCustId, gridBagConstraints);

        lblAgentCustId.setText("Agent Cust Id");
        lblAgentCustId.setName("lblCareOf"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf1.add(lblAgentCustId, gridBagConstraints);

        lblAgentCustIdValue.setText("Agent Cust Id");
        lblAgentCustIdValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblAgentCustIdValue.setMinimumSize(new java.awt.Dimension(100, 18));
        lblAgentCustIdValue.setName("lblCareOf"); // NOI18N
        lblAgentCustIdValue.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf1.add(lblAgentCustIdValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panCareOf1, gridBagConstraints);

        panCareOf2.setMinimumSize(new java.awt.Dimension(400, 22));
        panCareOf2.setPreferredSize(new java.awt.Dimension(450, 22));
        panCareOf2.setLayout(new java.awt.GridBagLayout());

        panMaritalStatus.setMinimumSize(new java.awt.Dimension(200, 20));
        panMaritalStatus.setName("panMaritalStatus"); // NOI18N
        panMaritalStatus.setPreferredSize(new java.awt.Dimension(200, 20));
        panMaritalStatus.setLayout(new java.awt.GridBagLayout());

        rdoMaritalStatus.add(rdoMaritalStatus_Single);
        rdoMaritalStatus_Single.setText("Single");
        rdoMaritalStatus_Single.setName("rdoMaritalStatus_Single"); // NOI18N
        rdoMaritalStatus_Single.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMaritalStatus_SingleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMaritalStatus.add(rdoMaritalStatus_Single, gridBagConstraints);

        rdoMaritalStatus.add(rdoMaritalStatus_Married);
        rdoMaritalStatus_Married.setText("Married");
        rdoMaritalStatus_Married.setName("rdoMaritalStatus_Married"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaritalStatus.add(rdoMaritalStatus_Married, gridBagConstraints);

        rdoMaritalStatus.add(rdoMaritalStatus_Widow);
        rdoMaritalStatus_Widow.setText("Widow");
        rdoMaritalStatus_Widow.setName("rdoMaritalStatus_Widow"); // NOI18N
        panMaritalStatus.add(rdoMaritalStatus_Widow, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 18, 1, 33);
        panCareOf2.add(panMaritalStatus, gridBagConstraints);

        lblMaritalStatus.setText("Marital Status");
        lblMaritalStatus.setName("lblMaritalStatus"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 0, 0);
        panCareOf2.add(lblMaritalStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panCareOf2, gridBagConstraints);

        panCareOf3.setMinimumSize(new java.awt.Dimension(375, 22));
        panCareOf3.setPreferredSize(new java.awt.Dimension(375, 22));
        panCareOf3.setLayout(new java.awt.GridBagLayout());

        lblRetireDt.setText("Retirement Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf3.add(lblRetireDt, gridBagConstraints);

        tdtretireDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtretireDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf3.add(tdtretireDt, gridBagConstraints);

        lblRetireAge.setText("Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf3.add(lblRetireAge, gridBagConstraints);

        txtRetireAge.setAllowAll(true);
        txtRetireAge.setMinimumSize(new java.awt.Dimension(40, 21));
        txtRetireAge.setPreferredSize(new java.awt.Dimension(40, 21));
        txtRetireAge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRetireAgeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf3.add(txtRetireAge, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panCareOf3, gridBagConstraints);

        panCareOf4.setMinimumSize(new java.awt.Dimension(375, 22));
        panCareOf4.setPreferredSize(new java.awt.Dimension(375, 22));
        panCareOf4.setLayout(new java.awt.GridBagLayout());

        chkMinor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMinorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf4.add(chkMinor, gridBagConstraints);

        lblChkMinor.setText("Minor");
        lblChkMinor.setName("lblLastName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf4.add(lblChkMinor, gridBagConstraints);

        lblJoiningDate.setText("Joining Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf4.add(lblJoiningDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCareOf4.add(tdtJoiningDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panAdditionalInfo.add(panCareOf4, gridBagConstraints);

        panFirstName1.setMinimumSize(new java.awt.Dimension(375, 42));
        panFirstName1.setPreferredSize(new java.awt.Dimension(375, 42));
        panFirstName1.setLayout(new java.awt.GridBagLayout());

        lblMiddleName.setText("Middle Name");
        lblMiddleName.setName("lblMiddleName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panFirstName1.add(lblMiddleName, gridBagConstraints);

        lblLastName.setText("Last Name");
        lblLastName.setName("lblLastName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panFirstName1.add(lblLastName, gridBagConstraints);

        txtMiddleName.setMaxLength(128);
        txtMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtMiddleName.setName("txtMiddleName"); // NOI18N
        txtMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtMiddleName.setValidation(new DefaultValidation());
        txtMiddleName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMiddleNameKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 4);
        panFirstName1.add(txtMiddleName, gridBagConstraints);

        txtLastName.setMaxLength(128);
        txtLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtLastName.setName("txtLastName"); // NOI18N
        txtLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtLastName.setValidation(new DefaultValidation());
        txtLastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLastNameKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 4);
        panFirstName1.add(txtLastName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 4);
        panAdditionalInfo.add(panFirstName1, gridBagConstraints);

        panFirstName2.setMinimumSize(new java.awt.Dimension(375, 21));
        panFirstName2.setPreferredSize(new java.awt.Dimension(375, 21));
        panFirstName2.setLayout(new java.awt.GridBagLayout());

        lblBranchId.setText("Branch Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panFirstName2.add(lblBranchId, gridBagConstraints);

        txtBranchId.setEditable(false);
        txtBranchId.setText("  ");
        txtBranchId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 4);
        panFirstName2.add(txtBranchId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAdditionalInfo.add(panFirstName2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPersonalInfo.add(panAdditionalInfo, gridBagConstraints);

        panCustomerInfo.setMinimumSize(new java.awt.Dimension(300, 325));
        panCustomerInfo.setName("panCustomerInfo"); // NOI18N
        panCustomerInfo.setPreferredSize(new java.awt.Dimension(300, 325));
        panCustomerInfo.setLayout(new java.awt.GridBagLayout());

        lblCustomerType.setText("Customer Type");
        lblCustomerType.setName("lblCustomerType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblCustomerType, gridBagConstraints);

        cboCustomerType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCustomerType.setName("cboCustomerType"); // NOI18N
        cboCustomerType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCustomerTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(cboCustomerType, gridBagConstraints);

        lblCustomerGroup.setText("Customer Group");
        lblCustomerGroup.setName("lblCustomerGroup"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblCustomerGroup, gridBagConstraints);

        lblRelationManager.setText("Relation Manager");
        lblRelationManager.setName("lblRelationManager"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblRelationManager, gridBagConstraints);

        cboRelationManager.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRelationManager.setName("cboRelationManager"); // NOI18N
        cboRelationManager.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(cboRelationManager, gridBagConstraints);

        cboCustomerGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCustomerGroup.setName("cboCustomerGroup"); // NOI18N
        cboCustomerGroup.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(cboCustomerGroup, gridBagConstraints);

        lblMemNum.setText("Membership No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblMemNum, gridBagConstraints);

        lblMembershipClass.setText("Membership Class");
        lblMembershipClass.setName("lblCustomerType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblMembershipClass, gridBagConstraints);

        cboMembershipClass.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMembershipClass.setName("cboCustomerType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(cboMembershipClass, gridBagConstraints);

        lblCustStatus.setText("Customer Status");
        lblCustStatus.setName("lblCustomerType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerInfo.add(lblCustStatus, gridBagConstraints);

        cboCustStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCustStatus.setName("cboCustomerType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(cboCustStatus, gridBagConstraints);

        lblNationality.setText("Nationality");
        lblNationality.setName("lblNationality"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblNationality, gridBagConstraints);

        txtNationality.setMaxLength(48);
        txtNationality.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNationality.setName("txtNationality"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(txtNationality, gridBagConstraints);

        lblResidentialStatus.setText("Residential Status");
        lblResidentialStatus.setName("lblResidentialStatus"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblResidentialStatus, gridBagConstraints);

        cboResidentialStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboResidentialStatus.setName("cboResidentialStatus"); // NOI18N
        cboResidentialStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboResidentialStatusActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(cboResidentialStatus, gridBagConstraints);

        lblLanguage.setText("Language");
        lblLanguage.setName("lblLanguage"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerInfo.add(lblLanguage, gridBagConstraints);

        txtLanguage.setMaxLength(48);
        txtLanguage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLanguage.setName("txtLanguage"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerInfo.add(txtLanguage, gridBagConstraints);

        lblIntroType.setText("Introducer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblIntroType, gridBagConstraints);

        cboIntroType.setPopupWidth(150);
        cboIntroType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntroTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(cboIntroType, gridBagConstraints);

        lblStaffId.setText("Staff Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblStaffId, gridBagConstraints);

        txtStaffId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStaffId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStaffIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        panCustomerInfo.add(txtStaffId, gridBagConstraints);

        lblCreatedDt.setText("Created Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblCreatedDt, gridBagConstraints);

        lblCreatedDt1.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCreatedDt1.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panCustomerInfo.add(lblCreatedDt1, gridBagConstraints);

        chkStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStaffActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, -25, 0, 0);
        panCustomerInfo.add(chkStaff, gridBagConstraints);

        txtMemNum.setMaxLength(48);
        txtMemNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemNum.setName("txtNationality"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerInfo.add(txtMemNum, gridBagConstraints);

        btnStaffId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStaffId.setToolTipText("Save");
        btnStaffId.setMaximumSize(new java.awt.Dimension(21, 18));
        btnStaffId.setMinimumSize(new java.awt.Dimension(21, 18));
        btnStaffId.setPreferredSize(new java.awt.Dimension(21, 18));
        btnStaffId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStaffIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 110, 0, 0);
        panCustomerInfo.add(btnStaffId, gridBagConstraints);

        chkRegionalLang.setMaximumSize(new java.awt.Dimension(30, 27));
        chkRegionalLang.setMinimumSize(new java.awt.Dimension(30, 27));
        chkRegionalLang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRegionalLangActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panCustomerInfo.add(chkRegionalLang, gridBagConstraints);

        lblRegionalLang.setText("Regional Language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        panCustomerInfo.add(lblRegionalLang, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPersonalInfo.add(panCustomerInfo, gridBagConstraints);

        panContactsList.setName("panContactsList"); // NOI18N
        panContactsList.setLayout(new java.awt.GridBagLayout());

        panContacts.setBorder(javax.swing.BorderFactory.createTitledBorder("Contacts"));
        panContacts.setMinimumSize(new java.awt.Dimension(230, 280));
        panContacts.setName("panContacts"); // NOI18N
        panContacts.setLayout(new java.awt.GridBagLayout());

        srpContactList.setMinimumSize(new java.awt.Dimension(210, 250));
        srpContactList.setName("srpContactList"); // NOI18N

        tblContactList.setName("tblContactList"); // NOI18N
        tblContactList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblContactListMousePressed(evt);
            }
        });
        srpContactList.setViewportView(tblContactList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panContacts.add(srpContactList, gridBagConstraints);

        panContactControl.setMinimumSize(new java.awt.Dimension(210, 33));
        panContactControl.setName("panContactControl"); // NOI18N
        panContactControl.setPreferredSize(new java.awt.Dimension(210, 33));
        panContactControl.setLayout(new java.awt.GridBagLayout());

        btnContactToMain.setText("Set as Primary");
        btnContactToMain.setEnabled(false);
        btnContactToMain.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnContactToMain.setName("btnContactToMain"); // NOI18N
        btnContactToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactToMainActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panContactControl.add(btnContactToMain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContacts.add(panContactControl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 3, 2);
        panContactsList.add(panContacts, gridBagConstraints);

        txtCustomerID.setEditable(false);
        txtCustomerID.setMaxLength(10);
        txtCustomerID.setMinimumSize(new java.awt.Dimension(140, 21));
        txtCustomerID.setName("txtCustomerID"); // NOI18N
        txtCustomerID.setPreferredSize(new java.awt.Dimension(140, 21));
        txtCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerIDActionPerformed(evt);
            }
        });
        txtCustomerID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panContactsList.add(txtCustomerID, gridBagConstraints);

        lblCustomerID.setText("Customer ID");
        lblCustomerID.setMinimumSize(new java.awt.Dimension(82, 18));
        lblCustomerID.setName("lblCustomerID"); // NOI18N
        lblCustomerID.setPreferredSize(new java.awt.Dimension(82, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panContactsList.add(lblCustomerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        panPersonalInfo.add(panContactsList, gridBagConstraints);

        tabIndCust.addTab("Personal Details", panPersonalInfo);

        panMISKYC.setMinimumSize(new java.awt.Dimension(600, 390));
        panMISKYC.setPreferredSize(new java.awt.Dimension(600, 390));
        panMISKYC.setLayout(new java.awt.GridBagLayout());

        panMIS.setBorder(javax.swing.BorderFactory.createTitledBorder("MIS"));
        panMIS.setMinimumSize(new java.awt.Dimension(370, 450));
        panMIS.setPreferredSize(new java.awt.Dimension(370, 450));
        panMIS.setLayout(new java.awt.GridBagLayout());

        lblCompany.setText("Company");
        lblCompany.setName("lblCompany"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panMIS.add(lblCompany, gridBagConstraints);

        txtCompany.setMaxLength(128);
        txtCompany.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCompany.setName("txtCompany"); // NOI18N
        txtCompany.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panMIS.add(txtCompany, gridBagConstraints);

        lblPrimaryOccupation.setText("Primary Occupation");
        lblPrimaryOccupation.setName("lblPrimaryOccupation"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panMIS.add(lblPrimaryOccupation, gridBagConstraints);

        cboPrimaryOccupation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPrimaryOccupation.setName("cboPrimaryOccupation"); // NOI18N
        cboPrimaryOccupation.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panMIS.add(cboPrimaryOccupation, gridBagConstraints);

        lblProfession.setText("Profession");
        lblProfession.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panMIS.add(lblProfession, gridBagConstraints);

        cboProfession.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProfession.setName("cboProfession"); // NOI18N
        cboProfession.setPopupWidth(200);
        cboProfession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProfessionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panMIS.add(cboProfession, gridBagConstraints);

        lblAnnualIncomeLevel.setText("Annual Income Level");
        lblAnnualIncomeLevel.setName("lblAnnualIncomeLevel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panMIS.add(lblAnnualIncomeLevel, gridBagConstraints);

        cboAnnualIncomeLevel.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAnnualIncomeLevel.setName("cboAnnualIncomeLevel"); // NOI18N
        cboAnnualIncomeLevel.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panMIS.add(cboAnnualIncomeLevel, gridBagConstraints);

        lblEducationalLevel.setText("Educational Level");
        lblEducationalLevel.setName("lblEducationalLevel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panMIS.add(lblEducationalLevel, gridBagConstraints);

        cboEducationalLevel.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEducationalLevel.setName("cboEducationalLevel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panMIS.add(cboEducationalLevel, gridBagConstraints);

        lblPrefCommunication.setText("Pref. Communication");
        lblPrefCommunication.setName("lblPrefCommunication"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panMIS.add(lblPrefCommunication, gridBagConstraints);

        cboPrefCommunication.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPrefCommunication.setName("cboPrefCommunication"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panMIS.add(cboPrefCommunication, gridBagConstraints);

        lblDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panMIS.add(lblDesignation, gridBagConstraints);

        txtDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDesignation.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panMIS.add(txtDesignation, gridBagConstraints);

        panITDetails.setMinimumSize(new java.awt.Dimension(300, 23));
        panITDetails.setPreferredSize(new java.awt.Dimension(300, 23));
        panITDetails.setLayout(new java.awt.GridBagLayout());

        rdoITDec.add(rdoITDec_Pan);
        rdoITDec_Pan.setText("Pan");
        rdoITDec_Pan.setMaximumSize(new java.awt.Dimension(63, 27));
        rdoITDec_Pan.setMinimumSize(new java.awt.Dimension(63, 27));
        rdoITDec_Pan.setPreferredSize(new java.awt.Dimension(63, 27));
        rdoITDec_Pan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoITDec_PanActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -5;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panITDetails.add(rdoITDec_Pan, gridBagConstraints);

        rdoITDec.add(rdoITDec_F60);
        rdoITDec_F60.setText("Form-60");
        rdoITDec_F60.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoITDec_F60.setMaximumSize(new java.awt.Dimension(61, 27));
        rdoITDec_F60.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoITDec_F60.setPreferredSize(new java.awt.Dimension(76, 27));
        rdoITDec_F60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoITDec_F60ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panITDetails.add(rdoITDec_F60, gridBagConstraints);

        rdoITDec.add(rdoITDec_F61);
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panITDetails.add(rdoITDec_F61, gridBagConstraints);

        txtPanNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPanNumber.setValidation(new DefaultValidation());
        txtPanNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPanNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 19);
        panITDetails.add(txtPanNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 0, 0);
        panMIS.add(panITDetails, gridBagConstraints);

        panITDetails1.setMinimumSize(new java.awt.Dimension(290, 45));
        panITDetails1.setName(""); // NOI18N
        panITDetails1.setPreferredSize(new java.awt.Dimension(520, 100));
        panITDetails1.setLayout(new java.awt.GridBagLayout());

        cboReligion.setMinimumSize(new java.awt.Dimension(100, 21));
        cboReligion.setName("cboProfession"); // NOI18N
        cboReligion.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 0);
        panITDetails1.add(cboReligion, gridBagConstraints);

        lblReligion.setText("Religion");
        lblReligion.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panITDetails1.add(lblReligion, gridBagConstraints);

        cboCaste.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCaste.setName("cboProfession"); // NOI18N
        cboCaste.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panITDetails1.add(cboCaste, gridBagConstraints);

        lblCaste.setText("Caste");
        lblCaste.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panITDetails1.add(lblCaste, gridBagConstraints);

        lblSubCaste.setText("SubCaste");
        lblSubCaste.setMinimumSize(new java.awt.Dimension(63, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        panITDetails1.add(lblSubCaste, gridBagConstraints);

        cboSubCaste.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panITDetails1.add(cboSubCaste, gridBagConstraints);

        lblChkMinority.setText("Minority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panITDetails1.add(lblChkMinority, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panITDetails1.add(chkMinority, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 0, 0);
        panMIS.add(panITDetails1, gridBagConstraints);

        panITDetails2.setMinimumSize(new java.awt.Dimension(285, 43));
        panITDetails2.setPreferredSize(new java.awt.Dimension(285, 43));
        panITDetails2.setLayout(new java.awt.GridBagLayout());

        lblWardNo.setText("Ward No");
        lblWardNo.setMaximumSize(new java.awt.Dimension(46, 18));
        lblWardNo.setMinimumSize(new java.awt.Dimension(65, 18));
        lblWardNo.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panITDetails2.add(lblWardNo, gridBagConstraints);

        txtWardNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtWardNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panITDetails2.add(txtWardNo, gridBagConstraints);

        lblDesam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDesam.setText("Desam");
        lblDesam.setMaximumSize(new java.awt.Dimension(45, 18));
        lblDesam.setMinimumSize(new java.awt.Dimension(45, 18));
        lblDesam.setPreferredSize(new java.awt.Dimension(45, 18));
        panITDetails2.add(lblDesam, new java.awt.GridBagConstraints());

        cbcomboDesam.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panITDetails2.add(cbcomboDesam, gridBagConstraints);

        lblAmsam.setText("Amsam");
        lblAmsam.setMaximumSize(new java.awt.Dimension(60, 18));
        lblAmsam.setMinimumSize(new java.awt.Dimension(60, 18));
        lblAmsam.setPreferredSize(new java.awt.Dimension(60, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panITDetails2.add(lblAmsam, gridBagConstraints);

        cbcomboAmsam.setMinimumSize(new java.awt.Dimension(150, 21));
        cbcomboAmsam.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panITDetails2.add(cbcomboAmsam, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 0, 8);
        panMIS.add(panITDetails2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 120, 0);
        panMISKYC.add(panMIS, gridBagConstraints);

        panKYC.setBorder(javax.swing.BorderFactory.createTitledBorder("KYC"));
        panKYC.setMinimumSize(new java.awt.Dimension(290, 390));
        panKYC.setPreferredSize(new java.awt.Dimension(290, 390));
        panKYC.setLayout(new java.awt.GridBagLayout());

        lblAddrVerified.setText("Address Verified");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblAddrVerified, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(chkAddrVerified, gridBagConstraints);

        lblPhVerified.setText("Phone Number Verified");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblPhVerified, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(chkPhVerified, gridBagConstraints);

        lblFinanceStmtVerified.setText("Obtained Financial Statement");
        lblFinanceStmtVerified.setToolTipText("Obtained Financial Statement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblFinanceStmtVerified, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(chkFinanceStmtVerified, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(chkSentThanksLetter, gridBagConstraints);

        lblSentThanksLetter.setText("Sent Thanks Letter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblSentThanksLetter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(chkConfirmationfromIntroducer, gridBagConstraints);

        lblConfirmationfromIntroducer.setText("Confirmation from Introducer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblConfirmationfromIntroducer, gridBagConstraints);

        cboAddrProof.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAddrProof.setName("cboCareOf"); // NOI18N
        cboAddrProof.setPopupWidth(100);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panKYC.add(cboAddrProof, gridBagConstraints);

        lblAddrProof.setText("Address Proof");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblAddrProof, gridBagConstraints);

        lblIncomeParticulars.setText("Income Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblIncomeParticulars, gridBagConstraints);

        chkIncParticulars.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkIncParticulars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIncParticularsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(chkIncParticulars, gridBagConstraints);

        chkLandDetails.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chkLandDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLandDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(chkLandDetails, gridBagConstraints);

        lblLandDetails.setText("Land Holding Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panKYC.add(lblLandDetails, gridBagConstraints);

        panProofDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        panProofDetails.setMinimumSize(new java.awt.Dimension(200, 55));
        panProofDetails.setPreferredSize(new java.awt.Dimension(198, 55));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panKYC.add(panProofDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 128, 0);
        panMISKYC.add(panKYC, gridBagConstraints);

        panProofList.setName("panProofList"); // NOI18N
        panProofList.setLayout(new java.awt.GridBagLayout());

        panProof.setBorder(javax.swing.BorderFactory.createTitledBorder("Proof"));
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 3, 2);
        panProofList.add(panProof, gridBagConstraints);

        panMISKYC.add(panProofList, new java.awt.GridBagConstraints());

        tabIndCust.addTab("MIS and KYC", panMISKYC);

        panGuardianDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("In case of Minor"));
        panGuardianDetails.setLayout(new java.awt.GridBagLayout());

        panGuardData.setLayout(new java.awt.GridBagLayout());

        lblRelationNO.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblRelationNO, gridBagConstraints);

        cboRelationNO.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRelationNO.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(cboRelationNO, gridBagConstraints);

        lblGuardianNameNO.setText("Gurdian's Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblGuardianNameNO, gridBagConstraints);

        txtGuardianNameNO.setMaxLength(64);
        txtGuardianNameNO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGuardianNameNO.setValidation(new DefaultValidation());
        txtGuardianNameNO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGuardianNameNOKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(txtGuardianNameNO, gridBagConstraints);

        lblGuardianPhoneNO.setText("Phone No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblGuardianPhoneNO, gridBagConstraints);

        phoneGPanelNO.setLayout(new java.awt.GridBagLayout());

        txtGuardianACodeNO.setMaxLength(5);
        txtGuardianACodeNO.setMinimumSize(new java.awt.Dimension(50, 21));
        txtGuardianACodeNO.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        phoneGPanelNO.add(txtGuardianACodeNO, gridBagConstraints);

        txtGuardianPhoneNO.setMaxLength(12);
        txtGuardianPhoneNO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        phoneGPanelNO.add(txtGuardianPhoneNO, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGuardData.add(phoneGPanelNO, gridBagConstraints);

        tdtGuardianDOB.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtGuardianDOB.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtGuardianDOB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtGuardianDOBFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(tdtGuardianDOB, gridBagConstraints);

        lblGuardianDoB.setText("DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblGuardianDoB, gridBagConstraints);

        txtGuardianAge.setMaxLength(128);
        txtGuardianAge.setMinimumSize(new java.awt.Dimension(40, 21));
        txtGuardianAge.setName("txtFirstName"); // NOI18N
        txtGuardianAge.setPreferredSize(new java.awt.Dimension(40, 21));
        txtGuardianAge.setValidation(new DefaultValidation());
        txtGuardianAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGuardianAgeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGuardData.add(txtGuardianAge, gridBagConstraints);

        lblGuardianAge.setText("Age");
        lblGuardianAge.setName("lblGuardianAge"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panGuardData.add(lblGuardianAge, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 90, 4);
        panGuardianDetails.add(panGuardData, gridBagConstraints);

        sptGuard.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardianDetails.add(sptGuard, gridBagConstraints);

        panGuardAddr.setLayout(new java.awt.GridBagLayout());

        lblGStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGStreet, gridBagConstraints);

        txtGStreet.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGStreet.setValidation(new DefaultValidation());
        txtGStreet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGStreetKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(txtGStreet, gridBagConstraints);

        lblGArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGArea, gridBagConstraints);

        txtGArea.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGArea.setValidation(new DefaultValidation());
        txtGArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGAreaKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(txtGArea, gridBagConstraints);

        lblGCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGCountry, gridBagConstraints);

        cboGCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(cboGCountry, gridBagConstraints);

        lblGState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGState, gridBagConstraints);

        cboGState.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGState.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(cboGState, gridBagConstraints);

        lblGCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGCity, gridBagConstraints);

        cboGCity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGCity.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(cboGCity, gridBagConstraints);

        lblGPinCode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGPinCode, gridBagConstraints);

        txtGPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGPinCode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(txtGPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardianDetails.add(panGuardAddr, gridBagConstraints);

        tabIndCust.addTab("Guardian Details", panGuardianDetails);

        panIncomeParticulars.setBorder(javax.swing.BorderFactory.createTitledBorder("Income Details"));
        panIncomeParticulars.setMaximumSize(new java.awt.Dimension(120, 125));
        panIncomeParticulars.setMinimumSize(new java.awt.Dimension(120, 125));
        panIncomeParticulars.setName("panPersonalInfo"); // NOI18N
        panIncomeParticulars.setPreferredSize(new java.awt.Dimension(120, 125));
        panIncomeParticulars.setLayout(new java.awt.GridBagLayout());

        panAdditionalInfo1.setMaximumSize(new java.awt.Dimension(30, 94));
        panAdditionalInfo1.setMinimumSize(new java.awt.Dimension(30, 94));
        panAdditionalInfo1.setName("panAdditionalInfo"); // NOI18N
        panAdditionalInfo1.setPreferredSize(new java.awt.Dimension(30, 94));
        panAdditionalInfo1.setLayout(new java.awt.GridBagLayout());

        lblIncName.setText("Name");
        lblIncName.setName("lblMiddleName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo1.add(lblIncName, gridBagConstraints);

        txtIncName.setMaxLength(128);
        txtIncName.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIncName.setName("txtMiddleName"); // NOI18N
        txtIncName.setPreferredSize(new java.awt.Dimension(150, 21));
        txtIncName.setValidation(new DefaultValidation());
        txtIncName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIncNameKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAdditionalInfo1.add(txtIncName, gridBagConstraints);

        lblIncRelation.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panAdditionalInfo1.add(lblIncRelation, gridBagConstraints);

        cboIncRelation.setMaximumSize(new java.awt.Dimension(27, 22));
        cboIncRelation.setMinimumSize(new java.awt.Dimension(100, 22));
        cboIncRelation.setPopupWidth(150);
        cboIncRelation.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAdditionalInfo1.add(cboIncRelation, gridBagConstraints);

        lblIncIncome.setText("Income");
        lblIncIncome.setName("lblMiddleName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo1.add(lblIncIncome, gridBagConstraints);

        panSearch2.setMaximumSize(new java.awt.Dimension(100, 25));
        panSearch2.setMinimumSize(new java.awt.Dimension(150, 25));
        panSearch2.setPreferredSize(new java.awt.Dimension(150, 25));
        panSearch2.setLayout(new java.awt.GridBagLayout());

        txtIncIncome.setMaxLength(4);
        txtIncIncome.setMinimumSize(new java.awt.Dimension(70, 21));
        txtIncIncome.setPreferredSize(new java.awt.Dimension(70, 21));
        txtIncIncome.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        panSearch2.add(txtIncIncome, gridBagConstraints);

        cboIncPack.setMinimumSize(new java.awt.Dimension(75, 22));
        cboIncPack.setPopupWidth(150);
        cboIncPack.setPreferredSize(new java.awt.Dimension(75, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 14);
        panSearch2.add(cboIncPack, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAdditionalInfo1.add(panSearch2, gridBagConstraints);

        panFreezeSave.setMaximumSize(new java.awt.Dimension(25, 35));
        panFreezeSave.setMinimumSize(new java.awt.Dimension(25, 35));
        panFreezeSave.setPreferredSize(new java.awt.Dimension(25, 35));
        panFreezeSave.setLayout(new java.awt.GridBagLayout());

        btnIncSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnIncSave.setToolTipText("Save");
        btnIncSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnIncSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnIncSave.setName("btnContactNoAdd"); // NOI18N
        btnIncSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnIncSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnIncSave, gridBagConstraints);

        btnIncNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnIncNew.setToolTipText("New");
        btnIncNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnIncNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnIncNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnIncNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnIncNew, gridBagConstraints);

        btnIncDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnIncDelete.setToolTipText("Delete");
        btnIncDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnIncDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnIncDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnIncDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnIncDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAdditionalInfo1.add(panFreezeSave, gridBagConstraints);

        cboIncDetProfession.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIncDetProfession.setName("cboProfession"); // NOI18N
        cboIncDetProfession.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo1.add(cboIncDetProfession, gridBagConstraints);

        txtIncDetCompany.setMaxLength(128);
        txtIncDetCompany.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIncDetCompany.setName("txtCompany"); // NOI18N
        txtIncDetCompany.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo1.add(txtIncDetCompany, gridBagConstraints);

        lblIncDetProfession.setText("Profession");
        lblIncDetProfession.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo1.add(lblIncDetProfession, gridBagConstraints);

        lblIncDetCompany.setText("Institution");
        lblIncDetCompany.setName("lblCompany"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo1.add(lblIncDetCompany, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panIncomeParticulars.add(panAdditionalInfo1, gridBagConstraints);

        panCustomerInfo1.setMaximumSize(new java.awt.Dimension(70, 50));
        panCustomerInfo1.setMinimumSize(new java.awt.Dimension(50, 50));
        panCustomerInfo1.setName("panCustomerInfo"); // NOI18N
        panCustomerInfo1.setPreferredSize(new java.awt.Dimension(70, 50));
        panCustomerInfo1.setLayout(new java.awt.GridBagLayout());

        srpIncParticulars.setMinimumSize(new java.awt.Dimension(300, 200));
        srpIncParticulars.setPreferredSize(new java.awt.Dimension(300, 200));

        tblIncParticulars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl_No", "Name", "Relationship", "Profession", "Institution", "Income", "Yr/Month"
            }
        ));
        tblIncParticulars.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblIncParticularsMousePressed(evt);
            }
        });
        srpIncParticulars.setViewportView(tblIncParticulars);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerInfo1.add(srpIncParticulars, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIncomeParticulars.add(panCustomerInfo1, gridBagConstraints);

        tabIndCust.addTab("Income Details", panIncomeParticulars);
        panIncomeParticulars.getAccessibleContext().setAccessibleName("Particulars");

        panLandDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Land Details  Info"));
        panLandDetails.setMaximumSize(new java.awt.Dimension(120, 125));
        panLandDetails.setMinimumSize(new java.awt.Dimension(120, 125));
        panLandDetails.setName("panLandInfo"); // NOI18N
        panLandDetails.setPreferredSize(new java.awt.Dimension(120, 125));
        panLandDetails.setLayout(new java.awt.GridBagLayout());

        panLandInfoDetails.setAutoscrolls(true);
        panLandInfoDetails.setMaximumSize(new java.awt.Dimension(30, 10));
        panLandInfoDetails.setMinimumSize(new java.awt.Dimension(598, 165));
        panLandInfoDetails.setName("panLandInfo"); // NOI18N
        panLandInfoDetails.setPreferredSize(new java.awt.Dimension(598, 165));
        panLandInfoDetails.setLayout(new java.awt.GridBagLayout());

        lblLocation.setText("Location");
        lblLocation.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblLocation, gridBagConstraints);

        lblTaluk.setText("Taluk");
        lblTaluk.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLandInfoDetails.add(lblTaluk, gridBagConstraints);

        lblAreaInAcrs.setText("Area In Acres");
        lblAreaInAcrs.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblAreaInAcrs, gridBagConstraints);

        lblIrrigated.setText("Irrigated");
        lblIrrigated.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblIrrigated, gridBagConstraints);

        lblHobli.setText("Hobli");
        lblHobli.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblHobli, gridBagConstraints);

        lblVillage.setText("Village");
        lblVillage.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblVillage, gridBagConstraints);

        lblPost.setText("Post Office");
        lblPost.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblPost, gridBagConstraints);

        lblSurNo.setText("Survey No");
        lblSurNo.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblSurNo, gridBagConstraints);

        lblSrcIrr.setText("Irrigation Source");
        lblSrcIrr.setName("lblEducationalLevel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(lblSrcIrr, gridBagConstraints);

        lbLandlState.setText("State");
        lbLandlState.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLandInfoDetails.add(lbLandlState, gridBagConstraints);

        lblDistrict.setText("District");
        lblDistrict.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLandInfoDetails.add(lblDistrict, gridBagConstraints);

        lblLandPin.setText("Pin");
        lblLandPin.setName("lblFirstName"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panLandInfoDetails.add(lblLandPin, gridBagConstraints);

        txtLoc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoc.setNextFocusableComponent(txtSrNo);
        txtLoc.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(txtLoc, gridBagConstraints);

        txtSrNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSrNo.setNextFocusableComponent(txtAreaInAcrs);
        txtSrNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(txtSrNo, gridBagConstraints);

        txtAreaInAcrs.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAreaInAcrs.setNextFocusableComponent(cboIrrigated);
        txtAreaInAcrs.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(txtAreaInAcrs, gridBagConstraints);

        cboIrrigated.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIrrigated.setName("cboResidentialStatus"); // NOI18N
        cboIrrigated.setNextFocusableComponent(cboSrIrrigation);
        cboIrrigated.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLandInfoDetails.add(cboIrrigated, gridBagConstraints);

        cboSrIrrigation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSrIrrigation.setName("cboResidentialStatus"); // NOI18N
        cboSrIrrigation.setNextFocusableComponent(txtVillage);
        cboSrIrrigation.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 4);
        panLandInfoDetails.add(cboSrIrrigation, gridBagConstraints);

        txtVillage.setMaxLength(128);
        txtVillage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtVillage.setName("txtFirstName"); // NOI18N
        txtVillage.setNextFocusableComponent(txtPo);
        txtVillage.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panLandInfoDetails.add(txtVillage, gridBagConstraints);

        txtPo.setMaxLength(128);
        txtPo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPo.setName("txtFirstName"); // NOI18N
        txtPo.setNextFocusableComponent(txtHobli);
        txtPo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panLandInfoDetails.add(txtPo, gridBagConstraints);

        txtHobli.setMaxLength(128);
        txtHobli.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHobli.setName("txtFirstName"); // NOI18N
        txtHobli.setNextFocusableComponent(cboLandDetState);
        txtHobli.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panLandInfoDetails.add(txtHobli, gridBagConstraints);

        cboLandDetState.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLandDetState.setName("cboResidentialStatus"); // NOI18N
        cboLandDetState.setNextFocusableComponent(cboDistrict);
        cboLandDetState.setPopupWidth(150);
        cboLandDetState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLandDetStateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panLandInfoDetails.add(cboLandDetState, gridBagConstraints);

        cboDistrict.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDistrict.setName("cboResidentialStatus"); // NOI18N
        cboDistrict.setNextFocusableComponent(cboTaluk);
        cboDistrict.setPopupWidth(150);
        cboDistrict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDistrictActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panLandInfoDetails.add(cboDistrict, gridBagConstraints);

        cboTaluk.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTaluk.setName("cboResidentialStatus"); // NOI18N
        cboTaluk.setNextFocusableComponent(txtLandDetPincode);
        cboTaluk.setPopupWidth(200);
        cboTaluk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTalukActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panLandInfoDetails.add(cboTaluk, gridBagConstraints);

        txtLandDetPincode.setMaxLength(16);
        txtLandDetPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLandDetPincode.setName("txtPincode"); // NOI18N
        txtLandDetPincode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panLandInfoDetails.add(txtLandDetPincode, gridBagConstraints);

        panFreezeSave1.setMinimumSize(new java.awt.Dimension(111, 28));
        panFreezeSave1.setPreferredSize(new java.awt.Dimension(111, 28));
        panFreezeSave1.setLayout(new java.awt.GridBagLayout());

        btnLandNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnLandNew.setToolTipText("New");
        btnLandNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLandNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLandNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLandNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLandNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave1.add(btnLandNew, gridBagConstraints);

        btnLandSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnLandSave.setToolTipText("Save");
        btnLandSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLandSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLandSave.setName("btnContactNoAdd"); // NOI18N
        btnLandSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLandSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLandSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave1.add(btnLandSave, gridBagConstraints);

        btnLandDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnLandDelete.setToolTipText("Delete");
        btnLandDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLandDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLandDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLandDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLandDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave1.add(btnLandDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        panLandInfoDetails.add(panFreezeSave1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 20);
        panLandDetails.add(panLandInfoDetails, gridBagConstraints);

        panLandInfoTable.setMaximumSize(new java.awt.Dimension(30, 10));
        panLandInfoTable.setMinimumSize(new java.awt.Dimension(580, 110));
        panLandInfoTable.setName("panLandInfo"); // NOI18N
        panLandInfoTable.setPreferredSize(new java.awt.Dimension(580, 110));
        panLandInfoTable.setAutoscrolls(true);
        panLandInfoTable.setLayout(new java.awt.GridBagLayout());

        srpCustomerLandDetails.setMinimumSize(new java.awt.Dimension(570, 90));
        srpCustomerLandDetails.setPreferredSize(new java.awt.Dimension(570, 90));

        tblCustomerLandDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Location", "Survey No", "Area", "Irrigated", "Source", "Village", "PostOffice", "Hobli"
            }
        ));
        tblCustomerLandDetails.setMinimumSize(new java.awt.Dimension(560, 80));
        tblCustomerLandDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(900, 400));
        tblCustomerLandDetails.setPreferredSize(new java.awt.Dimension(560, 80));
        tblCustomerLandDetails.setOpaque(false);
        tblCustomerLandDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCustomerLandDetailsMousePressed(evt);
            }
        });
        srpCustomerLandDetails.setViewportView(tblCustomerLandDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLandInfoTable.add(srpCustomerLandDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 15);
        panLandDetails.add(panLandInfoTable, gridBagConstraints);

        tabIndCust.addTab("Land Holding Details", panLandDetails);
        panLandDetails.getAccessibleContext().setAccessibleName("Land Details");
        panLandDetails.getAccessibleContext().setAccessibleParent(tabIndCust);

        panCustomerHistory.setMinimumSize(new java.awt.Dimension(600, 204));
        panCustomerHistory.setPreferredSize(new java.awt.Dimension(600, 204));
        panCustomerHistory.setLayout(new java.awt.GridBagLayout());

        srpCustomerHistory.setMinimumSize(new java.awt.Dimension(580, 200));
        srpCustomerHistory.setPreferredSize(new java.awt.Dimension(580, 200));

        tblCustomerHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Customer Id", "Relationship", "Product Id", "Product Type", "Account No.", "From", "To", "Deposit Amount", "Maturity Amount", "Available Balance", "Status"
            }
        ));
        tblCustomerHistory.setMinimumSize(new java.awt.Dimension(600, 300));
        tblCustomerHistory.setOpaque(false);
        tblCustomerHistory.setPreferredScrollableViewportSize(new java.awt.Dimension(900, 400));
        tblCustomerHistory.setPreferredSize(new java.awt.Dimension(600, 300));
        srpCustomerHistory.setViewportView(tblCustomerHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCustomerHistory.add(srpCustomerHistory, gridBagConstraints);

        lblDealingWith.setText("Dealing with the bank since :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerHistory.add(lblDealingWith, gridBagConstraints);

        chkClosAcdetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkClosAcdetailActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 80, 0, 0);
        panCustomerHistory.add(chkClosAcdetail, gridBagConstraints);

        lblDealingPeriod.setMinimumSize(new java.awt.Dimension(200, 21));
        lblDealingPeriod.setPreferredSize(new java.awt.Dimension(200, 21));
        panCustomerHistory.add(lblDealingPeriod, new java.awt.GridBagConstraints());

        lblClosAcdetail.setText("Include closed account details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCustomerHistory.add(lblClosAcdetail, gridBagConstraints);

        tabIndCust.addTab("Cust.360", panCustomerHistory);

        panRegLanguage.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRegLanguage.setLayout(new java.awt.GridBagLayout());

        lblRegName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegName, gridBagConstraints);

        txtRegName.setAllowAll(true);
        txtRegName.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        txtRegName.setMinimumSize(new java.awt.Dimension(300, 21));
        txtRegName.setPreferredSize(new java.awt.Dimension(300, 21));
        txtRegName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegName, gridBagConstraints);

        txtRegMName.setAllowAll(true);
        txtRegMName.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMName.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMName.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMNameKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMName, gridBagConstraints);

        lblRegHname.setText("House Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panRegLanguage.add(lblRegHname, gridBagConstraints);

        txtRegHname.setAllowAll(true);
        txtRegHname.setMinimumSize(new java.awt.Dimension(300, 21));
        txtRegHname.setPreferredSize(new java.awt.Dimension(300, 21));
        txtRegHname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegHnameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegHname, gridBagConstraints);

        lblRegPlace.setText("Place");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegPlace, gridBagConstraints);

        txtRegPlace.setAllowAll(true);
        txtRegPlace.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegPlace.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegPlace.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegPlaceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegPlace, gridBagConstraints);

        lblRegVillage.setText("Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegVillage, gridBagConstraints);

        txtRegState.setAllowAll(true);
        txtRegState.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegState.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegState.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegStateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegState, gridBagConstraints);

        txtRegMaHname.setAllowAll(true);
        txtRegMaHname.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaHname.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaHname.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaHname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaHnameKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaHname, gridBagConstraints);

        txtRegMaPlace.setAllowAll(true);
        txtRegMaPlace.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaPlace.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaPlace.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaPlace.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaPlaceKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaPlace, gridBagConstraints);

        txtRegMavillage.setAllowAll(true);
        txtRegMavillage.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMavillage.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMavillage.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMavillage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMavillageKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        panRegLanguage.add(txtRegMavillage, gridBagConstraints);

        lblRegTaluk.setText("Taluk");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegTaluk, gridBagConstraints);

        lblRegCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegCity, gridBagConstraints);

        lblRegState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegState, gridBagConstraints);

        txtRegMaCity.setAllowAll(true);
        txtRegMaCity.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaCity.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaCity.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaCityKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaCity, gridBagConstraints);

        txtRegCity.setAllowAll(true);
        txtRegCity.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegCity.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegCity.setRequestFocusEnabled(false);
        txtRegCity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegCityFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegCity, gridBagConstraints);

        txtRegMaTaluk.setAllowAll(true);
        txtRegMaTaluk.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaTaluk.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaTaluk.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaTaluk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaTalukKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaTaluk, gridBagConstraints);

        txtRegMaState.setAllowAll(true);
        txtRegMaState.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaState.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaState.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaState.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaStateKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaState, gridBagConstraints);

        txtRegMaCountry.setAllowAll(true);
        txtRegMaCountry.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaCountry.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaCountry.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaCountry.setRequestFocusEnabled(false);
        txtRegMaCountry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaCountryKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaCountry, gridBagConstraints);

        txtRegTaluk.setAllowAll(true);
        txtRegTaluk.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegTaluk.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegTaluk.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegTalukFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegTaluk, gridBagConstraints);

        lblRegAmsam.setText("Amsam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegAmsam, gridBagConstraints);

        lblRegDesam.setText("Desam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegDesam, gridBagConstraints);

        lblRegCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegCountry, gridBagConstraints);

        txtRegMaAmsam.setAllowAll(true);
        txtRegMaAmsam.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaAmsam.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaAmsam.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaAmsam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaAmsamKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaAmsam, gridBagConstraints);

        txtRegAmsam.setAllowAll(true);
        txtRegAmsam.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegAmsam.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegAmsam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegAmsamFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegAmsam, gridBagConstraints);

        txtRegMaDesam.setAllowAll(true);
        txtRegMaDesam.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaDesam.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaDesam.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaDesam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaDesamKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaDesam, gridBagConstraints);

        txtRegDesam.setAllowAll(true);
        txtRegDesam.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegDesam.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegDesam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegDesamFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegDesam, gridBagConstraints);

        txtRegCountry.setAllowAll(true);
        txtRegCountry.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegCountry.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegCountry.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegCountryFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegCountry, gridBagConstraints);

        txtRegGardName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegGardName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegGardName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegGardNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegGardName, gridBagConstraints);

        lblRegGardName.setText("Guardian Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(lblRegGardName, gridBagConstraints);

        txtRegMaGardName.setAllowAll(true);
        txtRegMaGardName.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaGardName.setMinimumSize(new java.awt.Dimension(450, 21));
        txtRegMaGardName.setPreferredSize(new java.awt.Dimension(450, 21));
        txtRegMaGardName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaGardNameKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegMaGardName, gridBagConstraints);

        txtRegvillage.setAllowAll(true);
        txtRegvillage.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegvillage.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegvillage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegvillageFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panRegLanguage.add(txtRegvillage, gridBagConstraints);

        cLabel1.setText("Malayalam Fields : F4- Malayalam Keyboard, F2- save to malayalam dictionary");
        cLabel1.setFont(new java.awt.Font("MS Sans Serif", 1, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 11, 0);
        panRegLanguage.add(cLabel1, gridBagConstraints);

        tabIndCust.addTab("Regional Language", panRegLanguage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCustomer.add(tabIndCust, gridBagConstraints);

        getContentPane().add(panCustomer, java.awt.BorderLayout.CENTER);

        tbrOperativeAcctProduct.setMinimumSize(new java.awt.Dimension(830, 27));
        tbrOperativeAcctProduct.setPreferredSize(new java.awt.Dimension(830, 27));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
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

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace12);

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

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setNextFocusableComponent(btnNew);
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

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace15);

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
        btnPrint.setFocusable(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace16);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace17);

        btnDeletedDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeletedDetails.setToolTipText("Enquiry Of Closed Individuals");
        btnDeletedDetails.setEnabled(false);
        btnDeletedDetails.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDeletedDetails.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDeletedDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletedDetailsActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDeletedDetails);

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

        mbrCustomer.setName("mbrCustomer"); // NOI18N
        mbrCustomer.setPreferredSize(new java.awt.Dimension(49, 18));

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
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
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

        mitSaveAs.setText("Save As");
        mitSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveAsActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSaveAs);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPanNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPanNumberFocusLost
        // TODO add your handling code here:
        if (txtPanNumber.getText().length() > 0 && !ClientUtil.validatePAN(txtPanNumber)) {
            ClientUtil.showMessageWindow("Invalid Pan Number, Enter Proper Pan No (Format :ABCDE1234F)");
            txtPanNumber.setText("");
        }

    }//GEN-LAST:event_txtPanNumberFocusLost

//    private Image zoom(Image temp, int newWidth, int newHieght) throws Exception {
//	//Need an array (for RGB, with the size of original image)
//	int tX = temp.getWidth();
//	int tY = temp.getHeight();
//	int rgb[] = new int[tX * tY];//Get the RGB array of image into  "rgb"
//	temp.getRGB(rgb, 0, tX, 0, 0, tX, tY);//Call to our function and obtain RGB2
//	int rgb2[] = reescalaArray(rgb, tX, tY, newWidth, newHieght);//Create an image with that RGB array
//	rgb = null;
//	Image temp2 = Image.createRGBImage(rgb2, newWidth, newHieght, true);
//	rgb2 = null;
//	return temp2;
//    }
//
//    private int[] reescalaArray(int[] ini, int x, int y, int x2, int y2) {
//        int out[] = new int[x2 * y2];
//        int dy, dx;
//        for (int yy = 0; yy < y2; yy++) {
//                dy = yy * y / y2;
//                for (int xx = 0; xx < x2; xx++) {
//                        dx = xx * x / x2;
//                        out[(x2 * yy) + xx] = ini[(x * dy) + dx];
//                }
//        }
//        return out;
//    }
    private void rdoITDec_F61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoITDec_F61ActionPerformed
        // TODO add your handling code here:
//        rdoITDec_F60.setSelected(false);
//        rdoITDec_Pan.setSelected(false);
        txtPanNumber.setText("");
        txtPanNumber.setEnabled(false);
//        lblPanNumber.setVisible(false);

    }//GEN-LAST:event_rdoITDec_F61ActionPerformed

    private void rdoITDec_F60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoITDec_F60ActionPerformed
        // TODO add your handling code here:
//        rdoITDec_F61.setSelected(false);
//        rdoITDec_Pan.setSelected(false);
        txtPanNumber.setEnabled(false);
//        lblPanNumber.setVisible(false);
        txtPanNumber.setText("");
    }//GEN-LAST:event_rdoITDec_F60ActionPerformed

    private void rdoITDec_PanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoITDec_PanActionPerformed
        // TODO add your handling code here:
//        rdoITDec_F61.setSelected(false);
//        rdoITDec_F60.setSelected(false);
        if (rdoITDec_Pan.isSelected()) {
            txtPanNumber.setEnabled(true);
//        lblPanNumber.setVisible(true);
        } else {
            txtPanNumber.setEnabled(false);
//            lblPanNumber.setVisible(false);
        }
    }//GEN-LAST:event_rdoITDec_PanActionPerformed

    private void txtGuardianAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGuardianAgeActionPerformed
        // TODO add your handling code here:
        int a = CommonUtil.convertObjToInt(txtGuardianAge.getText());
        if (a < minorAge) {
            ClientUtil.displayAlert("Guardian Should Not Be Minor");
            txtGuardianAge.setText("");
        }
    }//GEN-LAST:event_txtGuardianAgeActionPerformed

    private void tdtGuardianDOBFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtGuardianDOBFocusLost
        // TODO add your handling code here:
        if (tdtGuardianDOB.getDateValue().length() > 0) {
            boolean guardianIsMinor = isGuardianMinor();
            if (guardianIsMinor) {
                ClientUtil.displayAlert("Guardian Should Not Be Minor");
                tdtGuardianDOB.setDateValue("");
            } else {
                String GuarDob = CommonUtil.convertObjToStr(tdtGuardianDOB.getDateValue());
                Date dt = DateUtil.getDateMMDDYYYY(GuarDob);
                long DateDiff = DateUtil.dateDiff(dt, currDt);
                long Dat = (DateDiff / 365);
                txtGuardianAge.setText(String.valueOf(Dat));
            }
        }
    }//GEN-LAST:event_tdtGuardianDOBFocusLost

    private void cboDistrictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDistrictActionPerformed
        // TODO add your handling code here:
        String state = ((ComboBoxModel) cboLandDetState.getModel()).getKeyForSelected().toString();
        if (cboDistrict.getSelectedIndex() > 0) {
            String dis = ((ComboBoxModel) cboDistrict.getModel()).getKeyForSelected().toString();
            cboTaluk.setSelectedItem("");
            observable.setCbmTaluk(dis, state);
            cboTaluk.setModel(observable.getCbmTaluk());
        }
    }//GEN-LAST:event_cboDistrictActionPerformed

    private void cboLandDetStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLandDetStateActionPerformed
        // TODO add your handling code here:
        if (cboLandDetState.getSelectedIndex() > 0) {
            String state = ((ComboBoxModel) cboLandDetState.getModel()).getKeyForSelected().toString();
            cboDistrict.setSelectedItem("");
            cboTaluk.setSelectedItem("");
            observable.setCbmDistrict(state);
            cboDistrict.setModel(observable.getCbmDistrict());
        }
    }//GEN-LAST:event_cboLandDetStateActionPerformed

    private void cboTalukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTalukActionPerformed
    }//GEN-LAST:event_cboTalukActionPerformed

    private void cboProfessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProfessionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProfessionActionPerformed

    private void chkLandDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLandDetailsActionPerformed
        // TODO add your handling code here:
        chkLand = chkLandDetails.isSelected();
        if (chkLand) {
            tabIndCust.addTab("Land Details", panLandDetails);
            ClientUtil.enableDisable(panLandDetails, false);
            observable.setIsLandDetails(true);
            objCustomerUISupport.setLandDetailsButtonEnableDisable(true, btnLandNew, btnLandDelete, btnLandSave);
        } else {
            if (tblCustomerLandDetails.getRowCount() > 0) {
                ClientUtil.displayAlert("First Delete All the Records From LandDetails Tab");
                chkLandDetails.setSelected(true);
            } else {
                tabIndCust.remove(panLandDetails);
                observable.setIsLandDetails(false);
            }
        }
    }//GEN-LAST:event_chkLandDetailsActionPerformed

    private void btnLandDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLandDeleteActionPerformed
        // TODO add your handling code here:
        observable.deleteLandDetails(tblCustomerLandDetails.getSelectedRow());
        observable.resetNewLandDetails();
        observable.ttNotifyObservers();
        ClientUtil.clearAll(panLandDetails);
        ClientUtil.enableDisable(panLandDetails, false);
    }//GEN-LAST:event_btnLandDeleteActionPerformed

    private void tblCustomerLandDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomerLandDetailsMousePressed
        // TODO add your handling code here:
        updateOBFields();
        btnLandNew.setEnabled(true);
        LandDetupdateMode = true;
        observable.setNewLandDet(false);
        observable.populateLandDetailsOfCustomer(tblCustomerLandDetails.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            //            objCustomerUISupport.setContactButtonEnableDisableDefault(true,btnContactNew, btnContactDelete, btnContactToMain);

            ClientUtil.enableDisable(panLandDetails, true);
            btnLandDelete.setEnabled(true);
            btnLandSave.setEnabled(true);
            LandDetailsupdateTab = tblCustomerLandDetails.getSelectedRow();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")) {
            btnLandNew.setEnabled(false);

        }
    }//GEN-LAST:event_tblCustomerLandDetailsMousePressed

    private void btnLandSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLandSaveActionPerformed
        // TODO add your handling code here:
        try {
            updateOBFields();
            mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME, panLandDetails);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if (mandatoryMessage.length() > 0) {
                objCustomerUISupport.displayAlert(mandatoryMessage);
            } else {
                observable.addLandDetMap(LandDetailsupdateTab, LandDetupdateMode);
                ClientUtil.clearAll(panLandDetails);
                ClientUtil.enableDisable(panLandDetails, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLandSaveActionPerformed

    private void btnLandNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLandNewActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.setNewLandDet(true);
        ClientUtil.enableDisable(panLandDetails, true);
        btnLandNew.setEnabled(true);
        observable.resetNewLandDetails();
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnLandNewActionPerformed

    private void btnIncDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncDeleteActionPerformed
        // TODO add your handling code here:
        observable.deleteIncome(tblIncParticulars.getSelectedRow());
        observable.resetNewIncomeParticulars();
        observable.ttNotifyObservers();
        ClientUtil.clearAll(panIncomeParticulars);
        ClientUtil.enableDisable(panIncomeParticulars, false);
    }//GEN-LAST:event_btnIncDeleteActionPerformed

    private void tblIncParticularsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIncParticularsMousePressed
        // TODO add your handling code here:
        updateOBFields();
        btnIncNew.setEnabled(true);
        updateMode = true;
        observable.setNewIncomeDet(false);
        observable.populateIncParDetails(tblIncParticulars.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            ClientUtil.enableDisable(panIncomeParticulars, true);
            btnIncDelete.setEnabled(true);
            btnIncSave.setEnabled(true);
            updateTab = tblIncParticulars.getSelectedRow();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || actionType.equals("DeletedDetails")) {
            btnIncNew.setEnabled(false);

        }
    }//GEN-LAST:event_tblIncParticularsMousePressed

    private void btnIncSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncSaveActionPerformed
        // TODO add your handling code here:
        try {
            updateOBFields();
            mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME, panIncomeParticulars);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if (mandatoryMessage.length() > 0) {
                objCustomerUISupport.displayAlert(mandatoryMessage);
            } else {
                observable.addIncomeParMap(updateTab, updateMode);
                ClientUtil.clearAll(panIncomeParticulars);
                ClientUtil.enableDisable(panIncomeParticulars, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnIncSaveActionPerformed

    private void btnIncNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncNewActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.setNewIncomeDet(true);
        ClientUtil.enableDisable(panIncomeParticulars, true);
        btnIncNew.setEnabled(true);
        observable.resetNewIncomeParticulars();
        observable.ttNotifyObservers();
        updateMode = false;
        updateTab = -1;
    }//GEN-LAST:event_btnIncNewActionPerformed

    private void chkIncParticularsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIncParticularsActionPerformed
        // TODO add your handling code here:
        chk = chkIncParticulars.isSelected();
        if (chk) {
            tabIndCust.addTab("Income Details", panIncomeParticulars);
            ClientUtil.enableDisable(panIncomeParticulars, false);
            observable.setIsIncomePar(true);
            objCustomerUISupport.setIncomeButtonEnableDisable(true, btnIncNew, btnIncDelete, btnIncSave);
        } else {
            if (tblIncParticulars.getRowCount() > 0) {
                ClientUtil.displayAlert("First Delete All the Records From Income Particulrs Tab");
                chkIncParticulars.setSelected(true);
            } else {
                tabIndCust.remove(panIncomeParticulars);
                observable.setIsIncomePar(false);
            }
        }
    }//GEN-LAST:event_chkIncParticularsActionPerformed

    private void txtAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgeActionPerformed
        // TODO add your handling code here:
        int a = CommonUtil.convertObjToInt(txtAge.getText());
        if (a < minorAge) {
            checkminor = true;
                chkMinor.setSelected(true);
            ClientUtil.enableDisable(panGuardianDetails, true);
            tabIndCust.addTab("Guardian Details", panGuardianDetails);
            makeMaritialStatusVisible(false);
            rdoMaritalStatus_Single.setSelected(true);
            enableDisableFields(false);
            observable.setNewGuardian(true);
            observable.setIsMinor(true);
            chkMinor.setSelected(true);
        } else if(a > minorAge){
             checkminor = false;
                chkMinor.setSelected(false);
            resetGaurdianPanel();
            makeMaritialStatusVisible(true);
            ClientUtil.enableDisable(panGuardianDetails, false);
            observable.setIsMinor(false);
            chkMinor.setSelected(false);
            
        }
        
                
    }//GEN-LAST:event_txtAgeActionPerformed
    public void calculateRetirementDate() {
        if (txtRetireAge.getText().length() > 0 && tdtDateOfBirth.getDateValue().length() > 0) {
            tdtretireDt.setDateValue("");
            String dob = CommonUtil.convertObjToStr(tdtDateOfBirth.getDateValue());
            Date dt = DateUtil.getDateMMDDYYYY(dob);
            int ages = CommonUtil.convertObjToInt(txtRetireAge.getText());
            Date retDt = DateUtil.addDays(dt, (ages * 365));
            tdtretireDt.setDateValue(DateUtil.getStringDate(retDt));
        }
    }

    public void calculateRetirementAge() {
        if (tdtDateOfBirth.getDateValue().length() > 0 && tdtretireDt.getDateValue().length() > 0) {
            ClientUtil.validateLTDate(tdtDateOfBirth);
            String dob = CommonUtil.convertObjToStr(tdtDateOfBirth.getDateValue());
            Date dt = DateUtil.getDateMMDDYYYY(dob);
            Date ret_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtretireDt.getDateValue()));
            long a = DateUtil.dateDiff(dt, ret_dt);
            long b = a / 365;
            String Ret_ages = String.valueOf(b);
            txtRetireAge.setText(Ret_ages);

        }
    }
    private void cboResidentialStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboResidentialStatusActionPerformed
        // TODO add your handling code here:
//        String status =(String)((ComboBoxModel)cboResidentialStatus.getModel()).getKeyForSelected();
//        if(status.equals("NONRESIDENT"))
//        ClientUtil.enableDisable(panPassPortDetails,true);
//        else
//        ClientUtil.enableDisable(panPassPortDetails,false);    
    }//GEN-LAST:event_cboResidentialStatusActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
//         popUp("ViewDetails");
        actionType = "ViewDetails";
        HashMap sourceMap = new HashMap();
        sourceMap.put("CUST_TYPE", "INDIVIDUAL");
        new CheckCustomerIdUI(this, sourceMap);
        btnCheck();
        if (chkIncParticulars.isSelected()) {
            tabIndCust.add("Income Details", panIncomeParticulars);
        } else {
            tabIndCust.remove(panIncomeParticulars);
        }
        if (chkLandDetails.isSelected()) {
            tabIndCust.add("Land Details", panLandDetails);
        } else {
            tabIndCust.remove(panLandDetails);
        }
    }//GEN-LAST:event_btnViewActionPerformed

    private void chkStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStaffActionPerformed
        // TODO add your handling code here:
        if (chkStaff.isSelected()) {
            cboCustomerType.setEnabled(false);
            cboCustomerType.setSelectedItem(observable.getCbmCustomerType().getDataForKey("STAFF"));
            btnStaffId.setEnabled(true);
            txtStaffId.setEnabled(true);
            txtStaffId.setEditable(true);
        } else {
//          cboCustomerType.setSelectedItem(observable.getCbmCustomerType().getDataForKey("OTHERS"));
            cboCustomerType.setEnabled(true);
            btnStaffId.setEnabled(false);
            txtStaffId.setEnabled(false);
            txtStaffId.setEditable(false);
            txtStaffId.setText("");
        }
    }//GEN-LAST:event_chkStaffActionPerformed

    private void btnDeletedDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletedDetailsActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
        popUp("DeletedDetails");
        btnCheck();
        if (chkIncParticulars.isSelected()) {
            tabIndCust.add(panIncomeParticulars);
        } else {
            tabIndCust.remove(panIncomeParticulars);
        }
        if (chkLandDetails.isSelected()) {
            tabIndCust.add(panLandDetails);
        } else {
            tabIndCust.remove(panLandDetails);
        }
    }//GEN-LAST:event_btnDeletedDetailsActionPerformed

    private void chkMinorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMinorActionPerformed
        // TODO add your handling code here:
       // checkminor = true;
        if (txtAge.getText().length() > 0) {
            int a = CommonUtil.convertObjToInt(txtAge.getText());
            if (a < minorAge) {
                checkminor = true;
                chkMinor.setSelected(true);
            } else if(a > minorAge){
                checkminor = false;
                chkMinor.setSelected(false);
                //System.out.println("fffff");
                ClientUtil.displayAlert("Check the Customer Age!!!");
                
            }
        }
    }//GEN-LAST:event_chkMinorActionPerformed

    private void rdoGender_FemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGender_FemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoGender_FemaleActionPerformed

    private void rdoMaritalStatus_SingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMaritalStatus_SingleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMaritalStatus_SingleActionPerformed

    private void rdoGender_MaleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_MaleFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoGender_MaleFocusLost

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void txtStaffIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStaffIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStaffIdFocusLost

    private void btnStaffIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStaffIdActionPerformed
        // TODO add your handling code here:
        popUp("Staff");
    }//GEN-LAST:event_btnStaffIdActionPerformed

    private void cboCareOfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboCareOfFocusLost
        //--- If Male or Single is chosen, donot allow him to chose husband as care of
        if ((rdoGender_Male.isSelected() == true && cboCareOf.getSelectedItem().equals("Husband")) || (rdoMaritalStatus_Single.isSelected() == true && cboCareOf.getSelectedItem().equals("Husband"))) {
            cboCareOf.setSelectedItem("");
        }
        //        System.out.println("cboCareOf.getSelectedItem:" + CommonUtil.convertObjToStr(observable.getCbmCareOf().getKeyForSelected()));
        if (CommonUtil.convertObjToStr(observable.getCbmCareOf().getKeyForSelected()).equals(CUST_TYPE_COURT)) {
//            lblCustomerStatus.setText(resourceBundle.getString("minorWards"));
//            lblCustomerStatus.setToolTipText(lblCustomerStatus.getText());
        }
    }//GEN-LAST:event_cboCareOfFocusLost

    private void cboCustomerTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCustomerTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCustomerTypeActionPerformed

    private void mitSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveAsActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        popUp(ClientConstants.ACTION_STATUS[2]);
        saveAsSettings();

    }//GEN-LAST:event_mitSaveAsActionPerformed

    private void txtFirstNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFirstNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameFocusLost

    private void cboIntroTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntroTypeActionPerformed
        // TODO add your handling code here:
        //        updateOBFields();
        if (cboIntroType.getSelectedIndex() > 0) {
            if (observable.getCboIntroType() != null) {
                if (!observable.getCboIntroType().equals("Intro N/A - Legacy")) {
                    if (observable.getCbmIntroType().getDataForKey("INTRO_NOT_APPLICABLE") != null) {
                        observable.getCbmIntroType().removeKeyAndElement("INTRO_NOT_APPLICABLE");
                        cboIntroType.setModel(observable.getCbmIntroType());
                    }
                }
            }
//            observable.setCboIntroType((String) ((ComboBoxModel) cboIntroType.getModel()).getKeyForSelected());
            introducerUI.setIntroducerType(CommonUtil.convertObjToStr(cboIntroType.getSelectedItem()));
            final String INTRO = (String) ((ComboBoxModel) cboIntroType.getModel()).getKeyForSelected();

            /* If Some Value is Selected...*/
            if (INTRO.equalsIgnoreCase("")) {
                introducerUI.setIntroPanel(false);
                introducerUI.setPanInVisible();
            } else {
                introducerUI.setPanInVisible();
                introducerUI.setIntroPanel(true);
                introducerUI.setPanVisible(INTRO);
            }
        }
    }//GEN-LAST:event_cboIntroTypeActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        //        if(viewType==ClientConstants.ACTIONTYPE_NEW){
        phoneExist = false;
        isFilled = false;/*When not made like this, when the user clicks help button for any
         other textfield,and thereby user selects a relevant row fillData method will be called and the isFilled boolean variable will become true, so now
         when the user clicks the searchbutton and the user selects none of the row and clicks the cancel avaialable and isFilled boolean variables are true, so unnecessarily
         the action type is set to Edit**/
        HashMap map = new HashMap();
        map.put("FNAME", txtFirstName.getText());
        map.put("MNAME", txtMiddleName.getText());
        map.put("LNAME", txtLastName.getText());
        map.put("DOB", tdtDateOfBirth.getDateValue());
        map.put("EMAIL_ID", txtEmailID.getText());
        callView("getSearchCustomerValues", map);
        //        }
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * To display customer list popup for Edit & Delete options
     */
    private void callView(String mapName, HashMap whereMap) {
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, mapName);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        boolean available = new com.see.truetransact.ui.common.viewsearch.ViewSearchUI(this, viewMap).show("search");
        if (available && isFilled) {
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            HashMap hash = new HashMap();
            hash.put(CommonConstants.MAP_WHERE, CUSTOMERID);
            hash.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            observable.getData(hash, introducerUI.getIntroducerOB());
            //        cboIntroType.setSelectedItem(observable.getCboIntroType());
            introducerUI.update(null, null);
            objCustomerUISupport.fillPhotoSign(lblPhoto, lblSign, btnPhotoRemove, btnSignRemove);
            objCustomerUISupport.fillProofPhoto(lblProofPhoto, btnProofPhotoRemove); //28-11-2020
            observable.fillTbmCustomerHistory(txtCustomerID.getText());
            observable.fillTbmCustomerDepositHistory(txtCustomerID.getText());
            tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
            setButtonEnableDisable();
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
    private boolean chkContactExistance(String addrType) {
        final ArrayList tbldata = new ArrayList();
        final int tblContactListSize = tblContactList.getRowCount();
        for (int i = 0; i < tblContactListSize; i++) {
            tbldata.add(tblContactList.getValueAt(i, 0));
        }
        return tbldata.contains(addrType);
    }
    
     private boolean chkProofExistance(String ProofType) {
        final ArrayList tbldata = new ArrayList();
        final int tblproofListSize = tblProofList.getRowCount();
        for (int i = 0; i < tblproofListSize; i++) {
            tbldata.add(tblProofList.getValueAt(i, 0));
        }
        return tbldata.contains(ProofType);
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
        btnClearPassport.setEnabled(false);
        if (chkIncParticulars.isSelected()) {
            tabIndCust.add(panIncomeParticulars);
        } else {
            tabIndCust.remove(panIncomeParticulars);
        }
        if (chkLandDetails.isSelected()) {
            tabIndCust.add(panLandDetails);
        } else {
            tabIndCust.remove(panLandDetails);
        }
    }//GEN-LAST:event_btnRejectActionPerformed

    public void authorizeStatus(String authorizeStatus) {
//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnGenerateAppPwd.setEnabled(false);
        if (viewType == AUTHORIZE && isFilled) {
             
            observable.setResult(observable.getActionType());
//            String strWarnMsg = tabIndCust.isAllTabsVisited();
            //Changed By Suresh
//            String strWarnMsg;
//            if(isMinor()) 
//                strWarnMsg= tabIndCust.isAllTabsVisited();
//            else
//                strWarnMsg= tabIndCust.isAllTabsVisited("Guardian Details");
//            strWarnMsg = strWarnMsg + tabContactAndIdentityInfo.isAllTabsVisited();
//            if (strWarnMsg.length() > 0){
//                objCustomerUISupport.displayAlert(strWarnMsg);
//                return;
//            }
//            strWarnMsg = null;
            tabIndCust.resetVisits();
            tabContactAndIdentityInfo.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            if (authorizeStatus.equalsIgnoreCase("REJECTED") && flag == true) {
                singleAuthorizeMap.put("DELETESTATUS", "MODIFIED");
                singleAuthorizeMap.put(CommonConstants.STATUS, "AUTHORIZED");
                singleAuthorizeMap.put("DELETEREMARKS", "");
                singleAuthorizeMap.put("STATUSCHECK", "");
            }
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("CUSTOMER ID", this.txtCustomerID.getText());
            singleAuthorizeMap.put("CUST_PWD",this.txtCustPwd.getText());
//            ClientUtil.execute("authorizeIndiCust", singleAuthorizeMap);
            //Changed By Suresh
            observable.set_authorizeMap(singleAuthorizeMap);
            observable.doAction(introducerUI.getIntroducerOB());
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtCustomerID.getText());
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Customer Master");
            }
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Customer Master");
            }
            btnCancelActionPerformed(null);
            if (goldLoanUI != null) {
                goldLoanUI.customerIdPopulating(authorizeStatus);
                this.dispose();
            }
//            observable.setStatus();
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);  //This line added by Rajesh
            viewType = 0;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSelectIndiCustAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeIndiCust");
            isFilled = false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
        }
    }

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnSearch.setEnabled(false);
        btnClearPassport.setEnabled(false);
        if (chkIncParticulars.isSelected()) {
            tabIndCust.add(panIncomeParticulars);
        } else {
            tabIndCust.remove(panIncomeParticulars);
        }
        if (chkLandDetails.isSelected()) {
            tabIndCust.add(panLandDetails);
        } else {
            tabIndCust.remove(panLandDetails);
        }



    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void tdtDateOfBirthFocusLost(java.awt.event.FocusEvent evt) {
        if (tdtDateOfBirth.getDateValue().length() > 0) {
            ClientUtil.validateLTDate(tdtDateOfBirth);
            String dob = CommonUtil.convertObjToStr(tdtDateOfBirth.getDateValue());
            Date dt = DateUtil.getDateMMDDYYYY(dob);
            Date curr_dt = (Date) currDt.clone();
            if (tdtDateOfBirth.getDateValue() != null && tdtDateOfBirth.getDateValue().length() > 0) {
                enableDisablePanGaurdian();
            }
            if (dt != null) {
                long a = DateUtil.dateDiff(dt, curr_dt);
                long b = a / 365;
                String ages = String.valueOf(b);
                txtAge.setText(ages);
                int agediff = retireAge - CommonUtil.convertObjToInt(ages);
                if (agediff > 0) {
                    Date retDt = DateUtil.addDays(dt, (retireAge * 365));
                    tdtretireDt.setDateValue(DateUtil.getStringDate(retDt));
                    long diff = DateUtil.dateDiff(dt, retDt);
                    long retAge = diff / 365;
                    txtRetireAge.setText(CommonUtil.convertObjToStr(retAge));
                }
            }
        }
    }
private void rdoGender_FemaleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_FemaleFocusGained
    }//GEN-LAST:event_rdoGender_FemaleFocusGained

    private void rdoGender_MaleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_MaleFocusGained
        // Add your handling code here:
        rdoGender_FemaleFocusGained(evt);
    }//GEN-LAST:event_rdoGender_MaleFocusGained

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        btnAuthorize.setBorderPainted(false);
        if (observable.getAuthorizeStatus() != null) {
            super.removeEditLock(observable.getTxtCustomerID());
        }
        cbcomboAmsam.setSelectedIndex(0);
        cbcomboDesam.setSelectedIndex(0);
        setModified(false);
        enableHelpBtn(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.setStatus();
        objCustomerUISupport.setContactButtonEnableDisableDefault(false, btnContactNew, btnContactDelete, btnContactToMain);
        objCustomerUISupport.setPhoneButtonEnableDisable(false, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
        objCustomerUISupport.setIncomeButtonEnableDisable(false, btnIncNew, btnIncDelete, btnIncSave);
        objCustomerUISupport.setLandDetailsButtonEnableDisable(false, btnLandNew, btnLandDelete, btnLandSave);
        observable.resetForm();
        observable.resetCustomerHistoryTable();
        objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
        objCustomerUISupport.setLblPhotoSignDefault(lblPhoto, lblSign);
        lblPhoto.setIcon(null);
        lblProofPhoto.setIcon(null);
        lblSign.setIcon(null);
        btnSearch.setEnabled(false);
        ClientUtil.clearAll(this);

        //introducerUI.setEnabled(false);
        introducerUI.enableIntroducerData(false);
        introducerUI.resetIntroducerData();
        //introducerUI.setEnabled(false);
        introducerUI.enableDisableBtn(false);
        btnContactNew.setEnabled(false);
        btnContactAdd.setEnabled(false);
        btnDeletedDetails.setEnabled(true);
        if (getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            cifClosingAlert();
        }
        ClientUtil.enableDisable(panCustomer, false);

        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        txtCustomerID.setEnabled(true);
        btnGenerateAppPwd.setEnabled(false); 
        txtCustomerID.setEditable(true);
        btnEdit.setEnabled(true);
        if (goldLoanUI != null) {
            goldLoanUI.customerIdPopulating("CANCELLED");
            this.dispose();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
         if (fromCashierAuthorizeUI) {
            CashierauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        } 
        if (fromManagerAuthorizeUI) {
            ManagerauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromCashierAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        chkRegionalLang.setSelected(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:

        try {
             if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
                    && observable.getMobileAppLoginStatus().equals("N")) {
                HashMap hm = new HashMap();
                if (!CommonUtil.convertObjToStr(txtCustPwd.getText()).equals("") && CommonUtil.convertObjToStr(txtCustUserid.getText()).equals("")) {
                    ClientUtil.showAlertWindow("Customer user_Id should not be empty...");
                    txtCustUserid.setText("");
                    return;
                } else if (!CommonUtil.convertObjToStr(observable.getTxtCustUserid()).equals(CommonUtil.convertObjToStr(txtCustUserid.getText()))) {
                    hm.put("CUST_USERID", txtCustUserid.getText());
                    List lst = ClientUtil.executeQuery("getCustUserId", hm);
                    if (lst != null && lst.size() > 0) {
                        ClientUtil.showAlertWindow("This user_Id not available!!! Please enter another user_Id");
                        txtCustUserid.setText("");
                        return;
                    }
                }
            }
            updateOBFields();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                String DelRemarks = COptionPane.showInputDialog(this, "Delete Remarks");
                observable.setDeletedRemarks(DelRemarks);
            }

            //To check mandtoryness of the Customer Personal panel and diplay appropriate
            //error message, else proceed
            mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME, panPersonalInfo);
            mandatoryMessage += objCustomerUISupport.checkMandatory(CLASSNAME, panKYC);    
            //--- For mandatory check for Intro details
            final String INTRO = CommonUtil.convertObjToStr(observable.getCbmIntroType().getKeyForSelected());
            if (observable.getCbmIntroType().getKeyForSelected() != "INTRO_NOT_APPLICABLE");
            mandatoryMessage = mandatoryMessage + (introducerUI.mandatoryCheck(INTRO));

            if (isMinorSave()) {
                mandatoryMessage = mandatoryMessage + objCustomerUISupport.checkMandatory(CLASSNAME, panGuardianDetails);
            }
            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                if (tdtJoiningDate.getDateValue().equals("")) {
                    mandatoryMessage = mandatoryMessage + "Joining Date Cannot be Empty !!! \n";
                }
            }

            if (isPassPort()) {
                mandatoryMessage = mandatoryMessage + objCustomerUISupport.checkMandatory(CLASSNAME, panPassPortDetails);
            }
            if (chk) {
                if (objCustomerUISupport.chkIncomeParticulars(tblIncParticulars)) {
                    mandatoryMessage = mandatoryMessage + objCustomerUISupport.checkMandatory(CLASSNAME, panIncomeParticulars);
                }
            }
            if (chkLand) {
                if (objCustomerUISupport.chkLandDetails(tblCustomerLandDetails)) {
                    mandatoryMessage = mandatoryMessage + objCustomerUISupport.checkMandatory(CLASSNAME, panLandDetails);
                }
            }

            if (chkSuspendCust.isSelected() || chkRevokeCust.isSelected()) {
                observable.setIsCustSuspended(true);
            }
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                objCustomerUISupport.displayAlert(mandatoryMessage);
            } else {
                if (txtCustUserid.getText().length() > 0 && txtCustPwd.getText().length() <= 0) {
                    ClientUtil.displayAlert("Customer password not entered");
                    return;
                }
                if (!rdoITDec_Pan.isSelected() && !rdoITDec_F60.isSelected() && !rdoITDec_F61.isSelected()) {
                    ClientUtil.displayAlert("Enter Pan/Form60/61 Details");
                    return;
                }
                if (rdoITDec_Pan.isSelected() && txtPanNumber.getText().length() <= 0) {
                    ClientUtil.displayAlert("Enter Pan Number");
                    return;
                }
                if (cbcomboDesam.getSelectedItem().equals("") || cbcomboDesam.getSelectedItem()==null) {
                    ClientUtil.displayAlert("Enter Desam");
                    return;
                }               
                String alertMsg = "BlockedMsg";
                int optionSelected = 2;
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                    if (isBlocked()) {
                        optionSelected = observable.showAlertWindow(alertMsg);
                    }
                    if (optionSelected == 1) {
                        return;
                    }
                }
                saveAction();
            }
            lblProofPhoto.setIcon(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private boolean isPassPort() {
        boolean isPassPort = false;
        if (observable.getCbmResidentialStatus().getKeyForSelected().equals("NONRESIDENT")) {
            isPassPort = true;
            observable.setIsPassport(isPassPort);
        } else {
            isPassPort = false;
        }
        return isPassPort;
    }

    private boolean isInc() {
        boolean isIncome = false;
        if (chk) {
            isIncome = true;
            observable.setIsPassport(isIncome);
        } else {
            isIncome = false;
        }
        return isIncome;
    }

    private void saveAction() {
        //If communication address type has been set, then proceed
        if (objCustomerUISupport.chkMinAddrTypeCommAddr(tblContactList)) {
            introducerUI.updateOBFields();
            HashMap resultMap = observable.doAction(introducerUI.getIntroducerOB());
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("CUSTOMER ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getResult() == ClientConstants.ACTIONTYPE_NEW) {
                    if (observable.getProxyReturnMap() != null) {
                        if (observable.getProxyReturnMap().containsKey("CUST_ID")) {
                            lockMap.put("CUSTOMER ID", observable.getProxyReturnMap().get("CUST_ID"));
                          
                        }
                    }
                }
                if (observable.getResult() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("CUSTOMER ID", observable.getTxtCustomerID());
                }
                setEditLockMap(lockMap);
                setEditLock();

                setModified(false);
                ClientUtil.enableDisable(this, false);
                setButtonEnableDisable();
                objCustomerUISupport.setPhoneButtonEnableDisable(false, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
                objCustomerUISupport.setContactButtonEnableDisableDefault(false, btnContactNew, btnContactDelete, btnContactToMain);
                objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
                objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
                ClientUtil.clearAll(this);
                btnSearch.setEnabled(false);
                observable.resetForm();
                observable.ttNotifyObservers();
                objCustomerUISupport.setLblPhotoSignDefault(lblPhoto, lblSign);
                introducerUI.resetIntroducerData();
                introducerUI.setIntroPanel(false);
                introducerUI.setPanInVisible();
                enableHelpBtn(false);
                btnContactNew.setEnabled(false);
                btnContactAdd.setEnabled(false);
                observable.setResultStatus();
                if (observable.getResult() == ClientConstants.ACTIONTYPE_NEW) {
//                    txtCustomerID.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    ClientUtil.showMessageWindow("Customer ID : " + CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                }
                //Added By Suresh
                if (loanCustMap == null) {
                    loanCustMap = new HashMap();
                }
                if (loanCustMap.containsKey("LOAN_CUSTOMER_ID")) {
//                    HashMap updateMap = new HashMap();
//                    updateMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
//                    updateMap.put("CUST_ID",resultMap.get("CUST_ID"));
//                    ClientUtil.execute("updateCustIDBranchCode",updateMap);
                    btnCancelActionPerformed(null);
                    cifClosingAlert();
                    HashMap loanMap = new HashMap();
                    loanMap.put("CUSTOMER ID", resultMap.get("CUST_ID"));
//                    goldLoanUI.setViewType("CUSTOMER ID");
//                    goldLoanUI.setCustomerScreen("CUSTOMER_SCREEN");
//                    goldLoanUI.fillData(loanMap);
//                   termLoanUI.customerIdPopulating(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                }
                if (ScreenName.equals("TermLoan")) {
                    TermLoanUI.txtCustID.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    TermLoanUI.txtCustID.setFocusable(true);
                    TermLoanUI.txtCustID.requestFocus(true);
                } else if (ScreenName.equals("Share")) {
                    ShareUI.txtCustId.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    ShareUI.txtCustId.setFocusable(true);
                    ShareUI.txtCustId.requestFocus(true);
                } else if (ScreenName.equals("GoldLoan")) {
                    GoldLoanUI.txtCustID.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    GoldLoanUI.txtCustID.setFocusable(true);
                    GoldLoanUI.txtCustID.requestFocus(true);
                } else if (ScreenName.equals("TermDeposit")) {
                   // TermDepositUI.txtcustid = CommonUtil.convertObjToStr(resultMap.get("CUST_ID"));
                    TermDepositUI.txtCustomerId.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    TermDepositUI.txtCustomerId.setFocusable(true);
                    TermDepositUI.txtCustomerId.requestFocus(true);
                } else if (ScreenName.equals("SuspenseAccount")) {
                    SuspenseAccountMasterUI.txtCustomerId.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    SuspenseAccountMasterUI.txtCustomerId.setFocusable(true);
                    SuspenseAccountMasterUI.txtCustomerId.requestFocus(true);
                } else if (ScreenName.equals("Account")) {
                    AccountsUI.txtCustomerIdAI.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    AccountsUI.txtCustomerIdAI.setFocusable(true);
                    AccountsUI.txtCustomerIdAI.requestFocus(true);
                } else if (ScreenName.equals("MultileTermDeposit")){
                    MultipleTermDepositUI.CustomerId = CommonUtil.convertObjToStr(resultMap.get("CUST_ID"));
                    // added by shihad for mantis 10347 on 22.02.2015
                    MultipleTermDepositUI.txtCustomerId.setText(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    MultipleTermDepositUI.txtCustomerId.setFocusable(true);
                    MultipleTermDepositUI.txtCustomerId.requestFocus(true);
                }                 
            }
        }
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        tabIndCust.addTab("Cust.360", panCustomerHistory);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(ClientConstants.ACTION_STATUS[3]);
        enableHelpBtn(false);
        introducerUI.setActionType(DELETE);
        objCustomerUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
        btnSearch.setEnabled(false);
        ClientUtil.enableDisable(panCustomer, false);
        if (chkIncParticulars.isSelected()) {
            tabIndCust.addTab("Income Details", panIncomeParticulars);
        } else {
            tabIndCust.remove(panIncomeParticulars);
        }
        if (chkLandDetails.isSelected()) {
            tabIndCust.addTab("Land Details", panLandDetails);
        } else {
            tabIndCust.remove(panLandDetails);
        }
        objCustomerUISupport.setIncomeButtonEnableDisable(false, btnIncNew, btnIncDelete, btnIncSave);
        objCustomerUISupport.setLandDetailsButtonEnableDisable(false, btnLandNew, btnLandDelete, btnLandSave);

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if (txtCustomerID.getText().length() > 0) {
            String[] obj = {"Yes ", "No"};
            int option = COptionPane.showOptionDialog(null, ("Do you want to Enable all those fields?"), ("Customer"),
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
            if (option == 0) {
                ClientUtil.enableDisable(this, true);
                txtBranchId.setEnabled(false);
                btnSave.setEnabled(true);
                txtCustomerID.setEnabled(false);
                btnEdit.setEnabled(false);
                cboCustomerType.setEnabled(false);
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                btnClearPassport.setEnabled(false);
                HashMap hash = new HashMap();
                hash.put("CUSTOMER ID", txtCustomerID.getText());
                List authList = ClientUtil.executeQuery("authStatus", hash);
                HashMap statusMap = new HashMap();
                statusMap = (HashMap) authList.get(0);
                String status = CommonUtil.convertObjToStr(statusMap.get("AUTHORIZED_DT"));
                if (!status.equals("")) {
                    txtFirstName.setEnabled(true);
                    txtMiddleName.setEnabled(true);
                    txtLastName.setEnabled(true);
                    tdtJoiningDate.setEnabled(true);
                } else {
                    txtFirstName.setEnabled(true);
                    txtMiddleName.setEnabled(true);
                    txtLastName.setEnabled(true);
                }
                if (chkIncParticulars.isSelected()) {
                    tabIndCust.addTab("Income Details", panIncomeParticulars);
                } else {
                    tabIndCust.remove(panIncomeParticulars);
                }
                if (chkLandDetails.isSelected()) {
                    tabIndCust.addTab("Land Details", panLandDetails);
                } else {
                    tabIndCust.remove(panLandDetails);
                }
                ClientUtil.enableDisable(panLandDetails, false);
                ClientUtil.enableDisable(panIncomeParticulars, false);
                btnClearPassport.setEnabled(true);
                boolean ck = chkSuspendCust.isSelected();
                if (ck) {
                    chkRevokeCust.setEnabled(true);
                    tdtRevokedCustDate.setEnabled(true);
                    chkSuspendCust.setEnabled(false);
                    tdtSuspendCustFrom.setEnabled(false);
                } else {
                    chkRevokeCust.setEnabled(false);
                    tdtRevokedCustDate.setEnabled(false);
                    chkSuspendCust.setEnabled(true);
                    tdtSuspendCustFrom.setEnabled(true);
                }
            } else {
                ClientUtil.enableDisable(this, false);
                cboCustomerType.setEnabled(true);
            }
        } else {
            phoneExist = false;
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            tabIndCust.addTab("Cust.360", panCustomerHistory);
//            popUp(ClientConstants.ACTION_STATUS[2]);
            actionType = ClientConstants.ACTION_STATUS[2];
            HashMap sourceMap = new HashMap();
            sourceMap.put("CUST_TYPE", "INDIVIDUAL");
            new CheckCustomerIdUI(this, sourceMap);
            btnContactDelete.setEnabled(false);
            txtBranchId.setEnabled(false);
            btnContactToMain.setEnabled(false);
            btnPhoneNew.setEnabled(false);
            introducerUI.enableDisableBtn(true);
            if (chkIncParticulars.isSelected()) {
                tabIndCust.addTab("Income Details", panIncomeParticulars);
            } else {
                tabIndCust.remove(panIncomeParticulars);
            }
            if (chkLandDetails.isSelected()) {
                tabIndCust.addTab("Land Details", panLandDetails);
            } else {
                tabIndCust.remove(panLandDetails);
            }
            ClientUtil.enableDisable(panLandDetails, false);
            ClientUtil.enableDisable(panIncomeParticulars, false);
            btnClearPassport.setEnabled(true);
            txtCustomerID.setEnabled(false);
            boolean ck = chkSuspendCust.isSelected();
            if (ck) {
                chkRevokeCust.setEnabled(true);
                tdtRevokedCustDate.setEnabled(true);
                chkSuspendCust.setEnabled(false);
                tdtSuspendCustFrom.setEnabled(false);
            } else {
                chkRevokeCust.setEnabled(false);
                tdtRevokedCustDate.setEnabled(false);
                chkSuspendCust.setEnabled(true);
                tdtSuspendCustFrom.setEnabled(true);
            }
        }
        btnProofPhotoLoad.setEnabled(true);
        btnProofPhotoRemove.setEnabled(true);
//txtAge.setEditable(false);
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
        setModified(true);
        enableHelpBtn(false);
        txtCustPwd.setEnabled(false);
        txtStaffId.setEditable(false);
        txtStaffId.setEnabled(false);
        btnGenerateAppPwd.setEnabled(true); 
        introducerUI.enableDisableBtn(true);
        phoneExist = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        // To remove the Intro Type -   Intro N/A - Legacy for new customers.
        if (observable.getCbmIntroType().getDataForKey("INTRO_NOT_APPLICABLE") != null) {
            observable.getCbmIntroType().removeKeyAndElement("INTRO_NOT_APPLICABLE");
            cboIntroType.setModel(observable.getCbmIntroType());
        }

        ClientUtil.enableDisable(this, true);
        ClientUtil.enableDisable(panGuardianDetails, false);
        ClientUtil.enableDisable(panAddressDetails, false);
//        ClientUtil.enableDisable(panPassPortDetails,false);
        setButtonEnableDisable();
        cboCustomerType.setSelectedItem(observable.getCbmCustomerType().getDataForKey("OTHERS"));
//        cboCustomerType.setEnabled(false);
        cboResidentialStatus.setSelectedItem(observable.getCbmResidentialStatus().getDataForKey("RESIDENT"));
        cboMembershipClass.setSelectedItem(observable.getCbmMembershipClass().getDataForKey("NONE"));
        cboMembershipClass.setEnabled(true);
        cboCustStatus.setSelectedItem(observable.getCbmCustStatus().getDataForKey("PRESENT"));
        objCustomerUISupport.setPhoneButtonEnableDisableDefault(btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        btnPhoneNew.setEnabled(false);
        ClientUtil.enableDisable(this.panPhoneAreaNumber, false);
        ClientUtil.enableDisable(this.panProofDetails, false);
        objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
        objCustomerUISupport.setProofButtonEnableDisable(btnProofNew, btnProofAdd, btnProofDelete);
        objCustomerUISupport.setContactAddEnableDisable(true, btnContactAdd);
        objCustomerUISupport.setPhotoSignLoadEnableDisable(true, btnPhotoLoad, btnSignLoad);
        txtNationality.setText("INDIAN");
        btnSearch.setEnabled(true);
        introducerUI.resetIntroducerData();
        btnContactAdd.setEnabled(false);
        btnContactDelete.setEnabled(false);
        btnContactToMain.setEnabled(false);
        txtBranchId.setText(getSelectedBranchID());
        tabIndCust.remove(panCustomerHistory);
        tabIndCust.remove(panIncomeParticulars);
        tabIndCust.remove(panLandDetails);
        tabIndCust.remove(panRegLanguage);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnDeletedDetails.setEnabled(true);
        chkRevokeCust.setEnabled(false);
        tdtRevokedCustDate.setEnabled(false);
        chkSuspendCust.setEnabled(false);
        tdtSuspendCustFrom.setEnabled(false);
        btnClearPassport.setEnabled(true);
        observable.getCbmIntroType().setKeyForSelected("NOT_APPLICABLE");
        txtCustPwd.setEnabled(false);
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
//            lblJoiningDate.setVisible(true);
//            tdtJoiningDate.setVisible(true);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String today = formatter.format(currDt.clone());
            tdtJoiningDate.setDateValue(today);
        }
        }
        btnProofPhotoLoad.setEnabled(true);
        btnProofPhotoRemove.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

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

    /**
     * To disable all the phone related entry fields and enable only Phone New
     * button
     */
    private void phoneDetailsBtnDefault() {
        ClientUtil.enableDisable(this.panPhoneAreaNumber, false);
        objCustomerUISupport.setPhoneButtonEnableDisableDefault(btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
    }

    /**
     * To initialize comboboxes
     */
    private void initComponentData() {
        cboTitle.setModel(observable.getCbmTitle());
        cboResidentialStatus.setModel(observable.getCbmResidentialStatus());
        cboCustomerType.setModel(observable.getCbmCustomerType());
        cboRelationManager.setModel(observable.getCbmRelationManager());
        cboAnnualIncomeLevel.setModel(observable.getCbmAnnualIncomeLevel());
        cboPrefCommunication.setModel(observable.getCbmPrefCommunication());
        cboCustomerGroup.setModel(observable.getCbmCustomerGroup());
        cboVehicle.setModel(observable.getCbmVehicle());
        cboProfession.setModel(observable.getCbmProfession());
        cboPrimaryOccupation.setModel(observable.getCbmPrimaryOccupation());
        cboEducationalLevel.setModel(observable.getCbmEducationalLevel());
        cboCountry.setModel(observable.getCbmCountry());
        cboAddressType.setModel(observable.getCbmAddressType());
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboPhoneType.setModel(observable.getCbmPhoneType());
        cboPostOffice.setModel(observable.getCbmPostOffice());
        cboCareOf.setModel(observable.getCbmCareOf());
        cboMembershipClass.setModel(observable.getCbmMembershipClass());
        cboCustStatus.setModel(observable.getCbmCustStatus());
        cboIntroType.setModel(observable.getCbmIntroType());

        cboGCity.setModel(observable.getCbmGCity());
        cboGCountry.setModel(observable.getCbmGCountry());
        cboGState.setModel(observable.getCbmGState());
        cboRelationNO.setModel(observable.getCbmRelationNO());
        cboCaste.setModel(observable.getCbmCaste());
        cboSubCaste.setModel(observable.getCbmSubCaste());
//        changed by nikhil
        cboReligion.setModel(observable.getCbmReligion());
        cboAddrProof.setModel(observable.getCbmAddrProof());
        cboIdenProof.setModel(observable.getCbmIdenProof());
        cboPassportIssuePlace.setModel(observable.getCbmPassportIssuePlace());
        cboPassportTitle.setModel(observable.getCbmTitle());
        cboFarClass.setModel(observable.getCbmFarClass());
        cboIncRelation.setModel(observable.getCbmIncRelation());
        cboIncPack.setModel(observable.getCbmIncPack());
        cboSrIrrigation.setModel(observable.getCbmSourceIrrigated());
//        cboTaluk.setModel(observable.getCbmTaluk());
//        cboDistrict.setModel(observable.getCbmDistrict());
        cboLandDetState.setModel(observable.getCbmLandDetState());
        cboIrrigated.setModel(observable.getCbmIrrigated());
        cboIncDetProfession.setModel(observable.getCbmIncProfession());
        //added by jithin 
        cboCustVillage.setModel(observable.getCbmCustVillage());
        cboCustTaluk.setModel(observable.getCbmCustTaluk());
        cbcomboAmsam.setModel(observable.getCbmcomboAmsam());
        cbcomboDesam.setModel(observable.getCbmcomboDesam());
        cboWardName.setModel(observable.getCbmWardName()); // Added by nithya
        txtUniqueId.setAllowAll(true);
        
        // if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
             lblDivision.setVisible(true);
             cboDivision.setVisible(true);
             lblDivision.setEnabled(true);
             cboDivision.setEnabled(true);
             if (observable.getCbmDivision() != null) {
                 System.out.println("observable.getCbmDivision(): " + observable.getCbmDivision());
                 cboDivision.setModel(observable.getCbmDivision());
             }
             
             if (observable.getCbmAgentCustId() != null) {
                 System.out.println("observable.getCbmAgentCustId(): " + observable.getCbmAgentCustId());
                 cboAgentCustId.setModel(observable.getCbmAgentCustId());
             }
           //  }
         //else{
        // lblDivision.setVisible(false);
         //    cboDivision.setVisible(false);
         //}
        
   }

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void chkClosAcdetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkClosAcdetailActionPerformed
        // TODO add your handling code here:  
        observable.resetCustomerHistoryTable();
        tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
        if (txtCustomerID.getText().length() > 0) {
            if (chkClosAcdetail.isSelected()) {
                observable.fillTbmAllCustomerHistory("NON_CLOSED");
            } else {
                observable.fillTbmAllCustomerHistory("CLOSED");
//                observable.fillTbmCustomerHistory(txtCustomerID.getText());
//                observable.fillTbmCustomerDepositHistory(txtCustomerID.getText());
            }
            tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
        }
    }//GEN-LAST:event_chkClosAcdetailActionPerformed

    private void tdtDateOfBirthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtDateOfBirthMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtDateOfBirthMouseClicked

    private void txtAgeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAgeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgeFocusLost

    private void txtAgeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAgeFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgeFocusGained

    private void txtAgeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAgeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgeMouseClicked

    private void txtAgeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAgeMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgeMouseEntered

    private void tdtRevokedCustDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRevokedCustDateFocusLost
        // TODO add your handling code here:
        Date cur_date = (Date) currDt.clone();
        ClientUtil.validateToDate(tdtRevokedCustDate, DateUtil.getStringDate(DateUtil.addDays(cur_date, -1)));
        boolean chk = chkRevokeCust.isSelected();
        if (!chk) {
            ClientUtil.displayAlert("Select The Revoke Option First!!!!!!!!");
            tdtRevokedCustDate.setDateValue("");
        }
    }//GEN-LAST:event_tdtRevokedCustDateFocusLost

    private void chkRevokeCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRevokeCustActionPerformed
        // TODO add your handling code here:
        boolean ck = chkRevokeCust.isSelected();
        if (ck) {
            tdtRevokedCustDate.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
            chkSuspendCust.setSelected(false);
            tdtSuspendCustFrom.setDateValue("");
        } else {
            tdtRevokedCustDate.setDateValue("");
        }
    }//GEN-LAST:event_chkRevokeCustActionPerformed

    private void tdtSuspendCustFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSuspendCustFromFocusLost
        // TODO add your handling code here:
        Date cur_date = (Date) currDt.clone();
        ClientUtil.validateToDate(tdtSuspendCustFrom, DateUtil.getStringDate(DateUtil.addDays(cur_date, -1)));
        boolean chk = chkSuspendCust.isSelected();
        if (!chk) {
            ClientUtil.displayAlert("Select The Suspend Option First!!!!!!!!");
            tdtSuspendCustFrom.setDateValue("");
        }
    }//GEN-LAST:event_tdtSuspendCustFromFocusLost

    private void chkSuspendCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSuspendCustActionPerformed
        // TODO add your handling code here:
        boolean ck = chkSuspendCust.isSelected();
        if (ck) {
            HashMap relationMap = new HashMap();
            relationMap.put("CUST_ID", txtCustomerID.getText());
            List lst = ClientUtil.executeQuery("getSelectCustomerHistory", relationMap);
            if (lst != null && lst.size() > 0) {
                ClientUtil.displayAlert("Cannot Suspend Customer!! Refer Cust.360 Tab");
                tdtSuspendCustFrom.setDateValue("");
                chkSuspendCust.setSelected(false);
            } else {
                tdtSuspendCustFrom.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
                chkRevokeCust.setSelected(false);
                tdtRevokedCustDate.setDateValue("");
            }
        } else {
            tdtSuspendCustFrom.setDateValue("");
        }
    }//GEN-LAST:event_chkSuspendCustActionPerformed

    private void btnClearPassportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPassportActionPerformed
        // TODO add your handling code here:
        txtPassportFirstName.setText("");
        txtPassportMiddleName.setText("");
        txtPassportLastName.setText("");
        tdtPassportIssueDt.setDateValue("");
        tdtPassportValidUpto.setDateValue("");
        txtPassportNo.setText("");
        cboPassportIssuePlace.setSelectedItem("");
        cboPassportTitle.setSelectedItem("");
        txtPassportIssueAuth.setText("");
    }//GEN-LAST:event_btnClearPassportActionPerformed

    private void tdtPassportIssueDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPassportIssueDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtPassportIssueDtFocusLost

    private void tdtPassportValidUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPassportValidUptoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtPassportValidUptoFocusLost

    private void tdtCrAvldSinceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCrAvldSinceFocusLost
        // TODO add your handling code here:
        boolean retDate = false;
        if (tdtCrAvldSince.getDateValue() != null && tdtCrAvldSince.getDateValue().length() > 0) {
            String date = lblCreatedDt1.getText();
            Date createdDt = DateUtil.getDateMMDDYYYY(date);
            String availDate = tdtCrAvldSince.getDateValue();
            Date dtAv = DateUtil.getDateMMDDYYYY(availDate);
            Date dt = (Date) currDt.clone();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (DateUtil.dateDiff(dt, dtAv) != 0) {
                    ClientUtil.showMessageWindow("Date Cannot Be Future Or Previous Date!!!!!!");
                    tdtCrAvldSince.setDateValue("");
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {

                Date TodaysDt = dtAv;
                //--- If Today is Between the "From date" and "To Date" , then return true.
                if (((TodaysDt.after(createdDt) || TodaysDt.compareTo(createdDt) == 0) && (TodaysDt.before((dt))) || TodaysDt.compareTo(dt) == 0)) {
                    retDate = true;
                }
                if (retDate == false) {
                    ClientUtil.showMessageWindow("Credit Availed Since Date Should  Be  Between Account Created Date And Todays Date !!!!!!");
                    tdtCrAvldSince.setDateValue("");
                }
            }

        }
    }//GEN-LAST:event_tdtCrAvldSinceFocusLost

                                       
    private void btnSignRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignRemoveActionPerformed
        lblSign.setIcon(null);
        btnSignRemove.setEnabled(false);
        observable.setSignFile(null);
        observable.setSignByteArray(null);
    }//GEN-LAST:event_btnSignRemoveActionPerformed

    private void btnSignLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignLoadActionPerformed
        // Add your handling code here:
        observable.setScreen(getScreen());
        objCustomerUISupport.loadActivities(lblSign, btnSignRemove);
    }//GEN-LAST:event_btnSignLoadActionPerformed

    private void lblSignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSignMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            objCustomerUISupport.zoomImage(lblSign);
        }
    }//GEN-LAST:event_lblSignMouseClicked

    private void btnPhotoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoRemoveActionPerformed

        // Add your handling code here:
        lblPhoto.setIcon(null);
        btnPhotoRemove.setEnabled(false);
        observable.setPhotoFile(null);
        observable.setPhotoByteArray(null);
    }//GEN-LAST:event_btnPhotoRemoveActionPerformed

    private void btnPhotoLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoLoadActionPerformed
        // Add your handling code here:
        observable.setScreen(getScreen());
        objCustomerUISupport.loadActivities(lblPhoto, btnPhotoRemove);
    }//GEN-LAST:event_btnPhotoLoadActionPerformed

    private void lblPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPhotoMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            objCustomerUISupport.zoomImage(lblPhoto);
            //            javax.swing.JWindow photoFrame = new javax.swing.JWindow();
            //            javax.swing.JLabel photoLabel = new javax.swing.JLabel();
            //            javax.swing.JLayeredPane jlp = new javax.swing.JLayeredPane();
            //            photoLabel.setIcon(lblPhoto.getIcon());
            //            photoFrame.getContentPane().setLayout(new java.awt.BorderLayout()) ;
            //            jlp.add(photoLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
            //            photoFrame.getContentPane().add("Center", jlp);
            //            photoFrame.setSize(lblPhoto.getIcon().getIconWidth(), lblPhoto.getIcon().getIconHeight());
            //            photoFrame.addMouseListener(new MyMouseListener(photoFrame));
            //            TrueTransactMain.showWindow(photoFrame);
        }
    }//GEN-LAST:event_lblPhotoMouseClicked

    private void btnContactDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactDeleteActionPerformed
        // Add your handling code here:
        try {
            if (((String) tblContactList.getModel().getValueAt(tblContactList.getSelectedRow(), 0)).equals(observable.getCommAddrType())) {
                objCustomerUISupport.displayAlert(resourceBundle.getString("mainAddrType"));
            } else {
                String alertMsg = "deleteWarningMsg";
                int optionSelected = observable.showAlertWindow(alertMsg);
                if (optionSelected == 0) {
                    observable.deleteAddress(tblContactList.getSelectedRow());
                }

            }
            objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnContactDeleteActionPerformed

    private void btnContactNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactNewActionPerformed
        // Add your handling code here:
        updateOBFields();
        observable.setNewAddress(true);
        ClientUtil.enableDisable(panAddressDetails, true);
        btnContactAdd.setEnabled(true);
        observable.resetNewAddress();
        objCustomerUISupport.setPhoneButtonEnableDisable(false, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        objCustomerUISupport.setContactButtonEnableDisable(btnContactAdd, btnContactDelete, btnContactToMain);
        //Added By Suresh 26-11-2012
        cboState.setSelectedItem(observable.getCbmState().getDataForKey("84"));
        cboCountry.setSelectedItem(observable.getCbmCountry().getDataForKey("IN"));
    }//GEN-LAST:event_btnContactNewActionPerformed

    private void btnContactAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactAddActionPerformed
        // Add your handling code here:
        try {
            updateOBFields();
            mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME, panAddressDetails);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if (mandatoryMessage.length() > 0) {
                System.out.println("Mandatory message");
                objCustomerUISupport.displayAlert(mandatoryMessage);
            } else {
                final String alertMsg = "phoneDetailsMsg";
                int action = observable.showAlertWindow(alertMsg);
                if (action == 0) {
                    btnPhoneNew.setEnabled(true);
                    btnContactAdd.setEnabled(false);
                } else if (action == 1) {
                    observable.addAddressMap();
                    ClientUtil.clearAll(panContactInfo);
                    ClientUtil.enableDisable(panContactInfo, false);
                    objCustomerUISupport.setPhoneButtonEnableDisable(false, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
                    objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnContactAddActionPerformed

    private void tblPhoneListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhoneListMousePressed
        // Add your handling code here:
        phoneExist = true;
        updateOBFields();
        phoneRow = tblPhoneList.getSelectedRow();
        observable.populatePhone(phoneRow);
        ClientUtil.enableDisable(panPhoneAreaNumber, false);  //Changed true as false by Rajesh.
        updatePhone();
        //To enable PhoneDetails fields for NEW & EDIT options
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && viewType != AUTHORIZE && (!actionType.equals("DeletedDetails"))) {
            ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
            objCustomerUISupport.setPhoneButtonEnableDisable(true, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        }
        if (getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) { //Added by Rajesh.
            ClientUtil.enableDisable(panPhoneType, false);
            btnPhoneNew.setEnabled(false);
            btnContactNoAdd.setEnabled(false);
            btnPhoneDelete.setEnabled(false);
        }
    }//GEN-LAST:event_tblPhoneListMousePressed

    private void btnPhoneDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneDeleteActionPerformed
        // Add your handling code here:
        observable.deletePhoneDetails(tblPhoneList.getSelectedRow());
        updatePhone();
        phoneDetailsBtnDefault();
    }//GEN-LAST:event_btnPhoneDeleteActionPerformed

    private void btnPhoneNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneNewActionPerformed
        // Add your handling code here:
        
        phoneExist = false;
        phoneRow = tblPhoneList.getModel().getRowCount();
        //Added code by nithya on 22-04-2016 for 4312 // Commented on 27-12-2016 [ Customer needs to add more than one phone number
//        if(phoneRow > 0){
//            btnPhoneNew.setEnabled(false);
//            ClientUtil.showAlertWindow("Contact No already exists");
//            return;
//        }else{
        updateOBFields();
        ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
        observable.resetPhoneDetails();
        updatePhone();
        objCustomerUISupport.setPhoneButtonEnableDisableNew(btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
        //}
    }//GEN-LAST:event_btnPhoneNewActionPerformed

    private void btnContactNoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactNoAddActionPerformed
        // Add your handling code here:
        
        mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME, panPhoneAreaNumber);
        //To check whether all the mandatory fields of Phone details have been entered.
        //If not entered properly display alert message, else proceed
        if (mandatoryMessage.length() > 0) {
            objCustomerUISupport.displayAlert(mandatoryMessage);
        } else {
              boolean contactAdded = false;
              for(int i=0; i<tblPhoneList.getRowCount();i++){
                  if(CommonUtil.convertObjToStr(tblPhoneList.getValueAt(i, 1)).equalsIgnoreCase(CommonUtil.convertObjToStr(((ComboBoxModel) cboPhoneType.getModel()).getKeyForSelected()))){
                      contactAdded = true;
                      break;
                  }                      
              }
              if(contactAdded && phoneExist == false){
                  ClientUtil.showAlertWindow("Contact No already exists for this type");
                  return;
              } else if( cboPhoneType.getSelectedItem().equals("Mobile") && txtPhoneNumber.getText().length() !=10) {
                  ClientUtil.showAlertWindow("Mobile Number should be 10 digits only!!!");
                  return;
              } else{
                  updateOBFields();
                  observable.addPhoneList(phoneExist, phoneRow);
                  updatePhone();
                  observable.addAddressMap();
                  objCustomerUISupport.setPhoneButtonEnableDisableDefault(btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
                  objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
                  ClientUtil.enableDisable(panPhoneAreaNumber, false);
                  ClientUtil.enableDisable(panAddressDetails, false);
              }              
              //btnPhoneNew.setEnabled(false); //Added code by nithya on 22-04-2016 for 4312 // Commented on 27-12-2016 [ Customer needs to add more than one phone number
            //            ClientUtil.clearAll(panAddressDetails);
            }
        
    }//GEN-LAST:event_btnContactNoAddActionPerformed

    private void cboAddressTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddressTypeActionPerformed
        // Add your handling code here:
        final String addrType = (String) ((ComboBoxModel) cboAddressType.getModel()).getKeyForSelected();
        if (cboAddressType.getSelectedIndex() != 0 && chkContactExistance(addrType)) {
            observable.setNewAddress(false);
            observable.addressTypeChanged(addrType);
            //To enable contact buttons for NEW & EDIT
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
                objCustomerUISupport.setContactAddEnableDisable(true, btnContactAdd);
                phoneDetailsBtnDefault();
                ClientUtil.enableDisable(panAddressDetails, true);
                ClientUtil.enableDisable(panPhoneAreaNumber, false);
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                objCustomerUISupport.setPhoneButtonEnableDisable(false, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
                objCustomerUISupport.setContactButtonEnableDisableDefault(false, btnContactNew, btnContactDelete, btnContactToMain);
                objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
            }
        } else {
            observable.setNewAddress(true);
            objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
            objCustomerUISupport.setContactAddEnableDisable(true, btnContactAdd);
            //observable.setCboAddressType((String) cboAddressType.getSelectedItem());
            updateOBFields();
            observable.resetAddressExceptAddTypeDetails();
            observable.resetPhoneListTable();
            observable.ttNotifyObservers();
        }
        //Added By Suresh 26-11-2012
        cboState.setSelectedItem(observable.getCbmState().getDataForKey("84"));
        cboCountry.setSelectedItem(observable.getCbmCountry().getDataForKey("IN"));
    }//GEN-LAST:event_cboAddressTypeActionPerformed

    private void tblContactListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContactListMousePressed
        // Add your handling code here:
        updateOBFields();
        btnContactAdd.setEnabled(true);        
        observable.populateAddress(tblContactList.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objCustomerUISupport.setContactButtonEnableDisableDefault(true, btnContactNew, btnContactDelete, btnContactToMain);
            phoneDetailsBtnDefault();
            ClientUtil.enableDisable(panAddressDetails, true);
            ClientUtil.enableDisable(panPhoneAreaNumber, false);
            btnContactDelete.setEnabled(true);
            btnContactToMain.setEnabled(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")) {
            
            introducerUI.enableIntroducerData(false);
            objCustomerUISupport.setPhoneButtonEnableDisable(false, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
            objCustomerUISupport.setContactButtonEnableDisableDefault(false, btnContactNew, btnContactDelete, btnContactToMain);
            objCustomerUISupport.setContactAddEnableDisable(false, btnContactAdd);
        }
}//GEN-LAST:event_tblContactListMousePressed

    private void btnContactToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactToMainActionPerformed
        // Add your handling code here:
        observable.setCommunicationAddress(tblContactList.getSelectedRow());
        objCustomerUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
}//GEN-LAST:event_btnContactToMainActionPerformed

    private void txtCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIDActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtCustomerIDActionPerformed

    private void txtCustomerIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIDFocusLost
        // TODO add your handling code here:
        if (txtCustomerID.getText().length() > 0) {
            HashMap fillMap = new HashMap();
            String CUSTID = txtCustomerID.getText();
            fillMap.put("CUSTOMER ID", txtCustomerID.getText());
            fillMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getSelectCustomerTOList", fillMap);
            if (lst != null && lst.size() > 0) {
                if (txtCustomerID.getText().equals(observable.getTxtCustomerID())) {
                    btnCancel.setEnabled(true);
                    btnEdit.setEnabled(true);
                    btnSave.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                    return;
                } else {
                    btnCancelActionPerformed(null);
                    txtCustomerID.setText(CUSTID);
                    actionType = "ViewDetails";
                    fillData(fillMap);
                    btnCancel.setEnabled(true);
                    btnEdit.setEnabled(true);
                    btnSave.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                    if (chkIncParticulars.isSelected()) {
                        tabIndCust.addTab("Income Details", panIncomeParticulars);
                    } else {
                        tabIndCust.remove(panIncomeParticulars);
                    }
                    if (chkLandDetails.isSelected()) {
                        tabIndCust.addTab("Land Details", panLandDetails);
                    } else {
                        tabIndCust.remove(panLandDetails);
                    }
                }
            } else {
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
            if (cboIdenProof.getSelectedItem().equals("") || cboIdenProof.getSelectedItem() == null) {
                ClientUtil.displayAlert("Enter Identity Proof Type");
                return;
            }
            else if (txtUniqueId.getText().length() == 0) {
                ClientUtil.displayAlert("Enter Identity Proof Unique Id");
                return;
            }else{  
            updateOBFields();
            observable.addProofMap(proofExist, proofRow);
            ClientUtil.clearAll(panProofDetails);
            ClientUtil.enableDisable(panProofDetails, false);
            }
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

    private void tdtretireDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtretireDtFocusLost
        // TODO add your handling code here:
        calculateRetirementAge();
    }//GEN-LAST:event_tdtretireDtFocusLost

    private void txtRetireAgeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRetireAgeFocusLost
        // TODO add your handling code here:
        calculateRetirementDate();
    }//GEN-LAST:event_txtRetireAgeFocusLost

    private void tabIndCustMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabIndCustMouseClicked
        // TODO add your handling code here:
        int index = tabIndCust.getSelectedIndex();
        if(index>2 && chkRegionalLang.isSelected()){
            updateRegPanDetails();
        }
    }//GEN-LAST:event_tabIndCustMouseClicked

    private void txtRegMaHnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaHnameKeyPressed
        // TODO add your handling code here:
        System.out.println("evt.getKeyCode()  :"+evt.getKeyCode());
        if(evt.getKeyCode()==115){
            System.out.println("gggg  :"+ txtRegMaHname.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaHname.getText(),txtRegMaHname.getCaretPosition(),txtRegHname.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaHname.setText(showObj.getFinalTxt());
        }
         if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaHname.getText(), txtRegMaHname.getCaretPosition(), txtRegHname.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaHnameKeyPressed

    private void txtRegMNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMNameKeyPressed
        if (evt.getKeyCode() == 115) {
            System.out.println("gggg 1 :" + txtRegMName.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMName.getText(), txtRegMName.getCaretPosition(), txtRegName.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMName.setText(showObj.getFinalTxt());
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMName.getText(), txtRegMName.getCaretPosition(), txtRegName.getText());
            showObj.show();
        }
        
    }//GEN-LAST:event_txtRegMNameKeyPressed

    private void txtRegMaPlaceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaPlaceKeyPressed
        if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaPlace.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaPlace.getText(),txtRegMaPlace.getCaretPosition(),txtRegPlace.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaPlace.setText(showObj.getFinalTxt());
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaPlace.getText(), txtRegMaPlace.getCaretPosition(), txtRegPlace.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaPlaceKeyPressed

    private void txtRegMavillageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMavillageKeyPressed
      if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMavillage.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMavillage.getText(),txtRegMavillage.getCaretPosition(),txtRegvillage.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMavillage.setText(showObj.getFinalTxt());
        }
      if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMavillage.getText(), txtRegMavillage.getCaretPosition(), txtRegvillage.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMavillageKeyPressed

    private void txtRegMaTalukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaTalukKeyPressed
      if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaTaluk.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaTaluk.getText(),txtRegMaTaluk.getCaretPosition(),txtRegTaluk.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaTaluk.setText(showObj.getFinalTxt());
        }
      if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaTaluk.getText(), txtRegMaTaluk.getCaretPosition(), txtRegTaluk.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaTalukKeyPressed

    private void txtRegMaCityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaCityKeyPressed
       if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaCity.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaCity.getText(),txtRegMaCity.getCaretPosition(),txtRegCity.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaCity.setText(showObj.getFinalTxt());
        }
       if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaCity.getText(), txtRegMaCity.getCaretPosition(), txtRegCity.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaCityKeyPressed

    private void txtRegMaStateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaStateKeyPressed
         if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaState.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaState.getText(),txtRegMaState.getCaretPosition(),txtRegState.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaState.setText(showObj.getFinalTxt());
        }
         if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaState.getText(), txtRegMaState.getCaretPosition(), txtRegState.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaStateKeyPressed

    private void txtRegMaCountryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaCountryKeyPressed
      if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaCountry.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaCountry.getText(),txtRegMaCountry.getCaretPosition(),txtRegCountry.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaCountry.setText(showObj.getFinalTxt());
        }
      if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaCountry.getText(), txtRegMaCountry.getCaretPosition(), txtRegCountry.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaCountryKeyPressed

    private void txtRegMaAmsamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaAmsamKeyPressed
       if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaAmsam.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaAmsam.getText(),txtRegMaAmsam.getCaretPosition(),txtRegAmsam.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaAmsam.setText(showObj.getFinalTxt());
        }
       if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaAmsam.getText(), txtRegMaAmsam.getCaretPosition(), txtRegAmsam.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaAmsamKeyPressed

    private void txtRegMaDesamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaDesamKeyPressed
      if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaDesam.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaDesam.getText(),txtRegMaDesam.getCaretPosition(),txtRegDesam.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaDesam.setText(showObj.getFinalTxt());
        }
      if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaDesam.getText(), txtRegMaDesam.getCaretPosition(), txtRegDesam.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaDesamKeyPressed

    private void txtRegNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegNameFocusLost
       txtRegMName.setText(getwordFromDict(txtRegName.getText()));
   
    }//GEN-LAST:event_txtRegNameFocusLost

    private void txtRegHnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegHnameFocusLost
      txtRegMaHname.setText(getwordFromDict(txtRegHname.getText()));
   
    }//GEN-LAST:event_txtRegHnameFocusLost

    private void txtRegPlaceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegPlaceFocusLost
      txtRegMaPlace.setText(getwordFromDict(txtRegPlace.getText()));
    
    }//GEN-LAST:event_txtRegPlaceFocusLost

    private void txtRegTalukFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegTalukFocusLost
       txtRegMaTaluk.setText(getwordFromDict(txtRegTaluk.getText()));
    
    }//GEN-LAST:event_txtRegTalukFocusLost

    private void txtRegCityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegCityFocusLost
      txtRegMaCity.setText(getwordFromDict(txtRegCity.getText()));
    }//GEN-LAST:event_txtRegCityFocusLost

    private void txtRegStateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegStateFocusLost
     txtRegMaState.setText(getwordFromDict(txtRegState.getText()));
   
    }//GEN-LAST:event_txtRegStateFocusLost

    private void txtRegCountryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegCountryFocusLost
      txtRegMaCountry.setText(getwordFromDict(txtRegCountry.getText()));
    
    }//GEN-LAST:event_txtRegCountryFocusLost

    private void txtRegAmsamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegAmsamFocusLost
     txtRegMaAmsam.setText(getwordFromDict(txtRegAmsam.getText()));
    }//GEN-LAST:event_txtRegAmsamFocusLost

    private void txtRegDesamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegDesamFocusLost
    txtRegMaDesam.setText(getwordFromDict(txtRegDesam.getText()));
    }//GEN-LAST:event_txtRegDesamFocusLost

    private void txtRegMaGardNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaGardNameKeyPressed
     if(evt.getKeyCode()==115){
            System.out.println("gggg 2 :"+ txtRegMaGardName.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer",txtRegMaGardName.getText(),txtRegMaGardName.getCaretPosition(),txtRegGardName.getText());
            showObj.show();
            if(showObj.getFinalTxt()!=null && showObj.getFinalTxt().length()>0)
            txtRegMaGardName.setText(showObj.getFinalTxt());
        }
      if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaGardName.getText(), txtRegMaGardName.getCaretPosition(), txtRegGardName.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaGardNameKeyPressed

    private void txtRegGardNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegGardNameFocusLost
        txtRegMaGardName.setText(getwordFromDict(txtRegGardName.getText()));
    }//GEN-LAST:event_txtRegGardNameFocusLost

    private void txtRegvillageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegvillageFocusLost
         txtRegMavillage.setText(getwordFromDict(txtRegvillage.getText()));
    }//GEN-LAST:event_txtRegvillageFocusLost

    private void chkRegionalLangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRegionalLangActionPerformed
       if (chkRegionalLang.isSelected()) {
           tabIndCust.add("Regional Language", panRegLanguage);
        } else {
            tabIndCust.remove(panRegLanguage);
            observable.setRegionalData(new HashMap());
        }
    }//GEN-LAST:event_chkRegionalLangActionPerformed

    private void cboPostOfficeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPostOfficeFocusLost
        if (cboPostOffice.getSelectedIndex() > 0) {
            txtPincode.setText(CommonUtil.convertObjToStr(observable.getCbmPostOffice().getKeyForSelected()));
        }else{
            txtPincode.setText("");
        }
    }//GEN-LAST:event_cboPostOfficeFocusLost

    private void txtFirstNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFirstNameKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtFirstName.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
       
    }//GEN-LAST:event_txtFirstNameKeyTyped

    private void txtMiddleNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMiddleNameKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtMiddleName.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtMiddleNameKeyTyped

    private void txtLastNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLastNameKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtLastName.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtLastNameKeyTyped

    private void txtNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtName.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtNameKeyTyped

    private void txtGuardianNameNOKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGuardianNameNOKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtGuardianNameNO.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtGuardianNameNOKeyTyped

    private void txtGStreetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGStreetKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtGStreet.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtGStreetKeyTyped

    private void txtGAreaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGAreaKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtGArea.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtGAreaKeyTyped

    private void txtIncNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIncNameKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIncNameKeyTyped

    private void txtStreetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStreetKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtStreet.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtStreetKeyTyped

    private void txtAreaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaKeyTyped
        // TODO add your handling code here:
        // Added by nithya on 19.02.2016 for 2969
        char keyChar = evt.getKeyChar();
        if(txtArea.getText().length() == 0){
          if (Character.isLowerCase(keyChar)) {
          evt.setKeyChar(Character.toUpperCase(keyChar));
         }
        } 
    }//GEN-LAST:event_txtAreaKeyTyped

    private void cboAgentCustIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboAgentCustIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAgentCustIdFocusLost

    private void cboAgentCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentCustIdActionPerformed
        // TODO add your handling code here:
        String agentId = "";
        if (cboAgentCustId.getSelectedItem() != null && !cboAgentCustId.getSelectedItem().equals("")) {
            agentId = ((ComboBoxModel) cboAgentCustId.getModel()).getKeyForSelected().toString();
            System.out.println("agentId^$^$^$^^^$^" + agentId);
            lblAgentCustIdValue.setText(CommonUtil.convertObjToStr(((ComboBoxModel) cboAgentCustId.getModel()).getKeyForSelected()));
            observable.setCboAgentCustId(agentId);
            observable.setLblAgentCustIdValue(lblAgentCustIdValue.getText());
        } else {
            lblAgentCustIdValue.setText("");
        }

    }//GEN-LAST:event_cboAgentCustIdActionPerformed

    private void cboPostOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPostOfficeActionPerformed
        // TODO add your handling code here:
//        int cboPostOfficeItemCount = cboPostOffice.getItemCount();  
//        System.out.println("cboPostOfficeItemCount :" +cboPostOfficeItemCount);
//        List<String> sortItemsList = new ArrayList() ;
//        for(int i=1; i<cboPostOfficeItemCount; i++){
//            String cboPostOfficeItem = (String)cboPostOffice.getItemAt(i);
//            sortItemsList.add(cboPostOfficeItem);
//        }
//        System.out.println("Before sorting :" + sortItemsList);
//        Collections.sort(sortItemsList);
//        cboPostOfficeItemsList = sortItemsList;
//        cboPostOffice.removeAllItems(); 
//         System.out.println("After sorting :" + sortItemsList);
//         cboPostOffice.addItem("");
//        for(int i=1 ; i<sortItemsList.size(); i++){
//         cboPostOffice.addItem(sortItemsList.get(i));
//        } 
//        sortItemsList.clear();
    }//GEN-LAST:event_cboPostOfficeActionPerformed

    private void cboPostOfficeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboPostOfficeMouseClicked

        
    }//GEN-LAST:event_cboPostOfficeMouseClicked

    private void cboPostOfficeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPostOfficeFocusGained
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_cboPostOfficeFocusGained

    private void txtCustUseridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustUseridActionPerformed
        
    }//GEN-LAST:event_txtCustUseridActionPerformed

    private void btnGenerateAppPwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateAppPwdActionPerformed
        // TODO add your handling code here:
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Are you sure want to generate OTP?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            String pwd = getRandomValue(6, 999999, '0');
            System.out.println("$#$#$# random pwd generated :" + pwd);
            txtCustPwd.setText(pwd);
            observable.setTxtCustPwd(pwd);
            txtCustPwd.setEnabled(false);
        }
    }//GEN-LAST:event_btnGenerateAppPwdActionPerformed

    private void lblProofPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblProofPhotoMouseClicked
        // TODO add your handling code here:
          if (evt.getClickCount() == 2) {
            objCustomerUISupport.zoomImage(lblProofPhoto);
          }
    }//GEN-LAST:event_lblProofPhotoMouseClicked

    private void btnProofPhotoLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProofPhotoLoadActionPerformed
        // TODO add your handling code here:
        observable.setScreen(getScreen());
        objCustomerUISupport.loadActivities(lblProofPhoto, btnProofPhotoRemove);
    }//GEN-LAST:event_btnProofPhotoLoadActionPerformed

    private void btnProofPhotoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProofPhotoRemoveActionPerformed
        // TODO add your handling code here:
        lblProofPhoto.setIcon(null);
        btnProofPhotoRemove.setEnabled(false);
        observable.setProofPhotoFile(null);
        observable.setProofPhotoByteArray(null);
    }//GEN-LAST:event_btnProofPhotoRemoveActionPerformed

    private void txtPhoneNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNumberKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != java.awt.event.KeyEvent.VK_SPACE)) {
            evt.consume();  // ignore event
        }
    }//GEN-LAST:event_txtPhoneNumberKeyTyped
 public String getRandomValue(int len, long multiplier, char paddingChar) {
        long l = (long) new Double(Math.random()*multiplier).longValue();
        String randomValue = String.format("%"+len+"s", String.valueOf(l)).replace(' ', paddingChar);
        return randomValue;
    }
    
    /**
     * To display customer list popup for Edit & Delete options
     */
    private void popUp(String actionType) {
        this.actionType = actionType;
        if (actionType != null) {
            final HashMap viewMap = new HashMap();
            HashMap wheres = new HashMap();
            wheres.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            if (actionType.equals(ClientConstants.ACTION_STATUS[2])) {
                ArrayList lst = new ArrayList();
                lst.add("CUSTOMER ID");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectCustomerTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
                viewMap.put(CommonConstants.MAP_WHERE, "CUST_TYPE = 'INDIVIDUAL'");
            } else if (actionType.equals(ClientConstants.ACTION_STATUS[3])) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectCustomerTOListDelete");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals("Staff")) {
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getStaffId");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            } else if (actionType.equals("ViewDetails")) {
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectCustomerTOList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            } else {
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "DeletedCustomerDetails");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap, true).show();
        }

    }

    /**
     * To get data based on customer id received from the popup and populate
     * into the screen
     */
    public void fillData(Object obj) {
        isFilled = true;
        setModified(true);
        final HashMap hash = (HashMap) obj;
        System.out.println("@@@@hash" + hash);
        // The following code block created by Rajesh
        // For enabling authorization functionality from AuthorizeListUI
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancel.setFocusable(false);
            btnAuthorize.setEnabled(true);
           
        }
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancel.setFocusable(false);
            btnAuthorize.setEnabled(true);           
        }
        
         if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnCancel.setFocusable(false);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
          
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnCancel.setFocusable(false);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
           
        }
        if (hash.containsKey("CUST_ID")) {
            hash.put("CUSTOMER ID", hash.get("CUST_ID"));
        }
        String st = CommonUtil.convertObjToStr(hash.get("STATUS"));
        if (st.equalsIgnoreCase("DELETED")) {
            flag = true;
        }
        HashMap map = new HashMap();
        CUSTOMERID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
//        hash.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
//        hash.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        map.put("CUST_ID", hash.get("CUSTOMER ID"));
        map.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
        map.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
//         if (actionType.equals("DeletedDetails"))
//         {
//           map.put("DELETECHECK","");
//         }
        if (viewType == AUTHORIZE) {
            map.put("AUTHORIZECHECK", "");
        }
        if (actionType.equals(ClientConstants.ACTION_STATUS[2])
                || actionType.equals(ClientConstants.ACTION_STATUS[3]) || actionType.equals("DeletedDetails") || actionType.equals("ViewDetails") || viewType == AUTHORIZE
                || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            observable.getData(map, introducerUI.getIntroducerOB());
            //        cboIntroType.setSelectedItem(observable.getCboIntroType());
            introducerUI.update(null, null);
//            objCustomerUISupport.fillPhotoSign(lblPhoto, lblSign, btnPhotoRemove, btnSignRemove);
            setButtonEnableDisable();
            observable.fillTbmCustomerHistory(txtCustomerID.getText());
            observable.fillTbmCustomerDepositHistory(txtCustomerID.getText());

            tblCustomerHistory.setModel(observable.getTbmCustomerHistory());
            enableDisablePanGaurdian();
            ClientUtil.enableDisable(panMISKYC, false);
            //For EDIT option enable disable fields and controls appropriately
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                List authList = ClientUtil.executeQuery("authStatus", hash);
                HashMap statusMap = new HashMap();
                statusMap = (HashMap) authList.get(0);
                enableDisable();
                txtCustPwd.setEnabled(false);
                btnGenerateAppPwd.setEnabled(true);
                if( observable.getMobileAppLoginStatus().equals("Y")){
                 txtCustUserid.setEnabled(false);
                 btnGenerateAppPwd.setEnabled(false);
                 txtCustPwd.setEnabled(false);
             }
                String status = CommonUtil.convertObjToStr(statusMap.get("AUTHORIZED_DT"));
                if (!status.equals("")) {
                    txtFirstName.setEnabled(true);
                    txtMiddleName.setEnabled(true);
                    txtLastName.setEnabled(true);
                } else {
                    txtFirstName.setEnabled(true);
                    txtMiddleName.setEnabled(true);
                    txtLastName.setEnabled(true);
                }
            }
            observable.setStatus();
            if (getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
                introducerUI.enableDisableBtn(false);
                introducerUI.enableIntroducerData(false);
                btnAuthorize.setVisible(false);
                btnReject.setVisible(false);
                btnException.setVisible(false);
                objCustomerUISupport.setLandDetailsButtonEnableDisable(false, btnLandNew, btnLandDelete, btnLandSave);
                objCustomerUISupport.setIncomeButtonEnableDisable(false, btnIncNew, btnIncDelete, btnIncSave);
            }
            if (!observable.getIsStaff()) {
                enableHelpBtn(false);
                txtStaffId.setEditable(false);
                txtStaffId.setEnabled(false);
                chkStaff.setSelected(false);
                chkStaff.setEnabled(true);
            } else {
                enableHelpBtn(true);
                txtStaffId.setEditable(true);
                txtStaffId.setEnabled(true);
                chkStaff.setSelected(true);
                chkStaff.setSelected(true);
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                objCustomerUISupport.setLandDetailsButtonEnableDisable(false, btnLandNew, btnLandDelete, btnLandSave);
                objCustomerUISupport.setIncomeButtonEnableDisable(false, btnIncNew, btnIncDelete, btnIncSave);
            }
            if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                btnReject.setEnabled(true);
                btnSave.setEnabled(false);
            }
        } else {
            txtStaffId.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_CODE")));
            cboCustomerType.setSelectedItem(((ComboBoxModel) cboCustomerType.getModel()).getDataForKey("STAFF"));
        }
        if (viewType == AUTHORIZE) {
            ClientUtil.enableDisable(panCustomer, false);
            enableHelpBtn(false);
            introducerUI.enableDisableBtn(false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            ClientUtil.enableDisable(this, false);
            objCustomerUISupport.setLandDetailsButtonEnableDisable(false, btnLandNew, btnLandDelete, btnLandSave);
            objCustomerUISupport.setIncomeButtonEnableDisable(false, btnIncNew, btnIncDelete, btnIncSave);
        }
       
        if (observable.isChkRegioalLang()) {
            chkRegionalLang.setSelected(true);
        } else {
            chkRegionalLang.setSelected(false);
        }
        if (chkRegionalLang.isSelected()) {
           tabIndCust.add("Regional Language", panRegLanguage);
        } else {
            tabIndCust.remove(panRegLanguage);
        }
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
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
        cboAddressType.setName("cboAddressType");
        cboAnnualIncomeLevel.setName("cboAnnualIncomeLevel");
        cboCareOf.setName("cboCareOf");
        cboCity.setName("cboCity");
        cboCountry.setName("cboCountry");
        cboCustStatus.setName("cboCustStatus");
        cboCustomerGroup.setName("cboCustomerGroup");
        cboCustomerType.setName("cboCustomerType");
        cboEducationalLevel.setName("cboEducationalLevel");
        cboMembershipClass.setName("cboMembershipClass");
        cboPhoneType.setName("cboPhoneType");
        cboPrefCommunication.setName("cboPrefCommunication");
        cboPrimaryOccupation.setName("cboPrimaryOccupation");


        cboProfession.setName("cboProfession");
        cboRelationManager.setName("cboRelationManager");
        cboResidentialStatus.setName("cboResidentialStatus");
        cboState.setName("cboState");
        cboTitle.setName("cboTitle");
        cboVehicle.setName("cboVehicle");
        lblAddressType.setName("lblAddressType");
        lblAnnualIncomeLevel.setName("lblAnnualIncomeLevel");
        lblArea.setName("lblArea");
        lblAreaCode.setName("lblAreaCode");
        lblCareOf.setName("lblCareOf");
        lblCity.setName("lblCity");
        lblCompany.setName("lblCompany");
        lblCountry.setName("lblCountry");
        lblCustPwd.setName("lblCustPwd");
        lblCustStatus.setName("lblCustStatus");
        lblCustUserid.setName("lblCustUserid");
        lblCustomerGroup.setName("lblCustomerGroup");
        lblCustomerID.setName("lblCustomerID");
//        lblCustomerStatus.setName("lblCustomerStatus");
        lblCustomerType.setName("lblCustomerType");
        lblDateOfBirth.setName("lblDateOfBirth");
        lblEducationalLevel.setName("lblEducationalLevel");
        lblEmailID.setName("lblEmailID");
        lblFirstName.setName("lblFirstName");
        lblGender.setName("lblGender");
        lblLanguage.setName("lblLanguage");
        lblLastName.setName("lblLastName");
        lblMaritalStatus.setName("lblMaritalStatus");
        lblMembershipClass.setName("lblMembershipClass");
        lblMiddleName.setName("lblMiddleName");
        lblMsg.setName("lblMsg");
        lblName.setName("lblName");
        lblNationality.setName("lblNationality");
//        lblNetWorth.setName("lblNetWorth");
        lblPhoneNumber.setName("lblPhoneNumber");
        lblPhoneType.setName("lblPhoneType");
        lblPhoto.setName("lblPhoto");
        lblPincode.setName("lblPincode");
        lblPrefCommunication.setName("lblPrefCommunication");
        lblPrimaryOccupation.setName("lblPrimaryOccupation");
        lblProfession.setName("lblProfession");
        lblRelationManager.setName("lblRelationManager");
        lblRemarks.setName("lblRemarks");
        lblResidentialStatus.setName("lblResidentialStatus");
        lblSign.setName("lblSign");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSsn.setName("lblSsn");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblStreet.setName("lblStreet");
      //  lblDesam.setName("lblDesam");
        lblVehicle.setName("lblVehicle");
        lblAddrRemarks.setName("lblAddrRemarks");
        lblAddrVerified.setName("lblAddrVerified");
        lblPhVerified.setName("lblPhVerified");
        lblFinanceStmtVerified.setName("lblFinanceStmtVerified");
        mbrCustomer.setName("mbrCustomer");
        panAdditionalInfo.setName("panAdditionalInfo");
        panAdditionalInformation.setName("panAdditionalInformation");
        panAddress.setName("panAddress");
        panAddressDetails.setName("panAddressDetails");
        panAddressType.setName("panAddressType");
        panCareOf.setName("panCareOf");
        panCity.setName("panCity");
        panContactAndIdentityInfo.setName("panContactAndIdentityInfo");
        panContactControl.setName("panContactControl");
        panContactInfo.setName("panContactInfo");
        panContactNo.setName("panContactNo");
        panContacts.setName("panContacts");
        panContactsList.setName("panContactsList");
        panCountry.setName("panCountry");
        panCountryDetails.setName("panCountryDetails");
        panCustomer.setName("panCustomer");
        panCustomerInfo.setName("panCustomerInfo");
        panDOB.setName("panDOB");
        panGender.setName("panGender");
        panMaritalStatus.setName("panMaritalStatus");
        panPersonalInfo.setName("panPersonalInfo");
        panPost.setName("panPost"); // Added by nithya
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
        panAddrRemarks.setName("panAddrRemarks");
        panMISKYC.setName("panMISKYC");
        panKYC.setName("panKYC");
        rdoGender_Female.setName("rdoGender_Female");
        rdoGender_Male.setName("rdoGender_Male");
        rdoMaritalStatus_Married.setName("rdoMaritalStatus_Married");
        rdoMaritalStatus_Single.setName("rdoMaritalStatus_Single");
        rdoMaritalStatus_Widow.setName("rdoMaritalStatus_Widow");
        srpContactList.setName("srpContactList");
        srpPhoneList.setName("srpPhoneList");
        srpPhotoLoad.setName("srpPhotoLoad");
        srpSignLoad.setName("srpSignLoad");
        tabContactAndIdentityInfo.setName("tabContactAndIdentityInfo");
        tblContactList.setName("tblContactList");
        tblPhoneList.setName("tblPhoneList");
        tdtDateOfBirth.setName("tdtDateOfBirth");
        txtArea.setName("txtArea");
        txtAreaCode.setName("txtAreaCode");
        txtCompany.setName("txtCompany");
        txtCustPwd.setName("txtCustPwd");
        txtCustUserid.setName("txtCustUserid");
        txtCustomerID.setName("txtCustomerID");
        txtEmailID.setName("txtEmailID");
        txtFirstName.setName("txtFirstName");
        txtLanguage.setName("txtLanguage");
        txtLastName.setName("txtLastName");
        txtMiddleName.setName("txtMiddleName");
        txtName.setName("txtName");
        txtNationality.setName("txtNationality");
//        txtNetWorth.setName("txtNetWorth");
        txtPhoneNumber.setName("txtPhoneNumber");
        txtPincode.setName("txtPincode");
        txtRemarks.setName("txtRemarks");
        txtAddrRemarks.setName("txtAddrRemarks");
        txtSsn.setName("txtSsn");
        txtStreet.setName("txtStreet");
        txtTransPwd.setName("txtTransPwd");
        lblCrAvldSince.setName("lblCrAvldSince");
        tdtCrAvldSince.setName("tdtCrAvldSince");
        lblRiskRate.setName("lblRiskRate");
        txtRiskRate.setName("txtRiskRate");
        cboIntroType.setName("cboIntroType");
        chkAddrVerified.setName("chkAddrVerified");
        chkPhVerified.setName("chkPhVerified");
        chkFinanceStmtVerified.setName("chkFinanceStmtVerified");
        lblGuardianNameNO.setName("lblGuardianNameNO");
        lblRelationNO.setName("lblRelationNO");
        lblGuardianPhoneNO.setName("lblGuardianPhoneNO");
        lblGStreet.setName("lblGStreet");
        lblGArea.setName("lblGArea");
        lblGCountry.setName("lblGCountry");
        lblGState.setName("lblGState");
        lblGCity.setName("lblGCity");
        lblGPinCode.setName("lblGPinCode");
        lblBranchId.setName("lblBranchId");
        lblCreatedDt.setName("lblCreatedDt");
        lblCreatedDt1.setName("lblCreatedDt1");
//        lblDealingWith.setName("lblDealingWith");
        lblStaffId.setName("lblStaffId");
        txtBranchId.setName("txtBranchId");
        txtGuardianNameNO.setName("txtGuardianNameNO");
        cboRelationNO.setName("cboRelationNO");
        txtGuardianACodeNO.setName("txtGuardianACodeNO");
        txtGuardianPhoneNO.setName("txtGuardianPhoneNO");
        txtGStreet.setName("txtGStreet");
        txtGArea.setName("txtGArea");
        cboGCountry.setName("cboGCountry");
        cboGCity.setName("cboGCity");
        cboGState.setName("cboGState");
        txtGPinCode.setName("txtGPinCode");
        lblSentThanksLetter.setName("lblSentThanksLetter");
        chkSentThanksLetter.setName("chkSentThanksLetter");
        lblConfirmationfromIntroducer.setName("lblConfirmationfromIntroducer");
        chkConfirmationfromIntroducer.setName("chkConfirmationfromIntroducer");
        txtStaffId.setName("txtStaffId");
        btnStaffId.setName("btnStaffId");
        txtDesignation.setName("txtDesignation");
        lblDesignation.setName("lblDesignation");
        txtPanNumber.setName("txtPanNumber");
        cboCaste.setName("cboCaste");
        cboSubCaste.setName("cboSubCaste");
        cboReligion.setName("cboReligion");
        lblCaste.setName("lblCaste");
        lblReligion.setName("lblReligion");
//        lblPanNumber.setName("lblPanNumber");
        btnDeletedDetails.setName("btnDeleteDetails");
        lblAddrProof.setName("lblAddrProof");
        cboAddrProof.setName("cboAddrProof");
        lblIdenProof.setName("lblIdenProof");
        cboIdenProof.setName("cboIdenProof");
        lblFirstNamePass.setName("lblFirstNamePass");
        lblMiddleNamePass.setName("lblMiddleNamePass");
        lblLastNamePass.setName("lblLastNamePass");
        lblPassIssueAuth.setName("lblPassIssueAuth");
        lblPassPlaceIssued.setName("lblPassPlaceIssued");
        lblPassPortNo.setName("lblPassPortNo");
        lblIssuedDt.setName("lblIssuedDt");
        lblValidUpto.setName("lblValidUpto");
        txtPassportFirstName.setName("txtPassportFirstName");
        txtPassportMiddleName.setName("txtPassportMiddleName");
        tdtPassportIssueDt.setName("tdtPassportIssueDt");
        tdtPassportValidUpto.setName("tdtPassportValidUpto");
        txtPassportLastName.setName("txtPassportLastName");
        cboPassportTitle.setName("cboPassportTitle");
        txtPassportNo.setName("txtPassportNo");
        txtPassportIssueAuth.setName("txtPassportIssueAuth");
        cboPassportIssuePlace.setName("cboPassportIssuePlace");
        panPassPortDetails.setName("panPassPortDetails");
        txtKartha.setName("txtKartha");
        cboFarClass.setName("cboFarClass");
        lblKartha.setName("lblKartha");
        cboFarClass.setName("cboFarClass");
        txtAge.setName("txtAge");
        lblAge.setName("lblAge");
        lblIncomeParticulars.setName("lblIncomeParticulars");
        chkIncParticulars.setName("chkIncParticulars");
        lblIncName.setName("lblIncName");
        lblIncIncome.setName("lblIncIncome");
        lblIncRelation.setName("lblIncRelation");
        txtIncIncome.setName("txtIncIncome");
        txtIncName.setName("txtIncName");
        cboIncPack.setName("cboIncPack");
        cboIncRelation.setName("cboIncRelation");
        tblIncParticulars.setName("tblIncParticulars");
        srpIncParticulars.setName("srpIncParticulars");
        panIncomeParticulars.setName("panIncomeParticulars");
        btnIncNew.setName("btnIncNew");
        btnIncSave.setName("btnIncSave");
        btnIncDelete.setName("btnIncDelete");
        chkLandDetails.setName("chkLandDetails");
        lblLandDetails.setName("lblLandDetails");
        panLandDetails.setName("panLandDetails");
        txtLoc.setName("txtLoc");
        cboTaluk.setName("cboTaluk");
        txtSrNo.setName("txtSrNo");
        txtAreaInAcrs.setName("txtAreaInAcrs");
        cboIrrigated.setName("cboIrrigated");
        cboSrIrrigation.setName("cboSrIrrigation");
        txtVillage.setName("txtVillage");
        txtPo.setName("txtPo");
        txtHobli.setName("txtHobli");
        lblLocation.setName("lblLocation");
        lblSurNo.setName("lblSurNo");
        lblAreaInAcrs.setName("lblAreaInAcrs");
        lblIrrigated.setName("lblIrrigated");
        lblSrcIrr.setName("lblSrcIrr");
        lblVillage.setName("lblVillage");
        lblPost.setName("lblPost");
        lblHobli.setName("lblHobli");
        cboDistrict.setName("cboDistrict");
        cboLandDetState.setName("cboLandDetState");
        txtLandDetPincode.setName("txtLandDetPincode");
        panLandInfoDetails.setName("panLandInfoDetails");
        panLandInfoTable.setName("panLandInfoTable");
        lblGuardianDoB.setName("lblGuardianDoB");
        tdtGuardianDOB.setName("tdtGuardianDOB");
        txtGuardianAge.setName("txtGuardianAge");
        lblGuardianAge.setName("lblGuardianAge");
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
        txtBankruptsy.setName("txtBankruptsy");
        lblBankruptsy.setName("lblBankruptsy");
        btnClearPassport.setName("btnClearPassport");
        txtMemNum.setText("txtMemNum");
        lblMemNum.setText("lblMemNum");
        rdoITDec_Pan.setName("rdoITDec_Pan");
        rdoITDec_F60.setName("rdoITDec_F60");
        rdoITDec_F61.setName("rdoITDec_F61");
        panITDetails.setName("panITDetails");
//        txtUid.setName("txtUid");
//        lblUid.setName("lblUid");
        lblWardNo.setName("lblWardNo");
        txtWardNo.setName("txtWardNo");
//        lblRationCardNo.setName("lblRationCardNo");
//        txtRationCardNo.setName("txtRationCardNo");
        cboIncDetProfession.setName("cboIncDetProfession");
        txtIncDetCompany.setName("txtIncDetCompany");
        cbcomboDesam.setName("cbcomboDesam");
        lblDivision.setName("lblDivision");
        cboDivision.setName("cboDivision");
        cboWardName.setName("cboWardName"); // Added by nithya
        lblProofPhoto.setName("lblProofPhoto");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        setSwitchEnglish(!isSwitchEnglish());
        java.util.Locale currentLocale = null;
//        if (isSwitchEnglish()) {
//        currentLocale = new java.util.Locale("en", "US");
//        } else {
            currentLocale = new java.util.Locale(TrueTransactMain.language, TrueTransactMain.country);
//        }
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.CustomerRB", currentLocale);
        observable.refreshTableColumns(resourceBundle);
        objCustomerUISupport.setObjCustomerRB(resourceBundle);
//        changeTableColumns(tblContactList, resourceBundle);
        lblEducationalLevel.setText(resourceBundle.getString("lblEducationalLevel"));
        lblSign.setText(resourceBundle.getString("lblSign"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSearch.setText(resourceBundle.getString("btnSearch"));
        lblAddressType.setText(resourceBundle.getString("lblAddressType"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        ((javax.swing.border.TitledBorder) panSign.getBorder()).setTitle(resourceBundle.getString("panSign"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblPincode.setText(resourceBundle.getString("lblPincode"));
        btnContactNew.setText(resourceBundle.getString("btnContactNew"));
        rdoGender_Male.setText(resourceBundle.getString("rdoGender_Male"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblCustomerID.setText(resourceBundle.getString("lblCustomerID"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPhotoRemove.setText(resourceBundle.getString("btnPhotoRemove"));
        lblMiddleName.setText(resourceBundle.getString("lblMiddleName"));
        lblAreaCode.setText(resourceBundle.getString("lblAreaCode"));
        btnSignRemove.setText(resourceBundle.getString("btnSignRemove"));
        lblName.setText(resourceBundle.getString("lblName"));
        lblMaritalStatus.setText(resourceBundle.getString("lblMaritalStatus"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
//        ((javax.swing.border.TitledBorder) panContactInfo.getBorder()).setTitle(resourceBundle.getString("panContactInfo"));
        btnSignLoad.setText(resourceBundle.getString("btnSignLoad"));
        rdoMaritalStatus_Married.setText(resourceBundle.getString("rdoMaritalStatus_Married"));
        lblPrimaryOccupation.setText(resourceBundle.getString("lblPrimaryOccupation"));
        lblCustomerGroup.setText(resourceBundle.getString("lblCustomerGroup"));
        btnContactDelete.setText(resourceBundle.getString("btnContactDelete"));
        lblCareOf.setText(resourceBundle.getString("lblCareOf"));
        lblGender.setText(resourceBundle.getString("lblGender"));
//        lblNetWorth.setText(resourceBundle.getString("lblNetWorth"));
        lblRelationManager.setText(resourceBundle.getString("lblRelationManager"));
        lblLastName.setText(resourceBundle.getString("lblLastName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        ((javax.swing.border.TitledBorder) panCustomer.getBorder()).setTitle(resourceBundle.getString("panCustomer"));
        btnPhotoLoad.setText(resourceBundle.getString("btnPhotoLoad"));
        ((javax.swing.border.TitledBorder) panTelecomDetails.getBorder()).setTitle(resourceBundle.getString("panTelecomDetails"));
        btnContactAdd.setText(resourceBundle.getString("btnContactAdd"));
        lblDateOfBirth.setText(resourceBundle.getString("lblDateOfBirth"));
        lblPhoneNumber.setText(resourceBundle.getString("lblPhoneNumber"));
        ((javax.swing.border.TitledBorder) panContacts.getBorder()).setTitle(resourceBundle.getString("panContacts"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblVehicle.setText(resourceBundle.getString("lblVehicle"));
        lblLanguage.setText(resourceBundle.getString("lblLanguage"));
        lblCustUserid.setText(resourceBundle.getString("lblCustUserid"));
        lblPrefCommunication.setText(resourceBundle.getString("lblPrefCommunication"));
        lblAnnualIncomeLevel.setText(resourceBundle.getString("lblAnnualIncomeLevel"));
        btnException.setText(resourceBundle.getString("btnException"));
        rdoGender_Female.setText(resourceBundle.getString("rdoGender_Female"));
        lblEmailID.setText(resourceBundle.getString("lblEmailID"));
        lblCustStatus.setText(resourceBundle.getString("lblCustStatus"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblNationality.setText(resourceBundle.getString("lblNationality"));
        btnPhoneDelete.setText(resourceBundle.getString("btnPhoneDelete"));
        ((javax.swing.border.TitledBorder) panPersonalInfo.getBorder()).setTitle(resourceBundle.getString("panPersonalInfo"));
        lblCustomerType.setText(resourceBundle.getString("lblCustomerType"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
//        lblCustomerStatus.setText(resourceBundle.getString("lblCustomerStatus"));
        lblSsn.setText(resourceBundle.getString("lblSsn"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblProfession.setText(resourceBundle.getString("lblProfession"));
        btnContactNoAdd.setText(resourceBundle.getString("btnContactNoAdd"));
        lblPhoneType.setText(resourceBundle.getString("lblPhoneType"));
        lblPhoto.setText(resourceBundle.getString("lblPhoto"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        rdoMaritalStatus_Single.setText(resourceBundle.getString("rdoMaritalStatus_Single"));
        rdoMaritalStatus_Widow.setText(resourceBundle.getString("rdoMaritalStatus_Widow"));
        lblResidentialStatus.setText(resourceBundle.getString("lblResidentialStatus"));
        lblCompany.setText(resourceBundle.getString("lblCompany"));
        lblMembershipClass.setText(resourceBundle.getString("lblMembershipClass"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnContactToMain.setText(resourceBundle.getString("btnContactToMain"));
        lblCustPwd.setText(resourceBundle.getString("lblCustPwd"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPhoneNew.setText(resourceBundle.getString("btnPhoneNew"));
        lblState.setText(resourceBundle.getString("lblState"));
        ((javax.swing.border.TitledBorder) panPhoto.getBorder()).setTitle(resourceBundle.getString("panPhoto"));
        //  lblDesam.setText(resourceBundle.getString("lblDesam"));
        lblFirstName.setText(resourceBundle.getString("lblFirstName"));
        lblCrAvldSince.setText(resourceBundle.getString("lblCrAvldSince"));
        lblRiskRate.setText(resourceBundle.getString("lblRiskRate"));
        lblAddrRemarks.setText(resourceBundle.getString("lblAddrRemarks"));
        lblAddrVerified.setText(resourceBundle.getString("lblAddrVerified"));
        lblFinanceStmtVerified.setText(resourceBundle.getString("lblFinanceStmtVerified"));
        lblGuardianNameNO.setText(resourceBundle.getString("lblGuardianNameNO"));
        lblRelationNO.setText(resourceBundle.getString("lblRelationNO"));
        lblGuardianPhoneNO.setText(resourceBundle.getString("lblGuardianPhoneNO"));
        lblGStreet.setText(resourceBundle.getString("lblGStreet"));
        lblGArea.setText(resourceBundle.getString("lblGArea"));
        lblGCountry.setText(resourceBundle.getString("lblGCountry"));
        lblGState.setText(resourceBundle.getString("lblGState"));
        lblGCity.setText(resourceBundle.getString("lblGCity"));
        lblGPinCode.setText(resourceBundle.getString("lblGPinCode"));
        lblBranchId.setText(resourceBundle.getString("lblBranchId"));
        lblCreatedDt.setText(resourceBundle.getString("lblCreatedDt"));
        lblCreatedDt1.setText(resourceBundle.getString("lblCreatedDt1"));
//        lblDealingWith.setText(resourceBundle.getString("lblDealingWith"));
        lblStaffId.setText(resourceBundle.getString("lblStaffId"));
        lblConfirmationfromIntroducer.setText(resourceBundle.getString("lblConfirmationfromIntroducer"));
        lblSentThanksLetter.setText(resourceBundle.getString("lblSentThanksLetter"));
        btnStaffId.setText(resourceBundle.getString("btnStaffId"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        lblCaste.setText(resourceBundle.getString("lblCaste"));
        lblReligion.setText(resourceBundle.getString("lblReligion"));
//        lblPanNumber.setText(resourceBundle.getString("lblPanNumber"));
        lblAddrProof.setText(resourceBundle.getString("lblAddrProof"));
//        lblIdenProof.setText(resourceBundle.getString("lblIdenProof"));
        lblFirstNamePass.setText(resourceBundle.getString("lblFirstNamePass"));
        lblMiddleNamePass.setText(resourceBundle.getString("lblMiddleNamePass"));
        lblLastNamePass.setText(resourceBundle.getString("lblLastNamePass"));
        lblPassIssueAuth.setText(resourceBundle.getString("lblPassIssueAuth"));
        lblPassPlaceIssued.setText(resourceBundle.getString("lblPassPlaceIssued"));
        lblPassPortNo.setText(resourceBundle.getString("lblPassPortNo"));
        lblIssuedDt.setText(resourceBundle.getString("lblIssuedDt"));
        lblValidUpto.setText(resourceBundle.getString("lblValidUpto"));
        lblKartha.setText(resourceBundle.getString("lblKartha"));
        lblFarClass.setText(resourceBundle.getString("lblFarClass"));
        lblIncomeParticulars.setText(resourceBundle.getString("lblIncomeParticulars"));
        lblIncRelation.setText(resourceBundle.getString("lblIncRelation"));
        lblIncIncome.setText(resourceBundle.getString("lblIncIncome"));
        lblIncName.setText(resourceBundle.getString("lblIncName"));
        btnIncNew.setText(resourceBundle.getString("btnPhoneNew"));
        btnIncSave.setText(resourceBundle.getString("btnIncSave"));
        btnIncDelete.setText(resourceBundle.getString("btnIncDelete"));
        lblLandDetails.setText(resourceBundle.getString("lblLandDetails"));
        lblLocation.setText(resourceBundle.getString("lblLocation"));
        lblSurNo.setText(resourceBundle.getString("lblSurNo"));
        lblAreaInAcrs.setText(resourceBundle.getString("lblAreaInAcrs"));
        lblIrrigated.setText(resourceBundle.getString("lblIrrigated"));
        lblSrcIrr.setText(resourceBundle.getString("lblSrcIrr"));
        lblVillage.setText(resourceBundle.getString("lblVillage"));
        lblPost.setText(resourceBundle.getString("lblPost"));
        lblHobli.setText(resourceBundle.getString("lblHobli"));
        lblTaluk.setText(resourceBundle.getString("lblTaluk"));
        lblDistrict.setText(resourceBundle.getString("lblDistrict"));
        lbLandlState.setText(resourceBundle.getString("lbLandlState"));
        lblLandPin.setText(resourceBundle.getString("lblLandPin"));
        lblGuardianDoB.setText(resourceBundle.getString("lblGuardianDoB"));
        lblSuspendCust.setText(resourceBundle.getString("lblSuspendCust"));
        lblSuspendDate.setText(resourceBundle.getString("lblSuspendDate"));
        lblSuspendRemarks.setText(resourceBundle.getString("lblSuspendRemarks"));
        lblRevokeDate.setText(resourceBundle.getString("lblRevokeDate"));
        lblRevokeCust.setText(resourceBundle.getString("lblRevokeCust"));
        btnClearPassport.setText(resourceBundle.getString("btnClearPassport"));
        lblMemNum.setText(resourceBundle.getString("lblMemNum"));
        rdoITDec_Pan.setText(resourceBundle.getString("rdoITDec_Pan"));
        rdoITDec_F60.setText(resourceBundle.getString("rdoITDec_F60"));
        rdoITDec_F61.setText(resourceBundle.getString("rdoITDec_F61"));
        //commented by rishad 23/02/2015
        // lblUid.setText(resourceBundle.getString("lblUid"));
        lblWardNo.setText(resourceBundle.getString("lblWardNo"));
        //commented by rishad 23/02/2015
        //  lblRationCardNo.setText(resourceBundle.getString("lblRationCardNo"));
        //added by rishad 06/08/2016 for taking label name from properties File
        lblIdenProof.setText(resourceBundle.getString("lblIdenProof"));
        lblUniqueNo.setText(resourceBundle.getString("lblUniqueNo"));
        lblPhVerified.setText(resourceBundle.getString("lblPhVerified"));
        lblDesam.setText(resourceBundle.getString("lblDesam"));
        lblAmsam.setText(resourceBundle.getString("lblAmsam"));
        lblJoiningDate.setText(resourceBundle.getString("lblJoiningDate"));
        lblChkMinor.setText(resourceBundle.getString("lblChkMinor"));
//                panProof.setText(resourceBundle.getString("panProof"));
        lblChkMinority.setText(resourceBundle.getString("lblChkMinority"));
        lblSubCaste.setText(resourceBundle.getString("lblSubCaste"));
        lblAge.setText(resourceBundle.getString("lblAge"));
        lblRetireDt.setText(resourceBundle.getString("lblRetireDt"));
        lblRetireAge.setText(resourceBundle.getString("lblRetireAge"));
        lblDivision.setText(resourceBundle.getString("lblDivision"));
        lblAgentCustId.setText(resourceBundle.getString("lblAgentCustId"));
        lblIntroType.setText(resourceBundle.getString("lblIntroType"));
        lblRegionalLang.setText(resourceBundle.getString("lblRegionalLang"));
        lblGuardianAge.setText(resourceBundle.getString("lblGuardianAge"));
        lblCustVillage.setText(resourceBundle.getString("lblCustVillage"));
        lblCustTaluk.setText(resourceBundle.getString("lblCustTaluk"));
        lblPostOffice.setText(resourceBundle.getString("lblPostOffice"));
        lblWardName.setText(resourceBundle.getString("lblWardName"));
        lblBankruptsy.setText(resourceBundle.getString("lblBankruptsy"));
        lblCustTaluk.setText(resourceBundle.getString("lblCustTaluk"));
        lblTransPwd.setText(resourceBundle.getString("lblTransPwd"));
        lblGuardianAge.setText(resourceBundle.getString("lblGuardianAge"));
        lblRegCountry.setText(resourceBundle.getString("lblRegCountry"));
        lblRegState.setText(resourceBundle.getString("lblRegState"));
        lblRegAmsam.setText(resourceBundle.getString("lblRegAmsam"));
        lblRegDesam.setText(resourceBundle.getString("lblRegDesam"));
        lblRegGardName.setText(resourceBundle.getString("lblRegGardName"));
        lblRegCity.setText(resourceBundle.getString("lblRegCity"));
        lblRegVillage.setText(resourceBundle.getString("lblRegVillage"));
        lblRegPlace.setText(resourceBundle.getString("lblRegPlace"));
        lblRegHname.setText(resourceBundle.getString("lblRegHname"));
        lblRegName.setText(resourceBundle.getString("lblRegName"));
        lblRegTaluk.setText(resourceBundle.getString("lblRegTaluk"));
        lblAgentCustIdValue.setText(resourceBundle.getString("lblAgentCustIdValue"));
        cLabel1.setText(resourceBundle.getString("cLabel1"));
        lblDealingWith.setText(resourceBundle.getString("lblDealingWith"));
        lblClosAcdetail.setText(resourceBundle.getString("lblClosAcdetail"));
        ((javax.swing.border.TitledBorder) panProof.getBorder()).setTitle(resourceBundle.getString("panProof"));
        //  panSuspendCustomer.setText(resourceBundle.getString("panSuspendCustomer"));
        ((javax.swing.border.TitledBorder) panSuspendCustomer.getBorder()).setTitle(resourceBundle.getString("panSuspendCustomer"));
        ((javax.swing.border.TitledBorder) panPassPortDetails.getBorder()).setTitle(resourceBundle.getString("panPassPortDetails"));
        ((javax.swing.border.TitledBorder) panCompanyInfo.getBorder()).setTitle(resourceBundle.getString("panCompanyInfo"));
        ((javax.swing.border.TitledBorder) panAdditionalInformation.getBorder()).setTitle(resourceBundle.getString("panAdditionalInformation"));
        ((javax.swing.border.TitledBorder) panPhotoSign.getBorder()).setTitle(resourceBundle.getString("panPhotoSign"));
        ((javax.swing.border.TitledBorder) panContactInfo.getBorder()).setTitle(resourceBundle.getString("panContactInfo"));
         ((javax.swing.border.TitledBorder) panRegLanguage.getBorder()).setTitle(resourceBundle.getString("panRegLanguage"));
         ((javax.swing.border.TitledBorder) panLandDetails.getBorder()).setTitle(resourceBundle.getString("panLandDetails"));
         ((javax.swing.border.TitledBorder) panIncomeParticulars.getBorder()).setTitle(resourceBundle.getString("panIncomeParticulars"));
         ((javax.swing.border.TitledBorder) panGuardianDetails.getBorder()).setTitle(resourceBundle.getString("panGuardianDetails"));
       
        ClientUtil.setLanguage(this, resourceBundle.getLocale());
    }

//    private void changeTableColumns(com.see.truetransact.uicomponent.CTable tbl, ResourceBundle rb) {
//        if (tbl!=null && tbl.getModel() instanceof com.see.truetransact.clientutil.EnhancedTableModel) {
//            com.see.truetransact.clientutil.EnhancedTableModel model = (com.see.truetransact.clientutil.EnhancedTableModel)tbl.getModel();
//            
//            for (int i=0; i<tbl.getColumnCount(); i++) {
//                int viewColumn = tbl.convertColumnIndexToView(i);
//                javax.swing.table.TableColumn column = tbl.getColumnModel().getColumn(viewColumn);
//                column.setHeaderValue(rb.getString(model.getColumnName(i)));
//                
//            }
//            tbl.getTableHeader().repaint();
//        }
//    }
    public void update(Observable observed, Object arg) {
        updateAddress();
        updatePhone();
        updateCusotmerData();
        updateGaurdianData();
        updateRegPanDetails();
        lblStatus.setText(observable.getLblStatus());
        try {
//            if (observable.getPhotoFile() != null && !observable.getPhotoFile().startsWith(".") && observable.getLblPhoto() != null && observable.getLblPhoto().length() > 0){
//                lblPhoto.setIcon(new javax.swing.ImageIcon(new URL(observable.getLblPhoto())));
//            }
//            if (observable.getSignFile() != null && !observable.getSignFile().startsWith(".") && observable.getLblSign() != null && observable.getLblSign().length() > 0){
//                lblSign.setIcon(new javax.swing.ImageIcon(new URL(observable.getLblSign())));
//            }
            if (observable.getPhotoByteArray() != null) //                lblPhoto.setIcon(new javax.swing.ImageIcon(observable.getPhotoFile()));
            {
                lblPhoto.setIcon(new javax.swing.ImageIcon(observable.getPhotoByteArray()));
            }
            if (observable.getSignByteArray() != null) //                lblSign.setIcon(new javax.swing.ImageIcon(observable.getSignFile()));
            {
                lblSign.setIcon(new javax.swing.ImageIcon(observable.getSignByteArray()));
            }
            if (observable.getProofPhotoByteArray() != null)   {
                lblProofPhoto.setIcon(new javax.swing.ImageIcon(observable.getProofPhotoByteArray()));
            }
        } catch (Exception e) {

            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    /**
     * Method to populate Garudian Details *
     */
    private void updateGaurdianData() {
        cboRelationNO.setSelectedItem(observable.getCboRelationNO());
        txtGuardianNameNO.setText(observable.getTxtGuardianNameNO());
        txtGuardianACodeNO.setText(observable.getTxtGuardianACodeNO());
        txtGuardianPhoneNO.setText(observable.getTxtGuardianPhoneNO());
        txtGStreet.setText(observable.getTxtGStreet());
        txtGArea.setText(observable.getTxtGArea());
        cboGCountry.setSelectedItem(observable.getCboGCountry());
        cboGState.setSelectedItem(observable.getCboGState());
        cboGCity.setSelectedItem(observable.getCboGCity());
        txtGPinCode.setText(observable.getTxtGPinCode());
        tdtGuardianDOB.setDateValue(observable.getTdtGuardianDOB());
        txtGuardianAge.setText(observable.getTxtGuardianAge());
    }

    private void updateAddress() {
//        cboAddressType.setSelectedItem(observable.getCboAddressType());
        updateAddressExceptAddType();
    }

    private void updateCusotmerData() {
        cbcomboAmsam.setSelectedItem(observable.getCbcomboAmsam());
        cbcomboDesam.setSelectedItem(observable.getCbcomboDesam());
        rdoGender_Male.setSelected(observable.getRdoGender_Male());
        rdoGender_Female.setSelected(observable.getRdoGender_Female());
        rdoMaritalStatus_Single.setSelected(observable.getRdoMaritalStatus_Single());
        rdoMaritalStatus_Married.setSelected(observable.getRdoMaritalStatus_Married());
        rdoMaritalStatus_Widow.setSelected(observable.getRdoMaritalStatus_Widow());
        txtStaffId.setText(observable.getTxtStaffId());
        chkAddrVerified.setSelected(observable.getChkAddrVerified());
        chkPhVerified.setSelected(observable.getChkPhVerified());
        chkFinanceStmtVerified.setSelected(observable.getChkFinanceStmtVerified());
        chkSentThanksLetter.setSelected(observable.getChkSentThanksLetter());
        chkConfirmationfromIntroducer.setSelected(observable.getChkConfirmationfromIntroducer());
        txtBranchId.setText(observable.getTxtBranchId());
        lblCreatedDt1.setText(observable.getLblCreatedDt1());
        lblDealingPeriod.setText(observable.getLblDealingPeriod());
        txtDesignation.setText(observable.getTxtDesignation());
        cboCareOf.setSelectedItem(observable.getCboCareOf());
        txtName.setText(observable.getTxtName());
        cboMembershipClass.setSelectedItem(observable.getCboMembershipClass());
        cboCustStatus.setSelectedItem(observable.getCboCustStatus());
//        txtNetWorth.setText(observable.getTxtNetWorth());
        txtFirstName.setText(observable.getTxtFirstName());
        txtMiddleName.setText(observable.getTxtMiddleName());
        txtLastName.setText(observable.getTxtLastName());
        txtNationality.setText(observable.getTxtNationality());
        txtLanguage.setText(observable.getTxtLanguage());
        tdtDateOfBirth.setDateValue(observable.getTdtDateOfBirth());
        tdtretireDt.setDateValue(observable.getTdtRetireDt());
        txtCustomerID.setText(observable.getTxtCustomerID());
        txtRemarks.setText(observable.getTxtRemarks());
        tdtJoiningDate.setDateValue(observable.getTdtjoiningDate());
//        cboResidentialStatus.setSelectedItem(observable.getCboResidentialStatus());
        cboCustomerType.setSelectedItem(observable.getCboCustomerType());
        cboRelationManager.setSelectedItem(observable.getCboRelationManager());
        txtCompany.setText(observable.getTxtCompany());
        cboAnnualIncomeLevel.setSelectedItem(observable.getCboAnnualIncomeLevel());
        cboPrefCommunication.setSelectedItem(observable.getCboPrefCommunication());
        cboCustomerGroup.setSelectedItem(observable.getCboCustomerGroup());
        cboVehicle.setSelectedItem(observable.getCboVehicle());
        cboProfession.setSelectedItem(observable.getCboProfession());
        cboPrimaryOccupation.setSelectedItem(observable.getCboPrimaryOccupation());
        cboEducationalLevel.setSelectedItem(observable.getCboEducationalLevel());
        txtEmailID.setText(observable.getTxtEmailID());
//        cboIntroType.setSelectedItem(observable.getCboIntroType());
        tblContactList.setModel(observable.getTblContactList());
        tblProofList.setModel(observable.getTblProofList());
        txtSsn.setText(observable.getTxtSsn());
        txtTransPwd.setText(observable.getTxtTransPwd());
        txtCustUserid.setText(observable.getTxtCustUserid());
        txtCustPwd.setText(observable.getTxtCustPwd());
        tdtCrAvldSince.setDateValue(observable.getTdtCrAvldSince());
        txtRiskRate.setText(observable.getTxtRiskRate());
//        lblCustomerStatus.setText(observable.getLblCustomerStatus());
        cboSubCaste.setSelectedItem(observable.getCboCaste());
        cboCaste.setSelectedItem(observable.getCboCaste());
        chkMinority.setSelected(observable.isIsMinority());
//        changed by nikhil
        cboReligion.setSelectedItem(observable.getCboReligion());
        txtPanNumber.setText(observable.getTxtPanNumber());
        chkMinor.setSelected(observable.isChkMinor());
        cboAddrProof.setSelectedItem(observable.getCboAddrProof());
//        cboIdenProof.setSelectedItem(observable.getCboIdenProof());
//        cboCustomerType.setSelectedItem(observable.getCustTypeId());
        txtPassportFirstName.setText(observable.getTxtPassportFirstName());
        txtPassportMiddleName.setText(observable.getTxtPassportMiddleName());
        txtPassportLastName.setText(observable.getTxtPassportLastName());
        tdtPassportIssueDt.setDateValue(observable.getTdtPassportIssueDt());
        tdtPassportValidUpto.setDateValue(observable.getTdtPassportValidUpto());
        txtPassportNo.setText(observable.getTxtPassportNo());
        cboPassportIssuePlace.setSelectedItem(observable.getCboPassportIssuePlace());
        cboPassportTitle.setSelectedItem(observable.getCboPassportTitle());
        txtPassportIssueAuth.setText(observable.getTxtPassportIssueAuth());
        txtKartha.setText(observable.getTxtKartha());
        cboFarClass.setSelectedItem(observable.getCboFarClass());
        txtAge.setText(observable.getTxtAge());
        txtRetireAge.setText(observable.getTxtRetireAge());
        txtIncIncome.setText(observable.getTxtIncAmount());
        txtIncName.setText(observable.getTxtIncName());
        cboIncRelation.setSelectedItem(observable.getCboIncRelation());
        cboIncPack.setSelectedItem(observable.getCboIncPack());
        tblIncParticulars.setModel(observable.getTblIncomeParticulars());
        chkIncParticulars.setSelected(observable.isChkIncomeDetails());
        tblCustomerLandDetails.setModel(observable.getTblCustomerLandDetails());
        txtLoc.setText(observable.getTxtLocation());
        txtSrNo.setText(observable.getTxtSurNo());
        txtAreaInAcrs.setText(observable.getTxtLandDetArea());
        cboIrrigated.setSelectedItem(observable.getCboIrrigated());
        cboSrIrrigation.setSelectedItem(observable.getCboSourceIrrigated());
        txtVillage.setText(observable.getTxtVillage());
        txtHobli.setText(observable.getTxtHobli());
        txtPo.setText(observable.getTxtPost());
        cboLandDetState.setSelectedItem(observable.getCboLandDetState());
        cboDistrict.setSelectedItem(observable.getCboDistrict());
        cboTaluk.setSelectedItem(observable.getCboTaluk());
        txtLandDetPincode.setText(observable.getTxtLandDetPin());
        chkLandDetails.setSelected(observable.isChkLandDetails());
        chkSuspendCust.setSelected(observable.isChksuspendedBy());
        chkRevokeCust.setSelected(observable.isChkrevokedBy());
        tdtSuspendCustFrom.setDateValue(observable.getSuspendedDate());
        tdtRevokedCustDate.setDateValue(observable.getRevokedDate());
        txtSuspRevRemarks.setText(observable.getSusRevRemarks());
        cboTitle.setSelectedItem(observable.getCboTitle());
        txtBankruptsy.setText(observable.getBankruptcy());
        txtMemNum.setText(observable.getMemberShipNo());
        rdoITDec_Pan.setSelected(observable.isRdoITDec_pan());
        rdoITDec_F60.setSelected(observable.isRdoITDec_F60());
        rdoITDec_F61.setSelected(observable.isRdoITDec_F61());
          //commented by rishad 23/02/2015
        //txtUid.setText(observable.getTxtuid());
        txtWardNo.setText(observable.getTxtWardNo());
          //commented by rishad 23/02/2015
       // txtRationCardNo.setText(observable.getTxtRationCardNo());
        cboIncDetProfession.setSelectedItem(observable.getCboIncProfession());
        txtIncDetCompany.setText(observable.getIncCompany());
        
        if (observable.getCboDivision() != null) {
            cboDivision.setSelectedItem(observable.getCboDivision());
        }
        if (observable.getCboAgentCustId() != null) {
            cboAgentCustId.setSelectedItem(observable.getCboAgentCustId());
        }
        if (observable.getIsStaff()) {
            cboCustomerType.setEnabled(false);
            cboCustomerType.setSelectedItem(observable.getCbmCustomerType().getDataForKey("STAFF"));
            btnStaffId.setEnabled(true);
            txtStaffId.setEnabled(true);
            txtStaffId.setEditable(true);
        } else {
            cboCustomerType.setEnabled(true);
        }
    }
    public void updateRegPanDetails() {
        System.out.println("observable.getRegionalData()    LL :"+observable.getRegionalData());
        if (observable.getRegionalData() != null && observable.getRegionalData().size() > 0) {
            txtRegHname.setText(CommonUtil.convertObjToStr(observable.getRegionalData().get("HNAME")));
            txtRegPlace.setText(CommonUtil.convertObjToStr(observable.getRegionalData().get("PLACE")));
            txtRegvillage.setText(CommonUtil.convertObjToStr(observable.getRegionalData().get("VILLAGE")));
            txtRegTaluk.setText(CommonUtil.convertObjToStr(observable.getRegionalData().get("TALUK")));
            txtRegCity.setText(CommonUtil.convertObjToStr(observable.getRegionalData().get("CITY")));
            txtRegCountry.setText(CommonUtil.convertObjToStr(observable.getRegionalData().get("COUNTRY")));
            txtRegState.setText(CommonUtil.convertObjToStr(observable.getRegionalData().get("STATE")));
        } else {
            txtRegHname.setText(txtStreet.getText());
            txtRegPlace.setText(txtArea.getText());
            txtRegvillage.setText(CommonUtil.convertObjToStr(cboCustVillage.getSelectedItem()));
            txtRegTaluk.setText(CommonUtil.convertObjToStr(cboCustTaluk.getSelectedItem()));
            txtRegCity.setText(CommonUtil.convertObjToStr(cboCity.getSelectedItem()));
            txtRegCountry.setText(CommonUtil.convertObjToStr(cboCountry.getSelectedItem()));
            txtRegState.setText(CommonUtil.convertObjToStr(cboState.getSelectedItem()));
        }
        String name = txtFirstName.getText() + " " + txtMiddleName.getText() + " " + txtLastName.getText();
        txtRegName.setText(name.trim());
        txtRegAmsam.setText(CommonUtil.convertObjToStr(cbcomboAmsam.getSelectedItem()));
        txtRegDesam.setText(CommonUtil.convertObjToStr(cbcomboDesam.getSelectedItem()));
        txtRegGardName.setText(txtName.getText());

        if(observable.getTxtRegMName() != null && observable.getTxtRegMName() != "" ){
            txtRegMName.setText(observable.getTxtRegMName());   
            System.out.println("not taking from dictionary");
        }else{
            txtRegMName.setText(getwordFromDict(txtRegName.getText()));
            System.out.println("taking from dictionary");
        }
        if(observable.getTxtRegMaHname() != null && observable.getTxtRegMaHname() != ""){
            txtRegMaHname.setText(observable.getTxtRegMaHname());
        }else{
            txtRegMaHname.setText(getwordFromDict(txtRegHname.getText())); 
        }
        if(observable.getTxtRegMaPlace() != null && observable.getTxtRegMaPlace() != ""){
            txtRegMaPlace.setText(observable.getTxtRegMaPlace());
        }else{
            txtRegMaPlace.setText(getwordFromDict(txtRegPlace.getText()));
        }
        if(observable.getTxtRegMavillage() != null && observable.getTxtRegMavillage() != ""){
            txtRegMavillage.setText(observable.getTxtRegMavillage());
        }else{
            txtRegMavillage.setText(getwordFromDict(txtRegvillage.getText()));
        }
        if(observable.getTxtRegMaTaluk() != null && observable.getTxtRegMaTaluk() != ""){
            txtRegMaTaluk.setText(observable.getTxtRegMaTaluk());
        }else{
            txtRegMaTaluk.setText(getwordFromDict(txtRegTaluk.getText()));
        }
        if(observable.getTxtRegMaCity() != null && observable.getTxtRegMaCity() != ""){
            txtRegMaCity.setText(observable.getTxtRegMaCity());
        }else{
            txtRegMaCity.setText(getwordFromDict(txtRegCity.getText()));
        }
        if(observable.getTxtRegMaCountry() != null && observable.getTxtRegMaCountry() != ""){
            txtRegMaCountry.setText(observable.getTxtRegMaCountry());
        }else{
            txtRegMaCountry.setText(getwordFromDict(txtRegCountry.getText()));
        }
        if(observable.getTxtRegMaState() != null && observable.getTxtRegMaState() != ""){
            txtRegMaState.setText(observable.getTxtRegMaState());
        }else{
            txtRegMaState.setText(getwordFromDict(txtRegState.getText()));
        }
        if(observable.getTxtRegMaAmsam() != null && observable.getTxtRegMaAmsam() != ""){
            txtRegMaAmsam.setText(observable.getTxtRegMaAmsam());
        }else{
            txtRegMaAmsam.setText(getwordFromDict(txtRegAmsam.getText()));
        }
        if(observable.getTxtRegMaDesam() != null && observable.getTxtRegMaDesam() != ""){
            txtRegMaDesam.setText(observable.getTxtRegMaDesam());
        }else{
            txtRegMaDesam.setText(getwordFromDict(txtRegDesam.getText()));
        }
        if(observable.getTxtRegMaGardName() != null && observable.getTxtRegMaGardName() != ""){
            txtRegMaGardName.setText(observable.getTxtRegMaGardName());
        }else{
            txtRegMaGardName.setText(getwordFromDict(txtRegGardName.getText()));
        }
        
      
//        txtRegMName.setText(getwordFromDict(txtRegName.getText())); done
//        txtRegMaHname.setText(getwordFromDict(txtRegHname.getText())); done
//        txtRegMaPlace.setText(getwordFromDict(txtRegPlace.getText())); done
//        txtRegMavillage.setText(getwordFromDict(txtRegvillage.getText())); // done
//        txtRegMaTaluk.setText(getwordFromDict(txtRegTaluk.getText())); // done
//        txtRegMaCity.setText(getwordFromDict(txtRegCity.getText())); DONE
//        txtRegMaCountry.setText(getwordFromDict(txtRegCountry.getText())); done
//        txtRegMaState.setText(getwordFromDict(txtRegState.getText())); done
//        txtRegMaAmsam.setText(getwordFromDict(txtRegAmsam.getText())); done
//        txtRegMaDesam.setText(getwordFromDict(txtRegDesam.getText())); done
//        txtRegMaGardName.setText(getwordFromDict(txtRegGardName.getText()));

    }

    public String getwordFromDict(String word) {
        String malWord = "";
        if (word != null && word.length() > 0) {
            String arr[] = word.split(" ");
            //for (int i = 0; i < arr.length; i++) {
                HashMap whereMap = new HashMap();
                whereMap.put("ENG_WORD", word.toUpperCase());
                List lst = (List) ClientUtil.executeQuery("getMalayalamWord", whereMap);
                if (lst != null && lst.size() > 0) {
                    HashMap one = (HashMap) lst.get(0);
                    if (one != null && one.containsKey("M")) {
                        String mWord = CommonUtil.convertObjToStr(one.get("M"));
                        malWord += " " + mWord;
                    } else {
                        malWord += " " + EngToMalTransliterator.get_ml(word);
                    }

                } else {
                    malWord += " " + EngToMalTransliterator.get_ml(word);
                }
            //}
        }
        return malWord.trim();
    }
    private void updateAddressExceptAddType() {
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        txtPincode.setText(observable.getTxtPincode());
        txtStreet.setText(observable.getTxtStreet());
        cboCountry.setSelectedItem(observable.getCboCountry());
        cboCustVillage.setSelectedItem(observable.getCboCustVillage());
        cboCustTaluk.setSelectedItem(observable.getCboCustTaluk());
        txtArea.setText(observable.getTxtArea());
        txtAddrRemarks.setText(observable.getTxtAddrRemarks());
        cboPostOffice.setSelectedItem(observable.getCboPostOffice());
        cboWardName.setSelectedItem(observable.getCboWardName()); // Added by nithya
        updatePhone();
    }

    private void updatePhone() {
        tblPhoneList.setModel(observable.getTblPhoneList());
        txtPhoneNumber.setText(observable.getTxtPhoneNumber());
        txtAreaCode.setText(observable.getTxtAreaCode());
        cboPhoneType.setSelectedItem(observable.getCboPhoneType());
    }
     private void updateProof()
     {
     //tblProofList.setModel(observable.getTblProofList());
     txtUniqueId.setText(observable.getTxtUniqueId());
     }
    private void removeButtons() {
        rdoGender.remove(rdoGender_Male);
        rdoGender.remove(rdoGender_Female);

        rdoMaritalStatus.remove(rdoMaritalStatus_Single);
        rdoMaritalStatus.remove(rdoMaritalStatus_Married);
        rdoMaritalStatus.remove(rdoMaritalStatus_Widow);

        rdoITDec.remove(rdoITDec_F60);
        rdoITDec.remove(rdoITDec_F61);
        rdoITDec.remove(rdoITDec_Pan);
    }

    private void addButtons() {
        rdoGender.add(rdoGender_Male);
        rdoGender.add(rdoGender_Female);

        rdoMaritalStatus.add(rdoMaritalStatus_Single);
        rdoMaritalStatus.add(rdoMaritalStatus_Married);
        rdoMaritalStatus.add(rdoMaritalStatus_Widow);
        rdoITDec.add(rdoITDec_F60);
        rdoITDec.add(rdoITDec_F61);
        rdoITDec.add(rdoITDec_Pan);
    }

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
        // Added by nithya
        if(cboWardName.getSelectedItem() != null){
            observable.setCboWardName((String)((ComboBoxModel) cboWardName.getModel()).getKeyForSelected());
            System.out.println("Ward Name :"+observable.getCboWardName());
        }
        observable.setCboIntroType((String) cboIntroType.getSelectedItem());
        observable.setTxtFirstName(txtFirstName.getText());
        observable.setTxtMiddleName(txtMiddleName.getText());
        observable.setTxtLastName(txtLastName.getText());
        observable.setRdoGender_Male(rdoGender_Male.isSelected());
        observable.setRdoGender_Female(rdoGender_Female.isSelected());
        observable.setRdoMaritalStatus_Single(rdoMaritalStatus_Single.isSelected());
        observable.setRdoMaritalStatus_Married(rdoMaritalStatus_Married.isSelected());
        observable.setRdoMaritalStatus_Widow(rdoMaritalStatus_Widow.isSelected());
        observable.setTxtNationality(txtNationality.getText());
        observable.setTxtLanguage(txtLanguage.getText());
        observable.setCboTitle((String) cboTitle.getSelectedItem());
        observable.setTxtCustomerID(txtCustomerID.getText());
        observable.setCboResidentialStatus((String) cboResidentialStatus.getSelectedItem());
        observable.setTdtDateOfBirth(tdtDateOfBirth.getDateValue());
        observable.setTdtRetireDt(tdtretireDt.getDateValue());
        observable.setCboCareOf((String) cboCareOf.getSelectedItem());
        observable.setTxtName(txtName.getText());
        observable.setCboVehicle((String) cboVehicle.getSelectedItem());
        observable.setCboCustomerType((String) cboCustomerType.getSelectedItem());
        observable.setCboRelationManager((String) cboRelationManager.getSelectedItem());
        observable.setTxtStaffId(txtStaffId.getText());
        observable.setTxtDesignation(txtDesignation.getText());
        observable.setTxtCompany(txtCompany.getText());
        observable.setCboAnnualIncomeLevel((String) cboAnnualIncomeLevel.getSelectedItem());
        observable.setCboPrefCommunication((String) cboPrefCommunication.getSelectedItem());
        observable.setCboCustomerGroup((String) cboCustomerGroup.getSelectedItem());
        observable.setCboProfession((String) cboProfession.getSelectedItem());
        observable.setCboPrimaryOccupation((String) cboPrimaryOccupation.getSelectedItem());
        observable.setCboEducationalLevel((String) cboEducationalLevel.getSelectedItem());
        observable.setTxtEmailID(txtEmailID.getText());
        observable.setTdtjoiningDate(tdtJoiningDate.getDateValue());

        //  observable.setTdt
//        observable.setTxtNetWorth(txtNetWorth.getText());
        observable.setCboMembershipClass((String) cboMembershipClass.getSelectedItem());
        observable.setCboCustStatus((String) cboCustStatus.getSelectedItem());
        observable.setTxtPanNumber(txtPanNumber.getText());
        observable.setCboCaste(CommonUtil.convertObjToStr(cboCaste.getSelectedItem()));
        observable.setCboSubCaste(CommonUtil.convertObjToStr(cboSubCaste.getSelectedItem()));
//        changed by nikhil
        observable.setCboReligion((String) cboReligion.getSelectedItem());
        observable.setChkSentThanksLetter(chkSentThanksLetter.isSelected());
        observable.setChkConfirmationfromIntroducer(chkConfirmationfromIntroducer.isSelected());
        observable.setTxtStreet(txtStreet.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setCboAddressType((String) cboAddressType.getSelectedItem());
        observable.setCboCity((String) cboCity.getSelectedItem());
        observable.setCboState((String) cboState.getSelectedItem());
        observable.setCboPostOffice(CommonUtil.convertObjToStr(cboPostOffice.getSelectedItem()));
        observable.setTxtPincode(txtPincode.getText());
        observable.setCboCountry((String) cboCountry.getSelectedItem());
        observable.setTxtPhoneNumber(txtPhoneNumber.getText());
        observable.setTxtAreaCode(txtAreaCode.getText());
        observable.setTxtAddrRemarks(txtAddrRemarks.getText());
        observable.setChkMinor(chkMinor.isSelected());
        observable.setIsMinority(chkMinority.isSelected());
        ///added by jithin
        observable.setCboCustTaluk((String) cboCustTaluk.getSelectedItem());
        observable.setCboCustVillage((String) cboCustVillage.getSelectedItem());



        ///

        //		observable.setCboPhoneType((String) cboPhoneType.getSelectedItem());
//        observable.setPhotoFile(lblPhoto.getIcon().toString().getBytes());
//        observable.setSignFile(lblSign.getIcon().toString().getBytes());
//        observable.setPhotoFile(observable.getPhotoByteArray());
//        observable.setSignFile(observable.getSignByteArray());
        if (cboPhoneType.getSelectedItem() != null) {
            observable.setCboPhoneType((String) ((ComboBoxModel) cboPhoneType.getModel()).getKeyForSelected());
        }
        observable.setTxtSsn(txtSsn.getText());
        observable.setTxtTransPwd(txtTransPwd.getText());
        observable.setTxtCustUserid(txtCustUserid.getText());
        observable.setTxtCustPwd(txtCustPwd.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtRiskRate(txtRiskRate.getText());
        observable.setTdtCrAvldSince(tdtCrAvldSince.getDateValue());
        observable.setChkAddrVerified(chkAddrVerified.isSelected());
        observable.setChkMinor(chkMinor.isSelected());
        observable.setChkFinanceStmtVerified(chkFinanceStmtVerified.isSelected());
        observable.setChkPhVerified(chkPhVerified.isSelected());

        /**
         * Gaurdian Details *
         */
        observable.setCboRelationNO((String) cboRelationNO.getSelectedItem());
        observable.setTxtGuardianNameNO(txtGuardianNameNO.getText());
        observable.setTxtGuardianACodeNO(txtGuardianACodeNO.getText());
        observable.setTxtGuardianPhoneNO(txtGuardianPhoneNO.getText());
        observable.setTxtGStreet(txtGStreet.getText());
        observable.setTxtGArea(txtGArea.getText());
        observable.setCboGCountry((String) cboGCountry.getSelectedItem());
        observable.setCboGState((String) cboGState.getSelectedItem());
        observable.setCboGCity((String) cboGCity.getSelectedItem());
        observable.setTxtGPinCode(txtGPinCode.getText());
        /**
         * Setting up the Selected BranchId *
         */
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setCboAddrProof((String) cboAddrProof.getSelectedItem());
        observable.setTxtUniqueId(txtUniqueId.getText());
       

        observable.setTxtPassportFirstName(txtPassportFirstName.getText());
        observable.setTxtPassportMiddleName(txtPassportMiddleName.getText());
        observable.setTxtPassportLastName(txtPassportLastName.getText());
        observable.setTxtPassportIssueAuth(txtPassportIssueAuth.getText());
        observable.setTxtPassportNo(txtPassportNo.getText());
        observable.setCboPassportIssuePlace((String) cboPassportIssuePlace.getSelectedItem());
        observable.setCboPassportTitle((String) cboPassportTitle.getSelectedItem());
        observable.setTdtPassportIssueDt(tdtPassportIssueDt.getDateValue());
        observable.setTdtPassportValidUpto(tdtPassportValidUpto.getDateValue());
        observable.setTxtKartha(txtKartha.getText());
        observable.setTxtAge(txtAge.getText());
        observable.setTxtRetireAge(txtRetireAge.getText());
        observable.setCboFarClass((String) cboFarClass.getSelectedItem());
        observable.setTxtIncAmount(txtIncIncome.getText());
        observable.setTxtIncName(txtIncName.getText());
        observable.setCboIncPack((String) cboIncPack.getSelectedItem());
        observable.setCboIncRelation((String) cboIncRelation.getSelectedItem());
        observable.setTxtLocation(txtLoc.getText());
        observable.setTxtSurNo(txtSrNo.getText());
        observable.setTxtLandDetArea(txtAreaInAcrs.getText());
        observable.setCboIrrigated((String) cboIrrigated.getSelectedItem());
        observable.setCboSourceIrrigated((String) cboSrIrrigation.getSelectedItem());
        observable.setTxtVillage(txtVillage.getText());
        observable.setTxtPost(txtPo.getText());
        observable.setTxtHobli(txtHobli.getText());
        observable.setCboDistrict((String) cboDistrict.getSelectedItem());
        observable.setCboTaluk((String) cboTaluk.getSelectedItem());
        observable.setCboLandDetState((String) cboLandDetState.getSelectedItem());
        observable.setTxtLandDetPin(txtLandDetPincode.getText());
        observable.setChkLandDetails(chkLandDetails.isSelected());
        observable.setChkIncomeDetails(chkIncParticulars.isSelected());
        observable.setTdtGuardianDOB(tdtGuardianDOB.getDateValue());
        observable.setTxtGuardianAge(txtGuardianAge.getText());
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
        observable.setTxtWardNo(txtWardNo.getText());
        observable.setCboIncProfession((String) cboIncDetProfession.getSelectedItem());
        observable.setIncCompany(txtIncDetCompany.getText());
                
        //if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
        
        // Commented and Added new statement by nithya on 30-06-2016 for 4774
        //observable.setCboDivision(CommonUtil.convertObjToStr(cboDivision.getSelectedItem()));
        observable.setCboDivision(CommonUtil.convertObjToStr(((ComboBoxModel) cboDivision.getModel()).getKeyForSelected()));
        // End
        observable.setCboAgentCustId(CommonUtil.convertObjToStr(cboAgentCustId.getSelectedItem()));
        System.out.println("^#^#^#^#^#^#^#"+observable.getCboDivision());
        //}
        //else{
        //observable.setCboDivision("");
       // }
        if(chkRegionalLang.isSelected()){
         observable.setTxtRegMName(txtRegMName.getText());
         observable.setTxtRegMaHname(txtRegMaHname.getText());
         observable.setTxtRegMaPlace(txtRegMaPlace.getText());
         observable.setTxtRegMavillage(txtRegMavillage.getText());
         observable.setTxtRegMaTaluk(txtRegMaTaluk.getText());
         observable.setTxtRegMaCity(txtRegMaCity.getText());
         observable.setTxtRegMaCountry(txtRegMaCountry.getText());
         observable.setTxtRegMaState(txtRegMaState.getText());
         observable.setTxtRegMaAmsam(txtRegMaAmsam.getText());
         observable.setTxtRegMaDesam(txtRegMaDesam.getText());
         observable.setTxtRegMaGardName(txtRegMaGardName.getText());
        }
        
        observable.setChkRegioalLang(chkRegionalLang.isSelected());
//         observable.setCboIdenProof(CommonUtil.convertObjToStr(cboIdenProof.getSelectedItem()));
//         observable.setTxtUniqueId(txtUniqueId.getText());
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        txtFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFirstName"));
        txtMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiddleName"));
        txtLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastName"));
        rdoGender_Male.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoGender_Male"));
        rdoMaritalStatus_Single.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMaritalStatus_Single"));
        txtNationality.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNationality"));
        txtLanguage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLanguage"));
        cboTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTitle"));
        txtCustomerID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerID"));
        cboResidentialStatus.setHelpMessage(lblMsg, objMandatoryRB.getString("cboResidentialStatus"));
        tdtDateOfBirth.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfBirth"));
        cboCareOf.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCareOf"));
        txtName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtName"));
        cboVehicle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboVehicle"));
        cboCustomerType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustomerType"));
        cboRelationManager.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRelationManager"));
        txtCompany.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCompany"));
        cboAnnualIncomeLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAnnualIncomeLevel"));
        cboPrefCommunication.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPrefCommunication"));
        cboCustomerGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustomerGroup"));
        cboProfession.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProfession"));
        cboPrimaryOccupation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPrimaryOccupation"));
        cboEducationalLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEducationalLevel"));
        txtEmailID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmailID"));
//        txtNetWorth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetWorth"));
        cboMembershipClass.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMembershipClass"));
        cboCustStatus.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustStatus"));
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
        txtSsn.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSsn"));
        txtTransPwd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransPwd"));
        txtCustUserid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustUserid"));
        txtCustPwd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustPwd"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtAddrRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddrRemarks"));
        chkAddrVerified.setHelpMessage(lblMsg, objMandatoryRB.getString("chkAddrVerified"));
        chkPhVerified.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPhVerified"));
        chkFinanceStmtVerified.setHelpMessage(lblMsg, objMandatoryRB.getString("chkFinanceStmtVerified"));
        txtRiskRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRiskRate"));
        tdtCrAvldSince.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCrAvldSince"));
        txtBranchId.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCrAvldSince"));
        txtGuardianNameNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianNameNO"));
        cboRelationNO.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRelationNO"));
        txtGuardianACodeNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianACodeNO"));
        txtGuardianPhoneNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianPhoneNO"));
        txtGStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGStreet"));
        txtGArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGArea"));
        cboGCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGCountry"));
        cboGCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGCity"));
        cboGState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGState"));
        txtGPinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGPinCode"));
        chkSentThanksLetter.setHelpMessage(lblMsg, objMandatoryRB.getString("chkSentThanksLetter"));
        chkConfirmationfromIntroducer.setHelpMessage(lblMsg, objMandatoryRB.getString("chkConfirmationfromIntroducer"));
        txtStaffId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStaffId"));
        txtDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesignation"));
        cboCaste.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCaste"));
        cboSubCaste.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubCaste"));
        txtPanNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPanNumber"));
        cboAddrProof.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAddrProof"));
//        cboIdenProof.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIdenProof"));

        txtPassportFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportFirstName"));
        txtPassportMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportMiddleName"));
        tdtPassportIssueDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPassportIssueDt"));
        tdtPassportValidUpto.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPassportValidUpto"));
        txtPassportLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportLastName"));
        cboPassportTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPassportTitle"));
        txtPassportNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportNo"));
        txtPassportIssueAuth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportIssueAuth"));
        cboPassportIssuePlace.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPassportIssuePlace"));
        txtKartha.setHelpMessage(lblMsg, objMandatoryRB.getString("txtKartha"));
        cboFarClass.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFarClass"));
        txtAge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAge"));
        chkIncParticulars.setHelpMessage(lblMsg, objMandatoryRB.getString("chkIncParticulars"));
        txtIncIncome.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIncIncome"));
        txtIncName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIncName"));
        cboIncPack.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIncPack"));
        cboIncRelation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIncRelation"));
        chkLandDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("chkLandDetails"));
        txtLoc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoc"));
        txtSrNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSrNo"));
        txtAreaInAcrs.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAreaInAcrs"));
        cboIrrigated.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIrrigated"));
        cboSrIrrigation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSrIrrigation"));
        txtVillage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtVillage"));
        txtPo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPo"));
        txtHobli.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHobli"));
        cboDistrict.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDistrict"));
        cboTaluk.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTaluk"));
        cboLandDetState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLandDetState"));
        txtLandDetPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLandDetPincode"));
        txtGuardianAge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianAge"));
        rdoITDec_Pan.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoITDec_Pan"));
        cbcomboDesam.setHelpMessage(lblMsg, objMandatoryRB.getString("cbcomboDesam"));        
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
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
        btnDeletedDetails.setEnabled(!btnDeletedDetails.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        txtBranchId.setEnabled(false);
    }

    private void colWidthChange() {
//        System.out.println("!!!! resourceBundle.getLocale().getLanguage() :"+resourceBundle.getLocale().getLanguage());
//        System.out.println("!!!! resourceBundle.getLocale().getCountry() :"+resourceBundle.getLocale().getCountry());
        tblPhoneList.getColumn(observable.getObjCustomerRB().getString("tblPhoneColumn1")).setPreferredWidth(40);
    }

    public static void main(java.lang.String[] args) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        IndividualCustUI individualCustUI = new IndividualCustUI();
        frm.getContentPane().add(individualCustUI);
        individualCustUI.show();
        frm.setSize(800, 650);
        frm.show();
    }

    public void loanCreationCustId(GoldLoanUI objGoldLoanUI) {
        goldLoanUI = objGoldLoanUI;
         ScreenName="GoldLoan";
        btnNewActionPerformed(null);
        txtBranchId.setText(TrueTransactMain.BRANCH_ID);
        observable.setTxtBranchId(TrueTransactMain.BRANCH_ID);
        observable.setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        updateOBFields();
        loanCustMap = new HashMap();
        loanCustMap.put("LOAN_CUSTOMER_ID", "LOAN_CUSTOMER_ID");
          observable.setLoanCustMap(loanCustMap);
    }

    public void setGoldLoanUI(GoldLoanUI objGoldLoanUI) {
        goldLoanUI = objGoldLoanUI;
    }

    public void loanCreationCustId(AccountsUI objAccountsUI) {
        accountsUI = objAccountsUI;
         ScreenName="Account";
        btnNewActionPerformed(null);
        txtBranchId.setText(TrueTransactMain.BRANCH_ID);
        observable.setTxtBranchId(TrueTransactMain.BRANCH_ID);
        observable.setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        updateOBFields();
        loanCustMap = new HashMap();
        loanCustMap.put("LOAN_CUSTOMER_ID", "LOAN_CUSTOMER_ID");
        observable.setLoanCustMap(loanCustMap);
    }

    public void setAccountsUI(AccountsUI objAccountsUI) {
        accountsUI = objAccountsUI;
    }

    public void loanCreationCustId(TermDepositUI objTermDepositUI) {
        termdepositUI = objTermDepositUI;
        ScreenName="TermDeposit";
        btnNewActionPerformed(null);
        txtBranchId.setText(TrueTransactMain.BRANCH_ID);
        observable.setTxtBranchId(TrueTransactMain.BRANCH_ID);
        observable.setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        updateOBFields();
        loanCustMap = new HashMap();
        loanCustMap.put("LOAN_CUSTOMER_ID", "LOAN_CUSTOMER_ID");
        observable.setLoanCustMap(loanCustMap);
    }
    
    public void loanCreationCustId(MultipleTermDepositUI objMultipleTermDepositUI) {
        multipleTermDepositUI = objMultipleTermDepositUI;
        ScreenName="MultileTermDeposit";
        btnNewActionPerformed(null);
        txtBranchId.setText(TrueTransactMain.BRANCH_ID);
        observable.setTxtBranchId(TrueTransactMain.BRANCH_ID);
        observable.setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        updateOBFields();
        loanCustMap = new HashMap();
        loanCustMap.put("LOAN_CUSTOMER_ID", "LOAN_CUSTOMER_ID");
        observable.setLoanCustMap(loanCustMap);
    }

    public void setAccountsUI(TermDepositUI objTermDepositUI) {
        termdepositUI = objTermDepositUI;
    }
    
    public void setAccountsUI(MultipleTermDepositUI objMultipleTermDepositUI) {
        multipleTermDepositUI = objMultipleTermDepositUI;
    }
    
    public void loanCreationCustId(TermLoanUI objTermLoanUI) {
        termloanUI = objTermLoanUI;
        btnNewActionPerformed(null);
        ScreenName="TermLoan";
        txtBranchId.setText(TrueTransactMain.BRANCH_ID);
        observable.setTxtBranchId(TrueTransactMain.BRANCH_ID);
        observable.setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        updateOBFields();
        loanCustMap = new HashMap();
        loanCustMap.put("LOAN_CUSTOMER_ID", "LOAN_CUSTOMER_ID");
         observable.setLoanCustMap(loanCustMap);
    }

    public void setAccountsUI(TermLoanUI objTermLoanUI) {
        termloanUI = objTermLoanUI;
    }

    public void loanCreationCustId(SuspenseAccountMasterUI objSuspenseAccountMasterUI) {
        suspenseaccountUI = objSuspenseAccountMasterUI;
        ScreenName="SuspenseAccount";
        btnNewActionPerformed(null);
        txtBranchId.setText(TrueTransactMain.BRANCH_ID);
        observable.setTxtBranchId(TrueTransactMain.BRANCH_ID);
        observable.setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        updateOBFields();
        loanCustMap = new HashMap();
        loanCustMap.put("LOAN_CUSTOMER_ID", "LOAN_CUSTOMER_ID");
        observable.setLoanCustMap(loanCustMap);
    }

    public void setAccountsUI(SuspenseAccountMasterUI objSuspenseAccountMasterUI) {
        suspenseaccountUI = objSuspenseAccountMasterUI;
    }

    public void loanCreationCustId(ShareUI objShareUI) {
        shareUI = objShareUI;
         ScreenName="Share";
        btnNewActionPerformed(null);
        txtBranchId.setText(TrueTransactMain.BRANCH_ID);
        observable.setTxtBranchId(TrueTransactMain.BRANCH_ID);
        observable.setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        updateOBFields();
        loanCustMap = new HashMap();
        loanCustMap.put("LOAN_CUSTOMER_ID", "LOAN_CUSTOMER_ID");
        observable.setLoanCustMap(loanCustMap);
    }

    public void setAccountsUI(ShareUI objShareUI) {
        shareUI = objShareUI;
    }

    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    public void updateIncomeFields() {
    }

    /**
     * Getter for property updateMode.
     *
     * @return Value of property updateMode.
     */
    public boolean isUpdateMode() {
        return updateMode;
    }

    /**
     * Setter for property updateMode.
     *
     * @param updateMode New value of property updateMode.
     */
    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    /**
     * Getter for property LandDetupdateMode.
     *
     * @return Value of property LandDetupdateMode.
     */
    public boolean isLandDetupdateMode() {
        return LandDetupdateMode;
    }

    /**
     * Setter for property LandDetupdateMode.
     *
     * @param LandDetupdateMode New value of property LandDetupdateMode.
     */
    public void setLandDetupdateMode(boolean LandDetupdateMode) {
        this.LandDetupdateMode = LandDetupdateMode;
    }

    /**
     * Getter for property authorizeListUI.
     *
     * @return Value of property authorizeListUI.
     */
    public com.see.truetransact.ui.common.viewall.AuthorizeListUI getAuthorizeListUI() {
        return authorizeListUI;
    }

    /**
     * Setter for property authorizeListUI.
     *
     * @param authorizeListUI New value of property authorizeListUI.
     */
    public void setAuthorizeListUI(com.see.truetransact.ui.common.viewall.AuthorizeListUI authorizeListUI) {
        this.authorizeListUI = authorizeListUI;
    }
//      public void incPar(){
//          txtIncIncome.setText("");
//          txtIncName.setText("");
//          cboIncPack.set
//      }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClearPassport;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnContactAdd;
    private com.see.truetransact.uicomponent.CButton btnContactDelete;
    private com.see.truetransact.uicomponent.CButton btnContactNew;
    private com.see.truetransact.uicomponent.CButton btnContactNoAdd;
    private com.see.truetransact.uicomponent.CButton btnContactToMain;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeletedDetails;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGenerateAppPwd;
    private com.see.truetransact.uicomponent.CButton btnIncDelete;
    private com.see.truetransact.uicomponent.CButton btnIncNew;
    private com.see.truetransact.uicomponent.CButton btnIncSave;
    private com.see.truetransact.uicomponent.CButton btnLandDelete;
    private com.see.truetransact.uicomponent.CButton btnLandNew;
    private com.see.truetransact.uicomponent.CButton btnLandSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPhoneDelete;
    private com.see.truetransact.uicomponent.CButton btnPhoneNew;
    private com.see.truetransact.uicomponent.CButton btnPhotoLoad;
    private com.see.truetransact.uicomponent.CButton btnPhotoRemove;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProofAdd;
    private com.see.truetransact.uicomponent.CButton btnProofDelete;
    private com.see.truetransact.uicomponent.CButton btnProofNew;
    private com.see.truetransact.uicomponent.CButton btnProofPhotoLoad;
    private com.see.truetransact.uicomponent.CButton btnProofPhotoRemove;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnSignLoad;
    private com.see.truetransact.uicomponent.CButton btnSignRemove;
    private com.see.truetransact.uicomponent.CButton btnStaffId;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private javax.swing.JComboBox cbcomboAmsam;
    private com.see.truetransact.uicomponent.CComboBox cbcomboDesam;
    private com.see.truetransact.uicomponent.CComboBox cboAddrProof;
    private com.see.truetransact.uicomponent.CComboBox cboAddressType;
    private com.see.truetransact.uicomponent.CComboBox cboAgentCustId;
    private com.see.truetransact.uicomponent.CComboBox cboAnnualIncomeLevel;
    private com.see.truetransact.uicomponent.CComboBox cboCareOf;
    private com.see.truetransact.uicomponent.CComboBox cboCaste;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboCustStatus;
    private com.see.truetransact.uicomponent.CComboBox cboCustTaluk;
    private com.see.truetransact.uicomponent.CComboBox cboCustVillage;
    private com.see.truetransact.uicomponent.CComboBox cboCustomerGroup;
    private com.see.truetransact.uicomponent.CComboBox cboCustomerType;
    private com.see.truetransact.uicomponent.CComboBox cboDistrict;
    private com.see.truetransact.uicomponent.CComboBox cboDivision;
    private com.see.truetransact.uicomponent.CComboBox cboEducationalLevel;
    private com.see.truetransact.uicomponent.CComboBox cboFarClass;
    private com.see.truetransact.uicomponent.CComboBox cboGCity;
    private com.see.truetransact.uicomponent.CComboBox cboGCountry;
    private com.see.truetransact.uicomponent.CComboBox cboGState;
    private com.see.truetransact.uicomponent.CComboBox cboIdenProof;
    private com.see.truetransact.uicomponent.CComboBox cboIncDetProfession;
    private com.see.truetransact.uicomponent.CComboBox cboIncPack;
    private com.see.truetransact.uicomponent.CComboBox cboIncRelation;
    private com.see.truetransact.uicomponent.CComboBox cboIntroType;
    private com.see.truetransact.uicomponent.CComboBox cboIrrigated;
    private com.see.truetransact.uicomponent.CComboBox cboLandDetState;
    private com.see.truetransact.uicomponent.CComboBox cboMembershipClass;
    private com.see.truetransact.uicomponent.CComboBox cboPassportIssuePlace;
    private com.see.truetransact.uicomponent.CComboBox cboPassportTitle;
    private com.see.truetransact.uicomponent.CComboBox cboPhoneType;
    private com.see.truetransact.uicomponent.CComboBox cboPostOffice;
    private com.see.truetransact.uicomponent.CComboBox cboPrefCommunication;
    private com.see.truetransact.uicomponent.CComboBox cboPrimaryOccupation;
    private com.see.truetransact.uicomponent.CComboBox cboProfession;
    private com.see.truetransact.uicomponent.CComboBox cboRelationManager;
    private com.see.truetransact.uicomponent.CComboBox cboRelationNO;
    private com.see.truetransact.uicomponent.CComboBox cboReligion;
    private com.see.truetransact.uicomponent.CComboBox cboResidentialStatus;
    private com.see.truetransact.uicomponent.CComboBox cboSrIrrigation;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CComboBox cboSubCaste;
    private com.see.truetransact.uicomponent.CComboBox cboTaluk;
    private com.see.truetransact.uicomponent.CComboBox cboTitle;
    private com.see.truetransact.uicomponent.CComboBox cboVehicle;
    private com.see.truetransact.uicomponent.CComboBox cboWardName;
    private com.see.truetransact.uicomponent.CCheckBox chkAddrVerified;
    private com.see.truetransact.uicomponent.CCheckBox chkClosAcdetail;
    private com.see.truetransact.uicomponent.CCheckBox chkConfirmationfromIntroducer;
    private com.see.truetransact.uicomponent.CCheckBox chkFinanceStmtVerified;
    private com.see.truetransact.uicomponent.CCheckBox chkIncParticulars;
    private com.see.truetransact.uicomponent.CCheckBox chkLandDetails;
    private com.see.truetransact.uicomponent.CCheckBox chkMinor;
    private com.see.truetransact.uicomponent.CCheckBox chkMinority;
    private com.see.truetransact.uicomponent.CCheckBox chkPhVerified;
    private com.see.truetransact.uicomponent.CCheckBox chkRegionalLang;
    private com.see.truetransact.uicomponent.CCheckBox chkRevokeCust;
    private com.see.truetransact.uicomponent.CCheckBox chkSentThanksLetter;
    private com.see.truetransact.uicomponent.CCheckBox chkStaff;
    private com.see.truetransact.uicomponent.CCheckBox chkSuspendCust;
    private com.see.truetransact.uicomponent.CLabel lbLandlState;
    private com.see.truetransact.uicomponent.CLabel lblAddrProof;
    private com.see.truetransact.uicomponent.CLabel lblAddrRemarks;
    private com.see.truetransact.uicomponent.CLabel lblAddrVerified;
    private com.see.truetransact.uicomponent.CLabel lblAddressType;
    private com.see.truetransact.uicomponent.CLabel lblAge;
    private com.see.truetransact.uicomponent.CLabel lblAgentCustId;
    private com.see.truetransact.uicomponent.CLabel lblAgentCustIdValue;
    private com.see.truetransact.uicomponent.CLabel lblAmsam;
    private com.see.truetransact.uicomponent.CLabel lblAnnualIncomeLevel;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaCode;
    private com.see.truetransact.uicomponent.CLabel lblAreaInAcrs;
    private com.see.truetransact.uicomponent.CLabel lblBankruptsy;
    private com.see.truetransact.uicomponent.CLabel lblBranchId;
    private com.see.truetransact.uicomponent.CLabel lblCareOf;
    private com.see.truetransact.uicomponent.CLabel lblCaste;
    private com.see.truetransact.uicomponent.CLabel lblChkMinor;
    private com.see.truetransact.uicomponent.CLabel lblChkMinority;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private javax.swing.JLabel lblClosAcdetail;
    private com.see.truetransact.uicomponent.CLabel lblCompany;
    private com.see.truetransact.uicomponent.CLabel lblConfirmationfromIntroducer;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCrAvldSince;
    private com.see.truetransact.uicomponent.CLabel lblCreatedDt;
    private com.see.truetransact.uicomponent.CLabel lblCreatedDt1;
    private com.see.truetransact.uicomponent.CLabel lblCustPwd;
    private com.see.truetransact.uicomponent.CLabel lblCustStatus;
    private com.see.truetransact.uicomponent.CLabel lblCustTaluk;
    private com.see.truetransact.uicomponent.CLabel lblCustUserid;
    private com.see.truetransact.uicomponent.CLabel lblCustVillage;
    private com.see.truetransact.uicomponent.CLabel lblCustomerGroup;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerType;
    private com.see.truetransact.uicomponent.CLabel lblDateOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblDealingPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDealingWith;
    private com.see.truetransact.uicomponent.CLabel lblDesam;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblDistrict;
    private com.see.truetransact.uicomponent.CLabel lblDivision;
    private com.see.truetransact.uicomponent.CLabel lblEducationalLevel;
    private com.see.truetransact.uicomponent.CLabel lblEmailID;
    private com.see.truetransact.uicomponent.CLabel lblFarClass;
    private com.see.truetransact.uicomponent.CLabel lblFinanceStmtVerified;
    private com.see.truetransact.uicomponent.CLabel lblFirstName;
    private com.see.truetransact.uicomponent.CLabel lblFirstNamePass;
    private com.see.truetransact.uicomponent.CLabel lblGArea;
    private com.see.truetransact.uicomponent.CLabel lblGCity;
    private com.see.truetransact.uicomponent.CLabel lblGCountry;
    private com.see.truetransact.uicomponent.CLabel lblGPinCode;
    private com.see.truetransact.uicomponent.CLabel lblGState;
    private com.see.truetransact.uicomponent.CLabel lblGStreet;
    private com.see.truetransact.uicomponent.CLabel lblGender;
    private com.see.truetransact.uicomponent.CLabel lblGuardianAge;
    private com.see.truetransact.uicomponent.CLabel lblGuardianDoB;
    private com.see.truetransact.uicomponent.CLabel lblGuardianNameNO;
    private com.see.truetransact.uicomponent.CLabel lblGuardianPhoneNO;
    private com.see.truetransact.uicomponent.CLabel lblHobli;
    private com.see.truetransact.uicomponent.CLabel lblIdenProof;
    private com.see.truetransact.uicomponent.CLabel lblIncDetCompany;
    private com.see.truetransact.uicomponent.CLabel lblIncDetProfession;
    private com.see.truetransact.uicomponent.CLabel lblIncIncome;
    private com.see.truetransact.uicomponent.CLabel lblIncName;
    private com.see.truetransact.uicomponent.CLabel lblIncRelation;
    private com.see.truetransact.uicomponent.CLabel lblIncomeParticulars;
    private com.see.truetransact.uicomponent.CLabel lblIntroType;
    private com.see.truetransact.uicomponent.CLabel lblIrrigated;
    private com.see.truetransact.uicomponent.CLabel lblIssuedDt;
    private com.see.truetransact.uicomponent.CLabel lblJoiningDate;
    private com.see.truetransact.uicomponent.CLabel lblKartha;
    private com.see.truetransact.uicomponent.CLabel lblLandDetails;
    private com.see.truetransact.uicomponent.CLabel lblLandPin;
    private com.see.truetransact.uicomponent.CLabel lblLanguage;
    private com.see.truetransact.uicomponent.CLabel lblLastName;
    private com.see.truetransact.uicomponent.CLabel lblLastNamePass;
    private com.see.truetransact.uicomponent.CLabel lblLocation;
    private com.see.truetransact.uicomponent.CLabel lblMaritalStatus;
    private com.see.truetransact.uicomponent.CLabel lblMemNum;
    private com.see.truetransact.uicomponent.CLabel lblMembershipClass;
    private com.see.truetransact.uicomponent.CLabel lblMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblMiddleNamePass;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNationality;
    private com.see.truetransact.uicomponent.CLabel lblPassIssueAuth;
    private com.see.truetransact.uicomponent.CLabel lblPassPlaceIssued;
    private com.see.truetransact.uicomponent.CLabel lblPassPortNo;
    private com.see.truetransact.uicomponent.CLabel lblPhVerified;
    private com.see.truetransact.uicomponent.CLabel lblPhoneNumber;
    private com.see.truetransact.uicomponent.CLabel lblPhoneType;
    private com.see.truetransact.uicomponent.CLabel lblPhoto;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblPost;
    private com.see.truetransact.uicomponent.CLabel lblPostOffice;
    private com.see.truetransact.uicomponent.CLabel lblPrefCommunication;
    private com.see.truetransact.uicomponent.CLabel lblPrimaryOccupation;
    private com.see.truetransact.uicomponent.CLabel lblProfession;
    private com.see.truetransact.uicomponent.CLabel lblProofPhoto;
    private com.see.truetransact.uicomponent.CLabel lblRegAmsam;
    private com.see.truetransact.uicomponent.CLabel lblRegCity;
    private com.see.truetransact.uicomponent.CLabel lblRegCountry;
    private com.see.truetransact.uicomponent.CLabel lblRegDesam;
    private com.see.truetransact.uicomponent.CLabel lblRegGardName;
    private com.see.truetransact.uicomponent.CLabel lblRegHname;
    private com.see.truetransact.uicomponent.CLabel lblRegName;
    private com.see.truetransact.uicomponent.CLabel lblRegPlace;
    private com.see.truetransact.uicomponent.CLabel lblRegState;
    private com.see.truetransact.uicomponent.CLabel lblRegTaluk;
    private com.see.truetransact.uicomponent.CLabel lblRegVillage;
    private com.see.truetransact.uicomponent.CLabel lblRegionalLang;
    private com.see.truetransact.uicomponent.CLabel lblRelationManager;
    private com.see.truetransact.uicomponent.CLabel lblRelationNO;
    private com.see.truetransact.uicomponent.CLabel lblReligion;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblResidentialStatus;
    private com.see.truetransact.uicomponent.CLabel lblRetireAge;
    private com.see.truetransact.uicomponent.CLabel lblRetireDt;
    private com.see.truetransact.uicomponent.CLabel lblRevokeCust;
    private com.see.truetransact.uicomponent.CLabel lblRevokeDate;
    private com.see.truetransact.uicomponent.CLabel lblRiskRate;
    private com.see.truetransact.uicomponent.CLabel lblSentThanksLetter;
    private com.see.truetransact.uicomponent.CLabel lblSign;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSrcIrr;
    private com.see.truetransact.uicomponent.CLabel lblSsn;
    private com.see.truetransact.uicomponent.CLabel lblStaffId;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblSubCaste;
    private com.see.truetransact.uicomponent.CLabel lblSurNo;
    private com.see.truetransact.uicomponent.CLabel lblSuspendCust;
    private com.see.truetransact.uicomponent.CLabel lblSuspendDate;
    private com.see.truetransact.uicomponent.CLabel lblSuspendRemarks;
    private com.see.truetransact.uicomponent.CLabel lblTaluk;
    private com.see.truetransact.uicomponent.CLabel lblTransPwd;
    private com.see.truetransact.uicomponent.CLabel lblUniqueNo;
    private com.see.truetransact.uicomponent.CLabel lblValidUpto;
    private com.see.truetransact.uicomponent.CLabel lblVehicle;
    private com.see.truetransact.uicomponent.CLabel lblVillage;
    private com.see.truetransact.uicomponent.CLabel lblWardName;
    private com.see.truetransact.uicomponent.CLabel lblWardNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenuItem mitSaveAs;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAdditionalInfo;
    private com.see.truetransact.uicomponent.CPanel panAdditionalInfo1;
    private com.see.truetransact.uicomponent.CPanel panAdditionalInformation;
    private com.see.truetransact.uicomponent.CPanel panAddrRemarks;
    private com.see.truetransact.uicomponent.CPanel panAddress;
    private com.see.truetransact.uicomponent.CPanel panAddressDetails;
    private com.see.truetransact.uicomponent.CPanel panAddressProofPhoto;
    private com.see.truetransact.uicomponent.CPanel panAddressType;
    private com.see.truetransact.uicomponent.CPanel panCareOf;
    private com.see.truetransact.uicomponent.CPanel panCareOf1;
    private com.see.truetransact.uicomponent.CPanel panCareOf2;
    private com.see.truetransact.uicomponent.CPanel panCareOf3;
    private com.see.truetransact.uicomponent.CPanel panCareOf4;
    private com.see.truetransact.uicomponent.CPanel panCity;
    private com.see.truetransact.uicomponent.CPanel panCompanyInfo;
    private com.see.truetransact.uicomponent.CPanel panContactAndIdentityInfo;
    private com.see.truetransact.uicomponent.CPanel panContactControl;
    private com.see.truetransact.uicomponent.CPanel panContactInfo;
    private com.see.truetransact.uicomponent.CPanel panContactNo;
    private com.see.truetransact.uicomponent.CPanel panContacts;
    private com.see.truetransact.uicomponent.CPanel panContactsList;
    private com.see.truetransact.uicomponent.CPanel panCountry;
    private com.see.truetransact.uicomponent.CPanel panCountryDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomer;
    private com.see.truetransact.uicomponent.CPanel panCustomerHistory;
    private com.see.truetransact.uicomponent.CPanel panCustomerInfo;
    private com.see.truetransact.uicomponent.CPanel panCustomerInfo1;
    private com.see.truetransact.uicomponent.CPanel panDOB;
    private com.see.truetransact.uicomponent.CPanel panFirstName;
    private com.see.truetransact.uicomponent.CPanel panFirstName1;
    private com.see.truetransact.uicomponent.CPanel panFirstName2;
    private com.see.truetransact.uicomponent.CPanel panFreezeSave;
    private com.see.truetransact.uicomponent.CPanel panFreezeSave1;
    private com.see.truetransact.uicomponent.CPanel panGender;
    private com.see.truetransact.uicomponent.CPanel panGuardAddr;
    private com.see.truetransact.uicomponent.CPanel panGuardData;
    private com.see.truetransact.uicomponent.CPanel panGuardianDetails;
    private com.see.truetransact.uicomponent.CPanel panITDetails;
    private com.see.truetransact.uicomponent.CPanel panITDetails1;
    private com.see.truetransact.uicomponent.CPanel panITDetails2;
    private com.see.truetransact.uicomponent.CPanel panIncomeParticulars;
    private com.see.truetransact.uicomponent.CPanel panKYC;
    private com.see.truetransact.uicomponent.CPanel panKara;
    private com.see.truetransact.uicomponent.CPanel panLandDetails;
    private com.see.truetransact.uicomponent.CPanel panLandInfoDetails;
    private com.see.truetransact.uicomponent.CPanel panLandInfoTable;
    private com.see.truetransact.uicomponent.CPanel panMIS;
    private com.see.truetransact.uicomponent.CPanel panMISKYC;
    private com.see.truetransact.uicomponent.CPanel panMaritalStatus;
    private com.see.truetransact.uicomponent.CPanel panPassPortDetails;
    private com.see.truetransact.uicomponent.CPanel panPassportFirstName;
    private com.see.truetransact.uicomponent.CPanel panPersonalInfo;
    private com.see.truetransact.uicomponent.CPanel panPhoneAreaNumber;
    private com.see.truetransact.uicomponent.CPanel panPhoneList;
    private com.see.truetransact.uicomponent.CPanel panPhoneSave;
    private com.see.truetransact.uicomponent.CPanel panPhoneType;
    private com.see.truetransact.uicomponent.CPanel panPhoto;
    private com.see.truetransact.uicomponent.CPanel panPhotoButtons;
    private com.see.truetransact.uicomponent.CPanel panPhotoSign;
    private com.see.truetransact.uicomponent.CPanel panPost;
    private com.see.truetransact.uicomponent.CPanel panProof;
    private com.see.truetransact.uicomponent.CPanel panProofControl;
    private com.see.truetransact.uicomponent.CPanel panProofDetails;
    private com.see.truetransact.uicomponent.CPanel panProofList;
    private com.see.truetransact.uicomponent.CPanel panProofPhoto;
    private com.see.truetransact.uicomponent.CPanel panProofUploanButtons;
    private com.see.truetransact.uicomponent.CPanel panRegLanguage;
    private com.see.truetransact.uicomponent.CPanel panSaveBtn;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch1;
    private com.see.truetransact.uicomponent.CPanel panSearch2;
    private com.see.truetransact.uicomponent.CPanel panSearch3;
    private com.see.truetransact.uicomponent.CPanel panSign;
    private com.see.truetransact.uicomponent.CPanel panSignButtons;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuspendCustomer;
    private com.see.truetransact.uicomponent.CPanel panTeleCommunication;
    private com.see.truetransact.uicomponent.CPanel panTelecomDetails;
    private com.see.truetransact.uicomponent.CPanel phoneGPanelNO;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGender;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Female;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Male;
    private com.see.truetransact.uicomponent.CButtonGroup rdoITDec;
    private com.see.truetransact.uicomponent.CRadioButton rdoITDec_F60;
    private com.see.truetransact.uicomponent.CRadioButton rdoITDec_F61;
    private com.see.truetransact.uicomponent.CRadioButton rdoITDec_Pan;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMaritalStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaritalStatus_Married;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaritalStatus_Single;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaritalStatus_Widow;
    private com.see.truetransact.uicomponent.CSeparator sptGuard;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpContactList;
    private com.see.truetransact.uicomponent.CScrollPane srpCustomerHistory;
    private com.see.truetransact.uicomponent.CScrollPane srpCustomerLandDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpIncParticulars;
    private com.see.truetransact.uicomponent.CScrollPane srpPhoneList;
    private com.see.truetransact.uicomponent.CScrollPane srpPhotoLoad;
    private com.see.truetransact.uicomponent.CScrollPane srpProofList;
    private com.see.truetransact.uicomponent.CScrollPane srpProofPhotoLoad;
    private com.see.truetransact.uicomponent.CScrollPane srpSignLoad;
    private com.see.truetransact.uicomponent.CTabbedPane tabContactAndIdentityInfo;
    private com.see.truetransact.uicomponent.CTabbedPane tabIndCust;
    private com.see.truetransact.uicomponent.CTable tblContactList;
    private com.see.truetransact.uicomponent.CTable tblCustomerHistory;
    private com.see.truetransact.uicomponent.CTable tblCustomerLandDetails;
    private com.see.truetransact.uicomponent.CTable tblIncParticulars;
    private com.see.truetransact.uicomponent.CTable tblPhoneList;
    private com.see.truetransact.uicomponent.CTable tblProofList;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtCrAvldSince;
    private com.see.truetransact.uicomponent.CDateField tdtDateOfBirth;
    private com.see.truetransact.uicomponent.CDateField tdtGuardianDOB;
    private com.see.truetransact.uicomponent.CDateField tdtJoiningDate;
    private com.see.truetransact.uicomponent.CDateField tdtPassportIssueDt;
    private com.see.truetransact.uicomponent.CDateField tdtPassportValidUpto;
    private com.see.truetransact.uicomponent.CDateField tdtRevokedCustDate;
    private com.see.truetransact.uicomponent.CDateField tdtSuspendCustFrom;
    private com.see.truetransact.uicomponent.CDateField tdtretireDt;
    private com.see.truetransact.uicomponent.CTextField txtAddrRemarks;
    private com.see.truetransact.uicomponent.CTextField txtAge;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtAreaCode;
    private com.see.truetransact.uicomponent.CTextField txtAreaInAcrs;
    private com.see.truetransact.uicomponent.CTextField txtBankruptsy;
    private com.see.truetransact.uicomponent.CTextField txtBranchId;
    private com.see.truetransact.uicomponent.CTextField txtCompany;
    private com.see.truetransact.uicomponent.CPasswordField txtCustPwd;
    private com.see.truetransact.uicomponent.CTextField txtCustUserid;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtDesignation;
    private com.see.truetransact.uicomponent.CTextField txtEmailID;
    private com.see.truetransact.uicomponent.CTextField txtFirstName;
    private com.see.truetransact.uicomponent.CTextField txtGArea;
    private com.see.truetransact.uicomponent.CTextField txtGPinCode;
    private com.see.truetransact.uicomponent.CTextField txtGStreet;
    private com.see.truetransact.uicomponent.CTextField txtGuardianACodeNO;
    private com.see.truetransact.uicomponent.CTextField txtGuardianAge;
    private com.see.truetransact.uicomponent.CTextField txtGuardianNameNO;
    private com.see.truetransact.uicomponent.CTextField txtGuardianPhoneNO;
    private com.see.truetransact.uicomponent.CTextField txtHobli;
    private com.see.truetransact.uicomponent.CTextField txtIncDetCompany;
    private com.see.truetransact.uicomponent.CTextField txtIncIncome;
    private com.see.truetransact.uicomponent.CTextField txtIncName;
    private com.see.truetransact.uicomponent.CTextField txtKartha;
    private com.see.truetransact.uicomponent.CTextField txtLandDetPincode;
    private com.see.truetransact.uicomponent.CTextField txtLanguage;
    private com.see.truetransact.uicomponent.CTextField txtLastName;
    private com.see.truetransact.uicomponent.CTextField txtLoc;
    private com.see.truetransact.uicomponent.CTextField txtMemNum;
    private com.see.truetransact.uicomponent.CTextField txtMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtNationality;
    private com.see.truetransact.uicomponent.CTextField txtPanNumber;
    private com.see.truetransact.uicomponent.CTextField txtPassportFirstName;
    private com.see.truetransact.uicomponent.CTextField txtPassportIssueAuth;
    private com.see.truetransact.uicomponent.CTextField txtPassportLastName;
    private com.see.truetransact.uicomponent.CTextField txtPassportMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtPassportNo;
    private com.see.truetransact.uicomponent.CTextField txtPhoneNumber;
    private com.see.truetransact.uicomponent.CTextField txtPincode;
    private com.see.truetransact.uicomponent.CTextField txtPo;
    private com.see.truetransact.uicomponent.CTextField txtRegAmsam;
    private com.see.truetransact.uicomponent.CTextField txtRegCity;
    private com.see.truetransact.uicomponent.CTextField txtRegCountry;
    private com.see.truetransact.uicomponent.CTextField txtRegDesam;
    private com.see.truetransact.uicomponent.CTextField txtRegGardName;
    private com.see.truetransact.uicomponent.CTextField txtRegHname;
    private com.see.truetransact.uicomponent.CTextField txtRegMName;
    private com.see.truetransact.uicomponent.CTextField txtRegMaAmsam;
    private com.see.truetransact.uicomponent.CTextField txtRegMaCity;
    private com.see.truetransact.uicomponent.CTextField txtRegMaCountry;
    private com.see.truetransact.uicomponent.CTextField txtRegMaDesam;
    private com.see.truetransact.uicomponent.CTextField txtRegMaGardName;
    private com.see.truetransact.uicomponent.CTextField txtRegMaHname;
    private com.see.truetransact.uicomponent.CTextField txtRegMaPlace;
    private com.see.truetransact.uicomponent.CTextField txtRegMaState;
    private com.see.truetransact.uicomponent.CTextField txtRegMaTaluk;
    private com.see.truetransact.uicomponent.CTextField txtRegMavillage;
    private com.see.truetransact.uicomponent.CTextField txtRegName;
    private com.see.truetransact.uicomponent.CTextField txtRegPlace;
    private com.see.truetransact.uicomponent.CTextField txtRegState;
    private com.see.truetransact.uicomponent.CTextField txtRegTaluk;
    private com.see.truetransact.uicomponent.CTextField txtRegvillage;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRetireAge;
    private com.see.truetransact.uicomponent.CTextField txtRiskRate;
    private com.see.truetransact.uicomponent.CTextField txtSrNo;
    private com.see.truetransact.uicomponent.CTextField txtSsn;
    private com.see.truetransact.uicomponent.CTextField txtStaffId;
    private com.see.truetransact.uicomponent.CTextField txtStreet;
    private com.see.truetransact.uicomponent.CTextField txtSuspRevRemarks;
    private com.see.truetransact.uicomponent.CPasswordField txtTransPwd;
    private com.see.truetransact.uicomponent.CTextField txtUniqueId;
    private com.see.truetransact.uicomponent.CTextField txtVillage;
    private com.see.truetransact.uicomponent.CTextField txtWardNo;
    // End of variables declaration//GEN-END:variables
}
