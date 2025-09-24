/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ChequeBookMRB.java
 */
package com.see.truetransact.ui.supporting.chequebook;
import java.util.ListResourceBundle;
public class ChequeBookMRB extends ListResourceBundle {
    public ChequeBookMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboNoOfLeaves", "NoOfLeaves should be a proper value!!!"},
        {"txtEndingCheque", "Endingcheque should not be empty!!!"},
        {"txtSeriesFrom", "SeriesFrom should not be empty!!!"},
        {"txtEndingchequeNo", "EndingchequeNo should not be empty!!!"},
        {"cboProductId", "ProductId should be a proper value!!!"},
        {"rdoLeaf_single", "Leaf should be selected!!!"},
        {"tdtChequeDate", "ChequeDate should not be empty!!!"},
        {"txtEndingChequeNo", "EndingChequeNo should not be empty!!!"},
        {"txtStopPaymentCharges", "StopPaymentCharges should not be empty!!!"},
        {"txtNamesOfAccount", "NamesOfAccount should not be empty!!!"},
        {"txtPayeeName", "PayeeName should not be empty!!!"},
        {"cboProdId", "ProdId should be a proper value!!!"},
        {"cboMethodOfDelivery", "MethodOfDelivery should be a proper value!!!"},
        {"txtAccNo", "AccNo should not be empty!!!"},
        {"txtChequeAmt", "ChequeAmt should not be empty!!!"},
        {"txtChargesCollected", "ChargesCollected should not be empty!!!"},
        {"txtStartingCheque", "StartingCheque Prefix should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtRemark", "Remark should not be empty!!!"},
        {"txtLeafNo2", "LeafNo2 should not be empty!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"txtNoOfChequeBooks", "NoOfChequeBooks should not be empty!!!"},
        {"txtLeafNo1", "LeafNo1 should not be empty!!!"},
        {"cboReasonStopPayment", "ReasonStopPayment should be a proper value!!!"},
        {"txtStartingChequeNo", "StartingChequeNo should not be empty!!!"},
        {"txtAccountNo", "AccountNo should not be empty!!!"},
        {"txtStartCheque", "StartCheque should not be empty!!!"},
        {"txtSeriesNoTo", "SeriesNoTo should not be empty!!!"},
        {"txtEndCheque", "EndCheque should not be empty!!!"},
        {"txtStartchequeNo", "StartchequeNo should not be empty!!!"},
        {"txtAccounNumber", "AccounNumber should not be empty!!!"} ,
        {"cboUsage", "Usage should be a proper value!!!"} ,
         {"cboProductType", "ProductType should be a proper value!!!"} ,
        {"cboEcsProductType", "ProductType should be a proper value!!!"} ,
        {"cboEcsProductId", "ProductID should be a proper value!!!"} ,
        {"txtEcsAccNo",     "Account No should be a proper value!!!"},
        {"txtEndEcs1", "Ecs No1 should be a proper value!!!"},
        {"txtEndEcs2", "Ecs NO2 should be a proper value!!!"},
        {"cboEcsReasonStopPayment", "Reason For Stop Payment should be a proper value!!!"},
         {"cboProduct_Type", "ProductType should be a proper value!!!"} 
        
        
        

   };

}
