
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MDSMasterMaintenanceHashMap.java
 */

package com.see.truetransact.ui.mdsapplication.mdsmastermaintenance;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MDSMasterMaintenanceHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MDSMasterMaintenanceHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtOverDueAmount", new Boolean(false));
        mandatoryMap.put("txtTotalAmountTillDate", new Boolean(false));
        mandatoryMap.put("txtLastInstNo", new Boolean(false));
        mandatoryMap.put("txtOverDueInstallments", new Boolean(false));
        mandatoryMap.put("tdtLastInstDate", new Boolean(false));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("tdtChitStartDt", new Boolean(false));
        mandatoryMap.put("txtDivisionNo", new Boolean(false));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtMemberNo", new Boolean(true));
        mandatoryMap.put("txtBondNo", new Boolean(false));
        mandatoryMap.put("tdtBondDt", new Boolean(false));
        mandatoryMap.put("txtPrizedAmount", new Boolean(false));
        mandatoryMap.put("tdtResolutionDt", new Boolean(false));
        mandatoryMap.put("tdtPayDt", new Boolean(false));
        mandatoryMap.put("txtResolutionNo", new Boolean(false));
        mandatoryMap.put("txtContactNo", new Boolean(false));
        mandatoryMap.put("txtMemberNum", new Boolean(false));
        mandatoryMap.put("txtSalaryRemark", new Boolean(false));
        mandatoryMap.put("txtDesignation", new Boolean(false));
        mandatoryMap.put("txtAddress", new Boolean(false));
        mandatoryMap.put("txtEmployerName", new Boolean(false));
        mandatoryMap.put("txtSalaryCertificateNo", new Boolean(false));
        mandatoryMap.put("txtTotalSalary", new Boolean(false));
        mandatoryMap.put("txtNetWorth", new Boolean(false));
        mandatoryMap.put("txtPinCode", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(false));
        mandatoryMap.put("tdtRetirementDt", new Boolean(false));
        mandatoryMap.put("txtMemNetworth", new Boolean(false));
        mandatoryMap.put("txtContactNum", new Boolean(false));
        mandatoryMap.put("txtMemType", new Boolean(false));
        mandatoryMap.put("txtMemName", new Boolean(false));
        mandatoryMap.put("txtMemNo", new Boolean(false));
        mandatoryMap.put("txtSecurityRemarks", new Boolean(false));
        mandatoryMap.put("cboSecurityType", new Boolean(false));
        mandatoryMap.put("txtGoldRemarks", new Boolean(false));
        mandatoryMap.put("txtValueOfGold", new Boolean(false));
        mandatoryMap.put("txtNetWeight", new Boolean(false));
        mandatoryMap.put("txtGrossWeight", new Boolean(false));
        mandatoryMap.put("txtJewelleryDetails", new Boolean(false));
        mandatoryMap.put("txtDepAmount", new Boolean(false));
        mandatoryMap.put("txtMaturityDt", new Boolean(false));
        mandatoryMap.put("txtMaturityValue", new Boolean(false));
        mandatoryMap.put("txtProductId", new Boolean(false));
        mandatoryMap.put("txtRateOfInterest", new Boolean(false));
        mandatoryMap.put("txtDepNo", new Boolean(false));
        mandatoryMap.put("tdtDepDt", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
