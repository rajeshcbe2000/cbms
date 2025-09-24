/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IndividualCustHashMap.java
 *
 * Created on Tue Nov 23 11:53:26 GMT+05:30 2004
 */

package com.see.truetransact.ui.customer;


import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class IndividualCustHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public IndividualCustHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerID", new Boolean(false));
//        mandatoryMap.put("tdtDateOfBirth", new Boolean(true));
        mandatoryMap.put("cboTitle", new Boolean(true));
        mandatoryMap.put("cboCustomerGroup", new Boolean(false));
        mandatoryMap.put("txtNationality", new Boolean(true));
        mandatoryMap.put("cboMembershipClass", new Boolean(false));
        mandatoryMap.put("cboCareOf", new Boolean(false));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtLanguage", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("rdoGender_Male", new Boolean(true));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("txtNetWorth", new Boolean(false));   //Set to false for Migration records
        mandatoryMap.put("txtCustUserid", new Boolean(true));
        mandatoryMap.put("cboPrimaryOccupation", new Boolean(true));
        mandatoryMap.put("cboVehicle", new Boolean(false));
        mandatoryMap.put("txtCustPwd", new Boolean(true));
        mandatoryMap.put("txtAreaCode", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboPhoneType", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("cboProfession", new Boolean(true));
        mandatoryMap.put("rdoMaritalStatus_Single", new Boolean(true));
        mandatoryMap.put("cboCustStatus", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboRelationManager", new Boolean(false));
        mandatoryMap.put("txtEmailID", new Boolean(false));
        mandatoryMap.put("txtLastName", new Boolean(false));
        mandatoryMap.put("txtPhoneNumber", new Boolean(true));
        mandatoryMap.put("cboAnnualIncomeLevel", new Boolean(true));
        mandatoryMap.put("txtMiddleName", new Boolean(false));
        mandatoryMap.put("cboResidentialStatus", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(false));
        mandatoryMap.put("txtTransPwd", new Boolean(true));
        mandatoryMap.put("cboPrefCommunication", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
        mandatoryMap.put("cboCustomerType", new Boolean(false));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtCompany", new Boolean(true));
        mandatoryMap.put("cboEducationalLevel", new Boolean(true));
        mandatoryMap.put("txtSsn", new Boolean(true));
        mandatoryMap.put("cboIntroType", new Boolean(true));
        mandatoryMap.put("txtAddrRemarks", new Boolean(false));
        mandatoryMap.put("chkAddrVerified", new Boolean(true));
        mandatoryMap.put("chkPhVerified", new Boolean(false));
        mandatoryMap.put("chkFinanceStmtVerified", new Boolean(false));
        mandatoryMap.put("tdtCrAvldSince", new Boolean(true));
        mandatoryMap.put("txtRiskRate", new Boolean(true));
        mandatoryMap.put("txtBranchId", new Boolean(false));
        mandatoryMap.put("txtGuardianNameNO", new Boolean(true));
        mandatoryMap.put("cboRelationNO", new Boolean(true));
        mandatoryMap.put("txtGuardianACodeNO", new Boolean(false));
        mandatoryMap.put("txtGuardianPhoneNO", new Boolean(false));
        mandatoryMap.put("txtGStreet", new Boolean(true));
        mandatoryMap.put("txtGArea", new Boolean(true));
        mandatoryMap.put("cboGCountry", new Boolean(true));
        mandatoryMap.put("cboGCity", new Boolean(true));
        mandatoryMap.put("cboGState", new Boolean(true));
        mandatoryMap.put("txtGPinCode", new Boolean(true));
        mandatoryMap.put("chkSentThanksLetter", new Boolean(false));
        mandatoryMap.put("chkConfirmationfromIntroducer", new Boolean(false));
        mandatoryMap.put("txtStaffId", new Boolean(false));
        mandatoryMap.put("txtDesignation", new Boolean(false));
        mandatoryMap.put("cboCaste", new Boolean(true));
        mandatoryMap.put("cboReligion", new Boolean(false));
        mandatoryMap.put("txtPanNumber", new Boolean(false));
        mandatoryMap.put("txtPassportFirstName", new Boolean(true));
        mandatoryMap.put("txtPassportMiddleName", new Boolean(true));
        mandatoryMap.put("tdtPassportIssueDt", new Boolean(true));
        mandatoryMap.put("tdtPassportValidUpto", new Boolean(true));
        mandatoryMap.put("txtPassportLastName", new Boolean(true));
        mandatoryMap.put("cboPassportTitle", new Boolean(true));
        mandatoryMap.put("txtPassportNo", new Boolean(true));
        mandatoryMap.put("txtPassportIssueAuth", new Boolean(true));
        mandatoryMap.put("cboPassportIssuePlace", new Boolean(true));
        mandatoryMap.put("txtAge", new Boolean(false));
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
        mandatoryMap.put("rdoITDec_Pan", new Boolean(true));
        mandatoryMap.put("cbcomboDesam", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}