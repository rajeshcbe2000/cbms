/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeHashMap.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.employee;

/**
 *
 * @author anjuanand
 */
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class EmployeeHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public EmployeeHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTitle", new Boolean(false));
        mandatoryMap.put("cboMartialStatus", new Boolean(false));
        mandatoryMap.put("txtHomePhone", new Boolean(false));
        mandatoryMap.put("txtPinCode", new Boolean(false));
        mandatoryMap.put("cboCountry", new Boolean(false));
        mandatoryMap.put("txaEducation", new Boolean(false));
        mandatoryMap.put("txtAlternateEmail", new Boolean(false));
        mandatoryMap.put("txaResponsibility", new Boolean(false));
        mandatoryMap.put("txtPanNo", new Boolean(false));
        mandatoryMap.put("rdoGender_Male", new Boolean(false));
        mandatoryMap.put("cboManager", new Boolean(false));
        mandatoryMap.put("txtSsnNo", new Boolean(false));
        mandatoryMap.put("txaPerformance", new Boolean(false));
        mandatoryMap.put("txtArea", new Boolean(false));
        mandatoryMap.put("txtPassPortNo", new Boolean(false));
        mandatoryMap.put("txaExperience", new Boolean(false));
        mandatoryMap.put("txtLastName", new Boolean(false));
        mandatoryMap.put("cboState", new Boolean(false));
        mandatoryMap.put("tdtWeddindDate", new Boolean(false));
        mandatoryMap.put("txtCellular", new Boolean(false));
        mandatoryMap.put("txtEmployeeId", new Boolean(false));
        mandatoryMap.put("txaSkills", new Boolean(false));
        mandatoryMap.put("txtStreet", new Boolean(false));
        mandatoryMap.put("txaComments", new Boolean(false));
        mandatoryMap.put("txtCustomerId", new Boolean(false));
        mandatoryMap.put("cboAddressType", new Boolean(false));
        mandatoryMap.put("tdtLastIncrementDate", new Boolean(false));
        mandatoryMap.put("tdtNextIncrementDate", new Boolean(false));
        mandatoryMap.put("tdtDateOfRetirement", new Boolean(false));
        mandatoryMap.put("cboNetSalaryProductType", new Boolean(false));
        mandatoryMap.put("cboNetSalaryProductId", new Boolean(false));
        mandatoryMap.put("txtNetSalaryAccNo", new Boolean(false));
        mandatoryMap.put("txtCustomerName", new Boolean(false));
        mandatoryMap.put("txtPensionOpeningBalance", new Boolean(false));
        mandatoryMap.put("tdtPensionOpeningBalanceOn", new Boolean(false));
        mandatoryMap.put("txtWfOpeningBalance", new Boolean(false));
        mandatoryMap.put("tdtWfOpeningBalanceOn", new Boolean(false));
        mandatoryMap.put("txtSortOrder", new Boolean(false));
        mandatoryMap.put("txtOfficePhone", new Boolean(false));
        mandatoryMap.put("txtOfficialEmail", new Boolean(false));
        mandatoryMap.put("txtCustomerId", new Boolean(true));
        mandatoryMap.put("txtEmployeeId", new Boolean(false));
        mandatoryMap.put("tdtBirthDate", new Boolean(true));
        mandatoryMap.put("cboDepartment", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("cboJobTitle", new Boolean(true));
        mandatoryMap.put("cboEmployeeType", new Boolean(true));
        mandatoryMap.put("tdtLastIncrementDate", new Boolean(true));
        mandatoryMap.put("tdtNextIncrementDate", new Boolean(true));
        mandatoryMap.put("cboDesignation", new Boolean(true));
        mandatoryMap.put("txtScaleId", new Boolean(true));
        mandatoryMap.put("tdtEffectiveDate", new Boolean(true));
        mandatoryMap.put("txtIncrementCount", new Boolean(true));
        mandatoryMap.put("txtPresentBasicSalary", new Boolean(true));
        mandatoryMap.put("tdtDateOfJoin", new Boolean(true));
        mandatoryMap.put("tdtProbationEndDate", new Boolean(true));
        mandatoryMap.put("cboStatusOfEmp", new Boolean(true));
        mandatoryMap.put("txtPensionCodeNo", new Boolean(true));
        mandatoryMap.put("txtWfNumber", new Boolean(true));
        mandatoryMap.put("cboManager", new Boolean(true));
        mandatoryMap.put("txtVersionNo", new Boolean(false));
        mandatoryMap.put("cboBranch", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}
