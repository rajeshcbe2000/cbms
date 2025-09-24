/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeTrainingHashMap.java
 *
 */

package com.see.truetransact.ui.payroll.employeeTraining;

/*
 * 
 * Modified by anjuanand on 08/12/2014
 * 
 */
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class EmployeeTrainingHashMap implements UIMandatoryHashMap {
    
    private HashMap mandatoryMap;
    
    public EmployeeTrainingHashMap(){
        
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTrainDest", new Boolean(true));
        mandatoryMap.put("txtLocation", new Boolean(true));
        mandatoryMap.put("txtCondTeam", new Boolean(true));
        mandatoryMap.put("txtSize", new Boolean(true));
        mandatoryMap.put("txtNoOfTrainees", new Boolean(true));
        mandatoryMap.put("tdtFrom", new Boolean(true));
        mandatoryMap.put("tdtTo", new Boolean(true));
        mandatoryMap.put("txtEmpRemarks", new Boolean(false));
        mandatoryMap.put("txtSubj", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
