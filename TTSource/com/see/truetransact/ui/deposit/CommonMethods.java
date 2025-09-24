/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CommoMethods.java
 *
 * Created on December 27, 2004, 4:37 PM
 */

package com.see.truetransact.ui.deposit;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;

/**
 *
 * @author  K.R.Jayakrishnan
 */
public class CommonMethods {
    
    /** Creates a new instance of CommoMethods */
    public CommonMethods() {
    }
 
    private static final String DIALOG_YES = "Yes";
    private static final String DIALOG_OK = "OK";
    private static final String DIALOG_NO = "No";
    private static final String WARN_MESSAGE = "This Record Already exist. Do you want to replace it?";
    private static final String WARN_MESSAGE_OK = "This Record Already exists";

    CommonRB objCommonRB = new CommonRB();
    
    public static int showDialogYesNo(String dialogString){
        COptionPane dialog = new COptionPane();
        String[] strDialog = {DIALOG_YES,DIALOG_NO};
        int yesOrNo = dialog.showOptionDialog(null,dialogString,CommonConstants.WARNINGTITLE,COptionPane.YES_NO_OPTION,COptionPane.WARNING_MESSAGE, null, strDialog, strDialog[0]);
        return yesOrNo;
    }
    
    public static int showDialogOk(String dialogString){
        COptionPane dialog = new COptionPane();
        String[] strDialog = {DIALOG_OK};
        int checkDt = dialog.showOptionDialog(null,dialogString,CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION,COptionPane.WARNING_MESSAGE, null, strDialog, strDialog[0]);
        return checkDt;
    }
    
   public static void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
}
