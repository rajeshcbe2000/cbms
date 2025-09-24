/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizedSignatoryHashMap.java
 *
 * Created on December 23, 2004, 11:30 AM
 */

package com.see.truetransact.ui.common.authorizedsignatory;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  152713
 */
public class AuthorizedSignatoryHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    /** Creates a new instance of AuthorizedSignatoryHashMap */
    public AuthorizedSignatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtPager_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtHomeFax_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtState_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtName_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("cboAddrCommunication_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtArea_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtMobile_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtPin_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtBusinessFax_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtBusinessPhone_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtDesig_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("cboCity_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtStreet_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtAreaCode_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("cboCountry_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtEmailId_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtHomePhone_AuthorizedSignatory", new Boolean(false));
        mandatoryMap.put("txtLimits", new Boolean(true));
        mandatoryMap.put("txtNumberAuthSignatory", new Boolean(true));
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("txtInstruction", new Boolean(true));
        mandatoryMap.put("txtFromAmount", new Boolean(true));
        mandatoryMap.put("txtToAmount", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
    
}
