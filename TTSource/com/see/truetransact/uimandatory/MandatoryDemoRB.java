/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MandatoryConstants.java
 *
 * Created on July 28, 2003, 2:08 PM
 */

package com.see.truetransact.uimandatory;
import java.util.ListResourceBundle;
/**
 *
 * @author  karthik
 */
public class MandatoryDemoRB extends ListResourceBundle{
    static final String[][] contents = {
              {"mandatory_file_path", "E:/TTFramework/JavaBean/com/see/truetransact/uimandatory/MandatoryDemoHashMap.java"},
              {"jTextField_UserName", "UserName should not be empty!!!"},
              {"jCheckBox_Password", "Password should be selected!!!"},
              {"jRadioButton_Gender_Male", "Gender should be selected!!!"},
              {"jComboBox_Technology", "ComboBoxTechnology should be a proper value!!!"},
              {"jList_Language", "ListLanguage should be selected!!!"},
              {"jTextArea_Description", "Description should not be empty!!!"}
      };
    /** Creates a new instance of MandatoryDemoRB */
      public MandatoryDemoRB() {
    }
    
    /** To get all the contents
     * @return To return contents
     */    
    public Object[][] getContents() {
        return contents;
    }
}
