/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InventoryDetailsUI.java
 *
 * Created on August 23, 2004, 3:29 PM
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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CButtonGroup;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
/**
 *
 * @author  rahul
 */
public class InventoryDetailsUI extends CInternalFrame implements java.util.Observer, UIMandatoryField{
    HashMap mandatoryMap;
    
    InventoryDetailsOB observable;
//    final InventoryDetailsRB resourceBundle = new InventoryDetailsRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.supporting.inventory.InventoryDetailsRB", ProxyParameters.LANGUAGE);
    
    
    final int EDIT=0, DELETE=1, ITEMNO=2, AUTHORIZE=3, VIEW = 4;
    int viewType=-1;
    boolean isFilled = false;
    final String TRANSIN = "TRANS_IN";
    /*
     * To Store the Created-By Data...
     */
    String CREATEDBY = "";
    String STATUS = "";
  //  IntroducerUI introducerUI = new IntroducerUI(SCREEN);
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private String ScreenName="";
    /** Creates new form InventoryDetailsUI */
    public InventoryDetailsUI() {
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        
        setObservable();
        initComponentData();    // Fill all the combo boxes...
        setMaxLenths(); // To set the Numeric Validation and the Maximum length of the Text fields...
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panInventoryDetails);
        setHelpMessage();
        
        observable.resetStatus();   //__ to reset the status
        observable.resetForm();     //__ To reset all the fields in UI...
        observable.resetStatus();   //__ To reset the Satus in the UI...
        
        ClientUtil.enableDisable(this, false);  //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();   //__ Enables/Disables the necessary buttons and menu items...
        btnItemID.setEnabled(false);
        txtBookFrom.setEditable(false);
//        txtBookFrom.setEnabled(false);
        //        txtBookTo.setEditable(false);
    }
    
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        observable = InventoryDetailsOB.getInstance();
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
        btnItemID.setName("btnItemID");
        btnItemLst.setName("btnItemLst");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblAccountNoDesc.setName("lblAccountNoDesc");
        lblAccountNo.setName("lblAccountNo");
        cboTransType.setName("cboTransType");
        lblBookFrom.setName("lblBookFrom");
        lblBookQuantity.setName("lblBookQuantity");
        lblInstrumentPrefix.setName("lblInstrumentPrefix");
        lblBookSeries.setName("lblBookSeries");
        lblBookTo.setName("lblBookTo");
        lblChequeFrom.setName("lblChequeFrom");
        lblChequeNo.setName("lblChequeNo");
        lblChequeTo.setName("lblChequeTo");
        lblItemID.setName("lblItemID");
        lblMsg.setName("lblMsg");
        lblProdType.setName("lblProdType");
        lblProdTypeDesc.setName("lblProdTypeDesc");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");
        lblTranID.setName("lblTranID");
        lblTranIDDesc.setName("lblTranIDDesc");
        lblTransDate.setName("lblTransDate");
        lblTransDateDesc.setName("lblTransDateDesc");
        lblItemSubType.setName("lblItemSubType");
        lblItemSubTypeDesc.setName("lblItemSubTypeDesc");
        lblLeavesNO.setName("lblLeavesNO");
        lblLeavesNODesc.setName("lblLeavesNODesc");
        lblAvailableBooks.setName("lblAvailableBooks");
        lblAvailableBooksDesc.setName("lblAvailableBooksDesc");
        lblTransType.setName("lblTransType");
        mbrLoanProduct.setName("mbrLoanProduct");
        panBookSeries.setName("panBookSeries");
        panItemId.setName("panItemId");
        panChequeNo.setName("panChequeNo");
        panInventoryDetails.setName("panInventoryDetails");
        panStatus.setName("panStatus");
        txtBookFrom.setName("txtBookFrom");
        txtBookTo.setName("txtBookTo");
        txtChequeFrom.setName("txtChequeFrom");
        txtChequeTo.setName("txtChequeTo");
        txtItemID.setName("txtItemID");
        txtBookQuantity.setName("txtBookQuantity");
        txtInstrumentPrefix.setName("txtInstrumentPrefix");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        //        InventoryDetailsRB resourceBundle = new InventoryDetailsRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTranIDDesc.setText(resourceBundle.getString("lblTranIDDesc"));
        lblItemSubTypeDesc.setText(resourceBundle.getString("lblItemSubTypeDesc"));
        lblLeavesNODesc.setText(resourceBundle.getString("lblLeavesNODesc"));
        lblAvailableBooksDesc.setText(resourceBundle.getString("lblAvailableBooksDesc"));
        lblChequeTo.setText(resourceBundle.getString("lblChequeTo"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblBookFrom.setText(resourceBundle.getString("lblBookFrom"));
        lblTransDate.setText(resourceBundle.getString("lblTransDate"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblBookTo.setText(resourceBundle.getString("lblBookTo"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblAccountNoDesc.setText(resourceBundle.getString("lblAccountNoDesc"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblChequeFrom.setText(resourceBundle.getString("lblChequeFrom"));
        lblTransDateDesc.setText(resourceBundle.getString("lblTransDateDesc"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblBookSeries.setText(resourceBundle.getString("lblBookSeries"));
        lblProdTypeDesc.setText(resourceBundle.getString("lblProdTypeDesc"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblItemID.setText(resourceBundle.getString("lblItemID"));
        lblTranID.setText(resourceBundle.getString("lblTranID"));
        lblItemSubType.setText(resourceBundle.getString("lblItemSubType"));
        lblLeavesNO.setText(resourceBundle.getString("lblLeavesNO"));
        lblAvailableBooks.setText(resourceBundle.getString("lblAvailableBooks"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        btnItemID.setText(resourceBundle.getString("btnItemID"));
        btnItemLst.setText(resourceBundle.getString("btnItemLst"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblBookQuantity.setText(resourceBundle.getString("lblBookQuantity"));
        lblInstrumentPrefix.setText(resourceBundle.getString("lblInstrumentPrefix"));
        lblChequeNo.setText(resourceBundle.getString("lblChequeNo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblTransType.setText(resourceBundle.getString("lblTransType"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtItemID", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("txtBookQuantity", new Boolean(true));
        mandatoryMap.put("txtInstrumentPrefix", new Boolean(true));
        mandatoryMap.put("txtBookFrom", new Boolean(true));
        mandatoryMap.put("txtBookTo", new Boolean(true));
        mandatoryMap.put("txtChequeFrom", new Boolean(true));
        mandatoryMap.put("txtChequeTo", new Boolean(true));
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
        txtItemID.setText(observable.getTxtItemID());
        cboTransType.setSelectedItem(observable.getCboTransType());
        txtBookQuantity.setText(observable.getTxtBookQuantity());
        txtInstrumentPrefix.setText(observable.getTxtInstrumentPrefix());
        txtBookFrom.setText(observable.getTxtBookFrom());
        txtBookTo.setText(observable.getTxtBookTo());
        txtChequeFrom.setText(observable.getTxtChequeFrom());
        txtChequeTo.setText(observable.getTxtChequeTo());
        
        lblTranIDDesc.setText(observable.getLblTransID());
        lblTransDateDesc.setText(observable.getLblTransDate());
        lblItemSubTypeDesc.setText(observable.getLblItemSubType());
        lblLeavesNODesc.setText(observable.getLblLeavesNO());
        lblAvailableBooksDesc.setText(observable.getLblAvailableBooksDesc());
        lblProdTypeDesc.setText(observable.getLblProdType());
        lblAccountNoDesc.setText(observable.getLblAcctNo());
        //lblInstrumentPrefix.setText(observable.getLblInstrumentPrefix());
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
    }
    
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        
        observable.setTxtItemID(txtItemID.getText());
        observable.setCboTransType((String) cboTransType.getSelectedItem());
        observable.setTxtBookQuantity(txtBookQuantity.getText());
        observable.setTxtInstrumentPrefix(txtInstrumentPrefix.getText());
        observable.setTxtBookFrom(txtBookFrom.getText());
        observable.setTxtBookTo(txtBookTo.getText());
        observable.setTxtChequeFrom(txtChequeFrom.getText());
        observable.setTxtChequeTo(txtChequeTo.getText());
        
        observable.setLblTransID(lblTranIDDesc.getText());
        observable.setLblItemSubType(lblItemSubTypeDesc.getText());
        observable.setLblAvailableBooksDesc(lblAvailableBooksDesc.getText());
        observable.setLblLeavesNO(lblLeavesNODesc.getText());
//        observable.setLblInstrumentPrefix(lblInstrumentPrefix.getText());
        observable.setSelectedBranchID(getSelectedBranchID());
    }
    
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        InventoryDetailsMRB objMandatoryRB = new InventoryDetailsMRB();
        txtItemID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtItemID"));
        cboTransType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransType"));
        txtBookQuantity.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBookQuantity"));
        txtInstrumentPrefix.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentPrefix"));
        txtBookFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBookFrom"));
        txtBookTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBookTo"));
        txtChequeFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequeFrom"));
        txtChequeTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequeTo"));
    }
    
    private void setMaxLenths() {
        txtItemID.setMaxLength(16);
        txtBookQuantity.setMaxLength(16);
        txtBookQuantity.setValidation(new NumericValidation());
        
        txtInstrumentPrefix.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtBookFrom.setMaxLength(ClientConstants.CHEQUE_SERIES);
        txtBookFrom.setValidation(new NumericValidation());
        
        txtBookTo.setMaxLength(ClientConstants.CHEQUE_SERIES);
        txtBookTo.setValidation(new NumericValidation());
        
        txtChequeFrom.setMaxLength(ClientConstants.INSTRUMENT_NO2);
        txtChequeFrom.setValidation(new NumericValidation());
        
        txtChequeTo.setMaxLength(ClientConstants.INSTRUMENT_NO2);
        txtChequeTo.setValidation(new NumericValidation());
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboTransType.setModel(observable.getCbmTransType());
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panInventoryDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTranID = new com.see.truetransact.uicomponent.CLabel();
        lblTranIDDesc = new com.see.truetransact.uicomponent.CLabel();
        lblTransDate = new com.see.truetransact.uicomponent.CLabel();
        lblTransDateDesc = new com.see.truetransact.uicomponent.CLabel();
        lblItemID = new com.see.truetransact.uicomponent.CLabel();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        lblBookQuantity = new com.see.truetransact.uicomponent.CLabel();
        txtBookQuantity = new com.see.truetransact.uicomponent.CTextField();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblBookSeries = new com.see.truetransact.uicomponent.CLabel();
        panBookSeries = new com.see.truetransact.uicomponent.CPanel();
        lblBookFrom = new com.see.truetransact.uicomponent.CLabel();
        txtBookFrom = new com.see.truetransact.uicomponent.CTextField();
        lblBookTo = new com.see.truetransact.uicomponent.CLabel();
        txtBookTo = new com.see.truetransact.uicomponent.CTextField();
        lblChequeNo = new com.see.truetransact.uicomponent.CLabel();
        panChequeNo = new com.see.truetransact.uicomponent.CPanel();
        lblChequeFrom = new com.see.truetransact.uicomponent.CLabel();
        txtChequeFrom = new com.see.truetransact.uicomponent.CTextField();
        lblChequeTo = new com.see.truetransact.uicomponent.CLabel();
        txtChequeTo = new com.see.truetransact.uicomponent.CTextField();
        lblProdTypeDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNoDesc = new com.see.truetransact.uicomponent.CLabel();
        lblItemSubType = new com.see.truetransact.uicomponent.CLabel();
        lblLeavesNO = new com.see.truetransact.uicomponent.CLabel();
        lblItemSubTypeDesc = new com.see.truetransact.uicomponent.CLabel();
        lblLeavesNODesc = new com.see.truetransact.uicomponent.CLabel();
        lblAvailableBooks = new com.see.truetransact.uicomponent.CLabel();
        lblAvailableBooksDesc = new com.see.truetransact.uicomponent.CLabel();
        txtInstrumentPrefix = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentPrefix = new com.see.truetransact.uicomponent.CLabel();
        panItemId = new com.see.truetransact.uicomponent.CPanel();
        txtItemID = new com.see.truetransact.uicomponent.CTextField();
        btnItemID = new com.see.truetransact.uicomponent.CButton();
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
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnItemLst = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(554, 527));
        setPreferredSize(new java.awt.Dimension(554, 527));

        panInventoryDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Inventory Details"));
        panInventoryDetails.setMinimumSize(new java.awt.Dimension(284, 282));
        panInventoryDetails.setPreferredSize(new java.awt.Dimension(284, 282));
        panInventoryDetails.setLayout(new java.awt.GridBagLayout());

        lblTranID.setText("Transaction ID");
        lblTranID.setMaximumSize(new java.awt.Dimension(84, 21));
        lblTranID.setMinimumSize(new java.awt.Dimension(84, 21));
        lblTranID.setPreferredSize(new java.awt.Dimension(84, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblTranID, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblTranIDDesc, gridBagConstraints);

        lblTransDate.setText("Transaction Date");
        lblTransDate.setMaximumSize(new java.awt.Dimension(99, 21));
        lblTransDate.setMinimumSize(new java.awt.Dimension(99, 21));
        lblTransDate.setPreferredSize(new java.awt.Dimension(99, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblTransDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblTransDateDesc, gridBagConstraints);

        lblItemID.setText("Item ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblItemID, gridBagConstraints);

        lblTransType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblTransType, gridBagConstraints);

        cboTransType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(cboTransType, gridBagConstraints);

        lblBookQuantity.setText("Book Quantity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblBookQuantity, gridBagConstraints);

        txtBookQuantity.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBookQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBookQuantityFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(txtBookQuantity, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblProdType, gridBagConstraints);

        lblAccountNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblAccountNo, gridBagConstraints);

        lblBookSeries.setText("Book Series");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblBookSeries, gridBagConstraints);

        panBookSeries.setMinimumSize(new java.awt.Dimension(270, 21));
        panBookSeries.setPreferredSize(new java.awt.Dimension(270, 21));
        panBookSeries.setLayout(new java.awt.GridBagLayout());

        lblBookFrom.setText("From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 7);
        panBookSeries.add(lblBookFrom, gridBagConstraints);

        txtBookFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBookFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBookFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBookSeries.add(txtBookFrom, gridBagConstraints);

        lblBookTo.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBookSeries.add(lblBookTo, gridBagConstraints);

        txtBookTo.setMinimumSize(new java.awt.Dimension(100, 21));
        panBookSeries.add(txtBookTo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(panBookSeries, gridBagConstraints);

        lblChequeNo.setText("Instrument No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblChequeNo, gridBagConstraints);

        panChequeNo.setMinimumSize(new java.awt.Dimension(270, 21));
        panChequeNo.setPreferredSize(new java.awt.Dimension(270, 21));
        panChequeNo.setLayout(new java.awt.GridBagLayout());

        lblChequeFrom.setText("From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        panChequeNo.add(lblChequeFrom, gridBagConstraints);

        txtChequeFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChequeFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChequeFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panChequeNo.add(txtChequeFrom, gridBagConstraints);

        lblChequeTo.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panChequeNo.add(lblChequeTo, gridBagConstraints);

        txtChequeTo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChequeTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChequeToFocusLost(evt);
            }
        });
        panChequeNo.add(txtChequeTo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(panChequeNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblProdTypeDesc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblAccountNoDesc, gridBagConstraints);

        lblItemSubType.setText("Item Sub Type");
        lblItemSubType.setMaximumSize(new java.awt.Dimension(85, 21));
        lblItemSubType.setMinimumSize(new java.awt.Dimension(85, 21));
        lblItemSubType.setPreferredSize(new java.awt.Dimension(85, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblItemSubType, gridBagConstraints);

        lblLeavesNO.setText("NO of Leaves");
        lblLeavesNO.setMaximumSize(new java.awt.Dimension(82, 21));
        lblLeavesNO.setMinimumSize(new java.awt.Dimension(82, 21));
        lblLeavesNO.setPreferredSize(new java.awt.Dimension(82, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblLeavesNO, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblItemSubTypeDesc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblLeavesNODesc, gridBagConstraints);

        lblAvailableBooks.setText("Available Books");
        lblAvailableBooks.setMaximumSize(new java.awt.Dimension(92, 21));
        lblAvailableBooks.setMinimumSize(new java.awt.Dimension(92, 21));
        lblAvailableBooks.setPreferredSize(new java.awt.Dimension(92, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 11, 4);
        panInventoryDetails.add(lblAvailableBooks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 11, 4);
        panInventoryDetails.add(lblAvailableBooksDesc, gridBagConstraints);

        txtInstrumentPrefix.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 51, 0, 0);
        panInventoryDetails.add(txtInstrumentPrefix, gridBagConstraints);

        lblInstrumentPrefix.setText("Instrument Prefix ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInventoryDetails.add(lblInstrumentPrefix, gridBagConstraints);

        panItemId.setMinimumSize(new java.awt.Dimension(140, 24));
        panItemId.setPreferredSize(new java.awt.Dimension(140, 24));
        panItemId.setLayout(new java.awt.GridBagLayout());

        txtItemID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtItemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panItemId.add(txtItemID, gridBagConstraints);

        btnItemID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnItemID.setToolTipText("Item ID");
        btnItemID.setMinimumSize(new java.awt.Dimension(31, 23));
        btnItemID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnItemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemIDActionPerformed(evt);
            }
        });
        panItemId.add(btnItemID, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panInventoryDetails.add(panItemId, gridBagConstraints);

        getContentPane().add(panInventoryDetails, java.awt.BorderLayout.CENTER);

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

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace30);

        btnItemLst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnItemLst.setToolTipText("Item Balance Listing");
        btnItemLst.setMinimumSize(new java.awt.Dimension(31, 23));
        btnItemLst.setPreferredSize(new java.awt.Dimension(21, 21));
        btnItemLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemLstActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnItemLst);

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

    private void btnItemLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemLstActionPerformed
        // TODO add your handling code here:
        popUp(ITEMNO);
        ClientUtil.enableDisable(panInventoryDetails, false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        txtBookFrom.setText("");
    }//GEN-LAST:event_btnItemLstActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();

    }//GEN-LAST:event_btnViewActionPerformed

    private void txtItemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemIDActionPerformed
    
    private void txtChequeFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChequeFromFocusLost
        // TODO add your handling code here:
        calculateChequesTo();
    }//GEN-LAST:event_txtChequeFromFocusLost
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
    private void calculateChequesTo(){
        if(!(txtItemID.getText().equalsIgnoreCase("")
        || txtBookQuantity.getText().equalsIgnoreCase("")
        || txtChequeFrom.getText().equalsIgnoreCase(""))){
            int Leaves = observable.getLeavesNo(txtItemID.getText());
            int books = CommonUtil.convertObjToInt(txtBookQuantity.getText());
//            txtChequeTo.setText(String.valueOf( (Leaves * books) + CommonUtil.convertObjToInt(txtChequeFrom.getText()) - 1));
            txtChequeTo.setText(String.valueOf( (Leaves * books) + Long.parseLong(txtChequeFrom.getText()) - 1));
        }
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
    /** To authorize the tranasaction   */
    public void authorize(HashMap map) {
        observable.setAuthorizeMap(map);
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(lblTranIDDesc.getText());
            isFilled = false;
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
    }
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("TRANSACTION ID", lblTranIDDesc.getText());
            authDataMap.put("BOOK QUANTITY", CommonUtil.convertObjToLong(txtBookQuantity.getText()));
            authDataMap.put("ITEM ID", txtItemID.getText());
            authDataMap.put("TRANSACTION TYPE", CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected()));
            authDataMap.put(CommonConstants.USER_ID, STATUS);
            authDataMap.put("CREATEDBY", CREATEDBY);
            authDataMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
        } else {
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectInventoryDetailsTOList");
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...  
//            if(!isModified()){
//                setButtonEnableDisable();
//                btnCancelActionPerformed(null);
//            }
        }
    }
    private void txtBookQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookQuantityFocusLost
        // TODO add your handling code here:
        calculateBooksTo();
        calculateChequesTo();
    }//GEN-LAST:event_txtBookQuantityFocusLost
    
    private void txtChequeToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChequeToFocusLost
        // TODO add your handling code here:
        String message = txtChequeToRule();
        if(message.length() > 0){
            displayAlert(message);
        }
    }//GEN-LAST:event_txtChequeToFocusLost
    private String txtChequeToRule(){
        String message = "";
        
        if(!(txtChequeFrom.getText().equalsIgnoreCase("")
        || txtChequeTo.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble(txtChequeFrom.getText()) > Double.parseDouble(txtChequeTo.getText()) ){
                message = resourceBundle.getString("CHEQUENOWARNING");
            }
        }
        return message;
    }
    private void txtBookFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookFromFocusLost
        // TODO add your handling code here://txtBookQuantity, txtBookFrom
        calculateBooksTo();
    }//GEN-LAST:event_txtBookFromFocusLost
    private void calculateBooksTo(){
        if(!(txtBookQuantity.getText().equalsIgnoreCase("")
        || txtBookFrom.getText().equalsIgnoreCase(""))){
            long totalBooks = CommonUtil.convertObjToInt(txtBookQuantity.getText()) + Long.parseLong(CommonUtil.convertObjToStr(txtBookFrom.getText())) - 1;
            txtBookTo.setText(String.valueOf(totalBooks));
        }
    }
    private void btnItemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemIDActionPerformed
        // TODO add your handling code here:
        popUp(ITEMNO);
    }//GEN-LAST:event_btnItemIDActionPerformed
    
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
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        
        observable.resetForm(); //__ Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, false); //__ Disables the panel...
        
        //__ 
        if(!btnSave.isEnabled()){
            btnSave.setEnabled(true);
        }
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(lblTranIDDesc.getText());    
        setButtonEnableDisable(); //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL); //__ Sets the Action Type to be performed...
        observable.setStatus(); //__ To set the Value of lblStatus...
        btnItemID.setEnabled(false);
        
        viewType = -1;
        isFilled = false;
        
        //__ Make the Screen Closable..
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        StringBuffer strBAlert = new StringBuffer();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInventoryDetails);
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            strBAlert.append(mandatoryMessage+"\n");
        }
        if(txtChequeToRule().length() > 0){
            strBAlert.append(txtChequeToRule()+"\n");
        }
        //__ To Validate The Instrument Nos...
        if(observable.validateInstruments()){
            strBAlert.append(resourceBundle.getString("ISTRU_WARNING")+"\n");  
        }
//        if(observable.getAvailableBooks()){
//            strBAlert.append("Available Books is less than the Book Quantity");
//        }
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0 ){
            displayAlert(strBAlert.toString());
        }
        else{
            observable.doAction(); // __ To perform the necessary operation depending on the Action type...
            
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("TRANSACTION ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("TRANSACTION ID")) {
                        lockMap.put("TRANSACTION ID", observable.getProxyReturnMap().get("TRANSACTION ID"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("TRANSACTION ID", observable.getLblTransID());
                }
                setEditLockMap(lockMap);
                setEditLock();
                observable.resetForm();       //__ Reset the fields in the UI to null...
                ClientUtil.enableDisable(this, false); //__ Disables the panel...
                setButtonEnableDisable();     //__ Enables or Disables the buttons and menu Items depending on their previous state...
                observable.setResultStatus(); //__ To Reset the Value of lblStatus...
                btnItemID.setEnabled(false);
                
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
        observable.resetForm(); //__ Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__Sets the Action Type to be performed...
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.resetForm(); //__ Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        
        viewType = field;
        if(field==EDIT || field==DELETE || field==VIEW){//Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("TRANSACTION ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewInventoryDetails");
            new ViewAll(this, viewMap).show();
            
        }else{
            viewMap.put(CommonConstants.MAP_NAME, "viewInventoryMaster");
            new ViewAll(this, viewMap, true).show();
        }
//        new ViewAll(this, viewMap).show();
    }

  
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap map = new HashMap();
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
        }

        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW ) {
            isFilled = true;
            hash.put(CommonConstants.MAP_WHERE, hash.get("TRANSACTION ID"));
            if(viewType==AUTHORIZE){
                CREATEDBY = CommonUtil.convertObjToStr(hash.get("CREATED BY"));
                STATUS = CommonUtil.convertObjToStr(hash.get("STATUS"));
//                 if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
//                }
              
            }
            observable.populateData(hash);// Called to display the Data in the UI fields...
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||viewType==AUTHORIZE ||viewType==VIEW) {
             
                ClientUtil.enableDisable(this, false);     // Disables the panel...
                btnItemID.setEnabled(false);
            }else{
                if(hash.containsValue("AUTHORIZED") || hash.containsValue("REJECTED")){
                    ClientUtil.enableDisable(this, false); 
//                    btnSave.setEnabled(false);
                }
                else{
                    ClientUtil.enableDisable(this, true);     // Enables the panel...
                    btnItemID.setEnabled(true);
                    txtBookTo.setEditable(false);
                    txtChequeTo.setEditable(false);  // Enables the panel...   
                }
            }
            cboTransType.setEnabled(false);
            observable.setStatus();             // To set the Value of lblStatus...
            setButtonEnableDisable();         // Enables or Disables the buttons and menu Items depending on their previous state...
            txtItemID.setEnabled(false);
        }else if (viewType==ITEMNO) {
            txtItemID.setText(CommonUtil.convertObjToStr(hash.get("ITEM ID")));
            lblItemSubTypeDesc.setText(CommonUtil.convertObjToStr(hash.get("ITEM SUB TYPE")));
            lblLeavesNODesc.setText(CommonUtil.convertObjToStr(hash.get("LEAVES PER BOOK")));
            lblAvailableBooksDesc.setText(CommonUtil.convertObjToStr(hash.get("AVAILABLE BOOKS")));
            hash.put("BRANCH_ID", getSelectedBranchID());
            List lst = (List) ClientUtil.executeQuery("getBookSeriesNo", hash);
            if(lst != null)
                if(lst.size() > 0){
                   map = (HashMap)lst.get(0);
                   txtBookFrom.setText(CommonUtil.convertObjToStr(map.get("BOOK_SLNO")));
                   map = null;
                }
        }
        if(viewType==AUTHORIZE){
              System.out.println("@@@@@@hash"+hash);
                hash.put("BRANCH_ID", getSelectedBranchID());
                HashMap hMap = new HashMap();
                hMap.put("ITEM ID",CommonUtil.convertObjToStr(hash.get("ITEM ID")));
                hMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                List lst = (List) ClientUtil.executeQuery("getInvenCheqDetails", hMap);
                if(lst != null)
                    if(lst.size() > 0){
                      map = (HashMap) lst.get(0);
                      lblItemSubTypeDesc.setText(CommonUtil.convertObjToStr(map.get("ITEM SUB TYPE")));
                      lblLeavesNODesc.setText(CommonUtil.convertObjToStr(map.get("LEAVES PER BOOK")));
                      lblAvailableBooksDesc.setText(CommonUtil.convertObjToStr(map.get("AVAILABLE BOOKS")));
                }
            if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                btnReject.setEnabled(true);
                btnSave.setEnabled(false);
            }
        }
        //__ To Save the data in the Internal Frame...
        if(hash.containsValue("AUTHORIZED") && ((viewType==EDIT ) || (viewType==DELETE))){
                    btnSave.setEnabled(false);
                }
        setModified(true);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();              //__ Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, true);//__ Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW); //__Sets the Action Type to be performed...
        setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();             //__ To set the Value of lblStatus...
        btnItemID.setEnabled(true);
        txtBookTo.setEditable(false);
        //txtBookFrom.setEditable(false);
        txtBookFrom.setEditable(true);
        txtChequeTo.setEditable(false);
        cboTransType.setSelectedItem(((ComboBoxModel) cboTransType.getModel()).getDataForKey(TRANSIN));
        cboTransType.setEnabled(false);
        
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
    private com.see.truetransact.uicomponent.CButton btnItemID;
    private com.see.truetransact.uicomponent.CButton btnItemLst;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountNoDesc;
    private com.see.truetransact.uicomponent.CLabel lblAvailableBooks;
    private com.see.truetransact.uicomponent.CLabel lblAvailableBooksDesc;
    private com.see.truetransact.uicomponent.CLabel lblBookFrom;
    private com.see.truetransact.uicomponent.CLabel lblBookQuantity;
    private com.see.truetransact.uicomponent.CLabel lblBookSeries;
    private com.see.truetransact.uicomponent.CLabel lblBookTo;
    private com.see.truetransact.uicomponent.CLabel lblChequeFrom;
    private com.see.truetransact.uicomponent.CLabel lblChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblChequeTo;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentPrefix;
    private com.see.truetransact.uicomponent.CLabel lblItemID;
    private com.see.truetransact.uicomponent.CLabel lblItemSubType;
    private com.see.truetransact.uicomponent.CLabel lblItemSubTypeDesc;
    private com.see.truetransact.uicomponent.CLabel lblLeavesNO;
    private com.see.truetransact.uicomponent.CLabel lblLeavesNODesc;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProdTypeDesc;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTranID;
    private com.see.truetransact.uicomponent.CLabel lblTranIDDesc;
    private com.see.truetransact.uicomponent.CLabel lblTransDate;
    private com.see.truetransact.uicomponent.CLabel lblTransDateDesc;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBookSeries;
    private com.see.truetransact.uicomponent.CPanel panChequeNo;
    private com.see.truetransact.uicomponent.CPanel panInventoryDetails;
    private com.see.truetransact.uicomponent.CPanel panItemId;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CTextField txtBookFrom;
    private com.see.truetransact.uicomponent.CTextField txtBookQuantity;
    private com.see.truetransact.uicomponent.CTextField txtBookTo;
    private com.see.truetransact.uicomponent.CTextField txtChequeFrom;
    private com.see.truetransact.uicomponent.CTextField txtChequeTo;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentPrefix;
    private com.see.truetransact.uicomponent.CTextField txtItemID;
    // End of variables declaration//GEN-END:variables
    
}
