/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeTrainingRB.java
 *
 * Created on Thu Jan 20 15:41:51 IST 2005
 * 
 * Modified by anjuanand on 08/12/2014
 * 
 */

package com.see.truetransact.ui.payroll.employeeTraining;

import java.util.ListResourceBundle;

public class EmployeeTrainingRB extends ListResourceBundle {
    public EmployeeTrainingRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblTrainingID", "Training ID"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblTrainDest", "Training Destination"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblLocation", "Location"},
        {"btnNew", ""},
        {"lblCondTeam", "Conducting Team"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"lblSubj", "Subject"},
        {"btnPrint", ""} ,
        {"btnUserId", ""},
        {"lblSize", "Team Size"},
        {"cDialogOK", "OK"},
        {"lblNoOfTrainees", "No Of Trainees"},
        {"lblFromDt", "Training From"},
        {"lblToDt", "Training To"},
        {"lblEmpRemarks", "Remarks"},
        {"lblEmpID", "Employee ID"}
    };
    
}
