/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BranchManagementHashMap.java
 */

package com.see.truetransact.ui.sysadmin.branch;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class BranchManagementHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public BranchManagementHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtPinCode", new Boolean(true));
        mandatoryMap.put("txtIPDataBC", new Boolean(false));
        mandatoryMap.put("txtBranchShortName", new Boolean(true));
        mandatoryMap.put("cboFromHrs", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtRegionalOffice", new Boolean(false));
        mandatoryMap.put("rdoBalanceLimitBP_Yes", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("txtUserIDDataBC", new Boolean(false));
        mandatoryMap.put("cboToMin", new Boolean(true));
        mandatoryMap.put("txtDriverDataBC", new Boolean(false));
        mandatoryMap.put("cboContactType", new Boolean(false));
        mandatoryMap.put("txtAvgCashStockBP", new Boolean(true));
        mandatoryMap.put("cboFromMin", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("txtPortAppBC", new Boolean(false));
        mandatoryMap.put("txtAreaCode", new Boolean(true));
        mandatoryMap.put("txtMICRCode", new Boolean(false));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboBranchGroup", new Boolean(true));
        mandatoryMap.put("cboBranchGLGroup", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtMaxCashStockBP", new Boolean(true));
        mandatoryMap.put("txtIPAppBC", new Boolean(false));
        mandatoryMap.put("tdtOpeningDate", new Boolean(true));
        mandatoryMap.put("txtPasswordDataBC", new Boolean(false));
        mandatoryMap.put("cboToHrs", new Boolean(true));
        mandatoryMap.put("txtContactNo", new Boolean(false));
        mandatoryMap.put("txtPortDataBC", new Boolean(false));
        mandatoryMap.put("txtDBNameDataBC", new Boolean(false));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtURLDataBC", new Boolean(false));
        mandatoryMap.put("txtBranchManagerID", new Boolean(false));
        mandatoryMap.put("txtBSRCode", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
