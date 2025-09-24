/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InventoryMasterUI.java
 *
 * Created on August 20, 2004, 1:26 PM
 */

package com.see.truetransact.ui.supporting.inventory;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CButtonGroup;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import java.util.HashMap;
import java.util.ArrayList ;
import java.util.Observer;
import java.util.Observable;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  rahul
 * @modified Sunil
 *  Added multi branch support
 */
public class InventoryMasterUI extends CInternalFrame implements java.util.Observer, UIMandatoryField{
    HashMap mandatoryMap;
    
    
    InventoryMasterOB observable;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.supporting.inventory.InventoryMasterRB", ProxyParameters.LANGUAGE);
    
    final int EDIT=0, DELETE=1, AUTHORIZE=2, VIEW =3;
    int viewType=-1;
    boolean isFilled = false;
    boolean verified = false;
    
    /** Creates new form InventorymasterUI */
    public InventoryMasterUI() {
        initComponents();
        initSetup();
    }//
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        
        setObservable();
        initComponentData();    // Fill all the combo boxes...
        setMaxLenths(); // To set the Numeric Validation and the Maximum length of the Text fields...
         new MandatoryCheck().putMandatoryMarks(getClass().getName(),panInventoryMaster);
        setHelpMessage();
        
        observable.resetStatus();   // to reset the status
        observable.resetForm(); // To reset all the fields in UI...
        observable.resetStatus();   // To reset the Satus in the UI...
        resetTransData();
        
        ClientUtil.enableDisable(this, false);  // Disables all when the screen appears for the 1st time
        setButtonEnableDisable();   // Enables/Disables the necessary buttons and menu items...
        lblInstrumentPrefix.setVisible(false);
        txtInstrumentPrefix.setVisible(false);
    }
    
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        observable = InventoryMasterOB.getInstance();
        observable.addObserver(this);
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboItemType.setName("cboItemType");
        cboUsage.setName("cboUsage");
        lblDangerLevel.setName("lblDangerLevel");
        lblInstrumentPrefix.setName("lblInstrumentPrefix");
        lblInventoryID.setName("lblInventoryID");
        lblInventoryIDDesc.setName("lblInventoryIDDesc");
        lblItemType.setName("lblItemType");
        lblLeavesPerBook.setName("lblLeavesPerBook");
        lblMsg.setName("lblMsg");
        lblReOrderLevel.setName("lblReOrderLevel");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");
        lblTotalBooks.setName("lblTotalBooks");
        lblTotalBooksDesc.setName("lblTotalBooksDesc");
        lblTotalLeaves.setName("lblTotalLeaves");
        lblTotalLeavesDesc.setName("lblTotalLeavesDesc");
        lblTransaIn.setName("lblTransaIn");
        lblTransaInDesc.setName("lblTransaInDesc");
        lblTransaOut.setName("lblTransaOut");
        lblTransaOutDesc.setName("lblTransaOutDesc");
        lblUsage.setName("lblUsage");
        mbrLoanProduct.setName("mbrLoanProduct");
        panAuthorization.setName("panAuthorization");
        panInventoryMaster.setName("panInventoryMaster");
        panStatus.setName("panStatus");
        txtDangerLevel.setName("txtDangerLevel");
        txtInstrumentPrefix.setName("txtInstrumentPrefix");
        txtLeavesPerBook.setName("txtLeavesPerBook");
        txtReOrderLevel.setName("txtReOrderLevel");
    }
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        //        InventoryMasterRB resourceBundle = new InventoryMasterRB();
        lblTransaInDesc.setText(resourceBundle.getString("lblTransaInDesc"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblItemType.setText(resourceBundle.getString("lblItemType"));
        lblInstrumentPrefix.setText(resourceBundle.getString("lblInstrumentPrefix"));
        lblInventoryIDDesc.setText(resourceBundle.getString("lblInventoryIDDesc"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblTotalBooksDesc.setText(resourceBundle.getString("lblTotalBooksDesc"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblUsage.setText(resourceBundle.getString("lblUsage"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblTransaOutDesc.setText(resourceBundle.getString("lblTransaOutDesc"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblReOrderLevel.setText(resourceBundle.getString("lblReOrderLevel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblDangerLevel.setText(resourceBundle.getString("lblDangerLevel"));
        lblTransaOut.setText(resourceBundle.getString("lblTransaOut"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblTotalLeavesDesc.setText(resourceBundle.getString("lblTotalLeavesDesc"));
        lblTransaIn.setText(resourceBundle.getString("lblTransaIn"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblTotalBooks.setText(resourceBundle.getString("lblTotalBooks"));
        lblLeavesPerBook.setText(resourceBundle.getString("lblLeavesPerBook"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        ((javax.swing.border.TitledBorder)panAuthorization.getBorder()).setTitle(resourceBundle.getString("panAuthorization"));
        lblInventoryID.setText(resourceBundle.getString("lblInventoryID"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblTotalLeaves.setText(resourceBundle.getString("lblTotalLeaves"));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboItemType", new Boolean(true));
        mandatoryMap.put("cboUsage", new Boolean(true));
        mandatoryMap.put("txtLeavesPerBook", new Boolean(true));
        mandatoryMap.put("txtReOrderLevel", new Boolean(true));
        mandatoryMap.put("txtDangerLevel", new Boolean(true));
        //mandatoryMap.put("txtInstrumentPrefix", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboItemType.setSelectedItem(observable.getCboItemType());
        cboUsage.setSelectedItem(observable.getCboUsage());
        txtLeavesPerBook.setText(observable.getTxtLeavesPerBook());
        txtReOrderLevel.setText(observable.getTxtReOrderLevel());
        txtDangerLevel.setText(observable.getTxtDangerLevel());
        //txtInstrumentPrefix.setText(observable.getTxtInstrumentPrefix());
        txtRemarks.setText(observable.getTxtRemarks());
        
        lblInventoryIDDesc.setText(observable.getLblInventoryID());
        lblTotalBooksDesc.setText(observable.getLblTotalBooks());
        lblTotalLeavesDesc.setText(observable.getLblTotalLeaves());
        
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
                /*
                 *setLblInventoryID("");
        setLblTotalBooks("");
        setLblTotalLeaves("");*/
    }
    
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboItemType((String) cboItemType.getSelectedItem());
        observable.setCboUsage((String) cboUsage.getSelectedItem());
        observable.setTxtLeavesPerBook(txtLeavesPerBook.getText());
        observable.setTxtReOrderLevel(txtReOrderLevel.getText());
        observable.setTxtDangerLevel(txtDangerLevel.getText());
        //observable.setTxtInstrumentPrefix(txtInstrumentPrefix.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        
        observable.setLblInventoryID(lblInventoryIDDesc.getText());
        observable.setLblTotalBooks(lblTotalBooksDesc.getText());
        observable.setLblTotalLeaves(lblTotalLeavesDesc.getText());
        observable.setSelectedBranchID(getSelectedBranchID());
    }
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        InventoryMasterMRB objMandatoryRB = new InventoryMasterMRB();
        cboItemType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboItemType"));
        cboUsage.setHelpMessage(lblMsg, objMandatoryRB.getString("cboUsage"));
        txtLeavesPerBook.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLeavesPerBook"));
        txtReOrderLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReOrderLevel"));
        txtDangerLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDangerLevel"));
       // txtInstrumentPrefix.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentPrefix"));
    }
    
    private void setMaxLenths() {
        txtLeavesPerBook.setMaxLength(16);
        txtLeavesPerBook.setValidation(new NumericValidation());
        
        txtReOrderLevel.setMaxLength(16);
        txtReOrderLevel.setValidation(new NumericValidation());
        
        txtDangerLevel.setMaxLength(16);
        txtDangerLevel.setValidation(new NumericValidation());
        
        txtInstrumentPrefix.setMaxLength(ClientConstants.INSTRUMENT_NO1);
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboItemType.setModel(observable.getCbmItemType());
        cboUsage.setModel(observable.getCbmUsage());
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    
/*
 **/
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panInventoryMaster = new com.see.truetransact.uicomponent.CPanel();
        lblInventoryID = new com.see.truetransact.uicomponent.CLabel();
        lblInventoryIDDesc = new com.see.truetransact.uicomponent.CLabel();
        lblItemType = new com.see.truetransact.uicomponent.CLabel();
        cboItemType = new com.see.truetransact.uicomponent.CComboBox();
        lblUsage = new com.see.truetransact.uicomponent.CLabel();
        cboUsage = new com.see.truetransact.uicomponent.CComboBox();
        lblLeavesPerBook = new com.see.truetransact.uicomponent.CLabel();
        txtLeavesPerBook = new com.see.truetransact.uicomponent.CTextField();
        lblTotalBooks = new com.see.truetransact.uicomponent.CLabel();
        lblTotalBooksDesc = new com.see.truetransact.uicomponent.CLabel();
        lblTotalLeaves = new com.see.truetransact.uicomponent.CLabel();
        lblTotalLeavesDesc = new com.see.truetransact.uicomponent.CLabel();
        lblReOrderLevel = new com.see.truetransact.uicomponent.CLabel();
        txtReOrderLevel = new com.see.truetransact.uicomponent.CTextField();
        lblDangerLevel = new com.see.truetransact.uicomponent.CLabel();
        txtDangerLevel = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentPrefix = new com.see.truetransact.uicomponent.CLabel();
        txtInstrumentPrefix = new com.see.truetransact.uicomponent.CTextField();
        panAuthorization = new com.see.truetransact.uicomponent.CPanel();
        lblTransaIn = new com.see.truetransact.uicomponent.CLabel();
        lblTransaInDesc = new com.see.truetransact.uicomponent.CLabel();
        lblTransaOut = new com.see.truetransact.uicomponent.CLabel();
        lblTransaOutDesc = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrLoanProduct = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(399, 450));
        setPreferredSize(new java.awt.Dimension(399, 450));

        panInventoryMaster.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInventoryMaster.setMinimumSize(new java.awt.Dimension(393, 400));
        panInventoryMaster.setPreferredSize(new java.awt.Dimension(393, 400));
        panInventoryMaster.setLayout(new java.awt.GridBagLayout());

        lblInventoryID.setText("Inventory ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblInventoryID, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblInventoryIDDesc, gridBagConstraints);

        lblItemType.setText("Item Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblItemType, gridBagConstraints);

        cboItemType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboItemType.setPopupWidth(130);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(cboItemType, gridBagConstraints);

        lblUsage.setText("Usage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblUsage, gridBagConstraints);

        cboUsage.setMinimumSize(new java.awt.Dimension(100, 21));
        cboUsage.setPopupWidth(160);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(cboUsage, gridBagConstraints);

        lblLeavesPerBook.setText("No. Of Leaves Per Book");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblLeavesPerBook, gridBagConstraints);

        txtLeavesPerBook.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(txtLeavesPerBook, gridBagConstraints);

        lblTotalBooks.setText("Total No. Of Books");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblTotalBooks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblTotalBooksDesc, gridBagConstraints);

        lblTotalLeaves.setText("Total No. Of Leaves");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblTotalLeaves, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblTotalLeavesDesc, gridBagConstraints);

        lblReOrderLevel.setText("ReOrder Level");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblReOrderLevel, gridBagConstraints);

        txtReOrderLevel.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReOrderLevel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReOrderLevelFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(txtReOrderLevel, gridBagConstraints);

        lblDangerLevel.setText("Danger Level");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblDangerLevel, gridBagConstraints);

        txtDangerLevel.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDangerLevel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDangerLevelFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(txtDangerLevel, gridBagConstraints);

        lblInstrumentPrefix.setText("Instrument Prefix No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblInstrumentPrefix, gridBagConstraints);

        txtInstrumentPrefix.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(txtInstrumentPrefix, gridBagConstraints);

        panAuthorization.setBorder(javax.swing.BorderFactory.createTitledBorder("Pending For Authorization"));
        panAuthorization.setLayout(new java.awt.GridBagLayout());

        lblTransaIn.setText("Transaction In");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorization.add(lblTransaIn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorization.add(lblTransaInDesc, gridBagConstraints);

        lblTransaOut.setText("Transaction Out");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panAuthorization.add(lblTransaOut, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorization.add(lblTransaOutDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panInventoryMaster.add(panAuthorization, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryMaster.add(txtRemarks, gridBagConstraints);

        getContentPane().add(panInventoryMaster, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        tbrLoantProduct.setMaximumSize(new java.awt.Dimension(424, 29));
        tbrLoantProduct.setMinimumSize(new java.awt.Dimension(424, 29));
        tbrLoantProduct.setPreferredSize(new java.awt.Dimension(424, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrLoanProduct.add(mnuProcess);

        setJMenuBar(mbrLoanProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void txtReOrderLevelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReOrderLevelFocusLost
        // TODO add your handling code here:
        if(!verified){
            String message = verifyReOrder();
            if(message.length() > 0){
                displayAlert(message);
            }
            verified = false;
        }
    }//GEN-LAST:event_txtReOrderLevelFocusLost
    
    private void txtDangerLevelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDangerLevelFocusLost
        // TODO add your handling code here:
        if(!verified){
            String message = verifyReOrder();
            if(message.length() > 0){
                displayAlert(message);
            }
            verified = false;
        }
    }//GEN-LAST:event_txtDangerLevelFocusLost
   private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    private String verifyReOrder(){
        String message = "";
        
        if(!(txtDangerLevel.getText().equalsIgnoreCase("")
        || txtReOrderLevel.getText().equalsIgnoreCase(""))){
            if(CommonUtil.convertObjToDouble(txtDangerLevel.getText()).doubleValue() >  CommonUtil.convertObjToDouble(txtReOrderLevel.getText()).doubleValue()){
                message = resourceBundle.getString("REORDER_WARNING");
            }
        }
        verified = true;
        return message;
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){      
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("ITEM ID", lblInventoryIDDesc.getText());
            ClientUtil.execute("authInventoryMaster", singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(lblInventoryIDDesc.getText());
            btnCancelActionPerformed(null);
            observable.setResult(observable.getActionType());
            observable.setResultStatus();
        } else{
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectInventoryMasterTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authInventoryMaster");
            
            isFilled = false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            
            whereMap = null;
            
            //__ If there's no data to be Authorized, call Cancel action...  
//            if(!isModified()){
//                setButtonEnableDisable();
//                btnCancelActionPerformed(null);
//            }
        }
        
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    private void resetTransData(){
        lblTransaInDesc.setText("");
        lblTransaOutDesc.setText("");
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
//        super.removeEditLock(lblInventoryIDDesc.getText());
        observable.resetForm(); //__ Reset the fields in the UI to null...
        resetTransData();
        ClientUtil.enableDisable(this, false); //__ Disables the panel...
        
        if(!btnSave.isEnabled()){
            btnSave.setEnabled(true);
        }
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(lblInventoryIDDesc.getText());
        
        setButtonEnableDisable(); //__ Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
            observable.getActionType()!=ClientConstants.ACTIONTYPE_REJECT)
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL); //__ Sets the Action Type to be performed...
        observable.setStatus(); //__ To set the Value of lblStatus...
        
        isFilled = false;
        viewType = -1;
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        StringBuffer str = new StringBuffer();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInventoryMaster);
        observable.setLblInventoryID(lblInventoryIDDesc.getText());
        if(mandatoryMessage.length() > 0){
            str.append(mandatoryMessage + "\n");
        }
        
        String alert = verifyReOrder();
        if(alert.length() > 0){
            str.append(alert);
        }
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && str.toString().length() > 0 ){
            displayAlert(str.toString());
        }else{
            observable.doAction();// To perform the necessary operation depending on the Action type...
            
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
//                super.removeEditLock(lblInventoryIDDesc.getText());
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("ITEM ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("ITEM ID")) {
                        lockMap.put("ITEM ID", observable.getProxyReturnMap().get("ITEM ID"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("ITEM ID", observable.getLblInventoryID());
                }
                setEditLockMap(lockMap);
                setEditLock();
                observable.resetForm();// Reset the fields in the UI to null...
                resetTransData();
                ClientUtil.enableDisable(this, false);// Disables the panel...
                setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
                observable.setResultStatus();// To Reset the Value of lblStatus...
                
                //__ Make the Screen Closable..
                setModified(false);
            }
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.resetForm();// Reset the fields in the UI to null...
        //observable.resetLable();// Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.resetForm();// Reset the fields in the UI to null...
        //observable.resetLable();// Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field==VIEW){ //__ Edit=0 and Delete=1
            final HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            ArrayList lst = new ArrayList();
            lst.add("ITEM ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewInventoryMaster");
        }
        new ViewAll(this, viewMap).show();        
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW ) {
            isFilled = true;
            hash.put(CommonConstants.MAP_WHERE, hash.get("ITEM ID"));
            observable.populateData(hash);// Called to display the Data in the UI fields...
            // To set the Value of Transaction Id...
            
            // To set the Value of Transaction Id in UI...
            //-----observable.setLblTransactionId(INWARDID);
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||viewType==AUTHORIZE || viewType==VIEW ) {
                ClientUtil.enableDisable(this, false);     // Disables the panel...
            }else{
                if(hash.get("AUTHORIZE_STATUS").equals("AUTHORIZED"))
                    ClientUtil.enableDisable(this, false);     // Enables the panel...
                else
                    ClientUtil.enableDisable(this, true);
                //                setFieldsEnable(false);
            }
            HashMap resultIn = observable.getBookQuanIn();
            lblTransaInDesc.setText(String.valueOf(CommonUtil.convertObjToInt(resultIn.get("BOOK_QUAN_IN"))));
            HashMap resultOut = observable.getBookQuanOut();
            lblTransaOutDesc.setText(String.valueOf(CommonUtil.convertObjToInt(resultOut.get("BOOK_QUAN_OUT"))));
            resultIn = null;
            resultOut = null;
            
            observable.setStatus();             // To set the Value of lblStatus...
            setButtonEnableDisable();         // Enables or Disables the buttons and menu Items depending on their previous state...
            if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            //__ To Save the data in the Internal Frame...
            if(hash.containsKey("AUTHORIZE_STATUS")){
            if(hash.get("AUTHORIZE_STATUS").equals("AUTHORIZED")){
                btnSave.setEnabled(false);
            }
            }
            setModified(true);
        }
    }
    
    private void setFieldsEnable(boolean value){
        cboItemType.setEnabled(value);
        cboUsage.setEnabled(value);
        txtLeavesPerBook.setEnabled(value);
        //txtInstrumentPrefix.setEnabled(value);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();              // Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, true);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
        setButtonEnableDisable();           // Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();             // To set the Value of lblStatus...
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboItemType;
    private com.see.truetransact.uicomponent.CComboBox cboUsage;
    private com.see.truetransact.uicomponent.CLabel lblDangerLevel;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentPrefix;
    private com.see.truetransact.uicomponent.CLabel lblInventoryID;
    private com.see.truetransact.uicomponent.CLabel lblInventoryIDDesc;
    private com.see.truetransact.uicomponent.CLabel lblItemType;
    private com.see.truetransact.uicomponent.CLabel lblLeavesPerBook;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblReOrderLevel;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
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
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTotalBooks;
    private com.see.truetransact.uicomponent.CLabel lblTotalBooksDesc;
    private com.see.truetransact.uicomponent.CLabel lblTotalLeaves;
    private com.see.truetransact.uicomponent.CLabel lblTotalLeavesDesc;
    private com.see.truetransact.uicomponent.CLabel lblTransaIn;
    private com.see.truetransact.uicomponent.CLabel lblTransaInDesc;
    private com.see.truetransact.uicomponent.CLabel lblTransaOut;
    private com.see.truetransact.uicomponent.CLabel lblTransaOutDesc;
    private com.see.truetransact.uicomponent.CLabel lblUsage;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAuthorization;
    private com.see.truetransact.uicomponent.CPanel panInventoryMaster;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CTextField txtDangerLevel;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentPrefix;
    private com.see.truetransact.uicomponent.CTextField txtLeavesPerBook;
    private com.see.truetransact.uicomponent.CTextField txtReOrderLevel;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
    
}
