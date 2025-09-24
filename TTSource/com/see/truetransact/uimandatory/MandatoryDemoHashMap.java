/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.uimandatory;
import java.util.HashMap;
public class MandatoryDemoHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MandatoryDemoHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("jList_Language", new Boolean(false));
        mandatoryMap.put("jCheckBox_Password", new Boolean(true));
        mandatoryMap.put("jTextField_UserName", new Boolean(true));
        mandatoryMap.put("jRadioButton_Gender_Male", new Boolean(true));
        mandatoryMap.put("jTextArea_Description", new Boolean(true));
        mandatoryMap.put("jComboBox_Technology", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
