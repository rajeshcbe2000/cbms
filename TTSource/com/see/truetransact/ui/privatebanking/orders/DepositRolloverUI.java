/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositRolloverUI.java
 *
 * Created on June 16, 2004, 12:26 PM
 */

package com.see.truetransact.ui.privatebanking.orders;

import com.see.truetransact.ui.privatebanking.orders.DepositRolloverRB;
import com.see.truetransact.ui.privatebanking.orders.DepositRolloverOB;
import com.see.truetransact.ui.privatebanking.orders.DepositRolloverMRB;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ComboBoxModel;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;


/**
 *
 * @author  Lohith R.
 */
public class DepositRolloverUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {
    
    private DepositRolloverOB observable;
    HashMap mandatoryMap;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2;
    boolean isFilled = false;
    int ACTION=-1;
    
    /** Creates new form DepositRolloverUI */
    public DepositRolloverUI() {
        initComponents();
        initStartUP();
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        setObservable();;
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        setMaximumLength();
        setHelpMessage();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.resetStatus();
        observable.resetFields();
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = DepositRolloverOB.getInstance();
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
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboRelationship.setName("cboRelationship");
        cboClientContact.setName("cboClientContact");
        cboContactMode.setName("cboContactMode");
        cboContactTimeHours.setName("cboContactTimeHours");
        cboContactTimeMinutes.setName("cboContactTimeMinutes");
        cboInstructionFrom.setName("cboInstructionFrom");
        cboOrderType.setName("cboOrderType");
        cboSolicited.setName("cboSolicited");
        txtSrcDocDetails.setName("txtSrcDocDetails");
        dateContactDate.setName("dateContactDate");
        dateSrcDocDate.setName("dateSrcDocDate");
        lblAuthSrcDoc.setName("lblAuthSrcDoc");
        lblClientContact.setName("lblClientContact");
        lblContactDate.setName("lblContactDate");
        lblContactMode.setName("lblContactMode");
        lblContactTime.setName("lblContactTime");
        lblContactTimeHours.setName("lblContactTimeHours");
        lblContactTimeMinutes.setName("lblContactTimeMinutes");
        lblDescription.setName("lblDescription");
        lblInstructionFrom.setName("lblInstructionFrom");
        lblMember.setName("lblMember");
        lblMsg.setName("lblMsg");
        lblOrderType.setName("lblOrderType");
        lblPhoneExtnum.setName("lblPhoneExtnum");
        lblRelationship.setName("lblRelationship");
        lblSolicited.setName("lblSolicited");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSrcDocDate.setName("lblSrcDocDate");
        lblSrcDocDetails.setName("lblSrcDocDetails");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panContactDetails.setName("panContactDetails");
        panContactTime.setName("panContactTime");
        panMain.setName("panMain");
        panMemberDetails.setName("panMemberDetails");
        panStatus.setName("panStatus");
        cboAuthSrcDoc.setName("cboAuthSrcDoc");
        txtDescription.setName("txtDescription");
        txtMember.setName("txtMember");
        txtPhoneExtnum.setName("txtPhoneExtnum");
        cboViewInfoDoc.setName("cboViewInfoDoc");
        lblViewInfoDoc.setName("lblViewInfoDoc");
        panViewInfoDoc.setName("panViewInfoDoc");
    }
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        DepositRolloverRB resourceBundle = new DepositRolloverRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblContactMode.setText(resourceBundle.getString("lblContactMode"));
        lblContactTimeHours.setText(resourceBundle.getString("lblContactTimeHours"));
        lblInstructionFrom.setText(resourceBundle.getString("lblInstructionFrom"));
        lblAuthSrcDoc.setText(resourceBundle.getString("lblAuthSrcDoc"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblSrcDocDate.setText(resourceBundle.getString("lblSrcDocDate"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblContactDate.setText(resourceBundle.getString("lblContactDate"));
        ((javax.swing.border.TitledBorder)panMemberDetails.getBorder()).setTitle(resourceBundle.getString("panMemberDetails"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        ((javax.swing.border.TitledBorder)panContactDetails.getBorder()).setTitle(resourceBundle.getString("panContactDetails"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblClientContact.setText(resourceBundle.getString("lblClientContact"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblDescription.setText(resourceBundle.getString("lblDescription"));
        lblPhoneExtnum.setText(resourceBundle.getString("lblPhoneExtnum"));
        lblOrderType.setText(resourceBundle.getString("lblOrderType"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        lblViewInfoDoc.setText(resourceBundle.getString("lblViewInfoDoc"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblMember.setText(resourceBundle.getString("lblMember"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblContactTime.setText(resourceBundle.getString("lblContactTime"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSrcDocDetails.setText(resourceBundle.getString("lblSrcDocDetails"));
        lblSolicited.setText(resourceBundle.getString("lblSolicited"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblContactTimeMinutes.setText(resourceBundle.getString("lblContactTimeMinutes"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("cboContactMode", new Boolean(true));
        mandatoryMap.put("cboRelationship", new Boolean(false));
        mandatoryMap.put("cboOrderType", new Boolean(true));
        mandatoryMap.put("txtSrcDocDetails", new Boolean(true));
        mandatoryMap.put("cboAuthSrcDoc", new Boolean(true));
        mandatoryMap.put("cboSolicited", new Boolean(true));
        mandatoryMap.put("cboInstructionFrom", new Boolean(true));
        mandatoryMap.put("txtPhoneExtnum", new Boolean(true));
        mandatoryMap.put("dateContactDate", new Boolean(true));
        mandatoryMap.put("cboContactTimeMinutes", new Boolean(true));
        mandatoryMap.put("cboContactTimeHours", new Boolean(true));
        mandatoryMap.put("cboClientContact", new Boolean(true));
        mandatoryMap.put("dateSrcDocDate", new Boolean(true));
        mandatoryMap.put("txtDescription", new Boolean(true));
        mandatoryMap.put("cboViewInfoDoc", new Boolean(true));
    }
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        cboContactMode.setModel(observable.getCbmContactMode());
        cboOrderType.setModel(observable.getCbmOrderType());
        cboInstructionFrom.setModel(observable.getCbmInstructionFrom());
        cboSolicited.setModel(observable.getCbmSolicited());
        cboAuthSrcDoc.setModel(observable.getCbmAuthSrcDoc());
        cboContactTimeHours.setModel(observable.getCbmContactTimeHours());
        cboContactTimeMinutes.setModel(observable.getCbmContactTimeMinutes());
        cboViewInfoDoc.setModel(observable.getCbmViewInfoDoc());
        cboRelationship.setModel(observable.getCbmRelationship());
        cboClientContact.setModel(observable.getCbmClientContact());
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        DepositRolloverMRB objDepositRolloverMRB = new DepositRolloverMRB();
        txtMember.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("txtMember"));
        cboContactMode.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboContactMode"));
        cboRelationship.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboRelationship"));
        cboOrderType.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboOrderType"));
        txtSrcDocDetails.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("txtSrcDocDetails"));
        cboAuthSrcDoc.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboAuthSrcDoc"));
        cboSolicited.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboSolicited"));
        cboInstructionFrom.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboInstructionFrom"));
        txtPhoneExtnum.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("txtPhoneExtnum"));
        dateContactDate.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("dateContactDate"));
        cboContactTimeMinutes.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboContactTimeMinutes"));
        cboContactTimeHours.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboContactTimeHours"));
        cboClientContact.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboClientContact"));
        dateSrcDocDate.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("dateSrcDocDate"));
        txtDescription.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("txtDescription"));
        cboViewInfoDoc.setHelpMessage(lblMsg, objDepositRolloverMRB.getString("cboViewInfoDoc"));
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtMember.setText(observable.getTxtMember());
        cboContactMode.setSelectedItem(observable.getCboContactMode());
        cboOrderType.setSelectedItem(observable.getCboOrderType());
        cboRelationship.setSelectedItem(observable.getCboRelationship());
        txtSrcDocDetails.setText(observable.getTxtSrcDocDetails());
        cboAuthSrcDoc.setSelectedItem(observable.getCboAuthSrcDoc());
        cboSolicited.setSelectedItem(observable.getCboSolicited());
        cboInstructionFrom.setSelectedItem(observable.getCboInstructionFrom());
        txtPhoneExtnum.setText(observable.getTxtPhoneExtnum());
        cboContactTimeMinutes.setSelectedItem(observable.getCboContactTimeMinutes());
        cboContactTimeHours.setSelectedItem(observable.getCboContactTimeHours());
        cboClientContact.setSelectedItem(observable.getCboClientContact());
        txtDescription.setText(observable.getTxtDescription());
        cboViewInfoDoc.setSelectedItem(observable.getCboViewInfoDoc());
        dateContactDate.setDateValue(observable.getDateContactDate());
        dateSrcDocDate.setDateValue(observable.getDateSrcDocDate());
        lblStatus.setText(observable.getLblStatus());
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtMember(txtMember.getText());
        observable.setCboContactMode((String) cboContactMode.getSelectedItem());
        observable.setCboOrderType((String) cboOrderType.getSelectedItem());
        observable.setCboRelationship((String)cboRelationship.getSelectedItem());
        observable.setTxtSrcDocDetails(txtSrcDocDetails.getText());
        observable.setCboAuthSrcDoc((String)cboAuthSrcDoc.getSelectedItem());
        observable.setCboSolicited((String) cboSolicited.getSelectedItem());
        observable.setCboInstructionFrom((String) cboInstructionFrom.getSelectedItem());
        observable.setTxtPhoneExtnum(txtPhoneExtnum.getText());
        observable.setCboContactTimeMinutes((String) cboContactTimeMinutes.getSelectedItem());
        observable.setCboContactTimeHours((String) cboContactTimeHours.getSelectedItem());
        observable.setCboClientContact((String) cboClientContact.getSelectedItem());
        observable.setTxtDescription(txtDescription.getText());
        observable.setCboViewInfoDoc((String) cboViewInfoDoc.getSelectedItem());
        observable.setDateContactDate(dateContactDate.getDateValue());
        observable.setDateSrcDocDate(dateSrcDocDate.getDateValue());
    }
    
    private void setMaximumLength() {
        txtMember.setMaxLength(64);
        txtPhoneExtnum.setMaxLength(16);
        txtDescription.setMaxLength(1024);
        txtSrcDocDetails.setMaxLength(32);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panMain = new com.see.truetransact.uicomponent.CPanel();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        txtMember = new com.see.truetransact.uicomponent.CTextField();
        cboContactMode = new com.see.truetransact.uicomponent.CComboBox();
        lblContactMode = new com.see.truetransact.uicomponent.CLabel();
        lblMember = new com.see.truetransact.uicomponent.CLabel();
        lblRelationship = new com.see.truetransact.uicomponent.CLabel();
        lblOrderType = new com.see.truetransact.uicomponent.CLabel();
        cboOrderType = new com.see.truetransact.uicomponent.CComboBox();
        cboRelationship = new com.see.truetransact.uicomponent.CComboBox();
        panContactDetails = new com.see.truetransact.uicomponent.CPanel();
        cboAuthSrcDoc = new com.see.truetransact.uicomponent.CComboBox();
        lblSrcDocDetails = new com.see.truetransact.uicomponent.CLabel();
        lblAuthSrcDoc = new com.see.truetransact.uicomponent.CLabel();
        txtSrcDocDetails = new com.see.truetransact.uicomponent.CTextField();
        lblSolicited = new com.see.truetransact.uicomponent.CLabel();
        cboSolicited = new com.see.truetransact.uicomponent.CComboBox();
        lblInstructionFrom = new com.see.truetransact.uicomponent.CLabel();
        cboInstructionFrom = new com.see.truetransact.uicomponent.CComboBox();
        lblPhoneExtnum = new com.see.truetransact.uicomponent.CLabel();
        txtPhoneExtnum = new com.see.truetransact.uicomponent.CTextField();
        lblContactDate = new com.see.truetransact.uicomponent.CLabel();
        dateContactDate = new com.see.truetransact.uicomponent.CDateField();
        panContactTime = new com.see.truetransact.uicomponent.CPanel();
        cboContactTimeMinutes = new com.see.truetransact.uicomponent.CComboBox();
        lblContactTimeMinutes = new com.see.truetransact.uicomponent.CLabel();
        cboContactTimeHours = new com.see.truetransact.uicomponent.CComboBox();
        lblContactTimeHours = new com.see.truetransact.uicomponent.CLabel();
        lblContactTime = new com.see.truetransact.uicomponent.CLabel();
        lblClientContact = new com.see.truetransact.uicomponent.CLabel();
        cboClientContact = new com.see.truetransact.uicomponent.CComboBox();
        lblSrcDocDate = new com.see.truetransact.uicomponent.CLabel();
        dateSrcDocDate = new com.see.truetransact.uicomponent.CDateField();
        lblDescription = new com.see.truetransact.uicomponent.CLabel();
        txtDescription = new com.see.truetransact.uicomponent.CTextField();
        panViewInfoDoc = new com.see.truetransact.uicomponent.CPanel();
        lblViewInfoDoc = new com.see.truetransact.uicomponent.CLabel();
        cboViewInfoDoc = new com.see.truetransact.uicomponent.CComboBox();
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
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
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
        setMaximizable(true);
        setResizable(true);
        setTitle("Deposit Rollover");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMain.setMinimumSize(new java.awt.Dimension(510, 280));
        panMain.setPreferredSize(new java.awt.Dimension(510, 290));
        panMain.setLayout(new java.awt.GridBagLayout());

        panMemberDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        txtMember.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(txtMember, gridBagConstraints);

        cboContactMode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(cboContactMode, gridBagConstraints);

        lblContactMode.setText("Contact Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 22, 4, 4);
        panMemberDetails.add(lblContactMode, gridBagConstraints);

        lblMember.setText("Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 22, 4, 4);
        panMemberDetails.add(lblMember, gridBagConstraints);

        lblRelationship.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panMemberDetails.add(lblRelationship, gridBagConstraints);

        lblOrderType.setText("Order Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panMemberDetails.add(lblOrderType, gridBagConstraints);

        cboOrderType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(cboOrderType, gridBagConstraints);

        cboRelationship.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(cboRelationship, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panMemberDetails, gridBagConstraints);

        panContactDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panContactDetails.setLayout(new java.awt.GridBagLayout());

        cboAuthSrcDoc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(cboAuthSrcDoc, gridBagConstraints);

        lblSrcDocDetails.setText("Source Doc Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblSrcDocDetails, gridBagConstraints);

        lblAuthSrcDoc.setText("Auth Source Doc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblAuthSrcDoc, gridBagConstraints);

        txtSrcDocDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(txtSrcDocDetails, gridBagConstraints);

        lblSolicited.setText("Solicited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblSolicited, gridBagConstraints);

        cboSolicited.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(cboSolicited, gridBagConstraints);

        lblInstructionFrom.setText("Instruction From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblInstructionFrom, gridBagConstraints);

        cboInstructionFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(cboInstructionFrom, gridBagConstraints);

        lblPhoneExtnum.setText("Phone Extension No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblPhoneExtnum, gridBagConstraints);

        txtPhoneExtnum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(txtPhoneExtnum, gridBagConstraints);

        lblContactDate.setText("Contact Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblContactDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(dateContactDate, gridBagConstraints);

        panContactTime.setLayout(new java.awt.GridBagLayout());

        cboContactTimeMinutes.setMinimumSize(new java.awt.Dimension(37, 21));
        cboContactTimeMinutes.setPreferredSize(new java.awt.Dimension(37, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panContactTime.add(cboContactTimeMinutes, gridBagConstraints);

        lblContactTimeMinutes.setText("min");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panContactTime.add(lblContactTimeMinutes, gridBagConstraints);

        cboContactTimeHours.setMinimumSize(new java.awt.Dimension(37, 21));
        cboContactTimeHours.setPreferredSize(new java.awt.Dimension(37, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panContactTime.add(cboContactTimeHours, gridBagConstraints);

        lblContactTimeHours.setText("hrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panContactTime.add(lblContactTimeHours, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(panContactTime, gridBagConstraints);

        lblContactTime.setText("Contact Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblContactTime, gridBagConstraints);

        lblClientContact.setText("Client Contact");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblClientContact, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(cboClientContact, gridBagConstraints);

        lblSrcDocDate.setText("Source Doc Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblSrcDocDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(dateSrcDocDate, gridBagConstraints);

        lblDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(lblDescription, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panContactDetails.add(txtDescription, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panContactDetails, gridBagConstraints);

        panViewInfoDoc.setPreferredSize(new java.awt.Dimension(250, 33));
        panViewInfoDoc.setLayout(new java.awt.GridBagLayout());

        lblViewInfoDoc.setText("View Vistual Info Doc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewInfoDoc.add(lblViewInfoDoc, gridBagConstraints);

        cboViewInfoDoc.setMinimumSize(new java.awt.Dimension(150, 21));
        cboViewInfoDoc.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewInfoDoc.add(cboViewInfoDoc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panViewInfoDoc, gridBagConstraints);

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

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMain.add(btnReject);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace28);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMain.add(btnException);

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
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetStatus();
        observable.resetFields();
        setButtonEnableDisable();
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
        cboRelationship.setModel(new ComboBoxModel());
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUpItems(EDIT);
        observable.setStatus();
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        cboRelationship.setModel(new ComboBoxModel());
        observable.setComboRelationship();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        cboRelationship.setModel(observable.getCbmRelationship());
        observable.setStatus();
        ClientUtil.enableDisable(this, true);
        setButtonEnableDisable();
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
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            /* If Action Type is DELETE */
            memberRelationExists = observable.memberDependency();
        }
        if(!memberRelationExists){
            observable.doAction();
        }
        observable.setResultStatus();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.resetFields();
    }
    
    /** This method helps in popoualting the data from the data base
     * @param Action the argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        if (Action == EDIT || Action == DELETE){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        final HashMap viewMap = new HashMap();
        ACTION=Action;
        viewMap.put(CommonConstants.MAP_NAME, "ViewAllDepositRolloverTO");
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
                observable.setOrderID((String) hash.get("ORDER ID"));
                observable.setComboRelationship();
                cboRelationship.setModel(observable.getCbmRelationship());
                observable.populateData(hash);
                ClientUtil.enableDisable(this, false);
                observable.setStatus();
                setButtonEnableDisable();
                lblStatus.setText(observable.getLblStatus());
            }
        }
        if (ACTION == EDIT){
            ClientUtil.enableDisable(this, true);
        }
    }
    
    
    
    public void authorizeStatus(String authorizeStatus) {
        if (ACTION == AUTHORIZE && isFilled) {
            final HashMap depositRolloverMap = new HashMap();
            depositRolloverMap.put("STATUS", authorizeStatus);
            depositRolloverMap.put("USER_ID", TrueTransactMain.USER_ID);
            depositRolloverMap.put("ORDER ID",observable.getOrderID());
            ClientUtil.execute("authorizeDepositRollover", depositRolloverMap);
            observable.setResult(observable.getActionType());
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            ACTION = 0;
        } else {
            final HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getDepositRolloverAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDepositRollover");
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
    
    /**
     * @param args the command line arguments
     */
    
    
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
    private com.see.truetransact.uicomponent.CComboBox cboAuthSrcDoc;
    private com.see.truetransact.uicomponent.CComboBox cboClientContact;
    private com.see.truetransact.uicomponent.CComboBox cboContactMode;
    private com.see.truetransact.uicomponent.CComboBox cboContactTimeHours;
    private com.see.truetransact.uicomponent.CComboBox cboContactTimeMinutes;
    private com.see.truetransact.uicomponent.CComboBox cboInstructionFrom;
    private com.see.truetransact.uicomponent.CComboBox cboOrderType;
    private com.see.truetransact.uicomponent.CComboBox cboRelationship;
    private com.see.truetransact.uicomponent.CComboBox cboSolicited;
    private com.see.truetransact.uicomponent.CComboBox cboViewInfoDoc;
    private com.see.truetransact.uicomponent.CDateField dateContactDate;
    private com.see.truetransact.uicomponent.CDateField dateSrcDocDate;
    private com.see.truetransact.uicomponent.CLabel lblAuthSrcDoc;
    private com.see.truetransact.uicomponent.CLabel lblClientContact;
    private com.see.truetransact.uicomponent.CLabel lblContactDate;
    private com.see.truetransact.uicomponent.CLabel lblContactMode;
    private com.see.truetransact.uicomponent.CLabel lblContactTime;
    private com.see.truetransact.uicomponent.CLabel lblContactTimeHours;
    private com.see.truetransact.uicomponent.CLabel lblContactTimeMinutes;
    private com.see.truetransact.uicomponent.CLabel lblDescription;
    private com.see.truetransact.uicomponent.CLabel lblInstructionFrom;
    private com.see.truetransact.uicomponent.CLabel lblMember;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOrderType;
    private com.see.truetransact.uicomponent.CLabel lblPhoneExtnum;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblSolicited;
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
    private com.see.truetransact.uicomponent.CLabel lblSrcDocDate;
    private com.see.truetransact.uicomponent.CLabel lblSrcDocDetails;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblViewInfoDoc;
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
    private com.see.truetransact.uicomponent.CPanel panContactDetails;
    private com.see.truetransact.uicomponent.CPanel panContactTime;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panViewInfoDoc;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtDescription;
    private com.see.truetransact.uicomponent.CTextField txtMember;
    private com.see.truetransact.uicomponent.CTextField txtPhoneExtnum;
    private com.see.truetransact.uicomponent.CTextField txtSrcDocDetails;
    // End of variables declaration//GEN-END:variables
    
}
