/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanDisbursementHashMap.java
 *
 * Created on August 24, 2009, 3:08 PM
 */

package com.see.truetransact.ui.termloan.loandisbursement;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  abi
 */
public class LoanDisbursementHashMap implements UIMandatoryHashMap {
      private HashMap mandatoryMap;
    /** Creates a new instance of LoanDisbursementHashMap */
    public LoanDisbursementHashMap() {
         mandatoryMap = new HashMap();
         mandatoryMap.put("cboPurpose", new Boolean(true));
         mandatoryMap.put("cboType", new Boolean(true));
         mandatoryMap.put("txtHectare", new Boolean(false));
         mandatoryMap.put("txtSurveyNo", new Boolean(false));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
    
}
