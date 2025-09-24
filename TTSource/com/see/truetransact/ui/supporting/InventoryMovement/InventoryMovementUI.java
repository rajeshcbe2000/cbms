/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InventoryMovementUI.java
 *
 * Created on January 24, 2005, 5:39 PM
 */

package com.see.truetransact.ui.supporting.InventoryMovement;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.ToDateValidation;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
/**
 *
 * @author  152721
 */
public class InventoryMovementUI extends CInternalFrame implements java.util.Observer, UIMandatoryField{
    HashMap mandatoryMap;
    InventoryMovementOB observable;
//    RemitStopPaymentRB resourceBundle = new RemitStopPaymentRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.supporting.InventoryMovement.InventoryMovementRB", ProxyParameters.LANGUAGE);
    
    final int EDIT=0,DELETE=1,AUTHORIZE=2, DDSTART=3, DDEND=4, VIEW=5;
    int viewType=-1;
    CInternalFrame parent;
    boolean print = false;
    
    /** Creates new form InventoryMovementUI */
    public InventoryMovementUI() {
        initComponents();
        initSetup();
        setPrinting(false);
    }
    
    /** Creates new form InventoryMovementUI */
    public InventoryMovementUI(CInternalFrame parent) {
        this.parent = parent;
        initComponents();
        initSetup();
       setPrinting(false);
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panRemitStop);
        setHelpMessage();
        setMaxLenths();
        
        ClientUtil.enableDisable(this, false);// Disables all when the screen appears for the 1st time
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        observable.resetForm();
        observable.resetStatus();// to reset the status
        setFieldsEnable(false);
        observable.ttNotifyObservers();
        panPaymentRevoke.setVisible(false);
    }
    
    private void setObservable() {
        observable = InventoryMovementOB.getInstance();
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
        btnPaymentRevoke.setName("btnPaymentRevoke");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboProdId.setName("cboProdId");
        cboReason.setName("cboReason");
        lblDDLeaf.setName("lblDDLeaf");
        lblEndNo.setName("lblEndNo");
        lblRemarks.setName("lblRemarks");
        lblMsg.setName("lblMsg");
        lblProdId.setName("lblProdId");
        lblReason.setName("lblReason");
        lblMissingId.setName("lblMissingId");
        lblMissingIdValue.setName("lblMissingIdValue");
        lblMissingDate.setName("lblMissingDate");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStartNo1.setName("lblStartNo");
        lblStatus1.setName("lblStatus1");
        mbrLoanProduct.setName("mbrLoanProduct");
        panDDLeaf.setName("panDDLeaf");
        panEndDDNo.setName("panEndDDNo");
        panPaymentRevoke.setName("panPaymentRevoke");
        panRemitStop.setName("panRemitStop");
        panRemitStopPayment.setName("panRemitStopPayment");
        panStartDDNo.setName("panStartDDNo");
        panStatus.setName("panStatus");
        rdoDDLeaf_Bulk.setName("rdoDDLeaf_Bulk");
        rdoDDLeaf_Single.setName("rdoDDLeaf_Single");
        txtEndNo1.setName("txtEndNo1");
        txtEndNo2.setName("txtEndNo2");
        txtReason.setName("txtReason");
        txtStartNo1.setName("txtStartNo1");
        txtStartNo2.setName("txtStartNo2");
        tdtDate.setName("tdtDate");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        rdoDDLeaf_Bulk.setText(resourceBundle.getString("rdoDDLeaf_Bulk"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblReason.setText(resourceBundle.getString("lblReason"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        rdoDDLeaf_Single.setText(resourceBundle.getString("rdoDDLeaf_Single"));
        btnPaymentRevoke.setText(resourceBundle.getString("btnPaymentRevoke"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblStartNo1.setText(resourceBundle.getString("lblStartNo1"));
        lblDDLeaf.setText(resourceBundle.getString("lblDDLeaf"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        ((javax.swing.border.TitledBorder)panRemitStop.getBorder()).setTitle(resourceBundle.getString("panRemitStop"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblEndNo.setText(resourceBundle.getString("lblEndNo"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblMissingId.setText(resourceBundle.getString("lblMissingId"));
        lblMissingIdValue.setText(resourceBundle.getString("lblMissingIdValue"));
        lblMissingDate.setText(resourceBundle.getString("lblMissingDate"));
    }
    
      
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        
        
        cboProdId.setSelectedItem(observable.getCboProdId());
        rdoDDLeaf_Single.setSelected(observable.getRdoDDLeaf_Single());
        rdoDDLeaf_Bulk.setSelected(observable.getRdoDDLeaf_Bulk());
        rdoDDLeaf_SingleActionPerformed(null);
        
        txtStartNo1.setText(observable.getTxtStartNo1());
        txtStartNo2.setText(observable.getTxtStartNo2());
        txtEndNo1.setText(observable.getTxtEndNo1());
        txtEndNo2.setText(observable.getTxtEndNo2());
        txtReason.setText(observable.getRemarks());
        cboReason.setSelectedItem(observable.getCboReason());
        //__ Lables...
        lblMissingIdValue.setText(observable.getLblMissingIdValue());
        tdtDate.setDateValue(observable.getTdtDate());
        
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
        
      
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setRdoDDLeaf_Single(rdoDDLeaf_Single.isSelected());
        observable.setRdoDDLeaf_Bulk(rdoDDLeaf_Bulk.isSelected());
        observable.setTxtStartNo1(txtStartNo1.getText());
        observable.setTxtStartNo2(txtStartNo2.getText());
        observable.setTxtEndNo1(txtEndNo1.getText());
        observable.setTxtEndNo2(txtEndNo2.getText());
        observable.setTxtReason((String)cboReason.getSelectedItem());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setTdtDate(tdtDate.getDateValue());
        observable.setRemarks(txtReason.getText());
        observable.setStatusDt(CommonUtil.convertObjToStr(ClientUtil.getCurrentDate()));
     }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("rdoDDLeaf_Single", new Boolean(true));
        mandatoryMap.put("txtStartNo1", new Boolean(true));
        mandatoryMap.put("txtStartNo2", new Boolean(true));
        mandatoryMap.put("txtEndNo1", new Boolean(true));
        mandatoryMap.put("txtEndNo2", new Boolean(true));
        mandatoryMap.put("txtReason", new Boolean(true));
        mandatoryMap.put("cboReason", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        cboProdId.setModel(observable.getCbmProdId());
        cboReason.setModel(observable.getCbmReason());
    }
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        InventoryMovementMRB objMandatoryRB = new InventoryMovementMRB();
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        cboReason.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReason"));
        rdoDDLeaf_Single.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDDLeaf_Single"));
        rdoDDLeaf_Bulk.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDDLeaf_Bulk"));
        txtStartNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartNo1"));
        txtStartNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartNo2"));
        txtEndNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndNo1"));
        txtEndNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndNo2"));
        txtReason.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReason"));
        tdtDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDate"));
    }
   
    private void setMaxLenths() {
        txtStartNo1.setMaxLength(16);
        txtStartNo2.setMaxLength(16);
        txtStartNo2.setValidation(new NumericValidation());
        
        txtEndNo1.setMaxLength(16);
        txtEndNo2.setMaxLength(16);
        txtEndNo2.setValidation(new NumericValidation());
        
        
        txtReason.setMaxLength(256);
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

        rdoDDLeaf = new com.see.truetransact.uicomponent.CButtonGroup();
        panRemitStopPayment = new com.see.truetransact.uicomponent.CPanel();
        panRemitStop = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblDDLeaf = new com.see.truetransact.uicomponent.CLabel();
        panDDLeaf = new com.see.truetransact.uicomponent.CPanel();
        rdoDDLeaf_Single = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDDLeaf_Bulk = new com.see.truetransact.uicomponent.CRadioButton();
        lblStartNo1 = new com.see.truetransact.uicomponent.CLabel();
        panStartDDNo = new com.see.truetransact.uicomponent.CPanel();
        txtStartNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtStartNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblEndNo = new com.see.truetransact.uicomponent.CLabel();
        panEndDDNo = new com.see.truetransact.uicomponent.CPanel();
        txtEndNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtEndNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblReason = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtReason = new com.see.truetransact.uicomponent.CTextField();
        lblMissingId = new com.see.truetransact.uicomponent.CLabel();
        lblMissingIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblMissingDate = new com.see.truetransact.uicomponent.CLabel();
        cboReason = new com.see.truetransact.uicomponent.CComboBox();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        panPaymentRevoke = new com.see.truetransact.uicomponent.CPanel();
        btnPaymentRevoke = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(650, 500));
        setPreferredSize(new java.awt.Dimension(650, 500));

        panRemitStopPayment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRemitStopPayment.setLayout(new java.awt.GridBagLayout());

        panRemitStop.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRemitStop.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(120);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(cboProdId, gridBagConstraints);

        lblDDLeaf.setText("Leaf");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblDDLeaf, gridBagConstraints);

        panDDLeaf.setLayout(new java.awt.GridBagLayout());

        rdoDDLeaf.add(rdoDDLeaf_Single);
        rdoDDLeaf_Single.setText("Single");
        rdoDDLeaf_Single.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDDLeaf_SingleActionPerformed(evt);
            }
        });
        panDDLeaf.add(rdoDDLeaf_Single, new java.awt.GridBagConstraints());

        rdoDDLeaf.add(rdoDDLeaf_Bulk);
        rdoDDLeaf_Bulk.setText("Bulk");
        rdoDDLeaf_Bulk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDDLeaf_BulkActionPerformed(evt);
            }
        });
        panDDLeaf.add(rdoDDLeaf_Bulk, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(panDDLeaf, gridBagConstraints);

        lblStartNo1.setText("Starting No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblStartNo1, gridBagConstraints);

        panStartDDNo.setLayout(new java.awt.GridBagLayout());

        txtStartNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStartNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panStartDDNo.add(txtStartNo1, gridBagConstraints);

        txtStartNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panStartDDNo.add(txtStartNo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(panStartDDNo, gridBagConstraints);

        lblEndNo.setText("Ending  No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblEndNo, gridBagConstraints);

        panEndDDNo.setLayout(new java.awt.GridBagLayout());

        txtEndNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtEndNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panEndDDNo.add(txtEndNo1, gridBagConstraints);

        txtEndNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panEndDDNo.add(txtEndNo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(panEndDDNo, gridBagConstraints);

        lblReason.setText("Reason");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblReason, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblRemarks, gridBagConstraints);

        txtReason.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(txtReason, gridBagConstraints);

        lblMissingId.setText("Missing Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblMissingId, gridBagConstraints);

        lblMissingIdValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblMissingIdValue, gridBagConstraints);

        lblMissingDate.setText("Missing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblMissingDate, gridBagConstraints);

        cboReason.setMinimumSize(new java.awt.Dimension(100, 21));
        cboReason.setPopupWidth(120);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(cboReason, gridBagConstraints);

        tdtDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDate.setPreferredSize(new java.awt.Dimension(21, 200));
        tdtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tdtDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(tdtDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStopPayment.add(panRemitStop, gridBagConstraints);

        panPaymentRevoke.setLayout(new java.awt.GridBagLayout());

        btnPaymentRevoke.setText("Payment Revoke");
        btnPaymentRevoke.setToolTipText("Payment Revoke");
        btnPaymentRevoke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentRevokeActionPerformed(evt);
            }
        });
        panPaymentRevoke.add(btnPaymentRevoke, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStopPayment.add(panPaymentRevoke, gridBagConstraints);

        getContentPane().add(panRemitStopPayment, java.awt.BorderLayout.CENTER);

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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace31);

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

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace33);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace34);

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

    private void tdtDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtDateFocusGained

    private void tdtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusLost
   ClientUtil.validateLTDate(tdtDate);
    }//GEN-LAST:event_tdtDateFocusLost
    
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
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectInventoryMovementAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeInventoryMovement");
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
             observable.setStatus();  
            //__ If there's no data to be Authorized, call Cancel action...  
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
            
        } else if (viewType == AUTHORIZE){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("id",lblMissingIdValue.getText());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            
            ClientUtil.execute("authorizeInventoryMovement", singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(lblMissingIdValue.getText());
            viewType = -1;
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
            observable.setResult(observable.getActionType());
            observable.setResultStatus();  
        }
    }
    private void btnPaymentRevokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentRevokeActionPerformed
        // TODO add your handling code here:
//        observable.paymentRevoke();
        
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, false);// Disables the panel...
        observable.resetForm();
        observable.setResultStatus();// To Reset the Value of lblStatus...
    }//GEN-LAST:event_btnPaymentRevokeActionPerformed
                    
    private void rdoDDLeaf_BulkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDDLeaf_BulkActionPerformed
        // TODO add your handling code here:
          txtEndNo1.setEnabled(true);
          txtEndNo2.setEnabled(true);
          txtEndNo1.setEditable(true);
          txtEndNo2.setEditable(true);
          txtStartNo1.setEnabled(true);
          txtStartNo2.setEnabled(true);
          txtStartNo1.setEditable(true);
          txtStartNo2.setEditable(true);
       
    }//GEN-LAST:event_rdoDDLeaf_BulkActionPerformed
    
    private void rdoDDLeaf_SingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDDLeaf_SingleActionPerformed
        // TODO add your handling code here:
        if(rdoDDLeaf_Single.isSelected()) {
           txtStartNo1.setEnabled(true);
           txtStartNo2.setEnabled(true);
           txtStartNo1.setEditable(true);
           txtStartNo2.setEditable(true);
           txtEndNo1.setText("");
           txtEndNo1.setEnabled(false);
           txtEndNo2.setText("");
           txtEndNo2.setEnabled(false);
           
        }
    }//GEN-LAST:event_rdoDDLeaf_SingleActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        rdoDDLeaf_BulkActionPerformed(null);
//        observable.resetForm();                 //__ Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        if(observable.getAuthStatus()!=null)
            super.removeEditLock(lblMissingIdValue.getText());
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
            observable.getActionType()!=ClientConstants.ACTIONTYPE_REJECT)
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
         observable.resetForm();
         observable.setStatus();
        viewType = -1;
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
         updateOBFields();
         if(rdoDDLeaf_Single.isSelected())
         {
           rdoDDLeaf_SingleActionPerformed(null);   
         }
        String mandatoryMessage = "";
        StringBuffer str  =  new StringBuffer();
            //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
            mandatoryMessage =  new MandatoryCheck().checkMandatory(getClass().getName(), panRemitStop);
            if(mandatoryMessage.length() > 0){
                str.append(mandatoryMessage + "\n");
            }
            if(observable.getActionType()!= ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage.toString());
        }
            else{
            observable.doAction();// To perform the necessary operation depending on the Action type...
            
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("MISSINGID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("MISSINGID")) {
                        lockMap.put("MISSINGID", observable.getProxyReturnMap().get("MISSINGID"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("MISSINGID",lblMissingIdValue.getText());
                }
                setEditLockMap(lockMap);
                setEditLock();
                rdoDDLeaf_BulkActionPerformed(null);
                observable.resetForm();                    //__ Reset the fields in the UI to null...
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                setButtonEnableDisable();                  //__ Enables or Disables the buttons and menu Items depending on their previous state...
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
                observable.setResultStatus();              //__ To Reset the Value of lblStatus...
                setPrinting(true);
                //__ Make the Screen Closable..
                setModified(false);
            
        }
            }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
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
     
    public boolean setPrinting(boolean t){
           return t;
    }
    
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field== AUTHORIZE || field== VIEW){ //Edit=0 and Delete=1
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            ArrayList lst = new ArrayList();
            lst.add("MISSINGID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            lst = null;
              setButtonEnableDisable();
            viewMap.put(CommonConstants.MAP_NAME, "viewMissingInstruments");
            new ViewAll(this, viewMap).show();
            
        }
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType== VIEW) {
            hash.put("WHERE", hash.get("MISSINGID"));
            observable.populateData(hash);// Called to display the Data in the UI fields...
            
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE ) {
                ClientUtil.enableDisable(this, false);     // Disables the panel...
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(this, true);   
           }
                
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
   
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
      public boolean missingInstrument(HashMap where){
        if(where!=null) {
            int a = (CommonUtil.convertObjToInt(where.get("INSTRUMENT_START_NO2")));
            int b = (CommonUtil.convertObjToInt(where.get("INSTRUMENT_END_NO2")));
            btnNewActionPerformed(null);
        cboProdId.setEnabled(true);
        cboProdId.setEditable(true);
//        rdoDDLeaf_Single.setSelected(observable.getRdoDDLeaf_Single());
        rdoDDLeaf_Bulk.setSelected(true);
         buttons();
        
        txtStartNo1.setText(CommonUtil.convertObjToStr(where.get("INSTRUMENT_START_NO1")));
        txtStartNo2.setText(CommonUtil.convertObjToStr(where.get("INSTRUMENT_START_NO2")));
        txtEndNo1.setText(CommonUtil.convertObjToStr(where.get("INSTRUMENT_END_NO1")));
        txtEndNo2.setText(CommonUtil.convertObjToStr(where.get("INSTRUMENT_END_NO2")));
        txtReason.setEnabled(true);
        txtReason.setEditable(true);
        tdtDate.setEnabled(true);
        cboReason.setEnabled(true);
        cboReason.setEditable(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false); 
        txtReason.setEnabled(true);
        btnCancel.setEnabled(true);
        if(a==b)
        {
            txtEndNo1.setText("") ;
            txtEndNo2.setText("") ;
            rdoDDLeaf_Single.setSelected(true);
        }
         return true;
      }
        else{
            return false;
        }
      }
      private void buttons()
      {
          txtEndNo1.setEnabled(false);
          txtEndNo2.setEnabled(false);
          txtEndNo1.setEditable(false);
          txtEndNo2.setEditable(false);
          txtStartNo1.setEnabled(false);
          txtStartNo2.setEnabled(false);
          txtStartNo1.setEditable(false);
          txtStartNo2.setEditable(false);
      }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();               // to Reset all the Fields and Status in UI...
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        
        ClientUtil.enableDisable(this, true); // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        observable.setStatus();               // To set the Value of lblStatus...
        setFieldsEnable(false);
        btnPaymentRevoke.setEnabled(false);
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setFieldsEnable(boolean value){
        txtStartNo1.setEnabled(value);
        txtStartNo2.setEnabled(value);
        txtEndNo1.setEnabled(value);
        txtEndNo2.setEnabled(value);
        }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new InventoryMovementUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPaymentRevoke;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboReason;
    private com.see.truetransact.uicomponent.CLabel lblDDLeaf;
    private com.see.truetransact.uicomponent.CLabel lblEndNo;
    private com.see.truetransact.uicomponent.CLabel lblMissingDate;
    private com.see.truetransact.uicomponent.CLabel lblMissingId;
    private com.see.truetransact.uicomponent.CLabel lblMissingIdValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblReason;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStartNo1;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDDLeaf;
    private com.see.truetransact.uicomponent.CPanel panEndDDNo;
    private com.see.truetransact.uicomponent.CPanel panPaymentRevoke;
    private com.see.truetransact.uicomponent.CPanel panRemitStop;
    private com.see.truetransact.uicomponent.CPanel panRemitStopPayment;
    private com.see.truetransact.uicomponent.CPanel panStartDDNo;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDDLeaf;
    private com.see.truetransact.uicomponent.CRadioButton rdoDDLeaf_Bulk;
    private com.see.truetransact.uicomponent.CRadioButton rdoDDLeaf_Single;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextField txtEndNo1;
    private com.see.truetransact.uicomponent.CTextField txtEndNo2;
    private com.see.truetransact.uicomponent.CTextField txtReason;
    private com.see.truetransact.uicomponent.CTextField txtStartNo1;
    private com.see.truetransact.uicomponent.CTextField txtStartNo2;
    // End of variables declaration//GEN-END:variables
    
}
