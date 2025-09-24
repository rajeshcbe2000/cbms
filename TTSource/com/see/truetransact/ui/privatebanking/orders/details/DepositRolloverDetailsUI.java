/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositRolloverDetailsUI.java
 *
 * Created on July 7, 2004, 1:31 PM
 */

package com.see.truetransact.ui.privatebanking.orders.details;

import com.see.truetransact.ui.privatebanking.orders.details.DepositRolloverDetailsRB;
import com.see.truetransact.ui.privatebanking.orders.details.DepositRolloverDetailsMRB;
import com.see.truetransact.ui.privatebanking.orders.details.DepositRolloverDetailsOB;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.NumericValidation;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;

/**
 *
 * @author  Lohith R.
 */
public class DepositRolloverDetailsUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {
    
    private DepositRolloverDetailsOB observable;
    HashMap mandatoryMap;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, ENTITLEMENTGROUP = 3 ;
    boolean isFilled = false;
    int ACTION=-1;
    
    /** Creates new form DepositRolloverDetailsUI */
    public DepositRolloverDetailsUI() {
        initComponents();
        initStartUP();
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
        setButtonEnableDisable();
        setEntitlementGrpBtnEnableDisable();
        ClientUtil.enableDisable(this, false);
        observable.resetStatus();
        observable.resetFields();
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = DepositRolloverDetailsOB.getInstance();
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
        btnEdit.setName("btnEdit");
        btnEntitlementGroup.setName("btnEntitlementGroup");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        dateStartDate.setName("dateStartDate");
        cboCurrency.setName("cboCurrency");
        cboRolloverType.setName("cboRolloverType");
        cboTenor.setName("cboTenor");
        dateMaturityDate.setName("dateMaturityDate");
        txtRolloverAmount.setName("txtRolloverAmount");
        lblAccount.setName("lblAccount");
        lblAssetSubClass.setName("lblAssetSubClass");
        lblCSPMemoAvailableBalance.setName("lblCSPMemoAvailableBalance");
        lblCurrency.setName("lblCurrency");
        lblDepositReferenceNumber.setName("lblDepositReferenceNumber");
        lblEntitlementGroup.setName("lblEntitlementGroup");
        lblInterestEarned.setName("lblInterestEarned");
        lblMaturityDate.setName("lblMaturityDate");
        lblMsg.setName("lblMsg");
        lblPhoneOrder.setName("lblPhoneOrder");
        lblPortfolioLocation.setName("lblPortfolioLocation");
        lblPrincipal.setName("lblPrincipal");
        lblRollover.setName("lblRollover");
        lblRolloverAmount.setName("lblRolloverAmount");
        lblRolloverType.setName("lblRolloverType");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpread.setName("lblSpread");
        lblStartDate.setName("lblStartDate");
        lblStatus.setName("lblStatus");
        lblTenor.setName("lblTenor");
        lblTenorDays.setName("lblTenorDays");
        mbrMain.setName("mbrMain");
        panDepositRollover.setName("panDepositRollover");
        panEnterRolloverDetails.setName("panEnterRolloverDetails");
        panEntitlementGrp.setName("panEntitlementGrp");
        panMain.setName("panMain");
        panPhoneOrder.setName("panPhoneOrder");
        panStatus.setName("panStatus");
        panTenor.setName("panTenor");
        rdoPhoneOrder_Yes.setName("rdoPhoneOrder_Yes");
        rdoPhoneOrder_No.setName("rdoPhoneOrder_No");
        txtAccount.setName("txtAccount");
        txtAssetSubClass.setName("txtAssetSubClass");
        txtCSPMemoAvailableBalance.setName("txtCSPMemoAvailableBalance");
        txtDepositReferenceNumber.setName("txtDepositReferenceNumber");
        txtEntitlementGroup.setName("txtEntitlementGroup");
        txtInterestEarned.setName("txtInterestEarned");
        txtPortfolioLocation.setName("txtPortfolioLocation");
        txtPrincipal.setName("txtPrincipal");
        txtRollover.setName("txtRollover");
        txtSpread.setName("txtSpread");
        txtTenorDays.setName("txtTenorDays");
    }
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        DepositRolloverDetailsRB resourceBundle = new DepositRolloverDetailsRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblEntitlementGroup.setText(resourceBundle.getString("lblEntitlementGroup"));
        lblSpread.setText(resourceBundle.getString("lblSpread"));
        lblCurrency.setText(resourceBundle.getString("lblCurrency"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        rdoPhoneOrder_No.setText(resourceBundle.getString("rdoPhoneOrder_No"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblInterestEarned.setText(resourceBundle.getString("lblInterestEarned"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblRolloverAmount.setText(resourceBundle.getString("lblRolloverAmount"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblDepositReferenceNumber.setText(resourceBundle.getString("lblDepositReferenceNumber"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        ((javax.swing.border.TitledBorder)panDepositRollover.getBorder()).setTitle(resourceBundle.getString("panDepositRollover"));
        ((javax.swing.border.TitledBorder)panEnterRolloverDetails.getBorder()).setTitle(resourceBundle.getString("panEnterRolloverDetails"));
        lblRollover.setText(resourceBundle.getString("lblRollover"));
        lblStartDate.setText(resourceBundle.getString("lblStartDate"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblAssetSubClass.setText(resourceBundle.getString("lblAssetSubClass"));
        lblPortfolioLocation.setText(resourceBundle.getString("lblPortfolioLocation"));
        lblTenorDays.setText(resourceBundle.getString("lblTenorDays"));
        lblTenor.setText(resourceBundle.getString("lblTenor"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnEntitlementGroup.setText(resourceBundle.getString("btnEntitlementGroup"));
        lblPhoneOrder.setText(resourceBundle.getString("lblPhoneOrder"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        lblRolloverType.setText(resourceBundle.getString("lblRolloverType"));
        rdoPhoneOrder_Yes.setText(resourceBundle.getString("rdoPhoneOrder_Yes"));
        lblAccount.setText(resourceBundle.getString("lblAccount"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblCSPMemoAvailableBalance.setText(resourceBundle.getString("lblCSPMemoAvailableBalance"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblPrincipal.setText(resourceBundle.getString("lblPrincipal"));
    }
    
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtDepositReferenceNumber", new Boolean(true));
        mandatoryMap.put("txtPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtAccount", new Boolean(true));
        mandatoryMap.put("txtPrincipal", new Boolean(true));
        mandatoryMap.put("txtInterestEarned", new Boolean(true));
        mandatoryMap.put("txtRollover", new Boolean(true));
        mandatoryMap.put("txtCSPMemoAvailableBalance", new Boolean(true));
        mandatoryMap.put("dateStartDate", new Boolean(true));
        mandatoryMap.put("rdoPhoneOrder_Yes", new Boolean(true));
        mandatoryMap.put("cboTenor", new Boolean(true));
        mandatoryMap.put("txtTenorDays", new Boolean(true));
        mandatoryMap.put("cboRolloverType", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("txtSpread", new Boolean(true));
        mandatoryMap.put("dateMaturityDate", new Boolean(true));
        mandatoryMap.put("txtRolloverAmount", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        cboTenor.setModel(observable.getCbmTenor());
        cboRolloverType.setModel(observable.getCbmRolloverType());
        cboCurrency.setModel(observable.getCbmCurrency());
        
    }
    
    private void setMaximumLength() {
        txtDepositReferenceNumber.setMaxLength(16);
        txtPortfolioLocation.setMaxLength(32);
        txtAssetSubClass.setMaxLength(32);
        txtAccount.setMaxLength(32);
        txtPrincipal.setMaxLength(16);
        txtPrincipal.setValidation(new NumericValidation(14, 2));
        txtRollover.setMaxLength(16);
        txtInterestEarned.setMaxLength(16);
        txtInterestEarned.setValidation(new NumericValidation(14, 2));
        txtCSPMemoAvailableBalance.setMaxLength(16);
        txtCSPMemoAvailableBalance.setValidation(new NumericValidation(14, 2));
        txtRolloverAmount.setMaxLength(16);
        txtRolloverAmount.setValidation(new NumericValidation(14, 2));
        txtTenorDays.setMaxLength(5);
        txtTenorDays.setValidation(new NumericValidation());
        txtSpread.setMaxLength(16);
        txtSpread.setValidation(new NumericValidation(14, 2));
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        DepositRolloverDetailsMRB objMandatoryRB = new DepositRolloverDetailsMRB();
        txtEntitlementGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEntitlementGroup"));
        txtDepositReferenceNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositReferenceNumber"));
        txtPortfolioLocation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPortfolioLocation"));
        txtAssetSubClass.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetSubClass"));
        txtAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccount"));
        txtPrincipal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrincipal"));
        txtInterestEarned.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestEarned"));
        txtRollover.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRollover"));
        txtCSPMemoAvailableBalance.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCSPMemoAvailableBalance"));
        dateStartDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dateStartDate"));
        rdoPhoneOrder_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPhoneOrder_Yes"));
        cboTenor.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTenor"));
        txtTenorDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTenorDays"));
        cboRolloverType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRolloverType"));
        cboCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCurrency"));
        txtSpread.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSpread"));
        dateMaturityDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dateMaturityDate"));
        txtRolloverAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRolloverAmount"));
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtEntitlementGroup.setText(observable.getTxtEntitlementGroup());
        txtDepositReferenceNumber.setText(observable.getTxtDepositReferenceNumber());
        txtPortfolioLocation.setText(observable.getTxtPortfolioLocation());
        txtAssetSubClass.setText(observable.getTxtAssetSubClass());
        txtAccount.setText(observable.getTxtAccount());
        txtPrincipal.setText(observable.getTxtPrincipal());
        txtInterestEarned.setText(observable.getTxtInterestEarned());
        txtRollover.setText(observable.getTxtRollover());
        txtCSPMemoAvailableBalance.setText(observable.getTxtCSPMemoAvailableBalance());
        txtRolloverAmount.setText(observable.getTxtRolloverAmount());
        rdoPhoneOrder_Yes.setSelected(observable.getRdoPhoneOrder_Yes());
        rdoPhoneOrder_No.setSelected(observable.getRdoPhoneOrder_No());
        cboTenor.setSelectedItem(observable.getCboTenor());
        txtTenorDays.setText(observable.getTxtTenorDays());
        cboRolloverType.setSelectedItem(observable.getCboRolloverType());
        cboCurrency.setSelectedItem(observable.getCboCurrency());
        txtSpread.setText(observable.getTxtSpread());
        dateStartDate.setDateValue(observable.getDateStartDate());
        dateMaturityDate.setDateValue(observable.getDateMaturityDate());
        lblStatus.setText(observable.getLblStatus());
        addRadioButtons();
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdgPhoneOrder.remove(rdoPhoneOrder_Yes);
        rdgPhoneOrder.remove(rdoPhoneOrder_No);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdgPhoneOrder = new CButtonGroup();
        rdgPhoneOrder.add(rdoPhoneOrder_Yes);
        rdgPhoneOrder.add(rdoPhoneOrder_No);
    }
    
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtEntitlementGroup(txtEntitlementGroup.getText());
        observable.setTxtDepositReferenceNumber(txtDepositReferenceNumber.getText());
        observable.setTxtPortfolioLocation(txtPortfolioLocation.getText());
        observable.setTxtAssetSubClass(txtAssetSubClass.getText());
        observable.setTxtAccount(txtAccount.getText());
        observable.setTxtPrincipal(txtPrincipal.getText());
        observable.setTxtInterestEarned(txtInterestEarned.getText());
        observable.setTxtRollover(txtRollover.getText());
        observable.setTxtCSPMemoAvailableBalance(txtCSPMemoAvailableBalance.getText());
        observable.setTxtRolloverAmount(txtRolloverAmount.getText());
        observable.setRdoPhoneOrder_Yes(rdoPhoneOrder_Yes.isSelected());
        observable.setRdoPhoneOrder_No(rdoPhoneOrder_No.isSelected());
        observable.setCboTenor((String) cboTenor.getSelectedItem());
        observable.setTxtTenorDays(txtTenorDays.getText());
        observable.setCboRolloverType((String) cboRolloverType.getSelectedItem());
        observable.setCboCurrency((String) cboCurrency.getSelectedItem());
        observable.setTxtSpread(txtSpread.getText());
        observable.setDateStartDate(dateStartDate.getDateValue());
        observable.setDateMaturityDate(dateMaturityDate.getDateValue());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgPhoneOrder = new com.see.truetransact.uicomponent.CButtonGroup();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panDepositRollover = new com.see.truetransact.uicomponent.CPanel();
        lblEntitlementGroup = new com.see.truetransact.uicomponent.CLabel();
        lblDepositReferenceNumber = new com.see.truetransact.uicomponent.CLabel();
        lblPortfolioLocation = new com.see.truetransact.uicomponent.CLabel();
        lblAssetSubClass = new com.see.truetransact.uicomponent.CLabel();
        lblAccount = new com.see.truetransact.uicomponent.CLabel();
        lblPrincipal = new com.see.truetransact.uicomponent.CLabel();
        lblInterestEarned = new com.see.truetransact.uicomponent.CLabel();
        lblRollover = new com.see.truetransact.uicomponent.CLabel();
        lblCSPMemoAvailableBalance = new com.see.truetransact.uicomponent.CLabel();
        panEntitlementGrp = new com.see.truetransact.uicomponent.CPanel();
        txtEntitlementGroup = new com.see.truetransact.uicomponent.CTextField();
        btnEntitlementGroup = new com.see.truetransact.uicomponent.CButton();
        txtDepositReferenceNumber = new com.see.truetransact.uicomponent.CTextField();
        txtPortfolioLocation = new com.see.truetransact.uicomponent.CTextField();
        txtAssetSubClass = new com.see.truetransact.uicomponent.CTextField();
        txtAccount = new com.see.truetransact.uicomponent.CTextField();
        txtPrincipal = new com.see.truetransact.uicomponent.CTextField();
        txtInterestEarned = new com.see.truetransact.uicomponent.CTextField();
        txtRollover = new com.see.truetransact.uicomponent.CTextField();
        txtCSPMemoAvailableBalance = new com.see.truetransact.uicomponent.CTextField();
        panEnterRolloverDetails = new com.see.truetransact.uicomponent.CPanel();
        lblStartDate = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        lblRolloverAmount = new com.see.truetransact.uicomponent.CLabel();
        lblPhoneOrder = new com.see.truetransact.uicomponent.CLabel();
        lblTenor = new com.see.truetransact.uicomponent.CLabel();
        lblRolloverType = new com.see.truetransact.uicomponent.CLabel();
        lblCurrency = new com.see.truetransact.uicomponent.CLabel();
        lblSpread = new com.see.truetransact.uicomponent.CLabel();
        panPhoneOrder = new com.see.truetransact.uicomponent.CPanel();
        rdoPhoneOrder_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPhoneOrder_No = new com.see.truetransact.uicomponent.CRadioButton();
        panTenor = new com.see.truetransact.uicomponent.CPanel();
        cboTenor = new com.see.truetransact.uicomponent.CComboBox();
        lblTenorDays = new com.see.truetransact.uicomponent.CLabel();
        txtTenorDays = new com.see.truetransact.uicomponent.CTextField();
        cboRolloverType = new com.see.truetransact.uicomponent.CComboBox();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        txtSpread = new com.see.truetransact.uicomponent.CTextField();
        dateMaturityDate = new com.see.truetransact.uicomponent.CDateField();
        dateStartDate = new com.see.truetransact.uicomponent.CDateField();
        txtRolloverAmount = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setTitle("Order Details - Deposit Rollover");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMain.setMinimumSize(new java.awt.Dimension(600, 400));
        panMain.setPreferredSize(new java.awt.Dimension(609, 340));
        panMain.setLayout(new java.awt.GridBagLayout());

        panDepositRollover.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Rollover"));
        panDepositRollover.setMinimumSize(new java.awt.Dimension(604, 175));
        panDepositRollover.setPreferredSize(new java.awt.Dimension(604, 175));
        panDepositRollover.setLayout(new java.awt.GridBagLayout());

        lblEntitlementGroup.setText("Entitlement Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(lblEntitlementGroup, gridBagConstraints);

        lblDepositReferenceNumber.setText("Deposit Reference No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(lblDepositReferenceNumber, gridBagConstraints);

        lblPortfolioLocation.setText("Portfolio Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(lblPortfolioLocation, gridBagConstraints);

        lblAssetSubClass.setText("Asset Sub – Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(lblAssetSubClass, gridBagConstraints);

        lblAccount.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(lblAccount, gridBagConstraints);

        lblPrincipal.setText("Principal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 4, 4);
        panDepositRollover.add(lblPrincipal, gridBagConstraints);

        lblInterestEarned.setText("Interest Earned");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 4, 4);
        panDepositRollover.add(lblInterestEarned, gridBagConstraints);

        lblRollover.setText("Rollover");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 4, 4);
        panDepositRollover.add(lblRollover, gridBagConstraints);

        lblCSPMemoAvailableBalance.setText("CSP Memo Avail Bal.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 4, 4);
        panDepositRollover.add(lblCSPMemoAvailableBalance, gridBagConstraints);

        panEntitlementGrp.setLayout(new java.awt.GridBagLayout());

        txtEntitlementGroup.setMinimumSize(new java.awt.Dimension(76, 21));
        txtEntitlementGroup.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        panEntitlementGrp.add(txtEntitlementGroup, gridBagConstraints);

        btnEntitlementGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEntitlementGroup.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEntitlementGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntitlementGroupActionPerformed(evt);
            }
        });
        panEntitlementGrp.add(btnEntitlementGroup, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(panEntitlementGrp, gridBagConstraints);

        txtDepositReferenceNumber.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtDepositReferenceNumber, gridBagConstraints);

        txtPortfolioLocation.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtPortfolioLocation, gridBagConstraints);

        txtAssetSubClass.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtAssetSubClass, gridBagConstraints);

        txtAccount.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtAccount, gridBagConstraints);

        txtPrincipal.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtPrincipal, gridBagConstraints);

        txtInterestEarned.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtInterestEarned, gridBagConstraints);

        txtRollover.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtRollover, gridBagConstraints);

        txtCSPMemoAvailableBalance.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositRollover.add(txtCSPMemoAvailableBalance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panDepositRollover, gridBagConstraints);

        panEnterRolloverDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter Rollover Details"));
        panEnterRolloverDetails.setMinimumSize(new java.awt.Dimension(604, 145));
        panEnterRolloverDetails.setPreferredSize(new java.awt.Dimension(604, 145));
        panEnterRolloverDetails.setLayout(new java.awt.GridBagLayout());

        lblStartDate.setText("Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 102, 4, 4);
        panEnterRolloverDetails.add(lblStartDate, gridBagConstraints);

        lblMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 102, 4, 4);
        panEnterRolloverDetails.add(lblMaturityDate, gridBagConstraints);

        lblRolloverAmount.setText("Rollover Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 102, 4, 4);
        panEnterRolloverDetails.add(lblRolloverAmount, gridBagConstraints);

        lblPhoneOrder.setText("Phone Order");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 102, 4, 4);
        panEnterRolloverDetails.add(lblPhoneOrder, gridBagConstraints);

        lblTenor.setText("Tenor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 53, 4, 4);
        panEnterRolloverDetails.add(lblTenor, gridBagConstraints);

        lblRolloverType.setText("Rollover Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 53, 4, 4);
        panEnterRolloverDetails.add(lblRolloverType, gridBagConstraints);

        lblCurrency.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 53, 4, 4);
        panEnterRolloverDetails.add(lblCurrency, gridBagConstraints);

        lblSpread.setText("Spread");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 53, 4, 4);
        panEnterRolloverDetails.add(lblSpread, gridBagConstraints);

        panPhoneOrder.setLayout(new java.awt.GridBagLayout());

        rdoPhoneOrder_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panPhoneOrder.add(rdoPhoneOrder_Yes, gridBagConstraints);

        rdoPhoneOrder_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panPhoneOrder.add(rdoPhoneOrder_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(panPhoneOrder, gridBagConstraints);

        panTenor.setLayout(new java.awt.GridBagLayout());

        cboTenor.setMaximumSize(new java.awt.Dimension(100, 21));
        panTenor.add(cboTenor, new java.awt.GridBagConstraints());

        lblTenorDays.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTenor.add(lblTenorDays, gridBagConstraints);

        txtTenorDays.setMinimumSize(new java.awt.Dimension(30, 21));
        txtTenorDays.setPreferredSize(new java.awt.Dimension(30, 21));
        panTenor.add(txtTenorDays, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(panTenor, gridBagConstraints);

        cboRolloverType.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(cboRolloverType, gridBagConstraints);

        cboCurrency.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(cboCurrency, gridBagConstraints);

        txtSpread.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSpread.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(txtSpread, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(dateMaturityDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(dateStartDate, gridBagConstraints);

        txtRolloverAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRolloverAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEnterRolloverDetails.add(txtRolloverAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panEnterRolloverDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
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
    
    private void btnEntitlementGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntitlementGroupActionPerformed
        // TODO add your handling code here:
        popUpItems(ENTITLEMENTGROUP);
    }//GEN-LAST:event_btnEntitlementGroupActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        observable.resetStatus();
        observable.resetFields();
        ClientUtil.enableDisable(this, false);
        this.dispose();
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
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetStatus();
        observable.resetFields();
        setButtonEnableDisable();
        setEntitlementGrpBtnEnableDisable();
        ClientUtil.enableDisable(this, false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMain);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUpItems(DELETE);
        ClientUtil.enableDisable(this, false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUpItems(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(this, true);
        setButtonEnableDisable();
        setEntitlementGrpBtnEnableDisable();
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void savePerformed(){
        boolean memberRelationExists = false;
        updateOBFields();
        observable.doAction();
        observable.setResultStatus();
        setButtonEnableDisable();
        setEntitlementGrpBtnEnableDisable();
        ClientUtil.enableDisable(this, false);
        observable.resetFields();
    }
    
    
    /** This method helps in popoualting the data from the data base
     * @param Action the argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        updateOBFields();
        if (Action == EDIT || Action == DELETE){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        final HashMap viewMap = new HashMap();
        ACTION=Action;
        if ( Action == EDIT || Action == DELETE){
            viewMap.put(CommonConstants.MAP_NAME, "ViewAllDepositRolloverDetailsTO");
        }else{
            viewMap.put(CommonConstants.MAP_NAME, "ViewAllDepositRollover_EntitlementGroup");
        }
        new ViewAll(this, viewMap).show();
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (ACTION == EDIT || ACTION == DELETE || ACTION == AUTHORIZE){
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || ACTION == AUTHORIZE){
                hash.put(CommonConstants.MAP_WHERE, hash.get("ORDER ID"));
                observable.setTxtDepositReferenceNumber((String) hash.get("ORDER ID"));
                observable.setTxtEntitlementGroup((String) hash.get("ENTITLEMENT GROUP"));
                observable.populateData(hash);
                ClientUtil.enableDisable(this, false);
                setButtonEnableDisable();
            }
        }else if (ACTION == ENTITLEMENTGROUP){
            observable.setTxtDepositReferenceNumber((String) hash.get("ORDER ID"));
            observable.setTxtEntitlementGroup((String) hash.get("MEMBER"));
        }
        if (ACTION == EDIT){
            ClientUtil.enableDisable(this, true);
            setEntitlementGrpBtnEnableDisable();
            btnEntitlementGroup.setEnabled(btnNew.isEnabled());
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void authorizeStatus(String authorizeStatus) {
        if (ACTION == AUTHORIZE && isFilled) {
            final HashMap depositRolloverDetailsMap = new HashMap();
            depositRolloverDetailsMap.put("USER_ID", TrueTransactMain.USER_ID);
            depositRolloverDetailsMap.put("STATUS", authorizeStatus);
            depositRolloverDetailsMap.put("ORDER ID",observable.getTxtDepositReferenceNumber());
            ClientUtil.execute("authorizeDepositRolloverDetails", depositRolloverDetailsMap);
            observable.setResult(observable.getActionType());
            observable.resetFields();
            setButtonEnableDisable();
            ClientUtil.enableDisable(this, false);
            observable.setResultStatus();
            ACTION = 0;
        } else {
            final HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getDepositRolloverDetailsAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDepositRolloverDetails");
            ACTION = AUTHORIZE;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            setAuthBtnEnableDisable();
        }
    }
    
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
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
    
    private void setEntitlementGrpBtnEnableDisable(){
        btnEntitlementGroup.setEnabled(!btnNew.isEnabled());
        txtEntitlementGroup.setEditable(btnNew.isEnabled());
        txtEntitlementGroup.setEnabled(btnNew.isEnabled());
        txtDepositReferenceNumber.setEditable(btnNew.isEnabled());
        txtDepositReferenceNumber.setEnabled(btnNew.isEnabled());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEntitlementGroup;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboRolloverType;
    private com.see.truetransact.uicomponent.CComboBox cboTenor;
    private com.see.truetransact.uicomponent.CDateField dateMaturityDate;
    private com.see.truetransact.uicomponent.CDateField dateStartDate;
    private com.see.truetransact.uicomponent.CLabel lblAccount;
    private com.see.truetransact.uicomponent.CLabel lblAssetSubClass;
    private com.see.truetransact.uicomponent.CLabel lblCSPMemoAvailableBalance;
    private com.see.truetransact.uicomponent.CLabel lblCurrency;
    private com.see.truetransact.uicomponent.CLabel lblDepositReferenceNumber;
    private com.see.truetransact.uicomponent.CLabel lblEntitlementGroup;
    private com.see.truetransact.uicomponent.CLabel lblInterestEarned;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPhoneOrder;
    private com.see.truetransact.uicomponent.CLabel lblPortfolioLocation;
    private com.see.truetransact.uicomponent.CLabel lblPrincipal;
    private com.see.truetransact.uicomponent.CLabel lblRollover;
    private com.see.truetransact.uicomponent.CLabel lblRolloverAmount;
    private com.see.truetransact.uicomponent.CLabel lblRolloverType;
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
    private com.see.truetransact.uicomponent.CLabel lblSpread;
    private com.see.truetransact.uicomponent.CLabel lblStartDate;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTenor;
    private com.see.truetransact.uicomponent.CLabel lblTenorDays;
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
    private com.see.truetransact.uicomponent.CPanel panDepositRollover;
    private com.see.truetransact.uicomponent.CPanel panEnterRolloverDetails;
    private com.see.truetransact.uicomponent.CPanel panEntitlementGrp;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panPhoneOrder;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTenor;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPhoneOrder;
    private com.see.truetransact.uicomponent.CRadioButton rdoPhoneOrder_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPhoneOrder_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtAccount;
    private com.see.truetransact.uicomponent.CTextField txtAssetSubClass;
    private com.see.truetransact.uicomponent.CTextField txtCSPMemoAvailableBalance;
    private com.see.truetransact.uicomponent.CTextField txtDepositReferenceNumber;
    private com.see.truetransact.uicomponent.CTextField txtEntitlementGroup;
    private com.see.truetransact.uicomponent.CTextField txtInterestEarned;
    private com.see.truetransact.uicomponent.CTextField txtPortfolioLocation;
    private com.see.truetransact.uicomponent.CTextField txtPrincipal;
    private com.see.truetransact.uicomponent.CTextField txtRollover;
    private com.see.truetransact.uicomponent.CTextField txtRolloverAmount;
    private com.see.truetransact.uicomponent.CTextField txtSpread;
    private com.see.truetransact.uicomponent.CTextField txtTenorDays;
    // End of variables declaration//GEN-END:variables
    
}
