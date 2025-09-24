/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IndividualCustHashMap.java
 *
 * Created on Tue Nov 23 11:53:26 GMT+05:30 2004
 */

package com.see.truetransact.ui.employee;


import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class EmployeeMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public EmployeeMasterHashMap(){
        mandatoryMap = new HashMap();
        
        mandatoryMap.put("txtEmpID", new Boolean(true));
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
        mandatoryMap.put("txtFatherFirstName", new Boolean(true));
        mandatoryMap.put("txtFatherMiddleName", new Boolean(false));
        mandatoryMap.put("txtFatherLastName", new Boolean(false));
        mandatoryMap.put("cboMotherTitle", new Boolean(false));
        mandatoryMap.put("txtMotherFirstName", new Boolean(false));
        mandatoryMap.put("txtMotherMiddleName", new Boolean(false));
        mandatoryMap.put("txtMotherLastName", new Boolean(false));
        mandatoryMap.put("tdtDateOfBirth", new Boolean(true)); //changed here
        mandatoryMap.put("txtAge", new Boolean(false));
        mandatoryMap.put("txtPlaceOfBirth", new Boolean(false));
        mandatoryMap.put("cboReligion", new Boolean(true));
        mandatoryMap.put("cboCaste", new Boolean(true));
        mandatoryMap.put("txtHomeTown", new Boolean(true));
        mandatoryMap.put("txtIDCardNoNo", new Boolean(false));
        mandatoryMap.put("txtUIDNo", new Boolean(false));
        mandatoryMap.put("tdtDateOfJoin", new Boolean(true));
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
        mandatoryMap.put("txtNameOfSchool", new Boolean(false));
        mandatoryMap.put("cboReletaionShip", new Boolean(true));
        mandatoryMap.put("cboReleationFHTitle", new Boolean(true));
        mandatoryMap.put("txtReleationFHFirstName", new Boolean(false));
        mandatoryMap.put("txtReleationFHMiddleName", new Boolean(false));
        mandatoryMap.put("txtReleationFHLastName", new Boolean(false));
        mandatoryMap.put("tdtRelationShipDateofBirth", new Boolean(false));
        mandatoryMap.put("cboFamilyMemEducation", new Boolean(false));
        mandatoryMap.put("cboFamilyMemberProf", new Boolean(false));
        mandatoryMap.put("rdoDependentYes", new Boolean(false));
        //ADDED FROM HERE ON 15-12-2010
        mandatoryMap.put("cboBloodGroup", new Boolean(false));
        mandatoryMap.put("txtMajorHealthProbeem", new Boolean(false));
        mandatoryMap.put("txtPhysicalyHandicap", new Boolean(false));
        mandatoryMap.put("cboReligion", new Boolean(true));
        mandatoryMap.put("cboDomicileState", new Boolean(false));
        mandatoryMap.put("txtDrivingLicenceNo", new Boolean(false));
        
        //educational details
        
        mandatoryMap.put("cboEmpLevelEducation", new Boolean(false));
        mandatoryMap.put("cboGrade", new Boolean(false));
        mandatoryMap.put("txtTotMarksPer", new Boolean(false));
        mandatoryMap.put("tdtDateOfPassing", new Boolean(false));
        mandatoryMap.put("cboSpecilization", new Boolean(false));
        mandatoryMap.put("txtMarksScored", new Boolean(false));
        mandatoryMap.put("txtUniversity", new Boolean(false));
        mandatoryMap.put("txtTotMarks", new Boolean(false));
        mandatoryMap.put("txtNameOfSchool", new Boolean(false));
        mandatoryMap.put("cboLanguageType", new Boolean(false));
        mandatoryMap.put("rdoLanguage_Read", new Boolean(false));
        mandatoryMap.put("cboTechnicalQualificationType", new Boolean(false));
        mandatoryMap.put("cboTechnicalQualificationGrade", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationTotMarksPer", new Boolean(false));
        mandatoryMap.put("tdtTechnicalQualificationDateOfPassing", new Boolean(false));
        mandatoryMap.put("cboTechnicalQualificationSpecilization", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationUniversity", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationMarksScored", new Boolean(false));
        mandatoryMap.put("txtTechnicalQualificationTotMarks", new Boolean(false));
        mandatoryMap.put("txtNameOfTechInst", new Boolean(false));
        
        //loans details
        mandatoryMap.put("cboEmployeeLoanType", new Boolean(false));
        mandatoryMap.put("cboLoanAvailedBranch", new Boolean(false));
        mandatoryMap.put("txtLoanSanctionRefNo", new Boolean(false));
        mandatoryMap.put("tdtLoanSanctionDate", new Boolean(false));
        mandatoryMap.put("txtLoanNo", new Boolean(false));
        mandatoryMap.put("txtLoanAmount", new Boolean(false));
        mandatoryMap.put("txtLoanRateofInterest", new Boolean(false));
        mandatoryMap.put("txtLoanInstallmentAmount", new Boolean(false));
        mandatoryMap.put("txtLoanNoOfInstallments", new Boolean(false));
        mandatoryMap.put("tdtLoanRepaymentStartDate", new Boolean(false));
        mandatoryMap.put("tdtLoanCloserDate", new Boolean(false));
        mandatoryMap.put("rdoLoanPreCloser_Yes", new Boolean(false));
        mandatoryMap.put("tdtLoanRepaymentEndDate", new Boolean(false));
        mandatoryMap.put("txtLoanRemarks", new Boolean(false));
        mandatoryMap.put("txtAccountNo", new Boolean(false));
        mandatoryMap.put("cboAccountType", new Boolean(false));
        mandatoryMap.put("cboSalaryCrBranch", new Boolean(false));
        
        //personal details
        
        mandatoryMap.put("rdoGradutionIncrementYes", new Boolean(false));
        mandatoryMap.put("rdoCAIIBPART1IncrementYes", new Boolean(false));
        mandatoryMap.put("tdtGradutionIncrementReleasedDate", new Boolean(false));
        mandatoryMap.put("tdtCAIIBPART1IncrementReleasedDate", new Boolean(false));
        mandatoryMap.put("rdoCAIIBPART2Increment_Yes", new Boolean(false));
        mandatoryMap.put("tdtCAIIBPART2IncrementReleasedDate", new Boolean(false));
        mandatoryMap.put("rdoAnyOtherIncrement_Yes", new Boolean(false));
        mandatoryMap.put("txtAnyOtherIncrementInstitutionName", new Boolean(false));
        mandatoryMap.put("tdtAnyOtherIncrementReleasedDate", new Boolean(false));
        mandatoryMap.put("tdtLastIncrmentDate", new Boolean(false));
        mandatoryMap.put("txtLossPay_Months", new Boolean(false));
        mandatoryMap.put("txtLossOfpay_Days", new Boolean(false));
        mandatoryMap.put("tdtNextIncrmentDate", new Boolean(false));
        mandatoryMap.put("txtPresentBasic", new Boolean(false));
        mandatoryMap.put("rdoSignature_Yes", new Boolean(false));
        mandatoryMap.put("txtSignatureNo", new Boolean(false));
        mandatoryMap.put("txtSocietyMemberNo", new Boolean(false));
        mandatoryMap.put("cboSocietyMember", new Boolean(false));
        mandatoryMap.put("cboUnionMember", new Boolean(false));
        mandatoryMap.put("txtClubName", new Boolean(false));
        mandatoryMap.put("rdoClubMembership_YES", new Boolean(false));
        mandatoryMap.put("cboZonalOffice", new Boolean(true));
        mandatoryMap.put("cboPresentBranchId", new Boolean(true));
        mandatoryMap.put("tdtWorkingSince", new Boolean(false));
        mandatoryMap.put("cboReginoalOffice", new Boolean(false));
        mandatoryMap.put("cboPresentGrade", new Boolean(true));
        mandatoryMap.put("cboDesignation", new Boolean(false));
        mandatoryMap.put("txtProbationPeriod", new Boolean(false));
        mandatoryMap.put("cboProbationPeriod", new Boolean(false));
        mandatoryMap.put("tdtConfirmationDate", new Boolean(true));
        mandatoryMap.put("tdtDateofRetirement", new Boolean(false));
        mandatoryMap.put("tdtDateOfJoin", new Boolean(true));
        mandatoryMap.put("txtPFNumber", new Boolean(false));
        mandatoryMap.put("cboPFAcNominee", new Boolean(false));
//        Promotion Details
        mandatoryMap.put("cboPromotedDesignation", new Boolean(true));
        mandatoryMap.put("cboPromotionGradeValue",new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}