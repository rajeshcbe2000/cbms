/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DrawingPowerMaintenanceMRB.java
 * 
 * Created on Fri Jul 16 10:41:26 GMT+05:30 2004
 */

package com.see.truetransact.ui.termloan.drawingpower;

import java.util.ListResourceBundle;

public class DrawingPowerMaintenanceMRB extends ListResourceBundle {
    public DrawingPowerMaintenanceMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboCurrentDPMonth", "CurrentDPMonth should be a proper value!!!"},
        {"txtMargin", "Margin should not be empty!!!"},
        {"txtPurchase", "Purchase should not be empty!!!"},
        {"dateDateofInspection", "Date of Inspection should not be empty!!!"},
        {"txtParticularsofGoods", "ParticularsofGoods should not be empty!!!"},
        {"cboPreviousDPMonth", "PreviousDPMonth should be a proper value!!!"},
        {"cboStockStmtFreq", "StockStmtFreq should be a proper value!!!"},
        {"txtValueofOpeningStock", "ValueofOpeningStock should not be empty!!!"},
        {"dateDueDate", "Due Date should not be empty!!!"},
        {"txtCalculatedDrawingPower", "CalculatedDrawingPower should not be empty!!!"},
        {"dateStockSubmittedDate", "Stock Submitted Date should not be empty!!!"},
        {"txtPreviousDPValue", "PreviousDPValue should not be empty!!!"},
        {"txtSales", "Sales should not be empty!!!"},
        {"txtValueofClosingStock", "ValueofClosingStock should not be empty!!!"},
        {"datePreviousDPValCalOn", "Previous DP Val Cal On should not be empty!!!"},
        {"txtPresentStockValue", "PresentStockValue should not be empty!!!"} 

   };

}
