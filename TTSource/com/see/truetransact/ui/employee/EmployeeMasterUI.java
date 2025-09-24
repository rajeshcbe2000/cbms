/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * EmployeeMasterUI.java
 *
 * Created on August 5, 2003, 1:02 PM
 */

package com.see.truetransact.ui.employee;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
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
import com.see.truetransact.transferobject.common.PromotionTO;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import java.net.URL;
import com.see.truetransact.uivalidation.ToDateValidation;
import java.util.List;
/**
 *
 * @author  karthik
 * Modified by Annamalai
 * Bala RS
 * Ashok .V
 * Swaroop.V
 * Nikhil
 */

public class EmployeeMasterUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private EmployeeMasterOB observable = null;
    private Date curDate = null;
    ResourceBundle resourceBundle  = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.employee.EmployeeMasterRB", ProxyParameters.LANGUAGE);
    EmployeeMasterMRB objMandatoryRB = new EmployeeMasterMRB();
    private HashMap mandatoryMap;
    private EmployeeMasterUISupport objEmployeeMasterUISupport;
    private boolean newButtonEnable = false;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String mandatoryMessage="";
    final int AUTHORIZE=3;
    final int DELETE = 1;
    private int viewType=-1;
    private int INCREMENT = 1,PROMOTION = 2,ARREARS = 3;
    private int INCREMENT_ID = 1,ACCT_HEAD = 2,EDIT = 3,VIEW = 5,PROMOTION_ID = 6;
    boolean isFilled = false;
    private boolean phoneExist;
    private boolean loanExist;
    private int loanRow;
    private int phoneRow;
    private final String CLASSNAME = this.getClass().getName();
    private String actionType = "";
    private String EMPLOYEEID;//Variable used to get the CustomerId when the popup comes on clicking on Search button in the UI
    private String CUST_TYPE_COURT = "COURT";
    boolean checkminor = false;
    boolean flag = false;
    int updateTab = -1;
    private boolean updateMode = false;
    boolean  chk= false;
    boolean  chkLand= false;
    String releativeSysId="";
    final String SCREEN = "EPLOYEEMASTER"; // earlier screen = CUS
    boolean tabInduFocused = false; //to check if the tab tabIndCust has been visited
    //to check whether the specific table is in insert or update mode and to repopulate it after editing an already inserted row
    private boolean _intHANew = false;
    private boolean _intEDNew = false;
    private boolean familyDetailsFlag = false;
    private boolean directorDetailsFlag = false;
    private boolean relativeWorkingFlag = false;
    private boolean academicEducationFlag = false;
    private boolean technicalEducationFlag = false;
    private boolean selectedSingleRow = false;
    private boolean promotiondetailsFlag = false;
    private boolean operativeFlag = false;
    int pan = -1;
    int panEditDelete = -1;
    //added from here
    private boolean dependentId = false;
    /**
     * Declare a new instance of the  IntroducerUI...
     */
    
    /** Creates new form IndividualCustomer */
    public EmployeeMasterUI() {
        initComponents();
        initStartup();
    }
    private void initStartup(){
        curDate = ClientUtil.getCurrentDate();
        internationalize();
        setFieldNames();
        setObservable();
        initComponentData();
        ClientUtil.enableDisable(this, false, false, true);
        setMaximumLength();
        setButtonEnableDisable();
        setMandatoryHashMap();
        setHelpMessage();
        colWidthChange();
        setMandatoryMarks();
        initTableData();
        panBtnOprativeDetails.setEnabled(false);
        panBtnTermLoans.setEnabled(false);
        tabIndCust.resetVisits();
    }
    
    private void removeButtons(){
        rdoGender.remove(rdoGender_Male);
        rdoGender.remove(rdoGender_Female);
        rdoGradutionIncrement.remove(rdoGradutionIncrementNo);
        rdoGradutionIncrement.remove(rdoGradutionIncrementYes);
        rdoCAIIBPART1Increment.remove(rdoCAIIBPART1IncrementYes);
        rdoCAIIBPART1Increment.remove(rdoCAIIBPART1IncrementNo);
        rdoCAIIBPART2Increment.remove(rdoCAIIBPART2Increment_Yes);
        rdoCAIIBPART2Increment.remove(rdoCAIIBPART2Increment_No);
        rdoAnyotherIncrement.remove(rdoAnyOtherIncrement_Yes);
        rdoAnyotherIncrement.remove(rdoAnyOtherIncrement_No);
        rdoSignature.remove(rdoSignature_Yes);
        rdoSignature.remove(rdoSignature_No);
        rdoClubMembership.remove(rdoClubMembership_YES);
        rdoClubMembership.remove(rdoClubMembership_No);
        rdoMaritalStatus.remove(rdoMaritalStatus_Single);
        rdoMaritalStatus.remove(rdoMaritalStatus_Married);
        rdoLoanPreCloser.remove(rdoLoanPreCloserYes);
        rdoLoanPreCloser.remove(rdoLoanPreCloserNo);
    }
    
    private void addButtons(){
        rdoGender.add(rdoGender_Male);
        rdoGender.add(rdoGender_Female);
        rdoMaritalStatus.add(rdoMaritalStatus_Single);
        rdoMaritalStatus.add(rdoMaritalStatus_Married);
        rdoGradutionIncrement.add(rdoGradutionIncrementNo);
        rdoGradutionIncrement.add(rdoGradutionIncrementYes);
        rdoCAIIBPART1Increment.add(rdoCAIIBPART1IncrementYes);
        rdoCAIIBPART1Increment.add(rdoCAIIBPART1IncrementNo);
        rdoCAIIBPART2Increment.add(rdoCAIIBPART2Increment_Yes);
        rdoCAIIBPART2Increment.add(rdoCAIIBPART2Increment_No);
        rdoAnyotherIncrement.add(rdoAnyOtherIncrement_Yes);
        rdoAnyotherIncrement.add(rdoAnyOtherIncrement_No);
        rdoSignature.add(rdoSignature_Yes);
        rdoSignature.add(rdoSignature_No);
        rdoClubMembership.add(rdoClubMembership_YES);
        rdoClubMembership.add(rdoClubMembership_No);
        rdoLoanPreCloser.add(rdoLoanPreCloserYes);
        rdoLoanPreCloser.add(rdoLoanPreCloserNo);
    }
    private void initTableData(){
        tblContactList.setModel(observable.getTblContDet());
        tblAcademicEducation.setModel(observable.getTblAcademicDet());
        tblTechnicalEducation.setModel(observable.getTblTechnicalDet());
        tblLanguage.setModel(observable.getTblLanguageDet());
        tblFamily.setModel(observable.getTblFamilyDet());
        tblPhoneList.setModel(observable.getTblPhoneList());
        tblReleativeWorkingInBank.setModel(observable.getTblReleativeWorkingInBank());
        tblDetailsOfRelativeDirector.setModel(observable.getTblDetailsOfRelativeDirector());
        tblOprative.setModel(observable.getTblOprative());
        tblEmployeeLoan.setModel(observable.getTblEmployeeLoan());
        tblPromotion.setModel(observable.getTblPromotion());
    }
    
    
    private void setObservable(){
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '1' indicates that the customer type is INDIVIDUAL
        observable = new EmployeeMasterOB(1);
        observable.addObserver(this);
    }
    
    private void setMaximumLength(){
        txtEmpID.setMaxLength(16);
        txtEmpID.setAllowAll(true);
        txtFirstName.setMaxLength(128);
        txtMiddleName.setMaxLength(128);
        txtLastName.setMaxLength(128);
        txtFatherFirstName.setMaxLength(128);
        txtFatherMiddleName.setMaxLength(128);
        txtFatherLastName.setMaxLength(128);
        txtMotherFirstName.setMaxLength(128);
        txtMotherMiddleName.setMaxLength(128);
        txtMotherLastName.setMaxLength(128);
        txtPlaceOfBirth.setMaxLength(50);
        txtHomeTown.setMaxLength(24);
        txtIDCardNoNo.setMaxLength(16);
        txtUIDNo.setMaxLength(16);
        txtPanNumber.setMaxLength(16);
        txtPFNumber.setMaxLength(16);
        txtPincode.setMaxLength(16);
        txtPhoneNumber.setValidation(new NumericValidation());
        txtPhoneNumber.setMaxLength(16);
        txtPhoneNumber.setAllowNumber(true);
        txtPhoneNumber.setAllowAll(false);
        txtAreaCode.setValidation(new NumericValidation());
        txtAreaCode.setMaxLength(16);
        txtAreaCode.setAllowNumber(true);
        txtAreaCode.setAllowAll(false);
        txtPassportFirstName.setMaxLength(128);
        txtPassportMiddleName.setMaxLength(128);
        txtPassportLastName.setMaxLength(128);
        txtMarksScored.setValidation(new NumericValidation(5,0));
        txtTotMarks.setValidation(new NumericValidation(5,0));
        txtTotMarksPer.setValidation(new NumericValidation(4,2));
        txtProbationPeriod.setValidation(new NumericValidation(4,2));
        txtNameOfSchool.setMaxLength(60);
        txtNameOfTechInst.setMaxLength(60);
        txtTechnicalQualificationMarksScored.setValidation(new NumericValidation(5,0));
        txtDepIncomePerannum.setValidation(new CurrencyValidation());
        txtTechnicalQualificationTotMarks.setValidation(new NumericValidation(5,0));
        txtTechnicalQualificationTotMarksPer.setValidation(new NumericValidation(4,2));
        txtLoanRateofInterest.setValidation(new NumericValidation(4,2));
        txtLoanNoOfInstallments.setValidation(new NumericValidation(6,2));
        txtLoanInstallmentAmount.setValidation(new CurrencyValidation());
        txtLoanAmount.setValidation(new CurrencyValidation());
        txtReleativeStaffId.setMaxLength(16);
        txtPresentBasic.setValidation(new CurrencyValidation());
        txtLossPay_Months.setValidation(new NumericValidation());
        txtLossOfpay_Days.setValidation(new NumericValidation());
        txtNewBasic.setValidation(new CurrencyValidation());
    }
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEmployeeId", new Boolean(true));
        mandatoryMap.put("cboTitle", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("txtMiddleName", new Boolean(false));
        mandatoryMap.put("txtLastName", new Boolean(false));
        mandatoryMap.put("rdoGender_Male", new Boolean(false));
        mandatoryMap.put("rdoGender_Female", new Boolean(false));
        mandatoryMap.put("rdoMaritalStatus_Single", new Boolean(false));
        mandatoryMap.put("rdoMaritalStatus_Married", new Boolean(false));
        mandatoryMap.put("rdoFather", new Boolean(false));
        mandatoryMap.put("cboFsatherTitle", new Boolean(false));
        mandatoryMap.put("txtFatherFirstName", new Boolean(false));
        mandatoryMap.put("txtFatherMiddleName", new Boolean(false));
        mandatoryMap.put("txtFatherLastName", new Boolean(false));
        mandatoryMap.put("cboMotherTitle", new Boolean(false));
        mandatoryMap.put("txtMotherFirstName", new Boolean(false));
        mandatoryMap.put("txtMotherMiddleName", new Boolean(false));
        mandatoryMap.put("txtMotherLastName", new Boolean(false));
        mandatoryMap.put("tdtDateOfBirth", new Boolean(true));
        mandatoryMap.put("txtAge", new Boolean(false));
        mandatoryMap.put("txtPlaceOfBirth", new Boolean(false));
        mandatoryMap.put("cboReligion", new Boolean(true));
        mandatoryMap.put("cboCaste", new Boolean(true));
        mandatoryMap.put("txtHomeTown", new Boolean(true));
        mandatoryMap.put("txtIDCardNoNo", new Boolean(false));
        mandatoryMap.put("txtUIDNo", new Boolean(false));
        mandatoryMap.put("txtPFNumber", new Boolean(false));
        mandatoryMap.put("cboPFAcNominee", new Boolean(false));
        mandatoryMap.put("tdtDateOfJoin", new Boolean(true));
        mandatoryMap.put("tdtDateofRetirement", new Boolean(false));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
        mandatoryMap.put("txtPhoneNoCountryCode", new Boolean(false));
        mandatoryMap.put("txtPhoneNoCode", new Boolean(false));
        mandatoryMap.put("txtPhoneNo", new Boolean(false));
        mandatoryMap.put("txtMobileNoCountryCode", new Boolean(false));
        mandatoryMap.put("txtMobileNoCode", new Boolean(false));
        mandatoryMap.put("txtMobileNo", new Boolean(false));
        mandatoryMap.put("txtPassportFirstName", new Boolean(false));
        mandatoryMap.put("txtPassportMiddleName", new Boolean(false));
        mandatoryMap.put("tdtPassportIssueDt", new Boolean(false));
        mandatoryMap.put("tdtPassportValidUpto", new Boolean(false));
        mandatoryMap.put("txtPassportLastName", new Boolean(false));
        mandatoryMap.put("cboPassportTitle", new Boolean(false));
        mandatoryMap.put("txtPassportNo", new Boolean(false));
        mandatoryMap.put("txtPassportIssueAuth", new Boolean(false));
        mandatoryMap.put("cboPassportIssuePlace", new Boolean(false));
        mandatoryMap.put("cboEmpLevelEducation", new Boolean(false));
        mandatoryMap.put("txtNameOfSchool", new Boolean(false));
        mandatoryMap.put("tdtDateOfPassing", new Boolean(false));
        mandatoryMap.put("cboSpecilization", new Boolean(false));
        mandatoryMap.put("txtUniversity", new Boolean(false));
        mandatoryMap.put("txtMarksScored", new Boolean(false));
        mandatoryMap.put("txtTotMarks", new Boolean(false));
        mandatoryMap.put("txtTotMarksPer", new Boolean(false));
        mandatoryMap.put("cboGrade", new Boolean(false));
        mandatoryMap.put("cboTechnicalQualificationType", new Boolean(false));
        mandatoryMap.put("txtNameOfTechInst", new Boolean(false));
        mandatoryMap.put("tdtTechnicalQualificationDateOfPassing", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationMarksScored", new Boolean(false));
        mandatoryMap.put("cboTechnicalQualificationSpecilization", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationUniversity", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationTotMarks", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationTotMarksPer", new Boolean(false));
        mandatoryMap.put("cboLanguageType", new Boolean(false));
        mandatoryMap.put("rdoLanguageRead", new Boolean(false));
        mandatoryMap.put("rdoLanguageWrite", new Boolean(false));
        mandatoryMap.put("rdoLanguageReadeSpeak", new Boolean(false));
        mandatoryMap.put("cboReletaionShip", new Boolean(true));
        mandatoryMap.put("cboReleationFHTitle", new Boolean(true));
        mandatoryMap.put("txtReleationFHFirstName", new Boolean(false));
        mandatoryMap.put("txtReleationFHMiddleName", new Boolean(false));
        mandatoryMap.put("txtReleationFHLastName", new Boolean(false));
        mandatoryMap.put("tdtRelationShipDateofBirth", new Boolean(false));
        mandatoryMap.put("cboFamilyMemEducation", new Boolean(false));
        mandatoryMap.put("cboFamilyMemberProf", new Boolean(false));
        mandatoryMap.put("rdoDependentYes", new Boolean(false));
        mandatoryMap.put("cboDomicileState", new Boolean(false));
        mandatoryMap.put("txtDrivingLicenceNo", new Boolean(false));
        mandatoryMap.put("txtEmpID", new Boolean(true));
        mandatoryMap.put("cboZonalOffice",new Boolean(true));
    }
    
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    /** Making Maritial Status field visible or invisible according to
     *the custoemertype wheher its major or minor **/
    
    private void makeMaritialStatusVisible(boolean flag){
        panMaritalStatus.setVisible(flag);
        lblMaritalStatus.setVisible(flag);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoGender = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMaritalStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoGradutionIncrement = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCAIIBPART1Increment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCAIIBPART2Increment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAnyotherIncrement = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSignature = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoClubMembership = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLanguage = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLoanPreCloser = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLiableForTransfer = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDependent = new com.see.truetransact.uicomponent.CButtonGroup();
        panCustomer = new com.see.truetransact.uicomponent.CPanel();
        tabIndCust = new com.see.truetransact.uicomponent.CTabbedPane();
        panPersonalInfo = new com.see.truetransact.uicomponent.CPanel();
        panAdditionalInfo2 = new com.see.truetransact.uicomponent.CPanel();
        panAdditionalInfo = new com.see.truetransact.uicomponent.CPanel();
        lblFirstName = new com.see.truetransact.uicomponent.CLabel();
        lblMiddleName = new com.see.truetransact.uicomponent.CLabel();
        lblMotherLastName = new com.see.truetransact.uicomponent.CLabel();
        txtMiddleName = new com.see.truetransact.uicomponent.CTextField();
        txtLastName = new com.see.truetransact.uicomponent.CTextField();
        lblGender = new com.see.truetransact.uicomponent.CLabel();
        panGender = new com.see.truetransact.uicomponent.CPanel();
        rdoGender_Male = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGender_Female = new com.see.truetransact.uicomponent.CRadioButton();
        lblDateOfBirth = new com.see.truetransact.uicomponent.CLabel();
        lblMaritalStatus = new com.see.truetransact.uicomponent.CLabel();
        panMaritalStatus = new com.see.truetransact.uicomponent.CPanel();
        rdoMaritalStatus_Single = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMaritalStatus_Married = new com.see.truetransact.uicomponent.CRadioButton();
        panFirstName = new com.see.truetransact.uicomponent.CPanel();
        txtFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboTitle = new com.see.truetransact.uicomponent.CComboBox();
        lblEmpID = new com.see.truetransact.uicomponent.CLabel();
        txtEmpID = new com.see.truetransact.uicomponent.CTextField();
        lblFatherFirstName = new com.see.truetransact.uicomponent.CLabel();
        panFirstName1 = new com.see.truetransact.uicomponent.CPanel();
        txtFatherFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboFsatherTitle = new com.see.truetransact.uicomponent.CComboBox();
        txtMotherMiddleName = new com.see.truetransact.uicomponent.CTextField();
        txtMotherLastName = new com.see.truetransact.uicomponent.CTextField();
        lblLastName = new com.see.truetransact.uicomponent.CLabel();
        lblMotheMiddleName = new com.see.truetransact.uicomponent.CLabel();
        panHusband_father = new com.see.truetransact.uicomponent.CPanel();
        rdoFather = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHusband = new com.see.truetransact.uicomponent.CRadioButton();
        lblMotheFirstName = new com.see.truetransact.uicomponent.CLabel();
        panFirstName2 = new com.see.truetransact.uicomponent.CPanel();
        txtMotherFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboMotherTitle = new com.see.truetransact.uicomponent.CComboBox();
        txtFatherMiddleName = new com.see.truetransact.uicomponent.CTextField();
        lblFatherMiddleName = new com.see.truetransact.uicomponent.CLabel();
        lblFatherLastName = new com.see.truetransact.uicomponent.CLabel();
        txtFatherLastName = new com.see.truetransact.uicomponent.CTextField();
        txtUIDNo = new com.see.truetransact.uicomponent.CTextField();
        lblUIDNo = new com.see.truetransact.uicomponent.CLabel();
        lblIDCardNoNo = new com.see.truetransact.uicomponent.CLabel();
        txtIDCardNoNo = new com.see.truetransact.uicomponent.CTextField();
        lblPanNumber = new com.see.truetransact.uicomponent.CLabel();
        txtPanNumber = new com.see.truetransact.uicomponent.CTextField();
        lblCaste = new com.see.truetransact.uicomponent.CLabel();
        cboCaste = new com.see.truetransact.uicomponent.CComboBox();
        lblPhysicalHandicap = new com.see.truetransact.uicomponent.CLabel();
        txtPlaceOfBirth = new com.see.truetransact.uicomponent.CTextField();
        lblPlaceOfBirth = new com.see.truetransact.uicomponent.CLabel();
        lblDomicileState = new com.see.truetransact.uicomponent.CLabel();
        lblHomeTown = new com.see.truetransact.uicomponent.CLabel();
        txtHomeTown = new com.see.truetransact.uicomponent.CTextField();
        cboDomicileState = new com.see.truetransact.uicomponent.CComboBox();
        txtEmailId = new com.see.truetransact.uicomponent.CTextField();
        lblEmailId = new com.see.truetransact.uicomponent.CLabel();
        tdtDateOfBirth = new com.see.truetransact.uicomponent.CDateField();
        cboBloodGroup = new com.see.truetransact.uicomponent.CComboBox();
        lblBloodGroup = new com.see.truetransact.uicomponent.CLabel();
        panparticulars = new com.see.truetransact.uicomponent.CPanel();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtMajorHealthProbeem = new com.see.truetransact.uicomponent.CTextArea();
        lblMajorHealthProbeem = new com.see.truetransact.uicomponent.CLabel();
        cboReligion = new com.see.truetransact.uicomponent.CComboBox();
        lblReligion = new com.see.truetransact.uicomponent.CLabel();
        txtDrivingLicenceNo = new com.see.truetransact.uicomponent.CTextField();
        lblDrivingLicenceNo = new com.see.truetransact.uicomponent.CLabel();
        tdtDLRenewalDate = new com.see.truetransact.uicomponent.CDateField();
        lblDLRenewalDate = new com.see.truetransact.uicomponent.CLabel();
        panPhysicalHandicap = new com.see.truetransact.uicomponent.CPanel();
        srpTxtAreaHandicap = new com.see.truetransact.uicomponent.CScrollPane();
        txtPhysicalyHandicap = new com.see.truetransact.uicomponent.CTextArea();
        panCustomerHistory = new com.see.truetransact.uicomponent.CPanel();
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
        txtAddrRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblAddrRemarks = new com.see.truetransact.uicomponent.CLabel();
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
        panPassPortDet = new com.see.truetransact.uicomponent.CPanel();
        panPassPortDetails = new com.see.truetransact.uicomponent.CPanel();
        panPassportFirstName = new com.see.truetransact.uicomponent.CPanel();
        txtPassportFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboPassportTitle = new com.see.truetransact.uicomponent.CComboBox();
        txtPassportMiddleName = new com.see.truetransact.uicomponent.CTextField();
        txtPassportLastName = new com.see.truetransact.uicomponent.CTextField();
        lblPassportFirstName = new com.see.truetransact.uicomponent.CLabel();
        lblPassportMiddleName = new com.see.truetransact.uicomponent.CLabel();
        lblPassportLastName = new com.see.truetransact.uicomponent.CLabel();
        txtPassportNo = new com.see.truetransact.uicomponent.CTextField();
        lblPassportNo = new com.see.truetransact.uicomponent.CLabel();
        tdtPassportValidUpto = new com.see.truetransact.uicomponent.CDateField();
        lblPassportValidUpto = new com.see.truetransact.uicomponent.CLabel();
        tdtPassportIssueDt = new com.see.truetransact.uicomponent.CDateField();
        lblPassportIssueDt = new com.see.truetransact.uicomponent.CLabel();
        txtPassportIssueAuth = new com.see.truetransact.uicomponent.CTextField();
        cboPassportIssuePlace = new com.see.truetransact.uicomponent.CComboBox();
        lblPassportIssuePlace = new com.see.truetransact.uicomponent.CLabel();
        lblPassportIssueAuth = new com.see.truetransact.uicomponent.CLabel();
        btnClearPassport = new com.see.truetransact.uicomponent.CButton();
        panContacts1 = new com.see.truetransact.uicomponent.CPanel();
        srpContactList = new com.see.truetransact.uicomponent.CScrollPane();
        tblContactList = new com.see.truetransact.uicomponent.CTable();
        panContactControl = new com.see.truetransact.uicomponent.CPanel();
        btnContactToMain = new com.see.truetransact.uicomponent.CButton();
        panCustomerHistory1 = new com.see.truetransact.uicomponent.CPanel();
        lblDealingPeriod1 = new com.see.truetransact.uicomponent.CLabel();
        panRelativesWorkingDetails = new com.see.truetransact.uicomponent.CPanel();
        panRelativesWorkingDetailsEntery = new com.see.truetransact.uicomponent.CPanel();
        lblReleativeStaffId = new com.see.truetransact.uicomponent.CLabel();
        cboRelativesWorkingReletionShip = new com.see.truetransact.uicomponent.CComboBox();
        lblRelativesWorkingReletionShip = new com.see.truetransact.uicomponent.CLabel();
        cboWorkingDesignation = new com.see.truetransact.uicomponent.CComboBox();
        lblWorkingDesignation = new com.see.truetransact.uicomponent.CLabel();
        panBranch = new com.see.truetransact.uicomponent.CPanel();
        txtReleativeStaffId = new com.see.truetransact.uicomponent.CTextField();
        btnReleativeStaffId = new com.see.truetransact.uicomponent.CButton();
        panFirstName4 = new com.see.truetransact.uicomponent.CPanel();
        txtRelativesWorkingFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboRelativesWorkingTittle = new com.see.truetransact.uicomponent.CComboBox();
        txtRelativesWorkingMiddleName = new com.see.truetransact.uicomponent.CTextField();
        txtRelativesWorkingLastName = new com.see.truetransact.uicomponent.CTextField();
        lblRelativesWorkingLastName = new com.see.truetransact.uicomponent.CLabel();
        lblRelativesWorkingMiddleName = new com.see.truetransact.uicomponent.CLabel();
        lblRelativesWorkingFirstName = new com.see.truetransact.uicomponent.CLabel();
        cboRelativesWorkingBranch = new com.see.truetransact.uicomponent.CComboBox();
        lblRelativesWorkingBranch = new com.see.truetransact.uicomponent.CLabel();
        panBtnRelativeWorkingBramch = new com.see.truetransact.uicomponent.CPanel();
        btnRelativeWorkingBramchDetSave = new com.see.truetransact.uicomponent.CButton();
        btnRelativeWorkingBramchDetNew = new com.see.truetransact.uicomponent.CButton();
        btnRelativeWorkingBramchDetDel = new com.see.truetransact.uicomponent.CButton();
        panTechnicalQualificationTable1 = new com.see.truetransact.uicomponent.CPanel();
        srpRelativesWorkingDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblReleativeWorkingInBank = new com.see.truetransact.uicomponent.CTable();
        panFamilyDetails = new com.see.truetransact.uicomponent.CPanel();
        panFamilyDateEntery = new com.see.truetransact.uicomponent.CPanel();
        lblReletaionShip = new com.see.truetransact.uicomponent.CLabel();
        cboReletaionShip = new com.see.truetransact.uicomponent.CComboBox();
        panFirstName3 = new com.see.truetransact.uicomponent.CPanel();
        txtReleationFHFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboReleationFHTitle = new com.see.truetransact.uicomponent.CComboBox();
        lblReleationFHFirstName = new com.see.truetransact.uicomponent.CLabel();
        lblReleationFHMiddleName = new com.see.truetransact.uicomponent.CLabel();
        txtReleationFHMiddleName = new com.see.truetransact.uicomponent.CTextField();
        txtReleationFHLastName = new com.see.truetransact.uicomponent.CTextField();
        lblReleationFHLastName = new com.see.truetransact.uicomponent.CLabel();
        tdtRelationShipDateofBirth = new com.see.truetransact.uicomponent.CDateField();
        lblRelationShipDateofBirth = new com.see.truetransact.uicomponent.CLabel();
        cboFamilyMemEducation = new com.see.truetransact.uicomponent.CComboBox();
        lblFamilyMemEducation = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeedWith = new com.see.truetransact.uicomponent.CLabel();
        panMaritalStatus1 = new com.see.truetransact.uicomponent.CPanel();
        rdoDependentYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDependentNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblDependent = new com.see.truetransact.uicomponent.CLabel();
        panBtnFamily = new com.see.truetransact.uicomponent.CPanel();
        btnfamiyDetSave = new com.see.truetransact.uicomponent.CButton();
        btnfamiyDetNew = new com.see.truetransact.uicomponent.CButton();
        btnfamiyDetDel = new com.see.truetransact.uicomponent.CButton();
        cboFamilyMemberProf = new com.see.truetransact.uicomponent.CComboBox();
        lblFamilyMemberProf = new com.see.truetransact.uicomponent.CLabel();
        lblLiableForTransfer = new com.see.truetransact.uicomponent.CLabel();
        panMaritalStatus2 = new com.see.truetransact.uicomponent.CPanel();
        rdoLiableForTransferYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLiableForTransferNo = new com.see.truetransact.uicomponent.CRadioButton();
        txtDepIncomePerannum = new com.see.truetransact.uicomponent.CTextField();
        lblDepIncomePerannum = new com.see.truetransact.uicomponent.CLabel();
        txtEmpWith = new com.see.truetransact.uicomponent.CTextField();
        panDependentTable = new com.see.truetransact.uicomponent.CPanel();
        srpFamily = new com.see.truetransact.uicomponent.CScrollPane();
        tblFamily = new com.see.truetransact.uicomponent.CTable();
        panDirectorReleative = new com.see.truetransact.uicomponent.CPanel();
        panDetailsOfRelativeDirector = new com.see.truetransact.uicomponent.CPanel();
        txtDirectorLastName = new com.see.truetransact.uicomponent.CTextField();
        lblDirectorLastName = new com.see.truetransact.uicomponent.CLabel();
        lblDirectorMiddleName = new com.see.truetransact.uicomponent.CLabel();
        txtDirectorMiddleName = new com.see.truetransact.uicomponent.CTextField();
        panFirstName5 = new com.see.truetransact.uicomponent.CPanel();
        txtDirectorFirstName = new com.see.truetransact.uicomponent.CTextField();
        cboDirectorTittle = new com.see.truetransact.uicomponent.CComboBox();
        lblDirectorFirstName = new com.see.truetransact.uicomponent.CLabel();
        cboDirectorReleationShip = new com.see.truetransact.uicomponent.CComboBox();
        lblDirectorReleationShip = new com.see.truetransact.uicomponent.CLabel();
        panBtnRelativeDirector = new com.see.truetransact.uicomponent.CPanel();
        btnRelativeDirectorDetSave = new com.see.truetransact.uicomponent.CButton();
        btnRelativeDirectorDetNew = new com.see.truetransact.uicomponent.CButton();
        btnRelativeDirectorDetDel = new com.see.truetransact.uicomponent.CButton();
        panTrainingTable = new com.see.truetransact.uicomponent.CPanel();
        srpDetailsOfRelativeDirector = new com.see.truetransact.uicomponent.CScrollPane();
        tblDetailsOfRelativeDirector = new com.see.truetransact.uicomponent.CTable();
        panMISKYC = new com.see.truetransact.uicomponent.CPanel();
        panAcademicEducation = new com.see.truetransact.uicomponent.CPanel();
        panAcademicEducationDateEntery = new com.see.truetransact.uicomponent.CPanel();
        lblSpecilization = new com.see.truetransact.uicomponent.CLabel();
        lblUniversity = new com.see.truetransact.uicomponent.CLabel();
        lblEmpLevelEducation = new com.see.truetransact.uicomponent.CLabel();
        cboEmpLevelEducation = new com.see.truetransact.uicomponent.CComboBox();
        lblTotMarks = new com.see.truetransact.uicomponent.CLabel();
        lblMarksScored = new com.see.truetransact.uicomponent.CLabel();
        lblTotMarksPer = new com.see.truetransact.uicomponent.CLabel();
        lblGrade = new com.see.truetransact.uicomponent.CLabel();
        cboGrade = new com.see.truetransact.uicomponent.CComboBox();
        lblDateOfPassing = new com.see.truetransact.uicomponent.CLabel();
        lblNameOfSchool = new com.see.truetransact.uicomponent.CLabel();
        txtTotMarksPer = new com.see.truetransact.uicomponent.CTextField();
        tdtDateOfPassing = new com.see.truetransact.uicomponent.CDateField();
        cboSpecilization = new com.see.truetransact.uicomponent.CComboBox();
        txtMarksScored = new com.see.truetransact.uicomponent.CTextField();
        txtUniversity = new com.see.truetransact.uicomponent.CTextField();
        txtTotMarks = new com.see.truetransact.uicomponent.CTextField();
        txtNameOfSchool = new com.see.truetransact.uicomponent.CTextField();
        panBtnAcademicEducation = new com.see.truetransact.uicomponent.CPanel();
        btnAcademicEducationDetSave = new com.see.truetransact.uicomponent.CButton();
        btnAcademicEducationDetNew = new com.see.truetransact.uicomponent.CButton();
        btnAcademicEducationDetDel = new com.see.truetransact.uicomponent.CButton();
        panAcademicEducationTable = new com.see.truetransact.uicomponent.CPanel();
        srpAcademicEducation = new com.see.truetransact.uicomponent.CScrollPane();
        tblAcademicEducation = new com.see.truetransact.uicomponent.CTable();
        panLanguageStatus = new com.see.truetransact.uicomponent.CPanel();
        panLanguageDate = new com.see.truetransact.uicomponent.CPanel();
        cboLanguageType = new com.see.truetransact.uicomponent.CComboBox();
        lblLanguageType = new com.see.truetransact.uicomponent.CLabel();
        panLangues = new com.see.truetransact.uicomponent.CPanel();
        rdoLanguageRead = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLanguageWrite = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLanguageReadeSpeak = new com.see.truetransact.uicomponent.CRadioButton();
        panBtnLangues1 = new com.see.truetransact.uicomponent.CPanel();
        btnLanguageDetSave = new com.see.truetransact.uicomponent.CButton();
        btnLanguageDetNew = new com.see.truetransact.uicomponent.CButton();
        btnLanguageDetDel = new com.see.truetransact.uicomponent.CButton();
        panLanguageTable = new com.see.truetransact.uicomponent.CPanel();
        srpLanguage = new com.see.truetransact.uicomponent.CScrollPane();
        tblLanguage = new com.see.truetransact.uicomponent.CTable();
        panAcademicEducation2 = new com.see.truetransact.uicomponent.CPanel();
        panTechnicalQualificationDateEntery = new com.see.truetransact.uicomponent.CPanel();
        lblTechnicalQualificationSpecilization = new com.see.truetransact.uicomponent.CLabel();
        lblTechnicalQualificationMarksScored = new com.see.truetransact.uicomponent.CLabel();
        lblTechnicalQualificationType = new com.see.truetransact.uicomponent.CLabel();
        cboTechnicalQualificationType = new com.see.truetransact.uicomponent.CComboBox();
        lblTechnicalQualificationTotMarks = new com.see.truetransact.uicomponent.CLabel();
        lblTechnicalQualificationUniversity = new com.see.truetransact.uicomponent.CLabel();
        lblTechnicalQualificationTotMarksPer = new com.see.truetransact.uicomponent.CLabel();
        lblTechnicalQualificationGrade = new com.see.truetransact.uicomponent.CLabel();
        cboTechnicalQualificationGrade = new com.see.truetransact.uicomponent.CComboBox();
        lblTechnicalQualificationDateOfPassing = new com.see.truetransact.uicomponent.CLabel();
        lblNameOfTechInst = new com.see.truetransact.uicomponent.CLabel();
        txtTechnicalQualificationTotMarksPer = new com.see.truetransact.uicomponent.CTextField();
        tdtTechnicalQualificationDateOfPassing = new com.see.truetransact.uicomponent.CDateField();
        cboTechnicalQualificationSpecilization = new com.see.truetransact.uicomponent.CComboBox();
        txtTechnicalQualificationUniversity = new com.see.truetransact.uicomponent.CTextField();
        txtTechnicalQualificationMarksScored = new com.see.truetransact.uicomponent.CTextField();
        txtTechnicalQualificationTotMarks = new com.see.truetransact.uicomponent.CTextField();
        txtNameOfTechInst = new com.see.truetransact.uicomponent.CTextField();
        panBtnTechnicalQualification2 = new com.see.truetransact.uicomponent.CPanel();
        btnTechnicalQualificationDetSave = new com.see.truetransact.uicomponent.CButton();
        btnTechnicalQualificationDetNew = new com.see.truetransact.uicomponent.CButton();
        btnTechnicalQualificationDetDel = new com.see.truetransact.uicomponent.CButton();
        panTechnicalQualificationTable = new com.see.truetransact.uicomponent.CPanel();
        srpTechnicalQualification = new com.see.truetransact.uicomponent.CScrollPane();
        tblTechnicalEducation = new com.see.truetransact.uicomponent.CTable();
        panIncomeParticulars = new com.see.truetransact.uicomponent.CPanel();
        panCustomerInfo1 = new com.see.truetransact.uicomponent.CPanel();
        panOfficeDetails = new com.see.truetransact.uicomponent.CPanel();
        panTrainingDateEntery1 = new com.see.truetransact.uicomponent.CPanel();
        panGradutionIncrement = new com.see.truetransact.uicomponent.CPanel();
        rdoGradutionIncrementYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGradutionIncrementNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblGradutionIncrementYesNo = new com.see.truetransact.uicomponent.CLabel();
        panCAIIBPART = new com.see.truetransact.uicomponent.CPanel();
        rdoCAIIBPART1IncrementYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCAIIBPART1IncrementNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblCAIIBPART1IncrementYesNo = new com.see.truetransact.uicomponent.CLabel();
        lblGradutionIncrementReleasedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtGradutionIncrementReleasedDate = new com.see.truetransact.uicomponent.CDateField();
        tdtCAIIBPART1IncrementReleasedDate = new com.see.truetransact.uicomponent.CDateField();
        lblCAIIBPART1IncrementReleasedDate = new com.see.truetransact.uicomponent.CLabel();
        panCAIIBPART2 = new com.see.truetransact.uicomponent.CPanel();
        rdoCAIIBPART2Increment_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCAIIBPART2Increment_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCAIIBPART2IncrementYesNo = new com.see.truetransact.uicomponent.CLabel();
        tdtCAIIBPART2IncrementReleasedDate = new com.see.truetransact.uicomponent.CDateField();
        lblCAIIBPART2IncrementReleasedDate = new com.see.truetransact.uicomponent.CLabel();
        panAnyOtherIncrement = new com.see.truetransact.uicomponent.CPanel();
        rdoAnyOtherIncrement_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAnyOtherIncrement_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtAnyOtherIncrementInstitutionName = new com.see.truetransact.uicomponent.CTextField();
        lblAnyOtherIncrementYesNo = new com.see.truetransact.uicomponent.CLabel();
        lblAnyOtherIncrementInstitutionName = new com.see.truetransact.uicomponent.CLabel();
        tdtAnyOtherIncrementReleasedDate = new com.see.truetransact.uicomponent.CDateField();
        lblAnyOtherIncrementReleasedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLastIncrmentDate = new com.see.truetransact.uicomponent.CDateField();
        lblLastIncrmentDate = new com.see.truetransact.uicomponent.CLabel();
        panLossOfpay = new com.see.truetransact.uicomponent.CPanel();
        txtLossPay_Months = new com.see.truetransact.uicomponent.CTextField();
        lblLossPay_Months = new com.see.truetransact.uicomponent.CLabel();
        txtLossOfpay_Days = new com.see.truetransact.uicomponent.CTextField();
        lblLossOfpay_Days = new com.see.truetransact.uicomponent.CLabel();
        lblAnyLossOfPay = new com.see.truetransact.uicomponent.CLabel();
        tdtNextIncrmentDate = new com.see.truetransact.uicomponent.CDateField();
        lblNextIncrmentDate = new com.see.truetransact.uicomponent.CLabel();
        txtPresentBasic = new com.see.truetransact.uicomponent.CTextField();
        lblPresentBasic = new com.see.truetransact.uicomponent.CLabel();
        panSignatureNo1 = new com.see.truetransact.uicomponent.CPanel();
        rdoSignature_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSignature_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSigNoYesNo = new com.see.truetransact.uicomponent.CLabel();
        txtSignatureNo = new com.see.truetransact.uicomponent.CTextField();
        lblSignatureNo = new com.see.truetransact.uicomponent.CLabel();
        lblSocietyMember = new com.see.truetransact.uicomponent.CLabel();
        lblSocietyMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtSocietyMemberNo = new com.see.truetransact.uicomponent.CTextField();
        cboSocietyMember = new com.see.truetransact.uicomponent.CComboBox();
        lblUnionMember = new com.see.truetransact.uicomponent.CLabel();
        cboUnionMember = new com.see.truetransact.uicomponent.CComboBox();
        txtClubName = new com.see.truetransact.uicomponent.CTextField();
        lblClubName = new com.see.truetransact.uicomponent.CLabel();
        lblClubMembership = new com.see.truetransact.uicomponent.CLabel();
        panClubDet = new com.see.truetransact.uicomponent.CPanel();
        rdoClubMembership_YES = new com.see.truetransact.uicomponent.CRadioButton();
        rdoClubMembership_No = new com.see.truetransact.uicomponent.CRadioButton();
        panPresent = new com.see.truetransact.uicomponent.CPanel();
        panPresentDetails = new com.see.truetransact.uicomponent.CPanel();
        panPresentWorkingDetails = new com.see.truetransact.uicomponent.CPanel();
        cboZonalOffice = new com.see.truetransact.uicomponent.CComboBox();
        lblZonalOffice = new com.see.truetransact.uicomponent.CLabel();
        cboPresentBranchId = new com.see.truetransact.uicomponent.CComboBox();
        lblPresentBranchId = new com.see.truetransact.uicomponent.CLabel();
        panDOB2 = new com.see.truetransact.uicomponent.CPanel();
        tdtWorkingSince = new com.see.truetransact.uicomponent.CDateField();
        lblWorkingSince = new com.see.truetransact.uicomponent.CLabel();
        cboReginoalOffice = new com.see.truetransact.uicomponent.CComboBox();
        lbReginoalOffice = new com.see.truetransact.uicomponent.CLabel();
        cboPresentGrade = new com.see.truetransact.uicomponent.CComboBox();
        lblPresentGrade = new com.see.truetransact.uicomponent.CLabel();
        cboDesignation = new com.see.truetransact.uicomponent.CComboBox();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        panJoiningDetails = new com.see.truetransact.uicomponent.CPanel();
        panMinDepositPeriod1 = new com.see.truetransact.uicomponent.CPanel();
        txtProbationPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboProbationPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblProbationPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblConfirmationDate = new com.see.truetransact.uicomponent.CLabel();
        tdtConfirmationDate = new com.see.truetransact.uicomponent.CDateField();
        tdtDateofRetirement = new com.see.truetransact.uicomponent.CDateField();
        lblDateofRetirement = new com.see.truetransact.uicomponent.CLabel();
        lblDateOfJoin = new com.see.truetransact.uicomponent.CLabel();
        tdtDateOfJoin = new com.see.truetransact.uicomponent.CDateField();
        lblPFNumber = new com.see.truetransact.uicomponent.CLabel();
        txtPFNumber = new com.see.truetransact.uicomponent.CTextField();
        cboPFAcNominee = new com.see.truetransact.uicomponent.CComboBox();
        lblPFAcNominee = new com.see.truetransact.uicomponent.CLabel();
        panPromotionInfo = new com.see.truetransact.uicomponent.CPanel();
        panPromotionDetails = new com.see.truetransact.uicomponent.CPanel();
        panPromotionTable = new com.see.truetransact.uicomponent.CPanel();
        srpPromotion = new com.see.truetransact.uicomponent.CScrollPane();
        tblPromotion = new com.see.truetransact.uicomponent.CTable();
        panPromotionDetailsinfo = new com.see.truetransact.uicomponent.CPanel();
        lblPromotionEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        lblPromotionEmployeeName = new com.see.truetransact.uicomponent.CLabel();
        lblPresentDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblPromotionLastGrade = new com.see.truetransact.uicomponent.CLabel();
        lblPromotionEffectiveDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPromotionEffectiveDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblPromotionCreatedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPromotionCreatedDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblPromotionBasicPay = new com.see.truetransact.uicomponent.CLabel();
        txtPromotionBasicPayValue = new com.see.truetransact.uicomponent.CTextField();
        lblPromotionGrade = new com.see.truetransact.uicomponent.CLabel();
        cboPromotionGrade = new com.see.truetransact.uicomponent.CComboBox();
        panPromotionButtons = new com.see.truetransact.uicomponent.CPanel();
        btnPromotionNew = new com.see.truetransact.uicomponent.CButton();
        btnPromotionSave = new com.see.truetransact.uicomponent.CButton();
        btnPromotionDelete = new com.see.truetransact.uicomponent.CButton();
        txtPromotionEmployeeName = new com.see.truetransact.uicomponent.CTextField();
        txtPromotionLastDesg = new com.see.truetransact.uicomponent.CTextField();
        txtPromotionLastGrade = new com.see.truetransact.uicomponent.CTextField();
        cboPromotedDesignation = new com.see.truetransact.uicomponent.CComboBox();
        lblPromotedDesignation = new com.see.truetransact.uicomponent.CLabel();
        txtPromotionSalId = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblPromotionSalId = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtNewBasic = new com.see.truetransact.uicomponent.CTextField();
        lblNewBasic = new com.see.truetransact.uicomponent.CLabel();
        txtPromotionEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        panLandDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmployeeLoan = new com.see.truetransact.uicomponent.CPanel();
        panEmployeeDateEntery = new com.see.truetransact.uicomponent.CPanel();
        cboEmployeeLoanType = new com.see.truetransact.uicomponent.CComboBox();
        lblEmployeeLoanType = new com.see.truetransact.uicomponent.CLabel();
        cboLoanAvailedBranch = new com.see.truetransact.uicomponent.CComboBox();
        lblLoanAvailedBranch = new com.see.truetransact.uicomponent.CLabel();
        txtLoanSanctionRefNo = new com.see.truetransact.uicomponent.CTextField();
        lblLoanSanctionRefNo = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanSanctionDate = new com.see.truetransact.uicomponent.CDateField();
        lblLoanSanctionDate = new com.see.truetransact.uicomponent.CLabel();
        txtLoanNo = new com.see.truetransact.uicomponent.CTextField();
        lblLoanNo = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAmount = new com.see.truetransact.uicomponent.CTextField();
        lblLoanAmount = new com.see.truetransact.uicomponent.CLabel();
        lblLoanRateofInterest = new com.see.truetransact.uicomponent.CLabel();
        txtLoanRateofInterest = new com.see.truetransact.uicomponent.CTextField();
        txtLoanInstallmentAmount = new com.see.truetransact.uicomponent.CTextField();
        lblLoanInstallmentAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLoanNoOfInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblLoanNoOfInstallments = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanRepaymentStartDate = new com.see.truetransact.uicomponent.CDateField();
        lblLoanRepaymentStartDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanCloserDate = new com.see.truetransact.uicomponent.CDateField();
        lblLoanCloserDate = new com.see.truetransact.uicomponent.CLabel();
        panLoanPreCloserYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoLoanPreCloserYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLoanPreCloserNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblLoanPreCloserYesNo = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanRepaymentEndDate = new com.see.truetransact.uicomponent.CDateField();
        lblLoanRepaymentEndDate = new com.see.truetransact.uicomponent.CLabel();
        txtLoanRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblLoanRemarks = new com.see.truetransact.uicomponent.CLabel();
        panBtnTermLoans = new com.see.truetransact.uicomponent.CPanel();
        btnTermLoansDetSave = new com.see.truetransact.uicomponent.CButton();
        btnTermLoansDetNew = new com.see.truetransact.uicomponent.CButton();
        btnTermLoansDetDel = new com.see.truetransact.uicomponent.CButton();
        panEmployeeLoanTable = new com.see.truetransact.uicomponent.CPanel();
        srpEmployeeLoan = new com.see.truetransact.uicomponent.CScrollPane();
        tblEmployeeLoan = new com.see.truetransact.uicomponent.CTable();
        panLanguageStatus1 = new com.see.truetransact.uicomponent.CPanel();
        panOprativeDetails = new com.see.truetransact.uicomponent.CPanel();
        panCustId1 = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNo = new com.see.truetransact.uicomponent.CButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblAccountType = new com.see.truetransact.uicomponent.CLabel();
        cboAccountType = new com.see.truetransact.uicomponent.CComboBox();
        cboSalaryCrBranch = new com.see.truetransact.uicomponent.CComboBox();
        lblSalaryCrBranch = new com.see.truetransact.uicomponent.CLabel();
        panBtnOprativeDetails = new com.see.truetransact.uicomponent.CPanel();
        btnOprativeDetailsDetSave = new com.see.truetransact.uicomponent.CButton();
        btnOprativeDetailsDetNew = new com.see.truetransact.uicomponent.CButton();
        btnOprativeDetailsDetDel = new com.see.truetransact.uicomponent.CButton();
        panLanguageTable1 = new com.see.truetransact.uicomponent.CPanel();
        srpOprative = new com.see.truetransact.uicomponent.CScrollPane();
        tblOprative = new com.see.truetransact.uicomponent.CTable();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(850, 650));
        setPreferredSize(new java.awt.Dimension(850, 670));

        panCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panCustomer.setLayout(new java.awt.GridBagLayout());

        tabIndCust.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabIndCust.setMinimumSize(new java.awt.Dimension(624, 390));
        tabIndCust.setPreferredSize(new java.awt.Dimension(624, 390));
        tabIndCust.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabIndCustStateChanged(evt);
            }
        });
        tabIndCust.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabIndCustMouseClicked(evt);
            }
        });

        panPersonalInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Personal Info"));
        panPersonalInfo.setName("panPersonalInfo");
        panPersonalInfo.setLayout(new java.awt.GridBagLayout());

        panAdditionalInfo2.setMinimumSize(new java.awt.Dimension(678, 330));
        panAdditionalInfo2.setName("panAdditionalInfo");
        panAdditionalInfo2.setPreferredSize(new java.awt.Dimension(678, 330));
        panAdditionalInfo2.setLayout(new java.awt.GridBagLayout());

        panAdditionalInfo.setMinimumSize(new java.awt.Dimension(550, 380));
        panAdditionalInfo.setName("panAdditionalInfo");
        panAdditionalInfo.setPreferredSize(new java.awt.Dimension(550, 380));
        panAdditionalInfo.setLayout(new java.awt.GridBagLayout());

        lblFirstName.setText("First Name");
        lblFirstName.setName("lblFirstName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblFirstName, gridBagConstraints);

        lblMiddleName.setText("Middle Name");
        lblMiddleName.setName("lblMiddleName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblMiddleName, gridBagConstraints);

        lblMotherLastName.setText("Last Name");
        lblMotherLastName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblMotherLastName, gridBagConstraints);

        txtMiddleName.setMaxLength(128);
        txtMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtMiddleName.setName("txtMiddleName");
        txtMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtMiddleName, gridBagConstraints);

        txtLastName.setMaxLength(128);
        txtLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtLastName.setName("txtLastName");
        txtLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtLastName, gridBagConstraints);

        lblGender.setText("Gender");
        lblGender.setName("lblGender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblGender, gridBagConstraints);

        panGender.setName("panGender");
        panGender.setLayout(new java.awt.GridBagLayout());

        rdoGender.add(rdoGender_Male);
        rdoGender_Male.setText("Male");
        rdoGender_Male.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoGender_Male.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoGender_Male.setName("rdoGender_Male");
        rdoGender_Male.setPreferredSize(new java.awt.Dimension(53, 21));
        rdoGender_Male.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGender_MaleActionPerformed(evt);
            }
        });
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
        rdoGender_Female.setMaximumSize(new java.awt.Dimension(69, 21));
        rdoGender_Female.setMinimumSize(new java.awt.Dimension(69, 21));
        rdoGender_Female.setName("rdoGender_Female");
        rdoGender_Female.setPreferredSize(new java.awt.Dimension(69, 21));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panAdditionalInfo.add(panGender, gridBagConstraints);

        lblDateOfBirth.setText("Date of Birth");
        lblDateOfBirth.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panAdditionalInfo.add(lblDateOfBirth, gridBagConstraints);

        lblMaritalStatus.setText("Marital Status");
        lblMaritalStatus.setName("lblMaritalStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblMaritalStatus, gridBagConstraints);

        panMaritalStatus.setName("panMaritalStatus");
        panMaritalStatus.setLayout(new java.awt.GridBagLayout());

        rdoMaritalStatus.add(rdoMaritalStatus_Single);
        rdoMaritalStatus_Single.setText("Single");
        rdoMaritalStatus_Single.setMaximumSize(new java.awt.Dimension(61, 21));
        rdoMaritalStatus_Single.setMinimumSize(new java.awt.Dimension(61, 21));
        rdoMaritalStatus_Single.setName("rdoMaritalStatus_Single");
        rdoMaritalStatus_Single.setPreferredSize(new java.awt.Dimension(61, 21));
        rdoMaritalStatus_Single.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMaritalStatus_SingleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaritalStatus.add(rdoMaritalStatus_Single, gridBagConstraints);

        rdoMaritalStatus.add(rdoMaritalStatus_Married);
        rdoMaritalStatus_Married.setText("Married");
        rdoMaritalStatus_Married.setName("rdoMaritalStatus_Married");
        rdoMaritalStatus_Married.setPreferredSize(new java.awt.Dimension(69, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaritalStatus.add(rdoMaritalStatus_Married, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panAdditionalInfo.add(panMaritalStatus, gridBagConstraints);

        panFirstName.setLayout(new java.awt.GridBagLayout());

        txtFirstName.setMaxLength(128);
        txtFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtFirstName.setName("txtFirstName");
        txtFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtFirstName.setValidation(new DefaultValidation());
        txtFirstName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFirstNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName.add(txtFirstName, gridBagConstraints);

        cboTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboTitle.setName("cboTitle");
        cboTitle.setPopupWidth(80);
        cboTitle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName.add(cboTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAdditionalInfo.add(panFirstName, gridBagConstraints);

        lblEmpID.setText("Employee ID");
        lblEmpID.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblEmpID, gridBagConstraints);

        txtEmpID.setEditable(false);
        txtEmpID.setMaxLength(10);
        txtEmpID.setMinimumSize(new java.awt.Dimension(150, 21));
        txtEmpID.setName("txtCustomerID");
        txtEmpID.setPreferredSize(new java.awt.Dimension(150, 21));
        txtEmpID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmpIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtEmpID, gridBagConstraints);

        lblFatherFirstName.setText("Father's Name");
        lblFatherFirstName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblFatherFirstName, gridBagConstraints);

        panFirstName1.setLayout(new java.awt.GridBagLayout());

        txtFatherFirstName.setMaxLength(128);
        txtFatherFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtFatherFirstName.setName("txtFirstName");
        txtFatherFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtFatherFirstName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName1.add(txtFatherFirstName, gridBagConstraints);

        cboFsatherTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboFsatherTitle.setName("cboTitle");
        cboFsatherTitle.setPopupWidth(80);
        cboFsatherTitle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName1.add(cboFsatherTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAdditionalInfo.add(panFirstName1, gridBagConstraints);

        txtMotherMiddleName.setMaxLength(128);
        txtMotherMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtMotherMiddleName.setName("txtMiddleName");
        txtMotherMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtMotherMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtMotherMiddleName, gridBagConstraints);

        txtMotherLastName.setMaxLength(128);
        txtMotherLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtMotherLastName.setName("txtLastName");
        txtMotherLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtMotherLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtMotherLastName, gridBagConstraints);

        lblLastName.setText("Last Name");
        lblLastName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblLastName, gridBagConstraints);

        lblMotheMiddleName.setText("Middle Name");
        lblMotheMiddleName.setName("lblMiddleName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 7);
        panAdditionalInfo.add(lblMotheMiddleName, gridBagConstraints);

        panHusband_father.setName("panMaritalStatus");
        panHusband_father.setLayout(new java.awt.GridBagLayout());

        rdoFather.setText("Father's Name");
        rdoFather.setMaximumSize(new java.awt.Dimension(90, 21));
        rdoFather.setMinimumSize(new java.awt.Dimension(90, 21));
        rdoFather.setName("rdoFather");
        rdoFather.setPreferredSize(new java.awt.Dimension(90, 21));
        rdoFather.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFatherActionPerformed(evt);
            }
        });
        rdoFather.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoFatherFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panHusband_father.add(rdoFather, gridBagConstraints);

        rdoHusband.setText("Husband's Name");
        rdoHusband.setToolTipText("husband_father");
        rdoHusband.setMaximumSize(new java.awt.Dimension(90, 21));
        rdoHusband.setMinimumSize(new java.awt.Dimension(123, 21));
        rdoHusband.setName("rdoHusband");
        rdoHusband.setPreferredSize(new java.awt.Dimension(123, 21));
        rdoHusband.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHusbandActionPerformed(evt);
            }
        });
        rdoHusband.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoHusbandFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panHusband_father.add(rdoHusband, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panAdditionalInfo.add(panHusband_father, gridBagConstraints);

        lblMotheFirstName.setText("Mother's Name");
        lblMotheFirstName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
        panAdditionalInfo.add(lblMotheFirstName, gridBagConstraints);

        panFirstName2.setLayout(new java.awt.GridBagLayout());

        txtMotherFirstName.setMaxLength(128);
        txtMotherFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtMotherFirstName.setName("txtFirstName");
        txtMotherFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtMotherFirstName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName2.add(txtMotherFirstName, gridBagConstraints);

        cboMotherTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboMotherTitle.setName("cboTitle");
        cboMotherTitle.setPopupWidth(80);
        cboMotherTitle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName2.add(cboMotherTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAdditionalInfo.add(panFirstName2, gridBagConstraints);

        txtFatherMiddleName.setMaxLength(128);
        txtFatherMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtFatherMiddleName.setName("txtMiddleName");
        txtFatherMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtFatherMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtFatherMiddleName, gridBagConstraints);

        lblFatherMiddleName.setText("Middle Name");
        lblFatherMiddleName.setName("lblMiddleName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblFatherMiddleName, gridBagConstraints);

        lblFatherLastName.setText("Last Name");
        lblFatherLastName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblFatherLastName, gridBagConstraints);

        txtFatherLastName.setMaxLength(128);
        txtFatherLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtFatherLastName.setName("txtLastName");
        txtFatherLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtFatherLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtFatherLastName, gridBagConstraints);

        txtUIDNo.setMaxLength(256);
        txtUIDNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUIDNo.setName("txtRemarks");
        txtUIDNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtUIDNo, gridBagConstraints);

        lblUIDNo.setText("U ID Number");
        lblUIDNo.setName("lblRemarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblUIDNo, gridBagConstraints);

        lblIDCardNoNo.setText(" ID Card Number");
        lblIDCardNoNo.setName("lblRemarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAdditionalInfo.add(lblIDCardNoNo, gridBagConstraints);

        txtIDCardNoNo.setMaxLength(256);
        txtIDCardNoNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIDCardNoNo.setName("txtRemarks");
        txtIDCardNoNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtIDCardNoNo, gridBagConstraints);

        lblPanNumber.setText("PAN Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblPanNumber, gridBagConstraints);

        txtPanNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPanNumber.setValidation(new DefaultValidation());
        txtPanNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPanNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtPanNumber, gridBagConstraints);

        lblCaste.setText("Caste");
        lblCaste.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblCaste, gridBagConstraints);

        cboCaste.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCaste.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(cboCaste, gridBagConstraints);

        lblPhysicalHandicap.setText("If Physically Handicaped");
        lblPhysicalHandicap.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblPhysicalHandicap, gridBagConstraints);

        txtPlaceOfBirth.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPlaceOfBirth.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
        panAdditionalInfo.add(txtPlaceOfBirth, gridBagConstraints);

        lblPlaceOfBirth.setText("Place Of Birth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblPlaceOfBirth, gridBagConstraints);

        lblDomicileState.setText("Domicile State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblDomicileState, gridBagConstraints);

        lblHomeTown.setText("Home Town");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblHomeTown, gridBagConstraints);

        txtHomeTown.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHomeTown.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtHomeTown, gridBagConstraints);

        cboDomicileState.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDomicileState.setName("cboState");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(cboDomicileState, gridBagConstraints);

        txtEmailId.setMaxLength(128);
        txtEmailId.setMinimumSize(new java.awt.Dimension(200, 21));
        txtEmailId.setName("txtLastName");
        txtEmailId.setPreferredSize(new java.awt.Dimension(200, 21));
        txtEmailId.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtEmailId, gridBagConstraints);

        lblEmailId.setText("Email ID");
        lblEmailId.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblEmailId, gridBagConstraints);

        tdtDateOfBirth.setName("tdtDateOfBirth");
        tdtDateOfBirth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateOfBirthFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(tdtDateOfBirth, gridBagConstraints);

        cboBloodGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBloodGroup.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(cboBloodGroup, gridBagConstraints);

        lblBloodGroup.setText("Blood Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblBloodGroup, gridBagConstraints);

        panparticulars.setMinimumSize(new java.awt.Dimension(150, 30));
        panparticulars.setPreferredSize(new java.awt.Dimension(150, 30));
        panparticulars.setLayout(new java.awt.GridBagLayout());

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(150, 30));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(200, 30));

        txtMajorHealthProbeem.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtMajorHealthProbeem.setLineWrap(true);
        txtMajorHealthProbeem.setMinimumSize(new java.awt.Dimension(104, 30));
        txtMajorHealthProbeem.setPreferredSize(new java.awt.Dimension(104, 30));
        srpTxtAreaParticulars.setViewportView(txtMajorHealthProbeem);

        panparticulars.add(srpTxtAreaParticulars, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        panAdditionalInfo.add(panparticulars, gridBagConstraints);

        lblMajorHealthProbeem.setText("If Major Health Problem");
        lblMajorHealthProbeem.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblMajorHealthProbeem, gridBagConstraints);

        cboReligion.setMinimumSize(new java.awt.Dimension(100, 21));
        cboReligion.setName("cboProfession");
        cboReligion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReligionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(cboReligion, gridBagConstraints);

        lblReligion.setText("Religion");
        lblReligion.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblReligion, gridBagConstraints);

        txtDrivingLicenceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDrivingLicenceNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(txtDrivingLicenceNo, gridBagConstraints);

        lblDrivingLicenceNo.setText("Driving Licence No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(lblDrivingLicenceNo, gridBagConstraints);

        tdtDLRenewalDate.setName("tdtDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAdditionalInfo.add(tdtDLRenewalDate, gridBagConstraints);

        lblDLRenewalDate.setText("DL Renewal Date");
        lblDLRenewalDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panAdditionalInfo.add(lblDLRenewalDate, gridBagConstraints);

        panPhysicalHandicap.setPreferredSize(new java.awt.Dimension(150, 30));
        panPhysicalHandicap.setLayout(new java.awt.GridBagLayout());

        srpTxtAreaHandicap.setMinimumSize(new java.awt.Dimension(150, 30));
        srpTxtAreaHandicap.setPreferredSize(new java.awt.Dimension(200, 30));

        txtPhysicalyHandicap.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtPhysicalyHandicap.setLineWrap(true);
        txtPhysicalyHandicap.setMinimumSize(new java.awt.Dimension(104, 30));
        txtPhysicalyHandicap.setPreferredSize(new java.awt.Dimension(104, 30));
        srpTxtAreaHandicap.setViewportView(txtPhysicalyHandicap);

        panPhysicalHandicap.add(srpTxtAreaHandicap, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        panAdditionalInfo.add(panPhysicalHandicap, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAdditionalInfo2.add(panAdditionalInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPersonalInfo.add(panAdditionalInfo2, gridBagConstraints);

        tabIndCust.addTab("Personal Details", panPersonalInfo);

        panCustomerHistory.setMinimumSize(new java.awt.Dimension(800, 600));
        panCustomerHistory.setPreferredSize(new java.awt.Dimension(800, 600));
        panCustomerHistory.setLayout(new java.awt.GridBagLayout());

        panPhotoSign.setMinimumSize(new java.awt.Dimension(800, 210));
        panPhotoSign.setPreferredSize(new java.awt.Dimension(800, 200));
        panPhotoSign.setLayout(new java.awt.GridBagLayout());

        panPhoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Photograph"));
        panPhoto.setMinimumSize(new java.awt.Dimension(109, 96));
        panPhoto.setPreferredSize(new java.awt.Dimension(229, 184));
        panPhoto.setLayout(new java.awt.GridBagLayout());

        srpPhotoLoad.setMinimumSize(new java.awt.Dimension(0, 0));
        srpPhotoLoad.setPreferredSize(new java.awt.Dimension(120, 150));
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

        panSign.add(panSignButtons, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPhotoSign.add(panSign, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panCustomerHistory.add(panPhotoSign, gridBagConstraints);

        panContactInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Address Detail"));
        panContactInfo.setName("panContactInfo");
        panContactInfo.setLayout(new java.awt.GridBagLayout());

        panAddress.setMinimumSize(new java.awt.Dimension(360, 165));
        panAddress.setName("panAddress");
        panAddress.setPreferredSize(new java.awt.Dimension(360, 165));
        panAddress.setLayout(new java.awt.GridBagLayout());

        panAddressDetails.setMinimumSize(new java.awt.Dimension(349, 165));
        panAddressDetails.setName("panAddressDetails");
        panAddressDetails.setPreferredSize(new java.awt.Dimension(349, 165));
        panAddressDetails.setLayout(new java.awt.GridBagLayout());

        panAddressType.setLayout(new java.awt.GridBagLayout());

        lblAddressType.setText("Address Type");
        lblAddressType.setName("lblAddressType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(lblAddressType, gridBagConstraints);

        txtStreet.setMaxLength(256);
        txtStreet.setMinimumSize(new java.awt.Dimension(200, 21));
        txtStreet.setName("txtStreet");
        txtStreet.setPreferredSize(new java.awt.Dimension(200, 21));
        txtStreet.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panAddressType.add(txtStreet, gridBagConstraints);

        lblArea.setText("Area");
        lblArea.setName("lblArea");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(lblArea, gridBagConstraints);

        lblStreet.setText("Street");
        lblStreet.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAddressType.add(lblStreet, gridBagConstraints);

        txtArea.setMaxLength(128);
        txtArea.setMinimumSize(new java.awt.Dimension(200, 21));
        txtArea.setName("txtArea");
        txtArea.setPreferredSize(new java.awt.Dimension(200, 21));
        txtArea.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddressType.add(txtArea, gridBagConstraints);

        cboAddressType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAddressType.setName("cboAddressType");
        cboAddressType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAddressTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panAddressType.add(cboAddressType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAddressDetails.add(panAddressType, gridBagConstraints);

        panCountryDetails.setLayout(new java.awt.GridBagLayout());

        panCountry.setLayout(new java.awt.GridBagLayout());

        lblCity.setText("City");
        lblCity.setName("lblCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 6);
        panCountry.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCity.setName("cboCity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panCountry.add(cboCity, gridBagConstraints);

        lblState.setText("State");
        lblState.setName("lblState");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCountry.add(lblState, gridBagConstraints);

        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        cboState.setName("cboState");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 0);
        panCountry.add(cboState, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panCountryDetails.add(panCountry, gridBagConstraints);

        panCity.setLayout(new java.awt.GridBagLayout());

        lblPincode.setText("Pincode");
        lblPincode.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCity.add(lblPincode, gridBagConstraints);

        txtPincode.setMaxLength(16);
        txtPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPincode.setName("txtPincode");
        txtPincode.setValidation(new PincodeValidation_IN());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panCity.add(txtPincode, gridBagConstraints);

        lblCountry.setText("Country");
        lblCountry.setName("lblCountry");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 1, 4);
        panCity.add(lblCountry, gridBagConstraints);

        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCountry.setName("cboCountry");
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 48, 0, 0);
        panAddressDetails.add(panCountryDetails, gridBagConstraints);

        panAddrRemarks.setLayout(new java.awt.GridBagLayout());

        txtAddrRemarks.setMinimumSize(new java.awt.Dimension(200, 21));
        txtAddrRemarks.setPreferredSize(new java.awt.Dimension(200, 21));
        txtAddrRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddrRemarks.add(txtAddrRemarks, gridBagConstraints);

        lblAddrRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAddrRemarks.add(lblAddrRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 31, 1, 0);
        panAddressDetails.add(panAddrRemarks, gridBagConstraints);

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

        panContactNo.setName("panContactNo");
        panContactNo.setLayout(new java.awt.GridBagLayout());

        panTeleCommunication.setName("panTeleCommunication");
        panTeleCommunication.setLayout(new java.awt.GridBagLayout());

        panTelecomDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTelecomDetails.setName("panTelecomDetails");
        panTelecomDetails.setLayout(new java.awt.GridBagLayout());

        panPhoneType.setName("panPhoneType");
        panPhoneType.setLayout(new java.awt.GridBagLayout());

        panPhoneSave.setLayout(new java.awt.GridBagLayout());

        btnContactNoAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnContactNoAdd.setMaximumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setMinimumSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setName("btnContactNoAdd");
        btnContactNoAdd.setPreferredSize(new java.awt.Dimension(29, 27));
        btnContactNoAdd.setEnabled(false);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPhoneType.add(panPhoneSave, gridBagConstraints);

        panPhoneAreaNumber.setLayout(new java.awt.GridBagLayout());

        lblPhoneType.setText("Phone Type");
        lblPhoneType.setName("lblPhoneType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblPhoneType, gridBagConstraints);

        lblPhoneNumber.setText("Number");
        lblPhoneNumber.setName("lblPhoneNumber");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblPhoneNumber, gridBagConstraints);

        txtPhoneNumber.setMaxLength(16);
        txtPhoneNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPhoneNumber.setName("txtPhoneNumber");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(txtPhoneNumber, gridBagConstraints);

        lblAreaCode.setText("Area Code");
        lblAreaCode.setName("lblAreaCode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(lblAreaCode, gridBagConstraints);

        txtAreaCode.setMaxLength(16);
        txtAreaCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAreaCode.setName("txtAreaCode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPhoneAreaNumber.add(txtAreaCode, gridBagConstraints);

        cboPhoneType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPhoneType.setName("cboPhoneType");
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

        panPhoneList.setName("panPhoneList");
        panPhoneList.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 2);
        panTelecomDetails.add(panPhoneList, gridBagConstraints);

        srpPhoneList.setMinimumSize(new java.awt.Dimension(200, 75));
        srpPhoneList.setName("srpPhoneList");
        srpPhoneList.setPreferredSize(new java.awt.Dimension(200, 75));

        tblPhoneList.setName("tblPhoneList");
        tblPhoneList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPhoneListMousePressed(evt);
            }
        });
        srpPhoneList.setViewportView(tblPhoneList);

        panTelecomDetails.add(srpPhoneList, new java.awt.GridBagConstraints());

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
        btnContactAdd.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnContactAdd.setMinimumSize(new java.awt.Dimension(140, 27));
        btnContactAdd.setName("btnContactAdd");
        btnContactAdd.setPreferredSize(new java.awt.Dimension(140, 27));
        btnContactAdd.setEnabled(false);
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
        btnContactNew.setName("btnContactNew");
        btnContactNew.setEnabled(false);
        btnContactNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContactNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panContactNo.add(btnContactNew, gridBagConstraints);

        btnContactDelete.setText("Delete");
        btnContactDelete.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnContactDelete.setName("btnContactDelete");
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
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panContactNo.add(btnContactDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panContactInfo.add(panContactNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCustomerHistory.add(panContactInfo, gridBagConstraints);

        panPassPortDet.setMinimumSize(new java.awt.Dimension(783, 190));
        panPassPortDet.setPreferredSize(new java.awt.Dimension(783, 190));
        panPassPortDet.setLayout(new java.awt.GridBagLayout());

        panPassPortDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("PassPort Detail"));
        panPassPortDetails.setMinimumSize(new java.awt.Dimension(550, 180));
        panPassPortDetails.setPreferredSize(new java.awt.Dimension(550, 180));
        panPassPortDetails.setLayout(new java.awt.GridBagLayout());

        panPassportFirstName.setLayout(new java.awt.GridBagLayout());

        txtPassportFirstName.setMaxLength(128);
        txtPassportFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtPassportFirstName.setName("txtFirstName");
        txtPassportFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtPassportFirstName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassportFirstName.add(txtPassportFirstName, gridBagConstraints);

        cboPassportTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboPassportTitle.setName("cboPassportTitle");
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassPortDetails.add(panPassportFirstName, gridBagConstraints);

        txtPassportMiddleName.setMaxLength(128);
        txtPassportMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPassportMiddleName.setName("txtMiddleName");
        txtPassportMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtPassportMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(txtPassportMiddleName, gridBagConstraints);

        txtPassportLastName.setMaxLength(128);
        txtPassportLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPassportLastName.setName("txtLastName");
        txtPassportLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtPassportLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(txtPassportLastName, gridBagConstraints);

        lblPassportFirstName.setText("First Name");
        lblPassportFirstName.setName("lblFirstName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportFirstName, gridBagConstraints);

        lblPassportMiddleName.setText("Middle Name");
        lblPassportMiddleName.setName("lblMiddleName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportMiddleName, gridBagConstraints);

        lblPassportLastName.setText("Last Name");
        lblPassportLastName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportLastName, gridBagConstraints);

        txtPassportNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPassportNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(txtPassportNo, gridBagConstraints);

        lblPassportNo.setText("PassPort Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportNo, gridBagConstraints);

        tdtPassportValidUpto.setName("tdtPassportValidUpto");
        tdtPassportValidUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPassportValidUptoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(tdtPassportValidUpto, gridBagConstraints);

        lblPassportValidUpto.setText("Valid upto");
        lblPassportValidUpto.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportValidUpto, gridBagConstraints);

        tdtPassportIssueDt.setName("tdtPassportIssueDt");
        tdtPassportIssueDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPassportIssueDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(tdtPassportIssueDt, gridBagConstraints);

        lblPassportIssueDt.setText("Issued Date");
        lblPassportIssueDt.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportIssueDt, gridBagConstraints);

        txtPassportIssueAuth.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPassportIssueAuth.setPreferredSize(new java.awt.Dimension(200, 21));
        txtPassportIssueAuth.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPassPortDetails.add(txtPassportIssueAuth, gridBagConstraints);

        cboPassportIssuePlace.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPassportIssuePlace.setName("cboCustomerType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(cboPassportIssuePlace, gridBagConstraints);

        lblPassportIssuePlace.setText("Place Of Issue");
        lblPassportIssuePlace.setName("lblResidentialStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportIssuePlace, gridBagConstraints);

        lblPassportIssueAuth.setText("Issueing Authority");
        lblPassportIssueAuth.setName("lblResidentialStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPassPortDetails.add(lblPassportIssueAuth, gridBagConstraints);

        btnClearPassport.setText("Clear Passport Details");
        btnClearPassport.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnClearPassport.setName("btnContactToMain");
        btnClearPassport.setEnabled(false);
        btnClearPassport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPassportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panPassPortDetails.add(btnClearPassport, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panPassPortDet.add(panPassPortDetails, gridBagConstraints);
        panPassPortDetails.getAccessibleContext().setAccessibleName("Passport Detail");

        panContacts1.setBorder(javax.swing.BorderFactory.createTitledBorder("Contacts"));
        panContacts1.setName("panContacts");
        panContacts1.setLayout(new java.awt.GridBagLayout());

        srpContactList.setMinimumSize(new java.awt.Dimension(210, 250));
        srpContactList.setName("srpContactList");
        srpContactList.setPreferredSize(new java.awt.Dimension(210, 250));

        tblContactList.setName("tblContactList");
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
        panContacts1.add(srpContactList, gridBagConstraints);

        panContactControl.setMinimumSize(new java.awt.Dimension(210, 33));
        panContactControl.setName("panContactControl");
        panContactControl.setPreferredSize(new java.awt.Dimension(210, 33));
        panContactControl.setLayout(new java.awt.GridBagLayout());

        btnContactToMain.setText("Set as Primary");
        btnContactToMain.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnContactToMain.setName("btnContactToMain");
        btnContactToMain.setEnabled(false);
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
        panContacts1.add(panContactControl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 3, 2);
        panPassPortDet.add(panContacts1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerHistory.add(panPassPortDet, gridBagConstraints);

        tabIndCust.addTab("Address,Passport,Sign,Photo", panCustomerHistory);
        panCustomerHistory.getAccessibleContext().setAccessibleName("Address,Passport,Sign,Photo");

        panCustomerHistory1.setMinimumSize(new java.awt.Dimension(600, 204));
        panCustomerHistory1.setPreferredSize(new java.awt.Dimension(800, 204));
        panCustomerHistory1.setLayout(new java.awt.GridBagLayout());

        lblDealingPeriod1.setMinimumSize(new java.awt.Dimension(200, 21));
        lblDealingPeriod1.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerHistory1.add(lblDealingPeriod1, gridBagConstraints);

        panRelativesWorkingDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Details of Relatives Working In the Bank"));
        panRelativesWorkingDetails.setMinimumSize(new java.awt.Dimension(840, 185));
        panRelativesWorkingDetails.setPreferredSize(new java.awt.Dimension(840, 185));
        panRelativesWorkingDetails.setLayout(new java.awt.GridBagLayout());

        panRelativesWorkingDetailsEntery.setMaximumSize(new java.awt.Dimension(500, 150));
        panRelativesWorkingDetailsEntery.setMinimumSize(new java.awt.Dimension(500, 150));
        panRelativesWorkingDetailsEntery.setPreferredSize(new java.awt.Dimension(500, 150));
        panRelativesWorkingDetailsEntery.setLayout(new java.awt.GridBagLayout());

        lblReleativeStaffId.setText("Staff Id");
        lblReleativeStaffId.setName("lblPrimaryOccupation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(lblReleativeStaffId, gridBagConstraints);

        cboRelativesWorkingReletionShip.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRelativesWorkingReletionShip.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(cboRelativesWorkingReletionShip, gridBagConstraints);

        lblRelativesWorkingReletionShip.setText("Releationship");
        lblRelativesWorkingReletionShip.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(lblRelativesWorkingReletionShip, gridBagConstraints);

        cboWorkingDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboWorkingDesignation.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(cboWorkingDesignation, gridBagConstraints);

        lblWorkingDesignation.setText("Designation");
        lblWorkingDesignation.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(lblWorkingDesignation, gridBagConstraints);

        panBranch.setMinimumSize(new java.awt.Dimension(130, 29));
        panBranch.setPreferredSize(new java.awt.Dimension(130, 29));
        panBranch.setLayout(new java.awt.GridBagLayout());

        txtReleativeStaffId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranch.add(txtReleativeStaffId, gridBagConstraints);

        btnReleativeStaffId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnReleativeStaffId.setToolTipText("Save");
        btnReleativeStaffId.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReleativeStaffId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnReleativeStaffId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReleativeStaffIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBranch.add(btnReleativeStaffId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRelativesWorkingDetailsEntery.add(panBranch, gridBagConstraints);

        panFirstName4.setLayout(new java.awt.GridBagLayout());

        txtRelativesWorkingFirstName.setMaxLength(128);
        txtRelativesWorkingFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtRelativesWorkingFirstName.setName("txtFirstName");
        txtRelativesWorkingFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtRelativesWorkingFirstName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName4.add(txtRelativesWorkingFirstName, gridBagConstraints);

        cboRelativesWorkingTittle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboRelativesWorkingTittle.setName("cboTitle");
        cboRelativesWorkingTittle.setPopupWidth(80);
        cboRelativesWorkingTittle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName4.add(cboRelativesWorkingTittle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRelativesWorkingDetailsEntery.add(panFirstName4, gridBagConstraints);

        txtRelativesWorkingMiddleName.setMaxLength(128);
        txtRelativesWorkingMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRelativesWorkingMiddleName.setName("txtMiddleName");
        txtRelativesWorkingMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRelativesWorkingMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(txtRelativesWorkingMiddleName, gridBagConstraints);

        txtRelativesWorkingLastName.setMaxLength(128);
        txtRelativesWorkingLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRelativesWorkingLastName.setName("txtLastName");
        txtRelativesWorkingLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRelativesWorkingLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(txtRelativesWorkingLastName, gridBagConstraints);

        lblRelativesWorkingLastName.setText("Last Name");
        lblRelativesWorkingLastName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(lblRelativesWorkingLastName, gridBagConstraints);

        lblRelativesWorkingMiddleName.setText("Middle Name");
        lblRelativesWorkingMiddleName.setName("lblMiddleName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(lblRelativesWorkingMiddleName, gridBagConstraints);

        lblRelativesWorkingFirstName.setText("First Name");
        lblRelativesWorkingFirstName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(lblRelativesWorkingFirstName, gridBagConstraints);

        cboRelativesWorkingBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRelativesWorkingBranch.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(cboRelativesWorkingBranch, gridBagConstraints);

        lblRelativesWorkingBranch.setText("Working At");
        lblRelativesWorkingBranch.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panRelativesWorkingDetailsEntery.add(lblRelativesWorkingBranch, gridBagConstraints);

        panBtnRelativeWorkingBramch.setLayout(new java.awt.GridBagLayout());

        btnRelativeWorkingBramchDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnRelativeWorkingBramchDetSave.setText("Save");
        btnRelativeWorkingBramchDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnRelativeWorkingBramchDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnRelativeWorkingBramchDetSave.setName("btnContactAdd");
        btnRelativeWorkingBramchDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnRelativeWorkingBramchDetSave.setEnabled(false);
        btnRelativeWorkingBramchDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelativeWorkingBramchDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnRelativeWorkingBramch.add(btnRelativeWorkingBramchDetSave, gridBagConstraints);

        btnRelativeWorkingBramchDetNew.setText("New");
        btnRelativeWorkingBramchDetNew.setName("btnContactNew");
        btnRelativeWorkingBramchDetNew.setEnabled(false);
        btnRelativeWorkingBramchDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelativeWorkingBramchDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnRelativeWorkingBramch.add(btnRelativeWorkingBramchDetNew, gridBagConstraints);

        btnRelativeWorkingBramchDetDel.setText("Delete");
        btnRelativeWorkingBramchDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnRelativeWorkingBramchDetDel.setName("btnContactDelete");
        btnRelativeWorkingBramchDetDel.setEnabled(false);
        btnRelativeWorkingBramchDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelativeWorkingBramchDetDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnRelativeWorkingBramch.add(btnRelativeWorkingBramchDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 3);
        panRelativesWorkingDetailsEntery.add(panBtnRelativeWorkingBramch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panRelativesWorkingDetails.add(panRelativesWorkingDetailsEntery, gridBagConstraints);

        panTechnicalQualificationTable1.setMinimumSize(new java.awt.Dimension(340, 150));
        panTechnicalQualificationTable1.setName("panContacts");
        panTechnicalQualificationTable1.setPreferredSize(new java.awt.Dimension(340, 150));
        panTechnicalQualificationTable1.setLayout(new java.awt.GridBagLayout());

        srpRelativesWorkingDetails.setMinimumSize(new java.awt.Dimension(340, 75));
        srpRelativesWorkingDetails.setName("srpContactList");
        srpRelativesWorkingDetails.setPreferredSize(new java.awt.Dimension(340, 75));

        tblReleativeWorkingInBank.setName("tblContactList");
        tblReleativeWorkingInBank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblReleativeWorkingInBankMousePressed(evt);
            }
        });
        srpRelativesWorkingDetails.setViewportView(tblReleativeWorkingInBank);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panTechnicalQualificationTable1.add(srpRelativesWorkingDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panRelativesWorkingDetails.add(panTechnicalQualificationTable1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerHistory1.add(panRelativesWorkingDetails, gridBagConstraints);

        panFamilyDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Family Details"));
        panFamilyDetails.setMinimumSize(new java.awt.Dimension(840, 225));
        panFamilyDetails.setPreferredSize(new java.awt.Dimension(840, 225));
        panFamilyDetails.setLayout(new java.awt.GridBagLayout());

        panFamilyDateEntery.setMaximumSize(new java.awt.Dimension(520, 150));
        panFamilyDateEntery.setMinimumSize(new java.awt.Dimension(520, 150));
        panFamilyDateEntery.setPreferredSize(new java.awt.Dimension(520, 150));
        panFamilyDateEntery.setLayout(new java.awt.GridBagLayout());

        lblReletaionShip.setText("ReletaionShip");
        lblReletaionShip.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblReletaionShip, gridBagConstraints);

        cboReletaionShip.setMinimumSize(new java.awt.Dimension(100, 21));
        cboReletaionShip.setName("cboProfession");
        cboReletaionShip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReletaionShipActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(cboReletaionShip, gridBagConstraints);

        panFirstName3.setLayout(new java.awt.GridBagLayout());

        txtReleationFHFirstName.setMaxLength(128);
        txtReleationFHFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtReleationFHFirstName.setName("txtFirstName");
        txtReleationFHFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtReleationFHFirstName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName3.add(txtReleationFHFirstName, gridBagConstraints);

        cboReleationFHTitle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboReleationFHTitle.setName("cboTitle");
        cboReleationFHTitle.setPopupWidth(80);
        cboReleationFHTitle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName3.add(cboReleationFHTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFamilyDateEntery.add(panFirstName3, gridBagConstraints);

        lblReleationFHFirstName.setText("First Name");
        lblReleationFHFirstName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblReleationFHFirstName, gridBagConstraints);

        lblReleationFHMiddleName.setText("Middle Name");
        lblReleationFHMiddleName.setName("lblMiddleName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblReleationFHMiddleName, gridBagConstraints);

        txtReleationFHMiddleName.setMaxLength(128);
        txtReleationFHMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtReleationFHMiddleName.setName("txtMiddleName");
        txtReleationFHMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtReleationFHMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(txtReleationFHMiddleName, gridBagConstraints);

        txtReleationFHLastName.setMaxLength(128);
        txtReleationFHLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtReleationFHLastName.setName("txtLastName");
        txtReleationFHLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtReleationFHLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(txtReleationFHLastName, gridBagConstraints);

        lblReleationFHLastName.setText("Last Name");
        lblReleationFHLastName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblReleationFHLastName, gridBagConstraints);

        tdtRelationShipDateofBirth.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtRelationShipDateofBirth.setName("tdtDateOfBirth");
        tdtRelationShipDateofBirth.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtRelationShipDateofBirth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRelationShipDateofBirthFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(tdtRelationShipDateofBirth, gridBagConstraints);

        lblRelationShipDateofBirth.setText("Date of Birth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblRelationShipDateofBirth, gridBagConstraints);

        cboFamilyMemEducation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFamilyMemEducation.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(cboFamilyMemEducation, gridBagConstraints);

        lblFamilyMemEducation.setText("Level  of Education");
        lblFamilyMemEducation.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblFamilyMemEducation, gridBagConstraints);

        lblEmployeedWith.setText("Employeed With");
        lblEmployeedWith.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblEmployeedWith, gridBagConstraints);

        panMaritalStatus1.setName("panMaritalStatus");
        panMaritalStatus1.setLayout(new java.awt.GridBagLayout());

        rdoDependent.add(rdoDependentYes);
        rdoDependentYes.setText("Yes");
        rdoDependentYes.setMaximumSize(new java.awt.Dimension(61, 21));
        rdoDependentYes.setMinimumSize(new java.awt.Dimension(61, 21));
        rdoDependentYes.setName("rdoMaritalStatus_Single");
        rdoDependentYes.setPreferredSize(new java.awt.Dimension(61, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaritalStatus1.add(rdoDependentYes, gridBagConstraints);

        rdoDependent.add(rdoDependentNo);
        rdoDependentNo.setText("No");
        rdoDependentNo.setName("rdoMaritalStatus_Married");
        rdoDependentNo.setPreferredSize(new java.awt.Dimension(69, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaritalStatus1.add(rdoDependentNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panFamilyDateEntery.add(panMaritalStatus1, gridBagConstraints);

        lblDependent.setText("Dependent");
        lblDependent.setName("lblMaritalStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblDependent, gridBagConstraints);

        panBtnFamily.setLayout(new java.awt.GridBagLayout());

        btnfamiyDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnfamiyDetSave.setText("Save");
        btnfamiyDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnfamiyDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnfamiyDetSave.setName("btnContactAdd");
        btnfamiyDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnfamiyDetSave.setEnabled(false);
        btnfamiyDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfamiyDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnFamily.add(btnfamiyDetSave, gridBagConstraints);

        btnfamiyDetNew.setText("New");
        btnfamiyDetNew.setName("btnContactNew");
        btnfamiyDetNew.setEnabled(false);
        btnfamiyDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfamiyDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnFamily.add(btnfamiyDetNew, gridBagConstraints);

        btnfamiyDetDel.setText("Delete");
        btnfamiyDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnfamiyDetDel.setName("btnContactDelete");
        btnfamiyDetDel.setEnabled(false);
        btnfamiyDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfamiyDetDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnFamily.add(btnfamiyDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 3);
        panFamilyDateEntery.add(panBtnFamily, gridBagConstraints);

        cboFamilyMemberProf.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFamilyMemberProf.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(cboFamilyMemberProf, gridBagConstraints);

        lblFamilyMemberProf.setText("Profession");
        lblFamilyMemberProf.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblFamilyMemberProf, gridBagConstraints);

        lblLiableForTransfer.setText("Liable For Transfer");
        lblLiableForTransfer.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblLiableForTransfer, gridBagConstraints);

        panMaritalStatus2.setName("panMaritalStatus");
        panMaritalStatus2.setLayout(new java.awt.GridBagLayout());

        rdoLiableForTransfer.add(rdoLiableForTransferYes);
        rdoLiableForTransferYes.setText("Yes");
        rdoLiableForTransferYes.setMaximumSize(new java.awt.Dimension(61, 21));
        rdoLiableForTransferYes.setMinimumSize(new java.awt.Dimension(61, 21));
        rdoLiableForTransferYes.setName("rdoMaritalStatus_Single");
        rdoLiableForTransferYes.setPreferredSize(new java.awt.Dimension(61, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaritalStatus2.add(rdoLiableForTransferYes, gridBagConstraints);

        rdoLiableForTransfer.add(rdoLiableForTransferNo);
        rdoLiableForTransferNo.setText("No");
        rdoLiableForTransferNo.setName("rdoMaritalStatus_Married");
        rdoLiableForTransferNo.setPreferredSize(new java.awt.Dimension(69, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMaritalStatus2.add(rdoLiableForTransferNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panFamilyDateEntery.add(panMaritalStatus2, gridBagConstraints);

        txtDepIncomePerannum.setMaxLength(128);
        txtDepIncomePerannum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepIncomePerannum.setName("txtCompany");
        txtDepIncomePerannum.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(txtDepIncomePerannum, gridBagConstraints);

        lblDepIncomePerannum.setText("Income per annum");
        lblDepIncomePerannum.setName("lblPrimaryOccupation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(lblDepIncomePerannum, gridBagConstraints);

        txtEmpWith.setMaxLength(128);
        txtEmpWith.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmpWith.setName("txtCompany");
        txtEmpWith.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFamilyDateEntery.add(txtEmpWith, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panFamilyDetails.add(panFamilyDateEntery, gridBagConstraints);

        panDependentTable.setMinimumSize(new java.awt.Dimension(310, 150));
        panDependentTable.setName("panContacts");
        panDependentTable.setPreferredSize(new java.awt.Dimension(310, 150));
        panDependentTable.setLayout(new java.awt.GridBagLayout());

        srpFamily.setMinimumSize(new java.awt.Dimension(240, 75));
        srpFamily.setName("srpContactList");
        srpFamily.setPreferredSize(new java.awt.Dimension(240, 75));

        tblFamily.setName("tblContactList");
        tblFamily.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblFamilyMousePressed(evt);
            }
        });
        srpFamily.setViewportView(tblFamily);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panDependentTable.add(srpFamily, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panFamilyDetails.add(panDependentTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerHistory1.add(panFamilyDetails, gridBagConstraints);

        panDirectorReleative.setBorder(javax.swing.BorderFactory.createTitledBorder("Details of Director Relatives in the Bank  "));
        panDirectorReleative.setMinimumSize(new java.awt.Dimension(840, 185));
        panDirectorReleative.setPreferredSize(new java.awt.Dimension(840, 185));
        panDirectorReleative.setLayout(new java.awt.GridBagLayout());

        panDetailsOfRelativeDirector.setMaximumSize(new java.awt.Dimension(500, 150));
        panDetailsOfRelativeDirector.setMinimumSize(new java.awt.Dimension(500, 150));
        panDetailsOfRelativeDirector.setPreferredSize(new java.awt.Dimension(500, 150));
        panDetailsOfRelativeDirector.setLayout(new java.awt.GridBagLayout());

        txtDirectorLastName.setMaxLength(128);
        txtDirectorLastName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtDirectorLastName.setName("txtLastName");
        txtDirectorLastName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtDirectorLastName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDetailsOfRelativeDirector.add(txtDirectorLastName, gridBagConstraints);

        lblDirectorLastName.setText("Last Name");
        lblDirectorLastName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDetailsOfRelativeDirector.add(lblDirectorLastName, gridBagConstraints);

        lblDirectorMiddleName.setText("Middle Name");
        lblDirectorMiddleName.setName("lblMiddleName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDetailsOfRelativeDirector.add(lblDirectorMiddleName, gridBagConstraints);

        txtDirectorMiddleName.setMaxLength(128);
        txtDirectorMiddleName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtDirectorMiddleName.setName("txtMiddleName");
        txtDirectorMiddleName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtDirectorMiddleName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDetailsOfRelativeDirector.add(txtDirectorMiddleName, gridBagConstraints);

        panFirstName5.setLayout(new java.awt.GridBagLayout());

        txtDirectorFirstName.setMaxLength(128);
        txtDirectorFirstName.setMinimumSize(new java.awt.Dimension(141, 21));
        txtDirectorFirstName.setName("txtFirstName");
        txtDirectorFirstName.setPreferredSize(new java.awt.Dimension(141, 21));
        txtDirectorFirstName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName5.add(txtDirectorFirstName, gridBagConstraints);

        cboDirectorTittle.setMinimumSize(new java.awt.Dimension(50, 21));
        cboDirectorTittle.setName("cboTitle");
        cboDirectorTittle.setPopupWidth(80);
        cboDirectorTittle.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panFirstName5.add(cboDirectorTittle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDetailsOfRelativeDirector.add(panFirstName5, gridBagConstraints);

        lblDirectorFirstName.setText("First Name");
        lblDirectorFirstName.setName("lblLastName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDetailsOfRelativeDirector.add(lblDirectorFirstName, gridBagConstraints);

        cboDirectorReleationShip.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDirectorReleationShip.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDetailsOfRelativeDirector.add(cboDirectorReleationShip, gridBagConstraints);

        lblDirectorReleationShip.setText("Releationship");
        lblDirectorReleationShip.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDetailsOfRelativeDirector.add(lblDirectorReleationShip, gridBagConstraints);

        panBtnRelativeDirector.setLayout(new java.awt.GridBagLayout());

        btnRelativeDirectorDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnRelativeDirectorDetSave.setText("Save");
        btnRelativeDirectorDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnRelativeDirectorDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnRelativeDirectorDetSave.setName("btnContactAdd");
        btnRelativeDirectorDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnRelativeDirectorDetSave.setEnabled(false);
        btnRelativeDirectorDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelativeDirectorDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnRelativeDirector.add(btnRelativeDirectorDetSave, gridBagConstraints);

        btnRelativeDirectorDetNew.setText("New");
        btnRelativeDirectorDetNew.setName("btnContactNew");
        btnRelativeDirectorDetNew.setEnabled(false);
        btnRelativeDirectorDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelativeDirectorDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnRelativeDirector.add(btnRelativeDirectorDetNew, gridBagConstraints);

        btnRelativeDirectorDetDel.setText("Delete");
        btnRelativeDirectorDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnRelativeDirectorDetDel.setName("btnContactDelete");
        btnRelativeDirectorDetDel.setEnabled(false);
        btnRelativeDirectorDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelativeDirectorDetDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnRelativeDirector.add(btnRelativeDirectorDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 26, 3);
        panDetailsOfRelativeDirector.add(panBtnRelativeDirector, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panDirectorReleative.add(panDetailsOfRelativeDirector, gridBagConstraints);

        panTrainingTable.setMinimumSize(new java.awt.Dimension(340, 150));
        panTrainingTable.setName("panContacts");
        panTrainingTable.setPreferredSize(new java.awt.Dimension(340, 150));
        panTrainingTable.setLayout(new java.awt.GridBagLayout());

        srpDetailsOfRelativeDirector.setMinimumSize(new java.awt.Dimension(340, 75));
        srpDetailsOfRelativeDirector.setName("srpContactList");
        srpDetailsOfRelativeDirector.setPreferredSize(new java.awt.Dimension(340, 75));

        tblDetailsOfRelativeDirector.setName("tblContactList");
        tblDetailsOfRelativeDirector.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDetailsOfRelativeDirectorMousePressed(evt);
            }
        });
        srpDetailsOfRelativeDirector.setViewportView(tblDetailsOfRelativeDirector);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panTrainingTable.add(srpDetailsOfRelativeDirector, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panDirectorReleative.add(panTrainingTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerHistory1.add(panDirectorReleative, gridBagConstraints);

        tabIndCust.addTab("Family Details", panCustomerHistory1);

        panMISKYC.setMinimumSize(new java.awt.Dimension(800, 600));
        panMISKYC.setPreferredSize(new java.awt.Dimension(800, 600));
        panMISKYC.setLayout(new java.awt.GridBagLayout());

        panAcademicEducation.setBorder(javax.swing.BorderFactory.createTitledBorder("Academic Education Details"));
        panAcademicEducation.setMinimumSize(new java.awt.Dimension(840, 185));
        panAcademicEducation.setPreferredSize(new java.awt.Dimension(840, 185));
        panAcademicEducation.setLayout(new java.awt.GridBagLayout());

        panAcademicEducationDateEntery.setMaximumSize(new java.awt.Dimension(500, 150));
        panAcademicEducationDateEntery.setMinimumSize(new java.awt.Dimension(500, 150));
        panAcademicEducationDateEntery.setPreferredSize(new java.awt.Dimension(500, 150));
        panAcademicEducationDateEntery.setLayout(new java.awt.GridBagLayout());

        lblSpecilization.setText("Specialization");
        lblSpecilization.setName("lblCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblSpecilization, gridBagConstraints);

        lblUniversity.setText("Name Of the University");
        lblUniversity.setName("lblPrimaryOccupation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblUniversity, gridBagConstraints);

        lblEmpLevelEducation.setText("Level  of Education");
        lblEmpLevelEducation.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblEmpLevelEducation, gridBagConstraints);

        cboEmpLevelEducation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEmpLevelEducation.setName("cboProfession");
        cboEmpLevelEducation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEmpLevelEducationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(cboEmpLevelEducation, gridBagConstraints);

        lblTotMarks.setText("Total Marks");
        lblTotMarks.setName("lblAnnualIncomeLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblTotMarks, gridBagConstraints);

        lblMarksScored.setText("Marks Scored");
        lblMarksScored.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblMarksScored, gridBagConstraints);

        lblTotMarksPer.setText("Marks %");
        lblTotMarksPer.setName("lblVehicle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblTotMarksPer, gridBagConstraints);

        lblGrade.setText("Grade");
        lblGrade.setName("lblPrefCommunication");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblGrade, gridBagConstraints);

        cboGrade.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGrade.setName("cboPrefCommunication");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(cboGrade, gridBagConstraints);

        lblDateOfPassing.setText("Year Of Passing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblDateOfPassing, gridBagConstraints);

        lblNameOfSchool.setText("School /College");
        lblNameOfSchool.setName("lblCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(lblNameOfSchool, gridBagConstraints);

        txtTotMarksPer.setMaxLength(128);
        txtTotMarksPer.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotMarksPer.setName("txtCompany");
        txtTotMarksPer.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(txtTotMarksPer, gridBagConstraints);

        tdtDateOfPassing.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDateOfPassing.setName("tdtDateOfBirth");
        tdtDateOfPassing.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(tdtDateOfPassing, gridBagConstraints);

        cboSpecilization.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSpecilization.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(cboSpecilization, gridBagConstraints);

        txtMarksScored.setMaxLength(128);
        txtMarksScored.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMarksScored.setName("txtCompany");
        txtMarksScored.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(txtMarksScored, gridBagConstraints);

        txtUniversity.setMaxLength(128);
        txtUniversity.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUniversity.setName("txtCompany");
        txtUniversity.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(txtUniversity, gridBagConstraints);

        txtTotMarks.setMaxLength(128);
        txtTotMarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotMarks.setName("txtCompany");
        txtTotMarks.setValidation(new DefaultValidation());
        txtTotMarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotMarksFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(txtTotMarks, gridBagConstraints);

        txtNameOfSchool.setMaxLength(128);
        txtNameOfSchool.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNameOfSchool.setName("txtCompany");
        txtNameOfSchool.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAcademicEducationDateEntery.add(txtNameOfSchool, gridBagConstraints);

        panBtnAcademicEducation.setLayout(new java.awt.GridBagLayout());

        btnAcademicEducationDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnAcademicEducationDetSave.setText("Save");
        btnAcademicEducationDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnAcademicEducationDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnAcademicEducationDetSave.setName("btnContactAdd");
        btnAcademicEducationDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnAcademicEducationDetSave.setEnabled(false);
        btnAcademicEducationDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcademicEducationDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnAcademicEducation.add(btnAcademicEducationDetSave, gridBagConstraints);

        btnAcademicEducationDetNew.setText("New");
        btnAcademicEducationDetNew.setName("btnContactNew");
        btnAcademicEducationDetNew.setEnabled(false);
        btnAcademicEducationDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcademicEducationDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnAcademicEducation.add(btnAcademicEducationDetNew, gridBagConstraints);

        btnAcademicEducationDetDel.setText("Delete");
        btnAcademicEducationDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnAcademicEducationDetDel.setName("btnContactDelete");
        btnAcademicEducationDetDel.setEnabled(false);
        btnAcademicEducationDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcademicEducationDetDelActionPerformed(evt);
            }
        });
        btnAcademicEducationDetDel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                btnAcademicEducationDetDelComponentResized(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnAcademicEducation.add(btnAcademicEducationDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 3);
        panAcademicEducationDateEntery.add(panBtnAcademicEducation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panAcademicEducation.add(panAcademicEducationDateEntery, gridBagConstraints);

        panAcademicEducationTable.setMinimumSize(new java.awt.Dimension(340, 150));
        panAcademicEducationTable.setName("panContacts");
        panAcademicEducationTable.setPreferredSize(new java.awt.Dimension(340, 150));
        panAcademicEducationTable.setLayout(new java.awt.GridBagLayout());

        srpAcademicEducation.setMinimumSize(new java.awt.Dimension(340, 75));
        srpAcademicEducation.setName("srpContactList");
        srpAcademicEducation.setPreferredSize(new java.awt.Dimension(340, 75));

        tblAcademicEducation.setName("tblContactList");
        tblAcademicEducation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAcademicEducationMousePressed(evt);
            }
        });
        srpAcademicEducation.setViewportView(tblAcademicEducation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panAcademicEducationTable.add(srpAcademicEducation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panAcademicEducation.add(panAcademicEducationTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMISKYC.add(panAcademicEducation, gridBagConstraints);

        panLanguageStatus.setBorder(javax.swing.BorderFactory.createTitledBorder("Language Known Details"));
        panLanguageStatus.setMinimumSize(new java.awt.Dimension(840, 185));
        panLanguageStatus.setPreferredSize(new java.awt.Dimension(840, 185));
        panLanguageStatus.setLayout(new java.awt.GridBagLayout());

        panLanguageDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLanguageDate.setMaximumSize(new java.awt.Dimension(500, 150));
        panLanguageDate.setMinimumSize(new java.awt.Dimension(500, 150));
        panLanguageDate.setPreferredSize(new java.awt.Dimension(500, 140));
        panLanguageDate.setLayout(new java.awt.GridBagLayout());

        cboLanguageType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLanguageType.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLanguageDate.add(cboLanguageType, gridBagConstraints);

        lblLanguageType.setText("Language Type");
        lblLanguageType.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLanguageDate.add(lblLanguageType, gridBagConstraints);

        panLangues.setName("panGender");
        panLangues.setLayout(new java.awt.GridBagLayout());

        rdoLanguageRead.setText("Read");
        rdoLanguageRead.setName("rdoGender_Male");
        panLangues.add(rdoLanguageRead, new java.awt.GridBagConstraints());

        rdoLanguageWrite.setText("Write");
        rdoLanguageWrite.setName("rdoGender_Female");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLangues.add(rdoLanguageWrite, gridBagConstraints);

        rdoLanguageReadeSpeak.setText("Speak");
        rdoLanguageReadeSpeak.setName("rdoGender_Female");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLangues.add(rdoLanguageReadeSpeak, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panLanguageDate.add(panLangues, gridBagConstraints);

        panBtnLangues1.setLayout(new java.awt.GridBagLayout());

        btnLanguageDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnLanguageDetSave.setText("Save");
        btnLanguageDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnLanguageDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnLanguageDetSave.setName("btnContactAdd");
        btnLanguageDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnLanguageDetSave.setEnabled(false);
        btnLanguageDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnLangues1.add(btnLanguageDetSave, gridBagConstraints);

        btnLanguageDetNew.setText("New");
        btnLanguageDetNew.setName("btnContactNew");
        btnLanguageDetNew.setEnabled(false);
        btnLanguageDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnLangues1.add(btnLanguageDetNew, gridBagConstraints);

        btnLanguageDetDel.setText("Delete");
        btnLanguageDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnLanguageDetDel.setName("btnContactDelete");
        btnLanguageDetDel.setEnabled(false);
        btnLanguageDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageDetDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnLangues1.add(btnLanguageDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 3);
        panLanguageDate.add(panBtnLangues1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panLanguageStatus.add(panLanguageDate, gridBagConstraints);

        panLanguageTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLanguageTable.setMinimumSize(new java.awt.Dimension(340, 150));
        panLanguageTable.setName("panContacts");
        panLanguageTable.setPreferredSize(new java.awt.Dimension(340, 150));
        panLanguageTable.setLayout(new java.awt.GridBagLayout());

        srpLanguage.setMinimumSize(new java.awt.Dimension(340, 75));
        srpLanguage.setName("srpContactList");
        srpLanguage.setPreferredSize(new java.awt.Dimension(340, 75));

        tblLanguage.setName("tblContactList");
        tblLanguage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblLanguageMousePressed(evt);
            }
        });
        srpLanguage.setViewportView(tblLanguage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panLanguageTable.add(srpLanguage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panLanguageStatus.add(panLanguageTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMISKYC.add(panLanguageStatus, gridBagConstraints);

        panAcademicEducation2.setBorder(javax.swing.BorderFactory.createTitledBorder("Technical Qualification Details"));
        panAcademicEducation2.setMinimumSize(new java.awt.Dimension(840, 185));
        panAcademicEducation2.setPreferredSize(new java.awt.Dimension(840, 185));
        panAcademicEducation2.setLayout(new java.awt.GridBagLayout());

        panTechnicalQualificationDateEntery.setMaximumSize(new java.awt.Dimension(500, 150));
        panTechnicalQualificationDateEntery.setMinimumSize(new java.awt.Dimension(500, 150));
        panTechnicalQualificationDateEntery.setPreferredSize(new java.awt.Dimension(500, 150));
        panTechnicalQualificationDateEntery.setLayout(new java.awt.GridBagLayout());

        lblTechnicalQualificationSpecilization.setText("Specialization");
        lblTechnicalQualificationSpecilization.setName("lblCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationSpecilization, gridBagConstraints);

        lblTechnicalQualificationMarksScored.setText("Total Marks Scored");
        lblTechnicalQualificationMarksScored.setName("lblPrimaryOccupation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationMarksScored, gridBagConstraints);

        lblTechnicalQualificationType.setText("Technical Qualification");
        lblTechnicalQualificationType.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationType, gridBagConstraints);

        cboTechnicalQualificationType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTechnicalQualificationType.setName("cboProfession");
        cboTechnicalQualificationType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTechnicalQualificationTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(cboTechnicalQualificationType, gridBagConstraints);

        lblTechnicalQualificationTotMarks.setText("Total Marks");
        lblTechnicalQualificationTotMarks.setName("lblAnnualIncomeLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationTotMarks, gridBagConstraints);

        lblTechnicalQualificationUniversity.setText("Name Of the University");
        lblTechnicalQualificationUniversity.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationUniversity, gridBagConstraints);

        lblTechnicalQualificationTotMarksPer.setText("Marks %");
        lblTechnicalQualificationTotMarksPer.setName("lblVehicle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationTotMarksPer, gridBagConstraints);

        lblTechnicalQualificationGrade.setText("Grade");
        lblTechnicalQualificationGrade.setName("lblPrefCommunication");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationGrade, gridBagConstraints);

        cboTechnicalQualificationGrade.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTechnicalQualificationGrade.setName("cboPrefCommunication");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(cboTechnicalQualificationGrade, gridBagConstraints);

        lblTechnicalQualificationDateOfPassing.setText("Year Of Passing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblTechnicalQualificationDateOfPassing, gridBagConstraints);

        lblNameOfTechInst.setText("College/Institution");
        lblNameOfTechInst.setName("lblCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(lblNameOfTechInst, gridBagConstraints);

        txtTechnicalQualificationTotMarksPer.setMaxLength(128);
        txtTechnicalQualificationTotMarksPer.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTechnicalQualificationTotMarksPer.setName("txtCompany");
        txtTechnicalQualificationTotMarksPer.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(txtTechnicalQualificationTotMarksPer, gridBagConstraints);

        tdtTechnicalQualificationDateOfPassing.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtTechnicalQualificationDateOfPassing.setName("tdtDateOfBirth");
        tdtTechnicalQualificationDateOfPassing.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(tdtTechnicalQualificationDateOfPassing, gridBagConstraints);

        cboTechnicalQualificationSpecilization.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTechnicalQualificationSpecilization.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(cboTechnicalQualificationSpecilization, gridBagConstraints);

        txtTechnicalQualificationUniversity.setMaxLength(128);
        txtTechnicalQualificationUniversity.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTechnicalQualificationUniversity.setName("txtCompany");
        txtTechnicalQualificationUniversity.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(txtTechnicalQualificationUniversity, gridBagConstraints);

        txtTechnicalQualificationMarksScored.setMaxLength(128);
        txtTechnicalQualificationMarksScored.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTechnicalQualificationMarksScored.setName("txtCompany");
        txtTechnicalQualificationMarksScored.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(txtTechnicalQualificationMarksScored, gridBagConstraints);

        txtTechnicalQualificationTotMarks.setMaxLength(128);
        txtTechnicalQualificationTotMarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTechnicalQualificationTotMarks.setName("txtCompany");
        txtTechnicalQualificationTotMarks.setValidation(new DefaultValidation());
        txtTechnicalQualificationTotMarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTechnicalQualificationTotMarksFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(txtTechnicalQualificationTotMarks, gridBagConstraints);

        txtNameOfTechInst.setMaxLength(128);
        txtNameOfTechInst.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNameOfTechInst.setName("txtCompany");
        txtNameOfTechInst.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTechnicalQualificationDateEntery.add(txtNameOfTechInst, gridBagConstraints);

        panBtnTechnicalQualification2.setLayout(new java.awt.GridBagLayout());

        btnTechnicalQualificationDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnTechnicalQualificationDetSave.setText("Save");
        btnTechnicalQualificationDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnTechnicalQualificationDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnTechnicalQualificationDetSave.setName("btnContactAdd");
        btnTechnicalQualificationDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnTechnicalQualificationDetSave.setEnabled(false);
        btnTechnicalQualificationDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTechnicalQualificationDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnTechnicalQualification2.add(btnTechnicalQualificationDetSave, gridBagConstraints);

        btnTechnicalQualificationDetNew.setText("New");
        btnTechnicalQualificationDetNew.setName("btnContactNew");
        btnTechnicalQualificationDetNew.setEnabled(false);
        btnTechnicalQualificationDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTechnicalQualificationDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnTechnicalQualification2.add(btnTechnicalQualificationDetNew, gridBagConstraints);

        btnTechnicalQualificationDetDel.setText("Delete");
        btnTechnicalQualificationDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnTechnicalQualificationDetDel.setName("btnContactDelete");
        btnTechnicalQualificationDetDel.setEnabled(false);
        btnTechnicalQualificationDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTechnicalQualificationDetDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnTechnicalQualification2.add(btnTechnicalQualificationDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 3);
        panTechnicalQualificationDateEntery.add(panBtnTechnicalQualification2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panAcademicEducation2.add(panTechnicalQualificationDateEntery, gridBagConstraints);

        panTechnicalQualificationTable.setMinimumSize(new java.awt.Dimension(340, 150));
        panTechnicalQualificationTable.setName("panContacts");
        panTechnicalQualificationTable.setPreferredSize(new java.awt.Dimension(340, 150));
        panTechnicalQualificationTable.setLayout(new java.awt.GridBagLayout());

        srpTechnicalQualification.setMinimumSize(new java.awt.Dimension(340, 75));
        srpTechnicalQualification.setName("srpContactList");
        srpTechnicalQualification.setPreferredSize(new java.awt.Dimension(340, 75));

        tblTechnicalEducation.setName("tblContactList");
        tblTechnicalEducation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblTechnicalEducationMousePressed(evt);
            }
        });
        srpTechnicalQualification.setViewportView(tblTechnicalEducation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panTechnicalQualificationTable.add(srpTechnicalQualification, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panAcademicEducation2.add(panTechnicalQualificationTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMISKYC.add(panAcademicEducation2, gridBagConstraints);

        tabIndCust.addTab("Educational Details", panMISKYC);

        panIncomeParticulars.setMaximumSize(new java.awt.Dimension(120, 125));
        panIncomeParticulars.setMinimumSize(new java.awt.Dimension(120, 125));
        panIncomeParticulars.setName("panPersonalInfo");
        panIncomeParticulars.setPreferredSize(new java.awt.Dimension(120, 125));
        panIncomeParticulars.setLayout(new java.awt.GridBagLayout());

        panCustomerInfo1.setMaximumSize(new java.awt.Dimension(70, 50));
        panCustomerInfo1.setMinimumSize(new java.awt.Dimension(50, 50));
        panCustomerInfo1.setName("panCustomerInfo");
        panCustomerInfo1.setPreferredSize(new java.awt.Dimension(70, 50));
        panCustomerInfo1.setLayout(new java.awt.GridBagLayout());

        panOfficeDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Present Details"));
        panOfficeDetails.setMinimumSize(new java.awt.Dimension(840, 600));
        panOfficeDetails.setPreferredSize(new java.awt.Dimension(840, 600));
        panOfficeDetails.setLayout(new java.awt.GridBagLayout());

        panTrainingDateEntery1.setMaximumSize(new java.awt.Dimension(410, 150));
        panTrainingDateEntery1.setMinimumSize(new java.awt.Dimension(450, 600));
        panTrainingDateEntery1.setPreferredSize(new java.awt.Dimension(450, 600));
        panTrainingDateEntery1.setLayout(new java.awt.GridBagLayout());

        panGradutionIncrement.setMinimumSize(new java.awt.Dimension(95, 21));
        panGradutionIncrement.setName("panMaritalStatus");
        panGradutionIncrement.setPreferredSize(new java.awt.Dimension(95, 21));
        panGradutionIncrement.setLayout(new java.awt.GridBagLayout());

        rdoGradutionIncrement.add(rdoGradutionIncrementYes);
        rdoGradutionIncrementYes.setText("Yes");
        rdoGradutionIncrementYes.setMaximumSize(new java.awt.Dimension(61, 21));
        rdoGradutionIncrementYes.setMinimumSize(new java.awt.Dimension(61, 21));
        rdoGradutionIncrementYes.setName("rdoMaritalStatus_Single");
        rdoGradutionIncrementYes.setPreferredSize(new java.awt.Dimension(50, 21));
        rdoGradutionIncrementYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGradutionIncrementYesActionPerformed(evt);
            }
        });
        rdoGradutionIncrementYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoGradutionIncrementYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGradutionIncrement.add(rdoGradutionIncrementYes, gridBagConstraints);

        rdoGradutionIncrement.add(rdoGradutionIncrementNo);
        rdoGradutionIncrementNo.setText("No");
        rdoGradutionIncrementNo.setName("rdoMaritalStatus_Married");
        rdoGradutionIncrementNo.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoGradutionIncrementNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGradutionIncrementNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGradutionIncrement.add(rdoGradutionIncrementNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 0);
        panTrainingDateEntery1.add(panGradutionIncrement, gridBagConstraints);

        lblGradutionIncrementYesNo.setText("Graduation Increment Released");
        lblGradutionIncrementYesNo.setName("lblMaritalStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblGradutionIncrementYesNo, gridBagConstraints);

        panCAIIBPART.setMinimumSize(new java.awt.Dimension(105, 21));
        panCAIIBPART.setName("panMaritalStatus");
        panCAIIBPART.setPreferredSize(new java.awt.Dimension(105, 21));
        panCAIIBPART.setLayout(new java.awt.GridBagLayout());

        rdoCAIIBPART1Increment.add(rdoCAIIBPART1IncrementYes);
        rdoCAIIBPART1IncrementYes.setText("Yes");
        rdoCAIIBPART1IncrementYes.setMaximumSize(new java.awt.Dimension(41, 21));
        rdoCAIIBPART1IncrementYes.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoCAIIBPART1IncrementYes.setName("rdoMaritalStatus_Single");
        rdoCAIIBPART1IncrementYes.setPreferredSize(new java.awt.Dimension(51, 21));
        rdoCAIIBPART1IncrementYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCAIIBPART1IncrementYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCAIIBPART.add(rdoCAIIBPART1IncrementYes, gridBagConstraints);

        rdoCAIIBPART1Increment.add(rdoCAIIBPART1IncrementNo);
        rdoCAIIBPART1IncrementNo.setText("No");
        rdoCAIIBPART1IncrementNo.setName("rdoMaritalStatus_Married");
        rdoCAIIBPART1IncrementNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCAIIBPART1IncrementNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 13);
        panCAIIBPART.add(rdoCAIIBPART1IncrementNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        panTrainingDateEntery1.add(panCAIIBPART, gridBagConstraints);

        lblCAIIBPART1IncrementYesNo.setText("CAIIB Increment");
        lblCAIIBPART1IncrementYesNo.setName("lblMaritalStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblCAIIBPART1IncrementYesNo, gridBagConstraints);

        lblGradutionIncrementReleasedDate.setText("Graduation Increment Released Date");
        lblGradutionIncrementReleasedDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 4);
        panTrainingDateEntery1.add(lblGradutionIncrementReleasedDate, gridBagConstraints);

        tdtGradutionIncrementReleasedDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtGradutionIncrementReleasedDate.setName("tdtDateOfBirth");
        tdtGradutionIncrementReleasedDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(tdtGradutionIncrementReleasedDate, gridBagConstraints);

        tdtCAIIBPART1IncrementReleasedDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtCAIIBPART1IncrementReleasedDate.setName("tdtDateOfBirth");
        tdtCAIIBPART1IncrementReleasedDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(tdtCAIIBPART1IncrementReleasedDate, gridBagConstraints);

        lblCAIIBPART1IncrementReleasedDate.setText("CAIIB Increment Date");
        lblCAIIBPART1IncrementReleasedDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 3);
        panTrainingDateEntery1.add(lblCAIIBPART1IncrementReleasedDate, gridBagConstraints);

        panCAIIBPART2.setMinimumSize(new java.awt.Dimension(164, 27));
        panCAIIBPART2.setName("panMaritalStatus");
        panCAIIBPART2.setPreferredSize(new java.awt.Dimension(164, 27));
        panCAIIBPART2.setLayout(new java.awt.GridBagLayout());

        rdoCAIIBPART2Increment.add(rdoCAIIBPART2Increment_Yes);
        rdoCAIIBPART2Increment_Yes.setText("Yes");
        rdoCAIIBPART2Increment_Yes.setMaximumSize(new java.awt.Dimension(61, 21));
        rdoCAIIBPART2Increment_Yes.setMinimumSize(new java.awt.Dimension(61, 21));
        rdoCAIIBPART2Increment_Yes.setName("rdoMaritalStatus_Single");
        rdoCAIIBPART2Increment_Yes.setPreferredSize(new java.awt.Dimension(61, 21));
        rdoCAIIBPART2Increment_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCAIIBPART2Increment_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCAIIBPART2.add(rdoCAIIBPART2Increment_Yes, gridBagConstraints);

        rdoCAIIBPART2Increment.add(rdoCAIIBPART2Increment_No);
        rdoCAIIBPART2Increment_No.setText("No");
        rdoCAIIBPART2Increment_No.setName("rdoMaritalStatus_Married");
        rdoCAIIBPART2Increment_No.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoCAIIBPART2Increment_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCAIIBPART2Increment_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCAIIBPART2.add(rdoCAIIBPART2Increment_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        panTrainingDateEntery1.add(panCAIIBPART2, gridBagConstraints);

        lblCAIIBPART2IncrementYesNo.setText("CO-Operative Diploma Increment Released");
        lblCAIIBPART2IncrementYesNo.setName("lblMaritalStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblCAIIBPART2IncrementYesNo, gridBagConstraints);

        tdtCAIIBPART2IncrementReleasedDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtCAIIBPART2IncrementReleasedDate.setName("tdtDateOfBirth");
        tdtCAIIBPART2IncrementReleasedDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(tdtCAIIBPART2IncrementReleasedDate, gridBagConstraints);

        lblCAIIBPART2IncrementReleasedDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCAIIBPART2IncrementReleasedDate.setText("CO-Op Diploma Increment Released Date");
        lblCAIIBPART2IncrementReleasedDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 3);
        panTrainingDateEntery1.add(lblCAIIBPART2IncrementReleasedDate, gridBagConstraints);

        panAnyOtherIncrement.setMinimumSize(new java.awt.Dimension(164, 27));
        panAnyOtherIncrement.setName("panMaritalStatus");
        panAnyOtherIncrement.setPreferredSize(new java.awt.Dimension(164, 27));
        panAnyOtherIncrement.setLayout(new java.awt.GridBagLayout());

        rdoAnyotherIncrement.add(rdoAnyOtherIncrement_Yes);
        rdoAnyOtherIncrement_Yes.setText("Yes");
        rdoAnyOtherIncrement_Yes.setMaximumSize(new java.awt.Dimension(61, 21));
        rdoAnyOtherIncrement_Yes.setMinimumSize(new java.awt.Dimension(61, 21));
        rdoAnyOtherIncrement_Yes.setName("rdoMaritalStatus_Single");
        rdoAnyOtherIncrement_Yes.setPreferredSize(new java.awt.Dimension(61, 21));
        rdoAnyOtherIncrement_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAnyOtherIncrement_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAnyOtherIncrement.add(rdoAnyOtherIncrement_Yes, gridBagConstraints);

        rdoAnyotherIncrement.add(rdoAnyOtherIncrement_No);
        rdoAnyOtherIncrement_No.setText("No");
        rdoAnyOtherIncrement_No.setName("rdoMaritalStatus_Married");
        rdoAnyOtherIncrement_No.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoAnyOtherIncrement_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAnyOtherIncrement_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAnyOtherIncrement.add(rdoAnyOtherIncrement_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panTrainingDateEntery1.add(panAnyOtherIncrement, gridBagConstraints);

        txtAnyOtherIncrementInstitutionName.setMaxLength(256);
        txtAnyOtherIncrementInstitutionName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAnyOtherIncrementInstitutionName.setName("txtRemarks");
        txtAnyOtherIncrementInstitutionName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(txtAnyOtherIncrementInstitutionName, gridBagConstraints);

        lblAnyOtherIncrementYesNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAnyOtherIncrementYesNo.setText("Increment Released Any Other Qualification");
        lblAnyOtherIncrementYesNo.setMinimumSize(new java.awt.Dimension(320, 18));
        lblAnyOtherIncrementYesNo.setName("lblMaritalStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblAnyOtherIncrementYesNo, gridBagConstraints);

        lblAnyOtherIncrementInstitutionName.setText("Special Increment Release");
        lblAnyOtherIncrementInstitutionName.setName("lblRemarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTrainingDateEntery1.add(lblAnyOtherIncrementInstitutionName, gridBagConstraints);

        tdtAnyOtherIncrementReleasedDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtAnyOtherIncrementReleasedDate.setName("tdtDateOfBirth");
        tdtAnyOtherIncrementReleasedDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(tdtAnyOtherIncrementReleasedDate, gridBagConstraints);

        lblAnyOtherIncrementReleasedDate.setText("Released Date");
        lblAnyOtherIncrementReleasedDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 4);
        panTrainingDateEntery1.add(lblAnyOtherIncrementReleasedDate, gridBagConstraints);

        tdtLastIncrmentDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLastIncrmentDate.setName("tdtDateOfBirth");
        tdtLastIncrmentDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(tdtLastIncrmentDate, gridBagConstraints);

        lblLastIncrmentDate.setText("Last Incement Date");
        lblLastIncrmentDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 3);
        panTrainingDateEntery1.add(lblLastIncrmentDate, gridBagConstraints);

        panLossOfpay.setLayout(new java.awt.GridBagLayout());

        txtLossPay_Months.setMinimumSize(new java.awt.Dimension(30, 21));
        txtLossPay_Months.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panLossOfpay.add(txtLossPay_Months, gridBagConstraints);

        lblLossPay_Months.setText("Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panLossOfpay.add(lblLossPay_Months, gridBagConstraints);

        txtLossOfpay_Days.setAllowAll(true);
        txtLossOfpay_Days.setMinimumSize(new java.awt.Dimension(30, 21));
        txtLossOfpay_Days.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panLossOfpay.add(txtLossOfpay_Days, gridBagConstraints);

        lblLossOfpay_Days.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 9);
        panLossOfpay.add(lblLossOfpay_Days, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTrainingDateEntery1.add(panLossOfpay, gridBagConstraints);

        lblAnyLossOfPay.setText("Loss of Pay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTrainingDateEntery1.add(lblAnyLossOfPay, gridBagConstraints);

        tdtNextIncrmentDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtNextIncrmentDate.setName("tdtDateOfBirth");
        tdtNextIncrmentDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(tdtNextIncrmentDate, gridBagConstraints);

        lblNextIncrmentDate.setText("Next Incement Date");
        lblNextIncrmentDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 5);
        panTrainingDateEntery1.add(lblNextIncrmentDate, gridBagConstraints);

        txtPresentBasic.setMaxLength(256);
        txtPresentBasic.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPresentBasic.setName("txtRemarks");
        txtPresentBasic.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(txtPresentBasic, gridBagConstraints);

        lblPresentBasic.setText("Present Basic Pay");
        lblPresentBasic.setName("lblRemarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblPresentBasic, gridBagConstraints);

        panSignatureNo1.setName("panGender");
        panSignatureNo1.setLayout(new java.awt.GridBagLayout());

        rdoSignature.add(rdoSignature_Yes);
        rdoSignature_Yes.setText("Yes");
        rdoSignature_Yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoSignature_Yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoSignature_Yes.setName("rdoGender_Male");
        rdoSignature_Yes.setPreferredSize(new java.awt.Dimension(53, 21));
        rdoSignature_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSignature_YesActionPerformed(evt);
            }
        });
        panSignatureNo1.add(rdoSignature_Yes, new java.awt.GridBagConstraints());

        rdoSignature.add(rdoSignature_No);
        rdoSignature_No.setText("No");
        rdoSignature_No.setMaximumSize(new java.awt.Dimension(69, 21));
        rdoSignature_No.setMinimumSize(new java.awt.Dimension(69, 21));
        rdoSignature_No.setName("rdoGender_Female");
        rdoSignature_No.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoSignature_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSignature_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSignatureNo1.add(rdoSignature_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(panSignatureNo1, gridBagConstraints);

        lblSigNoYesNo.setText("Specimen Signature Lodged");
        lblSigNoYesNo.setName("lblGender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblSigNoYesNo, gridBagConstraints);

        txtSignatureNo.setMaxLength(128);
        txtSignatureNo.setMinimumSize(new java.awt.Dimension(120, 21));
        txtSignatureNo.setName("txtLastName");
        txtSignatureNo.setPreferredSize(new java.awt.Dimension(150, 21));
        txtSignatureNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(txtSignatureNo, gridBagConstraints);

        lblSignatureNo.setText("Signing Power No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblSignatureNo, gridBagConstraints);

        lblSocietyMember.setText("If Society Member");
        lblSocietyMember.setName("lblGender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblSocietyMember, gridBagConstraints);

        lblSocietyMemberNo.setText("Society MemberShip No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblSocietyMemberNo, gridBagConstraints);

        txtSocietyMemberNo.setMaxLength(128);
        txtSocietyMemberNo.setMinimumSize(new java.awt.Dimension(120, 21));
        txtSocietyMemberNo.setName("txtLastName");
        txtSocietyMemberNo.setPreferredSize(new java.awt.Dimension(150, 21));
        txtSocietyMemberNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(txtSocietyMemberNo, gridBagConstraints);

        cboSocietyMember.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSocietyMember.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(cboSocietyMember, gridBagConstraints);

        lblUnionMember.setText("If Union Member");
        lblUnionMember.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblUnionMember, gridBagConstraints);

        cboUnionMember.setMinimumSize(new java.awt.Dimension(100, 21));
        cboUnionMember.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(cboUnionMember, gridBagConstraints);

        txtClubName.setMaxLength(128);
        txtClubName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtClubName.setName("txtLastName");
        txtClubName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtClubName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 12, 0);
        panTrainingDateEntery1.add(txtClubName, gridBagConstraints);

        lblClubName.setText("Club Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 20, 4);
        panTrainingDateEntery1.add(lblClubName, gridBagConstraints);

        lblClubMembership.setText("ClubMembership");
        lblClubMembership.setName("lblGender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingDateEntery1.add(lblClubMembership, gridBagConstraints);

        panClubDet.setName("panGender");
        panClubDet.setLayout(new java.awt.GridBagLayout());

        rdoClubMembership.add(rdoClubMembership_YES);
        rdoClubMembership_YES.setText("Yes");
        rdoClubMembership_YES.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoClubMembership_YES.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoClubMembership_YES.setName("rdoGender_Male");
        rdoClubMembership_YES.setPreferredSize(new java.awt.Dimension(53, 21));
        rdoClubMembership_YES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoClubMembership_YESActionPerformed(evt);
            }
        });
        panClubDet.add(rdoClubMembership_YES, new java.awt.GridBagConstraints());

        rdoClubMembership.add(rdoClubMembership_No);
        rdoClubMembership_No.setText("No");
        rdoClubMembership_No.setMaximumSize(new java.awt.Dimension(69, 21));
        rdoClubMembership_No.setMinimumSize(new java.awt.Dimension(69, 21));
        rdoClubMembership_No.setName("rdoGender_Female");
        rdoClubMembership_No.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoClubMembership_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoClubMembership_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panClubDet.add(rdoClubMembership_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 4);
        panTrainingDateEntery1.add(panClubDet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panOfficeDetails.add(panTrainingDateEntery1, gridBagConstraints);

        panPresent.setMinimumSize(new java.awt.Dimension(280, 150));
        panPresent.setName("panContacts");
        panPresent.setPreferredSize(new java.awt.Dimension(250, 150));
        panPresent.setLayout(new java.awt.GridBagLayout());

        panPresentDetails.setMaximumSize(new java.awt.Dimension(275, 380));
        panPresentDetails.setMinimumSize(new java.awt.Dimension(275, 380));
        panPresentDetails.setName("panContacts");
        panPresentDetails.setPreferredSize(new java.awt.Dimension(275, 380));
        panPresentDetails.setLayout(new java.awt.GridBagLayout());

        panPresentWorkingDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Present Working Details"));
        panPresentWorkingDetails.setMinimumSize(new java.awt.Dimension(230, 215));
        panPresentWorkingDetails.setPreferredSize(new java.awt.Dimension(230, 215));
        panPresentWorkingDetails.setLayout(new java.awt.GridBagLayout());

        cboZonalOffice.setMinimumSize(new java.awt.Dimension(100, 21));
        cboZonalOffice.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(cboZonalOffice, gridBagConstraints);

        lblZonalOffice.setText("ZO/CO ID");
        lblZonalOffice.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(lblZonalOffice, gridBagConstraints);

        cboPresentBranchId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPresentBranchId.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(cboPresentBranchId, gridBagConstraints);

        lblPresentBranchId.setText("Branch Id");
        lblPresentBranchId.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(lblPresentBranchId, gridBagConstraints);

        panDOB2.setMinimumSize(new java.awt.Dimension(100, 23));
        panDOB2.setPreferredSize(new java.awt.Dimension(100, 23));
        panDOB2.setLayout(new java.awt.GridBagLayout());

        tdtWorkingSince.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtWorkingSince.setName("tdtDateOfBirth");
        tdtWorkingSince.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtWorkingSince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtWorkingSinceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDOB2.add(tdtWorkingSince, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 40);
        panPresentWorkingDetails.add(panDOB2, gridBagConstraints);

        lblWorkingSince.setText("Working Since");
        lblWorkingSince.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 3);
        panPresentWorkingDetails.add(lblWorkingSince, gridBagConstraints);

        cboReginoalOffice.setMinimumSize(new java.awt.Dimension(100, 21));
        cboReginoalOffice.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(cboReginoalOffice, gridBagConstraints);

        lbReginoalOffice.setText("DO/RO ID");
        lbReginoalOffice.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(lbReginoalOffice, gridBagConstraints);

        cboPresentGrade.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPresentGrade.setName("cboProfession");
        cboPresentGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPresentGradeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(cboPresentGrade, gridBagConstraints);

        lblPresentGrade.setText("Grade");
        lblPresentGrade.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(lblPresentGrade, gridBagConstraints);

        cboDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDesignation.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(cboDesignation, gridBagConstraints);

        lblDesignation.setText("Designation");
        lblDesignation.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPresentWorkingDetails.add(lblDesignation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 0);
        panPresentDetails.add(panPresentWorkingDetails, gridBagConstraints);

        panJoiningDetails.setMinimumSize(new java.awt.Dimension(275, 210));
        panJoiningDetails.setPreferredSize(new java.awt.Dimension(275, 210));
        panJoiningDetails.setLayout(new java.awt.GridBagLayout());

        panMinDepositPeriod1.setPreferredSize(new java.awt.Dimension(134, 21));
        panMinDepositPeriod1.setLayout(new java.awt.GridBagLayout());

        txtProbationPeriod.setMinimumSize(new java.awt.Dimension(30, 21));
        txtProbationPeriod.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMinDepositPeriod1.add(txtProbationPeriod, gridBagConstraints);

        cboProbationPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProbationPeriod.setPreferredSize(new java.awt.Dimension(100, 20));
        cboProbationPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProbationPeriodActionPerformed(evt);
            }
        });
        cboProbationPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProbationPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMinDepositPeriod1.add(cboProbationPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panJoiningDetails.add(panMinDepositPeriod1, gridBagConstraints);

        lblProbationPeriod.setText("Probation Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 5);
        panJoiningDetails.add(lblProbationPeriod, gridBagConstraints);

        lblConfirmationDate.setText("Confirmation Date");
        lblConfirmationDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 6);
        panJoiningDetails.add(lblConfirmationDate, gridBagConstraints);

        tdtConfirmationDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtConfirmationDate.setName("tdtDateOfBirth");
        tdtConfirmationDate.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtConfirmationDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtConfirmationDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panJoiningDetails.add(tdtConfirmationDate, gridBagConstraints);

        tdtDateofRetirement.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtDateofRetirement.setName("tdtDateOfBirth");
        tdtDateofRetirement.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panJoiningDetails.add(tdtDateofRetirement, gridBagConstraints);

        lblDateofRetirement.setText("Retirement Date");
        lblDateofRetirement.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 6);
        panJoiningDetails.add(lblDateofRetirement, gridBagConstraints);

        lblDateOfJoin.setText("Joining Date");
        lblDateOfJoin.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 4);
        panJoiningDetails.add(lblDateOfJoin, gridBagConstraints);

        tdtDateOfJoin.setName("tdtDepDateOfBirth");
        tdtDateOfJoin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateOfJoinFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panJoiningDetails.add(tdtDateOfJoin, gridBagConstraints);

        lblPFNumber.setText("PF A/c Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 7);
        panJoiningDetails.add(lblPFNumber, gridBagConstraints);

        txtPFNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPFNumber.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panJoiningDetails.add(txtPFNumber, gridBagConstraints);

        cboPFAcNominee.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPFAcNominee.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panJoiningDetails.add(cboPFAcNominee, gridBagConstraints);

        lblPFAcNominee.setText("PF A/c Nominee");
        lblPFAcNominee.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 6);
        panJoiningDetails.add(lblPFAcNominee, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 17, 0);
        panPresentDetails.add(panJoiningDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 3, 2);
        panPresent.add(panPresentDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 2);
        panOfficeDetails.add(panPresent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerInfo1.add(panOfficeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIncomeParticulars.add(panCustomerInfo1, gridBagConstraints);

        tabIndCust.addTab("Present Details", panIncomeParticulars);
        panIncomeParticulars.getAccessibleContext().setAccessibleName("Particulars");

        panPromotionInfo.setMinimumSize(new java.awt.Dimension(750, 230));
        panPromotionInfo.setPreferredSize(new java.awt.Dimension(750, 230));
        panPromotionInfo.setLayout(new java.awt.GridBagLayout());

        panPromotionDetails.setMinimumSize(new java.awt.Dimension(800, 400));
        panPromotionDetails.setPreferredSize(new java.awt.Dimension(800, 400));
        panPromotionDetails.setLayout(new java.awt.GridBagLayout());

        panPromotionTable.setMinimumSize(new java.awt.Dimension(450, 350));
        panPromotionTable.setPreferredSize(new java.awt.Dimension(450, 350));
        panPromotionTable.setLayout(new java.awt.GridBagLayout());

        srpPromotion.setMinimumSize(new java.awt.Dimension(340, 75));
        srpPromotion.setName("srpContactList");
        srpPromotion.setPreferredSize(new java.awt.Dimension(340, 75));

        tblPromotion.setName("tblContactList");
        tblPromotion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPromotionMouseClicked(evt);
            }
        });
        srpPromotion.setViewportView(tblPromotion);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panPromotionTable.add(srpPromotion, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPromotionDetails.add(panPromotionTable, gridBagConstraints);

        panPromotionDetailsinfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panPromotionDetailsinfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panPromotionDetailsinfo.setLayout(new java.awt.GridBagLayout());

        lblPromotionEmployeeId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionEmployeeId, gridBagConstraints);

        lblPromotionEmployeeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPromotionEmployeeName.setText("Employee name");
        lblPromotionEmployeeName.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionEmployeeName, gridBagConstraints);

        lblPresentDesignation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPresentDesignation.setText("Present Designation");
        lblPresentDesignation.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPresentDesignation, gridBagConstraints);

        lblPromotionLastGrade.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPromotionLastGrade.setText("Present Grade");
        lblPromotionLastGrade.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionLastGrade, gridBagConstraints);

        lblPromotionEffectiveDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPromotionEffectiveDate.setText("Effective date");
        lblPromotionEffectiveDate.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionEffectiveDate, gridBagConstraints);

        tdtPromotionEffectiveDateValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPromotionEffectiveDateValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(tdtPromotionEffectiveDateValue, gridBagConstraints);

        lblPromotionCreatedDate.setText("Created date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionCreatedDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(tdtPromotionCreatedDateValue, gridBagConstraints);

        lblPromotionBasicPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPromotionBasicPay.setText("Present basic pay");
        lblPromotionBasicPay.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionBasicPay, gridBagConstraints);

        txtPromotionBasicPayValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(txtPromotionBasicPayValue, gridBagConstraints);

        lblPromotionGrade.setText("Promotion Grade");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionGrade, gridBagConstraints);

        cboPromotionGrade.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPromotionGrade.setPopupWidth(250);
        cboPromotionGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPromotionGradeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(cboPromotionGrade, gridBagConstraints);

        btnPromotionNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnPromotionNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPromotionNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromotionNewActionPerformed(evt);
            }
        });
        panPromotionButtons.add(btnPromotionNew);

        btnPromotionSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnPromotionSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPromotionSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromotionSaveActionPerformed(evt);
            }
        });
        panPromotionButtons.add(btnPromotionSave);

        btnPromotionDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnPromotionDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPromotionDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromotionDeleteActionPerformed(evt);
            }
        });
        panPromotionButtons.add(btnPromotionDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 120, 2, 2);
        panPromotionDetailsinfo.add(panPromotionButtons, gridBagConstraints);

        txtPromotionEmployeeName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panPromotionDetailsinfo.add(txtPromotionEmployeeName, gridBagConstraints);

        txtPromotionLastDesg.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panPromotionDetailsinfo.add(txtPromotionLastDesg, gridBagConstraints);

        txtPromotionLastGrade.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPromotionLastGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPromotionLastGradeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panPromotionDetailsinfo.add(txtPromotionLastGrade, gridBagConstraints);

        cboPromotedDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPromotedDesignation.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(cboPromotedDesignation, gridBagConstraints);

        lblPromotedDesignation.setText("Promoted As");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotedDesignation, gridBagConstraints);

        txtPromotionSalId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(txtPromotionSalId, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblRemarks, gridBagConstraints);

        lblPromotionSalId.setText("Reference No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblPromotionSalId, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(txtRemarks, gridBagConstraints);

        txtNewBasic.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPromotionDetailsinfo.add(txtNewBasic, gridBagConstraints);

        lblNewBasic.setText("New Basic");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPromotionDetailsinfo.add(lblNewBasic, gridBagConstraints);

        txtPromotionEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panPromotionDetailsinfo.add(txtPromotionEmployeeId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPromotionDetails.add(panPromotionDetailsinfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPromotionInfo.add(panPromotionDetails, gridBagConstraints);

        tabIndCust.addTab("Promotion Details", panPromotionInfo);

        panLandDetails.setMaximumSize(new java.awt.Dimension(120, 125));
        panLandDetails.setMinimumSize(new java.awt.Dimension(120, 125));
        panLandDetails.setName("panLandInfo");
        panLandDetails.setPreferredSize(new java.awt.Dimension(120, 125));
        panLandDetails.setLayout(new java.awt.GridBagLayout());

        panEmployeeLoan.setBorder(javax.swing.BorderFactory.createTitledBorder("Loans/Advance Details"));
        panEmployeeLoan.setMinimumSize(new java.awt.Dimension(840, 300));
        panEmployeeLoan.setPreferredSize(new java.awt.Dimension(840, 300));
        panEmployeeLoan.setLayout(new java.awt.GridBagLayout());

        panEmployeeDateEntery.setMaximumSize(new java.awt.Dimension(520, 285));
        panEmployeeDateEntery.setMinimumSize(new java.awt.Dimension(520, 285));
        panEmployeeDateEntery.setPreferredSize(new java.awt.Dimension(520, 285));
        panEmployeeDateEntery.setLayout(new java.awt.GridBagLayout());

        cboEmployeeLoanType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEmployeeLoanType.setName("cboProfession");
        cboEmployeeLoanType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEmployeeLoanTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(cboEmployeeLoanType, gridBagConstraints);

        lblEmployeeLoanType.setText("Prod Type");
        lblEmployeeLoanType.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblEmployeeLoanType, gridBagConstraints);

        cboLoanAvailedBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLoanAvailedBranch.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(cboLoanAvailedBranch, gridBagConstraints);

        lblLoanAvailedBranch.setText("Loan Availed Branch");
        lblLoanAvailedBranch.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanAvailedBranch, gridBagConstraints);

        txtLoanSanctionRefNo.setMaxLength(128);
        txtLoanSanctionRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanSanctionRefNo.setName("txtCompany");
        txtLoanSanctionRefNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(txtLoanSanctionRefNo, gridBagConstraints);

        lblLoanSanctionRefNo.setText("Sanction Ref No");
        lblLoanSanctionRefNo.setName("lblPrimaryOccupation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanSanctionRefNo, gridBagConstraints);

        tdtLoanSanctionDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLoanSanctionDate.setName("tdtDateOfBirth");
        tdtLoanSanctionDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(tdtLoanSanctionDate, gridBagConstraints);

        lblLoanSanctionDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanSanctionDate, gridBagConstraints);

        txtLoanNo.setMaxLength(128);
        txtLoanNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanNo.setName("txtCompany");
        txtLoanNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(txtLoanNo, gridBagConstraints);

        lblLoanNo.setText("Loan No");
        lblLoanNo.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanNo, gridBagConstraints);

        txtLoanAmount.setMaxLength(128);
        txtLoanAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanAmount.setName("txtCompany");
        txtLoanAmount.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(txtLoanAmount, gridBagConstraints);

        lblLoanAmount.setText("Loan Amount");
        lblLoanAmount.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanAmount, gridBagConstraints);

        lblLoanRateofInterest.setText("Rate of Interest");
        lblLoanRateofInterest.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanRateofInterest, gridBagConstraints);

        txtLoanRateofInterest.setMaxLength(128);
        txtLoanRateofInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanRateofInterest.setName("txtCompany");
        txtLoanRateofInterest.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(txtLoanRateofInterest, gridBagConstraints);

        txtLoanInstallmentAmount.setMaxLength(128);
        txtLoanInstallmentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanInstallmentAmount.setName("txtCompany");
        txtLoanInstallmentAmount.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(txtLoanInstallmentAmount, gridBagConstraints);

        lblLoanInstallmentAmount.setText(" Installment Amount");
        lblLoanInstallmentAmount.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanInstallmentAmount, gridBagConstraints);

        txtLoanNoOfInstallments.setMaxLength(128);
        txtLoanNoOfInstallments.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanNoOfInstallments.setName("txtCompany");
        txtLoanNoOfInstallments.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(txtLoanNoOfInstallments, gridBagConstraints);

        lblLoanNoOfInstallments.setText("Repayment Installments");
        lblLoanNoOfInstallments.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanNoOfInstallments, gridBagConstraints);

        tdtLoanRepaymentStartDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLoanRepaymentStartDate.setName("tdtDateOfBirth");
        tdtLoanRepaymentStartDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(tdtLoanRepaymentStartDate, gridBagConstraints);

        lblLoanRepaymentStartDate.setText("Repayment Start Date");
        lblLoanRepaymentStartDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panEmployeeDateEntery.add(lblLoanRepaymentStartDate, gridBagConstraints);

        tdtLoanCloserDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLoanCloserDate.setName("tdtDateOfBirth");
        tdtLoanCloserDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(tdtLoanCloserDate, gridBagConstraints);

        lblLoanCloserDate.setText("Loan Closer Date");
        lblLoanCloserDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panEmployeeDateEntery.add(lblLoanCloserDate, gridBagConstraints);

        panLoanPreCloserYesNo.setMinimumSize(new java.awt.Dimension(164, 27));
        panLoanPreCloserYesNo.setName("panMaritalStatus");
        panLoanPreCloserYesNo.setPreferredSize(new java.awt.Dimension(164, 27));
        panLoanPreCloserYesNo.setLayout(new java.awt.GridBagLayout());

        rdoLoanPreCloser.add(rdoLoanPreCloserYes);
        rdoLoanPreCloserYes.setText("Yes");
        rdoLoanPreCloserYes.setMaximumSize(new java.awt.Dimension(61, 21));
        rdoLoanPreCloserYes.setMinimumSize(new java.awt.Dimension(61, 21));
        rdoLoanPreCloserYes.setName("rdoMaritalStatus_Single");
        rdoLoanPreCloserYes.setPreferredSize(new java.awt.Dimension(61, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLoanPreCloserYesNo.add(rdoLoanPreCloserYes, gridBagConstraints);

        rdoLoanPreCloser.add(rdoLoanPreCloserNo);
        rdoLoanPreCloserNo.setText("No");
        rdoLoanPreCloserNo.setName("rdoMaritalStatus_Married");
        rdoLoanPreCloserNo.setPreferredSize(new java.awt.Dimension(69, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLoanPreCloserYesNo.add(rdoLoanPreCloserNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEmployeeDateEntery.add(panLoanPreCloserYesNo, gridBagConstraints);

        lblLoanPreCloserYesNo.setText("Loan Pre Close");
        lblLoanPreCloserYesNo.setName("lblMaritalStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(lblLoanPreCloserYesNo, gridBagConstraints);

        tdtLoanRepaymentEndDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLoanRepaymentEndDate.setName("tdtDateOfBirth");
        tdtLoanRepaymentEndDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(tdtLoanRepaymentEndDate, gridBagConstraints);

        lblLoanRepaymentEndDate.setText("Repayment End  Date");
        lblLoanRepaymentEndDate.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panEmployeeDateEntery.add(lblLoanRepaymentEndDate, gridBagConstraints);

        txtLoanRemarks.setMaxLength(256);
        txtLoanRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanRemarks.setName("txtRemarks");
        txtLoanRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmployeeDateEntery.add(txtLoanRemarks, gridBagConstraints);

        lblLoanRemarks.setText("Remarks");
        lblLoanRemarks.setName("lblRemarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmployeeDateEntery.add(lblLoanRemarks, gridBagConstraints);

        panBtnTermLoans.setLayout(new java.awt.GridBagLayout());

        btnTermLoansDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnTermLoansDetSave.setText("Save");
        btnTermLoansDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnTermLoansDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnTermLoansDetSave.setName("btnContactAdd");
        btnTermLoansDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnTermLoansDetSave.setEnabled(false);
        btnTermLoansDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTermLoansDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnTermLoans.add(btnTermLoansDetSave, gridBagConstraints);

        btnTermLoansDetNew.setText("New");
        btnTermLoansDetNew.setName("btnContactNew");
        btnTermLoansDetNew.setEnabled(false);
        btnTermLoansDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTermLoansDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnTermLoans.add(btnTermLoansDetNew, gridBagConstraints);

        btnTermLoansDetDel.setText("Delete");
        btnTermLoansDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnTermLoansDetDel.setName("btnContactDelete");
        btnTermLoansDetDel.setEnabled(false);
        btnTermLoansDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTermLoansDetDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnTermLoans.add(btnTermLoansDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 3);
        panEmployeeDateEntery.add(panBtnTermLoans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panEmployeeLoan.add(panEmployeeDateEntery, gridBagConstraints);

        panEmployeeLoanTable.setMinimumSize(new java.awt.Dimension(310, 150));
        panEmployeeLoanTable.setName("panContacts");
        panEmployeeLoanTable.setPreferredSize(new java.awt.Dimension(310, 150));
        panEmployeeLoanTable.setLayout(new java.awt.GridBagLayout());

        srpEmployeeLoan.setMinimumSize(new java.awt.Dimension(240, 75));
        srpEmployeeLoan.setName("srpContactList");
        srpEmployeeLoan.setPreferredSize(new java.awt.Dimension(240, 75));

        tblEmployeeLoan.setName("tblContactList");
        tblEmployeeLoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblEmployeeLoanMousePressed(evt);
            }
        });
        srpEmployeeLoan.setViewportView(tblEmployeeLoan);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panEmployeeLoanTable.add(srpEmployeeLoan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panEmployeeLoan.add(panEmployeeLoanTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLandDetails.add(panEmployeeLoan, gridBagConstraints);

        panLanguageStatus1.setBorder(javax.swing.BorderFactory.createTitledBorder("Salary Credit Account Details"));
        panLanguageStatus1.setMinimumSize(new java.awt.Dimension(840, 185));
        panLanguageStatus1.setPreferredSize(new java.awt.Dimension(840, 185));
        panLanguageStatus1.setLayout(new java.awt.GridBagLayout());

        panOprativeDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panOprativeDetails.setMaximumSize(new java.awt.Dimension(500, 150));
        panOprativeDetails.setMinimumSize(new java.awt.Dimension(500, 150));
        panOprativeDetails.setPreferredSize(new java.awt.Dimension(500, 140));
        panOprativeDetails.setLayout(new java.awt.GridBagLayout());

        panCustId1.setLayout(new java.awt.GridBagLayout());

        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panCustId1.add(txtAccountNo, gridBagConstraints);

        btnAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo.setMinimumSize(new java.awt.Dimension(28, 28));
        btnAccountNo.setPreferredSize(new java.awt.Dimension(18, 18));
        btnAccountNo.setEnabled(false);
        btnAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustId1.add(btnAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOprativeDetails.add(panCustId1, gridBagConstraints);

        lblAccountNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOprativeDetails.add(lblAccountNo, gridBagConstraints);

        lblAccountType.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOprativeDetails.add(lblAccountType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOprativeDetails.add(cboAccountType, gridBagConstraints);

        cboSalaryCrBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSalaryCrBranch.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOprativeDetails.add(cboSalaryCrBranch, gridBagConstraints);

        lblSalaryCrBranch.setText("Working At");
        lblSalaryCrBranch.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panOprativeDetails.add(lblSalaryCrBranch, gridBagConstraints);

        panBtnOprativeDetails.setLayout(new java.awt.GridBagLayout());

        btnOprativeDetailsDetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnOprativeDetailsDetSave.setText("Save");
        btnOprativeDetailsDetSave.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnOprativeDetailsDetSave.setMinimumSize(new java.awt.Dimension(70, 27));
        btnOprativeDetailsDetSave.setName("btnContactAdd");
        btnOprativeDetailsDetSave.setPreferredSize(new java.awt.Dimension(70, 27));
        btnOprativeDetailsDetSave.setEnabled(false);
        btnOprativeDetailsDetSave.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnOprativeDetailsDetSaveItemStateChanged(evt);
            }
        });
        btnOprativeDetailsDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOprativeDetailsDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panBtnOprativeDetails.add(btnOprativeDetailsDetSave, gridBagConstraints);

        btnOprativeDetailsDetNew.setText("New");
        btnOprativeDetailsDetNew.setName("btnContactNew");
        btnOprativeDetailsDetNew.setEnabled(false);
        btnOprativeDetailsDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOprativeDetailsDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnOprativeDetails.add(btnOprativeDetailsDetNew, gridBagConstraints);

        btnOprativeDetailsDetDel.setText("Delete");
        btnOprativeDetailsDetDel.setMargin(new java.awt.Insets(2, 9, 2, 9));
        btnOprativeDetailsDetDel.setName("btnContactDelete");
        btnOprativeDetailsDetDel.setEnabled(false);
        btnOprativeDetailsDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOprativeDetailsDetDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panBtnOprativeDetails.add(btnOprativeDetailsDetDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 3);
        panOprativeDetails.add(panBtnOprativeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panLanguageStatus1.add(panOprativeDetails, gridBagConstraints);

        panLanguageTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLanguageTable1.setMinimumSize(new java.awt.Dimension(340, 150));
        panLanguageTable1.setName("panContacts");
        panLanguageTable1.setPreferredSize(new java.awt.Dimension(340, 150));
        panLanguageTable1.setLayout(new java.awt.GridBagLayout());

        srpOprative.setMinimumSize(new java.awt.Dimension(340, 75));
        srpOprative.setName("srpContactList");
        srpOprative.setPreferredSize(new java.awt.Dimension(340, 75));

        tblOprative.setName("tblContactList");
        tblOprative.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblOprativeMousePressed(evt);
            }
        });
        srpOprative.setViewportView(tblOprative);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 4);
        panLanguageTable1.add(srpOprative, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panLanguageStatus1.add(panLanguageTable1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLandDetails.add(panLanguageStatus1, gridBagConstraints);

        tabIndCust.addTab("Oper a/c/Loan Details", panLandDetails);
        panLandDetails.getAccessibleContext().setAccessibleName("Loan Details");
        panLandDetails.getAccessibleContext().setAccessibleParent(tabIndCust);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCustomer.add(tabIndCust, gridBagConstraints);

        getContentPane().add(panCustomer, java.awt.BorderLayout.CENTER);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace35);

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

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace36);

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

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace38);

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

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace40);

        btnDeletedDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeletedDetails.setToolTipText("Enquiry Of Closed Individuals");
        btnDeletedDetails.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDeletedDetails.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDeletedDetails.setEnabled(false);
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

        mbrCustomer.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
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

    private void cboProbationPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProbationPeriodFocusLost
        // TODO add your handling code here:ZXX
         Date joinDt = DateUtil.getDateMMDDYYYY(tdtDateOfJoin.getDateValue());
            if(cboProbationPeriod.getSelectedItem().equals("Years")){
                int years = CommonUtil.convertObjToInt(txtProbationPeriod.getText());
                joinDt.setYear(joinDt.getYear() + years);
                String confimDate = CommonUtil.convertObjToStr(joinDt);
                System.out.println("!@##!change in year:"+confimDate);
                tdtConfirmationDate.setDateValue(confimDate);
            }else if(cboProbationPeriod.getSelectedItem().equals("Days")){
                int days = CommonUtil.convertObjToInt(txtProbationPeriod.getText());
                joinDt.setDate(joinDt.getDate() + days);
                String confimDate = CommonUtil.convertObjToStr(joinDt);
                System.out.println("!@##!change in days:"+confimDate);
                tdtConfirmationDate.setDateValue(confimDate);
            }else if(cboProbationPeriod.getSelectedItem().equals("Months")){
                int months = CommonUtil.convertObjToInt(txtProbationPeriod.getText());
                joinDt.setMonth(joinDt.getMonth() + months);
                String confimDate = CommonUtil.convertObjToStr(joinDt);
                System.out.println("!@##!change in months:"+confimDate);
                tdtConfirmationDate.setDateValue(confimDate);
            }
        Date confirmDt = DateUtil.getDateMMDDYYYY(tdtConfirmationDate.getDateValue());
        if(confirmDt != null){
            String workingSinceDt = CommonUtil.convertObjToStr(confirmDt);
            tdtWorkingSince.setDateValue(workingSinceDt);
        }
    }//GEN-LAST:event_cboProbationPeriodFocusLost
    
    private void cboProbationPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProbationPeriodActionPerformed
        // TODO add your handling code here:
       
//            Date joinDt = DateUtil.getDateMMDDYYYY(tdtDateOfJoin.getDateValue());
//            if(cboProbationPeriod.getSelectedItem().equals("Years")){
//                int years = CommonUtil.convertObjToInt(txtProbationPeriod.getText());
//                joinDt.setYear(joinDt.getYear() + years);
//                String confimDate = CommonUtil.convertObjToStr(joinDt);
//                System.out.println("!@##!change in year:"+confimDate);
//                tdtConfirmationDate.setDateValue(confimDate);
//            }else if(cboProbationPeriod.getSelectedItem().equals("Days")){
//                int days = CommonUtil.convertObjToInt(txtProbationPeriod.getText());
//                joinDt.setDate(joinDt.getDate() + days);
//                String confimDate = CommonUtil.convertObjToStr(joinDt);
//                System.out.println("!@##!change in days:"+confimDate);
//                tdtConfirmationDate.setDateValue(confimDate);
//            }else if(cboProbationPeriod.getSelectedItem().equals("Months")){
//                int months = CommonUtil.convertObjToInt(txtProbationPeriod.getText());
//                joinDt.setMonth(joinDt.getMonth() + months);
//                String confimDate = CommonUtil.convertObjToStr(joinDt);
//                System.out.println("!@##!change in months:"+confimDate);
//                tdtConfirmationDate.setDateValue(confimDate);
//            }
//        Date confirmDt = DateUtil.getDateMMDDYYYY(tdtConfirmationDate.getDateValue());
//        if(confirmDt != null){
//            String workingSinceDt = CommonUtil.convertObjToStr(confirmDt);
//            tdtWorkingSince.setDateValue(workingSinceDt);
//        }
    }//GEN-LAST:event_cboProbationPeriodActionPerformed
    
    private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
        // TODO add your handling code here:
        popUp("OPERATIVE");
        txtAccountNo.setEnabled(false);
        cboSalaryCrBranch.setEnabled(false);
        cboAccountType.setEnabled(false);
    }//GEN-LAST:event_btnAccountNoActionPerformed
    
    private void tdtPromotionEffectiveDateValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPromotionEffectiveDateValueFocusLost
        Date promotionEffDate = DateUtil.getDateMMDDYYYY(tdtPromotionEffectiveDateValue.getDateValue());
        HashMap employeePromMap = new HashMap();
        employeePromMap.put("EMP_ID",CommonUtil.convertObjToStr(observable.getSysId()));
        List getPrevDate = ClientUtil.executeQuery("getPrevDateValue",employeePromMap);
        if(getPrevDate != null && getPrevDate.size()>0){
            employeePromMap = new HashMap();
            employeePromMap = (HashMap)getPrevDate.get(0);
            System.out.println("#@$@#$@#$getPrevDate:"+getPrevDate);
            Date prevPromEffDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(employeePromMap.get("EFFECTIVE_DATE")));
            if(promotionEffDate != null && prevPromEffDate != null ){
                if(promotionEffDate.before(prevPromEffDate)){
                    ClientUtil.showAlertWindow("Date lesser than the previous promotion's effective date!!");
                    tdtPromotionEffectiveDateValue.setDateValue("");
                }
            }
        }
    }//GEN-LAST:event_tdtPromotionEffectiveDateValueFocusLost
    
    private void tblPromotionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPromotionMouseClicked
        int rowCountPromTbl = tblPromotion.getRowCount();
        int selectedRow = tblPromotion.getSelectedRow();
        promotiondetailsFlag = true;
        updateOBFields();
        observable.setNewPromotion(false);
        final String promotion = (String) tblPromotion.getValueAt(tblPromotion.getSelectedRow(),0);
        observable.populatePromotion(tblPromotion.getSelectedRow());;
        populatePromotionDetails();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            if((selectedRow  + 1) < rowCountPromTbl){
                ClientUtil.enableDisable(panPromotionDetailsinfo,false);
                ClientUtil.enableDisable(panPromotionInfo,false);
                btnPromotionDelete.setEnabled(false);
                btnPromotionNew.setEnabled(false);
                btnPromotionSave.setEnabled(false);
            }
            else if((selectedRow  + 1) == rowCountPromTbl){
                String promSlNo = (String) tblPromotion.getValueAt(tblPromotion.getSelectedRow(),0);
                HashMap chkAuthMap = new HashMap();
                chkAuthMap.put("SL_NO",promSlNo);
                chkAuthMap.put("EMP_ID",CommonUtil.convertObjToStr(observable.getSysId()));
                List checkAuthList = ClientUtil.executeQuery("chckIfAuthPromotion",chkAuthMap);
                if(checkAuthList != null && checkAuthList.size() >0){
                    ClientUtil.enableDisable(panPromotionInfo,true);
                    btnPromotionDelete.setEnabled(true);
                    btnPromotionNew.setEnabled(false);
                    btnPromotionSave.setEnabled(true);
                }
                else{
                    ClientUtil.showAlertWindow("the record has already been AUTHORIZED,cannot modify");
                    ClientUtil.enableDisable(panPromotionDetailsinfo,false);
                    ClientUtil.enableDisable(panPromotionInfo,false);
                    btnPromotionDelete.setEnabled(false);
                    btnPromotionNew.setEnabled(true);
                    btnPromotionSave.setEnabled(false);
                }
            }
            txtPromotionSalId.setEnabled(false);
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblPromotionMouseClicked
    
    private void txtPromotionLastGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPromotionLastGradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPromotionLastGradeActionPerformed
    
    private void cboPromotionGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPromotionGradeActionPerformed
        if(cboPromotionGrade.getSelectedIndex()>0){
            HashMap designationMap = new HashMap();
            designationMap.put("LOOKUP_ID",cboPromotionGrade.getSelectedItem());
            observable.setPromotedDesignation(designationMap);
            cboPromotedDesignation.setModel(observable.getCbmPromotedDesignation());
        }
    }//GEN-LAST:event_cboPromotionGradeActionPerformed
    private void updatePromotion(int selectDARow) {
        observable.populatePromotion(selectDARow);
        populatePromotionDetails();
    }
    private void updatePromotionDetails() {
        populatePromotionDetails();
    }
    private void updateDirector() {
        cboDirectorTittle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmDirectorTittle().getDataForKey(observable.getCboDirectorTittle())));
        cboDirectorReleationShip.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmDirectorReleationShip().getDataForKey(observable.getTxtDirectorReleationShip())));
        txtDirectorFirstName.setText(observable.getTxtDirectorFirstName());
        txtDirectorMiddleName.setText(observable.getTxtDirectorMiddleName());
        txtDirectorLastName.setText(observable.getTxtDirectorLastName());
    }
    private void populatePromotionDetails(){
        txtPromotionEmployeeId.setText(observable.getTxtPromotionEmployeeId());
        txtPromotionEmployeeName.setText(observable.getTxtPromotionEmployeeName());
        txtPromotionLastGrade.setText(observable.getTxtPromotionLastGrade());
        txtPromotionLastDesg.setText(observable.getTxtPromotionLastDesg());
        tdtPromotionEffectiveDateValue.setDateValue(observable.getTdtPromotionEffectiveDateValue());
        tdtPromotionCreatedDateValue.setDateValue(observable.getTdtPromotionCreatedDateValue());
        txtPromotionBasicPayValue.setText(observable.getTxtPromotionBasicPayValue());
        txtNewBasic.setText(observable.getTxtNewBasic());
        txtPromotionSalId.setText(observable.getPromotionID());
        txtRemarks.setText(observable.getTxtRemarks());
        cboPromotionGrade.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmPromotionGradeValue().getDataForKey(observable.getcboPromotionGradeValue())));
        cboPromotedDesignation.setSelectedItem(observable.getCboPromotedDesignation());
    }
    private void btnPromotionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromotionDeleteActionPerformed
        
        try{
            
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deletePromotionDeatails(tblPromotion.getSelectedRow());
                ClientUtil.clearAll(panPromotionDetailsinfo);
                ClientUtil.enableDisable(panPromotionDetailsinfo,false);
                btnPromotionNew.setEnabled(true);
                btnPromotionSave.setEnabled(false);
                btnPromotionDelete.setEnabled(false);
                promotiondetailsFlag = false;
                updateOBFields();
                
            }else{
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPromotionDeleteActionPerformed
    private void resetPromotionForm(){
        txtPromotionEmployeeId.setText("");
        txtPromotionEmployeeName.setText("");
        txtPromotionLastDesg.setText("");
        txtPromotionLastDesg.setText("");
        txtPromotionBasicPayValue.setText("");
        tdtPromotionEffectiveDateValue.setDateValue("");
        tdtPromotionCreatedDateValue.setDateValue("");
        cboPromotionGrade.setSelectedItem("");
        cboPromotedDesignation.setSelectedItem("");
    }
    private void updateTable(){
        this.tblPromotion.setModel(observable.getTblPromotion());
        this.tblPromotion.revalidate();
    }
    private void enableDisablePromotion(boolean MAVal){
        txtPromotionEmployeeId.setEnabled(MAVal);
        txtPromotionEmployeeName.setEnabled(MAVal);
        tdtPromotionEffectiveDateValue.setEnabled(MAVal);
        tdtPromotionCreatedDateValue.setEnabled(MAVal);
        txtPromotionBasicPayValue.setEnabled(MAVal);
        txtPromotionLastGrade.setEnabled(MAVal);
        txtPromotionLastDesg.setEnabled(MAVal);
        txtPromotionBasicPayValue.setEnabled(MAVal);
        cboPromotionGrade.setEnabled(MAVal);
        cboPromotedDesignation.setEnabled(MAVal);
        btnPromotionNew.setEnabled(MAVal);
        btnPromotionSave.setEnabled(MAVal);
        btnPromotionDelete.setEnabled(MAVal);
    }
    private void btnPromotionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromotionSaveActionPerformed
        
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panAcademicEducationDateEntery);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if (promotiondetailsFlag == false) {
                    observable.promotionDeailsMap(-1,promotiondetailsFlag);
                }
                else{
                    observable.promotionDeailsMap(tblPromotion.getSelectedRow(),promotiondetailsFlag);
                }
                ClientUtil.clearAll(panPromotionDetailsinfo);
                ClientUtil.enableDisable(panPromotionDetailsinfo,false);
                btnPromotionNew.setEnabled(false);
                btnPromotionSave.setEnabled(false);
                btnPromotionDelete.setEnabled(false);
                updateOBFields();
                promotiondetailsFlag = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPromotionSaveActionPerformed
    
    private void btnPromotionNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromotionNewActionPerformed
        updateOBFields();
        observable.setNewPromotion(true);
        promotiondetailsFlag = false;
        ClientUtil.enableDisable(panPromotionInfo,true);
        observable.resetPromotion();
        observable.ttNotifyObservers();
        btnPromotionSave.setEnabled(true);
        btnPromotionDelete.setEnabled(false);
        btnPromotionNew.setEnabled(false);
        HashMap promotionDataMap = new HashMap();
        promotionDataMap.put("SYS_EMPID",CommonUtil.convertObjToStr(observable.getSysId()));
        List promotionExistList = ClientUtil.executeQuery("getPromotionExistDetails", promotionDataMap);
        if(promotionExistList!= null &&promotionExistList.size()>0){
            System.out.println("#@#$@#promotionExistList:"+promotionExistList);
            ClientUtil.showMessageWindow("Promotion pending for Authorization!!");
            ClientUtil.enableDisable(panPromotionDetailsinfo,false);
            btnPromotionSave.setEnabled(false);
        }
        else{
            ClientUtil.clearAll(panPromotionDetailsinfo);
            ClientUtil.enableDisable(panPromotionDetailsinfo,true);
            List PromotionDetailsLst = ClientUtil.executeQuery("getSelectEmployeePromotionDetails", promotionDataMap);
            if(PromotionDetailsLst!= null){
                promotionDataMap = new HashMap();
                promotionDataMap = (HashMap)PromotionDetailsLst.get(0);
                txtPromotionEmployeeId.setText(CommonUtil.convertObjToStr(promotionDataMap.get("EMPLOYEE_CODE")));
                txtPromotionEmployeeName.setText(CommonUtil.convertObjToStr(promotionDataMap.get("FNAME")));
                txtPromotionLastGrade.setText(CommonUtil.convertObjToStr(promotionDataMap.get("PRESENT_GRADE")));
                txtPromotionLastDesg.setText(CommonUtil.convertObjToStr(promotionDataMap.get("DESIG_ID")));
                txtPromotionBasicPayValue.setText(CommonUtil.convertObjToStr(promotionDataMap.get("BASICS")));
            }
            
            txtPromotionSalId.setEnabled(false);
            tdtPromotionCreatedDateValue.setDateValue(CommonUtil.convertObjToStr(curDate.clone()));
            tdtPromotionCreatedDateValue.setEnabled(false);
            txtPromotionEmployeeId.setEnabled(false);
            txtPromotionEmployeeName.setEnabled(false);
            txtPromotionLastGrade.setEnabled(false);
            txtPromotionLastDesg.setEnabled(false);
            txtPromotionBasicPayValue.setEnabled(false);
        }
    }//GEN-LAST:event_btnPromotionNewActionPerformed
    
    private void cboReligionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReligionActionPerformed
        // TODO add your handling code here:
        if(cboReligion.getSelectedIndex()>0){
            HashMap religionMap = new HashMap();
            religionMap.put("LOOKUP_ID",cboReligion.getSelectedItem());
            observable.setCaste(religionMap);
            cboCaste.setModel(observable.getCbmCaste());
        }
    }//GEN-LAST:event_cboReligionActionPerformed
    
    private void cboPresentGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPresentGradeActionPerformed
        if(cboPresentGrade.getSelectedIndex()>0){
            HashMap designationMap = new HashMap();
            designationMap.put("LOOKUP_ID",cboPresentGrade.getSelectedItem());
            observable.setDesignation(designationMap);
            cboDesignation.setModel(observable.getCbmDesignation());
        }
    }//GEN-LAST:event_cboPresentGradeActionPerformed
    
    private void tdtConfirmationDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtConfirmationDateFocusLost
        Date confirmDt = DateUtil.getDateMMDDYYYY(tdtConfirmationDate.getDateValue());
        if(confirmDt != null){
            String workingSinceDt = CommonUtil.convertObjToStr(confirmDt);
            tdtWorkingSince.setDateValue(workingSinceDt);
        }
    }//GEN-LAST:event_tdtConfirmationDateFocusLost
    
    private void cboEmployeeLoanTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEmployeeLoanTypeActionPerformed
        final String loans = (String)((ComboBoxModel)cboEmployeeLoanType.getModel()).getKeyForSelected();
        if (cboEmployeeLoanType.getSelectedIndex() != 0 && chkLoansExistance(loans)){
            observable.setNewLoans(false);
            observable.LoansTypeChanged(loans);
            //To enable contact buttons for NEW & EDIT
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                ClientUtil.enableDisable(panTechnicalQualificationDateEntery,true);
                btnTechnicalQualificationDetDel.setEnabled(true);
                btnTechnicalQualificationDetNew.setEnabled(false);
                btnTechnicalQualificationDetSave.setEnabled(true);
            }else  if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()== ClientConstants.ACTIONTYPE_AUTHORIZE){
            }
        }else {
            observable.setNewLoans(true);
        }
    }//GEN-LAST:event_cboEmployeeLoanTypeActionPerformed
    
    private void tdtWorkingSinceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtWorkingSinceFocusLost
        // To Validate wheather the Working Since date is not more than the sys date
        ClientUtil.validateLTDate(tdtWorkingSince);
    }//GEN-LAST:event_tdtWorkingSinceFocusLost
    
    private void tdtPassportIssueDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPassportIssueDtFocusLost
        // To Validate wheather the Passport Issue date is not more than the sys date
        ClientUtil.validateLTDate(tdtPassportIssueDt);
    }//GEN-LAST:event_tdtPassportIssueDtFocusLost
    
    private void tdtPassportValidUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPassportValidUptoFocusLost
        // To Validate wheather the Passport Issue date is not more than the passport Valid Upto Date
        
        Date passportIssueDt= DateUtil.getDateMMDDYYYY(tdtPassportIssueDt.getDateValue());
        Date passportValidUptoDt= DateUtil.getDateMMDDYYYY(tdtPassportValidUpto.getDateValue());
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            if(passportValidUptoDt!=null){
                if(DateUtil.dateDiff(passportValidUptoDt,passportIssueDt)>0){
                    ClientUtil.displayAlert("Passport Valid upto Should be greater than Issue date");
                    tdtPassportValidUpto.setDateValue("");
                }
            }
        }
    }//GEN-LAST:event_tdtPassportValidUptoFocusLost
    
    private void tdtRelationShipDateofBirthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRelationShipDateofBirthFocusLost
        // To Validate wheather the DOB of relative is not more than the sys date
        ClientUtil.validateLTDate(tdtRelationShipDateofBirth);
    }//GEN-LAST:event_tdtRelationShipDateofBirthFocusLost
    
    private void txtPanNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPanNumberFocusLost
        // TODO add your handling code here:
        if(txtPanNumber.getText().length()>0 && !ClientUtil.validatePAN(txtPanNumber)){
            ClientUtil.showMessageWindow("Invalid Pan Number, Enter Proper Pan No (Format :ABCDE1234F)");
            txtPanNumber.setText("");
        }
        
    }//GEN-LAST:event_txtPanNumberFocusLost
    
    private void tabIndCustMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabIndCustMouseClicked
        // TODO add your handling code here:
        tabInduFocused =true;
    }//GEN-LAST:event_tabIndCustMouseClicked
    
    private void tabIndCustStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabIndCustStateChanged
        // TODO add your handling code here:
        if (tabInduFocused) {
            if (tabIndCust.getTitleAt(tabIndCust.getSelectedIndex()).equals("Authorized Persons")) {
                tabIndCust.setSelectedIndex(1);
            }
        }
        
    }//GEN-LAST:event_tabIndCustStateChanged
    
    private void tdtDateOfBirthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateOfBirthFocusLost
        // To Validate wheather the DOB is not more than the sys date
        ClientUtil.validateLTDate(tdtDateOfBirth);
        Date birthDt = DateUtil.getDateMMDDYYYY(tdtDateOfBirth.getDateValue());
        if(birthDt != null){
            birthDt.setYear(birthDt.getYear() +60);
            String confimDate = CommonUtil.convertObjToStr(birthDt);
            tdtDateofRetirement.setDateValue(confimDate);
        }
    }//GEN-LAST:event_tdtDateOfBirthFocusLost
    
    private void tblEmployeeLoanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeLoanMousePressed
        updateOBFields();
        observable.populateLoans(tblEmployeeLoan.getSelectedRow());
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panEmployeeDateEntery,false);
            btnTermLoansDetSave.setEnabled(false);
            btnTermLoansDetDel.setEnabled(false);
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
        
    }//GEN-LAST:event_tblEmployeeLoanMousePressed
    
    private void btnTermLoansDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTermLoansDetDelActionPerformed
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteLoans(tblEmployeeLoan.getSelectedRow());
                ClientUtil.clearAll(panEmployeeDateEntery);
                ClientUtil.enableDisable(panEmployeeDateEntery,false);
                btnTermLoansDetSave.setEnabled(false);
                btnTermLoansDetDel.setEnabled(false);
                updateOBFields();
            }else{
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTermLoansDetDelActionPerformed
    
    private void btnTermLoansDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTermLoansDetSaveActionPerformed
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panEmployeeDateEntery);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                observable.loans();
                ClientUtil.clearAll(panEmployeeDateEntery);
                ClientUtil.enableDisable(panEmployeeDateEntery,false);
                btnTermLoansDetSave.setEnabled(false);
                btnTermLoansDetDel.setEnabled(false);
                updateOBFields();
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTermLoansDetSaveActionPerformed
    
    private void tblOprativeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOprativeMousePressed
        
        operativeFlag = true;
        updateOBFields();
        observable.setNewOperative(false);
        observable.populateOprative(tblOprative.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panOprativeDetails,true);
            btnOprativeDetailsDetDel.setEnabled(true);
            btnOprativeDetailsDetNew.setEnabled(false);
            btnOprativeDetailsDetSave.setEnabled(true);
            
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblOprativeMousePressed
    
    private void btnOprativeDetailsDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOprativeDetailsDetDelActionPerformed
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteOprative(tblOprative.getSelectedRow());
                ClientUtil.clearAll(panOprativeDetails);
                ClientUtil.enableDisable(panOprativeDetails,false);
                btnOprativeDetailsDetNew.setEnabled(true);
                btnOprativeDetailsDetSave.setEnabled(false);
                btnOprativeDetailsDetDel.setEnabled(false);
                observable.resetOprative();
                operativeFlag = false;
                updateOBFields();
            }else{
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnOprativeDetailsDetDelActionPerformed
    
    private void btnOprativeDetailsDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOprativeDetailsDetSaveActionPerformed
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panOprativeDetails);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if (operativeFlag == false) {
                    observable.oprateive(-1,operativeFlag);
                }
                else{
                    observable.oprateive(tblOprative.getSelectedRow(),operativeFlag);
                }
                ClientUtil.clearAll(panOprativeDetails);
                ClientUtil.enableDisable(panOprativeDetails,false);
                btnOprativeDetailsDetNew.setEnabled(true);
                btnOprativeDetailsDetSave.setEnabled(false);
                btnOprativeDetailsDetDel.setEnabled(false);
                updateOBFields();
                operativeFlag = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnOprativeDetailsDetSaveActionPerformed
    
    private void btnTermLoansDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTermLoansDetNewActionPerformed
        ClientUtil.enableDisable(panEmployeeDateEntery,true);
        updateOBFields();
        observable.setNewLoans(true);
        observable.resetLoans();
        observable.ttNotifyObservers();
        btnTermLoansDetSave.setEnabled(true);
        btnTermLoansDetDel.setEnabled(false);
    }//GEN-LAST:event_btnTermLoansDetNewActionPerformed
    
    private void btnOprativeDetailsDetSaveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnOprativeDetailsDetSaveItemStateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnOprativeDetailsDetSaveItemStateChanged
    
    private void btnOprativeDetailsDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOprativeDetailsDetNewActionPerformed
        if(tblOprative.getRowCount() >= 1){
            ClientUtil.showMessageWindow("A record already exists,cannot insert a new record!!");
        }else{
            ClientUtil.enableDisable(panOprativeDetails,true);
            ClientUtil.enableDisable(panCustId1,true);
            btnAccountNo.setEnabled(true);
            updateOBFields();
            observable.setNewOperative(true);
            observable.resetOprative();
            observable.ttNotifyObservers();
            btnOprativeDetailsDetNew.setEnabled(false);
            btnOprativeDetailsDetSave.setEnabled(true);
            btnOprativeDetailsDetDel.setEnabled(false);
        }
    }//GEN-LAST:event_btnOprativeDetailsDetNewActionPerformed
    
    private void rdoAnyOtherIncrement_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAnyOtherIncrement_NoActionPerformed
        // TODO add your handling code here:
        if(rdoAnyOtherIncrement_Yes.isSelected()==true){
            tdtAnyOtherIncrementReleasedDate.setEnabled(true);
            txtAnyOtherIncrementInstitutionName.setEnabled(true);
        }
        else{
            tdtAnyOtherIncrementReleasedDate.setEnabled(false);
            txtAnyOtherIncrementInstitutionName.setEnabled(false);
        }
    }//GEN-LAST:event_rdoAnyOtherIncrement_NoActionPerformed
    
    private void rdoClubMembership_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoClubMembership_NoActionPerformed
        // TODO add your handling code here:
        if(rdoClubMembership_YES.isSelected()==true){
            txtClubName.setEnabled(true);
        }
        else{
            txtClubName.setEnabled(false);
        }
    }//GEN-LAST:event_rdoClubMembership_NoActionPerformed
    
    private void rdoClubMembership_YESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoClubMembership_YESActionPerformed
        // TODO add your handling code here:
        if(rdoClubMembership_YES.isSelected()==true){
            
            txtClubName.setEnabled(true);
        }
        else{
            txtClubName.setEnabled(false);
            
        }
    }//GEN-LAST:event_rdoClubMembership_YESActionPerformed
    
    private void rdoSignature_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSignature_NoActionPerformed
        // TODO add your handling code here:
        if(rdoSignature_Yes.isSelected()==true){
            txtSignatureNo.setEnabled(true);
        }
        else{
            txtSignatureNo.setEnabled(false);
        }
    }//GEN-LAST:event_rdoSignature_NoActionPerformed
    
    private void rdoSignature_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSignature_YesActionPerformed
        // TODO add your handling code here:
        if(rdoSignature_Yes.isSelected()==true){
            
            txtSignatureNo.setEnabled(true);
        }
        else{
            txtSignatureNo.setEnabled(false);
        }
    }//GEN-LAST:event_rdoSignature_YesActionPerformed
    
    private void rdoAnyOtherIncrement_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAnyOtherIncrement_YesActionPerformed
        // TODO add your handling code here:
        if(rdoAnyOtherIncrement_Yes.isSelected()==true){
            tdtAnyOtherIncrementReleasedDate.setEnabled(true);
            txtAnyOtherIncrementInstitutionName.setEnabled(true);
        }
        else{
            tdtAnyOtherIncrementReleasedDate.setEnabled(false);
            txtAnyOtherIncrementInstitutionName.setEnabled(false);
        }
    }//GEN-LAST:event_rdoAnyOtherIncrement_YesActionPerformed
    
    private void rdoCAIIBPART2Increment_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCAIIBPART2Increment_NoActionPerformed
        if(rdoCAIIBPART2Increment_Yes.isSelected()==true){
            tdtCAIIBPART2IncrementReleasedDate.setEnabled(true);
        }
        else{
            tdtCAIIBPART2IncrementReleasedDate.setEnabled(false);
        }
    }//GEN-LAST:event_rdoCAIIBPART2Increment_NoActionPerformed
    
    private void rdoCAIIBPART2Increment_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCAIIBPART2Increment_YesActionPerformed
        // TODO add your handling code here:
        if(rdoCAIIBPART2Increment_Yes.isSelected()==true){
            tdtCAIIBPART2IncrementReleasedDate.setEnabled(true);
        }
        else{
            tdtCAIIBPART2IncrementReleasedDate.setEnabled(false);
        }
    }//GEN-LAST:event_rdoCAIIBPART2Increment_YesActionPerformed
    
    private void rdoCAIIBPART1IncrementNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCAIIBPART1IncrementNoActionPerformed
        // TODO add your handling code here:
        if(rdoCAIIBPART1IncrementYes.isSelected()==true){
            tdtCAIIBPART1IncrementReleasedDate.setEnabled(true);
        }
        else{
            tdtCAIIBPART1IncrementReleasedDate.setEnabled(false);
        }
    }//GEN-LAST:event_rdoCAIIBPART1IncrementNoActionPerformed
    
    private void rdoCAIIBPART1IncrementYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCAIIBPART1IncrementYesActionPerformed
        // TODO add your handling code here:
        if(rdoCAIIBPART1IncrementYes.isSelected()==true){
            tdtCAIIBPART1IncrementReleasedDate.setEnabled(true);
        }
        else{
            tdtCAIIBPART1IncrementReleasedDate.setEnabled(false);
        }
    }//GEN-LAST:event_rdoCAIIBPART1IncrementYesActionPerformed
    
    private void rdoGradutionIncrementNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGradutionIncrementNoActionPerformed
        // TODO add your handling code here:
        if(rdoGradutionIncrementYes.isSelected()==true){
            tdtGradutionIncrementReleasedDate.setEnabled(true);
        }
        else{
            tdtGradutionIncrementReleasedDate.setEnabled(false);
        }
    }//GEN-LAST:event_rdoGradutionIncrementNoActionPerformed
    
    private void rdoGradutionIncrementYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGradutionIncrementYesActionPerformed
        // TODO add your handling code here:
        if(rdoGradutionIncrementYes.isSelected()==true){
            tdtGradutionIncrementReleasedDate.setEnabled(true);
        }
        else{
            tdtGradutionIncrementReleasedDate.setEnabled(false);
        }
    }//GEN-LAST:event_rdoGradutionIncrementYesActionPerformed
    
    private void rdoGradutionIncrementYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGradutionIncrementYesFocusLost
        if(rdoGradutionIncrementYes.isSelected()==true){
            tdtGradutionIncrementReleasedDate.setEnabled(true);
        }
        else{
            tdtGradutionIncrementReleasedDate.setEnabled(false);
        }
    }//GEN-LAST:event_rdoGradutionIncrementYesFocusLost
    
    private void tblDetailsOfRelativeDirectorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetailsOfRelativeDirectorMousePressed
        directorDetailsFlag = true;
        updateOBFields();
        observable.setNewDirector(false);
        observable.populateDirector(tblDetailsOfRelativeDirector.getSelectedRow());
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panDetailsOfRelativeDirector,true);
            btnRelativeDirectorDetDel.setEnabled(true);
            btnRelativeDirectorDetNew.setEnabled(false);
            btnRelativeDirectorDetSave.setEnabled(true);
            
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblDetailsOfRelativeDirectorMousePressed
    
    private void btnRelativeDirectorDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelativeDirectorDetDelActionPerformed
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteDirector(tblDetailsOfRelativeDirector.getSelectedRow());
                ClientUtil.enableDisable(panDetailsOfRelativeDirector,false);
                ClientUtil.clearAll(panDetailsOfRelativeDirector);
                btnRelativeDirectorDetNew.setEnabled(true);
                btnRelativeDirectorDetSave.setEnabled(false);
                btnRelativeDirectorDetDel.setEnabled(false);
                directorDetailsFlag = false;
                updateOBFields();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRelativeDirectorDetDelActionPerformed
    
    private void btnRelativeDirectorDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelativeDirectorDetSaveActionPerformed
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panDetailsOfRelativeDirector);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if(directorDetailsFlag == false){
                    //if the row is empty
                    observable.director(-1,directorDetailsFlag);
                }else{
                    
                    observable.director(tblDetailsOfRelativeDirector.getSelectedRow(),directorDetailsFlag);
                }
                ClientUtil.clearAll(panDetailsOfRelativeDirector);
                ClientUtil.enableDisable(panDetailsOfRelativeDirector,false);
                btnRelativeDirectorDetNew.setEnabled(true);
                btnRelativeDirectorDetSave.setEnabled(false);
                btnRelativeDirectorDetDel.setEnabled(false);
                updateOBFields();
                directorDetailsFlag = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRelativeDirectorDetSaveActionPerformed
    
    private void btnRelativeDirectorDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelativeDirectorDetNewActionPerformed
        updateOBFields();
        observable.setNewDirector(true);
        observable.resetDirector();
        ClientUtil.enableDisable(panDetailsOfRelativeDirector,true);
        btnRelativeDirectorDetNew.setEnabled(false);
        btnRelativeDirectorDetSave.setEnabled(true);
        btnRelativeDirectorDetDel.setEnabled(false);
    }//GEN-LAST:event_btnRelativeDirectorDetNewActionPerformed
    
    private void btnRelativeWorkingBramchDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelativeWorkingBramchDetDelActionPerformed
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteRelative(tblReleativeWorkingInBank.getSelectedRow());
                ClientUtil.enableDisable(tblReleativeWorkingInBank,false);
                btnRelativeWorkingBramchDetNew.setEnabled(true);
                btnRelativeWorkingBramchDetSave.setEnabled(false);
                btnRelativeWorkingBramchDetDel.setEnabled(false);
                relativeWorkingFlag = false;
                updateOBFields();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRelativeWorkingBramchDetDelActionPerformed
    
    private void tblReleativeWorkingInBankMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReleativeWorkingInBankMousePressed
        relativeWorkingFlag = true;
        updateOBFields();
        observable.setNewRelativesWorking(false);
        observable.populateReleative(tblReleativeWorkingInBank.getSelectedRow());
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panRelativesWorkingDetailsEntery,true);
            btnRelativeWorkingBramchDetDel.setEnabled(true);
            btnRelativeWorkingBramchDetNew.setEnabled(false);
            btnRelativeWorkingBramchDetSave.setEnabled(true);
            btnReleativeStaffId.setEnabled(true);
            cboRelativesWorkingTittle.setEnabled(false);
            txtRelativesWorkingFirstName.setEnabled(false);
            txtRelativesWorkingMiddleName.setEnabled(false);
            txtRelativesWorkingLastName.setEnabled(false);
            txtReleativeStaffId.setEnabled(false);
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblReleativeWorkingInBankMousePressed
    
    private void btnRelativeWorkingBramchDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelativeWorkingBramchDetSaveActionPerformed
        // TODO add your handling code here:
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panRelativesWorkingDetailsEntery);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if(relativeWorkingFlag == false) {
                    observable.relative(-1);
                }
                else{
                    observable.relative(tblReleativeWorkingInBank.getSelectedRow());
                }
                ClientUtil.clearAll(panRelativesWorkingDetailsEntery);
                ClientUtil.enableDisable(panRelativesWorkingDetailsEntery,false);
                btnRelativeWorkingBramchDetNew.setEnabled(true);
                btnRelativeWorkingBramchDetSave.setEnabled(false);
                btnRelativeWorkingBramchDetDel.setEnabled(false);
                btnReleativeStaffId.setEnabled(false);
                updateOBFields();
                relativeWorkingFlag= false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnRelativeWorkingBramchDetSaveActionPerformed
    
    private void btnReleativeStaffIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReleativeStaffIdActionPerformed
        popUp("STAFFID");
        cboRelativesWorkingTittle.setEnabled(false);
        txtRelativesWorkingFirstName.setEditable(false);
        txtRelativesWorkingMiddleName.setEditable(false);
        txtRelativesWorkingLastName.setEditable(false);
        txtReleativeStaffId.setEditable(false);
    }//GEN-LAST:event_btnReleativeStaffIdActionPerformed
    
    private void btnRelativeWorkingBramchDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelativeWorkingBramchDetNewActionPerformed
        updateOBFields();
        observable.setNewRelativesWorking(true);
        ClientUtil.enableDisable(panRelativesWorkingDetails,true);
        observable.resetRelative();
        observable.ttNotifyObservers();
        btnReleativeStaffId.setEnabled(true);
        btnRelativeWorkingBramchDetSave.setEnabled(true);
        btnRelativeWorkingBramchDetNew.setEnabled(false);
        btnRelativeWorkingBramchDetDel.setEnabled(false);
    }//GEN-LAST:event_btnRelativeWorkingBramchDetNewActionPerformed
    
    private void cboAddressTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAddressTypeActionPerformed
        // Add your handling code here:
        final String addrType = (String)((ComboBoxModel)cboAddressType.getModel()).getKeyForSelected();
        if (cboAddressType.getSelectedIndex() != 0 && chkContactExistance(addrType)){
            observable.setNewAddress(false);
            observable.addressTypeChanged(addrType);
            //To enable contact buttons for NEW & EDIT
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                objEmployeeMasterUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
                objEmployeeMasterUISupport.setContactAddEnableDisable(true,btnContactAdd);
                phoneDetailsBtnDefault();
                ClientUtil.enableDisable(panAddressDetails,true);
                ClientUtil.enableDisable(panPhoneAreaNumber,false);
            }else  if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()== ClientConstants.ACTIONTYPE_AUTHORIZE){
                objEmployeeMasterUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
                objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactToMain);
                objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
            }
        }else {
            observable.setNewAddress(true);
            objEmployeeMasterUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
            objEmployeeMasterUISupport.setContactAddEnableDisable(true,btnContactAdd);
            updateOBFields();
            observable.resetAddressExceptAddTypeDetails();
            observable.resetPhoneListTable();
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_cboAddressTypeActionPerformed
    
    private void btnSignRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignRemoveActionPerformed
        // TODO add your handling code here:
        lblSign.setIcon(null);
        btnSignRemove.setEnabled(false);
        observable.setSignFile(null);
        observable.setSignByteArray(null);
    }//GEN-LAST:event_btnSignRemoveActionPerformed
    
    private void btnSignLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignLoadActionPerformed
        observable.setScreen(getScreen());
        objEmployeeMasterUISupport.loadActivities(lblSign,btnSignRemove);
    }//GEN-LAST:event_btnSignLoadActionPerformed
    
    private void btnPhotoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoRemoveActionPerformed
        lblPhoto.setIcon(null);
        btnPhotoRemove.setEnabled(false);
        observable.setPhotoFile(null);
        observable.setPhotoByteArray(null);
    }//GEN-LAST:event_btnPhotoRemoveActionPerformed
    
    private void btnPhotoLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoLoadActionPerformed
        observable.setScreen(getScreen());
        objEmployeeMasterUISupport.loadActivities(lblPhoto,btnPhotoRemove);
    }//GEN-LAST:event_btnPhotoLoadActionPerformed
    
    private void btnClearPassportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPassportActionPerformed
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
    
    private void tblPhoneListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhoneListMousePressed
        phoneExist = true;
        updateOBFields();
        phoneRow = tblPhoneList.getSelectedRow();
        observable.populatePhone(phoneRow);
        ClientUtil.enableDisable(panPhoneAreaNumber,false);  //Changed true as false by Rajesh.
        updatePhone();
        //To enable PhoneDetails fields for NEW & EDIT options
        if(  observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && viewType != AUTHORIZE && (!actionType.equals("DeletedDetails"))){
            ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
            objEmployeeMasterUISupport.setPhoneButtonEnableDisable(true, btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        }
        if(getMode()==ClientConstants.ACTIONTYPE_VIEW_MODE){ //Added by Rajesh.
            ClientUtil.enableDisable(panPhoneType,false);
            btnPhoneNew.setEnabled(false);
            btnContactNoAdd.setEnabled(false);
            btnPhoneDelete.setEnabled(false);
        }
    }//GEN-LAST:event_tblPhoneListMousePressed
    
    private void tblContactListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContactListMousePressed
        updateOBFields();
        btnContactAdd.setEnabled(true);
        observable.populateAddress(tblContactList.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(true,btnContactNew, btnContactDelete, btnContactToMain);
            phoneDetailsBtnDefault();
            ClientUtil.enableDisable(panAddressDetails,true);
            ClientUtil.enableDisable(panPhoneAreaNumber,false);
            btnContactDelete.setEnabled(true);
            btnContactToMain.setEnabled(true);
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            
            
            objEmployeeMasterUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactToMain);
            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblContactListMousePressed
    
    private void btnContactToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactToMainActionPerformed
        observable.setCommunicationAddress(tblContactList.getSelectedRow());
        objEmployeeMasterUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
    }//GEN-LAST:event_btnContactToMainActionPerformed
    
    private void btnContactDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactDeleteActionPerformed
        try{
            if (((String)tblContactList.getModel().getValueAt(tblContactList.getSelectedRow(),0)).equals(observable.getCommAddrType())){
                objEmployeeMasterUISupport.displayAlert(resourceBundle.getString("mainAddrType"));
            }else{
                String alertMsg = "deleteWarningMsg";
                int optionSelected = observable.showAlertWindow(alertMsg);
                if(optionSelected==0){
                    observable.deleteAddress(tblContactList.getSelectedRow());
                }
                
            }
            objEmployeeMasterUISupport.setContactButtonEnableDisable(btnContactNew, btnContactDelete, btnContactToMain);
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnContactDeleteActionPerformed
    
    private void btnContactAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactAddActionPerformed
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panAddressDetails);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                
                final String alertMsg = "phoneDetailsMsg";
                int action = observable.showAlertWindow(alertMsg);
                if(action==0){
                    btnPhoneNew.setEnabled(true);
                    btnContactAdd.setEnabled(false);
                }else if(action==1){
                    observable.addAddressMap();
                    ClientUtil.clearAll(panContactInfo);
                    ClientUtil.enableDisable(panContactInfo,false);
                    objEmployeeMasterUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew,btnContactNoAdd,btnPhoneDelete);
                    objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
                }
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnContactAddActionPerformed
    
    private void btnContactNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactNewActionPerformed
        // TODO add your handling code here:
        // Add your handling code here:
        updateOBFields();
        observable.setNewAddress(true);
        ClientUtil.enableDisable(panAddressDetails,true);
        btnContactAdd.setEnabled(true);
        observable.resetNewAddress();
        objEmployeeMasterUISupport.setPhoneButtonEnableDisable(false,btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        objEmployeeMasterUISupport.setContactButtonEnableDisable(btnContactAdd,btnContactDelete,btnContactToMain);
    }//GEN-LAST:event_btnContactNewActionPerformed
    
    private void btnPhoneDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneDeleteActionPerformed
        // TODO add your handling code here:
        // Add your handling code here:
        observable.deletePhoneDetails(tblPhoneList.getSelectedRow());
        updatePhone();
        phoneDetailsBtnDefault();
    }//GEN-LAST:event_btnPhoneDeleteActionPerformed
    
    private void phoneDetailsBtnDefault(){
        ClientUtil.enableDisable(this.panPhoneAreaNumber, false);
        objEmployeeMasterUISupport.setPhoneButtonEnableDisableDefault( btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
    }
    private void btnContactNoAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContactNoAddActionPerformed
        // TODO add your handling code here:
        if( mandatoryMessage.length() > 0 ){
            objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
        }else{
            updateOBFields();
            observable.addPhoneList(phoneExist,phoneRow);
            updatePhone();
            observable.addAddressMap();
            objEmployeeMasterUISupport.setPhoneButtonEnableDisableDefault(btnPhoneNew,btnContactNoAdd,btnPhoneDelete);
            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
            ClientUtil.enableDisable(panPhoneAreaNumber,false);
            ClientUtil.enableDisable(panAddressDetails,false);
            btnContactAdd.setEnabled(true);
        }
    }//GEN-LAST:event_btnContactNoAddActionPerformed
    
    private void btnPhoneNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhoneNewActionPerformed
        phoneExist = false;
        phoneRow = tblPhoneList.getModel().getRowCount();
        updateOBFields();
        ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
        observable.resetPhoneDetails();
        updatePhone();
        objEmployeeMasterUISupport.setPhoneButtonEnableDisableNew(btnPhoneNew, btnContactNoAdd, btnPhoneDelete);
        ClientUtil.enableDisable(this.panPhoneAreaNumber, true);
    }//GEN-LAST:event_btnPhoneNewActionPerformed
    
    private void txtTechnicalQualificationTotMarksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTechnicalQualificationTotMarksFocusLost
        // TODO add your handling code here:
        double scoredMarks=CommonUtil.convertObjToDouble(txtTechnicalQualificationMarksScored.getText()).doubleValue();
        double totMarks=CommonUtil.convertObjToDouble(txtTechnicalQualificationTotMarks.getText()).doubleValue();
        double per=(scoredMarks*100)/totMarks;
        Rounding rod =new Rounding();
        
        per = (double)rod.getNearest((long)(per *100),100)/100;
        txtTechnicalQualificationTotMarksPer.setText(CommonUtil.convertObjToStr(new Double(per)));
        txtTechnicalQualificationTotMarksPer.setEnabled(false);
    }//GEN-LAST:event_txtTechnicalQualificationTotMarksFocusLost
    
    private void txtTotMarksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotMarksFocusLost
        double scoredMarks=CommonUtil.convertObjToDouble(txtMarksScored.getText()).doubleValue();
        double totMarks=CommonUtil.convertObjToDouble(txtTotMarks.getText()).doubleValue();
        double per=(scoredMarks*100)/totMarks;
        Rounding rod =new Rounding();
        per = (double)rod.getNearest((long)(per *100),100)/100;
        txtTotMarksPer.setText(CommonUtil.convertObjToStr(new Double(per)));
        txtTotMarksPer.setEnabled(false);
    }//GEN-LAST:event_txtTotMarksFocusLost
    
    
    private void updatePhone(){
        tblPhoneList.setModel(observable.getTblPhoneList());
        txtPhoneNumber.setText(observable.getTxtPhoneNumber());
        txtAreaCode.setText(observable.getTxtAreaCode());
        cboPhoneType.setSelectedItem(observable.getCboPhoneType());
    }
    private void tdtDateOfJoinFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateOfJoinFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtDateOfJoin);
        Date birthDt = DateUtil.getDateMMDDYYYY(tdtDateOfJoin.getDateValue());
        if(birthDt != null){
            birthDt.setYear(birthDt.getYear() +1);
            String confimDate = CommonUtil.convertObjToStr(birthDt);
            tdtNextIncrmentDate.setDateValue(confimDate);
        }
    }//GEN-LAST:event_tdtDateOfJoinFocusLost
    
    private void tblFamilyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFamilyMousePressed
        // TODO add your handling code here:
        familyDetailsFlag = true;
        //        updateTab = tblFamily.getSelectedRow();
        //        UpdateMode = true;
        updateOBFields();
        observable.setNewDependent(false);
        observable.populateDependent(tblFamily.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panFamilyDateEntery,true);
            btnfamiyDetDel.setEnabled(true);
            btnfamiyDetNew.setEnabled(false);
            btnfamiyDetSave.setEnabled(true);
            
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblFamilyMousePressed
    
    private void cboReletaionShipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReletaionShipActionPerformed
        final String relaation = (String)((ComboBoxModel)cboReletaionShip.getModel()).getKeyForSelected();
        if (cboReletaionShip.getSelectedIndex() != 0 && chkRelationExistance(relaation)){
            // observable.setNewDependent(false);
            observable.dependentTypeChanged(relaation);
            //To enable contact buttons for NEW & EDIT
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                ClientUtil.enableDisable(panAcademicEducation,true);
                btnfamiyDetDel.setEnabled(true);
                btnfamiyDetNew.setEnabled(false);
                btnfamiyDetSave.setEnabled(true);
            }else  if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()== ClientConstants.ACTIONTYPE_AUTHORIZE){
                
                
            }
        }else {
            
            // observable.setNewDependent(true);
            
        }
    }//GEN-LAST:event_cboReletaionShipActionPerformed
    
    private void btnfamiyDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfamiyDetDelActionPerformed
        try{
            
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteDependent(tblFamily.getSelectedRow());
                ClientUtil.clearAll(panFamilyDateEntery);
                ClientUtil.enableDisable(panFamilyDateEntery,false);
                //ClientUtil.enableDisable(tblFamily,false);
                btnfamiyDetNew.setEnabled(true);
                btnfamiyDetSave.setEnabled(false);
                btnfamiyDetDel.setEnabled(false);
                
                updateOBFields();
                familyDetailsFlag = false ;
                //updateDependentDetails();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnfamiyDetDelActionPerformed
    
    private void btnfamiyDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfamiyDetSaveActionPerformed
        // TODO add your handling code here:
        try{
            
            updateOBFields();
            
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panFamilyDateEntery);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if(familyDetailsFlag == false){
                    //if the row is empty
                    observable.dependent(-1,familyDetailsFlag);
                }else{
                    observable.dependent(tblFamily.getSelectedRow(),familyDetailsFlag);
                }
                ClientUtil.clearAll(panFamilyDateEntery);
                ClientUtil.enableDisable(panFamilyDateEntery,false);
                btnfamiyDetNew.setEnabled(true);
                btnfamiyDetSave.setEnabled(false);
                btnfamiyDetDel.setEnabled(false);
                updateOBFields();
                familyDetailsFlag = false;
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnfamiyDetSaveActionPerformed
    
    private void btnfamiyDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfamiyDetNewActionPerformed
        updateOBFields();
        observable.setNewDependent(true);
        ClientUtil.enableDisable(panFamilyDateEntery,true);
        observable.resetDependent();
        observable.ttNotifyObservers();
        btnfamiyDetSave.setEnabled(true);
        btnfamiyDetDel.setEnabled(false);
        btnfamiyDetNew.setEnabled(false);
    }//GEN-LAST:event_btnfamiyDetNewActionPerformed
    
    private void btnLanguageDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageDetDelActionPerformed
        // TODO add your handling code here:
        try{
            
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteLanguage(tblLanguage.getSelectedRow());
                ClientUtil.clearAll(panLanguageDate);
                ClientUtil.enableDisable(panLanguageDate,false);
                btnLanguageDetNew.setEnabled(true);
                btnLanguageDetSave.setEnabled(false);
                btnLanguageDetDel.setEnabled(false);
                updateOBFields();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLanguageDetDelActionPerformed
    
    private void tblLanguageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLanguageMousePressed
        updateOBFields();
        observable.populateLanguage(tblLanguage.getSelectedRow());
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panLanguageDate,true);
            btnLanguageDetDel.setEnabled(true);
            btnLanguageDetNew.setEnabled(false);
            btnLanguageDetSave.setEnabled(true);
            
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblLanguageMousePressed
    
    private void btnLanguageDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageDetSaveActionPerformed
        // TODO add your handling code here:
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panLanguageDate);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                observable.languageMap();
                ClientUtil.clearAll(panLanguageDate);
                //ClientUtil.clearAll(panLangues);
                ClientUtil.enableDisable(panLanguageDate,false);
                //ClientUtil.enableDisable(panLangues,false);
                rdoLanguageWrite.setSelected(false);
                btnLanguageDetNew.setEnabled(true);
                btnLanguageDetSave.setEnabled(false);
                btnLanguageDetDel.setEnabled(false);
                updateOBFields();
                
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btnLanguageDetSaveActionPerformed
    
    private void btnLanguageDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageDetNewActionPerformed
        updateOBFields();
        observable.setNewLanguage(true);
        ClientUtil.enableDisable(panLanguageDate,true);
        observable.resetLanguage();
        observable.ttNotifyObservers();
        btnLanguageDetSave.setEnabled(true);
        btnLanguageDetDel.setEnabled(false);
        btnLanguageDetNew.setEnabled(false);
    }//GEN-LAST:event_btnLanguageDetNewActionPerformed
    
    private void tblTechnicalEducationMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTechnicalEducationMousePressed
        // TODO add your handling code here:
        updateOBFields();
        final String technical = (String) tblTechnicalEducation.getValueAt(tblTechnicalEducation.getSelectedRow(),0);
        technicalEducationFlag = true;
        observable.setNewTechnical(false);
        observable.technicalTypeChanged(technical);
        observable.populateTechnical(tblTechnicalEducation.getSelectedRow());
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panTechnicalQualificationDateEntery,true);
            btnTechnicalQualificationDetDel.setEnabled(true);
            btnTechnicalQualificationDetNew.setEnabled(false);
            btnTechnicalQualificationDetSave.setEnabled(true);
            txtTechnicalQualificationTotMarksPer.setEnabled(false);
            
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblTechnicalEducationMousePressed
    
    private void btnAcademicEducationDetDelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_btnAcademicEducationDetDelComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAcademicEducationDetDelComponentResized
    
    private void cboTechnicalQualificationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTechnicalQualificationTypeActionPerformed
        // TODO add your handling code here:
        final String technical = (String)((ComboBoxModel)cboEmpLevelEducation.getModel()).getKeyForSelected();
        if (cboEmpLevelEducation.getSelectedIndex() != 0 && chkTechnicalExistance(technical)){
            //            observable.setNewTechnical(false);
            //            observable.technicalTypeChanged(technical);
            //To enable contact buttons for NEW & EDIT
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                ClientUtil.enableDisable(panTechnicalQualificationDateEntery,true);
                btnTechnicalQualificationDetDel.setEnabled(true);
                btnTechnicalQualificationDetNew.setEnabled(false);
                btnTechnicalQualificationDetSave.setEnabled(true);
            }else  if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()== ClientConstants.ACTIONTYPE_AUTHORIZE){
                
                
            }
        }else {
        }
    }//GEN-LAST:event_cboTechnicalQualificationTypeActionPerformed
    
    private void btnTechnicalQualificationDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTechnicalQualificationDetDelActionPerformed
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteTechnical(tblTechnicalEducation.getSelectedRow());
                ClientUtil.clearAll(panTechnicalQualificationDateEntery);
                ClientUtil.enableDisable(panTechnicalQualificationDateEntery,false);
                btnTechnicalQualificationDetNew.setEnabled(true);
                btnTechnicalQualificationDetSave.setEnabled(false);
                btnTechnicalQualificationDetDel.setEnabled(false);
                technicalEducationFlag = false;
                updateOBFields();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTechnicalQualificationDetDelActionPerformed
    
    private void btnTechnicalQualificationDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTechnicalQualificationDetSaveActionPerformed
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panTechnicalQualificationDateEntery);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if(technicalEducationFlag == false){
                    observable.technicalMap(-1,technicalEducationFlag);
                }
                else {
                    observable.technicalMap(tblTechnicalEducation.getSelectedRow(),technicalEducationFlag);
                }
                ClientUtil.clearAll(panTechnicalQualificationDateEntery);
                ClientUtil.enableDisable(panTechnicalQualificationDateEntery,false);
                btnTechnicalQualificationDetNew.setEnabled(true);
                btnTechnicalQualificationDetSave.setEnabled(false);
                btnTechnicalQualificationDetDel.setEnabled(false);
                updateOBFields();
                technicalEducationFlag = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTechnicalQualificationDetSaveActionPerformed
    
    private void btnTechnicalQualificationDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTechnicalQualificationDetNewActionPerformed
        ClientUtil.enableDisable(panTechnicalQualificationDateEntery,true);
        updateOBFields();
        observable.setNewTechnical(true);
        observable.resetTechnical();
        observable.ttNotifyObservers();
        btnTechnicalQualificationDetSave.setEnabled(true);
        btnTechnicalQualificationDetDel.setEnabled(false);
        btnTechnicalQualificationDetNew.setEnabled(false);
    }//GEN-LAST:event_btnTechnicalQualificationDetNewActionPerformed
    
    private void btnAcademicEducationDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcademicEducationDetDelActionPerformed
        // TODO add your handling code here:
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteEducation(tblAcademicEducation.getSelectedRow());
                ClientUtil.clearAll(panAcademicEducationDateEntery);
                ClientUtil.enableDisable(panAcademicEducation,false);
                btnAcademicEducationDetNew.setEnabled(true);
                btnAcademicEducationDetSave.setEnabled(false);
                btnAcademicEducationDetDel.setEnabled(false);
                academicEducationFlag = false;
                updateOBFields();
            }else{
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnAcademicEducationDetDelActionPerformed
    
    private void btnAcademicEducationDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcademicEducationDetSaveActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        try{
            updateOBFields();
            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panAcademicEducationDateEntery);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if (academicEducationFlag == false) {
                    observable.educationMap(-1,academicEducationFlag);
                }
                else{
                    observable.educationMap(tblAcademicEducation.getSelectedRow(),academicEducationFlag);
                }
                ClientUtil.clearAll(panAcademicEducationDateEntery);
                ClientUtil.enableDisable(panAcademicEducationDateEntery,false);
                btnAcademicEducationDetNew.setEnabled(true);
                btnAcademicEducationDetSave.setEnabled(false);
                btnAcademicEducationDetDel.setEnabled(false);
                updateOBFields();
                academicEducationFlag = false;
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnAcademicEducationDetSaveActionPerformed
    
    private void btnAcademicEducationDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcademicEducationDetNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panAcademicEducationDateEntery,true);
        updateOBFields();
        observable.setNewEducation(true);
        observable.resetAcademic();
        observable.ttNotifyObservers();
        btnAcademicEducationDetSave.setEnabled(true);
        btnAcademicEducationDetDel.setEnabled(false);
        btnAcademicEducationDetNew.setEnabled(false);
    }//GEN-LAST:event_btnAcademicEducationDetNewActionPerformed
    
    private void tblAcademicEducationMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAcademicEducationMousePressed
        academicEducationFlag = true;
        updateOBFields();
        observable.setNewEducation(false);
        final String education = (String) tblAcademicEducation.getValueAt(tblAcademicEducation.getSelectedRow(),0);
        observable.populateEducation(tblAcademicEducation.getSelectedRow());
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panAcademicEducation,true);
            btnAcademicEducationDetDel.setEnabled(true);
            btnAcademicEducationDetNew.setEnabled(false);
            btnAcademicEducationDetSave.setEnabled(true);
            txtTotMarksPer.setEnabled(false);
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblAcademicEducationMousePressed
    
    private void rdoFatherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFatherActionPerformed
        if(rdoFather.isSelected()==true){
            rdoFather.setSelected(true);
            rdoHusband.setSelected(false);
            lblFatherFirstName.setText("Father Name");
        }else {
            rdoHusband.setSelected(true);
            rdoFather.setSelected(false);
            lblFatherFirstName.setText("Husband Name");
        }
    }//GEN-LAST:event_rdoFatherActionPerformed
    
    private void rdoHusbandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHusbandActionPerformed
        if(rdoHusband.isSelected()==true){
            rdoHusband.setSelected(true);
            rdoFather.setSelected(false);
            lblFatherFirstName.setText("Husband Name");
        }else {
            rdoHusband.setSelected(false);
            rdoFather.setSelected(true);
            lblFatherFirstName.setText("Father Name");
        }
        
    }//GEN-LAST:event_rdoHusbandActionPerformed
    
    private void rdoFatherFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoFatherFocusLost
        if(rdoFather.isSelected()==true){
            lblFatherFirstName.setText("Father Name");
        }else{
            lblFatherFirstName.setText("Husband Name");
        }
        
    }//GEN-LAST:event_rdoFatherFocusLost
    
    private void rdoHusbandFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoHusbandFocusLost
        if(rdoHusband.isSelected()==true){
            lblFatherFirstName.setText("Husband Name");
        }else{
            lblFatherFirstName.setText("Father Name");
        }
        
    }//GEN-LAST:event_rdoHusbandFocusLost
    
    private void rdoGender_MaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGender_MaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoGender_MaleActionPerformed
    
    private void cboEmpLevelEducationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEmpLevelEducationActionPerformed
        // Add your handling code here:
        final String education = (String)((ComboBoxModel)cboEmpLevelEducation.getModel()).getKeyForSelected();
        if (cboEmpLevelEducation.getSelectedIndex() != 0 && chkEducationExistance(education)){
            //To enable contact buttons for NEW & EDIT
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                ClientUtil.enableDisable(panAcademicEducation,true);
                btnAcademicEducationDetDel.setEnabled(true);
                btnAcademicEducationDetNew.setEnabled(false);
                btnAcademicEducationDetSave.setEnabled(true);
                
            }else  if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()== ClientConstants.ACTIONTYPE_AUTHORIZE){
                
                
            }
        }else {
        }
    }//GEN-LAST:event_cboEmpLevelEducationActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp("ViewDetails");
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        setButtonEnableDisable();
        btnSignLoad.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnDeletedDetails.setEnabled(true);
        panBtnOprativeDetails.setEnabled(false);
        panBtnTermLoans.setEnabled(false);
        btnRelativeWorkingBramchDetNew.setEnabled(true);
        btnRelativeDirectorDetNew.setEnabled(true);
        btnClearPassport.setEnabled(true);
        ClientUtil.enableDisable(this,false);
        ClientUtil.enableDisable(panAdditionalInfo,true);
        btnContactNew.setEnabled(true);
        btnPhotoLoad.setEnabled(true);
        btnAcademicEducationDetNew.setEnabled(true);
        btnTechnicalQualificationDetNew.setEnabled(true);
        btnLanguageDetNew.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(panPassPortDetails,true);
        btnfamiyDetNew.setEnabled(true);
        rdoFather.setSelected(true);
        rdoHusband.setSelected(false);
        ClientUtil.enableDisable(panPromotionButtons,false);
        ClientUtil.enableDisable(panAcademicEducation,false);
        ClientUtil.enableDisable(panOfficeDetails,true);
        ClientUtil.enableDisable(panRelativesWorkingDetails,false);
        txtHomeTown.setText("Bangalore");
        btnRelativeDirectorDetNew.setEnabled(true);
        lblStatus.setText(observable.getLblStatus());
        observable.resetDirector();
        clearButton();
        objEmployeeMasterUISupport.setLblPhotoSignDefault(lblPhoto, lblSign);
    }//GEN-LAST:event_btnNewActionPerformed
    private void panPromotionEnable(){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panPromotionInfo, false);
            btnPromotionNew.setEnabled(true);
        }
        else {
            ClientUtil.enableDisable(panPromotionInfo, false);
            ClientUtil.enableDisable(panPromotionDetailsinfo, false);
            btnPromotionSave.setEnabled(true);
            btnPromotionDelete.setEnabled(true);
        }
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.resetDirector();
        popUp(ClientConstants.ACTION_STATUS[2]);
        setModified(true);
        observable.setStatus();
        btnEdit.setEnabled(false);
        btnSignLoad.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnDeletedDetails.setEnabled(true);
        btnClearPassport.setEnabled(true);
        ClientUtil.enableDisable(this,false);
        ClientUtil.enableDisable(panAdditionalInfo,true);
        btnContactNew.setEnabled(true);
        btnPhotoLoad.setEnabled(true);
        btnAcademicEducationDetNew.setEnabled(true);
        btnTechnicalQualificationDetNew.setEnabled(true);
        btnLanguageDetNew.setEnabled(true);
        ClientUtil.enableDisable(panPassPortDetails,true);
        btnfamiyDetNew.setEnabled(true);
        btnRelativeWorkingBramchDetNew.setEnabled(true);
        rdoFather.setSelected(true);
        rdoHusband.setSelected(false);
        txtEmpID.setEnabled(false);
        ClientUtil.enableDisable(tbrOperativeAcctProduct,true);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        btnContactNew.setEnabled(true);
        btnAcademicEducationDetNew.setEnabled(true);
        btnTechnicalQualificationDetNew.setEnabled(true);
        btnLanguageDetNew.setEnabled(true);
        btnfamiyDetNew.setEnabled(true);
        ClientUtil.clearAll(panDetailsOfRelativeDirector);
        ClientUtil.enableDisable(panDetailsOfRelativeDirector,false);
        btnRelativeDirectorDetNew.setEnabled(true);
        btnRelativeWorkingBramchDetNew.setEnabled(true);
        ClientUtil.enableDisable(panOfficeDetails,true);
        btnReleativeStaffId.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        System.out.println("reset Promotion");
        observable.resetPromotion();
        btnPromotionNew.setEnabled(true);
        btnOprativeDetailsDetNew.setEnabled(true);
        panPromotionEnable();    //To Enable the Promotion panel
        pan = PROMOTION;
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(this, false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        updateOBFields();
        mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panAdditionalInfo);
        mandatoryMessage += objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panOfficeDetails);
        
        if( mandatoryMessage.length() > 0 ){
            objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
        }else{
            observable.doAction();
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                btnCancelActionPerformed(null);
                observable.resetDirector();
                btnCancel.setEnabled(true);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            }
        }
        
    }//GEN-LAST:event_btnSaveActionPerformed
    public void clearButton() {
        //this function is used to disable buttons which are not required during the insert mode and to clear them during cancel
        btnContactAdd.setEnabled(false);
        btnfamiyDetDel.setEnabled(false);
        btnRelativeWorkingBramchDetSave.setEnabled(false);
        btnRelativeWorkingBramchDetDel.setEnabled(false);
        btnReleativeStaffId.setEnabled(false);
        btnRelativeDirectorDetSave.setEnabled(false);
        btnRelativeDirectorDetDel.setEnabled(false);
        btnAcademicEducationDetDel.setEnabled(false);
        btnTechnicalQualificationDetSave.setEnabled(false);
        btnTechnicalQualificationDetDel.setEnabled(false);
        btnAcademicEducationDetSave.setEnabled(false);
        btnLanguageDetSave.setEnabled(false);
        btnLanguageDetDel.setEnabled(false);
        btnOprativeDetailsDetSave.setEnabled(false);
        btnOprativeDetailsDetDel.setEnabled(false);
        btnTermLoansDetSave.setEnabled(false);
        btnTermLoansDetDel.setEnabled(false);
        btnfamiyDetSave.setEnabled(false);
        tdtGradutionIncrementReleasedDate.setEnabled(false);
        tdtCAIIBPART1IncrementReleasedDate.setEnabled(false);
        tdtAnyOtherIncrementReleasedDate.setEnabled(false);
        tdtCAIIBPART2IncrementReleasedDate.setEnabled(false);
        txtAnyOtherIncrementInstitutionName.setEnabled(false);
        txtSignatureNo.setEnabled(false);
    }
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if(observable.getAuthorizeStatus()!=null)
            setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        ClientUtil.enableDisable(this,false,false,true);
        HashMap map= new HashMap();
        map.put("SCREEN_ID",getScreen());
        map.put("RECORD_KEY", this.txtEmpID.getText());
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", curDate.clone());
        System.out.println("Record Key Map : " + map);
        ClientUtil.execute("deleteEditLock", map);
        setButtonEnableDisable();
        observable.setStatus();
        lblPhoto.setIcon(null);
        lblSign.setIcon(null);
        ClientUtil.clearAll(this);
        btnDeletedDetails.setEnabled(true);
        if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            cifClosingAlert();
        }
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnView.setEnabled(true);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(true);
        panBtnOprativeDetails.setEnabled(false);
        panBtnTermLoans.setEnabled(false);
        txtEmpID.setEnabled(true);
        txtEmpID.setEditable(true);
        btnEdit.setEnabled(true);
        lblStatus.setText("            ");
        observable.resetFormEmployeeMaster();
        tdtGradutionIncrementReleasedDate.setDateValue("");
        tdtCAIIBPART1IncrementReleasedDate.setDateValue("");
        tdtCAIIBPART2IncrementReleasedDate.setDateValue("");
        txtAnyOtherIncrementInstitutionName.setText("");
        tdtAnyOtherIncrementReleasedDate.setDateValue("");
        txtPresentBasic.setText("");
        tdtLastIncrmentDate.setDateValue("");
        txtLossPay_Months.setText("");
        txtLossOfpay_Days.setText("");
        tdtNextIncrmentDate.setDateValue("");
        tdtWorkingSince.setDateValue("");
        cboDesignation.setSelectedItem("");
        cboPromotedDesignation.setSelectedItem("");
        cboPresentGrade.setSelectedItem("");
        tdtConfirmationDate.setDateValue("");
        txtProbationPeriod.setText("");
        // added from here by nikhil 10-12-2010
        //        clearButton();
        observable.resetFormEmployeeMaster();
        observable.resetDirector();
        observable.resetPromotion();
        objEmployeeMasterUISupport.setLblPhotoSignDefault(lblPhoto, lblSign);
        btnContactAdd.setEnabled(false);
        btnContactNew.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnClearPassport.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnDeletedDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletedDetailsActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
        popUp("DeletedDetails");
        btnCheck();
        
    }//GEN-LAST:event_btnDeletedDetailsActionPerformed
    
    private void rdoGender_MaleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_MaleFocusGained
        // Add your handling code here:
        rdoGender_FemaleFocusGained(evt);
    }//GEN-LAST:event_rdoGender_MaleFocusGained
    
    private void rdoGender_MaleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_MaleFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoGender_MaleFocusLost
    
    private void rdoGender_FemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGender_FemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoGender_FemaleActionPerformed
    
    private void rdoGender_FemaleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoGender_FemaleFocusGained
        
    }//GEN-LAST:event_rdoGender_FemaleFocusGained
    
    private void txtEmpIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmpIDFocusLost
        // TODO add your handling code here:
        
        txtEmpID.setText(CommonUtil.convertObjToStr(txtEmpID.getText()).toUpperCase());
        HashMap  existMap=new HashMap();
        existMap.put("EMPLOYEEID",CommonUtil.convertObjToStr(txtEmpID.getText()));
        List lst=ClientUtil.executeQuery("empIdExistanceCheck",existMap);
        if(lst!=null && lst.size()>0){
            existMap=new HashMap();
            existMap=(HashMap)lst.get(0);
            ClientUtil.showMessageWindow("Employee already Exists: " + CommonUtil.convertObjToStr(existMap.get("EMP_NAME")));
            txtEmpID.setText("");
            existMap=null;
            
            
        }
        
        if(existMap.containsValue("")) {
            ClientUtil.showMessageWindow("EMPLOYEE ID CANNOT BE NULL!!");
        }
    }//GEN-LAST:event_txtEmpIDFocusLost
    
    private void rdoMaritalStatus_SingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMaritalStatus_SingleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMaritalStatus_SingleActionPerformed
    
    private void mitSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveAsActionPerformed
        
        
    }//GEN-LAST:event_mitSaveAsActionPerformed
    
    private void txtFirstNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFirstNameFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtFirstNameFocusLost
    
    /** To display customer list popup for Edit & Delete options */
    private void callView(int viewType,int panEditDelete){
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        this.viewType = viewType;
        if(viewType == PROMOTION_ID){
            //            To select Employee Values for Promotion related values
            viewMap.put(CommonConstants.MAP_NAME,"getSelectEmployeePromotionDetails");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }else if (viewType == EDIT || viewType == DELETE || viewType == VIEW ){
            if(viewType== VIEW){
                if(panEditDelete==PROMOTION){
                    viewMap.put(CommonConstants.MAP_NAME,"getSelectViewPromotionDetails");
                    where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    viewMap.put(CommonConstants.MAP_WHERE, where);
                }
            }
            //            TO Select values for EDIT and DELETE
            else if(panEditDelete==PROMOTION){
                viewMap.put(CommonConstants.MAP_NAME,"getSelectPromotionDetails");
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
            }
        }
        new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap,true).show();
    }
    
    private boolean chkContactExistance(String addrType){
        final ArrayList tbldata = new ArrayList();
        final int tblContactListSize = tblContactList.getRowCount();
        for (int i = 0;i<tblContactListSize;i++){
            tbldata.add(tblContactList.getValueAt(i, 0));
        }
        return tbldata.contains(addrType);
    }
    private boolean chkEducationExistance(String educationType){
        final ArrayList tbldata = new ArrayList();
        final int tblEducationSize = tblAcademicEducation.getRowCount();
        for (int i = 0;i<tblEducationSize;i++){
            tbldata.add(tblAcademicEducation.getValueAt(i, 0));
        }
        return tbldata.contains(educationType);
    }
    
    private boolean chkTechnicalExistance(String technical){
        final ArrayList tbldata = new ArrayList();
        final int tblTechnicalListSize = tblTechnicalEducation.getRowCount();
        for (int i = 0;i<tblTechnicalListSize;i++){
            tbldata.add(tblTechnicalEducation.getValueAt(i, 0));
        }
        return tbldata.contains(technical);
    }
    
    private boolean chkRelationExistance(String releation){
        final ArrayList tbldata = new ArrayList();
        final int tblTechnicalListSize = tblFamily.getRowCount();
        for (int i = 0;i<tblTechnicalListSize;i++){
            tbldata.add(tblFamily.getValueAt(i, 0));
        }
        return tbldata.contains(releation);
    }
    private boolean chkLoansExistance(String loans){
        final ArrayList tbldata = new ArrayList();
        final int tblLoansListSize = tblEmployeeLoan.getRowCount();
        for (int i = 0;i<tblLoansListSize;i++){
            tbldata.add(tblEmployeeLoan.getValueAt(i, 0));
        }
        return tbldata.contains(loans);
    }
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            observable.setResult(observable.getActionType());
            //Changed By Suresh
//            String strWarnMsg = tabIndCust.isAllTabsVisited();
            //strWarnMsg = strWarnMsg + tabIndCust.isAllTabsVisited();
//            if (strWarnMsg.length() > 0){
//                objEmployeeMasterUISupport.displayAlert(strWarnMsg);
//                return;
//            }
//            strWarnMsg = null;
            tabIndCust.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            if(authorizeStatus.equalsIgnoreCase("REJECTED") && flag == true) {
                singleAuthorizeMap.put("DELETESTATUS", "MODIFIED");
                singleAuthorizeMap.put(CommonConstants.STATUS, "REJECTED");
                singleAuthorizeMap.put("DELETEREMARKS", "");
                singleAuthorizeMap.put("STATUSCHECK", "");
            }
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curDate.clone());
            singleAuthorizeMap.put("EMP_ID", observable.getSysId());
            singleAuthorizeMap.put("INCREMENT_AMOUNT", "");
            singleAuthorizeMap.put("EFFECTIVEDATE",curDate);
            
            singleAuthorizeMap.put("CREATED_DATE", curDate);
            HashMap promotionDataMap = new HashMap();
            promotionDataMap.put("SYS_EMPID",CommonUtil.convertObjToStr(observable.getSysId()));
            List promotionExistList = ClientUtil.executeQuery("getPromotionExistDetails", promotionDataMap);
            if(promotionExistList!= null &&promotionExistList.size()>0){
                HashMap promotionMap = new HashMap();
                promotionMap = observable.getPromotionMapData();
                System.out.println("#@#$@#promotionMap:"+promotionMap);
                ArrayList addList =new ArrayList(promotionMap.keySet());
                String rowcount = (String) String.valueOf(tblPromotion.getRowCount());
                PromotionTO objPromotionTO = (PromotionTO) promotionMap.get(rowcount);
                System.out.println("#@#$@#promotionExistList:"+promotionExistList);
                singleAuthorizeMap.put("SL_NO", objPromotionTO.getPromotionID());
                singleAuthorizeMap.put("INCREMENT_REASON", "PROMOTION");
                singleAuthorizeMap.put("LAST_BASIC", objPromotionTO.getPresentBasic());
                singleAuthorizeMap.put("NEWBASIC", objPromotionTO.getTxtNewBasic());
                singleAuthorizeMap.put("PRESENT_GRADE", objPromotionTO.getPromotionGrade());
                singleAuthorizeMap.put("PRESENT_DISGNATION", objPromotionTO.getPromotionDesignation());
                singleAuthorizeMap.put("PROMOTION", "PROMOTION");
                singleAuthorizeMap.put("SYS_EMPID", CommonUtil.convertObjToStr(observable.getSysId()));
                singleAuthorizeMap.put("EMPLOYEEID", this.txtEmpID.getText());
            }else{
                
                singleAuthorizeMap.put("EMPLOYEEID", this.txtEmpID.getText());
                singleAuthorizeMap.put("LAST_BASIC", "");
                singleAuthorizeMap.put("NEWBASIC", this.txtPresentBasic.getText());
                singleAuthorizeMap.put("INCREMENT_REASON", "EMP_MASTER_UPDATE");
            }
            
            System.out.println("!@#$@#$@#$singleAuthorizeMap:"+singleAuthorizeMap);
            observable.authorizeEmployee(singleAuthorizeMap);
            //          .execute("authorizeIndEmployee", singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtEmpID.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            viewType = 0;
        } else{
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSelectIndiAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeIndEmployee");
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            objEmployeeMasterUISupport.setPhotoSignEnableDisableDefault(btnPhotoLoad, btnSignLoad, btnPhotoRemove, btnSignRemove);
        }
    }
    
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    /** To initialize comboboxes */
    private void initComponentData() {
        cboTitle.setModel(observable.getCbmTitle());
        cboFsatherTitle.setModel(observable.getCbmFsatherTitle());
        cboMotherTitle.setModel(observable.getCbmMotherTitle());
        cboReleationFHTitle.setModel(observable.getCbmReleationFHTitle());
        cboPFAcNominee.setModel(observable.getCbmPFAcNominee());
        cboAddressType.setModel(observable.getCbmAddressType());
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboCountry.setModel(observable.getCbmCountry());
        cboPassportTitle.setModel(observable.getCbmPassportTitle());
//        cboCaste.setModel(observable.getCbmCaste());
        cboReligion.setModel(observable.getCbmReligion());
        cboReletaionShip.setModel(observable.getCbmReleationShip());
        cboEmpLevelEducation.setModel(observable.getCbmLevelEducation());
        cboSpecilization.setModel(observable.getCbmSpecilization());
        cboFamilyMemberProf.setModel(observable.getCbmFamilyMemberProf());
        cboGrade.setModel(observable.getCbmGrade());
        cboTechnicalQualificationType.setModel(observable.getCbmTechnicalQualification());
        cboTechnicalQualificationSpecilization.setModel(observable.getCbmTechnicalQualificationSpecilization());
        cboTechnicalQualificationGrade.setModel(observable.getCbmTechnicalQualificationGrade());
        cboLanguageType.setModel(observable.getCbmLanguage());
        cboFamilyMemEducation.setModel(observable.getCbmFamilyMemEducation());
        cboPassportIssuePlace.setModel(observable.getCbmPassportIssuePlace());
        cboPhoneType.setModel(observable.getCbmPhoneType());
        cboBloodGroup.setModel(observable.getCbmBloodGroup());
        cboDomicileState.setModel(observable.getCbmDomicileState());
        cboRelativesWorkingTittle.setModel(observable.getCbmReleativeTittle());
        cboDirectorTittle.setModel(observable.getCbmDirectorTittle());
        cboWorkingDesignation.setModel(observable.getCbmReleativeDisg());
        cboRelativesWorkingReletionShip.setModel(observable.getCbmReleativeReleationShip());
        cboDirectorReleationShip.setModel(observable.getCbmDirectorReleationShip());
        cboUnionMember.setModel(observable.getCbmUnionMember());
        cboSocietyMember.setModel(observable.getCbmSocietyMember());
        cboProbationPeriod.setModel(observable.getCbmProbationPeriod());
        cboDesignation.setModel(observable.getCbmDesignation());
        cboPresentGrade.setModel(observable.getCbmPresentGrade());
        cboAccountType.setModel(observable.getCbmAccountType());
        cboEmployeeLoanType.setModel(observable.getCbmEmployeeLoanType());
        cboCaste.setModel(observable.getCbmCaste());
        cboRelativesWorkingBranch.setModel(observable.getCbmReleativeBranchId());
        cboPresentBranchId.setModel(observable.getCbmPresentBranchId());
        cboReginoalOffice.setModel(observable.getCbmReginoalOffice());
        cboZonalOffice.setModel(observable.getCbmZonalOffice());
        cboSalaryCrBranch.setModel(observable.getCbmSalaryCrBranch());
        cboLoanAvailedBranch.setModel(observable.getCbmLoanAvailedBranch());
        cboPromotionGrade.setModel(observable.getCbmPromotionGradeValue());
        //        cboPromotedDesignation.setModel(observable.getCbmPromotedDesignation());
        
    }
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /** To display customer list popup for Edit & Delete options */
    private void popUp(String actionType){
        this.actionType = actionType;
        if(actionType != null){
            final HashMap viewMap = new HashMap();
            HashMap wheres = new HashMap();
            
            if(actionType.equals(ClientConstants. ACTION_STATUS[2]))  {
                ArrayList lst = new ArrayList();
                lst.add("EMPLOYEE NO");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectEmployeeMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals(ClientConstants. ACTION_STATUS[3])){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectEmployeeMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            else if(actionType.equals("ViewDetails")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectEmployeeMasterTOList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }else if(actionType.length()>0 && actionType.equals("OPERATIVE")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                where.put("SYS_EMPID", observable.getSysId());
                viewMap.put(CommonConstants.MAP_NAME, "getEmployeeAcntNo");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.length()>0 && actionType.equals("STAFFID")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                where.put("SYS_EMPID", observable.getSysId());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectEmployeeMasterStaffId");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else{
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectEmployeeMasterDelete");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap,true).show();
        }
        
    }
    
    /** To get data based on customer id received from the popup and populate into the
     * screen
     */
    public void fillData(Object obj) {
        isFilled = true;
        setModified(true);
        final HashMap hash = (HashMap) obj;
        System.out.println("@@@@hash"+hash);
        String st = CommonUtil.convertObjToStr(hash.get("STATUS"));
        if(st.equalsIgnoreCase("DELETED")) {
            flag = true;
        }
        if(actionType.equals(ClientConstants.ACTION_STATUS[2]) ||
        actionType.equals(ClientConstants.ACTION_STATUS[3])|| actionType.equals("DeletedDetails") || actionType.equals("ViewDetails")|| viewType == AUTHORIZE ||
        getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            HashMap map = new HashMap();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                map.put("SYS_EMPID",hash.get("SYS_EMP_ID"));
                map.put(CommonConstants.MAP_WHERE, hash.get("SYS_EMP_ID"));
            }else{
                map.put("SYS_EMPID",hash.get("SYSTEM_NO"));
                map.put(CommonConstants.MAP_WHERE, hash.get("SYSTEM_NO"));
            }
            observable.getData(map);
            setButtonEnableDisable();
            ClientUtil.enableDisable(panMISKYC,false);
            //For EDIT option enable disable fields and controls appropriately
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                //                //                List authList=ClientUtil.executeQuery("authStatusEmployeMaster",hash);
                //                //                HashMap statusMap=new HashMap();
                //                //                statusMap=(HashMap)authList.get(0);
                //                //                String status=CommonUtil.convertObjToStr(statusMap.get("AUTHORIZED_DT"));
                //                //                if(!status.equals("")){
                //                //                    txtFirstName.setEnabled(false);
                //                //                    txtMiddleName.setEnabled(false);
                //                //                    txtLastName.setEnabled(false);
                //                //                }
                //                //                else{
                //                //                    txtFirstName.setEnabled(true);
                //                //                    txtMiddleName.setEnabled(true);
                //                //                    txtLastName.setEnabled(true);
                //                //                }
            }
            observable.setStatus();
            if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
                
                btnAuthorize.setVisible(false);
                btnReject.setVisible(false);
                btnException.setVisible(false);
            }
            
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                
            }
        }else if(actionType.length()>0 && actionType.equals("STAFFID")){
            System.out.println("@@@@hash"+hash);
            txtReleativeStaffId.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE NO")));
            txtRelativesWorkingFirstName.setText(CommonUtil.convertObjToStr(hash.get("FIRST_NAME")));
            txtRelativesWorkingMiddleName.setText(CommonUtil.convertObjToStr(hash.get("MIDDLE_NAME")));
            txtRelativesWorkingLastName.setText(CommonUtil.convertObjToStr(hash.get("LAST_NAME")));
            cboRelativesWorkingTittle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReleativeTittle().getDataForKey(hash.get("TITLE"))));
            releativeSysId= CommonUtil.convertObjToStr(hash.get("SYSTEM_NO"));
        }
        else if(actionType.length()>0 && actionType.equals("OPERATIVE")){
            System.out.println("@@@@hash"+hash);
            txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            cboSalaryCrBranch.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmSalaryCrBranch().getDataForKey(hash.get("BRANCH_ID"))));
            cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmAccountType().getDataForKey(hash.get("PROD_ID"))));
            
        }
        if(viewType == AUTHORIZE){
            ClientUtil.enableDisable(panCustomer, false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            ClientUtil.enableDisable(this, false);
        }
        HashMap hashMap=(HashMap)obj;
        System.out.println("### fillData Hash : "+hashMap);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(viewType == PROMOTION_ID && panPromotionDetails.isShowing()==true){
                panEditDelete=PROMOTION;
                txtPromotionEmployeeId.setText(CommonUtil.convertObjToStr(hashMap.get("EMPLOYEE_CODE")));
                txtPromotionEmployeeName.setText(CommonUtil.convertObjToStr(hashMap.get("FNAME")));
                txtPromotionLastGrade.setText(CommonUtil.convertObjToStr(hashMap.get("PRESENT_GRADE")));
                txtPromotionLastDesg.setText(CommonUtil.convertObjToStr(hashMap.get("DESIG_ID")));
                txtPromotionBasicPayValue.setText(CommonUtil.convertObjToStr(hashMap.get("BASICS")));
            }
        }
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        lblEmpID.setName("lblEmpID");
        lblFirstName.setName("lblFirstName");
        lblMiddleName.setName("lblMiddleName");
        lblLastName.setName("lblLastName");
        lblGender.setName("lblGender");
        rdoGender_Male.setName("rdoGender_Male");
        rdoGender_Female.setName("rdoGender_Female");
        lblMaritalStatus.setName("lblMaritalStatus");
        rdoMaritalStatus_Single.setName("rdoMaritalStatus_Single");
        rdoMaritalStatus_Married.setName("rdoMaritalStatus_Married");
        rdoFather.setName("rdoFather");
        rdoHusband.setName("rdoHusband");
        lblFatherFirstName.setName("lblFatherFirstName");
        lblFatherMiddleName.setName("lblFatherMiddleName");
        lblFatherLastName.setName("lblFatherLastName");
        lblMotheFirstName.setName("lblMotheFirstNam~e");
        lblMotheMiddleName.setName("lblMotheMiddleName");
        lblMotherLastName.setName("lblMotherLastName");
        lblDateOfBirth.setName("lblDateOfBirth");
        lblPlaceOfBirth.setName("lblPlaceOfBirth");
        lblReligion.setName("lblReligion");
        lblCaste.setName("lblCaste");
        lblHomeTown.setName("lblHomeTown");
        lblUIDNo.setName("lblUIDNo");
        lblPanNumber.setName("lblPanNumber");
        lblPFNumber.setName("lblPFNumber");
        lblPFAcNominee.setName("lblPFAcNominee");
        lblDateOfJoin.setName("lblDateOfJoin");
        lblDateofRetirement.setName("lblDateofRetirement");
        lblAddressType.setName("lblAddressType");
        lblStreet.setName("lblStreet");
        lblArea.setName("lblArea");
        lblCity.setName("lblCity");
        lblState.setName("lblState");
        lblCountry.setName("lblCountry");
        lblPincode.setName("lblPincode");
        lblReleativeStaffId.setName("lblReleativeStaffId");
        //        lblPhoneNo.setName("lblPhoneNo");
        //        lblMobileNo.setName("lblMobileNo");
        btnContactNew.setName("btnContactNew");
        btnContactNew.setName("btnContactNew");
        btnContactDelete.setName("btnContactDelete");
        btnContactAdd.setName("btnContactAdd");
        btnPhotoLoad.setName("btnPhotoLoad");
        btnPhotoRemove.setName("btnPhotoRemove");
        btnSignLoad.setName("btnSignLoad");
        ((javax.swing.border.TitledBorder)panSign.getBorder()).setTitle("panSign");
        ((javax.swing.border.TitledBorder)panPhoto.getBorder()).setTitle("panPhoto");
        lblPassportFirstName.setName("lblPassportFirstName");
        lblPassportMiddleName.setName("PassportMiddleName");
        lblPassportLastName.setName("lblPassportLastName");
        lblPassportIssueAuth.setName("lblPassportIssueAuth");
        lblPassportIssuePlace.setName("lblPassportIssuePlace");
        lblPassportNo.setName("lblPassportNo");
        lblPassportIssueDt.setName("lblPassportIssueDt");
        lblPassportValidUpto.setName("lblPassportValidUpto");
        btnClearPassport.setName("btnClearPassport");
        lblSign.setName("lblSign");
        btnClose.setName("btnClose");
        btnAuthorize.setName("btnAuthorize");
        lblMsg.setName("lblMsg");
        lblSpace4.setName("lblSpace4");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblEmpLevelEducation.setName("lblEmpLevelEducation");
        lblNameOfSchool.setName("lblNameOfSchool");
        lblDateOfPassing.setName("lblDateOfPassing");
        lblSpecilization.setName("lblSpecilization");
        lblUniversity.setName("lblUniversity");
        lblMarksScored.setName("lblMarksScored");
        lblTotMarks.setName("lblTotMarks");
        lblTotMarksPer.setName("lblTotMarksPer");
        lblGrade.setName("lblGrade");
        btnAcademicEducationDetDel.setName("btnAcademicEducationDetDel");
        btnAcademicEducationDetSave.setName("btnAcademicEducationDetSave");
        btnAcademicEducationDetNew.setName("btnAcademicEducationDetNew");
        lblTechnicalQualificationType.setName("lblTechnicalQualificationType");
        lblNameOfTechInst.setName("lblNameOfTechInst");
        lblTechnicalQualificationDateOfPassing.setName("lblTechnicalQualificationDateOfPassing");
        lblTechnicalQualificationMarksScored.setName("lblTechnicalQualificationMarksScored");
        lblTechnicalQualificationGrade.setName("lblTechnicalQualificationGrade");
        lblTechnicalQualificationSpecilization.setName("lblTechnicalQualificationSpecilization");
        lblTechnicalQualificationUniversity.setName("lblTechnicalQualificationUniversity");
        lblTechnicalQualificationTotMarks.setName("lblTechnicalQualificationTotMarks");
        lblTechnicalQualificationTotMarksPer.setName("lblTechnicalQualificationTotMarksPer");
        btnTechnicalQualificationDetNew.setName("btnTechnicalQualificationDetNew");
        btnTechnicalQualificationDetSave.setName("btnTechnicalQualificationDetSave");
        btnTechnicalQualificationDetDel.setName("btnTechnicalQualificationDetDel");
        lblLanguageType.setName("lblLanguageType");
        rdoLanguageRead.setName("rdoLanguageRead");
        rdoLanguageWrite.setName("rdoLanguageWrite");
        rdoLanguageReadeSpeak.setName("rdoLanguageReadeSpeak");
        btnLanguageDetNew.setName("btnLanguageDetNew");
        btnLanguageDetSave.setName("btnLanguageDetSave");
        btnLanguageDetDel.setName("btnLanguageDetDel");
        lblReletaionShip.setName("lblReletaionShip");
        lblReleationFHFirstName.setName("lblReleationFHFirstName");
        lblReleationFHMiddleName.setName("lblReleationFHMiddleName");
        lblReleationFHLastName.setName("lblReleationFHLastName");
        lblRelationShipDateofBirth.setName("lblRelationShipDateofBirth");
        lblFamilyMemEducation.setName("lblFamilyMemEducation");
        lblFamilyMemberProf.setName("lblFamilyMemberProf");
        rdoDependentYes.setName("rdoDependentYes");
        rdoDependentNo.setName("rdoDependentNo");
        btnfamiyDetNew.setName("btnfamiyDetNew");
        btnfamiyDetSave.setName("btnfamiyDetSave");
        btnfamiyDetDel.setName("btnfamiyDetDel");
        btnReject.setName("btnReject");
        btnEdit.setName("btnEdit");
        btnPrint.setName("btnPrint");
        btnException.setName("btnException");
        btnSave.setName("btnSave");
        lblPhoto.setName("lblPhoto");
        btnDelete.setName("btnDelete");
        btnNew.setName("btnNew");
        lblBloodGroup.setName("lblBloodGroup");
        lblMajorHealthProbeem.setName("lblMajorHealthProbeem");
        lblPhysicalHandicap.setName("lblPhysicalHandicap");
        lblDrivingLicenceNo.setName("lblDrivingLicenceNo");
        lblDLRenewalDate.setName("lblDLRenewalDate");
        lblEmailId.setName("lblEmailId");
        lblDomicileState.setName("lblDomicileState");
        lblGradutionIncrementYesNo.setName("lblGradutionIncrementYesNo");
        rdoGradutionIncrementYes.setName("rdoGradutionIncrementYes");
        rdoGradutionIncrementNo.setName("rdoGradutionIncrementNo");
        lblGradutionIncrementReleasedDate.setName("lblGradutionIncrementReleasedDate");
        lblCAIIBPART1IncrementYesNo.setName("lblCAIIBPART1IncrementYesNo");
        rdoCAIIBPART1IncrementYes.setName("rdoCAIIBPART1IncrementYes");
        rdoCAIIBPART1IncrementNo.setName("rdoCAIIBPART1IncrementNo");
        lblCAIIBPART1IncrementReleasedDate.setName("lblCAIIBPART1IncrementReleasedDate");
        lblCAIIBPART2IncrementYesNo.setName("lblCAIIBPART2IncrementYesNo");
        rdoCAIIBPART2Increment_Yes.setName("rdoCAIIBPART2Increment_Yes");
        rdoCAIIBPART2Increment_No.setName("rdoCAIIBPART2Increment_No");
        lblCAIIBPART2IncrementReleasedDate.setName("lblCAIIBPART2IncrementReleasedDate");
        rdoAnyOtherIncrement_Yes.setName("rdoAnyOtherIncrement_Yes");
        lblAnyOtherIncrementYesNo.setName("lblAnyOtherIncrementYesNo");
        rdoAnyOtherIncrement_Yes.setName("rdoAnyOtherIncrement_Yes");
        lblAnyOtherIncrementInstitutionName.setName("lblAnyOtherIncrementInstitutionName");
        lblAnyOtherIncrementReleasedDate.setName("lblAnyOtherIncrementReleasedDate");
        lblPresentBasic.setName("lblPresentBasic");
        lblLastIncrmentDate.setName("lblLastIncrmentDate");
        lblAnyLossOfPay.setName("lblAnyLossOfPay");
        lblLossPay_Months.setName("lblLossPay_Months");
        lblLossOfpay_Days.setName("lblLossOfpay_Days");
        lblNextIncrmentDate.setName("lblNextIncrmentDate");
        lblSigNoYesNo.setName("lblSigNoYesNo");
        rdoSignature_Yes.setName("rdoSignature_Yes");
        rdoSignature_No.setName("rdoSignature_No");
        lblSignatureNo.setName("lblSignatureNo");
        lblPhoneNumber.setName("lblPhoneNumber");
        lblPhoneType.setName("lblPhoneType");
        lblAreaCode.setName("lblAreaCode");
        lblDateOfJoin.setName("lblDateOfJoin");
        lblProbationPeriod.setName("lblProbationPeriod");
        lblConfirmationDate.setName("lblConfirmationDate");
        lblDateofRetirement.setName("lblDateofRetirement");
        lblPFNumber.setName("lblPFNumber");
        lblPFAcNominee.setName("lblPFAcNominee");
        lblPresentBranchId.setName("lblPresentBranchId");
        lblZonalOffice.setName("lblZonalOffice");
        lblWorkingSince.setName("lblWorkingSince");
        lblDesignation.setName("lblDesignation");
        lblPresentGrade.setName("lblPresentGrade");
        lblAccountType.setName("lblAccountType");
        lblAccountNo.setName("lblAccountNo");
        lblSalaryCrBranch.setName("lblSalaryCrBranch");
        lblEmployeedWith.setName("lblEmployeedWith");
        lblLiableForTransfer.setName("lblLiableForTransfer");
        rdoLiableForTransferYes.setName("rdoLiableForTransferYes");
        rdoLiableForTransferNo.setName("rdoLiableForTransferNo");
        lblDepIncomePerannum.setName("lblDepIncomePerannum");
        lblEmployeeLoanType.setName("lblEmployeeLoanType");
        lblLoanAvailedBranch.setName("lblLoanAvailedBranch");
        lblLoanSanctionRefNo.setName("lblLoanSanctionRefNo");
        lblLoanSanctionDate.setName("lblLoanSanctionDate");
        lblLoanNo.setName("lblLoanNo");
        lblLoanAmount.setName("lblLoanAmount");
        lblLoanRateofInterest.setName("lblLoanRateofInterest");
        lblLoanNoOfInstallments.setName("lblLoanNoOfInstallments");
        lblLoanInstallmentAmount.setName("lblLoanInstallmentAmount");
        lblLoanRepaymentStartDate.setName("lblLoanRepaymentStartDate");
        lblLoanRepaymentEndDate.setName("lblLoanRepaymentEndDate");
        lblLoanPreCloserYesNo.setName("lblLoanPreCloserYesNo");
        rdoLoanPreCloserYes.setName("rdoLoanPreCloserYes");
        rdoLoanPreCloserNo.setName("rdoLoanPreCloserNo");
        lblLoanCloserDate.setName("lblLoanCloserDate");
        lblLoanRemarks.setName("lblLoanRemarks");
        tabIndCust.setName("tabIndCust");
        txtMiddleName.setName("txtMiddleName");
        txtLastName.setName("txtLastName");
        txtFatherFirstName.setName("txtFatherFirstName");
        cboBloodGroup.setName("cboBloodGroup");
        txtMajorHealthProbeem.setName("txtMajorHealthProbeem");
        txtPhysicalyHandicap.setName("txtPhysicalyHandicap");
        cboReligion.setName("cboReligion");
        cboCaste.setName("cboCaste");
        txtHomeTown.setName("txtHomeTown");
        cboDomicileState.setName("cboDomicileState");
        txtIDCardNoNo.setName("txtIDCardNoNo");
        lblIDCardNoNo.setName("lblIDCardNoNo");
        txtUIDNo.setName("txtUIDNo");
        txtPanNumber.setName("txtPanNumber");
        txtDrivingLicenceNo.setName("txtDrivingLicenceNo");
        txtEmailId.setName("txtEmailId");
        txtPassportFirstName.setName("txtPassportFirstName");
        txtPassportMiddleName.setName("txtPassportMiddleName");
        txtPassportLastName.setName("txtPassportLastName");
        txtPassportNo.setName("txtPassportNo");
        txtPassportIssueAuth.setName("txtPassportIssueAuth");
        tdtPassportIssueDt.setName("tdtPassportIssueDt");
        tdtPassportValidUpto.setName("tdtPassportValidUpto");
        cboPassportIssuePlace.setName("cboPassportIssuePlace");
        tdtDateOfBirth.setName("tdtDateOfBirth");
        cboTechnicalQualificationType.setName("cboTechnicalQualificationType");
        cboTitle.setName("cboTitle");
        txtEmpID.setName("txtEmpID");
        tdtDateOfJoin.setName("tdtDateOfJoin");
        cboReletaionShip.setName("cboReletaionShip");
        txtReleationFHFirstName.setName("txtReleationFHFirstName");
        txtReleationFHMiddleName.setName("txtReleationFHMiddleName");
        txtReleationFHLastName.setName("txtReleationFHLastName");
        tdtRelationShipDateofBirth.setName("tdtRelationShipDateofBirth");
        cboFamilyMemEducation.setName("cboFamilyMemEducation");
        cboFamilyMemberProf.setName("cboFamilyMemberProf");
        txtEmpWith.setName("txtEmpWith");
        txtDepIncomePerannum.setName("txtDepIncomePerannum");
        txtReleativeStaffId.setName("txtReleativeStaffId");
        txtRelativesWorkingFirstName.setName("txtRelativesWorkingFirstName");
        lblRelativesWorkingFirstName.setName("lblRelativesWorkingFirstName");
        txtRelativesWorkingMiddleName.setName("txtRelativesWorkingMiddleName");
        lblRelativesWorkingMiddleName.setName("lblRelativesWorkingMiddleName");
        txtRelativesWorkingLastName.setName("txtRelativesWorkingLastName");
        lblRelativesWorkingLastName.setName("lblRelativesWorkingLastName");
        cboWorkingDesignation.setName("cboWorkingDesignation");
        lblWorkingDesignation.setName("lblWorkingDesignation");
        cboRelativesWorkingBranch.setName("cboRelativesWorkingBranch");
        lblRelativesWorkingBranch.setName("lblRelativesWorkingBranch");
        cboRelativesWorkingReletionShip.setName("cboRelativesWorkingReletionShip");
        lblRelativesWorkingReletionShip.setName("lblRelativesWorkingReletionShip");
        txtDirectorFirstName.setName("txtDirectorFirstName");
        lblDirectorFirstName.setName("lblDirectorFirstName");
        txtDirectorMiddleName.setName("txtDirectorMiddleName");
        lblDirectorMiddleName.setName("lblDirectorMiddleName");
        txtDirectorLastName.setName("txtDirectorLastName");
        lblDirectorLastName.setName("lblDirectorLastName");
        cboDirectorReleationShip.setName("cboDirectorReleationShip");
        lblDirectorReleationShip.setName("lblDirectorReleationShip");
        cboEmpLevelEducation.setName("cboEmpLevelEducation");
        txtNameOfSchool.setName("txtNameOfSchool");
        tdtDateOfPassing.setName("tdtDateOfPassing");
        cboSpecilization.setName("cboSpecilization");
        txtUniversity.setName("txtUniversity");
        txtMarksScored.setName("txtMarksScored");
        txtTotMarks.setName("txtTotMarks");
        txtTotMarksPer.setName("txtTotMarksPer");
        cboGrade.setName("cboGrade");
        cboTechnicalQualificationType.setName("cboTechnicalQualificationType");
        txtNameOfTechInst.setName("txtNameOfTechInst");
        tdtTechnicalQualificationDateOfPassing.setName("tdtTechnicalQualificationDateOfPassing");
        txtTechnicalQualificationUniversity.setName("txtTechnicalQualificationUniversity");
        cboTechnicalQualificationGrade.setName("cboTechnicalQualificationGrade");
        cboTechnicalQualificationSpecilization.setName("cboTechnicalQualificationSpecilization");
        //educational details
        btnAcademicEducationDetDel.setName("btnAcademicEducationDetDel");
        btnAcademicEducationDetNew.setName("btnAcademicEducationDetNew");
        btnAcademicEducationDetSave.setName("btnAcademicEducationDetSave");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDeletedDetails.setName("btnDeletedDetails");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnLanguageDetDel.setName("btnLanguageDetDel");
        btnLanguageDetNew.setName("btnLanguageDetNew");
        btnLanguageDetSave.setName("btnLanguageDetSave");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnTechnicalQualificationDetDel.setName("btnTechnicalQualificationDetDel");
        btnTechnicalQualificationDetNew.setName("btnTechnicalQualificationDetNew");
        btnTechnicalQualificationDetSave.setName("btnTechnicalQualificationDetSave");
        btnView.setName("btnView");
        cboEmpLevelEducation.setName("cboEmpLevelEducation");
        cboGrade.setName("cboGrade");
        cboLanguageType.setName("cboLanguageType");
        cboSpecilization.setName("cboSpecilization");
        cboTechnicalQualificationGrade.setName("cboTechnicalQualificationGrade");
        cboTechnicalQualificationSpecilization.setName("cboTechnicalQualificationSpecilization");
        cboTechnicalQualificationType.setName("cboTechnicalQualificationType");
        lblDateOfPassing.setName("lblDateOfPassing");
        lblEmpLevelEducation.setName("lblEmpLevelEducation");
        lblGrade.setName("lblGrade");
        lblLanguageType.setName("lblLanguageType");
        lblMarksScored.setName("lblMarksScored");
        lblMsg.setName("lblMsg");
        lblNameOfSchool.setName("lblNameOfSchool");
        lblNameOfTechInst.setName("lblNameOfTechInst");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpecilization.setName("lblSpecilization");
        lblStatus.setName("lblStatus");
        lblTechnicalQualificationDateOfPassing.setName("lblTechnicalQualificationDateOfPassing");
        lblTechnicalQualificationGrade.setName("lblTechnicalQualificationGrade");
        lblTechnicalQualificationMarksScored.setName("lblTechnicalQualificationMarksScored");
        lblTechnicalQualificationSpecilization.setName("lblTechnicalQualificationSpecilization");
        lblTechnicalQualificationTotMarks.setName("lblTechnicalQualificationTotMarks");
        lblTechnicalQualificationTotMarksPer.setName("lblTechnicalQualificationTotMarksPer");
        lblTechnicalQualificationType.setName("lblTechnicalQualificationType");
        lblTechnicalQualificationUniversity.setName("lblTechnicalQualificationUniversity");
        lblTotMarks.setName("lblTotMarks");
        lblTotMarksPer.setName("lblTotMarksPer");
        lblUniversity.setName("lblUniversity");
        mbrCustomer.setName("mbrCustomer");
        panAcademicEducation.setName("panAcademicEducation");
        panAcademicEducation2.setName("panAcademicEducation2");
        panAcademicEducationDateEntery.setName("panAcademicEducationDateEntery");
        panAcademicEducationTable.setName("panAcademicEducationTable");
        panBtnAcademicEducation.setName("panBtnAcademicEducation");
        panBtnLangues1.setName("panBtnLangues1");
        panBtnTechnicalQualification2.setName("panBtnTechnicalQualification2");
        panCustomer.setName("panCustomer");
        panLanguageDate.setName("panLanguageDate");
        panLanguageStatus.setName("panLanguageStatus");
        panLanguageTable.setName("panLanguageTable");
        panLangues.setName("panLangues");
        panMISKYC.setName("panMISKYC");
        panStatus.setName("panStatus");
        panTechnicalQualificationDateEntery.setName("panTechnicalQualificationDateEntery");
        panTechnicalQualificationTable.setName("panTechnicalQualificationTable");
        rdoLanguageRead.setName("rdoLanguageRead");
        rdoLanguageReadeSpeak.setName("rdoLanguageReadeSpeak");
        rdoLanguageWrite.setName("rdoLanguageWrite");
        srpAcademicEducation.setName("srpAcademicEducation");
        srpLanguage.setName("srpLanguage");
        srpTechnicalQualification.setName("srpTechnicalQualification");
        tabIndCust.setName("tabIndCust");
        tblAcademicEducation.setName("tblAcademicEducation");
        tblLanguage.setName("tblLanguage");
        tblTechnicalEducation.setName("tblTechnicalEducation");
        tdtDateOfPassing.setName("tdtDateOfPassing");
        tdtTechnicalQualificationDateOfPassing.setName("tdtTechnicalQualificationDateOfPassing");
        txtMarksScored.setName("txtMarksScored");
        txtNameOfSchool.setName("txtNameOfSchool");
        txtNameOfTechInst.setName("txtNameOfTechInst");
        txtTechnicalQualificationMarksScored.setName("txtTechnicalQualificationMarksScored");
        txtTechnicalQualificationTotMarks.setName("txtTechnicalQualificationTotMarks");
        txtTechnicalQualificationTotMarksPer.setName("txtTechnicalQualificationTotMarksPer");
        txtTechnicalQualificationUniversity.setName("txtTechnicalQualificationUniversity");
        txtTotMarks.setName("txtTotMarks");
        txtTotMarksPer.setName("txtTotMarksPer");
        txtUniversity.setName("txtUniversity");
        
        
        //loan details
        btnAccountNo.setName("btnAccountNo");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDeletedDetails.setName("btnDeletedDetails");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnOprativeDetailsDetDel.setName("btnOprativeDetailsDetDel");
        btnOprativeDetailsDetNew.setName("btnOprativeDetailsDetNew");
        btnOprativeDetailsDetSave.setName("btnOprativeDetailsDetSave");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnTermLoansDetDel.setName("btnTermLoansDetDel");
        btnTermLoansDetNew.setName("btnTermLoansDetNew");
        btnTermLoansDetSave.setName("btnTermLoansDetSave");
        btnView.setName("btnView");
        cboAccountType.setName("cboAccountType");
        cboEmployeeLoanType.setName("cboEmployeeLoanType");
        cboLoanAvailedBranch.setName("cboLoanAvailedBranch");
        cboSalaryCrBranch.setName("cboSalaryCrBranch");
        lblAccountNo.setName("lblAccountNo");
        lblAccountType.setName("lblAccountType");
        lblEmployeeLoanType.setName("lblEmployeeLoanType");
        lblLoanAmount.setName("lblLoanAmount");
        lblLoanAvailedBranch.setName("lblLoanAvailedBranch");
        lblLoanCloserDate.setName("lblLoanCloserDate");
        lblLoanInstallmentAmount.setName("lblLoanInstallmentAmount");
        lblLoanNo.setName("lblLoanNo");
        lblLoanNoOfInstallments.setName("lblLoanNoOfInstallments");
        lblLoanPreCloserYesNo.setName("lblLoanPreCloserYesNo");
        lblLoanRateofInterest.setName("lblLoanRateofInterest");
        lblLoanRemarks.setName("lblLoanRemarks");
        lblLoanRepaymentEndDate.setName("lblLoanRepaymentEndDate");
        lblLoanRepaymentStartDate.setName("lblLoanRepaymentStartDate");
        lblLoanSanctionDate.setName("lblLoanSanctionDate");
        lblLoanSanctionRefNo.setName("lblLoanSanctionRefNo");
        lblMsg.setName("lblMsg");
        lblSalaryCrBranch.setName("lblSalaryCrBranch");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrCustomer.setName("mbrCustomer");
        panBtnOprativeDetails.setName("panBtnOprativeDetails");
        panBtnTermLoans.setName("panBtnTermLoans");
        panCustId1.setName("panCustId1");
        panCustomer.setName("panCustomer");
        panEmployeeDateEntery.setName("panEmployeeDateEntery");
        panEmployeeLoan.setName("panEmployeeLoan");
        panEmployeeLoanTable.setName("panEmployeeLoanTable");
        panLandDetails.setName("panLandDetails");
        panLanguageStatus1.setName("panLanguageStatus1");
        panLanguageTable1.setName("panLanguageTable1");
        panLoanPreCloserYesNo.setName("panLoanPreCloserYesNo");
        panOprativeDetails.setName("panOprativeDetails");
        panStatus.setName("panStatus");
        rdoLoanPreCloserNo.setName("rdoLoanPreCloserNo");
        rdoLoanPreCloserYes.setName("rdoLoanPreCloserYes");
        srpEmployeeLoan.setName("srpEmployeeLoan");
        srpOprative.setName("srpOprative");
        tabIndCust.setName("tabIndCust");
        tblEmployeeLoan.setName("tblEmployeeLoan");
        tblOprative.setName("tblOprative");
        tdtLoanCloserDate.setName("tdtLoanCloserDate");
        tdtLoanRepaymentEndDate.setName("tdtLoanRepaymentEndDate");
        tdtLoanRepaymentStartDate.setName("tdtLoanRepaymentStartDate");
        tdtLoanSanctionDate.setName("tdtLoanSanctionDate");
        txtAccountNo.setName("txtAccountNo");
        txtLoanAmount.setName("txtLoanAmount");
        txtLoanInstallmentAmount.setName("txtLoanInstallmentAmount");
        txtLoanNo.setName("txtLoanNo");
        txtLoanNoOfInstallments.setName("txtLoanNoOfInstallments");
        txtLoanRateofInterest.setName("txtLoanRateofInterest");
        txtLoanRemarks.setName("txtLoanRemarks");
        txtLoanSanctionRefNo.setName("txtLoanSanctionRefNo");
        
        //PERSONAL DETAILS
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDeletedDetails.setName("btnDeletedDetails");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        cboDesignation.setName("cboDesignation");
        cboPromotedDesignation.setName("cboPromotedDesignation");
        cboPFAcNominee.setName("cboPFAcNominee");
        cboPresentBranchId.setName("cboPresentBranchId");
        cboPresentGrade.setName("cboPresentGrade");
        cboProbationPeriod.setName("cboProbationPeriod");
        cboReginoalOffice.setName("cboReginoalOffice");
        cboSocietyMember.setName("cboSocietyMember");
        cboUnionMember.setName("cboUnionMember");
        cboZonalOffice.setName("cboZonalOffice");
        lbReginoalOffice.setName("lbReginoalOffice");
        lblAnyLossOfPay.setName("lblAnyLossOfPay");
        lblAnyOtherIncrementInstitutionName.setName("lblAnyOtherIncrementInstitutionName");
        lblAnyOtherIncrementReleasedDate.setName("lblAnyOtherIncrementReleasedDate");
        lblAnyOtherIncrementYesNo.setName("lblAnyOtherIncrementYesNo");
        lblCAIIBPART1IncrementReleasedDate.setName("lblCAIIBPART1IncrementReleasedDate");
        lblCAIIBPART1IncrementYesNo.setName("lblCAIIBPART1IncrementYesNo");
        lblCAIIBPART2IncrementReleasedDate.setName("lblCAIIBPART2IncrementReleasedDate");
        lblCAIIBPART2IncrementYesNo.setName("lblCAIIBPART2IncrementYesNo");
        lblClubMembership.setName("lblClubMembership");
        lblClubName.setName("lblClubName");
        lblConfirmationDate.setName("lblConfirmationDate");
        lblDateOfJoin.setName("lblDateOfJoin");
        lblDateofRetirement.setName("lblDateofRetirement");
        lblDesignation.setName("lblDesignation");
        lblGradutionIncrementReleasedDate.setName("lblGradutionIncrementReleasedDate");
        lblGradutionIncrementYesNo.setName("lblGradutionIncrementYesNo");
        lblLastIncrmentDate.setName("lblLastIncrmentDate");
        lblLossOfpay_Days.setName("lblLossOfpay_Days");
        lblLossPay_Months.setName("lblLossPay_Months");
        lblMsg.setName("lblMsg");
        lblNextIncrmentDate.setName("lblNextIncrmentDate");
        lblPFAcNominee.setName("lblPFAcNominee");
        lblPFNumber.setName("lblPFNumber");
        lblPresentBasic.setName("lblPresentBasic");
        lblPresentBranchId.setName("lblPresentBranchId");
        lblPresentGrade.setName("lblPresentGrade");
        lblProbationPeriod.setName("lblProbationPeriod");
        lblSigNoYesNo.setName("lblSigNoYesNo");
        lblSignatureNo.setName("lblSignatureNo");
        lblSocietyMember.setName("lblSocietyMember");
        lblSocietyMemberNo.setName("lblSocietyMemberNo");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblUnionMember.setName("lblUnionMember");
        lblWorkingSince.setName("lblWorkingSince");
        lblZonalOffice.setName("lblZonalOffice");
        mbrCustomer.setName("mbrCustomer");
        panAnyOtherIncrement.setName("panAnyOtherIncrement");
        panCAIIBPART.setName("panCAIIBPART");
        panCAIIBPART2.setName("panCAIIBPART2");
        panClubDet.setName("panClubDet");
        panCustomer.setName("panCustomer");
        panCustomerInfo1.setName("panCustomerInfo1");
        panDOB2.setName("panDOB2");
        panGradutionIncrement.setName("panGradutionIncrement");
        panIncomeParticulars.setName("panIncomeParticulars");
        panJoiningDetails.setName("panJoiningDetails");
        panLossOfpay.setName("panLossOfpay");
        panMinDepositPeriod1.setName("panMinDepositPeriod1");
        panOfficeDetails.setName("panOfficeDetails");
        panPresent.setName("panPresent");
        panPresentDetails.setName("panPresentDetails");
        panPresentWorkingDetails.setName("panPresentWorkingDetails");
        panSignatureNo1.setName("panSignatureNo1");
        panStatus.setName("panStatus");
        panTrainingDateEntery1.setName("panTrainingDateEntery1");
        rdoAnyOtherIncrement_No.setName("rdoAnyOtherIncrement_No");
        rdoAnyOtherIncrement_Yes.setName("rdoAnyOtherIncrement_Yes");
        rdoCAIIBPART1IncrementNo.setName("rdoCAIIBPART1IncrementNo");
        rdoCAIIBPART1IncrementYes.setName("rdoCAIIBPART1IncrementYes");
        rdoCAIIBPART2Increment_No.setName("rdoCAIIBPART2Increment_No");
        rdoCAIIBPART2Increment_Yes.setName("rdoCAIIBPART2Increment_Yes");
        rdoClubMembership_No.setName("rdoClubMembership_No");
        rdoClubMembership_YES.setName("rdoClubMembership_YES");
        rdoGradutionIncrementNo.setName("rdoGradutionIncrementNo");
        rdoGradutionIncrementYes.setName("rdoGradutionIncrementYes");
        rdoSignature_No.setName("rdoSignature_No");
        rdoSignature_Yes.setName("rdoSignature_Yes");
        tabIndCust.setName("tabIndCust");
        tdtAnyOtherIncrementReleasedDate.setName("tdtAnyOtherIncrementReleasedDate");
        tdtCAIIBPART1IncrementReleasedDate.setName("tdtCAIIBPART1IncrementReleasedDate");
        tdtCAIIBPART2IncrementReleasedDate.setName("tdtCAIIBPART2IncrementReleasedDate");
        tdtConfirmationDate.setName("tdtConfirmationDate");
        tdtDateOfJoin.setName("tdtDateOfJoin");
        tdtDateofRetirement.setName("tdtDateofRetirement");
        tdtGradutionIncrementReleasedDate.setName("tdtGradutionIncrementReleasedDate");
        tdtLastIncrmentDate.setName("tdtLastIncrmentDate");
        tdtNextIncrmentDate.setName("tdtNextIncrmentDate");
        tdtWorkingSince.setName("tdtWorkingSince");
        txtAnyOtherIncrementInstitutionName.setName("txtAnyOtherIncrementInstitutionName");
        txtClubName.setName("txtClubName");
        txtLossOfpay_Days.setName("txtLossOfpay_Days");
        txtLossPay_Months.setName("txtLossPay_Months");
        txtPFNumber.setName("txtPFNumber");
        txtPresentBasic.setName("txtPresentBasic");
        txtProbationPeriod.setName("txtProbationPeriod");
        txtSignatureNo.setName("txtSignatureNo");
        txtSocietyMemberNo.setName("txtSocietyMemberNo");
        
        
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblEmpID.setText(resourceBundle.getString("lblEmpID"));
        lblFirstName.setText(resourceBundle.getString("lblFirstName"));
        lblMiddleName.setText(resourceBundle.getString("lblMiddleName"));
        lblLastName.setText(resourceBundle.getString("lblLastName"));
        lblGender.setText(resourceBundle.getString("lblGender"));
        rdoGender_Male.setText(resourceBundle.getString("rdoGender_Male"));
        rdoGender_Female.setText(resourceBundle.getString("rdoGender_Female"));
        lblMaritalStatus.setText(resourceBundle.getString("lblMaritalStatus"));
        rdoMaritalStatus_Single.setText(resourceBundle.getString("rdoMaritalStatus_Single"));
        rdoMaritalStatus_Married.setText(resourceBundle.getString("rdoMaritalStatus_Married"));
        rdoFather.setText(resourceBundle.getString("rdoFather"));
        rdoHusband.setText(resourceBundle.getString("rdoHusband"));
        lblFatherFirstName.setText(resourceBundle.getString("lblFatherFirstName"));
        lblFatherMiddleName.setText(resourceBundle.getString("lblFatherMiddleName"));
        lblFatherLastName.setText(resourceBundle.getString("lblFatherLastName"));
        lblMotheFirstName.setText(resourceBundle.getString("lblMotheFirstName"));
        lblMotheMiddleName.setText(resourceBundle.getString("lblMotheMiddleName"));
        lblMotherLastName.setText(resourceBundle.getString("lblMotherLastName"));
        lblDateOfBirth.setText(resourceBundle.getString("lblDateOfBirth"));
        //        lblAge.setText(resourceBundle.getString("lblAge"));
        lblPlaceOfBirth.setText(resourceBundle.getString("lblPlaceOfBirth"));
        lblReligion.setText(resourceBundle.getString("lblReligion"));
        lblCaste.setText(resourceBundle.getString("lblCaste"));
        lblHomeTown.setText(resourceBundle.getString("lblHomeTown"));
        lblIDCardNoNo.setText(resourceBundle.getString("lblIDCardNoNo"));
        lblUIDNo.setText(resourceBundle.getString("lblUIDNo"));
        lblPanNumber.setText(resourceBundle.getString("lblPanNumber"));
        lblPFNumber.setText(resourceBundle.getString("lblPFNumber"));
        lblPFAcNominee.setText(resourceBundle.getString("lblPFAcNominee"));
        lblDateOfJoin.setText(resourceBundle.getString("lblDateOfJoin"));
        lblDateofRetirement.setText(resourceBundle.getString("lblDateofRetirement"));
        lblAddressType.setText(resourceBundle.getString("lblAddressType"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        lblPincode.setText(resourceBundle.getString("lblPincode"));
        btnContactNew.setText(resourceBundle.getString("btnContactNew"));
        btnContactAdd.setText(resourceBundle.getString("btnContactAdd"));
        btnContactDelete.setText(resourceBundle.getString("btnContactDelete"));
        btnPhotoLoad.setText(resourceBundle.getString("btnPhotoLoad"));
        btnPhotoRemove.setText(resourceBundle.getString("btnPhotoRemove"));
        btnSignLoad.setText(resourceBundle.getString("btnSignLoad"));
        ((javax.swing.border.TitledBorder)panSign.getBorder()).setTitle(resourceBundle.getString("panSign"));
        ((javax.swing.border.TitledBorder)panPhoto.getBorder()).setTitle(resourceBundle.getString("panPhoto"));
        lblPassportFirstName.setText(resourceBundle.getString("lblPassportFirstName"));
        lblPassportMiddleName.setText(resourceBundle.getString("lblPassportMiddleName"));
        lblPassportLastName.setText(resourceBundle.getString("lblPassportLastName"));
        lblPassportIssueAuth.setText(resourceBundle.getString("lblPassportIssueAuth"));
        lblPassportIssuePlace.setText(resourceBundle.getString("lblPassportIssuePlace"));
        lblPassportNo.setText(resourceBundle.getString("lblPassportNo"));
        lblPassportIssueDt.setText(resourceBundle.getString("lblPassportIssueDt"));
        btnClearPassport.setText(resourceBundle.getString("btnClearPassport"));
        lblSign.setText(resourceBundle.getString("lblSign"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblEmpLevelEducation.setText(resourceBundle.getString("lblEmpLevelEducation"));
        lblNameOfSchool.setText(resourceBundle.getString("lblNameOfSchool"));
        lblDateOfPassing.setText(resourceBundle.getString("lblDateOfPassing"));
        lblSpecilization.setText(resourceBundle.getString("lblSpecilization"));
        lblUniversity.setText(resourceBundle.getString("lblUniversity"));
        lblMarksScored.setText(resourceBundle.getString("lblMarksScored"));
        lblTotMarks.setText(resourceBundle.getString("lblTotMarks"));
        lblTotMarksPer.setText(resourceBundle.getString("lblTotMarksPer"));
        lblGrade.setText(resourceBundle.getString("lblGrade"));
        btnAcademicEducationDetDel.setText(resourceBundle.getString("btnAcademicEducationDetDel"));
        btnAcademicEducationDetSave.setText(resourceBundle.getString("btnAcademicEducationDetSave"));
        btnAcademicEducationDetNew.setText(resourceBundle.getString("btnAcademicEducationDetNew"));
        lblTechnicalQualificationType.setText(resourceBundle.getString("lblTechnicalQualificationType"));
        lblNameOfTechInst.setText(resourceBundle.getString("lblNameOfTechInst"));
        lblTechnicalQualificationDateOfPassing.setText(resourceBundle.getString("lblTechnicalQualificationDateOfPassing"));
        lblTechnicalQualificationMarksScored.setText(resourceBundle.getString("lblTechnicalQualificationMarksScored"));
        lblTechnicalQualificationGrade.setText(resourceBundle.getString("lblTechnicalQualificationGrade"));
        lblTechnicalQualificationSpecilization.setText(resourceBundle.getString("lblTechnicalQualificationSpecilization"));
        lblTechnicalQualificationUniversity.setText(resourceBundle.getString("lblTechnicalQualificationUniversity"));
        lblTechnicalQualificationTotMarks.setText(resourceBundle.getString("lblTechnicalQualificationTotMarks"));
        lblTechnicalQualificationTotMarksPer.setText(resourceBundle.getString("lblTechnicalQualificationTotMarksPer"));
        btnTechnicalQualificationDetNew.setText(resourceBundle.getString("btnTechnicalQualificationDetNew"));
        btnTechnicalQualificationDetSave.setText(resourceBundle.getString("btnTechnicalQualificationDetSave"));
        btnTechnicalQualificationDetDel.setText(resourceBundle.getString("btnTechnicalQualificationDetDel"));
        lblLanguageType.setText(resourceBundle.getString("lblLanguageType"));
        rdoLanguageRead.setText(resourceBundle.getString("rdoLanguageRead"));
        rdoLanguageWrite.setText(resourceBundle.getString("rdoLanguageWrite"));
        rdoLanguageReadeSpeak.setText(resourceBundle.getString("rdoLanguageReadeSpeak"));
        btnLanguageDetNew.setText(resourceBundle.getString("btnLanguageDetNew"));
        btnLanguageDetSave.setText(resourceBundle.getString("btnLanguageDetSave"));
        btnLanguageDetDel.setText(resourceBundle.getString("btnLanguageDetDel"));
        lblReletaionShip.setText(resourceBundle.getString("lblReletaionShip"));
        lblReleationFHFirstName.setText(resourceBundle.getString("lblReleationFHFirstName"));
        lblReleationFHMiddleName.setText(resourceBundle.getString("lblReleationFHMiddleName"));
        lblReleationFHLastName.setText(resourceBundle.getString("lblReleationFHLastName"));
        lblRelationShipDateofBirth.setText(resourceBundle.getString("lblRelationShipDateofBirth"));
        lblFamilyMemEducation.setText(resourceBundle.getString("lblFamilyMemEducation"));
        lblFamilyMemberProf.setText(resourceBundle.getString("lblFamilyMemberProf"));
        rdoDependentYes.setText(resourceBundle.getString("rdoDependentYes"));
        rdoDependentNo.setText(resourceBundle.getString("rdoDependentNo"));
        btnfamiyDetNew.setText(resourceBundle.getString("btnfamiyDetNew"));
        btnfamiyDetSave.setText(resourceBundle.getString("btnfamiyDetSave"));
        btnfamiyDetDel.setText(resourceBundle.getString("btnfamiyDetDel"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblPhoto.setText(resourceBundle.getString("lblPhoto"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        //------
        lblBloodGroup.setText(resourceBundle.getString("lblBloodGroup"));
        lblMajorHealthProbeem.setText(resourceBundle.getString("lblMajorHealthProbeem"));
        lblPhysicalHandicap.setText(resourceBundle.getString("lblPhysicalHandicap"));
        lblDrivingLicenceNo.setText(resourceBundle.getString("lblDrivingLicenceNo"));
        lblDLRenewalDate.setText(resourceBundle.getString("lblDLRenewalDate"));
        lblEmailId.setText(resourceBundle.getString("lblEmailId"));
        lblDomicileState.setText(resourceBundle.getString("lblDomicileState"));
        lblGradutionIncrementYesNo.setText(resourceBundle.getString("lblGradutionIncrementYesNo"));
        rdoGradutionIncrementYes.setText(resourceBundle.getString("rdoGradutionIncrementYes"));
        rdoGradutionIncrementNo.setText(resourceBundle.getString("rdoGradutionIncrementNo"));
        lblGradutionIncrementReleasedDate.setText(resourceBundle.getString("lblGradutionIncrementReleasedDate"));
        lblCAIIBPART1IncrementYesNo.setText(resourceBundle.getString("lblCAIIBPART1IncrementYesNo"));
        rdoCAIIBPART1IncrementYes.setText(resourceBundle.getString("rdoCAIIBPART1IncrementYes"));
        rdoCAIIBPART1IncrementNo.setText(resourceBundle.getString("rdoCAIIBPART1IncrementNo"));
        lblCAIIBPART1IncrementReleasedDate.setText(resourceBundle.getString("lblCAIIBPART1IncrementReleasedDate"));
        lblCAIIBPART2IncrementYesNo.setText(resourceBundle.getString("lblCAIIBPART2IncrementYesNo"));
        rdoCAIIBPART2Increment_Yes.setText(resourceBundle.getString("rdoCAIIBPARTIncrementYes"));
        rdoCAIIBPART2Increment_No.setText(resourceBundle.getString("rdoCAIIBPART2Increment_No"));
        lblCAIIBPART2IncrementReleasedDate.setText(resourceBundle.getString("lblCAIIBPART2IncrementReleasedDate"));
        rdoAnyOtherIncrement_Yes.setText(resourceBundle.getString("rdoAnyOtherIncrement_Yes"));
        lblAnyOtherIncrementYesNo.setText(resourceBundle.getString("lblAnyOtherIncrementYesNo"));
        rdoAnyOtherIncrement_Yes.setText(resourceBundle.getString("rdoAnyOtherIncrement_Yes"));
        lblAnyOtherIncrementInstitutionName.setText(resourceBundle.getString("lblAnyOtherIncrementInstitutionName"));
        lblAnyOtherIncrementReleasedDate.setText(resourceBundle.getString("lblAnyOtherIncrementReleasedDate"));
        lblPresentBasic.setText(resourceBundle.getString("lblPresentBasic"));
        lblLastIncrmentDate.setText(resourceBundle.getString("lblLastIncrmentDate"));
        lblAnyLossOfPay.setText(resourceBundle.getString("lblAnyLossOfPay"));
        lblLossPay_Months.setText(resourceBundle.getString("lblLossPay_Months"));
        lblLossOfpay_Days.setText(resourceBundle.getString("lblLossOfpay_Days"));
        lblNextIncrmentDate.setText(resourceBundle.getString("lblNextIncrmentDate"));
        lblSigNoYesNo.setText(resourceBundle.getString("lblSigNoYesNo"));
        rdoSignature_Yes.setText(resourceBundle.getString("rdoSignature_Yes"));
        rdoSignature_No.setText(resourceBundle.getString("rdoSignature_No"));
        lblSignatureNo.setText(resourceBundle.getString("lblSignatureNo"));
        lblPhoneNumber.setText(resourceBundle.getString("lblPhoneNumber"));
        lblPhoneType.setText(resourceBundle.getString("lblPhoneType"));
        lblAreaCode.setText(resourceBundle.getString("lblAreaCode"));
        lblDateOfJoin.setText(resourceBundle.getString("lblDateOfJoin"));
        lblProbationPeriod.setText(resourceBundle.getString("lblProbationPeriod"));
        lblConfirmationDate.setText(resourceBundle.getString("lblConfirmationDate"));
        lblDateofRetirement.setText(resourceBundle.getString("lblDateofRetirement"));
        lblPFNumber.setText(resourceBundle.getString("lblPFNumber"));
        lblPFAcNominee.setText(resourceBundle.getString("lblPFAcNominee"));
        lblPresentBranchId.setText(resourceBundle.getString("lblPresentBranchId"));
        lbReginoalOffice.setText(resourceBundle.getString("lbReginoalOffice"));
        lblZonalOffice.setText(resourceBundle.getString("lblZonalOffice"));
        lblWorkingSince.setText(resourceBundle.getString("lblWorkingSince"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        lblPresentGrade.setText(resourceBundle.getString("lblPresentGrade"));
        lblAccountType.setText(resourceBundle.getString("lblAccountType"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        lblSalaryCrBranch.setText(resourceBundle.getString("lblSalaryCrBranch"));
        lblEmployeedWith.setText(resourceBundle.getString("lblEmployeedWith"));
        lblLiableForTransfer.setText(resourceBundle.getString("lblLiableForTransfer"));
        rdoLiableForTransferYes.setText(resourceBundle.getString("rdoLiableForTransferYes"));
        rdoLiableForTransferNo.setText(resourceBundle.getString("rdoLiableForTransferNo"));
        lblDepIncomePerannum.setText(resourceBundle.getString("lblDepIncomePerannum"));
        lblEmployeeLoanType.setText(resourceBundle.getString("lblEmployeeLoanType"));
        lblLoanAvailedBranch.setText(resourceBundle.getString("lblLoanAvailedBranch"));
        lblLoanSanctionRefNo.setText(resourceBundle.getString("lblLoanSanctionRefNo"));
        lblLoanSanctionDate.setText(resourceBundle.getString("lblLoanSanctionDate"));
        lblLoanNo.setText(resourceBundle.getString("lblLoanNo"));
        lblLoanAmount.setText(resourceBundle.getString("lblLoanAmount"));
        lblLoanRateofInterest.setText(resourceBundle.getString("lblLoanRateofInterest"));
        lblLoanNoOfInstallments.setText(resourceBundle.getString("lblLoanNoOfInstallments"));
        lblLoanInstallmentAmount.setText(resourceBundle.getString("lblLoanInstallmentAmount"));
        lblLoanRepaymentStartDate.setText(resourceBundle.getString("lblLoanRepaymentStartDate"));
        lblLoanRepaymentEndDate.setText(resourceBundle.getString("lblLoanRepaymentEndDate"));
        lblLoanPreCloserYesNo.setText(resourceBundle.getString("lblLoanPreCloserYesNo"));
        rdoLoanPreCloserYes.setText(resourceBundle.getString("rdoLoanPreCloserYes"));
        rdoLoanPreCloserNo.setText(resourceBundle.getString("rdoLoanPreCloserNo"));
        lblLoanCloserDate.setText(resourceBundle.getString("lblLoanCloserDate"));
        lblLoanRemarks.setText(resourceBundle.getString("lblLoanRemarks"));
        lblReleativeStaffId.setText(resourceBundle.getString("lblReleativeStaffId"));
        lblRelativesWorkingFirstName.setText(resourceBundle.getString("lblRelativesWorkingFirstName"));
        lblRelativesWorkingMiddleName.setText(resourceBundle.getString("lblRelativesWorkingMiddleName"));
        lblRelativesWorkingLastName.setText(resourceBundle.getString("lblRelativesWorkingLastName"));
        lblWorkingDesignation.setText(resourceBundle.getString("lblWorkingDesignation"));
        lblRelativesWorkingBranch.setText(resourceBundle.getString("lblRelativesWorkingBranch"));
        lblRelativesWorkingReletionShip.setText(resourceBundle.getString("lblRelativesWorkingReletionShip"));
        lblDirectorFirstName.setText(resourceBundle.getString("lblDirectorFirstName"));
        lblDirectorMiddleName.setText(resourceBundle.getString("lblDirectorMiddleName"));
        lblDirectorLastName.setText(resourceBundle.getString("lblDirectorLastName"));
        lblDirectorReleationShip.setText(resourceBundle.getString("lblDirectorReleationShip"));
        
        
        //educational details
        
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTechnicalQualificationMarksScored.setText(resourceBundle.getString("lblTechnicalQualificationMarksScored"));
        btnLanguageDetDel.setText(resourceBundle.getString("btnLanguageDetDel"));
        rdoLanguageRead.setText(resourceBundle.getString("rdoLanguageRead"));
        btnAcademicEducationDetSave.setText(resourceBundle.getString("btnAcademicEducationDetSave"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblTechnicalQualificationTotMarks.setText(resourceBundle.getString("lblTechnicalQualificationTotMarks"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblUniversity.setText(resourceBundle.getString("lblUniversity"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblTechnicalQualificationType.setText(resourceBundle.getString("lblTechnicalQualificationType"));
        ((javax.swing.border.TitledBorder)panAcademicEducation.getBorder()).setTitle(resourceBundle.getString("panAcademicEducation"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        ((javax.swing.border.TitledBorder)panLanguageDate.getBorder()).setTitle(resourceBundle.getString("panLanguageDate"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnTechnicalQualificationDetSave.setText(resourceBundle.getString("btnTechnicalQualificationDetSave"));
        ((javax.swing.border.TitledBorder)panLanguageTable.getBorder()).setTitle(resourceBundle.getString("panLanguageTable"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnTechnicalQualificationDetDel.setText(resourceBundle.getString("btnTechnicalQualificationDetDel"));
        lblTotMarks.setText(resourceBundle.getString("lblTotMarks"));
        btnDeletedDetails.setText(resourceBundle.getString("btnDeletedDetails"));
        ((javax.swing.border.TitledBorder)panAcademicEducation2.getBorder()).setTitle(resourceBundle.getString("panAcademicEducation2"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnAcademicEducationDetNew.setText(resourceBundle.getString("btnAcademicEducationDetNew"));
        lblTechnicalQualificationUniversity.setText(resourceBundle.getString("lblTechnicalQualificationUniversity"));
        ((javax.swing.border.TitledBorder)panCustomer.getBorder()).setTitle(resourceBundle.getString("panCustomer"));
        lblNameOfSchool.setText(resourceBundle.getString("lblNameOfSchool"));
        lblTechnicalQualificationSpecilization.setText(resourceBundle.getString("lblTechnicalQualificationSpecilization"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblTechnicalQualificationDateOfPassing.setText(resourceBundle.getString("lblTechnicalQualificationDateOfPassing"));
        lblLanguageType.setText(resourceBundle.getString("lblLanguageType"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblTotMarksPer.setText(resourceBundle.getString("lblTotMarksPer"));
        lblGrade.setText(resourceBundle.getString("lblGrade"));
        lblEmpLevelEducation.setText(resourceBundle.getString("lblEmpLevelEducation"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        rdoLanguageReadeSpeak.setText(resourceBundle.getString("rdoLanguageReadeSpeak"));
        btnAcademicEducationDetDel.setText(resourceBundle.getString("btnAcademicEducationDetDel"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnLanguageDetNew.setText(resourceBundle.getString("btnLanguageDetNew"));
        lblSpecilization.setText(resourceBundle.getString("lblSpecilization"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        ((javax.swing.border.TitledBorder)panLanguageStatus.getBorder()).setTitle(resourceBundle.getString("panLanguageStatus"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblTechnicalQualificationGrade.setText(resourceBundle.getString("lblTechnicalQualificationGrade"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblMarksScored.setText(resourceBundle.getString("lblMarksScored"));
        lblDateOfPassing.setText(resourceBundle.getString("lblDateOfPassing"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblTechnicalQualificationTotMarksPer.setText(resourceBundle.getString("lblTechnicalQualificationTotMarksPer"));
        btnLanguageDetSave.setText(resourceBundle.getString("btnLanguageDetSave"));
        lblNameOfTechInst.setText(resourceBundle.getString("lblNameOfTechInst"));
        btnTechnicalQualificationDetNew.setText(resourceBundle.getString("btnTechnicalQualificationDetNew"));
        rdoLanguageWrite.setText(resourceBundle.getString("rdoLanguageWrite"));
        
        //loans details
        lblLoanRepaymentEndDate.setText(resourceBundle.getString("lblLoanRepaymentEndDate"));
        lblAccountType.setText(resourceBundle.getString("lblAccountType"));
        lblLoanAmount.setText(resourceBundle.getString("lblLoanAmount"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblEmployeeLoanType.setText(resourceBundle.getString("lblEmployeeLoanType"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnOprativeDetailsDetSave.setText(resourceBundle.getString("btnOprativeDetailsDetSave"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblLoanRepaymentStartDate.setText(resourceBundle.getString("lblLoanRepaymentStartDate"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        rdoLoanPreCloserNo.setText(resourceBundle.getString("rdoLoanPreCloserNo"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnTermLoansDetDel.setText(resourceBundle.getString("btnTermLoansDetDel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnOprativeDetailsDetDel.setText(resourceBundle.getString("btnOprativeDetailsDetDel"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        ((javax.swing.border.TitledBorder)panOprativeDetails.getBorder()).setTitle(resourceBundle.getString("panOprativeDetails"));
        lblLoanRemarks.setText(resourceBundle.getString("lblLoanRemarks"));
        lblLoanInstallmentAmount.setText(resourceBundle.getString("lblLoanInstallmentAmount"));
        lblLoanNoOfInstallments.setText(resourceBundle.getString("lblLoanNoOfInstallments"));
        btnDeletedDetails.setText(resourceBundle.getString("btnDeletedDetails"));
        btnTermLoansDetSave.setText(resourceBundle.getString("btnTermLoansDetSave"));
        lblSalaryCrBranch.setText(resourceBundle.getString("lblSalaryCrBranch"));
        rdoLoanPreCloserYes.setText(resourceBundle.getString("rdoLoanPreCloserYes"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        ((javax.swing.border.TitledBorder)panCustomer.getBorder()).setTitle(resourceBundle.getString("panCustomer"));
        lblLoanNo.setText(resourceBundle.getString("lblLoanNo"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        btnAccountNo.setText(resourceBundle.getString("btnAccountNo"));
        btnTermLoansDetNew.setText(resourceBundle.getString("btnTermLoansDetNew"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblLoanSanctionRefNo.setText(resourceBundle.getString("lblLoanSanctionRefNo"));
        ((javax.swing.border.TitledBorder)panLanguageTable1.getBorder()).setTitle(resourceBundle.getString("panLanguageTable1"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblLoanSanctionDate.setText(resourceBundle.getString("lblLoanSanctionDate"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnOprativeDetailsDetNew.setText(resourceBundle.getString("btnOprativeDetailsDetNew"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        lblLoanPreCloserYesNo.setText(resourceBundle.getString("lblLoanPreCloserYesNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblLoanCloserDate.setText(resourceBundle.getString("lblLoanCloserDate"));
        lblLoanRateofInterest.setText(resourceBundle.getString("lblLoanRateofInterest"));
        ((javax.swing.border.TitledBorder)panLanguageStatus1.getBorder()).setTitle(resourceBundle.getString("panLanguageStatus1"));
        lblLoanAvailedBranch.setText(resourceBundle.getString("lblLoanAvailedBranch"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        ((javax.swing.border.TitledBorder)panEmployeeLoan.getBorder()).setTitle(resourceBundle.getString("panEmployeeLoan"));
        
        //personal details
        lblDateofRetirement.setText(resourceBundle.getString("lblDateofRetirement"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblGradutionIncrementReleasedDate.setText(resourceBundle.getString("lblGradutionIncrementReleasedDate"));
        lblConfirmationDate.setText(resourceBundle.getString("lblConfirmationDate"));
        lblSignatureNo.setText(resourceBundle.getString("lblSignatureNo"));
        rdoCAIIBPART1IncrementNo.setText(resourceBundle.getString("rdoCAIIBPART1IncrementNo"));
        rdoGradutionIncrementNo.setText(resourceBundle.getString("rdoGradutionIncrementNo"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblCAIIBPART2IncrementReleasedDate.setText(resourceBundle.getString("lblCAIIBPART2IncrementReleasedDate"));
        lblPFNumber.setText(resourceBundle.getString("lblPFNumber"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblZonalOffice.setText(resourceBundle.getString("lblZonalOffice"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        rdoSignature_Yes.setText(resourceBundle.getString("rdoSignature_Yes"));
        rdoCAIIBPART2Increment_Yes.setText(resourceBundle.getString("rdoCAIIBPART2Increment_Yes"));
        lblSocietyMemberNo.setText(resourceBundle.getString("lblSocietyMemberNo"));
        rdoClubMembership_YES.setText(resourceBundle.getString("rdoClubMembership_YES"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        rdoCAIIBPART2Increment_No.setText(resourceBundle.getString("rdoCAIIBPART2Increment_No"));
        lblPresentBranchId.setText(resourceBundle.getString("lblPresentBranchId"));
        lblCAIIBPART1IncrementReleasedDate.setText(resourceBundle.getString("lblCAIIBPART1IncrementReleasedDate"));
        lblPresentGrade.setText(resourceBundle.getString("lblPresentGrade"));
        rdoAnyOtherIncrement_No.setText(resourceBundle.getString("rdoAnyOtherIncrement_No"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblClubMembership.setText(resourceBundle.getString("lblClubMembership"));
        lblLossPay_Months.setText(resourceBundle.getString("lblLossPay_Months"));
        lblPresentBasic.setText(resourceBundle.getString("lblPresentBasic"));
        lblCAIIBPART2IncrementYesNo.setText(resourceBundle.getString("lblCAIIBPART2IncrementYesNo"));
        lblNextIncrmentDate.setText(resourceBundle.getString("lblNextIncrmentDate"));
        btnDeletedDetails.setText(resourceBundle.getString("btnDeletedDetails"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        ((javax.swing.border.TitledBorder)panPresentWorkingDetails.getBorder()).setTitle(resourceBundle.getString("panPresentWorkingDetails"));
        ((javax.swing.border.TitledBorder)panCustomer.getBorder()).setTitle(resourceBundle.getString("panCustomer"));
        lblSigNoYesNo.setText(resourceBundle.getString("lblSigNoYesNo"));
        lblDateOfJoin.setText(resourceBundle.getString("lblDateOfJoin"));
        lblPFAcNominee.setText(resourceBundle.getString("lblPFAcNominee"));
        lblAnyOtherIncrementYesNo.setText(resourceBundle.getString("lblAnyOtherIncrementYesNo"));
        lbReginoalOffice.setText(resourceBundle.getString("lbReginoalOffice"));
        rdoAnyOtherIncrement_Yes.setText(resourceBundle.getString("rdoAnyOtherIncrement_Yes"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblProbationPeriod.setText(resourceBundle.getString("lblProbationPeriod"));
        ((javax.swing.border.TitledBorder)panOfficeDetails.getBorder()).setTitle(resourceBundle.getString("panOfficeDetails"));
        lblAnyOtherIncrementInstitutionName.setText(resourceBundle.getString("lblAnyOtherIncrementInstitutionName"));
        rdoClubMembership_No.setText(resourceBundle.getString("rdoClubMembership_No"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblLastIncrmentDate.setText(resourceBundle.getString("lblLastIncrmentDate"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        rdoCAIIBPART1IncrementYes.setText(resourceBundle.getString("rdoCAIIBPART1IncrementYes"));
        lblCAIIBPART1IncrementYesNo.setText(resourceBundle.getString("lblCAIIBPART1IncrementYesNo"));
        lblAnyLossOfPay.setText(resourceBundle.getString("lblAnyLossOfPay"));
        lblClubName.setText(resourceBundle.getString("lblClubName"));
        lblLossOfpay_Days.setText(resourceBundle.getString("lblLossOfpay_Days"));
        lblSocietyMember.setText(resourceBundle.getString("lblSocietyMember"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        rdoSignature_No.setText(resourceBundle.getString("rdoSignature_No"));
        rdoGradutionIncrementYes.setText(resourceBundle.getString("rdoGradutionIncrementYes"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblUnionMember.setText(resourceBundle.getString("lblUnionMember"));
        lblGradutionIncrementYesNo.setText(resourceBundle.getString("lblGradutionIncrementYesNo"));
        lblAnyOtherIncrementReleasedDate.setText(resourceBundle.getString("lblAnyOtherIncrementReleasedDate"));
        lblWorkingSince.setText(resourceBundle.getString("lblWorkingSince"));
        
    }
    
    private void setMandatoryMarks() {
        objEmployeeMasterUISupport =new EmployeeMasterUISupport(observable);
        objEmployeeMasterUISupport.putMandatoryMarks(CLASSNAME, this);
        //        objEmployeeMasterUISupport.putMandatoryMarks(CLASSNAME,panGuardianDetails);
        //        objEmployeeMasterUISupport.putMandatoryMarks(CLASSNAME,panContactInfo);
        //        objEmployeeMasterUISupport.putMandatoryMarks(CLASSNAME,panMISKYC);
        //        objEmployeeMasterUISupport.putMandatoryMarks(CLASSNAME,panPassPortDetails);
        //        objEmployeeMasterUISupport.putMandatoryMarks(CLASSNAME,panIncomeParticulars);
        //        objEmployeeMasterUISupport.putMandatoryMarks(CLASSNAME,panLandDetails);
    }
    public void update(Observable observed, Object arg) {
        updatePresentDetails();
        updateEmployeeLoanDetails();
        updateOprativeDetails();
        updateEducationDetails();
        updateDirector();
        updateLanguageDetails();
        updateTechnicalDetails();
        updateDependentDetails();
        updateEmployeeMasterData();
        updateRelative();
        updateAddress();
        updatePromotionDetails();
        try{
            if (observable.getPhotoByteArray()!=null) {      //lblPhoto.setIcon(new javax.swing.ImageIcon(observable.getPhotoFile()));
                lblPhoto.setIcon(new javax.swing.ImageIcon(observable.getPhotoByteArray()));
            }
            if (observable.getSignByteArray()!=null) {
                //               lblSign.setIcon(new javax.swing.ImageIcon(observable.getSignFile()));
                lblSign.setIcon(new javax.swing.ImageIcon(observable.getSignByteArray()));
            }
        }catch(Exception e){
            
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    private void updateOprativeDetails(){
        
        //        cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmAccountType().getDataForKey(observable.getCboOprativePordId())));
        //        cboSalaryCrBranch.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmSalaryCrBranch().getDataForKey(observable.getCboOpACBranch())));
        txtAccountNo.setText(observable.getTxtOPAcNo());
    }
    
    private void updateEmployeeLoanDetails(){
        cboEmployeeLoanType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmEmployeeLoanType().getDataForKey(observable.getCboEmployeeLoanType())));
        //        cboLoanAvailedBranch.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmLoanAvailedBranch().getDataForKey(observable.getCboLoanAvailedBranch())));
        System.out.println("$@$@$@ : "+((ComboBoxModel)cboLoanAvailedBranch.getModel()).getKey(1));
        System.out.println("$@$@$@ : "+((ComboBoxModel)cboLoanAvailedBranch.getModel()).getKey(2));
        txtLoanSanctionRefNo.setText(observable.getTxtLoanSanctionRefNo());
        tdtLoanSanctionDate.setDateValue(observable.getTdtLoanSanctionDate());
        txtLoanNo.setText(observable.getTxtLoanNo());
        txtLoanAmount.setText(observable.getTxtLoanAmount());
        txtLoanRateofInterest.setText(observable.getTxtLoanRateofInterest());
        txtLoanNoOfInstallments.setText(observable.getTxtLoanNoOfInstallments());
        txtLoanInstallmentAmount.setText(observable.getTxtLoanInstallmentAmount());
        tdtLoanRepaymentStartDate.setDateValue(observable.getTdtLoanRepaymentStartDate());
        tdtLoanRepaymentEndDate.setDateValue(observable.getTdtLoanRepaymentEndDate());
        if(observable.getRdoLoanPreCloserYesNo().equals("Y")){
            rdoLoanPreCloserYes.setSelected(true);
            rdoLoanPreCloserNo.setSelected(false);
        }else{
            rdoLoanPreCloserYes.setSelected(false);
            rdoLoanPreCloserNo.setSelected(true);
        }
        
        tdtLoanCloserDate.setDateValue(observable.getTdtLoanCloserDate());
        txtLoanRemarks.setText(observable.getTxtLoanRemarks());
        
    }
    private void updateSalaryDetails(){
        cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmAccountType().getDataForKey(observable.getCboOprativePordId())));
        txtAccountNo.setText(observable.getTxtOPAcNo());
    }
    private void updatePresentDetails(){
        if(observable.getRdoGradutionIncrementYesNo().equals("Y")){
            rdoGradutionIncrementYes.setSelected(true);
            rdoGradutionIncrementNo.setSelected(false);
        }else{
            rdoGradutionIncrementNo.setSelected(true);
            rdoGradutionIncrementYes.setSelected(false);
        }
        tdtGradutionIncrementReleasedDate.setDateValue(observable.getTdtGradutionIncrementReleasedDate());
        if(observable.getRdoCAIIBPART1IncrementYesNo().equals("Y")){
            rdoCAIIBPART1IncrementYes.setSelected(true);
            rdoCAIIBPART1IncrementNo.setSelected(false);
        }else{
            rdoCAIIBPART1IncrementNo.setSelected(true);
            rdoCAIIBPART1IncrementYes.setSelected(false);
        }
        tdtCAIIBPART1IncrementReleasedDate.setDateValue(observable.getTdtCAIIBPART1IncrementReleasedDate());
        if(observable.getRdoCAIIBPART2IncrementYesNo().equals("Y")){
            rdoCAIIBPART2Increment_Yes.setSelected(true);
            rdoCAIIBPART2Increment_No.setSelected(false);
        }else{
            rdoCAIIBPART2Increment_No.setSelected(true);
            rdoCAIIBPART2Increment_Yes.setSelected(false);
        }
        tdtCAIIBPART2IncrementReleasedDate.setDateValue(observable.getTdtCAIIBPART2IncrementReleasedDate());
        if(observable.getRdoAnyOtherIncrementYesNo().equals("Y")){
            rdoAnyOtherIncrement_Yes.setSelected(true);
            rdoAnyOtherIncrement_No.setSelected(false);
        }else{
            rdoAnyOtherIncrement_No.setSelected(true);
            rdoAnyOtherIncrement_Yes.setSelected(false);
        }
        txtAnyOtherIncrementInstitutionName.setText(observable.getTxtAnyOtherIncrementInstitutionName());
        tdtAnyOtherIncrementReleasedDate.setDateValue(observable.getTdtAnyOtherIncrementReleasedDate());
        txtPresentBasic.setText(observable.getTxtPresentBasic());
        tdtLastIncrmentDate.setDateValue(observable.getTdtLastIncrmentDate());
        txtLossPay_Months.setText(observable.getTxtLossPay_Months());
        txtLossOfpay_Days.setText(observable.getTxtLossOfpay_Days());
        tdtNextIncrmentDate.setDateValue(observable.getTdtNextIncrmentDate());
        if(observable.getRdoSigNoYesNo().equals("Y")){
            rdoSignature_Yes.setSelected(true);
            rdoSignature_No.setSelected(false);
        }else{
            rdoSignature_No.setSelected(true);
            rdoSignature_Yes.setSelected(false);
        }
        txtSignatureNo.setText(observable.getTxtSignatureNo());
        cboUnionMember.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmUnionMember().getDataForKey(observable.getCboUnionMember())));
        cboSocietyMember.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmSocietyMember().getDataForKey(observable.getCboSocietyMember())));
        txtSocietyMemberNo.setText(observable.getSocietyMemberNo());
        
        if(observable.getClubMembership().equals("Y")){
            rdoClubMembership_YES.setSelected(true);
            rdoClubMembership_No.setSelected(false);
        }else{
            rdoClubMembership_No.setSelected(true);
            rdoClubMembership_YES.setSelected(false);
        }
        
        txtClubName.setText(observable.getClubName());
        txtProbationPeriod.setText(observable.getTxtProbationPeriod());
        cboProbationPeriod.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmProbationPeriod().getDataForKey(observable.getCboProbationPeriod())));
        tdtConfirmationDate.setDateValue(observable.getTdtConfirmationDate());
        tdtWorkingSince.setDateValue(observable.getTdtWorkingSince());
        cboPresentGrade.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmPresentGrade().getDataForKey(observable.getCboPresentGrade())));
        cboDesignation.setSelectedItem(observable.getCboDesignation());
        txtPhysicalyHandicap.setText(CommonUtil.convertObjToStr(observable.getTxtPhysicalHandicap()));
        cboZonalOffice.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmZonalOffice().getDataForKey(observable.getCboZonalOffice())));
        cboPresentBranchId.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmPresentBranchId().getDataForKey(observable.getCboPresentBranchId())));
        cboReginoalOffice.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReginoalOffice().getDataForKey(observable.getCboReginoalOffice())));
        
    }
    private void updateRelative(){
        txtReleativeStaffId.setText(observable.getTxtRelativeStaffId());
        cboRelativesWorkingTittle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReleativeTittle().getDataForKey(observable.getCboRelativeTittle())));
        txtRelativesWorkingFirstName.setText(observable.getTxtReleativeFirstName());
        txtRelativesWorkingMiddleName.setText(observable.getTxtReleativeMiddleName());
        txtRelativesWorkingLastName.setText(observable.getTxtReleativeLastName());
        cboWorkingDesignation.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReleativeDisg().getDataForKey(observable.getCboReleativeDisg())));
        cboRelativesWorkingReletionShip.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReleativeReleationShip().getDataForKey(observable.getCboReleativeReleationShip())));
        cboRelativesWorkingBranch.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReleativeBranchId().getDataForKey(observable.getCboReleativeBranchId())));
        
    }
    private void updateEducationDetails(){
        cboEmpLevelEducation.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmLevelEducation().getDataForKey(observable.getCboEmpAcademicLevel())));
        txtNameOfSchool.setText(observable.getTxtEmpAcademicSchool());
        tdtDateOfPassing.setDateValue(observable.getTdtAcademicYearOfPassing());
        cboSpecilization.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmSpecilization().getDataForKey(observable.getCboAcademicSpecialization())));
        txtUniversity.setText(observable.getTxtAcademicUniverSity());
        txtMarksScored.setText(observable.getTxtAcademicMarksScored());
        txtTotMarks.setText(observable.getTxtAcademicTotalMarks());
        txtTotMarksPer.setText(observable.getTxtAcademicPer());
        cboGrade.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmGrade().getDataForKey(observable.getCboAcademicGrade())));
    }
    private void updateTechnicalDetails(){
        cboTechnicalQualificationType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmTechnicalQualification().getDataForKey(observable.getCboTechnicalLevel())));
        txtNameOfTechInst.setText(observable.getTxtTechnicalSchool());
        tdtTechnicalQualificationDateOfPassing.setDateValue(observable.getTdtTechnicalYearOfPassing());
        cboTechnicalQualificationSpecilization.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmTechnicalQualificationSpecilization().getDataForKey(observable.getCboTechnicalSpecialization())));
        txtTechnicalQualificationUniversity.setText(observable.getTxtTechnicalUniverSity());
        txtTechnicalQualificationMarksScored.setText(observable.getTxtTechnicalMarksScored());
        txtTechnicalQualificationTotMarks.setText(observable.getTxtTechnicalTotalMarks());
        txtTechnicalQualificationTotMarksPer.setText(observable.getTxtTechnicalPer());
        cboTechnicalQualificationGrade.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmTechnicalQualificationGrade().getDataForKey(observable.getCboTechnicalGrade())));
    }
    
    private void updateLanguageDetails(){
        cboLanguageType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmLanguage().getDataForKey(observable.getCboLanguageType())));
        
        
        if(CommonUtil.convertObjToStr(observable.getRdoRead()).length()>0 && CommonUtil.convertObjToStr(observable.getRdoRead()).equals("Y"))
            rdoLanguageRead.setSelected(true);
        else
            rdoLanguageRead.setSelected(false);
        
        if(CommonUtil.convertObjToStr(observable.getRdoWrite()).length()>0 && CommonUtil.convertObjToStr(observable.getRdoWrite()).equals("Y"))
            rdoLanguageWrite.setSelected(true);
        else
            rdoLanguageWrite.setSelected(false);
        
        if(CommonUtil.convertObjToStr(observable.getRdoSpeak()).length()>0 && CommonUtil.convertObjToStr(observable.getRdoSpeak()).equals("Y"))
            rdoLanguageReadeSpeak.setSelected(true);
        else
            rdoLanguageReadeSpeak.setSelected(false);
    }
    
    private void updateAddress(){
        cboAddressType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmAddressType().getDataForKey(observable.getCboEmpAddressType())));
        cboCity.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmCity().getDataForKey(observable.getCboEmpCity())));
        cboState.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmState().getDataForKey(observable.getCboEmpState())));
        txtPincode.setText(observable.getTxtEmpPinNo());
        txtStreet.setText(observable.getTxtEmpStreet());
        cboCountry.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmCountry().getDataForKey(observable.getCboEmpCountry())));
        txtArea.setText(observable.getTxtEmpArea());
        updatePhone();
    }
    
    private void updateEmployeeMasterData(){
        txtEmpID.setText(observable.getTxtEmpId());
        cboTitle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmTitle().getDataForKey(observable.getCboEmpTitle())));
        cboFsatherTitle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmFsatherTitle().getDataForKey(observable.getCboEmpFatheTitle())));
        cboMotherTitle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmMotherTitle().getDataForKey(observable.getCboEmpMotherTitle())));
        cboReligion.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReligion().getDataForKey(observable.getCboEmpReligon())));
        //        cboCaste.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmCaste().getDataForKey(observable.getCboEmpCaste())));
        cboCaste.setSelectedItem(observable.getCboEmpCaste());
        cboPFAcNominee.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmPFAcNominee().getDataForKey(observable.getCboEmpPfNominee())));
        tdtDateOfBirth.setDateValue(observable.getTdtEmpDateOfBirth());
        tdtDateOfJoin.setDateValue(observable.getTdtEmpJoinDate());
        tdtDateofRetirement.setDateValue(observable.getTdtEmpRetirementDate());
        String  maritalStatus=CommonUtil.convertObjToStr(observable.getRdoMaritalStatus());
        if(maritalStatus.length()>0 && maritalStatus.equals("M")){
            rdoMaritalStatus_Married.setSelected(true);
            rdoMaritalStatus_Single.setSelected(false);
        }else{
            rdoMaritalStatus_Married.setSelected(false);
            rdoMaritalStatus_Single.setSelected(true);
        }
        
        String  male=CommonUtil.convertObjToStr(observable.getRdoEmpGender());
        if(male.length()>0 && male.equals("M")){
            rdoGender_Male.setSelected(true);
            rdoGender_Female.setSelected(false);
            
        }else{
            rdoGender_Male.setSelected(false);
            rdoGender_Female.setSelected(true);
        }
        
        String  father=CommonUtil.convertObjToStr(observable.getRdoFatherHusband());
        if(father.length()>0 && father.equals("F")){
            rdoFather.setSelected(true);
            rdoHusband.setSelected(false);
            lblFatherFirstName.setName("Father Name");
            
        }else{
            rdoFather.setSelected(false);
            rdoHusband.setSelected(true);
            lblFatherFirstName.setName("Husband Name");
        }
        father="";
        male="";
        maritalStatus="";
        txtFirstName.setText(observable.getTxtEmpFirstName());
        txtMiddleName.setText(observable.getTxtEmpMiddleName());
        txtLastName.setText(observable.getTxtEmpLastName());
        txtFatherFirstName.setText(observable.getTxtEmpFatherFirstName());
        txtFatherMiddleName.setText(observable.getTxtEmpFatherMIddleName());
        txtFatherLastName.setText(observable.getTxtEmpFatherLasteName());
        txtMotherFirstName.setText(observable.getTxtEmpMotherFirstName());
        txtMotherMiddleName.setText(observable.getTxtEmpMotherMIddleName());
        txtMotherLastName.setText(observable.getTxtEmpMotherLasteName());
        txtPlaceOfBirth.setText(observable.getTxtempPlaceOfBirth());
        txtHomeTown.setText(observable.getTxtEmpHomeTown());
        txtIDCardNoNo.setText(observable.getTxtEmpIdCardNo());
        txtUIDNo.setText(observable.getTxtEmpUIdNo());
        txtPanNumber.setText(observable.getTxtEmpPanNo());
        txtPFNumber.setText(observable.getTxtEmpPfNo());
        txtPassportFirstName.setText(observable.getTxtPassportFirstName());
        txtPassportMiddleName.setText(observable.getTxtPassportMiddleName());
        txtPassportLastName.setText(observable.getTxtPassportLastName());
        tdtPassportIssueDt.setDateValue(observable.getTdtPassportIssueDt());
        tdtPassportValidUpto.setDateValue(observable.getTdtPassportValidUpto());
        txtPassportNo.setText(observable.getTxtPassportNo());
        cboPassportIssuePlace.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmPassportIssuePlace().getDataForKey(observable.getCboPassportIssuePlace())));
        cboPassportTitle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmPassportTitle().getDataForKey(observable.getCboPassportTitle())));
        txtPassportIssueAuth.setText(observable.getTxtPassportIssueAuth());
        cboBloodGroup.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmBloodGroup().getDataForKey(observable.getCboBloodGroup())));
        cboDomicileState.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmDomicileState().getDataForKey(observable.getCboDomicileState())));
        txtEmailId.setText(observable.getTxtEmailId());
        txtDrivingLicenceNo.setText(observable.getTxtDrivingLicenceNo());
        tdtDLRenewalDate.setDateValue(observable.getTdtDLRenewalDate());
        txtMajorHealthProbeem.setText(observable.getTxtMajorHealthProbeem());
    }
    
    private void updateDependentDetails(){
        cboReletaionShip.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReleationShip().getDataForKey(observable.getCboDepReleationShip())));
        cboReleationFHTitle.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmReleationFHTitle().getDataForKey(observable.getCboEmpDepTitle())));
        cboFamilyMemEducation.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmFamilyMemEducation().getDataForKey(observable.getCboDepEducation())));
        cboFamilyMemberProf.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmFamilyMemberProf().getDataForKey(observable.getCboDepProfession())));
        txtReleationFHFirstName.setText(CommonUtil.convertObjToStr(observable.getTxtEmpDepFirstName()));
        txtReleationFHMiddleName.setText(CommonUtil.convertObjToStr(observable.getTxtEmpDepMIddleName()));
        txtReleationFHLastName.setText(CommonUtil.convertObjToStr(observable.getTxtEmpDepLasteName()));
        txtDepIncomePerannum.setText(CommonUtil.convertObjToStr(observable.getTxtDepIncomePerannum()));
        txtEmpWith.setText(CommonUtil.convertObjToStr(observable.getTxtEmpWith()));
        tdtRelationShipDateofBirth.setDateValue(observable.getTdtDepDateOfBirth());
        
        if(CommonUtil.convertObjToStr(observable.getRdoDepYesNo()).length()>0 && CommonUtil.convertObjToStr(observable.getRdoDepYesNo()).equals("Y"))
            rdoDependentYes.setSelected(true);
        else
            rdoDependentNo.setSelected(true);
        if(CommonUtil.convertObjToStr(observable.getRdoLiableYesNo()).length()>0 && CommonUtil.convertObjToStr(observable.getRdoLiableYesNo()).equals("Y"))
            rdoLiableForTransferYes.setSelected(true);
        else
            rdoLiableForTransferNo.setSelected(true);
        
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtEmpId(CommonUtil.convertObjToStr(txtEmpID.getText()));
        observable.setCboEmpTitle(CommonUtil.convertObjToStr(((ComboBoxModel)cboTitle.getModel()).getKeyForSelected().toString()));
        observable.setTxtEmpFirstName(CommonUtil.convertObjToStr(txtFirstName.getText()));
        observable.setTxtEmpMiddleName(CommonUtil.convertObjToStr(txtMiddleName.getText()));
        observable.setTxtEmpLastName(CommonUtil.convertObjToStr(txtLastName.getText()));
        observable.setTxtEmpFatherFirstName(CommonUtil.convertObjToStr(txtFatherFirstName.getText()));
        observable.setTxtEmpFatherMIddleName(CommonUtil.convertObjToStr(txtFatherMiddleName.getText()));
        observable.setTxtEmpFatherLasteName(CommonUtil.convertObjToStr(txtFatherLastName.getText()));
        observable.setTxtEmpMotherFirstName(CommonUtil.convertObjToStr(txtMotherFirstName.getText()));
        observable.setTxtEmpMotherMIddleName(CommonUtil.convertObjToStr(txtMotherMiddleName.getText()));
        observable.setTxtEmpMotherLasteName(CommonUtil.convertObjToStr(txtMotherLastName.getText()));
        observable.setCboEmpFatheTitle(CommonUtil.convertObjToStr(((ComboBoxModel)cboFsatherTitle.getModel()).getKeyForSelected().toString()));
        observable.setCboEmpMotherTitle(CommonUtil.convertObjToStr(((ComboBoxModel)cboMotherTitle.getModel()).getKeyForSelected().toString()));
        observable.setTdtEmpDateOfBirth(CommonUtil.convertObjToStr(tdtDateOfBirth.getDateValue()));
        observable.setCboBloodGroup(CommonUtil.convertObjToStr(((ComboBoxModel)cboBloodGroup.getModel()).getKeyForSelected().toString()));
        observable.setCboDomicileState(CommonUtil.convertObjToStr(((ComboBoxModel)cboDomicileState.getModel()).getKeyForSelected().toString()));
        observable.setTxtEmailId(txtEmailId.getText());
        observable.setTxtDrivingLicenceNo(txtDrivingLicenceNo.getText());
        observable.setTdtDLRenewalDate(CommonUtil.convertObjToStr(tdtDLRenewalDate.getDateValue()));
        //        observable.setCboPhysicalHandicap(CommonUtil.convertObjToStr(((ComboBoxModel)cboPhysicalHandicap.getModel()).getKeyForSelected().toString()));
        observable.setTxtMajorHealthProbeem(txtMajorHealthProbeem.getText());
        //        observable.setTxtempAge(CommonUtil.convertObjToStr(txtAge.getText()));
        observable.setTxtempPlaceOfBirth(CommonUtil.convertObjToStr(txtPlaceOfBirth.getText()));
        observable.setCboEmpReligon(CommonUtil.convertObjToStr(((ComboBoxModel)cboReligion.getModel()).getKeyForSelected().toString()));
        //        observable.setCboEmpCaste(CommonUtil.convertObjToStr(((ComboBoxModel)cboCaste.getModel()).getKeyForSelected().toString()));
        observable.setCboEmpCaste((String) cboCaste.getSelectedItem());
        observable.setTxtEmpHomeTown(CommonUtil.convertObjToStr(txtHomeTown.getText()));
        observable.setTxtEmpIdCardNo(CommonUtil.convertObjToStr(txtIDCardNoNo.getText()));
        observable.setTxtEmpUIdNo(CommonUtil.convertObjToStr(txtUIDNo.getText()));
        observable.setTxtEmpPanNo(CommonUtil.convertObjToStr(txtPanNumber.getText()));
        observable.setTxtEmpPfNo(CommonUtil.convertObjToStr(txtPFNumber.getText()));
        observable.setCboEmpPfNominee(CommonUtil.convertObjToStr(((ComboBoxModel)cboPFAcNominee.getModel()).getKeyForSelected().toString()));
        observable.setTdtEmpJoinDate(CommonUtil.convertObjToStr(tdtDateOfJoin.getDateValue()));
        observable.setTdtEmpRetirementDate(CommonUtil.convertObjToStr(tdtDateofRetirement.getDateValue()));
        if(rdoGender_Male.isSelected()==true)
            observable.setRdoEmpGender(CommonUtil.convertObjToStr("M"));
        if(rdoGender_Female.isSelected()==true)
            observable.setRdoEmpGender(CommonUtil.convertObjToStr("F"));
        if(rdoMaritalStatus_Married.isSelected()==true)
            observable.setRdoMaritalStatus(CommonUtil.convertObjToStr("M"));
        if(rdoMaritalStatus_Single.isSelected()==true)
            observable.setRdoMaritalStatus(CommonUtil.convertObjToStr("U"));
        
        if(rdoFather.isSelected()==true)
            observable.setRdoFatherHusband(CommonUtil.convertObjToStr("F"));
        if(rdoHusband.isSelected()==true)
            observable.setRdoFatherHusband(CommonUtil.convertObjToStr("H"));
        
        observable.setCboEmpAddressType(CommonUtil.convertObjToStr(((ComboBoxModel)cboAddressType.getModel()).getKeyForSelected().toString()));
        observable.setTxtEmpStreet(CommonUtil.convertObjToStr(txtStreet.getText()));
        observable.setTxtEmpArea(CommonUtil.convertObjToStr(txtArea.getText()));
        observable.setCboEmpCity(CommonUtil.convertObjToStr(((ComboBoxModel)cboCity.getModel()).getKeyForSelected().toString()));
        observable.setCboEmpState(CommonUtil.convertObjToStr(((ComboBoxModel)cboState.getModel()).getKeyForSelected().toString()));
        observable.setCboEmpCountry(CommonUtil.convertObjToStr(((ComboBoxModel)cboCountry.getModel()).getKeyForSelected().toString()));
        observable.setTxtEmpPinNo(CommonUtil.convertObjToStr(txtPincode.getText()));
        observable.setTxtPhoneNumber(txtPhoneNumber.getText());
        if (cboPhoneType.getSelectedItem() != null) {
            observable.setCboPhoneType((String)((ComboBoxModel)cboPhoneType.getModel()).getKeyForSelected());
        }
        //        observable.setCboPhoneType(CommonUtil.convertObjToStr(((ComboBoxModel)cboPhoneType.getModel()).getKeyForSelected().toString()));
        observable.setTxtAreaCode(txtAreaCode.getText());observable.setTxtPassportFirstName(CommonUtil.convertObjToStr(txtPassportFirstName.getText()));
        observable.setTxtPassportMiddleName(CommonUtil.convertObjToStr(txtPassportMiddleName.getText()));
        observable.setTxtPassportLastName(CommonUtil.convertObjToStr(txtPassportLastName.getText()));
        observable.setTdtPassportIssueDt(CommonUtil.convertObjToStr(tdtPassportIssueDt.getDateValue()));
        observable.setTdtPassportValidUpto(CommonUtil.convertObjToStr(tdtPassportValidUpto.getDateValue()));
        observable.setTxtPassportNo(CommonUtil.convertObjToStr(txtPassportNo.getText()));
        observable.setTxtPassportIssueAuth(CommonUtil.convertObjToStr(txtPassportIssueAuth.getText()));
        observable.setCboPassportTitle(CommonUtil.convertObjToStr(((ComboBoxModel)cboPassportTitle.getModel()).getKeyForSelected().toString()));
        observable.setCboPassportIssuePlace(CommonUtil.convertObjToStr(((ComboBoxModel)cboPassportIssuePlace.getModel()).getKeyForSelected().toString()));
        
        //Academic Education Details
        if(tblAcademicEducation.getSelectedRow()!= -1) {
            observable.setAcademicID(CommonUtil.convertObjToStr(tblAcademicEducation.getValueAt(tblAcademicEducation.getSelectedRow(),0)));
        }
        observable.setCboEmpAcademicLevel(CommonUtil.convertObjToStr(((ComboBoxModel)cboEmpLevelEducation.getModel()).getKeyForSelected().toString()));
        observable.setTxtEmpAcademicSchool(CommonUtil.convertObjToStr(txtNameOfSchool.getText()));
        observable.setTdtAcademicYearOfPassing(CommonUtil.convertObjToStr(tdtDateOfPassing.getDateValue()));
        observable.setCboAcademicSpecialization(CommonUtil.convertObjToStr(((ComboBoxModel)cboSpecilization.getModel()).getKeyForSelected().toString()));
        observable.setTxtAcademicUniverSity(CommonUtil.convertObjToStr(txtUniversity.getText()));
        observable.setTxtAcademicMarksScored(CommonUtil.convertObjToStr(txtMarksScored.getText()));
        observable.setTxtAcademicTotalMarks(CommonUtil.convertObjToStr(txtTotMarks.getText()));
        observable.setTxtAcademicPer(CommonUtil.convertObjToStr(txtTotMarksPer.getText()));
        observable.setCboAcademicGrade(CommonUtil.convertObjToStr(((ComboBoxModel)cboGrade.getModel()).getKeyForSelected().toString()));
        
        //Technical  Education Details
        observable.setCboTechnicalLevel(CommonUtil.convertObjToStr(((ComboBoxModel)cboTechnicalQualificationType.getModel()).getKeyForSelected().toString()));
        observable.setTxtTechnicalSchool(CommonUtil.convertObjToStr(txtNameOfTechInst.getText()));
        observable.setTdtTechnicalYearOfPassing(CommonUtil.convertObjToStr(tdtTechnicalQualificationDateOfPassing.getDateValue()));
        observable.setCboTechnicalSpecialization(CommonUtil.convertObjToStr(((ComboBoxModel)cboTechnicalQualificationSpecilization.getModel()).getKeyForSelected().toString()));
        observable.setTxtTechnicalUniverSity(CommonUtil.convertObjToStr(txtTechnicalQualificationUniversity.getText()));
        observable.setTxtTechnicalMarksScored(CommonUtil.convertObjToStr(txtTechnicalQualificationMarksScored.getText()));
        observable.setTxtTechnicalTotalMarks(CommonUtil.convertObjToStr(txtTechnicalQualificationTotMarks.getText()));
        observable.setTxtTechnicalPer(CommonUtil.convertObjToStr(txtTechnicalQualificationTotMarksPer.getText()));
        observable.setCboTechnicalGrade(CommonUtil.convertObjToStr(((ComboBoxModel)cboTechnicalQualificationGrade.getModel()).getKeyForSelected().toString()));
        
        
        //PRIVATE LANGUAGE
        observable.setCboLanguageType(CommonUtil.convertObjToStr(((ComboBoxModel)cboLanguageType.getModel()).getKeyForSelected().toString()));
        
        if(rdoLanguageRead.isSelected()==true)
            observable.setRdoRead(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoRead(CommonUtil.convertObjToStr("N"));
        if(rdoLanguageWrite.isSelected()==true)
            observable.setRdoWrite(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoWrite(CommonUtil.convertObjToStr("N"));
        if(rdoLanguageReadeSpeak.isSelected()==true)
            observable.setRdoSpeak(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoSpeak(CommonUtil.convertObjToStr("N"));
        //private Dependent
        
        observable.setCboDepReleationShip(CommonUtil.convertObjToStr(((ComboBoxModel)cboReletaionShip.getModel()).getKeyForSelected().toString()));
        observable.setTxtEmpDepFirstName(CommonUtil.convertObjToStr(txtReleationFHFirstName.getText()));
        observable.setTxtEmpDepMIddleName(CommonUtil.convertObjToStr(txtReleationFHMiddleName.getText()));
        observable.setTxtEmpDepLasteName(CommonUtil.convertObjToStr(txtReleationFHLastName.getText()));
        observable.setCboEmpDepTitle(CommonUtil.convertObjToStr(((ComboBoxModel)cboReleationFHTitle.getModel()).getKeyForSelected().toString()));
        observable.setTxtDepIncomePerannum(CommonUtil.convertObjToStr(txtDepIncomePerannum.getText()));
        observable.setTxtEmpWith(CommonUtil.convertObjToStr(txtEmpWith.getText()));
        //added here
        if(tblFamily.getSelectedRow()!= -1) {
            observable.setDependentId(CommonUtil.convertObjToStr(tblFamily.getValueAt(tblFamily.getSelectedRow(),0)));
        }
        observable.setTdtDepDateOfBirth(CommonUtil.convertObjToStr(tdtRelationShipDateofBirth.getDateValue()) );
        observable.setCboDepEducation(CommonUtil.convertObjToStr(((ComboBoxModel)cboFamilyMemEducation.getModel()).getKeyForSelected().toString()));
        observable.setCboDepProfession(CommonUtil.convertObjToStr(((ComboBoxModel)cboFamilyMemberProf.getModel()).getKeyForSelected().toString()));
        if(rdoDependentYes.isSelected()==true)
            observable.setRdoDepYesNo(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoDepYesNo(CommonUtil.convertObjToStr("N"));
        if(rdoLiableForTransferYes.isSelected()==true)
            observable.setRdoLiableYesNo(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoLiableYesNo(CommonUtil.convertObjToStr("N"));
        
        
        //RELATIVES DETAILS
        observable.setReleativeSysId(releativeSysId);
        observable.setTxtRelativeStaffId(CommonUtil.convertObjToStr(txtReleativeStaffId.getText()));
        observable.setCboRelativeTittle(CommonUtil.convertObjToStr(((ComboBoxModel)cboRelativesWorkingTittle.getModel()).getKeyForSelected().toString()));
        observable.setTxtReleativeFirstName(CommonUtil.convertObjToStr(txtRelativesWorkingFirstName.getText()));
        observable.setTxtReleativeMiddleName(CommonUtil.convertObjToStr(txtRelativesWorkingMiddleName.getText()));
        observable.setCboReleativeReleationShip(CommonUtil.convertObjToStr(((ComboBoxModel)cboRelativesWorkingReletionShip.getModel()).getKeyForSelected().toString()));
        observable.setTxtReleativeLastName(CommonUtil.convertObjToStr(txtRelativesWorkingLastName.getText()));
        observable.setCboReleativeBranchId(CommonUtil.convertObjToStr(((ComboBoxModel)cboRelativesWorkingBranch.getModel()).getKeyForSelected().toString()));
        observable.setCboReleativeDisg(CommonUtil.convertObjToStr(((ComboBoxModel)cboWorkingDesignation.getModel()).getKeyForSelected().toString()));
        
        
        //director details
        
        if(tblDetailsOfRelativeDirector.getSelectedRow()!= -1) {
            observable.setDirectorID(CommonUtil.convertObjToStr(tblDetailsOfRelativeDirector.getValueAt(tblDetailsOfRelativeDirector.getSelectedRow(),0)));
        }
        observable.setCboDirectorTittle(CommonUtil.convertObjToStr(((ComboBoxModel)cboDirectorTittle.getModel()).getKeyForSelected().toString()));
        observable.setTxtDirectorReleationShip(CommonUtil.convertObjToStr(((ComboBoxModel)cboDirectorReleationShip.getModel()).getKeyForSelected().toString()));
        observable.setTxtDirectorFirstName(txtDirectorFirstName.getText());
        observable.setTxtDirectorMiddleName(txtDirectorMiddleName.getText());
        observable.setTxtDirectorLastName(txtDirectorLastName.getText());
        
        // present Details
        
        
        
        if(rdoGradutionIncrementYes.isSelected()==true)
            observable.setRdoGradutionIncrementYesNo(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoGradutionIncrementYesNo(CommonUtil.convertObjToStr("N"));
        observable.setTdtGradutionIncrementReleasedDate(CommonUtil.convertObjToStr(tdtGradutionIncrementReleasedDate.getDateValue()));
        
        if(rdoCAIIBPART1IncrementYes.isSelected()==true)
            observable.setRdoCAIIBPART1IncrementYesNo(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoCAIIBPART1IncrementYesNo(CommonUtil.convertObjToStr("N"));
        observable.setTdtCAIIBPART1IncrementReleasedDate(CommonUtil.convertObjToStr(tdtCAIIBPART1IncrementReleasedDate.getDateValue()));
        
        if(rdoCAIIBPART2Increment_Yes.isSelected()==true)
            observable.setRdoCAIIBPART2IncrementYesNo(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoCAIIBPART2IncrementYesNo(CommonUtil.convertObjToStr("N"));
        observable.setTdtCAIIBPART2IncrementReleasedDate(CommonUtil.convertObjToStr(tdtCAIIBPART2IncrementReleasedDate.getDateValue()));
        
        if(rdoAnyOtherIncrement_Yes.isSelected()==true)
            observable.setRdoAnyOtherIncrementYesNo(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoAnyOtherIncrementYesNo(CommonUtil.convertObjToStr("N"));
        observable.setTdtAnyOtherIncrementReleasedDate(CommonUtil.convertObjToStr(tdtAnyOtherIncrementReleasedDate.getDateValue()));
        observable.setTxtAnyOtherIncrementInstitutionName(CommonUtil.convertObjToStr(txtAnyOtherIncrementInstitutionName.getText()));
        observable.setTxtPresentBasic(CommonUtil.convertObjToStr(txtPresentBasic.getText()));
        observable.setTdtLastIncrmentDate(CommonUtil.convertObjToStr(tdtLastIncrmentDate.getDateValue()));
        observable.setTxtLossOfpay_Days(CommonUtil.convertObjToStr(txtLossOfpay_Days.getText()));
        observable.setTxtLossPay_Months(CommonUtil.convertObjToStr(txtLossPay_Months.getText()));
        observable.setTdtNextIncrmentDate(CommonUtil.convertObjToStr(tdtNextIncrmentDate.getDateValue()));
        if(rdoSignature_Yes.isSelected()==true)
            observable.setRdoSigNoYesNo(CommonUtil.convertObjToStr("Y"));
        else
            observable.setRdoSigNoYesNo(CommonUtil.convertObjToStr("N"));
        observable.setTxtSignatureNo(CommonUtil.convertObjToStr(txtSignatureNo.getText()));
        observable.setCboUnionMember(CommonUtil.convertObjToStr(((ComboBoxModel)cboUnionMember.getModel()).getKeyForSelected().toString()));
        observable.setCboSocietyMember(CommonUtil.convertObjToStr(((ComboBoxModel)cboSocietyMember.getModel()).getKeyForSelected().toString()));
        observable.setSocietyMemberNo(CommonUtil.convertObjToStr(txtSocietyMemberNo.getText()));
        
        if(rdoClubMembership_YES.isSelected()==true)
            observable.setClubMembership(CommonUtil.convertObjToStr("Y"));
        else
            observable.setClubMembership(CommonUtil.convertObjToStr("N"));
        observable.setClubName(CommonUtil.convertObjToStr(txtClubName.getText()));
        observable.setTxtProbationPeriod(CommonUtil.convertObjToStr(txtProbationPeriod.getText()));
        observable.setCboProbationPeriod(CommonUtil.convertObjToStr(((ComboBoxModel)cboProbationPeriod.getModel()).getKeyForSelected().toString()));
        observable.setTdtConfirmationDate(CommonUtil.convertObjToStr(tdtConfirmationDate.getDateValue()));
        observable.setCboPresentBranchId(CommonUtil.convertObjToStr(((ComboBoxModel)cboPresentBranchId.getModel()).getKeyForSelected().toString()));
        observable.setCboReginoalOffice(CommonUtil.convertObjToStr(((ComboBoxModel)cboReginoalOffice.getModel()).getKeyForSelected().toString()));
        observable.setCboZonalOffice(CommonUtil.convertObjToStr(((ComboBoxModel)cboZonalOffice.getModel()).getKeyForSelected().toString()));
        observable.setTdtWorkingSince(CommonUtil.convertObjToStr(tdtWorkingSince.getDateValue()));
        //        observable.setCboDesignation(CommonUtil.convertObjToStr(((ComboBoxModel)cboDesignation.getModel()).getKeyForSelected().toString()));
        observable.setCboPresentGrade(CommonUtil.convertObjToStr(((ComboBoxModel)cboPresentGrade.getModel()).getKeyForSelected().toString()));
        observable.setCboDesignation((String) cboDesignation.getSelectedItem());
        //        observable.setCboEmployeeLoanType(CommonUtil.convertObjToStr(((ComboBoxModel)cboEmployeeLoanType.getModel()).getKeyForSelected().toString()));
        //        observable.setCboLoanAvailedBranch(CommonUtil.convertObjToStr(((ComboBoxModel)cboLoanAvailedBranch.getModel()).getKeyForSelected().toString()));
        //        System.out.println("@$@#@ setCboEmployeeLoanType : "+((ComboBoxModel)cboEmployeeLoanType.getModel()).getKeyForSelected());
        //        System.out.println("@$@#@ setCboLoanAvailedBranch : "+((ComboBoxModel)cboLoanAvailedBranch.getModel()).getKeyForSelected());
        // LOAN DETAILS
        observable.setTxtLoanSanctionRefNo(CommonUtil.convertObjToStr(txtLoanSanctionRefNo.getText()));
        observable.setTdtLoanSanctionDate(CommonUtil.convertObjToStr(tdtLoanSanctionDate.getDateValue()));
        observable.setTxtLoanNo(CommonUtil.convertObjToStr(txtLoanNo.getText()));
        observable.setTxtLoanAmount(CommonUtil.convertObjToStr(txtLoanAmount.getText()));
        observable.setTxtLoanRateofInterest(CommonUtil.convertObjToStr(txtLoanRateofInterest.getText()));
        observable.setTxtLoanNoOfInstallments(CommonUtil.convertObjToStr(txtLoanNoOfInstallments.getText()));
        observable.setTxtLoanInstallmentAmount(CommonUtil.convertObjToStr(txtLoanInstallmentAmount.getText()));
        observable.setTdtLoanRepaymentStartDate(CommonUtil.convertObjToStr(tdtLoanRepaymentStartDate.getDateValue()));
        observable.setTdtLoanRepaymentEndDate(CommonUtil.convertObjToStr(tdtLoanRepaymentEndDate.getDateValue()));
        observable.setCboEmployeeLoanType(CommonUtil.convertObjToStr(((ComboBoxModel)cboEmployeeLoanType.getModel()).getKeyForSelected().toString()));
        observable.setCboLoanAvailedBranch(CommonUtil.convertObjToStr(((ComboBoxModel)cboLoanAvailedBranch.getModel()).getKeyForSelected().toString()));
        observable.setTdtLoanCloserDate(CommonUtil.convertObjToStr(tdtLoanCloserDate.getDateValue()));
        observable.setTxtLoanRemarks(CommonUtil.convertObjToStr(txtLoanRemarks.getText()));
        
        
        if(rdoLoanPreCloserYes.isSelected()==true){
            observable.setRdoLoanPreCloserYesNo("Y");
        }else{
            observable.setRdoLoanPreCloserYesNo("N");
        }
        
        observable.setCboOprativePordId(CommonUtil.convertObjToStr(((ComboBoxModel)cboAccountType.getModel()).getKeyForSelected().toString()));
        observable.setCboOpACBranch(CommonUtil.convertObjToStr(((ComboBoxModel)cboSalaryCrBranch.getModel()).getKeyForSelected().toString()));
        observable.setTxtOPAcNo(CommonUtil.convertObjToStr(txtAccountNo.getText()));
        observable.setTxtPhysicalHandicap(CommonUtil.convertObjToStr(txtPhysicalyHandicap.getText()));
        //        promotion details
        observable.setTxtPromotionEmployeeId(CommonUtil.convertObjToStr(txtPromotionEmployeeId.getText()));
        observable.setTxtRemarks(CommonUtil.convertObjToStr(txtRemarks.getText()));
        observable.setTxtNewBasic(CommonUtil.convertObjToStr(txtNewBasic.getText()));
        observable.setPromotionID(CommonUtil.convertObjToStr(txtPromotionSalId.getText()));
        observable.setTxtPromotionEmployeeName(CommonUtil.convertObjToStr(txtPromotionEmployeeName.getText()));
        observable.setTxtPromotionLastGrade(CommonUtil.convertObjToStr(txtPromotionLastGrade.getText()));
        observable.setTxtPromotionLastDesg(CommonUtil.convertObjToStr(txtPromotionLastDesg.getText()));
        observable.setTdtPromotionEffectiveDateValue(CommonUtil.convertObjToStr(tdtPromotionEffectiveDateValue.getDateValue()));
        observable.setTdtPromotionCreatedDateValue(CommonUtil.convertObjToStr(tdtPromotionCreatedDateValue.getDateValue()));
        observable.setTxtPromotionBasicPayValue(CommonUtil.convertObjToStr(txtPromotionBasicPayValue.getText()));
        observable.setcboPromotionGradeValue(CommonUtil.convertObjToStr(((ComboBoxModel)this.cboPromotionGrade.getModel()).getKeyForSelected()));
        observable.setCboPromotedDesignation((String) cboPromotedDesignation.getSelectedItem());
        
        //        observable.setCboPromotedDesignation((String)((ComboBoxModel)this.cboPromotedDesignation.getModel()).getKeyForSelected());
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        txtEmpID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmployeeId"));
        cboTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTitle"));
        txtFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFirstName"));
        txtMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiddleName"));
        txtLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastName"));
        rdoGender_Male.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoGender_Male"));
        rdoGender_Female.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoGender_Female"));
        rdoMaritalStatus_Single.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMaritalStatus_Single"));
        rdoMaritalStatus_Married.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMaritalStatus_Married"));
        rdoFather.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoFather"));
        cboFsatherTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFsatherTitle"));
        txtFatherFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFatherFirstName"));
        txtFatherMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFatherMiddleName"));
        txtFatherLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFatherLastName"));
        cboMotherTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMotherTitle"));
        txtMotherFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMotherFirstName"));
        txtMotherMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMotherMiddleName"));
        txtMotherLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMotherLastName"));
        tdtDateOfBirth.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfBirth"));
        txtPlaceOfBirth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPlaceOfBirth"));
        cboReligion.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReligion"));
        cboCaste.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCaste"));
        txtHomeTown.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHomeTown"));
        txtIDCardNoNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIDCardNoNo"));
        txtUIDNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUIDNo"));
        txtPanNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPanNumber"));
        txtPFNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPFNumber"));
        cboPFAcNominee.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPFAcNominee"));
        tdtDateOfJoin.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfJoin"));
        tdtDateofRetirement.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateofRetirement"));
        cboAddressType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAddressType"));
        txtStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet"));
        txtArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
        cboCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry"));
        txtPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPincode"));
        txtPassportFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportFirstName"));
        txtPassportMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportMiddleName"));
        tdtPassportIssueDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPassportIssueDt"));
        tdtPassportValidUpto.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPassportValidUpto"));
        txtPassportLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportLastName"));
        cboPassportTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPassportTitle"));
        txtPassportNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportNo"));
        txtPassportIssueAuth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPassportIssueAuth"));
        cboPassportIssuePlace.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPassportIssuePlace"));
        cboEmpLevelEducation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEmpLevelEducation"));
        txtNameOfSchool.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNameOfSchool"));
        tdtDateOfPassing.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfPassing"));
        cboSpecilization.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSpecilization"));
        txtUniversity.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUniversity"));
        txtMarksScored.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMarksScored"));
        txtTotMarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotMarks"));
        txtTotMarksPer.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotMarksPer"));
        cboGrade.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGrade"));
        cboTechnicalQualificationType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTechnicalQualificationType"));
        txtNameOfTechInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNameOfTechInst"));
        tdtTechnicalQualificationDateOfPassing.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTechnicalQualificationDateOfPassing"));
        txtTechnicalQualificationMarksScored.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationMarksScored"));
        cboTechnicalQualificationGrade.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTechnicalQualificationGrade"));
        cboTechnicalQualificationSpecilization.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTechnicalQualificationSpecilization"));
        txtTechnicalQualificationUniversity.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationUniversity"));
        txtTechnicalQualificationTotMarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationTotMarks"));
        txtTechnicalQualificationTotMarksPer.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationTotMarksPer"));
        cboLanguageType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLanguageType"));
        rdoLanguageRead.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLanguageRead"));
        rdoLanguageWrite.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLanguageWrite"));
        //        rdoLanguageReadeSpeak.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLanguageReadeSpeak"));
        cboReletaionShip.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReletaionShip"));
        cboReleationFHTitle.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReleationFHTitle"));
        txtReleationFHFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReleationFHFirstName"));
        txtReleationFHMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReleationFHMiddleName"));
        txtReleationFHLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReleationFHLastName"));
        tdtRelationShipDateofBirth.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtRelationShipDateofBirth"));
        cboFamilyMemEducation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFamilyMemEducation"));
        cboFamilyMemberProf.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFamilyMemberProf"));
        rdoDependentYes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDependentYes"));
        cboBloodGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBloodGroup"));
        txtMajorHealthProbeem.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMajorHealthProbeem"));
        txtPhysicalyHandicap.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhysicalyHandicap"));
        cboDomicileState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDomicileState"));
        txtDrivingLicenceNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrivingLicenceNo"));
        txtEmailId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmailId"));
        txtDepIncomePerannum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepIncomePerannum"));
        txtReleativeStaffId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReleativeStaffId"));
        txtRelativesWorkingFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRelativesWorkingFirstName"));
        txtRelativesWorkingMiddleName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRelativesWorkingMiddleName"));
        txtRelativesWorkingLastName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRelativesWorkingLastName"));
        cboWorkingDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWorkingDesignation"));
        cboRelativesWorkingBranch.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRelativesWorkingBranch"));
        cboRelativesWorkingReletionShip.setHelpMessage(lblMsg,objMandatoryRB.getString("cboRelativesWorkingReletionShip"));
        txtDirectorFirstName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDirectorFirstName"));
        txtDirectorMiddleName.setHelpMessage(lblMsg,objMandatoryRB.getString("txtDirectorMiddleName"));
        txtDirectorLastName.setHelpMessage(lblMsg,objMandatoryRB.getString("txtDirectorLastName"));
        cboDirectorReleationShip.setHelpMessage(lblMsg,objMandatoryRB.getString("cboDirectorReleationShip"));
        //   cboEmployeedWith.setHelpMessage(lblMsg,objMandatoryRB.getString("cboEmployeedWith"));
        
        //educational details
        
        cboEmpLevelEducation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEmpLevelEducation"));
        cboGrade.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGrade"));
        txtTotMarksPer.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotMarksPer"));
        tdtDateOfPassing.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfPassing"));
        cboSpecilization.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSpecilization"));
        txtMarksScored.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMarksScored"));
        txtUniversity.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUniversity"));
        txtTotMarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotMarks"));
        txtNameOfSchool.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNameOfSchool"));
        cboLanguageType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLanguageType"));
        rdoLanguageRead.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLanguageRead"));
        cboTechnicalQualificationType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTechnicalQualificationType"));
        cboTechnicalQualificationGrade.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTechnicalQualificationGrade"));
        txtTechnicalQualificationTotMarksPer.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationTotMarksPer"));
        tdtTechnicalQualificationDateOfPassing.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTechnicalQualificationDateOfPassing"));
        cboTechnicalQualificationSpecilization.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTechnicalQualificationSpecilization"));
        txtTechnicalQualificationUniversity.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationUniversity"));
        txtTechnicalQualificationMarksScored.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationMarksScored"));
        txtTechnicalQualificationTotMarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTechnicalQualificationTotMarks"));
        txtNameOfTechInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNameOfTechInst"));
        
        //loan details
        cboEmployeeLoanType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEmployeeLoanType"));
        cboLoanAvailedBranch.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLoanAvailedBranch"));
        txtLoanSanctionRefNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanSanctionRefNo"));
        tdtLoanSanctionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLoanSanctionDate"));
        txtLoanNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanNo"));
        txtLoanAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanAmount"));
        txtLoanRateofInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanRateofInterest"));
        txtLoanInstallmentAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanInstallmentAmount"));
        txtLoanNoOfInstallments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanNoOfInstallments"));
        tdtLoanRepaymentStartDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLoanRepaymentStartDate"));
        tdtLoanCloserDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLoanCloserDate"));
        rdoLoanPreCloserYes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLoanPreCloserYes"));
        tdtLoanRepaymentEndDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLoanRepaymentEndDate"));
        txtLoanRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanRemarks"));
        txtAccountNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNo"));
        cboAccountType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAccountType"));
        cboSalaryCrBranch.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSalaryCrBranch"));
        
        //personal details
        rdoGradutionIncrementYes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoGradutionIncrementYes"));
        rdoCAIIBPART1IncrementYes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCAIIBPART1IncrementYes"));
        tdtGradutionIncrementReleasedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtGradutionIncrementReleasedDate"));
        tdtCAIIBPART1IncrementReleasedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCAIIBPART1IncrementReleasedDate"));
        rdoCAIIBPART2Increment_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCAIIBPART2Increment_Yes"));
        tdtCAIIBPART2IncrementReleasedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCAIIBPART2IncrementReleasedDate"));
        rdoAnyOtherIncrement_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAnyOtherIncrement_Yes"));
        txtAnyOtherIncrementInstitutionName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAnyOtherIncrementInstitutionName"));
        tdtAnyOtherIncrementReleasedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAnyOtherIncrementReleasedDate"));
        tdtLastIncrmentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastIncrmentDate"));
        txtLossPay_Months.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLossPay_Months"));
        txtLossOfpay_Days.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLossOfpay_Days"));
        tdtNextIncrmentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNextIncrmentDate"));
        txtPresentBasic.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPresentBasic"));
        rdoSignature_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSignature_Yes"));
        txtSignatureNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSignatureNo"));
        txtSocietyMemberNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSocietyMemberNo"));
        cboSocietyMember.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSocietyMember"));
        cboUnionMember.setHelpMessage(lblMsg, objMandatoryRB.getString("cboUnionMember"));
        txtClubName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClubName"));
        rdoClubMembership_YES.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoClubMembership_YES"));
        cboZonalOffice.setHelpMessage(lblMsg, objMandatoryRB.getString("cboZonalOffice"));
        cboPresentBranchId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPresentBranchId"));
        tdtWorkingSince.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtWorkingSince"));
        cboReginoalOffice.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReginoalOffice"));
        cboPresentGrade.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPresentGrade"));
        cboDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDesignation"));
        cboPromotedDesignation.setHelpMessage(lblMsg,objMandatoryRB.getString("cboPromotedDesignation"));
        txtProbationPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProbationPeriod"));
        cboProbationPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProbationPeriod"));
        tdtConfirmationDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtConfirmationDate"));
        tdtDateofRetirement.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateofRetirement"));
        tdtDateOfJoin.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfJoin"));
        txtPFNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPFNumber"));
        cboPFAcNominee.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPFAcNominee"));
        
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
        btnReject.setEnabled(!btnReject.isEnabled());
        btnException.setEnabled(!btnException.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnDeletedDetails.setEnabled(!btnDeletedDetails.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnClose.setEnabled(true);
    }
    
    private void colWidthChange(){
        
    }
    
    public static void main(java.lang.String[] args) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        EmployeeMasterUI employeeMasterUI = new EmployeeMasterUI();
        frm.getContentPane().add(employeeMasterUI);
        employeeMasterUI.show();
        frm.setSize(800, 650);
        frm.show();
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
    public void updateIncomeFields(){
        
    }
    
    /**
     * Getter for property updateMode.
     * @return Value of property updateMode.
     */
    public boolean isUpdateMode() {
        return updateMode;
    }
    
    /**
     * Setter for property updateMode.
     * @param updateMode New value of property updateMode.
     */
    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }
    
    /**
     * Getter for property LandDetupdateMode.
     * @return Value of property LandDetupdateMode.
     */
    
    
    /**
     * Setter for property LandDetupdateMode.
     * @param LandDetupdateMode New value of property LandDetupdateMode.
     */
    
    
    //      public void incPar(){
    //          txtIncIncome.setText("");
    //          txtIncName.setText("");
    //          cboIncPack.set
    //      }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcademicEducationDetDel;
    private com.see.truetransact.uicomponent.CButton btnAcademicEducationDetNew;
    private com.see.truetransact.uicomponent.CButton btnAcademicEducationDetSave;
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
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
    private com.see.truetransact.uicomponent.CButton btnLanguageDetDel;
    private com.see.truetransact.uicomponent.CButton btnLanguageDetNew;
    private com.see.truetransact.uicomponent.CButton btnLanguageDetSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOprativeDetailsDetDel;
    private com.see.truetransact.uicomponent.CButton btnOprativeDetailsDetNew;
    private com.see.truetransact.uicomponent.CButton btnOprativeDetailsDetSave;
    private com.see.truetransact.uicomponent.CButton btnPhoneDelete;
    private com.see.truetransact.uicomponent.CButton btnPhoneNew;
    private com.see.truetransact.uicomponent.CButton btnPhotoLoad;
    private com.see.truetransact.uicomponent.CButton btnPhotoRemove;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPromotionDelete;
    private com.see.truetransact.uicomponent.CButton btnPromotionNew;
    private com.see.truetransact.uicomponent.CButton btnPromotionSave;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRelativeDirectorDetDel;
    private com.see.truetransact.uicomponent.CButton btnRelativeDirectorDetNew;
    private com.see.truetransact.uicomponent.CButton btnRelativeDirectorDetSave;
    private com.see.truetransact.uicomponent.CButton btnRelativeWorkingBramchDetDel;
    private com.see.truetransact.uicomponent.CButton btnRelativeWorkingBramchDetNew;
    private com.see.truetransact.uicomponent.CButton btnRelativeWorkingBramchDetSave;
    private com.see.truetransact.uicomponent.CButton btnReleativeStaffId;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSignLoad;
    private com.see.truetransact.uicomponent.CButton btnSignRemove;
    private com.see.truetransact.uicomponent.CButton btnTechnicalQualificationDetDel;
    private com.see.truetransact.uicomponent.CButton btnTechnicalQualificationDetNew;
    private com.see.truetransact.uicomponent.CButton btnTechnicalQualificationDetSave;
    private com.see.truetransact.uicomponent.CButton btnTermLoansDetDel;
    private com.see.truetransact.uicomponent.CButton btnTermLoansDetNew;
    private com.see.truetransact.uicomponent.CButton btnTermLoansDetSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnfamiyDetDel;
    private com.see.truetransact.uicomponent.CButton btnfamiyDetNew;
    private com.see.truetransact.uicomponent.CButton btnfamiyDetSave;
    private com.see.truetransact.uicomponent.CComboBox cboAccountType;
    private com.see.truetransact.uicomponent.CComboBox cboAddressType;
    private com.see.truetransact.uicomponent.CComboBox cboBloodGroup;
    private com.see.truetransact.uicomponent.CComboBox cboCaste;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboDesignation;
    private com.see.truetransact.uicomponent.CComboBox cboDirectorReleationShip;
    private com.see.truetransact.uicomponent.CComboBox cboDirectorTittle;
    private com.see.truetransact.uicomponent.CComboBox cboDomicileState;
    private com.see.truetransact.uicomponent.CComboBox cboEmpLevelEducation;
    private com.see.truetransact.uicomponent.CComboBox cboEmployeeLoanType;
    private com.see.truetransact.uicomponent.CComboBox cboFamilyMemEducation;
    private com.see.truetransact.uicomponent.CComboBox cboFamilyMemberProf;
    private com.see.truetransact.uicomponent.CComboBox cboFsatherTitle;
    private com.see.truetransact.uicomponent.CComboBox cboGrade;
    private com.see.truetransact.uicomponent.CComboBox cboLanguageType;
    private com.see.truetransact.uicomponent.CComboBox cboLoanAvailedBranch;
    private com.see.truetransact.uicomponent.CComboBox cboMotherTitle;
    private com.see.truetransact.uicomponent.CComboBox cboPFAcNominee;
    private com.see.truetransact.uicomponent.CComboBox cboPassportIssuePlace;
    private com.see.truetransact.uicomponent.CComboBox cboPassportTitle;
    private com.see.truetransact.uicomponent.CComboBox cboPhoneType;
    private com.see.truetransact.uicomponent.CComboBox cboPresentBranchId;
    private com.see.truetransact.uicomponent.CComboBox cboPresentGrade;
    private com.see.truetransact.uicomponent.CComboBox cboProbationPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboPromotedDesignation;
    private com.see.truetransact.uicomponent.CComboBox cboPromotionGrade;
    private com.see.truetransact.uicomponent.CComboBox cboReginoalOffice;
    private com.see.truetransact.uicomponent.CComboBox cboRelativesWorkingBranch;
    private com.see.truetransact.uicomponent.CComboBox cboRelativesWorkingReletionShip;
    private com.see.truetransact.uicomponent.CComboBox cboRelativesWorkingTittle;
    private com.see.truetransact.uicomponent.CComboBox cboReleationFHTitle;
    private com.see.truetransact.uicomponent.CComboBox cboReletaionShip;
    private com.see.truetransact.uicomponent.CComboBox cboReligion;
    private com.see.truetransact.uicomponent.CComboBox cboSalaryCrBranch;
    private com.see.truetransact.uicomponent.CComboBox cboSocietyMember;
    private com.see.truetransact.uicomponent.CComboBox cboSpecilization;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CComboBox cboTechnicalQualificationGrade;
    private com.see.truetransact.uicomponent.CComboBox cboTechnicalQualificationSpecilization;
    private com.see.truetransact.uicomponent.CComboBox cboTechnicalQualificationType;
    private com.see.truetransact.uicomponent.CComboBox cboTitle;
    private com.see.truetransact.uicomponent.CComboBox cboUnionMember;
    private com.see.truetransact.uicomponent.CComboBox cboWorkingDesignation;
    private com.see.truetransact.uicomponent.CComboBox cboZonalOffice;
    private com.see.truetransact.uicomponent.CLabel lbReginoalOffice;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountType;
    private com.see.truetransact.uicomponent.CLabel lblAddrRemarks;
    private com.see.truetransact.uicomponent.CLabel lblAddressType;
    private com.see.truetransact.uicomponent.CLabel lblAnyLossOfPay;
    private com.see.truetransact.uicomponent.CLabel lblAnyOtherIncrementInstitutionName;
    private com.see.truetransact.uicomponent.CLabel lblAnyOtherIncrementReleasedDate;
    private com.see.truetransact.uicomponent.CLabel lblAnyOtherIncrementYesNo;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaCode;
    private com.see.truetransact.uicomponent.CLabel lblBloodGroup;
    private com.see.truetransact.uicomponent.CLabel lblCAIIBPART1IncrementReleasedDate;
    private com.see.truetransact.uicomponent.CLabel lblCAIIBPART1IncrementYesNo;
    private com.see.truetransact.uicomponent.CLabel lblCAIIBPART2IncrementReleasedDate;
    private com.see.truetransact.uicomponent.CLabel lblCAIIBPART2IncrementYesNo;
    private com.see.truetransact.uicomponent.CLabel lblCaste;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblClubMembership;
    private com.see.truetransact.uicomponent.CLabel lblClubName;
    private com.see.truetransact.uicomponent.CLabel lblConfirmationDate;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblDLRenewalDate;
    private com.see.truetransact.uicomponent.CLabel lblDateOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblDateOfJoin;
    private com.see.truetransact.uicomponent.CLabel lblDateOfPassing;
    private com.see.truetransact.uicomponent.CLabel lblDateofRetirement;
    private com.see.truetransact.uicomponent.CLabel lblDealingPeriod1;
    private com.see.truetransact.uicomponent.CLabel lblDepIncomePerannum;
    private com.see.truetransact.uicomponent.CLabel lblDependent;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblDirectorFirstName;
    private com.see.truetransact.uicomponent.CLabel lblDirectorLastName;
    private com.see.truetransact.uicomponent.CLabel lblDirectorMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblDirectorReleationShip;
    private com.see.truetransact.uicomponent.CLabel lblDomicileState;
    private com.see.truetransact.uicomponent.CLabel lblDrivingLicenceNo;
    private com.see.truetransact.uicomponent.CLabel lblEmailId;
    private com.see.truetransact.uicomponent.CLabel lblEmpID;
    private com.see.truetransact.uicomponent.CLabel lblEmpLevelEducation;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeLoanType;
    private com.see.truetransact.uicomponent.CLabel lblEmployeedWith;
    private com.see.truetransact.uicomponent.CLabel lblFamilyMemEducation;
    private com.see.truetransact.uicomponent.CLabel lblFamilyMemberProf;
    private com.see.truetransact.uicomponent.CLabel lblFatherFirstName;
    private com.see.truetransact.uicomponent.CLabel lblFatherLastName;
    private com.see.truetransact.uicomponent.CLabel lblFatherMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblFirstName;
    private com.see.truetransact.uicomponent.CLabel lblGender;
    private com.see.truetransact.uicomponent.CLabel lblGrade;
    private com.see.truetransact.uicomponent.CLabel lblGradutionIncrementReleasedDate;
    private com.see.truetransact.uicomponent.CLabel lblGradutionIncrementYesNo;
    private com.see.truetransact.uicomponent.CLabel lblHomeTown;
    private com.see.truetransact.uicomponent.CLabel lblIDCardNoNo;
    private com.see.truetransact.uicomponent.CLabel lblLanguageType;
    private com.see.truetransact.uicomponent.CLabel lblLastIncrmentDate;
    private com.see.truetransact.uicomponent.CLabel lblLastName;
    private com.see.truetransact.uicomponent.CLabel lblLiableForTransfer;
    private com.see.truetransact.uicomponent.CLabel lblLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblLoanAvailedBranch;
    private com.see.truetransact.uicomponent.CLabel lblLoanCloserDate;
    private com.see.truetransact.uicomponent.CLabel lblLoanInstallmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblLoanNo;
    private com.see.truetransact.uicomponent.CLabel lblLoanNoOfInstallments;
    private com.see.truetransact.uicomponent.CLabel lblLoanPreCloserYesNo;
    private com.see.truetransact.uicomponent.CLabel lblLoanRateofInterest;
    private com.see.truetransact.uicomponent.CLabel lblLoanRemarks;
    private com.see.truetransact.uicomponent.CLabel lblLoanRepaymentEndDate;
    private com.see.truetransact.uicomponent.CLabel lblLoanRepaymentStartDate;
    private com.see.truetransact.uicomponent.CLabel lblLoanSanctionDate;
    private com.see.truetransact.uicomponent.CLabel lblLoanSanctionRefNo;
    private com.see.truetransact.uicomponent.CLabel lblLossOfpay_Days;
    private com.see.truetransact.uicomponent.CLabel lblLossPay_Months;
    private com.see.truetransact.uicomponent.CLabel lblMajorHealthProbeem;
    private com.see.truetransact.uicomponent.CLabel lblMaritalStatus;
    private com.see.truetransact.uicomponent.CLabel lblMarksScored;
    private com.see.truetransact.uicomponent.CLabel lblMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblMotheFirstName;
    private com.see.truetransact.uicomponent.CLabel lblMotheMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblMotherLastName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNameOfSchool;
    private com.see.truetransact.uicomponent.CLabel lblNameOfTechInst;
    private com.see.truetransact.uicomponent.CLabel lblNewBasic;
    private com.see.truetransact.uicomponent.CLabel lblNextIncrmentDate;
    private com.see.truetransact.uicomponent.CLabel lblPFAcNominee;
    private com.see.truetransact.uicomponent.CLabel lblPFNumber;
    private com.see.truetransact.uicomponent.CLabel lblPanNumber;
    private com.see.truetransact.uicomponent.CLabel lblPassportFirstName;
    private com.see.truetransact.uicomponent.CLabel lblPassportIssueAuth;
    private com.see.truetransact.uicomponent.CLabel lblPassportIssueDt;
    private com.see.truetransact.uicomponent.CLabel lblPassportIssuePlace;
    private com.see.truetransact.uicomponent.CLabel lblPassportLastName;
    private com.see.truetransact.uicomponent.CLabel lblPassportMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblPassportNo;
    private com.see.truetransact.uicomponent.CLabel lblPassportValidUpto;
    private com.see.truetransact.uicomponent.CLabel lblPhoneNumber;
    private com.see.truetransact.uicomponent.CLabel lblPhoneType;
    private com.see.truetransact.uicomponent.CLabel lblPhoto;
    private com.see.truetransact.uicomponent.CLabel lblPhysicalHandicap;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblPlaceOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblPresentBasic;
    private com.see.truetransact.uicomponent.CLabel lblPresentBranchId;
    private com.see.truetransact.uicomponent.CLabel lblPresentDesignation;
    private com.see.truetransact.uicomponent.CLabel lblPresentGrade;
    private com.see.truetransact.uicomponent.CLabel lblProbationPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPromotedDesignation;
    private com.see.truetransact.uicomponent.CLabel lblPromotionBasicPay;
    private com.see.truetransact.uicomponent.CLabel lblPromotionCreatedDate;
    private com.see.truetransact.uicomponent.CLabel lblPromotionEffectiveDate;
    private com.see.truetransact.uicomponent.CLabel lblPromotionEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblPromotionEmployeeName;
    private com.see.truetransact.uicomponent.CLabel lblPromotionGrade;
    private com.see.truetransact.uicomponent.CLabel lblPromotionLastGrade;
    private com.see.truetransact.uicomponent.CLabel lblPromotionSalId;
    private com.see.truetransact.uicomponent.CLabel lblRelationShipDateofBirth;
    private com.see.truetransact.uicomponent.CLabel lblRelativesWorkingBranch;
    private com.see.truetransact.uicomponent.CLabel lblRelativesWorkingFirstName;
    private com.see.truetransact.uicomponent.CLabel lblRelativesWorkingLastName;
    private com.see.truetransact.uicomponent.CLabel lblRelativesWorkingMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblRelativesWorkingReletionShip;
    private com.see.truetransact.uicomponent.CLabel lblReleationFHFirstName;
    private com.see.truetransact.uicomponent.CLabel lblReleationFHLastName;
    private com.see.truetransact.uicomponent.CLabel lblReleationFHMiddleName;
    private com.see.truetransact.uicomponent.CLabel lblReleativeStaffId;
    private com.see.truetransact.uicomponent.CLabel lblReletaionShip;
    private com.see.truetransact.uicomponent.CLabel lblReligion;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSalaryCrBranch;
    private com.see.truetransact.uicomponent.CLabel lblSigNoYesNo;
    private com.see.truetransact.uicomponent.CLabel lblSign;
    private com.see.truetransact.uicomponent.CLabel lblSignatureNo;
    private com.see.truetransact.uicomponent.CLabel lblSocietyMember;
    private com.see.truetransact.uicomponent.CLabel lblSocietyMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpecilization;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationDateOfPassing;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationGrade;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationMarksScored;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationSpecilization;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationTotMarks;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationTotMarksPer;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationType;
    private com.see.truetransact.uicomponent.CLabel lblTechnicalQualificationUniversity;
    private com.see.truetransact.uicomponent.CLabel lblTotMarks;
    private com.see.truetransact.uicomponent.CLabel lblTotMarksPer;
    private com.see.truetransact.uicomponent.CLabel lblUIDNo;
    private com.see.truetransact.uicomponent.CLabel lblUnionMember;
    private com.see.truetransact.uicomponent.CLabel lblUniversity;
    private com.see.truetransact.uicomponent.CLabel lblWorkingDesignation;
    private com.see.truetransact.uicomponent.CLabel lblWorkingSince;
    private com.see.truetransact.uicomponent.CLabel lblZonalOffice;
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
    private com.see.truetransact.uicomponent.CPanel panAcademicEducation;
    private com.see.truetransact.uicomponent.CPanel panAcademicEducation2;
    private com.see.truetransact.uicomponent.CPanel panAcademicEducationDateEntery;
    private com.see.truetransact.uicomponent.CPanel panAcademicEducationTable;
    private com.see.truetransact.uicomponent.CPanel panAdditionalInfo;
    private com.see.truetransact.uicomponent.CPanel panAdditionalInfo2;
    private com.see.truetransact.uicomponent.CPanel panAddrRemarks;
    private com.see.truetransact.uicomponent.CPanel panAddress;
    private com.see.truetransact.uicomponent.CPanel panAddressDetails;
    private com.see.truetransact.uicomponent.CPanel panAddressType;
    private com.see.truetransact.uicomponent.CPanel panAnyOtherIncrement;
    private com.see.truetransact.uicomponent.CPanel panBranch;
    private com.see.truetransact.uicomponent.CPanel panBtnAcademicEducation;
    private com.see.truetransact.uicomponent.CPanel panBtnFamily;
    private com.see.truetransact.uicomponent.CPanel panBtnLangues1;
    private com.see.truetransact.uicomponent.CPanel panBtnOprativeDetails;
    private com.see.truetransact.uicomponent.CPanel panBtnRelativeDirector;
    private com.see.truetransact.uicomponent.CPanel panBtnRelativeWorkingBramch;
    private com.see.truetransact.uicomponent.CPanel panBtnTechnicalQualification2;
    private com.see.truetransact.uicomponent.CPanel panBtnTermLoans;
    private com.see.truetransact.uicomponent.CPanel panCAIIBPART;
    private com.see.truetransact.uicomponent.CPanel panCAIIBPART2;
    private com.see.truetransact.uicomponent.CPanel panCity;
    private com.see.truetransact.uicomponent.CPanel panClubDet;
    private com.see.truetransact.uicomponent.CPanel panContactControl;
    private com.see.truetransact.uicomponent.CPanel panContactInfo;
    private com.see.truetransact.uicomponent.CPanel panContactNo;
    private com.see.truetransact.uicomponent.CPanel panContacts1;
    private com.see.truetransact.uicomponent.CPanel panCountry;
    private com.see.truetransact.uicomponent.CPanel panCountryDetails;
    private com.see.truetransact.uicomponent.CPanel panCustId1;
    private com.see.truetransact.uicomponent.CPanel panCustomer;
    private com.see.truetransact.uicomponent.CPanel panCustomerHistory;
    private com.see.truetransact.uicomponent.CPanel panCustomerHistory1;
    private com.see.truetransact.uicomponent.CPanel panCustomerInfo1;
    private com.see.truetransact.uicomponent.CPanel panDOB2;
    private com.see.truetransact.uicomponent.CPanel panDependentTable;
    private com.see.truetransact.uicomponent.CPanel panDetailsOfRelativeDirector;
    private com.see.truetransact.uicomponent.CPanel panDirectorReleative;
    private com.see.truetransact.uicomponent.CPanel panEmployeeDateEntery;
    private com.see.truetransact.uicomponent.CPanel panEmployeeLoan;
    private com.see.truetransact.uicomponent.CPanel panEmployeeLoanTable;
    private com.see.truetransact.uicomponent.CPanel panFamilyDateEntery;
    private com.see.truetransact.uicomponent.CPanel panFamilyDetails;
    private com.see.truetransact.uicomponent.CPanel panFirstName;
    private com.see.truetransact.uicomponent.CPanel panFirstName1;
    private com.see.truetransact.uicomponent.CPanel panFirstName2;
    private com.see.truetransact.uicomponent.CPanel panFirstName3;
    private com.see.truetransact.uicomponent.CPanel panFirstName4;
    private com.see.truetransact.uicomponent.CPanel panFirstName5;
    private com.see.truetransact.uicomponent.CPanel panGender;
    private com.see.truetransact.uicomponent.CPanel panGradutionIncrement;
    private com.see.truetransact.uicomponent.CPanel panHusband_father;
    private com.see.truetransact.uicomponent.CPanel panIncomeParticulars;
    private com.see.truetransact.uicomponent.CPanel panJoiningDetails;
    private com.see.truetransact.uicomponent.CPanel panLandDetails;
    private com.see.truetransact.uicomponent.CPanel panLanguageDate;
    private com.see.truetransact.uicomponent.CPanel panLanguageStatus;
    private com.see.truetransact.uicomponent.CPanel panLanguageStatus1;
    private com.see.truetransact.uicomponent.CPanel panLanguageTable;
    private com.see.truetransact.uicomponent.CPanel panLanguageTable1;
    private com.see.truetransact.uicomponent.CPanel panLangues;
    private com.see.truetransact.uicomponent.CPanel panLoanPreCloserYesNo;
    private com.see.truetransact.uicomponent.CPanel panLossOfpay;
    private com.see.truetransact.uicomponent.CPanel panMISKYC;
    private com.see.truetransact.uicomponent.CPanel panMaritalStatus;
    private com.see.truetransact.uicomponent.CPanel panMaritalStatus1;
    private com.see.truetransact.uicomponent.CPanel panMaritalStatus2;
    private com.see.truetransact.uicomponent.CPanel panMinDepositPeriod1;
    private com.see.truetransact.uicomponent.CPanel panOfficeDetails;
    private com.see.truetransact.uicomponent.CPanel panOprativeDetails;
    private com.see.truetransact.uicomponent.CPanel panPassPortDet;
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
    private com.see.truetransact.uicomponent.CPanel panPhysicalHandicap;
    private com.see.truetransact.uicomponent.CPanel panPresent;
    private com.see.truetransact.uicomponent.CPanel panPresentDetails;
    private com.see.truetransact.uicomponent.CPanel panPresentWorkingDetails;
    private com.see.truetransact.uicomponent.CPanel panPromotionButtons;
    private com.see.truetransact.uicomponent.CPanel panPromotionDetails;
    private com.see.truetransact.uicomponent.CPanel panPromotionDetailsinfo;
    private com.see.truetransact.uicomponent.CPanel panPromotionInfo;
    private com.see.truetransact.uicomponent.CPanel panPromotionTable;
    private com.see.truetransact.uicomponent.CPanel panRelativesWorkingDetails;
    private com.see.truetransact.uicomponent.CPanel panRelativesWorkingDetailsEntery;
    private com.see.truetransact.uicomponent.CPanel panSign;
    private com.see.truetransact.uicomponent.CPanel panSignButtons;
    private com.see.truetransact.uicomponent.CPanel panSignatureNo1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTechnicalQualificationDateEntery;
    private com.see.truetransact.uicomponent.CPanel panTechnicalQualificationTable;
    private com.see.truetransact.uicomponent.CPanel panTechnicalQualificationTable1;
    private com.see.truetransact.uicomponent.CPanel panTeleCommunication;
    private com.see.truetransact.uicomponent.CPanel panTelecomDetails;
    private com.see.truetransact.uicomponent.CPanel panTrainingDateEntery1;
    private com.see.truetransact.uicomponent.CPanel panTrainingTable;
    private com.see.truetransact.uicomponent.CPanel panparticulars;
    private com.see.truetransact.uicomponent.CRadioButton rdoAnyOtherIncrement_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAnyOtherIncrement_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAnyotherIncrement;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCAIIBPART1Increment;
    private com.see.truetransact.uicomponent.CRadioButton rdoCAIIBPART1IncrementNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoCAIIBPART1IncrementYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCAIIBPART2Increment;
    private com.see.truetransact.uicomponent.CRadioButton rdoCAIIBPART2Increment_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCAIIBPART2Increment_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoClubMembership;
    private com.see.truetransact.uicomponent.CRadioButton rdoClubMembership_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoClubMembership_YES;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDependent;
    private com.see.truetransact.uicomponent.CRadioButton rdoDependentNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoDependentYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoFather;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGender;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Female;
    private com.see.truetransact.uicomponent.CRadioButton rdoGender_Male;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGradutionIncrement;
    private com.see.truetransact.uicomponent.CRadioButton rdoGradutionIncrementNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoGradutionIncrementYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoHusband;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLanguage;
    private com.see.truetransact.uicomponent.CRadioButton rdoLanguageRead;
    private com.see.truetransact.uicomponent.CRadioButton rdoLanguageReadeSpeak;
    private com.see.truetransact.uicomponent.CRadioButton rdoLanguageWrite;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLiableForTransfer;
    private com.see.truetransact.uicomponent.CRadioButton rdoLiableForTransferNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoLiableForTransferYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLoanPreCloser;
    private com.see.truetransact.uicomponent.CRadioButton rdoLoanPreCloserNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoLoanPreCloserYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMaritalStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaritalStatus_Married;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaritalStatus_Single;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSignature;
    private com.see.truetransact.uicomponent.CRadioButton rdoSignature_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSignature_Yes;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpAcademicEducation;
    private com.see.truetransact.uicomponent.CScrollPane srpContactList;
    private com.see.truetransact.uicomponent.CScrollPane srpDetailsOfRelativeDirector;
    private com.see.truetransact.uicomponent.CScrollPane srpEmployeeLoan;
    private com.see.truetransact.uicomponent.CScrollPane srpFamily;
    private com.see.truetransact.uicomponent.CScrollPane srpLanguage;
    private com.see.truetransact.uicomponent.CScrollPane srpOprative;
    private com.see.truetransact.uicomponent.CScrollPane srpPhoneList;
    private com.see.truetransact.uicomponent.CScrollPane srpPhotoLoad;
    private com.see.truetransact.uicomponent.CScrollPane srpPromotion;
    private com.see.truetransact.uicomponent.CScrollPane srpRelativesWorkingDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpSignLoad;
    private com.see.truetransact.uicomponent.CScrollPane srpTechnicalQualification;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaHandicap;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTabbedPane tabIndCust;
    private com.see.truetransact.uicomponent.CTable tblAcademicEducation;
    private com.see.truetransact.uicomponent.CTable tblContactList;
    private com.see.truetransact.uicomponent.CTable tblDetailsOfRelativeDirector;
    private com.see.truetransact.uicomponent.CTable tblEmployeeLoan;
    private com.see.truetransact.uicomponent.CTable tblFamily;
    private com.see.truetransact.uicomponent.CTable tblLanguage;
    private com.see.truetransact.uicomponent.CTable tblOprative;
    private com.see.truetransact.uicomponent.CTable tblPhoneList;
    private com.see.truetransact.uicomponent.CTable tblPromotion;
    private com.see.truetransact.uicomponent.CTable tblReleativeWorkingInBank;
    private com.see.truetransact.uicomponent.CTable tblTechnicalEducation;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtAnyOtherIncrementReleasedDate;
    private com.see.truetransact.uicomponent.CDateField tdtCAIIBPART1IncrementReleasedDate;
    private com.see.truetransact.uicomponent.CDateField tdtCAIIBPART2IncrementReleasedDate;
    private com.see.truetransact.uicomponent.CDateField tdtConfirmationDate;
    private com.see.truetransact.uicomponent.CDateField tdtDLRenewalDate;
    private com.see.truetransact.uicomponent.CDateField tdtDateOfBirth;
    private com.see.truetransact.uicomponent.CDateField tdtDateOfJoin;
    private com.see.truetransact.uicomponent.CDateField tdtDateOfPassing;
    private com.see.truetransact.uicomponent.CDateField tdtDateofRetirement;
    private com.see.truetransact.uicomponent.CDateField tdtGradutionIncrementReleasedDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastIncrmentDate;
    private com.see.truetransact.uicomponent.CDateField tdtLoanCloserDate;
    private com.see.truetransact.uicomponent.CDateField tdtLoanRepaymentEndDate;
    private com.see.truetransact.uicomponent.CDateField tdtLoanRepaymentStartDate;
    private com.see.truetransact.uicomponent.CDateField tdtLoanSanctionDate;
    private com.see.truetransact.uicomponent.CDateField tdtNextIncrmentDate;
    private com.see.truetransact.uicomponent.CDateField tdtPassportIssueDt;
    private com.see.truetransact.uicomponent.CDateField tdtPassportValidUpto;
    private com.see.truetransact.uicomponent.CDateField tdtPromotionCreatedDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtPromotionEffectiveDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtRelationShipDateofBirth;
    private com.see.truetransact.uicomponent.CDateField tdtTechnicalQualificationDateOfPassing;
    private com.see.truetransact.uicomponent.CDateField tdtWorkingSince;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtAddrRemarks;
    private com.see.truetransact.uicomponent.CTextField txtAnyOtherIncrementInstitutionName;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtAreaCode;
    private com.see.truetransact.uicomponent.CTextField txtClubName;
    private com.see.truetransact.uicomponent.CTextField txtDepIncomePerannum;
    private com.see.truetransact.uicomponent.CTextField txtDirectorFirstName;
    private com.see.truetransact.uicomponent.CTextField txtDirectorLastName;
    private com.see.truetransact.uicomponent.CTextField txtDirectorMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtDrivingLicenceNo;
    private com.see.truetransact.uicomponent.CTextField txtEmailId;
    private com.see.truetransact.uicomponent.CTextField txtEmpID;
    private com.see.truetransact.uicomponent.CTextField txtEmpWith;
    private com.see.truetransact.uicomponent.CTextField txtFatherFirstName;
    private com.see.truetransact.uicomponent.CTextField txtFatherLastName;
    private com.see.truetransact.uicomponent.CTextField txtFatherMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtFirstName;
    private com.see.truetransact.uicomponent.CTextField txtHomeTown;
    private com.see.truetransact.uicomponent.CTextField txtIDCardNoNo;
    private com.see.truetransact.uicomponent.CTextField txtLastName;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmount;
    private com.see.truetransact.uicomponent.CTextField txtLoanInstallmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtLoanNo;
    private com.see.truetransact.uicomponent.CTextField txtLoanNoOfInstallments;
    private com.see.truetransact.uicomponent.CTextField txtLoanRateofInterest;
    private com.see.truetransact.uicomponent.CTextField txtLoanRemarks;
    private com.see.truetransact.uicomponent.CTextField txtLoanSanctionRefNo;
    private com.see.truetransact.uicomponent.CTextField txtLossOfpay_Days;
    private com.see.truetransact.uicomponent.CTextField txtLossPay_Months;
    private com.see.truetransact.uicomponent.CTextArea txtMajorHealthProbeem;
    private com.see.truetransact.uicomponent.CTextField txtMarksScored;
    private com.see.truetransact.uicomponent.CTextField txtMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtMotherFirstName;
    private com.see.truetransact.uicomponent.CTextField txtMotherLastName;
    private com.see.truetransact.uicomponent.CTextField txtMotherMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtNameOfSchool;
    private com.see.truetransact.uicomponent.CTextField txtNameOfTechInst;
    private com.see.truetransact.uicomponent.CTextField txtNewBasic;
    private com.see.truetransact.uicomponent.CTextField txtPFNumber;
    private com.see.truetransact.uicomponent.CTextField txtPanNumber;
    private com.see.truetransact.uicomponent.CTextField txtPassportFirstName;
    private com.see.truetransact.uicomponent.CTextField txtPassportIssueAuth;
    private com.see.truetransact.uicomponent.CTextField txtPassportLastName;
    private com.see.truetransact.uicomponent.CTextField txtPassportMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtPassportNo;
    private com.see.truetransact.uicomponent.CTextField txtPhoneNumber;
    private com.see.truetransact.uicomponent.CTextArea txtPhysicalyHandicap;
    private com.see.truetransact.uicomponent.CTextField txtPincode;
    private com.see.truetransact.uicomponent.CTextField txtPlaceOfBirth;
    private com.see.truetransact.uicomponent.CTextField txtPresentBasic;
    private com.see.truetransact.uicomponent.CTextField txtProbationPeriod;
    private com.see.truetransact.uicomponent.CTextField txtPromotionBasicPayValue;
    private com.see.truetransact.uicomponent.CTextField txtPromotionEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtPromotionEmployeeName;
    private com.see.truetransact.uicomponent.CTextField txtPromotionLastDesg;
    private com.see.truetransact.uicomponent.CTextField txtPromotionLastGrade;
    private com.see.truetransact.uicomponent.CTextField txtPromotionSalId;
    private com.see.truetransact.uicomponent.CTextField txtRelativesWorkingFirstName;
    private com.see.truetransact.uicomponent.CTextField txtRelativesWorkingLastName;
    private com.see.truetransact.uicomponent.CTextField txtRelativesWorkingMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtReleationFHFirstName;
    private com.see.truetransact.uicomponent.CTextField txtReleationFHLastName;
    private com.see.truetransact.uicomponent.CTextField txtReleationFHMiddleName;
    private com.see.truetransact.uicomponent.CTextField txtReleativeStaffId;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSignatureNo;
    private com.see.truetransact.uicomponent.CTextField txtSocietyMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtStreet;
    private com.see.truetransact.uicomponent.CTextField txtTechnicalQualificationMarksScored;
    private com.see.truetransact.uicomponent.CTextField txtTechnicalQualificationTotMarks;
    private com.see.truetransact.uicomponent.CTextField txtTechnicalQualificationTotMarksPer;
    private com.see.truetransact.uicomponent.CTextField txtTechnicalQualificationUniversity;
    private com.see.truetransact.uicomponent.CTextField txtTotMarks;
    private com.see.truetransact.uicomponent.CTextField txtTotMarksPer;
    private com.see.truetransact.uicomponent.CTextField txtUIDNo;
    private com.see.truetransact.uicomponent.CTextField txtUniversity;
    // End of variables declaration//GEN-END:variables
}
