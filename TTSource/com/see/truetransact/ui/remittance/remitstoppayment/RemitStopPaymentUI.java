/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RemitStopPaymentUI.java
 *
 * Created on January 24, 2005, 5:39 PM
 */

package com.see.truetransact.ui.remittance.remitstoppayment;

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
import com.see.truetransact.uicomponent.COptionPane;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
/**
 *
 * @author  152721
 */
public class RemitStopPaymentUI extends CInternalFrame implements java.util.Observer ,UIMandatoryField{
    HashMap mandatoryMap;
    RemitStopPaymentOB observable;
//    RemitStopPaymentRB resourceBundle = new RemitStopPaymentRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.remittance.remitstoppayment.RemitStopPaymentRB", ProxyParameters.LANGUAGE);
    
    final int EDIT=0,DELETE=1,AUTHORIZE=2, DDSTART=3, DDEND=4, VIEW = 8 ;
    int viewType=-1;
    private boolean isRevoked = false;
//    String remark = "";
    private Date currDt = null;
    /** Creates new form RemitStopPayment */
    public RemitStopPaymentUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panRemitStopPayment);
        setHelpMessage();
        setMaxLenths();
        
        ClientUtil.enableDisable(this, false);// Disables all when the screen appears for the 1st time
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        observable.resetForm();
        observable.resetStatus();// to reset the status
        setButtonEnable(false);
        setFieldsEnable(false);
        txtRevokeReason.setVisible(false);
        lblRevokeReason.setVisible(false);
        lblRevokeDateVal.setVisible(false);
        lblDDRevokeDate.setVisible(false);
        observable.ttNotifyObservers();
    }
    
    private void setObservable() {
        observable = RemitStopPaymentOB.getInstance();
        observable.addObserver(this);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnDDEndNo.setName("btnDDEndNo");
        btnDDStartNo.setName("btnDDStartNo");
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
        lblAmount.setName("lblAmount");
        lblAmountValue.setName("lblAmountValue");
        lblDDDate.setName("lblDDDate");
        lblDDDateValue.setName("lblDDDateValue");
        lblDDLeaf.setName("lblDDLeaf");
        lblEndDDNo.setName("lblEndDDNo");
        lblEndVariableNo.setName("lblEndVariableNo");
        lblMsg.setName("lblMsg");
        lblPayeeName.setName("lblPayeeName");
        lblPayeeNameValue.setName("lblPayeeNameValue");
        lblProdId.setName("lblProdId");
        lblReason.setName("lblReason");
        lblRevokeReason.setName("lblRevokeReason");
        lblStopId.setName("lblStopId");
        lblStopIdValue.setName("lblStopIdValue");
        lblDDStopDate.setName("lblDDStopDate");
        lblDDStopDateValue.setName("lblDDStopDateValue");
        lblDDRevokeDate.setName("lblDDRevokeDate");
        lblRevokeDateVal.setName("lblRevokeDateVal");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStartDDNo1.setName("lblStartDDNo");
        lblStartVariableNo.setName("lblStartVariableNo");
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
        txtEndDDNo1.setName("txtEndDDNo1");
        txtEndDDNo2.setName("txtEndDDNo2");
        txtEndVariableNo.setName("txtEndVariableNo");
        txtReason.setName("txtReason");
        txtRevokeReason.setName("txtRevokeReason");
        txtStartDDNo1.setName("txtStartDDNo1");
        txtStartDDNo2.setName("txtStartDDNo2");
        txtStartVariableNo.setName("txtStartVariableNo");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        rdoDDLeaf_Bulk.setText(resourceBundle.getString("rdoDDLeaf_Bulk"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblStartVariableNo.setText(resourceBundle.getString("lblStartVariableNo"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblDDDateValue.setText(resourceBundle.getString("lblDDDateValue"));
        lblPayeeName.setText(resourceBundle.getString("lblPayeeName"));
        lblReason.setText(resourceBundle.getString("lblReason"));
        lblRevokeReason.setText(resourceBundle.getString("lblRevokeReason"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblEndVariableNo.setText(resourceBundle.getString("lblEndVariableNo"));
        lblPayeeNameValue.setText(resourceBundle.getString("lblPayeeNameValue"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblDDDate.setText(resourceBundle.getString("lblDDDate"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        rdoDDLeaf_Single.setText(resourceBundle.getString("rdoDDLeaf_Single"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        btnPaymentRevoke.setText(resourceBundle.getString("btnPaymentRevoke"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblStartDDNo1.setText(resourceBundle.getString("lblStartDDNo1"));
        lblDDLeaf.setText(resourceBundle.getString("lblDDLeaf"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        ((javax.swing.border.TitledBorder)panRemitStop.getBorder()).setTitle(resourceBundle.getString("panRemitStop"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblEndDDNo.setText(resourceBundle.getString("lblEndDDNo"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblAmountValue.setText(resourceBundle.getString("lblAmountValue"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblStopId.setText(resourceBundle.getString("lblStopId"));
        lblStopIdValue.setText(resourceBundle.getString("lblStopIdValue"));
        lblDDStopDate.setText(resourceBundle.getString("lblDDStopDate"));
        lblDDStopDateValue.setText(resourceBundle.getString("lblDDStopDateValue"));
        lblDDRevokeDate.setText(resourceBundle.getString("lblDDRevokeDate"));
        lblRevokeDateVal.setText(resourceBundle.getString("lblRevokeDateVal"));
        btnDDStartNo.setText(resourceBundle.getString("btnDDStartNo"));
        btnDDEndNo.setText(resourceBundle.getString("btnDDEndNo"));
    }
    
    private void removeRadioButtons() {
        rdoDDLeaf.remove(rdoDDLeaf_Single);
        rdoDDLeaf.remove(rdoDDLeaf_Bulk);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        
        rdoDDLeaf = new CButtonGroup();
        rdoDDLeaf.add(rdoDDLeaf_Single);
        rdoDDLeaf.add(rdoDDLeaf_Bulk);
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        
        cboProdId.setSelectedItem(observable.getCboProdId());
        rdoDDLeaf_Single.setSelected(observable.getRdoDDLeaf_Single());
        rdoDDLeaf_Bulk.setSelected(observable.getRdoDDLeaf_Bulk());
        rdoDDLeaf_SingleActionPerformed(null);
        
        txtStartDDNo1.setText(observable.getTxtStartDDNo1());
        txtStartDDNo2.setText(observable.getTxtStartDDNo2());
        txtEndDDNo1.setText(observable.getTxtEndDDNo1());
        txtEndDDNo2.setText(observable.getTxtEndDDNo2());
        txtStartVariableNo.setText(observable.getTxtStartVariableNo());
        txtEndVariableNo.setText(observable.getTxtEndVariableNo());
        txtReason.setText(observable.getTxtReason());
        txtRevokeReason.setText(observable.getTxtRevokeRemark());
        //__ Lables...
        lblStopIdValue.setText(observable.getLblStopIdValue());
        lblDDDateValue.setText(observable.getLblDDDateValue());
        lblPayeeNameValue.setText(observable.getLblPayeeNameValue());
        lblAmountValue.setText(observable.getLblAmountValue());
        lblDDStopDateValue.setText(observable.getLblDDStopDateValue());
        lblRevokeDateVal.setText(observable.getLblRevokeDateVal());
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
        
        addRadioButtons();
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setRdoDDLeaf_Single(rdoDDLeaf_Single.isSelected());
        observable.setRdoDDLeaf_Bulk(rdoDDLeaf_Bulk.isSelected());
        observable.setTxtStartDDNo1(txtStartDDNo1.getText());
        observable.setTxtStartDDNo2(txtStartDDNo2.getText());
        observable.setTxtEndDDNo1(txtEndDDNo1.getText());
        observable.setTxtEndDDNo2(txtEndDDNo2.getText());
        observable.setTxtStartVariableNo(txtStartVariableNo.getText());
        observable.setTxtEndVariableNo(txtEndVariableNo.getText());
        observable.setTxtReason(txtReason.getText());
        observable.setTxtRevokeRemark(txtRevokeReason.getText());
        observable.setSelectedBranchID(getSelectedBranchID());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("rdoDDLeaf_Single", new Boolean(true));
        mandatoryMap.put("txtStartDDNo1", new Boolean(true));
        mandatoryMap.put("txtStartDDNo2", new Boolean(true));
        mandatoryMap.put("txtEndDDNo1", new Boolean(true));
        mandatoryMap.put("txtEndDDNo2", new Boolean(true));
        mandatoryMap.put("txtStartVariableNo", new Boolean(true));
        mandatoryMap.put("txtEndVariableNo", new Boolean(true));
        mandatoryMap.put("txtReason", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        cboProdId.setModel(observable.getCbmProdId());
    }
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        RemitStopPaymentMRB objMandatoryRB = new RemitStopPaymentMRB();
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        rdoDDLeaf_Single.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDDLeaf_Single"));
        rdoDDLeaf_Bulk.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDDLeaf_Bulk"));
        txtStartDDNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartDDNo1"));
        txtStartDDNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartDDNo2"));
        txtEndDDNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndDDNo1"));
        txtEndDDNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndDDNo2"));
        txtStartVariableNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartVariableNo"));
        txtEndVariableNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndVariableNo"));
        txtReason.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReason"));
        txtRevokeReason.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRevokeReason")); 
    }
    
    private void setMaxLenths() {
        txtStartDDNo1.setMaxLength(16);
        txtStartDDNo2.setMaxLength(16);
        txtStartDDNo2.setValidation(new NumericValidation());
        
        txtEndDDNo1.setMaxLength(16);
        txtEndDDNo2.setMaxLength(16);
        txtEndDDNo2.setValidation(new NumericValidation());
        
        txtStartVariableNo.setMaxLength(32);
        txtEndVariableNo.setMaxLength(32);
        txtReason.setMaxLength(256);
        txtRevokeReason.setMaxLength(256);
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnSave.isEnabled());
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
        lblStartDDNo1 = new com.see.truetransact.uicomponent.CLabel();
        panStartDDNo = new com.see.truetransact.uicomponent.CPanel();
        txtStartDDNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtStartDDNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblEndDDNo = new com.see.truetransact.uicomponent.CLabel();
        panEndDDNo = new com.see.truetransact.uicomponent.CPanel();
        txtEndDDNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtEndDDNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblStartVariableNo = new com.see.truetransact.uicomponent.CLabel();
        txtStartVariableNo = new com.see.truetransact.uicomponent.CTextField();
        lblEndVariableNo = new com.see.truetransact.uicomponent.CLabel();
        txtEndVariableNo = new com.see.truetransact.uicomponent.CTextField();
        lblDDDate = new com.see.truetransact.uicomponent.CLabel();
        lblDDDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblPayeeName = new com.see.truetransact.uicomponent.CLabel();
        lblPayeeNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblReason = new com.see.truetransact.uicomponent.CLabel();
        txtReason = new com.see.truetransact.uicomponent.CTextField();
        lblStopId = new com.see.truetransact.uicomponent.CLabel();
        lblStopIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblDDStopDate = new com.see.truetransact.uicomponent.CLabel();
        lblDDStopDateValue = new com.see.truetransact.uicomponent.CLabel();
        btnDDStartNo = new com.see.truetransact.uicomponent.CButton();
        btnDDEndNo = new com.see.truetransact.uicomponent.CButton();
        txtRevokeReason = new com.see.truetransact.uicomponent.CTextField();
        lblRevokeReason = new com.see.truetransact.uicomponent.CLabel();
        lblRevokeDateVal = new com.see.truetransact.uicomponent.CLabel();
        lblDDRevokeDate = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(550, 600));
        setPreferredSize(new java.awt.Dimension(550, 600));

        panRemitStopPayment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRemitStopPayment.setMinimumSize(new java.awt.Dimension(379, 550));
        panRemitStopPayment.setPreferredSize(new java.awt.Dimension(379, 550));
        panRemitStopPayment.setLayout(new java.awt.GridBagLayout());

        panRemitStop.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRemitStop.setMinimumSize(new java.awt.Dimension(367, 400));
        panRemitStop.setPreferredSize(new java.awt.Dimension(367, 400));
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

        lblDDLeaf.setText("DD Leaf");
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

        lblStartDDNo1.setText("Starting DD No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblStartDDNo1, gridBagConstraints);

        panStartDDNo.setLayout(new java.awt.GridBagLayout());

        txtStartDDNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStartDDNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panStartDDNo.add(txtStartDDNo1, gridBagConstraints);

        txtStartDDNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panStartDDNo.add(txtStartDDNo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(panStartDDNo, gridBagConstraints);

        lblEndDDNo.setText("Ending DD No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblEndDDNo, gridBagConstraints);

        panEndDDNo.setLayout(new java.awt.GridBagLayout());

        txtEndDDNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtEndDDNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panEndDDNo.add(txtEndDDNo1, gridBagConstraints);

        txtEndDDNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panEndDDNo.add(txtEndDDNo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(panEndDDNo, gridBagConstraints);

        lblStartVariableNo.setText("Starting Variable No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblStartVariableNo, gridBagConstraints);

        txtStartVariableNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(txtStartVariableNo, gridBagConstraints);

        lblEndVariableNo.setText("Ending Variable No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblEndVariableNo, gridBagConstraints);

        txtEndVariableNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(txtEndVariableNo, gridBagConstraints);

        lblDDDate.setText("DD Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblDDDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblDDDateValue, gridBagConstraints);

        lblAmount.setText("DD Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblAmountValue, gridBagConstraints);

        lblPayeeName.setText("Payee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblPayeeName, gridBagConstraints);

        lblPayeeNameValue.setMinimumSize(new java.awt.Dimension(150, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblPayeeNameValue, gridBagConstraints);

        lblReason.setText("Reason To Stop Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblReason, gridBagConstraints);

        txtReason.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(txtReason, gridBagConstraints);

        lblStopId.setText("DD Stop Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblStopId, gridBagConstraints);

        lblStopIdValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblStopIdValue, gridBagConstraints);

        lblDDStopDate.setText("DD Stop Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblDDStopDate, gridBagConstraints);

        lblDDStopDateValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblDDStopDateValue, gridBagConstraints);

        btnDDStartNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDDStartNo.setToolTipText("Starting DD No");
        btnDDStartNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDDStartNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDDStartNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDDStartNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRemitStop.add(btnDDStartNo, gridBagConstraints);

        btnDDEndNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDDEndNo.setToolTipText("Ending DD No");
        btnDDEndNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDDEndNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDDEndNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDDEndNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRemitStop.add(btnDDEndNo, gridBagConstraints);

        txtRevokeReason.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(txtRevokeReason, gridBagConstraints);

        lblRevokeReason.setText("Reason To Revoke Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblRevokeReason, gridBagConstraints);

        lblRevokeDateVal.setText("DD Revoke Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblRevokeDateVal, gridBagConstraints);

        lblDDRevokeDate.setText("DD Revoke Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitStop.add(lblDDRevokeDate, gridBagConstraints);

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

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace27);

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

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace28);

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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace31);

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
            String authStatus = observable.getAuthStatus();
            String stopstatus = observable.getStopStatus();
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
            mapParam.put(CommonConstants.MAP_NAME, "getDDStopPaymentAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDDStopPayment");
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...  
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
            
        } else if (viewType == AUTHORIZE){
            if((authStatus.equals("")) && (stopstatus.equals("REVOKED")) && (authorizeStatus.equals(CommonConstants.STATUS_REJECTED))){
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STOP_STATUS", "STOPPED");
                singleAuthorizeMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("STOPPAYMENTID",lblStopIdValue.getText());
//                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                ClientUtil.execute("rejectDDStopPayment", singleAuthorizeMap);
                singleAuthorizeMap = null;
                
            }
            else{
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("STOP PAYMENT ID",lblStopIdValue.getText());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                ClientUtil.execute("authorizeDDStopPayment", singleAuthorizeMap);
//                super.setOpenForEditBy(observable.getStatusBy());
//                super.removeEditLock(lblStopIdValue.getText());
//                viewType = -1;
//                btnSave.setEnabled(true);
//                btnCancelActionPerformed(null);
//                observable.setResult(observable.getActionType());
//                observable.setResultStatus();  
            }
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(lblStopIdValue.getText());
                viewType = -1;
                btnSave.setEnabled(true);
                btnCancelActionPerformed(null);
                observable.setResult(observable.getActionType());
                observable.setResultStatus();  
        }
    }
    private void btnPaymentRevokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentRevokeActionPerformed
        // TODO add your handling code here:
        /*observable.paymentRevoke();*/   //commented for testing
        ClientUtil.enableDisable(this, true);
        setEnableDisable(false);
        isRevoked = true;
        btnPaymentRevoke.setEnabled(true);
        txtRevokeReason.setVisible(true);
        lblRevokeReason.setVisible(true);
        txtRevokeReason.setEditable(true);
        txtRevokeReason.setEnabled(true);
        lblDDRevokeDate.setVisible(true);
        lblRevokeDateVal.setVisible(true);
        Date curDt = (Date) currDt.clone();
        lblRevokeDateVal.setText(CommonUtil.convertObjToStr(curDt));
        btnPaymentRevoke.setEnabled(false);
        
        
//        txtRevokeReason.setBackground(null);
//        remark = COptionPane.showInputDialog(this,resourceBundle.getString("REVOKE_REMARKS"));
//        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
//        ClientUtil.enableDisable(this, false);// Disables the panel...
//        observable.resetForm();
//        observable.setResultStatus();// To Reset the Value of lblStatus...
    }//GEN-LAST:event_btnPaymentRevokeActionPerformed
    
    private void btnDDEndNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDDEndNoActionPerformed
        // TODO add your handling code here:
        popUp(DDEND);
    }//GEN-LAST:event_btnDDEndNoActionPerformed
    
    private void btnDDStartNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDDStartNoActionPerformed
        // TODO add your handling code here:
        popUp(DDSTART);
    }//GEN-LAST:event_btnDDStartNoActionPerformed
            
    private void rdoDDLeaf_BulkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDDLeaf_BulkActionPerformed
        // TODO add your handling code here:
        txtEndDDNo1.setEditable(true);
        txtEndDDNo1.setEnabled(false);
        
        txtEndDDNo2.setEditable(true);
        txtEndDDNo2.setEnabled(false);
        
        txtEndVariableNo.setEditable(true);
        txtEndVariableNo.setEnabled(false);
        
        btnDDEndNo.setEnabled(true);
    }//GEN-LAST:event_rdoDDLeaf_BulkActionPerformed
    
    private void rdoDDLeaf_SingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDDLeaf_SingleActionPerformed
        // TODO add your handling code here:
        if(rdoDDLeaf_Single.isSelected()) {
            txtEndDDNo1.setText("");
            txtEndDDNo1.setEditable(false);
            txtEndDDNo1.setEnabled(false);
            
            
            txtEndDDNo2.setText("");
            txtEndDDNo2.setEditable(false);
            txtEndDDNo2.setEnabled(false);
            
            txtEndVariableNo.setText("");
            txtEndVariableNo.setEditable(false);
            txtEndVariableNo.setEnabled(false);
            
            btnDDEndNo.setEnabled(false);
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
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(lblStopIdValue.getText());
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
            observable.getActionType()!=ClientConstants.ACTIONTYPE_REJECT)
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..
        setButtonEnable(false);
        
        viewType = -1;
        isRevoked = false;
        txtRevokeReason.setVisible(false);
        lblRevokeReason.setVisible(false);
        lblRevokeDateVal.setVisible(false);
        lblDDRevokeDate.setVisible(false);
//        remark = "";
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panRemitStopPayment);
        System.out.println("mandatoryMessage: " + mandatoryMessage);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            if(isRevoked){
                if(txtRevokeReason.getText().equals("")){
                     displayAlert("Enter the reason to revoke");
                }else{
                    observable.paymentRevoke(observable.getTxtRevokeRemark(), (Date) currDt.clone());
                    setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
                    ClientUtil.enableDisable(this, false);// Disables the panel...
                    observable.resetForm();
                    observable.setResultStatus();// To Reset the Value of lblStatus...
                }
            }else{
                observable.doAction();// To perform the necessary operation depending on the Action type...
            }
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                if(isRevoked){
                    //do nothing
                }
                else{
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("STOP PAYMENT ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("STOP PAYMENT ID")) {
                        lockMap.put("STOP PAYMENT ID", observable.getProxyReturnMap().get("STOP PAYMENT ID"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("STOP PAYMENT ID", observable.getLblStopIdValue());
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
                setButtonEnable(false);
                
                //__ Make the Screen Closable..
                setModified(false);
                }
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
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field== AUTHORIZE || field== VIEW){ //Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("STOP PAYMENT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewStopDDPayment");
            new ViewAll(this, viewMap).show();
            
        }else if(field==DDSTART){
            HashMap whereMap = new HashMap();
            final String PRODID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            whereMap.put("PRODID", PRODID);
            whereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getDDStopData");
            new ViewAll(this, viewMap, true).show();
            
        }else if(field==DDEND){
            if( !txtStartDDNo1.getText().equalsIgnoreCase("") && !txtStartDDNo2.getText().equalsIgnoreCase("") ){
                HashMap whereMap = new HashMap();
                final String PRODID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                whereMap.put("PRODID", PRODID);
                whereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                whereMap.put("INSTRUMENTNO1", txtStartDDNo1.getText());
                whereMap.put("INSTRUMENTNO2", txtStartDDNo2.getText());
                System.out.println("whereMap: "  + whereMap);
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                viewMap.put(CommonConstants.MAP_NAME, "getDDStopData");
                
            }else{
                displayAlert(resourceBundle.getString("END_DD_WARNING"));
            }
            
           new ViewAll(this, viewMap, true).show();
        }
        
//        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {

        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW ) {
            hash.put("WHERE", hash.get("STOP PAYMENT ID"));
            observable.populateData(hash);// Called to display the Data in the UI fields...
            
            final String AUTHSTATUS = observable.getAuthStatus();
            String stopstatus = observable.getStopStatus();
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE ||viewType==VIEW) {
                ClientUtil.enableDisable(this, false);     // Disables the panel...
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                if(AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
//                    ClientUtil.enableDisable(this, false);     // Enables the panel...
//                    ClientUtil.enableDisable(panRemitStop,false);
//                    setEnableDisable(false);
                    btnPaymentRevoke.setEnabled(true);
//                    txtRevokeReason.setVisible(true);
//                    lblRevokeReason.setVisible(true);
                }
                else if(AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_REJECTED) && stopstatus.equalsIgnoreCase("REVOKED")){
                    ClientUtil.enableDisable(this, false);     // Enables the panel...
                    btnPaymentRevoke.setEnabled(true);
//                    txtRevokeReason.setVisible(true);
//                    lblRevokeReason.setVisible(true);
                }
                else{
                    ClientUtil.enableDisable(this, true);     // Enables the panel...
                    setButtonEnable(true);
                    setFieldsEnable(false);
                    btnPaymentRevoke.setEnabled(false);
//                    txtRevokeReason.setVisible(false);
//                    lblRevokeReason.setVisible(false);
                }
            }
            observable.setStatus();             // To set the Value of lblStatus...
            setButtonEnableDisable();         // Enables or Disables the buttons and menu Items depending on their previous state...
             if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    if((CommonUtil.convertObjToStr(hash.get("STOP_STATUS")).equalsIgnoreCase("REVOKED")) &&
                    (CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equalsIgnoreCase(""))){
                         txtRevokeReason.setVisible(true);
                         lblRevokeReason.setVisible(true);
//                         txtRevokeReason.setEditable(true);
//                         txtRevokeReason.setEnabled(true);
                         lblDDRevokeDate.setVisible(true);
                         lblRevokeDateVal.setVisible(true);
                    }
                }
        }else if (viewType==DDSTART) {
            txtStartDDNo1.setText(CommonUtil.convertObjToStr(hash.get("INSTRUMENT NO1")));
            txtStartDDNo2.setText(CommonUtil.convertObjToStr(hash.get("INSTRUMENT NO2")));
            txtStartVariableNo.setText(CommonUtil.convertObjToStr(hash.get("VARIABLE NO")));
            lblDDDateValue.setText(CommonUtil.convertObjToStr(hash.get("ISSUE DATE")));
            lblAmountValue.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            lblPayeeNameValue.setText(CommonUtil.convertObjToStr(hash.get("PAYEE NAME")));
            if(CommonUtil.convertObjToStr(hash.get("REMARKS")).equalsIgnoreCase("LAPSED")){
                ClientUtil.showMessageWindow("Instrument Lapsed Cannot be Stopped");
                btnSave.setEnabled(false);
            }else{
                btnSave.setEnabled(true);
            }
            
        }else if (viewType==DDEND) {
            txtEndDDNo1.setText(CommonUtil.convertObjToStr(hash.get("INSTRUMENT NO1")));
            txtEndDDNo2.setText(CommonUtil.convertObjToStr(hash.get("INSTRUMENT NO2")));
            txtEndVariableNo.setText(CommonUtil.convertObjToStr(hash.get("VARIABLE NO")));
            if(CommonUtil.convertObjToStr(hash.get("REMARKS")).equalsIgnoreCase("LAPSED")){
                ClientUtil.showMessageWindow("Instrument Lapsed Cannot be Stopped");
                btnSave.setEnabled(false);
            }else{
                btnSave.setEnabled(true);
            }
        }
         if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType == VIEW) {
//            if(panEditDelete==STOP){       //__ pan selected is Stop Payment issue...
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) || 
                (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)){
                    if((CommonUtil.convertObjToStr(hash.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")) &&
                    (CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equalsIgnoreCase(""))){
                        btnSave.setEnabled(true);
                    }else{
                        btnSave.setEnabled(true);     //changed for testing btnSave.setEnabled(false)
                    }
                }
//            }
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    private void setEnableDisable(boolean value){
        cboProdId.setEnabled(value);
        rdoDDLeaf_Single.setEnabled(value);
        rdoDDLeaf_Bulk.setEnabled(value);
        panStartDDNo.setEnabled(value);
        txtStartDDNo1.setEnabled(value);
        txtStartDDNo2.setEnabled(value);
        txtEndDDNo1.setEnabled(value);
        txtEndDDNo2.setEnabled(value);
        panEndDDNo.setEnabled(value);
        txtStartVariableNo.setEnabled(value);
        txtEndVariableNo.setEnabled(value);
        txtReason.setEnabled(value);
    }
    private void setButtonEnable(boolean value){
        btnDDStartNo.setEnabled(value);
        btnDDEndNo.setEnabled(value);
        btnPaymentRevoke.setEnabled(value);
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
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
        setButtonEnable(true);
        setFieldsEnable(false);
        btnPaymentRevoke.setEnabled(false);
        isRevoked = false;
        Date curDt = (Date) currDt.clone();
        lblDDStopDateValue.setText(CommonUtil.convertObjToStr(curDt));
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setFieldsEnable(boolean value){
        txtStartDDNo1.setEnabled(value);
        txtStartDDNo2.setEnabled(value);
        txtStartVariableNo.setEnabled(value);
        
        txtEndDDNo1.setEnabled(value);
        txtEndDDNo2.setEnabled(value);
        txtEndVariableNo.setEnabled(value);
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
        new RemitStopPaymentUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDDEndNo;
    private com.see.truetransact.uicomponent.CButton btnDDStartNo;
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
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblDDDate;
    private com.see.truetransact.uicomponent.CLabel lblDDDateValue;
    private com.see.truetransact.uicomponent.CLabel lblDDLeaf;
    private com.see.truetransact.uicomponent.CLabel lblDDRevokeDate;
    private com.see.truetransact.uicomponent.CLabel lblDDStopDate;
    private com.see.truetransact.uicomponent.CLabel lblDDStopDateValue;
    private com.see.truetransact.uicomponent.CLabel lblEndDDNo;
    private com.see.truetransact.uicomponent.CLabel lblEndVariableNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPayeeName;
    private com.see.truetransact.uicomponent.CLabel lblPayeeNameValue;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblReason;
    private com.see.truetransact.uicomponent.CLabel lblRevokeDateVal;
    private com.see.truetransact.uicomponent.CLabel lblRevokeReason;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStartDDNo1;
    private com.see.truetransact.uicomponent.CLabel lblStartVariableNo;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblStopId;
    private com.see.truetransact.uicomponent.CLabel lblStopIdValue;
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
    private com.see.truetransact.uicomponent.CTextField txtEndDDNo1;
    private com.see.truetransact.uicomponent.CTextField txtEndDDNo2;
    private com.see.truetransact.uicomponent.CTextField txtEndVariableNo;
    private com.see.truetransact.uicomponent.CTextField txtReason;
    private com.see.truetransact.uicomponent.CTextField txtRevokeReason;
    private com.see.truetransact.uicomponent.CTextField txtStartDDNo1;
    private com.see.truetransact.uicomponent.CTextField txtStartDDNo2;
    private com.see.truetransact.uicomponent.CTextField txtStartVariableNo;
    // End of variables declaration//GEN-END:variables
    
}
