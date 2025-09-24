/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RoleScreenUI.java
 *
 * Created on April 9, 2004, 5:20 PM
 */

package com.see.truetransact.ui.sysadmin.role;

/**
 *
 * @author  Pinky
 * @modified  Jayakrishnan.K.R.
 * @modified  JK (Feb 25, 2005 11.03 am)
 *@modified : Sunil
 *      Added Edit Locking - 07-07-2005
 */

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientutil.ComboBoxModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class RoleUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    //    private RoleRB resourceBundle;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.role.RoleRB", ProxyParameters.LANGUAGE);
    
    private HashMap mandatoryMap;
    private RoleMRB objMandatoryRB = new RoleMRB();
    private RoleOB observable;
    
    private boolean _intRateNew = false;
    private final int LEVEL_MASTER=10, LEVEL_FOEIGN = 11;
    
    private int viewType = -1;
    private final int AUTHORIZE = 666;
    
    /** Creates new form BeanForm */
    public RoleUI() {
        initComponents();
        initSetup();
        panChks.setVisible(false);
        tabLevelDetails.remove(1);
    }
    public void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaxLength();
        setComponent();
        setEditable();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panRoleDetails,false);
        ClientUtil.enableDisable(panLevel,false);
        enableDisableRoleButtons(true);
    }
    private void setMaxLength() {
        txtRoleDesc.setMaxLength(128);
        
        //__ To get the Data in the Proper format...
        txtCashCredit.setValidation(new CurrencyValidation());
        txtCashDebit.setValidation(new CurrencyValidation());
        txtTransCredit.setValidation(new CurrencyValidation());
        txtTransDebit.setValidation(new CurrencyValidation());
        txtClearingCredit.setValidation(new CurrencyValidation());
        txtClearingDebit.setValidation(new CurrencyValidation());
        
        //__ To get the Data in the Proper format...
        txtCashCreditForeign.setValidation(new CurrencyValidation());
        txtCashDebitForeign.setValidation(new CurrencyValidation());
        txtTransCreditForeign.setValidation(new CurrencyValidation());
        txtTransDebitForeign.setValidation(new CurrencyValidation());
        txtClearingCreditForeign.setValidation(new CurrencyValidation());
        txtClearingDebitForeign.setValidation(new CurrencyValidation());
    }
    
    private void setComponent(){
        tblRoleDetails.setModel(observable.getTmlRoles());
        cboRoleHierarchy.setModel(observable.getCbmRoleHierarchy());
        cboRoleId.setModel(observable.getCbmRoleID());
    }
    // To set The Value of the Buttons Depending on the Value or Condition.
    private void setButtonEnableDisable() {
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    private void setEditable(){
        txtGroupID.setEditable(false);
        txtGroupDesc.setEditable(false);
        txtLevelID.setEditable(false);
        txtLevelDesc.setEditable(false);
        txtLevelName.setEditable(false);
        
        txtCashCredit.setEditable(false);
        txtCashDebit.setEditable(false);
        txtTransCredit.setEditable(false);
        txtTransDebit.setEditable(false);
        txtClearingCredit.setEditable(false);
        txtClearingDebit.setEditable(false);
        ClientUtil.enableDisable(panLevelDetails,false);
        
        txtLevelIDForeign.setEditable(false);
        txtLevelDescForeign.setEditable(false);
        txtLevelNameForeign.setEditable(false);
        
        txtCashCreditForeign.setEditable(false);
        txtCashDebitForeign.setEditable(false);
        txtTransCreditForeign.setEditable(false);
        txtTransDebitForeign.setEditable(false);
        txtClearingCreditForeign.setEditable(false);
        txtClearingDebitForeign.setEditable(false);
        ClientUtil.enableDisable(panLevelDetailsForeign,false);
    }
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        txtGroupID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGroupID"));
        txtGroupDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGroupDesc"));
        cboRoleId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoleId"));
        txtRoleDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRoleDesc"));
        chkAccAllBran.setHelpMessage(lblMsg, objMandatoryRB.getString("chkAccAllBran"));
        txtLevelID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelID"));
        txtLevelName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelName"));
        txtLevelDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelDesc"));
        txtCashDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashDebit"));
        txtCashCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashCredit"));
        txtClearingCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingCredit"));
        txtClearingDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingDebit"));
        txtTransCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransCredit"));
        txtTransDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransDebit"));
        txtLevelNameForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelNameForeign"));
        txtLevelDescForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelDescForeign"));
        txtCashDebitForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashDebitForeign"));
        txtCashCreditForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashCreditForeign"));
        txtClearingCreditForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingCreditForeign"));
        txtClearingDebitForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingDebitForeign"));
        txtTransCreditForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransCreditForeign"));
        txtTransDebitForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransDebitForeign"));
        txtLevelIDForeign.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelIDForeign"));
        cboRoleHierarchy.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoleHierarchy"));
        chkHierarchyAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkHierarchyAllowed"));
    }
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtGroupID.setText(observable.getTxtGroupID());
        txtGroupDesc.setText(observable.getTxtGroupDesc());
        cboRoleId.setSelectedItem(observable.getCboRoleID());
        txtRoleDesc.setText(observable.getTxtRoleDesc());
        chkAccAllBran.setSelected(observable.getChkAccAllBran());
        txtLevelID.setText(observable.getTxtLevelID());
        txtLevelName.setText(observable.getTxtLevelName());
        txtLevelDesc.setText(observable.getTxtLevelDesc());
        txtCashDebit.setText(observable.getTxtCashDebit());
        txtCashCredit.setText(observable.getTxtCashCredit());
        txtClearingCredit.setText(observable.getTxtClearingCredit());
        txtClearingDebit.setText(observable.getTxtClearingDebit());
        txtTransCredit.setText(observable.getTxtTransCredit());
        txtTransDebit.setText(observable.getTxtTransDebit());
        txtLevelNameForeign.setText(observable.getTxtLevelNameForeign());
        txtLevelDescForeign.setText(observable.getTxtLevelDescForeign());
        txtCashDebitForeign.setText(observable.getTxtCashDebitForeign());
        txtCashCreditForeign.setText(observable.getTxtCashCreditForeign());
        txtClearingCreditForeign.setText(observable.getTxtClearingCreditForeign());
        txtClearingDebitForeign.setText(observable.getTxtClearingDebitForeign());
        txtTransCreditForeign.setText(observable.getTxtTransCreditForeign());
        txtTransDebitForeign.setText(observable.getTxtTransDebitForeign());
        txtLevelIDForeign.setText(observable.getTxtLevelIDForeign());
        cboRoleHierarchy.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboRoleHierarchy()));
        lblStatus.setText(observable.getLblStatus());
        chkHierarchyAllowed.setSelected(observable.getChkHierarchyAllowed());
    }
    
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtGroupID(txtGroupID.getText());
        observable.setTxtGroupDesc(txtGroupDesc.getText());
        observable.setCboRoleID((String)(((ComboBoxModel)(cboRoleId).getModel())).getKeyForSelected());
        observable.setTxtRoleDesc(txtRoleDesc.getText());
        observable.setChkAccAllBran(chkAccAllBran.isSelected());
        observable.setTxtLevelID(txtLevelID.getText());
        observable.setTxtLevelName(txtLevelName.getText());
        observable.setTxtLevelDesc(txtLevelDesc.getText());
        observable.setTxtCashDebit(txtCashDebit.getText());
        observable.setTxtCashCredit(txtCashCredit.getText());
        observable.setTxtClearingCredit(txtClearingCredit.getText());
        observable.setTxtClearingDebit(txtClearingDebit.getText());
        observable.setTxtTransCredit(txtTransCredit.getText());
        observable.setTxtTransDebit(txtTransDebit.getText());
        observable.setTxtLevelNameForeign(txtLevelNameForeign.getText());
        observable.setTxtLevelDescForeign(txtLevelDescForeign.getText());
        observable.setTxtCashDebitForeign(txtCashDebitForeign.getText());
        observable.setTxtCashCreditForeign(txtCashCreditForeign.getText());
        observable.setTxtClearingCreditForeign(txtClearingCreditForeign.getText());
        observable.setTxtClearingDebitForeign(txtClearingDebitForeign.getText());
        observable.setTxtTransCreditForeign(txtTransCreditForeign.getText());
        observable.setTxtTransDebitForeign(txtTransDebitForeign.getText());
        observable.setTxtLevelIDForeign(txtLevelIDForeign.getText());
        observable.setCboRoleHierarchy(CommonUtil.convertObjToStr(observable.getCbmRoleHierarchy().getKeyForSelected()));
        observable.setChkHierarchyAllowed(chkHierarchyAllowed.isSelected());
    }
    /** Set observable */
    private void setObservable() {
        observable = RoleOB.getInstance();
        observable.addObserver(this);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panGroup = new com.see.truetransact.uicomponent.CPanel();
        lblGroupID = new com.see.truetransact.uicomponent.CLabel();
        txtGroupID = new com.see.truetransact.uicomponent.CTextField();
        txtGroupDesc = new com.see.truetransact.uicomponent.CTextField();
        lblGroupDesc = new com.see.truetransact.uicomponent.CLabel();
        sptGroup = new com.see.truetransact.uicomponent.CSeparator();
        panLevelRole = new com.see.truetransact.uicomponent.CPanel();
        panRoleLevelDetails = new com.see.truetransact.uicomponent.CPanel();
        panRoleDetails = new com.see.truetransact.uicomponent.CPanel();
        lblRoleID = new com.see.truetransact.uicomponent.CLabel();
        lblRoleDesc = new com.see.truetransact.uicomponent.CLabel();
        txtRoleDesc = new com.see.truetransact.uicomponent.CTextField();
        lblRoleHierarchy = new com.see.truetransact.uicomponent.CLabel();
        cboRoleHierarchy = new com.see.truetransact.uicomponent.CComboBox();
        panChks = new com.see.truetransact.uicomponent.CPanel();
        chkAccAllBran = new com.see.truetransact.uicomponent.CCheckBox();
        lblAccAllBran = new com.see.truetransact.uicomponent.CLabel();
        sptRole = new com.see.truetransact.uicomponent.CSeparator();
        lblHierarchyAllowed = new com.see.truetransact.uicomponent.CLabel();
        chkHierarchyAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        cboRoleId = new com.see.truetransact.uicomponent.CComboBox();
        panLevel = new com.see.truetransact.uicomponent.CPanel();
        tabLevelDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panLevelHome = new com.see.truetransact.uicomponent.CPanel();
        lblLevelID = new com.see.truetransact.uicomponent.CLabel();
        panLevelID = new com.see.truetransact.uicomponent.CPanel();
        btnLevelID = new com.see.truetransact.uicomponent.CButton();
        txtLevelID = new com.see.truetransact.uicomponent.CTextField();
        lblLevelName = new com.see.truetransact.uicomponent.CLabel();
        txtLevelName = new com.see.truetransact.uicomponent.CTextField();
        lblLevelDesc = new com.see.truetransact.uicomponent.CLabel();
        txtLevelDesc = new com.see.truetransact.uicomponent.CTextField();
        panLevelDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCashCredit = new com.see.truetransact.uicomponent.CLabel();
        lblClearingCredit = new com.see.truetransact.uicomponent.CLabel();
        lblTransCredit = new com.see.truetransact.uicomponent.CLabel();
        lblCashDebit = new com.see.truetransact.uicomponent.CLabel();
        lblClearingDebit = new com.see.truetransact.uicomponent.CLabel();
        lblTransDebit = new com.see.truetransact.uicomponent.CLabel();
        txtCashDebit = new com.see.truetransact.uicomponent.CTextField();
        txtCashCredit = new com.see.truetransact.uicomponent.CTextField();
        txtClearingCredit = new com.see.truetransact.uicomponent.CTextField();
        txtClearingDebit = new com.see.truetransact.uicomponent.CTextField();
        txtTransCredit = new com.see.truetransact.uicomponent.CTextField();
        txtTransDebit = new com.see.truetransact.uicomponent.CTextField();
        panLevelForeign = new com.see.truetransact.uicomponent.CPanel();
        lblLevelIDForeign = new com.see.truetransact.uicomponent.CLabel();
        lblLevelNameForeign = new com.see.truetransact.uicomponent.CLabel();
        txtLevelNameForeign = new com.see.truetransact.uicomponent.CTextField();
        lblLevelDescForeign = new com.see.truetransact.uicomponent.CLabel();
        txtLevelDescForeign = new com.see.truetransact.uicomponent.CTextField();
        panLevelDetailsForeign = new com.see.truetransact.uicomponent.CPanel();
        lblCashCreditForeign = new com.see.truetransact.uicomponent.CLabel();
        lblClearingCreditForeign = new com.see.truetransact.uicomponent.CLabel();
        lblTransCreditForeign = new com.see.truetransact.uicomponent.CLabel();
        lblCashDebitForeign = new com.see.truetransact.uicomponent.CLabel();
        lblClearingDebitForeign = new com.see.truetransact.uicomponent.CLabel();
        lblTransDebitForeign = new com.see.truetransact.uicomponent.CLabel();
        txtCashDebitForeign = new com.see.truetransact.uicomponent.CTextField();
        txtCashCreditForeign = new com.see.truetransact.uicomponent.CTextField();
        txtClearingCreditForeign = new com.see.truetransact.uicomponent.CTextField();
        txtClearingDebitForeign = new com.see.truetransact.uicomponent.CTextField();
        txtTransCreditForeign = new com.see.truetransact.uicomponent.CTextField();
        txtTransDebitForeign = new com.see.truetransact.uicomponent.CTextField();
        panLevelIDForeign = new com.see.truetransact.uicomponent.CPanel();
        btnLevelIDForeign = new com.see.truetransact.uicomponent.CButton();
        txtLevelIDForeign = new com.see.truetransact.uicomponent.CTextField();
        panLevelButtons = new com.see.truetransact.uicomponent.CPanel();
        btnNewLevel = new com.see.truetransact.uicomponent.CButton();
        btnSaveLevel = new com.see.truetransact.uicomponent.CButton();
        btnDeleteLevel = new com.see.truetransact.uicomponent.CButton();
        panRoleTbl = new com.see.truetransact.uicomponent.CPanel();
        srpRole = new com.see.truetransact.uicomponent.CScrollPane();
        tblRoleDetails = new com.see.truetransact.uicomponent.CTable();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(87, 20));
        setPreferredSize(new java.awt.Dimension(850, 600));

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
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
        tbrAdvances.add(btnView);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace24);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace2.setText("     ");
        tbrAdvances.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace25);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace26);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace27);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnPrint);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace28);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        getContentPane().add(tbrAdvances, java.awt.BorderLayout.NORTH);

        panGroup.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGroup.setLayout(new java.awt.GridBagLayout());

        lblGroupID.setText("Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroup.add(lblGroupID, gridBagConstraints);

        txtGroupID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroup.add(txtGroupID, gridBagConstraints);

        txtGroupDesc.setPreferredSize(new java.awt.Dimension(200, 21));
        txtGroupDesc.setMinimumSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroup.add(txtGroupDesc, gridBagConstraints);

        lblGroupDesc.setText("Group Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroup.add(lblGroupDesc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroup.add(sptGroup, gridBagConstraints);

        panLevelRole.setLayout(new java.awt.GridBagLayout());

        panRoleLevelDetails.setPreferredSize(new java.awt.Dimension(400, 60));
        panRoleLevelDetails.setMinimumSize(new java.awt.Dimension(200, 46));
        panRoleLevelDetails.setLayout(new java.awt.GridBagLayout());

        panRoleDetails.setLayout(new java.awt.GridBagLayout());

        lblRoleID.setText("Role ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(lblRoleID, gridBagConstraints);

        lblRoleDesc.setText("Role Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(lblRoleDesc, gridBagConstraints);

        txtRoleDesc.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRoleDesc.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(txtRoleDesc, gridBagConstraints);

        lblRoleHierarchy.setText("Role Hierarchy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(lblRoleHierarchy, gridBagConstraints);

        cboRoleHierarchy.setPopupWidth(125);
        cboRoleHierarchy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRoleHierarchyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(cboRoleHierarchy, gridBagConstraints);

        panChks.setLayout(new java.awt.GridBagLayout());

        chkAccAllBran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAccAllBranActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panChks.add(chkAccAllBran, gridBagConstraints);

        lblAccAllBran.setText("Access All Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChks.add(lblAccAllBran, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRoleDetails.add(panChks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(sptRole, gridBagConstraints);

        lblHierarchyAllowed.setText("Same Hierarchy Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(lblHierarchyAllowed, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleDetails.add(chkHierarchyAllowed, gridBagConstraints);

        cboRoleId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoleId.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRoleDetails.add(cboRoleId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRoleLevelDetails.add(panRoleDetails, gridBagConstraints);

        panLevel.setLayout(new java.awt.GridBagLayout());

        panLevelHome.setLayout(new java.awt.GridBagLayout());

        lblLevelID.setText("Level ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelHome.add(lblLevelID, gridBagConstraints);

        panLevelID.setLayout(new java.awt.GridBagLayout());

        btnLevelID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLevelID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLevelID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLevelID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLevelIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLevelID.add(btnLevelID, gridBagConstraints);

        txtLevelID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelID.add(txtLevelID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLevelHome.add(panLevelID, gridBagConstraints);

        lblLevelName.setText("Level Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelHome.add(lblLevelName, gridBagConstraints);

        txtLevelName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelHome.add(txtLevelName, gridBagConstraints);

        lblLevelDesc.setText("Level Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelHome.add(lblLevelDesc, gridBagConstraints);

        txtLevelDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        txtLevelDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelHome.add(txtLevelDesc, gridBagConstraints);

        panLevelDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Level Details"));
        panLevelDetails.setLayout(new java.awt.GridBagLayout());

        lblCashCredit.setText("Cash Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(lblCashCredit, gridBagConstraints);

        lblClearingCredit.setText("Clearing Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(lblClearingCredit, gridBagConstraints);

        lblTransCredit.setText("Trans Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(lblTransCredit, gridBagConstraints);

        lblCashDebit.setText("Cash Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(lblCashDebit, gridBagConstraints);

        lblClearingDebit.setText("Clearing Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(lblClearingDebit, gridBagConstraints);

        lblTransDebit.setText("Trans Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(lblTransDebit, gridBagConstraints);

        txtCashDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(txtCashDebit, gridBagConstraints);

        txtCashCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(txtCashCredit, gridBagConstraints);

        txtClearingCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(txtClearingCredit, gridBagConstraints);

        txtClearingDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(txtClearingDebit, gridBagConstraints);

        txtTransCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(txtTransCredit, gridBagConstraints);

        txtTransDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetails.add(txtTransDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLevelHome.add(panLevelDetails, gridBagConstraints);

        tabLevelDetails.addTab("Home Level", panLevelHome);

        panLevelForeign.setLayout(new java.awt.GridBagLayout());

        lblLevelIDForeign.setText("Level ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelForeign.add(lblLevelIDForeign, gridBagConstraints);

        lblLevelNameForeign.setText("Level Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelForeign.add(lblLevelNameForeign, gridBagConstraints);

        txtLevelNameForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelForeign.add(txtLevelNameForeign, gridBagConstraints);

        lblLevelDescForeign.setText("Level Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelForeign.add(lblLevelDescForeign, gridBagConstraints);

        txtLevelDescForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLevelDescForeign.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelForeign.add(txtLevelDescForeign, gridBagConstraints);

        panLevelDetailsForeign.setBorder(javax.swing.BorderFactory.createTitledBorder("Level Details"));
        panLevelDetailsForeign.setLayout(new java.awt.GridBagLayout());

        lblCashCreditForeign.setText("Cash Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(lblCashCreditForeign, gridBagConstraints);

        lblClearingCreditForeign.setText("Clearing Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(lblClearingCreditForeign, gridBagConstraints);

        lblTransCreditForeign.setText("Trans Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(lblTransCreditForeign, gridBagConstraints);

        lblCashDebitForeign.setText("Cash Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(lblCashDebitForeign, gridBagConstraints);

        lblClearingDebitForeign.setText("Clearing Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(lblClearingDebitForeign, gridBagConstraints);

        lblTransDebitForeign.setText("Trans Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(lblTransDebitForeign, gridBagConstraints);

        txtCashDebitForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(txtCashDebitForeign, gridBagConstraints);

        txtCashCreditForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(txtCashCreditForeign, gridBagConstraints);

        txtClearingCreditForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(txtClearingCreditForeign, gridBagConstraints);

        txtClearingDebitForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(txtClearingDebitForeign, gridBagConstraints);

        txtTransCreditForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(txtTransCreditForeign, gridBagConstraints);

        txtTransDebitForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelDetailsForeign.add(txtTransDebitForeign, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLevelForeign.add(panLevelDetailsForeign, gridBagConstraints);

        panLevelIDForeign.setLayout(new java.awt.GridBagLayout());

        btnLevelIDForeign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLevelIDForeign.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLevelIDForeign.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLevelIDForeign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLevelIDForeignActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLevelIDForeign.add(btnLevelIDForeign, gridBagConstraints);

        txtLevelIDForeign.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelIDForeign.add(txtLevelIDForeign, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLevelForeign.add(panLevelIDForeign, gridBagConstraints);

        tabLevelDetails.addTab("Foreign Level", panLevelForeign);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panLevel.add(tabLevelDetails, gridBagConstraints);

        btnNewLevel.setText("New");
        btnNewLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewLevelActionPerformed(evt);
            }
        });
        panLevelButtons.add(btnNewLevel);

        btnSaveLevel.setText("Save");
        btnSaveLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveLevelActionPerformed(evt);
            }
        });
        panLevelButtons.add(btnSaveLevel);

        btnDeleteLevel.setText("Delete");
        btnDeleteLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLevelActionPerformed(evt);
            }
        });
        panLevelButtons.add(btnDeleteLevel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 4, 4, 4);
        panLevel.add(panLevelButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panRoleLevelDetails.add(panLevel, gridBagConstraints);

        panRoleTbl.setLayout(new java.awt.GridBagLayout());

        srpRole.setPreferredSize(new java.awt.Dimension(100, 50));

        tblRoleDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Role", "Desc.", "Hierarchy", "Home", "Foreign", "All", "Same Hierarchy"
            }
        ));
        tblRoleDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRoleDetailsMouseClicked(evt);
            }
        });
        srpRole.setViewportView(tblRoleDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRoleTbl.add(srpRole, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRoleLevelDetails.add(panRoleTbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelRole.add(panRoleLevelDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroup.add(panLevelRole, gridBagConstraints);

        getContentPane().add(panGroup, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

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

        mitClose.setMnemonic('L');
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

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void tblRoleDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRoleDetailsMouseClicked
        // TODO add your handling code here:
        System.out.println("Tbl Role Details "+ tblRoleDetails.isEnabled());
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE
        && viewType != AUTHORIZE ){
            panLevelEnableDisable(true);
        }
        if( observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW)
        {
             panLevelEnableDisable(false);  
        }
        updationRole();
        ClientUtil.enableDisable(panLevelDetails, false);
         txtLevelID.setEnabled(false);
        txtLevelName.setEnabled(false);
        txtLevelDesc.setEnabled(false);
    }//GEN-LAST:event_tblRoleDetailsMouseClicked
    
    private void chkAccAllBranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAccAllBranActionPerformed
        // TODO add your handling code here:
        if(chkAccAllBran.isSelected()){
            tabLevelDetails.addTab(resourceBundle.getString("panLevelDetailsForeign"), panLevelForeign);
            btnLevelIDForeign.setEnabled(true);
        }else{
            //            tabLevelDetails.remove(1);
            ClientUtil.clearAll(panLevelForeign);
            btnLevelIDForeign.setEnabled(false);
        }
    }//GEN-LAST:event_chkAccAllBranActionPerformed
    
    private void cboRoleHierarchyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRoleHierarchyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRoleHierarchyActionPerformed
    
    private void btnLevelIDForeignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLevelIDForeignActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        callView(LEVEL_FOEIGN);
    }//GEN-LAST:event_btnLevelIDForeignActionPerformed
    
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
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getRoleMasterAuthorizeList");
            
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRoleMasterMasterData");
            
            whereMap = null;
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        } else if (viewType == AUTHORIZE){
            if(tblRoleDetails.getRowCount()!=0){
//                if(CommonUtil.convertObjToStr(txtRoleID.getText()).equalsIgnoreCase("")){
//                    displayAlert(resourceBundle.getString("ROLEIDWARNING"));
//                }
            } else{
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);


                singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("ROLE ID",CommonUtil.convertObjToStr(cboRoleId.getSelectedItem()));
                singleAuthorizeMap.put("GROUP ID",CommonUtil.convertObjToStr(txtGroupID.getText()));
                singleAuthorizeMap.put("AUTHORIZEDT", ClientUtil.getCurrentDate());
                
                System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);
                
                ClientUtil.execute("authorizeRoleMasterMasterData", singleAuthorizeMap);
                viewType = -1;
                btnSave.setEnabled(true);
                btnCancelActionPerformed(null);
            }
        }
    }
    private void btnLevelIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLevelIDActionPerformed
        // Add your handling code here:
        updateOBFields();
        callView(LEVEL_MASTER);
        txtLevelID.setEnabled(false);
        txtLevelName.setEnabled(false);
        txtLevelDesc.setEnabled(false);
        ClientUtil.enableDisable(panLevelDetails, false);
    }//GEN-LAST:event_btnLevelIDActionPerformed
    
    private void btnDeleteLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLevelActionPerformed
        // Add your handling code here:
        panLevelEnableDisable(false);
        btnNewLevel.setEnabled(true);
        observable.editDelRoleData(tblRoleDetails.getSelectedRow());
        populateRoleTable();
        
    }//GEN-LAST:event_btnDeleteLevelActionPerformed
    private void populateRoleTable() {
        tblRoleDetails.setModel(observable.getTmlRoles());
        tblRoleDetails.revalidate();
    }
    private void btnSaveLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveLevelActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = checkMandatory(panGroup);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if(chkAccAllBran.isSelected()){
            if(txtLevelIDForeign.getText().length()==0){
                message.append(objMandatoryRB.getString("txtLevelIDForeign"));
            }
        }
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }
        else{
            ArrayList irRow = new ArrayList();
            irRow.add(CommonUtil.convertObjToStr(cboRoleId.getSelectedItem()));
            irRow.add(CommonUtil.convertObjToStr(txtRoleDesc.getText()));
            irRow.add(CommonUtil.convertObjToStr(observable.getCbmRoleHierarchy().getKeyForSelected()));
            irRow.add(CommonUtil.convertObjToStr(txtLevelID.getText()));
            irRow.add(CommonUtil.convertObjToStr(txtLevelIDForeign.getText()));
            /*if(chkAccAllBran.isSelected()==true){
                irRow.add(observable.YES);
            } else {
                irRow.add(observable.NO);
            }*/
            if(chkHierarchyAllowed.isSelected()){
                irRow.add(observable.YES);
            }else{
                irRow.add(observable.NO);
            }
            if (observable.chkDupRoleDat(irRow) < 0)
            {
                ClientUtil.displayAlert(resourceBundle.getString("RECORDEXIST"));
            return;
            }
            System.out.println("_intRateNew::::"+_intRateNew);
            if (!_intRateNew)  {
                irRow.add(tblRoleDetails.getValueAt(tblRoleDetails.getSelectedRow(),6));
              observable.deleteRoleData(tblRoleDetails.getSelectedRow());
              
            }
            else
            {
                irRow.add("");
                observable.setselRowDat();
            }
            if (observable.insertRoleData(irRow) < 0)
                ClientUtil.displayAlert(resourceBundle.getString("RECORDEXIST"));
//            observable.insertRoleData(observable.selectedRow);
            populateRoleTable();
            panLevelEnableDisable(false);
            btnNewLevel.setEnabled(true);
            setButtonEnableDisable();
            btnSave.setEnabled(true);
        }
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnSaveLevelActionPerformed
    
    private void btnNewLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewLevelActionPerformed
        // Add your handling code here:
        panLevelEnableDisable(true);
        setButtonEnableDisable();
        btnDeleteLevel.setEnabled(false);
        btnLevelIDForeign.setEnabled(false);
        _intRateNew = true;
        setModified(true);
    }//GEN-LAST:event_btnNewLevelActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
   private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }

    
    private void updationRole() {
        HashMap map=new HashMap();
        HashMap whereMap = new HashMap();
        try{
            System.out.println("Row Idex "+ tblRoleDetails.getSelectedRow());
            ArrayList arrRow = observable.populateRoles(tblRoleDetails.getSelectedRow());
            //        txtRoleID.setText(CommonUtil.convertObjToStr(arrRow.get(0)));
            //        txtRoleDesc.setText(CommonUtil.convertObjToStr(arrRow.get(1)));
            //        txtLevelID.setText(CommonUtil.convertObjToStr(arrRow.get(2)));
            //
            //        txtLevelIDForeign.setText(CommonUtil.convertObjToStr(arrRow.get(3)));
            
            observable.setCboRoleID(CommonUtil.convertObjToStr(arrRow.get(0)));
            observable.setTxtRoleDesc(CommonUtil.convertObjToStr(arrRow.get(1)));
            observable.setCboRoleHierarchy(CommonUtil.convertObjToStr(observable.getCbmRoleHierarchy().getDataForKey(String.valueOf(CommonUtil.convertObjToInt(arrRow.get(2))))));
            observable.setTxtLevelID(CommonUtil.convertObjToStr(arrRow.get(3)));
            observable.setTxtLevelIDForeign(CommonUtil.convertObjToStr(arrRow.get(4)));
            
            /*if((CommonUtil.convertObjToStr(arrRow.get(5))).equals(observable.YES)){
                observable.setChkAccAllBran(true);
                whereMap.put("LEVEL_ID", CommonUtil.convertObjToStr(arrRow.get(4)));
                System.out.println("Foreign Map in UI...");
                map.put(CommonConstants.MAP_WHERE, whereMap);
                observable.getLevelForeign(map);
                
            } else {
                observable.setChkAccAllBran(false);
                btnLevelIDForeign.setEnabled(false);
                observable.clearForeignLevelDetails();
            }*/
            
            if(CommonUtil.convertObjToStr(arrRow.get(5)).equals(observable.YES)){
                observable.setChkHierarchyAllowed(true);
            }else{
                observable.setChkHierarchyAllowed(false);
            }
            
            whereMap.put("LEVEL_ID", CommonUtil.convertObjToStr(arrRow.get(3)));
            
            
            map.put(CommonConstants.MAP_WHERE, whereMap);
            System.out.println("Map in UI: " + map);
            
            observable.getLevelHome(map);
            //        if(CommonUtil.convertObjToStr(txtLevelIDForeign.getText()).length() > 0){
            //            observable.setViewType(LEVEL_FOEIGN);
            //
            //        }else{
            //            observable.setViewType(LEVEL_MASTER);
            //        }
            
            //        observable.populateData(map);
            whereMap = null;
            map = null;
            observable.ttNotifyObservers();
            _intRateNew = false;
            
            
        }catch(Exception E){
            System.out.println("Error in updationRole()");
            E.printStackTrace();
        }
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        super.removeEditLock(txtGroupID.getText());
        observable.resetForm();
        commandSetUp(ClientConstants.ACTIONTYPE_CANCEL);
        panLevelEnableDisable(false);
        
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        //__ Reset to the Original Screen
        tabLevelDetails.addTab(resourceBundle.getString("panLevelDetailsForeign"), panLevelForeign);
        
        viewType = -1;
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        /*final String mandatoryMessage = checkMandatory(panGroup);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{*/
        updateOBFields();
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            super.removeEditLock(txtGroupID.getText());
            observable.resetForm();
            observable.setResultStatus();
            setButtonEnableDisable();
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            panLevelEnableDisable(false);
            //__ Reset to the Original Screen
            tabLevelDetails.addTab(resourceBundle.getString("panLevelDetailsForeign"), panLevelForeign);
        }
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        commandSetUp(ClientConstants.ACTIONTYPE_DELETE);
        panLevelEnableDisable(false);
        callView(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void enableDisableRoleButtons(boolean enable){
        btnNewLevel.setEnabled(enable);
        btnSaveLevel.setEnabled(enable);
        btnDeleteLevel.setEnabled(enable);
        
        btnLevelID.setEnabled(enable);
        btnLevelIDForeign.setEnabled(enable);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setViewType(2);
        viewType=2;
        commandSetUp(ClientConstants.ACTIONTYPE_EDIT);
        panLevelEnableDisable(false);
        btnNewLevel.setEnabled(true);
        callView(ClientConstants.ACTIONTYPE_EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        
    }//GEN-LAST:event_btnEditActionPerformed
    private void panLevelEnableDisable(boolean enable){
        ClientUtil.clearAll(panRoleDetails);
        ClientUtil.clearAll(panLevel);
        ClientUtil.enableDisable(panRoleDetails,enable);
        ClientUtil.enableDisable(panLevel,enable);
        enableDisableRoleButtons(enable);
    }
    private void commandSetUp(int actionType){
        //        setButtonEnableDisable();
        observable.setActionType(actionType);
        observable.setStatus();
    }
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void callView(int currField) {
        System.out.println("currField::::::"+currField);
        HashMap viewMap = new HashMap();
        if( currField == ClientConstants.ACTIONTYPE_EDIT ||
        currField == ClientConstants.ACTIONTYPE_DELETE ||
        currField == ClientConstants.ACTIONTYPE_VIEW){
            ArrayList lst = new ArrayList();
            lst.add("GROUP ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectGroupMasterTO");
        }
        else{
            viewMap.put(CommonConstants.MAP_NAME, "selectLevelMasterTO");
        }
        System.out.println("currFieldcurrField:::::"+currField);
        observable.setViewType(currField);
        new ViewAll(this, viewMap).show();
    }
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        System.out.println("Hash "+ hash);
        HashMap whereMap = new HashMap();
        int action = observable.getViewType();
        System.out.println("action::::"+action);
        if (action == ClientConstants.ACTIONTYPE_EDIT ||
        action == ClientConstants.ACTIONTYPE_DELETE ||
        viewType == AUTHORIZE ||
        action == ClientConstants.ACTIONTYPE_VIEW ) {
            whereMap.put("GROUP ID", CommonUtil.convertObjToStr(hash.get("GROUP ID")));
            whereMap.put("AUTH", "");
            setButtonEnableDisable();
             System.out.println("From444 OB"+observable.getViewType());
        }else{
            whereMap.put("LEVEL_ID", CommonUtil.convertObjToStr(hash.get("LEVEL ID")));
        }
         System.out.println("viewTypemmsdasm"+viewType);
         System.out.println("From OB"+observable.getViewType());
        if(viewType == AUTHORIZE){
            System.out.println("Chumma Keralle");
            whereMap.put("ROLE_ID",CommonUtil.convertObjToStr(hash.get("ROLE ID")));
            observable.setViewType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        
        hash.put(CommonConstants.MAP_WHERE, whereMap);
        System.out.println("Hash in ui: " + hash);
         System.out.println("Fro111 OB"+observable.getViewType());
        System.out.println("viewTypemmm"+viewType);
        observable.populateData(hash);
         System.out.println("From 222OB"+observable.getViewType());
        
        //        setButtonEnableDisable();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDeleteLevel.setName("btnDeleteLevel");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnLevelID.setName("btnLevelID");
        btnLevelIDForeign.setName("btnLevelIDForeign");
        btnNewLevel.setName("btnNewLevel");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSaveLevel.setName("btnSaveLevel");
        chkAccAllBran.setName("chkAccAllBran");
        lblAccAllBran.setName("lblAccAllBran");
        lblCashCredit.setName("lblCashCredit");
        lblCashCreditForeign.setName("lblCashCreditForeign");
        lblCashDebit.setName("lblCashDebit");
        lblCashDebitForeign.setName("lblCashDebitForeign");
        lblClearingCredit.setName("lblClearingCredit");
        lblClearingCreditForeign.setName("lblClearingCreditForeign");
        lblClearingDebit.setName("lblClearingDebit");
        lblClearingDebitForeign.setName("lblClearingDebitForeign");
        lblGroupDesc.setName("lblGroupDesc");
        lblGroupID.setName("lblGroupID");
        lblLevelDesc.setName("lblLevelDesc");
        lblLevelDescForeign.setName("lblLevelDescForeign");
        lblLevelID.setName("lblLevelID");
        lblLevelIDForeign.setName("lblLevelIDForeign");
        lblLevelName.setName("lblLevelName");
        lblLevelNameForeign.setName("lblLevelNameForeign");
        lblMsg.setName("lblMsg");
        lblRoleDesc.setName("lblRoleDesc");
        lblRoleID.setName("lblRoleID");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblTransCredit.setName("lblTransCredit");
        lblTransCreditForeign.setName("lblTransCreditForeign");
        lblTransDebit.setName("lblTransDebit");
        lblTransDebitForeign.setName("lblTransDebitForeign");
        mbrMain.setName("mbrMain");
        panGroup.setName("panGroup");
        panLevelButtons.setName("panLevelButtons");
        panLevelDetails.setName("panLevelDetails");
        panLevelDetailsForeign.setName("panLevelDetailsForeign");
        panLevelForeign.setName("panLevelForeign");
        panLevelHome.setName("panLevelHome");
        panLevelID.setName("panLevelID");
        panLevelIDForeign.setName("panLevelIDForeign");
        panLevelRole.setName("panLevelRole");
        panRoleLevelDetails.setName("panRoleLevelDetails");
        panStatus.setName("panStatus");
        sptGroup.setName("sptGroup");
        sptRole.setName("sptRole");
        srpRole.setName("srpRole");
        tabLevelDetails.setName("tabLevelDetails");
        tblRoleDetails.setName("tblRoleDetails");
        txtCashCredit.setName("txtCashCredit");
        txtCashCreditForeign.setName("txtCashCreditForeign");
        txtCashDebit.setName("txtCashDebit");
        txtCashDebitForeign.setName("txtCashDebitForeign");
        txtClearingCredit.setName("txtClearingCredit");
        txtClearingCreditForeign.setName("txtClearingCreditForeign");
        txtClearingDebit.setName("txtClearingDebit");
        txtClearingDebitForeign.setName("txtClearingDebitForeign");
        txtGroupDesc.setName("txtGroupDesc");
        txtGroupID.setName("txtGroupID");
        txtLevelDesc.setName("txtLevelDesc");
        txtLevelDescForeign.setName("txtLevelDescForeign");
        txtLevelID.setName("txtLevelID");
        txtLevelIDForeign.setName("txtLevelIDForeign");
        txtLevelName.setName("txtLevelName");
        txtLevelNameForeign.setName("txtLevelNameForeign");
        txtRoleDesc.setName("txtRoleDesc");
        txtTransCredit.setName("txtTransCredit");
        txtTransCreditForeign.setName("txtTransCreditForeign");
        txtTransDebit.setName("txtTransDebit");
        txtTransDebitForeign.setName("txtTransDebitForeign");
        cboRoleHierarchy.setName("cboRoleHierarchy");
        lblHierarchyAllowed.setName("lblHierarchyAllowed");
        chkHierarchyAllowed.setName("chkHierarchyAllowed");
        cboRoleId.setName("cboRoleId");
    }
    
    
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblClearingDebit.setText(resourceBundle.getString("lblClearingDebit"));
        ((javax.swing.border.TitledBorder)panLevelDetailsForeign.getBorder()).setTitle(resourceBundle.getString("panLevelDetailsForeign"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblRoleID.setText(resourceBundle.getString("lblRoleID"));
        lblRoleDesc.setText(resourceBundle.getString("lblRoleDesc"));
        lblTransDebitForeign.setText(resourceBundle.getString("lblTransDebitForeign"));
        btnSaveLevel.setText(resourceBundle.getString("btnSaveLevel"));
        lblGroupID.setText(resourceBundle.getString("lblGroupID"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblCashDebit.setText(resourceBundle.getString("lblCashDebit"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblLevelNameForeign.setText(resourceBundle.getString("lblLevelNameForeign"));
        lblTransCreditForeign.setText(resourceBundle.getString("lblTransCreditForeign"));
        lblTransCredit.setText(resourceBundle.getString("lblTransCredit"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnLevelIDForeign.setText(resourceBundle.getString("btnLevelIDForeign"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        chkAccAllBran.setText(resourceBundle.getString("chkAccAllBran"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblGroupDesc.setText(resourceBundle.getString("lblGroupDesc"));
        btnDeleteLevel.setText(resourceBundle.getString("btnDeleteLevel"));
        lblLevelIDForeign.setText(resourceBundle.getString("lblLevelIDForeign"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblLevelDesc.setText(resourceBundle.getString("lblLevelDesc"));
        btnLevelID.setText(resourceBundle.getString("btnLevelID"));
        lblTransDebit.setText(resourceBundle.getString("lblTransDebit"));
        lblClearingDebitForeign.setText(resourceBundle.getString("lblClearingDebitForeign"));
        lblLevelName.setText(resourceBundle.getString("lblLevelName"));
        btnNewLevel.setText(resourceBundle.getString("btnNewLevel"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblClearingCredit.setText(resourceBundle.getString("lblClearingCredit"));
        lblClearingCreditForeign.setText(resourceBundle.getString("lblClearingCreditForeign"));
        ((javax.swing.border.TitledBorder)panLevelDetails.getBorder()).setTitle(resourceBundle.getString("panLevelDetails"));
        lblCashCredit.setText(resourceBundle.getString("lblCashCredit"));
        lblCashCreditForeign.setText(resourceBundle.getString("lblCashCreditForeign"));
        lblCashDebitForeign.setText(resourceBundle.getString("lblCashDebitForeign"));
        lblLevelID.setText(resourceBundle.getString("lblLevelID"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAccAllBran.setText(resourceBundle.getString("lblAccAllBran"));
        lblLevelDescForeign.setText(resourceBundle.getString("lblLevelDescForeign"));
        lblRoleHierarchy.setText(resourceBundle.getString("lblRoleHierarchy"));
        lblHierarchyAllowed.setText(resourceBundle.getString("lblHierarchyAllowed"));
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupID", new Boolean(true));
        mandatoryMap.put("txtGroupDesc", new Boolean(true));
        mandatoryMap.put("cboRoleId", new Boolean(true));
        mandatoryMap.put("txtRoleDesc", new Boolean(true));
        mandatoryMap.put("chkAccAllBran", new Boolean(true));
        mandatoryMap.put("txtLevelID", new Boolean(true));
        mandatoryMap.put("txtLevelName", new Boolean(true));
        mandatoryMap.put("txtLevelDesc", new Boolean(true));
        mandatoryMap.put("txtCashDebit", new Boolean(true));
        mandatoryMap.put("txtCashCredit", new Boolean(true));
        mandatoryMap.put("txtClearingCredit", new Boolean(true));
        mandatoryMap.put("txtClearingDebit", new Boolean(true));
        mandatoryMap.put("txtTransCredit", new Boolean(true));
        mandatoryMap.put("txtTransDebit", new Boolean(true));
        mandatoryMap.put("txtLevelNameForeign", new Boolean(true));
        mandatoryMap.put("txtLevelDescForeign", new Boolean(true));
        mandatoryMap.put("txtCashDebitForeign", new Boolean(true));
        mandatoryMap.put("txtCashCreditForeign", new Boolean(true));
        mandatoryMap.put("txtClearingCreditForeign", new Boolean(true));
        mandatoryMap.put("txtClearingDebitForeign", new Boolean(true));
        mandatoryMap.put("txtTransCreditForeign", new Boolean(true));
        mandatoryMap.put("txtTransDebitForeign", new Boolean(true));
        mandatoryMap.put("txtLevelIDForeign", new Boolean(true));
        mandatoryMap.put("cboRoleHierarchy", new Boolean(true));
        mandatoryMap.put("chkHierarchyAllowed", new Boolean(false));
    }
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteLevel;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLevelID;
    private com.see.truetransact.uicomponent.CButton btnLevelIDForeign;
    private com.see.truetransact.uicomponent.CButton btnNewLevel;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveLevel;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboRoleHierarchy;
    private com.see.truetransact.uicomponent.CComboBox cboRoleId;
    private com.see.truetransact.uicomponent.CCheckBox chkAccAllBran;
    private com.see.truetransact.uicomponent.CCheckBox chkHierarchyAllowed;
    private com.see.truetransact.uicomponent.CLabel lblAccAllBran;
    private com.see.truetransact.uicomponent.CLabel lblCashCredit;
    private com.see.truetransact.uicomponent.CLabel lblCashCreditForeign;
    private com.see.truetransact.uicomponent.CLabel lblCashDebit;
    private com.see.truetransact.uicomponent.CLabel lblCashDebitForeign;
    private com.see.truetransact.uicomponent.CLabel lblClearingCredit;
    private com.see.truetransact.uicomponent.CLabel lblClearingCreditForeign;
    private com.see.truetransact.uicomponent.CLabel lblClearingDebit;
    private com.see.truetransact.uicomponent.CLabel lblClearingDebitForeign;
    private com.see.truetransact.uicomponent.CLabel lblGroupDesc;
    private com.see.truetransact.uicomponent.CLabel lblGroupID;
    private com.see.truetransact.uicomponent.CLabel lblHierarchyAllowed;
    private com.see.truetransact.uicomponent.CLabel lblLevelDesc;
    private com.see.truetransact.uicomponent.CLabel lblLevelDescForeign;
    private com.see.truetransact.uicomponent.CLabel lblLevelID;
    private com.see.truetransact.uicomponent.CLabel lblLevelIDForeign;
    private com.see.truetransact.uicomponent.CLabel lblLevelName;
    private com.see.truetransact.uicomponent.CLabel lblLevelNameForeign;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRoleDesc;
    private com.see.truetransact.uicomponent.CLabel lblRoleHierarchy;
    private com.see.truetransact.uicomponent.CLabel lblRoleID;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransCredit;
    private com.see.truetransact.uicomponent.CLabel lblTransCreditForeign;
    private com.see.truetransact.uicomponent.CLabel lblTransDebit;
    private com.see.truetransact.uicomponent.CLabel lblTransDebitForeign;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panChks;
    private com.see.truetransact.uicomponent.CPanel panGroup;
    private com.see.truetransact.uicomponent.CPanel panLevel;
    private com.see.truetransact.uicomponent.CPanel panLevelButtons;
    private com.see.truetransact.uicomponent.CPanel panLevelDetails;
    private com.see.truetransact.uicomponent.CPanel panLevelDetailsForeign;
    private com.see.truetransact.uicomponent.CPanel panLevelForeign;
    private com.see.truetransact.uicomponent.CPanel panLevelHome;
    private com.see.truetransact.uicomponent.CPanel panLevelID;
    private com.see.truetransact.uicomponent.CPanel panLevelIDForeign;
    private com.see.truetransact.uicomponent.CPanel panLevelRole;
    private com.see.truetransact.uicomponent.CPanel panRoleDetails;
    private com.see.truetransact.uicomponent.CPanel panRoleLevelDetails;
    private com.see.truetransact.uicomponent.CPanel panRoleTbl;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptGroup;
    private com.see.truetransact.uicomponent.CSeparator sptRole;
    private com.see.truetransact.uicomponent.CScrollPane srpRole;
    private com.see.truetransact.uicomponent.CTabbedPane tabLevelDetails;
    private com.see.truetransact.uicomponent.CTable tblRoleDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextField txtCashCredit;
    private com.see.truetransact.uicomponent.CTextField txtCashCreditForeign;
    private com.see.truetransact.uicomponent.CTextField txtCashDebit;
    private com.see.truetransact.uicomponent.CTextField txtCashDebitForeign;
    private com.see.truetransact.uicomponent.CTextField txtClearingCredit;
    private com.see.truetransact.uicomponent.CTextField txtClearingCreditForeign;
    private com.see.truetransact.uicomponent.CTextField txtClearingDebit;
    private com.see.truetransact.uicomponent.CTextField txtClearingDebitForeign;
    private com.see.truetransact.uicomponent.CTextField txtGroupDesc;
    private com.see.truetransact.uicomponent.CTextField txtGroupID;
    private com.see.truetransact.uicomponent.CTextField txtLevelDesc;
    private com.see.truetransact.uicomponent.CTextField txtLevelDescForeign;
    private com.see.truetransact.uicomponent.CTextField txtLevelID;
    private com.see.truetransact.uicomponent.CTextField txtLevelIDForeign;
    private com.see.truetransact.uicomponent.CTextField txtLevelName;
    private com.see.truetransact.uicomponent.CTextField txtLevelNameForeign;
    private com.see.truetransact.uicomponent.CTextField txtRoleDesc;
    private com.see.truetransact.uicomponent.CTextField txtTransCredit;
    private com.see.truetransact.uicomponent.CTextField txtTransCreditForeign;
    private com.see.truetransact.uicomponent.CTextField txtTransDebit;
    private com.see.truetransact.uicomponent.CTextField txtTransDebitForeign;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] arg){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        JFrame jf = new JFrame();
        RoleUI gui = new RoleUI();
        jf.getContentPane().add(gui);
        jf.setSize(650, 560);
        jf.show();
        gui.show();
    }
}