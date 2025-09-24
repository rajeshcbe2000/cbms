

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NbChequeBookMaintenanceRB.java
 */

package com.see.truetransact.ui.netbankingrequest.nbchequebookmaintenance;
import java.util.ListResourceBundle;
public class NbChequeBookMaintenanceRB extends ListResourceBundle {
    public NbChequeBookMaintenanceRB(){
    }
    
    public Object[][] getContents() {
    return contents;
    }
    
     static final String[][] contents = {
         {"lblCustid", "Customer Id"},
         {"lblCustid2", ""},
         {"lblAccNo", "Account Number"},
         {"lblAccNo2", ""},
         {"lblCbRequest", "CB Request"},
         {"lblCbRequest2", ""},
         {"lblNoOfCbLeave", "No Of CB Leave"},
         {"lblNoOfCbLeave2", ""},
         {"lblUsageType", "Usage Type"},
         {"lblUsageType2", ""},
         {"btnAuthorize", ""},
         {"btnCancel", ""},
         {"btnClose", ""},
         {"btnDelete", ""},
         {"btnEdit", ""},
         {"btnException", ""},
         {"btnNew", ""},
         {"btnPrint", ""},
         {"btnReject", ""},
         {"btnSave", ""}
     };
    
}
