/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DrawingPowerMaintenanceUI.java
 *
 * Created on July 15, 2004, 3:03 PM
 */

package com.see.truetransact.ui.termloan.drawingpower;

import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.Date;

/**
 *
 * @author  Lohith R.
 * @modified Sunil
 * Changes done : 30 March 2005
 *  a. Added lookup for borrower number
 *  b. Populated label data based on borrower number
 *  c. Added lookup for security number
 * d. Calculation and disply of total drawing power
 */
public class DrawingPowerMaintenanceUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {
    int viewType=-1;
    private HashMap mandatoryMap;
    private DrawingPowerMaintenanceRB resourceBundle ;
    private DrawingPowerMaintenanceOB observable;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, BORROWER = 3, SECURITY = 4;
    private boolean actionAuthExcepReject = false;
    boolean isFilled = false;
    int ACTION=-1;
    private TransDetailsUI transDetailsUI = null;
    private Date currDt = null;
    /** Creates new form DrawingPowerMaintenance */
    public DrawingPowerMaintenanceUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUP();
        transDetailsUI = new TransDetailsUI(panLableValues);
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        setMaximumLength();
        setHelpMessage();
        actionAuthExcepReject = false;
        observable.clearData();
        setButtonEnableDisable();
        setDrawinPowerDetailsTabBtnEndableDisable();
        observable.removeTableRow();
        ClientUtil.enableDisable(this, false);
        observable.resetStatus();
        lblStatus.setText(observable.getLblStatus());
        observable.resetDrawingPowerFields();
        observable.resetDrawingPowerDetailsFields();
        btnBorrowerNum.setEnabled(false);
        btnDelete.setVisible(false);
        hideFields();
        
    }
    
    //Below method is used to hide non functional elements. Unhide them as required
    private void hideFields(){
        //        lblLastStockValue.setVisible(false);
        //        lblLastStckVa.setVisible(false);
        //        lblDrwPowSancted.setVisible(false);
        //        lblDrawingPowerSanctioned.setVisible(false);
        
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = DrawingPowerMaintenanceOB.getInstance();
        observable.addObserver(this);
    }
    
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDrawingPowerDetailsDelete.setName("btnDrawingPowerDetailsDelete");
        btnDrawingPowerDetailsNew.setName("btnDrawingPowerDetailsNew");
        btnDrawingPowerDetailsSave.setName("btnDrawingPowerDetailsSave");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboCurrentDPMonth.setName("cboCurrentDPMonth");
        cboPreviousDPMonth.setName("cboPreviousDPMonth");
        cboStockStmtFreq.setName("cboStockStmtFreq");
        dateDateofInspection.setName("dateDateofInspection");
        dateDueDate.setName("dateDueDate");
        datePreviousDPValCalOn.setName("datePreviousDPValCalOn");
        dateStockSubmittedDate.setName("dateStockSubmittedDate");
        lblAccHd.setName("lblAccHd");
        //        lblAccNo.setName("lblAccNo");
        lblAccountHead.setName("lblAccountHead");
        //        lblAccountNumber.setName("lblAccountNumber");
        lblBorrowerNum.setName("lblBorrowerNum");
        txtAccountNumber.setName("txtAccountNumber");
        lblCalculatedDrawingPower.setName("lblCalculatedDrawingPower");
        lblCurrentDPMonth.setName("lblCurrentDPMonth");
        lblDateofInspection.setName("lblDateofInspection");
        //        lblDrawingPowerSanctioned.setName("lblDrawingPowerSanctioned");
        //        lblDrwPowSancted.setName("lblDrwPowSancted");
        lblDueDate.setName("lblDueDate");
        //        lblLastStckVa.setName("lblLastStckVa");
        //        lblLastStockValue.setName("lblLastStockValue");
        lblMargin.setName("lblMargin");
        lblMsg.setName("lblMsg");
        lblParticularsofGoods.setName("lblParticularsofGoods");
        lblPresentStockValue.setName("lblPresentStockValue");
        lblPreviousDPMonth.setName("lblPreviousDPMonth");
        lblPreviousDPValCalOn.setName("lblPreviousDPValCalOn");
        lblPreviousDPValue.setName("lblPreviousDPValue");
        lblProdID.setName("lblProdID");
        lblProductID.setName("lblProductID");
        lblPurchase.setName("lblPurchase");
        lblSales.setName("lblSales");
        //        lblSecurityNo.setName("lblSecurityNo");
        //        lblSecurityNumber.setName("lblSecurityNumber");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblStockStmtFreq.setName("lblStockStmtFreq");
        lblStockSubmittedDate.setName("lblStockSubmittedDate");
        //        lblTotalDrawingPower.setName("lblTotalDrawingPower");
        //        lblTtlDrwingPow.setName("lblTtlDrwingPow");
        lblValueofClosingStock.setName("lblValueofClosingStock");
        lblValueofOpeningStock.setName("lblValueofOpeningStock");
        lblCustId.setName("lblCustId");
        lblCustIdValue.setName("lblCustIdValue");
//        lblCustomerName.setName("lblCustomerName");
        lblCustomerNameValue.setName("lblCustomerNameValue");
        mbrMain.setName("mbrMain");
        panDrawingPower.setName("panDrawingPower");
        panDrawingPowerDetails.setName("panDrawingPowerDetails");
        panDrawingPowerDetailsButton.setName("panDrawingPowerDetailsButton");
        //        panDrawingPowerDetailsFields.setName("panDrawingPowerDetailsFields");
        //        panDrawingPowerDetailsFieldsButton.setName("panDrawingPowerDetailsFieldsButton");
        panMain.setName("panMain");
        panStatus.setName("panStatus");
        //        panTotalDrawingPower.setName("panTotalDrawingPower");
        //        sprDrawingPowerDetailsTab.setName("sprDrawingPowerDetailsTab");
        //        sptDrawingPowerDetailsFields.setName("sptDrawingPowerDetailsFields");
        //        sptDrawingPowerDetailsTable.setName("sptDrawingPowerDetailsTable");
        //        tblDrawingPowerDetailsTab.setName("tblDrawingPowerDetailsTab");
        txtCalculatedDrawingPower.setName("txtCalculatedDrawingPower");
        txtMargin.setName("txtMargin");
        txtParticularsofGoods.setName("txtParticularsofGoods");
        txtPresentStockValue.setName("txtPresentStockValue");
        txtPreviousDPValue.setName("txtPreviousDPValue");
        txtPurchase.setName("txtPurchase");
        txtSales.setName("txtSales");
        txtValueofClosingStock.setName("txtValueofClosingStock");
        txtValueofOpeningStock.setName("txtValueofOpeningStock");
    }
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new DrawingPowerMaintenanceRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnDrawingPowerDetailsNew.setText(resourceBundle.getString("btnDrawingPowerDetailsNew"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        ((javax.swing.border.TitledBorder)panDrawingPower.getBorder()).setTitle(resourceBundle.getString("panDrawingPower"));
        //        lblSecurityNumber.setText(resourceBundle.getString("lblSecurityNumber"));
        lblValueofOpeningStock.setText(resourceBundle.getString("lblValueofOpeningStock"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSales.setText(resourceBundle.getString("lblSales"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblPreviousDPValCalOn.setText(resourceBundle.getString("lblPreviousDPValCalOn"));
        lblDateofInspection.setText(resourceBundle.getString("lblDateofInspection"));
        lblPreviousDPMonth.setText(resourceBundle.getString("lblPreviousDPMonth"));
        btnDrawingPowerDetailsDelete.setText(resourceBundle.getString("btnDrawingPowerDetailsDelete"));
        btnDrawingPowerDetailsSave.setText(resourceBundle.getString("btnDrawingPowerDetailsSave"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //        lblTotalDrawingPower.setText(resourceBundle.getString("lblTotalDrawingPower"));
        lblPresentStockValue.setText(resourceBundle.getString("lblPresentStockValue"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblProdID.setText(resourceBundle.getString("lblProdID"));
        //        lblDrawingPowerSanctioned.setText(resourceBundle.getString("lblDrawingPowerSanctioned"));
        lblDueDate.setText(resourceBundle.getString("lblDueDate"));
        lblAccHd.setText(resourceBundle.getString("lblAccHd"));
        //        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblCalculatedDrawingPower.setText(resourceBundle.getString("lblCalculatedDrawingPower"));
        //        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblPurchase.setText(resourceBundle.getString("lblPurchase"));
        //        lblTtlDrwingPow.setText(resourceBundle.getString("lblTtlDrwingPow"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblBorrowerNum.setText(resourceBundle.getString("lblBorrowerNum"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblCurrentDPMonth.setText(resourceBundle.getString("lblCurrentDPMonth"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblPreviousDPValue.setText(resourceBundle.getString("lblPreviousDPValue"));
        ((javax.swing.border.TitledBorder)panDrawingPowerDetails.getBorder()).setTitle(resourceBundle.getString("panDrawingPowerDetails"));
        lblParticularsofGoods.setText(resourceBundle.getString("lblParticularsofGoods"));
        lblStockSubmittedDate.setText(resourceBundle.getString("lblStockSubmittedDate"));
        lblStockStmtFreq.setText(resourceBundle.getString("lblStockStmtFreq"));
        //        lblSecurityNo.setText(resourceBundle.getString("lblSecurityNo"));
        //        lblLastStockValue.setText(resourceBundle.getString("lblLastStockValue"));
        lblMargin.setText(resourceBundle.getString("lblMargin"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        //        lblLastStckVa.setText(resourceBundle.getString("lblLastStckVa"));
        //        lblDrwPowSancted.setText(resourceBundle.getString("lblDrwPowSancted"));
        lblValueofClosingStock.setText(resourceBundle.getString("lblValueofClosingStock"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblCustId.setText(resourceBundle.getString("lblCustID"));
//        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboStockStmtFreq", new Boolean(true));
        mandatoryMap.put("datePreviousDPValCalOn", new Boolean(true));
        mandatoryMap.put("cboPreviousDPMonth", new Boolean(true));
        mandatoryMap.put("dateDueDate", new Boolean(true));
        mandatoryMap.put("txtParticularsofGoods", new Boolean(true));
        mandatoryMap.put("txtValueofOpeningStock", new Boolean(true));
        mandatoryMap.put("txtValueofClosingStock", new Boolean(true));
        mandatoryMap.put("txtPreviousDPValue", new Boolean(true));
        mandatoryMap.put("cboCurrentDPMonth", new Boolean(true));
        mandatoryMap.put("dateStockSubmittedDate", new Boolean(true));
        mandatoryMap.put("dateDateofInspection", new Boolean(true));
        mandatoryMap.put("txtPurchase", new Boolean(true));
        mandatoryMap.put("txtSales", new Boolean(true));
        mandatoryMap.put("txtCalculatedDrawingPower", new Boolean(true));
        mandatoryMap.put("txtPresentStockValue", new Boolean(true));
        mandatoryMap.put("txtMargin", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        cboStockStmtFreq.setModel(observable.getCbmStockStmtFreq());
        cboStockStmtDay.setModel(observable.getCbmStockStmtDay());
        cboCurrentDPMonth.setModel(observable.getCbmCurrentDPMonth());
        cboPreviousDPMonth.setModel(observable.getCbmPreviousDPMonth());
    }
    
    private void setMaximumLength() {
        txtParticularsofGoods.setMaxLength(128);
        txtPurchase.setMaxLength(16);
        txtPurchase.setValidation(new CurrencyValidation(14, 2));
        txtSales.setMaxLength(16);
        txtSales.setValidation(new CurrencyValidation(14, 2));
        txtValueofOpeningStock.setMaxLength(16);
        txtValueofOpeningStock.setValidation(new CurrencyValidation(14, 2));
        txtValueofClosingStock.setMaxLength(16);
        txtValueofClosingStock.setValidation(new CurrencyValidation(14, 2));
        txtPreviousDPValue.setMaxLength(16);
        txtPreviousDPValue.setValidation(new CurrencyValidation(14, 2));
        txtPresentStockValue.setMaxLength(16);
        txtPreviousDPValue.setValidation(new CurrencyValidation(14, 2));
        txtMargin.setMaxLength(5);
        txtMargin.setValidation(new PercentageValidation());
        txtCalculatedDrawingPower.setMaxLength(16);
        txtCalculatedDrawingPower.setValidation(new CurrencyValidation(14, 2));
        txtPresentStockValue.setMaxLength(16);
        txtPresentStockValue.setValidation(new CurrencyValidation(14, 2));
        txtMarginAmount.setMaxLength(16);
        txtMarginAmount.setValidation(new CurrencyValidation(14, 2));
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        DrawingPowerMaintenanceMRB objDrawingPowerMaintenanceMRB = new DrawingPowerMaintenanceMRB();
        cboStockStmtFreq.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("cboStockStmtFreq"));
        datePreviousDPValCalOn.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("datePreviousDPValCalOn"));
        cboPreviousDPMonth.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("cboPreviousDPMonth"));
        dateDueDate.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("dateDueDate"));
        txtParticularsofGoods.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtParticularsofGoods"));
        txtValueofOpeningStock.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtValueofOpeningStock"));
        txtValueofClosingStock.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtValueofClosingStock"));
        txtPreviousDPValue.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtPreviousDPValue"));
        cboCurrentDPMonth.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("cboCurrentDPMonth"));
        dateStockSubmittedDate.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("dateStockSubmittedDate"));
        dateDateofInspection.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("dateDateofInspection"));
        txtPurchase.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtPurchase"));
        txtSales.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtSales"));
        txtCalculatedDrawingPower.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtCalculatedDrawingPower"));
        txtPresentStockValue.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtPresentStockValue"));
        txtMargin.setHelpMessage(lblMsg, objDrawingPowerMaintenanceMRB.getString("txtMargin"));
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboStockStmtFreq.setSelectedItem(observable.getCboStockStmtFreq());
        cboStockStmtDay.setSelectedItem(observable.getCboStockStmtDay());
        cboPreviousDPMonth.setSelectedItem(observable.getCboPreviousDPMonth());
        txtParticularsofGoods.setText(observable.getTxtParticularsofGoods());
        txtValueofOpeningStock.setText(observable.getTxtValueofOpeningStock());
        txtValueofClosingStock.setText(observable.getTxtValueofClosingStock());
        txtPreviousDPValue.setText(observable.getTxtPreviousDPValue());
        cboCurrentDPMonth.setSelectedItem(observable.getCboCurrentDPMonth());
        txtPurchase.setText(observable.getTxtPurchase());
        txtSales.setText(observable.getTxtSales());
        txtCalculatedDrawingPower.setText(observable.getTxtCalculatedDrawingPower());
        txtPresentStockValue.setText(observable.getTxtPresentStockValue());
        txtMargin.setText(observable.getTxtMargin());
        txtMarginAmount.setText(observable.getTxtMarginAmt());
        datePreviousDPValCalOn.setDateValue(observable.getDatePreviousDPValCalOn());
        dateDueDate.setDateValue(observable.getDateDueDate());
        dateStockSubmittedDate.setDateValue(observable.getDateStockSubmittedDate());
        dateDateofInspection.setDateValue(observable.getDateDateofInspection());
        tblDrawingPowerDetailsTab.setModel(observable.getTblDrawingPowerDetailsTab());
        tblDrawingPowerDetailsTab.revalidate();
        lblStatus.setText(observable.getLblStatus());
        lblProductID.setText(observable.getLblProductID());
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        lblAccountHead.setText(observable.getLblAccountHead());
        lblAccountNumber.setText(observable.getLblBorrowNumber());
        lblDrawingPowerDateValue.setText(observable.getLblDrawingPowerDateValue());
        //        lblAccountNumber.setText(observable.getLblAccountNumber());
        //        lblSecurityNumber.setText(observable.getLblSecurityNumber());
        //        lblTotalDrawingPower.setText(observable.getLblTotalDrawingPower());
        //        lblDrawingPowerSanctioned.setText(observable.getLblDrawingPowerSanctioned());
        //        lblLastStockValue.setText(observable.getLblLastStockValue());
        
        lblCustIdValue.setText(observable.getLblCustID());
        lblCustomerNameValue.setText(observable.getLblCustomerName());
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboStockStmtFreq((String) cboStockStmtFreq.getSelectedItem());
        observable.setCboStockStmtDay((String) cboStockStmtDay.getSelectedItem());
        observable.setCboPreviousDPMonth((String) cboPreviousDPMonth.getSelectedItem());
        observable.setTxtParticularsofGoods(txtParticularsofGoods.getText());
        observable.setTxtValueofOpeningStock(txtValueofOpeningStock.getText());
        observable.setTxtValueofClosingStock(txtValueofClosingStock.getText());
        observable.setTxtPreviousDPValue(txtPreviousDPValue.getText());
        observable.setCboCurrentDPMonth((String) cboCurrentDPMonth.getSelectedItem());
        observable.setTxtPurchase(txtPurchase.getText());
        observable.setTxtSales(txtSales.getText());
        observable.setTxtCalculatedDrawingPower(txtCalculatedDrawingPower.getText());
        observable.setTxtPresentStockValue(txtPresentStockValue.getText());
        observable.setTxtMargin(txtMargin.getText());
        observable.setTxtMarginAmt(txtMarginAmount.getText());
        observable.setTblDrawingPowerDetailsTab((com.see.truetransact.clientutil.EnhancedTableModel)tblDrawingPowerDetailsTab.getModel());
        observable.setDatePreviousDPValCalOn(datePreviousDPValCalOn.getDateValue());
        observable.setDateDueDate(dateDueDate.getDateValue());
        observable.setDateStockSubmittedDate(dateStockSubmittedDate.getDateValue());
        observable.setDateDateofInspection(dateDateofInspection.getDateValue());
        observable.setLblProductID(lblProductID.getText());
        observable.setLblAccountHead(lblAccountHead.getText());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setLblBorrowNumber(lblAccountNumber.getText());
        observable.setLblDrawingPowerDateValue(lblDrawingPowerDateValue.getText());
        //        observable.setTxtBorrowerNum(txtBorrowerNum.getText());
        //        observable.setLblAccountNumber(lblAccountNumber.getText());
        //        observable.setLblSecurityNumber(lblSecurityNumber.getText());
        //        observable.setLblTotalDrawingPower(lblTotalDrawingPower.getText());
        //        observable.setLblDrawingPowerSanctioned(lblDrawingPowerSanctioned.getText());
        //        observable.setLblLastStockValue(lblLastStockValue.getText());
        
        observable.setLblCustID(lblCustIdValue.getText());
        observable.setLblCustomerName(lblCustomerNameValue.getText());
        observable.setSelectedBranchID(getSelectedBranchID());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrMain = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panDrawingPower = new com.see.truetransact.uicomponent.CPanel();
        lblStockStmtFreq = new com.see.truetransact.uicomponent.CLabel();
        lblPreviousDPValCalOn = new com.see.truetransact.uicomponent.CLabel();
        lblPreviousDPMonth = new com.see.truetransact.uicomponent.CLabel();
        lblDueDate = new com.see.truetransact.uicomponent.CLabel();
        lblParticularsofGoods = new com.see.truetransact.uicomponent.CLabel();
        lblValueofOpeningStock = new com.see.truetransact.uicomponent.CLabel();
        lblValueofClosingStock = new com.see.truetransact.uicomponent.CLabel();
        lblPreviousDPValue = new com.see.truetransact.uicomponent.CLabel();
        lblCurrentDPMonth = new com.see.truetransact.uicomponent.CLabel();
        lblStockSubmittedDate = new com.see.truetransact.uicomponent.CLabel();
        lblDateofInspection = new com.see.truetransact.uicomponent.CLabel();
        lblPurchase = new com.see.truetransact.uicomponent.CLabel();
        lblSales = new com.see.truetransact.uicomponent.CLabel();
        datePreviousDPValCalOn = new com.see.truetransact.uicomponent.CDateField();
        cboPreviousDPMonth = new com.see.truetransact.uicomponent.CComboBox();
        dateDueDate = new com.see.truetransact.uicomponent.CDateField();
        txtValueofOpeningStock = new com.see.truetransact.uicomponent.CTextField();
        txtValueofClosingStock = new com.see.truetransact.uicomponent.CTextField();
        txtPreviousDPValue = new com.see.truetransact.uicomponent.CTextField();
        cboCurrentDPMonth = new com.see.truetransact.uicomponent.CComboBox();
        dateStockSubmittedDate = new com.see.truetransact.uicomponent.CDateField();
        dateDateofInspection = new com.see.truetransact.uicomponent.CDateField();
        txtPurchase = new com.see.truetransact.uicomponent.CTextField();
        txtSales = new com.see.truetransact.uicomponent.CTextField();
        panLableValues = new com.see.truetransact.uicomponent.CPanel();
        panDrawingPowerInfo = new com.see.truetransact.uicomponent.CPanel();
        srpDrawingPowerDetailsTab = new com.see.truetransact.uicomponent.CScrollPane();
        tblDrawingPowerDetailsTab = new com.see.truetransact.uicomponent.CTable();
        panDrawingPowerDetailsButton = new com.see.truetransact.uicomponent.CPanel();
        btnDrawingPowerDetailsNew = new com.see.truetransact.uicomponent.CButton();
        btnDrawingPowerDetailsSave = new com.see.truetransact.uicomponent.CButton();
        btnDrawingPowerDetailsDelete = new com.see.truetransact.uicomponent.CButton();
        lblCalculatedDrawingPower = new com.see.truetransact.uicomponent.CLabel();
        lblPresentStockValue = new com.see.truetransact.uicomponent.CLabel();
        lblMargin = new com.see.truetransact.uicomponent.CLabel();
        txtCalculatedDrawingPower = new com.see.truetransact.uicomponent.CTextField();
        txtPresentStockValue = new com.see.truetransact.uicomponent.CTextField();
        lblMargin1 = new com.see.truetransact.uicomponent.CLabel();
        txtMarginAmount = new com.see.truetransact.uicomponent.CTextField();
        panPayeeAccHead1 = new com.see.truetransact.uicomponent.CPanel();
        cboStockStmtDay = new com.see.truetransact.uicomponent.CComboBox();
        cboStockStmtFreq = new com.see.truetransact.uicomponent.CComboBox();
        panDrawingPowerDetailsButton1 = new com.see.truetransact.uicomponent.CPanel();
        lblMargin2 = new com.see.truetransact.uicomponent.CLabel();
        txtMargin = new com.see.truetransact.uicomponent.CTextField();
        lblDrawingPowerDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblDrawingPowerDate = new com.see.truetransact.uicomponent.CLabel();
        txtParticularsofGoods = new com.see.truetransact.uicomponent.CTextField();
        panDrawingPowerDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBorrowerNum = new com.see.truetransact.uicomponent.CLabel();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblAccHd = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblCustIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameValue = new com.see.truetransact.uicomponent.CLabel();
        panPayeeAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnBorrowerNum = new com.see.truetransact.uicomponent.CButton();
        lblCustId1 = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(603, 563));
        setPreferredSize(new java.awt.Dimension(607, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrMain.setAlignmentY(0.5F);
        tbrMain.setEnabled(false);
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrMain.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMain.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrMain.add(btnDelete);

        lblSpace1.setText("     ");
        tbrMain.add(lblSpace1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrMain.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrMain.add(btnCancel);

        lblSpace2.setText("     ");
        tbrMain.add(lblSpace2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrMain.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMain.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMain.add(btnReject);

        lblSpace4.setText("     ");
        tbrMain.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrMain.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMain.add(btnClose);

        lblSpace5.setText("     ");
        tbrMain.add(lblSpace5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tbrMain, gridBagConstraints);

        panMain.setMinimumSize(new java.awt.Dimension(597, 555));
        panMain.setPreferredSize(new java.awt.Dimension(601, 555));
        panMain.setLayout(new java.awt.GridBagLayout());

        panDrawingPower.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDrawingPower.setMinimumSize(new java.awt.Dimension(649, 350));
        panDrawingPower.setPreferredSize(new java.awt.Dimension(593, 400));
        panDrawingPower.setLayout(new java.awt.GridBagLayout());

        lblStockStmtFreq.setText("Stock Statement Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblStockStmtFreq, gridBagConstraints);

        lblPreviousDPValCalOn.setText("Prev D.P val calculated on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblPreviousDPValCalOn, gridBagConstraints);

        lblPreviousDPMonth.setText("Previous D.P Month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblPreviousDPMonth, gridBagConstraints);

        lblDueDate.setText("Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblDueDate, gridBagConstraints);

        lblParticularsofGoods.setText("Particulars of Goods");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblParticularsofGoods, gridBagConstraints);

        lblValueofOpeningStock.setText("Value of Opening Stock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblValueofOpeningStock, gridBagConstraints);

        lblValueofClosingStock.setText("Value of Closing Stock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblValueofClosingStock, gridBagConstraints);

        lblPreviousDPValue.setText("Previous D.P Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblPreviousDPValue, gridBagConstraints);

        lblCurrentDPMonth.setText("Current D.P Month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblCurrentDPMonth, gridBagConstraints);

        lblStockSubmittedDate.setText("Stock Submitted Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblStockSubmittedDate, gridBagConstraints);

        lblDateofInspection.setText(" Date of Inspection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblDateofInspection, gridBagConstraints);

        lblPurchase.setText("Purchase");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblPurchase, gridBagConstraints);

        lblSales.setText("Sales");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(lblSales, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(datePreviousDPValCalOn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(cboPreviousDPMonth, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(dateDueDate, gridBagConstraints);

        txtValueofOpeningStock.setMinimumSize(new java.awt.Dimension(100, 21));
        txtValueofOpeningStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValueofOpeningStockFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(txtValueofOpeningStock, gridBagConstraints);

        txtValueofClosingStock.setMinimumSize(new java.awt.Dimension(100, 21));
        txtValueofClosingStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValueofClosingStockFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(txtValueofClosingStock, gridBagConstraints);

        txtPreviousDPValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(txtPreviousDPValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(cboCurrentDPMonth, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(dateStockSubmittedDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(dateDateofInspection, gridBagConstraints);

        txtPurchase.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(txtPurchase, gridBagConstraints);

        txtSales.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSales.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(txtSales, gridBagConstraints);

        panLableValues.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLableValues.setMinimumSize(new java.awt.Dimension(100, 150));
        panLableValues.setPreferredSize(new java.awt.Dimension(100, 175));
        panLableValues.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panDrawingPower.add(panLableValues, gridBagConstraints);

        panDrawingPowerInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("DrawingPower Information"));
        panDrawingPowerInfo.setMinimumSize(new java.awt.Dimension(250, 150));
        panDrawingPowerInfo.setName("panTransInfo");
        panDrawingPowerInfo.setPreferredSize(new java.awt.Dimension(250, 175));
        panDrawingPowerInfo.setLayout(new java.awt.GridBagLayout());

        tblDrawingPowerDetailsTab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Due Date", "Previous D.P Value", "Opening Stock", "Closing Stock"
            }
        ));
        tblDrawingPowerDetailsTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDrawingPowerDetailsTabMouseClicked(evt);
            }
        });
        srpDrawingPowerDetailsTab.setViewportView(tblDrawingPowerDetailsTab);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDrawingPowerInfo.add(srpDrawingPowerDetailsTab, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panDrawingPower.add(panDrawingPowerInfo, gridBagConstraints);
        panDrawingPowerInfo.getAccessibleContext().setAccessibleName("DrawingPower Info");
        panDrawingPowerInfo.getAccessibleContext().setAccessibleParent(panDrawingPowerInfo);

        panDrawingPowerDetailsButton.setMinimumSize(new java.awt.Dimension(200, 30));
        panDrawingPowerDetailsButton.setPreferredSize(new java.awt.Dimension(200, 30));
        panDrawingPowerDetailsButton.setLayout(new java.awt.GridBagLayout());

        btnDrawingPowerDetailsNew.setText("New");
        btnDrawingPowerDetailsNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrawingPowerDetailsNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDrawingPowerDetailsButton.add(btnDrawingPowerDetailsNew, gridBagConstraints);

        btnDrawingPowerDetailsSave.setText("Save");
        btnDrawingPowerDetailsSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrawingPowerDetailsSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDrawingPowerDetailsButton.add(btnDrawingPowerDetailsSave, gridBagConstraints);

        btnDrawingPowerDetailsDelete.setText("Delete");
        btnDrawingPowerDetailsDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrawingPowerDetailsDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDrawingPowerDetailsButton.add(btnDrawingPowerDetailsDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panDrawingPower.add(panDrawingPowerDetailsButton, gridBagConstraints);

        lblCalculatedDrawingPower.setText("Calculated Drawing Power");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(lblCalculatedDrawingPower, gridBagConstraints);

        lblPresentStockValue.setText("Present Stock Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(lblPresentStockValue, gridBagConstraints);

        lblMargin.setText("Margin Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(lblMargin, gridBagConstraints);

        txtCalculatedDrawingPower.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(txtCalculatedDrawingPower, gridBagConstraints);

        txtPresentStockValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(txtPresentStockValue, gridBagConstraints);

        lblMargin1.setText("Margin Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(lblMargin1, gridBagConstraints);

        txtMarginAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(txtMarginAmount, gridBagConstraints);

        panPayeeAccHead1.setMinimumSize(new java.awt.Dimension(100, 21));
        panPayeeAccHead1.setLayout(new java.awt.GridBagLayout());

        cboStockStmtDay.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayeeAccHead1.add(cboStockStmtDay, gridBagConstraints);

        cboStockStmtFreq.setPreferredSize(new java.awt.Dimension(58, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayeeAccHead1.add(cboStockStmtFreq, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDrawingPower.add(panPayeeAccHead1, gridBagConstraints);

        panDrawingPowerDetailsButton1.setMinimumSize(new java.awt.Dimension(100, 22));
        panDrawingPowerDetailsButton1.setPreferredSize(new java.awt.Dimension(100, 21));
        panDrawingPowerDetailsButton1.setLayout(new java.awt.GridBagLayout());

        lblMargin2.setText("%");
        lblMargin2.setMaximumSize(new java.awt.Dimension(13, 18));
        lblMargin2.setMinimumSize(new java.awt.Dimension(14, 18));
        lblMargin2.setPreferredSize(new java.awt.Dimension(14, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPowerDetailsButton1.add(lblMargin2, gridBagConstraints);

        txtMargin.setMinimumSize(new java.awt.Dimension(84, 21));
        txtMargin.setPreferredSize(new java.awt.Dimension(80, 21));
        txtMargin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMarginFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPowerDetailsButton1.add(txtMargin, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panDrawingPower.add(panDrawingPowerDetailsButton1, gridBagConstraints);

        lblDrawingPowerDateValue.setText("Drawing Power date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(lblDrawingPowerDateValue, gridBagConstraints);

        lblDrawingPowerDate.setText("Drawing Power as on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPower.add(lblDrawingPowerDate, gridBagConstraints);

        txtParticularsofGoods.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtParticularsofGoods.setMinimumSize(new java.awt.Dimension(100, 40));
        txtParticularsofGoods.setPreferredSize(new java.awt.Dimension(100, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPower.add(txtParticularsofGoods, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMain.add(panDrawingPower, gridBagConstraints);

        panDrawingPowerDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDrawingPowerDetails.setMinimumSize(new java.awt.Dimension(593, 5));
        panDrawingPowerDetails.setPreferredSize(new java.awt.Dimension(593, 5));
        panDrawingPowerDetails.setLayout(new java.awt.GridBagLayout());

        lblBorrowerNum.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPowerDetails.add(lblBorrowerNum, gridBagConstraints);

        lblProdID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPowerDetails.add(lblProdID, gridBagConstraints);

        lblProductID.setMinimumSize(new java.awt.Dimension(100, 15));
        lblProductID.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPowerDetails.add(lblProductID, gridBagConstraints);

        lblAccHd.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPowerDetails.add(lblAccHd, gridBagConstraints);

        lblAccountHead.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHead.setMinimumSize(new java.awt.Dimension(100, 15));
        lblAccountHead.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPowerDetails.add(lblAccountHead, gridBagConstraints);

        lblCustIdValue.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPowerDetails.add(lblCustIdValue, gridBagConstraints);

        lblCustId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPowerDetails.add(lblCustId, gridBagConstraints);

        lblCustomerNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameValue.setText("Customer Name");
        lblCustomerNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 40, 2, 4);
        panDrawingPowerDetails.add(lblCustomerNameValue, gridBagConstraints);

        panPayeeAccHead.setMinimumSize(new java.awt.Dimension(129, 29));
        panPayeeAccHead.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panPayeeAccHead.add(txtAccountNumber, gridBagConstraints);

        btnBorrowerNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBorrowerNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBorrowerNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowerNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayeeAccHead.add(btnBorrowerNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrawingPowerDetails.add(panPayeeAccHead, gridBagConstraints);

        lblCustId1.setText("Borrower No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panDrawingPowerDetails.add(lblCustId1, gridBagConstraints);

        lblAccountNumber.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDrawingPowerDetails.add(lblAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMain.add(panDrawingPowerDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panMain, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace3.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace3, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMarginFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarginFocusLost
        // TODO add your handling code here:
        double closingStock = CommonUtil.convertObjToDouble(txtValueofClosingStock.getText()).doubleValue();
        double margin = CommonUtil.convertObjToDouble(txtMargin.getText()).doubleValue();
        double closingValue = (closingStock * margin)/100;
        closingValue = (double)getNearest((long)(closingValue *100),100)/100;
        txtMarginAmount.setText(String.valueOf(closingValue));
        txtCalculatedDrawingPower.setText(String.valueOf(closingStock - closingValue));
    }//GEN-LAST:event_txtMarginFocusLost
    
    private void btnDrawingPowerDetailsDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrawingPowerDetailsDeleteActionPerformed
        // TODO add your handling code here:
        updateOBFields();
//        observable.deleteDrawinPowerDetailsTab(tblDrawingPowerDetailsTab.getSelectedRow());
//        int selectedRow = tblDrawingPowerDetailsTab.getSelectedRow();
//        int serialNo = CommonUtil.convertObjToInt(tblDrawingPowerDetailsTab.getValueAt(selectedRow,0));
        observable.authStatus = observable.deleteDrawinPowerTab(tblDrawingPowerDetailsTab.getSelectedRow());
        if(observable.authStatus == false){
            observable.drawinPowerDetailsTabSelected = false;
            observable.resetDrawingPowerDetailsFields();
            ClientUtil.enableDisable(panDrawingPower,false);
            observable.drawinPowerDetailsSelectedRow = -1;
            btnDrawingPowerDetailsNew.setEnabled(!btnNew.isEnabled());
            btnDrawingPowerDetailsSave.setEnabled(btnNew.isEnabled());
            btnDrawingPowerDetailsDelete.setEnabled(btnNew.isEnabled());
            disabling(false);
            clearingSaveFields();
        }else{
            ClientUtil.enableDisable(this,false);
            ClientUtil.showAlertWindow("can not delete this record bcz already authorized");            
            disabling(false);
            clearingSaveFields();
            return;
        }
         
    }//GEN-LAST:event_btnDrawingPowerDetailsDeleteActionPerformed
    
    private void tblDrawingPowerDetailsTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDrawingPowerDetailsTabMouseClicked
        // TODO add your handling code here:
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE ){//&& actionAuthExcepReject == false ){
            observable.drawinPowerDetailsTabSelected = true;                
            observable.drawinPowerDetailsSelectedRow = tblDrawingPowerDetailsTab.getSelectedRow();
            updateOBFields();
            observable.authStatus = observable.populateDrawinPowerDetailsTab(tblDrawingPowerDetailsTab.getSelectedRow());
//            ClientUtil.enableDisable(panDrawingPowerInfo,true);
            if(observable.authStatus == true){
                ClientUtil.enableDisable(this,false);
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
                    btnDrawingPowerDetailsNew.setEnabled(false);                
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                    btnDrawingPowerDetailsNew.setEnabled(true);
                btnDrawingPowerDetailsSave.setEnabled(false);
                btnDrawingPowerDetailsDelete.setEnabled(false);                                
            }else{
                ClientUtil.enableDisable(panDrawingPower,true);
                btnDrawingPowerDetailsNew.setEnabled(false);        
                btnDrawingPowerDetailsDelete.setEnabled(true);        
                btnDrawingPowerDetailsSave.setEnabled(true);  
                txtPresentStockValue.setEditable(false);
                dateDateofInspection.setEnabled(false);
                dateStockSubmittedDate.setEnabled(false);
                datePreviousDPValCalOn.setEnabled(false);
                txtCalculatedDrawingPower.setEnabled(false);
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                ClientUtil.enableDisable(this,false);
                btnDrawingPowerDetailsSave.setEnabled(false);
                btnDrawingPowerDetailsDelete.setEnabled(false);
            }
//            }
//            setDrawinPowerDetailsTabBtnEndableDisable();
        }
    }//GEN-LAST:event_tblDrawingPowerDetailsTabMouseClicked
    private void clearingSaveFields(){
        txtCalculatedDrawingPower.setText("");
        txtMarginAmount.setText("");
        txtMargin.setText("");
        txtPresentStockValue.setText("");
        txtValueofClosingStock.setText("");
        txtSales.setText("");
        txtPurchase.setText("");
        txtParticularsofGoods.setText("");
        cboCurrentDPMonth.setSelectedItem("");
        txtMarginAmount.setText("");
        txtMargin.setText("");
        txtPresentStockValue.setText("");
        txtValueofClosingStock.setText("");
        txtSales.setText("");
        txtPurchase.setText("");
        txtParticularsofGoods.setText("");
        cboCurrentDPMonth.setSelectedItem("");
        dateDueDate.setDateValue("");
        cboStockStmtDay.setSelectedItem("");
        cboStockStmtFreq.setSelectedItem("");
        datePreviousDPValCalOn.setDateValue("");
        cboPreviousDPMonth.setSelectedItem("");
        txtPreviousDPValue.setText("");
        dateStockSubmittedDate.setDateValue("");
        txtValueofOpeningStock.setText("");
        dateDateofInspection.setDateValue("");
    }
    
    private void txtValueofClosingStockFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValueofClosingStockFocusLost
        // TODO add your handling code here:
//        txtPresentStockValue.setText(txtValueofClosingStock.getText());
//        txtPresentStockValue.setEnabled(false);
        calculateClosing();

    }//GEN-LAST:event_txtValueofClosingStockFocusLost
    
    private void disabling(boolean visible){
//        txtCalculatedDrawingPower.setEditable(visible);
//        txtMarginAmount.setEditable(visible);
//        txtMargin.setEditable(visible);
//        txtPresentStockValue.setEditable(visible);
//        txtValueofClosingStock.setEditable(visible);
//        txtSales.setEditable(visible);
//        txtPurchase.setEditable(visible);
//        txtParticularsofGoods.setEditable(visible);
//        cboCurrentDPMonth.setEditable(visible);
//        cboStockStmtDay.setEditable(visible);
//        cboStockStmtFreq.setEditable(visible);
//        cboPreviousDPMonth.setEditable(visible);
//        txtPreviousDPValue.setEditable(visible);
//        txtValueofOpeningStock.setEditable(visible);
//        txtCalculatedDrawingPower.setEditable(visible);
        
        txtMarginAmount.setEnabled(visible);
        txtMargin.setEnabled(visible);
        txtPresentStockValue.setEnabled(visible);
        txtValueofClosingStock.setEnabled(visible);
        txtSales.setEnabled(visible);
        txtPurchase.setEnabled(visible);
        txtParticularsofGoods.setEnabled(visible);
        cboCurrentDPMonth.setEnabled(visible);
        dateDueDate.setEnabled(visible);
        cboStockStmtDay.setEnabled(visible);
        cboStockStmtFreq.setEnabled(visible);
        datePreviousDPValCalOn.setEnabled(visible);
        cboPreviousDPMonth.setEnabled(visible);
        txtPreviousDPValue.setEnabled(visible);
        dateStockSubmittedDate.setEnabled(visible);
        txtValueofOpeningStock.setEnabled(visible);
        dateDateofInspection.setEnabled(visible);
    }
    
    private void btnDrawingPowerDetailsSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrawingPowerDetailsSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrawingPower);
        if(mandatoryMessage.length()>0){
            displayAlert(mandatoryMessage);
        }else{
            addDrawingPowerDetailsCTable();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                btnDrawingPowerDetailsNew.setEnabled(false);
                btnDrawingPowerDetailsSave.setEnabled(true);
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                btnDrawingPowerDetailsNew.setEnabled(true);
                btnDrawingPowerDetailsSave.setEnabled(false);
            }
            disabling(false);
            clearingSaveFields();
        }
    }//GEN-LAST:event_btnDrawingPowerDetailsSaveActionPerformed
    
    private void clearingFields(){
        txtCalculatedDrawingPower.setText("");
        txtMarginAmount.setText("");
        txtMargin.setText("");
        txtPresentStockValue.setText("");
        txtValueofClosingStock.setText("");
        txtSales.setText("");
        txtPurchase.setText("");
        txtParticularsofGoods.setText("");
        cboCurrentDPMonth.setSelectedItem("");
    }
    
    private void btnDrawingPowerDetailsNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrawingPowerDetailsNewActionPerformed
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){//txtBorrowerNum.getText().length()>0){//&& tblTransList.getSelectedRow() > 0){
            HashMap previousMap = new HashMap();
            HashMap todAllowedMap = new HashMap();
            todAllowedMap.put("ACT_NUM",txtAccountNumber.getText());
            todAllowedMap.put("TODAY_DATE",ClientUtil.getCurrentDate());
            todAllowedMap.put("REMARKS","DP Reduced");
            List lst = ClientUtil.executeQuery("getSelectSumOfTODAmount",todAllowedMap);
            if(lst!=null && lst.size()>0){
                todAllowedMap = (HashMap)lst.get(0);                
                ClientUtil.showAlertWindow("Today,already TOD is created you can not give more than one Entry...");
                return;
            }
            previousMap.put("ACCT_NO",txtAccountNumber.getText());
            lst = ClientUtil.executeQuery("getSelectAuthStatusofDP", previousMap);
            if(lst!=null && lst.size()>0){
                ClientUtil.showAlertWindow("Pending for Authorization, please Authorize or Reject it...");
                return;
            }else{
                clearingFields();
                ClientUtil.enableDisable(panDrawingPower,true);
                btnDrawingPowerDetailsNew.setEnabled(false);
                btnDrawingPowerDetailsSave.setEnabled(true);
                dateDueDate.setDateValue(CommonUtil.convertObjToStr(currDt));
                dateStockSubmittedDate.setDateValue(CommonUtil.convertObjToStr(currDt));
                dateDateofInspection.setDateValue(CommonUtil.convertObjToStr(currDt));
                txtCalculatedDrawingPower.setEnabled(false);
                observable.drawinPowerDetailsTabSelected = false;
                lst = ClientUtil.executeQuery("getPreviousMonthDetails", previousMap);
                if(lst!=null && lst.size()>0){
                    previousMap = (HashMap)lst.get(0);
                    datePreviousDPValCalOn.setDateValue(CommonUtil.convertObjToStr(previousMap.get("PREV_DPVALUE_CALCDT")));
                    cboPreviousDPMonth.setSelectedIndex(CommonUtil.convertObjToInt(previousMap.get("CURR_DP_MONTH")));
                    cboStockStmtDay.setSelectedItem(CommonUtil.convertObjToStr(previousMap.get("STOCK_STAT_DAY")));
                    txtPreviousDPValue.setText(CommonUtil.convertObjToStr(previousMap.get("PREV_DP_VALUE"))); 
                    txtValueofOpeningStock.setText(CommonUtil.convertObjToStr(previousMap.get("CLOSING_STOCK_VALUE")));
                    cboStockStmtFreq.setSelectedItem((String)observable.getCbmStockStmtFreq().getDataForKey(CommonUtil.convertObjToStr(previousMap.get("STOCK_STAT_FREQ"))));
                    datePreviousDPValCalOn.setEnabled(false);
                    cboPreviousDPMonth.setEnabled(false);
                }
                disabling(true);
                dateStockSubmittedDate.setEnabled(false);
                dateDateofInspection.setEnabled(false);
            }
        }//else{
        //            ClientUtil.displayAlert("Account Number should not be empty...");
        //            return;
        //        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            observable.resetDrawingPowerNewFields();
            ClientUtil.enableDisable(panDrawingPower,true);
            btnDrawingPowerDetailsNew.setEnabled(false);
            btnDrawingPowerDetailsSave.setEnabled(true);
        }
        
    }//GEN-LAST:event_btnDrawingPowerDetailsNewActionPerformed
    
    private void txtSalesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesFocusLost
        // TODO add your handling code here:
        calculateClosing();
    }//GEN-LAST:event_txtSalesFocusLost
    
    private void txtPurchaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseFocusLost
        // TODO add your handling code here:
        calculateClosing();
    }//GEN-LAST:event_txtPurchaseFocusLost
    public long roundOffLower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod <= (roundingFactor/2)) || (mod <= (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }

    private void txtValueofOpeningStockFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValueofOpeningStockFocusLost
        // TODO add your handling code here:
        calculateClosing();
    }//GEN-LAST:event_txtValueofOpeningStockFocusLost
    //__ To calculate the Closing Stock Value...
    private void calculateClosing(){
        double openBalance = CommonUtil.convertObjToDouble(txtValueofOpeningStock.getText()).doubleValue();
        double purchase = CommonUtil.convertObjToDouble(txtPurchase.getText()).doubleValue();
        double sales = CommonUtil.convertObjToDouble(txtSales.getText()).doubleValue();
        
        txtValueofClosingStock.setText(String.valueOf(openBalance + purchase - sales));
        txtPresentStockValue.setText(String.valueOf(openBalance + purchase - sales));
        txtPresentStockValue.setEnabled(false);
    }
    private void btnBorrowerNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowerNumActionPerformed
        popUpItems(BORROWER);
    }//GEN-LAST:event_btnBorrowerNumActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        // Add your handling code here:
        observable.resetStatus();
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnException.setEnabled(false);
        btnReject.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:'
        setModified(false);
        actionAuthExcepReject = false;
        super.removeEditLock(txtAccountNumber.getText());
        observable.clearData();
        setButtonEnableDisable();
        setDrawinPowerDetailsTabBtnEndableDisable();
        observable.removeTableRow();
        observable.resetDrawingPowerAllFields();
        observable.resetDetailsTabList();
        transDetailsUI.setTransDetails(null,null,null);
        ClientUtil.enableDisable(this, false);
        btnBorrowerNum.setEnabled(false);
        observable.resetStatus();
        lblStatus.setText(observable.getLblStatus());
        observable.resetDrawingPowerFields();
        observable.resetDrawingPowerDetailsFields();
        transDetailsUI.setTransDetails(null,null,null);
        setModified(false);
        isFilled = false;
        ACTION = -1;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            HashMap hashMap=new HashMap();
            hashMap.put("MEMBER_NO",lblCustIdValue.getText());
            hashMap.put("CUST_ID",lblCustIdValue.getText());
            hashMap.put("ACCT_NO", lblAccountNumber.getText());
            List lst=ClientUtil.executeQuery("getLoanAcDetailsIndividualJoint", hashMap);
            if(lst!=null && lst.size()>0){
                List lst1=ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashMap);
                if(lst1!=null && lst1.size()>0){
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
        }
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrawingPower);
        observable.drawinPowerDetailsSelectedRow = tblDrawingPowerDetailsTab.getSelectedRow();
//        if(tblDrawingPowerDetailsTab.getSelectedRow() != 0){
        //        if (mandatoryMessage.length() > 0){
        //            displayAlert(mandatoryMessage);
//        }else{
            savePerformed();
//        }
        transDetailsUI.setTransDetails(null,null,null);
            
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUpItems(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUpItems(EDIT);
        observable.drawinPowerDetailsTabUpdate = true;
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        ClientUtil.enableDisable(panDrawingPower, false);
        //        ClientUtil.enableDisable(panDrawingPowerDetailsFields, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        setButtonEnableDisable();
        txtValueofClosingStock.setEnabled(false);
        btnBorrowerNum.setEnabled(true);
        txtAccountNumber.setEnabled(false);
        txtAccountNumber.setEditable(false);
        observable.resetDrawingPowerFields();
        observable.resetDetailsTabList();
        observable.setLableDrawingPower();
        observable.getQuery();
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void setTotalWithdrawlPower(){
        double total = 0 ;
        int COLUMN = 2 ;
        int rows = tblDrawingPowerDetailsTab.getRowCount();
        for(int i=0 ; i < rows ; i++){
            total+= CommonUtil.convertObjToDouble(tblDrawingPowerDetailsTab.getValueAt(i,COLUMN)).doubleValue();
        }
        //        lblTotalDrawingPower.setText(String.valueOf(total));
        //        observable.setLblTotalDrawingPower(lblTotalDrawingPower.getText());
        System.out.println("observable.getLblTotalDrawingPower : " + observable.getLblTotalDrawingPower());
    }
    
    private void savePerformed(){
        //        updateOBFields();
        observable.doSave();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            setModified(false);
            super.removeEditLock(txtAccountNumber.getText());
        }
        
        btnBorrowerNum.setEnabled(false);
        setButtonEnableDisable();
        setDrawinPowerDetailsTabBtnEndableDisable();
        observable.removeTableRow();
        ClientUtil.enableDisable(this, false);
        observable.resetDrawingPowerFields();
        observable.resetDrawingPowerDetailsFields();
        observable.resetDrawingPowerAllFields();
        
        observable.resetDetailsTabList();
        
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
        
        ACTION = -1;
        isFilled = false;
    }
    
    /** This method helps in popoualting the data from the data base
     * @param Action the argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        boolean isError = false ;
        updateOBFields();
        HashMap viewMap = new HashMap();
        ACTION=Action;
        if (Action == EDIT || Action == DELETE){
            java.util.ArrayList lst = new java.util.ArrayList();
            lst.add("BORROWER NUMBER");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "ViewAllDrawingPowerMaintenanceTO");
        }else if(Action == BORROWER){
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getBorrowerList");
        }else if(Action == SECURITY){
            if(!CommonUtil.convertObjToStr(lblCustIdValue.getText()).equals("")){
                HashMap whereMap = new HashMap();
                StringBuffer strB = new StringBuffer();
                whereMap.put("CUST_ID", lblCustIdValue.getText()) ;
                
                //Dont query the security numbers already added
                int COLUMN = 1 ; // This column has the security no in the table
                int rows = tblDrawingPowerDetailsTab.getRowCount();
                for(int i=0 ; i < rows ; i++){
                    strB.append("'");
                    strB.append(CommonUtil.convertObjToStr(tblDrawingPowerDetailsTab.getValueAt(i,COLUMN)));
                    strB.append("',");
                }
                if(strB.length() > 0){
                    whereMap.put("SEC_NO_LIST", strB.toString().substring(0, strB.toString().length() -1));
                }
                viewMap.put(CommonConstants.MAP_NAME, "getSecurityDetailsForCust");
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                whereMap = null ;
            }else{
                displayAlert(resourceBundle.getString("custIdNotSelected"));
                //                ClientUtil.enableDisable(panDrawingPowerDetailsFields, false);
                btnDrawingPowerDetailsSave.setEnabled(false);
                btnDrawingPowerDetailsDelete.setEnabled(false);
                btnDrawingPowerDetailsNew.setEnabled(true);
                isError = true;
            }
        }
        
        if(!isError)
            new ViewAll(this, viewMap).show();
    }
    
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if(ACTION == BORROWER){
            //            txtBorrowerNum.setText(CommonUtil.convertObjToStr(hash.get("BORROW_NO")));
            txtAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
            lblAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("BORROW_NO")));
            lblProductID.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            lblAccountHead.setText(CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")));
            lblCustIdValue.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            lblCustomerNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUST_NAME")));
            transDetailsUI.setTransDetails("AD", ProxyParameters.BRANCH_ID, CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
            ClientUtil.enableDisable(panDrawingPower,false);
            hash.put("ACCOUNT NUMBER",hash.get("ACCT_NUM"));
            observable.populateData(hash);
            btnDrawingPowerDetailsNew.setEnabled(true);
            btnBorrowerNum.setEnabled(false);
        }
        else if(ACTION == SECURITY){
            observable.resetDrawingPowerDetailsFields();
            observable.drawinPowerDetailsTabSelected = false;
            observable.drawinPowerDetailsSelectedRow = -1;
            //            ClientUtil.enableDisable(panDrawingPowerDetailsFields, true);
            btnDrawingPowerDetailsSave.setEnabled(!btnNew.isEnabled());
            btnDrawingPowerDetailsDelete.setEnabled(btnNew.isEnabled());
            btnDrawingPowerDetailsNew.setEnabled(btnNew.isEnabled());
            observable.setLableDrawingPowerDetails();
            //            lblSecurityNumber.setText(CommonUtil.convertObjToStr(hash.get("SECURITY_NO")));
        }
        else if (ACTION == EDIT || ACTION == DELETE || ACTION == AUTHORIZE){
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || ACTION == AUTHORIZE){
                txtAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER")));
                observable.setTxtAccountNumber(CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER")));
                lblProductID.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT ID")));
                observable.setLblProductID(CommonUtil.convertObjToStr(hash.get("PRODUCT ID")));
                observable.setLblBorrowNumber(CommonUtil.convertObjToStr(hash.get("BORROWER NUMBER")));
                lblAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("BORROWER NUMBER")));
                observable.populateData(hash);
                if(ACTION == AUTHORIZE ){
                    lblAccountHead.setText(observable.getLblAccountHead());
                    lblCustIdValue.setText(observable.getLblCustID());
                    lblCustomerNameValue.setText(observable.getLblCustomerName());
                }
                transDetailsUI.setTransDetails("AD", ProxyParameters.BRANCH_ID, CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER")));
                ClientUtil.enableDisable(this, false);
                setButtonEnableDisable();
                ClientUtil.enableDisable(panDrawingPower, false);
                btnDrawingPowerDetailsNew.setEnabled(false);
            }
        }
        if (ACTION == EDIT){
//            ClientUtil.enableDisable(panDrawingPower, true);
//            //            ClientUtil.enableDisable(panDrawingPowerDetailsFields, false);
//            setBtnDrwPowDetNewEnableDisable();
//            
//            txtBorrowerNum.setEditable(false);
//            
//            observable.getQuery();
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        
        txtValueofClosingStock.setEnabled(false);
        setModified(true);
    }
    
    /** This method set Btn DrwPowDetNew Enable / Disable.... */
    private void setBtnDrwPowDetNewEnableDisable(){
        int totalMargin = 0;
        int marginalRange = 100 ;
        int tableSize = tblDrawingPowerDetailsTab.getRowCount();
        int marginalTableValue = 0;
        for(int i=0;i<tableSize;i++){
            marginalTableValue  = Integer.parseInt(tblDrawingPowerDetailsTab.getValueAt((i),3).toString());
            totalMargin = totalMargin + marginalTableValue ;
        }
        marginalRange = marginalRange - totalMargin;
        if(marginalRange != 0){
            btnDrawingPowerDetailsNew.setEnabled(true);
        }else{
            btnDrawingPowerDetailsNew.setEnabled(false);
        }
    }
    
    /** This method Inserts / Updates the vlues in CTable.... */
    private void addDrawingPowerDetailsCTable(){
        int option = -1;
        updateOBFields();
        option = observable.addDrawingPowerDetailsTab();
        System.out.println("option : " + option);
        setTotalWithdrawlPower();
        if(option == 0){
            // If Margin Text value is > 100
            btnDrawingPowerDetailsNew.setEnabled(btnNew.isEnabled());
            btnDrawingPowerDetailsSave.setEnabled(!btnNew.isEnabled());
            btnDrawingPowerDetailsDelete.setEnabled(btnNew.isEnabled());
        }else{
            if(observable.marginalRangeValue == 0 ){
                // If Total Marginal value = 100 (Disable New, Save and Delete btn of CTable)
                btnDrawingPowerDetailsNew.setEnabled(btnNew.isEnabled());
                btnDrawingPowerDetailsSave.setEnabled(btnNew.isEnabled());
                btnDrawingPowerDetailsDelete.setEnabled(btnNew.isEnabled());
            }else{
                // If Total Marginal value < 100 (Enable New and Disable Save, Delete btn of CTable)
                btnDrawingPowerDetailsNew.setEnabled(!btnNew.isEnabled());
                btnDrawingPowerDetailsSave.setEnabled(btnNew.isEnabled());
                btnDrawingPowerDetailsDelete.setEnabled(btnNew.isEnabled());
            }
        }
        //        if(option == 1){
        // If the vlues Inserted / Updated to CTabel (reset the vlues to null)
        //            observable.resetDrawingPowerDetailsFields();
        //        observable.resetDrawingPowerFields();
        //            ClientUtil.enableDisable(panDrawingPowerDetailsFields, false);
        //        }
    }
    
    /** Method for AUTHORIZE, EXCEPTION and REJECTION */
    public void authorizeStatus(String authorizeStatus) {
        actionAuthExcepReject = true;
        if (ACTION == AUTHORIZE && isFilled) {
            observable.tamMaintenanceCreateMap = new HashMap();
            observable.tamMaintenanceCreateMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            observable.tamMaintenanceCreateMap.put(CommonConstants.STATUS, authorizeStatus);
            observable.tamMaintenanceCreateMap.put("ACCOUNT NUMBER",observable.getTxtAccountNumber());
            observable.tamMaintenanceCreateMap.put("PRODUCT ID",observable.getLblProductID());
            observable.tamMaintenanceCreateMap.put("BORROWER NUMBER",observable.getLblBorrowNumber());
            observable.tamMaintenanceCreateMap.put("SECURITY NUMBER",observable.getLblSecurityNumber());
            observable.tamMaintenanceCreateMap.put(CommonConstants.AUTHORIZEDT, currDt);
            observable.tamMaintenanceCreateMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            observable.tamMaintenanceCreateMap.put("CUST_NAME", observable.getLblCustomerName());
            observable.tamMaintenanceCreateMap.put("AUTHORIZE_STATUS", authorizeStatus);
            observable.doAction();
            observable.setResult(observable.getActionType());
            observable.resetDrawingPowerFields();
            observable.resetDrawingPowerDetailsFields();
            setButtonEnableDisable();
            ClientUtil.enableDisable(this, false);
            btnBorrowerNum.setEnabled(false);
            observable.setResultStatus();
            ACTION = 0;
            observable.removeTableRow();
            btnCancelActionPerformed(null);
        } else {
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZEDT, currDt);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getDrawingPowerMaintenanceAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDrawingPowerMaintenance");
            ACTION = AUTHORIZE;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            setAuthBtnEnableDisable();
            
            //__ If there's no data to be Authorized, call Cancel action...
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        }
    }
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
    }
    
    
    /** To Enable or Disable the Buttons of Drawing Power Details (CTable)... */
    private void setDrawinPowerDetailsTabBtnEndableDisable(){
        btnDrawingPowerDetailsNew.setEnabled(!btnNew.isEnabled());
        btnDrawingPowerDetailsSave.setEnabled(!btnNew.isEnabled());
        btnDrawingPowerDetailsDelete.setEnabled(!btnNew.isEnabled());
    }
    
    /** To Enable or Disable New, Edit, Delete,Save and Cancel Button */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        setAuthBtnEnableDisable();
    }
    
    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable(){
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBorrowerNum;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDrawingPowerDetailsDelete;
    private com.see.truetransact.uicomponent.CButton btnDrawingPowerDetailsNew;
    private com.see.truetransact.uicomponent.CButton btnDrawingPowerDetailsSave;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboCurrentDPMonth;
    private com.see.truetransact.uicomponent.CComboBox cboPreviousDPMonth;
    private com.see.truetransact.uicomponent.CComboBox cboStockStmtDay;
    private com.see.truetransact.uicomponent.CComboBox cboStockStmtFreq;
    private com.see.truetransact.uicomponent.CDateField dateDateofInspection;
    private com.see.truetransact.uicomponent.CDateField dateDueDate;
    private com.see.truetransact.uicomponent.CDateField datePreviousDPValCalOn;
    private com.see.truetransact.uicomponent.CDateField dateStockSubmittedDate;
    private com.see.truetransact.uicomponent.CLabel lblAccHd;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerNum;
    private com.see.truetransact.uicomponent.CLabel lblCalculatedDrawingPower;
    private com.see.truetransact.uicomponent.CLabel lblCurrentDPMonth;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblCustId1;
    private com.see.truetransact.uicomponent.CLabel lblCustIdValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDateofInspection;
    private com.see.truetransact.uicomponent.CLabel lblDrawingPowerDate;
    private com.see.truetransact.uicomponent.CLabel lblDrawingPowerDateValue;
    private com.see.truetransact.uicomponent.CLabel lblDueDate;
    private com.see.truetransact.uicomponent.CLabel lblMargin;
    private com.see.truetransact.uicomponent.CLabel lblMargin1;
    private com.see.truetransact.uicomponent.CLabel lblMargin2;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblParticularsofGoods;
    private com.see.truetransact.uicomponent.CLabel lblPresentStockValue;
    private com.see.truetransact.uicomponent.CLabel lblPreviousDPMonth;
    private com.see.truetransact.uicomponent.CLabel lblPreviousDPValCalOn;
    private com.see.truetransact.uicomponent.CLabel lblPreviousDPValue;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblPurchase;
    private com.see.truetransact.uicomponent.CLabel lblSales;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStockStmtFreq;
    private com.see.truetransact.uicomponent.CLabel lblStockSubmittedDate;
    private com.see.truetransact.uicomponent.CLabel lblValueofClosingStock;
    private com.see.truetransact.uicomponent.CLabel lblValueofOpeningStock;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDrawingPower;
    private com.see.truetransact.uicomponent.CPanel panDrawingPowerDetails;
    private com.see.truetransact.uicomponent.CPanel panDrawingPowerDetailsButton;
    private com.see.truetransact.uicomponent.CPanel panDrawingPowerDetailsButton1;
    private com.see.truetransact.uicomponent.CPanel panDrawingPowerInfo;
    private com.see.truetransact.uicomponent.CPanel panLableValues;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panPayeeAccHead;
    private com.see.truetransact.uicomponent.CPanel panPayeeAccHead1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpDrawingPowerDetailsTab;
    private com.see.truetransact.uicomponent.CTable tblDrawingPowerDetailsTab;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtCalculatedDrawingPower;
    private com.see.truetransact.uicomponent.CTextField txtMargin;
    private com.see.truetransact.uicomponent.CTextField txtMarginAmount;
    private com.see.truetransact.uicomponent.CTextField txtParticularsofGoods;
    private com.see.truetransact.uicomponent.CTextField txtPresentStockValue;
    private com.see.truetransact.uicomponent.CTextField txtPreviousDPValue;
    private com.see.truetransact.uicomponent.CTextField txtPurchase;
    private com.see.truetransact.uicomponent.CTextField txtSales;
    private com.see.truetransact.uicomponent.CTextField txtValueofClosingStock;
    private com.see.truetransact.uicomponent.CTextField txtValueofOpeningStock;
    // End of variables declaration//GEN-END:variables
    
}

