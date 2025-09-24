/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * EmployeeHashMap.java
 */

package com.see.truetransact.ui.sysadmin.employee;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class EmployeeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public EmployeeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTitle", new Boolean(false));
        mandatoryMap.put("cboMartialStatus", new Boolean(true));
        mandatoryMap.put("txtHomePhone", new Boolean(false));
        mandatoryMap.put("txtPinCode", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txaEducation", new Boolean(false));
        mandatoryMap.put("txtAlternateEmail", new Boolean(false));
        mandatoryMap.put("cboEmployeeType", new Boolean(true));
        mandatoryMap.put("tdtLeavingDate", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("txaResponsibility", new Boolean(false));
        mandatoryMap.put("txtPanNo", new Boolean(false));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("rdoGender_Male", new Boolean(false));
        mandatoryMap.put("cboManager", new Boolean(false));
        mandatoryMap.put("tdtBirthDate", new Boolean(false));
        mandatoryMap.put("txtSsnNo", new Boolean(false));
        mandatoryMap.put("txtOfficePhone", new Boolean(true));
        mandatoryMap.put("txaPerformance", new Boolean(false));
        mandatoryMap.put("cboDepartment", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("txtPassPortNo", new Boolean(false));
        mandatoryMap.put("txaExperience", new Boolean(false));
        mandatoryMap.put("txtLastName", new Boolean(true));
        mandatoryMap.put("tdtJoiningDate", new Boolean(false));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboJobTitle", new Boolean(true));
        mandatoryMap.put("tdtWeddindDate", new Boolean(false));
        mandatoryMap.put("txtCellular", new Boolean(false));
        mandatoryMap.put("txtEmployeeId", new Boolean(false));
        mandatoryMap.put("txaSkills", new Boolean(false));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtOfficialEmail", new Boolean(true));
        mandatoryMap.put("txaComments", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
