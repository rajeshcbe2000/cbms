/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeOB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.employee;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;
import com.see.truetransact.transferobject.payroll.employee.EmployeeAddrTO;
import com.see.truetransact.transferobject.payroll.employee.EmployeeDetailsTO;
import com.see.truetransact.transferobject.payroll.employee.SalaryStructTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author anjuanand
 */
public class EmployeeOB extends Observable {

    private int actionType;
    private int result;
    private String lblValBranchName = "";
    private String lblPhoto = "";
    private String photoFile = "";
    Date curDate = null;
    private byte[] photoByteArray;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static Logger log = Logger.getLogger(EmployeeUI.class);
    private static EmployeeOB employeeOB;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy = null;
    private String txtEmployeeId = "";
    private String txtCustomerId = "";
    private String txtBranchCode = "";
    private String txtTitle = "";
    private String txtFirstName = "";
    private String txtMiddleName = "";
    private String txtLastName = "";
    private boolean rdoGender_Male = false;
    private boolean rdoGender_Female = false;
    private String cboEmployeeType = "";
    private String txtMartialStatus = "";
    private String cboJobTitle = "";
    private String cboDepartment = "";
    private String cboManager = "";
    private String txtOfficialEmail = "";
    private String txtOfficePhone = "";
    private String txtStreet = "";
    private String txtArea = "";
    private String txtCity = "";
    private String cboAddressType = "";
    private String txtState = "";
    private String txtCountry = "";
    private String txtPinCode = "";
    private String tdtBirthDate = "";
    private String tdtJoiningDate = "";
    private String tdtLeavingDate = "";
    private String txtPanNo = "";
    private String txtSsnNo = "";
    private String txtPassPortNo = "";
    private String txaSkills = "";
    private String txaEducation = "";
    private String txaExperience = "";
    private String txaResponsibility = "";
    private String txaPerformance = "";
    private String txaComments = "";
    private String addrType = "";
    private String txtWfNo = "";
    private String txtWfOpeningBalance = "";
    private String txtDaApplicable = "";
    private String txtHraApplicable = "";
    private String txtStopPayt = "";
    private String txtNetSalaryProductType = "";
    private String txtNetSalaryProductId = "";
    private String txtNetSalaryAccountNo = "";
    private String txtAuthorizeStatus = "";
    private String txtPensionCodeNo = "";
    private String txtPensionOpeningBalance = "";
    private String txtIncrementCount = "";
    private String txtCustomerName = "";
    private String txtPresentBasicSalary = "";
    private String cboDesignation = "";
    private String txtScaleId = "";
    private String txtVersionNo = "";
    private String cboNetSalaryProductType = "";
    private String cboNetSalaryProductId = "";
    private String cboStatusOfEmp = "";
    private String cboBloodGroup = "";
    private String tdtEffectiveDate = null;
    private String tdtLastIncrementDate = null;
    private String tdtNextIncrementDate = null;
    private String tdtDateOfJoin = null;
    private String tdtPensionOpeningBalanceOn = null;
    private String tdtWfOpeningBalanceOn = null;
    private String tdtProbationEndDate = null;
    private String tdtDateOfRetirement = null;
    boolean daAppl = false;
    boolean hraAppl = false;
    boolean stopPayt = false;
    private int txtSortOrder = 0;
    private String txtFatherName = null;
    private String txtMotherName = null;
    private String txtSpouseName = null;
    private String txtSpouseRelation = null;
    private String txtPlaceofBirth = null;
    private String txtReligion = null;
    private String txtCaste = null;
    private String txaIdentificationMark1 = null;
    private String txaIdentificationMark2 = null;
    private String txaPhysicallyHandicapped = null;
    private String txaMajorHealthProblem = null;
    private String cboFatherTitle = "";
    private String cboMotherTitle = "";
    private String cboCommAddressType = "";
    private String cboBranchCode = "";
    private ComboBoxModel cbmEmployeeType;
    private ComboBoxModel cbmJobTitle;
    private ComboBoxModel cbmDepartment;
    private ComboBoxModel cbmManager;
    private ComboBoxModel cbmAddressType;
    private ComboBoxModel cbmDesignation;
    private ComboBoxModel cbmNetSalaryProductType;
    private ComboBoxModel cbmNetSalaryProductId;
    private ComboBoxModel cbmStatusOfEmp;
    private ComboBoxModel cbmFatherTitle;
    private ComboBoxModel cbmMotherTitle;
    private ComboBoxModel cbmCommAddressType;
    private ComboBoxModel cbmBloodGroup;
    private ComboBoxModel cbmBranchCode;
    private boolean authorized = false;

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public String getTxtVersionNo() {
        return txtVersionNo;
    }

    public void setTxtVersionNo(String txtVersionNo) {
        this.txtVersionNo = txtVersionNo;
    }

    public String getCboBloodGroup() {
        return cboBloodGroup;
    }

    public void setCboBloodGroup(String cboBloodGroup) {
        this.cboBloodGroup = cboBloodGroup;
        setChanged();
    }

    public String getTxtCaste() {
        return txtCaste;
    }

    public void setTxtCaste(String txtCaste) {
        this.txtCaste = txtCaste;
    }

    public String getCboCommAddressType() {
        return cboCommAddressType;
    }

    public void setCboCommAddressType(String cboCommAddressType) {
        this.cboCommAddressType = cboCommAddressType;
    }

    public String getCboFatherTitle() {
        return cboFatherTitle;
    }

    public void setCboFatherTitle(String cboFatherTitle) {
        this.cboFatherTitle = cboFatherTitle;
    }

    public String getCboMotherTitle() {
        return cboMotherTitle;
    }

    public void setCboMotherTitle(String cboMotherTitle) {
        this.cboMotherTitle = cboMotherTitle;
    }

    public String getTxtFatherName() {
        return txtFatherName;
    }

    public void setTxtFatherName(String txtFatherName) {
        this.txtFatherName = txtFatherName;
    }

    public String getTxtMotherName() {
        return txtMotherName;
    }

    public void setTxtMotherName(String txtMotherName) {
        this.txtMotherName = txtMotherName;
    }

    public String getTxtPlaceofBirth() {
        return txtPlaceofBirth;
    }

    public void setTxtPlaceofBirth(String txtPlaceofBirth) {
        this.txtPlaceofBirth = txtPlaceofBirth;
    }

    public String getTxtReligion() {
        return txtReligion;
    }

    public void setTxtReligion(String txtReligion) {
        this.txtReligion = txtReligion;
    }

    

    public String getTxtSpouseName() {
        return txtSpouseName;
    }

    public void setTxtSpouseName(String txtSpouseName) {
        this.txtSpouseName = txtSpouseName;
    }

    public int getTxtSortOrder() {
        return txtSortOrder;
    }

    public void setTxtSortOrder(int txtSortOrder) {
        this.txtSortOrder = txtSortOrder;
    }

    public String getTxtSpouseRelation() {
        return txtSpouseRelation;
    }

    public void setTxtSpouseRelation(String txtSpouseRelation) {
        this.txtSpouseRelation = txtSpouseRelation;
    }

    public String getTxaMajorHealthProblem() {
        return txaMajorHealthProblem;
    }

    public void setTxaMajorHealthProblem(String txaMajorHealthProblem) {
        this.txaMajorHealthProblem = txaMajorHealthProblem;
    }

    public String getTxaPhysicallyHandicapped() {
        return txaPhysicallyHandicapped;
    }

    public void setTxaPhysicallyHandicapped(String txaPhysicallyHandicapped) {
        this.txaPhysicallyHandicapped = txaPhysicallyHandicapped;
    }

    public String getTxaIdentificationMark1() {
        return txaIdentificationMark1;
    }

    public void setTxaIdentificationMark1(String txaIdentificationMark1) {
        this.txaIdentificationMark1 = txaIdentificationMark1;
    }

    public String getTxaIdentificationMark2() {
        return txaIdentificationMark2;
    }

    public void setTxaIdentificationMark2(String txaIdentificationMark2) {
        this.txaIdentificationMark2 = txaIdentificationMark2;
    }

    public String getTxtPensionOpeningBalance() {
        return txtPensionOpeningBalance;
    }

    public void setTxtPensionOpeningBalance(String txtPensionOpeningBalance) {
        this.txtPensionOpeningBalance = txtPensionOpeningBalance;
    }

    public String getTxtWfOpeningBalance() {
        return txtWfOpeningBalance;
    }

    public void setTxtWfOpeningBalance(String txtWfOpeningBalance) {
        this.txtWfOpeningBalance = txtWfOpeningBalance;
    }

    public String getTxtIncrementCount() {
        return txtIncrementCount;
    }

    public void setTxtIncrementCount(String txtIncrementCount) {
        this.txtIncrementCount = txtIncrementCount;
    }

    public String getTxtPresentBasicSalary() {
        return txtPresentBasicSalary;
    }

    public void setTxtPresentBasicSalary(String txtPresentBasicSalary) {
        this.txtPresentBasicSalary = txtPresentBasicSalary;
    }

    public boolean isDaAppl() {
        return daAppl;
    }

    public void setDaAppl(boolean daAppl) {
        this.daAppl = daAppl;
        setChanged();
    }

    public boolean isHraAppl() {
        return hraAppl;
    }

    public void setHraAppl(boolean hraAppl) {
        this.hraAppl = hraAppl;
        setChanged();
    }

    public boolean isStopPayt() {
        return stopPayt;
    }

    public void setStopPayt(boolean stopPayt) {
        this.stopPayt = stopPayt;
        setChanged();
    }

    public String getTxtScaleId() {
        return txtScaleId;
    }

    public void setTxtScaleId(String txtScaleId) {
        this.txtScaleId = txtScaleId;
    }

    public String getTdtDateOfJoin() {
        return tdtDateOfJoin;
    }

    public void setTdtDateOfJoin(String tdtDateOfJoin) {
        this.tdtDateOfJoin = tdtDateOfJoin;
        setChanged();
    }

    public String getTdtDateOfRetirement() {
        return tdtDateOfRetirement;
    }

    public void setTdtDateOfRetirement(String tdtDateOfRetirement) {
        this.tdtDateOfRetirement = tdtDateOfRetirement;
        setChanged();
    }

    public String getTdtEffectiveDate() {
        return tdtEffectiveDate;
    }

    public void setTdtEffectiveDate(String tdtEffectiveDate) {
        this.tdtEffectiveDate = tdtEffectiveDate;
        setChanged();
    }

    public String getTdtLastIncrementDate() {
        return tdtLastIncrementDate;
    }

    public void setTdtLastIncrementDate(String tdtLastIncrementDate) {
        this.tdtLastIncrementDate = tdtLastIncrementDate;
        setChanged();
    }

    public String getTdtNextIncrementDate() {
        return tdtNextIncrementDate;
    }

    public void setTdtNextIncrementDate(String tdtNextIncrementDate) {
        this.tdtNextIncrementDate = tdtNextIncrementDate;
        setChanged();
    }

    public String getTdtPensionOpeningBalanceOn() {
        return tdtPensionOpeningBalanceOn;
    }

    public void setTdtPensionOpeningBalanceOn(String tdtPensionOpeningBalanceOn) {
        this.tdtPensionOpeningBalanceOn = tdtPensionOpeningBalanceOn;
        setChanged();
    }

    public String getTdtProbationEndDate() {
        return tdtProbationEndDate;
    }

    public void setTdtProbationEndDate(String tdtProbationEndDate) {
        this.tdtProbationEndDate = tdtProbationEndDate;
        setChanged();
    }

    public String getTdtWfOpeningBalanceOn() {
        return tdtWfOpeningBalanceOn;
    }

    public void setTdtWfOpeningBalanceOn(String tdtWfOpeningBalanceOn) {
        this.tdtWfOpeningBalanceOn = tdtWfOpeningBalanceOn;
        setChanged();
    }

    public String getTxtCustomerName() {
        return txtCustomerName;
    }

    public void setTxtCustomerName(String txtCustomerName) {
        this.txtCustomerName = txtCustomerName;
        setChanged();
    }

    public String getTxtAuthorizeStatus() {
        return txtAuthorizeStatus;
    }

    public void setTxtAuthorizeStatus(String txtAuthorizeStatus) {
        this.txtAuthorizeStatus = txtAuthorizeStatus;
        setChanged();
    }

    public String getTxtDaApplicable() {
        return txtDaApplicable;
    }

    public void setTxtDaApplicable(String txtDaApplicable) {
        this.txtDaApplicable = txtDaApplicable;
        setChanged();
    }

    public String getTxtHraApplicable() {
        return txtHraApplicable;
    }

    public void setTxtHraApplicable(String txtHraApplicable) {
        this.txtHraApplicable = txtHraApplicable;
        setChanged();
    }

    public String getTxtNetSalaryAccountNo() {
        return txtNetSalaryAccountNo;
    }

    public void setTxtNetSalaryAccountNo(String txtNetSalaryAccountNo) {
        this.txtNetSalaryAccountNo = txtNetSalaryAccountNo;
        setChanged();
    }

    public String getTxtNetSalaryProductId() {
        return txtNetSalaryProductId;
    }

    public void setTxtNetSalaryProductId(String txtNetSalaryProductId) {
        this.txtNetSalaryProductId = txtNetSalaryProductId;
        setChanged();
    }

    public String getTxtNetSalaryProductType() {
        return txtNetSalaryProductType;
    }

    public void setTxtNetSalaryProductType(String txtNetSalaryProductType) {
        this.txtNetSalaryProductType = txtNetSalaryProductType;
        setChanged();
    }

    public String getTxtPensionCodeNo() {
        return txtPensionCodeNo;
    }

    public void setTxtPensionCodeNo(String txtPensionCodeNo) {
        this.txtPensionCodeNo = txtPensionCodeNo;
        setChanged();
    }

    public String getTxtStopPayt() {
        return txtStopPayt;
    }

    public void setTxtStopPayt(String txtStopPayt) {
        this.txtStopPayt = txtStopPayt;
        setChanged();
    }

    public String getTxtWfNo() {
        return txtWfNo;
    }

    public void setTxtWfNo(String txtWfNo) {
        this.txtWfNo = txtWfNo;
        setChanged();
    }

    public String getCboDesignation() {
        return cboDesignation;
    }

    public void setCboDesignation(String cboDesignation) {
        this.cboDesignation = cboDesignation;
        setChanged();
    }

    public String getCboNetSalaryProductId() {
        return cboNetSalaryProductId;
    }

    public void setCboNetSalaryProductId(String cboNetSalaryProductId) {
        this.cboNetSalaryProductId = cboNetSalaryProductId;
        setChanged();
    }

    public String getCboNetSalaryProductType() {
        return cboNetSalaryProductType;
    }

    public void setCboNetSalaryProductType(String cboNetSalaryProductType) {
        this.cboNetSalaryProductType = cboNetSalaryProductType;
        setChanged();
    }

    public String getCboStatusOfEmp() {
        return cboStatusOfEmp;
    }

    public void setCboStatusOfEmp(String cboStatusOfEmp) {
        this.cboStatusOfEmp = cboStatusOfEmp;
        setChanged();
    }

    public String getCboBranchCode() {
        return cboBranchCode;
    }

    public void setCboBranchCode(String cboBranchCode) {
        this.cboBranchCode = cboBranchCode;
    }

    public String getTxtMiddleName() {
        return txtMiddleName;
    }

    public void setTxtMiddleName(String txtMiddleName) {
        this.txtMiddleName = txtMiddleName;
        setChanged();
    }

    public String getTxtCity() {
        return txtCity;
    }

    public void setTxtCity(String txtCity) {
        this.txtCity = txtCity;
        setChanged();
    }

    public String getTxtCountry() {
        return txtCountry;
    }

    public void setTxtCountry(String txtCountry) {
        this.txtCountry = txtCountry;
        setChanged();
    }

    public String getTxtMartialStatus() {
        return txtMartialStatus;
    }

    public void setTxtMartialStatus(String txtMartialStatus) {
        this.txtMartialStatus = txtMartialStatus;
        setChanged();
    }

    public String getTxtState() {
        return txtState;
    }

    public void setTxtState(String txtState) {
        this.txtState = txtState;
        setChanged();
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
        setChanged();
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType;
        setChanged();
    }

    public String getCboAddressType() {
        return cboAddressType;
    }

    public void setCboAddressType(String cboAddressType) {
        this.cboAddressType = cboAddressType;
        setChanged();
    }

    void setTxtEmployeeId(String txtEmployeeId) {
        this.txtEmployeeId = txtEmployeeId;
        setChanged();
    }

    String getTxtEmployeeId() {
        return this.txtEmployeeId;
    }

    public String getTxtCustomerId() {
        return txtCustomerId;
    }

    public void setTxtCustomerId(String txtCustomerId) {
        this.txtCustomerId = txtCustomerId;
        setChanged();
    }

    void setTxtBranchCode(String txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
        setChanged();
    }

    String getTxtBranchCode() {
        return this.txtBranchCode;
    }

    void setTxtFirstName(String txtFirstName) {
        this.txtFirstName = txtFirstName;
        setChanged();
    }

    String getTxtFirstName() {
        return this.txtFirstName;
    }

    void setTxtLastName(String txtLastName) {
        this.txtLastName = txtLastName;
        setChanged();
    }

    String getTxtLastName() {
        return this.txtLastName;
    }

    void setRdoGender_Male(boolean rdoGender_Male) {
        this.rdoGender_Male = rdoGender_Male;
        setChanged();
    }

    boolean getRdoGender_Male() {
        return this.rdoGender_Male;
    }

    void setRdoGender_Female(boolean rdoGender_Female) {
        this.rdoGender_Female = rdoGender_Female;
        setChanged();
    }

    boolean getRdoGender_Female() {
        return this.rdoGender_Female;
    }

    void setCboEmployeeType(String cboEmployeeType) {
        this.cboEmployeeType = cboEmployeeType;
        setChanged();
    }

    String getCboEmployeeType() {
        return this.cboEmployeeType;
    }

    void setCboJobTitle(String cboJobTitle) {
        this.cboJobTitle = cboJobTitle;
        setChanged();
    }

    String getCboJobTitle() {
        return this.cboJobTitle;
    }

    void setCboDepartment(String cboDepartment) {
        this.cboDepartment = cboDepartment;
        setChanged();
    }

    String getCboDepartment() {
        return this.cboDepartment;
    }

    void setCboManager(String cboManager) {
        this.cboManager = cboManager;
        setChanged();
    }

    String getCboManager() {
        return this.cboManager;
    }

    void setTxtOfficialEmail(String txtOfficialEmail) {
        this.txtOfficialEmail = txtOfficialEmail;
        setChanged();
    }

    String getTxtOfficialEmail() {
        return this.txtOfficialEmail;
    }

    void setTxtOfficePhone(String txtOfficePhone) {
        this.txtOfficePhone = txtOfficePhone;
        setChanged();
    }

    String getTxtOfficePhone() {
        return this.txtOfficePhone;
    }

    void setTxtStreet(String txtStreet) {
        this.txtStreet = txtStreet;
        setChanged();
    }

    String getTxtStreet() {
        return this.txtStreet;
    }

    void setTxtArea(String txtArea) {
        this.txtArea = txtArea;
        setChanged();
    }

    String getTxtArea() {
        return this.txtArea;
    }

    void setTxtPinCode(String txtPinCode) {
        this.txtPinCode = txtPinCode;
        setChanged();
    }

    String getTxtPinCode() {
        return this.txtPinCode;
    }

    void setTdtBirthDate(String tdtBirthDate) {
        this.tdtBirthDate = tdtBirthDate;
        setChanged();
    }

    String getTdtBirthDate() {
        return this.tdtBirthDate;
    }

    void setTdtJoiningDate(String tdtJoiningDate) {
        this.tdtJoiningDate = tdtJoiningDate;
        setChanged();
    }

    String getTdtJoiningDate() {
        return this.tdtJoiningDate;
    }

    void setTdtLeavingDate(String tdtLeavingDate) {
        this.tdtLeavingDate = tdtLeavingDate;
        setChanged();
    }

    String getTdtLeavingDate() {
        return this.tdtLeavingDate;
    }

    void setTxtPanNo(String txtPanNo) {
        this.txtPanNo = txtPanNo;
        setChanged();
    }

    String getTxtPanNo() {
        return this.txtPanNo;
    }

    void setTxtSsnNo(String txtSsnNo) {
        this.txtSsnNo = txtSsnNo;
        setChanged();
    }

    String getTxtSsnNo() {
        return this.txtSsnNo;
    }

    void setTxtPassPortNo(String txtPassPortNo) {
        this.txtPassPortNo = txtPassPortNo;
        setChanged();
    }

    String getTxtPassPortNo() {
        return this.txtPassPortNo;
    }

    void setTxaSkills(String txaSkills) {
        this.txaSkills = txaSkills;
        setChanged();
    }

    String getTxaSkills() {
        return this.txaSkills;
    }

    void setTxaEducation(String txaEducation) {
        this.txaEducation = txaEducation;
        setChanged();
    }

    String getTxaEducation() {
        return this.txaEducation;
    }

    void setTxaExperience(String txaExperience) {
        this.txaExperience = txaExperience;
        setChanged();
    }

    String getTxaExperience() {
        return this.txaExperience;
    }

    void setTxaResponsibility(String txaResponsibility) {
        this.txaResponsibility = txaResponsibility;
        setChanged();
    }

    String getTxaResponsibility() {
        return this.txaResponsibility;
    }

    void setTxaPerformance(String txaPerformance) {
        this.txaPerformance = txaPerformance;
        setChanged();
    }

    String getTxaPerformance() {
        return this.txaPerformance;
    }

    void setTxaComments(String txaComments) {
        this.txaComments = txaComments;
        setChanged();
    }

    String getTxaComments() {
        return this.txaComments;
    }

    //Setter and Getter for the Picture...
    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
        setChanged();
    }

    public String getPhotoFile() {
        return this.photoFile;
    }

    public void setLblPhoto(String lblPhoto) {
        this.lblPhoto = lblPhoto;
        setChanged();
    }

    public String getLblPhoto() {
        return this.lblPhoto;
    }

    public void setPhotoByteArray(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
        setChanged();
    }

    public byte[] getPhotoByteArray() {
        return this.photoByteArray;
    }

    public java.lang.String getLblValBranchName() {
        return lblValBranchName;
    }

    public void setLblValBranchName(java.lang.String lblValBranchName) {
        this.lblValBranchName = lblValBranchName;
    }
    // Returns the Current Value of Action type...

    public int getActionType() {
        return actionType;
    }

    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    //To reset the Value of lblStatus after each save action...
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public ComboBoxModel getCbmBloodGroup() {
        return cbmBloodGroup;
    }

    public void setCbmBloodGroup(ComboBoxModel cbmBloodGroup) {
        this.cbmBloodGroup = cbmBloodGroup;
    }

    public ComboBoxModel getCbmCommAddressType() {
        return cbmCommAddressType;
    }

    public void setCbmCommAddressType(ComboBoxModel cbmCommAddressType) {
        this.cbmCommAddressType = cbmCommAddressType;
    }

    public ComboBoxModel getCbmFatherTitle() {
        return cbmFatherTitle;
    }

    public void setCbmFatherTitle(ComboBoxModel cbmFatherTitle) {
        this.cbmFatherTitle = cbmFatherTitle;
    }

    public ComboBoxModel getCbmMotherTitle() {
        return cbmMotherTitle;
    }

    public void setCbmMotherTitle(ComboBoxModel cbmMotherTitle) {
        this.cbmMotherTitle = cbmMotherTitle;
    }

    public ComboBoxModel getCbmStatusOfEmp() {
        return cbmStatusOfEmp;
    }

    public void setCbmStatusOfEmp(ComboBoxModel cbmStatusOfEmp) {
        this.cbmStatusOfEmp = cbmStatusOfEmp;
        setChanged();
    }

    public ComboBoxModel getCbmNetSalaryProductId() {
        return cbmNetSalaryProductId;
    }

    public void setCbmNetSalaryProductId(ComboBoxModel cbmNetSalaryProductId) {
        this.cbmNetSalaryProductId = cbmNetSalaryProductId;
        setChanged();
    }

    public ComboBoxModel getCbmNetSalaryProductType() {
        return cbmNetSalaryProductType;
    }

    public void setCbmNetSalaryProductType(ComboBoxModel cbmNetSalaryProductType) {
        this.cbmNetSalaryProductType = cbmNetSalaryProductType;
        setChanged();
    }

    public ComboBoxModel getCbmDesignation() {
        return cbmDesignation;
    }

    public void setCbmDesignation(ComboBoxModel cbmDesignation) {
        this.cbmDesignation = cbmDesignation;
        setChanged();
    }

    public void setCbmEmployeeType(ComboBoxModel cbmEmployeeType) {
        this.cbmEmployeeType = cbmEmployeeType;
        setChanged();
    }

    ComboBoxModel getCbmEmployeeType() {
        return cbmEmployeeType;
    }

    public ComboBoxModel getCbmAddressType() {
        return cbmAddressType;
    }

    public void setCbmAddressType(ComboBoxModel cbmAddressType) {
        this.cbmAddressType = cbmAddressType;
    }

    public void setCbmJobTitle(ComboBoxModel cbmJobTitle) {
        this.cbmJobTitle = cbmJobTitle;
        setChanged();
    }

    ComboBoxModel getCbmJobTitle() {
        return cbmJobTitle;
    }

    public void setCbmDepartment(ComboBoxModel cbmDepartment) {
        this.cbmDepartment = cbmDepartment;
        setChanged();
    }

    ComboBoxModel getCbmDepartment() {
        return cbmDepartment;
    }

    public void setCbmManager(ComboBoxModel cbmManager) {
        this.cbmManager = cbmManager;
        setChanged();
    }

    ComboBoxModel getCbmManager() {
        return cbmManager;
    }

    public ComboBoxModel getCbmBranchCode() {
        return cbmBranchCode;
    }

    public void setCbmBranchCode(ComboBoxModel cbmBranchCode) {
        this.cbmBranchCode = cbmBranchCode;
    }

    static {
        try {
            employeeOB = new EmployeeOB();
        } catch (Exception e) {
            log.info("Error in EmployeeOB:");
        }
    }

    public static EmployeeOB getInstance() {
        return employeeOB;
    }

    /**
     * Creates a new instance of EmployeeOB
     */
    public EmployeeOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }

    private void initianSetup() throws Exception {
        log.info("initianSetup");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "EmployeeJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.employee.EmployeeHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.employee.Employee");
    }

    public void fillDropdown() throws Exception {
        log.info("fillDropdown");
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("EMPLOYEETYPE");
        lookup_keys.add("CUSTOMER.ADDRESSTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("EMPLOYEETYPE"));
        cbmEmployeeType = new ComboBoxModel(key, value);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);

        getKeyValue((HashMap) keyValue.get("CUSTOMER.ADDRESSTYPE"));
        cbmAddressType = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getDesignation");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmJobTitle = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getDepartment");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmDepartment = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getManager");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        key.remove(0);
        value.remove(0);
        key.add(0, "");
        value.add(0, "");
        key.add(1, "SELF");
        value.add(1, "SELF");
        cbmManager = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getDesg");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmDesignation = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getProdTypes");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmNetSalaryProductType = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getTitles");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmFatherTitle = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getTitles");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmMotherTitle = new ComboBoxModel(key, value);


        lookUpHash.put(CommonConstants.MAP_NAME, "getCommAddr");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmCommAddressType = new ComboBoxModel(key, value);

    }

    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals(CommonConstants.GL_TRANSMODE_TYPE)) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmNetSalaryProductId = new ComboBoxModel(key, value);
        this.cbmNetSalaryProductId = cbmNetSalaryProductId;
        setChanged();
    }

    public void setBranchCode() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValueList = null;
        keyValueList = ClientUtil.executeQuery("getbranches", null);
        key.add("");
        value.add("");
        if (keyValueList != null && keyValueList.size() > 0) {
            for (int i = 0; i < keyValueList.size(); i++) {
                mapShare = (HashMap) keyValueList.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_CODE"));
            }
        }
        cbmBranchCode = new ComboBoxModel(key, value);
        key = null;
        value = null;
        keyValueList.clear();
        keyValueList = null;
        mapShare.clear();
        mapShare = null;
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("getKeyValue");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch (Exception e) {
            log.info("Error In populateData()");
            e.printStackTrace();
        }
    }

    private void populateOB(HashMap mapData) throws Exception {
        log.info("In populateOB()");
        EmployeeDetailsTO objEmployeeDetailsTO = null;
        SalaryStructTO objSalaryStructureTO = null;
        if (mapData.containsKey("EmployeeMastrDetailsTO") && mapData.get("EmployeeMastrDetailsTO") != null) {
            objEmployeeDetailsTO = (EmployeeDetailsTO) ((List) mapData.get("EmployeeMastrDetailsTO")).get(0);
            setEmployeeDetailsTO(objEmployeeDetailsTO);
        }
        if (mapData.containsKey("EmployeeSalStructTO") && mapData.get("EmployeeSalStructTO") != null) {
            objSalaryStructureTO = (SalaryStructTO) ((List) mapData.get("EmployeeSalStructTO")).get(0);
            setSalaryStructTO(objSalaryStructureTO);
        }
        if (mapData.containsKey("EmployeeOtherDetailsTO") && mapData.get("EmployeeOtherDetailsTO") != null) {
            objEmployeeDetailsTO = (EmployeeDetailsTO) ((List) mapData.get("EmployeeOtherDetailsTO")).get(0);
            setEmployeeOtherDetailsTO(objEmployeeDetailsTO);
        }
        if (mapData.containsKey("EmployeePresentDetailsTO") && mapData.get("EmployeePresentDetailsTO") != null) {
            objSalaryStructureTO = (SalaryStructTO) ((List) mapData.get("EmployeePresentDetailsTO")).get(0);
            setEmployeePresentDetailsTO(objSalaryStructureTO);
        }
    }

    // To Enter the values in the UI fields, from the database...
    private void setEmployeeOtherDetailsTO(EmployeeDetailsTO objEmployeeDetailsTO) throws Exception {
        log.info("In setEmployeeOtherDetailsTO()");
        setCboJobTitle(CommonUtil.convertObjToStr(getCbmJobTitle().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getDesigId()))));
        setCboDepartment(CommonUtil.convertObjToStr(getCbmDepartment().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getDepttId()))));
        setTxaResponsibility(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getResponsibility()));
        setTxaPerformance(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPerformance()));
        setTxaComments(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getComments()));
        setTxaSkills(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getSkills()));
        setTxaEducation(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getEducation()));
        setTxaExperience(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getExperience()));
        setTxtSsnNo(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getSsn()));
        setTxtPassPortNo(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPassportNo()));
        setCboEmployeeType(CommonUtil.convertObjToStr(getCbmEmployeeType().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getEmployeeType()))));
        log.info("End of setEmployeeOtherDetailsTO()");
    }

    private void setEmployeeDetailsTO(EmployeeDetailsTO objEmployeeDetailsTO) throws Exception {
        log.info("In setEmployeeDetailsTO()");
        setTxtEmployeeId(objEmployeeDetailsTO.getEmployeeCode());
        setTxtCustomerId(objEmployeeDetailsTO.getCustomerId());
        setTxtTitle(objEmployeeDetailsTO.getTitle());
        if (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getGender()).equals("MALE")) {
            setRdoGender_Male(true);
            setRdoGender_Female(false);
        } else if (CommonUtil.convertObjToStr(objEmployeeDetailsTO.getGender()).equals("FEMALE")) {
            setRdoGender_Female(true);
            setRdoGender_Male(false);
        } else {
            setRdoGender_Male(false);
            setRdoGender_Female(false);
        }
        setTxtFirstName(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getFname()));
        setTdtBirthDate(DateUtil.getStringDate(objEmployeeDetailsTO.getDob()));
        setCboManager(CommonUtil.convertObjToStr(getCbmManager().getDataForKey(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getManagerCode()))));
        setTxtOfficialEmail(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getOfficialEmail()));
        setTxtOfficePhone(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getOfficePhone()));
        setTxtPanNo(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPanNo()));
        setPhotoFile(objEmployeeDetailsTO.getPhotoFile());
        setTxtMartialStatus(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getMaritalStatus()));
        setTxtSortOrder(CommonUtil.convertObjToInt(objEmployeeDetailsTO.getSortOrder()));
        setTxtFatherName(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getFatherName()));
        setTxtMotherName(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getMotherName()));
        setTxtSpouseName(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getSpouseName()));
        setTxtSpouseRelation(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getSpouseRelation()));
        setTxtPlaceofBirth(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPlaceofBirth()));
        setTxtReligion(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getReligion()));
        setTxtCaste(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getCaste()));
        setCboBloodGroup(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getBloodGroup()));
        setTxaIdentificationMark1(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getIdentificationMark1()));
        setTxaIdentificationMark2(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getIdentificationMark2()));
        setTxaPhysicallyHandicapped(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getPhysicallyHandicapped()));
        setTxaMajorHealthProblem(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getMajorHealthProblem()));
        setCboCommAddressType(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getCommAddressType()));
        setCboFatherTitle(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getFatherTitle()));
        setCboMotherTitle(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getMotherTitle()));
        setTxtLastName(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getLname()));
        setTxtMiddleName(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getMname()));
        setCboBranchCode(CommonUtil.convertObjToStr(objEmployeeDetailsTO.getBranchCode()));
        log.info("End of setEmployeeDetailsTO()");
    }

    private void setSalaryStructTO(SalaryStructTO objSalaryStructureTO) throws Exception {
        log.info("In setSalaryStructTO(): " + objSalaryStructureTO);
        setCboNetSalaryProductType(CommonUtil.convertObjToStr(getCbmNetSalaryProductType().getDataForKey(CommonUtil.convertObjToStr(objSalaryStructureTO.getNetSalaryProductType()))));
        setCboNetSalaryProductId(CommonUtil.convertObjToStr(objSalaryStructureTO.getNetSalaryProductId()));
        getCbmNetSalaryProductId().setKeyForSelected(CommonUtil.convertObjToStr(objSalaryStructureTO.getNetSalaryProductId()));
        setTdtDateOfRetirement(DateUtil.getStringDate(objSalaryStructureTO.getDateOfRetirement()));
        setTdtPensionOpeningBalanceOn(DateUtil.getStringDate(objSalaryStructureTO.getPensionOpeningBalanceOn()));
        setTdtWfOpeningBalanceOn(DateUtil.getStringDate(objSalaryStructureTO.getWfOpeningBalanceOn()));
        setTxtNetSalaryAccountNo(objSalaryStructureTO.getNetSalaryAccountNo());
        if (objSalaryStructureTO.getNetSalaryAccountNo() != null) {
            HashMap data = new HashMap();
            data.put("ACT_NUM", objSalaryStructureTO.getNetSalaryAccountNo());
            List custNames = ClientUtil.executeQuery("getNetSalAccCustName", data);
            if (custNames != null && custNames.size() > 0) {
                HashMap custMap = new HashMap();
                custMap = (HashMap) custNames.get(0);
                String custName = "";
                custName = CommonUtil.convertObjToStr(custMap.get("NAME"));
                setTxtCustomerName(custName);
            }
        }
        setTxtPensionOpeningBalance(CommonUtil.convertObjToStr(objSalaryStructureTO.getPensionOpeningBalance()));
        setTxtWfOpeningBalance(CommonUtil.convertObjToStr(objSalaryStructureTO.getWfOpeningBalance()));
        setTxtAuthorizeStatus(objSalaryStructureTO.getAuthorizeStatus());
        setTdtDateOfJoin(DateUtil.getStringDate(objSalaryStructureTO.getDateOfJoin()));
        setTdtProbationEndDate(DateUtil.getStringDate(objSalaryStructureTO.getProbationEndDate()));
        setTxtWfNo(objSalaryStructureTO.getWfNo());
        setTxtPensionCodeNo(objSalaryStructureTO.getPensionCodeNo());
        setCboStatusOfEmp(CommonUtil.convertObjToStr(objSalaryStructureTO.getStatusOfEmp()));
        if (objSalaryStructureTO.getDaApplicable().equals("YES")) {
            setDaAppl(true);
        } else {
            setDaAppl(false);
        }
        if (objSalaryStructureTO.getHraApplicable().equals("YES")) {
            setHraAppl(true);
        } else {
            setHraAppl(false);
        }

        if (objSalaryStructureTO.getStopPayt().equals("YES")) {
            setStopPayt(true);
        } else {
            setStopPayt(false);
        }
        log.info("End of setSalaryStructTO()");
    }

    private void setEmployeePresentDetailsTO(SalaryStructTO objSalaryStructureTO) throws Exception {
        log.info("In setEmployeePresentDetailsTO()");
        setCboDesignation(CommonUtil.convertObjToStr(objSalaryStructureTO.getDesignation()));
        setTxtIncrementCount(CommonUtil.convertObjToStr(objSalaryStructureTO.getIncrementCount()));
        setTxtPresentBasicSalary(CommonUtil.convertObjToStr(objSalaryStructureTO.getPresentBasicSalary()));
        setTdtEffectiveDate(DateUtil.getStringDate(objSalaryStructureTO.getEffectiveDate()));
        setTdtLastIncrementDate(DateUtil.getStringDate(objSalaryStructureTO.getLastIncrementDate()));
        setTdtNextIncrementDate(DateUtil.getStringDate(objSalaryStructureTO.getNextIncrementDate()));
        log.info("End of setEmployeePresentDetailsTO()");
    }

    public EmployeeDetailsTO setEmployeeDetails() {
        log.info("In setEmployeeDetails()");

        final EmployeeDetailsTO objEmployeeDetailsTO = new EmployeeDetailsTO();
        try {
            objEmployeeDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            objEmployeeDetailsTO.setCreatedBy(TrueTransactMain.USER_ID);
            objEmployeeDetailsTO.setStatusDt(curDate);
            objEmployeeDetailsTO.setCreatedDt(curDate);
            objEmployeeDetailsTO.setBranchCode(getCboBranchCode());
            objEmployeeDetailsTO.setEmployeeCode(txtEmployeeId);
            objEmployeeDetailsTO.setTitle(txtTitle);
            objEmployeeDetailsTO.setLname(txtLastName);
            objEmployeeDetailsTO.setFname(txtFirstName);
            objEmployeeDetailsTO.setMname(txtMiddleName);
            objEmployeeDetailsTO.setCustomerId(txtCustomerId);
            if (getRdoGender_Male() == true) {
                objEmployeeDetailsTO.setGender("MALE");
            } else if (getRdoGender_Female() == true) {
                objEmployeeDetailsTO.setGender("FEMALE");
            }
            objEmployeeDetailsTO.setDesigId(CommonUtil.convertObjToStr(cbmJobTitle.getKeyForSelected()));
            objEmployeeDetailsTO.setEmployeeType(CommonUtil.convertObjToStr(cbmEmployeeType.getKeyForSelected()));
            objEmployeeDetailsTO.setMaritalStatus(CommonUtil.convertObjToStr(txtMartialStatus));
            objEmployeeDetailsTO.setDepttId(CommonUtil.convertObjToStr(cbmDepartment.getKeyForSelected()));
            objEmployeeDetailsTO.setManagerCode(CommonUtil.convertObjToStr(cbmManager.getKeyForSelected()));
            objEmployeeDetailsTO.setOfficialEmail(txtOfficialEmail);
            objEmployeeDetailsTO.setOfficePhone(txtOfficePhone);
            objEmployeeDetailsTO.setPhotoFile(getPhotoFile());
            objEmployeeDetailsTO.setPanNo(txtPanNo);
            objEmployeeDetailsTO.setSsn(txtSsnNo);
            objEmployeeDetailsTO.setPassportNo(txtPassPortNo);
            objEmployeeDetailsTO.setSkills(txaSkills);
            objEmployeeDetailsTO.setEducation(txaEducation);
            objEmployeeDetailsTO.setExperience(txaExperience);
            objEmployeeDetailsTO.setResponsibility(txaResponsibility);
            objEmployeeDetailsTO.setPerformance(txaPerformance);
            objEmployeeDetailsTO.setComments(txaComments);
            objEmployeeDetailsTO.setCreatedBy(TrueTransactMain.USER_ID);
            objEmployeeDetailsTO.setIdentificationMark1(txaIdentificationMark1);
            objEmployeeDetailsTO.setIdentificationMark2(txaIdentificationMark2);
            objEmployeeDetailsTO.setPhysicallyHandicapped(txaPhysicallyHandicapped);
            objEmployeeDetailsTO.setMajorHealthProblem(txaMajorHealthProblem);
            objEmployeeDetailsTO.setSortOrder(CommonUtil.convertObjToInt(txtSortOrder));
            objEmployeeDetailsTO.setFatherName(txtFatherName);
            objEmployeeDetailsTO.setMotherName(txtMotherName);
            objEmployeeDetailsTO.setSpouseName(txtSpouseName);
            objEmployeeDetailsTO.setSpouseRelation(txtSpouseRelation);
            objEmployeeDetailsTO.setPlaceofBirth(txtPlaceofBirth);
            objEmployeeDetailsTO.setReligion(txtReligion);
            objEmployeeDetailsTO.setCaste(txtCaste);
            objEmployeeDetailsTO.setBloodGroup(CommonUtil.convertObjToStr(getCboBloodGroup()));
            objEmployeeDetailsTO.setCommAddressType(CommonUtil.convertObjToStr(cbmCommAddressType.getKeyForSelected()));
            objEmployeeDetailsTO.setFatherTitle(CommonUtil.convertObjToStr(cbmFatherTitle.getKeyForSelected()));
            objEmployeeDetailsTO.setMotherTitle(CommonUtil.convertObjToStr(cbmMotherTitle.getKeyForSelected()));
            objEmployeeDetailsTO.setBranchCode(CommonUtil.convertObjToStr(cbmBranchCode.getKeyForSelected()));
            Date BdDt = DateUtil.getDateMMDDYYYY(tdtBirthDate);
            if (BdDt != null) {
                Date bdDate = (Date) curDate.clone();
                bdDate.setDate(BdDt.getDate());
                bdDate.setMonth(BdDt.getMonth());
                bdDate.setYear(BdDt.getYear());
                objEmployeeDetailsTO.setDob(DateUtil.getDateMMDDYYYY(tdtBirthDate));
            } else {
                objEmployeeDetailsTO.setDob(DateUtil.getDateMMDDYYYY(tdtBirthDate));
            }
            Date JdDt = DateUtil.getDateMMDDYYYY(tdtJoiningDate);
            if (JdDt != null) {
                Date jdDate = (Date) curDate.clone();
                jdDate.setDate(JdDt.getDate());
                jdDate.setMonth(JdDt.getMonth());
                jdDate.setYear(JdDt.getYear());
                objEmployeeDetailsTO.setDoj(jdDate);
            } else {
                objEmployeeDetailsTO.setDoj(DateUtil.getDateMMDDYYYY(tdtJoiningDate));
            }
            Date LdDt = DateUtil.getDateMMDDYYYY(tdtLeavingDate);
            if (LdDt != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(LdDt.getDate());
                ldDate.setMonth(LdDt.getMonth());
                ldDate.setYear(LdDt.getYear());
                objEmployeeDetailsTO.setDol(DateUtil.getDateMMDDYYYY(tdtLeavingDate));
            } else {
                objEmployeeDetailsTO.setDol(DateUtil.getDateMMDDYYYY(tdtLeavingDate));
            }
        } catch (Exception e) {
            log.info("Error In setEmployeeDetails()");
            e.printStackTrace();
        }
        return objEmployeeDetailsTO;
    }

    public SalaryStructTO setSalaryStructTO() {
        log.info("in setSalaryStructTO");
        final SalaryStructTO objSalaryStructTO = new SalaryStructTO();
        try {
            objSalaryStructTO.setDesignation(CommonUtil.convertObjToStr(cbmDesignation.getKeyForSelected()));
            objSalaryStructTO.setNetSalaryProductType(CommonUtil.convertObjToStr(cbmNetSalaryProductType.getKeyForSelected()));
            objSalaryStructTO.setNetSalaryProductId(CommonUtil.convertObjToStr(cbmNetSalaryProductId.getKeyForSelected()));
            objSalaryStructTO.setStatusOfEmp(CommonUtil.convertObjToStr(getCboStatusOfEmp()));
            objSalaryStructTO.setEmployeeCode(txtEmployeeId);
            objSalaryStructTO.setCreatedBy(TrueTransactMain.USER_ID);
            objSalaryStructTO.setCreatedDt(curDate);
            objSalaryStructTO.setScale_id(CommonUtil.convertObjToInt(txtScaleId));
            objSalaryStructTO.setVersionNo(CommonUtil.convertObjToInt(txtVersionNo));
            objSalaryStructTO.setPensionCodeNo(txtPensionCodeNo);
            objSalaryStructTO.setPensionOpeningBalance(CommonUtil.convertObjToDouble(txtPensionOpeningBalance));
            objSalaryStructTO.setWfNo(txtWfNo);
            objSalaryStructTO.setWfOpeningBalance(CommonUtil.convertObjToDouble(txtWfOpeningBalance));
            objSalaryStructTO.setAuthorizeStatus(null);
            objSalaryStructTO.setIncrementCount(CommonUtil.convertObjToInt(txtIncrementCount));
            objSalaryStructTO.setCustomerName(txtCustomerName);
            objSalaryStructTO.setPresentBasicSalary(CommonUtil.convertObjToDouble(txtPresentBasicSalary));
            objSalaryStructTO.setNetSalaryAccountNo(txtNetSalaryAccountNo);
            objSalaryStructTO.setStatusBy(TrueTransactMain.USER_ID);
            if (isDaAppl() == true) {
                objSalaryStructTO.setDaApplicable("YES");
            } else {
                objSalaryStructTO.setDaApplicable("NO");
            }
            if (isHraAppl() == true) {
                objSalaryStructTO.setHraApplicable("YES");
            } else {
                objSalaryStructTO.setHraApplicable("NO");
            }
            if (isStopPayt() == true) {
                objSalaryStructTO.setStopPayt("YES");
            } else {
                objSalaryStructTO.setStopPayt("NO");
            }
            Date EfDt = DateUtil.getDateMMDDYYYY(tdtEffectiveDate);
            if (EfDt != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(EfDt.getDate());
                ldDate.setMonth(EfDt.getMonth());
                ldDate.setYear(EfDt.getYear());
                objSalaryStructTO.setEffectiveDate(DateUtil.getDateMMDDYYYY(tdtEffectiveDate));
            } else {
                objSalaryStructTO.setEffectiveDate(DateUtil.getDateMMDDYYYY(tdtEffectiveDate));
            }
            Date LastIncrDt = DateUtil.getDateMMDDYYYY(tdtLastIncrementDate);
            if (LastIncrDt != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(LastIncrDt.getDate());
                ldDate.setMonth(LastIncrDt.getMonth());
                ldDate.setYear(LastIncrDt.getYear());
                objSalaryStructTO.setLastIncrementDate(DateUtil.getDateMMDDYYYY(tdtLastIncrementDate));
            } else {
                objSalaryStructTO.setLastIncrementDate(DateUtil.getDateMMDDYYYY(tdtLastIncrementDate));
            }
            Date NextIncrDt = DateUtil.getDateMMDDYYYY(tdtNextIncrementDate);
            if (NextIncrDt != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(NextIncrDt.getDate());
                ldDate.setMonth(NextIncrDt.getMonth());
                ldDate.setYear(NextIncrDt.getYear());
                objSalaryStructTO.setNextIncrementDate(DateUtil.getDateMMDDYYYY(tdtNextIncrementDate));
            } else {
                objSalaryStructTO.setNextIncrementDate(DateUtil.getDateMMDDYYYY(tdtNextIncrementDate));
            }
            Date DtOfJoin = DateUtil.getDateMMDDYYYY(tdtDateOfJoin);
            if (DtOfJoin != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(DtOfJoin.getDate());
                ldDate.setMonth(DtOfJoin.getMonth());
                ldDate.setYear(DtOfJoin.getYear());
                objSalaryStructTO.setDateOfJoin(DateUtil.getDateMMDDYYYY(tdtDateOfJoin));
            } else {
                objSalaryStructTO.setDateOfJoin(DateUtil.getDateMMDDYYYY(tdtDateOfJoin));
            }
            Date ProbEndDt = DateUtil.getDateMMDDYYYY(tdtProbationEndDate);
            if (ProbEndDt != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(ProbEndDt.getDate());
                ldDate.setMonth(ProbEndDt.getMonth());
                ldDate.setYear(ProbEndDt.getYear());
                objSalaryStructTO.setProbationEndDate(DateUtil.getDateMMDDYYYY(tdtProbationEndDate));
            } else {
                objSalaryStructTO.setProbationEndDate(DateUtil.getDateMMDDYYYY(tdtProbationEndDate));
            }
            Date DtOfRet = DateUtil.getDateMMDDYYYY(tdtDateOfRetirement);
            if (DtOfRet != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(DtOfRet.getDate());
                ldDate.setMonth(DtOfRet.getMonth());
                ldDate.setYear(DtOfRet.getYear());
                objSalaryStructTO.setDateOfRetirement(DateUtil.getDateMMDDYYYY(tdtDateOfRetirement));
            } else {
                objSalaryStructTO.setDateOfRetirement(DateUtil.getDateMMDDYYYY(tdtDateOfRetirement));
            }
            Date PenOpBal = DateUtil.getDateMMDDYYYY(tdtPensionOpeningBalanceOn);
            if (PenOpBal != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(PenOpBal.getDate());
                ldDate.setMonth(PenOpBal.getMonth());
                ldDate.setYear(PenOpBal.getYear());
                objSalaryStructTO.setPensionOpeningBalanceOn(DateUtil.getDateMMDDYYYY(tdtPensionOpeningBalanceOn));
            } else {
                objSalaryStructTO.setPensionOpeningBalanceOn(DateUtil.getDateMMDDYYYY(tdtPensionOpeningBalanceOn));
            }
            Date WfOpBal = DateUtil.getDateMMDDYYYY(tdtWfOpeningBalanceOn);
            if (WfOpBal != null) {
                Date ldDate = (Date) curDate.clone();
                ldDate.setDate(WfOpBal.getDate());
                ldDate.setMonth(WfOpBal.getMonth());
                ldDate.setYear(WfOpBal.getYear());
                objSalaryStructTO.setWfOpeningBalanceOn(DateUtil.getDateMMDDYYYY(tdtWfOpeningBalanceOn));
            } else {
                objSalaryStructTO.setWfOpeningBalanceOn(DateUtil.getDateMMDDYYYY(tdtWfOpeningBalanceOn));
            }
        } catch (Exception e) {
            log.info("Error In setSalaryStructTO()");
            e.printStackTrace();
        }
        return objSalaryStructTO;
    }

    public EmployeeAddrTO setEmployeeAddr() {
        log.info("In setEmployeeAddr()");
        final EmployeeAddrTO objEmployeeAddrTO = new EmployeeAddrTO();
        try {
            objEmployeeAddrTO.setEmployeeId(txtEmployeeId);
            objEmployeeAddrTO.setStreet(txtStreet);
            objEmployeeAddrTO.setArea(txtArea);
            objEmployeeAddrTO.setPinCode(txtPinCode);
            objEmployeeAddrTO.setCity(txtCity);
            objEmployeeAddrTO.setState(txtState);
            objEmployeeAddrTO.setCountry(txtCountry);
        } catch (Exception e) {
            log.info("Error In setEmployeeAddr()");
            e.printStackTrace();
        }
        return objEmployeeAddrTO;
    }

    private EarnDeduPayTO setEarningsDeductionPay() {
        log.info("In setEarningsDeductionPay()");
        final EarnDeduPayTO objEarnDeduPayTO = new EarnDeduPayTO();
        objEarnDeduPayTO.setProdType("");
        objEarnDeduPayTO.setProdId("");
        objEarnDeduPayTO.setAccNo("");
        objEarnDeduPayTO.setCalcUpto(null);
        objEarnDeduPayTO.setStatus("CREATED");
        objEarnDeduPayTO.setStatusBy(TrueTransactMain.USER_ID);
        objEarnDeduPayTO.setStatusDate(ClientUtil.getCurrentDate());
        objEarnDeduPayTO.setCreatedBy(TrueTransactMain.USER_ID);
        objEarnDeduPayTO.setCreatedDate(ClientUtil.getCurrentDate());
        objEarnDeduPayTO.setFromDate(null);
        objEarnDeduPayTO.setToDate(null);
        objEarnDeduPayTO.setInterest(0.0);
        objEarnDeduPayTO.setPenalInterest(0.0);
        objEarnDeduPayTO.setPrincipal(0.0);
        objEarnDeduPayTO.setRemark("");
        return objEarnDeduPayTO;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void getCustAddrData(String custID) {
        try {
            HashMap lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, "Account.getCustAddr");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, custID);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmAddressType = new ComboBoxModel(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public HashMap getCustAddrType(HashMap dataMap) {
        HashMap resultMap = new HashMap();
        final List custAddrData = ClientUtil.executeQuery("getCustAddress", dataMap);
        if (custAddrData != null && custAddrData.size() > 0) {
            resultMap = (HashMap) custAddrData.get(0);
            setTxtStreet(CommonUtil.convertObjToStr(resultMap.get("STREET")));
            setTxtArea(CommonUtil.convertObjToStr(resultMap.get("AREA")));
            setTxtCity(CommonUtil.convertObjToStr(resultMap.get("CITY")));
            setTxtState(CommonUtil.convertObjToStr(resultMap.get("STATE")));
            setTxtCountry(CommonUtil.convertObjToStr(resultMap.get("COUNTRY_CODE")));
            setTxtPinCode(CommonUtil.convertObjToStr(resultMap.get("PIN_CODE")));
        }
        return resultMap;
    }

    public HashMap getCustData(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List custData = ClientUtil.executeQuery("getCustData", dataMap);
        if (custData != null && custData.size() > 0) {
            resultMap = (HashMap) custData.get(0);
            setTxtCustomerId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
            setTxtTitle(CommonUtil.convertObjToStr(resultMap.get("TITLE")));
            setTxtFirstName(CommonUtil.convertObjToStr(resultMap.get("FNAME")));
            setTxtMiddleName((CommonUtil.convertObjToStr(resultMap.get("MNAME"))));
            setTxtLastName(CommonUtil.convertObjToStr(resultMap.get("LNAME")));
            setTxtMartialStatus(CommonUtil.convertObjToStr(resultMap.get("MARITALSTATUS")));
            setTxtOfficialEmail(CommonUtil.convertObjToStr(resultMap.get("EMAIL_ID")));
            setTdtBirthDate(CommonUtil.convertObjToStr(resultMap.get("DOB")));
            setTxtSsnNo(CommonUtil.convertObjToStr(resultMap.get("SSN")));
            setTxtPanNo(CommonUtil.convertObjToStr(resultMap.get("PAN_NUMBER")));
            setTxtCaste(CommonUtil.convertObjToStr(resultMap.get("CASTE")));
            setTxtReligion(CommonUtil.convertObjToStr(resultMap.get("RELIGION")));
            if (resultMap.containsKey("GENDER") && resultMap.get("GENDER").equals("Male")) {
                setRdoGender_Male(true);
                setRdoGender_Female(false);
            } else if (resultMap.containsKey("GENDER") && resultMap.get("GENDER").equals("FeMale")) {
                setRdoGender_Female(true);
                setRdoGender_Male(false);
            } else if (resultMap.containsKey("GENDER") && resultMap.get("GENDER").equals("")) {
                setRdoGender_Male(false);
                setRdoGender_Female(false);
            }
        }
        return resultMap;
    }

    public HashMap getCustId(HashMap dataMap) throws SQLException {
        HashMap whereMap = new HashMap();
        whereMap.put("EMPID", dataMap.get("EMPLOYEE_ID"));
        List cust = ClientUtil.executeQuery("getCustIdforStaff", whereMap);
        HashMap resultMap = new HashMap();
        resultMap = (HashMap) cust.get(0);
        setTxtCustomerId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
        return resultMap;
    }

    public HashMap getCustIdAuth(HashMap dataMap) throws SQLException {
        HashMap whereMap = new HashMap();
        whereMap.put("EMPID", dataMap.get("EMPLOYEEID"));
        List cust = ClientUtil.executeQuery("getCustIdforStaff", whereMap);
        HashMap resultMap = new HashMap();
        resultMap = (HashMap) cust.get(0);
        setTxtCustomerId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
        return resultMap;
    }

    public HashMap getScaleId(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List scaleId = ClientUtil.executeQuery("getScaleId", dataMap);
        if (scaleId != null && scaleId.size() > 0) {
            resultMap = (HashMap) scaleId.get(0);
            setTxtScaleId(CommonUtil.convertObjToStr(resultMap.get("SCALE_ID")));
            setTxtVersionNo(CommonUtil.convertObjToStr(resultMap.get("VERSION_NO")));
        }
        return resultMap;
    }

    public HashMap getCustPhone(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List custPh = ClientUtil.executeQuery("getCustPhone", dataMap);
        if (custPh != null && custPh.size() > 0) {
            resultMap = (HashMap) custPh.get(0);
            setTxtOfficePhone(CommonUtil.convertObjToStr(resultMap.get("PHONE_NUMBER")));
        }
        return resultMap;
    }

    public HashMap getNetSalProdId(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List prodId = ClientUtil.executeQuery("getNetSalProdId", dataMap);
        if (prodId != null && prodId.size() > 0) {
            setCboNetSalaryProductId(CommonUtil.convertObjToStr(resultMap.get("PROD_DESC")));
        }
        return resultMap;
    }

    public HashMap getIncrementStagCount(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List incrCount = ClientUtil.executeQuery("getIncrStagCount", dataMap);
        if (incrCount != null && incrCount.size() > 0) {
            resultMap = (HashMap) incrCount.get(0);
        }
        return resultMap;
    }

    public HashMap getPreBasicSal(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List basicSal = ClientUtil.executeQuery("getBasicSal", dataMap);
        if (basicSal != null && basicSal.size() > 0) {
            resultMap = (HashMap) basicSal.get(0);
        }
        return resultMap;
    }

    public HashMap getScaleDetails(HashMap dataMap) throws SQLException {
        HashMap resultMap = new HashMap();
        final List scale = ClientUtil.executeQuery("getScaleDet", dataMap);
        if (scale != null && scale.size() > 0) {
            resultMap = (HashMap) scale.get(0);
        }
        return resultMap;
    }

    public List getManager(HashMap dataMap) throws SQLException {
        final List manager = ClientUtil.executeQuery("getManagerCode", dataMap);
        return manager;
    }

    public List getPayCodesList() {
        List list = (List) ClientUtil.executeQuery("getAllPayCodeId", null);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null) {
                    doActionPerform();
                }
            } else {
                log.info("Action Type Not Defined In setInwardClearingTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        try {
            final EmployeeDetailsTO objEmployeeDetailsTO = setEmployeeDetails();
            final SalaryStructTO objsalSalaryStructTO = setSalaryStructTO();
            final EmployeeAddrTO objEmployeeAddrTO = setEmployeeAddr();
            final EarnDeduPayTO objEarnDeduPayTO = setEarningsDeductionPay();
            final List payCodesList = getPayCodesList();
            objEmployeeDetailsTO.setCommand(getCommand());
            final HashMap data = new HashMap();
            if (isAuthorized() == true) {
                objEarnDeduPayTO.setCommand(getCommand());
                data.put("AuthMap", objEmployeeDetailsTO);
                data.put("PayCodesList", payCodesList);
                data.put("EarnDeduPayTO", objEarnDeduPayTO);
                data.put("SalaryDetailsTO", objsalSalaryStructTO);
            } else {
                data.put("EmployeeDetailsTO", objEmployeeDetailsTO);
                data.put("SalaryDetailsTO", objsalSalaryStructTO);
                data.put("EmployeeAddrTO", objEmployeeAddrTO);
                data.put("photo", this.photoByteArray);
            }
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            if (proxyResultMap != null && proxyResultMap.containsKey("EmployeeId") && proxyResultMap.get("EmployeeId") != null) {
                ClientUtil.showMessageWindow("Successfully Created EmployeeId: " + CommonUtil.convertObjToStr(proxyResultMap.get("EmployeeId")));
            }
            setResult(actionType);
            resetForm();
        } catch (Exception e) {
            log.info("Error In doActionPerform()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    // to decide which action Should be performed...
    private String getCommand() throws Exception {
        log.info("In getCommand()");

        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            default:
        }
        return command;
    }

    public void resetForm() {
        log.info("In resetForm()");
        setTxtEmployeeId("");
        setTxtCustomerId("");
        setTxtBranchCode("");
        setTxtFirstName("");
        setTxtMiddleName("");
        setTxtLastName("");
        setRdoGender_Male(false);
        setRdoGender_Female(false);
        setCboEmployeeType("");
        setTxtMartialStatus("");
        setCboJobTitle("");
        setCboDepartment("");
        setCboManager("");
        setTxtOfficialEmail("");
        setTxtOfficePhone("");
        setTxtStreet("");
        setTxtArea("");
        setCboAddressType("");
        setTxtTitle("");
        setTxtCity("");
        setTxtState("");
        setTxtCountry("");
        setTxtPinCode("");
        setTdtBirthDate("");
        setTdtJoiningDate("");
        setTdtLeavingDate("");
        setTxtPanNo("");
        setTxtSsnNo("");
        setTxtPassPortNo("");
        setTxaSkills("");
        setTxaEducation("");
        setTxaExperience("");
        setTxaResponsibility("");
        setTxaPerformance("");
        setTxaComments("");
        setLblValBranchName("");
        setLblPhoto("");
        setCboDesignation("");
        setTxtScaleId("");
        setTdtEffectiveDate("");
        setTxtIncrementCount("");
        setTxtPresentBasicSalary("");
        setTdtLastIncrementDate("");
        setTdtNextIncrementDate("");
        setTdtDateOfJoin("");
        setTdtDateOfRetirement("");
        setCboNetSalaryProductType("");
        setCboNetSalaryProductId("");
        setTxtNetSalaryAccountNo("");
        setTxtCustomerName("");
        setTxtPensionCodeNo("");
        setTxtPensionOpeningBalance("");
        setTdtPensionOpeningBalanceOn("");
        setTxtWfNo("");
        setTxtWfOpeningBalance("");
        setTdtWfOpeningBalanceOn("");
        setCboStatusOfEmp("");
        setTxtAuthorizeStatus("");
        setTdtProbationEndDate("");
        setDaAppl(false);
        setHraAppl(false);
        setStopPayt(false);
        setTxtSortOrder(0);
        setTxtFatherName("");
        setTxtMotherName("");
        setTxtSpouseName("");
        setTxtSpouseRelation("");
        setCboBloodGroup("");
        setCboFatherTitle("");
        setCboMotherTitle("");
        setCboManager("");
        setTxtPlaceofBirth("");
        setTxtCaste("");
        setCboCommAddressType("");
        setTxaIdentificationMark1("");
        setTxaIdentificationMark2("");
        setTxaPhysicallyHandicapped("");
        setTxaMajorHealthProblem("");
        setTxtReligion("");
        setCboAddressType("");
        setTxtVersionNo("");
        setCboBranchCode("");
        setAuthorized(false);
        ttNotifyObservers();
    }
}
