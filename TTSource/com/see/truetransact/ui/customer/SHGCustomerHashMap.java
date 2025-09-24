/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SHGCustomerHashMap.java
 */
package com.see.truetransact.ui.customer;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class SHGCustomerHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public SHGCustomerHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerID", new Boolean(false));
        mandatoryMap.put("cboTitle", new Boolean(true));
        mandatoryMap.put("txtWebSite", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtAuthCustId", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("rdoGender_Male", new Boolean(true));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("txtOtherApplNo", new Boolean(true));
        mandatoryMap.put("txtNetWorth", new Boolean(false));   //Set to false for Migration records
        mandatoryMap.put("txtCustUserid", new Boolean(true));
        mandatoryMap.put("txtAreaCode", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("txtCustPwd", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboPhoneType", new Boolean(true));
        mandatoryMap.put("txtFirstName", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboRelationManager", new Boolean(false));
        mandatoryMap.put("txtLastName", new Boolean(true));
        mandatoryMap.put("txtEmailID", new Boolean(false));
        mandatoryMap.put("txtPhoneNumber", new Boolean(true));
        mandatoryMap.put("txtMiddleName", new Boolean(false));
        mandatoryMap.put("cboPrefCommunication", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(false));
        mandatoryMap.put("cboCustomerType", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtCompany", new Boolean(true));
        mandatoryMap.put("cboBusNature", new Boolean(true));
        mandatoryMap.put("txtRegNumber", new Boolean(true));
        mandatoryMap.put("tdtDtEstablished", new Boolean(true));
        mandatoryMap.put("txtCEO", new Boolean(true));
        mandatoryMap.put("cboIntroType", new Boolean(true));
        mandatoryMap.put("tdtNetWorthAsOn", new Boolean(false));
        mandatoryMap.put("txtPanNumber", new Boolean(false));
        mandatoryMap.put("chkAddrVerified", new Boolean(true));
        mandatoryMap.put("cboMembershipClass", new Boolean(true));
        mandatoryMap.put("rdoITDec_Pan", new Boolean(true));
        
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
