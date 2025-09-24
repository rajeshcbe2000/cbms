/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsRB.java
 * 
 * Created on Sat Feb 05 14:16:21 IST 2005
 */

package com.see.truetransact.ui.product.locker;

import java.util.ListResourceBundle;

public class LockerProdRB extends ListResourceBundle {
    public LockerProdRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblOverdueRateCBP", "Overdue Rate For CBP"},
        {"btnClose", ""},
        {"lblOperatesLike", "Operates Like"},
        {"lblRegisterType", "Register Type"},
        {"lblSubRegType", "Sub Reg Type"},
        {"lblRateForCBP_Per", "%"},
        {"btnBillsRealisedHead1", ""},
        {"btnAuthorize", ""},
        {"lblCommissionAccountHead", "Commission Account Head"},
        {"lblMsg", ""},
        {"btnInterestAccountHead", ""},
        {"lblSpace4", "     "},
        {"lblOthersHead", "PO Account Head"},
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblBillsRealisedHD", "Bills/Cheques Realised Head"},
        {"lblStateCapMetroOthers", "Service Tax Head"},
        {"lblBaseCurrency", "Base Currency"},
        {"lblSpace1", " Status :"},
        {"btnMarginAccountHead", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblPostageAccountHead", "Postage Account Head"},
        {"btnChargesAccountHead", ""},
        {"rdoPostDtdCheqAllowed_No", "No"},
        {"lblInterestAccountHead", "Interest Account Head"},
        {"lblAtParLimit", "At Par Limit"},
        {"btnPrint", ""},
        {"lblContractAccountHead", "Contract Account Head"},
        {"lblCleanBillsPurchased", "Clean Bills Purchased"},
        {"lblDiscountRateOfBD", "Discount Rate For Bills"},
        {"lblMarginAccountHead", "Margin Head"},
        {"lblDefaultPostage", "Default Postage Rate"},
        {"lblIntICC", "Collect Interest during Lodgement for ICC"},
        {"lblPostDtdCheqAllowed", "Post Dated Cheque Allowed"},
        {"rdoPostDtdCheqAllowed_Yes", "Yes"},
        {"btnOthersHead", ""},
        {"lblChargesAccountHead", "Charges Account Head"},
        {"rdoContraAccountHead_No", "No"},
        {"cRadio_ICC_Yes", "Yes"},
        {"cRadio_ICC_No", "No"},
        {"btnDDAccountHead", ""},
        {"btnException", ""},
        {"btnGLAccountHead", ""},
        {"lblDDAccountHead", "DD Account Head"},
        {"lblOverdueInterestForBD", "Overdue Interest Rate For Bills"},
        {"lblTransitPeriod", "Transit Period"},
        {"lblProductId", "Product Id"},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnContractAccountHead", ""},
        {"btnDelete", ""},
        {"lblDiscountRateBills_Per", "%"},
        {"lblProdDesc", "Product Description"},
        {"lblIBRAccountHead", "IBR Account Head"},
        {"lblTelChargesHead", "Telephone Charges Head"},
        {"btnPostageAccountHead", ""},
        {"btnNew", ""},
        {"lblOverdueRateBills_Per", "%"},
        {"btnCancel", ""},
        {"lblContraAccountHead", "Contra Account Head"},
        {"rdoContraAccountHead_Yes", "Yes"},
        {"btnCommissionAccountHead", ""},
        {"btnTelChargesHead1", ""},
        {"lblGLAccountHead", "GL Account Head"},
        {"btnIBRAccountHead", ""},
        {"btnServiceTaxHd", ""},
        {"lblCleanBills_Per", "%"},
        {"lblDefaultPostage_Per", "%"},
        {"periodMsg", "Enter a Valid Transit Period"},
        {"warningProductId", "This ProductId Already Exist !!!"}, 
        {"lblInstrument", "Instrument / Bills"},
        {"lblChargeType", "Charge Type"},
        {"lblCustCategory", "Customer Category"},
        {"lblFromSlab", "From Slab"},
        {"lblToSlab", "To Slab"},
        {"lblMinimum", "Minimum"},
        {"lblMaximum", "Maximum"},
        {"lblCommision", "Commission"},
        {"lblServiceTax", "Service Tax"},
        {"lblFixedRate", "Fixed Rate"},
        {"lblForEvery","For Every"},
        {"tblColumn1", "Prod ID"},
        {"tblColumn2", "Instrument"},
        {"tblColumn3", "Charge Type"},
         {"tblColumn5", "From Date"},
        {"tblColumn6", "To Date"},
        {"tblColumn9",   "Amt"},
        {"tblColumn4", "Service Tax"},
        {"tblColumn14","AuthorizeStatus"},
        {"tblColumn7",  "From Slab"},
        {"tblColumn8",  "To Slab"},
        
        {"tblColumn10", "Fixed Rate"},
        {"tblColumn11", "For Every Amount"},
        {"tblColumn12", "For Every Rate"},
        {"tblColumn13", "For Every Type"},
        {"chargeCheck","Charge related details should not be empty"},
        {"lblRateForDelay","Margin"},
        {"lblIntDays","No. Of Days Interest to be collected for Cheque Discount"},
   };

}