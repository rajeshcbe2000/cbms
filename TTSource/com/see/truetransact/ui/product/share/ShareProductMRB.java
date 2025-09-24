/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.product.share;

import java.util.ListResourceBundle;

public class ShareProductMRB extends ListResourceBundle {
    public ShareProductMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboAdditionalShareRefund", "AdditionalShareRefund should be a proper value!!!"},
        {"cboUnclaimedDivPeriod", "UnclaimedDivPeriod should be a proper value!!!"},
        {"cboShareType", "ShareType should be a proper value!!!"},
        {"cboDivCalcFrequency", "DivCalcFrequency should be a proper value!!!"},
        {"cboNomineePeriod", "NomineePeriod should be a proper value!!!"},
        {"txtMaxShareHolding", "MaxShareHolding should not be empty!!!"},
        {"txtAdmissionFee", "AdmissionFee should not be empty!!!"},
        {"tdtDueDate", "DueDate should not be empty!!!"},
        {"txtSurityLimit", "SurityLimit should not be empty!!!"},
        {"txtAdditionalShareRefund", "AdditionalShareRefund should not be empty!!!"},
        {"txtDividentPercentage", "DividentPercentage should not be empty!!!"},
        {"txtNomineeaAllowed", "NomineeaAllowed should not be empty!!!"},
        {"cboRefundPeriod", "RefundPeriod should be a proper value!!!"},
        {"txtMaxLoanLimit", "MaxLoanLimit should not be empty!!!"},
        {"txtNomineePeriod", "NomineePeriod should not be empty!!!"},
        {"cboLoanType", "LoanType should be a proper value!!!"},
        {"tdtCalculatedDate", "CalculatedDate should not be empty!!!"},
        {"cboDivAppFrequency", "DivAppFrequency should be a proper value!!!"},
        {"txtApplicationFee", "ApplicationFee should not be empty!!!"},
        {"txtShareFee", "ShareFee should not be empty!!!"},
        {"txtSubscribedCapital", "SubscribedCapital should not be empty!!!"},
        {"txtRefundPeriod", "RefundPeriod should not be empty!!!"},
        {"txtFaceValue", "FaceValue should not be empty!!!"},
        {"txtShareSuspAccount", "ShareSuspAccount should not be empty!!!"},
        {"txtLoanAvailingLimit", "LoanAvailingLimit should not be empty!!!"},
        {"txtPaidupCapital", "PaidupCapital should not be empty!!!"},
        {"chkReqActHolder", "ReqActHolder should be selected!!!"},
        {"txtSecuredAdvances", "SecuredAdvances should not be empty!!!"},
        {"txtRefundinaYear", "RefundinaYear should not be empty!!!"},
        {"txtUnclaimedDivPeriod", "UnclaimedDivPeriod should not be empty!!!"},
        {"txtUnsecuredAdvances", "UnsecuredAdvances should not be empty!!!"},
        {"txtIssuedCapital", "Issued Capital should not be empty!!!"},
        {"txtShareAccount", "Share Account should not be empty!!!"},
        {"txtMemFeeAcct", "Membership Fee Account should not be empty!!!"},
        {"txtApplFeeAcct", "Application Fee Account should not be empty!!!"},
        {"txtShareFeeAcct", "Share Fee Account should not be empty!!!"},
        {"txtMaxLoanAmt", "MaxLoanAmount should not be empty!!!"}

   };

}
